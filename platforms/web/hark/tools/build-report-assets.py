from pathlib import Path
import json, hashlib, html

ROOT = Path(__file__).resolve().parents[1]
def read(p): return json.loads((ROOT/p).read_text())
def write(p, value): (ROOT/p).write_text(json.dumps(value, ensure_ascii=False, indent=2))

findings = [
 ('F01','文字排版','实测','普通气泡/输入框15px、19px、400；代码14px、20px、Mono。',['evidence/styles/targeted-computed-styles.json']),
 ('F02','摘要详情','实测','摘要详情Hark Serif标题40/52；正文18/25.2，与气泡分开。',['evidence/styles/rich-computed-styles.json','evidence/screenshots/15-summary-open.png']),
 ('F03','材质','实测+静态','用户/助手fill透明度.3/.7；composer glass blur50px。',['evidence/styles/rich-computed-styles.json','evidence/styles/B9bSKZIz.css']),
 ('F04','连续圆角','实测+静态','常见24px，独立fill/stroke + 尺寸驱动Bezier clip-path。',['evidence/dom/rea-code-06-reply-visible.html','evidence/analysis/design-rules.json']),
 ('F05','天空背景','静态+运行结构','WebGL画布；完整发布shader有FBM、梯度、LUT、颗粒。具体uniform运行值未测。',['evidence/scripts/wallpaper_core.html','evidence/scripts/wallpaper-fragment.glsl','evidence/analysis/wallpaper-source-excerpts.json']),
 ('F06','设计变量','静态','544条带原选择器的声明；同名变量有不同主题值。',['reports/design-tokens.css','reports/design-tokens.json']),
 ('F07','原生卡片','静态+部分实测','按presentation/归一化kind分发；summary/table/code/links已经生成。不是完整服务端schema。',['evidence/scripts/snippets/cards-normalization.readable.js','evidence/scripts/snippets/cards-render-dispatch.readable.js','evidence/interactions/06-stream-dom-samples.json']),
 ('F08','代码卡交互','实测','REA控制场景展开、收起、回复均完成。',['evidence/rea/05-code-bundle.json','evidence/screenshots/rea-code-06-reply-visible.png']),
 ('F09','摘要卡交互','实测','折叠预览点击后打开独立详情弹层。',['evidence/interactions/14-card-actions.json','evidence/screenshots/15-summary-open.png']),
 ('F10','图片叠卡','实测','7张图片pile展开；锁滚动；最终关闭态spread0/lockedfalse。',['evidence/screenshots/23-photo-stack-spread.png','evidence/interactions/24-collapse-final-state.json']),
 ('F11','表格与文件','实测','表格菜单CSV下载成功；文件CSV预览加载为电子表格。',['evidence/interactions/table-export.csv','evidence/interactions/21-table-download.json','evidence/screenshots/26-file-preview-loaded.png']),
 ('F12','表格排序','部分验证','静态有desc/asc/null；当前三行表格点击列头3次顺序不变。',['evidence/interactions/17-table-sorting.json','evidence/scripts/snippets/table-card-model.readable.js']),
 ('F13','实时协议','实测+静态','EventSource text/event-stream，snapshot/patches/seq/entry_add/message_update。',['evidence/network/13-sse-metadata.json','evidence/scripts/snippets/stream-feed-manager.readable.js']),
 ('F14','输出节奏','实测','100ms采样：富卡成批出现；短正文首次出现时完整。未证实逐token。',['evidence/interactions/06-stream-dom-samples.json','evidence/interactions/13-sse-dom-samples.json']),
 ('F15','连接归属','实测+推断','SharedWorker user事件、页面conversation SSE都出现；具体回退原因未知。',['evidence/network/07-worker-identity.json','evidence/network/11-reconnect-page-metadata.json']),
 ('F16','滚动','静态+实测','near-bottom<=70px；内容增长补偿；叠卡滚动锁为另一机制。',['evidence/scripts/snippets/scroll-pinning.readable.js','evidence/interactions/13-sse-dom-samples.json']),
 ('F17','窄屏','实测','390px视口下composer358px/左16px/高48px；无文档横向溢出。',['evidence/styles/27-mobile-layout.json','evidence/screenshots/27-mobile-chat.png']),
 ('F18','停止与HTML','静态','工具Stop确认/乐观取消/回滚；文件HTML sandbox allow-scripts；未实测停止。',['evidence/scripts/snippets/stop-tool-optimistic.readable.js','evidence/scripts/snippets/html-file-preview.readable.js']),
]
write('reports/findings-ledger.json', {'date':'2026-10-15','target':'https://hark.com/chat','findings':[{'id':i,'topic':t,'evidenceType':k,'finding':f,'sources':s} for i,t,k,f,s in findings]})

network=read('evidence/network/13-sse-metadata.json')['events']
send=max(e['atMs'] for e in network if e['kind']=='request' and e['url']['path']=='/api/messages/send')
sse=[{'timeMs':e['atMs']-send,'seq':e.get('seq'),'bytes':e['bytes'],'operations':[p.get('op',p.get('type','')) for p in e['patches']]} for e in network if e['kind']=='sse' and e['atMs']>=send and e['atMs']-send<16000]
samples=read('evidence/interactions/13-sse-dom-samples.json')['samples']
ui=[{'timeMs':s['atMs'],'length':sum(r['textLength'] for r in s['rows'] if r['id']!='47901db6-8613-42a2-995e-0936bbc2edfa'),'bottomDistance':s['scroll']['bottomDistance']} for s in samples if s['atMs']<=16000]
write('reports/stream-timeline.json',{'sampleIntervalRequestedMs':100,'networkZero':'Last /api/messages/send request','uiZero':'DOM probe arming; differs from network zero','uiLengthIncludesTimestamp':True,'events':sse,'ui':ui})

pairs=[
 ('代码卡','折叠','rea-code-01-code-ready.png','展开','rea-code-03-expanded.png','长代码：内容溢出才显示展开；默认底部渐隐。'),
 ('代码回复','原卡','rea-code-03-expanded.png','回复引用','rea-code-06-reply-visible.png','引用目标进入composer；不是向其他用户发送消息。'),
 ('摘要','折叠预览','14-summary-collapsed.png','详情弹层','15-summary-open.png','详情有独立正文/标题排版，不能沿用气泡字号。'),
 ('图片叠卡','Pile','22-photo-stack-collapsed.png','七张展开','23-photo-stack-spread.png','展开后锁定message-list，建立overlay；最终关闭态已另外核对。'),
 ('表格','三行表格','16-table-original.png','上下文菜单','19-table-context-menu.png','下载CSV已成功；点击当前样例列头未触发排序。'),
 ('文件','预览shell','25-file-preview.png','内容加载','26-file-preview-loaded.png','附件内容异步加载，CSV进入电子表格查看器。'),
 ('响应式','桌面1440','28-desktop-final.png','窄屏390','27-mobile-chat.png','CSS视口重排；未模拟真实手机触控和键盘。'),
]
galleryData={'pairs':[{'name':n,'leftLabel':a,'left':'../evidence/screenshots/'+b,'rightLabel':c,'right':'../evidence/screenshots/'+d,'note':f} for n,a,b,c,d,f in pairs],'timeline':{'events':sse,'ui':ui}}
template='''<!doctype html><html lang="zh-CN"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Hark 证据查看器</title>
<style>*{box-sizing:border-box}body{margin:0;background:#f4f5f7;color:#20242b;font:15px/1.6 system-ui,-apple-system,sans-serif}main{max-width:1500px;margin:auto;padding:32px}header{display:flex;align-items:start;justify-content:space-between;gap:24px}h1{font-size:30px;margin:0}h2{font-size:21px;margin:24px 0 12px}p{margin:4px 0 16px;color:#58616e}a{color:#244eb4}nav{display:flex;flex-wrap:wrap;gap:8px;margin:20px 0}button{border:1px solid #d2d7e0;background:white;color:#29323e;border-radius:8px;padding:9px 16px;font:inherit;cursor:pointer}button[aria-pressed=true]{background:#233e7e;color:white;border-color:#233e7e}section{background:white;border:1px solid #dce1e8;border-radius:14px;padding:20px;margin:18px 0}.compare{display:grid;grid-template-columns:1fr 1fr;gap:16px}figure{margin:0;background:#edf0f4;border-radius:10px;overflow:hidden}figcaption{padding:10px 14px;font-weight:600}figure img{display:block;width:100%;height:500px;object-fit:contain;background:#171c2a}#note{margin-top:12px}svg{width:100%;height:240px}input{width:100%;accent-color:#244eb4}table{width:100%;border-collapse:collapse;font-size:13px}td,th{text-align:left;padding:8px;border-bottom:1px solid #e3e7ed}code{font:13px ui-monospace,monospace;background:#eef1f5;padding:2px 5px;border-radius:3px}.small{font-size:13px}output{font-weight:600}@media(max-width:800px){main{padding:16px}header{display:block}.compare{grid-template-columns:1fr}figure img{height:370px}section{padding:14px}}</style>
<main><header><div><h1>Hark 聊天界面证据</h1><p>2026-10-15 · 实际浏览器截图与记录 · 非界面复刻</p></div><a href="hark-chat-analysis.md">阅读主报告 ↗</a></header>
<nav id="tabs" aria-label="证据类型"></nav><section><div class="compare"><figure><figcaption id="leftLabel"></figcaption><a id="leftLink" target="_blank"><img id="left" alt=""></a></figure><figure><figcaption id="rightLabel"></figcaption><a id="rightLink" target="_blank"><img id="right" alt=""></a></figure></div><p id="note"></p></section>
<section><h2>普通文字的实际显示时序</h2><p>100ms 请求采样间隔。该次回复首次出现时已完整；此图不证明逐 token 输出。文字长度含时间标签。UI 探针与网络的计时零点不同。</p>
<svg id="chart" viewBox="0 0 1000 240" role="img" aria-label="助手文字长度随时间变化"><path d="M50 20V200H970" fill="none" stroke="#b6bdc9"/><text x="50" y="225" fill="#657080">0s</text><text x="950" y="225" fill="#657080">16s</text><text x="5" y="28" fill="#657080">字符</text><polyline id="curve" fill="none" stroke="#244eb4" stroke-width="3"/><line id="cursor" y1="20" y2="200" stroke="#e26b28" stroke-width="2"/></svg>
<label for="time">采样时间：<output id="timeLabel"></output></label><input id="time" type="range" min="0" max="16000" step="100" value="10000"><p id="sample"></p>
<h2>SSE 结构事件</h2><p class="small">相对于该次 send 请求。仅保存类型、序号、字节数和操作；未保存原始历史快照内容或鉴权值。</p><table><thead><tr><th>时间</th><th>seq</th><th>字节</th><th>操作</th></tr></thead><tbody id="events"></tbody></table></section>
<footer><p class="small"><a href="design-analysis.md">排版和背景</a> · <a href="card-code-analysis.md">卡片代码</a> · <a href="design-tokens.json">原变量与来源</a> · <a href="findings-ledger.json">结论台账</a> · <a href="../evidence/manifest.json">文件哈希</a></p></footer></main>
<script>const data=__DATA__;const $=s=>document.querySelector(s);data.pairs.forEach((p,i)=>{const b=document.createElement('button');b.textContent=p.name;b.onclick=()=>choose(i);$('#tabs').append(b)});function choose(i){const p=data.pairs[i];[...$('#tabs').children].forEach((b,j)=>b.setAttribute('aria-pressed',String(i===j)));for(const side of ['left','right']){$('#'+side).src=p[side];$('#'+side).alt=p.name+' · '+p[side+'Label'];$('#'+side+'Link').href=p[side];$('#'+side+'Label').textContent=p[side+'Label']}$('#note').textContent=p.note}choose(0);$('#curve').setAttribute('points',data.timeline.ui.map(s=>`${50+s.timeMs/16000*920},${200-s.length/100*180}`).join(' '));function seek(){const t=Number($('#time').value),s=data.timeline.ui.filter(s=>s.timeMs<=t).at(-1)||{length:0,bottomDistance:0};$('#timeLabel').textContent=(t/1000).toFixed(1)+'s';$('#sample').textContent=`助手可读文字长度 ${s.length}；离聊天底部 ${Math.round(s.bottomDistance)}px。`;const x=50+t/16000*920;$('#cursor').setAttribute('x1',x);$('#cursor').setAttribute('x2',x)}$('#time').oninput=seek;seek();for(const e of data.timeline.events){const r=document.createElement('tr');for(const value of [(e.timeMs/1000).toFixed(3)+'s',e.seq??'—',e.bytes,e.operations.join(' / ')||'domain事件']){const c=document.createElement('td');c.textContent=value;r.append(c)}$('#events').append(r)}</script></html>'''
(ROOT/'reports/evidence-gallery.html').write_text(template.replace('__DATA__',json.dumps(galleryData,ensure_ascii=False).replace('</','<\\/')))

manifest=[]
for folder in ['evidence','reports','tools','scripts']:
 for p in sorted((ROOT/folder).rglob('*')):
  if p.is_file() and p.name not in ('manifest.json','index.json','verification.json'):
   b=p.read_bytes();manifest.append({'path':str(p.relative_to(ROOT)),'bytes':len(b),'sha256':hashlib.sha256(b).hexdigest()})
write('evidence/manifest.json',{'target':'https://hark.com/chat','date':'2026-10-15','hash':'SHA-256','files':manifest})
print(json.dumps({'findings':len(findings),'files':len(manifest),'galleryPairs':len(pairs)},ensure_ascii=False))
