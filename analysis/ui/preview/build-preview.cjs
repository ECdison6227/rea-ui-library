// Builds a self-contained local data snapshot. Original specifications are read only.
const fs=require('node:fs'),path=require('node:path'),crypto=require('node:crypto');
const here=__dirname,ui=path.dirname(here),workspace=path.resolve(ui,'../..');
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
function input(name){const b=fs.readFileSync(path.join(ui,name));return {value:JSON.parse(b),source:name,sha256:hash(b),bytes:b.length};}
const native=input('design-tokens.json'),components=input('components.json'),rn=input('rn-pages.json');
fs.mkdirSync(path.join(here,'fonts'),{recursive:true});
const fontFiles=['misanslatin_regular.otf','misanslatin_medium.otf','misanslatin_demibold.otf','misanslatin_bold.otf'];
const fontManifest=fontFiles.map(name=>{const actual=path.join(ui,'../phase2/apktool/res/font',name);const bytes=fs.readFileSync(actual);fs.writeFileSync(path.join(here,'fonts',name),bytes);return {file:'fonts/'+name,source:path.relative(workspace,actual).replaceAll('\\','/'),sha256:hash(bytes),bytes:bytes.length,note:'Copied for local preview; Latin glyph probes only. Chinese uses the host system fallback.'};});
const generatedAt=new Date().toISOString();
fs.writeFileSync(path.join(here,'fonts/manifest.json'),JSON.stringify({generatedAt,fonts:fontManifest},null,2)+'\n');
const manifest={generatedAt,scope:'Offline study display snapshot; does not modify or execute the APK or its bundles',sources:[native,components,rn].map(({source,sha256,bytes})=>({source,sha256,bytes})),fonts:fontManifest,provenance:'Templates are reconstructed cross-business examples; runtime/screens.json is separately fetched and is never synthesized.'};
fs.writeFileSync(path.join(here,'snapshot-manifest.json'),JSON.stringify(manifest,null,2)+'\n');
const snapshot={native:native.value,components:{componentContracts:components.value.componentContracts,rowCodeContracts:components.value.rowCodeContracts},rn:rn.value,snapshot:manifest};
fs.writeFileSync(path.join(here,'data.js'),'// Generated offline snapshot; run build-preview.cjs to refresh.\nwindow.__UI_STUDY_DATA__ = '+JSON.stringify(snapshot).replaceAll('<','\\u003c')+';\n');
console.log(JSON.stringify({snapshotBytes:fs.statSync(path.join(here,'data.js')).size,fontFiles:fontFiles.length,generatedAt}));
