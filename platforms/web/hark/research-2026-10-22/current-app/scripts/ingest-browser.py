import collections
import datetime
import hashlib
import json
import pathlib
import re
import subprocess
import urllib.parse

root=pathlib.Path(__file__).resolve().parent.parent
load=lambda name:json.loads((root/name).read_text())
state=load('acquisition.json')
transfer=load('browser-transfer-receipt.json')
manifest=load('browser-transfer-manifest.json')
allowed={item['id']:item for item in manifest['resources']}
successful={item['url']:item for item in state['items'] if item['available']}
report={'schemaVersion':1,'startedAt':datetime.datetime.now(datetime.timezone.utc).isoformat(),'method':'Verify raw browser transfer receipts and parse newly retained modules only; no target execution or new HTTP requests','items':[],'newModuleMarkers':[],'parseErrors':[]}
for item in transfer['items']:
    if item['url'] in successful:continue
    original=allowed[item['id']]
    if (item['url'],item['path'])!=(original['url'],original['path']):raise RuntimeError('Fixed resource mapping mismatch')
    data=(root/item['path']).read_bytes()
    if len(data)!=item['bytes'] or hashlib.sha256(data).hexdigest()!=item['sha256']:raise RuntimeError('Transferred byte identity mismatch')
    record={**item,'available':True,'method':'normal source-page resource fetch then fixed-ID localhost raw-byte transfer; request headers not exported','sourceTransferReceipt':'browser-transfer-receipt.json'}
    state['items'].append(record);successful[item['url']]=record;report['items'].append(record)
    parsed=subprocess.run(['node',str(root/'scripts/discover-added.cjs'),str(root/item['path']),item['url']],capture_output=True,text=True)
    if parsed.returncode:report['parseErrors'].append({'url':item['url'],'error':parsed.stderr[:1000]});continue
    discovery=json.loads(parsed.stdout);report['newModuleMarkers'].append({'path':item['path'],'sha256':item['sha256'],**discovery['markers']})
    for dependency in discovery['dependencies']:
        url=urllib.parse.urljoin(item['url'],dependency['specifier']);u=urllib.parse.urlparse(url)
        valid=u.scheme=='https' and u.netloc=='assets.hark.com' and not u.query and not u.fragment and bool(re.fullmatch(r'/fonts/[A-Za-z0-9_.-]+\.(?:woff2?|ttf|otf)' if dependency['kind']=='font' else r'/assets/[A-Za-z0-9_.-]+\.(?:js|css)',u.path))
        if not valid:
            state['exclusions'].append({'url':url,'from':item['url'],'kind':dependency['kind'],'reason':'outside exact static origin/path/type scope'});continue
        state['edges'].append({'from':item['url'],'to':url,'kind':dependency['kind'],'method':dependency['method'],'sourceRange':dependency['sourceRange']})

known={item['url'] for item in state['items']}|{edge['to'] for edge in state['edges']}
missing=known-set(successful)
kind_by_url={edge['to']:edge['kind'] for edge in state['edges']}
state['pendingAtBudget']=[{'url':url,'kind':kind_by_url.get(url,'module')} for url in sorted(missing)]
state['unretainedByFailure']=sorted(missing&{item['url'] for item in state['items'] if not item['available']})
raw_bytes=sum(item['bytes'] for item in successful.values())+(root/'resources/wallpaper_core.html').stat().st_size
state['summary']={'requestAttemptsIncludingSuppliedMainAndTransfers':len(state['items']),'successfulFiles':len(successful),'savedResourceBytes':sum(item['bytes'] for item in successful.values()),'combinedRawFilesIncludingWallpaper':len(successful)+1,'combinedRawBytesIncludingWallpaper':raw_bytes,'failedRequestAttempts':sum(not item['available'] for item in state['items']),'uniqueDiscovered':len(known),'queuedUnacquired':len(missing),'unretainedUniqueUrls':len(missing),'fontFaceDeclarations':len(state['fontFaces']),'parseErrors':len(state['parseErrors'])+len(report['parseErrors'])}
state['completedAt']=datetime.datetime.now(datetime.timezone.utc).isoformat()
state['completeness']='all discovered allowed literal static dependencies retained; computed URLs and runtime reachability remain unknown' if not missing and not report['parseErrors'] else 'partial; pending or parsing gaps retained explicitly'
report.update({'completedAt':state['completedAt'],'summary':{'newFilesVerifiedAndParsed':len(report['items']),'newBytes':sum(item['bytes'] for item in report['items']),'newSvgRoots':sum(item['svgRoots'] for item in report['newModuleMarkers']),'shaderLiteralCandidates':sum(item['shaderCodeLiterals'] for item in report['newModuleMarkers']),'unretainedUniqueUrls':len(missing),'pending':state['pendingAtBudget'],'parseErrors':len(report['parseErrors'])},'combinedArchive':state['summary']})
(root/'dependency-browser-ingest.json').write_text(json.dumps(report,indent=2)+'\n')
(root/'acquisition.json').write_text(json.dumps(state,indent=2)+'\n')
print(json.dumps(report['summary']))
