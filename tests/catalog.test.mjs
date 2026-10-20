import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import {once} from 'node:events';
import {definitions,validateDefinitions,getCatalog,contained,ROOT,sourceFile,readArtifact} from '../tools/catalog.mjs';
import {createCatalogServer} from '../tools/serve.mjs';
import {filterSources,migrationPrompt,comparisonRows} from '../site/logic.js';
import {buildPublic} from '../tools/build.mjs';
import {addStudy} from '../tools/studies.mjs';
import {verifyStudy} from '../tools/integrity.mjs';
import {makePrompt} from '../tools/catalog.mjs';
import {initDesignPackage,catalogProfile,validateDesignPackage} from '../skills/rea-design-transfer/scripts/design-package.mjs';
import crypto from 'node:crypto';

test('source catalog contains traceable REA research with dynamically counted platforms',()=>{
 const data=definitions();assert.deepEqual(validateDefinitions(data),[]);const c=getCatalog();assert.equal(c.sources.length,data.sources.length);for(const p of c.platforms)assert.equal(p.count,c.sources.filter(s=>s.platform===p.id).length);assert.ok(c.sources.every(s=>s.kind==='research'&&s.origin==='historical'));assert.ok(!fs.existsSync(path.join(ROOT,'.gitmodules')));
});
test('classification and multiple search terms work independently of UI rendering',()=>{
 const sources=[{id:'one',platform:'web',name:'alpha',subtitle:'',summary:'',tags:['draft'],stack:['Android']},{id:'two',platform:'desktop',name:'beta',subtitle:'',summary:'',tags:[],stack:[]}];assert.deepEqual(filterSources(sources,{platform:'web'}).map(s=>s.id),['one']);assert.deepEqual(filterSources(sources,{query:'Android draft'}).map(s=>s.id),['one']);assert.equal(filterSources(sources,{platform:'mobile'}).length,0);assert.equal(filterSources(sources,{query:'不存在的来源'}).length,0);
});
test('readiness acceptance does not change Kimi technical coverage or source identity',()=>{
 const s=getCatalog().sources.find(s=>s.id==='kimi');assert.equal(s.acceptance.acceptedByUser,true);assert.equal(s.acceptance.technicalCoverageUnchanged,true);assert.match(s.coverage,/20/);assert.match(s.coverage,/未测/);assert.equal(s.snapshot.sourceApkSha256,'9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332');const p=migrationPrompt(s);assert.ok(p.includes(s.root)&&p.includes(s.coverage)&&p.includes(s.limits[0]));
});
test('comparison preserves distinct conditions rather than computing a fidelity score',()=>{
 const a=getCatalog().sources.filter(s=>['hark','mi-fitness'].includes(s.id)),rows=comparisonRows(a);assert.equal(rows.find(r=>r.label==='原版平台').values.join('/'),'网页/手机端');assert.notEqual(rows.find(r=>r.label==='技术栈').values[0],rows.find(r=>r.label==='技术栈').values[1]);assert.equal(rows.find(r=>r.label==='保留边界').values.length,2);
});
test('document API permits only declared artifacts and protects raw evidence by mode',()=>{
 assert.equal(readArtifact('hark','README.md').source,'hark');assert.throws(()=>readArtifact('hark','evidence/manifest.json'));assert.throws(()=>readArtifact('hark','reports/evidence-gallery.html'));assert.throws(()=>sourceFile('hark','evidence/screenshots/02-chat-initial.png'));assert.doesNotThrow(()=>sourceFile('hark','reports/evidence-gallery.html',{privateMode:true}));assert.throws(()=>contained(ROOT,'../README.md'));assert.throws(()=>contained(ROOT,'.git/config'));assert.throws(()=>sourceFile('mirasim','mirasim-0.0.497.apk'));
});
test('HTTP catalog is read-only and blocks private paths and undeclared document reads',async()=>{
 const server=createCatalogServer();server.listen(0,'127.0.0.1');await once(server,'listening');const base='http://127.0.0.1:'+server.address().port;
 try{
  const c=await (await fetch(base+'/data/catalog.json')).json();assert.equal(c.sources.length,definitions().sources.length);assert.equal(c.privateMode,false);
  assert.equal((await fetch(base+'/api/document?source=hark&path=README.md')).status,200);
  assert.equal((await fetch(base+'/api/document?source=hark&path=evidence/manifest.json')).status,403);
  assert.equal((await fetch(base+'/source/hark/reports/evidence-gallery.html')).status,403);
  assert.equal((await fetch(base+'/source/kimi/%2e%2e%2f%2e%2e%2fREADME.md')).status,403);
  assert.equal((await fetch(base+'/.git/config')).status,403);
  assert.equal((await fetch(base+'/data/catalog.json',{method:'POST',body:'{}'})).status,405);
  assert.match((await fetch(base+'/')).headers.get('permissions-policy'),/microphone=\(\)/);
  const video='/source/duolingo-android-6-80-6-china-2368/evidence/reference-revision/motion/progress-video/screenrecord.mp4';const range=await fetch(base+video,{headers:{Range:'bytes=0-15'}});assert.equal(range.status,206);assert.match(range.headers.get('content-type'),/video\/mp4/);assert.equal((await range.arrayBuffer()).byteLength,16);assert.equal((await fetch(base+video,{headers:{Range:'bytes=999999999-'}})).status,416);
 }finally{await new Promise(resolve=>server.close(resolve));}
});
test('public build has only reviewed thumbnails and metadata; unknown stale files stop export',()=>{
 const base=path.join(ROOT,'.runtime');fs.mkdirSync(base,{recursive:true});const output=fs.mkdtempSync(path.join(base,'export-test-'));
 try{
  buildPublic({output});const manifest=JSON.parse(fs.readFileSync(path.join(output,'build-manifest.json'),'utf8'));
  assert.equal(manifest.rawEvidenceIncluded,false);assert.equal(manifest.sourceCount,definitions().sources.length);assert.ok(manifest.files.every(f=>!f.path.startsWith('platforms/')&&!f.path.includes('private/')));const catalog=JSON.parse(fs.readFileSync(path.join(output,'data/catalog.json'),'utf8'));for(const s of catalog.sources){assert.match(s.sourceRevisionUrl,/ECdison6227\/rea-ui-library\/tree\//);for(const a of s.artifacts)assert.match(a.url,/ECdison6227\/rea-ui-library\/blob\//);}
  const version=JSON.parse(fs.readFileSync(path.join(output,'data/version.json'),'utf8'));assert.equal(version.sourceCount,catalog.sources.length);assert.equal(version.snapshotId,catalog.publication.snapshotId);assert.equal(version.catalogSha256,crypto.createHash('sha256').update(fs.readFileSync(path.join(output,'data/catalog.json'))).digest('hex'));assert.ok(fs.readFileSync(path.join(output,'app.js'),'utf8').includes(version.appVersion));assert.ok(!fs.readFileSync(path.join(output,'app.js'),'utf8').includes('__BUILD_APP_VERSION__'));
  const bytes=Buffer.from('must never be included in an export');fs.writeFileSync(path.join(output,'private-note.txt'),bytes);assert.throws(()=>buildPublic({output}),/Unknown file/);assert.deepEqual(fs.readFileSync(path.join(output,'private-note.txt')),bytes);
 }finally{const resolved=path.resolve(output);if(!resolved.startsWith(path.resolve(base)+path.sep))throw Error('Unsafe test cleanup');fs.rmSync(resolved,{recursive:true,force:true});}
});

test('new legacy-only research is rejected without changing the registry',()=>{
 const before=fs.readFileSync(path.join(ROOT,'catalog/sources.json'));const profile=JSON.parse(fs.readFileSync(path.join(ROOT,'catalog/study.template.json'),'utf8'));delete profile.designPackage;profile.id='legacy-fixture-'+process.pid;profile.root='platforms/desktop/'+profile.id;assert.throws(()=>addStudy(profile),/require Design Package v1/);assert.deepEqual(fs.readFileSync(path.join(ROOT,'catalog/sources.json')),before);
});

test('client/server migration tasks carry repository, ref, source boundary and modern/legacy entry',()=>{
 const s=getCatalog().sources[0],a=migrationPrompt(s),b=makePrompt(s);assert.equal(a,b);for(const value of ['$rea-design-transfer',s.sourceRepositoryUrl,s.sourceRef,s.root,s.coverage,s.limits[0]])assert.ok(a.includes(value));assert.ok(a.includes(s.artifacts.find(x=>x.role==='readme').path));const modern={...s,designPackage:'design-package.json'};assert.ok(migrationPrompt(modern).includes('设计入口：design-package.json'));assert.ok(migrationPrompt(modern).includes('实际取得的commit'));
});

test('standard draft cannot auto-register; an explicit partial fixture preserves old sources',()=>{
 const id='design-fixture-'+process.pid,relative='platforms/desktop/'+id,folder=path.resolve(ROOT,relative),manifest=path.join(ROOT,'catalog/manifests',id+'.json'),registry=path.join(ROOT,'catalog/sources.json'),before=fs.readFileSync(registry);
 if(fs.existsSync(folder)||fs.existsSync(manifest))throw Error('Unexpected existing fixture');
 try{
  const file=initDesignPackage(folder,{id,name:'Synthetic catalog contract fixture',platform:'desktop',runtime:'synthetic',version:'fixture',identity:'synthetic-only'});
  assert.throws(()=>catalogProfile(file,{root:relative}),/research/);
  const legacy=JSON.parse(fs.readFileSync(path.join(ROOT,'catalog/study.template.json'),'utf8'));Object.assign(legacy,{id,root:relative,platform:'desktop',readiness:'draft',designPackage:'design-package.json'});legacy.artifacts=[{label:'entry',path:'README.md',role:'readme'},{label:'spec',path:'DESIGN_SPEC.md',role:'spec'}];legacy.run.cwd=relative;assert.throws(()=>addStudy(legacy),/Design Package/);assert.deepEqual(fs.readFileSync(registry),before);assert.equal(fs.existsSync(manifest),false);
  const p=JSON.parse(fs.readFileSync(file,'utf8')),receipt=Buffer.from('Synthetic checker fixture, not a real REA invocation');fs.writeFileSync(path.join(folder,'receipt.txt'),receipt);p.source.researchMethod='Synthetic contract test only';p.source.researchEvidenceRefs=['receipt'];p.readiness.status='partial';fs.writeFileSync(file,JSON.stringify(p,null,2)+'\n');fs.writeFileSync(path.join(folder,p.entries.evidence),JSON.stringify({formatVersion:1,kind:'evidence',items:[{id:'receipt',classification:'B',origin:'historical',subject:'source',kind:'static',sourceIdentity:'synthetic-only',capturedAt:null,provider:'REA',sharing:'included',path:'receipt.txt',sha256:crypto.createHash('sha256').update(receipt).digest('hex')}]}));
  p.readiness.supportedUses=['Synthetic usable-contract fixture only'];p.validation.reference.status='partial';p.validation.crossDomain.status='partial';fs.writeFileSync(file,JSON.stringify(p,null,2)+'\n');
  const rec={classification:'D',coverage:'partial',priority:'core',evidenceRefs:['receipt'],unknowns:[],implementation:{status:'specified',classification:'D',decision:'Synthetic target choice for tests'}};
  const fixtures={tokens:[{...rec,id:'t',role:'test.color',value:'#112233',unit:'hex',scope:'test'}],components:[{...rec,id:'c',name:'Test button',slots:['label'],tokenRefs:['t'],states:[{id:'default',appearance:'Visible test state',evidenceRefs:['receipt']}],events:['activate']}],layouts:[{...rec,id:'l',name:'Test layout',componentRefs:['c'],rules:['Single action']}],interactions:[{...rec,id:'i',componentRefs:['c'],transitions:[{from:'idle',event:'activate',to:'done',result:'test only',evidenceRefs:['receipt']}]}],motion:[{...rec,id:'m',trigger:'test action',properties:['opacity'],duration:{value:100,unit:'ms',basis:'chosen'},easing:{value:'linear',basis:'chosen'},interruption:['cancel']}]};
  for(const [kind,items]of Object.entries(fixtures))fs.writeFileSync(path.join(folder,p.entries[kind]),JSON.stringify({formatVersion:1,kind,items}));
  const profile=catalogProfile(file,{root:relative}),s=addStudy(profile);assert.equal(s.readiness,'partial');assert.equal(s.designPackage,'design-package.json');assert.ok(s.integrity.files>10);assert.equal(getCatalog().sources.find(x=>x.id===id).readiness,'partial');
  p.readiness.status='ready';fs.writeFileSync(file,JSON.stringify(p));assert.ok(validateDefinitions(definitions()).some(e=>e.includes('readiness differs')));assert.equal(validateDesignPackage(file).status,'failed');assert.throws(()=>verifyStudy(s),/changed/);
 }finally{fs.writeFileSync(registry,before);if(path.dirname(folder)!==path.join(ROOT,'platforms/desktop')||!path.basename(folder).startsWith('design-fixture-'))throw Error('Unsafe fixture cleanup');if(fs.existsSync(folder))fs.rmSync(folder,{recursive:true,force:true});if(fs.existsSync(manifest))fs.unlinkSync(manifest);}
});
