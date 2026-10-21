import http from 'node:http';
import fs from 'node:fs/promises';
import path from 'node:path';
const root=process.cwd();
const port=Number(process.env.PORT||4173);
const types={'.html':'text/html; charset=utf-8','.css':'text/css; charset=utf-8','.js':'text/javascript; charset=utf-8','.json':'application/json; charset=utf-8','.md':'text/plain; charset=utf-8','.svg':'image/svg+xml','.webp':'image/webp','.png':'image/png','.jpg':'image/jpeg','.woff2':'font/woff2','.mp4':'video/mp4'};
const server=http.createServer(async(req,res)=>{
  try{
    const url=new URL(req.url,'http://localhost');
    if(url.pathname==='/'){res.writeHead(302,{Location:'/reference/'+url.search});res.end();return;}
    const decoded=decodeURIComponent(url.pathname);
    let target=path.resolve(root,'.'+decoded);
    if(!target.startsWith(root+path.sep)){res.writeHead(403);res.end();return;}
    let stat=await fs.stat(target);
    if(stat.isDirectory()){target=path.join(target,'index.html');stat=await fs.stat(target);}
    const mime=types[path.extname(target)]||'application/octet-stream';
    res.setHeader('Content-Type',mime);
    res.setHeader('Cache-Control','no-store');
    res.setHeader('X-Content-Type-Options','nosniff');
    const bytes=await fs.readFile(target);
    const range=req.headers.range?.match(/^bytes=(\d+)-(\d*)$/);
    if(range){
      const start=Number(range[1]),end=Math.min(range[2]?Number(range[2]):bytes.length-1,bytes.length-1);
      if(start>end){res.writeHead(416,{'Content-Range':`bytes */${bytes.length}`});res.end();return;}
      res.writeHead(206,{'Content-Range':`bytes ${start}-${end}/${bytes.length}`,'Accept-Ranges':'bytes','Content-Length':end-start+1});res.end(bytes.subarray(start,end+1));return;
    }
    res.setHeader('Content-Length',stat.size);res.end(req.method==='HEAD'?undefined:bytes);
  }catch(error){res.writeHead(error.code==='ENOENT'?404:500);res.end('Not found');}
});
server.listen(port,'127.0.0.1',()=>console.log(`Muse design reference: http://127.0.0.1:${port}`));
