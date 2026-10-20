import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {ROOT,getCatalog,hash} from './catalog.mjs';
import {prepare} from './prepare.mjs';
import {execFileSync} from 'node:child_process';
export function buildPublic({output=path.join(ROOT,'dist')}={}){
 prepare();const target=path.resolve(output);if(!target.startsWith(ROOT+path.sep))throw Error('Build output must stay inside this project');
 const lock=JSON.parse(fs.readFileSync(path.join(ROOT,'catalog/imports.lock.json'),'utf8'));
 const core=['index.html','app.js','logic.js','updates.js','style.css'];
 const allowed=new Set([...core,'data/catalog.json','data/version.json','data/skill-review.md','build-manifest.json',...lock.thumbnails.map(t=>t.target.replace(/^site\//,''))]);
 function existing(dir){for(const e of fs.readdirSync(dir,{withFileTypes:true})){const f=path.join(dir,e.name),relative=path.relative(target,f).replaceAll(path.sep,'/');if(e.isSymbolicLink())throw Error('Symlink in build output');if(e.isDirectory())existing(f);else if(!allowed.has(relative))throw Error('Unknown file in generated output; move it before building: '+relative);}}
 if(fs.existsSync(target))existing(target);else fs.mkdirSync(target,{recursive:true});
 for(const f of core)fs.copyFileSync(path.join(ROOT,'site',f),path.join(target,f));
 for(const t of lock.thumbnails){const dest=path.join(target,t.target.replace(/^site\//,''));fs.mkdirSync(path.dirname(dest),{recursive:true});fs.copyFileSync(path.join(ROOT,t.target),dest);}
 fs.mkdirSync(path.join(target,'data'),{recursive:true});const data=getCatalog({staticMode:true});
 const content=JSON.stringify(data),snapshotId=hash(Buffer.from(content)),appVersion=hash(Buffer.concat(core.map(f=>fs.readFileSync(path.join(ROOT,'site',f)))));let revision=process.env.GITHUB_SHA||null;
 fs.writeFileSync(path.join(target,'app.js'),fs.readFileSync(path.join(ROOT,'site/app.js'),'utf8').replace('__BUILD_APP_VERSION__',appVersion));
 if(!revision)try{revision=execFileSync('git',['rev-parse','HEAD'],{cwd:ROOT,encoding:'utf8',windowsHide:true}).trim();}catch{}
 data.publication={snapshotId,appVersion,revision,builtAt:new Date().toISOString()};
 const catalogBytes=Buffer.from(JSON.stringify(data,null,2)+'\n');fs.writeFileSync(path.join(target,'data/catalog.json'),catalogBytes);
 fs.writeFileSync(path.join(target,'data/version.json'),JSON.stringify({schemaVersion:1,...data.publication,catalogSha256:hash(catalogBytes),sourceCount:data.sources.length},null,2)+'\n');
 fs.copyFileSync(path.join(ROOT,'docs/skill-review.md'),path.join(target,'data/skill-review.md'));
 const files=[];for(const relative of allowed){if(relative==='build-manifest.json')continue;const bytes=fs.readFileSync(path.join(target,relative));files.push({path:relative,bytes:bytes.length,sha256:hash(bytes)});}
 const report={schemaVersion:1,createdAt:new Date().toISOString(),mode:'public-catalog-only',sourceCount:data.sources.length,privateContentsIncluded:false,rawEvidenceIncluded:false,sourceAppRuntimeProven:false,files};
 fs.writeFileSync(path.join(target,'build-manifest.json'),JSON.stringify(report,null,2)+'\n');return {status:'built',sources:data.sources.length,files:files.length,rawEvidenceIncluded:false,output:path.relative(ROOT,target).replaceAll(path.sep,'/')};
}
if(process.argv[1]&&path.resolve(process.argv[1])===fileURLToPath(import.meta.url))console.log(JSON.stringify(buildPublic()));
