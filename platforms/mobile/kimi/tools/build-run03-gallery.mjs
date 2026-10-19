/** Offline gallery of explicitly pixel-reviewed original run03 screenshots.
 * node research/kimi-android/tools/build-run03-gallery.mjs [--check] [--reviewed-only]
 * Default refuses pending reviews. --reviewed-only deliberately builds the safe
 * subset while other captures continue; no unknown/private source is read.
 */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {spawnSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
import {sourceSceneDisposition} from './run03-distribution-policy.mjs';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const runId='run03-20261016',run=`evidence/runtime/${runId}`;
const sha=b=>crypto.createHash('sha256').update(b).digest('hex');
const read=p=>JSON.parse(fs.readFileSync(path.join(root,p),'utf8').replace(/^\uFEFF/,''));
const esc=s=>String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const within=p=>{const a=path.resolve(root,p);if(!a.startsWith(root+path.sep))throw Error('Path outside research root');return a;};
const auditFile='docs/run03-evidence-audit.json',safeFile='docs/run03-safe-scenes.json';
const audit=read(auditFile),safe=read(safeFile),session=read(run+'/session.json');
if(audit.runId!==runId||safe.runId!==runId)throw Error('Wrong run in audit/allowlist');
if(safe.auditSourceSha256!==sha(fs.readFileSync(within(auditFile))))throw Error('Allowlist stale: rerun audit-run03-evidence.mjs');
const probe=spawnSync(process.execPath,[path.join(root,'tools/audit-run03-evidence.mjs'),'--check'],{encoding:'utf8',windowsHide:true});
if(probe.error)throw probe.error;
let live;try{live=JSON.parse(probe.stdout);}catch{throw Error('Audit failed without a valid report: '+probe.stderr);}
if(live.integrityFailures?.length||![0,1].includes(probe.status))throw Error('Reviewed-source integrity check failed');
if(live.pending?.length&&!process.argv.includes('--reviewed-only'))throw Error('Unreviewed scenes remain; audit them first or explicitly use --reviewed-only for the reviewed subset');
const notes=new Map(audit.scenes.map(s=>[s.id,s])),registered=new Map(session.scenes.map(s=>[s.id,s]));
const withheldKinds=(live.withheld??[]).map(id=>sourceSceneDisposition(id,notes.get(id)));
const exclusionCounts={withheld:live.counts.withheld,private:withheldKinds.filter(s=>s.private).length,incomplete:withheldKinds.filter(s=>s.incomplete).length,otherWithheld:withheldKinds.filter(s=>s.otherWithheld).length};
const liveIds=new Set(live.safeSceneIds),included=[],images=[];
for(const entry of safe.scenes){
 const note=notes.get(entry.id),source=registered.get(entry.id);
 if(!liveIds.has(entry.id)||/private/i.test(entry.id)||note?.distribution!=='allow'||note?.privacy?.status!=='clear'||note.pixelReview!=='reviewed')throw Error('Scene lacks a current safe review: '+entry.id);
 if(!source||source.screenshot?.path!==entry.screenshot.path||source.hierarchy?.path!==entry.hierarchy.path)throw Error('Scene registration changed: '+entry.id);
 for(const [part,expected]of [['screenshot',note.exactScreenshotSha256],['hierarchy',note.exactHierarchySha256],['capture',note.exactCaptureSha256]]){
  if(entry[part]?.sha256!==expected||sha(fs.readFileSync(within(entry[part].path)))!==expected)throw Error('Reviewed bytes changed: '+entry.id+' '+part);
 }
 const png=fs.readFileSync(within(entry.screenshot.path));
 if(!png.subarray(0,8).equals(Buffer.from([137,80,78,71,13,10,26,10])))throw Error('Not a PNG: '+entry.id);
 if(png.readUInt32BE(16)!==source.screenshot.width||png.readUInt32BE(20)!==source.screenshot.height)throw Error('PNG dimensions differ: '+entry.id);
 included.push({id:entry.id,screenshotSha256:entry.screenshot.sha256,hierarchySha256:entry.hierarchy.sha256,captureSha256:entry.capture.sha256});
 images.push({source,note,data:'data:image/png;base64,'+png.toString('base64')});
}
if(included.length!==live.safeSceneIds.length)throw Error('Allowlist is stale or incomplete; rerun audit before building');
const manifest={schemaVersion:1,runId,createdAt:new Date().toISOString(),classification:'A original runtime pixels with separate reviewed annotations; no independent implementation images',
 output:'run03.html',auditSource:auditFile,auditSha256:safe.auditSourceSha256,sessionSnapshotSha256:sha(fs.readFileSync(within(run+'/session.json'))),
 included,excludedWithheldCount:exclusionCounts.withheld,excludedPrivateCount:exclusionCounts.private,excludedIncompleteCount:exclusionCounts.incomplete,excludedOtherWithheldCount:exclusionCounts.otherWithheld,excludedUnreviewedCount:live.counts.pending,unknownScenesIncluded:false,
 rawActionArgumentsIncluded:false,rawPrivateSourcesRead:false,atomicCapture:false,scopeComplete:false};
if(process.argv.includes('--check')){console.log(JSON.stringify({status:'checked-no-writes',runId,included:included.length,excludedWithheld:exclusionCounts.withheld,excludedPrivate:exclusionCounts.private,excludedIncomplete:exclusionCounts.incomplete,excludedOtherWithheld:exclusionCounts.otherWithheld,excludedUnreviewed:live.counts.pending}));process.exit(0);}
const cards=images.map(({source,note,data})=>`<article data-scene-id="${esc(source.id)}"><h2>${esc(source.id)}</h2><p>${esc(note.pixelSummary)}</p><img loading="lazy" alt="${esc(source.id)} 原版截图" src="${data}"><details><summary>独立 UIA 与采集复核</summary><p>${esc(note.uiaSummary)}</p><ul>${(note.findings??[]).map(s=>`<li>${esc(s)}</li>`).join('')}</ul><p>截图：${esc(source.screenshotCapturedAt)}；UIA/采集结束：${esc(source.capturedAt)}。两者非原子。</p><p class="hash">PNG SHA-256 ${esc(source.screenshot.sha256)}</p></details></article>`).join('\n');
const encoded=JSON.stringify(manifest).replaceAll('<','\\u003c');
const html=`<!doctype html><html lang="zh-CN"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Kimi run03 原版证据</title><style>body{font:16px/1.65 system-ui,sans-serif;background:#f5f5f5;color:#222;margin:0}header,main{max-width:1100px;margin:auto;padding:24px}header p{max-width:80ch}input{font:inherit;padding:10px 14px;border:1px solid #bbb;border-radius:12px;width:min(90%,500px)}main{display:grid;grid-template-columns:repeat(auto-fit,minmax(min(100%,310px),1fr));gap:24px}article{background:white;border-radius:20px;padding:20px;min-width:0}article h2{font-size:17px;overflow-wrap:anywhere}article img{display:block;width:100%;max-width:360px;height:auto;margin:16px auto;border:1px solid #eee;border-radius:12px}article p{font-size:14px}summary{cursor:pointer}.hash{font:11px/1.5 monospace;overflow-wrap:anywhere}[hidden]{display:none!important}</style><header><h1>Kimi run03 原版证据</h1><p>仅内嵌逐帧看图并核对哈希的安全原图。截图先于 UIA，不能合并为同一原子状态。这里是研究证据预览，独立原生参考 D 另验；约定范围尚未全部完成。</p><p>安全原图 ${included.length}；未分发 ${exclusionCounts.withheld}（隐私 ${exclusionCounts.private}、不完整 ${exclusionCounts.incomplete}、其他 ${exclusionCounts.otherWithheld}）；新增未审阅排除 ${live.counts.pending}。</p><label>筛选编号或画面摘要 <input id="filter" type="search" autocomplete="off"></label><p id="count" aria-live="polite"></p></header><main>${cards}</main><script id="run03-manifest" type="application/json">${encoded}</script><script>const input=document.getElementById('filter'),cards=[...document.querySelectorAll('article')];function update(){let n=0;for(const card of cards){card.hidden=!card.textContent.toLowerCase().includes(input.value.toLowerCase());if(!card.hidden)n++;}document.getElementById('count').textContent=n+' / '+cards.length+' 帧';}input.addEventListener('input',update);update();</script></html>`;
fs.writeFileSync(path.join(root,'run03.html'),html);
manifest.htmlSha256=sha(Buffer.from(html));
fs.writeFileSync(path.join(root,run,'gallery-verification.json'),JSON.stringify(manifest,null,2)+'\n');
console.log(JSON.stringify({status:'built-reviewed-only',output:'run03.html',frames:included.length,excludedWithheld:exclusionCounts.withheld,excludedPrivate:exclusionCounts.private,excludedIncomplete:exclusionCounts.incomplete,excludedOtherWithheld:exclusionCounts.otherWithheld,excludedUnreviewed:live.counts.pending,htmlSha256:manifest.htmlSha256},null,2));
