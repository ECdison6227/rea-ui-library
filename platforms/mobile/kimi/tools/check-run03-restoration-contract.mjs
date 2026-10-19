/** Offline synthetic contract checks; no ADB, device, archive or evidence mutation. */
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {verifyRun03Restoration} from './verify-run03-restoration.mjs';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const run='evidence/runtime/run03-20261016',fixtureRoot=path.resolve(root,'.runtime','synthetic-restoration-no-files');
const serial='emulator-5596',avd='rea_kimi_ui_20261015',pkg='com.moonshot.kimichat',digest='a'.repeat(64);
const expected={sha256:digest};
const proof={serial,avd,package:pkg,matches:true,originalSha256:digest,installedSha256:digest,versionName:'3.1.3',versionCode:376,bytes:41038830,verifiedAt:'2026-10-16T13:01:00.000Z'};
const permissions=['POST_NOTIFICATIONS','CAMERA','RECORD_AUDIO'].map((name,i)=>({permission:'android.permission.'+name,granted:false,flags:i===1?[]:['USER_SET']}));
const baseline={size:'Physical size: 1080x2400',density:'Physical density: 420',night:'Night mode: no',wifi:'1',mobileData:'1'};
const conditions={settings:{font_scale:'1.0'},size:baseline.size,density:baseline.density,appLocales:'[en-US]',ime:'fixture.ime'};
const state={schemaVersion:2,kind:'run03-final-read-only-state',runId:'run03-20261016',serial,avd,package:pkg,startedAt:'2026-10-16T13:00:00.000Z',completedAt:'2026-10-16T13:00:30.000Z',...baseline,settings:{...conditions.settings,wifi_on:'1',mobile_data:'1'},wmUserRotation:'free',wmFixedToUserRotation:'default',ime:conditions.ime,appLocales:conditions.appLocales,network:{wifi:'1',data:'1',activeDefaultNetworkValidated:true,defaultNetworkLine:'Active default network: 106'},permissions,locale:{systemLocalesGet:'null',systemLocalesKeyPresent:false,buildLocale:'en-US'}};
const restore={serial,avd,network:{settingsMatch:true,activeNetworkVerified:true},locale:{initialGet:'null',initialKeyPresenceKnown:false,exactStorageRestorationClaim:false,limitation:'Initial key presence was not measured; null get does not prove initial storage absence.'},appPreferences:{theme:'Follow System',fontSize:'Follow System',language:'English',model:'Instant',thinking:'High',webSearch:'Auto',reviewed:true,evidenceSceneIds:['appearance','model']},account:{loggedOut:false,credentialsRecorded:false}};
const session={identityVerificationFinal:proof,scenes:[{id:'appearance',classification:'A',hierarchy:{},conditions:{systemLocales:'null'},capturedAt:'2026-10-16T12:50:00.000Z'},{id:'model',classification:'A',hierarchy:{},capturedAt:'2026-10-16T12:51:00.000Z'}]};
const records={
 [run+'/restore.json']:restore,[run+'/final-device-state.json']:state,[run+'/artifact-identity-final.json']:proof,
 [run+'/shutdown-verification.json']:{serial,dedicatedAvd:avd,serialStillListed:false,remainingOwnedProcesses:[],checkedAt:'2026-10-16T13:02:00.000Z'},
 [run+'/environment-baseline.json']:baseline,[run+'/conditions-baseline.json']:conditions,[run+'/wm-rotation-baseline.json']:{userRotation:'free',fixed:'default'},
 [run+'/permission-baseline.json']:{permissions:permissions.map(p=>p.permission+': granted=false, flags=[ '+p.flags.join('|')+']')}
};
const realExists=fs.existsSync,realRead=fs.readFileSync;
let checked=0;
function evaluate(mutator=()=>{}){
 const copy=structuredClone(records),copySession=structuredClone(session);mutator(copy,copySession);
 const fixture=new Map(Object.entries(copy).map(([p,v])=>[path.join(fixtureRoot,p),JSON.stringify(v)]));
 const errors=[];let result;
 try{fs.existsSync=p=>fixture.has(String(p));fs.readFileSync=p=>{assert(fixture.has(String(p)),'unexpected synthetic read');return fixture.get(String(p));};result=verifyRun03Restoration({root:fixtureRoot,run,session:copySession,expected,required:true,check:(ok,message)=>{if(!ok)errors.push(message);}});}
 finally{fs.existsSync=realExists;fs.readFileSync=realRead;}
 checked++;return {result,errors};
}
assert.equal(evaluate().errors.length,0);
for(const mutate of [
 r=>r[run+'/final-device-state.json'].permissions[1].granted=true,
 r=>r[run+'/final-device-state.json'].permissions[1].flags=['USER_SET'],
 r=>r[run+'/final-device-state.json'].locale.systemLocalesGet='en-US',
 r=>r[run+'/restore.json'].locale.exactStorageRestorationClaim=true,
 r=>r[run+'/final-device-state.json'].network.activeDefaultNetworkValidated=false,
 r=>r[run+'/final-device-state.json'].avd='other-avd',
 r=>delete r[run+'/artifact-identity-final.json'],
 (r,s)=>s.scenes.at(-1).capturedAt='2026-10-16T14:00:00.000Z',
 r=>r[run+'/restore.json'].appPreferences.evidenceSceneIds=['unknown','model'],
 (r,s)=>delete s.identityVerificationFinal,
 r=>r[run+'/shutdown-verification.json'].serial='emulator-5580'
])assert(evaluate(mutate).errors.length>0,'synthetic invalid restoration was accepted');
const source=realRead(path.join(root,'tools/android-session.mjs'),'utf8');
const start=source.indexOf("if(command==='verify-apk'){")+"if(command==='verify-apk'){".length;
const branch=source.slice(start,source.indexOf("}else if(command==='status')",start));
assert(start>0&&branch.includes("flag:'wx'"));
const invoke=new Function('fs','path','root','runtime','runtimeRelative','session','args','serial','avd','hash','run','save','console',branch);
const stored=new Map(),mockSession={package:pkg,apkSha256:digest,identityVerification:{retained:true}};
let adbCalls=0,saves=0;
const mockFs={existsSync:p=>stored.has(p),readFileSync:()=>({length:41038830}),writeFileSync:(p,bytes,options)=>{assert.equal(options.flag,'wx');assert(!stored.has(p));stored.set(p,bytes);}};
const fakeRun=args=>{adbCalls++;if(args[1]==='pm')return 'package:/data/app/fixture/'+pkg+'-fixture/base.apk\n';if(args[1]==='sha256sum')return digest+'  fixture';return 'versionName=3.1.3\nversionCode=376';};
const execute=args=>invoke(mockFs,path,fixtureRoot,path.join(fixtureRoot,run),run,mockSession,args,serial,avd,()=>digest,fakeRun,()=>saves++,{log:()=>{}});
execute(['final']);assert.deepEqual(mockSession.identityVerification,{retained:true});assert.equal(mockSession.identityVerificationFinal.installedSha256,digest);assert.equal(saves,1);
const callsBeforeDuplicate=adbCalls;assert.throws(()=>execute(['final']),/already exists/);assert.equal(adbCalls,callsBeforeDuplicate);
assert.throws(()=>execute(['unknown']),/optional final/);assert.deepEqual(mockSession.identityVerification,{retained:true});
console.log(JSON.stringify({status:'passed-offline-synthetic-contracts',restorationCases:checked,immutableFinalProofCases:3,adbCalled:false,filesWritten:false}));
