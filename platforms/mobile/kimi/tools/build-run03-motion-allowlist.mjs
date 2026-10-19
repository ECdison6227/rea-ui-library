import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
// Offline finalization of explicitly inspected recordings only. Never discovers
// or auto-approves another clip. Original media/capture/frame files are read-only.
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const runId='run03-20261016',prefix='evidence/runtime/'+runId+'/motion';
const approved=[{id:'concurrency-01',sha256:'f3179706767794ba48dcb27c861b8cbe4967a43a9018277e85a10a6a55f3f246',frames:63},{id:'motion-01',sha256:'658942ddf94449eb6d30d4f87abd3f18265a7693cbbb6dc6b1ebf30ff0096340',frames:28}];
const sha=b=>crypto.createHash('sha256').update(b).digest('hex');
const bytes=p=>fs.readFileSync(path.join(root,p));
const read=p=>JSON.parse(bytes(p).toString('utf8').replace(/^\uFEFF/,''));
const files=[],recordings=[],seen=new Set();
function entry(recordingId,relative,kind,classification,expected){const b=bytes(relative),actual=sha(b);if(expected&&actual!==expected)throw Error('Reviewed bytes changed: '+relative);if(seen.has(relative))throw Error('Duplicate approved motion path');seen.add(relative);const value={recordingId,path:relative,bytes:b.length,sha256:actual,kind,classification,reviewStatus:'reviewed',privacyReview:'clear'};files.push(value);return {path:relative,bytes:b.length,sha256:actual};}
for(const spec of approved){
 const base=prefix+'/'+spec.id,reviewPath=base+'/review.json',review=read(reviewPath),indexPath=base+'/browser-decoded/frame-index.json',index=read(indexPath),capturePath=base+'/capture.json',capture=read(capturePath);
 if(review.id!==spec.id||review.source.sha256!==spec.sha256||sha(bytes(indexPath))!==review.source.frameIndexSha256||sha(bytes(capturePath))!==review.source.captureSha256)throw Error('Recording review/source binding changed');
 if(index.demux.sampleCount!==spec.frames||index.frames.length!==spec.frames||!review.pixelReview.everyDecodedFrameReviewedInContactSheets)throw Error('Full video-frame review is incomplete');
 if(capture.classification!=='A'||capture.package!=='com.moonshot.kimichat'||capture.runId!==runId||capture.serial!=='emulator-5596'||capture.avd!=='rea_kimi_ui_20261015')throw Error('Original recording identity changed');
 const clipSha=typeof capture.video==='string'?capture.sha256:capture.video.sha256;if(clipSha!==spec.sha256||index.source.sha256!==spec.sha256)throw Error('Original MP4 registration differs');
 review.distribution={status:'allow-reviewed-exact-files',currentlyPackaged:false,allowlist:'docs/run03-motion-safe-files.json',reason:'Only exact approved bytes may be packaged. New recordings, altered files, logs, HTML intermediates and browser profiles remain excluded. No archive is created by this script.'};
 review.privacy.distributionReview='clear';review.metadataReview={capture:'all fields reviewed; source task/device/timing metadata only',decoderIndex:'browser/codec/sample/PTS/hash metadata only; no user browser profile or account data'};
 if(spec.id==='motion-01'){
  const pngSha=sha(bytes(base+'/guard.png')),xmlSha=sha(bytes(base+'/guard.xml'));
  if(pngSha!==capture.guardArtifacts.screenshotSha256||xmlSha!==capture.guardArtifacts.hierarchySha256)throw Error('Original guard bytes changed');
  review.guardReview={status:'reviewed',privacyReview:'clear',screenshotSha256:pngSha,hierarchySha256:xmlSha,atomic:false,observation:'Original-size guard PNG plus every nonempty guard.xml text/description inspected: only own C/D test output, generic model/control labels and original package. No identity greeting, avatar, QR or private history.'};
 }
 fs.writeFileSync(path.join(root,reviewPath),JSON.stringify(review,null,2)+'\n');
 const video=entry(spec.id,base+'/screenrecord.mp4','original-runtime-video','A',spec.sha256),captureFile=entry(spec.id,capturePath,'source-capture-metadata','A',review.source.captureSha256),reviewFile=entry(spec.id,reviewPath,'independent-review','A observations and bounded conclusions'),frameIndex=entry(spec.id,indexPath,'decoded-pts-index','derived-from-A',review.source.frameIndexSha256);
 for(const frame of index.frames){if(!/^frame-\d{4}\.png$/.test(frame.file))throw Error('Unexpected decoded frame filename');entry(spec.id,base+'/browser-decoded/'+frame.file,'decoded-frame-pixels','derived-from-A',frame.sha256);}
 const reviewedNumbers=new Set();for(const sheet of review.pixelReview.contactSheets){if(!sheet.path.startsWith(base+'/review-sheets/')||!/^sheet-\d+\.png$/.test(path.posix.basename(sheet.path)))throw Error('Unexpected reviewed contact-sheet path');entry(spec.id,sheet.path,'derived-contact-sheet','derived-from-A',sheet.sha256);for(const number of sheet.frameNumbers)reviewedNumbers.add(number);}
 if(index.frames.some(f=>!reviewedNumbers.has(f.number)))throw Error('Contact-sheet review does not cover every frame');
 if(spec.id==='motion-01'){entry(spec.id,base+'/guard.png','original-runtime-guard-pixels','A',capture.guardArtifacts.screenshotSha256);entry(spec.id,base+'/guard.xml','original-runtime-guard-uia','A',capture.guardArtifacts.hierarchySha256);}
 recordings.push({id:spec.id,classification:'A',package:'com.moonshot.kimichat',video,capture:captureFile,review:reviewFile,frameIndex,sourceVideoSamples:spec.frames,decodedFrames:spec.frames,allDecodedFramesReviewed:true,privacyReview:'clear',distribution:'allow-reviewed-exact-files',animationDuration:null,easing:null,interruptionVerified:false});
}
const researchIndex='docs/run03-motion-research.md';
const result={schemaVersion:1,runId,generatedAt:new Date().toISOString(),reviewer:'native_reference independent offline worker',package:'com.moonshot.kimichat',defaultAllow:false,policy:'Only explicitly reviewed exact paths/bytes. Source video and raw capture are unchanged. No logs, HTML intermediates, browser caches, new recordings or wildcard approval.',researchIndex:{path:researchIndex,sha256:sha(bytes(researchIndex))},recordings,files,counts:{recordings:recordings.length,files:files.length,bytes:files.reduce((sum,f)=>sum+f.bytes,0)}};
fs.writeFileSync(path.join(root,'docs/run03-motion-safe-files.json'),JSON.stringify(result,null,2)+'\n');console.log(JSON.stringify({status:'wrote-exact-reviewed-motion-allowlist',...result.counts,archiveCreated:false,adbCalled:false},null,2));
