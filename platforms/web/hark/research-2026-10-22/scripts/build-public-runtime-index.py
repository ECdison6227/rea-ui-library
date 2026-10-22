#!/usr/bin/env python3
"""Index completed public captures; keep background limitations and reviews separate."""
import hashlib
import json
from datetime import datetime, timezone
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]/'public-runtime'
NOW=datetime.now(timezone.utc).isoformat()
def digest(p):return hashlib.sha256(p.read_bytes()).hexdigest()
receipts=[];captures=[];errors=[]
for name in ['home-carousel-receipt.json','home-foreground-receipt.json','pricing-narrow-receipt.json','faq-restore-receipt.json']:
    p=ROOT/name;j=json.loads(p.read_text());receipts.append({'path':name,'sha256':digest(p),'startedAt':j['startedAt'],'completedAt':j['completedAt'],'result':j.get('result',j.get('status'))})
    for c in j['captures']:
        reviewed=name!='home-carousel-receipt.json'
        row={k:v for k,v in c.items() if k not in ('data','pixelReview')}
        row['receipt']=name
        row['pixelReview']={'status':'reviewed' if reviewed else 'pending','reviewedAt':NOW if reviewed else None,'method':'Root viewed the actual PNG outputs; checks layout, state and visible loading limitations.' if reviewed else 'Retained diagnostic background run; not promoted as reviewed visual coverage.'}
        if 'data' in c:row['viewport']=c['data']['viewport']
        for f in c['files']:
            p=ROOT/f['path'];ok=p.stat().st_size==f['bytes'] and digest(p)==f['sha256']
            if not ok:errors.append('Changed capture '+f['path'])
        captures.append(row)
timelines=[]
for p in sorted((ROOT/'timelines').glob('foreground-*.json')):
    j=json.loads(p.read_text());samples=j['samples']
    def center(v):return max(v['cards'],key=lambda c:float(c.get('zIndex') or 0))['index']
    first,last=samples[0]['value'],samples[-1]['value']
    timelines.append({'path':str(p.relative_to(ROOT)),'sha256':digest(p),'sampleCount':len(samples),
        'observedIntervalMs':samples[-1]['elapsedMs'],'firstCenter':center(first),'lastCenter':center(last),
        'focus':first.get('focus'),'reducedMotion':first.get('reducedMotion'),
        'distinctCard0Transforms':len({s['value']['cards'][0]['transform'] for s in samples}),
        'firstCenterVideo':first['cards'][center(first)].get('video'),'lastCenterVideo':last['cards'][center(last)].get('video'),
        'limit':'Discrete instrumented samples, not compositor FPS or fitted spring duration.'})
fonts=[]
for name in ['foreground-home-desktop-intro','foreground-carousel-desktop-before']:
    p=ROOT/'captures'/(name+'.json');j=json.loads(p.read_text())
    fonts.append({'path':str(p.relative_to(ROOT)),'sha256':digest(p),'platformFonts':j.get('platformFonts',[])})
faq=json.loads((ROOT/'faq-restore-receipt.json').read_text())
payload={'formatVersion':1,'kind':'reviewed-current-public-runtime','sourceIdentity':'https://hark.com',
    'recordedAt':NOW,'provider':'Independent CDP plus exact retained REA records; local summary is not a REA Evidence ID',
    'receipts':receipts,'captures':captures,'foregroundTimelineFindings':timelines,'actualNodeFontReports':fonts,
    'faqStates':[{'id':c['id'],'ariaExpanded':c['data']['question']['attributes'].get('aria-expanded'),'viewport':c['data']['viewport']} for c in faq['captures']],
    'restoration':faq['restoration'],
    'observedRules':['Carousel surface is keyboard-focusable role=group, supports ArrowRight/ArrowLeft; no separate next/prev buttons were observed.',
        'Foreground next 0→1, previous 1→0, interrupted reversal 0→0; transforms vary during the normal runs.',
        'At 390px, normal next 0→1; reduced-motion next 1→2 with only two observed card0 transforms and video paused at zero.',
        'The reduced/focused idle sample retains center2 for about6.47s; focus and reduction are not isolated causal variables.',
        'Narrow pricing switches visible Free/pro2/pro3 content; keyboard activation shows a visible focus ring.',
        'FAQ real click changes aria-expanded false→true→false and visible answer appears/disappears.'],
    'limitations':['The earlier background capture froze rAF and had media readiness0; it is retained as limited evidence, never used to measure motion.',
        'Foreground carousel videos reached readyState4 and played in normal mode; this does not verify every media element.',
        'External YouTube showed a login/robot verification prompt in narrow source captures; no login or bypass attempted.',
        'Samples do not measure compositor FPS, exact animation start time, unique spring parameters, all drag/hover/hidden/error branches or cross-device state.',
        'DOM, computed values and pixels are sequential captures, not atomic snapshots; viewport/DPR follow each actual record.',
        'Restoration clears emulation and returns to the current real viewport1440×646/DPR2; it does not assert that original physical window dimensions were forced.'],
    'verification':{'errors':errors,'status':'passed' if not errors else 'failed','reviewedCaptures':sum(c['pixelReview']['status']=='reviewed' for c in captures),'pendingBackgroundCaptures':sum(c['pixelReview']['status']=='pending' for c in captures)}}
(ROOT/'index.json').write_text(json.dumps(payload,ensure_ascii=False,indent=2)+'\n')
print(json.dumps(payload['verification']))
