import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {inflateRawSync} from 'node:zlib';

/** Categories describe an exclusion reason, never an inferred user identity. */
export function sourceSceneDisposition(id,note){
 const status=String(note?.privacy?.status||'');
 const privateSource=/private/i.test(id)||/identity|history|credentials|personal-avatar|qr-code/i.test(status)||note?.privacy?.identityVisible===true||note?.privacy?.privateHistoryVisible===true||note?.privacy?.credentialsVisible===true;
 const withheld=note?.distribution==='withhold'||privateSource;
 const incomplete=!privateSource&&(/incomplete|missing|unavailable/i.test(status)||/unavailable|missing/i.test(String(note?.uiaReview||'')));
 return {withheld,private:privateSource,incomplete:withheld&&incomplete,otherWithheld:withheld&&!privateSource&&!incomplete};
}

/** A portable declared-content policy. Loading it never opens excluded images. */
export function loadRun03DistributionPolicy(root){
 const runId='run03-20261016',run=`evidence/runtime/${runId}`,dRoot=`evidence/reference/${runId}`;
 const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
 const absolute=relative=>{if(typeof relative!=='string'||relative.includes('\\')||relative.split('/').includes('..'))throw Error('Unsafe distribution path');const p=path.resolve(root,relative);if(!p.startsWith(root+path.sep))throw Error('Distribution path outside research root');return p;};
 const bytes=relative=>fs.readFileSync(absolute(relative)),read=relative=>JSON.parse(bytes(relative).toString('utf8').replace(/^\uFEFF/,'')),exists=relative=>fs.existsSync(absolute(relative));
 const exactFiles=new Map(),sourceIds=new Set(),referenceIds=new Set(),motionIds=new Set(),sourceOmissions=new Map(),auxiliaryOmissions=new Map();
 function add(relative,sha256,group,expectedBytes){
  if(!/^[a-f0-9]{64}$/.test(sha256||''))throw Error('Missing exact SHA: '+relative);
  const p=absolute(relative),previous=exactFiles.get(relative);if(previous&&previous.sha256!==sha256)throw Error('Conflicting distribution hash: '+relative);
  exactFiles.set(relative,{sha256,group,bytes:expectedBytes??(fs.existsSync(p)?fs.statSync(p).size:null)});
 }
 function addCurrent(relative,group='review-metadata'){add(relative,hash(bytes(relative)),group);}
 const safe=read('docs/run03-safe-scenes.json'),audit=read('docs/run03-evidence-audit.json'),notes=new Map(audit.scenes.map(n=>[n.id,n]));
 if(notes.size!==audit.scenes.length)throw Error('Duplicate source review IDs');
 if(safe.runId!==runId||audit.runId!==runId||safe.auditSource!=='docs/run03-evidence-audit.json'||safe.auditSourceSha256!==hash(bytes(safe.auditSource)))throw Error('Stale/wrong exact source distribution audit');
 if(safe.integrityFailures?.length)throw Error('Source distribution audit reports integrity failures');
 addCurrent('docs/run03-safe-scenes.json');add('docs/run03-evidence-audit.json',safe.auditSourceSha256,'review-metadata');
 for(const scene of safe.scenes){
  const note=notes.get(scene.id);if(!/^[a-z0-9][a-z0-9-]{0,95}$/.test(scene.id)||sourceSceneDisposition(scene.id,note).withheld||note?.distribution!=='allow'||note.privacy?.status!=='clear'||['pixelReview','uiaReview','captureReview'].some(k=>note[k]!=='reviewed'))throw Error('Source scene is not fully reviewed: '+scene.id);
  if(sourceIds.has(scene.id))throw Error('Duplicate approved source scene');sourceIds.add(scene.id);
  for(const [kind,name,noteHash]of [['screenshot','screen.png','exactScreenshotSha256'],['hierarchy','hierarchy.xml','exactHierarchySha256'],['capture','capture.json','exactCaptureSha256']]){
   const relative=`${run}/scenes/${scene.id}/${name}`;if(scene[kind]?.path!==relative||scene[kind].sha256!==note[noteHash])throw Error('Source exact byte declaration differs: '+scene.id);add(relative,scene[kind].sha256,'source');
  }
 }
 if(sourceIds.size!==safe.safeSceneIds.length||safe.safeSceneIds.some(id=>!sourceIds.has(id)))throw Error('Safe source IDs differ from exact declarations');
 for(const omission of safe.withheld??[]){
  const note=notes.get(omission.id);if(omission.sourceFilesExcluded!==true||note?.distribution!=='withhold'||sourceIds.has(omission.id)||sourceOmissions.has(omission.id))throw Error('Invalid/duplicate withheld omission declaration');
  sourceOmissions.set(omission.id,{category:sourceSceneDisposition(omission.id,note).private?'private':sourceSceneDisposition(omission.id,note).incomplete?'incomplete':'other-withheld',reason:omission.reason});
 }
 for(const omission of safe.pending??[]){if(sourceIds.has(omission.id)||sourceOmissions.has(omission.id))throw Error('Conflicting pending omission declaration');sourceOmissions.set(omission.id,{category:'unreviewed',reason:omission.reason});}
 if(exists('docs/run03-distribution-omissions.json')){
  const manifest=read('docs/run03-distribution-omissions.json');if(manifest.schemaVersion!==1||manifest.runId!==runId||manifest.defaultAllow!==false)throw Error('Wrong auxiliary omission declaration');addCurrent('docs/run03-distribution-omissions.json');
  for(const omission of manifest.omissions){
   if(!new RegExp('^'+run+'/(?:upload-check|stop-check)-[0-9]+\\.xml$').test(omission.path)||!/^[a-f0-9]{64}$/.test(omission.sha256||'')||omission.distribution!=='exclude-local-only'||omission.category!=='unreviewed-auxiliary'||auxiliaryOmissions.has(omission.path))throw Error('Invalid/duplicate auxiliary local-only omission');
   auxiliaryOmissions.set(omission.path,{track:'auxiliary-local-only',category:omission.category,sha256:omission.sha256,bytes:omission.bytes,reason:omission.reason});
  }
 }
 let referenceSafe=null;
 if(exists('docs/native-reference-safe-scenes.json')){
  referenceSafe=read('docs/native-reference-safe-scenes.json');if(referenceSafe.runId!==runId||referenceSafe.classification!=='D')throw Error('Wrong D distribution review identity');addCurrent('docs/native-reference-safe-scenes.json');
  for(const scene of referenceSafe.scenes){
   if(scene.pixelReview!=='reviewed'||scene.privacyReview!=='clear'||!/^[a-z0-9][a-z0-9-]{0,95}$/.test(scene.id)||referenceIds.has(scene.id))throw Error('D exact approval is missing/duplicate');referenceIds.add(scene.id);
   for(const [kind,name]of [['screenshot','screen.png'],['hierarchy','hierarchy.xml'],['capture','capture.json']]){const relative=`${dRoot}/scenes/${scene.id}/${name}`;if(scene[kind]?.path!==relative)throw Error('D approved path differs');add(relative,scene[kind].sha256,'reference');}
  }
 }
 let motionSafe=null;
 if(exists('docs/run03-motion-safe-files.json')){
  motionSafe=read('docs/run03-motion-safe-files.json');if(motionSafe.runId!==runId||motionSafe.defaultAllow!==false||motionSafe.package!=='com.moonshot.kimichat')throw Error('Wrong/default-allow motion policy');addCurrent('docs/run03-motion-safe-files.json');
  if(motionSafe.researchIndex.path!=='docs/run03-motion-research.md')throw Error('Wrong motion research index');add(motionSafe.researchIndex.path,motionSafe.researchIndex.sha256,'review-metadata');
  for(const item of motionSafe.recordings){if(item.classification!=='A'||item.privacyReview!=='clear'||item.distribution!=='allow-reviewed-exact-files'||!item.allDecodedFramesReviewed||motionIds.has(item.id))throw Error('Unreviewed/duplicate motion recording');motionIds.add(item.id);}
  const motionPaths=new Set();for(const item of motionSafe.files){
   const base=`${run}/motion/${item.recordingId}/`,suffix=item.path?.slice(base.length);
   if(!motionIds.has(item.recordingId)||!item.path.startsWith(base)||!/^(screenrecord\.mp4|capture\.json|review\.json|guard\.(png|xml)|browser-decoded\/frame-index\.json|browser-decoded\/frame-\d{4}\.png|review-sheets\/sheet-\d+\.png)$/.test(suffix)||item.reviewStatus!=='reviewed'||item.privacyReview!=='clear')throw Error('Unreviewed/non-exact motion file');
   if(motionPaths.has(item.path))throw Error('Duplicate exact motion file declaration');motionPaths.add(item.path);add(item.path,item.sha256,'motion',item.bytes);
  }
  if(motionSafe.counts.recordings!==motionIds.size||motionSafe.counts.files!==motionPaths.size)throw Error('Motion policy counts differ');
 }
 if(exists('reference/build/build-result.json')){const build=read('reference/build/build-result.json');if(build.classification!=='D'||build.package!=='org.research.kimistyle'||build.apk!=='reference/build/native-reference.apk')throw Error('Wrong independent APK declaration');add(build.apk,build.sha256,'artifact',build.bytes);addCurrent('reference/build/build-result.json');}
 if(exists('run03.html')){const gallery=read(run+'/gallery-verification.json');add('run03.html',gallery.htmlSha256,'review-metadata');addCurrent(run+'/gallery-verification.json');}
 function omitted(relative){
  if(auxiliaryOmissions.has(relative))return auxiliaryOmissions.get(relative);
  let match=new RegExp('^'+run+'/scenes/([^/]+)/(?:screen\\.png|hierarchy\\.xml|capture\\.json)$').exec(relative);
  if(match)return sourceOmissions.get(match[1])?{track:'source',id:match[1],...sourceOmissions.get(match[1])}:null;
  match=new RegExp('^'+dRoot+'/scenes/([^/]+)/(?:screen\\.png|hierarchy\\.xml|capture\\.json)$').exec(relative);
  if(match&&referenceSafe?.defaultPreviewAllowed===false&&!referenceIds.has(match[1]))return {track:'reference',id:match[1],category:'unreviewed-reference',reason:'D allowlist explicitly defaults to exclude unlisted scenes'};
  if(/^evidence\/runtime\/run02-20261016\/scenes\/r02-(18-post-login-guide|19-prompt-ready)\/(screen\.png|hierarchy\.xml|capture\.json)$/.test(relative))return {track:'historical-source',category:'private',reason:'Explicit retained run02 sharing-scope exclusion'};
  return null;
 }
 return {runId,run,dRoot,safe,audit,notes,sourceIds,referenceIds,motionIds,sourceOmissions,auxiliaryOmissions,referenceSafe,motionSafe,exactFiles,omitted};
}

/** Verify actual ZIP payload hashes without extracting or executing members. */
export function auditRun03Zip(zip,policy,check){
 const hash=b=>crypto.createHash('sha256').update(b).digest('hex');let end=-1;
 for(let at=zip.length-22;at>=Math.max(0,zip.length-65557);at--)if(zip.readUInt32LE(at)===0x06054b50&&at+22+zip.readUInt16LE(at+20)===zip.length){end=at;break;}
 if(end<0)throw Error('ZIP end record missing');
 if(zip.readUInt16LE(end+4)!==0||zip.readUInt16LE(end+6)!==0)throw Error('Split ZIP unsupported');
 const total=zip.readUInt16LE(end+10),offset=zip.readUInt32LE(end+16);if(total===65535||offset===0xffffffff)throw Error('ZIP64 requires a ZIP64-capable verifier');
 const prefix='kimi-android/',names=new Set(),caseNames=new Set(),groups={source:0,reference:0,motion:0,artifact:0,'review-metadata':0};let cursor=offset,exactPayloadHashesChecked=0;
 function rejectPath(relative,directory){
  if(/(?:^|\/)(?:\.runtime|\.git|\.gradle|node_modules|android-user|avd|[^/]+\.avd|\.private)(?:\/|$)/i.test(relative)||/\.(img|qcow2|vmdk|vdi|class|dex|jar|keystore|jks|key|pem|pfx|p12|idsig|lock|tmp|bak|log)$/i.test(relative))return true;
  if(/\.apk$/i.test(relative)&&relative!=='reference/build/native-reference.apk')return true;
  if(relative.startsWith('reference/build/')&&!directory&&!['reference/build/native-reference.apk','reference/build/build-result.json'].includes(relative))return true;
  if(/^evidence\/runtime\/run02-20261016\/scenes\/r02-(18-post-login-guide|19-prompt-ready)\//.test(relative))return true;
  for(const [base,ids,group]of [[policy.run+'/scenes/',policy.sourceIds,'source'],[policy.dRoot+'/scenes/',policy.referenceIds,'reference'],[policy.run+'/motion/',policy.motionIds,'motion']]){
   if(!relative.startsWith(base))continue;
   if(directory&&relative===base)return false;
   const id=relative.slice(base.length).split('/')[0];if(directory)return !ids.has(id);
   return policy.exactFiles.get(relative)?.group!==group;
  }
  if(relative.startsWith(policy.run+'/')&&!directory&&!new RegExp('^'+policy.run+'/[^/]+\\.json$').test(relative))return true;
  if(/^evidence\/runtime\/run[^/]+\/scenes\//.test(relative)&&!relative.startsWith('evidence/runtime/run02-20261016/scenes/'))return true;
  return false;
 }
 for(let i=0;i<total;i++){
  if(cursor+46>zip.length||zip.readUInt32LE(cursor)!==0x02014b50)throw Error('Malformed ZIP directory');
  const flags=zip.readUInt16LE(cursor+8),method=zip.readUInt16LE(cursor+10),packed=zip.readUInt32LE(cursor+20),raw=zip.readUInt32LE(cursor+24),nameLength=zip.readUInt16LE(cursor+28),extra=zip.readUInt16LE(cursor+30),comment=zip.readUInt16LE(cursor+32),local=zip.readUInt32LE(cursor+42);
  if(cursor+46+nameLength+extra+comment>zip.length)throw Error('Truncated ZIP directory member');
  const name=zip.toString('utf8',cursor+46,cursor+46+nameLength);cursor+=46+nameLength+extra+comment;
  check(name.startsWith(prefix)&&!name.includes('\\')&&!name.startsWith('/')&&!name.split('/').includes('..')&&!/^[A-Za-z]:/.test(name),'Unsafe ZIP entry path');
  check(!caseNames.has(name.toLowerCase()),'Duplicate ZIP entry');names.add(name);caseNames.add(name.toLowerCase());
  const relative=name.startsWith(prefix)?name.slice(prefix.length):name,directory=name.endsWith('/');check(!rejectPath(relative,directory),'ZIP contains private, incomplete, unreviewed or temporary path: '+relative);
  if(directory)continue;
  const descriptor=policy.exactFiles.get(relative);if(!descriptor)continue;
  check(!(flags&1),'Encrypted exact ZIP member is unsupported: '+relative);
  if(local+30>zip.length||zip.readUInt32LE(local)!==0x04034b50)throw Error('ZIP local member header missing');
  const localFlags=zip.readUInt16LE(local+6),localMethod=zip.readUInt16LE(local+8),localNameLength=zip.readUInt16LE(local+26),localExtra=zip.readUInt16LE(local+28),dataOffset=local+30+localNameLength+localExtra;
  const localName=zip.toString('utf8',local+30,local+30+localNameLength);check(localName===name&&method===localMethod&&flags===localFlags,'ZIP central/local member identity differs');
  if(dataOffset+packed>zip.length||raw>256*1024*1024)throw Error('ZIP exact member exceeds bounded payload');
  check(descriptor.bytes===null||descriptor.bytes===undefined||raw===descriptor.bytes,'ZIP exact member declared length differs: '+relative);
  if(flags&1)continue;
  let content;try{const data=zip.subarray(dataOffset,dataOffset+packed);content=method===0?data:method===8?inflateRawSync(data,{maxOutputLength:Math.min(raw+1,256*1024*1024)}):null;}catch(error){check(false,'ZIP exact member cannot decompress: '+relative);continue;}
  if(content===null){check(false,'Unsupported compression for exact ZIP member: '+relative);continue;}
  check(content.length===raw&&hash(content)===descriptor.sha256,'ZIP exact member bytes/hash differ: '+relative);exactPayloadHashesChecked++;groups[descriptor.group]=(groups[descriptor.group]??0)+1;
 }
 const missing=[];for(const relative of policy.exactFiles.keys())if(!names.has(prefix+relative)){missing.push(relative);check(false,'ZIP omits required exact public artifact: '+relative);}
 for(const relative of ['README.md','docs/remaining-validation.json',policy.run+'/session.json'])check(names.has(prefix+relative),'ZIP is missing required research entry/metadata: '+relative);
 return {checked:true,sha256:hash(zip),entries:total,exactPayloadHashesChecked,exactPayloadGroups:groups,missingExactEntries:missing,run03SceneFilesChecked:groups.source,independentDSceneFilesChecked:groups.reference,motionFilesChecked:groups.motion,policy:'Actual ZIP local headers and stored/deflated payload SHA-256 checked against exact A scene, D scene, motion and build declarations. Withheld/private/incomplete, unreviewed scenes, unknown motion, original APK, AVD, keys and intermediate build paths rejected. Other static library files retain the stated scope; renamed arbitrary contents are not automatically privacy-audited.'};
}
