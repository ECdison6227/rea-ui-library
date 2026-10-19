/** Offline original-screen evidence viewer; no device/GUI operations or deps.
 * Run from workspace: node research/kimi-android/tools/build-runtime-gallery.mjs
 * Additional reviewed scenes can be added to tools/runtime-gallery-notes.json;
 * each annotation is tied to an exact screenshot hash. Original records unchanged.
 */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import zlib from 'node:zlib';
import vm from 'node:vm';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const sessionPath = 'evidence/runtime/session.json';
const notesPath = 'tools/runtime-gallery-notes.json';
const sessionBytes = fs.readFileSync(path.join(root, sessionPath));
const notesBytes = fs.readFileSync(path.join(root, notesPath));
const session = JSON.parse(sessionBytes), notes = JSON.parse(notesBytes);
const sha = bytes => crypto.createHash('sha256').update(bytes).digest('hex');
const esc = value => String(value ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const htmlJson = value => JSON.stringify(value).replaceAll('<','\\u003c').replaceAll('\u2028','\\u2028').replaceAll('\u2029','\\u2029');
const relative = file => path.relative(root, file).replaceAll('\\','/');
const saveJson = (name, value) => {const file=path.join(root,name);fs.mkdirSync(path.dirname(file),{recursive:true});fs.writeFileSync(file,JSON.stringify(value,null,2)+'\n');};
const excludedReasons = {
  '01-first-launch': '安装完成前启动失败，仅环境审计记录。',
  '02-kimi-initial': 'Android System UI ANR 干扰，排除 Kimi 原生设计证据。',
  '06-after-agree': '过渡截图与稍后 UI hierarchy 不一致，排除设计与状态结论。'
};
const kindNames = {'app-ui':'Kimi 页面','system-result':'系统结果','system-permission':'Android 系统权限','unconfirmed-result':'动作结果待证'};

function pngHeader(bytes) {
  if(!bytes.subarray(0,8).equals(Buffer.from([137,80,78,71,13,10,26,10])))throw new Error('Not a PNG');
  if(bytes.toString('ascii',12,16)!=='IHDR')throw new Error('Missing PNG IHDR');
  return {width:bytes.readUInt32BE(16),height:bytes.readUInt32BE(20),bitDepth:bytes[24],colorType:bytes[25],compression:bytes[26],filter:bytes[27],interlace:bytes[28]};
}
/** Exact PNG unfilter for the known 8-bit RGBA/RGB non-interlaced screencaps.
 * Color values are composited screenshot samples, never source theme values. */
function decodePng(bytes) {
  const h=pngHeader(bytes);
  if(h.bitDepth!==8||![2,6].includes(h.colorType)||h.interlace!==0||h.compression!==0||h.filter!==0)throw new Error('Unsupported sample PNG format');
  const bpp=h.colorType===6?4:3,stride=h.width*bpp,parts=[];
  let cursor=8;
  while(cursor+12<=bytes.length){const length=bytes.readUInt32BE(cursor),type=bytes.toString('ascii',cursor+4,cursor+8);if(cursor+length+12>bytes.length)throw new Error('Truncated PNG');if(type==='IDAT')parts.push(bytes.subarray(cursor+8,cursor+8+length));cursor+=length+12;if(type==='IEND')break;}
  const packed=zlib.inflateSync(Buffer.concat(parts));
  if(packed.length!==(stride+1)*h.height)throw new Error('Unexpected decompressed PNG length');
  const pixels=Buffer.alloc(stride*h.height);
  const paeth=(a,b,c)=>{const p=a+b-c,pa=Math.abs(p-a),pb=Math.abs(p-b),pc=Math.abs(p-c);return pa<=pb&&pa<=pc?a:pb<=pc?b:c;};
  for(let y=0;y<h.height;y++){
    const filter=packed[y*(stride+1)],offset=y*(stride+1)+1;
    if(filter>4)throw new Error('Unsupported PNG filter');
    for(let x=0;x<stride;x++){
      const left=x>=bpp?pixels[y*stride+x-bpp]:0,above=y?pixels[(y-1)*stride+x]:0,upperLeft=y&&x>=bpp?pixels[(y-1)*stride+x-bpp]:0;
      const predict=[0,left,above,Math.floor((left+above)/2),paeth(left,above,upperLeft)][filter];
      pixels[y*stride+x]=(packed[offset+x]+predict)&255;
    }
  }
  return {header:h,pixels,bpp,stride};
}
function samplePixels(bytes,samples) {
  if(!samples?.length)return [];
  const png=decodePng(bytes);
  return samples.map(s=>{
    if(![s.x,s.y,s.width,s.height].every(Number.isInteger)||s.x<0||s.y<0||s.width<=0||s.height<=0||s.x+s.width>png.header.width||s.y+s.height>png.header.height)throw new Error('Pixel sample outside image');
    const histogram=new Map();
    for(let y=s.y;y<s.y+s.height;y++)for(let x=s.x;x<s.x+s.width;x++){
      const offset=y*png.stride+x*png.bpp,color=[...png.pixels.subarray(offset,offset+png.bpp)];if(png.bpp===3)color.push(255);
      const key=color.join(',');histogram.set(key,(histogram.get(key)||0)+1);
    }
    const top=[...histogram.entries()].sort((a,b)=>b[1]-a[1]);
    return {...s,unit:'physical screenshot px; origin top-left; x/y inclusive, right/bottom exclusive',pixelCount:s.width*s.height,distinctRgba:histogram.size,mostFrequent:top.slice(0,8).map(([rgba,count])=>({rgba:rgba.split(',').map(Number),count})),interpretation:'Composited screenshot pixels only. Not a code color token, whole-page measurement, dp/sp value or font-size estimate.'};
  });
}
function formatTime(iso) {
  if(!iso)return '未记录';
  return new Intl.DateTimeFormat('zh-CN',{timeZone:'Asia/Shanghai',year:'numeric',month:'2-digit',day:'2-digit',hour:'2-digit',minute:'2-digit',second:'2-digit',hour12:false}).format(new Date(iso))+' UTC+08';
}

const included=[],excluded=[],pending=[];
for(const scene of session.scenes) {
  if(excludedReasons[scene.id]||/excluded/i.test(scene.validity??'')||scene.classification==='environment-only'){
    excluded.push({id:scene.id,reason:excludedReasons[scene.id]??scene.visualReview??scene.validity});continue;
  }
  const note=notes.scenes[scene.id];
  if(!note){pending.push({id:scene.id,reason:'尚未在独立预览注记中完成像素复核；不自动按目录名纳入。'});continue;}
  if(note.screenshotSha256!==scene.screenshot?.sha256){pending.push({id:scene.id,reason:'截图哈希与已复核注记不同，需要重新看图。'});continue;}
  const sourceFile=path.resolve(root,scene.screenshot.path);
  if(!sourceFile.startsWith(root+path.sep))throw new Error('Screenshot path outside research root');
  const bytes=fs.readFileSync(sourceFile);
  if(sha(bytes)!==scene.screenshot.sha256)throw new Error('Screenshot SHA mismatch: '+scene.id);
  const dimensions=pngHeader(bytes);
  if(dimensions.width!==scene.screenshot.width||dimensions.height!==scene.screenshot.height)throw new Error('Screenshot dimensions mismatch: '+scene.id);
  const hierarchyBytes=scene.hierarchy?.path?fs.readFileSync(path.join(root,scene.hierarchy.path)):null;
  if(hierarchyBytes&&sha(hierarchyBytes)!==scene.hierarchy.sha256)throw new Error('Hierarchy SHA mismatch: '+scene.id);
  const actionIndex=(scene.actionsBeforeCapture??0)-1;
  const action=actionIndex>=0?session.actions[actionIndex]:null;
  const pixels=samplePixels(bytes,note.pixelSamples);
  included.push({scene,note,dimensions,pixels,action,sourceFile,sourceBytes:bytes.length,image:'data:image/png;base64,'+bytes.toString('base64')});
}
const kindCounts=Object.fromEntries(Object.keys(kindNames).map(kind=>[kind,included.filter(item=>item.note.kind===kind).length]));
const sourceReports=included.map(({scene,note,dimensions,pixels,action,sourceBytes})=>({id:scene.id,title:note.title,kind:note.kind,classification:scene.classification,origin:scene.origin,screenshotPath:scene.screenshot.path,screenshotSha256:scene.screenshot.sha256,sourceBytes,dimensions,screenshotCapturedAt:scene.screenshotCapturedAt??null,captureRecordCompletedAt:scene.capturedAt,atomicCapture:scene.atomicCapture===true,conditions:scene.conditions,hierarchy:scene.hierarchy,observations:note.observations,behavior:note.behavior,limitations:note.limitations,lastRecordedAction:action,pixelSamples:pixels}));
const manifestPath='assets/derived/runtime-gallery/manifest.json';
saveJson(manifestPath,{schemaVersion:1,generatedAt:new Date().toISOString(),purpose:'Offline original-screen evidence viewer; screenshot bytes unchanged; no native reconstruction',sourceApkSha256:session.apkSha256,sessionPath,sessionSha256:sha(sessionBytes),annotationPath:notesPath,annotationSha256:sha(notesBytes),reviewMethod:notes.reviewMethod,method:'Original PNG base64 embedding; dimensions from PNG IHDR; optional exact RGBA-region sampling using core Node zlib + PNG unfilter',captureBoundary:'Screenshots and UIAutomator dumps are sequential, not an atomic state snapshot. Where screenshotCapturedAt is absent, capturedAt is the record completion time, not a precise screenshot instant.',included:sourceReports,excluded,pending});

const cards=included.map(({scene,note,dimensions,pixels,action,image},index)=>{
  const time=scene.screenshotCapturedAt??scene.capturedAt;
  const timeLabel=scene.screenshotCapturedAt?'截图时间':'采集记录完成';
  const conditions=scene.conditions??{};
  const sceneMetaPath='evidence/runtime/scenes/'+scene.id+'/capture.json';
  const pixelText=pixels.map(s=>'<div class="pixel-note"><b>精确区域采样 · '+esc(s.id)+'</b><p>原图左上为 (0,0)；区域 [x='+s.x+', y='+s.y+', w='+s.width+', h='+s.height+']，共 '+s.pixelCount.toLocaleString('en-US')+' 个物理像素；'+s.distinctRgba+' 种 RGBA。</p><p>最多值 '+esc(JSON.stringify(s.mostFrequent[0].rgba))+'，'+s.mostFrequent[0].count.toLocaleString('en-US')+' px。只描述该合成图区域，不推导源码色值、全页背景或字号。</p></div>').join('');
  return '<article class="scene" id="'+esc(scene.id)+'" data-kind="'+esc(note.kind)+'"><div class="scene-top"><span class="kind '+esc(note.kind)+'">'+esc(kindNames[note.kind]??note.kind)+'</span><code>'+esc(scene.id)+'</code></div><h2>'+esc(note.title)+'</h2><p class="state">'+esc(note.state)+'</p><button class="shot-open" data-index="'+index+'" aria-label="放大查看 '+esc(note.title)+' 原始截图"><img src="'+image+'" width="'+dimensions.width+'" height="'+dimensions.height+'" alt="'+esc(note.title)+'，原始 Android 屏幕截图" loading="lazy"><span>查看完整原图 ↗</span></button><div class="scene-body"><div class="meta"><p><b>'+timeLabel+'</b><span>'+esc(formatTime(time))+'</span></p><p><b>原图尺寸</b><span>'+dimensions.width+' × '+dimensions.height+' 物理 px</span></p><p><b>记录条件</b><span>'+esc(conditions.density??'密度未记录')+' · fontScale '+esc(conditions.fontScale??'未知')+'</span></p></div><ul>'+note.observations.map(s=>'<li>'+esc(s)+'</li>').join('')+'</ul><p class="behavior"><b>操作 / 状态</b><br>'+esc(note.behavior)+'</p>'+pixelText+'<details><summary>来源、动作与证据边界</summary><p><b>最近记录动作</b><br>'+esc(action?.description??'未记录')+'</p>'+(action?'<p><code>'+esc(action.arguments.join(' '))+'</code><br>'+esc(formatTime(action.completedAt))+'</p>':'')+'<p><b>采集时间</b><br><code>'+esc(scene.screenshotCapturedAt??'未单独记录截图时间')+'</code><br>记录完成：<code>'+esc(scene.capturedAt)+'</code></p><p><b>截图与层级</b><br>截图和 UIAutomator dump 顺序采集，不能当作同一时刻的原子快照。'+(!scene.screenshotCapturedAt?' 早期记录仅保存完成时间。':'')+'</p><p><b>设备 / 模式</b><br>'+esc(scene.serial)+' · '+esc(scene.avd)+'<br>'+esc(conditions.nightMode??'未知主题')+'<br>'+esc(conditions.buildLocale?'buildLocale: '+conditions.buildLocale:'系统语言未单独核准')+'<br>'+esc(conditions.appLocales??'appLocales 未单独记录')+'</p><p><b>原图 SHA-256</b><br><code>'+esc(scene.screenshot.sha256)+'</code></p><ul>'+note.limitations.map(s=>'<li>'+esc(s)+'</li>').join('')+'</ul></details><div class="source-links"><a href="'+esc(scene.screenshot.path)+'">PNG 原件</a><a href="'+esc(scene.screenshot.path)+'" class="download-shot" data-index="'+index+'" download="'+esc(scene.id)+'.png">下载内嵌原图</a>'+(fs.existsSync(path.join(root,sceneMetaPath))?'<a href="'+esc(sceneMetaPath)+'">采集 JSON</a>':'')+(scene.hierarchy?.path?'<a href="'+esc(scene.hierarchy.path)+'">UI 层级</a>':'')+'</div></div></article>';
}).join('');
const omitted=(list,title)=>list.length?'<div class="omitted"><h3>'+esc(title)+'</h3><ul>'+list.map(s=>'<li><code>'+esc(s.id)+'</code>：'+esc(s.reason)+'</li>').join('')+'</ul></div>':'';
const optionalLinks=['README.md','index.html','docs/coverage.md'].filter(p=>fs.existsSync(path.join(root,p))).map(p=>'<a href="'+esc(p)+'">'+(p==='index.html'?'静态素材研究台':esc(p))+' ↗</a>').join('');
const html=`<!doctype html><html lang="zh-CN"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><meta name="color-scheme" content="light"><title>Kimi Android · 原版运行样本</title><style>
:root{font-family:Inter,"Segoe UI","Microsoft YaHei",sans-serif;color:#21394d;background:#edf2f5;font-size:14px;line-height:1.6}*{box-sizing:border-box}body{margin:0}a{color:#2a6188;text-decoration:none}a:hover{text-decoration:underline}button,select{font:inherit}button{cursor:pointer}button:focus-visible,a:focus-visible,select:focus-visible{outline:3px solid #71a4c7;outline-offset:3px}header{background:#18364c;color:#fff;padding:44px max(24px,calc((100vw - 1260px)/2));border-bottom:5px solid #8bbbb8}.eyebrow{color:#bad6df;font-size:11px;letter-spacing:.1em}h1{font-size:31px;line-height:1.3;margin:10px 0 14px}header p{max-width:820px;color:#d9e5ed;margin:0}.top-tags{display:flex;flex-wrap:wrap;gap:8px;margin-top:21px}.top-tags span{border:1px solid #5e7c90;color:#dcebf3;font-size:11px;padding:3px 9px;border-radius:20px}main{max-width:1308px;margin:0 auto;padding:28px 24px 60px}.intro{display:grid;grid-template-columns:1fr auto;gap:20px;align-items:center;margin-bottom:24px}.intro p{color:#637d8f;font-size:12px;margin:0;max-width:850px}.intro select{background:white;border:1px solid #c6d5df;border-radius:9px;padding:9px 14px;color:#36596f}.evidence-note{padding:15px 19px;background:#e2edf3;border-left:3px solid #628aa4;border-radius:0 10px 10px 0;font-size:12px;color:#46677f;margin-bottom:27px}.scene-grid{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:22px;align-items:start}.scene{border:1px solid #d2dfe8;background:#fff;border-radius:14px;overflow:hidden;min-width:0;box-shadow:0 5px 18px #18384d05}.scene-top{display:flex;align-items:center;justify-content:space-between;gap:10px;padding:17px 18px 0}.scene-top code{color:#8b9ba7;font-size:9px;overflow-wrap:anywhere}.kind{display:inline-block;font-size:10px;border-radius:5px;padding:2px 7px;background:#e4f0ee;color:#337366;white-space:nowrap}.kind.system-result,.kind.system-permission{background:#e9e9f5;color:#64628e}.kind.unconfirmed-result{background:#faf0dd;color:#93733d}h2{font-size:17px;margin:11px 18px 8px;line-height:1.4;letter-spacing:-.3px}.state{color:#718797;font-size:11px;margin:0 18px 17px;min-height:36px}.shot-open{padding:0;border:0;border-top:1px solid #e9eef1;border-bottom:1px solid #e2e9ee;display:block;width:100%;background:#dfe8ed;position:relative}.shot-open img{width:100%;height:auto;display:block;object-fit:contain}.shot-open span{position:absolute;right:10px;bottom:12px;background:#1a354dd9;color:white;padding:5px 10px;border-radius:6px;font-size:10px;opacity:0;transition:opacity .12s}.shot-open:hover span,.shot-open:focus-visible span{opacity:1}.scene-body{padding:18px}.meta{padding-bottom:12px;border-bottom:1px solid #e6edf2;margin-bottom:15px}.meta p{display:flex;gap:10px;font-size:10px;margin:4px 0}.meta b{color:#587386;min-width:72px}.meta span{color:#8a9ba7}.scene-body ul{margin:0 0 16px;padding-left:17px;font-size:12px;color:#506c80}.scene-body li{margin:6px 0}.behavior{font-size:12px;color:#527085;background:#f1f6f8;border-radius:8px;padding:12px;margin:0 0 15px}.behavior b{font-size:10px;color:#8b9ca7}.pixel-note{font-size:10px;background:#f3f4ee;color:#748068;padding:11px;border:1px solid #e2e5d8;border-radius:8px;margin-bottom:14px}.pixel-note b{color:#69795f}.pixel-note p{margin:6px 0}.scene-body details{font-size:10px;color:#7c8f9d;overflow-wrap:anywhere}.scene-body summary{cursor:pointer;color:#426b86}.scene-body details p{margin:13px 0}.scene-body details ul{font-size:10px;color:#8799a5}.scene-body code{font-size:9px}.source-links{display:flex;gap:9px 13px;flex-wrap:wrap;font-size:10px;padding-top:13px;border-top:1px solid #edf1f4;margin-top:15px}.omitted{margin:28px 0;padding:19px 23px;background:#f5f7f8;border:1px solid #d9e3e9;border-radius:11px;color:#7a8e9c;font-size:12px}.omitted h3{margin:0 0 10px;font-size:13px;color:#5c7587}.omitted ul{margin:0;padding-left:17px}.omitted li{margin:6px 0}.omitted code{font-size:10px}footer{margin-top:35px;padding-top:19px;border-top:1px solid #d1dee7;color:#78909f;font-size:11px;overflow-wrap:anywhere}.footer-links{display:flex;gap:17px;flex-wrap:wrap;margin-bottom:14px}footer p{margin:7px 0}footer code{font-size:9px}dialog{border:1px solid #526d81;border-radius:13px;padding:0;background:#142d40;color:#e5f1f8;max-width:94vw;max-height:96vh}dialog::backdrop{background:#132837d9}.viewer-bar{display:flex;gap:9px;align-items:center;justify-content:space-between;padding:12px 16px;position:sticky;top:0;background:#142d40;z-index:1}.viewer-bar b{font-size:12px;font-weight:500}.viewer-bar button{background:#28485f;color:#dcebf5;border:1px solid #506c82;border-radius:7px;padding:5px 12px;font-size:11px}.viewer-image{display:block;max-width:90vw;max-height:82vh;width:auto;height:auto;margin:auto}.viewer-caption{font-size:10px;color:#93b0c3;padding:9px 16px;max-width:700px}.hidden{display:none!important}noscript{font-size:12px;display:block;background:#fff0d5;padding:15px;margin-bottom:20px}@media(max-width:1000px){.scene-grid{grid-template-columns:repeat(2,minmax(0,1fr))}h1{font-size:27px}}@media(max-width:650px){header{padding:30px 20px}h1{font-size:23px}main{padding:22px 15px 40px}.intro{grid-template-columns:1fr}.intro select{width:100%}.scene-grid{grid-template-columns:1fr;gap:24px}.scene{max-width:430px;width:100%;margin:auto}.state{min-height:0}.shot-open span{opacity:1}}
</style></head><body><header><div class="eyebrow">REA STYLE LIBRARY / ORIGINAL ANDROID RUNTIME</div><h1>Kimi Android · 原版运行样本</h1><p>当前 APK 在专用 Android 模拟器中的真实截图。保留完整原图和操作路径，分别标注 Kimi 自有界面、Android 系统结果，以及尚未证实的动作结果。</p><div class="top-tags"><span>A · 当前实际观察</span><span>${included.length} 个已看图样本</span><span>${kindCounts['app-ui']} 个 Kimi 页面帧</span><span>原图内嵌 · 离线可看</span><span>无登录 · 原 App 网络</span></div></header><main><div class="intro"><p>采集环境：${esc(session.avd)} · ${esc(session.serial)}。下图是原始 PNG，页面只缩放展示；${included[0]?.dimensions.width??'—'} × ${included[0]?.dimensions.height??'—'} 均为截图物理像素，不是 CSS px、dp 或 sp。时间同时保留 UTC 原始值和北京时间。</p><select id="kind-filter" aria-label="样本类别"><option value="">查看全部运行样本</option>${Object.entries(kindNames).map(([k,v])=>'<option value="'+esc(k)+'">'+esc(v)+' · '+kindCounts[k]+'</option>').join('')}</select></div><div class="evidence-note">截图与 UI 层级为顺序采集，不能视为同一时刻。初期记录没有单独的截图时间，因此显示采集记录完成时间；后续记录优先显示 screenshotCapturedAt。系统通知权限和 launcher 只说明行为结果，不纳入 Kimi 自有视觉样式。此页不以单帧推定字号、动画、源码 token 或完整功能验证。</div><noscript>图片和说明可直接离线阅读；类别筛选和放大查看需要 JavaScript。每张图都提供下载内嵌原图入口。</noscript><div class="scene-grid">${cards}</div>${omitted(excluded,'排除的采集记录 · 不作为 Kimi 原生设计证据')}${omitted(pending,'新增或已变化的记录 · 等待独立看图复核')}<footer><div class="footer-links"><a href="${esc(sessionPath)}">完整操作 / 采集 Session ↗</a><a href="${esc(manifestPath)}">预览溯源与像素采样 JSON ↗</a><a href="${esc(notesPath)}">独立看图注记 ↗</a><a href="tools/build-runtime-gallery.mjs">生成脚本 ↗</a>${optionalLinks}</div><p>源 APK SHA-256：<code>${esc(session.apkSha256)}</code></p><p>Session 快照 SHA-256：<code>${sha(sessionBytes)}</code></p><p>HTML 内嵌原始 PNG 字节；说明与网页展示是独立研究注记。原始截图、capture.json、session.json 均未修改。新增页面须先补充带精确截图哈希的 runtime-gallery-notes.json，再重跑生成器。</p><p>生成于 ${new Date().toISOString()} · 重建：<code>node research/kimi-android/tools/build-runtime-gallery.mjs</code></p></footer></main><dialog id="viewer" aria-labelledby="viewer-title"><div class="viewer-bar"><b id="viewer-title"></b><button id="viewer-close" type="button">关闭 ×</button></div><img id="viewer-image" class="viewer-image" alt=""><div id="viewer-caption" class="viewer-caption"></div></dialog><script id="viewer-data" type="application/json">${htmlJson(included.map(({scene,note,dimensions})=>({id:scene.id,title:note.title,width:dimensions.width,height:dimensions.height,sha256:scene.screenshot.sha256})))}</script><script>
'use strict';
const data=JSON.parse(document.getElementById('viewer-data').textContent);
const scenes=[...document.querySelectorAll('.scene')],buttons=[...document.querySelectorAll('.shot-open')];
document.getElementById('kind-filter').addEventListener('change',event=>{for(const scene of scenes)scene.classList.toggle('hidden',!!event.target.value&&scene.dataset.kind!==event.target.value);});
const viewer=document.getElementById('viewer');
for(const download of document.querySelectorAll('.download-shot'))download.href=buttons[Number(download.dataset.index)].querySelector('img').src;
for(const button of buttons)button.addEventListener('click',()=>{const item=data[Number(button.dataset.index)];document.getElementById('viewer-title').textContent=item.title;const img=document.getElementById('viewer-image');img.src=button.querySelector('img').src;img.alt=item.title+' 原始 Android 截图';document.getElementById('viewer-caption').textContent=item.width+' × '+item.height+' 物理 px · 仅适应窗口缩放展示 · SHA-256 '+item.sha256;viewer.showModal();});
document.getElementById('viewer-close').addEventListener('click',()=>viewer.close());
viewer.addEventListener('click',event=>{if(event.target===viewer){const r=viewer.getBoundingClientRect();if(event.clientX<r.left||event.clientX>r.right||event.clientY<r.top||event.clientY>r.bottom)viewer.close();}});
</script></body></html>`;
for(const [,code] of html.matchAll(/<script>([\s\S]*?)<\/script>/g))new vm.Script(code,{filename:'runtime.html:inline-script'});
fs.writeFileSync(path.join(root,'runtime.html'),html);
const buildReport={schemaVersion:1,generatedAt:new Date().toISOString(),output:'runtime.html',htmlBytes:Buffer.byteLength(html),htmlSha256:sha(html),sessionSha256:sha(sessionBytes),annotationSha256:sha(notesBytes),included:included.map(s=>s.scene.id),kindCounts,excluded,pending,screenshotHashesVerified:included.length,hierarchyHashesVerified:included.filter(s=>s.scene.hierarchy?.path).length,sourceScreenshotsModified:0,sourceRecordsModified:0,pixelSamples:sourceReports.flatMap(s=>s.pixelSamples.map(p=>({scene:s.id,...p}))),browserScriptSyntaxChecked:true,guiOrDeviceOperationsPerformed:false};
saveJson('assets/derived/runtime-gallery/build-report.json',buildReport);
console.log(JSON.stringify(buildReport,null,2));
