// Independent CDP capture of one explicitly delegated public Hark target.
// Browser actions and measurements below are not claimed as REA tool operations.
const fs=require('node:fs/promises');
const path=require('node:path');
const crypto=require('node:crypto');
const {connectCDP}=require('../../../tools/cdp-worker-client.cjs');
const ROOT=path.resolve(__dirname,'..');
const ENDPOINT='http://127.0.0.1:52194';
const TARGET='370F28A0EB851696D0AE5539EA328426';
const hash=data=>crypto.createHash('sha256').update(data).digest('hex');
const delay=ms=>new Promise(resolve=>setTimeout(resolve,ms));
const SNAPSHOT=`(() => {
 const rect=e=>{const r=e.getBoundingClientRect();return{x:r.x,y:r.y,width:r.width,height:r.height}};
 const style=e=>{const s=getComputedStyle(e);return Object.fromEntries(['fontFamily','fontSize','fontWeight','lineHeight','letterSpacing','color','backgroundColor','border','borderRadius','padding','gap','display','position','transform','transformOrigin','perspective','perspectiveOrigin','opacity','visibility','zIndex','transition','animation','overflow','outline','outlineOffset'].map(k=>[k,s[k]]))};
 const item=e=>({tag:e.tagName,id:e.id,cls:typeof e.className==='string'?e.className:e.getAttribute('class'),text:e.innerText?.slice(0,2500)||e.textContent?.slice(0,2500),attrs:Object.fromEntries([...e.attributes].filter(a=>!['d','srcset','style'].includes(a.name)).map(a=>[a.name,a.value])),rect:rect(e),style:style(e)});
 const carousel=document.querySelector('[data-carousel-surface]');
 return {at:new Date().toISOString(),perfNow:performance.now(),url:location.origin+location.pathname,title:document.title,viewport:{width:innerWidth,height:innerHeight,dpr:devicePixelRatio,visualViewport:visualViewport?{width:visualViewport.width,height:visualViewport.height,offsetTop:visualViewport.offsetTop,scale:visualViewport.scale}:null},scroll:{x:scrollX,y:scrollY,documentHeight:document.documentElement.scrollHeight},language:{html:document.documentElement.lang,navigator:navigator.language},theme:{htmlClass:document.documentElement.className,dataTheme:document.documentElement.dataset.theme,preferredColorScheme:matchMedia('(prefers-color-scheme:dark)').matches?'dark':matchMedia('(prefers-color-scheme:light)').matches?'light':'no-preference'},media:{reducedMotion:matchMedia('(prefers-reduced-motion:reduce)').matches,hover:matchMedia('(hover:hover)').matches,pointerFine:matchMedia('(pointer:fine)').matches},focus:{tag:document.activeElement.tagName,id:document.activeElement.id,cls:document.activeElement.className},sections:[...document.querySelectorAll('[data-component=section]')].map(e=>({variant:e.dataset.variant,rect:rect(e),text:e.innerText?.slice(0,200)})),headings:[...document.querySelectorAll('h1,h2,h3')].map(item),paragraphSamples:[...document.querySelectorAll('[data-variant=app] p,[data-variant=feature] p,[data-variant=prose] h2')].slice(0,14).map(item),controls:[...document.querySelectorAll('button,[role=button],[role=radio],[role=tab],summary,input,select')].map(item),details:[...document.querySelectorAll('details')].map(e=>({...item(e),open:e.open})),pricing:document.querySelector('[data-variant=pricingV2]')?{container:item(document.querySelector('[data-variant=pricingV2]')),panels:[...document.querySelectorAll('[id$="-first"],[id$="-second"],[id$="-third"]')].map(item)}:null,fontFaces:[...document.fonts].map(f=>({family:f.family,weight:f.weight,style:f.style,status:f.status})),scripts:[...document.scripts].map(s=>s.src).filter(Boolean).map(s=>{try{const u=new URL(s);return u.origin===location.origin?u.origin+u.pathname:null}catch{return null}}).filter(Boolean),carousel:carousel?{surface:item(carousel),container:item(carousel.closest('section')),stage:item(document.querySelector('[data-carousel-stage]')),cards:[...document.querySelectorAll('[data-carousel-card]')].map(e=>({...item(e),index:Number(e.dataset.carouselCard),face:item(e.querySelector('[data-carousel-face]')),video:(()=>{const v=e.querySelector('video');return{currentSrc:v.currentSrc,poster:v.poster,currentTime:v.currentTime,duration:Number.isFinite(v.duration)?v.duration:null,paused:v.paused,ended:v.ended,readyState:v.readyState,networkState:v.networkState,videoWidth:v.videoWidth,videoHeight:v.videoHeight,error:v.error?{code:v.error.code,message:v.error.message}:null,sourceDeclarations:[...v.querySelectorAll('source')].map(s=>({type:s.type,src:s.src}))}})()})),prompts:[...document.querySelectorAll('[data-carousel-prompt]')].map((e,index)=>({...item(e),index}))}:null};
})()`;
const SAMPLE=`(() => {const s=e=>{const x=getComputedStyle(e);return{transform:x.transform,opacity:x.opacity,visibility:x.visibility,zIndex:x.zIndex,inlineTransform:e.style.transform}};return{at:new Date().toISOString(),perfNow:performance.now(),reducedMotion:matchMedia('(prefers-reduced-motion:reduce)').matches,focus:document.activeElement.getAttribute('data-carousel-surface')!==null?'carousel-surface':document.activeElement.tagName,stage:document.querySelector('[data-carousel-stage]')?.getBoundingClientRect().toJSON(),cards:[...document.querySelectorAll('[data-carousel-card]')].map(e=>({index:Number(e.dataset.carouselCard),...s(e),video:{currentTime:e.querySelector('video').currentTime,paused:e.querySelector('video').paused,readyState:e.querySelector('video').readyState}})),prompts:[...document.querySelectorAll('[data-carousel-prompt]')].map((e,index)=>({index,...s(e),text:e.textContent})).filter(e=>e.visibility!=='hidden'&&Number(e.opacity)>0)}})()`;
async function main(){
 const plan=JSON.parse(await fs.readFile(process.argv[2],'utf8'));
 if(!/^[a-z0-9-]+$/.test(plan.label))throw Error('Safe label required');
 const targets=await fetch(ENDPOINT+'/json/list').then(r=>r.json());
 const target=targets.find(t=>t.id===TARGET&&t.type==='page');
 if(!target||new URL(target.url).origin!=='https://hark.com')throw Error('Exact delegated target unavailable');
 const cdp=await connectCDP(target.webSocketDebuggerUrl);
 const report={label:plan.label,classification:'A current public-source observation; independent CDP, not a new REA Evidence ID',sourceIdentity:'Hark Web https://hark.com',targetId:TARGET,endpoint:ENDPOINT,startedAt:new Date().toISOString(),method:'Sequential declared normal DOM/UI actions, DOM/computed snapshots, optional CDP font glyph report, viewport PNG; not an atomic snapshot',reaDiscoveryEvidenceId:'ev_d90eff947caf12b775c317dd8dce4686ee7dc003a3a5a2059caab41b4b7578ad',plan,actions:[],captures:[],timelines:[]};
 const value=async expression=>{const r=await cdp.send('Runtime.evaluate',{expression,returnByValue:true,awaitPromise:true});if(r.exceptionDetails)throw Error(JSON.stringify(r.exceptionDetails));return r.result.value};
 const save=async(relative,data)=>{const file=path.join(ROOT,relative);await fs.mkdir(path.dirname(file),{recursive:true});await fs.writeFile(file,data,{flag:'wx'});return{path:relative,bytes:Buffer.byteLength(data),sha256:hash(data)}};
 const key=async name=>{const code={ArrowRight:39,ArrowLeft:37,Enter:13,Escape:27,Tab:9,' ':32}[name]||0;await cdp.send('Input.dispatchKeyEvent',{type:'keyDown',key:name,code:name===' '?'Space':name,windowsVirtualKeyCode:code,nativeVirtualKeyCode:code});await cdp.send('Input.dispatchKeyEvent',{type:'keyUp',key:name,code:name===' '?'Space':name,windowsVirtualKeyCode:code,nativeVirtualKeyCode:code});};
 async function click(selector){const box=await value(`(()=>{const e=document.querySelector(${JSON.stringify(selector)});if(!e)throw Error('missing selector');const r=e.getBoundingClientRect();return{x:r.x+r.width/2,y:r.y+r.height/2,width:r.width,height:r.height}})()`);if(box.width<=0||box.height<=0)throw Error('Control has no box');await cdp.send('Input.dispatchMouseEvent',{type:'mouseMoved',x:box.x,y:box.y});await cdp.send('Input.dispatchMouseEvent',{type:'mousePressed',x:box.x,y:box.y,button:'left',clickCount:1});await cdp.send('Input.dispatchMouseEvent',{type:'mouseReleased',x:box.x,y:box.y,button:'left',clickCount:1});return box;}
 try{
  await cdp.send('Page.enable');await cdp.send('Runtime.enable');await cdp.send('DOM.enable');await cdp.send('CSS.enable');
  report.browser=await fetch(ENDPOINT+'/json/version').then(r=>r.json()).then(v=>({browser:v.Browser,protocol:v['Protocol-Version'],'user-agent':v['User-Agent'],'v8':v['V8-Version']}));
  for(const step of plan.actions){
   const log={...step,startedAt:new Date().toISOString(),before:await value('({url:location.origin+location.pathname,scrollY,perfNow:performance.now()})')};report.actions.push(log);
   if(step.type==='foreground')await cdp.send('Page.bringToFront');
   else if(step.type==='viewport')await cdp.send('Emulation.setDeviceMetricsOverride',{width:step.width,height:step.height,deviceScaleFactor:step.dpr||1,mobile:false});
   else if(step.type==='media')await cdp.send('Emulation.setEmulatedMedia',{features:[{name:'prefers-reduced-motion',value:step.reducedMotion||'no-preference'},{name:'prefers-color-scheme',value:step.colorScheme||'light'}]});
   else if(step.type==='navigate'){
    const url=new URL(step.url);if(url.origin!=='https://hark.com'||!['/','/pricing','/using-hark'].includes(url.pathname)||url.search)throw Error('Outside delegated public page set');
    await cdp.send('Page.navigate',{url:step.url});for(let i=0;i<60;i++){await delay(100);if(await value("document.readyState==='complete'"))break;}await delay(800);
   }else if(step.type==='scroll')await value(`(()=>{const e=document.querySelector(${JSON.stringify(step.selector)});if(!e)throw Error('missing selector');window.scrollTo({top:e.getBoundingClientRect().top+scrollY-${step.top||120},behavior:'instant'});return{scrollY}})()`);
   else if(step.type==='focus')await value(`document.querySelector(${JSON.stringify(step.selector)}).focus({preventScroll:true})`);
   else if(step.type==='key')await key(step.key);
   else if(step.type==='click')log.hit=await click(step.selector);
   else if(step.type==='wait')await delay(step.ms);
   else if(step.type==='capture'){
    const startedAt=new Date().toISOString();
    const snapshot=await value(SNAPSHOT);
    const dom=await cdp.send('DOM.getDocument',{depth:-1,pierce:false});
    const fonts=[];
    for(const selector of step.fontSelectors||[]){try{const q=await cdp.send('DOM.querySelector',{nodeId:dom.root.nodeId,selector});if(!q.nodeId)throw Error('missing node');const actual=await cdp.send('CSS.getPlatformFontsForNode',{nodeId:q.nodeId});fonts.push({selector,nodeId:q.nodeId,...actual});}catch(e){fonts.push({selector,error:e.message});}}
    snapshot.platformFonts=fonts;
    const png=await cdp.send('Page.captureScreenshot',{format:'png',captureBeyondViewport:false,fromSurface:true});
    const files=[await save('captures/'+step.name+'.json',JSON.stringify(snapshot,null,2)+'\n'),await save('captures/'+step.name+'.png',Buffer.from(png.data,'base64')),await save('captures/'+step.name+'-dom.json',JSON.stringify(dom,null,2)+'\n')];
    report.captures.push({id:step.name,startedAt,completedAt:new Date().toISOString(),classification:'A current public source; independent CDP',viewport:snapshot.viewport,theme:snapshot.theme,language:snapshot.language,scroll:snapshot.scroll,reducedMotion:snapshot.media.reducedMotion,files,pixelReview:{status:'pending'}});
   }else if(step.type==='timeline'){
    const samples=[],events=[];let n=0;const start=performance.now();
    while(performance.now()-start<step.ms){
     while(n<(step.keys||[]).length&&performance.now()-start>=step.keys[n].atMs){const action=step.keys[n++];const at=new Date().toISOString(),elapsed=performance.now()-start;await key(action.key);events.push({...action,dispatchedAt:at,elapsedMs:elapsed});}
     samples.push({receiptAt:new Date().toISOString(),elapsedMs:performance.now()-start,value:await value(SAMPLE)});await delay(step.intervalMs||40);
    }
    const data={startedAt:log.startedAt,completedAt:new Date().toISOString(),classification:'A sampled runtime values; interval is instrumented receipt timing, not compositor FPS or exact spring duration',events,samples};
    const file=await save('timelines/'+step.name+'.json',JSON.stringify(data,null,2)+'\n');report.timelines.push({id:step.name,...file,samples:samples.length,events});
   }else throw Error('Unsupported step '+step.type);
   log.completedAt=new Date().toISOString();log.after=await value('({url:location.origin+location.pathname,scrollY,perfNow:performance.now()})');log.status='completed';
  }
  report.result='completed';
 }catch(error){report.result='failed';report.error=error.stack;throw error;}
 finally{report.completedAt=new Date().toISOString();await fs.writeFile(path.join(ROOT,plan.label+'-receipt.json'),JSON.stringify(report,null,2)+'\n',{flag:'wx'});cdp.close();console.log(JSON.stringify({label:plan.label,result:report.result,captures:report.captures.length,timelines:report.timelines.length,error:report.error}));}
}
main().catch(error=>{console.error(error.stack);process.exitCode=1});
