import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {execFileSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const run='evidence/runtime/run02-20261016';
const read=p=>JSON.parse(fs.readFileSync(path.join(root,p),'utf8').replace(/^\uFEFF/,''));
const sha=b=>crypto.createHash('sha256').update(b).digest('hex');
const session=read(run+'/session.json');
const library=read('library.json');
const gallery=read(run+'/gallery-verification.json');
const shutdown=read(run+'/shutdown-verification.json');
const restored=read(run+'/restore.json');
if(shutdown.remainingOwnedProcesses.length||shutdown.serialStillListed)throw Error('Owned AVD not closed');
if(!restored.network.settingsMatch||!restored.network.activeNetworkVerified)throw Error('Network restoration not verified');
if(!library.runs.some(r=>r.id==='run02-20261016'))throw Error('Missing current run in profile');
let screenshotHashes=0,hierarchyHashes=0;
for(const scene of session.scenes){
 if(sha(fs.readFileSync(path.join(root,scene.screenshot.path)))!==scene.screenshot.sha256)throw Error('Source PNG changed: '+scene.id);screenshotHashes++;
 if(scene.hierarchy){if(sha(fs.readFileSync(path.join(root,scene.hierarchy.path)))!==scene.hierarchy.sha256)throw Error('Source XML changed: '+scene.id);hierarchyHashes++;}
}
const fixture=fs.readFileSync(path.join(root,'assets/derived/test-fixtures/research-upload.txt'));
const push=read('evidence/rea/run02-push-fixture.json');
if(sha(fixture)!==push.normalized_result.sha256||sha(fixture)!==push.normalized_result.device_sha256)throw Error('Fixture identity mismatch');
for(const [file,expected] of [['r02-38-file-id-complete','KIMI-UI-20261016'],['r02-40-retry-after-network','offline-test-OK'],['r02-41-copy-paste','offline-test-OK']]){
 const scene=session.scenes.find(s=>s.id===file);if(!scene.hierarchy||!fs.readFileSync(path.join(root,scene.hierarchy.path),'utf8').includes('text="'+expected+'"'))throw Error('Expected observed result missing: '+file);
}
const xmlStop=fs.readFileSync(path.join(root,session.scenes.find(s=>s.id==='r02-26-checked-stop').hierarchy.path),'utf8');
if(!xmlStop.includes('Output stopped'))throw Error('No UI Stop result');
const documents=['docs/run02-results.md','docs/run02-test-plan.md','docs/sharing-scope.md','spec/run02-behavior-verification.md','spec/run02-authenticated-behavior.md','spec/run02-attachments-and-permissions.md'];
let links=0;
for(const file of documents){const text=fs.readFileSync(path.join(root,file),'utf8');for(const m of text.matchAll(/\[[^\]]*\]\(([^\s)]+)\)/g)){if(/^(https?:|#)/.test(m[1]))continue;links++;if(!fs.existsSync(path.resolve(path.dirname(path.join(root,file)),decodeURIComponent(m[1].split('#')[0]))))throw Error('Broken document link '+file+' '+m[1]);}}
for(const file of ['android-session.mjs','summarize-run02.mjs','verify-run02.mjs','build-run02-gallery.mjs'])execFileSync(process.execPath,['--check',path.join(root,'tools',file)],{windowsHide:true});
const report={verifiedAt:new Date().toISOString(),status:'passed',scope:'Retained artifact identity, source image/XML bytes, fixture consistency, specific expected UI outcomes, current document links and restoration; no new device execution',runId:'run02-20261016',capturedFrames:session.scenes.length,recordedActions:session.actions.length,screenshotHashesVerified:screenshotHashes,hierarchyHashesVerified:hierarchyHashes,semanticLimitation:'Byte-hash validity is separate from screenshot/UIA simultaneity; see independent reviews for early frames and system overlays.',fixtureBytes:fixture.length,fixtureHashMatchesLocalAndDevice:true,uiResultChecks:['File identifier','Offline retry answer','Android clipboard paste into draft','Output stopped'],documentLinksChecked:links,networkSettingsRestored:true,effectiveNetworkObservedBeforeShutdown:true,ownedAvdShutdownVerified:true,credentialsRecorded:false,withheldIdentityScenes:['r02-18-post-login-guide','r02-19-prompt-ready'],galleryVerification:run+'/gallery-verification.json',noFullProductValidation:true};
fs.writeFileSync(path.join(root,run,'integration-verification.json'),JSON.stringify(report,null,2)+'\n');console.log(JSON.stringify(report,null,2));
