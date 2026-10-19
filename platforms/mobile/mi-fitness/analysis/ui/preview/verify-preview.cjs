// Browser checks of the reconstructed study page, never of the APK.
const fs=require('node:fs'),path=require('node:path'),assert=require('node:assert/strict');
const {chromium}=require(path.join(process.env.APPDATA,'npm/node_modules/rea-agents/node_modules/playwright-core'));
(async()=>{
 const out=path.join(__dirname,'verification');fs.mkdirSync(out,{recursive:true});
 const browser=await chromium.launch({headless:true,executablePath:path.join(process.env.USERPROFILE,'.agent-browser/browsers/chrome-155.0.8059.39/chrome.exe')});
 const page=await browser.newPage({viewport:{width:1512,height:1040},deviceScaleFactor:1});
 const errors=[],external=[];page.on('pageerror',e=>errors.push(e.message));page.on('request',r=>{if(!r.url().startsWith('http://127.0.0.1:8766/')&&!r.url().startsWith('data:'))external.push(r.url());});
 try{
  await page.goto('http://127.0.0.1:8766/preview/',{waitUntil:'domcontentloaded'});
  await page.waitForSelector('.token-card');
  assert.equal(await page.locator('#learning-phone').getByRole('heading',{name:'学习',exact:true}).count(),1);
  assert.equal(await page.locator('.token-card').count(),21);
  await page.locator('#templates').scrollIntoViewIfNeeded();
  await page.screenshot({path:path.join(out,'reconstructed-examples-day.png'),fullPage:false});
  await page.selectOption('#account-state','guest');
  await page.locator('#learning-phone [data-act="private"]').first().click();
  await page.getByRole('button',{name:'继续登录',exact:true}).click();
  await page.getByRole('button',{name:'模拟成功',exact:true}).click();
  assert.equal(await page.locator('#account-state').inputValue(),'member');
  assert.equal(await page.locator('#study-dialog h3').innerText(),'视觉设计基础');
  await page.getByRole('button',{name:'返回模板',exact:true}).click();
  await page.selectOption('#load-result','failure');await page.click('#refresh-templates');
  await page.waitForFunction(()=>document.querySelector('#content-state').value==='error');
  assert.match(await page.locator('#learning-phone').innerText(),/已保留上次数据/);
  await page.selectOption('#content-state','empty');
  await page.locator('#learning-phone [data-act="create"]').click();
  assert.equal(await page.locator('#content-state').inputValue(),'ready');
  await page.selectOption('#theme','night');
  assert.equal(await page.locator('html').getAttribute('data-mi-theme'),'night');
  await page.screenshot({path:path.join(out,'reconstructed-examples-night.png'),fullPage:false});
  await page.selectOption('#token-system','native');assert.equal(await page.locator('.token-card').count(),19);
  await page.locator('#row-disabled').check();await page.locator('#sample-row').click({force:true});assert.match(await page.locator('#toast').innerText(),/操作未执行/);
  await page.locator('#row-allow-click').check();await page.locator('#sample-row').click({force:true});assert.match(await page.locator('#toast').innerText(),/请联系管理员/);
  await page.locator('#reduce-motion').check();await page.locator('#reveal-demo').click();await page.waitForFunction(()=>document.querySelector('#reveal-slot').offsetHeight>0);
  await page.setViewportSize({width:390,height:844});await page.locator('#learning-phone').evaluate(e=>e.scrollIntoView({block:'start',behavior:'instant'}));
  assert.equal(await page.evaluate(()=>document.documentElement.scrollWidth<=innerWidth),true);
  await page.screenshot({path:path.join(out,'reconstructed-example-mobile.png'),fullPage:false});
  if(fs.existsSync(path.join(__dirname,'../runtime/screens.json'))){
   await page.click('#reload-screens');await page.waitForSelector('#screens-gallery .screen-record');
   await page.locator('#screenshots').evaluate(e=>e.scrollIntoView({block:'start',behavior:'instant'}));
   await page.waitForFunction(()=>Array.from(document.querySelectorAll('#screens-gallery img')).some(i=>i.naturalWidth>0));
   assert.match(await page.locator('#screen-status').innerText(),/已载入/);
  }
  const filePage=await browser.newPage();await filePage.goto('file:///'+path.join(__dirname,'index.html').replaceAll('\\','/'));await filePage.waitForSelector('.token-card');assert.equal(await filePage.locator('.phone').count(),2);await filePage.close();
  assert.deepEqual(errors,[]);assert.deepEqual(external,[]);
  const result={passed:true,date:new Date().toISOString(),checks:['offline local snapshot loads','RN 21 colors / native 19 colors','explicit simulated login restores guarded action','refresh error retains existing content','empty state can create local content','day/night switch','disabled explanatory click behavior','reduced-motion reveal','390px viewport has no document overflow','runtime screenshot gallery loads existing evidence images','file:// fallback renders templates','no external network request','no page JavaScript error'],screenshots:'These files show the reconstructed study preview, not the original APK.',errors,externalRequests:external};
  fs.writeFileSync(path.join(out,'result.json'),JSON.stringify(result,null,2)+'\n');console.log(JSON.stringify(result));
 }finally{await browser.close();}
})().catch(e=>{console.error(e.stack);process.exitCode=1;});
