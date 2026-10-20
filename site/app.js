import {filterSources,migrationPrompt,comparisonRows} from './logic.js';
import {fetchCatalogSnapshot,retainSelections,validateSnapshot,REFRESH_INTERVAL_MS} from './updates.js';
const $=s=>document.querySelector(s),$$=s=>[...document.querySelectorAll(s)];
const initial=new URLSearchParams(location.search),state={platform:['web','desktop','mobile'].includes(initial.get('platform'))?initial.get('platform'):'all',selected:new Set()};
const BUILT_APP_VERSION='__BUILD_APP_VERSION__';
if(!BUILT_APP_VERSION.startsWith('__'))document.documentElement.dataset.appVersion=BUILT_APP_VERSION;
let catalog,toastTimer,publishedVersion=null,refreshing=false;
function el(tag,cls,text){const e=document.createElement(tag);if(cls)e.className=cls;if(text!==undefined)e.textContent=text;return e;}
function toast(text){$('#toast').textContent=text;$('#toast').hidden=false;clearTimeout(toastTimer);toastTimer=setTimeout(()=>$('#toast').hidden=true,2300);}
function syncURL(){const params=new URLSearchParams();if(state.platform!=='all')params.set('platform',state.platform);history.replaceState(null,'',location.pathname+(params.size?'?'+params:'')+location.hash);}
function render(){
 const shown=filterSources(catalog.sources,state),platformName=({all:'全部研究',web:'网页研究',desktop:'电脑端研究',mobile:'手机端研究'})[state.platform];
 $('#collection-title').firstChild.textContent=platformName+' ';$('#result-total').textContent=String(shown.length).padStart(2,'0');$('#result-description').textContent=`${catalog.sources.length} 份来源档案 · 当前显示 ${shown.length} 份`;
 for(const b of $$('[data-platform]')){const active=b.dataset.platform===state.platform;b.classList.toggle('active',active);b.setAttribute('aria-current',active?'page':'false');}
 const cards=$('#cards');cards.replaceChildren();
 for(const s of shown){
  const card=el('article','card');card.dataset.sourceId=s.id;card.tabIndex=0;card.setAttribute('role','button');card.setAttribute('aria-label','查看 '+s.name+' 的风格');
  const visual=el('div','visual '+s.cover);visual.append(el('span','platform-tag',({web:'网页',desktop:'电脑端',mobile:'手机端'})[s.platform]));
  if(s.thumbnail){const img=el('img');img.src=s.thumbnail.url;img.alt=s.name+' · '+s.thumbnail.classification;img.loading='lazy';visual.append(img);}
  else{const mini=el('div','hark-illustration');mini.append(el('div','mini-top',s.name+' / interface study'),el('div','mini-line'),el('div','mini-line short'));const row=el('div','mini-card-row');for(let i=0;i<3;i++)row.append(el('span'));mini.append(row);visual.append(mini);}
  card.append(visual);const body=el('div','card-body'),titleRow=el('div','card-title-row'),title=el('div');title.append(el('h3','',s.name));titleRow.append(title);body.append(titleRow);
  const chips=el('div','chips');for(const t of s.tags.slice(0,4))chips.append(el('span','chip',t));body.append(chips);
  const foot=el('div','card-footer'),open=el('span','card-open',s.designPackage?'使用这套风格  ↗':'查看研究参考  ↗');
  const label=el('label','compare-toggle'),checkbox=el('input');checkbox.type='checkbox';checkbox.checked=state.selected.has(s.id);checkbox.setAttribute('aria-label','选择'+s.name+'进行比较');checkbox.addEventListener('change',()=>{if(checkbox.checked&&state.selected.size>=3){checkbox.checked=false;toast('一次最多比较3个来源');return;}if(checkbox.checked)state.selected.add(s.id);else state.selected.delete(s.id);renderTray();});label.append(checkbox,document.createTextNode('加入比较'));foot.append(open,label);body.append(foot);card.append(body);cards.append(card);
  card.addEventListener('click',e=>{if(e.target.closest('.compare-toggle'))return;openDetail(s);});
  card.addEventListener('keydown',e=>{if(e.target!==card)return;if(e.key==='Enter'||e.key===' '){e.preventDefault();openDetail(s);}});
 }
 $('#empty').hidden=shown.length!==0;$('#empty-title').textContent=state.platform==='desktop'?'电脑端，等待下一份真实研究':'这个分类还没有研究';$('#empty-copy').textContent=state.platform==='desktop'?'当前账号内尚未找到独立桌面端REA研究，普通桌面应用不纳入此分类。':'换一个平台看看。';
 renderTray();syncURL();
}
function renderTray(){const n=state.selected.size;$('#compare-tray').hidden=n===0;$('#compare-count').textContent='已选择 '+n+' 个来源';$('#compare-open').disabled=n<2;}
function showDoc(doc){$('#document-title').textContent=doc.label;$('#document-path').textContent=doc.path||'';$('#document-content').textContent=doc.content;$('#document-dialog').showModal();}
function openDetail(s){
 const box=$('#detail-content');box.replaceChildren(el('h2','',s.name));
 const media=el('div','detail-media');
 if(s.thumbnail){const img=el('img');img.src=s.thumbnail.url;img.alt=s.name;media.append(img);}
 else{const mini=el('div','hark-illustration');mini.append(el('div','mini-top',s.name+' / interface study'),el('div','mini-line'),el('div','mini-line short'));media.append(mini);}
 box.append(media);
 const paletteSection=el('section','detail-section');paletteSection.append(el('h3','','配色'));
 const palette=el('div','detail-palette');for(const color of s.palette){const swatch=el('div','detail-swatch'),chip=el('span');chip.style.backgroundColor=color;swatch.append(chip,el('small','',color));palette.append(swatch);}paletteSection.append(palette);box.append(paletteSection);
 const fontSection=el('section','detail-section');fontSection.append(el('h3','','字体'));
 const fonts=el('div','detail-fonts');for(const family of s.typography?.families||['系统字体']){const item=el('div','detail-font'),sample=el('span','','Aa 界面');sample.style.fontFamily=family;item.append(el('strong','',family),sample);fonts.append(item);}fontSection.append(fonts);box.append(fontSection);
 const actions=el('div','detail-actions'),copy=el('button','button primary','复制给 AI ↗');copy.addEventListener('click',async()=>{const text=migrationPrompt(s);try{await navigator.clipboard.writeText(text);toast('已复制，填入项目路径即可');}catch{showDoc({label:'迁移任务',path:s.root,content:text});}});actions.append(copy);box.append(actions);
 $('#detail-dialog').showModal();
}
for(const d of $$('dialog')){d.querySelector('[data-close]').addEventListener('click',()=>d.close());d.addEventListener('click',e=>{if(e.target===d){const b=d.getBoundingClientRect();if(e.clientX<b.left||e.clientX>b.right||e.clientY<b.top||e.clientY>b.bottom)d.close();}});}
$('#platform-nav').addEventListener('click',e=>{const button=e.target.closest('[data-platform]');if(!button||!catalog)return;state.platform=button.dataset.platform;render();});
$('#compare-clear').addEventListener('click',()=>{state.selected.clear();render();});
$('#compare-open').addEventListener('click',()=>{const sources=catalog.sources.filter(s=>state.selected.has(s.id)),table=el('table'),head=el('thead'),tr=el('tr');tr.append(el('th','','比较项目'));for(const s of sources)tr.append(el('th','',s.name));head.append(tr);table.append(head);const body=el('tbody');for(const row of comparisonRows(sources)){const tr=el('tr');tr.append(el('td','',row.label));for(const value of row.values)tr.append(el('td','',value));body.append(tr);}table.append(body);$('#compare-content').replaceChildren(table);$('#compare-dialog').showModal();});
document.addEventListener('keydown',e=>{
 if(e.key==='Escape'){
  const open=$$('dialog[open]');if(open.length){e.preventDefault();e.stopPropagation();open.at(-1).close();return;}
 }
},true);
document.addEventListener('keyup',e=>{if(e.key==='Escape')e.preventDefault();},true);
function acceptCatalog(next){catalog=validateSnapshot(next);state.selected=retainSelections(state.selected,catalog.sources);$('[data-count="all"]').textContent=String(catalog.sources.length).padStart(2,'0');for(const p of catalog.platforms)$('[data-count="'+p.id+'"]').textContent=String(p.count).padStart(2,'0');$('#error').hidden=true;render();}
async function refreshCatalog(){if(refreshing)return;refreshing=true;
 try{const result=await fetchCatalogSnapshot({version:publishedVersion,apiEnabled:catalog?.apiEnabled===true});publishedVersion=result.version;if(result.changed)acceptCatalog(result.catalog);}
 catch{}
 finally{refreshing=false;}
}
try{const r=await fetch('./data/catalog.json',{cache:'no-store'});if(!r.ok)throw Error('目录读取失败');acceptCatalog(validateSnapshot(await r.json()));}catch(e){$('#error').hidden=false;$('#error').textContent='研究索引暂时不可用。'+e.message;$('#result-description').textContent='请稍后重试。';}
setInterval(()=>{if(!document.hidden&&!catalog?.apiEnabled)refreshCatalog();},REFRESH_INTERVAL_MS);
document.addEventListener('visibilitychange',()=>{if(!document.hidden&&!catalog?.apiEnabled)refreshCatalog();});
