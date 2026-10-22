import { continuousPath } from './assets/corner-path.mjs';

// D host/state adaptation. Geometry helpers and icon paths retain their B origin.
const $ = selector => document.querySelector(selector);
const $$ = selector => [...document.querySelectorAll(selector)];
const state = { route:'notes', draft:'', reply:null, pending:null, themeMode:'dark', messages:[] };
let composed = false;
const surfaceObservers = new WeakMap();
const iconNames = [...new Set($$('[data-icon]').map(el => el.dataset.icon))];
const iconMap = new Map(await Promise.all(iconNames.map(async name => {
  const response = await fetch(`../historical-assets/svg/glyph-${name}.svg`);
  if (!response.ok) throw Error(`Missing retained glyph ${name}`);
  return [name, await response.text()];
})));
function icons(root = document) {
  root.querySelectorAll('[data-icon]').forEach(el => {
    el.innerHTML = iconMap.get(el.dataset.icon) || '';
    const svg = el.querySelector('svg');
    if (svg) { svg.setAttribute('aria-hidden','true'); svg.setAttribute('focusable','false'); }
  });
}
icons();

function surface(el) {
  if (surfaceObservers.has(el)) return;
  const fill = document.createElement('span'), stroke = document.createElement('span');
  fill.className = 'surface-fill'; stroke.className = 'surface-stroke';
  fill.setAttribute('aria-hidden','true'); stroke.setAttribute('aria-hidden','true');
  el.prepend(fill, stroke);
  const update = () => {
    const { width, height } = el.getBoundingClientRect();
    const radius = parseFloat(getComputedStyle(el).getPropertyValue('--surface-radius')) || 24;
    const outer = continuousPath(width,height,radius);
    const inner = continuousPath(width-2,height-2,radius-1,1);
    if (!outer) return;
    fill.style.clipPath = `path("${outer}")`;
    stroke.style.clipPath = `path(evenodd,"${outer} ${inner}")`;
    el.dataset.cornerPath = '';
  };
  const translate = () => {
    fill.style.translate = stroke.style.translate = `${el.scrollLeft}px ${el.scrollTop}px`;
  };
  const observer = new ResizeObserver(update);
  observer.observe(el); el.addEventListener('scroll',translate,{passive:true});
  surfaceObservers.set(el,observer); update();
}
$$('.surface').forEach(surface);

const draft = $('#draft'), composer = $('#composer');
function resizeDraft() {
  draft.style.height = '48px';
  draft.style.height = `${Math.min(219,Math.max(48,draft.scrollHeight))}px`;
  draft.style.overflowY = draft.scrollHeight > 219 ? 'auto' : 'hidden';
}
function syncComposer() {
  draft.value = state.draft;
  const pending = !!state.pending;
  $('#send').disabled = pending || !state.draft.trim();
  $('#send').hidden = pending;
  $('#cancel-submit').hidden = !pending;
  draft.readOnly = pending;
  composer.dataset.state = pending ? 'pending' : (composer.dataset.state === 'error' ? 'error' : (state.draft.trim() ? 'draft' : 'empty'));
  $('#reply-context').hidden = !state.reply;
  resizeDraft();
}
draft.addEventListener('input',() => {
  state.draft = draft.value;
  composer.dataset.state = state.draft.trim() ? 'draft' : 'empty';
  $('#composer-feedback').hidden = true;
  syncComposer();
});
draft.addEventListener('compositionstart',() => { composed=true; });
draft.addEventListener('compositionend',() => { composed=false; });
draft.addEventListener('keydown',e => {
  if (e.key === 'Escape' && state.reply) { e.preventDefault(); clearReply(); }
  if (e.key === 'Enter' && !e.shiftKey && !composed && !e.isComposing && e.keyCode !== 229) {
    e.preventDefault(); composer.requestSubmit();
  }
});
function feedback(text,{error=false,retry=false}={}) {
  $('#composer-feedback').hidden = false;
  $('#composer-feedback').toggleAttribute('data-error',error);
  $('#feedback-text').textContent = text;
  $('#retry-submit').hidden = !retry;
}
function addMessage(text,reply) {
  const id = `local-note-${state.messages.length+1}`;
  state.messages.push({ id,text,reply });
  const p = document.createElement('p');
  p.className = 'bubble user surface new-entry'; p.id=id;
  if (reply) {
    const quote = document.createElement('small'); quote.textContent='↳ Observation schedule';
    quote.style.cssText='display:block;color:var(--secondary);font-size:12px;margin-bottom:6px';
    p.append(quote,document.createTextNode(text));
  } else p.textContent=text;
  $('#thread').append(p); surface(p);
  $('#notes-view').scrollTop = $('#notes-view').scrollHeight;
}
async function submit() {
  if (state.pending || !state.draft.trim()) return;
  const job = { text:state.draft, reply:state.reply, token:Symbol() };
  state.pending=job;
  const fail = $('#fail-next').checked;
  $('#fail-next').checked=false;
  composer.dataset.state='pending'; feedback('Saving in this tab…'); syncComposer();
  await new Promise(resolve => setTimeout(resolve,700));
  if (state.pending !== job) return;
  state.pending=null;
  if (fail) {
    composer.dataset.state='error'; feedback('Could not save. Your draft is still here.',{error:true,retry:true});
  } else {
    addMessage(job.text,job.reply); state.draft=''; state.reply=null;
    composer.dataset.state='empty'; feedback('Saved in this tab.');
  }
  syncComposer(); draft.focus({preventScroll:true});
}
composer.addEventListener('submit',e => { e.preventDefault(); if(!composed) submit(); });
$('#retry-submit').addEventListener('click',submit);
$('#cancel-submit').addEventListener('click',() => {
  if(!state.pending)return;
  state.pending=null; composer.dataset.state='draft';
  feedback('Save cancelled. Draft kept.'); syncComposer(); draft.focus({preventScroll:true});
});
function clearReply(){state.reply=null;syncComposer();draft.focus({preventScroll:true});}
$('#reply-cancel').addEventListener('click',clearReply);
$('#reply-schedule').addEventListener('click',() => {state.reply='schedule';syncComposer();draft.focus({preventScroll:true});});
$('#code-toggle').addEventListener('click',e => {
  const expanded=$('#schedule-card').classList.toggle('is-expanded');
  $('#code-toggle').setAttribute('aria-expanded',String(expanded));
  $('#code-toggle').firstChild.textContent = expanded ? 'Show less ' : 'Show all ';
});

const viewScroll = new Map();
function navigate(route,{focus=true}={}) {
  if(!['library','notes','projects'].includes(route))return;
  viewScroll.set(state.route,$(`#${state.route}-view`).scrollTop);
  state.route=route;
  $$('.view').forEach(view=>{view.hidden=view.id!==`${route}-view`;});
  $$('[data-route]').filter(el=>el.closest('nav')).forEach(el=>el.setAttribute('aria-current',el.dataset.route===route?'page':'false'));
  $('#composer-dock').hidden=route!=='notes';
  $(`#${route}-view`).scrollTop=viewScroll.get(route)||0;
  if(location.hash!==`#${route}`)history.pushState({route},'',`#${route}`);
  if(focus)document.querySelector(`nav [data-route="${route}"]`).focus({preventScroll:true});
}
$$('[data-route]').forEach(el=>el.addEventListener('click',()=>navigate(el.dataset.route)));
window.addEventListener('popstate',()=>navigate(location.hash.slice(1)||'notes',{focus:false}));
if(location.hash)navigate(location.hash.slice(1),{focus:false});

let dialogTrigger = null;
function openDialog(id,trigger) {
  dialogTrigger=trigger;
  const dialog=$('#'+id); dialog.showModal();
  requestAnimationFrame(()=>{
    surfaceObservers.get(dialog)?.disconnect();
    // Modal promotion can change border-box; an already observed element is refreshed.
    if(dialog.classList.contains('surface')) {
      const observer=surfaceObservers.get(dialog);
      observer?.observe(dialog);
    }
    if(id==='reader')$('#reader-close').focus({preventScroll:true});
    if(id==='search')$('#search-input').focus({preventScroll:true});
  });
}
['reader','settings','search'].forEach(id=>{
  const dialog=$('#'+id);
  dialog.addEventListener('keydown',e=>{
    if(e.key==='Escape'){e.preventDefault();dialog.close();}
  });
  dialog.addEventListener('close',()=>{
    // Native dialog restores its invoker. Avoid stealing a newer deliberate focus,
    // for example a selected search result or a quickly focused composer.
    if(document.activeElement===document.body||dialog.contains(document.activeElement))dialogTrigger?.focus({preventScroll:true});
    dialogTrigger=null;
  });
  dialog.addEventListener('click',e=>{
    if(e.target!==dialog)return;
    const r=dialog.getBoundingClientRect();
    if(e.clientX<r.left||e.clientX>r.right||e.clientY<r.top||e.clientY>r.bottom)dialog.close();
  });
});
$('#summary-open').addEventListener('click',e=>openDialog('reader',e.currentTarget));
$('#reader-close').addEventListener('click',()=>$('#reader').close());
$('#settings-open').addEventListener('click',e=>openDialog('settings',e.currentTarget));
$$('[data-close]').forEach(el=>el.addEventListener('click',()=>$('#'+el.dataset.close).close()));
$('#reader-copy').addEventListener('click',async()=>{
  try{await navigator.clipboard.writeText($('#reader-content').innerText);$('#copy-feedback').textContent='Proposal copied.';}
  catch{$('#copy-feedback').textContent='Clipboard unavailable. Select the proposal text to copy.';}
});

const media = matchMedia('(prefers-color-scheme:light)');
function theme(mode) {
  state.themeMode=mode;
  const appearance=mode==='auto'?'light':mode==='system'?(media.matches?'light':'dark'):mode;
  document.documentElement.dataset.theme=appearance;
  document.documentElement.dataset.themeMode=mode;
  $('#theme-explanation').textContent=`${appearance==='light'?'Light':'Dark'} materials · ${mode==='auto'?'D Auto fallback: Light':'fixed D backdrop'}`;
}
$$('[name="theme"]').forEach(input=>input.addEventListener('change',()=>theme(input.value)));
media.addEventListener('change',()=>{if(state.themeMode==='system')theme('system');});
theme('dark');

function searchNotes() {
  const query=$('#search-input').value.trim().toLowerCase();
  const source=[{id:'summary-open',text:'A little room for looking up · Neighborhood observatory'},{id:'schedule-card',text:'Observation schedule'},...state.messages];
  const matches=query?source.filter(note=>note.text.toLowerCase().includes(query)):source;
  const results=$('#search-results');results.replaceChildren();
  if(!matches.length){results.textContent='No matching notes. Try another word.';return;}
  matches.forEach(note=>{
    const button=document.createElement('button');button.type='button';button.textContent=note.text;
    button.addEventListener('click',()=>{
      $('#search').close();navigate('notes',{focus:false});
      const target=$('#'+note.id);target.scrollIntoView({block:'center'});
      if(target.matches('button'))target.focus({preventScroll:true});
      else{target.tabIndex=-1;target.focus({preventScroll:true});}
    });results.append(button);
  });
}
$('#search-open').addEventListener('click',e=>{searchNotes();openDialog('search',e.currentTarget);});
$('#search-input').addEventListener('input',searchNotes);
syncComposer();
window.addEventListener('resize',resizeDraft);
window.__referenceReady=true;
