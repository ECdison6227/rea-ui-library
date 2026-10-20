import {sourceIcons} from './source-icons.js';
const $=s=>document.querySelector(s);
const $$=s=>[...document.querySelectorAll(s)];
const source=new URLSearchParams(location.search).get('view')==='source';
const shapes={
  plus:'<path d="M12 4v16M4 12h16"/>',
  close:'<path d="m6 6 12 12M18 6 6 18"/>',
  panel:'<rect x="4" y="4" width="16" height="16" rx="2"/><path d="M10 4v16"/>',
  chat:'<path d="M5 18 3 21v-6a9 9 0 1 1 5 5"/><path d="M8 11h8M12 7v8"/>',
  circle:'<circle cx="12" cy="12" r="9"/><path d="M9 10h.01M15 10h.01M9 15h6"/>',
  plugin:'<path d="m16 3-3 3M21 8l-3 3M3 21l5-5M9 5l10 10-3 3a6 6 0 0 1-8-8z"/>',
  clock:'<circle cx="12" cy="13" r="8"/><path d="M12 8v5l3 2M3 5l3-3M18 2l3 3M6 20l-2 2M18 20l2 2"/>',
  bulb:'<path d="M8 17c0-4-3-4-3-8a7 7 0 0 1 14 0c0 4-3 4-3 8M8 17h8M9 20h6M11 17v-6M13 17v-6"/>',
  slides:'<path d="M3 4h18M5 4v12h14V4M12 16v5M8 21l4-5 4 5M8 8h8M8 11h5"/>',
  more:'<path d="M4 12h.01M12 12h.01M20 12h.01" stroke-width="3"/>',
  folder:'<path d="M3 7a2 2 0 0 1 2-2h5l2 2h7a2 2 0 0 1 2 2v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2zM8 11h8"/>',
  newfolder:'<path d="M3 8V6a2 2 0 0 1 2-2h5l2 3h7a2 2 0 0 1 2 2v3M11 20H5a2 2 0 0 1-2-2V8M18 14v8M14 18h8"/>',
  download:'<path d="M12 3v12m-5-5 5 5 5-5M4 16v4h16v-4"/>',
  chevron:'<path d="m9 5 7 7-7 7"/>',
  document:'<path d="M6 3h8l4 4v14H6zM14 3v5h4M9 12h6M9 16h6"/>',
  hammer:'<path d="m4 20 9-9m-2-6 4-3 7 7-3 4zM9 7l3-3M17 15l4-3"/>',
  sheet:'<rect x="4" y="3" width="16" height="18" rx="3"/><path d="M4 9h16M4 15h16M10 3v18"/>',
  brush:'<path d="m13 12 6-9 3 3-8 8M13 12l2 2-3 5H5l3-6zM5 19l-2 3"/>',
  search:'<circle cx="10" cy="10" r="6"/><path d="m15 15 6 6"/>',
  arrow:'<path d="M12 5v14m-5-9 5-5 5 5"/>',
  book:'<path d="M12 5C8 2 3 4 3 4v15s5-2 9 1c4-3 9-1 9-1V4s-5-2-9 1zM12 5v15"/>',
  check:'<path d="m4 12 5 5L20 6"/>',
  globe:'<circle cx="12" cy="12" r="9"/><ellipse cx="12" cy="12" rx="4" ry="9"/><path d="M3 12h18"/>',
  info:'<circle cx="12" cy="12" r="9"/><path d="M12 10v7M12 7h.01"/>',
  attach:'<path d="M8 13V6a4 4 0 0 1 8 0v12a6 6 0 0 1-12 0V7M12 7v11a1 1 0 0 1-2 0V7"/>'
};
const icon=n=>sourceIcons[n]||`<svg viewBox="0 0 24 24" aria-hidden="true">${shapes[n]||shapes.document}</svg>`;
const escapeHTML=s=>s.replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const storeKey='kimi-study-reading-room-v1';
let items;try{items=JSON.parse(localStorage.getItem(storeKey)||'[]')}catch{items=[]}
if(!Array.isArray(items))items=[];
let filter='All',activeMenu=null,returnFocus=null,toastTimer;
document.title=source?'Kimi · Source reference':'Reading Room · Design transfer';
document.body.classList.toggle('transfer-mode',!source);
$('.brand').textContent=source?'K·':'R';
$('.collapse').innerHTML=icon('panel');$('.expand').innerHTML=icon('panel');
$('.new-item span').innerHTML=icon(source?'chat':'plus')+(source?'New chat':'Add to reading list');
const nav=source?[['My Kimi','circle'],['Plugins','plugin'],['Scheduled','clock'],['Inspiration','bulb'],['Slides','slides'],['More','more']]:[['My library','book'],['Reading now','clock'],['Collections','folder'],['Discover','bulb'],['Finished','check'],['More','more']];
$('.primary-nav').innerHTML=nav.map(([label,i])=>`<button class="nav-item" data-nav="${label}">${icon(i)}${label}</button>`).join('');
$('.section-toggle').textContent=source?'Projects':'Collections';
$('.new-project').innerHTML=icon('newfolder')+(source?'New project':'New collection');
$('.section-title').textContent=source?'Chats':'Recently added';
$('.account-label').textContent=source?'Log in':'Personal library';
$('.app-action').innerHTML=icon('download');
$('.top-secondary').textContent=source?'Log in':'My library';
$('.top-primary').innerHTML=icon(source?'download':'plus')+(source?'Download for Desktop':'New collection');
$('.wordmark').innerHTML=source?'<img src="../assets/source/kimi-wordmark.png" alt="KIMI">':'Reading Room';
$('.wordmark').classList.toggle('transfer',!source);
const input=$('textarea');
input.placeholder=source?'Ask anything, or task an agent…':'Add a book, article or idea to read…';
input.setAttribute('aria-label',source?'Message':'Add a reading item');
input.value=sessionStorage.getItem(source?'source-draft':'reading-draft')||'';
$('.tools-trigger').innerHTML=icon('plus');$('.tools-trigger').classList.toggle('new',source);
$('.mode-label').textContent=source?'Instant':'Quick add';
$('.send').innerHTML=icon('arrow');
$('.project-trigger').innerHTML=icon('folder')+(source?'Select project':'Select collection');
$('.plugin-trigger').innerHTML=icon(source?'plugin':'book')+(source?'Plugins':'Reading status');
const shortcuts=source?[['Slides','slides'],['Deep Research','search'],['Docs','document'],['Build','hammer'],['Sheets','sheet'],['Design','brush']]:[['Book','book'],['Article','document'],['Research','search'],['Design','brush'],['Technology','hammer'],['Notes','sheet']];
$('.shortcuts').innerHTML=shortcuts.map(([name,i],j)=>`<button class="shortcut" style="--i:${j}" data-shortcut="${name}">${icon(i)}${name}</button>`).join('');
$('.footer-note').innerHTML=source?'<span>AI-generated, for reference only</span><span>© 2026 Moonshot AI</span><span>京ICP备2023011302号-14</span><span>京B2-20240852</span><span>京公网安备11010802043150号</span>':'<span>Your next good read starts here</span><span>Saved on this device</span>';
$('.explore').innerHTML=icon('bulb')+(source?'Explore inspiration':'Explore your reading list');
$('.account-menu').innerHTML=(source?[['Get App','download'],['About us','info'],['Language','globe'],['Get help','info']]:[['Export reading list','download'],['About this library','info'],['All reading items','book'],['Help','info']]).map(([t,i])=>`<button role="menuitem" data-account="${t}">${icon(i)}${t}<span class="chevron">${icon('chevron')}</span></button>`).join('');
const modes=source?[['K3','Chat & Agent, flagship all-rounder'],['K2.8','Preview · Well-rounded and efficient for everyday tasks'],['Instant','Fast chat, quick replies']]:[['Quick add','Capture a title and keep reading'],['Add with notes','Save a title with context'],['Review later','Save to your reading queue']];
function renderMode(){
 const selected=$('.mode-label').textContent;
 $('.mode-menu').innerHTML=`${source?'<div class="menu-guide">♧ &nbsp;Swarm has moved to the + panel.<br>&nbsp;&nbsp;&nbsp;&nbsp; <a tabindex="0">Take a look</a></div>':''}${modes.map(([name,desc])=>`<button class="menu-item" role="menuitemradio" aria-checked="${selected===name}" data-mode="${name}"><span class="item-copy"><span>${name}</span><small>${desc}</small></span><span class="check">${selected===name?icon('check'):''}</span></button>`).join('')}<div class="menu-divider"></div><button class="menu-item effort-row" aria-expanded="false">${source?'Thinking effort':'Priority'}<span class="muted effort-value">High ›</span></button><div class="effort-options" hidden><button data-effort="Standard">Standard</button><button data-effort="High" class="selected">High</button></div>`;
}
renderMode();
const tools=source?[['Add files & images','Upload from this computer','attach'],['Plugins','Connect external tools to Kimi so it can use apps and services to complete tasks','plugin'],['Skills','Turn know-how into skills. Reuse them anytime','document'],['Swarm','Search at scale and finish more in one run','more'],['Web Search','Find live news and info','globe']]:[['Add a title','Capture your next read','book'],['Add a link','Keep an article for later','attach'],['Add a note','Save a thought alongside a title','document'],['Choose collection','Keep related reading together','folder'],['Search your library','Find an item you already saved','search']];
$('.tools-menu').innerHTML=tools.map(([name,desc,i])=>`<button class="menu-item" role="menuitem" data-tool="${name}">${icon(i)}<span class="item-copy"><span>${name}</span><small>${desc}</small></span></button>`).join('');
function closeMenus(restore=false){
 for(const n of ['.mode-menu','.tools-menu','.account-menu'])$(n).hidden=true;
 for(const n of ['.mode-trigger','.tools-trigger','.account-trigger'])$(n).setAttribute('aria-expanded','false');
 $('.composer-stage').classList.remove('tools-open');$('.tools-trigger').innerHTML=icon('plus');
 if(restore&&activeMenu)activeMenu.focus();activeMenu=null;
}
function toggleMenu(selector,trigger){
 const opening=$(selector).hidden;closeMenus();
 if(opening){$(selector).hidden=false;trigger.setAttribute('aria-expanded','true');activeMenu=trigger;if(selector==='.tools-menu'){$('.composer-stage').classList.add('tools-open');$('.tools-trigger').innerHTML=icon('close')}}
}
function updateInput(){
 $('.send').disabled=!input.value.trim();input.style.height='48px';input.style.height=Math.min(170,Math.max(48,input.scrollHeight))+'px';
 sessionStorage.setItem(source?'source-draft':'reading-draft',input.value);
}
updateInput();
input.addEventListener('input',updateInput);
input.addEventListener('keydown',e=>{if(e.key==='Enter'&&!e.shiftKey&&!e.isComposing){e.preventDefault();submit()}});
function toast(message){clearTimeout(toastTimer);$('.toast').textContent=message;$('.toast').hidden=false;toastTimer=setTimeout(()=>$('.toast').hidden=true,3000)}
function persist(){localStorage.setItem(storeKey,JSON.stringify(items));renderItems()}
function submit(){
 const title=input.value.trim();if(!title)return;
 if(source){openModal('login');return;}
 items.unshift({id:crypto.randomUUID(),title,status:'To read',createdAt:new Date().toISOString()});input.value='';updateInput();persist();toast('Added to your reading list');
}
function renderItems(){
 $('.history-items').innerHTML=source?'<button class="history-login">Log in to sync chat history</button>':items.slice(0,4).map(i=>`<button data-history="${escapeHTML(i.id)}">${escapeHTML(i.title)}</button>`).join('')||'<button class="history-empty">Your list is ready for a first read</button>';
 if(source)return;
 $('.reading-list').hidden=items.length===0;$('.home').classList.toggle('has-items',items.length>0);
 $('.filter-tabs').innerHTML=['All','To read','Finished'].map(t=>`<button role="tab" data-filter="${t}" aria-selected="${filter===t}">${t}</button>`).join('');
 const visible=items.filter(i=>filter==='All'||i.status===filter);
 $('.reading-grid').innerHTML=visible.length?visible.map(i=>`<article class="reading-card"><span class="muted">${icon('book')}</span><h2>${escapeHTML(i.title)}</h2><p>${i.status}</p><button data-item="${escapeHTML(i.id)}">${i.status==='Finished'?'Read again':'Mark finished'}</button></article>`).join(''):'<div class="empty-state">No items here yet. Your other reading is saved in All.</div>';
}
renderItems();
function setSidebar(){
 const narrow=matchMedia('(max-width:840px)').matches;$('.expand').hidden=!narrow&&!document.body.classList.contains('collapsed');$('.navigation-mask').hidden=!document.body.classList.contains('mobile-open');
}
setSidebar();addEventListener('resize',setSidebar);
$('.collapse').onclick=()=>{if(matchMedia('(max-width:840px)').matches)document.body.classList.remove('mobile-open');else document.body.classList.add('collapsed');setSidebar()};
$('.expand').onclick=()=>{if(matchMedia('(max-width:840px)').matches)document.body.classList.toggle('mobile-open');else document.body.classList.remove('collapsed');setSidebar()};
$('.navigation-mask').onclick=()=>{document.body.classList.remove('mobile-open');setSidebar()};
$('.tools-trigger').onclick=e=>{e.stopPropagation();toggleMenu('.tools-menu',e.currentTarget)};
$('.mode-trigger').onclick=e=>toggleMenu('.mode-menu',e.currentTarget);
$('.account-trigger').onclick=e=>toggleMenu('.account-menu',e.currentTarget);
$('.send').onclick=submit;
$('.project-trigger').onclick=()=>openModal(source?'login':'collection');
$('.new-project').onclick=()=>openModal(source?'login':'collection');
$('.top-primary').onclick=()=>openModal(source?'download':'collection');
$('.top-secondary').onclick=()=>source?openModal('login'):showList();
$('.plugin-trigger').onclick=()=>source?openModal('login'):(filter='To read',showList());
$('.new-item').onclick=()=>{input.value='';updateInput();input.focus();closeMenus()};
$('.brand').onclick=e=>{e.preventDefault();$('.main').scrollTo({top:0,behavior:'smooth'})};
$('.section-toggle').onclick=e=>{const expanded=e.currentTarget.getAttribute('aria-expanded')==='true';e.currentTarget.setAttribute('aria-expanded',String(!expanded));$('.new-project').hidden=expanded};
$('.explore').onclick=()=>source?toast('Inspiration gallery is documented in the comparison view.'):showList();
$('.app-action').onclick=()=>source?openModal('download'):exportItems();
$('.clear-filter').onclick=()=>{filter='All';renderItems()};
function showList(){if(!items.length){toast('Add a title to start your reading list');input.focus();return;}renderItems();$('.reading-list').scrollIntoView({behavior:matchMedia('(prefers-reduced-motion:reduce)').matches?'instant':'smooth',block:'start'})}
function exportItems(){const blob=new Blob([JSON.stringify(items,null,2)],{type:'application/json'});const a=document.createElement('a');a.href=URL.createObjectURL(blob);a.download='reading-list.json';a.click();URL.revokeObjectURL(a.href)}
function openModal(type){
 closeMenus();returnFocus=document.activeElement;$('.modal-mask').hidden=false;$('.modal').dataset.type=type;
 if(type==='login'){
  $('#modal-title').textContent='KIMI';
  $('.modal-body').innerHTML=`<div class="modal-columns"><div class="modal-column"><h2>WeChat QR login</h2><div class="qr-sample" role="img" aria-label="Non-scannable QR placeholder">${Array.from({length:289},(_,i)=>`<i class="${(i*13+i%7)%5>2?'light':''}"></i>`).join('')}</div></div><div class="modal-column"><h2>Phone number</h2><form><input type="tel" aria-label="Phone number" placeholder="+86　⌄　 Phone number"><input aria-label="Verification code" placeholder="Verification code　　Send code" disabled><button class="primary form-submit" disabled>Log in</button></form><button>Sign in with SSO</button></div></div><div class="modal-note"><label><input type="checkbox"> I agree to <a>Terms of Service</a> and <a>Privacy Policy</a></label><br>Outside mainland China? Visit kimi.ai, Need help? Feedback</div>`;
 }else if(type==='collection'){
  $('#modal-title').textContent='Reading Room';
  $('.modal-body').innerHTML='<div class="modal-columns"><div class="modal-column"><h2>A little space for your next reads</h2><div class="collection-art"></div><p class="muted">Bring related books, articles and ideas together.</p></div><div class="modal-column"><h2>New collection</h2><form id="collection-form"><input name="name" aria-label="Collection name" placeholder="Collection name" maxlength="60" required><input name="description" aria-label="Description" placeholder="Description (optional)"><button class="primary form-submit" disabled>Create collection</button></form><span class="muted">Saved on this device</span></div></div>';
  $('#collection-form input').addEventListener('input',()=>$('#collection-form button').disabled=!$('#collection-form input').value.trim());
  $('#collection-form').onsubmit=e=>{e.preventDefault();const name=new FormData(e.currentTarget).get('name').trim();if(!name)return;$('.project-trigger').innerHTML=icon('folder')+escapeHTML(name);localStorage.setItem('reading-room-collection',name);closeModal();toast('Collection created')};
 }else{
  $('#modal-title').textContent=source?'KIMI':'Reading Room';$('.modal-body').innerHTML='<h2 style="font-size:18px;font-weight:500">A quieter place to focus</h2><p class="muted">The source download action is represented as a local dialog in this reference.</p><button class="primary dismiss-info">Continue</button>';
 }
 document.body.style.overflow='hidden';$('.modal').focus();
}
function closeModal(){if($('.modal-mask').hidden)return;$('.modal-mask').hidden=true;document.body.style.overflow='';returnFocus?.focus();}
$('.close-modal').onclick=closeModal;
$('.modal-mask').onclick=e=>{if(e.target===$('.modal-mask'))closeModal()};
document.addEventListener('click',e=>{
 const t=e.target.closest('button,a');
 if(t?.dataset.mode){$('.mode-label').textContent=t.dataset.mode;renderMode();closeMenus(true)}
 else if(t?.classList.contains('effort-row')){const expanded=t.getAttribute('aria-expanded')==='true';t.setAttribute('aria-expanded',String(!expanded));$('.effort-options').hidden=expanded}
 else if(t?.dataset.effort){$('.mode-trigger .muted').textContent=t.dataset.effort;$('.effort-value').textContent=t.dataset.effort+' ›';$$('[data-effort]').forEach(b=>b.classList.toggle('selected',b===t));closeMenus(true)}
 else if(t?.dataset.tool){if(source){closeMenus();if(t.dataset.tool==='Add files & images')toast('Attachment geometry is documented; uploads are outside this reference.');else openModal('login')}else if(t.dataset.tool==='Choose collection')openModal('collection');else{closeMenus();input.focus();if(t.dataset.tool==='Search your library')showList()}}
 else if(t?.dataset.shortcut){if(source){toast(`${t.dataset.shortcut} uses the same shortcut component`)}else{input.value=t.dataset.shortcut==='Notes'?'Note: ':'';input.placeholder=`Add ${t.dataset.shortcut.toLowerCase()} title…`;updateInput();input.focus()}}
 else if(t?.dataset.filter){filter=t.dataset.filter;renderItems()}
 else if(t?.dataset.item){const i=items.find(x=>x.id===t.dataset.item);i.status=i.status==='Finished'?'To read':'Finished';persist();toast(i.status==='Finished'?'Marked finished':'Moved to reading queue')}
 else if(t?.dataset.nav){if(source){if(t.dataset.nav==='More')toast('More navigation rows use the same component');else openModal('login')}else{if(t.dataset.nav==='Collections')openModal('collection');else{filter=t.dataset.nav==='Finished'?'Finished':t.dataset.nav==='Reading now'?'To read':'All';showList()}}}
 else if(t?.dataset.account){closeMenus();if(t.dataset.account==='Export reading list')exportItems();else toast(t.dataset.account)}
 else if(t?.classList.contains('history-login'))openModal('login');
 else if(t?.classList.contains('dismiss-info'))closeModal();
 if(!e.target.closest('.floating-menu,.tools-menu,.account-menu,.mode-trigger,.tools-trigger,.account-trigger'))closeMenus();
});
document.addEventListener('keydown',e=>{
 if(e.key==='Escape'){if(!$('.modal-mask').hidden)closeModal();else if(activeMenu)closeMenus(true);else{document.body.classList.remove('mobile-open');setSidebar()}return;}
 if((e.ctrlKey||e.metaKey)&&e.key.toLowerCase()==='k'){e.preventDefault();input.focus()}
 if((e.ctrlKey||e.metaKey)&&e.key.toLowerCase()==='b'){e.preventDefault();document.body.classList.toggle('collapsed');setSidebar()}
 if(!$('.modal-mask').hidden&&e.key==='Tab'){
  const elements=[...$('.modal').querySelectorAll('button:not(:disabled),input:not(:disabled),a[href], [tabindex="0"]')].filter(el=>el.getClientRects().length);
  const first=elements[0],last=elements.at(-1);
  if(e.shiftKey&&(document.activeElement===first||document.activeElement===$('.modal'))){e.preventDefault();last.focus()}
  else if(!e.shiftKey&&(document.activeElement===last||document.activeElement===$('.modal'))){e.preventDefault();first.focus()}
 }
 if(activeMenu&&['ArrowDown','ArrowUp','Home','End'].includes(e.key)){
  const menu=$$('[role="menu"]').find(el=>!el.hidden);const buttons=[...menu.querySelectorAll('button')].filter(el=>el.getClientRects().length);
  if(!buttons.length)return;e.preventDefault();let i=buttons.indexOf(document.activeElement);if(e.key==='Home')i=0;else if(e.key==='End')i=buttons.length-1;else i=(i+(e.key==='ArrowDown'?1:-1)+buttons.length)%buttons.length;buttons[i].focus();
 }
});
if(source){$('.account-menu').hidden=false;$('.account-trigger').setAttribute('aria-expanded','true');activeMenu=$('.account-trigger')}
else {const collection=localStorage.getItem('reading-room-collection');if(collection)$('.project-trigger').innerHTML=icon('folder')+escapeHTML(collection)}
