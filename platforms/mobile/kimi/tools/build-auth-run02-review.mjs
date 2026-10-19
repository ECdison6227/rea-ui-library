import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import assert from 'node:assert/strict';
import {fileURLToPath} from 'node:url';

// Retained safe research samples only. Does not connect to a device, inspect
// credential/identity captures, or copy the complete session into the review.
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const run='evidence/runtime/run02-20261016';
const runDir=path.join(root,run);
const session=JSON.parse(fs.readFileSync(path.join(runDir,'session.json'),'utf8'));
const safe=[
 ['r02-20-first-submit','Screenshot: full Reply only OK user bubble, generating appearance and white square control; later UIA: already Thinking complete / OK / toolbar','State advanced between screenshot and UIA'],
 ['r02-21-first-complete','Screenshot and UIA: Thinking complete, OK, answer toolbar, empty ordinary composer',null],
 ['r02-22-conversation-menu','After More click, screenshot and UIA still show chat; menu contents not visible','Filename is not proof of menu opening'],
 ['r02-24-generating-sequence','Full Count 1 to 1000, one per line. request, Thinking, Send messages anytime… placeholder and square appearance; UIA control desc remains Send message','Visual/semantic label mismatch'],
 ['r02-25-stop-result','Answer result card with only 1 through 20 visible in viewport, answer toolbar and Hold to talk composer','Late stop attempt is not a successful stop; voice standby visible, no recording established'],
 ['r02-26-checked-stop','Output stopped, tool panel contains Failed to execute Python code, ordinary empty text composer','Tool error causality unknown; left floating Gboard strip is system input UI by controller identification']
];
const hashFile=p=>crypto.createHash('sha256').update(fs.readFileSync(path.join(root,p))).digest('hex');
const scenes=safe.map(([id,observation,qualification])=>{
 const capture=JSON.parse(fs.readFileSync(path.join(runDir,'scenes',id,'capture.json'),'utf8'));
 assert.equal(hashFile(capture.screenshot.path),capture.screenshot.sha256);
 if(capture.hierarchy)assert.equal(hashFile(capture.hierarchy.path),capture.hierarchy.sha256);
 return {id,classification:'A',origin:'current',review:{originalImageViewed:true,sourceHierarchyRead:!!capture.hierarchy,observation,qualification},capture:`${run}/scenes/${id}/capture.json`,screenshot:capture.screenshot,hierarchy:capture.hierarchy,hierarchyError:capture.hierarchyError,screenshotCapturedAt:capture.screenshotCapturedAt,captureStartedAt:capture.startedAt,captureCompletedAt:capture.capturedAt,atomicCapture:false,conditions:capture.conditions,actionsBeforeCapture:capture.actionsBeforeCapture};
});
const transitions=[
 {id:'AUTH-T01',fromScene:null,actionIndexes:[51],toScene:'r02-20-first-submit',result:'Full Reply only OK request is visible in user bubble; screenshot is generating; later same-capture UIA is already complete',verification:'observed-submission-result-with-nonatomic-state-advance',unknowns:['Exact completion instant','Pre-submit identity-containing samples intentionally not inspected']},
 {id:'AUTH-T02',fromScene:'r02-20-first-submit',actionIndexes:[],toScene:'r02-21-first-complete',result:'Thinking complete and OK with toolbar, empty ordinary composer',verification:'observed-answer-completion',unknowns:['Generation duration','Backend model identity','Additional send during generation']},
 {id:'AUTH-T03',fromScene:'r02-21-first-complete',actionIndexes:[52],toScene:'r02-22-conversation-menu',result:'Early frame still shows chat; no menu contents are visible',verification:'menu-not-verified',unknowns:['Whether menu later opened','Menu contents','Outside or Back close semantics']},
 {id:'AUTH-T04',fromScene:'r02-22-conversation-menu',actionIndexes:[53],toScene:null,result:'Controller reports Back returned to blank home; Copy lookup found no match and did not execute',verification:'controller-report-without-reviewed-target-frame',copyVerification:{attemptedLookup:true,actionExecuted:false,sessionActionIndex:null,result:'No matching target; no clipboard result verified'},unknowns:['Uncaptured blank home details','Whether any menu existed before Back']},
 {id:'AUTH-T05',fromScene:null,actionIndexes:[54,55,56],toScene:'r02-24-generating-sequence',result:'Full Count 1 to 1000, one per line. request in bubble; Thinking and square generating control visible',verification:'observed-full-request-and-generating-state',unknowns:['Backend task implementation','Whether 1000 numbers eventually exist']},
 {id:'AUTH-T06',fromScene:'r02-24-generating-sequence',actionIndexes:[57],toScene:'r02-25-stop-result',result:'Settled result card and toolbar visible; composer is Hold to talk',verification:'late-stop-attempt-not-success',stopSuccessVerified:false,recordingVerified:false,unknowns:['Exact instant answer finished','Input target at instant before tap','Any backend cancellation']},
 {id:'AUTH-T07',fromScene:'r02-25-stop-result',actionIndexes:[58,59,60],toScene:'r02-26-checked-stop',result:'Output stopped shown after Retry, fresh generating-state guard and stop tap; Python execution failure also visible',verification:'ui-output-stop-verified',uiStopVerified:true,networkCancellationVerified:false,toolFailure:{text:'Failed to execute Python code',causedByStop:'unknown',timingRelativeToStop:'unknown'},unknowns:['Network/server task cancellation','Billing effect','Late-result handling','Tool error cause']}
].map(t=>({...t,classification:'A for reviewed captured results; controller-only reports explicitly marked',recordedActions:t.actionIndexes.map(i=>{const a=session.actions[i-1];assert.ok(a,`Missing action ${i}`);return {index:i,...a};})}));
const guardPath=`${run}/stop-check-1792121967622.xml`;
const guardXml=fs.readFileSync(path.join(root,guardPath),'utf8');
assert.ok(guardXml.includes('Send messages anytime…'));
assert.ok(guardXml.includes('content-desc="Send message"'));
const output={schemaVersion:1,runId:'run02-20261016',kind:'independent-authenticated-runtime-review',package:'com.moonshot.kimichat',version:'3.1.3 (376)',apkSha256:session.apkSha256,authenticationContext:{mode:'authenticated',basis:'Controller reports user completed normal login; no credential or account-identifier samples read or copied by this review'},sourceSession:`${run}/session.json`,actionIndexBase:1,scope:{reviewedSceneIds:safe.map(x=>x[0]),identityCapturesExcluded:['r02-18-post-login-guide','r02-19-prompt-ready'],deviceControlledByReviewer:false},scenes,transitions,stopGuard:{path:guardPath,sha256:hashFile(guardPath),basis:'Fresh UIA source retained before action 60; runner rejects unless generating placeholder and exactly one ImageView with Send message description exist',matchedText:['Thinking','Send messages anytime…'],candidate:{contentDescription:'Send message',class:'android.widget.ImageView',bounds:'[911,2168][1037,2294]',tapCenterPhysicalPx:[974,2231]},unknowns:['Read-to-click race is reduced, not eliminated']},limitations:['No credentials, default nickname or account identifier captured in this review.','This review does not inspect identity-containing samples 18 or 19; it does not inspect private input-state.png.','Gboard stylus teaching overlay identification is a controller report; its private source image is not copied.','Earlier incomplete Re input may have environmental interference, but a unique causal attribution is not established.','Screenshot and subsequent UIA are non-atomic; sample 20 visibly advances from generating screenshot to completed UIA.','Copy, Share, rating, read-aloud and More-menu contents are not verified by visible entry points.','A stale stop coordinate that toggled voice standby is not a successful stop test.','Output stopped verifies UI output cancellation only; tool error co-occurrence does not establish causality.','Sparse screenshot intervals and tool runtimes are not animation or generation durations.','Local AUTH-T identifiers are research labels, not REA Evidence IDs.']};
fs.writeFileSync(path.join(runDir,'auth-reviewed-transitions.json'),JSON.stringify(output,null,2)+'\n');
console.log(JSON.stringify({safeScenes:scenes.length,transitions:transitions.length,sourceHashes:'all matched',privateIdentityScenesInspected:false}));
