// Direct CDP client for a selected Hark SharedWorker. It reads network events
// without exporting cookies, request headers, storage, or authentication values.
const { EventEmitter } = require('node:events');
async function connectCDP(url, WebSocketClass = WebSocket) {
  const ws = new WebSocketClass(url), emitter = new EventEmitter();
  const pending = new Map(); let nextId = 0;
  ws.addEventListener('message', event => {
    const msg = JSON.parse(String(event.data));
    if (msg.id) {
      const p = pending.get(msg.id); if (!p) return;
      pending.delete(msg.id); msg.error ? p.reject(new Error(msg.error.message)) : p.resolve(msg.result);
    } else if (msg.method) emitter.emit(msg.method, msg.params);
  });
  await new Promise((resolve, reject) => { ws.addEventListener('open', resolve, {once:true}); ws.addEventListener('error', reject, {once:true}); });
  emitter.send = (method, params = {}) => new Promise((resolve, reject) => {
    const id = ++nextId; pending.set(id, {resolve, reject}); ws.send(JSON.stringify({id, method, params}));
  });
  emitter.close = () => ws.close();
  return emitter;
}
module.exports = { connectCDP };
