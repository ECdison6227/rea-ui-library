const {chromium}=require('../tools/node_modules/playwright');
const fs=require('fs');
(async()=>{
  const browser=await chromium.launch({executablePath:'/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',headless:true});
  const context=await browser.newContext({viewport:{width:393,height:852},deviceScaleFactor:2,colorScheme:'dark',locale:'zh-CN',isMobile:true,hasTouch:true});
  await context.route('**/*',route=>new URL(route.request().url()).hostname==='127.0.0.1'?route.continue():route.abort());
  const page=await context.newPage();
  const log=[];
  page.on('pageerror',e=>log.push({type:'error',message:e.message}));
  page.on('console',e=>{if(e.type()==='error') log.push({type:'console',message:e.text()});});
  await page.goto('http://127.0.0.1:4173',{waitUntil:'networkidle'});
  await page.screenshot({path:'analysis/preview/01-start-dark.png',fullPage:true});
  fs.writeFileSync('analysis/preview/01-start-dom.txt',await page.locator('body').innerText());
  fs.writeFileSync('analysis/preview/01-start.html',await page.content());
  fs.writeFileSync('analysis/preview/browser-log.json',JSON.stringify(log,null,2));
  console.log(await page.locator('body').innerText());
  console.log(await page.locator('button').evaluateAll(xs=>xs.map(x=>({text:x.innerText,label:x.getAttribute('aria-label')}))));
  await browser.close();
})();
