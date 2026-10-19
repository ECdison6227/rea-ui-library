#!/usr/bin/env python3
"""Build a local file/evidence catalog without refetching or executing target code."""
import datetime
import hashlib
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def main():
    records = {}
    for path in sorted((ROOT / 'evidence/rea').glob('*.json')):
        value = json.loads(path.read_text())
        items = value.get('records', []) if isinstance(value, dict) else []
        if isinstance(value, dict) and value.get('evidence_id'):
            items.append(value)
        for item in items:
            identity = item.get('evidence_id')
            if not identity:
                continue
            entry = records.setdefault(identity, {
                'evidence_id': identity, 'operation': item.get('operation'),
                'confidence': item.get('confidence'), 'authority': item.get('authority'),
                'limitations': item.get('limitations', []), 'files': []
            })
            entry['files'].append(str(path.relative_to(ROOT)))
    files = []
    for base in ('evidence', 'reports', 'tools', 'scripts'):
        for path in sorted((ROOT / base).rglob('*')):
            if not path.is_file() or path.name in ('index.json', 'verification.json') or '__pycache__' in path.parts:
                continue
            files.append({'path': str(path.relative_to(ROOT)), 'bytes': path.stat().st_size, 'sha256': digest(path)})
    files.append({'path': 'README.md', 'bytes': (ROOT/'README.md').stat().st_size, 'sha256': digest(ROOT/'README.md')})
    result = {
        'generated_at': datetime.datetime.now().astimezone().isoformat(),
        'target': 'https://hark.com/chat', 'file_count': len(files),
        'total_bytes': sum(x['bytes'] for x in files),
        'files': files, 'rea_records': list(records.values()),
        'privacy': 'Credentials stay outside project. Network logs retain shapes, lengths and operation names, without raw headers/cookies/SSE bodies. Screenshots and DOM include authorized visible account content.'
    }
    (ROOT/'evidence/index.json').write_text(json.dumps(result, ensure_ascii=False, indent=2)+'\n')
    print(json.dumps({'files': len(files), 'rea_records': len(records), 'bytes': result['total_bytes']}, ensure_ascii=False))

if __name__ == '__main__':
    main()
