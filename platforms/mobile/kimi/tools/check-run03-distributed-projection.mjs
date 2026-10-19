import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {spawnSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
import {loadRun03DistributionPolicy} from './run03-distribution-policy.mjs';

// Consumer-shape filesystem test, not an archive and not a device operation.
// Mutable text metadata is copied; approved immutable image/video/APK files may
// be hard-linked. The consumer verifier runs --check and cannot write through.
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const workspace=path.resolve(root,'../..'),privateRoot=path.join(workspace,'.runtime/distribution-verifier-check');
const container=path.join(privateRoot,crypto.randomUUID()),staged=path.join(container,'kimi-android');
const policy=loadRun03DistributionPolicy(root);
let included=0,excluded=0,report;
function allowed(relative){
 if(/(?:^|\/)(?:\.git|\.runtime|\.gradle|node_modules|android-user|avd|[^/]+\.avd|\.private)(?:\/|$)/i.test(relative)||/\.(img|qcow2|vmdk|vdi|class|dex|jar|keystore|jks|key|pem|pfx|p12|idsig|lock|tmp|bak|log)$/i.test(relative))return false;
 if(/\.apk$/i.test(relative)&&relative!=='reference/build/native-reference.apk')return false;
 if(relative.startsWith('reference/build/')&&!['reference/build/native-reference.apk','reference/build/build-result.json'].includes(relative))return false;
 if(/^evidence\/runtime\/run02-20261016\/scenes\/r02-(18-post-login-guide|19-prompt-ready)\//.test(relative))return false;
 if([policy.run+'/scenes/',policy.dRoot+'/scenes/',policy.run+'/motion/'].some(prefix=>relative.startsWith(prefix)))return policy.exactFiles.has(relative);
 if(relative.startsWith(policy.run+'/')&&!new RegExp('^'+policy.run+'/[^/]+\\.json$').test(relative))return false;
 if(/^evidence\/runtime\/(emulator-|launch-)/.test(relative))return false;
 return true;
}
function copyTree(directory){
 for(const item of fs.readdirSync(directory,{withFileTypes:true})){
  const absolute=path.join(directory,item.name),relative=path.relative(root,absolute).replaceAll(path.sep,'/');
  if(item.isSymbolicLink()){excluded++;continue;}
  if(item.isDirectory()){if(/(?:^|\/)(?:\.git|\.runtime|\.gradle|node_modules|\.private)(?:\/|$)/.test(relative))continue;copyTree(absolute);continue;}
  if(!item.isFile())continue;
  if(!allowed(relative)){excluded++;continue;}
  const destination=path.join(staged,relative);fs.mkdirSync(path.dirname(destination),{recursive:true});
  if(/\.(png|mp4|apk|woff2?|ttf|otf|docx|pdf)$/.test(relative)){try{fs.linkSync(absolute,destination);}catch{fs.copyFileSync(absolute,destination);}}
  else fs.copyFileSync(absolute,destination);
  included++;
 }
}
try{
 fs.mkdirSync(staged,{recursive:true});copyTree(root);
 const result=spawnSync(process.execPath,[path.join(staged,'tools/verify-run03.mjs'),'--distributed','--check'],{encoding:'utf8',windowsHide:true,maxBuffer:24*1024*1024});
 let verification;try{verification=JSON.parse(result.stdout);}catch{throw Error('Consumer verifier did not return JSON: '+result.stderr);}
 report={status:result.status===0?'passed-consumer-shape-preflight':'failed-consumer-shape-preflight',archiveCreated:false,adbCalled:false,includedFiles:included,excludedFiles:excluded,externalOriginalApkPresent:fs.existsSync(path.resolve(staged,'../../Kimi.apk')),sourceOmissions:verification.distribution?.sourceOmissions,excludedRawPresentInProjection:verification.distribution?.looseWorkspaceContainsExcludedRawFiles,originalApkBytesChecked:verification.source?.originalApkBytesChecked,sourcePngHashesChecked:verification.source?.screenshotHashesChecked,DFramesValidated:verification.nativeIndependent?.runtimeFramesValidated,motionAndReviewHashesChecked:verification.distribution?.motionAndReviewHashesChecked,warnings:verification.warnings,errors:verification.errors};
 if(result.status!==0)process.exitCode=1;
}finally{
 // Verify the exact resolved task-owned target before recursive cleanup on Windows.
 const target=path.resolve(container);if(path.dirname(target)!==path.resolve(privateRoot)||!target.startsWith(path.resolve(workspace)+path.sep))throw Error('Unsafe projection cleanup target');
 fs.rmSync(target,{recursive:true,force:true,maxRetries:3,retryDelay:100});
}
console.log(JSON.stringify(report,null,2));
