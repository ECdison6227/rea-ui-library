import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import {once} from 'node:events';
import {definitions,validateDefinitions,getCatalog,contained,ROOT,sourceFile,readArtifact} from '../tools/catalog.mjs';
import {createCatalogServer} from '../tools/serve.mjs';
import {filterSources,migrationPrompt,comparisonRows} from '../site/logic.js';
import {buildPublic} from '../tools/build.mjs';

test('source catalog contains only traceable REA research and keeps the empty desktop category',()=>{
 const data=definitions();assert.deepEqual(validateDefinitions(data),[]);const c=getCatalog();assert.equal(c.sources.length,4);assert.equal(c.platforms.find(p=>p.id==='desktop').count,0);assert.ok(c.sources.every(s=>s.kind==='research'&&s.origin==='historical'));assert.ok(!c.sources.some(s=>/ec-lab|quickpod|anyao/.test(s.id)));
});
test('classification and multiple search terms work independently of UI rendering',()=>{
 const sources=getCatalog().sources;assert.deepEqual(filterSources(sources,{platform:'web'}).map(s=>s.id),['hark']);assert.deepEqual(filterSources(sources,{query:'Android 草稿'}).map(s=>s.id),['kimi']);assert.equal(filterSources(sources,{platform:'desktop'}).length,0);assert.equal(filterSources(sources,{query:'不存在的来源'}).length,0);
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
  const c=await (await fetch(base+'/data/catalog.json')).json();assert.equal(c.sources.length,4);assert.equal(c.privateMode,false);
  assert.equal((await fetch(base+'/api/document?source=hark&path=README.md')).status,200);
  assert.equal((await fetch(base+'/api/document?source=hark&path=evidence/manifest.json')).status,403);
  assert.equal((await fetch(base+'/source/hark/reports/evidence-gallery.html')).status,403);
  assert.equal((await fetch(base+'/source/kimi/%2e%2e%2f%2e%2e%2fREADME.md')).status,403);
  assert.equal((await fetch(base+'/.git/config')).status,403);
  assert.equal((await fetch(base+'/data/catalog.json',{method:'POST',body:'{}'})).status,405);
  assert.match((await fetch(base+'/')).headers.get('permissions-policy'),/microphone=\(\)/);
 }finally{await new Promise(resolve=>server.close(resolve));}
});
test('public build has only reviewed thumbnails and metadata; unknown stale files stop export',()=>{
 const base=path.join(ROOT,'.runtime');fs.mkdirSync(base,{recursive:true});const output=fs.mkdtempSync(path.join(base,'export-test-'));
 try{
  buildPublic({output});const manifest=JSON.parse(fs.readFileSync(path.join(output,'build-manifest.json'),'utf8'));
  assert.equal(manifest.rawEvidenceIncluded,false);assert.equal(manifest.sourceCount,4);assert.ok(manifest.files.every(f=>!f.path.startsWith('platforms/')&&!f.path.includes('private/')));
  const bytes=Buffer.from('must never be included in an export');fs.writeFileSync(path.join(output,'private-note.txt'),bytes);assert.throws(()=>buildPublic({output}),/Unknown file/);assert.deepEqual(fs.readFileSync(path.join(output,'private-note.txt')),bytes);
 }finally{const resolved=path.resolve(output);if(!resolved.startsWith(path.resolve(base)+path.sep))throw Error('Unsafe test cleanup');fs.rmSync(resolved,{recursive:true,force:true});}
});
