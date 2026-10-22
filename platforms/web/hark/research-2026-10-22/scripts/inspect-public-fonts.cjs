const fs = require('node:fs/promises');
const path = require('node:path');
const crypto = require('node:crypto');
const { connectCDP } = require('../../tools/cdp-worker-client.cjs');
const args = {};
for(let i=2;i<process.argv.length;i+=2)args[process.argv[i].replace(/^--/,'')]=process.argv[i+1];
const pause=ms=>new Promise(resolve=>setTimeout(resolve,ms));
const hash=data=>crypto.createHash('sha256').update(data).digest('hex');
async function main(){
  const targets=await fetch(args.endpoint+'/json/list').then(r=>r.json());
  const target=targets.find(t=>t.id===args.target && t.type==='page');
  if(!target || new URL(target.url).origin!=='https://hark.com')throw new Error('Selected Hark target unavailable');
  const cdp=await connectCDP(target.webSocketDebuggerUrl);
  const startedAt=new Date().toISOString();
  const output=path.resolve(args.output);
  await fs.mkdir(output,{recursive:true});
  try{
    await cdp.send('Page.enable');
    if(args.url){
      if(new URL(args.url).origin!=='https://hark.com')throw new Error('Unexpected origin');
      const nav=await cdp.send('Page.navigate',{url:args.url});
      if(nav.errorText)throw new Error(nav.errorText);
      for(let i=0;i<50;i++){
        const state=await cdp.send('Runtime.evaluate',{expression:'({ready:document.readyState,path:location.pathname})',returnByValue:true});
        if(state.result.value.ready==='complete' && state.result.value.path===new URL(args.url).pathname)break;
        await pause(200);
      }
      await pause(900);
    }
    const identity=await cdp.send('Runtime.evaluate',{expression:'({url:location.origin+location.pathname,title:document.title,viewport:{width:innerWidth,height:innerHeight,dpr:devicePixelRatio},hidden:document.hidden})',returnByValue:true});
    const page=identity.result.value;
    if(/^https:\/\/hark.com\/(chat|home|settings|project|projects|plans|search)(\/|$)/.test(page.url))throw new Error('This probe is restricted to public source material');
    if(args.focusCustom==='true'){
      await cdp.send('Runtime.evaluate',{expression:'Array.from(document.querySelectorAll("h2")).find(e=>e.textContent.includes("custom"))?.scrollIntoView({block:"center"})',returnByValue:true});
      await pause(400);
    }
    await cdp.send('DOM.enable');
    await cdp.send('CSS.enable');
    const doc=await cdp.send('DOM.getDocument',{depth:1});
    const selector='h1,h2,h3,p,button,a,strong,em,input,textarea';
    const nodes=await cdp.send('DOM.querySelectorAll',{nodeId:doc.root.nodeId,selector});
    const meta=await cdp.send('Runtime.evaluate',{expression:'Array.from(document.querySelectorAll('+JSON.stringify(selector)+')).map(e=>({tag:e.tagName,text:(e.innerText||e.getAttribute("placeholder")||"").slice(0,220),className:e.getAttribute("class"),rect:{x:e.getBoundingClientRect().x,y:e.getBoundingClientRect().y,width:e.getBoundingClientRect().width,height:e.getBoundingClientRect().height}}))',returnByValue:true});
    const items=[];
    for(let i=0;i<nodes.nodeIds.length;i++){
      const nodeId=nodes.nodeIds[i];
      const m=meta.result.value[i];
      if(!m)continue;
      try{
        const fonts=await cdp.send('CSS.getPlatformFontsForNode',{nodeId});
        const styles=await cdp.send('CSS.getComputedStyleForNode',{nodeId});
        const keep=['font-family','font-size','font-weight','font-style','font-synthesis','font-synthesis-weight','line-height','letter-spacing','display','visibility','content-visibility'];
        items.push({index:i,nodeId,...m,styles:Object.fromEntries(styles.computedStyle.filter(x=>keep.includes(x.name)).map(x=>[x.name,x.value])),platformFonts:fonts.fonts,classification:'A',scope:'actual Chrome renderer report for this node; specific per-character assignment is not returned'});
      }catch(error){items.push({index:i,nodeId,...m,error:error.message,classification:'A',coverage:'partial'});}
    }
    const screenshot=await cdp.send('Page.captureScreenshot',{format:'png',fromSurface:true});
    const bytes=Buffer.from(screenshot.data,'base64');
    await fs.writeFile(path.join(output,args.label+'.png'),bytes,{flag:'wx'});
    const data={sourceIdentity:'https://hark.com',...page,startedAt,completedAt:new Date().toISOString(),method:'independent CDP CSS.getPlatformFontsForNode and getComputedStyleForNode; sequential renderer metadata plus screenshot, non-atomic',screenshot:{path:args.label+'.png',sha256:hash(bytes),bytes:bytes.length},items,limitations:['No exact per-character font assignment is returned by the platform-font API.','Font family/PostScript matches need original font/source identity correlation.','Source tab visibility and viewport are recorded; inactive or unrendered nodes can have empty reports.']};
    await fs.writeFile(path.join(output,args.label+'.json'),JSON.stringify(data,null,2)+'\n',{flag:'wx'});
    const families=[...new Set(items.flatMap(x=>(x.platformFonts||[]).map(f=>f.familyName)))];
    process.stdout.write(JSON.stringify({label:args.label,url:page.url,nodes:items.length,families,specialTitles:items.filter(x=>x.tag==='H2' && /custom|assured|easier/.test(x.text)),output})+'\n');
  }finally{cdp.close();}
}
main().catch(error=>{process.stderr.write(error.stack+'\n');process.exitCode=1;});
