import http from 'node:http';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

export const referenceRoot = path.dirname(fileURLToPath(import.meta.url));
const studyRoot = path.dirname(referenceRoot);
const prefix = '/research-2026-10-22/';
const currentFontAllowlist = new Set(['current-app/fonts/GT-Standard-L-Standard-Bold.woff2']);
const types = { '.html':'text/html; charset=utf-8', '.mjs':'text/javascript; charset=utf-8', '.css':'text/css; charset=utf-8', '.json':'application/json; charset=utf-8', '.png':'image/png', '.svg':'image/svg+xml', '.woff2':'font/woff2', '.md':'text/plain; charset=utf-8' };

export function createReferenceServer() {
  return http.createServer((req, res) => {
    const url = new URL(req.url, 'http://127.0.0.1');
    if (url.pathname === '/') { res.writeHead(302, { Location: prefix + 'reference/' }).end(); return; }
    if (!['GET','HEAD'].includes(req.method)) { res.writeHead(405).end(); return; }
    // Two inspected historical neutral-content baselines only; no general raw-evidence route.
    const baseline = { '/historical-baseline/summary.png':'15-summary-open.png', '/historical-baseline/mobile.png':'27-mobile-chat.png' }[url.pathname];
    if (baseline) {
      const file=path.resolve(studyRoot,'../evidence/screenshots',baseline);
      res.writeHead(200,{'Content-Type':'image/png','Cache-Control':'no-store'});
      if(req.method==='HEAD')res.end();else fs.createReadStream(file).pipe(res);
      return;
    }
    let relative;
    try { relative = decodeURIComponent(url.pathname.slice(prefix.length)); } catch { res.writeHead(400).end(); return; }
    if (relative.includes('\\') || relative.includes('\0') || relative.split('/').some(part => part === '..' || part.startsWith('.'))) { res.writeHead(404).end(); return; }
    const permitted = url.pathname.startsWith(prefix) && (
      relative.startsWith('reference/') || relative.startsWith('comparison/') ||
      /^public\/resources\/[^/]+\.woff2$/.test(relative) ||
      currentFontAllowlist.has(relative) ||
      /^historical-assets\/svg\/glyph-[a-z0-9-]+\.svg$/.test(relative)
    );
    const file = path.resolve(studyRoot, relative.endsWith('/') ? relative + 'index.html' : relative);
    if (!permitted || !file.startsWith(studyRoot + path.sep) || !fs.existsSync(file) || !fs.statSync(file).isFile() || !fs.realpathSync(file).startsWith(fs.realpathSync(studyRoot) + path.sep)) { res.writeHead(404).end(); return; }
    res.writeHead(200, {
      'Content-Type': types[path.extname(file)] || 'application/octet-stream',
      'Cache-Control':'no-store',
      'Content-Security-Policy': "default-src 'self'; connect-src 'self'; img-src 'self' data:; style-src 'self' 'unsafe-inline'; font-src 'self'; script-src 'self'; object-src 'none'; base-uri 'self'; frame-ancestors 'none'"
    });
    if (req.method === 'HEAD') res.end(); else fs.createReadStream(file).pipe(res);
  });
}

if (process.argv[1] && path.resolve(process.argv[1]) === fileURLToPath(import.meta.url)) {
  const port = Number(process.env.PORT || 4393);
  const server = createReferenceServer();
  server.listen(port, '127.0.0.1', () => console.log(`Independent D reference: http://127.0.0.1:${server.address().port}${prefix}reference/`));
}
