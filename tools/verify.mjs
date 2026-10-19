import fs from 'node:fs';
import path from 'node:path';
import {ROOT,definitions,validateDefinitions,hash,contained,PLATFORMS} from './catalog.mjs';
import {verifyStudy} from './integrity.mjs';
import {validateDesignPackage} from '../skills/rea-design-transfer/scripts/design-package.mjs';
import {checkSkillSync} from './check-skill-sync.mjs';
const data=definitions(),errors=validateDefinitions(data),lock=JSON.parse(fs.readFileSync(path.join(ROOT,'catalog/imports.lock.json'),'utf8'));
errors.push(...checkSkillSync().errors);
if(lock.sources.length!==data.sources.length)errors.push('Stale source registry lock');
for(const s of data.sources){try{verifyStudy(s);if(s.designPackage){const r=validateDesignPackage(contained(contained(ROOT,s.root),s.designPackage),{forCatalog:true});errors.push(...r.errors.map(e=>s.id+': '+e));}const l=lock.sources.find(l=>l.id===s.id);if(l?.path!==s.root||JSON.stringify(l?.integrity)!==JSON.stringify(s.integrity))errors.push('Stale source lock: '+s.id);}catch(e){errors.push(e.message);}}
for(const t of lock.thumbnails){if(hash(fs.readFileSync(contained(ROOT,t.source)))!==t.sha256||hash(fs.readFileSync(contained(ROOT,t.target)))!==t.sha256)errors.push('Thumbnail source byte mismatch '+t.id);}
for(const f of ['README.md','AGENTS.md','docs/architecture.md','docs/inventory.md','docs/skill-review.md','docs/adding-studies.md','skills/rea-design-transfer/SKILL.md','docs/history/skill-upgrade-initial.patch'])if(!fs.existsSync(path.join(ROOT,f)))errors.push('Missing entry '+f);
if(fs.existsSync(path.join(ROOT,'.gitmodules')))errors.push('Monorepo must not depend on submodules');
if(fs.existsSync(path.join(ROOT,'dist/build-manifest.json'))){const m=JSON.parse(fs.readFileSync(path.join(ROOT,'dist/build-manifest.json'),'utf8'));for(const f of m.files){if(/private|evidence\/|\.git\//.test(f.path))errors.push('Unexpected public content '+f.path);if(hash(fs.readFileSync(contained(path.join(ROOT,'dist'),f.path)))!==f.sha256)errors.push('Changed build file '+f.path);}}
const result={checkedAt:new Date().toISOString(),status:errors.length?'failed':'passed',sources:data.sources.length,sourcePlatforms:Object.fromEntries(Object.keys(PLATFORMS).map(p=>[p,data.sources.filter(s=>s.platform===p).length])),storage:'single-repository',sourceFilesChecked:data.sources.reduce((n,s)=>n+s.integrity.files,0),reviewedThumbnails:lock.thumbnails.length,originalAppsExecuted:false,limitations:['Integrity and catalog checks only; source-specific product validation remains in the original reports.'],errors};
fs.mkdirSync(path.join(ROOT,'.runtime'),{recursive:true});fs.writeFileSync(path.join(ROOT,'.runtime/verification.json'),JSON.stringify(result,null,2)+'\n');console.log(JSON.stringify(result,null,2));if(errors.length)process.exitCode=1;
