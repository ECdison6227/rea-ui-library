// New-run capture using the original study's CDP transport. No cookies,
// authentication headers, storage values, or browser profile are exported.
const fs = require('node:fs/promises');
const path = require('node:path');
const crypto = require('node:crypto');
const { connectCDP } = require('../../tools/cdp-worker-client.cjs');
const args = {};
for (let i = 2; i < process.argv.length; i += 2) args[process.argv[i].replace(/^--/, '')] = process.argv[i + 1];
const digest = data => crypto.createHash('sha256').update(data).digest('hex');
const pause = ms => new Promise(resolve => setTimeout(resolve, ms));
const safeUrl = raw => { const u = new URL(raw); return u.origin + u.pathname; };
async function main() {
  if (!args.endpoint || !args.target || !args.output || !args.label) throw new Error('endpoint, target, output and label are required');
  const endpoint = new URL(args.endpoint);
  if (!['127.0.0.1', 'localhost', '[::1]'].includes(endpoint.hostname)) throw new Error('CDP endpoint must be loopback');
  if (!/^[a-z0-9-]+$/.test(args.label)) throw new Error('Use a safe capture label');
  const listed = await fetch(args.endpoint + '/json/list').then(r => r.json());
  const target = listed.find(t => t.id === args.target && t.type === 'page');
  if (!target || new URL(target.url).origin !== 'https://hark.com') throw new Error('The exact selected Hark page target is unavailable');
  const cdp = await connectCDP(target.webSocketDebuggerUrl);
  const startedAt = new Date().toISOString();
  const output = path.resolve(args.output);
  await fs.mkdir(output, { recursive: true });
  try {
    await cdp.send('Page.enable');
    await cdp.send('Runtime.enable');
    if (args.url) {
      if (new URL(args.url).origin !== 'https://hark.com') throw new Error('Navigation is restricted to the selected Hark origin');
      const nav = await cdp.send('Page.navigate', { url: args.url });
      if (nav.errorText || nav.isDownload) throw new Error(nav.errorText || 'Unexpected download');
      for (let i = 0; i < 40; i++) {
        const ready = await cdp.send('Runtime.evaluate', { expression: 'document.readyState', returnByValue: true });
        if (ready.result.value === 'complete') break;
        await pause(250);
      }
      await pause(Number(args.settle || 1500));
    }
    if (args.width) {
      await cdp.send('Emulation.setDeviceMetricsOverride', { width: Number(args.width), height: Number(args.height || 844), deviceScaleFactor: 1, mobile: false });
      await pause(400);
    }
    if (args.mode === 'styles') {
      const sheets = [];
      cdp.on('CSS.styleSheetAdded', event => sheets.push(event.header));
      await cdp.send('DOM.enable');
      await cdp.send('CSS.enable');
      await pause(250);
      const items = [];
      for (const sheet of sheets) {
        if (!sheet.sourceURL || new URL(sheet.sourceURL).origin !== 'https://hark.com') continue;
        try {
          const r = await cdp.send('CSS.getStyleSheetText', { styleSheetId: sheet.styleSheetId });
          const bytes = Buffer.from(r.text, 'utf8');
          const hash = digest(bytes);
          const sourceName = path.basename(new URL(sheet.sourceURL).pathname);
          const name = !sheet.isInline && sourceName.endsWith('.css') ? sourceName : 'inline-' + hash.slice(0,16) + '.css';
          const file = path.join(output, name);
          try {
            const old = await fs.readFile(file);
            if (!old.equals(bytes)) throw new Error('Refusing different stylesheet bytes at ' + name);
          } catch (e) { if(e.code !== 'ENOENT') throw e; await fs.writeFile(file, bytes, { flag:'wx' }); }
          items.push({ path:name, url:sheet.sourceURL, isInline:sheet.isInline, origin:sheet.origin, bytes:bytes.length, sha256:hash, classification:'B', method:'CDP CSS.getStyleSheetText for actual registered stylesheet; saved decoded text identity' });
        } catch(error) { items.push({ url:sheet.sourceURL, error:error.message, available:false }); }
      }
      await fs.writeFile(path.join(output,args.label+'-styles-manifest.json'),JSON.stringify({sourceIdentity:'https://hark.com',sourceUrl:safeUrl(target.url),startedAt,completedAt:new Date().toISOString(),items},null,2)+'\n',{flag:'wx'});
      process.stdout.write(JSON.stringify({label:args.label,stylesheets:items.length,saved:items.filter(i=>i.path).length,items})+'\n');
      return;
    }
    if (args.mode === 'resources') {
      const tree = await cdp.send('Page.getResourceTree');
      const frame = tree.frameTree;
      const candidates = frame.resources.filter(r => new URL(r.url).origin === 'https://hark.com' && (r.type === 'Stylesheet' || r.type === 'Font'));
      const items = [];
      for (const r of candidates) {
        try {
          const content = await cdp.send('Page.getResourceContent', { frameId: frame.frame.id, url: r.url });
          const bytes = Buffer.from(content.content, content.base64Encoded ? 'base64' : 'utf8');
          const name = path.basename(new URL(r.url).pathname);
          const file = path.join(output, name);
          try {
            const existing = await fs.readFile(file);
            if (!existing.equals(bytes)) throw new Error('Refusing different bytes at ' + name);
          } catch (e) {
            if (e.code !== 'ENOENT') throw e;
            await fs.writeFile(file, bytes, { flag: 'wx' });
          }
          items.push({ path: name, url: r.url, type: r.type, mimeType: r.mimeType, bytes: bytes.length, sha256: digest(bytes), classification: 'B', method: 'CDP Page.getResourceContent, actual loaded page cache' });
        } catch (error) { items.push({ url: r.url, type: r.type, error: error.message, classification: 'B', available: false }); }
      }
      const manifest = { sourceIdentity: 'https://hark.com', sourceUrl: safeUrl(target.url), targetId: target.id, startedAt, completedAt: new Date().toISOString(), items };
      await fs.writeFile(path.join(output, args.label + '-manifest.json'), JSON.stringify(manifest, null, 2) + '\n', { flag: 'wx' });
      process.stdout.write(JSON.stringify({ label: args.label, resources: items.length, saved: items.filter(i => i.path).length, errors: items.filter(i => i.error), output }) + '\n');
      return;
    }
    if (args.mode === 'evaluate') {
      const expression = await fs.readFile(args.expression, 'utf8');
      const r = await cdp.send('Runtime.evaluate', { expression, returnByValue: true, awaitPromise: true });
      if (r.exceptionDetails) throw new Error(r.exceptionDetails.text);
      const file = path.join(output, args.label + '.json');
      const value = { startedAt, completedAt: new Date().toISOString(), sourceIdentity: 'https://hark.com', targetId: target.id, method: 'independent CDP evaluation', value: r.result.value };
      await fs.writeFile(file, JSON.stringify(value, null, 2) + '\n', { flag: 'wx' });
      process.stdout.write(JSON.stringify({ label: args.label, output: file, value: args.summary === 'true' ? r.result.value : undefined }) + '\n');
      return;
    }
    if (output.includes(path.sep + 'public' + path.sep)) {
      const state = await cdp.send('Runtime.evaluate', { expression:'location.pathname', returnByValue:true });
      if (/^\/(home|chat|search|settings|plans|project|projects)(\/|$)/.test(state.result.value)) throw new Error('Authenticated source material must be captured in the private runtime directory');
    }
    const expression = `(() => {
      const cleanUrl = raw => { try { const u = new URL(raw, location.href); return {url:u.origin==='null'?u.protocol+u.pathname:u.origin+u.pathname,queryNames:[...u.searchParams.keys()]}; } catch { return null; } };
      const box = el => { const r=el.getBoundingClientRect();return {x:r.x,y:r.y,width:r.width,height:r.height}; };
      const names = ['fontFamily','fontSize','fontWeight','lineHeight','letterSpacing','color','backgroundColor','borderRadius','border','boxShadow','backdropFilter','padding','gap','width','height','display','position'];
      const style = el => {const s=getComputedStyle(el);return Object.fromEntries(names.map(n=>[n,s[n]]));};
      const controls = Array.from(document.querySelectorAll('button,a,input,textarea,select,[role=button],[role=dialog],[role=tab]')).map(el=>({tag:el.tagName,role:el.getAttribute('role'),name:el.getAttribute('aria-label')||Array.from(el.labels||[]).map(l=>l.textContent.trim()).join(' ')||el.innerText||el.getAttribute('placeholder')||'',type:el.type,required:!!el.required,focused:document.activeElement===el,disabled:!!el.disabled,ariaDisabled:el.getAttribute('aria-disabled'),selected:el.getAttribute('aria-selected'),testId:el.getAttribute('data-testid'),href:el.href?cleanUrl(el.href):null,rect:box(el),style:style(el)}));
      const roots = Array.from(document.querySelectorAll('html,body,main,[data-theme],.theme-dark,.theme-light')).slice(0,10);
      const tokens = roots.map(el=>{const s=getComputedStyle(el);return {tag:el.tagName,className:el.className,items:Object.fromEntries(Array.from(s).filter(k=>k.startsWith('--')).map(k=>[k,s.getPropertyValue(k).trim()]))};});
      const roles = ['h1','h2','h3','p','nav','aside','section','header','footer'].flatMap(sel=>Array.from(document.querySelectorAll(sel)).slice(0,6).map(el=>({tag:el.tagName,text:el.innerText.slice(0,180),rect:box(el),style:style(el)})));
      return {url:cleanUrl(location.href),title:document.title,viewport:{width:innerWidth,height:innerHeight,dpr:devicePixelRatio},language:document.documentElement.lang,text:document.body.innerText,html:document.documentElement.outerHTML,controls,tokens,roles,links:Array.from(document.querySelectorAll('a[href]')).map(a=>({text:a.innerText,url:cleanUrl(a.href)})),media:Array.from(document.querySelectorAll('img,video,source')).map(el=>({tag:el.tagName,src:el.currentSrc||el.src||el.getAttribute('src'),dataSrc:el.getAttribute('data-src'),poster:el.poster,rect:box(el)})),scripts:Array.from(document.scripts).map(s=>s.src).filter(s=>s.startsWith(location.origin+'/_next/')||s.startsWith(location.origin+'/assets/')),styles:Array.from(document.querySelectorAll('link[rel=stylesheet]')).map(l=>l.href)};
    })()`;
    const observed = await cdp.send('Runtime.evaluate', { expression, returnByValue: true });
    if (observed.exceptionDetails) throw new Error(observed.exceptionDetails.text);
    const snapshot = observed.result.value;
    if (!snapshot || snapshot.url.url.split('/').slice(0,3).join('/') !== 'https://hark.com') throw new Error('Capture left the Hark origin');
    const html = snapshot.html;
    delete snapshot.html;
    const metrics = await cdp.send('Page.getLayoutMetrics');
    const imageOptions = {format:'png',fromSurface:true,captureBeyondViewport:args.full === 'true'};
    const documentHeight = Math.ceil(metrics.cssContentSize.height);
    const maximumCssHeight = Math.floor(12000 / snapshot.viewport.dpr);
    const imageHeight = args.full === 'true' ? Math.min(documentHeight, maximumCssHeight) : snapshot.viewport.height;
    if (args.full === 'true') imageOptions.clip = {x:0,y:0,width:Math.ceil(metrics.cssContentSize.width),height:imageHeight,scale:1};
    const shot = await cdp.send('Page.captureScreenshot', imageOptions);
    const bytes = Buffer.from(shot.data, 'base64');
    const files = [{suffix:'.html',data:html},{suffix:'.png',data:bytes}];
    const receipt = {label:args.label,sourceIdentity:'https://hark.com',targetId:target.id,startedAt,completedAt:new Date().toISOString(),method:'independent Node/CDP; sequential DOM, computed styles and screenshot; non-atomic',classification:'A',review:{status:'unreviewed'},screenshotScope:{requestedFullPage:args.full==='true',documentHeightCssPx:documentHeight,capturedHeightCssPx:imageHeight,croppedBottom:args.full==='true' && imageHeight<documentHeight,reason:imageHeight<documentHeight?'Representative top slice capped at 12000 physical pixels to stay within compositor limits':null},files:files.map(f=>({path:args.label+f.suffix,sha256:digest(f.data),bytes:Buffer.byteLength(f.data)}))};
    for (const f of files) await fs.writeFile(path.join(output,args.label+f.suffix),f.data,{flag:'wx'});
    await fs.writeFile(path.join(output,args.label+'.json'),JSON.stringify({receipt,...snapshot},null,2)+'\n',{flag:'wx'});
    process.stdout.write(JSON.stringify({label:args.label,url:snapshot.url.url,title:snapshot.title,textLength:snapshot.text.length,controls:snapshot.controls.length,links:snapshot.links.filter(l=>l.url?.url.startsWith('https://hark.com')),output})+'\n');
  } finally {
    if (args.width) await cdp.send('Emulation.clearDeviceMetricsOverride').catch(()=>{});
    cdp.close();
  }
}
main().catch(error => { process.stderr.write(error.stack + '\n'); process.exitCode = 1; });
