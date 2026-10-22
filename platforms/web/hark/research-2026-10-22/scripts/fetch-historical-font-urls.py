"""Acquire only font URLs already declared by the frozen original App source.

Acquisition is dated now; it does not rewrite the historical capture or prove
that the current authenticated App still uses these bytes.
"""
import concurrent.futures
import datetime
import hashlib
import json
from pathlib import Path
from urllib.parse import urlsplit
from urllib.request import Request, urlopen

root = Path(__file__).resolve().parent.parent
source = root / 'historical-app-assets.json'
data = json.loads(source.read_text())
urls = data['fontCoverage']['remoteFontUrls']
output = root / 'historical-assets' / 'acquired-fonts'
output.mkdir(parents=True, exist_ok=True)
started = datetime.datetime.now(datetime.timezone.utc).isoformat()

def acquire(url):
    u = urlsplit(url)
    if u.scheme != 'https' or u.netloc != 'assets.hark.com' or not u.path.startswith('/fonts/') or not u.path.endswith('.woff2'):
        raise ValueError('Unexpected URL outside declared first-party font scope')
    try:
        response = urlopen(Request(url, headers={'User-Agent': 'Mozilla/5.0'}), timeout=15)
        raw = response.read()
        if response.status != 200 or raw[:4] != b'wOF2':
            raise ValueError('Response is not the expected WOFF2 font')
        return {'url': url, 'bytesData': raw, 'contentType': response.headers.get('Content-Type'), 'acquiredAt': datetime.datetime.now(datetime.timezone.utc).isoformat()}
    except Exception as error:
        return {'url': url, 'available': False, 'error': str(error)}

with concurrent.futures.ThreadPoolExecutor(max_workers=4) as pool:
    obtained = list(pool.map(acquire, urls))

items = []
for row in obtained:
    raw = row.pop('bytesData', None)
    if raw is not None:
        name = Path(urlsplit(row['url']).path).name
        p = output / name
        if p.exists() and p.read_bytes() != raw:
            raise ValueError('Refusing different existing bytes at ' + name)
        if not p.exists():
            p.write_bytes(raw)
        row.update({'path': 'historical-assets/acquired-fonts/' + name, 'bytes': len(raw), 'sha256': hashlib.sha256(raw).hexdigest(), 'classification': 'B'})
    items.append(row)

manifest = {
    'sourceIdentity': 'https://hark.com',
    'declarationOrigin': 'historical',
    'declarationSource': 'historical-app-assets.json',
    'declarationSourceSha256': hashlib.sha256(source.read_bytes()).hexdigest(),
    'acquisitionOrigin': 'current',
    'startedAt': started,
    'completedAt': datetime.datetime.now(datetime.timezone.utc).isoformat(),
    'method': 'HTTP GET of exact font URLs already declared in original H15 shipped source; no credentials exported',
    'currentAppBinding': 'unverified until current loaded CSS/resource identity is observed',
    'distribution': 'private; font redistribution rights not established',
    'items': items,
}
p = root / 'historical-font-acquisition.json'
if p.exists():
    raise ValueError('Preserve prior acquisition manifest')
p.write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n')
print(json.dumps({'declared': len(urls), 'obtained': sum('path' in i for i in items), 'unavailable': [i for i in items if 'path' not in i], 'manifest': str(p)}, ensure_ascii=False))
