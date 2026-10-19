import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';

// Reads retained UIAutomator XML only; does not connect to or operate a device.
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const definitions=[
 ['privacy.disagree','03-kimi-privacy','[186,1354][540,1480]','Disagree'],
 ['privacy.agree','03-kimi-privacy','[541,1354][895,1480]','Agree'],
 ['home.navigation','08-guest-home','[37,131][163,257]','Navigation button'],
 ['home.announcement','08-guest-home','[917,131][1043,257]','Enable voice announcement'],
 ['home.model-capsule','08-guest-home','[190,131][483,257]','Instant / High'],
 ['home.new-project-chip','08-guest-home','[42,1948][372,2074]','New project'],
 ['home.slides-chip','08-guest-home','[393,1948][627,2074]','Slides'],
 ['home.swarm-chip','08-guest-home','[648,1948][897,2074]','Swarm'],
 ['home.composer-container','08-guest-home','[32,2101][1048,2269]','Composer container'],
 ['home.composer-editor','08-guest-home','[211,2122][900,2248]','Empty EditText'],
 ['home.more-input','08-guest-home','[53,2122][179,2248]','More input options'],
 ['home.voice-input','08-guest-home','[900,2122][1026,2248]','Switch to voice input'],
 ['drawer.settings','10-composer-focus','[754,163][880,289]','Gear icon'],
 ['drawer.login-cta','10-composer-focus','[354,1537][564,1663]','Log in'],
 ['settings.notifications-row','11-settings','[42,318][1038,465]','Notifications'],
 ['settings.appearance-row','11-settings','[42,466][1038,613]','Appearance'],
 ['settings.features-row','11-settings','[42,614][1038,761]','Feature management'],
 ['settings.login-row','11-settings','[42,1505][1038,1631]','Log In'],
 ['theme.dark-option','14-dark-theme','[42,593][996,740]','Dark mode']
];
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const decode=s=>s.replace(/&(?:quot|apos|lt|gt|amp);|&#(?:x[0-9a-f]+|[0-9]+);/gi,e=>({ '&quot;':'"','&apos;':"'",'&lt;':'<','&gt;':'>','&amp;':'&' }[e]??String.fromCodePoint(e[2].toLowerCase()==='x'?parseInt(e.slice(3,-1),16):parseInt(e.slice(2,-1),10))));
const documents=new Map();
function readScene(scene){
 if(documents.has(scene))return documents.get(scene);
 const dir=path.join(root,'evidence/runtime/scenes',scene);
 const xml=fs.readFileSync(path.join(dir,'hierarchy.xml'));
 const capture=JSON.parse(fs.readFileSync(path.join(dir,'capture.json'),'utf8'));
 if(capture.hierarchy.sha256!==hash(xml))throw Error(`Hierarchy hash differs: ${scene}`);
 const densityDpi=Number(capture.conditions.density.match(/Physical density:\s*(\d+)/)?.[1]);
 if(densityDpi!==420)throw Error(`Unexpected density for these scoped observations: ${scene}`);
 const nodes=[...xml.toString('utf8').matchAll(/<node\b([^>]+)>/g)].map(m=>Object.fromEntries([...m[1].matchAll(/([\w-]+)="([^"]*)"/g)].map(a=>[a[1],decode(a[2])])));
 const d={nodes,capture,densityDpi,densityFactor:densityDpi/160};documents.set(scene,d);return d;
}
const measurements=definitions.map(([id,scene,bounds,visibleContext])=>{
 const d=readScene(scene),matches=d.nodes.filter(n=>n.bounds===bounds&&n.clickable==='true');
 // The selected theme row reports clickable=false while checked=true.
 if(id==='theme.dark-option')matches.splice(0,matches.length,...d.nodes.filter(n=>n.bounds===bounds&&n.checkable==='true'));
 if(matches.length!==1)throw Error(`${id}: expected one exact semantic node, got ${matches.length}`);
 const n=matches[0],m=bounds.match(/^\[(\d+),(\d+)\]\[(\d+),(\d+)\]$/);
 const [left,top,right,bottom]=m.slice(1).map(Number),width=right-left,height=bottom-top;
 const dp=x=>Number((x/d.densityFactor).toFixed(6));
 return {id,classification:'A',origin:'current',scene,visibleContext,source:{hierarchy:`evidence/runtime/scenes/${scene}/hierarchy.xml`,hierarchySha256:d.capture.hierarchy.sha256,capture:`evidence/runtime/scenes/${scene}/capture.json`,screenshot:d.capture.screenshot.path,screenshotSha256:d.capture.screenshot.sha256,screenshotCapturedAt:d.capture.screenshotCapturedAt??null,captureCompletedAt:d.capture.capturedAt,atomicCapture:false},node:{class:n.class,text:n.text,contentDescription:n['content-desc'],package:n.package,originalBounds:n.bounds,clickable:n.clickable==='true',enabled:n.enabled==='true',focusable:n.focusable==='true',focused:n.focused==='true',checkable:n.checkable==='true',checked:n.checked==='true'},physicalPx:{left,top,right,bottom,width,height},densityDpi:d.densityDpi,densityFactor:d.densityFactor,dpFromBounds:{left:dp(left),top:dp(top),right:dp(right),bottom:dp(bottom),width:dp(width),height:dp(height)},promotedToGlobalToken:false};
});
const result={schemaVersion:1,kind:'scoped-native-semantic-geometry-observations',sourcePackage:'com.moonshot.kimichat',sourceVersion:'3.1.3 (376)',apkSha256:'9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332',classification:'A',origin:'current',conversion:{formula:'dp = physicalPx / (densityDpi / 160)',densityDpi:420,densityFactor:2.625,scope:'Only retained captures for this 1080x2400 device at recorded density',rounding:'JSON decimal dp values rounded to 6 decimal places; additional decimals do not imply new measurement precision'},measurements,unknowns:['App UI colors as native tokens','Font family, sp size, font weight and line height','Visual circle radius, corner radius, shadow extent and touch feedback','Other devices, density, font scale, orientation or app versions'],limitations:['Bounds are runtime UIAutomator semantic node rectangles, not glyph extents or visual shape sizes.','An enabled/clickable node does not prove its action result or full interaction state machine.','Screenshot and hierarchy were captured sequentially; bounds are not guaranteed to describe the exact screenshot instant.','A repeated 48dp or 56dp semantic dimension in these instances is not automatically a global product token.','No colors are inferred from binary-resource defaults or filled into this file.','visibleContext is an analyst-readable component locator; node.text and node.contentDescription preserve the actual node values.']};
fs.mkdirSync(path.join(root,'tokens'),{recursive:true});
fs.writeFileSync(path.join(root,'tokens/native-observed.json'),JSON.stringify(result,null,2)+'\n');
console.log(JSON.stringify({output:'tokens/native-observed.json',measurements:measurements.length,semanticButton48dp:measurements.filter(m=>m.dpFromBounds.width===48&&m.dpFromBounds.height===48).map(m=>m.id),settingsRowHeights:measurements.filter(m=>m.id.startsWith('settings.')).map(m=>({id:m.id,heightDp:m.dpFromBounds.height}))},null,2));
