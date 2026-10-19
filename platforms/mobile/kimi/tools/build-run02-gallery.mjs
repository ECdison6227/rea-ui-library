/** Build the independent, offline run02 original-screen browser, using Node core.
 * No GUI/device operations; no writes to first-run HTML, session or capture files.
 * Run: node research/kimi-android/tools/build-run02-gallery.mjs
 * New scenes require a reviewed exactSha256 entry in run02/gallery-notes.json.
 */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import vm from 'node:vm';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const runPath = 'evidence/runtime/run02-20261016';
const sourcePaths = {session:runPath+'/session.json',notes:runPath+'/gallery-notes.json',transitions:runPath+'/reviewed-transitions.json'};
for(const [name,file] of [['authTransitions','auth-reviewed-transitions.json'],['attachmentReview','attachment-permission-review.json']])if(fs.existsSync(path.join(root,runPath,file)))sourcePaths[name]=runPath+'/'+file;
const hash = bytes => crypto.createHash('sha256').update(bytes).digest('hex');
const esc = value => String(value ?? '').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const jsonHtml = value => JSON.stringify(value).replaceAll('<','\\u003c').replaceAll('\u2028','\\u2028').replaceAll('\u2029','\\u2029');
const sourceBytes = Object.fromEntries(Object.entries(sourcePaths).map(([k,p])=>[k,fs.readFileSync(path.join(root,p))]));
const session=JSON.parse(sourceBytes.session),notes=JSON.parse(sourceBytes.notes),transitions=JSON.parse(sourceBytes.transitions);
const reviewDocuments=[transitions,...['authTransitions','attachmentReview'].filter(k=>sourceBytes[k]).map(k=>JSON.parse(sourceBytes[k]))];
const protectedFiles=['index.html','runtime.html'].filter(p=>fs.existsSync(path.join(root,p)));
const protectedHashes=Object.fromEntries(protectedFiles.map(p=>[p,hash(fs.readFileSync(path.join(root,p)))]));
const categoryNames={model:'模型菜单与选择',draft:'草稿、键盘与提示',font:'字号预览与提交',voice:'语音模式入口',...(notes.categoryNames??{})};
const sourceAssetReads=[];
const readWithinRoot=relative=>{
  const file=path.resolve(root,relative);
  if(!file.startsWith(root+path.sep))throw new Error('Source path outside research root');
  const number=Number(/\/scenes\/r02-(\d+)/.exec(relative)?.[1]);
  if((notes.withholdSceneNumbers??[]).includes(number))throw new Error('Attempt to read withheld scene');
  sourceAssetReads.push(relative);
  return fs.readFileSync(file);
};
const beijing=iso=>iso?new Intl.DateTimeFormat('zh-CN',{timeZone:'Asia/Shanghai',year:'numeric',month:'2-digit',day:'2-digit',hour:'2-digit',minute:'2-digit',second:'2-digit',hour12:false}).format(new Date(iso))+' UTC+08':'未单独记录';
function pngDimensions(bytes){
  if(!bytes.subarray(0,8).equals(Buffer.from([137,80,78,71,13,10,26,10]))||bytes.toString('ascii',12,16)!=='IHDR')throw new Error('Not a PNG');
  return {width:bytes.readUInt32BE(16),height:bytes.readUInt32BE(20),unit:'physical screenshot px',bitDepth:bytes[24],colorType:bytes[25]};
}
function xmlDecode(s){return s.replace(/&(quot|apos|amp|lt|gt);/g,(_,e)=>({quot:'"',apos:"'",amp:'&',lt:'<',gt:'>'}[e]));}
function uiaSamples(bytes){
  if(!bytes)return [];
  return [...bytes.toString('utf8').matchAll(/<node\b[^>]*>/g)].map(m=>Object.fromEntries([...m[0].matchAll(/([\w-]+)=("[^"]*"|'[^']*')/g)].map(a=>[a[1],xmlDecode(a[2].slice(1,-1))]))).filter(a=>a.package===session.package&&(a.class==='android.widget.EditText'||a.class==='android.widget.SeekBar'||a.checkable==='true'||a.bounds==='[917,134][1043,260]'||/^Switch to (?:voice|text) input$/.test(a['content-desc']??'')||a.text==='Hold to talk')).map(a=>({class:a.class,text:a.text,contentDescription:a['content-desc'],boundsPhysicalPx:a.bounds,enabled:a.enabled,clickable:a.clickable,checkable:a.checkable,checked:a.checked,focused:a.focused}));
}

const included=[],excluded=[],pending=[],verified=[],withheld=[];
let previousActionCount=0;
for(const scene of session.scenes){
  const actionCount=scene.actionsBeforeCapture??0;
  const recordedActions=session.actions.slice(previousActionCount,actionCount).map((action,i)=>({index1Based:previousActionCount+i+1,...action}));
  previousActionCount=actionCount;
  if((notes.withholdSceneNumbers??[]).includes(Number(/^r02-(\d+)/.exec(scene.id)?.[1]))){withheld.push({id:scene.id,reason:'按授权范围不读取账号场景原图、层级或采集文件，也不纳入预览。'});continue;}
  const note=notes.scenes[scene.id];
  // Do not even read new/unreviewed images or UIA; login work may be in progress.
  if(!note||note.exactSha256!==scene.screenshot?.sha256){pending.push({id:scene.id,reason:!note?'没有独立 exactSha256 看图注记，未读图或内嵌。':'截图身份变化，须重新看图，未读图或内嵌。'});continue;}
  if(note.personalDataReview!=='clear'){pending.push({id:scene.id,reason:'未明确完成可纳入内容复核，未读图或内嵌。'});continue;}
  const screenshot=readWithinRoot(scene.screenshot.path);
  if(hash(screenshot)!==note.exactSha256)throw new Error('Screenshot hash mismatch: '+scene.id);
  const dimensions=pngDimensions(screenshot);
  if(dimensions.width!==scene.screenshot.width||dimensions.height!==scene.screenshot.height)throw new Error('Screenshot dimension mismatch: '+scene.id);
  const hierarchy=scene.hierarchy?.path?readWithinRoot(scene.hierarchy.path):null;
  if(hierarchy&&hash(hierarchy)!==scene.hierarchy.sha256)throw new Error('Hierarchy hash mismatch: '+scene.id);
  const capturePath=runPath+'/scenes/'+scene.id+'/capture.json';
  const capture=fs.existsSync(path.join(root,capturePath))?readWithinRoot(capturePath):null;
  if(capture){const c=JSON.parse(capture);if(c.id!==scene.id||c.screenshot?.sha256!==note.exactSha256)throw new Error('Capture/session identity mismatch: '+scene.id);}
  for(const doc of reviewDocuments){const reference=doc.scenes?.find(s=>s.id===scene.id);if(reference&&reference.screenshot?.sha256!==note.exactSha256)throw new Error('Reviewed-source screenshot identity mismatch: '+scene.id);}
  const record={id:scene.id,title:note.title,category:note.category,exactSha256:note.exactSha256,screenshotPath:scene.screenshot.path,screenshotBytes:screenshot.length,dimensions,capturePath:capture?capturePath:null,captureSha256:capture?hash(capture):null,screenshotCapturedAt:scene.screenshotCapturedAt??null,captureStartedAt:scene.startedAt,captureCompletedAt:scene.capturedAt,atomicCapture:scene.atomicCapture===true,conditions:scene.conditions,accountModeAtCapture:note.accountModeAtCapture,package:scene.package,serial:scene.serial,avd:scene.avd,apkSha256:scene.apkSha256,hierarchy:scene.hierarchy,hierarchyError:scene.hierarchyError,recordedActionsSincePreviousCapture:recordedActions,actionsBeforeCapture:actionCount,uiaSamples:uiaSamples(hierarchy),observationClassification:scene.classification??'A',origin:scene.origin??'current',observations:note.observations??[],state:note.state,behavior:note.behavior,limits:note.limits??[],personalDataReview:note.personalDataReview};
  verified.push(record);
  if(note.approvedForGallery!==true){excluded.push({...record,reason:note.excludeReason??'未允许作为产品设计样本。'});continue;}
  if(!categoryNames[note.category])categoryNames[note.category]=note.categoryLabel??note.category;
  included.push({...record,image:'data:image/png;base64,'+screenshot.toString('base64')});
}

function actionList(items){
  if(!items.length)return '<p class="quiet">与前一采集点之间没有新增 session.actions 条目；这不排除未入日志的操作，见本帧归因边界。</p>';
  return '<ol class="actions">'+items.map(a=>'<li><b>#'+a.index1Based+' · '+esc(a.operation)+'</b><span>'+esc(a.description)+'</span><code>'+esc(a.arguments.join(' '))+'</code><small>'+esc(beijing(a.startedAt))+' → '+esc(beijing(a.completedAt))+'</small></li>').join('')+'</ol>';
}
function uiaTable(items){
  if(!items.length)return '<p class="quiet">没有匹配的控件样本；完整层级以原件为准。</p>';
  return '<div class="table-scroll"><table><thead><tr><th>层级控件 / 内容</th><th>状态声明</th><th>bounds 物理 px</th></tr></thead><tbody>'+items.map(a=>'<tr><td>'+esc(a.class.replace('android.widget.','').replace('android.view.',''))+'<br>'+esc(a.text||a.contentDescription||'空标签')+'</td><td><code>enabled='+esc(a.enabled)+'<br>clickable='+esc(a.clickable)+'<br>checked='+esc(a.checked)+'<br>focused='+esc(a.focused)+'</code></td><td><code>'+esc(a.boundsPhysicalPx)+'</code></td></tr>').join('')+'</tbody></table></div>';
}
const cards=included.map((s,index)=>'<article id="'+esc(s.id)+'" class="scene" data-category="'+esc(s.category)+'"><div class="scene-kicker"><span>'+esc(categoryNames[s.category])+'</span><code>'+esc(s.id)+'</code></div><h2>'+esc(s.title)+'</h2><p class="state">'+esc(s.state)+'</p><button class="open-shot" data-index="'+index+'" aria-label="查看 '+esc(s.title)+' 原始截图"><img src="'+s.image+'" width="'+s.dimensions.width+'" height="'+s.dimensions.height+'" alt="'+esc(s.title)+' 原始 Android 截图" loading="lazy"><span>放大原图 ↗</span></button><div class="scene-body"><div class="metadata"><span>'+esc(beijing(s.screenshotCapturedAt))+'</span><span>'+s.dimensions.width+' × '+s.dimensions.height+' 物理 px · '+esc(s.accountModeAtCapture)+'</span></div><ul>'+s.observations.map(x=>'<li>'+esc(x)+'</li>').join('')+'</ul><p class="behavior"><b>观察到的路径</b>'+esc(s.behavior)+'</p><details><summary>实际动作、条件与来源</summary><h3>前一采集点至本帧的已记录动作</h3><p class="quiet">动作号为 session.actions 的 1-based 序号。命令描述表达操作意图，结果以原图为准；shell 耗时不等于产品动效时长。</p>'+actionList(s.recordedActionsSincePreviousCapture)+'<h3>本帧采集条件</h3><p><code>'+esc(s.serial)+' / '+esc(s.avd)+'<br>'+esc(s.conditions.size)+'<br>'+esc(s.conditions.density)+'<br>fontScale='+esc(s.conditions.fontScale)+'<br>'+esc(s.conditions.nightMode)+'<br>buildLocale='+esc(s.conditions.buildLocale)+'<br>'+esc(s.conditions.appLocales)+'<br>systemLocales='+esc(s.conditions.systemLocales)+'</code></p><p>截图：<code>'+esc(s.screenshotCapturedAt)+'</code><br>采集记录：<code>'+esc(s.captureStartedAt)+' → '+esc(s.captureCompletedAt)+'</code><br>图像与 UIA 顺序采集，非原子快照。</p><h3>层级样本 · 不能替代像素</h3>'+(s.hierarchy?uiaTable(s.uiaSamples):'<p class="caution">本次没有新的 UIA。'+esc(s.hierarchyError)+'；不代入旧 XML。</p>')+'<h3>归因与适用边界</h3><ul>'+s.limits.map(x=>'<li>'+esc(x)+'</li>').join('')+'</ul><p>原图 SHA-256<br><code>'+esc(s.exactSha256)+'</code></p></details><div class="source-links"><a href="'+esc(s.screenshotPath)+'">原始 PNG</a><a class="download-shot" data-index="'+index+'" href="'+esc(s.screenshotPath)+'" download="'+esc(s.id)+'.png">下载内嵌原图</a>'+(s.capturePath?'<a href="'+esc(s.capturePath)+'">采集 JSON</a>':'')+(s.hierarchy?.path?'<a href="'+esc(s.hierarchy.path)+'">UIA 原件</a>':'')+'</div></div></article>').join('');
const ids=new Set(included.map(s=>s.id));
const visibleTransitions=reviewDocuments.flatMap(doc=>doc.transitions??[]).filter(t=>ids.has(t.from)&&ids.has(t.to));
const transitionsTable='<div class="table-scroll"><table><thead><tr><th>本地复核编号</th><th>起点 → 结果</th><th>已记录动作号</th><th>复核结论与限制</th></tr></thead><tbody>'+visibleTransitions.map(t=>'<tr><td><code>'+esc(t.id)+'</code></td><td><a href="#'+esc(t.from)+'">'+esc(t.from.replace('r02-',''))+'</a> → <a href="#'+esc(t.to)+'">'+esc(t.to.replace('r02-',''))+'</a></td><td>'+esc(t.actionIndexes?.join(', ')||'缺失 / 未隔离')+'</td><td>'+esc(t.result)+'<small>'+esc((t.unknowns??[]).join('；'))+'</small></td></tr>').join('')+'</tbody></table></div>';
const omitted=(items,title)=>items.length?'<section class="omitted"><h2>'+esc(title)+'</h2><ul>'+items.map(s=>'<li><code>'+esc(s.id)+'</code> · '+esc(s.reason)+'</li>').join('')+'</ul></section>':'';
const categoryCounts=Object.fromEntries(Object.keys(categoryNames).map(k=>[k,included.filter(s=>s.category===k).length]));
const html=`<!doctype html><html lang="zh-CN"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><meta name="color-scheme" content="light"><title>Kimi Android · Run02 行为实测</title><style>
:root{font-family:Inter,"Segoe UI","Microsoft YaHei",sans-serif;color:#213b4f;background:#edf2f5;font-size:14px;line-height:1.65}*{box-sizing:border-box}body{margin:0}a{color:#2b638c;text-decoration:none}a:hover{text-decoration:underline}button,select{font:inherit;cursor:pointer}a:focus-visible,button:focus-visible,select:focus-visible{outline:3px solid #74a6ca;outline-offset:3px}header{background:#18374d;color:#fff;padding:43px max(24px,calc((100vw - 1290px)/2));border-bottom:5px solid #90bdb9}.eyebrow{font-size:11px;letter-spacing:.11em;color:#b9d8df}h1{font-size:31px;line-height:1.3;margin:9px 0 13px}header p{max-width:850px;margin:0;color:#d4e3ed}.badges{display:flex;gap:8px;flex-wrap:wrap;margin-top:22px}.badges span{font-size:11px;border:1px solid #5c7c91;padding:3px 9px;border-radius:20px}main{max-width:1338px;padding:26px 24px 50px;margin:auto}.overview{display:grid;grid-template-columns:1fr 1fr;gap:15px;margin-bottom:22px}.overview>div{background:#fff;border:1px solid #d5e0e8;border-radius:11px;padding:18px 21px}.overview b{font-size:13px;color:#4b6b82}.overview p{font-size:12px;color:#718b9c;margin:8px 0 0}.notice{font-size:12px;background:#e1edf3;border-left:3px solid #6d91ab;padding:14px 18px;margin-bottom:23px;border-radius:0 9px 9px 0;color:#4b6c83}.toolbar{display:flex;align-items:center;gap:15px;justify-content:space-between;margin:23px 0}.toolbar select{padding:9px 13px;background:white;border:1px solid #bdceda;color:#3e6178;border-radius:9px}.toolbar p{font-size:12px;color:#7890a0;margin:0}.scene-grid{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:22px;align-items:start}.scene{min-width:0;background:#fff;border:1px solid #d2dfe8;border-radius:13px;overflow:hidden;scroll-margin-top:20px;box-shadow:0 6px 20px #17364c05}.scene-kicker{padding:17px 17px 0;display:flex;justify-content:space-between;gap:8px;align-items:center}.scene-kicker span{font-size:10px;color:#3e7a70;background:#e4f1ed;border-radius:5px;padding:2px 6px;white-space:nowrap}.scene-kicker code{font-size:8px;color:#98a6b0;overflow-wrap:anywhere}h2{font-size:17px;line-height:1.45;margin:11px 17px 8px;letter-spacing:-.3px}.state{margin:0 17px 16px;font-size:11px;color:#7890a1;min-height:36px}.open-shot{display:block;padding:0;width:100%;border:0;border-top:1px solid #e8eef2;border-bottom:1px solid #e2e9ef;position:relative;background:#dce6ec}.open-shot img{width:100%;height:auto;display:block}.open-shot span{position:absolute;right:11px;bottom:11px;font-size:10px;background:#1b384dd9;color:#e8f3f9;padding:5px 10px;border-radius:6px;opacity:0}.open-shot:hover span,.open-shot:focus-visible span{opacity:1}.scene-body{padding:17px}.metadata{display:grid;gap:2px;font-size:9px;color:#8ba0af;border-bottom:1px solid #e7eef3;padding-bottom:12px;margin-bottom:13px}.scene-body ul{font-size:12px;color:#567187;padding-left:17px;margin:0 0 15px}.scene-body li{margin:6px 0}.behavior{background:#f1f6f8;color:#536f84;font-size:12px;padding:11px 13px;border-radius:8px;margin:15px 0}.behavior b{display:block;font-size:10px;color:#8ba0ac;margin-bottom:3px}.scene-body details{font-size:10px;color:#7d93a3;overflow-wrap:anywhere}.scene-body summary{cursor:pointer;color:#3e6a88}.scene-body details ul{font-size:10px;color:#7d93a3}.scene-body details p{margin:12px 0}.scene-body h3{font-size:11px;color:#4f6f86;margin:16px 0 7px}.scene-body code{font-size:9px}.quiet{font-size:10px;color:#8c9eab}.actions{padding-left:19px;margin:8px 0}.actions li{padding-bottom:10px}.actions b,.actions span,.actions code,.actions small{display:block}.actions b{color:#688399}.actions small{font-size:9px;color:#9badb8}.caution{color:#9a8055;background:#faf4e8;padding:10px;border-radius:6px}.source-links{display:flex;flex-wrap:wrap;gap:8px 13px;font-size:10px;border-top:1px solid #e5edf2;padding-top:13px;margin-top:14px}.table-scroll{overflow:auto;border:1px solid #d6e1e9;border-radius:10px;background:white}table{border-collapse:collapse;width:100%;font-size:11px}td,th{padding:10px 13px;text-align:left;border-bottom:1px solid #e4ecf1;vertical-align:top}th{color:#6a879b;background:#e9f0f5;font-size:10px}td small{display:block;color:#93a4b1;font-size:10px;margin-top:5px}td code{font-size:9px}.scene-body table{font-size:9px}.scene-body td,.scene-body th{padding:6px}.scene-body td code{font-size:8px}.transition-section{margin-top:39px}.transition-section h2,.omitted h2{margin:0 0 12px;font-size:18px}.transition-section>p{font-size:12px;color:#7e95a5}.omitted{background:#f5f7f9;border:1px solid #d5e1e9;border-radius:10px;padding:19px 23px;margin-top:27px;color:#8398a7;font-size:11px}.omitted h2{font-size:14px;color:#607f95}.omitted ul{padding-left:17px;margin:0}.omitted code{font-size:10px}.omitted li{margin:6px 0}footer{margin-top:32px;padding-top:20px;border-top:1px solid #cfdee7;font-size:11px;color:#8097a6;overflow-wrap:anywhere}.footer-links{display:flex;gap:11px 18px;flex-wrap:wrap;margin-bottom:13px}footer p{margin:6px 0}footer code{font-size:9px}.hidden{display:none!important}dialog{background:#142e41;color:#e5f1f9;border:1px solid #4b6b81;border-radius:12px;padding:0;max-width:95vw;max-height:96vh}dialog::backdrop{background:#0d263ad9}.viewer-bar{display:flex;align-items:center;justify-content:space-between;gap:12px;padding:12px 15px;position:sticky;top:0;background:#142e41}.viewer-bar b{font-size:12px;font-weight:500}.viewer-bar button{background:#284b63;border:1px solid #4e728c;border-radius:7px;padding:4px 12px;color:#e0eff9;font-size:11px}.viewer-image{display:block;width:auto;height:auto;max-width:91vw;max-height:82vh;margin:auto}.viewer-caption{font-size:10px;color:#94b2c7;padding:9px 15px;max-width:700px}noscript{display:block;font-size:12px;padding:15px;margin-bottom:20px;background:#fff0d6}@media(max-width:1000px){.scene-grid{grid-template-columns:repeat(2,minmax(0,1fr))}h1{font-size:27px}}@media(max-width:650px){header{padding:29px 20px}main{padding:21px 15px 40px}h1{font-size:23px}.overview{grid-template-columns:1fr}.toolbar{align-items:stretch;flex-direction:column;gap:8px}.scene-grid{grid-template-columns:1fr}.scene{max-width:430px;width:100%;margin:auto}.state{min-height:0}.open-shot span{opacity:1}}
</style></head><body><header><div class="eyebrow">REA STYLE LIBRARY / RUN02 · 2026-10-16</div><h1>Kimi Android · 模型、编辑与会话实测</h1><p>第二轮原版运行观察：从未登录的模型与偏好编辑，继续到用户辅助登录后的中性会话、附件、权限与恢复测试。所有样本来自当前 APK 的原始截图，按实际像素描述，不按目录名或命令意图改写结果。</p><div class="badges"><span>A · 当前实际观察</span><span>${included.length} 张安全运行原图</span><span>${excluded.length} 张环境 ANR 排除</span><span>账号条件逐帧保留</span><span>单文件离线预览</span></div></header><main><div class="overview"><div><b>本轮看到了什么</b><p>模型菜单与短草稿、字号预览 / 放弃 / 保存；安全登录后样本补充提交完成、停止结果、可移除附件、麦克风拒绝恢复、网络重试与自建答案复制粘贴。</p></div><div><b>每条结论的边界</b><p>早期草稿只插入 Re；后续完整消息与回答分别保留。截图和层级顺序采集；早图与后层级不合成同一时刻。25 不验证停止，26 的停止与工具错误不造因果；稀疏帧不测时长。</p></div></div><div class="notice">环境记录：1080 × 2400 物理 px、420 dpi、fontScale 1.0、系统 night mode=no、appLocales=en-US、buildLocale=en-US；systemLocales 字段为 null。时间显示北京时间，并在来源详情保留 UTC。蓝环按欢迎区头像 / 动效采样候选处理，不能据此称加载或网络失败。此页是原图研究浏览器，网页布局属于独立展示 D。</div><div class="toolbar"><select id="category-filter" aria-label="筛选观察主题"><option value="">全部主题 · ${included.length} 张</option>${Object.entries(categoryNames).map(([k,v])=>'<option value="'+esc(k)+'">'+esc(v)+' · '+categoryCounts[k]+'</option>').join('')}</select><p id="filter-count" role="status" aria-live="polite">显示 ${included.length} 张原图 · 点图放大，展开查看实际动作</p></div><noscript>原图与说明可直接阅读。主题筛选、放大和内嵌原图下载需要 JavaScript；随目录保存时，PNG 原件链接仍可使用。</noscript><div class="scene-grid">${cards}</div><section class="transition-section"><h2>转移索引 · 从前态对照后态</h2><p>来自本轮独立复核 JSON；R02-T / AUTH-T 是本地编号，不是 REA Evidence ID。点击起终点回看原图；缺失动作和没有隔离的中间帧继续保留。</p>${transitionsTable}</section>${omitted(excluded,'环境记录排除 · 已看图和核验，但不内嵌为 Kimi 样式')}${omitted(withheld,'账号场景 · 未读取或纳入')}${omitted(pending,'新增 / 变化记录 · 等待 exactSha256 独立看图注记')}<footer><div class="footer-links"><a href="${sourcePaths.session}">本轮操作 Session ↗</a><a href="${sourcePaths.transitions}">早期已复核转移 ↗</a>${sourcePaths.authTransitions?'<a href="'+esc(sourcePaths.authTransitions)+'">安全会话转移 ↗</a>':''}${sourcePaths.attachmentReview?'<a href="'+esc(sourcePaths.attachmentReview)+'">附件 / 权限记录 ↗</a>':''}<a href="${sourcePaths.notes}">独立看图注记 ↗</a><a href="${runPath}/gallery-verification.json">哈希与生成验证 ↗</a><a href="spec/run02-behavior-verification.md">未登录行为复核 ↗</a><a href="spec/run02-authenticated-behavior.md">安全会话复核 ↗</a><a href="spec/run02-attachments-and-permissions.md">附件 / 权限复核 ↗</a><a href="tools/build-run02-gallery.mjs">生成脚本 ↗</a><a href="runtime.html">首轮运行样本 ↗</a></div><p>源 APK SHA-256：<code>${esc(session.apkSha256)}</code></p><p>本页依据 Session 快照 SHA-256：<code>${hash(sourceBytes.session)}</code></p><p>原图以原始 PNG 字节内嵌，未裁切、重绘或覆盖。新增图片只有精确哈希匹配、明确纳入且完成内容复核后才会进入预览；未知帧不会自动读取。原始 session、capture、首轮 runtime.html 与 index.html 均未修改。</p><p>生成于 ${new Date().toISOString()} · <code>node research/kimi-android/tools/build-run02-gallery.mjs</code></p></footer></main><dialog id="viewer" aria-labelledby="viewer-title"><div class="viewer-bar"><b id="viewer-title"></b><button id="viewer-close" type="button">关闭 ×</button></div><img id="viewer-image" class="viewer-image" alt=""><div id="viewer-caption" class="viewer-caption"></div></dialog><script id="viewer-data" type="application/json">${jsonHtml(included.map(s=>({id:s.id,title:s.title,sha256:s.exactSha256,width:s.dimensions.width,height:s.dimensions.height})))}</script><script>
'use strict';
const data=JSON.parse(document.getElementById('viewer-data').textContent);
const scenes=[...document.querySelectorAll('.scene')],shots=[...document.querySelectorAll('.open-shot')];
document.getElementById('category-filter').addEventListener('change',event=>{let count=0;for(const scene of scenes){const hidden=!!event.target.value&&scene.dataset.category!==event.target.value;scene.classList.toggle('hidden',hidden);if(!hidden)count++;}document.getElementById('filter-count').textContent='显示 '+count+' 张原图 / 共 '+data.length+' 张';});
for(const download of document.querySelectorAll('.download-shot'))download.href=shots[Number(download.dataset.index)].querySelector('img').src;
const viewer=document.getElementById('viewer');
for(const shot of shots)shot.addEventListener('click',()=>{const item=data[Number(shot.dataset.index)];document.getElementById('viewer-title').textContent=item.title;const img=document.getElementById('viewer-image');img.src=shot.querySelector('img').src;img.alt=item.title+' 原始 Android 截图';document.getElementById('viewer-caption').textContent=item.width+' × '+item.height+' 物理 px · 适应窗口缩放展示 · SHA-256 '+item.sha256;viewer.showModal();});
document.getElementById('viewer-close').addEventListener('click',()=>viewer.close());
viewer.addEventListener('click',event=>{if(event.target===viewer){const r=viewer.getBoundingClientRect();if(event.clientX<r.left||event.clientX>r.right||event.clientY<r.top||event.clientY>r.bottom)viewer.close();}});
</script></body></html>`;

// Meaningful offline checks: exact source/embedded-byte identity, local links,
// and actual filter/viewer/download script behavior in a small DOM fixture.
const viewerDataMatch=/<script id="viewer-data" type="application\/json">([\s\S]*?)<\/script>/.exec(html);
const browserScript=[...html.matchAll(/<script>([\s\S]*?)<\/script>/g)][0][1];
new vm.Script(browserScript,{filename:'run02.html:inline-script'});
const embedded=[...html.matchAll(/<img src="(data:image\/png;base64,([^"]+))"/g)];
if(embedded.length!==included.length)throw new Error('Embedded image count mismatch');
embedded.forEach((m,i)=>{if(hash(Buffer.from(m[2],'base64'))!==included[i].exactSha256)throw new Error('Embedded PNG hash mismatch');});
class TestElement{
  constructor(){this.listeners={};this.dataset={};this.classes=new Set();this.classList={toggle:(k,on)=>on?this.classes.add(k):this.classes.delete(k)};}
  addEventListener(event,fn){this.listeners[event]=fn;}
  showModal(){this.open=true;}
  close(){this.open=false;}
  querySelector(){return this.img;}
  getBoundingClientRect(){return {left:10,right:100,top:10,bottom:100};}
}
const elements=Object.fromEntries(['viewer-data','viewer','viewer-title','viewer-image','viewer-caption','viewer-close','category-filter','filter-count'].map(k=>[k,new TestElement()]));
elements['viewer-data'].textContent=viewerDataMatch[1];
const fixtureScenes=included.map(s=>Object.assign(new TestElement(),{dataset:{category:s.category}}));
const fixtureShots=embedded.map((m,i)=>Object.assign(new TestElement(),{dataset:{index:String(i)},img:{src:m[1]}}));
const fixtureDownloads=embedded.map((m,i)=>Object.assign(new TestElement(),{dataset:{index:String(i)}}));
const document={getElementById:id=>elements[id],querySelectorAll:selector=>selector==='.scene'?fixtureScenes:selector==='.open-shot'?fixtureShots:selector==='.download-shot'?fixtureDownloads:[]};
vm.runInNewContext(browserScript,{document});
for(const category of Object.keys(categoryNames)){
  elements['category-filter'].listeners.change({target:{value:category}});
  if(fixtureScenes.filter(s=>!s.classes.has('hidden')).length!==categoryCounts[category])throw new Error('Category filter failed');
}
elements['category-filter'].listeners.change({target:{value:''}});
if(fixtureScenes.some(s=>s.classes.has('hidden')))throw new Error('Filter reset failed');
if(included.length){fixtureShots[0].listeners.click();if(!elements.viewer.open||elements['viewer-image'].src!==embedded[0][1])throw new Error('Viewer open failed');elements['viewer-close'].listeners.click();if(elements.viewer.open)throw new Error('Viewer close failed');}
if(fixtureDownloads.some((d,i)=>d.href!==embedded[i][1]))throw new Error('Embedded download failed');
const verificationPath=runPath+'/gallery-verification.json';
const missingLinks=[...html.matchAll(/href="([^"]+)"/g)].map(m=>m[1]).filter(p=>!p.startsWith('#')&&p!==verificationPath&&!fs.existsSync(path.join(root,p)));
if(missingLinks.length)throw new Error('Missing local links: '+missingLinks.join(', '));
for(const [file,before] of Object.entries(protectedHashes))if(hash(fs.readFileSync(path.join(root,file)))!==before)throw new Error('Protected first-run HTML changed');
fs.writeFileSync(path.join(root,'run02.html'),html);
const report={schemaVersion:1,runId:notes.runId,generatedAt:new Date().toISOString(),output:{path:'run02.html',bytes:Buffer.byteLength(html),sha256:hash(html)},generator:{path:'tools/build-run02-gallery.mjs',sha256:hash(fs.readFileSync(fileURLToPath(import.meta.url)))},sources:Object.fromEntries(Object.entries(sourcePaths).map(([k,p])=>[k,{path:p,sha256:hash(sourceBytes[k])}])),reviewMethod:notes.reviewMethod,inclusionPolicy:notes.inclusionPolicy,summary:{sessionScenes:session.scenes.length,exactReviewedScenes:verified.length,embeddedScenes:included.length,excludedScenes:excluded.length,pendingScenes:pending.length,withheldScenes:withheld.length,sourceScreenshotHashesVerified:verified.length,sourceHierarchyHashesVerified:verified.filter(s=>s.hierarchy).length,embeddedPngHashesVerified:embedded.length,categoryCounts},included:included.map(({image,...s})=>s),excluded,pending,withheld,checks:{pngDimensions:true,exactShaBindings:true,captureSessionIdentity:true,reviewedTransitionIdentity:true,embeddedOriginalBytes:true,allLocalLinksExist:true,browserScriptSyntax:true,browserScriptFixture:['all categories filter','reset filter','open original viewer','close viewer','embedded download byte reuse'],protectedFirstRunFiles:Object.entries(protectedHashes).map(([path,sha256])=>({path,sha256,unchanged:true})),sourceScreenshotsWritten:0,sourceRecordsWritten:0,sourceAssetReadCount:sourceAssetReads.length,withheldSceneSourceReads:0,guiOrDeviceOperations:false,realBrowserVisualValidation:false},scopeNotes:notes.scopeNotes};
fs.writeFileSync(path.join(root,verificationPath),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify({output:report.output,summary:report.summary,pending:report.pending,withheld:report.withheld,checks:report.checks},null,2));
