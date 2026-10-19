import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {execFileSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const serial=process.env.KIMI_ADB_SERIAL || 'emulator-5596';
const expectedAvd=process.env.KIMI_AVD_NAME || 'rea_kimi_ui_20261015';
const adb=process.env.KIMI_ADB || 'adb';
const runId=process.env.KIMI_RUN_ID||'';
const networkCycle=process.env.KIMI_NETWORK_CYCLE||'';
if(networkCycle&&!/^[a-z0-9-]+$/.test(networkCycle))throw Error('Invalid network cycle');
const networkSuffix=networkCycle?'-'+networkCycle:'';
if(runId&&!/^[a-z0-9-]+$/.test(runId))throw Error('Invalid KIMI_RUN_ID');
const runtimeRelative='evidence/runtime'+(runId?'/'+runId:'');
const runtime=path.join(root,runtimeRelative); fs.mkdirSync(runtime,{recursive:true});
const lockPath=path.join(runtime,'.operation.lock');
let lock;
try{lock=fs.openSync(lockPath,'wx');fs.writeFileSync(lock,String(process.pid));}
catch(e){throw Error('Another Kimi operation is active for this run; wait for completion before acting or capturing. '+e.message);}
process.on('exit',()=>{try{fs.closeSync(lock);fs.unlinkSync(lockPath);}catch{}});
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const decodeXml=value=>value.replace(/&(#x[0-9a-f]+|#\d+|amp|quot|apos|lt|gt);/gi,(_,x)=>x[0]==='#'?String.fromCodePoint(x[1].toLowerCase()==='x'?parseInt(x.slice(2),16):parseInt(x.slice(1),10)):({amp:'&',quot:'"',apos:"'",lt:'<',gt:'>'}[x.toLowerCase()]));
function run(args,binary=false){return execFileSync(adb,['-s',serial,...args],{encoding:binary?undefined:'utf8',timeout:30000,maxBuffer:12*1024*1024,windowsHide:true});}
function freshUi(deviceFile){
 let last='';
 for(let attempt=0;attempt<3;attempt++){
  run(['shell','rm','-f',deviceFile]);
  try {
   const out=run(['shell','uiautomator','dump',deviceFile]);
   if(!out.includes(deviceFile))throw Error('No newly written hierarchy');
   const xml=run(['exec-out','cat',deviceFile]);
   if(!xml.includes('<hierarchy'))throw Error('No hierarchy root');
   return xml;
  } catch(e){last=String(e.message);if(attempt<2)Atomics.wait(new Int32Array(new SharedArrayBuffer(4)),0,0,1000);}
 }
 throw Error('No fresh hierarchy after bounded read-only retries: '+last);
}
let avd;
for(let attempt=0;attempt<3&&!avd;attempt++){
 avd=run(['emu','avd','name']).split(/[\r\n]+/).map(x=>x.trim()).find(Boolean);
 if(!avd)avd=run(['shell','getprop','ro.boot.qemu.avd_name']).trim();
}
if(avd!==expectedAvd)throw Error(`Refusing target ${serial}: AVD ${avd} differs from ${expectedAvd}`);
const indexPath=path.join(runtime,'session.json');
const session=fs.existsSync(indexPath)?JSON.parse(fs.readFileSync(indexPath,'utf8')):{schemaVersion:1,serial,avd,apkSha256:'9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332',package:'com.moonshot.kimichat',accountMode:'no-login',backend:'original app network; no fixtures',actions:[],scenes:[]};
const [command,...args]=process.argv.slice(2);
const startedAt=new Date().toISOString();
function save(){fs.writeFileSync(indexPath,JSON.stringify(session,null,2)+'\n');}
function action(operation,adbArgs,description){const stdout=run(adbArgs);session.actions.push({operation,arguments:adbArgs,description,startedAt,completedAt:new Date().toISOString(),stdout});save();console.log(stdout);}
if(command==='verify-apk'){
 if(args.length>1||(args.length===1&&args[0]!=='final'))throw Error('verify-apk accepts only the optional final suffix');
 const finalProof=args[0]==='final',proofName=finalProof?'artifact-identity-final.json':'artifact-identity.json';
 const proofPath=path.join(runtime,proofName),proofField=finalProof?'identityVerificationFinal':'identityVerification';
 if(fs.existsSync(proofPath)||session[proofField])throw Error('APK identity proof already exists; preserve '+proofName+' and its session record');
 const originalPath=path.resolve(root,'../../Kimi.apk');const bytes=fs.readFileSync(originalPath);const originalSha256=hash(bytes);
 const installedPaths=run(['shell','pm','path',session.package]).split(/[\r\n]+/).filter(x=>x.startsWith('package:')).map(x=>x.slice(8).trim());
 if(installedPaths.length!==1||!/^\/data\/app\/[A-Za-z0-9/._~+=-]+\/base\.apk$/.test(installedPaths[0])||!installedPaths[0].includes('/'+session.package+'-'))throw Error('Unexpected source APK installation path; no identity inference');
 const installedSha256=run(['shell','sha256sum',installedPaths[0]]).trim().split(/\s/)[0];
 const pkg=run(['shell','dumpsys','package',session.package]);
 const versionName=pkg.match(/versionName=([^\r\n]+)/)?.[1].trim();
 const versionCode=Number(pkg.match(/versionCode=(\d+)/)?.[1]);
 if(originalSha256!==session.apkSha256||installedSha256!==originalSha256||bytes.length!==41038830||versionName!=='3.1.3'||versionCode!==376)throw Error('Original/installed APK identity mismatch');
 const proof={verifiedAt:new Date().toISOString(),serial,avd,package:session.package,versionName,versionCode,originalFile:'Kimi.apk',bytes:bytes.length,originalSha256,installedPath:installedPaths[0],installedSha256,matches:true,method:'Read-only host SHA256 plus adb pm path, device sha256sum and package version; not REA Evidence'};
 fs.writeFileSync(proofPath,JSON.stringify(proof,null,2)+'\n',{flag:'wx'});session[proofField]=proof;save();console.log(JSON.stringify({proofFile:runtimeRelative+'/'+proofName,...proof}));
}else if(command==='status'){
 console.log(JSON.stringify({serial,avd,boot:run(['shell','getprop','sys.boot_completed']).trim(),size:run(['shell','wm','size']).trim(),density:run(['shell','wm','density']).trim(),locale:run(['shell','settings','get','system','system_locales']).trim(),fontScale:run(['shell','settings','get','system','font_scale']).trim()},null,2));
}else if(command==='launch')action('launch',['shell','am','start','-n','com.moonshot.kimichat/.MainActivity'],'Launch original Kimi MainActivity');
else if(command==='stop')action('stop',['shell','am','force-stop','com.moonshot.kimichat'],args.join(' ')||'Stop only original Kimi in dedicated AVD');
else if(command==='exit-emulator')action('exit-emulator',['emu','kill'],'Close only the verified dedicated Kimi AVD; do not stop shared adb server');
else if(command==='tap'){
 if(!/^\d+$/.test(args[0])||!/^\d+$/.test(args[1]))throw Error('tap needs integer coordinates');
 action('tap',['shell','input','tap',args[0],args[1]],args.slice(2).join(' '));
}else if(command==='open-model-generating'){
 const ui=freshUi('/sdcard/kimi-generating-model.xml');
 const nodes=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]])));
 if(!nodes.some(n=>n.text==='Send messages anytime…')||!nodes.some(n=>decodeXml(n.text||'').startsWith('Research Run03')))throw Error('Own generating request not confirmed; no model tap');
 const models=nodes.filter(n=>['Instant','K3','K2.8'].includes(n.text)&&n.class==='android.widget.TextView');
 if(models.length!==1)throw Error('Unique current model required');const b=models[0].bounds.match(/\d+/g).map(Number);
 session.actions.push({operation:'observed-generation-before-model',placeholder:'Send messages anytime…',model:models[0].text,bounds:models[0].bounds,hierarchySha256:hash(Buffer.from(ui)),startedAt,completedAt:new Date().toISOString()});save();
 action('open-model-generating',['shell','input','tap',String(Math.floor((b[0]+b[2])/2)),String(Math.floor((b[1]+b[3])/2))],'Fresh generating placeholder and own research message verified immediately before model tap');
}else if(command==='tap-label'||command==='tap-last-label'||command==='tap-text-label'||command==='focus-edit'){
 const label=args[0];
 let matches=[],semanticMatchCount=0;
 for(let read=0;read<3;read++){
  const ui=freshUi('/sdcard/kimi-style-label.xml');
  matches=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]]))).filter(a=>a.enabled!=='false'&&(command==='focus-edit'?(a.class==='android.widget.EditText'&&a.password!=='true'):((a.text===label||a['content-desc']===label)&&(command!=='tap-text-label'||a.class==='android.widget.TextView'))));
  semanticMatchCount=matches.length;
  matches=[...new Map(matches.map(n=>[n.bounds+'|'+n.class+'|'+n.text+'|'+n['content-desc'],n])).values()];
  if(matches.length||read===2)break;
  Atomics.wait(new Int32Array(new SharedArrayBuffer(4)),0,0,1000);
 }
 if(command==='tap-last-label'&&matches.length)matches=[matches.sort((a,b)=>Number(b.bounds.match(/\d+/g)[1])-Number(a.bounds.match(/\d+/g)[1]))[0]];
 if(matches.length!==1)throw Error(`Expected one visible label ${label}; found ${matches.length}. No tap performed.`);
 const b=matches[0].bounds.match(/\d+/g).map(Number);
 action('tap-label',['shell','input','tap',String(Math.floor((b[0]+b[2])/2)),String(Math.floor((b[1]+b[3])/2))],`Observed label ${label}; ${args.slice(1).join(' ')}${semanticMatchCount>matches.length?' ; identical semantic nodes at identical bounds consolidated':''}`);
}else if(command==='back')action('back',['shell','input','keyevent','4'],args.join(' '));
else if(command==='text'){
 const value=args[0];if(!value||!/^[\x20-\x7e]+$/.test(value)||/[&;<>|`$\\]/.test(value))throw Error('Research text must be plain ASCII; no shell metacharacters');
 action('type-text',['shell','input','text',value.replaceAll(' ','%s')],args.slice(1).join(' ')||'Type local research draft; no submission');
}else if(command==='text-chunks'){
 const value=args[0];if(!value||!/^[\x20-\x7e]+$/.test(value)||/[&;<>|`$\\]/.test(value))throw Error('Plain ASCII research draft only');
 const chunks=value.match(/.{1,12}/g)||[];
 for(const chunk of chunks){run(['shell','input','text',chunk.replaceAll(' ','%s')]);Atomics.wait(new Int32Array(new SharedArrayBuffer(4)),0,0,350);}
 session.actions.push({operation:'type-text-chunks',text:value,chunkSize:12,intervalMs:350,description:args.slice(1).join(' ')||'Chunked research input; verify actual EditText before use',startedAt,completedAt:new Date().toISOString()});save();
}else if(command==='text-slow'){
 const value=args[0];if(!value||!/^[\x20-\x7e]+$/.test(value)||/[&;<>|`$\\]/.test(value))throw Error('Plain ASCII research draft only');
 for(const character of value){run(['shell','input','text',character===' '?'%s':character]);Atomics.wait(new Int32Array(new SharedArrayBuffer(4)),0,0,150);}
 session.actions.push({operation:'type-text-paced',text:value,intervalMs:150,description:args.slice(1).join(' ')||'Paced research input; check UI before submission',startedAt,completedAt:new Date().toISOString()});save();
}else if(command==='rtl-fixture-trim'){
 throw Error('Disabled: Run03 Arabic-first transport-prefix removal did not pass its exact result guard. Retained mixed Latin/Arabic evidence is not an Arabic-first input result. No input mutation performed.');
}else if(command==='shift-enter'){
 const ui=freshUi('/sdcard/kimi-shift-enter.xml');
 const edits=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]]))).filter(n=>n.class==='android.widget.EditText'&&n.focused==='true'&&n.password!=='true');
 if(edits.length!==1||!decodeXml(edits[0].text).startsWith('Research Run03'))throw Error('Shift+Enter requires focused own fixture');
 action('shift-enter',['shell','input','keycombination','59','66'],'Hardware Shift+Enter in known synthetic draft; result checked separately');
}else if(command==='key'){
 if(!/^[A-Z0-9_]+$/.test(args[0]))throw Error('Invalid key event');
 action('key',['shell','input','keyevent',args[0]],args.slice(1).join(' '));
}else if(command==='network-off'){
 const baselinePath=path.join(runtime,'network-baseline'+networkSuffix+'.json');
 if(fs.existsSync(baselinePath))throw Error('Network baseline already exists; restore before another offline experiment');
 fs.writeFileSync(baselinePath,JSON.stringify({serial,avd,recordedAt:new Date().toISOString(),wifi:run(['shell','settings','get','global','wifi_on']).trim(),data:run(['shell','settings','get','global','mobile_data']).trim()},null,2));
 action('disable-test-avd-wifi',['shell','svc','wifi','disable'],'Only dedicated Kimi AVD; original state recorded for restoration');
 action('disable-test-avd-mobile-data',['shell','svc','data','disable'],'Only dedicated Kimi AVD; do not change host or other emulator networking');
 let state;
 for(let attempt=0;attempt<10;attempt++){
  const connectivity=run(['shell','dumpsys','connectivity']);
  state={checkedAt:new Date().toISOString(),wifi:run(['shell','settings','get','global','wifi_on']).trim(),data:run(['shell','settings','get','global','mobile_data']).trim(),defaultNetworkLine:connectivity.split(/[\r\n]+/).find(x=>/Active default network:/.test(x))||null};
  if(state.wifi==='0'&&state.data==='0'&&/Active default network: none/.test(state.defaultNetworkLine||''))break;
  Atomics.wait(new Int32Array(new SharedArrayBuffer(4)),0,0,1000);
 }
 state.effectiveOffline=state.wifi==='0'&&state.data==='0'&&/Active default network: none/.test(state.defaultNetworkLine||'');
 fs.writeFileSync(path.join(runtime,'network-off-confirmed'+networkSuffix+'.json'),JSON.stringify(state,null,2)+'\n');
 console.log(JSON.stringify(state));if(!state.effectiveOffline)throw Error('Effective offline not confirmed; restore before further testing');
}else if(command==='network-restore'){
 const baselinePath=path.join(runtime,'network-baseline'+networkSuffix+'.json');const baseline=JSON.parse(fs.readFileSync(baselinePath));
 if(baseline.serial!==serial||baseline.avd!==avd)throw Error('Wrong network baseline');
 action('restore-test-avd-wifi',['shell','svc','wifi',baseline.wifi==='1'?'enable':'disable'],'Restore dedicated AVD Wi-Fi to recorded baseline');
 action('restore-test-avd-mobile-data',['shell','svc','data',baseline.data==='1'?'enable':'disable'],'Restore dedicated AVD data setting to recorded baseline');
 let connectivity='';
 for(let attempt=0;attempt<10;attempt++){
  connectivity=run(['shell','dumpsys','connectivity']);
  if(/Active default network: \d+/.test(connectivity)&&/VALIDATED/.test(connectivity))break;
  Atomics.wait(new Int32Array(new SharedArrayBuffer(4)),0,0,1000);
 }
 const restored={restoredAt:new Date().toISOString(),baseline,wifi:run(['shell','settings','get','global','wifi_on']).trim(),data:run(['shell','settings','get','global','mobile_data']).trim(),defaultNetworkLine:connectivity.split(/[\r\n]+/).find(x=>/Active default network:/.test(x))||null,activeNetworkVerified:/Active default network: \d+/.test(connectivity)&&/VALIDATED/.test(connectivity)};
 fs.writeFileSync(path.join(runtime,'network-restored'+networkSuffix+'.json'),JSON.stringify(restored,null,2)+'\n');console.log(JSON.stringify(restored));
 if(baseline.wifi==='1'&&!restored.activeNetworkVerified)throw Error('Network switches restored but effective validated network not confirmed');
}else if(command==='cleanup-share-image'){
 const metadata=JSON.parse(fs.readFileSync(path.join(runtime,'local-image-export.json'),'utf8'));
 const rows=metadata.newRows||[];if(rows.length!==1)throw Error('Export identity is not unique');
 const row=rows[0],id=row.match(/_id=(\d+)/)?.[1],devicePath=row.match(/_data=(\/storage\/emulated\/0\/DCIM\/KimiChat\/kimi_share_\d+\.png)/)?.[1];
 if(!id||!devicePath)throw Error('Not a known own generated export');
 const evidence=JSON.parse(fs.readFileSync(path.join(root,'evidence/rea/run03-pull-share-image.json'),'utf8').replace(/^\uFEFF/,''));
 if(evidence.normalized_result.device_path!==devicePath)throw Error('Export identity changed');
 const actual=run(['shell','sha256sum',devicePath]).trim().split(/\s/)[0];
 if(actual!==evidence.normalized_result.sha256)throw Error('Own export digest differs; no deletion');
 const uri='content://media/external/images/media/'+id;
 const current=run(['shell','content','query','--uri',uri,'--projection','_id:_display_name:_data']);
 if(!current.includes(devicePath))throw Error('MediaStore identity differs; no deletion');
 action('cleanup-own-share-image',['shell','content','delete','--uri',uri],'Remove only newly created disposable exported image after exact path and digest verification; private host copy retained outside package');
 fs.writeFileSync(path.join(runtime,'local-image-export-cleanup.json'),JSON.stringify({devicePath,sha256:actual,mediaStoreUri:uri,deletedAt:new Date().toISOString(),after:run(['shell','content','query','--uri',uri,'--projection','_id:_display_name:_data'])},null,2)+'\n');
}else if(command==='cancel-upload'){
 const expected=args[0]||'run03-large';if(!/^run03-(large|cancel-large)$/.test(expected))throw Error('Unknown disposable upload');
 const ui=freshUi('/sdcard/kimi-upload-check.xml');
 const nodes=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]])));
 const progress=nodes.filter(n=>/^Uploading \d+%$/.test(n.text));
 const close=nodes.filter(n=>n['content-desc']==='Off');
 if(!nodes.some(n=>n.text===expected)||progress.length!==1||close.length!==1)throw Error('A single own live larger-file upload is not confirmed; no cancellation tap');
 const proof='upload-check-'+Date.now()+'.xml';fs.writeFileSync(path.join(runtime,proof),ui);
 const b=close[0].bounds.match(/\d+/g).map(Number);
 action('cancel-upload-checked',['shell','input','tap',String(Math.floor((b[0]+b[2])/2)),String(Math.floor((b[1]+b[3])/2))],'Fresh own Uploading progress '+progress[0].text+' and single attachment close; proof '+proof+'; UI removal only, backend cancellation unknown');
}else if(command==='assert-draft'){
 const expected=args[0];if(!expected||!/^Research Run03/.test(expected))throw Error('Only explicit own research fixture text may be asserted');
 const ui=freshUi('/sdcard/kimi-assert-draft.xml');
 const edits=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]]))).filter(n=>n.class==='android.widget.EditText'&&n.password!=='true');
 if(edits.length!==1||decodeXml(edits[0].text)!==expected)throw Error('Own fixture field differs; no further action');
 session.actions.push({operation:'verify-draft-no-submit',text:decodeXml(edits[0].text),rawXmlText:edits[0].text,bounds:edits[0].bounds,startedAt,completedAt:new Date().toISOString()});save();console.log(JSON.stringify({verified:true,characters:expected.length,bounds:edits[0].bounds}));
}else if(command==='submit-exact'||command==='submit-repeat-exact'||command==='submit-busy-exact'){
 const expected=args[0];if(!expected||!(/^(Research|Run03|Reply|Count)/.test(expected)||/^\uE002\s+theme-kit Research Run03/.test(expected)))throw Error('Research submission guard requires explicit test text');
 run(['shell','rm','-f','/sdcard/kimi-submit-check.xml']);
 const out=run(['shell','uiautomator','dump','/sdcard/kimi-submit-check.xml']);
 if(!out.includes('/sdcard/kimi-submit-check.xml'))throw Error('No fresh submit hierarchy');
 const ui=run(['exec-out','cat','/sdcard/kimi-submit-check.xml']);
 const nodes=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]])));
 const edits=nodes.filter(n=>n.class==='android.widget.EditText');
 const buttons=nodes.filter(n=>n['content-desc']==='Send message'&&n.enabled!=='false');
 if(edits.length!==1||decodeXml(edits[0].text)!==expected||buttons.length!==1||nodes.some(n=>n.text==='Send messages anytime…'))throw Error('Draft mismatch or busy; no submission performed');
 if(command==='submit-busy-exact'){
  if(!nodes.some(n=>n.text==='Thinking'))throw Error('Active Thinking not confirmed; no second submission performed');
  session.actions.push({operation:'observed-thinking-before-second-send',activeText:'Thinking',ownDraft:expected,hierarchySha256:hash(Buffer.from(ui)),startedAt,completedAt:new Date().toISOString()});save();
 }
 session.actions.push({operation:'verified-research-draft',text:decodeXml(edits[0].text),rawXmlText:edits[0].text,bounds:edits[0].bounds,focused:edits[0].focused,startedAt,completedAt:new Date().toISOString()});
 const b=buttons[0].bounds.match(/\d+/g).map(Number);
 action('submit-exact',['shell','input','tap',String(Math.floor((b[0]+b[2])/2)),String(Math.floor((b[1]+b[3])/2))],'Fresh exact test draft and single Send action checked; '+args.slice(1).join(' '));
 if(command==='submit-repeat-exact'){
  Atomics.wait(new Int32Array(new SharedArrayBuffer(4)),0,0,80);
  action('repeat-send-coordinate-once',['shell','input','tap',String(Math.floor((b[0]+b[2])/2)),String(Math.floor((b[1]+b[3])/2))],'One bounded repeat tap at original Send position; control may have become Stop; actual result verified separately');
 }
}else if(command==='stop-generation'){
 const ui=freshUi('/sdcard/kimi-stop-check.xml');
 const nodes=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]])));
 const candidates=nodes.filter(n=>n['content-desc']==='Send message'&&n.class==='android.widget.ImageView');
 if(!nodes.some(n=>n.text==='Send messages anytime…')||candidates.length!==1)throw Error('Generating state not confirmed; no stop tap performed');
 const proof=path.join(runtime,'stop-check-'+Date.now()+'.xml');fs.writeFileSync(proof,ui);
 const b=candidates[0].bounds.match(/\d+/g).map(Number);
 action('stop-generation-checked',['shell','input','tap',String(Math.floor((b[0]+b[2])/2)),String(Math.floor((b[1]+b[3])/2))],'Observed live generating placeholder + square-control semantic node; UI cancellation only, network cancellation unknown');
}else if(command==='clear-draft'){
 run(['shell','rm','-f','/sdcard/kimi-draft-check.xml']);
 const dump=run(['shell','uiautomator','dump','/sdcard/kimi-draft-check.xml']);
 if(!dump.includes('/sdcard/kimi-draft-check.xml'))throw Error('No fresh draft hierarchy');
 const ui=run(['exec-out','cat','/sdcard/kimi-draft-check.xml']);
 const edits=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]]))).filter(a=>a.class==='android.widget.EditText'&&a.focused==='true');
 const ownUntitledRename=edits[0]?.text==='Untitled Chat'&&ui.includes('text="Edit chat name"');
 const ownRepeatRename=edits[0]?.text==='Run03 Repeat Request'&&ui.includes('text="Edit chat name"');
 const ownSkill=/^\uE002\s+theme-kit /.test(edits[0]?.text||'');
 const ownRtl=/^اختبار Run03 /.test(decodeXml(edits[0]?.text||''));
 if(edits.length!==1||edits[0].password==='true'||(!/^Re$|^UItest|^Reply|^Count|^Research|^offline-test-OK$|^\/$/.test(edits[0].text)&&!ownUntitledRename&&!ownRepeatRename&&!ownSkill&&!ownRtl))throw Error('Focused input is not a recognized research draft or known own untitled rename; refuse clearing');
 const stdout=run(['shell','input','keycombination','113','29']);
 action('clear-research-draft',['shell','input','keyevent','67'],'Select all and delete only a recognized local research draft');
}else if(command==='swipe'){
 if(args.slice(0,5).length!==5||!args.slice(0,5).every(x=>/^\d+$/.test(x)))throw Error('swipe requires x1 y1 x2 y2 durationMs');
 action('swipe',['shell','input','swipe',...args.slice(0,5)],args.slice(5).join(' '));
}else if(command==='ui'||command==='ui-brief'||command==='ui-controls'){
 const ui=freshUi('/sdcard/kimi-style-inspect.xml');
 const nodes=[...ui.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],a[2]]))).filter(a=>a.text||a['content-desc']||a.clickable==='true');
 const drawer=nodes.some(n=>n.text==='Search history')||(nodes.some(n=>n.text==='Chat history')&&nodes.some(n=>n.text==='Scheduled tasks'));
 const safeDrawer=/^(Research Run03|Run03|New project$|Project$|Chat history$|Search history$|Scheduled tasks$|Remote control$|Kimi Claw$|Search$|Upgrade Plan$)/;
 const shown=nodes.map(a=>({text:(/^Hi\s|^用户\d|\b\S+@\S+\.\S+\b/.test(a.text)||(drawer&&a.text&&!safeDrawer.test(a.text)))?'[withheld account/history text]':a.text,desc:a['content-desc'],bounds:a.bounds,enabled:a.enabled,checked:a.checked,focused:a.focused,class:a.class}));
 const selected=command==='ui-controls'?shown.filter(a=>a.desc||a.class==='android.widget.EditText'):shown;
 console.log(JSON.stringify(command!=='ui'?selected.filter(a=>a.text||a.desc||command==='ui-controls').map(a=>({text:a.text.length>300?a.text.slice(0,300)+' [preview truncated]':a.text,...(a.text.length>300?{fullTextLength:a.text.length}:{}),desc:a.desc,bounds:a.bounds,...(a.class==='android.widget.EditText'?{focused:a.focused,class:a.class}:{})})):selected,null,command!=='ui'?0:2));
}
else if(command==='capture'){
 const [id,...description]=args;if(!/^[a-z0-9-]+$/.test(id))throw Error('Use unique lowercase scene id');
 if(session.scenes.some(x=>x.id===id))throw Error('Do not overwrite original scenes');
 const dir=path.join(runtime,'scenes',id);fs.mkdirSync(dir,{recursive:true});
 const settlingDelayMs=Number(process.env.KIMI_CAPTURE_SETTLE_MS||0);
 if(!Number.isInteger(settlingDelayMs)||settlingDelayMs<0||settlingDelayMs>5000)throw Error('Invalid capture settling delay');
 if(settlingDelayMs)Atomics.wait(new Int32Array(new SharedArrayBuffer(4)),0,0,settlingDelayMs);
 const png=run(['exec-out','screencap','-p'],true); const screenshotCapturedAt=new Date().toISOString();
 if(png.subarray(0,8).toString('hex')!=='89504e470d0a1a0a')throw Error('No PNG from screencap');
 fs.writeFileSync(path.join(dir,'screen.png'),png);
 let hierarchy=null,hierarchyError=null;
 try{
  run(['shell','rm','-f','/sdcard/kimi-style-window.xml']);
  const dumpOutput=run(['shell','uiautomator','dump','/sdcard/kimi-style-window.xml']);
  if(!dumpOutput.includes('/sdcard/kimi-style-window.xml'))throw Error('UIAutomator did not confirm a newly written hierarchy; old device file was removed before dump');
  hierarchy=run(['exec-out','cat','/sdcard/kimi-style-window.xml']);
  if(!hierarchy.includes('<hierarchy'))throw Error('No hierarchy document returned');
  fs.writeFileSync(path.join(dir,'hierarchy.xml'),hierarchy);
 }catch(e){hierarchy=null;hierarchyError=String(e.message);}
 const scene={id,description:description.join(' '),classification:'A',origin:'current',runId:runId||'first-pass',serial,avd,package:session.package,apkSha256:session.apkSha256,startedAt,capturedAt:new Date().toISOString(),screenshotCapturedAt,atomicCapture:false,method:'Android adb exec-out screencap -p; subsequent uiautomator dump; non-atomic independent shell observation, not REA Evidence',screenshot:{path:`${runtimeRelative}/scenes/${id}/screen.png`,sha256:hash(png),width:png.readUInt32BE(16),height:png.readUInt32BE(20)},hierarchy:hierarchy?{path:`${runtimeRelative}/scenes/${id}/hierarchy.xml`,sha256:hash(hierarchy)}:null,hierarchyError,actionsBeforeCapture:session.actions.length,conditions:{size:run(['shell','wm','size']).trim(),density:run(['shell','wm','density']).trim(),fontScale:run(['shell','settings','get','system','font_scale']).trim(),systemLocales:run(['shell','settings','get','system','system_locales']).trim(),buildLocale:run(['shell','getprop','ro.product.locale']).trim(),appLocales:run(['shell','cmd','locale','get-app-locales',session.package]).trim(),nightMode:run(['shell','cmd','uimode','night']).trim()},window:run(['shell','dumpsys','activity','activities']).split(/[\r\n]+/).filter(x=>/topResumedActivity=/.test(x)),visualReview:'pending human/agent pixel inspection; capture success alone does not establish useful screen'};
 scene.captureSettlingDelayMs=settlingDelayMs;scene.accountMode=session.accountMode;
 fs.writeFileSync(path.join(dir,'capture.json'),JSON.stringify(scene,null,2)+'\n');session.scenes.push(scene);save();console.log(JSON.stringify({id:scene.id,path:scene.screenshot.path,sha256:scene.screenshot.sha256,hierarchyError:scene.hierarchyError}));
}else throw Error('Commands: status, launch, tap x y description, back description, capture id description');
