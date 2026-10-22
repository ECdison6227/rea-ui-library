// Finite current public FAQ state and restoration; no account or form writes.
const fs=require('node:fs/promises');
const path=require('node:path');
const crypto=require('node:crypto');
const {connectCDP}=require('../../../tools/cdp-worker-client.cjs');
const ROOT=path.resolve(__dirname,'..');
const pause=ms=>new Promise(r=>setTimeout(r,ms));
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
async function main(){
 const target=(await fetch('http://127.0.0.1:52194/json/list').then(r=>r.json())).find(t=>t.id==='370F28A0EB851696D0AE5539EA328426');
 if(!target||new URL(target.url).origin!=='https://hark.com')throw Error('Exact selected public Hark target unavailable');
 const cdp=await connectCDP(target.webSocketDebuggerUrl);
 const receipt={startedAt:new Date().toISOString(),sourceIdentity:'https://hark.com',method:'Declared normal public FAQ DOM click and state/pixel read through existing CDP; not a REA Evidence ID',captures:[],actions:[]};
 const evaluate=async expression=>{const r=await cdp.send('Runtime.evaluate',{expression,returnByValue:true,awaitPromise:true});if(r.exceptionDetails)throw Error(r.exceptionDetails.text);return r.result.value;};
 const query=`(()=>{const b=[...document.querySelectorAll('button')].find(e=>e.textContent.trim()==='What things can Hark do?');if(!b)return null;const p=b.parentElement;const rect=e=>{const r=e.getBoundingClientRect();return{x:r.x,y:r.y,w:r.width,h:r.height}};return{at:new Date().toISOString(),url:location.href,viewport:{w:innerWidth,h:innerHeight,dpr:devicePixelRatio},question:{attributes:Object.fromEntries([...b.attributes].map(a=>[a.name,a.value])),rect:rect(b),font:getComputedStyle(b).fontFamily,size:getComputedStyle(b).fontSize,lineHeight:getComputedStyle(b).lineHeight},parent:{tag:p.tagName,attributes:Object.fromEntries([...p.attributes].map(a=>[a.name,a.value])),rect:rect(p),text:p.innerText.slice(0,2200)},details:[...document.querySelectorAll('details')].map(e=>({open:e.open})),bodyHeight:document.documentElement.scrollHeight}})()`;
 const capture=async label=>{const startedAt=new Date().toISOString(),data=await evaluate(query);const p=await cdp.send('Page.captureScreenshot',{format:'png',captureBeyondViewport:false,fromSurface:true});const png=Buffer.from(p.data,'base64');await fs.writeFile(path.join(ROOT,'captures',label+'.png'),png,{flag:'wx'});await fs.writeFile(path.join(ROOT,'captures',label+'.json'),JSON.stringify(data,null,2)+'\n',{flag:'wx'});receipt.captures.push({id:label,startedAt,completedAt:new Date().toISOString(),data,files:[{path:'captures/'+label+'.png',bytes:png.length,sha256:hash(png)}],pixelReview:{status:'pending'}});return data;};
 try{
  await cdp.send('Page.enable');await cdp.send('Runtime.enable');
  await cdp.send('Emulation.setDeviceMetricsOverride',{width:390,height:844,deviceScaleFactor:1,mobile:false});
  await cdp.send('Emulation.setEmulatedMedia',{features:[{name:'prefers-reduced-motion',value:'no-preference'},{name:'prefers-color-scheme',value:'light'}]});
  await cdp.send('Page.navigate',{url:'https://hark.com/using-hark'});
  let ready=false;for(let i=0;i<30;i++){if(await evaluate(`Boolean([...document.querySelectorAll('button')].find(e=>e.textContent.trim()==='What things can Hark do?'))`)){ready=true;break;}await pause(200);}
  if(!ready)throw Error('Expected FAQ button did not mount');
  await evaluate(`document.querySelector('h1')?.scrollIntoView({block:'start',behavior:'instant'})`);await pause(400);
  await capture('faq-narrow-closed');
  const click=`(()=>{const b=[...document.querySelectorAll('button')].find(e=>e.textContent.trim()==='What things can Hark do?');if(!b)throw Error('FAQ missing');b.click();return{at:new Date().toISOString(),clicked:'What things can Hark do?'}})()`;
  receipt.actions.push(await evaluate(click));await pause(450);await capture('faq-narrow-open');
  receipt.actions.push(await evaluate(click));await pause(450);await capture('faq-narrow-closed-again');
  receipt.status='completed';
 }catch(e){receipt.status='limited';receipt.error=e.message;}
 finally{
  try{await cdp.send('Emulation.clearDeviceMetricsOverride');await cdp.send('Emulation.setEmulatedMedia',{features:[]});await cdp.send('Page.navigate',{url:'https://hark.com/'});await pause(600);receipt.restoration=await evaluate(`({at:new Date().toISOString(),url:location.origin+location.pathname,viewport:{w:innerWidth,h:innerHeight,dpr:devicePixelRatio},reducedMotion:matchMedia('(prefers-reduced-motion:reduce)').matches})`);}catch(e){receipt.restoration={error:e.message};}
  receipt.completedAt=new Date().toISOString();await fs.writeFile(path.join(ROOT,'faq-restore-receipt.json'),JSON.stringify(receipt,null,2)+'\n',{flag:'wx'});cdp.close();console.log(JSON.stringify(receipt));
 }
}
main().catch(e=>{console.error(e);process.exitCode=1;});
