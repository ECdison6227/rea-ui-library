/** Generate run03 reports from the live checklist, observations and exact audit.
 * node research/kimi-android/tools/summarize-run03.mjs [--check]
 * Writes only docs/run03-results.md, docs/run03-coverage.md and
 * spec/run03-observed-transitions.json. Never edits shared indexes or task status.
 */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {sourceSceneDisposition} from './run03-distribution-policy.mjs';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const runId='run03-20261016',run=`evidence/runtime/${runId}`;
const sha=b=>crypto.createHash('sha256').update(b).digest('hex');
const read=p=>JSON.parse(fs.readFileSync(path.join(root,p),'utf8').replace(/^\uFEFF/,''));
const exists=p=>fs.existsSync(path.join(root,p));
const md=s=>String(s??'').replaceAll('|','\\|').replaceAll('\n',' ');
const files={checklist:'docs/remaining-validation.json',observations:run+'/observations.json',audit:'docs/run03-evidence-audit.json',allowlist:'docs/run03-safe-scenes.json',session:run+'/session.json'};
const list=read(files.checklist),audit=read(files.audit),safe=read(files.allowlist),session=read(files.session);
const observations=exists(files.observations)?read(files.observations):[];
if(list.taskCount!==37||list.tasks.length!==37||new Set(list.tasks.map(t=>t.id)).size!==37)throw Error('Expected all 37 agreed tasks');
if(audit.runId!==runId||safe.runId!==runId||safe.auditSourceSha256!==sha(fs.readFileSync(path.join(root,files.audit))))throw Error('Audit/allowlist identity differs; rerun audit first');
if(!Array.isArray(observations))throw Error('Observations must be an array');
const review=new Map(audit.scenes.map(s=>[s.id,s])),safeIds=new Set(safe.safeSceneIds);
const captureId=p=>new RegExp('^'+run+'/scenes/([^/]+)/capture\\.json$').exec(p)?.[1];
const disposition=id=>sourceSceneDisposition(id,review.get(id));
const privateId=id=>disposition(id).private,withheldId=id=>disposition(id).withheld;
for(const id of safeIds){const note=review.get(id);if(withheldId(id)||note?.distribution!=='allow'||note?.privacy?.status!=='clear'||note?.pixelReview!=='reviewed')throw Error('Unsafe entry in allowlist');}
const evidenceInfo=paths=>{
 const ids=[...new Set((paths??[]).map(captureId).filter(Boolean))];
 return {safe:ids.filter(id=>safeIds.has(id)),withheldCount:ids.filter(withheldId).length,privateCount:ids.filter(privateId).length,incompleteCount:ids.filter(id=>disposition(id).incomplete).length,otherWithheldCount:ids.filter(id=>disposition(id).otherWithheld).length,unreviewedCount:ids.filter(id=>!safeIds.has(id)&&!withheldId(id)).length,otherReferences:(paths??[]).filter(p=>!captureId(p)).length};
};
const counts={};for(const task of list.tasks)counts[task.status]=(counts[task.status]??0)+1;
const completeStatus=t=>t.status==='verified'||(t.status==='not_applicable'&&!!t.scopeDecision&&!!(t.scopeDecision.acceptedByUser||t.scopeDecision.userConfirmed||t.scopeDecision.authority==='user'));
const outstanding=list.tasks.filter(t=>!completeStatus(t)),scopeComplete=outstanding.length===0;
const safeObservations=observations.map(o=>{
 const info=evidenceInfo(o.evidence),allReviewed=(o.evidence?.length??0)>0&&info.safe.length===(o.evidence?.length??0);
 return {taskId:o.taskId,statusAtObservation:o.status,recordedAt:o.recordedAt,safeSourceScenes:info.safe,withheldSourcesExcluded:info.withheldCount,privateSourcesExcluded:info.privateCount,incompleteSourcesExcluded:info.incompleteCount,otherWithheldSourcesExcluded:info.otherWithheldCount,unreviewedSourcesExcluded:info.unreviewedCount,
  summary:allReviewed?o.summary:'原始尝试含未分发（隐私、不完整或其他原因）、未审阅或其他类型来源；未复制原摘要。按任务当前状态、安全逐帧注记及独立证据继续核对。'};
});
const now=new Date().toISOString();
const pending=session.scenes.filter(s=>!safeIds.has(s.id)&&!withheldId(s.id)).length;
const withheld=session.scenes.filter(s=>withheldId(s.id)).length;
const privateExcluded=session.scenes.filter(s=>privateId(s.id)).length;
const incompleteExcluded=session.scenes.filter(s=>disposition(s.id).incomplete).length;
const otherWithheldExcluded=session.scenes.filter(s=>disposition(s.id).otherWithheld).length;
const sourceDigests=Object.fromEntries(Object.entries(files).filter(([,p])=>exists(p)).map(([key,p])=>[key,{path:p,sha256:sha(fs.readFileSync(path.join(root,p)))}]));
const nodes=session.scenes.filter(s=>safeIds.has(s.id)).map(s=>{
 const note=review.get(s.id);return {id:s.id,sourceClassification:'A',origin:'retained original run03 observation; review does not create new runtime evidence',
  screenshot:s.screenshot,hierarchy:s.hierarchy,captureSha256:note.exactCaptureSha256,
  screenshotCapturedAt:s.screenshotCapturedAt,capturedAt:s.capturedAt,atomicCapture:false,
  pixelSummary:note.pixelSummary,uiaSummary:note.uiaSummary,limitations:note.findings??[]};
});
const intervals=[];
for(let i=1;i<session.scenes.length;i++){
 const from=session.scenes[i-1],to=session.scenes[i];
 if(!safeIds.has(from.id)||!safeIds.has(to.id))continue;
 const start=from.actionsBeforeCapture,end=to.actionsBeforeCapture;
 if(!Number.isInteger(start)||!Number.isInteger(end)||end<start)throw Error('Invalid capture action interval');
 intervals.push({id:`R03-I${String(intervals.length+1).padStart(3,'0')}`,from:from.id,to:to.id,
  classification:'C bounded association of retained A observations and action records',sourceStateClassification:'A',
  actionIndexBase:1,actionIndexes:Array.from({length:end-start},(_,j)=>start+j+1),
  operations:session.actions.slice(start,end).map((a,j)=>({index1Based:start+j+1,operation:a.operation,status:a.status??'recorded-completion',startedAt:a.startedAt,completedAt:a.completedAt})),
  limitation:'Capture-to-capture interval only. Intervening UI is not exhaustively observed; logged action success is not result verification, screenshot/UIA are non-atomic, and causation/backend semantics are not inferred.'});
}
const transitions={schemaVersion:1,runId,generatedAt:now,scopeComplete,taskCount:37,statusCounts:counts,
 sourceDigests,withheldRawIncluded:false,privateRawIncluded:false,rawActionArgumentsIncluded:false,unreviewedSourcesIncluded:false,exclusions:{withheld,private:privateExcluded,incomplete:incompleteExcluded,otherWithheld:otherWithheldExcluded,unreviewed:pending},
 classificationBoundary:'A original runtime states; B packaged declarations are not runtime results; C bounded interval association; D independent native/transfer implementation is a separate verification track.',
 nodes,intervals,observations:safeObservations,outstandingTaskIds:outstanding.map(t=>t.id)};
const table=[
 '| ID | 项目 | 当前状态 | 安全原版场景 | 排除/其他证据 |',
 '| --- | --- | --- | --- | --- |',
 ...list.tasks.map(t=>{const info=evidenceInfo(t.evidence);return `| ${t.id} | ${md(t.title)} | ${t.status} | ${info.safe.map(id=>'`'+id+'`').join('、')||'—'} | 未分发 ${info.withheldCount}（隐私 ${info.privateCount}、不完整 ${info.incompleteCount}、其他 ${info.otherWithheldCount}）；未审阅 ${info.unreviewedCount}；其他类型 ${info.otherReferences} |`;})
].join('\n');
const preamble=`---\nrun_id: ${runId}\ngenerated_at: ${now}\nscope_complete: ${scopeComplete}\n---\n\n`;
const completion=scopeComplete?'当前清单所有条目均已由状态或用户明确范围决定销项；仍应分别核对 A 与 D 的证据边界。':`约定的 37 项范围尚未全部完成。当前 verified ${counts.verified??0} 项，仍有 ${outstanding.length} 项未销项；partial、in_progress、needs_user、needs_authorization 均不算完成。`;
const dependencies='用户已说明暂时无法提供设备，并要求保留相关项目未完成。语音、真实拍摄、双端同步及其他实际设备依赖继续保留；这不是缩减范围或“不适用”的授权。';
const boundary='逐帧复核重读的是本轮原版留存证据，不产生新的运行通过。截图先于 UIA，分别叙述；动作成功不等于结果验证。独立原生参考、配对对照和跨业务迁移属于 D，不能替代原版 A。';
const facts=nodes.map(n=>`### ${n.id}\n\n原图：${n.pixelSummary}\n\n后采 UIA：${n.uiaSummary}\n\n${n.limitations.map(s=>'- '+s).join('\n')}\n`).join('\n');
const observed=safeObservations.map(o=>`- ${o.taskId} · ${o.statusAtObservation} · ${o.recordedAt}：${o.summary}`).join('\n');
const result=preamble+`# 第三轮运行验证结果\n\n${completion}\n\n${dependencies}\n\n${boundary}\n\n本次生成快照：${session.scenes.length} 张原版采集，${session.actions.length} 条动作；安全像素审核 ${nodes.length} 张，未分发整套 ${withheld} 张（隐私 ${privateExcluded}、不完整 ${incompleteExcluded}、其他 ${otherWithheldExcluded}），未审阅排除 ${pending} 张。withheld表示未分发，不等于全部包含身份；例如缺失UIA可单独形成不完整排除。数量只作索引。\n\n[安全原图预览](../run03.html) · [37 项覆盖清单](run03-coverage.md) · [带哈希的原版状态与区间](../spec/run03-observed-transitions.json)\n\n## 当前逐项状态\n\n${table}\n\n## 本轮尝试记录\n\n${observed||'尚无可复制的尝试摘要。'}\n\n## 已审阅安全原版画面\n\n${facts}\n## 分发与恢复边界\n\n因隐私、不完整或其他原因withheld的PNG、UIA、capture整套不进入画廊或研究包；未审阅场景默认排除。原APK、AVD、凭据、调试密钥和本机私有调试资料不得打包。旧 run02 打包过滤不自动覆盖 run03。\n\n最终恢复记录、独占设备关闭和原APK身份由 verify-run03.mjs 的相应检查另验；生成报告本身不证明已恢复。\n`;
const gaps=list.tasks.map(t=>{
 const info=evidenceInfo(t.evidence),intro=completeStatus(t)?'清单已标记销项；以下是原验收条件，摘要工具不扩大该结论。':'以下验收条件尚未由当前任务整体状态销项；条目可能已有部分证据，无逐条映射时不自动删除任何条件。';
 return `### ${t.id} · ${t.title}\n\n状态：${t.status}。${intro}\n\n${(t.acceptance??[]).map(s=>'- '+s).join('\n')}\n\n授权/适用边界：${(t.boundaries??[]).join('；')||'以交接为准'}\n\n安全原版证据 ${info.safe.length}；未分发源 ${info.withheldCount}（隐私 ${info.privateCount}、不完整 ${info.incompleteCount}、其他 ${info.otherWithheldCount}）；未审阅排除 ${info.unreviewedCount}；独立或其他证据 ${info.otherReferences}。${t.assistanceRequest&&!t.assistanceRequest.resolved?'协助依赖尚未解决，继续保留未完成。':''}\n`;
}).join('\n');
const coverage=preamble+`# Run03 约定范围覆盖\n\n${completion}\n\n${dependencies}\n\n状态来自 remaining-validation.json，原验收条件原样保留；本工具不修改状态、不判定新的运行通过、不把菜单可见当完整功能。\n\n${table}\n\n## 逐项验收与剩余边界\n\n${gaps}\n## 证据分层\n\n${boundary}\n\n独立原生构建/状态自测、D 真正设备运行、原版配对对照、不同业务迁移分别验收。缺原版证据的独立实现不能将 F 项标为通过。\n`;
const nativeSection=`## 独立原生参考与双重验证\n\n当前清单：R01 ${list.tasks.find(t=>t.id==='R01').status}；R02 ${list.tasks.find(t=>t.id==='R02').status}；R03 ${list.tasks.find(t=>t.id==='R03').status}。\n\n[原生工程与启动](../reference/README.md) · [D运行验收](../reference/run03-acceptance.json) · [原版配对及差异](../comparison/native-run03.md) · [不同业务Fieldnotes验收](../transfer-example/run03-acceptance.md)。\n\nJava Android View、系统EditText/Dialog与本地fixture是独立D。D构建自测、设备运行、来源配对、不同业务迁移分别报告；旧构建修复帧不充当新构建运行通过。原版受限F项继续未完成。\n\n`;
const outputs={'docs/run03-results.md':result.replace('## 本轮尝试记录',nativeSection+'## 本轮尝试记录'),'docs/run03-coverage.md':coverage,'spec/run03-observed-transitions.json':JSON.stringify(transitions,null,2)+'\n'};
if(!process.argv.includes('--check'))for(const [p,body]of Object.entries(outputs))fs.writeFileSync(path.join(root,p),body);
console.log(JSON.stringify({status:process.argv.includes('--check')?'checked-no-writes':'reports-generated',runId,scopeComplete,taskCount:37,statusCounts:counts,outstandingTaskIds:outstanding.map(t=>t.id),safeFrames:nodes.length,withheldSourcesExcluded:withheld,privateSourcesExcluded:privateExcluded,incompleteSourcesExcluded:incompleteExcluded,otherWithheldSourcesExcluded:otherWithheldExcluded,unreviewedSourcesExcluded:pending,outputs:Object.keys(outputs),sharedIndexesModified:false},null,2));
