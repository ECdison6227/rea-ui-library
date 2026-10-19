import {filterSources,migrationPrompt,comparisonRows} from './logic.js';
const $=s=>document.querySelector(s),$$=s=>[...document.querySelectorAll(s)];
const initial=new URLSearchParams(location.search),state={platform:['web','desktop','mobile'].includes(initial.get('platform'))?initial.get('platform'):'all',query:initial.get('q')||'',selected:new Set()};
let catalog,toastTimer;
function el(tag,cls,text){const e=document.createElement(tag);if(cls)e.className=cls;if(text!==undefined)e.textContent=text;return e;}
function toast(text){$('#toast').textContent=text;$('#toast').hidden=false;clearTimeout(toastTimer);toastTimer=setTimeout(()=>$('#toast').hidden=true,2300);}
function syncURL(){const params=new URLSearchParams();if(state.platform!=='all')params.set('platform',state.platform);if(state.query)params.set('q',state.query);history.replaceState(null,'',location.pathname+(params.size?'?'+params:'')+location.hash);}
function render(){
 const shown=filterSources(catalog.sources,state),platformName=({all:'全部研究',web:'网页研究',desktop:'电脑端研究',mobile:'手机端研究'})[state.platform];
 $('#collection-title').firstChild.textContent=platformName+' ';$('#result-total').textContent=String(shown.length).padStart(2,'0');$('#result-description').textContent=`${catalog.sources.length} 份来源档案 · 当前显示 ${shown.length} 份`;
 for(const b of $$('[data-platform]')){const active=b.dataset.platform===state.platform;b.classList.toggle('active',active);b.setAttribute('aria-current',active?'page':'false');}
 const cards=$('#cards');cards.replaceChildren();
 for(const s of shown){
  const card=el('article','card');card.dataset.sourceId=s.id;
  const visual=el('div','visual '+s.cover);visual.append(el('span','ordinal','0'+(catalog.sources.indexOf(s)+1)),el('span','platform-tag',({web:'网页',desktop:'电脑端',mobile:'手机端'})[s.platform]));
  if(s.thumbnail){const img=el('img');img.src=s.thumbnail.url;img.alt=s.name+' · '+s.thumbnail.classification;img.loading='lazy';visual.append(img,el('span','visual-label',s.thumbnail.classification));}
  else{const mini=el('div','hark-illustration');mini.append(el('div','mini-top',s.name+' / interface study'),el('div','mini-line'),el('div','mini-line short'));const row=el('div','mini-card-row');for(let i=0;i<3;i++)row.append(el('span'));mini.append(row);visual.append(mini,el('span','visual-label','索引示意封面 · 非原版截图'));}
  card.append(visual);const body=el('div','card-body'),titleRow=el('div','card-title-row'),title=el('div');title.append(el('h3','',s.name),el('div','card-subtitle',s.subtitle));titleRow.append(title,el('span','card-arrow','↗'));body.append(titleRow,el('p','',s.summary));
  const chips=el('div','chips');for(const t of s.tags.slice(0,4))chips.append(el('span','chip',t));body.append(chips);
  const foot=el('div','card-footer'),open=el('button','card-open',s.designPackage?'使用这套风格  ↗':'查看研究参考  ↗');open.addEventListener('click',()=>openDetail(s));
  const label=el('label','compare-toggle'),checkbox=el('input');checkbox.type='checkbox';checkbox.checked=state.selected.has(s.id);checkbox.setAttribute('aria-label','选择'+s.name+'进行比较');checkbox.addEventListener('change',()=>{if(checkbox.checked&&state.selected.size>=3){checkbox.checked=false;toast('一次最多比较3个来源');return;}if(checkbox.checked)state.selected.add(s.id);else state.selected.delete(s.id);renderTray();});label.append(checkbox,document.createTextNode('加入比较'));foot.append(open,label);body.append(foot);card.append(body);cards.append(card);
 }
 $('#empty').hidden=shown.length!==0;$('#empty-title').textContent=state.platform==='desktop'&&!state.query?'电脑端，等待下一份真实研究':'没有匹配的研究';$('#empty-copy').textContent=state.platform==='desktop'&&!state.query?'当前账号内尚未找到独立桌面端REA研究，普通桌面应用不纳入此分类。':'试试“输入”“卡片”或其他产品名称。';$('#clear-search').hidden=!state.query;
 renderTray();syncURL();
}
function renderTray(){const n=state.selected.size;$('#compare-tray').hidden=n===0;$('#compare-count').textContent='已选择 '+n+' 个来源';$('#compare-open').disabled=n<2;}
function showDoc(doc){$('#document-title').textContent=doc.label;$('#document-path').textContent=doc.path||'';$('#document-content').textContent=doc.content;$('#document-dialog').showModal();}
async function openDocument(s,a){try{const r=await fetch('/api/document?source='+encodeURIComponent(s.id)+'&path='+encodeURIComponent(a.path));if(!r.ok)throw Error(await r.text());showDoc(await r.json());}catch(e){toast('暂时无法打开文档：'+e.message);}}
function openDetail(s){
 const box=$('#detail-content');box.replaceChildren(el('span','eyebrow',s.platform.toUpperCase()+' / REA RESEARCH'),el('h2','',s.name),el('p','muted',s.summary));
  const palette=el('div','detail-palette');for(const color of s.palette){const swatch=el('span');swatch.style.backgroundColor=color;swatch.title=color;palette.append(swatch);}box.append(palette,el('p','muted','目录配色示意 · D；实施时请读取来源 tokens。'),el('h3','','可参考的设计与行为'));
 const list=el('ul');for(const feature of s.features)list.append(el('li','',feature));box.append(list,el('h3','','来源条件与保留边界'),el('p','muted',s.coverage));
 const limits=el('ul');for(const limit of s.limits)limits.append(el('li','',limit));box.append(limits,el('h3','','研究与参考入口'));
 const grid=el('div','artifact-grid');for(const a of s.artifacts){let item;if(a.document&&a.available){item=el('button','button',a.label+' ↗');item.addEventListener('click',()=>openDocument(s,a));}else if(a.available&&a.url){item=el('a','button',a.label+' ↗');item.href=a.url;item.target='_blank';item.rel='noopener noreferrer';}else{item=el('button','button',a.label+(a.restricted?' · 本地私有查看':' · 需本地总库'));item.disabled=true;}grid.append(item);}box.append(grid,el('h3','','保留来源的运行方式'));
 const run=el('div','run-box');run.append(el('span','',s.run.cwd),el('code','',s.run.command),el('p','',s.run.requires+'；本次总库整合未重新运行来源应用。'));box.append(run);
 const actions=el('div','detail-actions'),copy=el('button','button primary','复制给AI，应用到我的软件 ↗');copy.addEventListener('click',async()=>{const text=migrationPrompt(s);try{await navigator.clipboard.writeText(text);toast('实施指令已复制，填入项目路径即可');}catch{showDoc({label:'迁移任务',path:s.root,content:text});}});actions.append(copy);
 if(s.sourceRevisionUrl){const link=el('a','button','查看总库研究目录');link.href=s.sourceRevisionUrl;link.target='_blank';link.rel='noopener noreferrer';actions.append(link);}box.append(actions,el('div','source-revision','Content SHA256 / '+s.integrity.treeSha256));$('#detail-dialog').showModal();
}
for(const d of $$('dialog')){d.querySelector('[data-close]').addEventListener('click',()=>d.close());d.addEventListener('click',e=>{if(e.target===d){const b=d.getBoundingClientRect();if(e.clientX<b.left||e.clientX>b.right||e.clientY<b.top||e.clientY>b.bottom)d.close();}});}
$('#platform-nav').addEventListener('click',e=>{const button=e.target.closest('[data-platform]');if(!button||!catalog)return;state.platform=button.dataset.platform;render();});
$('#search').value=state.query;$('#search').addEventListener('input',e=>{state.query=e.target.value;if(catalog)render();});$('#clear-search').addEventListener('click',()=>{state.query='';$('#search').value='';render();$('#search').focus();});
$('#compare-clear').addEventListener('click',()=>{state.selected.clear();render();});
$('#compare-open').addEventListener('click',()=>{const sources=catalog.sources.filter(s=>state.selected.has(s.id)),table=el('table'),head=el('thead'),tr=el('tr');tr.append(el('th','','比较项目'));for(const s of sources)tr.append(el('th','',s.name));head.append(tr);table.append(head);const body=el('tbody');for(const row of comparisonRows(sources)){const tr=el('tr');tr.append(el('td','',row.label));for(const value of row.values)tr.append(el('td','',value));body.append(tr);}table.append(body);$('#compare-content').replaceChildren(table);$('#compare-dialog').showModal();});
$('#guide-button').addEventListener('click',()=>showDoc({label:'如何使用这个库',path:'REA / 研究 → 规则 → 自己的产品',content:'1. 按实际来源平台选择研究，再看它的覆盖与保留边界。\n\n2. 新格式从design-package.json与DESIGN_SPEC.md进入，读取组件/布局/动效、资产、readiness与对照；旧研究按原入口映射。\n\n3. 复制迁移任务，填入已完成功能和普通UI的项目路径。使用rea-design-transfer，保留业务、数据、品牌和技术栈，映射所选设计并实施。\n\n4. 已有证据足够时直接进入代表页面实现。原版未测行为按你产品的需要作D决策，保留未知记录。\n\n5. 先验证你的App在正常、空、忙碌、错误、取消与返回状态下可用，再校准视觉细节。\n\n目录聚合和文件检查不会把历史证据变成新实测，也不会把未测项改为verified。'}));
$('#skill-button').addEventListener('click',async()=>{try{const r=await fetch(catalog?.apiEnabled?'/api/skill-review':'./data/skill-review.md');if(!r.ok)throw Error('文件不可用');showDoc(catalog?.apiEnabled?await r.json():{label:'REA 技能改进',path:'docs/skill-review.md',content:await r.text()});}catch(e){toast('无法打开技能改进：'+e.message);}});
$('#mobile-skill').addEventListener('click',()=>$('#skill-button').click());
$('#mobile-guide').addEventListener('click',()=>$('#guide-button').click());
document.addEventListener('keydown',e=>{
 if(e.key==='Escape'){
  const open=$$('dialog[open]');if(open.length){e.preventDefault();e.stopPropagation();open.at(-1).close();return;}
 }
 if(e.key==='/'&&!document.querySelector('dialog[open]')&&!['INPUT','TEXTAREA'].includes(document.activeElement.tagName)){e.preventDefault();$('#search').focus();}
},true);
document.addEventListener('keyup',e=>{if(e.key==='Escape')e.preventDefault();},true);
try{const r=await fetch('./data/catalog.json');if(!r.ok)throw Error('目录读取失败');catalog=await r.json();$('[data-count="all"]').textContent=String(catalog.sources.length).padStart(2,'0');for(const p of catalog.platforms)$('[data-count="'+p.id+'"]').textContent=String(p.count).padStart(2,'0');render();}catch(e){$('#error').hidden=false;$('#error').textContent='研究索引暂时不可用。'+e.message;$('#result-description').textContent='请从总库启动本地目录服务。';}
