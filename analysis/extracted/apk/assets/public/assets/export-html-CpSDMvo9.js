import{r as T,j as g}from"./react-vendor-BlDV3J9L.js";import{M as R,i as W,c as X,d as Z,e as ee,f as te,g as oe,h as ne,l as ae,m as re,t as ie,n as j,j as se,k as le,p as de,o as ce,s as pe,q,u as I,v as D,w as me,x as fe,y as ue,z as he,A as be,B as F,D as ge,E as xe,F as ve,G as ke,H as ye}from"./index-BCeL5nAW.js";import{M as we,r as $e,a as je}from"./markdown-BLdtTxHf.js";const ze="https://mirasim.ai/r/share-page",Se="https://mirasim.ai/r/share-page-top",Me=`
:root,:root[data-theme=light]{--brand-mark-ink:#16181A;--brand-mark-accent:#2A58C0}
:root[data-theme=dark]{--brand-mark-ink:#F1EFEA;--brand-mark-accent:#7499F2}
.brandbar{display:flex;align-items:center;justify-content:space-between;flex-wrap:wrap;gap:16px 24px;
  margin:56px 0 72px;padding:18px 22px;border:1px solid var(--rule);border-radius:14px;background:var(--elev)}
.bb-id{display:flex;align-items:center;gap:14px;min-width:0}
.bb-mark{width:34px;height:34px;flex-shrink:0}
/* The mark's strokes are drawn with var() in a stroke attribute; restate them as rules so a
   browser that does not resolve var() there still paints them in the theme's colours. */
.mirasim-mark-ink{stroke:var(--brand-mark-ink)}
.mirasim-mark-accent{stroke:var(--brand-mark-accent)}
.bb-lines{display:flex;flex-direction:column;line-height:1.4}
.bb-en{font-size:14px;font-weight:600;color:var(--ink)}
.bb-zh{font-size:12px;color:var(--ink-3)}
.bb-dl{display:inline-flex;flex-direction:column;align-items:center;padding:9px 20px;border-radius:999px;
  background:var(--ink);color:var(--paper);text-decoration:none;line-height:1.3;transition:background .15s}
.bb-dl:hover{background:var(--accent)}
.bb-dl:focus-visible{outline:2px solid var(--accent);outline-offset:3px}
.bb-dl .bb-en{font-size:13px;color:inherit}
.bb-dl .bb-zh{font-size:11px;color:inherit;opacity:.72}
@media (max-width:560px){.brandbar{flex-direction:column;text-align:center}.bb-id{flex-direction:column;gap:10px}}
.brandtop{display:flex;align-items:center;justify-content:space-between;gap:16px;padding-top:16px;
  font-size:12px;line-height:20px;color:var(--ink-3)}
.brandtop+.masthead{padding-top:32px}
.bt-id{display:flex;align-items:center;gap:7px;min-width:0}
.bt-mark{width:14px;height:14px}
/* The closing bar's button, one size down */
.bt-dl{padding:3px 13px;border-radius:999px;background:var(--ink);color:var(--paper);font-weight:500;
  text-decoration:none;white-space:nowrap;transition:background .15s}
.bt-dl:hover{background:var(--accent)}
.bt-dl:focus-visible{outline:2px solid var(--accent);outline-offset:3px}
@media print{.brandbar,.brandtop{display:none}}
`,Ce=T(g.jsx(R,{size:34,className:"bb-mark"})),Ee=T(g.jsx(R,{size:14,className:"bt-mark"})),Te=`<div class="brandtop" lang="en">
<span class="bt-id">${Ee}<span>Made with Mirasim</span></span>
<a class="bt-dl" href="${Se}" target="_blank" rel="noopener noreferrer">Download</a>
</div>`,Ae=`<footer class="brandbar">
<div class="bb-id">${Ce}<div class="bb-lines"><span class="bb-en" lang="en">Powered by Mirasim</span><span class="bb-zh" lang="zh-CN">由 Mirasim 提供支持</span></div></div>
<a class="bb-dl" href="${ze}" target="_blank" rel="noopener noreferrer"><span class="bb-en" lang="en">Download Mirasim</span><span class="bb-zh" lang="zh-CN">下载 Mirasim</span></a>
</footer>`,Le=new Uint32Array([1116352408,1899447441,3049323471,3921009573,961987163,1508970993,2453635748,2870763221,3624381080,310598401,607225278,1426881987,1925078388,2162078206,2614888103,3248222580,3835390401,4022224774,264347078,604807628,770255983,1249150122,1555081692,1996064986,2554220882,2821834349,2952996808,3210313671,3336571891,3584528711,113926993,338241895,666307205,773529912,1294757372,1396182291,1695183700,1986661051,2177026350,2456956037,2730485921,2820302411,3259730800,3345764771,3516065817,3600352804,4094571909,275423344,430227734,506948616,659060556,883997877,958139571,1322822218,1537002063,1747873779,1955562222,2024104815,2227730452,2361852424,2428436474,2756734187,3204031479,3329325298]),x=(e,t)=>e>>>t|e<<32-t;function _e(e){const t=new TextEncoder().encode(e),o=new Uint8Array(t.length+9+63&-64);o.set(t),o[t.length]=128;const n=new DataView(o.buffer),r=t.length*8;n.setUint32(o.length-8,Math.floor(r/4294967296),!1),n.setUint32(o.length-4,r>>>0,!1);const a=new Uint32Array([1779033703,3144134277,1013904242,2773480762,1359893119,2600822924,528734635,1541459225]),l=new Uint32Array(64);for(let i=0;i<o.length;i+=64){for(let h=0;h<16;h++)l[h]=n.getUint32(i+h*4,!1);for(let h=16;h<64;h++){const y=l[h-15],w=l[h-2],S=x(y,7)^x(y,18)^y>>>3,C=x(w,17)^x(w,19)^w>>>10;l[h]=l[h-16]+S+l[h-7]+C>>>0}let p=a[0],c=a[1],u=a[2],v=a[3],b=a[4],k=a[5],z=a[6],M=a[7];for(let h=0;h<64;h++){const y=x(b,6)^x(b,11)^x(b,25),w=b&k^~b&z,S=M+y+w+Le[h]+l[h]>>>0,C=x(p,2)^x(p,13)^x(p,22),Y=p&c^p&u^c&u,Q=C+Y>>>0;M=z,z=k,k=b,b=v+S>>>0,v=u,u=c,c=p,p=S+Q>>>0}a[0]=a[0]+p>>>0,a[1]=a[1]+c>>>0,a[2]=a[2]+u>>>0,a[3]=a[3]+v>>>0,a[4]=a[4]+b>>>0,a[5]=a[5]+k>>>0,a[6]=a[6]+z>>>0,a[7]=a[7]+M>>>0}const d=new Uint8Array(32),f=new DataView(d.buffer);for(let i=0;i<8;i++)f.setUint32(i*4,a[i],!1);let m="";for(const i of d)m+=String.fromCharCode(i);return btoa(m)}const L=e=>`'sha256-${_e(e)}'`;function Be(e,t){const o=["'self'","file:","blob:"];return[["default-src","'none'"],["script-src",e.map(L).join(" ")],["style-src",t.map(L).join(" ")],["img-src",[...o,"data:"].join(" ")],["media-src",o.join(" ")],["frame-src",o.join(" ")],["font-src","'none'"],["connect-src","'none'"],["object-src","'none'"],["base-uri","'none'"],["form-action","'none'"]]}function Ne(e,t){return Be(e,t).map(([o,n])=>`${o} ${n}`).join("; ")}function Re(e,t){return`<meta http-equiv="Content-Security-Policy" content="${Ne(e,t)}" />`}function lt(e){return e==="image"||e==="video"||e==="audio"||e==="pdf"||e==="office"||e==="binary"}function H(e){return e.path||e.fileName||""}function dt(e){if(W(e))return{kind:"image"};if(X(e))return{kind:"video"};if(Z(e))return{kind:"audio"};if(ee(e))return{kind:"pdf"};if(te(e))return{kind:"office"};if(oe(e))return{kind:"csv"};if(ne(e))return{kind:"markdown"};const t=ae(e);return t?{kind:"code",lang:t}:{kind:"text"}}function ct(e){const t=[],o=re(e);for(const[n,r]of e.entries()){const a=ie(r),l=o[n],d=l?.continues&&t.at(-1)?.snap===e[l.message.headIndex]?t.at(-1):void 0;if(d){d.rounds.push(r),a&&d.ids.push(a),d.at=r.updatedAt??d.at,r.error&&(d.failed=!0,d.error=j(r.error));continue}if(!a)continue;const f=r.prompt??"";if(se(r)||le(f))continue;const{text:m,paths:i}=de(f),p=j(m);!p||ce(p)||t.push({n:t.length+1,taskId:a,snap:r,rounds:[r],ids:[a],question:m.trim(),paths:i,imageRefs:(r.images??[]).map(c=>c.ref),at:r.updatedAt??r.startedAt??null,failed:!!r.error,error:r.error?j(r.error):null})}return t}const _=new Set,qe={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"};function s(e){return e.replace(/[&<>"']/g,t=>qe[t])}function pt(e){return(e||"").trim().toLowerCase().replace(/[^\p{L}\p{N}]+/gu,"-").replace(/^-+|-+$/g,"").slice(0,60)||"conversation"}function mt(e,t,o=72){const n=(e??"").trim();if(n)return n.length>o?`${n.slice(0,o).trimEnd()}…`:n;const r=(t??"").split(`
`).map(l=>l.trim()).find(l=>l)??"",a=j(r);return a?a.length>o?`${a.slice(0,o).trimEnd()}…`:a:""}const Ie="noindex,nofollow,noarchive,nosnippet";function De(e,t=200){const o=E(e.turns[0]?.question??"",120);let n="";for(let a=e.turns.length-1;a>=0;a--){const l=e.turns[a].rounds.map(d=>(d.text??"").trim()).filter(Boolean).at(-1)??"";if(l){n=E(l,120);break}}const r=[o,n].filter(Boolean).join("; ");return r.length>t?`${r.slice(0,t).trimEnd()}…`:r}function ft(e,t){const o=(e||"file").split(/[\\/]/).pop()||"file",n=o.lastIndexOf("."),a=(n>0?o.slice(n+1):"").replace(/[^\p{L}\p{N}]/gu,"").slice(0,8).toLowerCase(),l=(n>0?o.slice(0,n):o).replace(/[^\p{L}\p{N}._-]+/gu,"-").replace(/^[-.]+|[-.]+$/g,"").slice(0,48)||"file";let d=a?`${l}.${a}`:l,f=1;for(;t.has(d.toLowerCase());)d=a?`${l}-${f}.${a}`:`${l}-${f}`,f++;return t.add(d.toLowerCase()),d}const Fe={h1:({children:e})=>g.jsx("h1",{children:e}),h2:({children:e})=>g.jsx("h2",{children:e}),h3:({children:e})=>g.jsx("h3",{children:e}),h4:({children:e})=>g.jsx("h4",{children:e}),a:({href:e,children:t})=>g.jsx("a",{href:typeof e=="string"?e:void 0,target:"_blank",rel:"noopener noreferrer",children:t}),pre:({children:e})=>g.jsx(g.Fragment,{children:e}),code:({className:e,children:t})=>{const o=String(t??"").replace(/\n$/,""),n=/language-([\w-]+)/.exec(e||"");if(!n&&!e&&!o.includes(`
`))return g.jsx("code",{className:"inline",children:o});const r=F(o,n?.[1]??"");return g.jsx("pre",{children:g.jsx("code",{className:"hljs",dangerouslySetInnerHTML:{__html:r?r.html:s(o)}})})}};function P(e){return T(g.jsx(we,{remarkPlugins:[$e,je],components:Fe,children:e}))}function He(e,t){const o=t?F(e,t):null;return`<pre class="code"><code class="hljs">${o?o.html:s(e)}</code></pre>`}function Pe(e,t){const{rows:o,truncated:n}=ke(e,ye(t),500);if(!o.length)return`<pre class="code">${s(e)}</pre>`;const[r,...a]=o,l=`<thead><tr>${r.map(m=>`<th>${s(m)}</th>`).join("")}</tr></thead>`,d=`<tbody>${a.map(m=>`<tr>${m.map(i=>`<td>${s(i)}</td>`).join("")}</tr>`).join("")}</tbody>`;return`<div class="table-wrap"><table class="data">${l}${d}</table></div>${n?'<div class="ex-note">first 500 rows shown</div>':""}`}const U=new Intl.DateTimeFormat("en-US",{year:"numeric",month:"long",day:"numeric",hour:"2-digit",minute:"2-digit",hour12:!1}),O=new Intl.DateTimeFormat("en-US",{month:"short",day:"numeric",hour:"2-digit",minute:"2-digit",hour12:!1});function Ue(e){const t=e.split(`
`);for(let o=t.length-1;o>=0;o--){const n=t[o].trim();if(n)return n}return""}function E(e,t=96){const o=j(e);return o.length>t?`${o.slice(0,t).trimEnd()}…`:o}function Oe(e){return`<span class="chip">${e.kind==="dir"?"▸":"▪"} ${s(e.name)}</span>`}function A(e,t,o,n){const r=n?.count?`<span class="fd-count">${s(n.count)}</span>`:"";return`<details class="fold ${n?.extraClass??""}"${n?.open?" open":""}><summary><span class="fd-kind">${s(e)}</span><span class="fd-preview">${t}</span>${r}</summary><div class="fd-body">${o}</div></details>`}function G(e){return e.stat?` <span class="dstat"><span class="dadd">+${e.stat.added}</span> <span class="ddel">-${e.stat.removed}</span></span>`:""}function Ge(e,t){const o=t?.input??e.input??null,n=t?.result??e.result??null,r=e.status==="failed"?' <span class="failed-tag">failed</span>':"",a=o?ge(e.name,o):null,l=o&&!a?xe(e.name,o):null,d=a?`<pre class="io in">${s(a.command)}</pre>`+(a.meta.length?`<div class="submeta">${s(a.meta.map(m=>`${m.label} ${m.value}`).join("  |  "))}</div>`:""):l?`<pre class="io in">${s(ve(l))}</pre>`:o?`<pre class="io in">${s(o)}</pre>`:"",f=n?`<pre class="io out">${s(n)}</pre>`:"";return`<div class="call"><div class="call-sum">${s(e.summary||e.name)}${G(e)}${r}</div>${d}${f}</div>`}function Ke(e,t){if(!e.length)return"";const o=e[e.length-1],n=e.length-1,r=e.map(a=>Ge(a,t.get(a.id))).join("");return A("Tools",s(o.summary||o.name)+G(o),r,{count:n>0?`+${n}`:void 0,extraClass:"tools"})}function Ve(e){const{label:t,model:o}=ue(e,"Sub-agent"),n=(m,i)=>`<div class="subpart"><span class="sublabel">${m}</span>${i}</div>`,r=e.prompt?n("Brief",`<pre class="io in">${s(e.prompt)}</pre>`):"",a=e.tools.length?n("Trail",`<div class="subtools">${e.tools.map(m=>`<div class="subtool">${s(m.summary||m.name)}</div>`).join("")}</div>`):"",l=e.report?n("Result",`<pre class="io out">${s(e.report)}</pre>`):"",d=[o,e.tools.length&&`${e.tools.length} calls`,e.totalTokens&&`${he(e.totalTokens)} tok`,e.durationMs&&be(e.durationMs)].filter(Boolean).join(" · "),f=e.kind?.trim()?`<div class="submeta">${s(t)}</div>`:"";return A("Sub-agent",s(e.description||t),r+a+l+f,{count:d||void 0,extraClass:"agent"})}function Je(e){return`<a class="ex-open" href="${s(e)}" download target="_blank" rel="noopener">Open ↗</a>`}function Ye(e,t){const o=s(e.fileName||e.path||"artifact");if(!t)return`<div class="ex-note">${s(e.path||e.fileName||"")}</div>`;const n=t.assetPath?s(t.assetPath):"";switch(t.kind){case"image":return n?`<img class="ex-img zoomable" src="${n}" alt="${o}" loading="lazy" />`:`<div class="ex-note">${s(t.note||"image unavailable")}</div>`;case"video":return n?`<video class="ex-media" src="${n}" controls preload="metadata"></video>`:`<div class="ex-note">${s(t.note||"video unavailable")}</div>`;case"audio":return n?`<audio class="ex-audio" src="${n}" controls preload="metadata"></audio>`:`<div class="ex-note">${s(t.note||"audio unavailable")}</div>`;case"pdf":return n?`<iframe class="ex-pdf" src="${n}" title="${o}" loading="lazy"></iframe>`:`<div class="ex-note">${s(t.note||"pdf unavailable")}</div>`;case"office":case"binary":return n?`<a class="ex-file" href="${n}" download target="_blank" rel="noopener"><span class="ex-file-ic">▤</span><span class="ex-file-name">${o}</span><span class="ex-file-hint">download</span></a>`:`<div class="ex-note">${s(t.note||e.path||e.fileName||"")}</div>`;case"markdown":return t.text!=null?`<div class="md">${P(t.text)}</div>`:`<div class="ex-note">${s(t.note||"")}</div>`;case"csv":return t.text!=null?Pe(t.text,e.fileName||e.path||""):`<div class="ex-note">${s(t.note||"")}</div>`;case"code":case"text":return t.text!=null?He(t.text,t.lang):`<div class="ex-note">${s(t.note||"")}</div>`;default:return`<div class="ex-note">${s(t.note||"")}</div>`}}function B(e,t){const o=t.get(H(e)),n=s(e.fileName||e.path||"artifact"),r=e.caption?`<span class="ex-cap">${s(e.caption)}</span>`:"",a=o?.assetPath?Je(o.assetPath):"";return`<figure class="exhibit${o?` ex-${o.kind}`:""}"><figcaption><span class="ex-label">Exhibit</span><span class="ex-name">${n}</span>${r}<span class="ex-sp"></span>${a}</figcaption><div class="ex-body">${Ye(e,o)}</div></figure>`}function Qe(e,t){const o=e.rounds.flatMap(n=>We(n,t));if(e.failed&&e.error){const n=e.rounds.some(r=>r.text?.trim())?"⚠ ":"";o.push(`<div class="failed">${n}${s(e.error)}</div>`)}return o.join(`
`)||'<div class="empty">No response.</div>'}function We(e,t){const o=e.text??"",n=e.reasoning??"",r=new Map((e.toolCalls??[]).map(i=>[i.id,i])),a=new Map((e.subAgents??[]).map(i=>[i.id,i])),l=q(e.subAgents??[]),{byCall:d,trailing:f}=I(e),m=[];for(const i of D(e))if(i.kind==="text"){const p=o.slice(i.from,i.to);p.trim()&&m.push(`<div class="md">${P(p)}</div>`)}else if(i.kind==="thinking"){const p=n.slice(i.from,i.to).trim();p&&m.push(A("Thinking",s(Ue(p)),`<div class="thinking-body">${s(p)}</div>`,{extraClass:"thinking"}))}else if(i.kind==="agents")for(const p of i.ids){const c=a.get(p);c&&m.push(Ve(c))}else if(i.kind==="tools"){const p=i.ids.map(u=>r.get(u)).filter(u=>!!u&&!l.has(u.id));p.length&&m.push(Ke(p,t.toolDetails));const c=i.ids.flatMap(u=>d.get(u)??[]);for(const u of c)m.push(B(u,t.artifacts))}for(const i of f)m.push(B(i,t.artifacts));return m}function Xe(e,t){const o=r=>`<span class="tok">${s(r)}</span>`,n=r=>me(r).map(a=>a.kind==="session"?o(a.byTitle?`@${a.id}`:`@session:${a.id}`):fe(a.text).map(l=>l.kind==="text"?s(l.text):o(l.text)).join("")).join("");return pe(e,t?.skills??_,t?.commands??_).map(r=>r.kind==="text"?n(r.text):o(`/${r.name}`)).join("")}function Ze(e,t){const o=e.at?`<time>${s(O.format(e.at))}</time>`:"",n=e.paths.length?`<div class="chips">${e.paths.map(Oe).join("")}</div>`:"",r=e.imageRefs.map(d=>t.images.get(d)).filter(d=>!!d).map(d=>`<img class="pasted zoomable" src="${s(d)}" alt="attached image" loading="lazy" />`).join(""),a=e.question?`<div class="prompt-body">${Xe(e.question,t.marks)}</div>`:"",l=String(e.n).padStart(2,"0");return`
  <section class="round" id="r${e.n}">
    <div class="round-head"><span class="round-num">${l}</span><span class="round-rule"></span>${o}</div>
    <div class="prompt">
      <div class="speaker">Prompt</div>
      ${a}
      ${n}
      ${r?`<div class="imgs">${r}</div>`:""}
    </div>
    <div class="response">
      <div class="speaker accent">${s(t.agentLabel)}</div>
      ${Qe(e,t)}
    </div>
  </section>`}const K=`
*{box-sizing:border-box}
:root,:root[data-theme=light]{
  --paper:#faf9f6;--ink:#20201d;--ink-2:#47443f;--ink-3:#6d6960;--faint:#9c968b;
  --rule:#e7e3da;--rule-2:#f0ede6;--panel:#f3f0ea;--code-bg:#f4f1ea;--elev:#fffefb;
  --accent:#3f4c72;--link:#3f4c72;--err:#a3402f;--ok:#2f6b45;--shadow:0 1px 2px rgba(20,18,14,.05),0 12px 32px -18px rgba(20,18,14,.28);
  --hl-comment:#8a8577;--hl-key:#a3402f;--hl-str:#3f6b52;--hl-title:#5a4b8a;--hl-num:#3f4c72;--hl-meta:#9a5a1e;
}
:root[data-theme=dark]{
  --paper:#1a1917;--ink:#eceae3;--ink-2:#c2beb5;--ink-3:#938e84;--faint:#6f6a60;
  --rule:#2d2b26;--rule-2:#242320;--panel:#211f1c;--code-bg:#201e1b;--elev:#222019;
  --accent:#9aa6ce;--link:#a7b2d6;--err:#e79184;--ok:#8ecfa4;--shadow:0 1px 2px rgba(0,0,0,.3),0 16px 40px -20px rgba(0,0,0,.6);
  --hl-comment:#8b867a;--hl-key:#e79184;--hl-str:#9fce9c;--hl-title:#c8b6f2;--hl-num:#9aa6ce;--hl-meta:#e0a56a;
}
:root{--serif:"Iowan Old Style","Palatino Linotype",Palatino,"Book Antiqua",Georgia,"Songti SC",ui-serif,serif;
  --sans:-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue","PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;
  --mono:"SF Mono",ui-monospace,SFMono-Regular,Menlo,Consolas,"Liberation Mono",monospace;color-scheme:light dark}
html{-webkit-text-size-adjust:100%;scroll-behavior:smooth}
body{margin:0;background:var(--paper);color:var(--ink-2);font-family:var(--sans);
  font-size:15.5px;line-height:1.75;-webkit-font-smoothing:antialiased;text-rendering:optimizeLegibility}
::selection{background:color-mix(in oklab,var(--accent) 24%,transparent)}
a{color:var(--link)}
/* Reading-progress bar (fills with scroll) + fixed accent hairline under it */
.progress{position:fixed;top:0;left:0;height:3px;width:0;background:var(--accent);z-index:70;transition:width .08s linear}
/* Sticky chrome: appears once the masthead scrolls away */
.chrome{position:fixed;top:0;left:0;right:0;z-index:60;display:flex;align-items:center;gap:12px;
  padding:9px clamp(16px,4vw,40px);background:color-mix(in oklab,var(--paper) 86%,transparent);
  backdrop-filter:saturate(1.4) blur(10px);-webkit-backdrop-filter:saturate(1.4) blur(10px);
  border-bottom:1px solid var(--rule);transform:translateY(-102%);transition:transform .22s ease;opacity:0}
.chrome.show{transform:none;opacity:1}
.chrome .ch-title{flex:1;min-width:0;font-family:var(--serif);font-size:15px;font-weight:600;color:var(--ink);
  overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
.rail{width:min(1000px,92vw);margin:0 auto;padding:0 clamp(24px,4vw,60px)}
@media (min-width:1600px){.rail{width:min(1120px,82vw)}}
.topbar{height:3px;background:var(--accent);opacity:.85}
/* Buttons (shared: chrome + controls) */
.btn{font-family:var(--mono);font-size:10.5px;letter-spacing:.1em;text-transform:uppercase;color:var(--ink-3);
  background:transparent;border:1px solid var(--rule);border-radius:999px;padding:6px 13px;cursor:pointer;transition:.15s;
  display:inline-flex;align-items:center;gap:6px;white-space:nowrap;line-height:1}
.btn:hover{color:var(--ink);border-color:var(--faint);background:var(--panel)}
.btn svg{width:13px;height:13px}
/* Masthead */
.masthead{padding:60px 0 30px;margin-bottom:8px;border-bottom:1px solid var(--rule)}
.eyebrow{font-family:var(--mono);font-size:11px;letter-spacing:.22em;text-transform:uppercase;color:var(--accent);margin-bottom:20px}
.doc-title{font-family:var(--serif);font-weight:600;font-size:clamp(27px,4vw,40px);line-height:1.14;letter-spacing:-.015em;color:var(--ink);margin:0 0 26px;text-wrap:balance}
.meta{display:grid;grid-template-columns:repeat(auto-fit,minmax(120px,1fr));gap:18px 28px;margin:0}
.meta div{margin:0}
.meta dt{font-family:var(--mono);font-size:10px;letter-spacing:.14em;text-transform:uppercase;color:var(--faint);margin-bottom:4px}
.meta dd{margin:0;font-size:14px;color:var(--ink);font-variant-numeric:tabular-nums}
.controls{margin-top:26px;display:flex;gap:10px;flex-wrap:wrap}
/* Contents */
.contents{padding:26px 0 8px;border-bottom:1px solid var(--rule)}
.contents .eyebrow{color:var(--faint);margin-bottom:16px}
.contents ol{list-style:none;margin:0;padding:0}
.contents li{margin:0}
.contents a{display:flex;gap:16px;align-items:baseline;padding:7px 10px;margin:0 -10px;text-decoration:none;color:var(--ink-2);
  border-top:1px solid var(--rule-2);border-radius:6px;transition:background .12s,color .12s}
.contents li:first-child a{border-top:0}
.contents a:hover{color:var(--accent);background:var(--panel)}
.contents a.active{color:var(--accent);background:color-mix(in oklab,var(--accent) 9%,transparent)}
.contents a.active .c-num{color:var(--accent)}
.c-num{font-family:var(--mono);font-size:11px;color:var(--faint);flex-shrink:0;width:22px;font-variant-numeric:tabular-nums}
.c-q{flex:1;font-size:14px;line-height:1.5;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
/* Rounds */
main{padding-top:8px}
.round{padding:44px 0;border-bottom:1px solid var(--rule);scroll-margin-top:60px}
.round:last-of-type{border-bottom:0}
.round-head{display:flex;align-items:center;gap:16px;margin-bottom:22px}
.round-num{font-family:var(--serif);font-size:30px;font-weight:500;color:var(--faint);line-height:1;font-variant-numeric:tabular-nums}
.round-rule{flex:1;height:1px;background:var(--rule)}
.round-head time{font-family:var(--mono);font-size:11px;color:var(--faint);letter-spacing:.03em}
.speaker{font-family:var(--mono);font-size:10.5px;letter-spacing:.16em;text-transform:uppercase;color:var(--ink-3);margin-bottom:12px}
.speaker.accent{color:var(--accent)}
.prompt{margin-bottom:30px}
.prompt-body{font-family:var(--serif);font-size:20px;line-height:1.5;color:var(--ink);white-space:pre-wrap;word-break:break-word;
  padding-left:20px;border-left:2px solid var(--accent)}
/* A marked token in the lede (see renderMarkedPrompt). ONE treatment for all four kinds: the
   app tells them apart with icons, and a printed page has none to spend — what a reader needs
   here is only "this names something outside the sentence". Sized in em so it tracks the serif
   lede it sits in, and tinted off --accent so it holds in both themes. */
.tok{font-family:var(--mono);font-size:.62em;white-space:nowrap;padding:.16em .45em;border-radius:5px;
  vertical-align:.09em;background:color-mix(in oklab,var(--accent) 12%,transparent);color:var(--accent)}
.chips{margin:14px 0 0 22px;display:flex;flex-wrap:wrap;gap:8px}
.chip{font-family:var(--mono);font-size:11px;color:var(--ink-3);background:var(--panel);border:1px solid var(--rule);border-radius:5px;padding:3px 9px}
.imgs{margin:14px 0 0 22px;display:flex;flex-wrap:wrap;gap:10px}
.imgs img.pasted{max-width:min(100%,380px);max-height:340px;border-radius:6px;border:1px solid var(--rule)}
img.zoomable{cursor:zoom-in}
/* Disclosures */
.fold{margin:14px 0}
.fold>summary{cursor:pointer;list-style:none;display:flex;align-items:baseline;gap:12px;padding:7px 0;
  border-top:1px solid var(--rule-2);border-bottom:1px solid var(--rule-2)}
.fold[open]>summary{border-bottom-color:transparent}
.fold>summary::-webkit-details-marker{display:none}
.fold>summary::before{content:"+";font-family:var(--mono);font-size:13px;color:var(--faint);flex-shrink:0;width:12px;line-height:1.4}
.fold[open]>summary::before{content:"–"}
.fd-kind{font-family:var(--mono);font-size:10px;letter-spacing:.14em;text-transform:uppercase;color:var(--ink-3);flex-shrink:0;line-height:1.7}
.fold.thinking>summary .fd-kind{color:var(--accent)}
.fd-preview{flex:1;min-width:0;font-family:var(--mono);font-size:11.5px;color:var(--faint);
  overflow:hidden;text-overflow:ellipsis;white-space:nowrap;line-height:1.7}
.fd-count{flex-shrink:0;font-family:var(--mono);font-size:10.5px;color:var(--faint)}
.dstat{font-family:var(--mono);font-size:10.5px;font-variant-numeric:tabular-nums;white-space:nowrap}
.dadd{color:var(--ok)}.ddel{color:var(--err)}
.fd-body{margin:2px 0 6px;padding:10px 0 6px 16px;border-left:1px solid var(--rule)}
.thinking-body{white-space:pre-wrap;word-break:break-word;color:var(--ink-3);font-size:13.5px;line-height:1.7;font-style:italic}
.call{padding:8px 0}
.call+.call{border-top:1px dashed var(--rule-2)}
.call-sum{font-family:var(--mono);font-size:12px;color:var(--ink-2);margin-bottom:5px;word-break:break-word}
.failed-tag{color:var(--err);font-size:10px;text-transform:uppercase;letter-spacing:.1em;margin-left:6px}
.io{white-space:pre-wrap;word-break:break-word;font-family:var(--mono);font-size:11.5px;line-height:1.6;
  background:var(--code-bg);border:1px solid var(--rule-2);border-radius:6px;padding:8px 11px;margin:4px 0;max-height:440px;overflow:auto}
.io.in{color:var(--ink-3)}.io.out{color:var(--ink-2)}
.subtool{font-family:var(--mono);font-size:11.5px;line-height:1.7;color:var(--ink-3);padding-left:12px;position:relative}
.subtool::before{content:"·";position:absolute;left:0;color:var(--faint)}
.subpart+.subpart{margin-top:10px}
.sublabel{display:block;font-family:var(--mono);font-size:9.5px;text-transform:uppercase;letter-spacing:.08em;color:var(--faint);margin-bottom:3px}
.submeta{margin-top:8px;font-family:var(--mono);font-size:10.5px;color:var(--faint);letter-spacing:.02em}
.failed{color:var(--err);font-size:14px;margin:10px 0;padding-left:14px;border-left:2px solid var(--err)}
.empty{color:var(--faint);font-style:italic}
/* Exhibits (delivered artifacts) */
.exhibit{margin:20px 0;border:1px solid var(--rule);border-radius:10px;overflow:hidden;background:var(--elev);box-shadow:var(--shadow)}
.exhibit>figcaption{display:flex;align-items:baseline;gap:10px;padding:9px 14px;background:var(--panel);border-bottom:1px solid var(--rule)}
.ex-label{font-family:var(--mono);font-size:9.5px;letter-spacing:.16em;text-transform:uppercase;color:var(--faint);flex-shrink:0}
.ex-name{font-family:var(--mono);font-size:12px;color:var(--ink);font-weight:600;word-break:break-all}
.ex-cap{color:var(--ink-3);font-size:12.5px}
.ex-sp{flex:1}
.ex-open{font-family:var(--mono);font-size:10px;letter-spacing:.08em;text-transform:uppercase;color:var(--ink-3);text-decoration:none;
  border:1px solid var(--rule);border-radius:999px;padding:3px 9px;flex-shrink:0;transition:.15s}
.ex-open:hover{color:var(--accent);border-color:var(--accent)}
.ex-body{padding:16px}
.exhibit.ex-image>.ex-body,.exhibit.ex-video>.ex-body{padding:0;background:#0c0c0e}
.ex-img{max-width:100%;display:block;margin:0 auto}
.ex-media{width:100%;max-height:70vh;display:block;background:#000}
.ex-audio{width:100%}
.ex-pdf{width:100%;height:min(78vh,820px);border:0;display:block;background:var(--paper)}
.ex-file{display:flex;align-items:center;gap:12px;text-decoration:none;color:var(--ink);padding:6px 4px}
.ex-file-ic{font-size:22px;color:var(--faint)}
.ex-file-name{flex:1;font-family:var(--mono);font-size:13px;word-break:break-all}
.ex-file-hint{font-family:var(--mono);font-size:10px;letter-spacing:.1em;text-transform:uppercase;color:var(--ink-3);border:1px solid var(--rule);border-radius:999px;padding:4px 11px}
.ex-file:hover .ex-file-hint{color:var(--accent);border-color:var(--accent)}
.ex-note{font-family:var(--mono);font-size:11.5px;color:var(--faint)}
.ex-body>.md>:first-child,.ex-body>.code{margin-top:0}
.ex-body>.md>:last-child{margin-bottom:0}
.table-wrap{overflow-x:auto;border:1px solid var(--rule);border-radius:8px}
table.data{border-collapse:collapse;width:100%;font-size:12.5px;font-family:var(--mono)}
table.data th,table.data td{border-bottom:1px solid var(--rule-2);border-right:1px solid var(--rule-2);padding:6px 10px;text-align:left;white-space:nowrap}
table.data th{background:var(--panel);color:var(--ink);position:sticky;top:0;font-weight:600}
table.data tr:hover td{background:color-mix(in oklab,var(--accent) 6%,transparent)}
/* Prose */
.md{color:var(--ink-2);font-size:15.5px;line-height:1.78}
.md>:first-child{margin-top:0}.md>:last-child{margin-bottom:0}
.md+.md{margin-top:.75em}
.md h1,.md h2,.md h3,.md h4{font-family:var(--serif);color:var(--ink);font-weight:600;line-height:1.25;letter-spacing:-.01em}
.md h1{font-size:24px;margin:1.4em 0 .5em}.md h2{font-size:20px;margin:1.4em 0 .5em}
.md h3{font-size:17px;margin:1.3em 0 .4em}.md h4{font-size:15px;margin:1.2em 0 .4em}
.md p{margin:.75em 0}
.md ul,.md ol{margin:.75em 0;padding-left:1.5em}
.md li{margin:.3em 0}
.md a{color:var(--link);text-decoration:none;border-bottom:1px solid color-mix(in oklab,var(--link) 35%,transparent)}
.md a:hover{border-bottom-color:var(--link)}
.md blockquote{margin:1em 0;padding:.3em 0 .3em 1.1em;border-left:2px solid var(--rule);color:var(--ink-3);font-style:italic}
.md hr{border:0;border-top:1px solid var(--rule);margin:1.8em 0}
.md code.inline{font-family:var(--mono);font-size:.86em;background:var(--code-bg);border:1px solid var(--rule);border-radius:4px;padding:1px 5px}
.md pre,pre.code{position:relative;background:var(--code-bg);border:1px solid var(--rule);border-radius:8px;padding:14px 16px;overflow-x:auto;margin:1.1em 0}
.md pre code,pre.code code{font-family:var(--mono);font-size:12.5px;line-height:1.65;background:none;border:0;padding:0}
.md table{border-collapse:collapse;width:100%;margin:1.1em 0;font-size:13.5px;display:block;overflow-x:auto}
.md th,.md td{border:1px solid var(--rule);padding:7px 12px;text-align:left}
.md th{background:var(--panel);font-weight:600;color:var(--ink)}
.md img{max-width:100%;border-radius:6px}
/* Copy-code button (injected by JS onto <pre> blocks) */
.copy-btn{position:absolute;top:7px;right:7px;font-family:var(--mono);font-size:9.5px;letter-spacing:.08em;text-transform:uppercase;
  color:var(--ink-3);background:var(--paper);border:1px solid var(--rule);border-radius:6px;padding:3px 8px;cursor:pointer;opacity:0;transition:.14s}
pre:hover .copy-btn{opacity:1}
.copy-btn:hover{color:var(--accent);border-color:var(--accent)}
.copy-btn.done{color:var(--hl-str);border-color:var(--hl-str)}
/* Lightbox (injected by JS) */
.lightbox{position:fixed;inset:0;z-index:90;display:flex;align-items:center;justify-content:center;padding:32px;
  background:rgba(8,8,10,.82);backdrop-filter:blur(4px);-webkit-backdrop-filter:blur(4px);cursor:zoom-out;
  opacity:0;transition:opacity .16s}
.lightbox.show{opacity:1}
.lightbox img{max-width:100%;max-height:100%;border-radius:6px;box-shadow:0 24px 80px -12px rgba(0,0,0,.6)}
/* Brand bar (export-brand.tsx) */
${Me}
/* hljs — driven by the theme's --hl-* tokens (light + dark in one place) */
.hljs-comment,.hljs-quote{color:var(--hl-comment);font-style:italic}
.hljs-keyword,.hljs-selector-tag,.hljs-type,.hljs-tag{color:var(--hl-key)}
.hljs-string,.hljs-doctag,.hljs-regexp,.hljs-meta .hljs-string{color:var(--hl-str)}
.hljs-title,.hljs-section,.hljs-name,.hljs-selector-id,.hljs-selector-class{color:var(--hl-title)}
.hljs-literal,.hljs-number,.hljs-built_in,.hljs-attr,.hljs-attribute,.hljs-variable,.hljs-template-variable{color:var(--hl-num)}
.hljs-symbol,.hljs-bullet,.hljs-meta,.hljs-link{color:var(--hl-meta)}
.hljs-emphasis{font-style:italic}.hljs-strong{font-weight:600}
.hljs-addition{color:var(--hl-str);background:color-mix(in oklab,var(--hl-str) 12%,transparent)}
.hljs-deletion{color:var(--hl-key);background:color-mix(in oklab,var(--hl-key) 12%,transparent)}
@media (prefers-reduced-motion:reduce){*{scroll-behavior:auto!important;transition:none!important}}
@media print{
  .chrome,.progress,.controls,.copy-btn{display:none}
  .round,.exhibit,.fold{break-inside:avoid}
  details.fold[open]>.fd-body{display:block}
  .ex-pdf{height:520px}
  body{font-size:11pt}
}
`,V=`(function(){try{var k="mirasim-export-theme";var s=localStorage.getItem(k);
document.documentElement.dataset.theme=s||(matchMedia&&matchMedia("(prefers-color-scheme: dark)").matches?"dark":"light");}catch(e){document.documentElement.dataset.theme="light";}})();`,J=`(function(){
  var root=document.documentElement;
  // Theme toggle (persisted).
  function paintThemeBtns(){var d=root.dataset.theme==="dark";document.querySelectorAll("[data-theme-toggle]").forEach(function(b){b.setAttribute("aria-pressed",d);var l=b.querySelector(".tt-label");if(l)l.textContent=d?"Light":"Dark";});}
  document.addEventListener("click",function(e){
    var t=e.target.closest("[data-theme-toggle]");if(!t)return;
    var next=root.dataset.theme==="dark"?"light":"dark";root.dataset.theme=next;
    try{localStorage.setItem("mirasim-export-theme",next);}catch(_){}paintThemeBtns();
  });
  paintThemeBtns();
  // Expand / collapse all disclosures.
  document.addEventListener("click",function(e){
    var a=e.target.closest("[data-fold-all]");if(!a)return;
    var open=a.getAttribute("data-fold-all")==="open";
    document.querySelectorAll("details.fold").forEach(function(d){d.open=open;});
  });
  // Image lightbox: click any .zoomable to enlarge; click / Esc to close.
  var box=null;
  function closeBox(){if(box){box.classList.remove("show");var b=box;setTimeout(function(){if(b&&b.parentNode)b.parentNode.removeChild(b);},180);box=null;}}
  document.addEventListener("click",function(e){
    var img=e.target.closest("img.zoomable");if(!img)return;
    closeBox();box=document.createElement("div");box.className="lightbox";
    var big=document.createElement("img");big.src=img.currentSrc||img.src;big.alt=img.alt||"";
    box.appendChild(big);box.addEventListener("click",closeBox);document.body.appendChild(box);
    requestAnimationFrame(function(){if(box)box.classList.add("show");});
  });
  document.addEventListener("keydown",function(e){if(e.key==="Escape")closeBox();});
  // Copy buttons on code blocks.
  document.querySelectorAll("pre").forEach(function(pre){
    var code=pre.querySelector("code");if(!code)return;
    var b=document.createElement("button");b.className="copy-btn";b.type="button";b.textContent="Copy";
    b.addEventListener("click",function(){
      var txt=code.innerText;
      var ok=function(){b.textContent="Copied";b.classList.add("done");setTimeout(function(){b.textContent="Copy";b.classList.remove("done");},1400);};
      if(navigator.clipboard&&navigator.clipboard.writeText){navigator.clipboard.writeText(txt).then(ok,function(){});}
      else{try{var ta=document.createElement("textarea");ta.value=txt;document.body.appendChild(ta);ta.select();document.execCommand("copy");document.body.removeChild(ta);ok();}catch(_){}}
    });
    pre.appendChild(b);
  });
  // Reading-progress bar + sticky chrome reveal.
  var bar=document.querySelector(".progress"),chrome=document.querySelector(".chrome"),mast=document.querySelector(".masthead");
  function onScroll(){
    var h=document.documentElement,max=h.scrollHeight-h.clientHeight;
    if(bar)bar.style.width=(max>0?(h.scrollTop/max*100):0)+"%";
    if(chrome&&mast){var past=h.scrollTop>(mast.offsetTop+mast.offsetHeight-56);chrome.classList.toggle("show",past);}
  }
  window.addEventListener("scroll",onScroll,{passive:true});window.addEventListener("resize",onScroll);onScroll();
  // Scroll-spy: highlight the contents entry for the round in view.
  var links={};document.querySelectorAll(".contents a").forEach(function(a){var id=a.getAttribute("href").slice(1);links[id]=a;});
  if("IntersectionObserver" in window && Object.keys(links).length){
    var seen=new Map();
    var io=new IntersectionObserver(function(entries){
      entries.forEach(function(en){seen.set(en.target.id,en.intersectionRatio);});
      var best=null,bestR=0;seen.forEach(function(r,id){if(r>bestR){bestR=r;best=id;}});
      Object.keys(links).forEach(function(id){links[id].classList.toggle("active",id===best);});
    },{rootMargin:"-45% 0px -45% 0px",threshold:[0,.25,.5,1]});
    document.querySelectorAll("main .round").forEach(function(s){io.observe(s);});
  }
})();`,et='<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="4"/><path d="M12 2v2M12 20v2M4.9 4.9l1.4 1.4M17.7 17.7l1.4 1.4M2 12h2M20 12h2M4.9 19.1l1.4-1.4M17.7 6.3l1.4-1.4"/></svg>',tt=Re([V,J],[K]);function ut(e){const t=(e.title||"Conversation").trim(),o=[["Assistant",e.agentLabel],...e.modelLabel?[["Model",e.modelLabel]]:[],["Rounds",String(e.turns.length)],["Exported",U.format(e.exportedAt)]].map(([f,m])=>`<div><dt>${s(f)}</dt><dd>${s(m)}</dd></div>`).join(""),n=e.turns.length>1?`<nav class="contents"><div class="eyebrow">Contents</div><ol>${e.turns.map(f=>`<li><a href="#r${f.n}"><span class="c-num">${String(f.n).padStart(2,"0")}</span><span class="c-q">${s(E(f.question)||"—")}</span></a></li>`).join("")}</ol></nav>`:"",r=e.turns.map(f=>Ze(f,e)).join(`
`),a=`<button class="btn" data-theme-toggle type="button" aria-pressed="false">${et}<span class="tt-label">Dark</span></button>`,l=De(e),d=[`<meta name="robots" content="${Ie}" />`,l?`<meta name="description" content="${s(l)}" />`:"",`<meta property="og:title" content="${s(t)}" />`,l?`<meta property="og:description" content="${s(l)}" />`:"",'<meta property="og:site_name" content="Mirasim" />','<meta property="og:type" content="article" />',`<meta name="mirasim:agent" content="${s(e.agentLabel)}" />`,...e.modelLabel?[`<meta name="mirasim:model" content="${s(e.modelLabel)}" />`]:[],`<meta name="mirasim:rounds" content="${e.turns.length}" />`,`<meta name="mirasim:exported-at" content="${new Date(e.exportedAt).toISOString()}" />`].filter(Boolean).join(`
`);return`<!DOCTYPE html>
<html lang="en" data-theme="light">
<head>
<meta charset="utf-8" />
${tt}
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="generator" content="Mirasim" />
<title>${s(t)}</title>
${d}
<style>${K}</style>
<script>${V}<\/script>
</head>
<body>
<div class="progress"></div>
<div class="chrome"><span class="ch-title">${s(t)}</span>${a}<button class="btn" data-fold-all="open" type="button">Expand</button><button class="btn" data-fold-all="closed" type="button">Collapse</button></div>
<div class="topbar"></div>
<div class="page"><div class="rail">
${Te}
<header class="masthead">
<div class="eyebrow">Conversation Transcript</div>
<h1 class="doc-title">${s(t)}</h1>
<dl class="meta">${o}</dl>
<div class="controls">${a}<button class="btn" data-fold-all="open" type="button">Expand all</button><button class="btn" data-fold-all="closed" type="button">Collapse all</button></div>
</header>
${n}
<main>${r}</main>
${Ae}
</div></div>
<script>${J}<\/script>
</body>
</html>`}function ot(e){let t=0,o=0;for(const n of e)n==="`"?(o++,o>t&&(t=o)):o=0;return"`".repeat(Math.max(3,t+1))}function $(e,t=""){const o=ot(e);return`${o}${t}
${e}
${o}`}function N(e,t){const o=t.artifacts.get(H(e)),n=e.fileName||e.path||"artifact",r=e.caption?`（${e.caption}）`:"";if(!o)return`- \`${n}\`${r}`;if(o.kind==="image"&&o.assetPath)return`![${e.caption||n}](${o.assetPath})`;if(o.assetPath)return`- [${n}](${o.assetPath})${r}`;if(o.text!=null){const a=o.kind==="markdown"?"markdown":o.kind==="csv"?"csv":o.lang||"";return`**${n}**${r}

${$(o.text,a)}`}return`- \`${n}\`${r}${o.note?` (${o.note})`:""}`}function nt(e,t){const o=e.rounds.flatMap(n=>at(n,t));return e.failed&&e.error&&o.push(`> ⚠ ${e.error}`),o.join(`

`)||"_No response._"}function at(e,t){const o=e.text??"",n=e.reasoning??"",r=new Map((e.toolCalls??[]).map(i=>[i.id,i])),a=new Map((e.subAgents??[]).map(i=>[i.id,i])),l=q(e.subAgents??[]),{byCall:d,trailing:f}=I(e),m=[];for(const i of D(e))if(i.kind==="text"){const p=o.slice(i.from,i.to).trim();p&&m.push(p)}else if(i.kind==="thinking"){const p=n.slice(i.from,i.to).trim();p&&m.push(`> **Thinking**
>
${p.split(`
`).map(c=>`> ${c}`).join(`
`)}`)}else if(i.kind==="agents")for(const p of i.ids){const c=a.get(p);if(!c)continue;const u=[`#### Sub-agent — ${c.description||c.kind||"sub-agent"}`];c.prompt&&u.push(`_Prompt:_

${$(c.prompt)}`),c.tools.length&&u.push(c.tools.map(b=>`- ${b.summary||b.name}`).join(`
`)),c.report&&u.push(`_Report:_

${$(c.report)}`);const v=[c.model,c.durationMs?`${Math.round(c.durationMs/1e3)}s`:"",c.totalTokens?`${c.totalTokens} tok`:""].filter(Boolean);v.length&&u.push(`_${v.join(" | ")}_`),m.push(u.join(`

`))}else if(i.kind==="tools"){for(const p of i.ids){const c=r.get(p);if(!c||l.has(c.id))continue;const u=t.toolDetails.get(p),v=u?.input??c.input??null,b=u?.result??c.result??null,k=[`#### Tool — ${c.summary||c.name}${c.status==="failed"?" (failed)":""}`];v&&k.push(`_Input:_

${$(v)}`),b&&k.push(`_Result:_

${$(b)}`),m.push(k.join(`

`))}for(const p of i.ids)for(const c of d.get(p)??[])m.push(N(c,t))}for(const i of f)m.push(N(i,t));return m}function ht(e){const t=[`# ${(e.title||"Conversation").trim()}`,""],o=[`- **Assistant:** ${e.agentLabel}`];e.modelLabel&&o.push(`- **Model:** ${e.modelLabel}`),o.push(`- **Rounds:** ${e.turns.length}`,`- **Exported:** ${U.format(e.exportedAt)}`);const n=[t.join(`
`),o.join(`
`)];for(const r of e.turns){const a=r.at?`（${O.format(r.at)}）`:"";n.push("---",`## ${String(r.n).padStart(2,"0")}${a}`);const l=["### Prompt"];r.question&&l.push(r.question),r.paths.length&&l.push(r.paths.map(d=>`- ${d.kind==="dir"?"📁":"📄"} \`${d.name}\``).join(`
`));for(const d of r.imageRefs){const f=e.images.get(d);f&&l.push(`![attached image](${f})`)}n.push(l.join(`

`)),n.push([`### ${e.agentLabel}`,nt(r,e)].join(`

`))}return`${n.join(`

`)}
`}export{H as artifactKey,dt as classifyExportAsset,ct as collectExportTurns,lt as isReferencedKind,ut as renderConversationHtml,ht as renderConversationMarkdown,mt as shortTitle,pt as slugifyTitle,ft as uniqueAssetName};
