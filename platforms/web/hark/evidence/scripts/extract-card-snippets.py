#!/usr/bin/env python3
"""Extract exact shipped-code spans. Offsets are UTF-8 bytes + Python Unicode code points, end-exclusive."""
from pathlib import Path
import hashlib, json
root=Path(__file__).resolve().parent
assets=root/'hark-assets/files'
out=root/'snippets'
main='main-DxHtaWdF.js'; bundle='BT2rDYKB.js'; route='BpSmQN_8.js'
selections=[
 ('cards-normalization',bundle,'const jO=()=>null','const $O='),
 ('cards-render-dispatch',bundle,'const Xge="list"','const nve='),
 ('links-preview-pipeline',bundle,'function epe(t)','function mpe(t)'),
 ('link-stack-counts-expansion',bundle,'function eP(e,t,n,r={})','function tP(e)'),
 ('link-stack-drag-flip',bundle,'function Uj(e,t)','const qj='),
 ('link-stack-grid-motion',bundle,'const qj=','function eP(e,t,n,r={})'),
 ('card-reply-context-expand',bundle,'function Fpe(t)','function $pe(t)'),
 ('card-code',bundle,'function $pe(t)','function Gpe(e)'),
 ('card-summary-modal',bundle,'function Gge(e)','function Kge(t)'),
 ('card-text-template',bundle,'function Kge(t)','const Xge="list"'),
 ('card-swipe-reply',bundle,'function Kpe(e)','function Jpe('),
 ('stream-reveal-buffer',bundle,'const h2={charsPerSecond:90','function k2(t,r)'),
 ('stream-sse-owner',bundle,'function Rre(e)','const Lre='),
 ('stream-message-patch-reducer',bundle,'class Jm{','function rg(e'),
 ('stream-records-compat',bundle,'function fg(e)','function mg(e)'),
 ('stream-feed-manager','DovrAg42.js','class v{','class m{'),
 ('stream-manager-link','DovrAg42.js','class g{','let b=null'),
 ('stream-tab-sync',main,'const yv=3','function xv(e'),
 ('scroll-pinning',bundle,'let vae=!1','function kae(e)'),
 ('stop-tool-optimistic',bundle,'async function AM(e)','const CM='),
 ('activity-phases',bundle,'function voe(e)','function Loe(e)'),
 ('website-artifact-preview',route,'function Mn(r)','function Fn(t)'),
 ('file-viewer-dispatch',main,'function CI(e)','function RI(e)'),
 ('markdown-component-map',bundle,'const p3={',',h3='),
 ('chat-markdown-config',bundle,'const D5={','const F5='),
 ('chat-live-text-preparation',bundle,'function a6(t)','function l6('),
 ('chat-text-rendering',bundle,'function n6(e)','function r6('),
 ('table-card-model',bundle,'function kme(t,r)','function Eme(e)'),
 ('html-file-preview',main,'function QC(e)','var HC='),
 ('activity-stop-confirmation',bundle,'const te=ee;let ne;','const ce=oe,ue=', 'txt'),
 ('api-base-url','Bz0NRe01.js','function Y(){','function G(){'),
 ('link-motion-preference',bundle,'const aj={','function vj(e,t,n)'),
]
records=[]
for name,filename,start,end,*extension in selections:
 ext=extension[0] if extension else "js"
 p=next(assets.rglob(filename));data=p.read_bytes();s=data.decode('utf-8');a=s.index(start);b=s.index(end,a+len(start))
 span=s[a:b];raw=span.encode('utf-8')
 (out/f'{name}.raw.{ext}').write_bytes(raw)
 meta={'evidence_type':'static shipped JavaScript span; execution not established','source_page_evidence_id':'ev_4d515902647d4a1d0224c3da682c34a39f9ed44ca7a978ac725a13eddf73e13f','source_export_evidence_id':'ev_f59cba60a4e30603b5f93e546735322e749383df1dc84d0c3d9c761afd214537','source_path':str(p.relative_to(root.parent.parent)),'source_filename':filename,'source_sha256':hashlib.sha256(data).hexdigest(),'source_bytes':len(data),'unicode_codepoint_offsets':{'start':a,'end_exclusive':b},'utf8_byte_offsets':{'start':len(s[:a].encode('utf-8')),'end_exclusive':len(s[:b].encode('utf-8'))},'snippet_sha256':hashlib.sha256(raw).hexdigest(),'snippet_bytes':len(raw),'start_marker':start,'next_marker':end,'raw_path':f'evidence/scripts/snippets/{name}.raw.{ext}','readable_path':f'evidence/scripts/snippets/{name}.readable.{ext}'}
 (out/f'{name}.meta.json').write_text(json.dumps(meta,indent=2,ensure_ascii=False)+'\n')
 (out/f'{name}.readable.{ext}').write_text('// Readability-only formatting of exact raw evidence. Source offsets/SHA: '+name+'.meta.json\n'+span+'\n')
 records.append({'id':name,**meta})
(out/'card-stream-snippet-index.json').write_text(json.dumps(records,indent=2,ensure_ascii=False)+'\n')
print(json.dumps({'snippet_count':len(records),'total_raw_bytes':sum(x['snippet_bytes'] for x in records),'index':str(out/'card-stream-snippet-index.json')},ensure_ascii=False))
