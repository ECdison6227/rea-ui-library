const fs = require('node:fs/promises');
const path = require('node:path');
const crypto = require('node:crypto');
const { connectCDP } = require('../../tools/cdp-worker-client.cjs');
const args = {};
for(let i=2;i<process.argv.length;i+=2)args[process.argv[i].replace(/^--/,'')]=process.argv[i+1];
async function main(){
  const listed=await fetch(args.endpoint+'/json/list').then(r=>r.json());
  const target=listed.find(t=>t.id===args.target && t.type==='page');
  if(!target || new URL(target.url).origin!=='https://hark.com')throw new Error('Exact Hark page target unavailable');
  const cdp=await connectCDP(target.webSocketDebuggerUrl);
  const source=JSON.parse(await fs.readFile(args.manifest,'utf8'));
  const fonts=source.items.filter(i=>i.type==='Font');
  const startedAt=new Date().toISOString();
  const items=[];
  await fs.mkdir(args.output,{recursive:true});
  try{
    for(const font of fonts){
      const u=new URL(font.url);
      if(u.origin!=='https://hark.com' || !u.pathname.startsWith('/_next/static/media/') || !u.pathname.endsWith('.woff2'))throw new Error('Font URL is outside observed first-party asset scope');
      const expression='(async()=>{const r=await fetch('+JSON.stringify(font.url)+');const b=new Uint8Array(await r.arrayBuffer());let s="";for(let i=0;i<b.length;i+=12000)s+=String.fromCharCode(...b.subarray(i,i+12000));return {status:r.status,contentType:r.headers.get("content-type"),data:btoa(s)}})()';
      try{
        const result=await cdp.send('Runtime.evaluate',{expression,returnByValue:true,awaitPromise:true});
        if(result.exceptionDetails)throw new Error(result.exceptionDetails.text);
        const r=result.result.value;
        const raw=Buffer.from(r.data,'base64');
        if(r.status!==200 || raw.subarray(0,4).toString('ascii')!=='wOF2')throw new Error('Unexpected font response '+r.status+' '+r.contentType);
        const name=path.basename(u.pathname);
        const file=path.join(args.output,name);
        try{const old=await fs.readFile(file);if(!old.equals(raw))throw new Error('Different bytes at '+name);}catch(e){if(e.code!=='ENOENT')throw e;await fs.writeFile(file,raw,{flag:'wx'});}
        items.push({url:font.url,path:name,bytes:raw.length,sha256:crypto.createHash('sha256').update(raw).digest('hex'),family:font.family,weight:font.weight,style:font.style,declaredSource:font.declaredSource,loadedInHomeCapture:font.loadedInHomeCapture,classification:'B',method:'HTTP GET in normal page context of observed or registered-CSS-declared font URL; no credentials exported',redistributionLicense:'not established; retained for private design research'});
      }catch(error){items.push({url:font.url,available:false,error:error.message});}
    }
    const body={sourceIdentity:'https://hark.com',sourceUrl:new URL(target.url).origin+new URL(target.url).pathname,targetId:target.id,startedAt,completedAt:new Date().toISOString(),observedUrlManifest:args.manifest,items};
    await fs.writeFile(path.join(args.output,args.label?args.label+'-manifest.json':'observed-fonts-manifest.json'),JSON.stringify(body,null,2)+'\n',{flag:'wx'});
    process.stdout.write(JSON.stringify({saved:items.filter(i=>i.path).length,items:items.map(({url,path,bytes,sha256,error})=>({url,path,bytes,sha256,error}))})+'\n');
  }finally{cdp.close();}
}
main().catch(error=>{process.stderr.write(error.stack+'\n');process.exitCode=1;});
