#!/usr/bin/env python3
from pathlib import Path
import zipfile, hashlib, json, re

ROOT=Path(__file__).resolve().parents[2]
PUBLIC=ROOT/'analysis/extracted/apk'
results={'frontend':[],'markdown_links':[]}
with zipfile.ZipFile(ROOT/'mirasim-0.0.497.apk') as archive:
    assert archive.testzip() is None, 'ZIP CRC failure'
    for item in archive.infolist():
        if not item.filename.startswith('assets/public/') or item.is_dir(): continue
        digest=hashlib.sha256(archive.read(item)).hexdigest()
        target=PUBLIC/item.filename
        assert target.is_file(), str(target)
        assert hashlib.sha256(target.read_bytes()).hexdigest()==digest, item.filename
        results['frontend'].append({'path':item.filename,'sha256':digest})
for document in [ROOT/'README.md',*(ROOT/'analysis/reports').glob('*.md'),ROOT/'analysis/reference/README.md']:
    for label,link in re.findall(r'\[([^\]]+)\]\(([^)]+)\)',document.read_text()):
        if re.match(r'\w+://',link) or link.startswith('#'):continue
        target=(document.parent/link.split('#')[0]).resolve()
        assert target.exists(), (document,link)
        results['markdown_links'].append({'from':str(document.relative_to(ROOT)),'target':str(target.relative_to(ROOT))})
results['apk_sha256']=hashlib.sha256((ROOT/'mirasim-0.0.497.apk').read_bytes()).hexdigest()
results['zip_crc']='passed'
results['source_map_entries']=[item.filename for item in archive.infolist() if item.filename.endswith('.map')]
results['frontend_verified']=len(results['frontend'])
results['markdown_links_verified']=len(results['markdown_links'])
(ROOT/'analysis/evidence/artifact-verification.json').write_text(json.dumps(results,indent=2))
print(f"Verified {results['frontend_verified']} frontend files, {results['markdown_links_verified']} local Markdown links; ZIP CRC passed")
