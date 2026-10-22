import collections
import datetime
import hashlib
import json
import pathlib
import re
import subprocess
import urllib.error
import urllib.parse
import urllib.request

root=pathlib.Path(__file__).resolve().parent.parent
state=json.loads((root/'acquisition.json').read_text())
now=lambda:datetime.datetime.now(datetime.timezone.utc).isoformat()
successful={item['url']:item for item in state['items'] if item['available']}
failed={item['url']:item for item in state['items'] if not item['available'] and item['url'] not in successful}
queue=collections.deque({'url':url,'kind':'font' if '/fonts/' in url else 'module'} for url in failed)
for item in state.get('pendingAtBudget',[]):
    if item['url'] not in successful and item['url'] not in failed:queue.append(item)
known=set(successful)|{item['url'] for item in queue}
wallpaper_bytes=(root/'resources/wallpaper_core.html').stat().st_size
total_bytes=sum(item['bytes'] for item in successful.values())+wallpaper_bytes
opener=urllib.request.build_opener(urllib.request.ProxyHandler({'https':'http://127.0.0.1:7897'}))
# A static asset must never redirect to another origin or account page.
class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self,req,fp,code,msg,headers,newurl):return None
opener=urllib.request.build_opener(urllib.request.ProxyHandler({'https':'http://127.0.0.1:7897'}),NoRedirect())
receipt={'schemaVersion':1,'startedAt':now(),'transport':'explicit user-configured HTTPS proxy http://127.0.0.1:7897; no cookies/auth exported; system settings unchanged','seedUrls':len(queue),'globalLimits':{'files':600,'bytes':80000000},'items':[],'newModuleMarkers':[],'parseErrors':[]}
def allowed(url,kind):
    u=urllib.parse.urlparse(url)
    return u.scheme=='https' and u.netloc=='assets.hark.com' and not u.query and not u.fragment and bool(re.fullmatch(r'/fonts/[A-Za-z0-9_.-]+\.(?:woff2?|ttf|otf)' if kind=='font' else r'/assets/[A-Za-z0-9_.-]+\.(?:js|css)',u.path))
def persist():
    state['pendingAtBudget']=list(queue)
    state['unretainedByFailure']=[url for url in known if url not in successful and not any(item['url']==url for item in queue)]
    state['summary']={'requestAttemptsIncludingSuppliedMain':len(state['items']),'successfulFiles':len(successful),'savedResourceBytes':sum(i['bytes'] for i in successful.values()),'combinedRawFilesIncludingWallpaper':len(successful)+1,'combinedRawBytesIncludingWallpaper':total_bytes,'failedRequestAttempts':sum(not i['available'] for i in state['items']),'uniqueDiscovered':len(known),'queuedUnacquired':len(queue),'unretainedUniqueUrls':len(known-set(successful)),'fontFaceDeclarations':len(state['fontFaces']),'parseErrors':len(state['parseErrors'])+len(receipt['parseErrors'])}
    state['completedAt']=now()
    state['completeness']='all discovered allowed literal dependencies retained; dynamic expressions and runtime selection remain separate' if not queue and not state['unretainedByFailure'] else 'partial; unretainedByFailure/pendingAtBudget are explicit'
    receipt['completedAt']=now()
    receipt['summary']={'newFiles':sum(i['available'] for i in receipt['items']),'failures':sum(not i['available'] for i in receipt['items']),'combinedRawFiles':len(successful)+1,'combinedRawBytes':total_bytes,'pending':len(queue),'unretainedUniqueUrls':len(known-set(successful))}
    (root/'acquisition.json').write_text(json.dumps(state,indent=2)+'\n')
    (root/'dependency-proxy-acquisition.json').write_text(json.dumps(receipt,indent=2)+'\n')
while queue and len(successful)+1<600 and total_bytes<80000000:
    item=queue.popleft();url=item['url']
    if url in successful:continue
    if not allowed(url,item['kind']):raise RuntimeError('Out-of-scope queued URL')
    record={'url':url,'classification':'B','method':'credential-free GET through explicit user-configured HTTPS proxy; redirects disabled','startedAt':now(),'available':False}
    try:
        request=urllib.request.Request(url,headers={'Accept-Encoding':'identity'})
        with opener.open(request,timeout=25) as response:
            record['status']=response.status;record['contentType']=response.headers.get('Content-Type')
            record['responseMetadata']={k:response.headers[k] for k in ['ETag','Last-Modified','Content-Length'] if k in response.headers}
            remaining=80000000-total_bytes
            if int(response.headers.get('Content-Length','0'))>remaining:raise RuntimeError('Response exceeds remaining byte budget')
            data=response.read(remaining+1)
            if len(data)>remaining:raise RuntimeError('Stream exceeds remaining byte budget')
        target=('fonts/' if item['kind']=='font' else 'resources/')+urllib.parse.urlparse(url).path.rsplit('/',1)[-1]
        if (root/target).exists():raise RuntimeError('Refusing to overwrite retained original')
        record.update({'path':target,'bytes':len(data),'sha256':hashlib.sha256(data).hexdigest(),'available':True})
        (root/target).write_bytes(data);successful[url]=record;total_bytes+=len(data)
        if item['kind']!='font':
            result=subprocess.run(['node',str(root/'scripts/discover-added.cjs'),str(root/target),url],capture_output=True,text=True)
            if result.returncode:receipt['parseErrors'].append({'url':url,'error':result.stderr[:1000]})
            else:
                parsed=json.loads(result.stdout);receipt['newModuleMarkers'].append({'path':target,'sha256':record['sha256'],**parsed['markers']})
                for dependency in parsed['dependencies']:
                    destination=urllib.parse.urljoin(url,dependency['specifier'])
                    if not allowed(destination,dependency['kind']):
                        state['exclusions'].append({'url':destination,'from':url,'kind':dependency['kind'],'reason':'outside exact static origin/path/type scope'});continue
                    state['edges'].append({'from':url,'to':destination,'kind':dependency['kind'],'method':dependency['method'],'sourceRange':dependency['sourceRange']})
                    if destination not in known:known.add(destination);queue.append({'url':destination,'kind':dependency['kind']})
    except urllib.error.HTTPError as error:
        record.update({'status':error.code,'error':'HTTP '+str(error.code),'contentType':error.headers.get('Content-Type')})
    except Exception as error:record['error']=str(error)
    record['completedAt']=now();state['items'].append(record);receipt['items'].append(record);persist()
    if not record['available']:
        # The new proxy condition is tried once per URL; no authentication workarounds.
        print(json.dumps({'failed':url,'error':record.get('error')}),flush=True)
    if len(receipt['items'])%10==0:print(json.dumps(receipt['summary']),flush=True)
persist();print(json.dumps(receipt['summary']),flush=True)
