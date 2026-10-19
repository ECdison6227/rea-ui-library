/** Retained-evidence verification; no ADB or network calls.
 * node research/kimi-android/tools/verify-run03.mjs [--check] [--distributed] [--require-complete] [--require-restored] [--archive PATH]
 * --check does not write. A passed integrity check is never a claim that all
 * product acceptance criteria passed. D artifacts are checked separately.
 */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {spawnSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
import {auditRun03Zip,loadRun03DistributionPolicy,sourceSceneDisposition} from './run03-distribution-policy.mjs';
import {verifyRun03Restoration} from './verify-run03-restoration.mjs';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const runId='run03-20261016',run=`evidence/runtime/${runId}`;
const expected={package:'com.moonshot.kimichat',versionName:'3.1.3',versionCode:376,sha256:'9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332',bytes:41038830};
const sha=b=>crypto.createHash('sha256').update(b).digest('hex');
const abs=p=>{const a=path.resolve(root,p);if(!a.startsWith(root+path.sep))throw Error('Path outside research root: '+p);return a;};
const read=p=>JSON.parse(fs.readFileSync(abs(p),'utf8').replace(/^\uFEFF/,''));
const exists=p=>fs.existsSync(abs(p));
const errors=[],warnings=[];
const check=(test,message)=>{if(!test)errors.push(message);};
const distributed=process.argv.includes('--distributed');
const distributionPolicy=loadRun03DistributionPolicy(root);
const omittedSourceScenes=[],omittedReferenceScenes=[],omittedEvidence=new Map();
const session=read(run+'/session.json'),list=read('docs/remaining-validation.json'),audit=read('docs/run03-evidence-audit.json');
const notes=new Map(audit.scenes.map(s=>[s.id,s]));
const disposition=id=>sourceSceneDisposition(id,notes.get(id));
const isPrivate=id=>disposition(id).private,isWithheld=id=>disposition(id).withheld;
// Local mode retains the original full-byte requirement. Distributed mode never
// requires or claims to rehash an original APK intentionally excluded from ZIP.
const sourceApk=distributed?null:fs.readFileSync(path.resolve(root,'../../Kimi.apk'));
if(!distributed)check(sourceApk.length===expected.bytes&&sha(sourceApk)===expected.sha256,'Original Kimi.apk identity changed');
if(!distributed)for(const [p,omission]of distributionPolicy.auxiliaryOmissions){check(exists(p),'Retained local-only helper evidence is missing: '+p);if(exists(p)){const bytes=fs.readFileSync(abs(p));check(sha(bytes)===omission.sha256&&bytes.length===omission.bytes,'Retained local-only helper evidence changed: '+p);}}
check(session.package===expected.package&&session.apkSha256===expected.sha256,'Source runtime identity differs');
check(list.target?.package===expected.package&&list.target?.sha256===expected.sha256,'Checklist target differs');
check(list.target?.versionName===expected.versionName&&list.target?.versionCode===expected.versionCode,'Checklist source version metadata differs');
check(session.serial==='emulator-5596'&&session.avd==='rea_kimi_ui_20261015','Source runtime belongs to another dedicated AVD');
const dates=s=>Number.isFinite(Date.parse(s));
let pngCount=0,uiaCount=0,previousActions=0;
const sceneMap=new Map();
for(const scene of session.scenes){
 check(!sceneMap.has(scene.id),'Duplicate source scene ID');sceneMap.set(scene.id,scene);
 check(scene.runId===runId&&scene.classification==='A'&&scene.package===expected.package&&scene.apkSha256===expected.sha256,'Source scene identity/class differs: '+scene.id);
 check(scene.atomicCapture===false,'Source scene incorrectly marked atomic: '+scene.id);
 check(dates(scene.startedAt)&&dates(scene.screenshotCapturedAt)&&dates(scene.capturedAt)&&Date.parse(scene.startedAt)<=Date.parse(scene.screenshotCapturedAt)&&Date.parse(scene.screenshotCapturedAt)<=Date.parse(scene.capturedAt),'Invalid non-atomic capture time order: '+scene.id);
 check(Number.isInteger(scene.actionsBeforeCapture)&&scene.actionsBeforeCapture>=previousActions&&scene.actionsBeforeCapture<=session.actions.length,'Invalid action boundary: '+scene.id);previousActions=scene.actionsBeforeCapture;
 const capturePath=`${run}/scenes/${scene.id}/capture.json`;
 if(distributed&&!distributionPolicy.sourceIds.has(scene.id)){
  const omission=distributionPolicy.sourceOmissions.get(scene.id);
  check(!!omission,'Unpublished source scene has no explicit withheld/pending omission declaration: '+scene.id);
  omittedSourceScenes.push({id:scene.id,category:omission?.category||'undeclared',rawBytesRead:false,rawFilesPresentLocally:[capturePath,scene.screenshot?.path,scene.hierarchy?.path].filter(p=>p&&exists(p)).length});
  continue;
 }
 const captureBytes=fs.readFileSync(abs(capturePath)),capture=JSON.parse(captureBytes.toString('utf8').replace(/^\uFEFF/,''));
 if(distributionPolicy.exactFiles.has(capturePath))check(sha(captureBytes)===distributionPolicy.exactFiles.get(capturePath).sha256,'Approved source capture bytes changed: '+scene.id);
 for(const key of ['id','runId','classification','package','apkSha256','screenshotCapturedAt','capturedAt','actionsBeforeCapture','atomicCapture'])check(capture[key]===scene[key],'Session/capture mismatch: '+scene.id+' '+key);
 for(const kind of ['screenshot','hierarchy']){
  const part=scene[kind];
  if(!part){check(kind==='hierarchy'&&!!scene.hierarchyError,'Unexplained missing source artifact: '+scene.id+' '+kind);continue;}
  check(part.path===capture[kind]?.path&&part.sha256===capture[kind]?.sha256,'Capture artifact registration differs: '+scene.id+' '+kind);
  const bytes=fs.readFileSync(abs(part.path));check(sha(bytes)===part.sha256,'Source artifact bytes changed: '+scene.id+' '+kind);
  if(kind==='screenshot'){
   pngCount++;check(bytes.subarray(0,8).equals(Buffer.from([137,80,78,71,13,10,26,10])),'Invalid source PNG: '+scene.id);
   check(bytes.length>24&&bytes.readUInt32BE(16)===part.width&&bytes.readUInt32BE(20)===part.height,'PNG dimensions differ: '+scene.id);
  }else uiaCount++;
 }
}
for(const id of distributionPolicy.sourceIds)check(sceneMap.has(id),'Approved source scene is absent from its registered session: '+id);
function evidenceAvailable(p){
 const omission=distributed?distributionPolicy.omitted(p):null;
 if(omission){omittedEvidence.set(p,{path:p,...omission,notAShippedRawArtifact:true});return true;}
 return exists(p);
}
for(let i=0;i<session.actions.length;i++){
 const action=session.actions[i];
 check(typeof action.operation==='string'&&dates(action.startedAt)&&dates(action.completedAt)&&Date.parse(action.startedAt)<=Date.parse(action.completedAt),'Malformed action record '+(i+1));
 if(action.arguments!==undefined)check(Array.isArray(action.arguments),'Action arguments are not an array '+(i+1));
}
const expectedIds=[...Array.from({length:33},(_,i)=>'F'+String(i+1).padStart(2,'0')),...Array.from({length:4},(_,i)=>'R'+String(i+1).padStart(2,'0'))];
check(list.taskCount===37&&list.tasks.length===37&&expectedIds.every(id=>list.tasks.some(t=>t.id===id))&&new Set(list.tasks.map(t=>t.id)).size===37,'Checklist must retain all 37 agreed tasks');
const observations=exists(run+'/observations.json')?read(run+'/observations.json'):[];
check(Array.isArray(observations),'Observations must be an array');
const originalCapture=p=>{const m=new RegExp('^'+run+'/scenes/([^/]+)/capture\\.json$').exec(p);return m?sceneMap.get(m[1]):null;};
const evidenceClass=p=>{
 if(originalCapture(p))return 'A';
 if(!exists(p))return 'missing';
 if(/(?:^|\/)reference\//.test(p)||p.startsWith('reference/')||/migration/i.test(p))return 'D';
 return 'other';
};
const counts={},verifiedTasks=[];
for(const task of list.tasks){
 counts[task.status]=(counts[task.status]??0)+1;
 check(Object.hasOwn(list.statusDefinitions,task.status),'Unknown task status: '+task.id);
 const paths=task.evidence??[];
 for(const p of paths)check(evidenceAvailable(p),'Task references missing undeclared evidence: '+task.id);
 if(task.status==='not_applicable')check(!!task.scopeDecision&&!!(task.scopeDecision.acceptedByUser||task.scopeDecision.userConfirmed||task.scopeDecision.authority==='user'),'Not applicable lacks an explicit user scope decision: '+task.id);
 if(task.status!=='verified')continue;
 check(paths.length>0,'Verified task has no evidence: '+task.id);
 check(!task.assistanceRequest||task.assistanceRequest.resolved===true,'Verified task still has unresolved assistance: '+task.id);
 const classes=paths.map(evidenceClass),a=classes.filter(c=>c==='A').length,d=classes.filter(c=>c==='D').length;
 if(task.id.startsWith('F')){
  check(a>0,'Verified source feature lacks original runtime capture evidence: '+task.id);
  check(paths.filter(p=>originalCapture(p)).some(p=>{const id=originalCapture(p).id;return distributionPolicy.sourceIds.has(id)&&notes.get(id)?.pixelReview==='reviewed';}),'Verified source feature has no independently reviewed complete public source scene: '+task.id);
 }
 if(['R01','R03'].includes(task.id))check(d>0,'Verified independent implementation lacks D evidence: '+task.id);
 if(task.id==='R02')check(a>0&&d>0,'Verified source/reference comparison must cite both A and D evidence');
 verifiedTasks.push({id:task.id,sourceACaptures:a,independentDReferences:d,withheldSourceReferences:paths.filter(p=>originalCapture(p)&&isWithheld(originalCapture(p).id)).length,privateSourceReferences:paths.filter(p=>originalCapture(p)&&isPrivate(originalCapture(p).id)).length,incompleteSourceReferences:paths.filter(p=>originalCapture(p)&&disposition(originalCapture(p).id).incomplete).length,declaredOmittedEvidenceReferences:distributed?paths.filter(p=>!!distributionPolicy.omitted(p)).length:0,
  limitation:'Evidence existence, identity and classification checked; task acceptance remains the recorded reviewer judgment, not inferred from counts.'});
}
for(const observation of observations){check(expectedIds.includes(observation.taskId),'Observation references unknown task');for(const p of observation.evidence??[])check(evidenceAvailable(p),'Observation references missing undeclared evidence');}
const auditProbe=spawnSync(process.execPath,[path.join(root,'tools/audit-run03-evidence.mjs'),'--check'],{encoding:'utf8',windowsHide:true});
let live;try{live=JSON.parse(auditProbe.stdout);}catch{errors.push('Audit did not return a valid report');}
if(live){check(!live.integrityFailures?.length,'Safe-review byte identity failed');if(live.pending?.length)warnings.push(`${live.pending.length} source scenes remain unreviewed and must be excluded`);}
let gallery={present:false};
if(exists('run03.html')){
 check(exists(run+'/gallery-verification.json'),'Gallery exists without verification manifest');
 if(exists(run+'/gallery-verification.json')){
  const manifest=read(run+'/gallery-verification.json'),html=fs.readFileSync(abs('run03.html'),'utf8');
  check(sha(Buffer.from(html))===manifest.htmlSha256,'Gallery HTML differs from its manifest');
  const used=[...html.matchAll(/data-scene-id="([^"]+)"/g)].map(m=>m[1]);
  check(used.length===manifest.included.length&&new Set(used).size===used.length,'Gallery scene count differs');
  for(const item of manifest.included){const note=notes.get(item.id);check(used.includes(item.id)&&!isPrivate(item.id)&&note?.distribution==='allow'&&note?.privacy?.status==='clear'&&note?.exactScreenshotSha256===item.screenshotSha256&&note?.exactHierarchySha256===item.hierarchySha256,'Gallery includes unreviewed/private/changed source: '+item.id);}
  for(const source of session.scenes.filter(s=>!distributionPolicy.sourceIds.has(s.id)))check(!html.includes(source.id)&&!html.includes(source.screenshot.path)&&!html.includes(source.screenshot.sha256),'Gallery exposes a withheld/unreviewed source reference');
  check(manifest.unknownScenesIncluded===false&&manifest.rawPrivateSourcesRead===false,'Gallery privacy declaration invalid');
  gallery={present:true,sourceAFrames:manifest.included.length,privateRawIncluded:false,scopeComplete:false};
 }
}
// D build/runtime identity is independent of original source A and never enters its gallery.
const native={classification:'D',buildPresent:exists('reference/build/build-result.json'),runtimePresent:exists(`evidence/reference/${runId}/session.json`),runtimeFrames:0,sourceAcceptanceProven:false};
if(native.buildPresent){const build=read('reference/build/build-result.json');check(build.classification==='D'&&build.package!==expected.package,'Native build incorrectly classified as source A');check(exists(build.apk)&&sha(fs.readFileSync(abs(build.apk)))===build.sha256,'Native D build artifact identity differs');native.buildSha256=build.sha256;native.stateTests=build.stateTests;}
if(native.runtimePresent){
 const dErrorsBefore=errors.length;
 const d=read(`evidence/reference/${runId}/session.json`),dRoot=`evidence/reference/${runId}`,dPackage='org.research.kimistyle',dArtifact='reference/build/native-reference.apk';
 const validSha=value=>typeof value==='string'&&/^[a-f0-9]{64}$/.test(value);
 check(d.classification==='D'&&d.package===dPackage&&d.sourceApkIsDifferentArtifact===true&&d.sourceApkSha256===expected.sha256,'Independent runtime identity/class differs');
 check(d.runId===runId&&d.serial==='emulator-5596'&&d.avd==='rea_kimi_ui_20261015','Independent runtime belongs to another run/device');
 check(d.artifact===dArtifact&&validSha(d.artifactSha256)&&d.artifactSha256!==expected.sha256,'Independent runtime artifact is missing or confused with source APK');
 if(native.buildSha256)check(d.artifactSha256===native.buildSha256,'Latest reference runtime artifact differs from the retained current D build');
 check(Array.isArray(d.scenes)&&Array.isArray(d.actions),'Independent runtime must retain scene/action arrays');
 check(Array.isArray(d.artifactHistory)&&d.artifactHistory.length>0,'Independent runtime lacks artifact history');
 const artifacts=new Set();
 for(const artifact of Array.isArray(d.artifactHistory)?d.artifactHistory:[]){
  check(artifact.artifact===dArtifact&&validSha(artifact.sha256)&&artifact.sha256!==expected.sha256&&dates(artifact.firstSeenAt),'Malformed independent artifact history');
  check(!artifacts.has(artifact.sha256),'Duplicate independent artifact history SHA');artifacts.add(artifact.sha256);
 }
 check(artifacts.has(d.artifactSha256),'Latest independent artifact is not registered in history');
 if(d.installedArtifact)check(d.installedArtifact.package===dPackage&&d.installedArtifact.matchesLocalArtifact===true&&d.installedArtifact.sha256===d.artifactSha256&&dates(d.installedArtifact.checkedAt),'Latest recorded reference installation differs from the runtime artifact');
 const dIds=new Set(),dCaptureHashes=[];let previousDAction=0,dPng=0,dUia=0,dCapture=0;
 for(const scene of Array.isArray(d.scenes)?d.scenes:[]){
  if(!scene||typeof scene!=='object'){check(false,'Independent scene summary is not an object');continue;}
  const label='Independent scene '+String(scene.id);
  check(typeof scene.id==='string'&&/^[a-z0-9][a-z0-9-]{0,95}$/.test(scene.id),label+' has an invalid ID');
  check(!dIds.has(scene.id),label+' ID is duplicated');dIds.add(scene.id);
  check(scene.classification==='D',label+' summary must remain D');
  // Flat driver summaries deliberately omit atomicCapture and, in older records,
  // package/runId. Their authoritative values must always be read from capture.
  if(Object.hasOwn(scene,'package'))check(scene.package===dPackage,label+' summary package differs');
  if(Object.hasOwn(scene,'runId'))check(scene.runId===runId,label+' summary run differs');
  if(Object.hasOwn(scene,'atomicCapture'))check(scene.atomicCapture===false,label+' summary incorrectly declares atomic capture');
  const prefix=`${dRoot}/scenes/${scene.id}`,capturePath=prefix+'/capture.json';
  check(scene.capture===capturePath,label+' flat capture path differs');
  if(distributed&&!distributionPolicy.referenceIds.has(scene.id)){
   check(distributionPolicy.referenceSafe?.defaultPreviewAllowed===false,label+' omission lacks an explicit D default-deny distribution policy');
   check(validSha(scene.artifactSha256)&&artifacts.has(scene.artifactSha256),label+' omitted summary artifact is not registered');
   omittedReferenceScenes.push({id:scene.id,category:'unreviewed-reference',rawBytesRead:false,rawFilesPresentLocally:[capturePath,scene.screenshot,prefix+'/hierarchy.xml'].filter(p=>typeof p==='string'&&exists(p)).length});
   continue;
  }
  if(scene.capture!==capturePath||!exists(capturePath)){check(false,label+' capture file is missing or not at its exact registered path');continue;}
  try{
   const captureBytes=fs.readFileSync(abs(capturePath)),capture=JSON.parse(captureBytes.toString('utf8').replace(/^\uFEFF/,''));dCapture++;dCaptureHashes.push({id:scene.id,sha256:sha(captureBytes)});
   if(distributionPolicy.exactFiles.has(capturePath))check(sha(captureBytes)===distributionPolicy.exactFiles.get(capturePath).sha256,label+' approved capture bytes changed');
   check(capture.id===scene.id&&capture.classification==='D'&&capture.package===dPackage&&capture.runId===runId,label+' capture identity/class differs');
   check(capture.serial===d.serial&&capture.avd===d.avd,label+' capture device differs');
   check(capture.atomicCapture===false,label+' capture must explicitly remain non-atomic');
   check(capture.sourceApkIsDifferentArtifact===true&&capture.sourceApkSha256===expected.sha256,label+' capture confuses reference with source APK');
   check(capture.artifact===dArtifact&&capture.artifactSha256===scene.artifactSha256&&validSha(capture.artifactSha256)&&artifacts.has(capture.artifactSha256),label+' artifact is missing, unregistered or differs from summary');
   const installed=capture.installedArtifact;
   check(installed?.package===dPackage&&installed?.matchesLocalArtifact===true&&installed?.sha256===capture.artifactSha256&&dates(installed?.checkedAt),label+' lacks a matching installed reference APK identity');
   if(dates(installed?.checkedAt)&&dates(capture.startedAt))check(Date.parse(installed.checkedAt)<=Date.parse(capture.startedAt),label+' installed artifact check occurs after capture started');
   check(capture.accountMode==='none',label+' incorrectly claims an authenticated reference account');
   check(capture.description===scene.description&&capture.screenshotCapturedAt===scene.screenshotCapturedAt,label+' flat description/time differs from capture');
   const orderedTimes=['startedAt','screenshotCapturedAt','hierarchyStartedAt','hierarchyCompletedAt','capturedAt'].map(key=>capture[key]);
   check(orderedTimes.every(dates)&&orderedTimes.every((value,i)=>i===0||Date.parse(value)>=Date.parse(orderedTimes[i-1])),label+' non-atomic screenshot/UIA time order differs');
   check(Number.isInteger(capture.actionsBeforeCapture)&&capture.actionsBeforeCapture>=previousDAction&&capture.actionsBeforeCapture<=(d.actions?.length??0),label+' action boundary differs');
   if(Number.isInteger(capture.actionsBeforeCapture))previousDAction=capture.actionsBeforeCapture;
   check(capture.hierarchyError===null,label+' hierarchy must be an actual successful fresh dump');
   for(const [kind,fileName,flatHash]of [['screenshot','screen.png','screenshotSha256'],['hierarchy','hierarchy.xml','hierarchySha256']]){
    const part=capture[kind],expectedPath=prefix+'/'+fileName;
    check(part?.path===expectedPath&&validSha(part?.sha256),label+' '+kind+' exact path/hash is missing');
    check(scene[flatHash]===part?.sha256,label+' '+kind+' flat SHA differs from capture');
    if(kind==='screenshot')check(scene.screenshot===expectedPath,label+' flat screenshot path differs');
    if(part?.path!==expectedPath||!exists(expectedPath)){check(false,label+' '+kind+' file is missing');continue;}
    const bytes=fs.readFileSync(abs(expectedPath));check(sha(bytes)===part.sha256,label+' '+kind+' bytes changed');
    if(distributionPolicy.exactFiles.has(expectedPath))check(sha(bytes)===distributionPolicy.exactFiles.get(expectedPath).sha256,label+' '+kind+' differs from its exact independent review');
    if(kind==='screenshot'){
     dPng++;const png=bytes.length>=24&&bytes.subarray(0,8).equals(Buffer.from([137,80,78,71,13,10,26,10]));check(png,label+' PNG is invalid');
     if(png)check(bytes.readUInt32BE(16)===part.width&&bytes.readUInt32BE(20)===part.height,label+' PNG dimensions differ from capture');
    }else{
     dUia++;const xml=bytes.toString('utf8');check(/<hierarchy\b/.test(xml)&&/<node\b/.test(xml)&&xml.includes('package="'+dPackage+'"'),label+' fresh hierarchy is empty or lacks the reference package');
     check(!xml.includes('package="'+expected.package+'"'),label+' hierarchy contains the original source package');
    }
   }
   for(const stage of ['beforeScreenshot','afterHierarchy'])check(Array.isArray(capture.window?.[stage])&&capture.window[stage].some(line=>line.includes('topResumedActivity=')&&line.includes(dPackage+'/')),label+' actual foreground is unverified at '+stage);
   if(Object.hasOwn(scene,'captureSha256'))check(scene.captureSha256===sha(captureBytes),label+' flat capture SHA differs');
   if(capture.artifactSha256!==d.artifactSha256)warnings.push(label+' belongs to an older registered D artifact; preserve that version boundary in comparisons');
  }catch(error){check(false,label+' capture/artifact validation failed: '+error.message);}
 }
 for(const id of distributionPolicy.referenceIds)check(dIds.has(id),'Approved reference scene is absent from its registered session: '+id);
 native.runtimeFrames=Array.isArray(d.scenes)?d.scenes.length:0;
 native.runtimeFramesValidated=dCapture;native.runtimeFramesDeclaredOmitted=omittedReferenceScenes.length;
 native.captureMetadataRead=dCapture;native.captureSha256Computed=dCaptureHashes;native.screenshotHashesChecked=dPng;native.hierarchyHashesChecked=dUia;
 native.flatSummarySchemaChecked=Array.isArray(d.scenes);
 const expectedCaptureCount=distributed?native.runtimeFrames-omittedReferenceScenes.length:native.runtimeFrames;
 native.captureIdentityAndNonAtomicOrderChecked=expectedCaptureCount>0&&dCapture===expectedCaptureCount&&errors.length===dErrorsBefore;
 native.validationScope=native.runtimeFrames?(distributed?'Exact-approved distributed D subset; excluded unreviewed scene summaries are metadata only':'All local D scenes')+'; authoritative capture identity/time/PNG/UIA/artifact checks remain strict. Prior exact review hashes are enforced whenever approved.':'No D scenes exist yet; per-capture runtime assertions have not been exercised';
}
if(distributionPolicy.referenceIds.size)check(native.runtimePresent,'Approved D evidence lacks its registered session');
let motionAndReviewHashes=0;
for(const [p,descriptor]of distributionPolicy.exactFiles){
 if(!['motion','review-metadata'].includes(descriptor.group))continue;
 if(!exists(p)){check(false,'Required exact distributed metadata/motion file is missing: '+p);continue;}
 const bytes=fs.readFileSync(abs(p));check(sha(bytes)===descriptor.sha256,'Exact distributed metadata/motion bytes changed: '+p);if(descriptor.bytes!==null)check(bytes.length===descriptor.bytes,'Exact distributed metadata/motion byte length differs: '+p);motionAndReviewHashes++;
}
if(list.tasks.some(t=>['R01','R02','R03'].includes(t.id)&&t.status==='verified'))check(native.runtimePresent&&native.runtimeFrames>0,'Reference/transfer task marked verified before D runtime capture');
if(distributed&&list.tasks.some(t=>['R01','R02','R03'].includes(t.id)&&t.status==='verified'))check(native.runtimeFramesValidated>0,'Verified reference/transfer task has no exact-reviewed D capture in the distributed set');
const outstanding=list.tasks.filter(t=>!['verified','not_applicable'].includes(t.status));
const scopeComplete=outstanding.length===0;
if(!scopeComplete)check(!['complete','completed','verified'].includes(list.status),'Checklist globally claims completion while tasks remain outstanding');
if(exists('spec/run03-observed-transitions.json')){const t=read('spec/run03-observed-transitions.json');check(!t.scopeComplete||scopeComplete,'Generated transition report claims completion with outstanding tasks');}
if(process.argv.includes('--require-complete'))check(scopeComplete,'Agreed scope remains incomplete: '+outstanding.map(t=>t.id).join(', '));
const requireRestored=process.argv.includes('--require-restored')||list.tasks.some(t=>t.id==='R04'&&t.status==='verified');
const restorationDetails=verifyRun03Restoration({root,run,session,expected,required:requireRestored,check});
const restoration=restorationDetails.status;
// Optional ZIP audit checks actual member paths AND decompressed exact payloads;
// no archive extraction, native execution, device access or helper process.
let archive={checked:false};
const archiveIndex=process.argv.indexOf('--archive');
if(archiveIndex>=0){
 const archiveArgument=process.argv[archiveIndex+1];if(!archiveArgument||archiveArgument.startsWith('--'))throw Error('--archive requires a ZIP path');
 archive=auditRun03Zip(fs.readFileSync(path.resolve(archiveArgument)),distributionPolicy,check);
}
const omittedCounts=Object.fromEntries(['private','incomplete','other-withheld','unreviewed','undeclared'].map(category=>[category,omittedSourceScenes.filter(s=>s.category===category).length]));
const report={schemaVersion:1,verifiedAt:new Date().toISOString(),runId,verificationMode:distributed?'distributed-exact-public-set':'local-all-retained-bytes',status:errors.length?'failed':'passed-integrity-only',scopeComplete,counts,
 source:{package:expected.package,apkSha256:sourceApk?sha(sourceApk):expected.sha256,originalApkBytesChecked:!distributed,originalApkDistributionBoundary:distributed?'Original APK intentionally not shipped; target/version/hash metadata checked, no claim that original APK bytes were rehashed':'Original retained APK bytes rehashed locally',screenshotHashesChecked:pngCount,hierarchyHashesChecked:uiaCount,actionsChecked:session.actions.length,nonAtomicTimeOrderChecked:true},
 verifiedTasks,outstandingTaskIds:outstanding.map(t=>t.id),gallery,nativeIndependent:native,restoration,restorationDetails,archive,
 distribution:{exactDeclaredFiles:distributionPolicy.exactFiles.size,motionAndReviewHashesChecked:motionAndReviewHashes,sourceOmissions:omittedCounts,omittedSourceScenes,omittedReferenceScenes,declaredOmittedEvidenceReferences:[...omittedEvidence.values()],omissionBoundary:'Excluded raw paths remain traceable metadata references, not shipped artifacts or additional runtime proof. Local mode never skips their retained bytes. Distributed mode requires all exact approved artifacts and permits only explicitly declared/default-deny omissions.',looseWorkspaceContainsExcludedRawFiles:distributed&&[...omittedSourceScenes,...omittedReferenceScenes].some(s=>s.rawFilesPresentLocally>0),actualZipPrivacyAndPayloadHashesChecked:archive.checked},
 privacy:'Withheld may mean private or incomplete, and is not an identity claim. Exact approved A/D/motion artifacts are checked; declared excluded raw sources are not required by --distributed. A loose local workspace may retain excluded files; use --archive PATH to reject their actual ZIP inclusion and verify exact member payload hashes.',
 limitation:'This read-only check does not execute the app, inspect pixels anew, prove internal backend semantics, infer acceptance from file counts, or turn D fixture tests into source A verification.',warnings,errors};
if(!process.argv.includes('--check'))fs.writeFileSync(abs(run+'/integration-verification.json'),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify(report,null,2));if(errors.length)process.exitCode=1;
