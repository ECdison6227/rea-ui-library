import collections
import datetime
import difflib
import hashlib
import json
import pathlib
import re
import xml.etree.ElementTree as ET

root = pathlib.Path(__file__).resolve().parent.parent
read_json = lambda name: json.loads((root / name).read_text())
digest = lambda data: hashlib.sha256(data).hexdigest()
acquisition = read_json('acquisition.json')
failures = []
counts = collections.Counter()

for item in acquisition['items']:
    if not item['available']:
        continue
    data = (root / item['path']).read_bytes()
    counts['archivedResourceIdentities'] += 1
    if len(data) != item['bytes'] or digest(data) != item['sha256']:
        failures.append({'kind': 'resourceIdentity', 'path': item['path']})

receipt = read_json('wallpaper-browser-acquisition.json')
data = (root / 'resources/wallpaper_core.html').read_bytes()
counts['wallpaperIdentity'] += 1
if len(data) != receipt['bytes'] or digest(data) != receipt['sha256']:
    failures.append({'kind': 'wallpaperIdentity'})

def verify_tree(value):
    if isinstance(value, list):
        for child in value:
            verify_tree(child)
    elif isinstance(value, dict):
        source = value.get('source')
        if isinstance(source, dict) and source.get('path') and source.get('sha256'):
            file = root / source['path']
            if file.is_file():
                data = file.read_bytes()
                counts['sourceShaReferences'] += 1
                if digest(data) != source['sha256']:
                    failures.append({'kind': 'sourceSha', 'path': source['path']})
                span = source.get('htmlRange', source.get('range', {})).get('utf8Bytes')
                if span:
                    for key in ['rawSource', 'rawLiteral']:
                        if isinstance(value.get(key), str):
                            counts['sourceByteSlices'] += 1
                            if data[span['start']:span['endExclusive']] != value[key].encode():
                                failures.append({'kind': 'sourceSlice', 'path': source['path'], 'key': key, 'range': span})
        for child in value.values():
            verify_tree(child)

for name in ['icons.json', 'theme-tokens.json', 'font-inventory.json', 'sky-static.json', 'summary-static.json', 'unretained-dependency-bindings.json']:
    verify_tree(read_json(name))

ET.parse(root / 'named-glyph-sprite.svg')
counts['spriteXml'] += 1
sky = read_json('sky-static.json')
for item in sky['shaders'] + [sky['lut']]:
    data = (root / item['path']).read_bytes()
    counts['shaderAndLutIdentities'] += 1
    if digest(data) != item['sha256'] or len(data) != item['bytes']:
        failures.append({'kind': 'shaderOrLutIdentity', 'path': item['path']})

old_html = (root.parent.parent / 'evidence/scripts/wallpaper_core.html').read_text()
old_script = re.search(r'<script\b[^>]*>([\s\S]*?)</script>', old_html).group(1)
new_script = (root / 'sky/wallpaper-engine.raw.js').read_text()
diff = ''.join(difflib.unified_diff(old_script.splitlines(keepends=True), new_script.splitlines(keepends=True), fromfile='H15 retained wallpaper engine', tofile='2026-10-22 current wallpaper engine', n=3))
(root / 'sky/engine-vs-h15.diff').write_text(diff)

retained_urls = {item['url'] for item in acquisition['items'] if item['available']}
main_urls = {edge['to'] for edge in acquisition['edges'] if edge['from'] == acquisition['source']['url'] and edge['method'] == 'complete-AST-vite-dependency-table-entry'}
discovered_urls = {item['url'] for item in acquisition['items']} | {edge['to'] for edge in acquisition['edges']}
transfer = read_json('browser-transfer-receipt.json')
for item in transfer['items']:
    data = (root / item['path']).read_bytes()
    counts['browserTransferIdentities'] += 1
    if len(data) != item['bytes'] or digest(data) != item['sha256']:
        failures.append({'kind': 'browserTransferIdentity', 'path': item['path']})
if transfer['status'] != 'stopped':
    failures.append({'kind': 'receiverNotStopped'})
counts['receiverStopped'] += 1
validation = {'schemaVersion': 1, 'validatedAt': datetime.datetime.now(datetime.timezone.utc).isoformat(), 'status': 'passed' if not failures else 'failed', 'checks': dict(counts), 'failures': failures, 'mainViteMapDependencies': {'declared': len(main_urls), 'retained': len(main_urls & retained_urls), 'unretained': sorted(main_urls - retained_urls)}, 'literalDependencyClosure': {'discovered': len(discovered_urls), 'retained': len(discovered_urls & retained_urls), 'unretained': sorted(discovered_urls - retained_urls)}, 'boundary': 'Inert B source-byte validation, not original product runtime verification.'}
(root / 'validation.json').write_text(json.dumps(validation, indent=2) + '\n')
if failures:
    raise SystemExit(json.dumps(failures[:5]))

entries = []
for file in sorted(root.rglob('*')):
    if file.is_file() and file.name != 'manifest.json':
        data = file.read_bytes()
        entries.append({'path': str(file.relative_to(root)), 'bytes': len(data), 'sha256': digest(data), 'kind': 'original-static-response' if file.parent.name in ['resources', 'fonts'] else 'derived-extraction-or-receipt'})
tree_text = '\n'.join(item['path'] + '\0' + item['sha256'] + '\0' + str(item['bytes']) for item in entries)
retained = {item['url']: item for item in acquisition['items'] if item['available']}
raw_resource_files = len(retained) + 1
raw_resource_bytes = sum(item['bytes'] for item in retained.values()) + receipt['bytes']
manifest = {'schemaVersion': 1, 'source': 'https://hark.com/chat', 'currentMain': acquisition['source']['url'], 'generatedAt': datetime.datetime.now(datetime.timezone.utc).isoformat(), 'entries': entries, 'fileCount': len(entries), 'totalBytes': sum(item['bytes'] for item in entries), 'rawResourceFiles': raw_resource_files, 'rawResourceBytes': raw_resource_bytes, 'literalStaticDependencyClosure': 'complete for discovered allowed literal imports and CSS font URLs', 'treeSha256': digest(tree_text.encode()), 'boundary': 'Current static source; runtime evidence owned by coordinating investigator.', 'redistributionLicense': 'not established; retained research assets'}
(root / 'manifest.json').write_text(json.dumps(manifest, indent=2) + '\n')
print(json.dumps({'validation': validation, 'manifestFileCount': manifest['fileCount'], 'totalPackageBytes': manifest['totalBytes']}))
