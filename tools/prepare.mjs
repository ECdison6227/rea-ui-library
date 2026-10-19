import fs from 'node:fs';
import path from 'node:path';
import {execFileSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
import {ROOT,definitions,validateDefinitions,contained,hash} from './catalog.mjs';
export function prepare(){
 const data=definitions(),errors=validateDefinitions(data);if(errors.length)throw Error(errors.join('\n'));
 const thumbnails=[],sources=[];fs.mkdirSync(path.join(ROOT,'site/assets/thumbs'),{recursive:true});
 for(const s of data.sources){
  if(s.revision){const actual=execFileSync('git',['-C',path.join(ROOT,s.root),'rev-parse','HEAD'],{encoding:'utf8',windowsHide:true}).trim();if(actual!==s.revision)throw Error('Source Git revision changed: '+s.id);}
  sources.push({id:s.id,path:s.root,url:s.sourceUrl,revision:s.revision,snapshot:s.snapshot||null,origin:'historical',workingTreeMutationPerformed:false});
  if(s.thumbnail){const source=contained(contained(ROOT,s.root),s.thumbnail.path),bytes=fs.readFileSync(source),target='site/assets/thumbs/'+s.id+path.extname(source);fs.writeFileSync(path.join(ROOT,target),bytes);thumbnails.push({id:s.id,source:s.root+'/'+s.thumbnail.path,target,sha256:hash(bytes),classification:s.thumbnail.classification,reviewed:true,transformed:false});}
 }
 fs.writeFileSync(path.join(ROOT,'catalog/imports.lock.json'),JSON.stringify({schemaVersion:1,sources,thumbnails},null,2)+'\n');
 return {sources:sources.length,thumbnails:thumbnails.length};
}
if(process.argv[1]&&path.resolve(process.argv[1])===fileURLToPath(import.meta.url))console.log(JSON.stringify(prepare()));
