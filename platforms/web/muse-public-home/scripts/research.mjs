import fs from 'node:fs/promises';
import path from 'node:path';
import { createHash } from 'node:crypto';
import { pathToFileURL } from 'node:url';

const root = process.cwd();
const followup = process.argv.includes('--followup');
const prefix = followup ? 'scroll-' : '';
const sha = data => createHash('sha256').update(data).digest('hex');
const json = (name, data) => fs.writeFile(path.join(root, name), JSON.stringify(data, null, 2) + '\n');
const bundle = JSON.parse(await fs.readFile('evidence/rea-baseline.bundle.json', 'utf8'));
const baseline = bundle.records.find(record => record.operation === 'capture_browser_scenario');
const baseArtifacts = baseline.normalized_result.steps.at(-1).artifacts;
await fs.mkdir('evidence/raw', { recursive: true });
await fs.mkdir('assets/source', { recursive: true });
if (!followup) {
  await fs.writeFile('evidence/raw/rea-desktop.png', Buffer.from(baseArtifacts.screenshot.value.data_base64, 'base64'));
  await fs.writeFile('evidence/raw/rea-desktop.html', baseArtifacts.dom.value.text);
  await fs.writeFile('evidence/raw/rea-desktop.aria.txt', baseArtifacts.accessibility.value.text);
}

const playwrightPath = process.env.PLAYWRIGHT_PATH || path.join(process.env.APPDATA, 'npm/node_modules/rea-agents/node_modules/playwright-core/index.mjs');
const { chromium } = await import(pathToFileURL(playwrightPath));
const browser = await chromium.launch({ executablePath: process.env.CHROME_PATH || 'C:/Program Files/Google/Chrome/Application/chrome.exe', headless: true });
const context = await browser.newContext({ viewport: { width: 1440, height: 1000 }, deviceScaleFactor: 1, locale: 'en-US', timezoneId: 'Asia/Shanghai', colorScheme: 'light', reducedMotion: 'no-preference', serviceWorkers: 'block' });
const page = await context.newPage();
const sources = new Map();
const scriptCandidates = [];
const pending = new Set();
const errors = [];
page.on('pageerror', error => errors.push(error.message));
page.on('response', response => {
  const task = (async () => {
    const url = new URL(response.url());
    if (url.origin !== 'https://muse.ai' || !response.ok()) return;
    const kind = response.request().resourceType();
    if (!['stylesheet', 'font', 'image', 'script'].includes(kind)) return;
    const data = await response.body();
    if (kind === 'script') {
      const text = data.toString('utf8');
      if (!['Muse landing page', 'landing-introduction-video-title', 'Every Muse user', 'icon-envelope.webp'].some(marker => text.includes(marker))) return;
      scriptCandidates.push({ url: response.url(), bytes: data.length });
    }
    const target = path.posix.join('assets/source', url.pathname.replace(/^\//, ''));
    await fs.mkdir(path.dirname(target), { recursive: true });
    try {
      const existing = await fs.readFile(target);
      if (sha(existing) !== sha(data)) throw new Error('Source asset changed during capture: ' + target);
    } catch (error) {
      if (error.code !== 'ENOENT') throw error;
      await fs.writeFile(target, data);
    }
    sources.set(url.pathname, { url: response.url(), path: target, kind, bytes: data.length, sha256: sha(data), capturedAt: new Date().toISOString() });
  })().catch(error => errors.push('asset: ' + error.message));
  pending.add(task);
  task.finally(() => pending.delete(task));
});

const captures = [];
async function capture(name, details = {}) {
  const startedAt = new Date().toISOString();
  const pngPath = `evidence/raw/${prefix}${name}.png`;
  const png = await page.screenshot({ path: pngPath });
  const record = { name:prefix+name, path: pngPath, sha256: sha(png), startedAt, completedAt: new Date().toISOString(), url: page.url(), viewport: page.viewportSize(), scroll: await page.evaluate(() => ({ x: scrollX, y: document.querySelector('[class~="group/scroller"]')?.scrollTop ?? scrollY })), ...details };
  captures.push(record);
  console.log(JSON.stringify({ capture: name, scrollY: record.scroll.y }));
}

async function measure(label) {
  const data = await page.evaluate(() => {
    const scroller = document.querySelector('[class~="group/scroller"]');
    const scrollOffset = scroller?.scrollTop ?? scrollY;
    const props = ['display','position','boxSizing','width','height','maxWidth','minWidth','minHeight','padding','margin','gap','rowGap','columnGap','gridTemplateColumns','alignItems','justifyContent','fontFamily','fontSize','fontWeight','lineHeight','letterSpacing','color','backgroundColor','backgroundImage','border','borderRadius','boxShadow','opacity','transform','transition','animation','overflow','objectFit','aspectRatio','inset','zIndex'];
    const describe = el => {
      const css = getComputedStyle(el);
      const r = el.getBoundingClientRect();
      return { tag: el.tagName.toLowerCase(), role: el.getAttribute('role'), ariaLabel: el.getAttribute('aria-label'), id: el.id, class: el.getAttribute('class'), text: (el.innerText || el.getAttribute('alt') || '').trim().slice(0, 180), rect: { x:r.x, y:r.y + scrollOffset, width:r.width, height:r.height }, style:Object.fromEntries(props.map(prop => [prop, css[prop]])), outer: el.outerHTML.slice(0, 3000) };
    };
    const all = [...document.querySelectorAll('main section,main header,main footer,main h1,main h2,main h3,main p,main button,main input,main a,main img,main video,main form,main article')].map(describe);
    const top = [...document.querySelector('main').querySelectorAll('*')].filter(el => {
      const r=el.getBoundingClientRect(); return r.y + scrollY < 1100 && r.width > 0 && r.height > 0;
    }).map(describe);
    return { capturedAt:new Date().toISOString(), url:location.href, title:document.title, viewport:{width:innerWidth,height:innerHeight,dpr:devicePixelRatio}, page:{width:document.documentElement.scrollWidth,height:scroller?.scrollHeight ?? document.documentElement.scrollHeight,scrollOffset}, root:describe(document.documentElement), body:describe(document.body), all, top, fonts:[...document.fonts].map(font => ({family:font.family,weight:font.weight,style:font.style,status:font.status})), sheets:[...document.styleSheets].map(s=>s.href), images:[...document.images].map(img=>({alt:img.alt,src:img.src,currentSrc:img.currentSrc,srcset:img.srcset,naturalWidth:img.naturalWidth,naturalHeight:img.naturalHeight})), videos:[...document.querySelectorAll('video')].map(v=>({src:v.src,currentSrc:v.currentSrc,poster:v.poster,autoplay:v.autoplay,loop:v.loop,muted:v.muted,controls:v.controls,preload:v.preload,paused:v.paused,duration:Number.isFinite(v.duration)?v.duration:null})), animations:document.getAnimations().map(a=>({target:a.effect?.target?.outerHTML.slice(0,200),timing:a.effect?.getTiming(),currentTime:a.currentTime,playState:a.playState})) };
  });
  await json(`evidence/raw/${prefix}${label}-measurements.json`, data);
  return data;
}

try {
  await page.goto(baseline.parameters.start_url.url, { waitUntil:'domcontentloaded', timeout:45000 });
  await page.locator('input').first().waitFor({ timeout:30000 });
  await page.evaluate(() => document.fonts.ready);
  await page.waitForTimeout(2200);
  if (!followup) await capture('desktop-home');
  const desktop = await measure('desktop');
  await fs.writeFile(`evidence/raw/${prefix}current-desktop.html`, await page.content());
  await fs.writeFile(`evidence/raw/${prefix}current-desktop.aria.txt`, await page.locator('body').ariaSnapshot());
  const svg = await page.locator('svg').evaluateAll(els => els.map((el,i)=>({index:i,ariaLabel:el.getAttribute('aria-label'),role:el.getAttribute('role'),width:el.getAttribute('width'),height:el.getAttribute('height'),outer:el.outerHTML})));
  await json(`evidence/raw/${prefix}inline-svg.json`, svg);
  if (!followup) {
  await page.getByRole('button', {name:'Continue',exact:true}).hover();
  await page.waitForTimeout(250);
  await capture('desktop-button-hover', { action:'hover Continue; no click' });
  await measure('hover');
  await page.getByRole('textbox', {name:'Mobile number or email'}).focus();
  await capture('desktop-input-focus', {action:'focus empty input; no submission'});
  await page.getByRole('textbox', {name:'Mobile number or email'}).fill('Design study');
  await capture('desktop-input-filled', {action:'fill neutral draft; no submission'});
  await measure('input');
  await page.getByRole('textbox', {name:'Mobile number or email'}).fill('');
  await page.getByRole('textbox', {name:'Mobile number or email'}).blur();
  }
  const scrollTo = y => page.locator('[class~="group/scroller"]').evaluate((el,y)=>el.scrollTo({top:y,behavior:'instant'}), y);
  const height = desktop.page.height;
  for (const y of [1000,2100,2800,3540,4280,5020,5760,6480,7330,7900].filter(y=>y<height)) {
    await scrollTo(y);
    await page.waitForTimeout(700);
    await capture(`desktop-scroll-${y}`);
    if ([1000,2100,6480,7330].includes(y)) await measure(`position-${y}`);
  }
  await scrollTo(0);
  await page.getByRole('link',{name:'Learn more',exact:true}).last().click();
  await page.waitForTimeout(1500);
  await capture('desktop-learn-more', {action:'click in-page Learn more anchor'});
  await measure('scrolled');
  await scrollTo(0);
  await page.setViewportSize({width:390,height:844});
  await page.waitForTimeout(1200);
  await capture('mobile-home');
  await measure('mobile');
  await scrollTo(1000);
  await page.waitForTimeout(800);
  await capture('mobile-scroll-1000');
  await page.emulateMedia({reducedMotion:'reduce'});
  await measure('reduced-motion');
  if (followup) {
    await scrollTo(0);
    await page.setViewportSize({width:768,height:1024});
    await page.waitForTimeout(700);
    await capture('tablet-home');
    await measure('tablet');
    await page.setViewportSize({width:1440,height:1000});
    await page.emulateMedia({reducedMotion:'no-preference'});
    await page.locator('.landing-rebus-avatar-slot').hover();
    await page.waitForTimeout(400);
    await capture('avatar-hover');
    await measure('avatar-hover');
  }
  await Promise.allSettled([...pending]);
  await json(`evidence/raw/${prefix}source-assets.json`,[...sources.values()]);
  await json(`evidence/raw/${prefix}research-captures.json`,captures);
  await json(`evidence/raw/${prefix}research-environment.json`,{browserVersion:browser.version(),platform:process.platform,arch:process.arch,startedFrom:baseline.evidence_id,capturedAt:new Date().toISOString(),errors,scriptCandidates,observations:'Fresh unauthenticated browser. No login, registration or account requests submitted. Scroll, hover, focus and draft input only. Screenshots and DOM/style measurements were collected sequentially, not atomically.'});
  console.log(JSON.stringify({complete:true,captures:captures.length,assets:sources.size,pageHeight:height,scripts:scriptCandidates,errors}));
} finally {
  await browser.close();
}
