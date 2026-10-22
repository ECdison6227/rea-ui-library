import datetime
import hashlib
import http.server
import json
import os
import pathlib
import re
import urllib.parse

root=pathlib.Path(__file__).resolve().parent.parent
state=json.loads((root/'acquisition.json').read_text())
retained={item['url'] for item in state['items'] if item['available']}
urls=list(dict.fromkeys(item['url'] for item in state['items'] if not item['available'] and item['url'] not in retained))
if len(urls)!=56:raise RuntimeError('Receiver must start from the exact56 known missing resources')
resources=[]
for index,url in enumerate(urls):
    parsed=urllib.parse.urlparse(url)
    if parsed.netloc!='assets.hark.com' or parsed.scheme!='https' or not re.fullmatch(r'/assets/[A-Za-z0-9_.-]+\.js',parsed.path):raise RuntimeError('Invalid fixed static resource URL')
    basename=parsed.path.rsplit('/',1)[-1]
    resources.append({'id':f'dep-{index+1:03d}-{basename[:-3]}','url':url,'path':'resources/'+basename})
allow={item['id']:item for item in resources}
manifest={'schemaVersion':1,'allowedOrigin':'https://hark.com','bind':'127.0.0.1','port':48761,'resources':resources,'boundary':'Only fixed IDs accept raw bytes; no request header, URL, cookie, profile or private metadata is retained.'}
(root/'browser-transfer-manifest.json').write_text(json.dumps(manifest,indent=2)+'\n')
now=lambda:datetime.datetime.now(datetime.timezone.utc).isoformat()
receipt={'schemaVersion':1,'startedAt':now(),'pid':os.getpid(),'status':'running','method':'coordinator normal browser resource fetch then raw-byte localhost POST','items':[]}
def persist(): (root/'browser-transfer-receipt.json').write_text(json.dumps(receipt,indent=2)+'\n')
persist()
class Handler(http.server.BaseHTTPRequestHandler):
    protocol_version='HTTP/1.1'
    def log_message(self,*args):pass
    def cors(self):
        self.send_header('Access-Control-Allow-Origin','https://hark.com')
        self.send_header('Vary','Origin')
    def reply(self,status,value,cors=True):
        body=json.dumps(value).encode();self.send_response(status)
        if cors:self.cors()
        self.send_header('Content-Type','application/json');self.send_header('Content-Length',str(len(body)));self.end_headers();self.wfile.write(body)
    def do_OPTIONS(self):
        if self.headers.get('Origin')!='https://hark.com':return self.reply(403,{'error':'origin'},False)
        if self.path not in {'/receive/'+item for item in allow}:return self.reply(404,{'error':'unknown id'})
        self.send_response(204);self.cors();self.send_header('Access-Control-Allow-Methods','POST, OPTIONS');self.send_header('Access-Control-Allow-Headers','content-type');self.send_header('Access-Control-Allow-Private-Network','true');self.send_header('Content-Length','0');self.end_headers()
    def do_POST(self):
        if self.headers.get('Origin')!='https://hark.com':return self.reply(403,{'error':'origin'},False)
        resource_id=self.path[len('/receive/'):] if self.path.startswith('/receive/') else ''
        if resource_id not in allow or self.path!='/receive/'+resource_id:return self.reply(404,{'error':'unknown id'})
        try:length=int(self.headers.get('Content-Length','-1'))
        except ValueError:return self.reply(411,{'error':'content length required'})
        if length<1 or length>10000000:return self.reply(413,{'error':'bounded resource size'})
        raw_count=len(list((root/'resources').iterdir()))+len(list((root/'fonts').iterdir()))
        raw_bytes=sum(p.stat().st_size for folder in ['resources','fonts'] for p in (root/folder).iterdir() if p.is_file())
        if raw_count>=600 or raw_bytes+length>80000000:return self.reply(413,{'error':'global archive budget'})
        data=self.rfile.read(length)
        if len(data)!=length:return self.reply(400,{'error':'incomplete body'})
        item=allow[resource_id];file=root/item['path'];digest=hashlib.sha256(data).hexdigest()
        if file.exists():
            if hashlib.sha256(file.read_bytes()).hexdigest()!=digest:return self.reply(409,{'error':'retained bytes differ'})
            return self.reply(200,{'id':resource_id,'bytes':len(data),'sha256':digest,'alreadyRetained':True})
        # Only static JS is accepted; challenge/login HTML is rejected before retention.
        if data.lstrip().startswith(b'<'):return self.reply(415,{'error':'HTML is not the fixed static JS resource'})
        with file.open('xb') as output:output.write(data)
        record={'id':resource_id,'url':item['url'],'path':item['path'],'bytes':len(data),'sha256':digest,'receivedAt':now(),'classification':'B original static resource bytes; supplied through authorized page fetch, no headers exported'}
        receipt['items'].append(record);persist();self.reply(201,{k:record[k] for k in ['id','bytes','sha256']})
server=http.server.HTTPServer(('127.0.0.1',48761),Handler)
print(json.dumps({'pid':os.getpid(),'bind':'127.0.0.1','port':48761,'first':resources[0]}),flush=True)
try:server.serve_forever()
except KeyboardInterrupt:pass
finally:
    server.server_close();receipt['status']='stopped';receipt['stoppedAt']=now();persist()
