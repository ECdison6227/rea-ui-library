import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {ROOT,hash,contained} from './catalog.mjs';
export function checkSkillSync(){
 const lock=JSON.parse(fs.readFileSync(path.join(ROOT,'skills/source.lock.json'),'utf8')),errors=[];
 if(lock.formatVersion!==1||lock.skill!=='rea-design-transfer'||!/^[a-f0-9]{40}$/.test(lock.revision||'')||lock.vendorPath!=='skills/rea-design-transfer'||lock.sourcePath!=='skill/rea-design-transfer'||lock.repository!=='https://github.com/ECdison6227/rea-style-library')errors.push('Invalid upstream skill lock');
 const base=contained(ROOT,lock.vendorPath),expected=new Set();
 for(const f of lock.files||[]){try{if(expected.has(f.path))throw Error('Duplicate locked path');expected.add(f.path);const file=contained(base,f.path),b=fs.readFileSync(file);if(b.length!==f.bytes||hash(b)!==f.sha256)errors.push('Vendored skill differs: '+f.path);}catch(e){errors.push(e.message);}}
 const actual=[];function walk(dir){for(const e of fs.readdirSync(dir,{withFileTypes:true})){if(e.name.startsWith('_')||e.name.startsWith('.'))continue;const f=path.join(dir,e.name);if(e.isSymbolicLink())errors.push('Unexpected skill symlink');else if(e.isDirectory())walk(f);else actual.push(path.relative(base,f).replaceAll(path.sep,'/'));}}walk(base);
 if(!expected.size||actual.length!==expected.size||actual.some(f=>!expected.has(f)))errors.push('Vendored skill inventory differs');
 return{status:errors.length?'failed':'passed',revision:lock.revision,files:expected.size,networkAccessed:false,errors};
}
if(process.argv[1]&&path.resolve(process.argv[1])===fileURLToPath(import.meta.url)){const r=checkSkillSync();console.log(JSON.stringify(r,null,2));if(r.errors.length)process.exitCode=1;}
