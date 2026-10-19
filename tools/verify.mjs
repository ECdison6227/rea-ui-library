import fs from 'node:fs';
import path from 'node:path';
import {execFileSync} from 'node:child_process';
import {ROOT,definitions,validateDefinitions,hash,contained} from './catalog.mjs';
const errors=validateDefinitions(definitions()),lock=JSON.parse(fs.readFileSync(path.join(ROOT,'catalog/imports.lock.json'),'utf8'));
for(const s of lock.sources){
 if(s.revision){const actual=execFileSync('git',['-C',path.join(ROOT,s.path),'rev-parse','HEAD'],{encoding:'utf8',windowsHide:true}).trim();if(actual!==s.revision)errors.push('Changed source revision '+s.id);}
}
for(const t of lock.thumbnails){if(hash(fs.readFileSync(contained(ROOT,t.source)))!==t.sha256||hash(fs.readFileSync(contained(ROOT,t.target)))!==t.sha256)errors.push('Thumbnail source byte mismatch '+t.id);}
for(const f of ['README.md','AGENTS.md','docs/architecture.md','docs/inventory.md','docs/skill-review.md','skills/rea-style-library/SKILL.md','docs/skill-upgrade.patch'])if(!fs.existsSync(path.join(ROOT,f)))errors.push('Missing entry '+f);
const modules=fs.readFileSync(path.join(ROOT,'.gitmodules'),'utf8');for(const name of ['QuickPod','ec-lab','anyao-frontend'])if(modules.includes(name))errors.push('Non-REA module registered: '+name);
if(fs.existsSync(path.join(ROOT,'dist/build-manifest.json'))){const m=JSON.parse(fs.readFileSync(path.join(ROOT,'dist/build-manifest.json'),'utf8'));for(const f of m.files){if(/private|evidence\/|\.git\//.test(f.path))errors.push('Unexpected public content '+f.path);if(hash(fs.readFileSync(contained(path.join(ROOT,'dist'),f.path)))!==f.sha256)errors.push('Changed build file '+f.path);}}
const result={checkedAt:new Date().toISOString(),status:errors.length?'failed':'passed',sources:lock.sources.length,sourcePlatforms:{web:1,desktop:0,mobile:3},gitRevisionsChecked:lock.sources.filter(s=>s.revision).length,reviewedThumbnails:lock.thumbnails.length,originalAppsExecuted:false,limitations:['Integrity and catalog checks only; source-specific product validation remains in the original reports.'],errors};
fs.mkdirSync(path.join(ROOT,'.runtime'),{recursive:true});fs.writeFileSync(path.join(ROOT,'.runtime/verification.json'),JSON.stringify(result,null,2)+'\n');console.log(JSON.stringify(result,null,2));if(errors.length)process.exitCode=1;
