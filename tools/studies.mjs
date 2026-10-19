import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {ROOT,definitions,validateDefinitions} from './catalog.mjs';
import {lockStudy,verifyStudy} from './integrity.mjs';
import {validateDesignPackage} from '../skills/rea-design-transfer/scripts/design-package.mjs';
import {checkConsumerPackage} from './consumer-contract.mjs';
export function addStudy(profile){
 const data=definitions(),s=structuredClone(profile);
 if(data.sources.some(x=>x.id===s.id||x.root===s.root))throw Error('Study id/root already registered');
 if(!s.designPackage)throw Error('New studies require Design Package v1 and usable style/motion/interaction guidance; legacy registration is closed.');
 if(!s.provenance?.target||!s.provenance?.researchEntry||!s.provenance?.method)throw Error('A traceable target, REA method and research entry are required');
 s.integrity={manifest:'catalog/manifests/'+s.id+'.json',files:1,treeSha256:'0'.repeat(64)};
 const candidate={...data,sources:[...data.sources,s]},errors=validateDefinitions(candidate);if(errors.length)throw Error(errors.join('\n'));
 if(!s.artifacts.some(a=>a.path===s.provenance.researchEntry&&a.role==='readme')||!s.artifacts.some(a=>a.role==='spec'))throw Error('Research entry and specification artifacts are required');
 if(s.designPackage){const file=path.join(ROOT,s.root,s.designPackage),result=validateDesignPackage(file,{forCatalog:true});if(result.errors.length)throw Error('Design Package: '+result.errors.join('\n'));const consumer=checkConsumerPackage(file);if(consumer.errors.length)throw Error('Consumer contract: '+consumer.errors.join('\n'));}
 s.integrity=lockStudy(s);fs.writeFileSync(path.join(ROOT,'catalog/sources.json'),JSON.stringify(candidate,null,2)+'\n');return s;
}
export function relockStudies(id){
 const data=definitions(),selected=id?data.sources.filter(s=>s.id===id):data.sources;if(!selected.length)throw Error('Unknown study id');
 for(const s of selected)s.integrity=lockStudy(s);
 fs.writeFileSync(path.join(ROOT,'catalog/sources.json'),JSON.stringify(data,null,2)+'\n');return selected.map(s=>({id:s.id,...s.integrity}));
}
if(process.argv[1]&&path.resolve(process.argv[1])===fileURLToPath(import.meta.url)){
 const [command,...args]=process.argv.slice(2);
 if(command==='add'){const i=args.indexOf('--profile');if(i<0||!args[i+1])throw Error('Usage: npm run study:add -- --profile path/to/profile.json');console.log(JSON.stringify({added:addStudy(JSON.parse(fs.readFileSync(path.resolve(args[i+1]),'utf8'))).id}));}
 else if(command==='lock'){const i=args.indexOf('--id');if(i<0||!args[i+1])throw Error('Usage: npm run study:lock -- --id study-id');console.log(JSON.stringify(relockStudies(args[i+1])));}
 else if(command==='status')console.log(JSON.stringify(definitions().sources.map(verifyStudy),null,2));
 else throw Error('Expected add, lock or status');
}
