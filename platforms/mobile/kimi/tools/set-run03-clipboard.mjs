import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {execFileSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
import {setTimeout as delay} from 'node:timers/promises';

// Only the designated device controller may execute this tool's device path.
// There is no host or guest clipboard read operation in this tool.
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const serial='emulator-5596',avd='rea_kimi_ui_20261015',packageName='org.research.kimistyle';
const artifact='reference/build/native-reference.apk';
const apk=path.join(root,artifact);
const runtime=path.join(root,'evidence/reference/run03-20261016');
const args=process.argv.slice(2);
const sha=bytes=>crypto.createHash('sha256').update(bytes).digest('hex');
if(args.length===1 && ['--help','help'].includes(args[0])){
 console.log("Usage: node tools/set-run03-clipboard.mjs 'Research Run03 synthetic text'\nValidation without ADB: node tools/set-run03-clipboard.mjs --validate-only 'Research Run03 synthetic text'\nWrites only the explicitly supplied synthetic fixture to guest Android clipboard. Reads neither host nor guest clipboard. Fixed emulator-5596 / rea_kimi_ui_20261015.");process.exit(0);
}
const validateOnly=args[0]==='--validate-only';
if(args.length!==(validateOnly?2:1))throw Error('Pass one synthetic text argument (or --validate-only and one argument).');
const fixture=args[validateOnly?1:0];
const characters=Array.from(fixture).length;
if(!fixture.startsWith('Research Run03')||characters>2000)throw Error('Fixture must start with Research Run03 and contain at most 2000 Unicode characters. No ADB command performed.');
// Reject unpaired UTF-16 surrogates, which Buffer would otherwise silently replace.
if(/(?:[\uD800-\uDBFF](?![\uDC00-\uDFFF])|(?<![\uD800-\uDBFF])[\uDC00-\uDFFF])/.test(fixture))throw Error('Fixture must be valid Unicode. No ADB command performed.');
const bytes=Buffer.from(fixture,'utf8'),fixtureSha256=sha(bytes);
if(validateOnly){console.log(JSON.stringify({classification:'D',status:'validated-only',characters,utf8Bytes:bytes.length,fixtureSha256,clipboardRead:false,hostClipboardAccess:false,noAdbCalled:true},null,2));process.exit(0);}
if(!fs.existsSync(apk))throw Error('Build the reference APK first. No ADB command performed.');
const artifactSha256=sha(fs.readFileSync(apk));
const localAdb=path.join(process.env.USERPROFILE||'','.rea/toolchains/android/sdk/platform-tools/adb.exe');
const adb=process.env.KIMI_ADB||(fs.existsSync(localAdb)?localAdb:'adb');
fs.mkdirSync(runtime,{recursive:true});
const lockPath=path.join(runtime,'.operation.lock');let lock,released=false;
try{lock=fs.openSync(lockPath,'wx');fs.writeFileSync(lock,JSON.stringify({pid:process.pid,startedAt:new Date().toISOString(),command:'set-run03-clipboard',serial,avd,package:packageName})+'\n');}
catch{throw Error('Reference run lock is held. Wait for the controller operation; remove a stale lock only after confirming its PID has exited.');}
function release(){if(released)return;released=true;try{fs.closeSync(lock);}catch{}try{fs.unlinkSync(lockPath);}catch{}}
process.once('exit',release);
const record={schemaVersion:1,classification:'D',operation:'write-synthetic-guest-clipboard',startedAt:new Date().toISOString(),serial,avd,package:packageName,artifact,artifactSha256,fixtureSha256,characters,utf8Bytes:bytes.length,clipboardRead:false,hostClipboardAccess:false,contentsRecorded:false,backend:'independent helper; no account/network/model service',status:'not-attempted'};
function run(parts){try{return execFileSync(adb,['-s',serial,...parts],{encoding:'utf8',timeout:30000,maxBuffer:4*1024*1024,windowsHide:true});}catch{throw Error('ADB '+parts.slice(0,2).join(' ')+' failed; command payload and tool stderr were not retained.');}}
function resumedComponent(lines){return lines.map(line=>line.match(/\s([A-Za-z0-9._]+\/[A-Za-z0-9._$]+)\s/)?.[1]).find(Boolean)||null;}
function helperRecords(){
 const log=run(['logcat','-d','-v','raw','-s','ResearchClipboardFixture:I']);
 return log.split(/[\r\n]+/).map(line=>{try{return JSON.parse(line);}catch{return null;}}).filter(r=>r?.helper==='ClipboardFixtureActivity'&&r?.classification==='D');
}
try{
 const actualAvd=run(['emu','avd','name']).split(/[\r\n]+/).map(s=>s.trim()).find(Boolean);
 if(actualAvd!==avd)throw Error('Target AVD identity mismatch. No clipboard helper launched.');
 if(run(['shell','getprop','sys.boot_completed']).trim()!=='1')throw Error('Dedicated AVD has not completed boot.');
 const paths=run(['shell','pm','path',packageName]).split(/[\r\n]+/).filter(s=>s.startsWith('package:')).map(s=>s.slice(8).trim());
 if(paths.length!==1||!/^\/data\/app\/[A-Za-z0-9/._~+=-]+\/base\.apk$/.test(paths[0])||!paths[0].includes('/'+packageName+'-'))throw Error('Unexpected reference APK installation path.');
 const installed=run(['shell','sha256sum',paths[0]]).match(/^([a-fA-F0-9]{64})\s/);
 if(!installed||installed[1].toLowerCase()!==artifactSha256)throw Error('Installed reference APK differs from the local build. Install the latest APK with native-reference-session.mjs first.');
 const priorAttempts=new Set(helperRecords().map(r=>r.attemptId));
 const previousTop=run(['shell','dumpsys','activity','activities']).split(/[\r\n]+/).filter(s=>s.includes('topResumedActivity=')).map(s=>s.trim());
 // Base64's closed alphabet avoids passing fixture text as shell command text.
 const result=run(['shell','am','start','-W','-f','0x18000000','-n',packageName+'/.ClipboardFixtureActivity','--es','fixtureBase64',bytes.toString('base64')]);
 if(/Error:|Exception/i.test(result))throw Error('Clipboard helper activity did not launch successfully.');
 record.status='launched-awaiting-helper';
 let status;
 for(let attempt=0;attempt<20;attempt++){
   const fresh=helperRecords().filter(r=>!priorAttempts.has(r.attemptId)&&r.sha256===fixtureSha256&&r.characters===characters&&r.utf8Bytes===bytes.length);
   status=fresh.findLast(r=>r.status==='written'||r.status==='write-failed'||String(r.status).startsWith('rejected-'));
   if(status)break;await delay(250);
 }
 if(!status)throw Error('No new matching helper completion metadata observed; clipboard success is unverified.');
 record.status=status.status;record.helperStatus=status;record.previousTop=previousTop;
 await delay(300);
 record.returnedTop=run(['shell','dumpsys','activity','activities']).split(/[\r\n]+/).filter(s=>s.includes('topResumedActivity=')).map(s=>s.trim());
 record.returnedFromHelper=!record.returnedTop.some(s=>s.includes(packageName+'/.ClipboardFixtureActivity'));
 record.previousComponent=resumedComponent(previousTop);record.returnedComponent=resumedComponent(record.returnedTop);
 record.returnedToPreviousActivity=!!record.previousComponent&&record.previousComponent===record.returnedComponent;
 if(status.status!=='written')throw Error('Helper reported '+status.status+'.');
 if(!record.returnedFromHelper)throw Error('Fixture was written, but helper has not returned; inspect the dedicated device.');
 if(!record.returnedToPreviousActivity)throw Error('Fixture was written, but return to the previous activity is unverified; inspect the dedicated device.');
}catch(error){record.status=record.status==='written'?'written-return-unverified':'failed';record.failure=String(error.message);process.exitCode=1;}
finally{
 record.completedAt=new Date().toISOString();
 const directory=path.join(runtime,'clipboard-events');fs.mkdirSync(directory,{recursive:true});
 const name='clipboard-'+Date.now()+'-'+crypto.randomUUID()+'.json';
 fs.writeFileSync(path.join(directory,name),JSON.stringify(record,null,2)+'\n',{flag:'wx'});
 release();
 console.log(JSON.stringify({...record,evidence:'evidence/reference/run03-20261016/clipboard-events/'+name,writesOnly:'explicit synthetic text to guest Android clipboard'},null,2));
}
