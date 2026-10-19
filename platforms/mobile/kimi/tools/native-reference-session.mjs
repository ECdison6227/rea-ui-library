import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {execFileSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
import {setTimeout as delay} from 'node:timers/promises';

// D-only device driver. The implementation worker must not execute ADB.
// Only the designated controller should run commands after source work has paused.
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const serial='emulator-5596';
const expectedAvd='rea_kimi_ui_20261015';
const packageName='org.research.kimistyle';
const component=packageName+'/.MainActivity';
const runId='run03-20261016';
const relativeOutput='evidence/reference/'+runId;
const runtime=path.join(root,relativeOutput);
const artifactRelative='reference/build/native-reference.apk';
const artifactPath=path.join(root,artifactRelative);
const sourceApkSha256='9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332';
const localAdb=path.join(process.env.USERPROFILE||'','.rea/toolchains/android/sdk/platform-tools/adb.exe');
const adb=process.env.KIMI_ADB || (fs.existsSync(localAdb)?localAdb:'adb');
const [command,...args]=process.argv.slice(2);
const knownScenes=new Set(['chat-empty','chat-complete','chat-busy','chat-error','source-capacity-error','chat-draft','settings','delete-dialog','documents-empty','documents-normal','documents-edit','documents-busy','documents-error','enable-failure','restore']);
const commands=new Set(['install','launch','scene','ui','tap-label','tap','back','capture','stop','summary']);
const digest=bytes=>crypto.createHash('sha256').update(bytes).digest('hex');
const now=()=>new Date().toISOString();
const help=`Independent native reference driver (classification D)
Fixed target: ${serial} / ${expectedAvd} / ${packageName}
Commands:
  install                       Install/update this reference APK only
  launch                        Launch existing local state without a fixture extra
  scene NAME                    Load a named local fixture and wait 1000 ms
  ui                            Print fresh native reference UI hierarchy
  tap-label LABEL               Tap one uniquely actionable fresh exact label
  tap X Y                       Tap nonnegative integer coordinates
  back                          Android Back while reference is foreground
  capture ID DESCRIPTION        Save a new screenshot/UIA/metadata scene; never overwrite
  stop                          Force-stop this reference package only
  summary                       Read local evidence counts; does not call ADB
No command addresses the original Kimi package or another device.
Capture is non-atomic; a reviewer must inspect pixels and hierarchy separately.`;
if(!command||command==='help'||command==='--help') { console.log(help); process.exit(0); }
if(!commands.has(command)) throw Error('Unknown command. Use --help. No device operation performed.');
if(command==='scene' && (args.length!==1||!knownScenes.has(args[0]))) throw Error('Unknown or invalid scene. No device operation performed.');
if(command==='tap' && (args.length!==2||!args.every(v=>/^\d{1,5}$/.test(v)))) throw Error('tap needs two nonnegative integer coordinates.');
if(command==='tap-label' && (args.length!==1||!args[0])) throw Error('tap-label needs one exact label.');
if(command==='launch' && args.length) throw Error('launch accepts no fixture arguments; use scene NAME separately.');
if(command==='capture' && (args.length<2||!/^[a-z0-9][a-z0-9-]{0,95}$/.test(args[0]))) throw Error('capture needs a fresh lowercase ID and a description.');
const indexPath=path.join(runtime,'session.json');
if(command==='summary') {
  const saved=fs.existsSync(indexPath)?JSON.parse(fs.readFileSync(indexPath,'utf8')):null;
  console.log(JSON.stringify({classification:'D',package:packageName,runId,sessionExists:!!saved,actions:saved?.actions.length||0,scenes:saved?.scenes.length||0,latestScene:saved?.scenes.at(-1)?.id||null,latestAction:saved?.actions.at(-1)?.operation||null,latestArtifactSha256:saved?.artifactSha256||null,output:relativeOutput,noAdbCalled:true},null,2));
  process.exit(0);
}
if(!fs.existsSync(artifactPath))throw Error('Build the reference APK first. No ADB command performed.');
const artifactSha256=digest(fs.readFileSync(artifactPath));
fs.mkdirSync(runtime,{recursive:true});
const lockPath=path.join(runtime,'.operation.lock');
let lock;
try { lock=fs.openSync(lockPath,'wx');fs.writeFileSync(lock,JSON.stringify({pid:process.pid,startedAt:now(),command,serial,avd:expectedAvd,package:packageName})+'\n'); }
catch { throw Error('Another reference operation holds this run lock. Wait; remove a stale lock only after confirming its owner PID has exited.'); }
let released=false;
function release(){if(released)return;released=true;try{fs.closeSync(lock);}catch{}try{fs.unlinkSync(lockPath);}catch{}}
process.once('exit',release);
const session=fs.existsSync(indexPath)?JSON.parse(fs.readFileSync(indexPath,'utf8')):{schemaVersion:1,classification:'D',origin:'current independent reference runtime',runId,serial,avd:expectedAvd,package:packageName,sourceApkSha256,sourceApkIsDifferentArtifact:true,backend:'independent local fixture; no model/network/account service',accountMode:'none',createdAt:now(),artifact:artifactRelative,artifactHistory:[],actions:[],scenes:[]};
if(session.package!==packageName||session.classification!=='D'||session.serial!==serial||session.avd!==expectedAvd)throw Error('Existing session identity differs; refusing to append.');
session.artifactSha256=artifactSha256;
if(!session.artifactHistory.some(a=>a.sha256===artifactSha256))session.artifactHistory.push({artifact:artifactRelative,sha256:artifactSha256,firstSeenAt:now()});
function save(){session.updatedAt=now();fs.writeFileSync(indexPath,JSON.stringify(session,null,2)+'\n');}
function adbRun(parts,binary=false){return execFileSync(adb,['-s',serial,...parts],{encoding:binary?undefined:'utf8',timeout:30000,maxBuffer:16*1024*1024,windowsHide:true});}
function action(operation,parts,description,extra={}) {
 const startedAt=now();
 try {const stdout=adbRun(parts);session.actions.push({operation,description,startedAt,completedAt:now(),arguments:operation==='install'?['install','-r',artifactRelative]:parts,stdout,...extra});save();return stdout;}
 catch(error){session.actions.push({operation,description,startedAt,completedAt:now(),status:'failed',error:String(error.message),...extra});save();throw error;}
}
function guardAvd(){
 let actual=adbRun(['emu','avd','name']).split(/[\r\n]+/).map(s=>s.trim()).find(Boolean);
 if(!actual)actual=adbRun(['shell','getprop','ro.boot.qemu.avd_name']).trim();
 if(actual!==expectedAvd)throw Error('Refusing '+serial+': actual AVD '+actual+' differs from '+expectedAvd);
 const boot=adbRun(['shell','getprop','sys.boot_completed']).trim();if(boot!=='1')throw Error('Dedicated AVD has not completed boot.');
 session.lastDeviceGuard={checkedAt:now(),serial,avd:actual,boot};save();
}
function foreground(){
 const raw=adbRun(['shell','dumpsys','activity','activities']);
 const lines=raw.split(/[\r\n]+/).filter(s=>s.includes('topResumedActivity='));
 if(!lines.some(s=>/org\.research\.kimistyle\//.test(s)))throw Error('Reference is not the actual topResumedActivity. No input/capture performed.');
 return lines.map(s=>s.trim());
}
function installedIdentity(){
 const output=adbRun(['shell','pm','path',packageName]);
 const paths=output.split(/[\r\n]+/).filter(s=>s.startsWith('package:')).map(s=>s.slice(8).trim());
 if(paths.length!==1||!/^\/data\/app\/[A-Za-z0-9/._~+=-]+\/base\.apk$/.test(paths[0])||!paths[0].includes('/'+packageName+'-'))throw Error('Unexpected reference APK path or split install; refusing identity inference.');
 const result=adbRun(['shell','sha256sum',paths[0]]).trim();const installed=result.match(/^([a-fA-F0-9]{64})\s/);
 if(!installed)throw Error('Could not hash installed reference APK.');
 const sha256=installed[1].toLowerCase();if(sha256!==artifactSha256)throw Error('Installed reference APK differs from the local build. Run install before capture or interaction.');
 return {package:packageName,sha256,checkedAt:now(),matchesLocalArtifact:true};
}
function freshUi(){
 const remote='/sdcard/native-reference-ui-'+process.pid+'-'+Date.now()+'.xml';
 const startedAt=now();let xml;
 try{
   const output=adbRun(['shell','uiautomator','dump',remote]);
   if(!output.includes(remote))throw Error('UIA did not confirm the fresh reference dump path.');
   xml=adbRun(['exec-out','cat',remote]);
   if(!/<hierarchy\b/.test(xml)||!/<node\b/.test(xml)||/root\s*=\s*["']?null/.test(xml))throw Error('Fresh UIA hierarchy is empty/null.');
 }finally{try{adbRun(['shell','rm','-f',remote]);}catch{}}
 return {xml,startedAt,completedAt:now()};
}
function decode(value){return value.replaceAll('&quot;','"').replaceAll('&apos;',"'").replaceAll('&lt;','<').replaceAll('&gt;','>').replaceAll('&amp;','&');}
function summary(value){console.log(JSON.stringify(value,null,2));}

try{
 guardAvd();
 if(command==='install'){
   const output=action('install',['install','-r',artifactPath],'Install/update independent native reference only',{artifactSha256});
   if(!/\bSuccess\b/.test(output))throw Error('ADB install did not report Success.');
   const installed=installedIdentity();session.installedArtifact=installed;save();summary({operation:'install',package:packageName,sha256:installed.sha256,classification:'D'});
 }else if(command==='scene'||command==='launch'){
   const installed=installedIdentity();
   if(command==='scene')action('scene',['shell','am','start','-f','0x20000000','-n',component,'--es','scene',args[0]],'Load disposable independent reference fixture',{scene:args[0],artifactSha256});
   else action('launch',['shell','am','start','-f','0x20000000','-n',component],'Launch independent reference without a fixture extra; preserve stored local state',{artifactSha256});
   await delay(1000);const window=foreground();summary({operation:command,scene:command==='scene'?args[0]:null,settlingDelayMs:1000,window,artifactSha256:installed.sha256,classification:'D'});
 }else if(command==='stop'){
   installedIdentity();action('stop',['shell','am','force-stop',packageName],'Stop only the independent reference package; preserve local reference data');summary({operation:'stop',package:packageName});
 }else{
   const installed=installedIdentity();foreground();
   if(command==='ui'){
     const ui=freshUi();foreground();session.actions.push({operation:'ui',startedAt:ui.startedAt,completedAt:ui.completedAt,sha256:digest(Buffer.from(ui.xml)),description:'Read fresh native reference hierarchy'});save();console.log(ui.xml);
   }else if(command==='tap-label'){
     const ui=freshUi();foreground();
     const observedMatches=[...ui.xml.matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],decode(a[2])]))).filter(n=>n.package===packageName&&n.enabled!=='false'&&(n.text===args[0]||n['content-desc']===args[0]));
     // Native clickable rows may also expose a nonclickable child with the same text.
     // Resolve only when exactly one exact candidate is explicitly clickable.
     const clickableMatches=observedMatches.filter(n=>n.clickable==='true');
     const matches=observedMatches.length>1&&clickableMatches.length===1?clickableMatches:observedMatches;
     if(matches.length!==1)throw Error('Expected one uniquely actionable fresh reference label '+args[0]+'; found '+observedMatches.length+' exact matches, '+clickableMatches.length+' clickable. No tap performed.');
     const b=matches[0].bounds?.match(/\d+/g)?.map(Number);if(!b||b.length!==4||b[2]<=b[0]||b[3]<=b[1])throw Error('Reference label has unusable bounds.');
     const x=Math.floor((b[0]+b[2])/2),y=Math.floor((b[1]+b[3])/2);
     action('tap-label',['shell','input','tap',String(x),String(y)],'Tap one freshly observed reference label',{label:args[0],bounds:matches[0].bounds,observedExactMatches:observedMatches.length,resolution:observedMatches.length>1?'unique-clickable-exact-match':'unique-exact-match',guardHierarchySha256:digest(Buffer.from(ui.xml))});summary({operation:'tap-label',label:args[0],x,y});
   }else if(command==='tap'){
     const size=adbRun(['shell','wm','size']);const values=[...size.matchAll(/(\d+)x(\d+)/g)].at(-1);if(!values||Number(args[0])>=Number(values[1])||Number(args[1])>=Number(values[2]))throw Error('Tap is outside the current device canvas.');
     action('tap',['shell','input','tap',args[0],args[1]],'Coordinate tap within foreground independent reference');summary({operation:'tap',x:Number(args[0]),y:Number(args[1])});
   }else if(command==='back'){
     action('back',['shell','input','keyevent','4'],'Android Back in foreground independent reference');summary({operation:'back'});
   }else if(command==='capture'){
     const id=args[0];const destination=path.join(runtime,'scenes',id);if(fs.existsSync(destination))throw Error('Capture ID already exists; refusing to overwrite '+id);
     const startedAt=now();await delay(1000);const beforeWindow=foreground();
     const conditions={size:adbRun(['shell','wm','size']).trim(),density:adbRun(['shell','wm','density']).trim(),systemFontScale:adbRun(['shell','settings','get','system','font_scale']).trim(),systemLocales:adbRun(['shell','settings','get','system','system_locales']).trim(),systemNightMode:adbRun(['shell','cmd','uimode','night']).trim(),referenceAppTheme:'not introspected; verify pixels and selected settings independently'};
     const png=adbRun(['exec-out','screencap','-p'],true);const screenshotCapturedAt=now();foreground();
     if(png.length<24||png.subarray(0,8).toString('hex')!=='89504e470d0a1a0a')throw Error('Device screenshot is not a valid PNG header.');
     const ui=freshUi();const afterWindow=foreground();
     const relativeScene=relativeOutput+'/scenes/'+id;
     const capture={schemaVersion:1,id,description:args.slice(1).join(' '),classification:'D',origin:'current independent reference runtime',runId,serial,avd:expectedAvd,package:packageName,artifact:artifactRelative,artifactSha256,installedArtifact:installed,sourceApkSha256,sourceApkIsDifferentArtifact:true,backend:'local fixture; no real model/network/account service',accountMode:'none',startedAt,screenshotCapturedAt,hierarchyStartedAt:ui.startedAt,hierarchyCompletedAt:ui.completedAt,capturedAt:now(),settlingDelayMs:1000,atomicCapture:false,method:'ADB PNG first; fresh UIA second; separate shell observations; not REA Evidence',screenshot:{path:relativeScene+'/screen.png',sha256:digest(png),width:png.readUInt32BE(16),height:png.readUInt32BE(20)},hierarchy:{path:relativeScene+'/hierarchy.xml',sha256:digest(Buffer.from(ui.xml)),rotation:ui.xml.match(/<hierarchy\b[^>]*rotation="([^"]*)"/)?.[1]||null},hierarchyError:null,conditions,window:{beforeScreenshot:beforeWindow,afterHierarchy:afterWindow},actionsBeforeCapture:session.actions.length,visualReview:'pending pixel and hierarchy inspection; command success does not verify behavior'};
     fs.mkdirSync(path.dirname(destination),{recursive:true});fs.mkdirSync(destination);fs.writeFileSync(path.join(destination,'screen.png'),png);fs.writeFileSync(path.join(destination,'hierarchy.xml'),ui.xml);fs.writeFileSync(path.join(destination,'capture.json'),JSON.stringify(capture,null,2)+'\n');
     session.scenes.push({id,description:capture.description,classification:'D',capture:relativeScene+'/capture.json',screenshot:capture.screenshot.path,screenshotSha256:capture.screenshot.sha256,hierarchySha256:capture.hierarchy.sha256,artifactSha256,screenshotCapturedAt});save();summary({operation:'capture',id,classification:'D',package:packageName,artifactSha256,screenshot:capture.screenshot.path,size:png.readUInt32BE(16)+'x'+png.readUInt32BE(20),atomicCapture:false});
   }
 }
}catch(error){
 session.failures ||= [];session.failures.push({command,arguments:command==='install'?[]:args,recordedAt:now(),error:String(error.message)});save();console.error('Reference operation failed: '+error.message);process.exitCode=1;
}finally{release();}
