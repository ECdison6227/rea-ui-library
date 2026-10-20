/** Static Pages refreshes use published snapshots, never browser GitHub credentials. */
export const REFRESH_INTERVAL_MS=60_000;
export function validateSnapshot(data){
 if(data?.schemaVersion!==1||!Array.isArray(data.sources)||!Array.isArray(data.platforms))throw Error('目录快照格式不正确');
 const ids=new Set();for(const s of data.sources){if(!s?.id||!s.name||ids.has(s.id)||!Array.isArray(s.artifacts))throw Error('目录来源不完整');ids.add(s.id);}return data;
}
export function retainSelections(selected,sources){const ids=new Set(sources.map(s=>s.id));return new Set([...selected].filter(id=>ids.has(id)));}
export async function fetchCatalogSnapshot({fetchImpl=fetch,version=null,force=false,apiEnabled=false,now=Date.now()}={}){
 const options={cache:'no-store',credentials:'same-origin'};
 if(apiEnabled){const r=await fetchImpl('./data/catalog.json',options);if(!r.ok)throw Error('目录读取失败');return {changed:true,catalog:validateSnapshot(await r.json()),version:null};}
 const v=await fetchImpl('./data/version.json?t='+now,options);if(!v.ok)throw Error('版本信息暂时不可用');const next=await v.json();
 if(next.schemaVersion!==1||!/^[a-f0-9]{64}$/.test(next.catalogSha256||''))throw Error('版本信息格式不正确');
 if(!force&&version?.catalogSha256===next.catalogSha256)return {changed:false,version:next};
 const r=await fetchImpl('./data/catalog.json?v='+next.catalogSha256,options);if(!r.ok)throw Error('目录读取失败');const catalog=validateSnapshot(await r.json());
 if(catalog.publication?.snapshotId!==next.snapshotId)throw Error('目录正在更新，稍后重试');
 return {changed:true,catalog,version:next};
}
