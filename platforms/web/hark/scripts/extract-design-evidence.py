#!/usr/bin/env python3
"""Derive source-bound design declarations from the saved Hark CSS, without a browser."""
from pathlib import Path
import re, json, hashlib
ROOT=Path(__file__).resolve().parent.parent
STYLES=ROOT/'evidence/styles'
REPORTS=ROOT/'reports'
REPORTS.mkdir(exist_ok=True)
css_files=sorted(STYLES.glob('*.css'))
all_rules=[]
for p in css_files:
    s=p.read_text()
    for m in re.finditer(r'([^{}]+)\{([^{}]*)\}',s):
        selector=m.group(1).strip()
        all_rules.append({'source':str(p.relative_to(ROOT)),'selector':selector,'declarations':m.group(2),'characterOffset':m.start(),'characterEndExclusive':m.end()})
selected=[]
for r in all_rules:
    if (r['source'].endswith('B9bSKZIz.css') and r['selector'] in [':root','.theme-light','.theme-dark','.mr6c8q0']) or (r['source'].endswith('Bgegb_jl.css') and r['selector'] in [':root','.rh8xfp0','.theme-light .rh8xfp0']):
        if '--' in r['declarations'] and not r['declarations'].startswith('--spring: ease-out') and not r['declarations'].startswith('--column-w: calc'):
            selected.append(r)

def infer(name):
    uses=[]
    for r in all_rules:
        for d in r['declarations'].split(';'):
            if 'var('+name in d:
                prop=d.split(':',1)[0].strip()
                uses.append({'property':prop,'selector':r['selector'],'source':r['source'],'characterOffset':r['characterOffset']})
    props=sorted(set(u['property'] for u in uses if not u['property'].startswith('--')))
    if not name.startswith('--mr6c8q'):
        return {'status':'original-name; interpretation inferred','label':name[2:],'usedByProperties':props,'usageExamples':uses[:4]}
    candidate=[]
    for p in props:
        if p=='font-size': candidate.append('font size')
        elif p=='font-family':candidate.append('font family')
        elif p=='font-weight':candidate.append('font weight')
        elif p=='line-height':candidate.append('line height')
        elif p=='letter-spacing':candidate.append('letter spacing')
        elif 'radius' in p:candidate.append('radius')
        elif p=='z-index':candidate.append('stacking level')
        elif p=='box-shadow':candidate.append('shadow')
        elif p in ['backdrop-filter','filter','-webkit-backdrop-filter']:candidate.append('blur/filter amount')
        elif p.startswith(('padding','margin')) or p in ['gap','row-gap','column-gap']:candidate.append('spacing')
        elif p=='transition' or p.startswith('transition-'):candidate.append('transition timing')
        elif 'height' in p or 'width' in p:candidate.append('dimension')
        elif p in ['top','bottom','left','right']:candidate.append('position offset')
    return {'status':'inferred from saved CSS usages','label':', '.join(dict.fromkeys(candidate)) or 'unresolved from selected CSS','usedByProperties':props,'usageExamples':uses[:5]}

definitions=[]
for r in selected:
    for m in re.finditer(r'(--[\w-]+)\s*:\s*([^;]+)(?:;|$)',r['declarations']):
        definitions.append({'name':m.group(1),'value':m.group(2).strip(),'selector':r['selector'],'source':r['source'],'ruleCharacterOffset':r['characterOffset'],'semanticMapping':infer(m.group(1)),'evidenceType':'static published stylesheet declaration'})

payload={'target':'https://hark.com/chat','scope':'Captured loaded CSS. These declarations are static source values, not computed measurements. Opaque CSS names are preserved; semantic labels are inferred from uses. CSS is scoped, so the same variable can have multiple definitions.','offsetConvention':'zero-based Unicode character offsets in the unmodified file; end offsets exclusive','sourceManifest':'evidence/styles/manifest.json','definitions':definitions}
existing_path=REPORTS/'design-tokens.json'
if existing_path.exists():
    existing=json.loads(existing_path.read_text())
    for section in ['runtimeRootVariables','runtimeElementSamples','staticJavascriptConstants','staticWallpaperConstants']:
        if section in existing: payload[section]=existing[section]
existing_path.write_text(json.dumps(payload,ensure_ascii=False,indent=2)+'\n')
lines=['/* Exact saved declarations. Original selectors and variable names retained. */','/* Static extraction; see design-tokens.json for evidence and inferred semantics. */']
for r in selected:
    lines+=['',f"/* {r['source']} @ character {r['characterOffset']} */",r['selector']+' {']
    lines += ['  '+m.group(1)+': '+m.group(2).strip()+';' for m in re.finditer(r'(--[\w-]+)\s*:\s*([^;]+)(?:;|$)',r['declarations'])]
    lines+=['}']
(REPORTS/'design-tokens.css').write_text('\n'.join(lines)+'\n')
terms=['_151xtfx','_10dfkk1','_13nvxnx','_1ebheeh','kg31mb','_7ol3wd','_1rkfhlb','_1p406y6','material','link-card-title','link-card-source','jb6aoa','jpeo2s','data-continuous']
rules=[r for r in all_rules if any(t in r['selector'] for t in terms)]
(ROOT/'evidence/analysis/design-rules.json').write_text(json.dumps({'offsetConvention':payload['offsetConvention'],'rules':rules},ensure_ascii=False,indent=2)+'\n')
print(json.dumps({'definitionCount':len(definitions),'scopeCount':len(selected),'ruleCount':len(rules),'output':['reports/design-tokens.css','reports/design-tokens.json','evidence/analysis/design-rules.json']},ensure_ascii=False))
