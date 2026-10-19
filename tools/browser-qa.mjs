import fs from 'node:fs';
import path from 'node:path';
import {spawn} from 'node:child_process';
import {once} from 'node:events';
import {setTimeout as delay} from 'node:timers/promises';
import assert from 'node:assert/strict';
import {ROOT,definitions} from './catalog.mjs';
import {prepare} from './prepare.mjs';
import {createCatalogServer} from './serve.mjs';
prepare();
const studies=definitions().sources,total=studies.length,mobileCount=studies.filter(s=>s.platform==='mobile').length,desktopCount=studies.filter(s=>s.platform==='desktop').length;
const chrome=process.env.REA_QA_CHROME||'C:/Program Files/Google/Chrome/Application/chrome.exe';
if(!fs.existsSync(chrome))throw Error('Set REA_QA_CHROME to an existing Chromium executable; no browser installation is performed.');
const stamp=Date.now(),profile=path.join(ROOT,'.runtime','browser-'+stamp),out=path.join(ROOT,'.runtime','browser-qa-'+stamp);fs.mkdirSync(profile,{recursive:true});fs.mkdirSync(out,{recursive:true});
const server=createCatalogServer();server.listen(0,'127.0.0.1');await once(server,'listening');const base='http://127.0.0.1:'+server.address().port;
const child=spawn(chrome,['--headless=new','--no-first-run','--no-default-browser-check','--disable-background-networking','--disable-component-update','--disable-sync','--user-data-dir='+profile,'--remote-debugging-port=0','about:blank'],{windowsHide:true,stdio:'ignore'});
let ws,seq=0;const waiting=new Map();
function cdp(method,params={},sessionId){return new Promise((resolve,reject)=>{const id=++seq;const timer=setTimeout(()=>{waiting.delete(id);reject(Error('CDP timed out: '+method));},15000);waiting.set(id,{resolve,reject,timer});ws.send(JSON.stringify({id,method,params,...(sessionId?{sessionId}:{})}));});}
let session;
const evaluate=async expression=>(await cdp('Runtime.evaluate',{expression,returnByValue:true,awaitPromise:true},session)).result.value;
async function until(expression){for(let i=0;i<100;i++){if(await evaluate(expression))return;await delay(100);}throw Error('Browser condition failed: '+expression);}
const steps=[];
try{
 const portFile=path.join(profile,'DevToolsActivePort');for(let i=0;i<150&&!fs.existsSync(portFile);i++)await delay(100);if(!fs.existsSync(portFile))throw Error('Isolated headless browser did not start');
 const [port,endpoint]=fs.readFileSync(portFile,'utf8').trim().split(/\r?\n/);ws=new WebSocket('ws://127.0.0.1:'+port+endpoint);await once(ws,'open');
 ws.addEventListener('message',event=>{const m=JSON.parse(event.data);if(m.id&&waiting.has(m.id)){const p=waiting.get(m.id);clearTimeout(p.timer);waiting.delete(m.id);if(m.error)p.reject(Error(m.error.message));else p.resolve(m.result||{});}});
 const {targetId}=await cdp('Target.createTarget',{url:'about:blank',background:true});session=(await cdp('Target.attachToTarget',{targetId,flatten:true})).sessionId;
 await cdp('Page.enable',{},session);await cdp('Runtime.enable',{},session);await cdp('Emulation.setDeviceMetricsOverride',{width:1440,height:1100,deviceScaleFactor:1,mobile:false},session);await cdp('Page.navigate',{url:base},session);await until("document.querySelectorAll('[data-source-id]').length==="+total);
 assert.equal(await evaluate("document.querySelectorAll('[data-platform=desktop] .nav-count')[0].textContent"),String(desktopCount).padStart(2,'0'));steps.push('All registered REA studies loaded with truthful platform counts');
 await evaluate("document.querySelector('[data-platform=desktop]').click()");await until("document.querySelectorAll('[data-source-id]').length==="+desktopCount);if(!desktopCount){await until("!document.querySelector('#empty').hidden");assert.match(await evaluate("document.querySelector('#empty-title').textContent"),/电脑端/);}steps.push('Desktop category reflects registry');
 await evaluate("document.querySelector('[data-platform=mobile]').click()");await until("document.querySelectorAll('[data-source-id]').length==="+mobileCount);
 await evaluate("document.querySelector('#search').value='草稿';document.querySelector('#search').dispatchEvent(new Event('input',{bubbles:true}))");await until("document.querySelectorAll('[data-source-id]').length===1");assert.equal(await evaluate("document.querySelector('[data-source-id]').dataset.sourceId"),'kimi');steps.push('Mobile and text filters compose correctly');
 await evaluate("document.querySelector('.card-open').click()");await until("document.querySelector('#detail-dialog').open");assert.match(await evaluate("document.querySelector('#detail-content').textContent"),/未测/);await evaluate("[...document.querySelectorAll('#detail-content button')].find(b=>b.textContent.includes('研究入口')).click()");await until("document.querySelector('#document-dialog').open");assert.match(await evaluate("document.querySelector('#document-content').textContent"),/Kimi/);steps.push('Source detail and actual local document open');
 await cdp('Input.dispatchKeyEvent',{type:'keyDown',key:'Escape',code:'Escape',windowsVirtualKeyCode:27},session);await cdp('Input.dispatchKeyEvent',{type:'keyUp',key:'Escape',code:'Escape',windowsVirtualKeyCode:27},session);await until("!document.querySelector('#document-dialog').open");assert.equal(await evaluate("document.querySelector('#detail-dialog').open"),true);await evaluate("document.querySelector('#detail-dialog').close()");steps.push('Escape closes the top document and preserves underlying detail');
 await evaluate("document.querySelector('#search').value='';document.querySelector('#search').dispatchEvent(new Event('input',{bubbles:true}));document.querySelector('[data-platform=all]').click()");await until("document.querySelectorAll('[data-source-id]').length==="+total);
 await evaluate("document.querySelector('[data-source-id=hark] input').click();document.querySelector('[data-source-id=kimi] input').click();document.querySelector('#compare-open').click()");await until("document.querySelector('#compare-dialog').open");assert.match(await evaluate("document.querySelector('#compare-content').textContent"),/网页/);assert.match(await evaluate("document.querySelector('#compare-content').textContent"),/手机端/);await evaluate("document.querySelector('#compare-dialog').close();document.querySelector('#compare-clear').click()");steps.push('Comparison shows distinct source platforms and conditions');
 await evaluate("document.querySelector('#skill-button').click()");await until("document.querySelector('#document-dialog').open");assert.match(await evaluate("document.querySelector('#document-content').textContent"),/技能|Skill|skill/);await evaluate("document.querySelector('#document-dialog').close()");steps.push('Skill review reachable from unified entry');
 assert.equal(await evaluate('document.documentElement.scrollWidth<=innerWidth'),true);await delay(600);let png=await cdp('Page.captureScreenshot',{format:'png',captureBeyondViewport:true},session);fs.writeFileSync(path.join(out,'desktop.png'),Buffer.from(png.data,'base64'));
 await cdp('Emulation.setDeviceMetricsOverride',{width:390,height:844,deviceScaleFactor:1,mobile:true},session);await delay(600);assert.equal(await evaluate('document.documentElement.scrollWidth<=innerWidth'),true);assert.equal(await evaluate("document.querySelectorAll('[data-source-id]').length"),total);png=await cdp('Page.captureScreenshot',{format:'png',captureBeyondViewport:true},session);fs.writeFileSync(path.join(out,'mobile.png'),Buffer.from(png.data,'base64'));steps.push('1440px and 390px layouts have no horizontal overflow');
 const report={status:'passed',scope:'Unified catalog only; no source app or device operation',headless:true,foregroundChanged:false,viewportWidths:[1440,390],steps,screenshots:{desktop:path.relative(ROOT,path.join(out,'desktop.png')).replaceAll(path.sep,'/'),mobile:path.relative(ROOT,path.join(out,'mobile.png')).replaceAll(path.sep,'/')}};
 fs.writeFileSync(path.join(ROOT,'.runtime/browser-qa.json'),JSON.stringify(report,null,2)+'\n');console.log(JSON.stringify(report,null,2));
}finally{
 if(ws?.readyState===WebSocket.OPEN){try{await cdp('Browser.close');}catch{}ws.close();}
 for(const p of waiting.values())clearTimeout(p.timer);
 child.kill();await new Promise(resolve=>server.close(resolve));
}
