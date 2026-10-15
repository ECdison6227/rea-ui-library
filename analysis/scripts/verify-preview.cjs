const {chromium}=require('../tools/node_modules/playwright');
const fs=require('fs');
const assert=require('assert/strict');
const tokenNames=[...new Set([...fs.readFileSync('analysis/formatted/app.css','utf8').matchAll(/(--[\w-]+)\s*:/g)].map(x=>x[1]))].filter(x=>/^--(?:th-|surface-|brand-|m-|font-|dur-|ease-|edge-|elev-|layer-|radius-)/.test(x));
(async()=>{
  const browser=await chromium.launch({executablePath:'/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',headless:true});
  const results=[];
  for(let run=1;run<=3;run++){
    const context=await browser.newContext({viewport:{width:393,height:852},locale:'zh-CN'});
    const cases=[['login','dark','连接到你的电脑'],['pair','dark','连接'],['home','dark','新建会话'],['settings','dark','后台保持连接'],['settings','light','设置'],['notifications','dark','解除电池优化'],['update','dark','自动检查更新']];
    for(const [view,theme,expected] of cases){
      const page=await context.newPage();const errors=[];const external=[];
      page.on('pageerror',error=>errors.push(error.message));
      page.on('request',request=>{if(new URL(request.url()).hostname!=='127.0.0.1')external.push(request.url());});
      await page.goto(`http://127.0.0.1:4174/app/?view=${view}&theme=${theme}&platform=android`,{waitUntil:'networkidle'});
      await page.waitForFunction(({text,theme})=>document.body.innerText.includes(text)&&document.documentElement.dataset.theme===theme,{text:expected,theme});
      const state=await page.evaluate(()=>({text:document.body.innerText,theme:document.documentElement.dataset.theme,overflow:document.documentElement.scrollWidth>innerWidth,calls:window.__previewCalls}));
      assert.equal(errors.length,0,errors.join('\n'));assert.equal(state.overflow,false);assert.equal(external.length,0);
      if(view==='settings'&&run===1){
        const tokens=await page.evaluate(names=>Object.fromEntries(names.map(name=>[name,getComputedStyle(document.documentElement).getPropertyValue(name).trim()]).filter(([,value])=>value)),tokenNames);
        fs.writeFileSync(`analysis/reference/design-tokens-${theme}.json`,JSON.stringify(tokens,null,2)+'\n');
      }
      if(run===1)fs.writeFileSync(`analysis/preview/${view}-${theme}-dom.json`,JSON.stringify(state,null,2)+'\n');
      results.push({run,view,theme,passed:true,pageErrors:errors,externalRequests:external,horizontalOverflow:state.overflow});
      await page.close();
    }
    await context.close();
    console.log(`Run ${run}: ${cases.length}/${cases.length} passed`);
  }
  fs.writeFileSync('analysis/evidence/preview-verification.json',JSON.stringify(results,null,2)+'\n');
  await browser.close();
})().catch(error=>{console.error(error);process.exit(1)});
