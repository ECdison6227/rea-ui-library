from pathlib import Path
import json, re, hashlib
ROOT=Path(__file__).resolve().parents[1]
manifest=json.loads((ROOT/'evidence/manifest.json').read_text())
errors=[]
for item in manifest['files']:
 p=ROOT/item['path']
 if not p.exists(): errors.append('Missing '+item['path']);continue
 if hashlib.sha256(p.read_bytes()).hexdigest()!=item['sha256']:errors.append('Changed '+item['path'])
for p in (ROOT/'reports').glob('*.md'):
 for target in re.findall(r'\]\(([^)]+)\)',p.read_text()):
  if target.startswith(('http:','https:','#')):continue
  file=target.split('#',1)[0]
  if not (p.parent/file).exists():errors.append('Broken link '+str(p.relative_to(ROOT))+' -> '+target)
for p in (ROOT/'evidence').rglob('*.json'):
 try:json.loads(p.read_text())
 except Exception as e:errors.append(str(p.relative_to(ROOT))+': '+str(e))
print(json.dumps({'checkedFiles':len(manifest['files']),'errors':errors},ensure_ascii=False,indent=2))
raise SystemExit(bool(errors))
