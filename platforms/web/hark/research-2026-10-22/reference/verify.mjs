import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import assert from 'node:assert/strict';
import { pathToFileURL } from 'node:url';
import { once } from 'node:events';
import { execFileSync } from 'node:child_process';
import { createReferenceServer, referenceRoot } from './serve.mjs';

// Optional developer tooling; no dependency installation or source-site browser attach.
const playwrightModule=process.env.PLAYWRIGHT_MODULE;
if(!playwrightModule)throw Error('Set PLAYWRIGHT_MODULE to an already installed playwright module path. No packages are downloaded.');
const { chromium }=await import(pathToFileURL(path.resolve(playwrightModule)).href);
const out=path.resolve(referenceRoot,'../comparison');
fs.mkdirSync(path.join(out,'captures'),{recursive:true});
const hash=data=>crypto.createHash('sha256').update(data).digest('hex');
const server=createReferenceServer();server.listen(0,'127.0.0.1');await once(server,'listening');
const origin=`http://127.0.0.1:${server.address().port}`;
const base=origin+'/research-2026-10-22/reference/';
const report={formatVersion:1,classification:'D',identity:'Fieldnotes independent reference; no current Hark application executed',startedAt:new Date().toISOString(),method:'Isolated Playwright Chromium process; local loopback server only; sequential DOM measurement and unaltered viewport PNG',sourceOriginalExecuted:false,sourceSnapshotEpoch:'H15 baseline plus current App B font/CSS declarations; independent D runtime only',previousRun:'runs/initial/reference-run.json',runId:'current-typography-layout-update',targetCommit:execFileSync('git',['rev-parse','HEAD'],{cwd:referenceRoot,encoding:'utf8'}).trim(),targetState:'working tree, implementationFiles hashes define tested bytes',environment:{node:process.version,platform:process.platform,arch:process.arch},checks:[],captures:[],errors:[],externalRequests:[],implementationFiles:[]};
for(const name of ['index.html','reference.css','reference.mjs','serve.mjs','verify.mjs','assets/corner-path.mjs','assets/corner-path-provenance.json','assets-reuse.json']){
 const bytes=fs.readFileSync(path.join(referenceRoot,name));report.implementationFiles.push({path:'../reference/'+name,sha256:hash(bytes),bytes:bytes.length});
}
let browser;
const record=(name,details={})=>report.checks.push({name,result:'passed',...details});
try{
 browser=await chromium.launch({headless:true,...(process.env.CHROME_EXECUTABLE?{executablePath:process.env.CHROME_EXECUTABLE}:{}),args:['--disable-background-networking','--disable-component-update','--disable-sync','--no-first-run']});
 report.environment.browser=browser.version();
 const context=await browser.newContext({viewport:{width:1440,height:900},deviceScaleFactor:1,reducedMotion:'no-preference',colorScheme:'dark',locale:'en-US'});
 await context.route('**/*',async route=>{
   if(new URL(route.request().url()).origin!==origin){report.externalRequests.push(route.request().url());await route.abort();return;}
   await route.continue();
 });
 const page=await context.newPage();page.setDefaultTimeout(8000);
 page.on('pageerror',error=>report.errors.push(error.message));
 page.on('console',message=>{if(message.type()==='error')report.errors.push(message.text());});
 page.on('response',response=>{if(response.status()>=400)report.errors.push(`${response.status()} ${response.url()}`);});
 await page.goto(base);await page.waitForFunction(()=>window.__referenceReady===true);await page.evaluate(()=>document.fonts.ready);
 const metrics=()=>page.evaluate(()=>{
   const get=selector=>{
     const e=document.querySelector(selector);if(!e)return null;
     const r=e.getBoundingClientRect(),s=getComputedStyle(e);
     return {rect:{x:r.x,y:r.y,width:r.width,height:r.height},fontFamily:s.fontFamily,fontSize:s.fontSize,fontWeight:s.fontWeight,lineHeight:s.lineHeight,color:s.color,background:s.backgroundColor,padding:s.padding,backdropFilter:s.backdropFilter,clipPath:s.clipPath,overflowY:s.overflowY};
   };
   return {viewport:{width:innerWidth,height:innerHeight,dpr:devicePixelRatio},theme:document.documentElement.dataset.theme,themeMode:document.documentElement.dataset.themeMode,route:location.hash||'#notes',reducedMotion:matchMedia('(prefers-reduced-motion:reduce)').matches,scroll:{notes:document.querySelector('#notes-view').scrollTop,reader:document.querySelector('.reader-sheet').scrollTop},draft:document.querySelector('#draft').value,composerState:document.querySelector('#composer').dataset.state,readerOpen:document.querySelector('#reader').open,activeElement:{id:document.activeElement.id,tag:document.activeElement.tagName},fontFaces:[...document.fonts].map(f=>({family:f.family,weight:f.weight,status:f.status})),elements:Object.fromEntries(['#composer','#draft','#composer>.surface-fill','.bubble.assistant','.bubble.assistant>.surface-fill','.summary-card','.card-heading strong','#reader-title','.reader-sheet','.reader-sheet h2','.reader-sheet p:not(.eyebrow)','.reader-sheet strong','#code-body','nav','.collection-content'].map(s=>[s,get(s)])),horizontalOverflow:document.documentElement.scrollWidth>innerWidth};
 });
 async function capture(name,description){
   await page.evaluate(()=>document.fonts.ready);await page.waitForTimeout(250);
   const startedAt=new Date().toISOString(),measurement=await metrics();
   assert.equal(measurement.horizontalOverflow,false,`overflow ${name}`);
   const file=path.join(out,'captures',name+'.png');await page.screenshot({path:file,fullPage:false,animations:'allow'});
   const bytes=fs.readFileSync(file);
   report.captures.push({id:name,path:'captures/'+name+'.png',classification:'D',description,startedAt,completedAt:new Date().toISOString(),sha256:hash(bytes),bytes:bytes.length,measurement,pixelReview:{status:'pending'}});
 }
 let m=await metrics();
 assert.equal(await page.locator('#send').isDisabled(),true);assert.equal(m.elements['#composer'].rect.width,512);assert.equal(m.elements['#composer'].rect.height,48);assert.equal(900-m.elements['#composer'].rect.y-48,32);assert.equal(m.elements['#draft'].fontSize,'15px');assert.equal(m.elements['#draft'].lineHeight,'19px');
 assert.equal(m.elements['#composer>.surface-fill'].backdropFilter,'blur(50px)');assert.equal(m.elements['.bubble.assistant>.surface-fill'].backdropFilter,'none');assert.ok(m.elements['#composer>.surface-fill'].clipPath.startsWith('path('));
 assert.equal(m.elements['.card-heading strong'].fontSize,'20px');assert.equal(m.elements['.card-heading strong'].fontWeight,'500');assert.equal(m.elements['.card-heading strong'].lineHeight,'26px');assert.match(m.elements['.card-heading strong'].fontFamily,/Hark Serif/);
 record('Desktop composer geometry and source path',{width:512,height:48,bottom:32,font:'15/19',glassBlur:50,ordinaryBubbleBlur:0,summaryPreviewTitle:'Hark Serif20/26/500'});
 await capture('desktop-dark','Default dark notes, composer and summary/code rich cards; fixed D backdrop');
 await page.locator('#draft').fill('One\nTwo\nThree');
 assert.equal((await metrics()).elements['#draft'].rect.height,86);
 await page.locator('#draft').fill(Array(25).fill('A small observation').join('\n'));
 m=await metrics();assert.equal(m.elements['#draft'].rect.height,219);assert.equal(m.elements['#draft'].overflowY,'auto');
 record('Multiline composer grows 48 → 86 and caps at 219 with internal scrolling');
 await page.locator('#draft').fill('Keep this draft while browsing');
 await page.locator('#draft').dispatchEvent('compositionstart');
 await page.locator('#draft').dispatchEvent('keydown',{key:'Enter',code:'Enter',isComposing:true,bubbles:true});
 await page.waitForTimeout(750);assert.equal(await page.locator('.new-entry').count(),0);assert.equal(await page.locator('#draft').inputValue(),'Keep this draft while browsing');
 await page.locator('#draft').dispatchEvent('compositionend');record('Synthetic IME composition suppresses submit; OS IME/soft keyboard not tested');
 await page.locator('nav [data-route="projects"]').click();assert.equal(await page.locator('#draft').inputValue(),'Keep this draft while browsing');
 await page.waitForTimeout(600);
 assert.equal(await page.locator('nav [data-route="projects"]').getAttribute('aria-current'),'page');
 await capture('desktop-projects','D project navigation, 420px collection band, independent target copy');
 await page.goBack();await page.waitForFunction(()=>!document.querySelector('#notes-view').hidden);assert.equal(await page.locator('#draft').inputValue(),'Keep this draft while browsing');
 record('Navigation and browser Back retain draft and original notes DOM');
 await page.locator('#summary-open').click();await page.waitForTimeout(300);assert.equal(await page.locator('#reader').evaluate(e=>e.open),true);
 m=await metrics();assert.equal(m.elements['#reader-title'].fontSize,'40px');assert.equal(m.elements['#reader-title'].fontWeight,'500');assert.equal(m.elements['#reader-title'].lineHeight,'52px');assert.equal(m.elements['.reader-sheet p:not(.eyebrow)'].fontSize,'18px');assert.equal(m.elements['.reader-sheet p:not(.eyebrow)'].lineHeight,'25.2px');
 assert.deepEqual(m.elements['.reader-sheet'].rect,{x:360,y:140,width:720,height:736});assert.equal(m.elements['.reader-sheet h2'].fontWeight,'700');assert.equal(m.elements['.reader-sheet h2'].lineHeight,'22.4px');assert.ok(m.fontFaces.some(f=>f.family==='GT Standard L'&&f.weight==='700'&&f.status==='loaded'));
 await capture('desktop-reader-dark','Independent summary dialog; current source top140 and original L700 h2 adopted as D');
 for(let i=0;i<5;i++)await page.keyboard.press('Tab');
 assert.equal(await page.evaluate(()=>document.querySelector('#reader').contains(document.activeElement)),true);
 const oldScroll=await page.locator('#notes-view').evaluate(e=>e.scrollTop);
 await page.mouse.move(40,400);await page.mouse.wheel(0,500);assert.equal(await page.locator('#notes-view').evaluate(e=>e.scrollTop),oldScroll);
 await page.keyboard.press('Escape');assert.equal(await page.locator('#reader').evaluate(e=>e.open),false);assert.equal(await page.evaluate(()=>document.activeElement.id),'summary-open');
 record('Summary independent dialog, top140, 40/52/500 title, original L700 h2, 18/25.2 body, native modal focus containment, background scroll lock and Escape focus return');
 await page.locator('#settings-open').click();await page.locator('[name="theme"][value="light"]').check();await page.locator('[data-close="settings"]').click();await page.waitForTimeout(2000);
 m=await metrics();assert.equal(m.theme,'light');assert.equal(m.elements['#draft'].color,'rgb(29, 29, 31)');await capture('desktop-light','Light material cascade with fixed D light backdrop');
 await page.locator('#settings-open').click();await page.locator('[name="theme"][value="auto"]').check();assert.equal((await metrics()).theme,'light');
 await page.locator('[name="theme"][value="system"]').check();await page.emulateMedia({colorScheme:'dark'});assert.equal((await metrics()).theme,'dark');
 await page.emulateMedia({colorScheme:'light'});await page.waitForFunction(()=>document.documentElement.dataset.theme==='light');
 await page.locator('[name="theme"][value="dark"]').check();await page.locator('#fail-next').check();await page.locator('[data-close="settings"]').click();await page.waitForTimeout(850);
 record('Light/dark materials; System follows media changes; Auto explicitly uses D Light fallback');
 await page.locator('#draft').fill('Retry preserves this local observation');await page.locator('#send').click();assert.equal(await page.locator('#composer').getAttribute('data-state'),'pending');
 await page.locator('#composer').evaluate(e=>{e.requestSubmit();e.requestSubmit();});await page.waitForFunction(()=>document.querySelector('#composer').dataset.state==='error');
 assert.equal(await page.locator('#draft').inputValue(),'Retry preserves this local observation');assert.equal(await page.locator('.new-entry').count(),0);await capture('desktop-error','D failed local save preserves draft and offers retry');
 await page.locator('#retry-submit').click();await page.waitForFunction(()=>document.querySelectorAll('.new-entry').length===1);assert.equal(await page.locator('#draft').inputValue(),'');
 record('Pending blocks duplicate submissions; local failure retains draft; Retry commits exactly once');
 await page.locator('#search-open').click();await page.locator('#search-input').fill('Retry preserves');assert.equal(await page.locator('#search-results button').count(),1);await page.locator('#search-results button').click();
 assert.equal(await page.locator('#search').evaluate(e=>e.open),false);assert.equal(await page.evaluate(()=>document.activeElement.id),'local-note-1');
 await page.locator('#search-open').click();await page.locator('#search-input').fill('no-such-neutral-phrase');assert.match(await page.locator('#search-results').innerText(),/No matching notes/);await page.keyboard.press('Escape');
 await page.waitForFunction(()=>!document.querySelector('#search').open);
 await page.waitForTimeout(100);
 record('Search includes newly saved local messages, returns to match, and shows a meaningful empty state');
 await page.locator('#draft').fill('Cancel keeps this text');assert.equal(await page.locator('#draft').inputValue(),'Cancel keeps this text');assert.equal(await page.locator('#send').isEnabled(),true);await page.locator('#send').click();await page.locator('#cancel-submit').click();await page.waitForTimeout(800);assert.equal(await page.locator('.new-entry').count(),1);assert.equal(await page.locator('#draft').inputValue(),'Cancel keeps this text');
 record('Cancel invalidates pending save and keeps draft without late insertion');
 await page.locator('#reply-schedule').click();assert.equal(await page.locator('#reply-context').isVisible(),true);await page.locator('#draft').press('Escape');assert.equal(await page.locator('#reply-context').isVisible(),false);assert.equal(await page.locator('#draft').inputValue(),'Cancel keeps this text');
 await page.locator('#draft').dispatchEvent('input');await page.locator('#code-toggle').click();assert.equal(await page.locator('#code-toggle').getAttribute('aria-expanded'),'true');assert.equal(await page.locator('#code-body').evaluate(e=>e.scrollHeight<=e.clientHeight+1),true);await capture('desktop-code-expanded','Expanded local code card, original Mono400; D independent reply context and disclosure');await page.locator('#code-toggle').click();
 record('Code overflow disclosure and reply context; Escape clears reply while retaining text');
 await page.setViewportSize({width:390,height:844});await page.goto(base);await page.waitForFunction(()=>window.__referenceReady===true);await page.evaluate(()=>document.fonts.ready);await page.waitForTimeout(300);
 m=await metrics();assert.equal(m.elements['#composer'].rect.width,358);assert.equal(m.elements['#composer'].rect.x,16);assert.equal(m.elements['#draft'].fontSize,'15px');assert.equal(m.elements['#draft'].lineHeight,'19px');await capture('mobile-dark','390×844 CSS viewport, 16px composer insets, dark materials; desktop emulation only');
 await page.locator('#summary-open').click();await page.waitForTimeout(300);m=await metrics();assert.deepEqual(m.elements['.reader-sheet'].rect,{x:0,y:62,width:390,height:782});assert.equal(m.elements['#reader-title'].fontSize,'32px');assert.equal(m.elements['#reader-title'].lineHeight,'41.6px');assert.equal(m.elements['.reader-sheet h2'].fontWeight,'700');await capture('mobile-reader-dark','390×844 independent reading sheet, current CSS top62/full-width and32/41.6 title adopted as D');await page.keyboard.press('Escape');
 record('Narrow viewport: 358px composer, x16 insets, 15/19 body; full-width390px reader top62/bottom0; no OS keyboard/touch claim');
 await page.emulateMedia({reducedMotion:'reduce'});await page.locator('#settings-open').click();await page.locator('[name="theme"][value="light"]').check();
 assert.equal(await page.locator('.backdrop').evaluate(e=>getComputedStyle(e).transitionDuration),'0s');assert.equal(await page.locator('#settings').evaluate(e=>getComputedStyle(e).animationDuration),'0s');await page.locator('[data-close="settings"]').click();
 record('Reduced motion sets theme transitions and dialog animation to zero');
 assert.deepEqual(report.errors,[]);assert.deepEqual(report.externalRequests,[]);record('No page/console/HTTP errors and no external app requests');
 report.result='passed';
}catch(error){report.result='failed';report.failure=error.stack;throw error;}
finally{
 report.completedAt=new Date().toISOString();fs.writeFileSync(path.join(out,'reference-run.json'),JSON.stringify(report,null,2)+'\n');
 await browser?.close();await new Promise(resolve=>server.close(resolve));
 console.log(JSON.stringify({result:report.result,checks:report.checks.length,captures:report.captures.length,errors:report.errors,output:path.join(out,'reference-run.json')},null,2));
}
