import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {execFileSync} from 'node:child_process';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const read=p=>JSON.parse(fs.readFileSync(path.join(root,p),'utf8').replace(/^\uFEFF/,''));
function walk(dir){return fs.readdirSync(dir,{withFileTypes:true}).flatMap(e=>e.name==='.git'?[]:e.isDirectory()?walk(path.join(dir,e.name)):[path.join(dir,e.name)]);}
const checkedJson=['library.json','evidence/index.json','docs/restore.json','docs/shutdown-verification.json','evidence/runtime/reviewed-scenes.json','evidence/runtime/observed-transitions.json'];
for(const file of checkedJson)read(file);
const missing=[];let links=0;
for(const file of walk(root).filter(x=>x.endsWith('.md'))){
 const source=fs.readFileSync(file,'utf8').replace(/```[\s\S]*?```/g,'');
 for(const m of source.matchAll(/\[[^\]]*\]\(([^\s)]+)\)/g)){
  const href=m[1];if(/^(https?:|mailto:|#)/.test(href))continue;
  const target=decodeURIComponent(href.split('#')[0]);if(!target)continue;links++;
  if(!fs.existsSync(path.resolve(path.dirname(file),target)))missing.push({file:path.relative(root,file),href});
 }
}
if(missing.length)throw Error('Missing local document links: '+JSON.stringify(missing));
const session=read('evidence/runtime/session.json');let screenshots=0,hierarchies=0;
for(const scene of session.scenes){
 if(hash(fs.readFileSync(path.join(root,scene.screenshot.path)))!==scene.screenshot.sha256)throw Error('Changed original screenshot '+scene.id);screenshots++;
 if(scene.hierarchy){if(hash(fs.readFileSync(path.join(root,scene.hierarchy.path)))!==scene.hierarchy.sha256)throw Error('Changed hierarchy '+scene.id);hierarchies++;}
}
const ledger=read('evidence/runtime/observed-transitions.json');
for(const t of ledger.transitions){
 for(const id of [t.fromScene,t.toScene])if(!session.scenes.some(s=>s.id===id))throw Error('Unknown transition scene '+id);
 for(const i of t.actionIndexes)if(!session.actions[i-1])throw Error('Unknown action index '+i);
}
const shutdown=read('docs/shutdown-verification.json');
if(shutdown.serialStillListed||shutdown.remainingOwnedProcesses.length)throw Error('Dedicated Kimi emulator cleanup not yet verified');
const scripts=['android-session.mjs','start-emulator.ps1','finalize-library.mjs','verify-library.mjs'];
for(const script of scripts.filter(x=>x.endsWith('.mjs')))execFileSync(process.execPath,['--check',path.join(root,'tools',script)],{windowsHide:true});
const apk=path.resolve(root,'../../Kimi.apk');let unchangedApk=null;
if(fs.existsSync(apk)){unchangedApk=hash(fs.readFileSync(apk))===session.apkSha256;if(!unchangedApk)throw Error('Original APK changed');}
const result={verifiedAt:new Date().toISOString(),status:'passed',scope:'New integration documents, current screenshot/hierarchy hashes, transition references, dedicated AVD cleanup and changed Node scripts; original runtime behavior beyond documented paths not asserted',jsonFiles:checkedJson,markdownLinksChecked:links,missingLinks:missing,originalScreenshotsVerified:screenshots,hierarchyByteHashesVerified:hierarchies,hierarchySemanticLimits:'Hash verification does not prove fresh/current hierarchy. See reviewed-scenes for null-root and nonatomic exclusions.',transitionsVerified:ledger.transitions.length,originalApkUnchanged:unchangedApk,dedicatedAvdShutdownVerified:true,referenceOrCrossBusinessValidation:false};
fs.writeFileSync(path.join(root,'evidence/static/library-verification.json'),JSON.stringify(result,null,2)+'\n');console.log(JSON.stringify(result,null,2));
