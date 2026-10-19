#!/usr/bin/env python3
"""Verify saved evidence, source exports, image readability, and report links."""
import hashlib
import json
import re
from pathlib import Path
from urllib.parse import unquote

ROOT = Path(__file__).resolve().parents[1]
errors = []
index = json.loads((ROOT/'evidence/index.json').read_text())
for item in index['files']:
    path = ROOT/item['path']
    if not path.is_file():
        errors.append('missing: '+item['path']); continue
    raw = path.read_bytes()
    if len(raw) != item['bytes'] or hashlib.sha256(raw).hexdigest() != item['sha256']:
        errors.append('file identity mismatch: '+item['path'])
    if path.suffix == '.json':
        try: json.loads(raw)
        except Exception as exc: errors.append('invalid JSON: '+item['path']+' '+str(exc))

links = 0
for path in [ROOT/'README.md', *sorted((ROOT/'reports').glob('*.md'))]:
    for target in re.findall(r'\]\(([^)]+)\)', path.read_text()):
        if '://' in target or target.startswith('#'):
            continue
        target = unquote(target.split('#')[0].strip('<>'))
        links += 1
        if not (path.parent/target).exists():
            errors.append('broken link: '+str(path.relative_to(ROOT))+' -> '+target)

manifest_path = ROOT/'evidence/scripts/hark-assets/manifest.json'
manifest = json.loads(manifest_path.read_text())
exported = 0
for item in manifest.get('scripts', []):
    content = item.get('content', {})
    if content.get('state') != 'exported':
        continue
    path = manifest_path.parent/'files'/content['relative_path']
    if not path.is_file() or hashlib.sha256(path.read_bytes()).hexdigest() != content['sha256']:
        errors.append('REA source identity mismatch: '+content['relative_path'])
    exported += 1

snippets = 0
for meta_path in sorted((ROOT/'evidence/scripts/snippets').glob('*.meta.json')):
    meta = json.loads(meta_path.read_text())
    source = (ROOT/meta['source_path']).read_bytes()
    raw = (ROOT/meta['raw_path']).read_bytes()
    bounds = meta['utf8_byte_offsets']
    if hashlib.sha256(source).hexdigest() != meta['source_sha256']:
        errors.append('snippet source mismatch: '+meta_path.name)
    if hashlib.sha256(raw).hexdigest() != meta['snippet_sha256'] or source[bounds['start']:bounds['end_exclusive']] != raw:
        errors.append('snippet range mismatch: '+meta_path.name)
    snippets += 1

tokens = json.loads((ROOT/'reports/design-tokens.json').read_text())['definitions']
for token in tokens:
    text = (ROOT/token['source']).read_text()
    declarations = re.findall(re.escape(token['name']) + r'\s*:\s*([^;}]+)', text)
    canonical = lambda x: re.sub(r'\s+', '', x)
    if not any(canonical(v) == canonical(token['value']) for v in declarations):
        errors.append('token declaration not in source: '+token['name'])

images = 0
try:
    from PIL import Image
    for path in sorted((ROOT/'evidence/screenshots').glob('*')):
        with Image.open(path) as im: im.verify()
        images += 1
except ImportError:
    errors.append('Pillow unavailable: screenshot readability was not verified')

scenario = json.loads((ROOT/'evidence/rea/05-code-bundle.json').read_text())
record = next(x for x in scenario['records'] if x.get('evidence_id') == 'ev_e41e6662d8c8987542a7ab16b5c9b52ff8e19aee54fc3606814a6da3f3dc3c8c')
steps = record['normalized_result']['steps']
if any(x['status'] != 'completed' for x in steps):
    errors.append('corrected code scenario has non-completed steps')

report = {'status': 'passed' if not errors else 'failed', 'files': len(index['files']),
          'links': links, 'rea_exported_scripts': exported, 'readable_screenshots': images,
          'verified_source_snippets': snippets,
          'verified_token_declarations': len(tokens),
          'completed_code_scenario_steps': len(steps), 'errors': errors}
(ROOT/'evidence/verification.json').write_text(json.dumps(report, ensure_ascii=False, indent=2)+'\n')
print(json.dumps(report, ensure_ascii=False, indent=2))
raise SystemExit(1 if errors else 0)
