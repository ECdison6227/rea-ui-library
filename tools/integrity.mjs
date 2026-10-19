import fs from 'node:fs';
import path from 'node:path';
import {ROOT,contained,hash} from './catalog.mjs';
export function inventory(root){
 const files=[];
 function visit(dir){for(const e of fs.readdirSync(dir,{withFileTypes:true})){
  if(e.name==='.git')throw Error('Nested Git metadata must be removed after backup: '+dir);
  if(['node_modules','.runtime','private'].includes(e.name))throw Error('Remove local/private runtime data before importing: '+path.join(dir,e.name));
  const f=path.join(dir,e.name);if(e.isSymbolicLink())throw Error('Study symlink requires explicit review: '+f);
  if(e.isDirectory())visit(f);else if(e.isFile()){const b=fs.readFileSync(f);files.push({path:path.relative(root,f).replaceAll(path.sep,'/'),bytes:b.length,sha256:hash(b)});}
 }}visit(root);files.sort((a,b)=>a.path<b.path?-1:a.path>b.path?1:0);return files;
}
export const treeHash=files=>hash(Buffer.from(JSON.stringify(files)));
export function lockStudy(s){
 const files=inventory(contained(ROOT,s.root));if(!files.length)throw Error('Empty study: '+s.id);
 const manifest='catalog/manifests/'+s.id+'.json',data={schemaVersion:1,id:s.id,root:s.root,treeSha256:treeHash(files),files};
 fs.mkdirSync(path.join(ROOT,'catalog/manifests'),{recursive:true});fs.writeFileSync(path.join(ROOT,manifest),JSON.stringify(data,null,2)+'\n');
 return {manifest,files:files.length,treeSha256:data.treeSha256};
}
export function verifyStudy(s){
 if(!s.integrity)throw Error('Missing integrity manifest: '+s.id);
 const m=JSON.parse(fs.readFileSync(contained(ROOT,s.integrity.manifest),'utf8'));
 if(m.schemaVersion!==1||m.id!==s.id||m.root!==s.root||m.files.length!==s.integrity.files||treeHash(m.files)!==m.treeSha256||m.treeSha256!==s.integrity.treeSha256)throw Error('Invalid manifest: '+s.id);
 const actual=inventory(contained(ROOT,s.root));if(treeHash(actual)!==m.treeSha256)throw Error('Study files changed: '+s.id+'; review before explicitly running study:lock');
 return {id:s.id,files:actual.length,treeSha256:m.treeSha256};
}
