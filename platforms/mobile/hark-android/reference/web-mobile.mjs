// Independent target behavior; the source of visual values is documented separately.
import { initialState, reduce } from './model.mjs';
const $ = id => document.getElementById(id);
let state = initialState(), timer, selected = 'chat', theme = 'dark', renderedMessages;
const media = matchMedia('(prefers-color-scheme: dark)');
function applyTheme() {
  document.documentElement.dataset.theme = theme === 'system' ? media.matches ? 'dark' : 'light' : theme;
  document.querySelectorAll('[data-theme-choice]').forEach(button => button.setAttribute('aria-pressed', String(button.dataset.themeChoice === theme)));
}
media.addEventListener('change', applyTheme); applyTheme();
function syncViewport() {
  const height = window.visualViewport?.height ?? innerHeight;
  document.documentElement.style.setProperty('--viewport-height', `${height}px`);
  document.documentElement.dataset.keyboard = innerHeight - height > 100 ? 'open' : 'closed';
}
visualViewport?.addEventListener('resize', syncViewport); addEventListener('resize', syncViewport); syncViewport();
function resizeDraft() {
  $('draft').style.height = '46px';
  $('draft').style.height = `${Math.max(46, Math.min(219, $('draft').scrollHeight))}px`;
  document.documentElement.style.setProperty('--transcript-bottom', `${$('composer-area').offsetHeight + 64}px`);
}
function selectTab(tab, focus = false) {
  selected = tab;
  for (const name of ['overview', 'chat', 'records']) {
    $(name).hidden = name !== tab;
    $(`tab-${name}`).setAttribute('aria-selected', String(name === tab));
    $(`tab-${name}`).tabIndex = name === tab ? 0 : -1;
  }
  $('composer-area').hidden = tab !== 'chat';
  if (focus) $(`tab-${tab}`).focus();
  if (tab === 'chat') resizeDraft();
}
function renderRecords() {
  const query = $('record-search').value.trim().toLocaleLowerCase();
  const rows = state.messages.filter(message => message.text.toLocaleLowerCase().includes(query));
  $('record-list').replaceChildren();
  if (!rows.length) {
    const empty = document.createElement('p'); empty.className = 'empty-records';
    empty.textContent = query ? '没有匹配的记录，试试其他关键词。' : '这里还没有记录。从对话里写下第一条观察。';
    $('record-list').append(empty);
  }
  for (const row of rows) { const article = document.createElement('article'); article.className = 'record'; article.textContent = row.text; $('record-list').append(article); }
}
function render() {
  const busy = state.phase === 'sending';
  $('draft').value = state.draft; $('draft').readOnly = busy;
  $('send').disabled = busy || !state.draft.trim(); $('templates').disabled = busy;
  $('send').setAttribute('aria-label', busy ? '正在发送本地记录' : '发送记录');
  $('send-ink').innerHTML = busy ? '<span class="spinner" aria-hidden="true"></span>' : '<svg viewBox="0 0 24 24"><path d="M12 19V5m-5 5 5-5 5 5"/></svg>';
  $('cancel').hidden = !busy; $('feedback').replaceChildren();
  if (state.error) {
    $('feedback').append(document.createTextNode('本地示例未完成，草稿已保留。 '));
    const retry = document.createElement('button'); retry.type = 'button'; retry.textContent = '重试'; retry.addEventListener('click', () => submit('retry')); $('feedback').append(retry);
  }
  $('card-details').hidden = !state.details; $('details').setAttribute('aria-expanded', String(state.details));
  $('details').textContent = state.details ? '收起详情 ⌃' : '展开详情 ⌄';
  $('complete').textContent = state.done ? '撤销完成' : '标记完成'; $('done').hidden = !state.done;
  $('overview-state').textContent = state.done ? '已完成 · 可在对话中撤销' : '待完成 · 3 个采样点'; $('record-count').textContent = String(state.messages.length);
  if (renderedMessages !== state.messages) {
    renderedMessages = state.messages; $('messages').replaceChildren();
    for (const message of state.messages) {
      const user = document.createElement('div'); user.className = 'bubble user-bubble'; user.textContent = message.text;
      const assistant = document.createElement('div'); assistant.className = 'bubble assistant-bubble'; assistant.textContent = '已记在这份本地示例中，可在「记录」中查找。';
      $('messages').append(user, assistant);
    }
    renderRecords();
  }
  if (selected === 'chat') resizeDraft();
}
function dispatch(event) { state = reduce(state, event); render(); }
function submit(type = 'submit') {
  const previous = state; dispatch({ type });
  if (state === previous || state.phase !== 'sending') return;
  const operation = state.operation, ok = !$('fail').checked; $('fail').checked = false;
  timer = setTimeout(() => { dispatch({ type: 'result', operation, ok }); if (ok) $('chat').scrollTop = $('chat').scrollHeight; }, 1300);
}
$('form').addEventListener('submit', event => { event.preventDefault(); submit(); });
$('draft').addEventListener('input', event => dispatch({ type: 'input', value: event.target.value }));
$('draft').addEventListener('keydown', event => { if (event.key === 'Enter' && !event.shiftKey && !event.isComposing) { event.preventDefault(); submit(); } });
$('cancel').addEventListener('click', () => { clearTimeout(timer); dispatch({ type: 'cancel' }); $('draft').focus(); });
$('complete').addEventListener('click', () => dispatch({ type: 'complete' }));
$('details').addEventListener('click', () => dispatch({ type: 'details' }));
const tabs = ['overview', 'chat', 'records'];
for (const name of tabs) {
  $(`tab-${name}`).addEventListener('click', () => selectTab(name));
  $(`tab-${name}`).addEventListener('keydown', event => {
    if (!['ArrowLeft', 'ArrowRight'].includes(event.key)) return;
    event.preventDefault(); selectTab(tabs[(tabs.indexOf(name) + (event.key === 'ArrowRight' ? 1 : 2)) % 3], true);
  });
}
$('open-plan').addEventListener('click', () => selectTab('chat', true));
$('add-note').addEventListener('click', () => { selectTab('chat'); $('draft').focus(); });
$('search').addEventListener('click', () => { selectTab('records'); $('record-search').focus(); });
$('record-search').addEventListener('input', renderRecords);
function bindSheet(id, opener, closer) {
  const dialog = $(id); $(opener).addEventListener('click', () => dialog.showModal()); $(closer).addEventListener('click', () => dialog.close());
  dialog.addEventListener('close', () => $(opener).focus());
  dialog.addEventListener('click', event => { if (event.target !== dialog) return; const box = dialog.getBoundingClientRect(); if (event.clientX < box.left || event.clientX > box.right || event.clientY < box.top || event.clientY > box.bottom) dialog.close(); });
}
bindSheet('sheet', 'settings', 'close'); bindSheet('template-sheet', 'templates', 'close-templates');
for (const button of document.querySelectorAll('[data-theme-choice]')) button.addEventListener('click', () => { theme = button.dataset.themeChoice; applyTheme(); });
for (const button of document.querySelectorAll('[data-template]')) button.addEventListener('click', () => {
  const separator = state.draft && !state.draft.endsWith('\n') ? '\n' : '';
  dispatch({ type: 'input', value: state.draft + separator + button.dataset.template });
  $('template-sheet').addEventListener('close', () => $('draft').focus(), { once: true }); $('template-sheet').close();
});
render(); selectTab('chat');
