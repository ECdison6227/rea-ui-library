import http from 'node:http';
import fs from 'node:fs/promises';
import path from 'node:path';
const root=path.resolve('.');
const types={'.html':'text/html; charset=utf-8','.css':'text/css; charset=utf-8','.js':'text/javascript; charset=utf-8','.json':'application/json; charset=utf-8','.svg':'image/svg+xml','.png':'image/png','.md':'text/plain; charset=utf-8'};
const server=http.createServer(async(req,res)=>{
  try{
    let relative=decodeURIComponent(new URL(req.url,'http://localhost').pathname).replace(/^\/+/, '');
    if(!relative)relative='index.html';
    let file=path.resolve(root,relative);
    if(file!==root&&!file.startsWith(root+path.sep))throw Error('Invalid path');
    if((await fs.stat(file)).isDirectory())file=path.join(file,'index.html');
    const content=await fs.readFile(file);res.writeHead(200,{'Content-Type':types[path.extname(file)]||'application/octet-stream','Cache-Control':'no-store'});res.end(content);
  }catch{res.writeHead(404);res.end('Not found');}
});
const port=+(process.env.KIMI_STUDY_PORT||4173);
server.listen(port,'127.0.0.1',()=>console.log(`Design package: http://127.0.0.1:${port}`));
