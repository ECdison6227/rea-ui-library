import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {execFileSync,spawn,spawnSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
import {setTimeout as delay} from 'node:timers/promises';

/** Prepared offline. Only the sole device controller may execute --capture.
 * It never enters history, selects a model, submits text, or opens another app.
 */
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const serial='emulator-5596',avd='rea_kimi_ui_20261015',packageName='com.moonshot.kimichat';
const apkSha256='9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332';
const runId='run03-20261016',runRoot=path.join(root,'evidence/runtime',runId);
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const argv=process.argv.slice(2),options={};
for(let i=0;i<argv.length;i++){
 const key=argv[i];if(['--check','--help'].includes(key)){options[key]=true;continue;}
 if(!['--capture','--guard-text','--ffmpeg','--ffprobe'].includes(key)||i+1>=argv.length)throw Error('Unknown/incomplete option; use --help.');
 if(options[key]!==undefined)throw Error('Duplicate option.');options[key]=argv[++i];
}
if(options['--help']){
 console.log(`Restricted original-source motion capture; sole controller only.
Offline tool discovery (no ADB):
  node tools/capture-run03-motion.mjs --check
Record after placing a newly-created safe English chat in the foreground:
  node tools/capture-run03-motion.mjs --capture motion-01 --guard-text 'Research Run03 exact submitted message'
Optional existing decoder paths: --ffmpeg PATH --ffprobe PATH. No software installation.
Pins ${serial} / ${avd} / ${packageName}, verifies installed source SHA,
requires a unique submitted self-created message, an idle empty composer,
unique observed placeholder/voice controls, and independent IME-hidden proof.
Focused empty or compact placeholder-only composers are allowed only with
mInputShown=false AND InputMethod window isVisible=false. mIsInputViewShown
is not an actual visibility test. No drawer/generation UI; 1080x2400 canvas.
Holds the original run lock.
Records at most the screenrecord-requested 6 seconds, opens model menu,
outside-closes, reopens then quickly outside-closes. Does not choose any model.
Writes evidence/runtime/${runId}/motion/ID, never source scenes/session.
Host monotonic action-dispatch intervals, guest uptime brackets and video PTS
are separate time bases. No exact animation duration/easing is inferred.
Without working ffmpeg+ffprobe: raw video only; no reliable frame analysis.`);
 process.exit(0);
}
function findTool(name,explicit){
 const guesses=[];if(explicit)guesses.push(path.resolve(explicit));
 const where=spawnSync('where.exe',[name+'.exe'],{encoding:'utf8',windowsHide:true});
 if(where.status===0)guesses.push(...where.stdout.split(/[\r\n]+/).filter(Boolean));
 guesses.push(path.join(process.env.USERPROFILE||'','.rea/toolchains/ffmpeg/bin',name+'.exe'),'C:/Program Files/ffmpeg/bin/'+name+'.exe');
 for(const candidate of [...new Set(guesses)]){
  if(!fs.existsSync(candidate))continue;
  const probe=spawnSync(candidate,['-version'],{encoding:'utf8',windowsHide:true,timeout:5000,maxBuffer:1024*1024});
  if(probe.status===0)return {available:true,path:candidate,version:probe.stdout.split(/[\r\n]+/)[0]};
 }
 return {available:false,path:null,version:null,searchBoundary:'explicit path, current PATH via where.exe, retained user FFmpeg toolchain, C:/Program Files/ffmpeg/bin; not a whole-disk absence claim'};
}
const decoders={ffmpeg:findTool('ffmpeg',options['--ffmpeg']),ffprobe:findTool('ffprobe',options['--ffprobe'])};
if(options['--check']){
 console.log(JSON.stringify({status:'offline-discovery-only',noAdbCalled:true,installationPerformed:false,decoders,reliableFrameDecodeAvailable:decoders.ffmpeg.available&&decoders.ffprobe.available,limitation:'Availability does not prove the eventual video decodes; frame extraction and PTS correspondence must pass separately.'},null,2));process.exit(0);
}
const id=options['--capture'],guardText=options['--guard-text'];
if(!id||!/^[a-z0-9][a-z0-9-]{0,79}$/.test(id))throw Error('Provide a fresh lowercase --capture ID; no ADB operation performed.');
if(!guardText||!/^(Research Run03|Run03 )/.test(guardText)||guardText.length<20||Array.from(guardText).length>2000)throw Error('Provide the exact newly submitted self-created Run03 message as --guard-text (20–2000 characters). No device operation performed.');
const destination=path.join(runRoot,'motion',id);
if(fs.existsSync(destination))throw Error('Motion ID already exists; never overwrite a recording.');
if(fs.existsSync(path.join(root,'evidence/reference',runId,'.operation.lock')))throw Error('Reference controller operation is active; do not share control.');
fs.mkdirSync(runRoot,{recursive:true});
const lockPath=path.join(runRoot,'.operation.lock');let lock,released=false;
try{lock=fs.openSync(lockPath,'wx');fs.writeFileSync(lock,JSON.stringify({pid:process.pid,command:'restricted-motion-capture',serial,avd,startedAt:new Date().toISOString()})+'\n');}
catch{throw Error('Source run lock is held; wait for the single controller. Do not remove a live lock.');}
function release(){if(released)return;released=true;try{fs.closeSync(lock);}catch{}try{fs.unlinkSync(lockPath);}catch{}}
process.once('exit',release);
const origin=process.hrtime.bigint();
const elapsed=()=>Number(process.hrtime.bigint()-origin)/1e6;
const events=[];
function event(name,extra={}){const entry={name,hostMonotonicNs:process.hrtime.bigint().toString(),hostElapsedMs:elapsed(),hostUtc:new Date().toISOString(),...extra};events.push(entry);return entry;}
const localAdb=path.join(process.env.USERPROFILE||'','.rea/toolchains/android/sdk/platform-tools/adb.exe');
const adb=process.env.KIMI_ADB||(fs.existsSync(localAdb)?localAdb:'adb');
function run(parts,binary=false){return execFileSync(adb,['-s',serial,...parts],{encoding:binary?undefined:'utf8',timeout:30000,maxBuffer:12*1024*1024,windowsHide:true});}
function top(){const lines=run(['shell','dumpsys','activity','activities']).split(/[\r\n]+/).filter(s=>s.includes('topResumedActivity='));if(!lines.some(s=>/com\.moonshot\.kimichat\//.test(s)))throw Error('Original Kimi is not actual topResumedActivity. No navigation to source is attempted.');return lines.map(s=>s.trim());}
function freshUi(){const remote='/sdcard/ResearchRun03-motion-ui-'+crypto.randomUUID()+'.xml';try{const output=run(['shell','uiautomator','dump',remote]);if(!output.includes(remote))throw Error('No fresh UIA dump confirmation.');const xml=run(['exec-out','cat',remote]);if(!/<hierarchy\b/.test(xml)||!/<node\b/.test(xml))throw Error('Fresh UIA is empty/null.');return xml;}finally{try{run(['shell','rm','-f',remote]);}catch{}}}
function decode(s){return s.replace(/&#(x[0-9a-f]+|\d+);/gi,(_,n)=>{const p=n[0].toLowerCase()==='x'?parseInt(n.slice(1),16):Number(n);return p<=0x10ffff?String.fromCodePoint(p):_}).replaceAll('&quot;','"').replaceAll('&apos;',"'").replaceAll('&lt;','<').replaceAll('&gt;','>').replaceAll('&amp;','&');}
function nodesFrom(xml){const stack=[],all=[];for(const token of xml.matchAll(/<node\b[^>]*>|<\/node>/g)){if(token[0]==='</node>'){stack.pop();continue;}const n=Object.fromEntries([...token[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a=>[a[1],decode(a[2])]));n.parent=stack.at(-1)||null;n.b=n.bounds?.match(/\d+/g)?.map(Number);all.push(n);if(!token[0].endsWith('/>'))stack.push(n);}return all;}
function imeVisibilityFrom(inputMethodText,windowText){
 const inputShownValues=[...inputMethodText.matchAll(/\bmInputShown=(true|false)\b/g)].map(m=>m[1]==='true');
 const inputShown=inputShownValues.some(Boolean)?true:inputShownValues.length?false:null;
 const headers=[...windowText.matchAll(/^\s*Window #\d+ Window\{[^\r\n]*/gm)];
 const windows=headers.map((m,i)=>({header:m[0].trim(),body:windowText.slice(m.index,headers[i+1]?.index??windowText.length)}));
 const imeWindows=windows.filter(w=>/\bInputMethod\b/.test(w.header)||/\bty=INPUT_METHOD\b/.test(w.body)).map(w=>{
  const values=[...w.body.matchAll(/\bisVisible(?:\(\))?=(true|false)\b/g)].map(m=>m[1]==='true');
  return {header:w.header,isVisible:values.some(Boolean)?true:values.length?false:null,drawState:w.body.match(/\bmDrawState=([A-Z_]+)\b/)?.[1]||null};
 });
 const confirmedHidden=inputShown===false&&imeWindows.length>0&&imeWindows.every(w=>w.isVisible===false);
 return {confirmedHidden,inputShown,inputShownFieldsObserved:inputShownValues.length,inputMethodWindows:imeWindows,windowScanRecognized:headers.length>0,mIsInputViewShownIgnored:true,policy:'Require input_method.mInputShown=false and every identified InputMethod window isVisible=false. NO_SURFACE is auxiliary only; missing/unknown fields do not prove hidden.'};
}
function ownChatGuard(xml,imeVisibility){
 const nodes=nodesFrom(xml),source=nodes.filter(n=>n.package===packageName);
 if(source.some(n=>['Chat history','Scheduled tasks','Project','Log in','Log In'].includes(n.text)))throw Error('Drawer/login context detected; private/identity contexts are rejected.');
 if(source.some(n=>n.text==='Thinking'||n.text==='Send messages anytime…'))throw Error('Generation is active; use an idle self-created test chat.');
 const editors=source.filter(n=>n.class==='android.widget.EditText');
 if(editors.length>1||editors.some(n=>n.text!==''))throw Error('Composer is ambiguous or contains a draft; do not record it.');
 if(source.some(n=>n['content-desc']==='Send message'||n['content-desc']==='Stop output'))throw Error('Send/stop control is visible; composer is nonempty or busy.');
 const placeholders=source.filter(n=>n.class==='android.widget.TextView'&&['Ask anything. Images work too.','Ask me. Task me.'].includes(n.text)&&n.b&&n.b[1]>=1800);
 const voices=source.filter(n=>n['content-desc']==='Switch to voice input'&&n.enabled!=='false'&&n.b&&n.b[1]>=1800);
 const additions=source.filter(n=>n['content-desc']==='More input options'&&n.enabled!=='false'&&n.b&&n.b[1]>=1800);
 if(placeholders.length!==1||voices.length!==1||additions.length!==1)throw Error('No unique observed compact empty-composer placeholder/voice/options structure.');
 if(imeVisibility?.confirmedHidden!==true)throw Error('IME hiding is unverified or visible; require mInputShown=false and InputMethod window isVisible=false independently, regardless of EditText focus.');
 const markers=source.filter(n=>n.class==='android.widget.TextView'&&n.text===guardText&&n.b&&n.b[1]>=280).filter(n=>{for(let p=n.parent;p;p=p.parent)if(p.class==='android.widget.EditText')return false;return true;});
 if(markers.length!==1)throw Error('Exact unique self-created submitted message is not visible. No history is opened.');
 if(source.filter(n=>n['content-desc']==='Navigation button').length!==1)throw Error('Expected a closed-drawer English chat navigation button.');
 const candidates=[];
 for(const node of source.filter(n=>['Kimi','Instant','Kimi K3','Kimi K2.8'].includes(n.text)&&n.b&&n.b[1]<280)){
  let p=node;while(p&&p.clickable!=='true')p=p.parent;
  if(p?.b&&p.b[0]>=160&&p.b[1]>=60&&p.b[3]<=300)candidates.push(p.b);
 }
 const unique=[...new Map(candidates.map(b=>[b.join(','),b])).values()];
 if(unique.length!==1)throw Error('Cannot uniquely locate the current model capsule without guessing.');
 const b=unique[0];return {modelBounds:b,modelTap:[Math.floor((b[0]+b[2])/2),Math.floor((b[1]+b[3])/2)],messageBounds:markers[0].b,guardTextSha256:hash(Buffer.from(guardText)),guardTextCharacters:Array.from(guardText).length,composer:{mode:!editors.length?'compact-placeholder-only':editors[0].focused==='true'?'empty-focused-ime-hidden':'empty-unfocused-ime-hidden',placeholder:placeholders[0].text,editorCount:editors.length,editorFocused:editors[0]?.focused||null,imeHiddenConfirmed:true}};
}
const remoteVideo='/sdcard/ResearchRun03-motion-'+crypto.randomUUID()+'.mp4';
let recorder,recorderDone,recorderExit=null,recordingStarted=false,remoteCleanup=false;
const record={schemaVersion:1,id,classification:'A',origin:'current original runtime only when this capture command is executed',runId,serial,avd,package:packageName,apkSha256,method:'bounded original Android screenrecord plus declared menu/outside taps; no REA Evidence ID',requestedRecordingLimitSeconds:6,sourceSessionAndSceneFilesModified:false,atomicCapture:false,decoders,events,analysis:{animationDuration:null,easing:null,interruptionVerified:false,review:'pending actual video/frame inspection'},timeBasis:{host:'process.hrtime.bigint monotonic; action dispatch and completion on host',guest:'separate /proc/uptime values bracketed by host requests',video:'PTS only if ffprobe successfully reads this actual video',alignment:'Host spawn is not encoder start. ADB dispatch is not on-screen input time. Independent clocks are not treated as synchronized.'}};
try{
 const actualAvd=run(['emu','avd','name']).split(/[\r\n]+/).map(s=>s.trim()).find(Boolean);
 if(actualAvd!==avd)throw Error('Dedicated AVD identity differs.');
 if(run(['shell','getprop','sys.boot_completed']).trim()!=='1')throw Error('Dedicated AVD has not completed boot.');
 const size=run(['shell','wm','size']).trim();if(!/1080x2400\s*$/.test(size))throw Error('This bounded coordinate sequence requires the known 1080x2400 portrait canvas.');
 const packagePaths=run(['shell','pm','path',packageName]).split(/[\r\n]+/).filter(s=>s.startsWith('package:')).map(s=>s.slice(8).trim());
 if(packagePaths.length!==1||!/^\/data\/app\/[A-Za-z0-9/._~+=-]+\/base\.apk$/.test(packagePaths[0])||!packagePaths[0].includes('/'+packageName+'-'))throw Error('Unexpected source package installation path.');
 if(run(['shell','sha256sum',packagePaths[0]]).match(/^([a-f0-9]{64})\s/i)?.[1].toLowerCase()!==apkSha256)throw Error('Installed original artifact hash differs.');
 record.beforeWindow=top();const xml=freshUi();
 const imeRequest=event('ime-visibility-request'),inputMethodDump=run(['shell','dumpsys','input_method']),windowDump=run(['shell','dumpsys','window','windows']),imeVisibility=imeVisibilityFrom(inputMethodDump,windowDump);
 record.imeVisibility={...imeVisibility,hostRequest:imeRequest,hostCompletion:event('ime-visibility-complete'),rawDumpsRecorded:false};record.chatGuard=ownChatGuard(xml,imeVisibility);top();
 record.conditions={size,density:run(['shell','wm','density']).trim(),systemFontScale:run(['shell','settings','get','system','font_scale']).trim(),systemNightMode:run(['shell','cmd','uimode','night']).trim(),keyboard:'idle empty/compact composer; independently confirmed IME hidden, not inferred from focus; pixel verification still required',locale:'English source navigation guard; exact locale not inferred'};
 const png=run(['exec-out','screencap','-p'],true);top();if(png.subarray(0,8).toString('hex')!=='89504e470d0a1a0a')throw Error('Guard screenshot is not PNG.');
 fs.mkdirSync(destination,{recursive:true});fs.writeFileSync(path.join(destination,'guard.png'),png);fs.writeFileSync(path.join(destination,'guard.xml'),xml);
 record.guardArtifacts={screenshotSha256:hash(png),hierarchySha256:hash(Buffer.from(xml)),atomic:false,privacyReview:'pending; exact self-created chat guard is not a pixel privacy review'};
 const guestBefore=event('guest-uptime-before-request');record.guestUptimeBefore={raw:run(['shell','cat','/proc/uptime']).trim(),request:guestBefore,completion:event('guest-uptime-before-complete')};
 let stderr='';recorder=spawn(adb,['-s',serial,'shell','screenrecord','--time-limit','6',remoteVideo],{windowsHide:true,stdio:['ignore','ignore','pipe']});
 recorder.stderr.on('data',b=>{if(stderr.length<16000)stderr+=b.toString('utf8');});
 recorderDone=new Promise(resolve=>{recorder.once('error',error=>{recorderExit={code:null,error:String(error.message)};resolve(recorderExit);});recorder.once('close',code=>{recorderExit={code,stderr};event('screenrecord-adb-process-exit',{code});resolve(recorderExit);});});
 await new Promise((resolve,reject)=>{recorder.once('spawn',resolve);recorder.once('error',reject);});
 const start=event('screenrecord-host-process-spawned');recordingStarted=true;record.hostRecordingSpawn=start;
 async function waitOffset(ms){const remaining=start.hostElapsedMs+ms-elapsed();if(remaining>0)await delay(remaining);if(recorderExit)throw Error('screenrecord exited before the declared action sequence completed.');}
 function tap(name,point,requestedOffsetMs){const dispatch=event(name+'-dispatch',{point,requestedOffsetMs});run(['shell','input','tap',String(point[0]),String(point[1])]);const done=event(name+'-adb-complete');return {dispatch,done};}
 const outside=[540,1680];record.outsideTap=outside;record.declaredSequence='open model menu, outside dismiss, reopen, rapid outside dismiss; no model selection';
 await waitOffset(750);tap('first-open',record.chatGuard.modelTap,750);
 await waitOffset(1500);tap('first-outside-close',outside,1500);
 await waitOffset(2300);const reopen=tap('reopen',record.chatGuard.modelTap,2300);
 const remaining=80-(elapsed()-reopen.dispatch.hostElapsedMs);if(remaining>0)await delay(remaining);
 const fast=tap('rapid-outside-close',outside,null);record.rapidClose={requestedHostDispatchGapMs:80,observedHostDispatchGapMs:fast.dispatch.hostElapsedMs-reopen.dispatch.hostElapsedMs,notAnAnimationDuration:true};
 record.recorderResult=await Promise.race([recorderDone,delay(10000).then(()=>({timeout:true}))]);
 if(record.recorderResult.timeout){recorder.kill();record.ownHostAdbClientTerminated=true;throw Error('Recorder completion is unverified; only this owned host ADB client was terminated, no device recorder process is killed.');}
 if(record.recorderResult.code!==0)throw Error('screenrecord failed; inspect its bounded stderr metadata.');
 const localVideo=path.join(destination,'screenrecord.mp4');run(['pull',remoteVideo,localVideo]);
 const videoBytes=fs.readFileSync(localVideo);if(videoBytes.length<32||videoBytes.subarray(4,8).toString()!=='ftyp')throw Error('Pulled file lacks an MP4 ftyp header.');
 record.video={path:'evidence/runtime/'+runId+'/motion/'+id+'/screenrecord.mp4',bytes:videoBytes.length,sha256:hash(videoBytes),actualDurationSeconds:null};
 run(['shell','rm','-f',remoteVideo]);remoteCleanup=true;
 const guestAfter=event('guest-uptime-after-request');record.guestUptimeAfter={raw:run(['shell','cat','/proc/uptime']).trim(),request:guestAfter,completion:event('guest-uptime-after-complete')};record.afterWindow=top();
 if(decoders.ffprobe.available&&decoders.ffmpeg.available){
  try{
   const raw=execFileSync(decoders.ffprobe.path,['-v','error','-select_streams','v:0','-show_streams','-show_format','-show_frames','-show_entries','frame=best_effort_timestamp_time,pkt_duration_time,key_frame,pict_type','-of','json',localVideo],{encoding:'utf8',timeout:30000,maxBuffer:8*1024*1024,windowsHide:true});
   const probe=JSON.parse(raw);const frames=probe.frames||[];if(!frames.length||frames.some(f=>!Number.isFinite(Number(f.best_effort_timestamp_time))))throw Error('No reliable frame PTS list.');
   const frameDirectory=path.join(destination,'frames');fs.mkdirSync(frameDirectory);
   execFileSync(decoders.ffmpeg.path,['-hide_banner','-loglevel','error','-i',localVideo,'-vsync','0','-frames:v','480',path.join(frameDirectory,'frame-%04d.png')],{timeout:30000,maxBuffer:1024*1024,windowsHide:true});
   const names=fs.readdirSync(frameDirectory).filter(n=>/^frame-\d{4}\.png$/.test(n)).sort();if(names.length!==frames.length||frames.length>480)throw Error('Decoded-frame count differs from PTS list or exceeds bounded cap.');
   const index=names.map((name,i)=>({file:'frames/'+name,ptsSeconds:Number(frames[i].best_effort_timestamp_time),sha256:hash(fs.readFileSync(path.join(frameDirectory,name)))}));
   fs.writeFileSync(path.join(destination,'frame-index.json'),JSON.stringify({method:'all decoded frames, passthrough timing; no FPS resampling',frames:index,animationDuration:null,easing:null},null,2)+'\n');
   record.video.actualDurationSeconds=Number(probe.format?.duration)||null;record.decode={status:'decoded-with-PTS-index',frames:index.length,frameIndex:'frame-index.json',review:'pending visual inspection; interruption and timing are not inferred automatically'};
  }catch(error){record.decode={status:'unreliable-or-failed',reason:String(error.message),limitation:'Raw MP4 retained; partial decoded artifacts are not evidence of a complete frame sequence. No duration/easing/interruption claim.'};}
 }else record.decode={status:'unavailable',limitation:'No verified ffmpeg+ffprobe pair found in the declared search boundary. Raw MP4 only; reliable frame decoding, animation endpoints and interruption remain unverified.'};
 record.status='captured-pending-review';
}catch(error){record.status='failed-or-partial';record.failure=String(error.message);process.exitCode=1;}
finally{
 if(recordingStarted&&recorderDone&&!recorderExit){const ended=await Promise.race([recorderDone,delay(10000).then(()=>({timeout:true}))]);record.finalRecorderResult=ended;if(ended.timeout){recorder.kill();record.ownHostAdbClientTerminated=true;}}
 record.remoteVideoRemoved=remoteCleanup;record.completedAt=new Date().toISOString();
 if(fs.existsSync(destination))fs.writeFileSync(path.join(destination,'capture.json'),JSON.stringify(record,null,2)+'\n');
 release();console.log(JSON.stringify({status:record.status,classification:'A original runtime if capture succeeded; tool preparation alone is not evidence',output:fs.existsSync(destination)?'evidence/runtime/'+runId+'/motion/'+id:null,videoSha256:record.video?.sha256||null,decode:record.decode||null,failure:record.failure||null,animationDuration:null,easing:null,interruptionVerified:false,sourceSceneOrSessionModified:false},null,2));
}
