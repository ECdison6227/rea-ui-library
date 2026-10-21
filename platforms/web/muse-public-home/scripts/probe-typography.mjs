import fs from 'node:fs/promises';
import {launch,saveJson,saveCapture} from './browser.mjs';
const {browser,context,page}=await launch({viewport:{width:390,height:844}});
const baseline=JSON.parse(await fs.readFile('evidence/rea-baseline.bundle.json','utf8')).records[0];
const results=[];
const settled=process.argv.includes('--settled');
const prefix=settled?'settled-':'';
try{
  for(const target of ['source','reference']){
    await page.goto(target==='source'?baseline.parameters.start_url.url:'http://127.0.0.1:4173/reference/',{waitUntil:'domcontentloaded',timeout:45000});
    await page.locator('input').waitFor();await page.evaluate(()=>document.fonts.ready);await page.waitForTimeout(1000);
    if(settled&&target==='source'){
      await page.waitForFunction(()=>{const button=[...document.querySelectorAll('button')].find(b=>b.textContent.trim()==='Continue');return button&&!button.disabled;},{},{timeout:20000});
      await page.evaluate(()=>document.fonts.ready);await page.waitForTimeout(500);
    }
    const selectors=target==='source'?['#landing-hero-title','input','form p','form button','a[href="#landing-introduction-video-title"]']:['.hero-title','input','form p','form button','.learn-cue'];
    const client=await context.newCDPSession(page);await client.send('DOM.enable');await client.send('CSS.enable');const {root}=await client.send('DOM.getDocument');
    const records=[];
    for(const selector of selectors){
      const {nodeId}=await client.send('DOM.querySelector',{nodeId:root.nodeId,selector});
      records.push({selector,fonts:await client.send('CSS.getPlatformFontsForNode',{nodeId}),computed:await page.locator(selector).evaluate(el=>{
        const s=getComputedStyle(el),r=el.getBoundingClientRect();
        return {rect:{x:r.x,y:r.y,width:r.width,height:r.height},style:Object.fromEntries(['fontFamily','fontSize','fontWeight','lineHeight','letterSpacing','fontOpticalSizing','fontVariationSettings','fontFeatureSettings','textRendering','webkitFontSmoothing','color','textWrapStyle','backgroundColor','backdropFilter','boxShadow','outlineColor','outlineStyle','outlineWidth'].map(k=>[k,s[k]]))};
      })});
    }
    const capture=await saveCapture(page,`comparison/${prefix}probe-${target}-mobile.png`,{subject:target});
    await page.locator('input').focus();
    const focused=await page.locator('input').evaluate(el=>{const s=getComputedStyle(el);return {outlineColor:s.outlineColor,outlineWidth:s.outlineWidth,outlineOffset:s.outlineOffset};});
    results.push({target,capturedAt:new Date().toISOString(),records,capture,focused});
    console.log(JSON.stringify({target,focused,records}));
    await client.detach();
  }
  await saveJson(`evidence/raw/${prefix}typography-probe.json`,results);
}finally{await browser.close();}
