/** Capture the built catalog, never source applications. Uses an isolated local Chromium profile. */
import fs from 'node:fs';
import path from 'node:path';
import http from 'node:http';
import {spawn} from 'node:child_process';
import {once} from 'node:events';
import {setTimeout as delay} from 'node:timers/promises';
import {ROOT,hash} from './catalog.mjs';

const chrome=process.env.REA_QA_CHROME||'C:/Program Files/Google/Chrome/Application/chrome.exe';
if(!fs.existsSync(chrome))throw Error('Set REA_QA_CHROME to an existing Chromium executable.');
const dist=path.join(ROOT,'dist'),out=path.join(ROOT,'assets/readme'),local=path.join(ROOT,'.runtime/readme-capture');
if(!fs.existsSync(path.join(dist,'data/catalog.json')))throw Error('Run npm run build before capturing.');
fs.mkdirSync(out,{recursive:true});fs.mkdirSync(local,{recursive:true});
const profile=fs.mkdtempSync(path.join(local,'chromium-'));
const types={'.html':'text/html; charset=utf-8','.js':'text/javascript; charset=utf-8','.css':'text/css; charset=utf-8','.json':'application/json','.png':'image/png','.svg':'image/svg+xml'};
const prefix='/rea-ui-library/';
const server=http.createServer((req,res)=>{
 const url=new URL(req.url,'http://localhost');
 if(!url.pathname.startsWith(prefix)){res.writeHead(404).end();return;}
 const relative=decodeURIComponent(url.pathname.slice(prefix.length))||'index.html',file=path.resolve(dist,relative);
 if(!file.startsWith(dist+path.sep)||!fs.existsSync(file)||!fs.statSync(file).isFile()){res.writeHead(404).end();return;}
 res.writeHead(200,{'Content-Type':types[path.extname(file)]||'application/octet-stream','Cache-Control':'no-store'});fs.createReadStream(file).pipe(res);
});
server.listen(0,'127.0.0.1');await once(server,'listening');const base='http://127.0.0.1:'+server.address().port+prefix;
const child=spawn(chrome,['--headless=new','--no-first-run','--no-default-browser-check','--disable-background-networking','--disable-component-update','--disable-sync','--user-data-dir='+profile,'--remote-debugging-port=0','about:blank'],{windowsHide:true,stdio:'ignore'});
let ws,seq=0,session;const pending=new Map(),captures=[];
function cdp(method,params={},target=session){return new Promise((resolve,reject)=>{const id=++seq,timer=setTimeout(()=>{pending.delete(id);reject(Error('CDP timeout: '+method));},45000);pending.set(id,{resolve,reject,timer});ws.send(JSON.stringify({id,method,params,...(target?{sessionId:target}:{})}));});}
const evaluate=async expression=>{const r=await cdp('Runtime.evaluate',{expression,returnByValue:true,awaitPromise:true});if(r.exceptionDetails)throw Error(r.exceptionDetails.text);return r.result.value;};
async function until(expression){for(let i=0;i<120;i++){if(await evaluate(expression))return;await delay(100);}throw Error('Missing state: '+expression);}
async function screenshot(name,width,height,description){
 await cdp('Emulation.setDeviceMetricsOverride',{width,height,deviceScaleFactor:1,mobile:width<600});
 await until('document.fonts.status === "loaded"');
 await evaluate('Promise.all([...document.images].map(i=>i.decode().catch(()=>{})))');await delay(500);
 if(!await evaluate('document.documentElement.scrollWidth<=innerWidth'))throw Error('Horizontal overflow '+name);
 const png=await cdp('Page.captureScreenshot',{format:'png',captureBeyondViewport:false,fromSurface:true}),bytes=Buffer.from(png.data,'base64');
 fs.writeFileSync(path.join(out,name),bytes);captures.push({path:'assets/readme/'+name,width,height,bytes:bytes.length,sha256:hash(bytes),capturedAt:new Date().toISOString(),description,classification:'D: catalog UI capture; contained source thumbnails retain their own source classifications.'});
}
try{
 const portFile=path.join(profile,'DevToolsActivePort');for(let i=0;i<150&&!fs.existsSync(portFile);i++)await delay(100);if(!fs.existsSync(portFile))throw Error('Chromium did not start.');
 const[port,endpoint]=fs.readFileSync(portFile,'utf8').trim().split(/\r?\n/);ws=new WebSocket('ws://127.0.0.1:'+port+endpoint);await once(ws,'open');
 ws.addEventListener('message',e=>{const m=JSON.parse(e.data),p=pending.get(m.id);if(!p)return;pending.delete(m.id);clearTimeout(p.timer);if(m.error)p.reject(Error(m.error.message));else p.resolve(m.result||{});});
 const{targetId}=await cdp('Target.createTarget',{url:'about:blank',background:false},null);session=(await cdp('Target.attachToTarget',{targetId,flatten:true},null)).sessionId;
 await cdp('Page.enable');await cdp('Runtime.enable');await cdp('Page.bringToFront');
 await cdp('Emulation.setDeviceMetricsOverride',{width:1440,height:1600,deviceScaleFactor:1,mobile:false});
 await cdp('Page.navigate',{url:base});const catalog=JSON.parse(fs.readFileSync(path.join(dist,'data/catalog.json'),'utf8'));
 await until(`document.querySelectorAll('[data-source-id]').length === ${catalog.sources.length}`);
 await screenshot('catalog-desktop.png',1440,1600,'All six source cards at the GitHub Pages project subpath; original, illustration and independent-reference thumbnails keep their source classifications.');
 await evaluate("document.querySelector('[data-source-id=kimi]').click()");await until("document.querySelector('#detail-dialog').open");
 await screenshot('catalog-detail.png',1100,1040,'Kimi detail dialog: source preview and style roles; no original application was launched.');
 await evaluate("document.querySelector('#detail-dialog').close(); window.scrollTo(0,0)");
 await screenshot('catalog-mobile.png',390,960,'Catalog responsive layout at 390 CSS pixels.');
 const report={schemaVersion:1,scope:'Built catalog only',sourceCount:catalog.sources.length,publicationRevision:catalog.publication.revision,method:'Isolated headless Chromium, deviceScaleFactor=1; unaltered viewport PNGs',pathTested:prefix,originalAppsExecuted:false,privateEvidenceOpened:false,review:{status:'pending',note:'Inspect the actual pixels before committing refreshed screenshots.'},captures};
 fs.writeFileSync(path.join(out,'manifest.json'),JSON.stringify(report,null,2)+'\n');console.log(JSON.stringify(report,null,2));
}finally{
 if(ws?.readyState===WebSocket.OPEN){try{await cdp('Browser.close',{},null);}catch{}ws.close();}
 for(const p of pending.values())clearTimeout(p.timer);child.kill();await new Promise(resolve=>server.close(resolve));
}
