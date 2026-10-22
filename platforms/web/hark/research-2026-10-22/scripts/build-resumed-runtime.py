#!/usr/bin/env python3
"""Build a safe current-runtime index from locally withheld computer receipts.

This does not execute Hark, copy cookies, or promote historical/D evidence to A.
"""
import hashlib
import json
import re
import shutil
from datetime import datetime, timezone
from pathlib import Path

STUDY = Path(__file__).resolve().parents[2]
REPO = STUDY.parents[2]
PRIVATE = REPO / 'private/hark-2026-10-22'
OUT = STUDY / 'research-2026-10-22/authenticated-runtime'
OUT.mkdir(parents=True, exist_ok=True)
NOW = datetime.now(timezone.utc).isoformat()

def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def returned(path):
    result = json.loads(path.read_text())
    for block in result.get('content', []):
        text = block.get('text', '')
        if 'Returned: ' in text:
            raw = text.split('Returned: ', 1)[1]
            try:
                obj, _ = json.JSONDecoder().raw_decode(raw)
                if isinstance(obj, str):
                    obj = json.loads(obj)
                if isinstance(obj, dict):
                    return obj
            except (ValueError, TypeError):
                pass
    raise ValueError(f'No successful JSON return in {path.name}')

def clean(value, key=''):
    if isinstance(value, list):
        return [clean(v, key) for v in value]
    if isinstance(value, dict):
        return {k: clean(v, k) for k, v in value.items()
                if k not in ('value', 'editorValue', 'html', 'preview', 'headings', 'composerText')}
    if isinstance(value, str):
        value = re.sub(r'[\w.+-]+@[\w.-]+\.[A-Za-z]{2,}', '[email]', value)
        value = re.sub(r'[0-9a-f]{8}-[0-9a-f-]{27,}', '[id]', value)
        value = re.sub(r'\d+\s*个活跃', '活跃状态（数量省略）', value)
        if key in ('name', 'label'):
            if re.search(r'\.(pptx?|csv|xlsx?|png|jpe?g|pdf|docx?)', value, re.I):
                return '[file preview]'
            if value.endswith('的详细信息'):
                return '连接项详情'
            if key in ('name', 'label') and value.startswith('Image to Editable '):
                return '[existing project title withheld]'
        return value
    return value

records = []
FILES = {
    'identity': 'resumed-auth-identity-tool.json',
    'theme-auto': 'theme-auto-styles-tool.json',
    'theme-dark': 'theme-dark-styles-tool.json',
    'theme-light': 'theme-light-styles-tool.json',
    'chat-computed': 'chat-computed-success-tool.json',
    'summary-owner-motion': 'summary-owner-motion-tool.json',
}
KEYS = [
    'hark-preferences-controls', 'hark-theme-options-stable',
    'hark-composer-draft-success', 'hark-projects-controls',
    'hark-new-project-initial', 'hark-chat-draft-return',
    'hark-attachment-menu-success', 'hark-search-query-verified',
    'hark-search-empty-verified', 'hark-search-settled',
    'hark-summary-computed', 'hark-summary-mobile-computed',
    'hark-chat-mobile-computed', 'hark-mobile-empty-composer',
    'hark-settings-profile', 'hark-settings-定时任务',
    'hark-settings-账户', 'hark-settings-安全',
    'hark-settings-wallet-stable', 'hark-settings-feedback',
    'hark-plans-controls', 'hark-settings-about',
    'hark-settings-search-query', 'hark-new-panel-stable',
    'hark-new-panel-cancel', 'hark-browser-restored',
]
FILES.update({k: 'receipts/' + k + '.json' for k in KEYS})
for name, relative in FILES.items():
    path = PRIVATE / relative
    obj = returned(path)
    if name == 'identity':
        obj = {k: obj[k] for k in ('capturedAt', 'url', 'title', 'viewport', 'html', 'fonts', 'canvas')}
        full = returned(path)
        obj['scripts'] = [u for u in full['scripts'] if u.startswith('https://assets.hark.com/assets/')]
        obj['styles'] = full['styles']
    safe = clean(obj)
    if name in ('hark-composer-draft-success', 'hark-chat-draft-return'):
        values = [x.get('value') for x in obj.get('editor', [])]
        safe['neutralResearchDraftPresent'] = bool(values) and all('研究用草稿' in (v or '') for v in values)
    records.append({
        'id': 'hark-20261022-a-' + name,
        'classification': 'A', 'kind': 'local-runtime-observation',
        'isReaEvidenceId': False,
        'method': 'computer_repl Chrome extension; normal user-authorized existing Hark session',
        'withheldOriginal': {'sha256': digest(path), 'sharing': 'private-local-only'},
        'observation': safe,
    })

motion = next(r['observation'] for r in records if r['id'].endswith('summary-owner-motion'))
frames = motion.get('frames', [])
motion['interpretation'] = {
    'selector': '._2za4zx8', 'event': motion.get('event'),
    'firstObservedFrameMs': frames[0]['t'] if frames else None,
    'firstObservedOpacityOneMs': next((f['t'] for f in frames if f.get('opacity') == '1'), None),
    'sourceDeclaration': '200ms easeOut opacity 0→1 and blur(4px)→0 on outer host',
    'limit': 'rAF samples start after the click and can miss frames; observed time is not a fitted duration/easing or FPS claim',
}
payload = {
    'formatVersion': 1, 'kind': 'current-authenticated-runtime',
    'source': {'identity': 'https://hark.com', 'currentMain': 'main-BgE0mqJD.js',
               'authentication': 'verified by accessible chat, projects and settings in existing user session',
               'firstVerifiedAt': '2026-10-22T15:04:08.734Z'},
    'recordedAt': NOW,
    'privacy': 'No cookies, tokens, account names, field values, project IDs, filenames, or private chat text in these normalized records. Raw receipts/screens remain local private.',
    'evidenceIdPolicy': 'These local IDs are analyst-assigned and are not invented REA Evidence IDs.',
    'conditions': ['1440×668 and 390×844 CSS px, DPR2; Chrome extension; zh-CN',
                   'Viewport overrides were reset; original Auto theme and expanded Home restored; neutral draft cleared.',
                   'Auto was first observed light/day and later dark/night. Separate captures, not a frozen atomic comparison.',
                   'No messages, feedback, purchases, new project submission, account connections or credential changes were submitted.'],
    'records': records,
    'unverified': ['OS soft keyboard/touch and OS IME', 'Per-glyph actual platform font attribution',
                   'All backend/error/permission states and cross-device matrix', 'Original reduced-motion and every weather branch'],
}
(OUT / 'observations.json').write_text(json.dumps(payload, ensure_ascii=False, indent=2) + '\n')
images = []
for name in ('summary-open.jpg', 'summary-mobile.jpg'):
    source = PRIVATE / name
    target = OUT / ('source-' + name)
    shutil.copyfile(source, target)
    images.append({'path': str(target.relative_to(STUDY)), 'sha256': digest(target),
                   'bytes': target.stat().st_size, 'classification': 'A',
                   'content': 'existing neutral REA UI sample; current original renderer',
                   'pixelReviewed': True,
                   'reviewTime': {'exactReviewedAt': None,
                                  'interval': ['2026-10-22T15:30:06Z', '2026-10-22T15:33:52Z'],
                                  'meaning': 'Root visually inspected actual screenshot tool outputs in this interval; exact review time not retained.'},
                   'processing': 'byte-identical copy; no edits or redaction'})
(OUT / 'images.json').write_text(json.dumps({'recordedAt': NOW, 'items': images}, ensure_ascii=False, indent=2) + '\n')
print(json.dumps({'records': len(records), 'sourceImages': len(images), 'output': str(OUT)}, ensure_ascii=False))
