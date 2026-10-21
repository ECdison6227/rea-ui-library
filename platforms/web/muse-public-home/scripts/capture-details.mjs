import fs from 'node:fs/promises';
import path from 'node:path';
import {launch,hash,saveJson,saveCapture} from './browser.mjs';
const baseline=JSON.parse(await fs.readFile('evidence/rea-baseline.bundle.json','utf8')).records[0];
const {browser,context,page}=await launch();
const captures=[],samples=[],assets=[];
try {
  await page.goto(baseline.parameters.start_url.url,{waitUntil:'domcontentloaded',timeout:45000});
  await page.locator('#landing-hero-title').waitFor();
  await page.evaluate(()=>document.fonts.ready);
  await page.waitForTimeout(2000);
  const client=await context.newCDPSession(page);
  await client.send('DOM.enable');await client.send('CSS.enable');
  const {root}=await client.send('DOM.getDocument');
  const fontBindings=[];
  for(const selector of ['#landing-hero-title','input','form button']) {
    const {nodeId}=await client.send('DOM.querySelector',{nodeId:root.nodeId,selector});
    const font=await client.send('CSS.getPlatformFontsForNode',{nodeId});
    fontBindings.push({selector,...font});
  }
  await saveJson('evidence/raw/font-bindings.json',{capturedAt:new Date().toISOString(),fontBindings});
  async function sample(label){
    samples.push({label,capturedAt:new Date().toISOString(),...await page.evaluate(()=>({
      phrase:[...document.querySelectorAll('[data-rebus-phrase-role]')].filter(e=>!e.hidden).map(e=>({text:e.textContent,role:e.dataset.rebusPhraseRole})),
      videos:[...document.querySelectorAll('.landing-rebus-avatar')].map(v=>({class:v.className,currentTime:v.currentTime,paused:v.paused,opacity:getComputedStyle(v).opacity})),
      scroll:[...document.querySelectorAll('*')].filter(el=>el.scrollTop>0).map(el=>({class:el.className,scrollTop:el.scrollTop})),
      animations:document.getAnimations().map(a=>({playState:a.playState,currentTime:a.currentTime,timing:a.effect.getTiming()}))
    }))});
  }
  await sample('rest');
  await page.locator('.landing-rebus-avatar-slot').hover();
  for(const delay of [200,800,1500]) {await page.waitForTimeout(delay);await sample(`hover+${delay}`);}
  captures.push(await saveCapture(page,'evidence/raw/details-avatar-hover.png'));
  await page.mouse.move(100,100);
  await page.waitForTimeout(300);
  await sample('pointer-out');
  await page.getByRole('textbox',{name:'Mobile number or email'}).focus();
  await page.getByRole('textbox',{name:'Mobile number or email'}).press('Tab');
  captures.push(await saveCapture(page,'evidence/raw/details-keyboard-focus.png'));
  await page.locator('.landing-rebus-avatar-slot').hover();
  await page.mouse.down();await page.waitForTimeout(700);await sample('hold');await page.mouse.up();
  const learn=page.getByRole('link',{name:'Learn more',exact:true}).last();
  await learn.click();
  for(const delay of [100,500,1200]) {await page.waitForTimeout(delay);await sample(`learn-more+${delay}`);}
  captures.push(await saveCapture(page,'evidence/raw/details-learn-more.png'));
  await page.locator('[class~="group/scroller"]').evaluate(el=>el.scrollTo({top:1000,behavior:'instant'}));
  await page.waitForTimeout(900);
  const intro=page.getByLabel('Muse introduction film');
  const introStartedAt=new Date().toISOString();
  const frame=await intro.screenshot({path:'assets/source/introduction-still.png'});
  assets.push({path:'assets/source/introduction-still.png',kind:'derived-video-frame',bytes:frame.length,sha256:hash(frame),captureInterval:{startedAt:introStartedAt,completedAt:new Date().toISOString()},source:'Video element screenshot at scroller position 1000; 1440×1000 CSS px viewport; no resizing or repainting.'});
  const avatarUrls=await page.locator('.landing-rebus-avatar').evaluateAll(els=>els.map(el=>el.src));
  for(const url of avatarUrls) {
    const response=await context.request.get(url,{timeout:25000});
    if(!response.ok()) throw new Error(`Cannot preserve avatar video ${response.status()}`);
    const bytes=await response.body();
    const target='assets/source/'+new URL(url).pathname.slice(1);
    await fs.mkdir(path.dirname(target),{recursive:true});await fs.writeFile(target,bytes);
    assets.push({path:target,url,kind:'video',bytes:bytes.length,sha256:hash(bytes),capturedAt:new Date().toISOString()});
  }
  await saveJson('evidence/raw/detail-observations.json',{capturedAt:new Date().toISOString(),browser:browser.version(),samples,captures,assets,limits:['No authentication or navigation to another page.','Pointer actions dispatched to the live page; sampled video times and scroll positions are the observed results.','No claim of whole-film or assistive-technology validation.']});
  console.log(JSON.stringify({complete:true,samples: samples.map(s=>({label:s.label,phrase:s.phrase,videos:s.videos,scroll:s.scroll})),assets}));
}finally{await browser.close();}
