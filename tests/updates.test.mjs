import test from 'node:test';import assert from 'node:assert/strict';
import {fetchCatalogSnapshot,retainSelections,REFRESH_INTERVAL_MS} from '../site/updates.js';
const snapshot=(ids,key)=>({schemaVersion:1,platforms:[],sources:ids.map(id=>({id,name:id,artifacts:[]})),publication:{snapshotId:key}});
const response=body=>({ok:true,json:async()=>body});
test('Pages notices a newly admitted source without calling GitHub or a local API',async()=>{
 const next={schemaVersion:1,catalogSha256:'b'.repeat(64),snapshotId:'new'},calls=[];
 const result=await fetchCatalogSnapshot({version:{catalogSha256:'a'.repeat(64)},fetchImpl:async(url,opts)=>{calls.push({url,opts});return response(url.includes('version')?next:snapshot(['first','new-source'],'new'));},now:123});
 assert.equal(result.changed,true);assert.equal(result.catalog.sources.length,2);assert.equal(calls.length,2);assert.ok(calls.every(c=>c.url.startsWith('./data/')&&c.opts.cache==='no-store'));assert.ok(calls[1].url.includes(next.catalogSha256));assert.equal(REFRESH_INTERVAL_MS,60000);
});
test('unchanged versions avoid reloading the catalog, while manual refresh can force it',async()=>{
 const v={schemaVersion:1,catalogSha256:'a'.repeat(64),snapshotId:'same'};let requests=0;
 const fetchImpl=async url=>{requests++;return response(url.includes('version')?v:snapshot(['one'],'same'));};
 assert.equal((await fetchCatalogSnapshot({version:v,fetchImpl})).changed,false);assert.equal(requests,1);
 assert.equal((await fetchCatalogSnapshot({version:v,force:true,fetchImpl})).changed,true);assert.equal(requests,3);
});
test('deployment mismatch and network errors do not yield a replacement snapshot',async()=>{
 const v={schemaVersion:1,catalogSha256:'a'.repeat(64),snapshotId:'next'};
 await assert.rejects(()=>fetchCatalogSnapshot({fetchImpl:async u=>response(u.includes('version')?v:snapshot(['old'],'old'))}),/正在更新/);
 await assert.rejects(()=>fetchCatalogSnapshot({fetchImpl:async()=>({ok:false})}),/暂时不可用/);
 assert.deepEqual([...retainSelections(new Set(['one','removed']),[{id:'one'},{id:'new'}])],['one']);
});
test('local development continues to use its catalog endpoint without static version files',async()=>{
 const urls=[];const r=await fetchCatalogSnapshot({apiEnabled:true,fetchImpl:async u=>{urls.push(u);return response(snapshot(['one'],'dev'));}});assert.equal(r.catalog.sources.length,1);assert.deepEqual(urls,['./data/catalog.json']);
});
