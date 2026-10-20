'use strict';
// Independent D reference. Local session-only task data; no network or account writes.
const $=id=>document.getElementById(id);
let records=[{id:1,title:'设计中的留白与节奏',done:false},{id:2,title:'把复杂问题拆成小步骤',done:false},{id:3,title:'一份可以慢慢读的笔记',done:true}],category='all',editingId=null,opener=null,deleteId=null,loadState='ready';
const state={get records(){return structuredClone(records)},get category(){return category},get loadState(){return loadState}};
Object.defineProperty(window,'referenceState',{value:state,writable:false});
function say(message){$('status').textContent=message;}
function render(){
  const root=$('records');root.replaceChildren();
  if(loadState==='error'){
    const card=document.createElement('section');card.className='state';card.innerHTML='<h3>这次没有加载好</h3><p>你的搜索与分类已保留，可以再试一次。</p><button class="primary" id="retry">重新加载</button>';
    root.append(card);$('retry').onclick=()=>{loadState='loading';render();setTimeout(()=>{loadState='ready';render();say('资料已重新加载');},180);};return;
  }
  if(loadState==='loading'){const p=document.createElement('p');p.setAttribute('role','status');p.textContent='正在加载资料…';root.append(p);return;}
  const query=$('query').value.trim().toLowerCase();$('clear-query').hidden=!query;
  const filtered=records.filter(r=>(category==='all'||(category==='done')===r.done)&&r.title.toLowerCase().includes(query));
  if(!filtered.length){const card=document.createElement('section');card.className='state';card.innerHTML='<h3>还没有找到</h3><p>换一个关键词，或者新建一条资料。</p><button class="primary" id="empty-create">新建资料</button>';root.append(card);$('empty-create').onclick=()=>openEditor();return;}
  for(const r of filtered){
    const row=document.createElement('article');row.className='record';
    const symbol=document.createElement('div');symbol.className='record-symbol';symbol.textContent=r.done?'读':'页';symbol.setAttribute('aria-hidden','true');
    const body=document.createElement('div'),title=document.createElement('button');title.className='record-title';title.textContent=r.title;title.dataset.edit=String(r.id);title.onclick=()=>openEditor(r.id);
    const meta=document.createElement('p');meta.className='record-meta';meta.textContent=r.done?'已读完 · 个人资料':'待阅读 · 个人资料';body.append(title,meta);
    const remove=document.createElement('button');remove.className='icon';remove.textContent='⋯';remove.setAttribute('aria-label','删除资料：'+r.title);remove.dataset.delete=String(r.id);remove.onclick=()=>{deleteId=r.id;$('delete-description').textContent='“'+r.title+'”会从这个本地样本移除。';$('delete-dialog').showModal();$('cancel-delete').focus();};
    row.append(symbol,body,remove);root.append(row);
  }
}
function updateCounter(){const count=Array.from($('item-title').value).length;$('counter').textContent=count+' / 60';}
function openEditor(id=null){editingId=id;opener=document.activeElement;$('editor-title').textContent=id?'编辑资料':'新建资料';$('item-title').value=records.find(r=>r.id===id)?.title || '';$('title-error').textContent='';$('item-title').removeAttribute('aria-invalid');updateCounter();$('editor').showModal();$('item-title').focus();}
function closeEditor(){ $('editor').close(); if(opener?.isConnected)opener.focus(); else $('new-item').focus(); }
function requestClose(){const original=records.find(r=>r.id===editingId)?.title || '';if($('item-title').value!==original){$('leave-dialog').showModal();$('keep-editing').focus();}else closeEditor();}
$('new-item').onclick=()=>openEditor();$('editor-close').onclick=requestClose;$('editor-cancel').onclick=requestClose;
$('editor').addEventListener('cancel',e=>{e.preventDefault();requestClose();});
$('keep-editing').onclick=()=>{$('leave-dialog').close();$('item-title').focus();};
$('discard-edit').onclick=()=>{$('leave-dialog').close();closeEditor();say('已放弃修改');};
$('item-title').addEventListener('input',updateCounter);
$('edit-form').addEventListener('submit',e=>{e.preventDefault();const value=$('item-title').value.trim();if(!value || Array.from(value).length>60){$('title-error').textContent=!value?'资料名称不能为空':'名称最多60个字符';$('item-title').setAttribute('aria-invalid','true');$('item-title').focus();return;}
  if(editingId){records.find(r=>r.id===editingId).title=value;}else records.unshift({id:Date.now(),title:value,done:false});loadState='ready';render();closeEditor();say('资料已保存');
});
$('cancel-delete').onclick=()=>{$('delete-dialog').close();document.querySelector('[data-delete="'+deleteId+'"]')?.focus();};
$('confirm-delete').onclick=()=>{records=records.filter(r=>r.id!==deleteId);$('delete-dialog').close();render();$('new-item').focus();say('资料已删除');};
$('query').addEventListener('input',()=>{loadState='ready';render();});$('clear-query').onclick=()=>{$('query').value='';render();$('query').focus();};
for(const tab of document.querySelectorAll('[data-category]'))tab.onclick=()=>{category=tab.dataset.category;for(const t of document.querySelectorAll('[data-category]'))t.setAttribute('aria-pressed',String(t===tab));render();};
$('simulate-error').onclick=()=>{loadState='error';render();};$('simulate-empty').onclick=()=>{$('query').value='尚未收录的资料';loadState='ready';render();$('query').focus();};
$('settings').onclick=()=>{$('settings-dialog').showModal();};$('settings-close').onclick=()=>{$('settings-dialog').close();$('settings').focus();};
$('apply-settings').onclick=()=>{document.documentElement.style.setProperty('--type-scale',document.querySelector('[name=text-size]:checked').value==='large'?'1.25':'1');$('settings-dialog').close();$('settings').focus();say('阅读设置已应用');};
for(const dialog of document.querySelectorAll('dialog'))dialog.addEventListener('keydown',event=>{
  if(event.key!=='Tab')return;
  const focusable=[...dialog.querySelectorAll('button:not(:disabled),input:not(:disabled),[tabindex="0"]')].filter(n=>n.getClientRects().length);
  const first=focusable[0],last=focusable.at(-1);
  if(event.shiftKey&&document.activeElement===first){event.preventDefault();last.focus();}
  else if(!event.shiftKey&&document.activeElement===last){event.preventDefault();first.focus();}
});
render();
