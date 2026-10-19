// Browser MCP helper: safe, value-free network probe and new-row UI samples.
// Requires an existing authorized Playwright page/context and CDP session.
function shape(value, depth = 0) {
  if (value === null) return 'null';
  if (Array.isArray(value)) return { type: 'array', length: value.length, items: value.slice(0, 12).map(x => shape(x, depth + 1)) };
  if (typeof value === 'object') {
    if (depth > 5) return { type: 'object', keys: Object.keys(value) };
    return Object.fromEntries(Object.entries(value).map(([key, v]) => [key, shape(v, depth + 1)]));
  }
  return typeof value === 'string' ? { type: 'string', length: value.length } : typeof value;
}
function summarizePatches(data) {
  const out = [];
  function walk(v, depth = 0) {
    if (!v || typeof v !== 'object' || depth > 6) return;
    if (typeof v.type === 'string' || typeof v.op === 'string') out.push({
      type: v.type, op: v.op, fieldNames: v.fields && typeof v.fields === 'object' ? Object.keys(v.fields) : undefined,
      keys: Object.keys(v), messageId: typeof v.message_id === 'string' ? v.message_id : undefined
    });
    if (Array.isArray(v)) v.slice(0, 30).forEach(x => walk(x, depth + 1));
    else Object.values(v).forEach(x => walk(x, depth + 1));
  }
  walk(data);
  return out;
}
function attachNetwork(cdp) {
  const started = Date.now(), events = [], requests = new Map();
  const safeURL = raw => { try { const u = new URL(raw); return { origin: u.origin, path: u.pathname, queryNames: [...u.searchParams.keys()] }; } catch { return null; } };
  const onRequest = e => {
    const url = safeURL(e.request.url);
    if (!url || !['https://hark.com', 'https://api.hark.com'].includes(url.origin)) return;
    requests.set(e.requestId, url);
    events.push({ atMs: Date.now() - started, kind: 'request', requestId: e.requestId, method: e.request.method, resourceType: e.type, url });
  };
  const onResponse = e => { if (requests.has(e.requestId)) events.push({ atMs: Date.now() - started, kind: 'response', requestId: e.requestId, status: e.response.status, mimeType: e.response.mimeType, url: requests.get(e.requestId) }); };
  const onSSE = e => {
    let parsed; try { parsed = JSON.parse(e.data); } catch { parsed = null; }
    events.push({ atMs: Date.now() - started, kind: 'sse', requestId: e.requestId, eventName: e.eventName, eventId: e.eventId,
      bytes: Buffer.byteLength(e.data), seq: parsed?.seq, shape: shape(parsed), patches: summarizePatches(parsed) });
  };
  const onWS = e => events.push({ atMs: Date.now() - started, kind: 'websocket-created', url: safeURL(e.url) });
  cdp.on('Network.requestWillBeSent', onRequest); cdp.on('Network.responseReceived', onResponse);
  cdp.on('Network.eventSourceMessageReceived', onSSE); cdp.on('Network.webSocketCreated', onWS);
  return { started, events, stop() { cdp.off('Network.requestWillBeSent', onRequest); cdp.off('Network.responseReceived', onResponse); cdp.off('Network.eventSourceMessageReceived', onSSE); cdp.off('Network.webSocketCreated', onWS); } };
}
function armDOM() {
  const rowId = e => e.getAttribute('data-message-id') || e.querySelector('[data-message-id]')?.getAttribute('data-message-id');
  const baseline = [...document.querySelectorAll('[data-transcript-row]')].map(rowId);
  window.__harkProbe = { started: performance.now(), baseline, samples: [], timer: null };
  function tick() {
    const p = window.__harkProbe;
    const rows = [...document.querySelectorAll('[data-transcript-row]')].filter(e => !p.baseline.includes(rowId(e)));
    const scroll = document.querySelector('[data-testid="message-list"]');
    p.samples.push({ atMs: performance.now() - p.started,
      rows: rows.map(e => ({ id: rowId(e), kind: e.getAttribute('data-entry-kind') || e.querySelector('[data-entry-kind]')?.getAttribute('data-entry-kind'), textLength: e.textContent.length,
        text: e.textContent.slice(0, 1600), height: e.getBoundingClientRect().height,
        testIds: [...e.querySelectorAll('[data-testid]')].map(x => x.getAttribute('data-testid')),
        phaseAttributes: [...e.querySelectorAll('*')].flatMap(x => [...x.attributes].filter(a => /phase|stream|pending|loading/.test(a.name)).map(a => [a.name, a.value])).slice(0, 30) })),
      scroll: scroll ? { top: scroll.scrollTop, height: scroll.clientHeight, total: scroll.scrollHeight, bottomDistance: scroll.scrollHeight - scroll.clientHeight - scroll.scrollTop } : null,
      buttons: [...document.querySelectorAll('button')].filter(e => /停止|Stop|取消|Cancel|发送|Send/.test(e.getAttribute('aria-label') || e.textContent)).map(e => ({ text: e.textContent, label: e.getAttribute('aria-label'), disabled: e.disabled }))
    });
    if (p.samples.length > 900) clearInterval(p.timer);
  }
  tick(); window.__harkProbe.timer = setInterval(tick, 100);
  return { baselineRows: baseline.length };
}
module.exports = { attachNetwork, armDOM };
