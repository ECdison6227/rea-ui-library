import fs from 'node:fs/promises';
import path from 'node:path';
import {validateDesignPackage} from './vendor/design-package.mjs';
import {hash,saveJson} from './browser.mjs';
const result=validateDesignPackage(path.resolve('design-package.json'));
const extraErrors=[];
const qa=JSON.parse(await fs.readFile('comparison/qa-results.json','utf8'));
if(qa.checks.some(item=>!item.passed))extraErrors.push('A reference browser check failed.');
if(!qa.implementationFiles)extraErrors.push('Reference QA has no implementation digests.');
for(const [file,digest] of Object.entries(qa.implementationFiles||{}))if(hash(await fs.readFile(file))!==digest)extraErrors.push('Reference changed after QA: '+file);
const pairs={desktop:['evidence/raw/rea-desktop.png','comparison/reference-desktop.png'],mobile:['comparison/settled-probe-source-mobile.png','comparison/reference-mobile.png'],feature:['evidence/raw/scroll-desktop-scroll-2100.png','comparison/reference-scroll-2100.png']};
for(const [name,[source,reference]] of Object.entries(pairs)){
  const record=JSON.parse((await fs.readFile(`comparison/rea-${name}-pixels.json`,'utf8')).replace(/^\uFEFF/,''));
  if(record.operation!=='compare_web_screenshots'||!/^ev_/.test(record.evidence_id))extraErrors.push('Invalid retained REA comparison '+name);
  if(record.parameters.before_artifact_sha256!==hash(await fs.readFile(source)))extraErrors.push('Source comparison image changed: '+name);
  if(record.parameters.after_artifact_sha256!==hash(await fs.readFile(reference)))extraErrors.push('Reference comparison image changed: '+name);
}
for(const file of ['README.md','DESIGN_SPEC.md','ADAPTATION_GUIDE.md','reference/README.md','comparison/README.md']){
  const markdown=await fs.readFile(file,'utf8');
  for(const match of markdown.matchAll(/\]\(([^)]+)\)/g)){
    const link=match[1];
    if(/^(https?:|#)/.test(link))continue;
    const target=path.resolve(path.dirname(file),decodeURIComponent(link.split('#')[0]));
    if(target===path.resolve('validation/integrity.json'))continue; // This verification creates its own report below.
    try{await fs.access(target);}catch{extraErrors.push(`Unresolved link in ${file}: ${link}`);}
  }
}
const report={...result,checkedAt:new Date().toISOString(),errors:[...result.errors,...extraErrors],browserChecks:qa.checks.length,implementationMatchesQA:!extraErrors.some(e=>e.includes('after QA'))};
report.status=report.errors.length?'failed':'passed';
await saveJson('validation/integrity.json',report);
console.log(JSON.stringify({status:report.status,readiness:report.readiness,filesChecked:report.filesChecked,counts:report.counts,browserChecks:report.browserChecks,errors:report.errors,warnings:report.warnings},null,2));
if(report.errors.length)process.exitCode=1;
