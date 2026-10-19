import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const run='evidence/runtime/run02-20261016';
const read=p=>JSON.parse(fs.readFileSync(path.join(root,p),'utf8').replace(/^\uFEFF/,''));
const save=(p,v)=>fs.writeFileSync(path.join(root,p),JSON.stringify(v,null,2)+'\n');
const sha=b=>crypto.createHash('sha256').update(b).digest('hex');
const session=read(run+'/session.json');
const state=read(run+'/final-device-state.json');
const baseline=read(run+'/network-baseline.json');
if(state.wifi!==baseline.wifi||state.mobileData!==baseline.data)throw Error('Network settings do not match baseline');
const final=session.scenes.find(s=>s.id==='r02-46-restored-appearance');
if(!final)throw Error('Missing final restoration scene');
const primary=[
 {id:'R02-O41',from:'r02-40-retry-after-network',to:'r02-41-copy-paste',observed:'Newest test answer Copy followed by Android paste places offline-test-OK in unsent composer',limits:['Screenshot text partly obscured by Android paste toast; later UIA confirms complete text','Host clipboard untouched']},
 {id:'R02-O42',from:'r02-41-copy-paste',to:'r02-42-conversation-menu-settled',observed:'Research paste draft cleared; top conversation menu visible with Read aloud/Pin/Rename/Delete',limits:['Intermediate fresh UIA did not expose popup; use original PNG for menu shape']},
 {id:'R02-O43',from:'r02-42-conversation-menu-settled',to:'r02-43-rename-dialog',observed:'Rename opens prefilled Edit chat name dialog for self-created test conversation',limits:['No text change or Done commit performed','Label query had no match; exact observed menu position used']},
 {id:'R02-O44',from:'r02-43-rename-dialog',to:'r02-44-menu-after-cancel',observed:'Cancel rename and reopen top menu; current test conversation retained',limits:['No dirty edit tested']},
 {id:'R02-O45',from:'r02-44-menu-after-cancel',to:'r02-45-delete-confirmation',observed:'Delete opens confirmation for selected 1 chat with Cancel/Delete',limits:['Confirmation preview only; deletion not executed']},
 {id:'R02-O46',from:'r02-45-delete-confirmation',to:'r02-46-restored-appearance',observed:'Cancel deletion; reach appearance through signed-in avatar/settings entry; theme/font Follow System and language English',limits:['Signed-in drawer differs from guest gear entry; intermediate unsuccessful coordinate attempt retained','No prior private history item opened']}
];
const direct=primary.map(p=>{
 const a=session.scenes.find(s=>s.id===p.from),b=session.scenes.find(s=>s.id===p.to);
 return {...p,classification:'A',origin:'current',fromScreenshot:a.screenshot,toScreenshot:b.screenshot,actionIndexBase:1,actionIndexes:Array.from({length:b.actionsBeforeCapture-a.actionsBeforeCapture},(_,i)=>a.actionsBeforeCapture+i+1),atomicCapture:false};
});
save(run+'/final-observations.json',{runId:'run02-20261016',reviewedAt:new Date().toISOString(),method:'Root inspected original PNGs and later UIA; separate time scopes kept',observations:direct});
const stop=session.actions.filter(a=>a.operation==='stop').at(-1);
const exit=session.actions.filter(a=>a.operation==='exit-emulator').at(-1);
save(run+'/restore.json',{recordedAt:new Date().toISOString(),avd:session.avd,serial:session.serial,originalApkModified:false,appearance:{theme:'Follow System',fontSize:'Follow System',language:'English',evidence:final.screenshot},network:{baseline,final:state,settingsMatch:true,activeNetworkVerified:state.connectivitySummary.some(x=>/Active default network: \d+/.test(x))},account:{normalLoginUserAssisted:true,credentialsRecorded:false,retainedOnlyInLocalAvd:true,loggedOut:false},permissions:{microphone:'Requested and refused once; no recording grant or audio test',notification:'Earlier refusal unchanged'},retained:['User-assisted normal login in dedicated local AVD','Self-created neutral test conversations and artificial TXT fixture','Existing tools and source APK'],notPerformed:['Existing private conversation opening','Destructive Delete confirmation','External sharing or publication','Payment/subscription','Host clipboard access'],lifecycle:{stopAction:stop||null,exitAction:exit||null,shutdownVerification:'shutdown-verification.json'},localOnlyEvidence:['r02-18-post-login-guide','r02-19-prompt-ready'],sourceSharing:'docs/sharing-scope.md'});
const sourceHash=sha(fs.readFileSync(path.resolve(root,'../../Kimi.apk')));
if(sourceHash!==session.apkSha256)throw Error('APK changed');
const lib=read('library.json');
lib.name='Kimi Android · 产品风格研究';lib.updatedAt=new Date().toISOString();
lib.runs=[{id:'first-pass-20261015',origin:'historical',session:'evidence/runtime/session.json',readme:'README.md',capturedFrames:read('evidence/runtime/session.json').scenes.length,accountMode:'no-login',restore:'docs/restore.json'},{id:'run02-20261016',origin:'current',session:run+'/session.json',summary:'docs/run02-results.md',accountMode:'no-login then normal user-assisted phone login; credential flow excluded',capturedFrames:session.scenes.length,actions:session.actions.length,restoration:run+'/restore.json',preview:'run02.html',scope:['Model selector and K3 selection','Keyboard and unsent draft','Font preview/Back/Save','Normal generation, Retry and UI Stop','Artificial TXT upload/removal/read','Microphone refusal and text recovery','Offline failure and successful retry','Android copy/paste','Rename/Delete confirmation cancellation']}];
lib.artifacts.runtimeCurrent='run02.html';lib.artifacts.latestSummary='docs/run02-results.md';lib.artifacts.sharingScope='docs/sharing-scope.md';
lib.artifacts.spec=[...new Set([...lib.artifacts.spec,'spec/run02-behavior-verification.md','spec/run02-authenticated-behavior.md','spec/run02-attachments-and-permissions.md'])];
lib.coverage.loggedInFlows='partial-current-run-verified';lib.coverage.generationLifecycle='partial-normal-complete-retry-ui-stop';lib.coverage.attachments='partial-artificial-TXT-end-to-end';lib.coverage.networkRecovery='verified-one-dedicated-avd-offline-retry-path';
lib.unknowns=lib.unknowns.filter(x=>x!=='Login and generated-result lifecycle');
lib.unknowns.push('Actual speech recognition, microphone audio and camera capture','Queueing, concurrent requests, late results and backend cancellation semantics','Other models and thinking effort submenus','Mutation completion/undo and external sharing');
save('library.json',lib);
const index=read('evidence/index.json');index.runs=lib.runs.map(r=>({id:r.id,origin:r.origin,session:r.session,summary:r.summary||'README.md',accountMode:r.accountMode}));
const push=read('evidence/rea/run02-push-fixture.json');const pushBytes=fs.readFileSync(path.join(root,'evidence/rea/run02-push-fixture.json'));
if(!index.rea.some(x=>x.path==='evidence/rea/run02-push-fixture.json'))index.rea.push({path:'evidence/rea/run02-push-fixture.json',sha256:sha(pushBytes),evidenceId:push.evidence_id,operation:push.operation,scope:'Dedicated Kimi AVD artificial attachment fixture; no backend substitution',status:'returned-evidence',limitations:push.limitations});
save('evidence/index.json',index);
console.log(JSON.stringify({run:'run02-20261016',frames:session.scenes.length,actions:session.actions.length,networkRestored:true,sourceApkUnchanged:true,summary:'docs/run02-results.md'}));
