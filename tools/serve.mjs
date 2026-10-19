import fs from 'node:fs';
import path from 'node:path';
import http from 'node:http';
import {fileURLToPath} from 'node:url';
import {ROOT,getCatalog,readArtifact,sourceFile,contained} from './catalog.mjs';
import {prepare} from './prepare.mjs';
const types={'.html':'text/html; charset=utf-8','.css':'text/css; charset=utf-8','.js':'text/javascript; charset=utf-8','.mjs':'text/javascript; charset=utf-8','.json':'application/json; charset=utf-8','.md':'text/plain; charset=utf-8','.png':'image/png','.jpg':'image/jpeg','.webp':'image/webp','.svg':'image/svg+xml','.woff2':'font/woff2','.woff':'font/woff','.ttf':'font/ttf','.otf':'font/otf','.mp4':'video/mp4'};
export function createCatalogServer({privateMode=false}={}){
 return http.createServer((req,res)=>{
  res.setHeader('X-Content-Type-Options','nosniff');res.setHeader('Referrer-Policy','no-referrer');res.setHeader('Cache-Control','no-store');res.setHeader('Permissions-Policy','camera=(), microphone=(), geolocation=()');
  if(req.method!=='GET'&&req.method!=='HEAD'){res.writeHead(405);res.end('Read-only catalog');return;}
  try{
   const u=new URL(req.url,'http://127.0.0.1');
   const json=value=>{res.writeHead(200,{'Content-Type':types['.json']});res.end(req.method==='HEAD'?undefined:JSON.stringify(value));};
   if(u.pathname==='/data/catalog.json'){json(getCatalog({privateMode}));return;}
   if(u.pathname==='/api/document'){json(readArtifact(u.searchParams.get('source'),u.searchParams.get('path'),{privateMode}));return;}
   if(u.pathname==='/api/skill-review'){
    const f=path.join(ROOT,'docs/skill-review.md');json({label:'技能改进',path:'docs/skill-review.md',content:fs.existsSync(f)?fs.readFileSync(f,'utf8'):'技能改进草案正在整理。'});return;
   }
   let target;
   if(u.pathname.startsWith('/source/')){
    const pieces=u.pathname.slice(8).split('/').map(decodeURIComponent),id=pieces.shift();target=sourceFile(id,pieces.join('/'),{privateMode});
    res.setHeader('Content-Security-Policy',"default-src 'self' data: blob:; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; connect-src 'self'; object-src 'none'; form-action 'none'; frame-ancestors 'self'");
   }else{target=contained(path.join(ROOT,'site'),u.pathname==='/'?'index.html':decodeURIComponent(u.pathname.slice(1)));}
   if(!fs.existsSync(target)||!fs.statSync(target).isFile()){res.writeHead(404);res.end('File not available');return;}
   const size=fs.statSync(target).size,type=types[path.extname(target).toLowerCase()]||'application/octet-stream';
   if(type==='video/mp4'&&req.headers.range){const match=/^bytes=(\d+)-(\d*)$/.exec(req.headers.range),start=match?Number(match[1]):NaN,end=match&&match[2]?Math.min(Number(match[2]),size-1):size-1;if(!Number.isSafeInteger(start)||start<0||start>=size||!Number.isSafeInteger(end)||end<start){res.writeHead(416,{'Content-Range':'bytes */'+size});res.end();return;}res.writeHead(206,{'Content-Type':type,'Content-Range':`bytes ${start}-${end}/${size}`,'Content-Length':end-start+1,'Accept-Ranges':'bytes'});if(req.method==='HEAD')res.end();else fs.createReadStream(target,{start,end}).pipe(res);return;}
   res.writeHead(200,{'Content-Type':type,'Content-Length':size,...(type==='video/mp4'?{'Accept-Ranges':'bytes'}:{})});
   if(req.method==='HEAD')res.end();else fs.createReadStream(target).pipe(res);
  }catch(error){res.writeHead(403,{'Content-Type':'text/plain; charset=utf-8'});res.end(error.message);}
 });
}
const invoked=process.argv[1]&&path.resolve(process.argv[1])===fileURLToPath(import.meta.url);
if(invoked){
 prepare();const index=process.argv.indexOf('--port');const port=index>=0?Number(process.argv[index+1]):4318;
 if(!Number.isInteger(port)||port<1024||port>65535)throw Error('Invalid port');
 const privateMode=process.argv.includes('--private'),server=createCatalogServer({privateMode});
 server.on('error',e=>{console.error(e.code==='EADDRINUSE'?'Port '+port+' is occupied; use --port with an explicit free port.':e.message);process.exitCode=1;});
 server.listen(port,'127.0.0.1',()=>console.log(`REA UI Library http://127.0.0.1:${port} · ${privateMode?'local-private evidence enabled':'public catalog'} · original apps remain stopped`));
}
