// Reproducible static extraction. Does not launch or modify the APK.
const fs = require('node:fs');
const path = require('node:path');
const crypto = require('node:crypto');
const { DOMParser } = require(path.join(process.env.APPDATA, 'npm/node_modules/rea-agents/node_modules/@xmldom/xmldom'));
const root = path.resolve('analysis/phase2/apktool/res');
const out = path.resolve('analysis/ui');
fs.mkdirSync(out, { recursive: true });
const rel = p => path.relative(process.cwd(), p).replaceAll('\\', '/');
const resources = new Map();
const trees = new Map();
function kids(node) { return Array.from(node.childNodes || []).filter(n => n.nodeType === 1); }
function attrs(node) { return Object.fromEntries(Array.from(node.attributes || []).filter(a => !a.name.startsWith('xmlns')).map(a => [a.name, a.value])); }
function read(file) {
  const source = fs.readFileSync(file, 'utf8');
  const doc = new DOMParser({ locator: {}, onError(level, message) { if (level === 'fatalError') throw Error(message); } }).parseFromString(source, 'text/xml');
  return { source, node: doc.documentElement, file, qualifier: path.basename(path.dirname(file)) };
}
function loc(node, file) { return { file: rel(file), line: node.lineNumber || 1 }; }
for (const dir of fs.readdirSync(root).filter(d => d.startsWith('values'))) {
  for (const name of ['colors.xml', 'dimens.xml', 'styles.xml', 'strings.xml', 'integers.xml']) {
    const file = path.join(root, dir, name);
    if (!fs.existsSync(file)) continue;
    const doc = read(file);
    for (const node of kids(doc.node)) {
      const name = node.getAttribute('name');
      if (!name) continue;
      const type = node.tagName === 'item' ? node.getAttribute('type') : node.tagName;
      const key = `${type}/${name}`;
      const entry = { qualifier: dir, raw: node.textContent.trim(), source: loc(node, file) };
      if (type === 'style') { entry.parent = node.getAttribute('parent') || null; entry.items = Object.fromEntries(kids(node).map(n => [n.getAttribute('name'), {raw: n.textContent.trim(), source: loc(n, file)}])); }
      if (!resources.has(key)) resources.set(key, []);
      resources.get(key).push(entry);
    }
  }
}
function chosen(key, mode) {
  const entries = resources.get(key) || [];
  return entries.find(r => r.qualifier === (mode === 'night' ? 'values-night' : 'values')) || entries.find(r => r.qualifier === 'values');
}
function argb(raw) {
  if (!/^#[0-9a-f]{3,8}$/i.test(raw)) return null;
  let s = raw.slice(1);
  if (s.length === 3) s = 'ff' + s.split('').map(v=>v+v).join('');
  else if (s.length === 4) s = s.split('').map(v=>v+v).join('');
  else if (s.length === 6) s = 'ff' + s;
  if (s.length !== 8) return null;
  const a = parseInt(s.slice(0,2),16), r = parseInt(s.slice(2,4),16), g = parseInt(s.slice(4,6),16), b = parseInt(s.slice(6,8),16);
  return { androidARGB: '#' + s.toLowerCase(), rgb: [r,g,b], alphaByte: a, alpha: a/255, cssHexRGBA: '#' + s.slice(2).toLowerCase() + s.slice(0,2).toLowerCase(), css: a === 255 ? '#' + s.slice(2).toLowerCase() : `rgba(${r}, ${g}, ${b}, ${Number((a/255).toFixed(6))})` };
}
function resolve(ref, mode = 'day', seen = []) {
  if (ref === '@android:color/transparent') return { raw: ref, value: '#00000000', color: argb('#00000000'), chain: [], platformConstant: true };
  if (ref === '@android:color/white' || ref === '@android:color/black') return {raw:ref,value:ref.endsWith('white')?'#ffffff':'#000000',color:argb(ref.endsWith('white')?'#ffffff':'#000000'),chain:[],platformConstant:true};
  if (!ref.startsWith('@')) return { raw: ref, value: ref, color: argb(ref), dimension: /^-?[\d.]+(dp|sp|px)$/.test(ref) ? { value:parseFloat(ref),unit:ref.match(/(dp|sp|px)$/)[0] } : undefined, chain: [] };
  const key = ref.slice(1);
  if (seen.includes(key)) return {raw:ref, unknown:'cyclic reference',chain:[]};
  const entry = chosen(key,mode);
  if (!entry) return {raw:ref,unknown:'not a simple value (selector/drawable/font/platform/theme may need separate resolution)',chain:[]};
  const next = resolve(entry.raw, mode, [...seen,key]);
  return {...next, raw:ref, chain:[{resource:key,qualifier:entry.qualifier,raw:entry.raw,source:entry.source},...next.chain]};
}
const selectedLayouts = ['main_activity_main','main_view_tab','main_fragment_homepage','layout_home_header_tab','layout_home_header_title_item','health_home_card_common_title','health_layout_common_card_icon_title','health_layout_card_data_item','device_fragment_device_tab_parent_v4','device_fragment_device_tab_v4','device_tab_normal_item','device_tab_setting_item_list','mine_fragment_v4','mine_user_info_view','mine_assets_item_view','mine_tab_basic_list_item','layout_single_textview_right_arrow','layout_two_textview_right_arrow','widget_title_bar','widget_top_bar','access_activity_misport_import','userinfo_layout_home_card_set_item'];
const styleNames = new Set(['FontNormal','FontRegular','FontMedium','FontDemiBold','FontLatinDemiBold','FontLatinHeavy','FontLatinMedium','FontLatinRegular','FontLatinSemiBold','FontRegular.13sp','CommonBtn','CommonBtn.Primary','CommonBtn.Normal','CommonBtn.Gray','CardTextAppearance.LabelTitle']);
const refs = new Set();
function seeRef(v) { if (/^@(color|dimen|string|style|drawable)\//.test(v)) { refs.add(v.slice(1)); if (v.startsWith('@style/') && /^(Font|CommonBtn|CardTextAppearance|HealthCardText)/.test(v.slice(7))) styleNames.add(v.slice(7)); } }
function tree(node, file) { const a=attrs(node); for(const v of Object.values(a))seeRef(v);return { tag:node.tagName,attributes:a,source:loc(node,file),children:kids(node).map(n=>tree(n,file)) }; }
const layouts=[];
for(const name of selectedLayouts) {
  const file=path.join(root,'layout',name+'.xml');if(!fs.existsSync(file))continue;
  const doc=read(file);layouts.push({id:name,provenance:'first-party native layout or native component; third-party widget classes are dependencies',routeStatus:name==='mine_fragment_v4'?'legacy/compatibility native resource; current MineFragmentV4 code is a YRN container, so this XML is not evidence of current v4 rendering':'resource declaration; actual active screen binding must be checked against code/runtime',xml:tree(doc.node,file)});
}
function styleRecord(name, mode='day', seen=[]) {
  if(seen.includes(name))return null;
  const entry=chosen('style/'+name,mode);if(!entry)return null;
  const parent=entry.parent?.startsWith('@style/')?entry.parent.slice(7):null;
  const parentEntry=parent && /^(Font|CommonBtn|CardTextAppearance|HealthCardText)/.test(parent)?styleRecord(parent,mode,[...seen,name]):null;
  const merged={...parentEntry?.effectiveItems};
  for(const [key,item] of Object.entries(entry.items)) { seeRef(item.raw);merged[key]={...item,...resolve(item.raw,mode)}; }
  return {name,parent:entry.parent,source:entry.source,ownItems:entry.items,effectiveItems:merged};
}
const styles=[];
for(const name of styleNames){const entry=styleRecord(name);if(entry)styles.push(entry);}
// Include only drawable/color trees reachable from the selected first-party components/styles.
const visited=new Set(); const drawables=[];
function loadTree(key) {
  if(visited.has(key))return;visited.add(key);
  const [type,name]=key.split('/');if(!['drawable','color'].includes(type))return;
  for(const dir of [type,type+'-night']) {
    const file=path.join(root,dir,name+'.xml');if(!fs.existsSync(file))continue;
    const doc=read(file);drawables.push({resource:key,qualifier:dir,origin:/^(miuix|abc|design|mtrl)_/.test(name)?'bound-library-dependency':'first-party-or-bound-app-resource',xml:tree(doc.node,file)});
  }
}
for(const key of refs)loadTree(key);
const semantic={
  'page-background':{ref:'@color/page_bg',usage:'main_activity_main.xml: main_fl_content; main_fragment_homepage through common_page_bg_color'},
  'legacy-native-mine-page-background':{ref:'@color/mine_page_background',usage:'legacy/compatibility native mine_fragment_v4.xml root; current MineFragmentV4 is a YRN container'},
  'card-background':{ref:'@color/common_card_bg_color',usage:'common_card_bg_white.xml'},
  'legacy-native-mine-card-background':{ref:'@color/mine_white',usage:'mine_label_bg_white.xml bound in legacy/compatibility native mine_fragment_v4.xml; not proof of current YRN Mine rendering'},
  'text-primary':{ref:'@color/text_color',usage:'health_home_card_common_title.xml and widget_title_bar.xml'},
  'text-muted':{ref:'@color/text_color_40',usage:'health_home_card_common_title.xml:tvHomeCardDesc'},
  'navigation-background':{ref:'@color/main_tab_bg',usage:'main_activity_main.xml:main_tl_bottom'},
  'navigation-label-inactive':{ref:'@color/main_tab_text_color_normal',usage:'main_view_tab.xml:main_tv_tab_name'},
  'navigation-label-selected':{ref:'@color/colorPrimary',usage:'MainActivity.selected(): z=true -> b5k.a=2131099886 -> public.xml colorPrimary'},
  'page-header-label-selected':{ref:'@color/text_color',usage:'HomeHeaderTabTitleView.bindTitle(): isCurrent=true -> b6k.o=2131103704 -> public.xml text_color'},
  'page-header-label-inactive':{ref:'@color/home_header_title_unselect',usage:'HomeHeaderTabTitleView.bindTitle(): isCurrent=false -> b6k.f=2131100815 -> public.xml home_header_title_unselect'},
  'action-primary-background':{ref:'@color/colorPrimary',usage:'CommonBtn.Primary > selector_bottom_btn_positive_bg > common_dialog_positive_btn_smooth'},
  'action-primary-label':{ref:'@color/common_btn_primary_text_color',usage:'CommonBtn.Primary > selector_common_btn_primary_text_color enabled/default'},
  'action-primary-label-disabled':{ref:'@color/common_btn_primary_text_color_unenabled',usage:'CommonBtn.Primary > selector_common_btn_primary_text_color state_enabled=false'},
  'list-label':{ref:'@color/feature_item_title_text_color',usage:'layout_single_textview_right_arrow.xml through selector_feature_item_title_text_color'},
  'list-label-disabled':{ref:'@color/feature_item_title_text_color_unenabled',usage:'selector_feature_item_title_text_color state_enabled=false'},
  'list-description':{ref:'@color/feature_item_subtitle_text_color',usage:'layout_two_textview_right_arrow.xml through selector_feature_item_subtitle_text_color'},
  'list-description-disabled':{ref:'@color/feature_item_subtitle_text_color_unenabled',usage:'selector_feature_item_subtitle_text_color state_enabled=false'},
  'navigation-divider':{ref:'@color/line_normal',usage:'main_activity_main.xml divider'}
};
for(const entry of Object.values(semantic)){seeRef(entry.ref);entry.day=resolve(entry.ref,'day');entry.night=resolve(entry.ref,'night');}
for(const k of ['main_tab_height','main_tab_layout_padding','divider_height','page_extra_padding_horizontal_large','common_btn_height','common_btn_margin','common_btn_text_size','title_bar_height','title_bar_text_size','title_bar_sub_text_size','feature_item_icon_size','feature_item_icon_top_bottom_margin','feature_item_title_start_margin','feature_item_title_top_bottom_margin','feature_item_remind_dot_size','feature_item_remind_margin_start','feature_item_more_margin_start','dimen_margin_16dp','miuix_appcompat_button_bg_corner_radius','miuix_appcompat_capsule_button_bg_corner_radius','miuix_appcompat_button_content_safe_padding','miuix_appcompat_button_content_safe_padding_vertical','miuix_appcompat_button_height'])refs.add('dimen/'+k);
const values={};
for(const key of refs)if(resources.has(key)&&!key.startsWith('style/'))values[key]={day:resolve('@'+key,'day'),night:resolve('@'+key,'night'),qualifiedDefinitions:resources.get(key)};
function fontFacts(buffer) {
  const tables={};for(let i=0;i<buffer.readUInt16BE(4);i++){const p=12+i*16;tables[buffer.toString('ascii',p,p+4)]={offset:buffer.readUInt32BE(p+8),length:buffer.readUInt32BE(p+12)};}
  const result={weightClass:tables['OS/2']?buffer.readUInt16BE(tables['OS/2'].offset+4):null,glyphCount:tables.maxp?buffer.readUInt16BE(tables.maxp.offset+4):null};
  if(!tables.cmap)return result;
  const cmap=tables.cmap.offset;const probes=[0x30,0x41,0x4e00,0x5065,0x5eb7];const maps=[];
  for(let i=0;i<buffer.readUInt16BE(cmap+2);i++){const p=cmap+4+i*8;const platform=buffer.readUInt16BE(p),encoding=buffer.readUInt16BE(p+2),off=cmap+buffer.readUInt32BE(p+4);if(platform===0||(platform===3&&[1,10].includes(encoding)))maps.push(off);}
  function has(off,cp){const f=buffer.readUInt16BE(off);if(f===12){for(let i=0;i<buffer.readUInt32BE(off+12);i++){const p=off+16+i*12,s=buffer.readUInt32BE(p),e=buffer.readUInt32BE(p+4);if(cp>=s&&cp<=e)return buffer.readUInt32BE(p+8)+cp-s!==0;}}if(f===4&&cp<=0xffff){const n=buffer.readUInt16BE(off+6)/2;const end=off+14,start=end+2*n+2,delta=start+2*n,range=delta+2*n;for(let i=0;i<n;i++){const s=buffer.readUInt16BE(start+2*i),e=buffer.readUInt16BE(end+2*i);if(cp<s||cp>e)continue;const d=buffer.readInt16BE(delta+2*i),r=buffer.readUInt16BE(range+2*i);if(!r)return ((cp+d)&65535)!==0;const pos=range+2*i+r+2*(cp-s);const glyph=buffer.readUInt16BE(pos);return glyph!==0&&((glyph+d)&65535)!==0;}}return false;}
  result.unicodeGlyphProbes=Object.fromEntries(probes.map(cp=>['U+'+cp.toString(16).toUpperCase().padStart(4,'0'),maps.some(off=>has(off,cp))]));
  result.probeScope='Unicode cmap formats 4/12, platform 0 or platform 3 Unicode; probes are bounded checks, not a complete coverage audit';return result;
}
const fonts=fs.readdirSync(path.join(root,'font')).map(name=>{const file=path.join(root,'font',name),buffer=fs.readFileSync(file);return {file:rel(file),bytes:buffer.length,sha256:crypto.createHash('sha256').update(buffer).digest('hex'),...fontFacts(buffer)};});
const publicFile=path.join(root,'values/public.xml'),publicDoc=read(publicFile);
const mappedConstants=[['b5k.a',2131099886,'MainActivity.selected(): true'],['b5k.c',2131101379,'MainActivity.selected(): false'],['b6k.o',2131103704,'HomeHeaderTabTitleView.bindTitle(): true'],['b6k.f',2131100815,'HomeHeaderTabTitleView.bindTitle(): false']].map(([field,id,usage])=>{const hex='0x'+id.toString(16),node=kids(publicDoc.node).find(n=>n.getAttribute('id')===hex);return {field,id,hex,usage,resource:node?node.getAttribute('type')+'/'+node.getAttribute('name'):null,source:node?loc(node,publicFile):null};});
const tokenDocument={schemaVersion:1,artifact:'Xiaomi Mi Fitness Android 3.57.0 com.mi.health',scope:'Selected shipped first-party native XML layouts, native component code and bound resource dependencies; static evidence only',evidenceType:'decoded APK resource declarations and decompiled native component code; no device rendering',limitations:['Day/night resolution here means base values and values-night only. Other qualifiers are preserved as qualifiedDefinitions and need Android configuration selection.','A declared XML value is not proof of the currently visible layout; code may replace it. Current MineFragmentV4/SportTabV4Fragment are YRN containers; similarly named native XML is legacy/compatibility evidence only.','dp to CSS px and sp to CSS px are reconstruction conventions, not physical pixel equivalence. Respect density, Android fontScale, system insets and font fallback.','CSS border-radius does not reproduce MIUI smooth adaptive corners exactly.','Screen-specific alpha, accessibility text scale, code animation and data-driven visibility need runtime validation.'],colorEncoding:{android:'#AARRGGBB (8 digits), #RRGGBB (6 digits), #ARGB (4 digits), #RGB (3 digits)',css:'#RRGGBBAA or rgba(r,g,b,AA/255)',example:{android:'#99000000',css:'#00000099',alpha:153/255},warning:'Never copy Android #AARRGGBB directly into an 8-digit CSS hex.'},semantic,values,styles,fonts,mappedConstants,unknowns:{exactFontMetrics:'mipro system faces and OEM fallback not bundled in this res/font inventory',motion:'resource availability alone does not prove binding or observed timing'}};
fs.writeFileSync(path.join(out,'design-tokens.json'),JSON.stringify(tokenDocument,null,2)+'\n');
const componentContracts=[
 {id:'bottom-navigation',source:rel(path.join(root,'layout/main_activity_main.xml')),metrics:{height:resolve('@dimen/main_tab_height'),horizontalMargin:resolve('@dimen/main_tab_layout_padding'),dividerHeight:resolve('@dimen/divider_height'),labelSize:resolve('@dimen/dimen_11sp'),labelIconGap:resolve('@dimen/dimen_margin_3dp')},declarations:{tabGravity:'fill',tabIndicator:'@null',tabRippleColor:'@color/transparent',topPadding:'2dp',badgeDefaultVisibility:'gone'},states:{selectedLabel:'colorPrimary, resolved MainActivity.selected -> b5k.a -> public.xml',inactiveLabel:'main_tab_text_color_normal, resolved MainActivity.selected -> b5k.c -> public.xml'},unknown:['icon intrinsic size depends on bound drawable','inset handling and runtime overrides']},
 {id:'page-header',source:rel(path.join(root,'layout/layout_home_header_tab.xml')),metrics:{titleSize:{value:32,unit:'sp',source:rel(path.join(root,'layout/layout_home_header_title_item.xml'))},menuSize:resolve('@dimen/dimen_margin_36dp'),titleGroupMarginVertical:resolve('@dimen/dimen_margin_20dp'),menuMarginEnd:resolve('@dimen/dimen_margin_20dp')},declarations:{titleFamily:'sans-serif-light',headerHeight:'wrap_content',titles:'horizontal RecyclerView, labels supplied by code'}},
 {id:'card-container-common',source:rel(path.join(root,'drawable/common_card_bg_white.xml')),metrics:{cornerRadius:resolve('@dimen/dimen_margin_16dp')},colors:{day:resolve('@color/common_card_bg_color','day'),night:resolve('@color/common_card_bg_color','night')},states:{pressed:'same color and radius as default',disabled:'same color and radius as default'},unknown:['additional code touch overlays/scale are not specified by this drawable']},
 {id:'card-container-mine',source:rel(path.join(root,'drawable/mine_label_bg_white.xml')),metrics:{cornerRadius:resolve('@dimen/dimen_margin_16dp')},colors:{day:resolve('@color/mine_white','day'),night:resolve('@color/mine_white','night')},states:{pressed:'same drawable fill as default',disabled:'same drawable fill as default'}},
 {id:'primary-action',source:chosen('style/CommonBtn.Primary','day').source,metrics:{declaredLayoutHeight:resolve('@dimen/common_btn_height'),fontSize:resolve('@dimen/common_btn_text_size'),defaultMargin:resolve('@dimen/common_btn_margin'),drawableIntrinsicHeight:resolve('@dimen/miuix_appcompat_button_height'),drawableCornerRadius:resolve('@dimen/miuix_appcompat_button_bg_corner_radius'),drawableCapsuleRadius:resolve('@dimen/miuix_appcompat_capsule_button_bg_corner_radius'),contentSafePaddingX:resolve('@dimen/miuix_appcompat_button_content_safe_padding'),contentSafePaddingY:resolve('@dimen/miuix_appcompat_button_content_safe_padding_vertical')},declarations:{width:'match_parent unless overridden by layout',corners:'miuix.appcompat.app.AdaptRoundButtonDrawable with miuix_useSmooth=true',foreground:'miuix.appcompat.app.ButtonForegroundDrawable normalAlpha=0, hoveredAlpha=.06, pressedAlpha=.12'},states:{labelDisabled:'selector_common_btn_primary_text_color -> common_btn_primary_text_color_unenabled',labelEnabled:'selector_common_btn_primary_text_color -> common_btn_primary_text_color'},unknown:['actual adaptive radius depends on drawable implementation/configuration','native minHeight/insets and TextView-versus-Button style may affect measured box','do not infer enabled click behavior from a color selector alone']},
 {id:'feature-row-single',source:rel(path.join(root,'layout/layout_single_textview_right_arrow.xml')),metrics:{leadingIcon:resolve('@dimen/feature_item_icon_size'),leadingIconMarginVertical:resolve('@dimen/feature_item_icon_top_bottom_margin'),iconLabelGap:resolve('@dimen/feature_item_title_start_margin')},declarations:{height:'wrap_content',title:'FontMedium.16sp; one line; ellipsize=end',rightText:'FontRegular.14sp',rightArrow:'wrap_content drawable',newBadgeDefault:'gone',titleEnabledColor:'selector_feature_item_title_text_color'},inference:'30dp icon plus 15dp top/bottom margins produces a 60dp content block when the icon participates; this is not proof of a universal fixed row height.'},
 {id:'feature-row-two',source:rel(path.join(root,'layout/layout_two_textview_right_arrow.xml')),metrics:{titleMarginTop:resolve('@dimen/feature_item_title_top_bottom_margin'),titleMarginBottom:{raw:'13.329987dp',value:13.329987,unit:'dp'},reminderDotSize:resolve('@dimen/feature_item_remind_dot_size')},declarations:{height:'wrap_content',title:'FontMedium.16sp; maxLines=2; ellipsize=end',subtitle:'FontRegular.12sp; maxLines=4; ellipsize=end',rightText:'FontRegular.14sp; maxWidth=96dp; maxLines=2',rightArrowGap:'feature_item_more_margin_start=10dp'}},
 {id:'health-summary-card-header',source:rel(path.join(root,'layout/health_home_card_common_title.xml')),metrics:{icon:{raw:'26dp',value:26,unit:'dp'},title:{raw:'17sp',value:17,unit:'sp'},value:{raw:'18sp',value:18,unit:'sp'},description:{raw:'13sp',value:13,unit:'sp'},titleMarginTop:{raw:'6dp',value:6,unit:'dp'}},declarations:{emptyDescription:'14sp FontNormal; gone by default; trailing arrow',titleAndValueStyle:'FontMedium',descriptionStyle:'FontNormal',optionalWidgets:'diet button and snore-monitor chip are business-specific, gone by default'}},
 {id:'detail-titlebar',source:rel(path.join(root,'layout/widget_title_bar.xml')),metrics:{height:resolve('@dimen/title_bar_height'),titleSize:resolve('@dimen/title_bar_text_size'),subtitleSize:resolve('@dimen/title_bar_sub_text_size')},declarations:{title:'bold centered single line with end ellipsis',titleHorizontalReserve:'60dp each side',subtitle:'gone by default',rightIconBox:'title_bar_height square',divider:'gone by default'},unknown:['this component coexists with widget_top_bar (53.329987dp/20sp), so a single global bar size is unsupported']}
];
const rowCodeContracts=[
 {component:'feature-row-single',source:'analysis/ui/native-extra/com/xiaomi/fitness/widget/RightArrowSingleLineTextView.java',findings:[{line:112,behavior:'onTouchEvent: if disabled AND allowClickWhenDisabled=true, ACTION_UP invokes performClick and consumes event'},{line:135,behavior:'setDefaultEnable: calls setEnable then sets leading icon alpha to 1.0/0.25'},{line:140,behavior:'setEnable: root, arrow, leading icon, reminder and title enabled states updated; right-value text is not updated here'},{line:122,behavior:'setArrowViewPadding body is empty; no default padding inferred from its name'},{line:244,behavior:'setViewPadding forwards raw integer values to setPadding; calling context determines pixels'}]},
 {component:'feature-row-two',source:'analysis/ui/native-extra/com/xiaomi/fitness/widget/RightArrowTwoLineTextView.java',findings:[{line:112,behavior:'onTouchEvent has the same optional allowClickWhenDisabled behavior'},{line:135,behavior:'setDefaultEnable: calls setEnable then sets leading icon alpha to 1.0/0.25'},{line:140,behavior:'setEnable: root, arrow, leading icon, reminder, subtitle, title and right-value text enabled states updated'},{line:368,behavior:'setEnable(enable, drawable) overload only changes title enabled state and replaces arrow drawable; differs from single-argument overload'},{line:122,behavior:'setArrowViewPadding body is empty; callers can set explicit padding via setViewPadding'}]}
];
fs.writeFileSync(path.join(out,'components.json'),JSON.stringify({schemaVersion:1,scope:tokenDocument.scope,limitations:tokenDocument.limitations,componentContracts,rowCodeContracts,layouts,styles,drawableAndSelectorDependencies:drawables},null,2)+'\n');
const geometry={
  'navigation-height':['main_tab_height','dp'], 'navigation-label-size':['dimen_11sp','sp'],
  'native-page-inline-padding-large':['page_extra_padding_horizontal_large','dp'],
  'card-radius-declared':['dimen_margin_16dp','dp'],
  'button-height':['common_btn_height','dp'], 'button-label-size':['common_btn_text_size','sp'], 'button-default-margin':['common_btn_margin','dp'],
  'list-leading-icon-size':['feature_item_icon_size','dp'], 'list-icon-block-margin':['feature_item_icon_top_bottom_margin','dp'],
  'list-icon-label-gap':['feature_item_title_start_margin','dp'],
  'detail-titlebar-height':['title_bar_height','dp'], 'detail-title-size':['title_bar_text_size','sp']
};
let css='/* Shipped XML-derived semantic candidates, not a screenshot-calibrated universal theme.\n * dp -> CSS px assumes logical 1:1 units; sp -> px assumes Android fontScale=1.\n * Retain original units/provenance in design-tokens.json.\n * Explicit [data-mi-theme] opt-in: app day/night mode was not observed at runtime.\n * MIUI adaptive/smooth corners require a platform implementation; card radius below is only the XML corner value. */\n:root, [data-mi-theme="day"] {\n';
for(const [key,entry] of Object.entries(semantic))if(entry.day.color)css+=`  /* ${entry.ref}; ${entry.usage} */\n  --mi-${key}: ${entry.day.color.css};\n`;
for(const [key,[name,unit]] of Object.entries(geometry)){const r=resolve('@dimen/'+name);if(r.dimension)css+=`  /* @dimen/${name}: ${r.value} */\n  --mi-${key}: ${r.dimension.value}px;\n`;}
css+='  /* XML literal: layout_home_header_title_item.xml; system face, fallback is a web adaptation. */\n  --mi-page-title-size: 32px;\n  --mi-page-title-family: "sans-serif-light", sans-serif;\n  /* XML FontRegular family resolves to mipro-regular; system fallback is platform-dependent. */\n  --mi-font-family: "mipro-regular", sans-serif;\n}\n[data-mi-theme="night"] {\n';
for(const [key,entry] of Object.entries(semantic))if(entry.night.color)css+=`  --mi-${key}: ${entry.night.color.css};\n`;
css+='}\n/* Unknown/conditional and deliberately unset:\n * navigation icon sizes, universal shadows, global motion durations,\n * keyboard/status/navigation insets, global page gutter (legacy native mine uses 14dp; buttons use 26dp),\n * universal button radius (MIUI AdaptRoundButtonDrawable uses adaptive radii).\n */\n';
fs.writeFileSync(path.join(out,'tokens.css'),css);
console.log(JSON.stringify({values:Object.keys(values).length,semantic:Object.keys(semantic).length,styles:styles.length,layouts:layouts.length,dependencies:drawables.length,fonts:fonts.length}));

