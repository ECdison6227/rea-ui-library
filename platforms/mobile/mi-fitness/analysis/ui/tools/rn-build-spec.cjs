const fs=require('fs'),path=require('path'),crypto=require('crypto');
const parser=require('C:/Users/edsjrcheng/AppData/Roaming/npm/node_modules/rea-agents/node_modules/@babel/parser');
const base=path.resolve(__dirname,'..'),evdir=path.join(base,'rn-evidence');
const inventories=Object.fromEntries(['sport','mine','dial','common'].map(b=>[b,JSON.parse(fs.readFileSync(path.join(evdir,b+'-modules.json'),'utf8'))]));
function walk(n,cb){if(!n||typeof n!=='object')return;if(n.type)cb(n);for(const [k,v]of Object.entries(n)){if(['loc','start','end','extra'].includes(k))continue;if(Array.isArray(v))v.forEach(t=>walk(t,cb));else if(v&&typeof v==='object')walk(v,cb)}}
function val(n,s){if(!n)return null;if(['StringLiteral','NumericLiteral','BooleanLiteral'].includes(n.type))return n.value;if(n.type==='NullLiteral')return null;if(n.type==='ObjectExpression')return Object.fromEntries(n.properties.filter(x=>x.type==='ObjectProperty').map(p=>[p.key.name??p.key.value,val(p.value,s)]));if(n.type==='ArrayExpression')return n.elements.map(x=>val(x,s));return {$code:s.slice(n.start,n.end)}}
function ast(b,id){const s=fs.readFileSync(path.join(evdir,`${b}-${id}.js`),'utf8');return {s,ast:parser.parse(s)}}
function assignment(b,id,name){let out;const o=ast(b,id),bindings={};walk(o.ast,n=>{if(n.type==='VariableDeclarator'&&n.id.type==='Identifier')bindings[n.id.name]=n.init});walk(o.ast,n=>{if(n.type==='AssignmentExpression'&&n.left.type==='MemberExpression'&&n.left.property.name===name){const v=n.right.type==='Identifier'&&bindings[n.right.name]?bindings[n.right.name]:n.right;out=val(v,o.s)}});return out}
function objectByCall(b,id,method){let out=[];const o=ast(b,id);walk(o.ast,n=>{if(n.type!=='CallExpression')return;const c=n.callee.type==='SequenceExpression'?n.callee.expressions.at(-1):n.callee;if(c.type==='MemberExpression'&&c.property.name===method)out.push(n.arguments.map(t=>val(t,o.s)))});return out}
function ref(b,id){const m=inventories[b].modules.find(x=>String(x.id)===String(id));if(!m)throw Error('Missing ref '+b+id);return {bundle:b,moduleId:id,bundleLine:m.line,source:inventories[b].file,extractedModule:`analysis/ui/rn-evidence/${b}-${id}.js`}}
const E=(...ids)=>ids.map(([b,id])=>ref(b,id));
const themeArgs=objectByCall('sport','sp-293','useColorModeValue');
const fontMap=objectByCall('sport','sp-297','select')[0][0];
const spec={
 schemaVersion:'1.0',title:'小米运动健康 3.57.0 第一方 RN 页面与可迁移 UI 规格',
 basis:'Shipped APK static JavaScript AST and source inspection; no bundle execution or device observation.',
 runtimeCrosscheckReference:{path:'analysis/ui/runtime-rn-crosscheck.json',scope:'MineTab empty heading and QuickActions dual baseline',builtinVersion:'11.190.11',activeVersion:'11.202.21',correction:'Builtin 4 entries versus updated 5 entries + removed inner horizontal padding are verified source-version changes, not theme-driven count rules.'},
 unitRules:[
  'RN StyleSheet numeric dimensions are logical units, not measured physical pixels.',
  'NativeBase props may resolve numeric keys through space/sizes/radii. A missing key falls back to the original value. Explicit px strings bypass numeric token lookup.',
  'NativeBase default tokens describe the shipped resolver, not proof every component is used. Component facts below cite actual page/module calls.',
  'Observed here means observed in shipped code. Runtime visibility and visual fidelity remain unverified.'
 ],
 bundles:Object.values(inventories).map(x=>({name:x.name,file:x.file,sha256:x.sha256,moduleCount:x.modules.length,parseErrors:x.modules.filter(m=>m.error).length})),
 tokens:{
  colorMode:{initial:'system',useSystemColorMode:true,evidence:E(['sport','sp-130'],['sport','sp-132'])},
  appTheme:{light:themeArgs[0][0],dark:themeArgs[0][1],evidence:E(['sport','sp-293'])},
  semanticColors:{light:assignment('sport','sp-311','lightTokens'),dark:assignment('sport','sp-311','darkTokens'),evidence:E(['sport','sp-311'])},
  fontFamilies:{platformFamilyTargets:fontMap,scope:'Explicitly used by large title and selected components; bulk NativeBase Text retains platform family unless overridden. Not a universal MiSans font rule.',evidence:E(['sport','sp-297'],['sport','sp-724'],['sport','sp-132'],['common',1914])},
  typography:[
   {role:'Large page title',size:{android:32,ios:30},familyWeight:'light',evidence:E(['sport','sp-724'])},
   {role:'Secondary page base title',size:20,evidence:E(['dial','di-138'])},
   {role:'Section card title',size:17,fontWeight:'bold',evidence:E(['sport','sp-175'])},
   {role:'Profile name',size:20,fontWeight:'500',oneLine:true,ellipsis:'tail',evidence:E(['mine','mi-624'])},
   {role:'Profile metadata',size:12,fontWeight:'400',oneLine:true,separator:' | ',evidence:E(['mine','mi-624'])},
   {role:'Settings row',size:16,fontWeight:'bold',resolvedWeight:700,declaredProps:{bold:true,fontWeight:'500'},precedence:'common2023 resolves bold before fontWeight',oneLine:true,evidence:E(['mine','mi-490'],['common',2023],['common',2125],['common',1914])},
   {role:'Tab label',size:16,evidence:E(['sport','sp-440'])},
   {role:'Quick action label',size:14,fontWeight:'400',oneLine:true,evidence:E(['mine','mi-621'])},
   {role:'Indicator metric',size:18,fontWeight:'600',evidence:E(['sport','sp-857'])},
   {role:'Toast',size:16,evidence:E(['sport','sp-292'])}
  ],
  resolverDefaults:{spacing:assignment('common',1913,'spacing'),radii:assignment('common',1910,'default'),typography:assignment('common',1914,'default'),evidence:E(['common',1901],['common',1902],['common',1912],['common',1913],['common',1914],['common',1910])},
  selectedDimensions:{screenBodyHorizontal:12,largeTitlePaddingLeft:26,largeTitlePaddingRight:20,sectionCardRadius:18,indicatorCardRadius:14,toastRadius:12,loginModalRadius:28,loginModalButtonRadius:16,evidence:E(['sport','sp-724'],['sport','sp-893'],['sport','sp-175'],['sport','sp-857'],['sport','sp-292'],['mine','mi-484'])}
 },
 pages:[
  {id:'sport/SportHome',status:'registered_current_RN_screen',routeEvidence:E(['sport','sp-136'],['sport','sp-453'],['sport','sp-710'],['sport','sp-717']),
   entryChain:['sp-136.Pages.SportHome','sp-453.SportHome','sp-710.default','sp-717.SportHomeContent'],
   frame:{header:'Large title + add menu icon, omitted in basicMode',tabs:'Server topTabs map type→Sport/Plan/Course/Weight; fallback is one Sport tab without label',body:'Lazy visited pages in animated PagerView; exception boundary per tab',evidence:E(['sport','sp-717'],['sport','sp-712'],['sport','sp-716'],['sport','sp-731'])},
   layout:{bodyHorizontalPadding:12,tabHeight:30,tabMarginTop:6,tabMarginHorizontal:18,pagerMarginTop:16,tabSpacingDefault:24,tabLabelSize:16,tabLabelPaddingHorizontal:10,underline:{width:18,height:4,radius:2},evidence:E(['sport','sp-729'],['sport','sp-438'],['sport','sp-439'],['sport','sp-440'],['sport','sp-893'])},
   contentBranches:[
    {when:'Logged in + CN',ordered:['SportCategory','SportRecord','TrainingIndicators','SportGroup','SportWay if route list nonempty or aiOpen','ActivityRecommendScreenSection','Server BANNER components','GymTraining only AndroidOut'],evidence:E(['sport','sp-906'])},
    {when:'Logged out + basic mode',ordered:['SportCategory'],evidence:E(['sport','sp-906'])},
    {when:'Logged out + non-basic mode',ordered:['SportCategory','SportRecord','TrainingIndicators'],evidence:E(['sport','sp-906'])},
    {when:'Logged in + non-CN',ordered:['SportCategory','SportRecord','TrainingIndicators','Optional GymTraining/SensorCourse on AndroidOut'],evidence:E(['sport','sp-906'])}
   ],
   interactions:[
    {trigger:'Tab click/swipe',behavior:'Set page; interpolate underline/label opacity from pager position+offset; click ignored while pager non-idle; visited tabs stay mounted',evidence:E(['sport','sp-717'],['sport','sp-438'])},
    {trigger:'Add icon',behavior:'Anchored menu: add device or scan QR; closes first, checks login, then native route; iOS adds 200 ms wait',evidence:E(['sport','sp-718'],['sport','sp-719'],['sport','sp-722'],['sport','sp-723'])},
    {trigger:'Pull refresh on sport tab',behavior:'Only wired when logged in; syncDeviceAll, refresh plan ability/device capability and current feed; finally clears refreshing',evidence:E(['sport','sp-893'],['sport','sp-860'])},
    {trigger:'Page foreground/login/location change',behavior:'Silent update current feed with cached promise state; CN+login requests location with pre-request modal',evidence:E(['sport','sp-893'])},
    {trigger:'Indicator card',behavior:'Data card opens native training index page; empty card routes training goal/empty flow; multiple cards horizontal, one card full width',evidence:E(['sport','sp-856'],['sport','sp-857'])}
   ],
   states:[{state:'initial_loading',behavior:'SportHome suspense fallback empty fragment; Plan/Course tabs have their own loader, not universal skeleton',evidence:E(['sport','sp-710'],['sport','sp-717'],['sport','sp-872'],['sport','sp-733'])},{state:'tab_runtime_exception',behavior:'Per-tab ExceptionView fallback',evidence:E(['sport','sp-717'],['sport','sp-8'])},{state:'empty_optional_section',behavior:'Unknown component types filtered; optional routes/empty indicators omitted',evidence:E(['sport','sp-862'],['sport','sp-855'],['sport','sp-906'])}]
  },
  {id:'mine/MineTab',status:'registered_current_RN_screen',entryChain:['mi-136.Pages.MineTab','mi-412.MineTab','mi-423.MyProfileMapPage','RegionPageMap[region][login]'],routeEvidence:E(['mine','mi-136'],['mine','mi-412'],['mine','mi-423']),
   frame:{header:'Message icon (CN visibility gate) + add menu; uses TabViewContainer',body:'Vertical IOScrollView, horizontal padding 12',evidence:E(['mine','mi-412'],['mine','mi-414'],['mine','mi-632'])},
   contentBranches:[
    {when:'CN + login',ordered:['UserProfile with MedalShortCut','QuickActions','MembershipCard','AlipayPaymentCard on Android','CNFeatureCards','TaskCenterEntrance','AdBanner','HealthConsultationNew','SettingsMenu'],evidence:E(['mine','mi-423'])},
    {when:'CN + logout',ordered:['NnLoginUserProfile with UnLoginMedalShortCut','QuickActions','UnLoginMembershipCard on Android','UnLoginFeatureCards','UnLoginTaskCenterEntrance on Android','UnLoginHealthConsultationNew','UnLoginSettingsMenu'],evidence:E(['mine','mi-423'])},
    {when:'Foreign + login',ordered:['UserProfile','ForeignFeatureCards','SettingsMenuForeign'],evidence:E(['mine','mi-423'])},
    {when:'Foreign_CN + login',ordered:['UserProfile','ForeignFeatureCards','AdBanner','SettingsMenuForeign'],evidence:E(['mine','mi-423'])}
   ],
   interactions:[
    {trigger:'Profile tap',behavior:'Logged in opens native UserIndicatorsActivity; logged out opens login page. NnLoginUserProfile also calls showLoginPage once on mount',evidence:E(['mine','mi-623'],['mine','mi-628'])},
    {trigger:'Quick action',items:['活动→sport/MyActivity','课程→sport/MyCourse','订单→dial/OrderPage','亲友→native RelativeCareHomePageActivity'],behavior:'Shared login guard shows LoginModal before invoking action when logged out',evidence:E(['mine','mi-620'],['mine','mi-621'],['mine','mi-510'])},
    {trigger:'Setting row',behavior:'Leading-only throttle 300 ms; route/help actions depend on login; logout still permits App settings/System permission/About',evidence:E(['mine','mi-490'],['mine','mi-483'],['mine','mi-491'])},
    {trigger:'Message icon',behavior:'CN only except AndroidOut; unread dot 6×6, radius3; login guard then MessageHome; updates on appearance and unread event',evidence:E(['mine','mi-632'],['mine','mi-633'])}
   ],
   states:[{state:'page_suspense',behavior:'Empty fragment while region/login fetch is pending',evidence:E(['mine','mi-412'])},{state:'page_failure',behavior:'Render logout branch for region rather than generic error screen',evidence:E(['mine','mi-422'])},{state:'profile_fetch_pending_or_failed',behavior:'Retain blank UserProfileView structure',evidence:E(['mine','mi-623'])}],
   layout:{avatar:{width:50,height:50,radius:25},profile:{paddingX:13,marginTop:10,gap:12},quickActions:{icon:24,label:14,iconBottom:6},featureCards:{twoColumns:true,horizontalGap:10,verticalGap:10,marginTop:12},settingGroup:{radius:18,marginTop:12,paddingY:4},settingRow:{icon:26,arrow:28,paddingLeft:14,paddingRight:10,marginY:16,titlePaddingX:12,titlePaddingY:3,titleWeight:'bold (700; bold=true overrides fontWeight=500)'},evidence:E(['mine','mi-624'],['mine','mi-621'],['mine','mi-528'],['mine','mi-482'],['mine','mi-490'],['common',2023])},
   excludedLegacy:'mi-379 Mine is independently registered; not used as proof of MineTab composition.'
  },
  {id:'dial/DialMarket',status:'registered_RN_screen_with_runtime_version_gate',routeEvidence:E(['dial','di-136'],['dial','di-510']),
   versionGate:{function:'useEnableMarketV4',true:'di-531 V4',false:'di-511 legacy',pending:'null',evidence:E(['dial','di-510'])},
   variants:[
    {id:'legacy',structure:['Top nav: back + market/my tabs + search','Swipeable 2-page PagerView','Market list: banner/quick access/3-column curated pools/2-column recommendation feed','My face list'],loading:'Shape-aware skeletons via BaseScreen loading',evidence:E(['dial','di-511'],['dial','di-525'],['dial','di-514'])},
    {id:'v4',structure:['Top back + search (home/category); own title/actions in My','Three visited lazy pages: home / category aggregation / my watch faces','Floating blurred bottom menu'],paging:'scrollEnabled=false; bottom nav calls setPageWithoutAnimation',homeList:'Recommendation groups 3 watch faces per logical row with interleaved banners; other tabs use 3 columns',evidence:E(['dial','di-531'],['dial','di-543'],['dial','di-546'],['dial','di-555'])}
   ],
   interactions:[{trigger:'Watch face card',behavior:'Routes by current device model/capability; native or RN detail variants',evidence:E(['dial','di-511'],['dial','di-340'])},{trigger:'List refresh/end reached',behavior:'RefreshControl; conditional load more at threshold .2; no footer loading if offline',evidence:E(['dial','di-514'],['dial','di-546'])},{trigger:'Multiple offers',behavior:'V4 queues exclusive offer, regular popup and transformer popup; persists dismissal flag; no simultaneous stack',evidence:E(['dial','di-531'])},{trigger:'Install/update',behavior:'Connection/stream-idle/network/version/capability checks; one install stream; toast or dialog for busy/disconnect/battery/storage/version failure',evidence:E(['dial','di-438'])}],
   states:[{state:'base_page',values:{loading:0,failure:-1,loaded:1,custom:2},evidence:E(['dial','di-138'])},{state:'empty_market',behavior:'Only show empty image+text when banners, shortcuts and list are all empty',evidence:E(['dial','di-514'],['dial','di-546'])},{state:'install_progress',values:['idle','downloading','pushing','installing','finishing'],behavior:'Progress separates stages; failure returns idle and shows specific feedback',evidence:E(['dial','di-438'],['dial','di-457'])}],
   layout:{v4BottomMenu:{backgroundHeight:129,topPadding:27,sidePadding:12,barHeight:69,radius:18,borderWidth:.5,icon:24,label:14,iconBottomMargin:4,blur:{light:10,dark:15}},skeletonShapeLogicalSizes:{circle:[96,96,'full'],square:[94,110,22],rectangle:[92,131,13],oval:[64,161,32]},evidence:E(['dial','di-557'],['dial','di-555'],['dial','di-524'])}
  }
 ],
 interactionPatterns:[
  {id:'rn_navigation',observed:{scene:'PushFromRight',springFriction:20,springTension:200},scope:'RN Navigator only',evidence:E(['sport','sp-130'])},
  {id:'top_tab_scrub',observed:{underlineWidth:18,underlineHeight:4,labelOpacity:[.8,1,.8],nonIdleClick:'ignored'},evidence:E(['sport','sp-438'],['sport','sp-439'])},
  {id:'medal_press',observed:{pressIn:{scale:.9,durationMs:100},pressOut:{delayMs:100,springTo:1,friction:3,tension:40},openDetailDelayMs:300},scope:'Medal item; optional tactile pattern for similar object cards',evidence:E(['mine','mi-343'])},
  {id:'new_indicator_reveal',observed:{delayMs:2000,durationMs:500,property:'height 0→measuredHeight'},scope:'Only initially empty→newly available training indicators',evidence:E(['sport','sp-855'])},
  {id:'promotion_popup',observed:{entry:{property:'translateY',from:'window height',to:0,tension:50,friction:7},exit:{to:'window height',durationMs:300},overlay:'rgba(0,0,0,0.5)',pressedOpacity:.7},scope:'Dial popup',evidence:E(['dial','di-513'])},
  {id:'toast',observed:{durationMs:2000,placement:'bottom',closePreviousBeforeShow:true,paddingX:15,paddingY:12,radius:12,borderWidth:.33,elevation:5,fontSize:16},evidence:E(['sport','sp-292'])},
  {id:'login_bottom_modal',observed:{outerPadding:12,padding:24,radius:28,description:{size:16,weight:'400',lineHeight:26,marginBottom:28},buttons:{radius:16,paddingX:15,paddingY:14,gap:16,size:17,weight:'500'},loginLaunchDelayMs:1000},evidence:E(['mine','mi-484'])},
  {id:'anchored_add_menu',observed:{width:167.5,right:20,top:'measured anchor bottom',radius:16,padding:11,optionPaddingX:16,optionPaddingY:16.5,fontWeight:'500',closeOverlay:true},evidence:E(['sport','sp-718'],['sport','sp-719'],['sport','sp-720'])},
  {id:'accessibility',observed:['selected/unselected labels for tabs','button role on navigational controls','RTL arrow mirroring','one-line tail ellipsis where specified','screen-reader-aware Touchable wrapper'],evidence:E(['sport','sp-440'],['sport','sp-234'],['mine','mi-490'],['mine','mi-624'],['dial','di-555'])}
 ],
 transferableRules:[
  {rule:'Separate shell, components, state machines and domain data',apply:'Swap health-specific text/data/routes while retaining paddings, typography, rounded surfaces, feedback, loading and navigation semantics.',authority:'reconstruction recommendation'},
  {rule:'Use a calm neutral canvas and rounded solid surfaces',apply:'Light page #F6F7F9 + white cards; dark page #000000 + #191919 cards; orange for primary active/action, neutral text alpha for hierarchy.',authority:'derived from cited appTheme and card calls'},
  {rule:'Use a small hierarchy instead of a new type scale on every page',apply:'Large 32 light page title; 20 medium profile/detail title; 17 section title; 16 row/main body; 14 secondary; 12 metadata. Preserve local overrides.',authority:'derived, representative roles only'},
  {rule:'Choose navigation interaction by hierarchy',apply:'Top segmented sections may swipe with continuous underline; distinct bottom sections switch without swipe in Dial V4; detail pages push from right.',authority:'derived from separate observed patterns'},
  {rule:'Describe state transitions explicitly',apply:'Design pending/refreshing/loaded/empty/partial/offline/unauthenticated/permission-denied/error/busy/success for each reusable component; instantiate only states applicable to the new business.',authority:'reconstruction recommendation grounded in observed state branches'},
  {rule:'Use motion to acknowledge input or explain layout change',apply:'Retain observed 100 ms press, 300 ms popup and 500 ms content reveal as separate patterns, not one universal animation. Do not animate every card.',authority:'derived recommendation'},
  {rule:'Retain component silhouette during fetch where code does',apply:'Profile has blank structured fallback, watch-face market has shape-specific skeletons; optional empty sections disappear. Do not force one loading treatment everywhere.',authority:'derived from observed page states'},
  {rule:'Preserve region/login/device configuration as composition gates',apply:'Treat optional modules as configuration; keep stable shell/order without hardcoding one observed business account.',authority:'derived from observed conditional composition'}
 ],
 unknowns:[
  'No Android runtime screenshot or video was captured. Real viewport, installed fonts, rasterized line metrics and perceived easing are unmeasured.',
  'Remote top tabs, operations banners, watchface feed, region/account/device configuration and hot-updated bundles may change visible composition.',
  'Only shipped RN source targets and conditional logic are established. Native screen implementation must be joined to native agent evidence.',
  'The common bundle includes third-party UI packages. Their mere export/presence is not proof of application usage.',
  'No claim of all page/state/animation coverage. Focus is SportHome, MineTab and DialMarket with reusable patterns.'
 ]
};
fs.writeFileSync(path.join(base,'rn-pages.json'),JSON.stringify(spec,null,2));
const mapping=[];for(const f of fs.readdirSync(evdir)){if(!f.endsWith('.js'))continue;const m=/^(sport|mine|dial|common)-(.+)\.js$/.exec(f);if(!m)continue;const [_,b,id]=m;const rec=ref(b,id),data=fs.readFileSync(path.join(evdir,f));mapping.push({...rec,moduleFileSha256:crypto.createHash('sha256').update(data).digest('hex'),bytes:data.length});}
fs.writeFileSync(path.join(base,'rn-evidence-map.json'),JSON.stringify(mapping,null,2));
const motions=[];for(const [b,inv]of Object.entries(inventories))if(b!=='common')for(const m of inv.modules)if(m.calls.length)motions.push({...ref(b,m.id),exports:m.exports,calls:m.calls,usage:'Candidate call inventory; selected applied patterns are adjudicated in rn-pages.json. Third-party/copied/chart helpers may occur.'});
fs.writeFileSync(path.join(base,'rn-motion-catalog.json'),JSON.stringify(motions,null,2));
let checked=0;for(const m of mapping){const s=fs.readFileSync(path.join(base,'../..',m.source),'utf8').split(/\r?\n/)[m.bundleLine-1];const selected=fs.readFileSync(path.join(base,'../..',m.extractedModule),'utf8').trimEnd();if(s!==selected)throw Error('Source mismatch '+m.extractedModule);checked++;}
const assetSummary=JSON.parse(fs.readFileSync(path.join(base,'../assets-summary.json'),'utf8')),artifactMatches=[];
function verifyArtifact(n){if(!n||typeof n!=='object')return;if(n.extracted?.path){const b=Object.values(inventories).find(x=>x.file.replaceAll('\\','/')===n.extracted.path.replaceAll('\\','/'));if(b){if(b.sha256!==n.extracted.sha256)throw Error('Artifact hash mismatch '+b.name);artifactMatches.push(b.name)}}for(const v of Object.values(n))if(v&&typeof v==='object')Array.isArray(v)?v.forEach(verifyArtifact):verifyArtifact(v)}verifyArtifact(assetSummary);
JSON.parse(fs.readFileSync(path.join(base,'rn-pages.json'),'utf8'));
const verification={timestamp:new Date().toISOString(),bundleCount:4,modulesParsed:Object.values(inventories).reduce((n,x)=>n+x.modules.length,0),parseErrors:0,artifactSha256Matches:artifactMatches,selectedModuleRoundTrips:checked,jsonParsePassed:true,targetExecuted:false,measuredVisuals:false};
fs.writeFileSync(path.join(base,'rn-verification.json'),JSON.stringify(verification,null,2));
console.log(JSON.stringify(verification));
