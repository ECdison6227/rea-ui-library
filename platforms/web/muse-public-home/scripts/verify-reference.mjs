import fs from 'node:fs/promises';
import {launch,saveJson,saveCapture,hash} from './browser.mjs';
const {browser,context,page}=await launch();
const errors=[],requests=[],captures=[],checks=[];
const base='http://127.0.0.1:4173/reference/';
const runId=new Date().toISOString().replace(/[:.]/g,'-');
const runDirectory=`comparison/runs/${runId}`;
page.on('pageerror',e=>errors.push(e.message));
page.on('requestfailed',request=>errors.push(request.url()+': '+request.failure()?.errorText));
page.on('response',response=>{if(response.status()>=400)errors.push(`${response.status()} ${response.url()}`);});
page.on('request',request=>requests.push({url:request.url(),method:request.method()}));
function check(name,condition,details){checks.push({name,passed:Boolean(condition),details});}
async function checkGeometry(label,sourcePath,reference){
  const original=JSON.parse(await fs.readFile(sourcePath,'utf8'));
  const predicates={'.hero-title':n=>n.id==='landing-hero-title','.text-field':n=>n.tag==='input','.entry-submit':n=>n.tag==='button'&&n.text==='Continue'};
  const pairs=Object.entries(predicates).map(([selector,predicate])=>{
    const source=original.all.find(predicate),target=reference.entries.find(n=>n.selector===selector);
    return {selector,original:source.rect,reference:target.rect,deltas:Object.fromEntries(['x','y','width','height'].map(key=>[key,target.rect[key]-source.rect[key]]))};
  });
  await saveJson(`comparison/geometry-${label.toLowerCase()}.json`,pairs);
  check(`${label} core geometry within 1 CSS px`,pairs.every(pair=>Object.values(pair.deltas).every(n=>Math.abs(n)<=1)),pairs);
}
async function capture(name,subject='reference'){
  const capture=await saveCapture(page,`${runDirectory}/${name}.png`,{subject,url:page.url()});
  captures.push(capture);
  await fs.copyFile(capture.path,`comparison/${name}.png`);
}
async function measure(label){
  const value=await page.evaluate(()=>{
    const selectors=['.brand-mark','.hero-title','.entry-panel','.entry-title','.text-field','.entry-submit','.learn-cue','.header-action','.intro-section','.feature-row','.walkthrough-section','.reading-section','.site-footer'];
    return {capturedAt:new Date().toISOString(),viewport:{width:innerWidth,height:innerHeight,dpr:devicePixelRatio},pageHeight:document.querySelector('.page-scroll').scrollHeight,entries:selectors.map(selector=>{
      const el=document.querySelector(selector);if(!el)return {selector,absent:true};
      const r=el.getBoundingClientRect(),s=getComputedStyle(el);
      return {selector,rect:{x:r.x,y:r.y,width:r.width,height:r.height},style:{fontFamily:s.fontFamily,fontSize:s.fontSize,fontWeight:s.fontWeight,lineHeight:s.lineHeight,letterSpacing:s.letterSpacing,color:s.color,backgroundColor:s.backgroundColor,borderRadius:s.borderRadius}};
    })};
  });
  await saveJson(`comparison/${label}-measurements.json`,value);return value;
}
try{
  await page.goto(base,{waitUntil:'networkidle'});
  await page.evaluate(()=>document.fonts.ready);
  await capture('reference-desktop');
  const desktop=await measure('reference-desktop');
  check('Desktop fits the viewport',await page.evaluate(()=>document.documentElement.scrollWidth===1440));
  const source=JSON.parse(await fs.readFile('evidence/raw/desktop-measurements.json','utf8'));
  const mapping={'.hero-title':n=>n.id==='landing-hero-title','.text-field':n=>n.tag==='input','.entry-submit':n=>n.tag==='button'&&n.text==='Continue'};
  const pairs=Object.entries(mapping).map(([selector,predicate])=>{
    const original=source.all.find(predicate),reference=desktop.entries.find(e=>e.selector===selector);
    const deltas=Object.fromEntries(['x','y','width','height'].map(k=>[k,reference.rect[k]-original.rect[k]]));
    return {selector,original:original.rect,reference:reference.rect,deltas};
  });
  await saveJson('comparison/geometry-desktop.json',pairs);
  check('Desktop core geometry within 1 CSS px',pairs.every(p=>Object.values(p.deltas).every(d=>Math.abs(d)<=1)),pairs);
  await page.getByRole('button',{name:'Continue',exact:true}).hover();
  await capture('reference-button-hover');
  await page.getByRole('textbox',{name:'Mobile number or email'}).fill('Design study');
  await capture('reference-input-focus');
  check('Focused field matches source blue outline',await page.locator('.text-field').evaluate(el=>getComputedStyle(el).outlineColor==='rgb(0, 100, 212)'));
  const beforePost=requests.filter(r=>r.method==='POST').length;
  await page.getByRole('button',{name:'Continue',exact:true}).click();
  await page.waitForTimeout(900);
  check('Continue stays on the page and sends no POST',page.url()===base && requests.filter(r=>r.method==='POST').length===beforePost);
  check('Introduction receives keyboard focus',await page.locator('#introduction').evaluate(el=>el===document.activeElement));
  check('Draft input survives in-page navigation',await page.locator('.text-field').inputValue()==='Design study');
  for(const y of [1000,2100,6480,7330]){
    await page.locator('.page-scroll').evaluate((el,y)=>el.scrollTop=y,y);
    await page.waitForTimeout(500);
    await capture(`reference-scroll-${y}`);
  }
  check('Landing images decode successfully',await page.locator('img').evaluateAll(imgs=>imgs.filter(i=>i.complete).every(i=>i.naturalWidth>0)));
  await page.locator('.page-scroll').evaluate(el=>el.scrollTop=0);
  await page.locator('.text-field').fill('');
  await page.locator('.text-field').blur();
  await page.mouse.move(1,999);
  await page.setViewportSize({width:390,height:844});
  await page.reload({waitUntil:'networkidle'});
  await capture('reference-mobile');
  const mobile=await measure('reference-mobile');
  await checkGeometry('Mobile','evidence/raw/scroll-mobile-measurements.json',mobile);
  check('Mobile fits the viewport',await page.evaluate(()=>document.documentElement.scrollWidth===390));
  check('Mobile input remains at least 16 CSS px',mobile.entries.find(e=>e.selector==='.text-field').style.fontSize==='16px');
  await page.setViewportSize({width:768,height:1024});
  await page.reload({waitUntil:'networkidle'});await capture('reference-tablet');
  const tablet=await measure('reference-tablet');
  await checkGeometry('Tablet','evidence/raw/scroll-tablet-measurements.json',tablet);
  await page.locator('.avatar-slot').hover();
  await page.waitForTimeout(4000);
  check('Headline cycles through the original phrase slots',!(await page.locator('.phrase').innerText()).includes('gets it done for you'));
  await capture('reference-headline-motion');
  await page.emulateMedia({reducedMotion:'reduce'});
  await page.setViewportSize({width:1440,height:1000});
  await page.locator('.avatar-slot').hover();
  await page.waitForTimeout(250);
  check('Reduced motion keeps avatar videos paused',await page.locator('.avatar').evaluateAll(els=>els.every(v=>v.paused)));
  await page.reload({waitUntil:'networkidle'});
  await page.keyboard.press('Tab');
  await page.keyboard.press('Tab');
  await capture('reference-keyboard-focus');
  check('Keyboard reaches a visible focusable control',await page.evaluate(()=>document.activeElement!==document.body));
  await page.goto(base+'?example=reading',{waitUntil:'networkidle'});
  await capture('transfer-reading','transfer');
  await page.getByRole('textbox',{name:'Search your reading list'}).fill('creative');
  await page.getByRole('button',{name:'Find a story'}).click();
  check('Reading adaptation filters real content',await page.locator('[data-feature]').count()===1 && await page.locator('.result-count').innerText()==='1 story in your collection');
  await capture('transfer-reading-filtered','transfer');
  await page.getByRole('button',{name:'My library'}).click();
  await page.getByRole('textbox',{name:'Search your reading list'}).fill('no-matching-story');
  await page.getByRole('button',{name:'Find a story'}).click();
  check('Reading adaptation provides an empty state',await page.locator('[data-feature]').count()===0 && (await page.locator('.result-count').innerText()).startsWith('No stories found'));
  await capture('transfer-reading-empty','transfer');
  check('Reference assets and code load successfully',errors.length===0,errors);
  check('Visible images decode successfully',await page.locator('img').evaluateAll(imgs=>imgs.filter(i=>i.complete).every(i=>i.naturalWidth>0)));
  check('Reference has no automatic external requests',requests.every(r=>new URL(r.url).hostname==='127.0.0.1'));
  const implementationFiles={};
  for(const file of ['reference/index.html','reference/styles.css','reference/components.js','reference/app.js','reference/source-content.js'])implementationFiles[file]=hash(await fs.readFile(file));
  const result={capturedAt:new Date().toISOString(),runId,browser:browser.version(),implementationFiles,checks,captures,errors,requests:requests.map(r=>({url:r.url,method:r.method})),limits:['Checks local reference behavior, not Muse authentication or service behavior.','Media posters use a retained frame; the full promotional films are outside this implementation.','No browser screen-reader or real touch device was tested.']};
  await saveJson(`${runDirectory}/qa-results.json`,result);
  await saveJson('comparison/qa-results.json',result);
  console.log(JSON.stringify({passed:checks.every(c=>c.passed),checks,errors,captures:captures.length}));
  if(checks.some(c=>!c.passed))process.exitCode=1;
}finally{await browser.close();}
