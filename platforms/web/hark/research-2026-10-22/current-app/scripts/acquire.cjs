// Inert AST/CSS dependency discovery plus bounded, credential-free static GETs.
const fs = require('node:fs');
const path = require('node:path');
const crypto = require('node:crypto');
const acorn = require('/Users/cjr/.npm/_npx/0aa74679bec75e15/node_modules/acorn');
const postcss = require('/Users/cjr/.npm/_npx/110f701c48e68d66/node_modules/postcss');
const out = path.resolve(__dirname, '..');
const repo = path.resolve(out, '../../../../..');
const sourcePath = path.join(repo, '.runtime/hark-2026-10-22/current-app-main.js');
const mainUrl = 'https://assets.hark.com/assets/main-BgE0mqJD.js';
const resume = process.argv.includes('--resume');
const wallpaperBytes = resume ? fs.statSync(path.join(out, 'resources/wallpaper_core.html')).size : 0;
const limits = { files: resume ? 599 : 200, bytes: resume ? 80000000 - wallpaperBytes : 40000000, perRequestTimeoutMs: 20000 };
const priorState = resume ? JSON.parse(fs.readFileSync(path.join(out, 'acquisition.json'))) : null;
const hash = b => crypto.createHash('sha256').update(b).digest('hex');
const initialState = { schemaVersion: 1, kind: 'credential-free-current-app-static-archive', startedAt: new Date().toISOString(), source: { url: mainUrl, suppliedPath: '.runtime/hark-2026-10-22/current-app-main.js', originalRuntimeEvidence: 'Coordinating investigator observed current authenticated app; acquisition here is B public shipped source only.' }, limits, policy: { credentials: 'omit; no cookie/auth/browser/profile access', requestOrigin: 'https://assets.hark.com', scriptAndStylePathPrefix: '/assets/', fontPathPrefix: '/fonts/', redirects: 'not followed', targetExecution: false, apiRequests: false }, parser: { javascript: { name: 'acorn', version: acorn.version }, css: { name: 'postcss', version: require('/Users/cjr/.npm/_npx/110f701c48e68d66/node_modules/postcss/package.json').version } }, items: [], edges: [], fontFaces: [], parseErrors: [], exclusions: [], pendingAtBudget: [] };
const state = priorState || initialState;
if (resume) {
  (state.resumeHistory ||= []).push({ resumedAt: new Date().toISOString(), retainedAssetFiles: state.items.filter(i => i.available).length, priorPending: state.pendingAtBudget.length, previousLimits: state.limits });
  state.limits = limits;
  state.globalBudget = { files: 600, bytes: 80000000, separatelyRetainedWallpaperFiles: 1, separatelyRetainedWallpaperBytes: wallpaperBytes };
}
fs.mkdirSync(path.join(out, 'resources'), { recursive: true });
fs.mkdirSync(path.join(out, 'fonts'), { recursive: true });
const retained = new Set(state.items.filter(i => i.available).map(i => i.url));
const queue = resume ? state.pendingAtBudget.filter(i => !retained.has(i.url)) : [];
const known = new Set(resume ? [...state.items.map(i => i.url), ...state.edges.map(e => e.to), ...queue.map(i => i.url)] : []);
let requested = resume ? state.items.length : 1, totalBytes = resume ? state.items.filter(i => i.available).reduce((sum, i) => sum + i.bytes, 0) : 0;
const span = (s, start, end) => ({ utf16CodeUnits: { start, endExclusive: end }, utf8Bytes: { start: Buffer.byteLength(s.slice(0, start)), endExclusive: Buffer.byteLength(s.slice(0, end)) } });
function allowed(url, kind) {
  try { const u = new URL(url); return u.origin === 'https://assets.hark.com' && !u.search && !u.hash && !u.username && !u.password && (kind === 'font' ? /^\/fonts\/[A-Za-z0-9_.-]+\.(?:woff2?|ttf|otf)$/.test(u.pathname) : /^\/assets\/[A-Za-z0-9_.-]+\.(?:js|css)$/.test(u.pathname)); } catch { return false; }
}
function enqueue(specifier, from, kind, method, sourceRange, priority = false) {
  let url;
  try { url = new URL(specifier, from).href; } catch { state.exclusions.push({ specifier, from, reason: 'invalid-url' }); return; }
  if (!allowed(url, kind)) { state.exclusions.push({ url, from, kind, reason: 'outside exact static origin/path/type scope' }); return; }
  state.edges.push({ from, to: url, kind, method, sourceRange });
  if (!known.has(url)) { known.add(url); const q = { url, kind }; priority ? queue.unshift(q) : queue.push(q); }
}
function walk(node, fn) {
  if (!node || typeof node !== 'object') return;
  if (node.type) fn(node);
  for (const value of Object.values(node)) if (Array.isArray(value)) value.forEach(n => walk(n, fn)); else if (value && typeof value === 'object') walk(value, fn);
}
function cssUrls(value) {
  const result = [];
  // Source is a parsed CSS declaration value; capture complete url() tokens only.
  const rx = /\burl\(\s*(?:"([^"\\]*)"|'([^'\\]*)'|([^\s()'"\\]+))\s*\)/g;
  let m; while ((m = rx.exec(value))) result.push({ value: m[1] ?? m[2] ?? m[3], start: m.index, end: rx.lastIndex });
  return result;
}
function discover(item, bytes) {
  const text = bytes.toString('utf8');
  if (item.url.endsWith('.js')) {
    let ast;
    try { ast = acorn.parse(text, { ecmaVersion: 'latest', sourceType: 'module' }); } catch (e) { state.parseErrors.push({ url: item.url, error: e.message }); return; }
    for (const n of ast.body) if (['ImportDeclaration', 'ExportNamedDeclaration', 'ExportAllDeclaration'].includes(n.type) && n.source?.value) enqueue(n.source.value, item.url, 'module', n.type, span(text, n.source.start, n.source.end), true);
    walk(ast, n => {
      if (n.type === 'ImportExpression' && n.source.type === 'Literal' && typeof n.source.value === 'string') enqueue(n.source.value, item.url, 'module', 'literal-dynamic-import', span(text, n.source.start, n.source.end));
      if (n.type === 'VariableDeclarator' && n.id.type === 'Identifier' && n.id.name === '__vite__mapDeps') walk(n.init, a => {
        if (a.type === 'ArrayExpression' && a.elements.length && a.elements.every(e => e?.type === 'Literal' && typeof e.value === 'string')) for (const e of a.elements) enqueue(e.value, item.url, 'module', 'complete-AST-vite-dependency-table-entry', span(text, e.start, e.end));
      });
    });
  } else if (item.url.endsWith('.css')) {
    let ast;
    try { ast = postcss.parse(text, { from: item.url }); } catch (e) { state.parseErrors.push({ url: item.url, error: e.message }); return; }
    ast.walkAtRules('font-face', rule => {
      const properties = Object.fromEntries(rule.nodes.filter(n => n.type === 'decl').map(n => [n.prop, n.value]));
      const urls = cssUrls(properties.src || '');
      const start = rule.source.start.offset, end = rule.source.end.offset + 1;
      const face = { sourceUrl: item.url, sourcePath: item.path, sourceSha256: item.sha256, sourceRange: span(text, start, end), properties, urls: urls.map(v => new URL(v.value, item.url).href), classification: 'B original CSS declaration; runtime use unverified' };
      state.fontFaces.push(face);
      for (const u of urls) enqueue(u.value, item.url, 'font', 'parsed-font-face-complete-url-token', face.sourceRange, true);
    });
    ast.walkAtRules('import', rule => {
      const urls = cssUrls(rule.params);
      if (!urls.length) { const m = rule.params.match(/^\s*["']([^"'\\]+)["']/); if (m) urls.push({ value: m[1] }); }
      for (const u of urls) enqueue(u.value, item.url, 'module', 'parsed-css-import', span(text, rule.source.start.offset, rule.source.end.offset + 1), true);
    });
  }
}
function persist() {
  state.completedAt = new Date().toISOString();
  state.summary = { requestSlotsUsedIncludingSuppliedMain: requested, successfulFiles: state.items.filter(i => i.available).length, savedResourceBytes: totalBytes, combinedRawFilesIncludingWallpaper: state.items.filter(i => i.available).length + (resume ? 1 : 0), combinedRawBytesIncludingWallpaper: totalBytes + wallpaperBytes, failedRequests: state.items.filter(i => !i.available).length, uniqueDiscovered: known.size, queuedUnacquired: queue.length, fontFaceDeclarations: state.fontFaces.length, parseErrors: state.parseErrors.length };
  fs.writeFileSync(path.join(out, 'acquisition.json'), JSON.stringify(state, null, 2) + '\n');
}
async function run() {
  if (!resume) {
  const main = fs.readFileSync(sourcePath);
  const mainItem = { url: mainUrl, path: 'resources/main-BgE0mqJD.js', bytes: main.length, sha256: hash(main), available: true, method: 'copy exact coordinating credential-free curl response; no refetch', acquiredAt: new Date().toISOString(), classification: 'B' };
  known.add(mainUrl); fs.writeFileSync(path.join(out, mainItem.path), main, { flag: 'wx' }); state.items.push(mainItem); totalBytes += main.length;
  discover(mainItem, main);
  const css = ['C87UuXAZ.css','B4F-AcTa.css','IxjshK-_.css','CtFaX487.css','CSkhKE46.css','DKLwRN3w.css','DJ9rwgU8.css','CxGbbAGp.css','Bw1XUwC2.css','DJZ-Gwdn.css'];
  // Prioritize the coordinator-observed CSS without dropping already-known entries.
  for (const name of css.reverse()) {
    const url = new URL(name, mainUrl).href;
    enqueue(url, mainUrl, 'module', 'coordinator-observed-stylesheet', null, true);
    const ix = queue.findIndex(i => i.url === url); if (ix > 0) queue.unshift(queue.splice(ix, 1)[0]);
  }
  }
  while (queue.length && requested < limits.files && totalBytes < limits.bytes) {
    const next = queue.shift(); requested++;
    const record = { url: next.url, classification: 'B', method: 'normal HTTP GET; credentials omitted; redirects not followed', startedAt: new Date().toISOString(), available: false };
    try {
      const response = await fetch(next.url, { credentials: 'omit', redirect: 'manual', signal: AbortSignal.timeout(limits.perRequestTimeoutMs), headers: { 'Accept-Encoding': 'identity' } });
      record.status = response.status; record.contentType = response.headers.get('content-type');
      for (const n of ['etag','last-modified','content-length']) if (response.headers.has(n)) (record.responseMetadata ||= {})[n] = response.headers.get(n);
      if (!response.ok) { record.error = `HTTP ${response.status}`; await response.body?.cancel(); }
      else {
        const declaredLength = Number(response.headers.get('content-length') || 0);
        const remaining = limits.bytes - totalBytes;
        if (declaredLength > remaining) { record.error = 'budget: Content-Length exceeds remaining byte cap'; await response.body?.cancel(); }
        else {
          const chunks = []; let length = 0;
          for await (const chunk of response.body) { length += chunk.length; if (length > remaining) throw new Error('budget: streamed response exceeds remaining byte cap'); chunks.push(chunk); }
          const bytes = Buffer.concat(chunks); const name = new URL(next.url).pathname.split('/').pop();
          record.path = (next.kind === 'font' ? 'fonts/' : 'resources/') + name; record.bytes = bytes.length; record.sha256 = hash(bytes); record.available = true;
          fs.writeFileSync(path.join(out, record.path), bytes, { flag: 'wx' }); totalBytes += bytes.length;
          if (next.kind !== 'font') discover(record, bytes);
        }
      }
    } catch (e) { record.error = e.message; }
    record.completedAt = new Date().toISOString(); state.items.push(record); persist();
    if (requested % 10 === 0) process.stdout.write(JSON.stringify(state.summary) + '\n');
  }
  state.pendingAtBudget = queue;
  state.completeness = queue.length ? 'bounded partial; pendingAtBudget explicitly retained' : 'all discovered dependency imports and CSS font URLs attempted within scope; runtime binding remains separate';
  persist(); process.stdout.write(JSON.stringify(state.summary) + '\n');
}
run().catch(e => { state.fatalError = e.stack; persist(); process.stderr.write(e.stack + '\n'); process.exitCode = 1; });
