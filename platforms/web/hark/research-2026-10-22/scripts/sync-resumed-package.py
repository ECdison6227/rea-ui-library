#!/usr/bin/env python3
"""Integrate the resumed study without changing historical source artifacts."""
import hashlib
import json
from datetime import datetime, timezone
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
RUN = ROOT / 'research-2026-10-22'
NOW = datetime.now(timezone.utc).isoformat()
def read(p): return json.loads((ROOT / p).read_text())
def write(p, obj): (ROOT / p).write_text(json.dumps(obj, ensure_ascii=False, indent=2) + '\n')
def sha(p): return hashlib.sha256((ROOT / p).read_bytes()).hexdigest()
def replace_item(items, item):
    items[:] = [x for x in items if x['id'] != item['id']]
    items.append(item)

obs = read('research-2026-10-22/authenticated-runtime/observations.json')
app = read('research-2026-10-22/current-app/manifest.json')
media = read('research-2026-10-22/current-public-media/manifest.json')
fonts = read('research-2026-10-22/public/resources/declared-fonts-fetched-manifest.json')
evidence = read('research-2026-10-22/evidence-index.json')
new_evidence = [
 ('hark-20261022-a-authenticated-runtime', 'A', 'runtime', 'source', 'research-2026-10-22/authenticated-runtime/observations.json', 'Existing authorized Chrome session: current chat, projects, settings, themes, neutral draft and search; raw account data withheld.'),
 ('hark-20261022-b-current-app-resources', 'B', 'static', 'source', 'research-2026-10-22/current-app/manifest.json', 'Current original resource bytes; complete discovered literal static dependency closure; no execution equivalence claim.'),
 ('hark-20261022-b-current-app-icons', 'B', 'static', 'source', 'research-2026-10-22/current-app/icons.json', 'Exact SVG factory/root/owner and named glyph source ranges; source bytes and hashes.'),
 ('hark-20261022-b-current-app-fonts', 'B', 'static', 'source', 'research-2026-10-22/current-app/font-inventory.json', '33 CSS font-face declarations and 21 original WOFF2 files, font tables and per-scope binding.'),
 ('hark-20261022-b-current-app-sky', 'B', 'static', 'source', 'research-2026-10-22/current-app/sky-static.json', 'Current wallpaper core, exact GLSL/LUT comparison and CPU fade interruption differences.'),
 ('hark-20261022-b-current-app-summary', 'B', 'static', 'source', 'research-2026-10-22/current-app/summary-static.json', 'Summary owner binding: outer host opacity/filter, declared 200ms easeOut; distinct from inner reading panel.'),
 ('hark-20261022-b-current-public-media', 'B', 'static', 'source', 'research-2026-10-22/current-public-media/manifest.json', 'Explicit public media locators, original response bytes, decoded metadata and carousel/feature binding; no all-media runtime claim.'),
 ('hark-20261022-d-reference-run', 'D', 'independent', 'reference', 'research-2026-10-22/comparison/reference-run.json', 'Independent local note workflow: 13 checks, 8 reviewed captures, no original service calls.'),
 ('hark-20261022-d-transfer-run', 'D', 'independent', 'transfer', 'research-2026-10-22/comparison/reference-run.json', 'Cross-domain notes workflow exercises draft retention, local save/cancel/retry, search, reading and themes.'),
]
if (RUN/'public-runtime/index.json').exists():
    new_evidence.append(('hark-20261022-a-public-runtime','A','runtime','source','research-2026-10-22/public-runtime/index.json','Current public foreground carousel keyboard/reversal/narrow/reduced, pricing keyboard and FAQ; 16 actual reviewed captures, background limitations separate.'))
if (RUN/'comparison/reference-update-current.json').exists():
    new_evidence.append(('hark-20261022-d-reference-update','D','independent','reference','research-2026-10-22/comparison/reference-update-current.json','Original Bold700/Serif20 font reuse, source-aligned reader panel, repeated13 checks and8 actual reviewed screenshots.'))
    new_evidence.append(('hark-20261022-d-server-check','D','independent','reference','research-2026-10-22/comparison/server-path-check.json','Seven actual local HTTP namespace/method/original-font checks; no original backend calls.'))
for id_, cls, kind, subject, path, method in new_evidence:
    entry = dict(id=id_, classification=cls, origin='current', subject=subject,
                 kind=kind, sourceIdentity='https://hark.com' if subject == 'source' else 'hark-independent-local-notes',
                 capturedAt=None, sharing='included', path=path, sha256=sha(path), method=method)
    if cls == 'A':
        if id_=='hark-20261022-a-public-runtime':
            pub=read(path)
            entry['captureInterval']={'startedAt':min(x['startedAt'] for x in pub['receipts']),'completedAt':max(x['completedAt'] for x in pub['receipts'])}
        else:
            entry['captureInterval'] = {'startedAt':'2026-10-22T15:04:08.734Z','completedAt':'2026-10-22T15:48:12.999Z'}
        previous = next((x for x in evidence['items'] if x['id']==id_), {})
        entry['review'] = previous.get('review') or {'status':'reviewed','reviewedAt':NOW,'method':'Root reviewed safe observations and source-summary desktop/mobile pixels; private receipts withheld.'}
        if id_=='hark-20261022-a-public-runtime':
            entry['review']['method']='Root actually viewed all16 foreground/pricing/FAQ PNGs and reviewed discrete timeline and node-font reports;8 background visual captures remain pending.'
            entry['provider']='Independent CDP; local ID, not a REA Evidence ID'
        else:
            entry['provider'] = 'computer_repl Chrome extension; local ID, not a REA Evidence ID'
    replace_item(evidence['items'], entry)
passive_path='research-2026-10-22/public-runtime/rea/passive-home.json'
if (ROOT/passive_path).exists():
    raw=read(passive_path);window=raw['normalized_result']['capture_window']
    replace_item(evidence['items'],dict(id=raw['evidence_id'],classification='A',origin='current',subject='source',kind='runtime',sourceIdentity='https://hark.com',capturedAt=None,
        captureInterval={'startedAt':window['started_at'],'completedAt':window['ended_at']},sharing='included',path=passive_path,sha256=sha(passive_path),provider='REA',
        method='Actual retained inspect_web_page result; policy_filtered/attach_limited sections and provider limits preserved. Pixel/state review is independently scoped by public-runtime/index.json.',
        review={'status':'reviewed','reviewedAt':NOW,'method':'Reviewed actual returned passive record, capture window and completeness; not an all-pixel/full-session assertion.'}))
for item in evidence['items']:
    if item.get('id') == 'hark-d-reference-contract':
        item.update(sha256=sha(item['path']), implementationStatus='implemented-and-tested',
                    method='Independent reference README updated after actual local run; see hark-20261022-d-reference-run.')
    if item.get('id') == 'hark-d-transfer-contract':
        item.update(sha256=sha(item['path']), method='D transfer guidance updated with actual local notes run; original behavior remains separately bounded.')
evidence['resumedRuntimeEntry'] = 'research-2026-10-22/authenticated-runtime/observations.json'
evidence['resumedAssetsEntry'] = 'research-2026-10-22/current-app/manifest.json'
evidence['updatedAt'] = NOW
write('research-2026-10-22/evidence-index.json', evidence)

assets = read('research-2026-10-22/assets-manifest.json')
byurl = {x['url']:x for x in fonts['items']}
for item in assets['items']:
    if item.get('sourceUrl') in byurl:
        f=byurl[item['sourceUrl']]
        item.update(sharing='included', path='research-2026-10-22/public/resources/'+f['path'],
                    bytes=f['bytes'], sha256=f['sha256'], usage=['Reuse original font file with the exact scope-specific CSS face binding.'])
        item.pop('reason', None)
assets['items'] = [x for x in assets['items'] if not x['id'].startswith('hark-asset-resumed-')]
def asset(id_, path, type_, refs, url=None, extra=None):
    p=ROOT/path
    result=dict(id='hark-asset-resumed-'+id_, type=type_, usage=['Original source bytes or explicitly identified source-derived resource; preserve scope and attribution.'],
                componentRefs=refs, sharing='included', path=path, bytes=p.stat().st_size, sha256=sha(path),
                license='not-established; local private research; no redistribution grant')
    if url: result['sourceUrl']=url
    if extra: result.update(extra)
    assets['items'].append(result)
for i,f in enumerate(app['entries']):
    if not f['path'].startswith(('resources/','fonts/')): continue
    ext=Path(f['path']).suffix
    type_={'.woff2':'font','.css':'stylesheet','.js':'script','.html':'source-document'}.get(ext,'resource')
    refs=['hark-component-sky'] if ext=='.html' else ['hark-component-app-navigation','hark-component-composer','hark-component-settings-group']
    if ext=='.woff2': refs=['hark-component-message','hark-component-code-card','hark-component-summary-card']
    asset('app-'+str(i),'research-2026-10-22/current-app/'+f['path'],type_,refs,
          extra={'evidenceRefs':['hark-20261022-b-current-app-resources']})
asset('named-glyphs','research-2026-10-22/current-app/named-glyph-sprite.svg','svg',
      ['hark-component-app-navigation','hark-component-menu','hark-component-composer'],
      extra={'evidenceRefs':['hark-20261022-b-current-app-icons'],'reuseMode':'source-derived sprite; preserve source mapping in icons.json'})
for i,f in enumerate(media['items']):
    if f.get('acquisition',{}).get('status')!='saved': continue
    asset('public-media-'+str(i),'research-2026-10-22/current-public-media/'+f['path'],f['kind'],
          ['hark-component-feature-carousel','hark-component-feature-section','hark-component-article'],f['url'],
          {'evidenceRefs':['hark-20261022-b-current-public-media'],'metadata':f.get('metadata',{}),
           'variantsEntry':'research-2026-10-22/current-public-media/manifest.json',
           'responseContentType':f.get('contentType'),'responseFormatMayDifferFromUrlExtension':True})
negotiated=read('research-2026-10-22/current-public-media/negotiated-representations.json')
for i,f in enumerate(negotiated['items']):
    asset('public-negotiated-'+str(i),'research-2026-10-22/current-public-media/'+f['path'],'image',
          ['hark-component-feature-carousel','hark-component-feature-section'],f['url'],
          {'evidenceRefs':['hark-20261022-b-current-public-media'],'metadata':f['metadata'],
           'requestHeaders':{'Accept':'image/webp'},'responseContentType':f['contentType'],
           'defaultResponseEntry':'research-2026-10-22/current-public-media/manifest.json'})
assets['relatedManifests']=['research-2026-10-22/current-app/manifest.json','research-2026-10-22/current-public-media/manifest.json','research-2026-10-22/asset-audit-resumed.json']
assets['acquisitionStatus']={'currentPublicDeclaredFonts':'19/19 original files','currentAppFonts':'21/21 original files; 15 byte-identical to public',
    'currentAppLiteralClosure':'256/256 URLs plus wallpaper HTML', 'publicMediaLocators':media['summary'],
    'negotiatedRepresentations':negotiated['summary'],
    'boundary':'Discovered static declarations and explicitly retained negotiated responses. Historical unavailable URLs and computed runtime URLs remain separate.'}
assets['updatedAt']=NOW
write('research-2026-10-22/assets-manifest.json',assets)

tokens=read('tokens/tokens.json')
facts=[('composer-width',512,'CSS px','1440×668, Home collapsed'),('composer-height',48,'CSS px','empty input'),
       ('composer-multiline-height',67,'CSS px','two-line neutral draft at desktop'),
       ('composer-text-size',15,'CSS px','desktop and 390px chat'),('composer-text-line-height',19,'CSS px','desktop and 390px chat'),
       ('summary-preview-width-desktop',424,'CSS px','1440px desktop'),('summary-preview-width-narrow',290,'CSS px','390px viewport'),
       ('summary-preview-height',181,'CSS px','current neutral sample'),('summary-preview-blur',50,'CSS px','preview backdrop-filter'),
       ('summary-read-width-desktop',720,'CSS px','1440×668'),('summary-read-title-size',40,'CSS px','desktop'),
       ('summary-read-title-line-height',52,'CSS px','desktop'),('summary-read-title-weight',500,'CSS font-weight','Hark Serif desktop/narrow'),
       ('summary-read-title-size-narrow',32,'CSS px','390×844'),('summary-read-title-line-height-narrow',41.6,'CSS px','390×844'),
       ('summary-read-body-size',18,'CSS px','desktop/narrow'),('summary-read-body-line-height',25.2,'CSS px','desktop/narrow'),
       ('settings-heading-size',32,'CSS px','1440×668'),('settings-heading-line-height',42.24,'CSS px','1440×668'),
       ('settings-row-width',548,'CSS px','1440×668'),('settings-row-height',56,'CSS px','generic settings row'),
       ('theme-radio-row-height',59.796875,'CSS px','15/19.8 text plus 20px vertical padding'),
       ('chat-inset-narrow',16,'CSS px','390px viewport')]
for role,value,unit,scope in facts:
    replace_item(tokens['items'],dict(id='hark-token-resumed-'+role, classification='A',coverage='verified',priority='core',
        evidenceRefs=['hark-20261022-a-authenticated-runtime'],unknowns=[],role=role,value=value,unit=unit,
        scope='Current authenticated App · '+scope,sourceEpoch='current-authenticated-2026-10-22',
        implementation={'status':'specified','classification':'D','decision':'Preserve CSS units and observed conditions; semantic name is a transfer alias.'}))
write('tokens/tokens.json',tokens)

covered=['app-navigation','home-panel','project-row','search','settings-group','setting-control','settings-form','account-row','menu','dialog','composer','code-card','summary-card']
components=read('components/catalog.json')
for item in components['items']:
    if item['id'] in ['hark-component-'+s for s in covered]:
        item['currentRuntime']={'status':'partial','evidenceRefs':['hark-20261022-a-authenticated-runtime'],
            'entry':'research-2026-10-22/authenticated-runtime/observations.json',
            'meaning':'Specified current representative states observed; entire component state/backend matrix not claimed.'}
    if item['id']=='hark-component-summary-card':
        item['currentGeometry']={'desktopPreview':[424,181],'narrowPreviewWidth':290,'desktopReadingWidth':720,
                                 'titleDesktop':'40/52/500 Hark Serif','titleNarrow':'32/41.6/500','body':'18/25.2 GT Standard L','classification':'A'}
        item['sourceMotionEntry']='research-2026-10-22/current-app/summary-static.json'
    if item['id']=='hark-component-sky':
        item['currentStaticEntry']='research-2026-10-22/current-app/sky-static.json'
    if item['id'] in ['hark-component-feature-carousel','hark-component-public-app-hero','hark-component-pricing','hark-component-accordion'] and (RUN/'public-runtime/index.json').exists():
        item['currentPublicRuntime']={'status':'partial','evidenceRefs':['hark-20261022-a-public-runtime'],'entry':'research-2026-10-22/public-runtime/index.json','meaning':'Only recorded foreground keyboard/narrow/reduced/pricing/FAQ and selected node font states; not all drag/media/backend branches.'}
write('components/catalog.json',components)

motion=read('motion/catalog.json')
replace_item(motion['items'],dict(id='hark-motion-resumed-summary-owner',classification='B',coverage='verified',priority='core',
    evidenceRefs=['hark-20261022-b-current-app-summary','hark-20261022-a-authenticated-runtime'],
    unknowns=['No fitted timing curve or full source reduced-motion/exit matrix from these samples.'],
    implementation={'status':'specified','classification':'D','decision':'Animate the outer reading host opacity/filter; retain the source 200ms easeOut declaration.'},
    trigger='summary preview activated',properties=['outerHost.opacity','outerHost.filter'],
    duration={'value':200,'unit':'ms','basis':'declared'},easing={'value':'easeOut','basis':'declared'},
    interruption=['Current open rAF samples show opacity/filter change; all overlapping close/reopen states not exhaustively measured.'],
    runtimeObservation={'selector':'._2za4zx8','firstOpacityZeroMs':53.2,'firstOpacityOneMs':306.1,
        'basis':'observed discrete click-relative samples; elapsed event time is not the declared duration or fitted easing'}))
write('motion/catalog.json',motion)

pkg=read('design-package.json')
pkg['source']['runtime']='Current public Next.js/Turbopack; authenticated compiled browser App/CSS/WebGL; independent reference HTML/CSS/JS'
pkg['source']['version']='public dpl_43vpACitAoHemg5zATXXCP8hBYHa; current App main-BgE0mqJD.js; captured 2026-10-22 UTC'
pkg['source']['researchMethod']='Historical REA retained; current public REA/CDP plus authorized Chrome extension authenticated UI, original asset archive and local independent reference. Actual methods and boundaries per evidence entry.'
if (ROOT/passive_path).exists() and raw['evidence_id'] not in pkg['source']['researchEvidenceRefs']:
    pkg['source']['researchEvidenceRefs'].append(raw['evidence_id'])
pkg['source']['authentication']={'requiredForCoreDesign':True,'status':'verified-existing-user-session','lastVerifiedAt':'2026-10-22T15:04:08.734Z',
    'evidenceRefs':['hark-20261022-a-authenticated-runtime'],'conditions':'Normal existing Chrome Hark session reused through connected extension; chat, projects and settings accessible; no cookies/tokens exported.'}
pkg['readiness']['status']='partial'
pkg['readiness']['supportedUses']=['Implement current 15/19 chat/composer, 424px summary preview and independent desktop/narrow reading layouts from current A and bound source B.',
    'Directly reuse archived scope-specific fonts, named SVG glyphs, shaders/LUT and 92 public media locators; preserve actual response MIME/encoding.',
    'Run the independent local notes reference with draft/search/save/cancel/retry/reading/theme checks; consult documented source differences.']
pkg['readiness']['coreGaps']=['Reference now reuses original Bold700 and Serif20/26/500 and aligns the reading panel top; extra category/title offset, toolbar, narrow padding, dialog motion and background/Auto remain explicit differences, so exact A/D equivalence remains partial.',
    'Current original UI representative states are observed; file/gallery/link-stack/reply/flow-error and complete focus/reduced-motion matrices are not all re-executed for this version.',
    'Per-glyph actual App fallback attribution and all weather/uniform/performance branches remain unmeasured; font files and computed family bindings alone do not prove them.']
pkg['readiness']['optionalGaps']=['No actual purchase, external account connection, feedback submission, security mutation, OS keyboard/touch, or full backend acceptance.','Static closure is complete for discovered literal allowed URLs, not computed URLs or global execution reachability.']
pkg['readiness']['originalResearchCompletion']='Representative deep study and declared-asset archive; residual current runtime coverage remains explicit, not a claim of every original business/state.'
pkg['validation']['reference']={'status':'partial','evidenceRefs':['hark-20261022-a-authenticated-runtime','hark-20261022-d-reference-run'],
    'description':'Independent reference 13 checks/8 reviewed captures passed; source/reference exact equivalence has documented differences.'}
pkg['validation']['crossDomain']={'status':'verified','evidenceRefs':['hark-20261022-d-transfer-run'],
    'description':'Independent local notes workflow executed; this D result does not verify original Hark behavior.'}
pkg['validation']['originalRuntime']={'status':'partial','evidenceRefs':['hark-20261022-a-authenticated-runtime'],
    'description':'Current authorized App observed at desktop/narrow, theme pair, draft navigation, search empty and summary open timing; residual states remain unknown.'}
if (RUN/'public-runtime/index.json').exists():
    pkg['validation']['originalRuntime']['evidenceRefs'].append('hark-20261022-a-public-runtime')
    pkg['validation']['originalRuntime']['description']+=' Public foreground keyboard/reversal/narrow/reduced carousel, pricing and FAQ are separately observed and reviewed.'
pkg['distribution']={'mode':'private','omissions':['Raw account receipts, private screenshots, profile, cookies and tokens withheld.','Original brand/font/media licenses are not a redistribution grant.','Historical external-clone originals retain their separate provenance and missing-byte boundaries.']}
pkg['taskDelivery']={'status':'deepening-with-explicit-residuals','userAcceptedPartial':False,
    'entry':'research-2026-10-22/README.md','resumption':'Local previous research context recovered',
    'meaning':'Session resumed through existing local context; prior authentication and declared-asset gaps closed. No global all-states completion claim.'}
pkg['updatedAt']=NOW
write('design-package.json',pkg)
print(json.dumps({'tokens':len(tokens['items']),'components':len(components['items']),'assets':len(assets['items']),
                  'evidence':len(evidence['items']),'appRawFiles':app['rawResourceFiles'],'media':media['summary']},ensure_ascii=False))
