const fs=require('fs'),path=require('path'),crypto=require('crypto');
const parser=require('C:/Users/edsjrcheng/AppData/Roaming/npm/node_modules/rea-agents/node_modules/@babel/parser');
const base=path.resolve(__dirname,'..'),root=path.resolve(base,'../..');
const sha=p=>crypto.createHash('sha256').update(fs.readFileSync(p)).digest('hex');
const ids=['11-mine-after-login-cancel','17c-mine-night-visible'];
const names=['我的活动','我的课程','我的订单','我的亲友','我的奖品'];
const rect=s=>s.match(/\d+/g).map(Number);
const snapshots=ids.map(id=>{
 const file=path.join(base,'runtime/hierarchy',id+'.json'),j=JSON.parse(fs.readFileSync(file,'utf8'));
 const items=[];
 for(let i=0;i<j.nodes.length;i++){
  const n=j.nodes[i];if(!names.includes(n.text))continue;
  // In these flattened hierarchy captures the wrapper/image/label sequence is
  // stable. Verify it before identifying the wrapper as the item's click target.
  const press=j.nodes[i-3],image=j.nodes[i-1];
  if(press?.clickable!=='true'||image?.class!=='android.widget.ImageView')throw Error('Unexpected hierarchy order '+id+' node '+i);
  const b=rect(press.bounds),ib=rect(image.bounds);
  items.push({label:n.text,labelNode:i,labelBoundsPx:n.bounds,clickTargetNode:i-3,clickBoundsPx:press.bounds,widthPx:b[2]-b[0],heightPx:b[3]-b[1],iconBoundsPx:image.bounds,iconSizePx:[ib[2]-ib[0],ib[3]-ib[1]]});
 }
 const title=j.nodes.map((n,i)=>({n,i})).find(x=>x.n.text===' ');
 const min=Math.min(...items.map(x=>rect(x.clickBoundsPx)[0])),max=Math.max(...items.map(x=>rect(x.clickBoundsPx)[2]));
 return {id,at:j.at,path:path.relative(root,file).replaceAll('\\','/'),sha256:sha(file),entryCount:items.length,entryOrder:items.map(x=>x.label),items,rowBoundsPx:{left:min,right:max,width:max-min},rowInsetsApproxDp:{left:min/2.75,right:(1080-max)/2.75},title:{text:title?.n.text,node:title?.i,boundsPx:title?.n.bounds}};
});
if(snapshots[0].entryCount!==4||snapshots[1].entryCount!==5)throw Error('Unexpected runtime entry counts');
const sourceFiles=['mine-mi-412.js','mine-mi-414.js','mine-mi-620.js','mine-mi-621.js','mine-mi-512.js','mine-mi-408.js','mine-mi-423.js'];
const sourceEvidence=sourceFiles.map(file=>({file:'analysis/ui/rn-evidence/'+file,sha256:sha(path.join(base,'rn-evidence',file))}));
const bundle=fs.readFileSync(path.join(root,'analysis/assets/extracted/fc87206bd4201baa-index.android.bundle'),'utf8');
const checks={};for(const q of ['lottery','mine.assets.item.prize','mine.assets.item.award','mine.assets.item.lottery','showPrize','showLottery','奖品'])checks[q]=bundle.toLowerCase().includes(q.toLowerCase());
const logFile=path.join(base,'runtime/logs/rn-bundle-log-probe.json');
const logs=JSON.parse(fs.readFileSync(logFile,'utf8'));
const runtimeMineMetadata=logs.matching.filter(x=>x.text.includes('RRLog')&&x.text.includes('"module": "mine"')).map(x=>({processLogLine:x.line,logTimestamp:x.text.slice(0,18),metadata:JSON.parse(x.text.slice(x.text.indexOf('{'),x.text.lastIndexOf('}')+1))}));
const builtinInfoPath=path.join(root,'analysis/assets/extracted/7ac11763bcbe7105-bundleInfo.json');
const builtinMineMetadata=JSON.parse(fs.readFileSync(builtinInfoPath,'utf8')).find(x=>x.moduleName==='mine');
function walk(n,cb){if(!n||typeof n!=='object')return;if(n.type)cb(n);for(const[k,v]of Object.entries(n)){if(['loc','start','end','extra'].includes(k))continue;if(Array.isArray(v))v.forEach(x=>walk(x,cb));else if(v&&typeof v==='object')walk(v,cb)}}
function staticValue(n,s){if(!n)return null;if(['StringLiteral','NumericLiteral','BooleanLiteral'].includes(n.type))return n.value;if(n.type==='NullLiteral')return null;if(n.type==='ObjectExpression')return Object.fromEntries(n.properties.filter(x=>x.type==='ObjectProperty').map(x=>[x.key.name??x.key.value,staticValue(x.value,s)]));return{$code:s.slice(n.start,n.end)}}
const activeBundlePath=path.join(base,'runtime/active-bundles/mine/code/index.android.bundle'),activeBundleSha256=sha(activeBundlePath);
if(activeBundleSha256!=='4f8012fb1ed726d3ef11f068ff7f42b6e74def6059eddaff3426b4dbb7a06958')throw Error('Unexpected active mine hash');
const activeLines=fs.readFileSync(activeBundlePath,'utf8').split(/\r?\n/),activeModules=[];
for(const moduleId of ['mi-412','mi-414','mi-406','mi-512','mi-621','mi-622']){
 const lineIndex=activeLines.findIndex(x=>x.includes('},"'+moduleId+'",['));if(lineIndex<0)throw Error('Missing module '+moduleId);
 const raw=activeLines[lineIndex],a=parser.parse(raw),call=a.program.body[0].expression;if(call.arguments[1].value!==moduleId)throw Error('Module mismatch '+moduleId);
 const destination=path.join(base,'rn-evidence','active-mine-'+moduleId+'.js');fs.writeFileSync(destination,raw+'\n');
 activeModules.push({moduleId,bundleLine:lineIndex+1,file:path.relative(root,destination).replaceAll('\\','/'),moduleFileSha256:sha(destination),source:path.relative(root,activeBundlePath).replaceAll('\\','/'),sourceSha256:activeBundleSha256});
}
function quickAst(file){const s=fs.readFileSync(file,'utf8'),a=parser.parse(s),entries=[];let rowProps,rowChildNodeTypes;
 walk(a,n=>{
  if(n.type!=='CallExpression'||n.callee.type!=='MemberExpression'||n.callee.property.name!=='createElement'||n.arguments[0]?.type!=='MemberExpression')return;
  if(n.arguments[0].property.name==='EntranceItem')entries.push({props:staticValue(n.arguments[1],s),translationKey:n.arguments[2]?.arguments?.[0]?.value});
  if(n.arguments[0].property.name==='HStack'){rowProps=staticValue(n.arguments[1],s);rowChildNodeTypes=n.arguments.slice(2).map(x=>x.type)}
 });return{entries,count:entries.length,rowProps,rowChildNodeTypes,entryCallsUnconditionalInRow:rowChildNodeTypes.every(x=>x==='CallExpression')};
}
const baselineQuickAst=quickAst(path.join(base,'rn-evidence/mine-mi-620.js')),activeQuickAst=quickAst(path.join(base,'rn-evidence/active-mine-mi-621.js'));
if(baselineQuickAst.count!==4||activeQuickAst.count!==5||baselineQuickAst.rowProps.px!=='14px'||'px'in activeQuickAst.rowProps)throw Error('QuickActions comparison unexpected');
function factorySource(file){const s=fs.readFileSync(file,'utf8'),n=parser.parse(s).program.body[0].expression.arguments[0];return s.slice(n.start,n.end)}
const unchangedFactories={entranceItem:factorySource(path.join(base,'rn-evidence/mine-mi-621.js'))===factorySource(path.join(base,'rn-evidence/active-mine-mi-622.js')),titleContainer:factorySource(path.join(base,'rn-evidence/mine-mi-414.js'))===factorySource(path.join(base,'rn-evidence/active-mine-mi-414.js'))};
const activeConfigPath=path.join(base,'runtime/active-bundles/mine/config/buz.config.json');
const result={
 title:'MineTab 运行时与内置 RN 静态基线窄交叉验证',
 scope:'Two existing hierarchy captures, builtin modules, read-only PID logs, and narrow AST comparison of the parent-retained active mine bundle. No new UI action, shared REA session, Java analysis or target bundle execution.',
 environment:{source:'Parent agent runtime environment record plus read-only adb timezone query',android:15,api:35,viewportPx:[1080,2400],densityDpi:440,densityScale:2.75,fontScale:1,locale:'zh-CN',device:'emulator-5580',deviceTimezone:'GMT',adbDateSample:'2026-10-15T12:08:49+0000'},
 snapshots,sourceEvidence,
 runtimeBundleIdentity:{authority:'Observed process log metadata + retained active bundle file SHA-256 + narrow source AST',logEvidence:{path:path.relative(root,logFile).replaceAll('\\','/'),sha256:sha(logFile),pid:logs.appPid},builtinInfoEvidence:{path:path.relative(root,builtinInfoPath).replaceAll('\\','/'),sha256:sha(builtinInfoPath)},builtinMineMetadata,runtimeMineMetadata,activeBundle:{file:path.relative(root,activeBundlePath).replaceAll('\\','/'),sha256:activeBundleSha256,reaAcquisitionEvidence:'ev_4b19bdb230e5223001a26c650fb1510cee45222ac78f7712f381ac8115c85bbb',config:JSON.parse(fs.readFileSync(activeConfigPath,'utf8')),configFile:path.relative(root,activeConfigPath).replaceAll('\\','/')},note:'Log md5 matches cache directory and builtin urlmd5. It is not represented here as the index.android.bundle file MD5 or SHA-256.'},
 activeModules,quickActionsAstComparison:{baseline:{version:'11.190.11',moduleId:'mi-620',...baselineQuickAst},active:{version:'11.202.21',moduleId:'mi-621',...activeQuickAst},unchangedFactories,result:'Updated code unconditionally has 5 entry calls, reorders friend/order, adds prize, and removes row px=14px. This accounts for the observed variants without a theme-dependent count rule.',prizeAction:{authority:'source-call only, not exercised',nativeRoute:'com.xiaomi.fitness.webview.healthwebview.HealthWebViewActivity',urlExpression:'https:// + getHttpHost(HostType.watch) + /mobile/lotteryRecord?channelType=5',loginGate:'Shared EntranceItem calls useLoginStatusCheck before onPress',theme:'prize_light.webp / prize_dark.webp changes icon only'}},
 sourceFindings:{
  title:{authority:'shipped-source + observed-runtime',rule:'mi-412 passes icons but omits title; mi-414 defaults undefined title to one space. Both captures node16 contain one space, not 我的.',visiblePageHeading:false},
  quickActions:{authority:'shipped-source',builtinCount:4,builtinOrder:['我的活动','我的课程','我的订单','我的亲友'],dynamicCountConditionFound:false,sourceChain:['mi-412.MineTab','mi-423.MyProfileMapPage','mi-512.QuickActions','mi-620.QuickActions','mi-621.EntranceItem'],layout:{row:{mtDp:8,pxDp:14,ptDp:18,mbDp:5,justifyContent:'space-between'},item:{flex:1,alignItems:'center',justifyContent:'center'},icon:{widthDp:24,heightDp:24,marginBottomDp:6},label:{fontSizeDp:14,fontWeight:'400',paddingTopDp:2,paddingBottomDp:10,numberOfLines:1}},themeBehavior:'mi-621 changes only light/dark icon source; does not change count or ordering. mi-620 has no color-mode branch.',wrapper:'mi-408 withRenderFC logs lifecycle and passes props through; it does not inject entries.'},
  keywordPresence:checks
 },
 adjudication:{
  countAndOrder:'Runtime 4→5 count and reordering are confirmed observations. Process logs and retained active bundle establish version11.190.11→11.202.21 change between captures; new QuickActions source unconditionally includes prize and the observed order.',
  layout:'4-entry row uses approx26dp total left/right inset, matching outer12dp + builtin inner14dp. 5-entry row uses12dp total inset. Count is not the only changed layout parameter.',
  preserved:'Both variants keep equal-width items, 24dp icons and the same icon/label vertical bounds in these captures.',
  themeCausality:'Count/order/inset difference is explained by the verified business-bundle source version change. Neither QuickActions version contains a theme-dependent entry-count branch.',
  unknowns:['Prize destination runtime behavior after its source-defined navigation; not exercised','What caused the updater to fetch this package at that moment; not needed to establish the UI code difference']
 },
 reconstructionRule:{authority:'Configurable cross-business implementation is a recommendation; both version-specific fixtures are source- and runtime-confirmed',entries:'Use an ordered entry list from product configuration; render N equal-width items. Preserve 4-entry and 5-entry source-version fixtures with separate insets. Do not invent a theme-dependent count switch.',variants:[{version:'11.190.11',count:4,order:snapshots[0].entryOrder,totalRowInsetDpApprox:26},{version:'11.202.21',count:5,order:snapshots[1].entryOrder,totalRowInsetDpApprox:12}],title:'Leave the large title text blank for MineTab while preserving header height and message/add controls.'},
  verification:{hierarchyItemOrderVerified:true,sourceBundleReadOnly:true,noTargetUiActions:true,activeBundleSha256Matched:true,activeModulesParsed:6,narrowScopeOnly:true}
};
fs.writeFileSync(path.join(base,'runtime-rn-crosscheck.json'),JSON.stringify(result,null,2));
const mainSpecPath=path.join(base,'rn-pages.json'),mainSpec=JSON.parse(fs.readFileSync(mainSpecPath,'utf8'));
mainSpec.runtimeCrosscheckReference={path:'analysis/ui/runtime-rn-crosscheck.json',scope:'MineTab empty heading and QuickActions dual baseline',builtinVersion:'11.190.11',activeVersion:'11.202.21',correction:'Builtin 4 entries versus updated 5 entries + removed inner horizontal padding are verified source-version changes, not theme-driven count rules.'};
fs.writeFileSync(mainSpecPath,JSON.stringify(mainSpec,null,2));
console.log(JSON.stringify({snapshots:result.snapshots.map(s=>({id:s.id,entryCount:s.entryCount,entryOrder:s.entryOrder,rowBoundsPx:s.rowBoundsPx,title:s.title})),keywordPresence:checks}));
