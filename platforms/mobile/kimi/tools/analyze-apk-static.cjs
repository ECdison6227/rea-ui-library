#!/usr/bin/env node
/* Read-only APK inspection using only Node built-ins. Never executes the app.
 * Usage: node analyze-apk-static.cjs APK OUTPUT_DIRECTORY
 * Supported: ZIP central directory (stored/deflate), Android binary XML,
 * standard Android resource tables, DEX identifier and class definition tables.
 * Not a DEX decompiler; method names do not establish runtime behavior.
 */
'use strict';
const fs = require('node:fs');
const path = require('node:path');
const zlib = require('node:zlib');
const crypto = require('node:crypto');
const apkPath = path.resolve(process.argv[2] || 'Kimi.apk');
const output = path.resolve(process.argv[3] || 'research/kimi-android/evidence/source');
fs.mkdirSync(output, {recursive: true});
const apk = fs.readFileSync(apkPath);
const hash = b => crypto.createHash('sha256').update(b).digest('hex');
const hex = n => '0x' + (n >>> 0).toString(16).padStart(8, '0');
const write = (name, value) => fs.writeFileSync(path.join(output, name), typeof value === 'string' ? value : JSON.stringify(value, null, 2) + '\n');
const limits = [];
const entries = [];
let eocd = apk.length - 22;
const minimumEocd = Math.max(0, apk.length - 65557);
for (; eocd >= minimumEocd; eocd--) if (apk.readUInt32LE(eocd) === 0x06054b50) break;
if (eocd < minimumEocd) throw new Error('ZIP EOCD not found');
let cd = apk.readUInt32LE(eocd + 16);
for (let i = 0; i < apk.readUInt16LE(eocd + 10); i++) {
  if (apk.readUInt32LE(cd) !== 0x02014b50) throw new Error('Invalid central directory');
  const nameLen = apk.readUInt16LE(cd + 28), extraLen = apk.readUInt16LE(cd + 30), commentLen = apk.readUInt16LE(cd + 32);
  entries.push({name: apk.toString('utf8', cd + 46, cd + 46 + nameLen), method: apk.readUInt16LE(cd + 10), compressedSize: apk.readUInt32LE(cd + 20), size: apk.readUInt32LE(cd + 24), localOffset: apk.readUInt32LE(cd + 42), centralOffset: cd});
  cd += 46 + nameLen + extraLen + commentLen;
}
function member(name) {
  const match = entries.filter(e => e.name === name);
  if (match.length !== 1) throw new Error('Expected one exact ZIP name: ' + name);
  const e = match[0], off = e.localOffset;
  if (apk.readUInt32LE(off) !== 0x04034b50) throw new Error('Invalid local header');
  const start = off + 30 + apk.readUInt16LE(off + 26) + apk.readUInt16LE(off + 28);
  const raw = apk.subarray(start, start + e.compressedSize);
  const data = e.method === 0 ? raw : e.method === 8 ? zlib.inflateRawSync(raw) : null;
  if (!data || data.length !== e.size) throw new Error('Unsupported compression or size mismatch: ' + name);
  return data;
}
function chunk(b, p) {
  const c = {type: b.readUInt16LE(p), headerSize: b.readUInt16LE(p + 2), size: b.readUInt32LE(p + 4), offset: p};
  if (c.size < c.headerSize || c.size < 8 || p + c.size > b.length) throw new Error('Invalid chunk at ' + p);
  return c;
}
function stringPool(b, p) {
  const c = chunk(b,p), count = b.readUInt32LE(p+8), flags = b.readUInt32LE(p+16), stringsStart = b.readUInt32LE(p+20), utf8 = !!(flags & 0x100), result=[];
  function len8(pos) { const n=b[pos]; return n & 128 ? [((n&127)<<8)|b[pos+1],pos+2]:[n,pos+1]; }
  function len16(pos) { const n=b.readUInt16LE(pos); return n&0x8000 ? [((n&0x7fff)<<16)|b.readUInt16LE(pos+2),pos+4]:[n,pos+2]; }
  for(let i=0;i<count;i++) {
    let q=p+stringsStart+b.readUInt32LE(p+c.headerSize+i*4), len;
    if(utf8){[,q]=len8(q);[len,q]=len8(q);result.push(b.toString('utf8',q,q+len));}
    else{[len,q]=len16(q);result.push(b.toString('utf16le',q,q+len*2));}
  }
  return result;
}
let resourceStrings=[];
function value(b,p,strings=resourceStrings) {
  const type=b[p+3], data=b.readUInt32LE(p+4);
  let decoded;
  if(type===0)decoded=null;
  else if(type===1||type===2||type===7||type===8)decoded=hex(data);
  else if(type===3)decoded=strings[data];
  else if(type===4)decoded=b.readFloatLE(p+4);
  else if(type===5||type===6){
    // Android complexToFloat: signed 24-bit mantissa times radix factor.
    const factors=[1/256,1/32768,1/8388608,1/2147483648];
    const number=(data&0xffffff00)*factors[(data>>4)&3];
    decoded={number,unit:type===5?['px','dp','sp','pt','in','mm'][data&15]:['fraction','fraction-parent'][data&15]};
  } else if(type===18)decoded=!!data;
  else if(type>=28&&type<=31)decoded='#'+data.toString(16).padStart(8,'0');
  else if(type===16)decoded=data|0;
  else decoded=hex(data);
  return {type,data:hex(data),decoded};
}
const arsc=member('resources.arsc');
const resources=[];
const resourcePackages=[];
function configAt(b,p,size) {
  const n=b.readUInt32LE(p), raw=b.subarray(p,p+Math.min(n,size));
  const language=n>=12?raw.subarray(8,10):Buffer.alloc(0), country=n>=12?raw.subarray(10,12):Buffer.alloc(0);
  const textCode = x => x[0] && !(x[0]&128) ? x.toString('ascii').replace(/\0/g,'') : x.length && x[0] ? 'packed:'+x.toString('hex') : '';
  return {rawHex:raw.toString('hex'),language:textCode(language),region:textCode(country),density:n>=16?b.readUInt16LE(p+14):0,sdkVersion:n>=28?b.readUInt16LE(p+24):0,uiMode:n>=32?b[p+29]:0};
}
const arscTop=chunk(arsc,0);
for(let p=arscTop.headerSize;p<arscTop.size;){
  const c=chunk(arsc,p);
  if(c.type===1)resourceStrings=stringPool(arsc,p);
  if(c.type===0x200){
    const packageId=arsc.readUInt32LE(p+8), packageName=arsc.toString('utf16le',p+12,p+268).split('\0')[0];
    const types=stringPool(arsc,p+arsc.readUInt32LE(p+268)),keys=stringPool(arsc,p+arsc.readUInt32LE(p+276)),typeIdOffset=c.headerSize>=288?arsc.readUInt32LE(p+284):0;
    resourcePackages.push({id:packageId,name:packageName,types,offset:p});
    for(let q=p+c.headerSize;q<p+c.size;){
      const t=chunk(arsc,q);
      if(t.type===0x201){
        const id=arsc[q+8],flags=arsc[q+9],count=arsc.readUInt32LE(q+12),start=arsc.readUInt32LE(q+16),config=configAt(arsc,q+20,t.headerSize-20);
        for(let i=0;i<count;i++){
          let idx=i,off;
          if(flags&1){idx=arsc.readUInt16LE(q+t.headerSize+i*4);off=arsc.readUInt16LE(q+t.headerSize+i*4+2)*4;}
          else if(flags&2){off=arsc.readUInt16LE(q+t.headerSize+i*2);if(off===0xffff)continue;off*=4;}
          else{off=arsc.readUInt32LE(q+t.headerSize+i*4);if(off===0xffffffff)continue;}
          const e=q+start+off,entrySize=arsc.readUInt16LE(e),entryFlags=arsc.readUInt16LE(e+2),key=arsc.readUInt32LE(e+4);
          const r={id:hex((packageId<<24)|((id+typeIdOffset)<<16)|idx),package:packageName,type:types[id-1],name:keys[key],config,sourceOffset:e};
          if(entryFlags&1){
            r.parent=hex(arsc.readUInt32LE(e+8));r.items=[];
            for(let m=0;m<arsc.readUInt32LE(e+12);m++){const a=e+entrySize+m*12;r.items.push({name:hex(arsc.readUInt32LE(a)),value:value(arsc,a+4)});}
          } else if(entryFlags&8){r.unsupportedCompact=true;limits.push('Compact resource entry encountered at '+e);}
          else r.value=value(arsc,e+entrySize);
          resources.push(r);
        }
      }
      q+=t.size;
    }
  }
  p+=c.size;
}
const byId=new Map();for(const r of resources){if(!byId.has(r.id))byId.set(r.id,[]);byId.get(r.id).push(r);}
function referenceName(id){const r=byId.get(id)?.[0];return r?'@'+r.type+'/'+r.name:id;}
function describe(v){return v.type===1||v.type===7?referenceName(v.data):v.type===2||v.type===8?'?'+referenceName(v.data):v.decoded;}
for(const r of resources){if(r.value)r.value.display=describe(r.value);if(r.items)for(const i of r.items){i.nameResolved=referenceName(i.name);i.value.display=describe(i.value);}}
const axml=member('AndroidManifest.xml');
function parseXml(b){
  const top=chunk(b,0);let strings=[],roots=[],stack=[],namespaces=[];
  for(let p=top.headerSize;p<top.size;){
    const c=chunk(b,p);
    if(c.type===1)strings=stringPool(b,p);
    if(c.type===0x100)namespaces.push({prefix:strings[b.readUInt32LE(p+16)],uri:strings[b.readUInt32LE(p+20)]});
    if(c.type===0x102){
      const n={tag:strings[b.readUInt32LE(p+20)],line:b.readUInt32LE(p+8),sourceOffset:p,attributes:{},attributeDetails:[],children:[]};
      const aStart=b.readUInt16LE(p+24),aSize=b.readUInt16LE(p+26),aCount=b.readUInt16LE(p+28);
      for(let i=0;i<aCount;i++){
        const a=p+16+aStart+i*aSize,ns=b.readUInt32LE(a),name=strings[b.readUInt32LE(a+4)],raw=b.readUInt32LE(a+8),typed=value(b,a+12,strings);
        const prefix=ns===0xffffffff?'':namespaces.find(x=>x.uri===strings[ns])?.prefix;
        const key=prefix?prefix+':'+name:name;
        n.attributes[key]=raw!==0xffffffff?strings[raw]:describe(typed);
        n.attributeDetails.push({name:key,namespace:ns===0xffffffff?null:strings[ns],raw:raw===0xffffffff?null:strings[raw],typed,sourceOffset:a});
      }
      if(stack.length)stack.at(-1).children.push(n);else roots.push(n);stack.push(n);
    }
    if(c.type===0x103)stack.pop();
    p+=c.size;
  }
  return roots;
}
const manifestTree=parseXml(axml);
const escapeXml=x=>String(typeof x==='object'?JSON.stringify(x):x).replace(/&/g,'&amp;').replace(/"/g,'&quot;').replace(/</g,'&lt;');
function xmlText(n,indent='') {const attrs=(n.tag==='manifest'?' xmlns:android="http://schemas.android.com/apk/res/android"':'')+Object.entries(n.attributes).map(([k,v])=>' '+k+'="'+escapeXml(v)+'"').join('');return indent+'<'+n.tag+attrs+(n.children.length?'>\n'+n.children.map(c=>xmlText(c,indent+'  ')).join('\n')+'\n'+indent+'</'+n.tag+'>':' />');}
const manifest=manifestTree[0], application=manifest.children.find(x=>x.tag==='application');
const launchers=application.children.filter(n=>n.tag==='activity'||n.tag==='activity-alias').filter(n=>n.children.some(i=>i.tag==='intent-filter'&&i.children.some(a=>a.tag==='action'&&a.attributes['android:name']==='android.intent.action.MAIN')&&i.children.some(a=>a.tag==='category'&&a.attributes['android:name']==='android.intent.category.LAUNCHER')));
const identity={source:'AndroidManifest.xml',evidenceLevel:'B: packaged declarations; no runtime validation',apkSha256:hash(apk),manifestSha256:hash(axml),resourcesSha256:hash(arsc),manifest:manifest.attributes,sdk:manifest.children.filter(n=>n.tag==='uses-sdk').map(n=>n.attributes),application:application.attributes,launchers:launchers.map(n=>({attributes:n.attributes,line:n.line,sourceOffset:n.sourceOffset})),permissions:manifest.children.filter(n=>/uses-permission/.test(n.tag)).map(n=>({tag:n.tag,...n.attributes})),features:manifest.children.filter(n=>n.tag==='uses-feature').map(n=>n.attributes),activityCount:application.children.filter(n=>n.tag==='activity'||n.tag==='activity-alias').length,resourcePackage:resourcePackages};
write('manifest-identity.json',identity);
write('manifest-decoded.json',manifestTree);
write('AndroidManifest.decoded.xml','<!-- Read-only binary XML decoding; reference names resolved from resources.arsc. Original line/offset records in manifest-decoded.json. -->\n'+manifestTree.map(n=>xmlText(n)).join('\n')+'\n');
write('resources-decoded.json',resources);
write('resources-design-candidates.json',{evidenceLevel:'B: packaged resource definitions; use by visible UI is unverified',resources:resources.filter(r=>['color','dimen','font','style','integer','bool'].includes(r.type)),limitations:['This includes AndroidX, Material and third-party library defaults. A resource value does not establish a Kimi design token.','Qualifiers are recorded as raw ResTable_config bytes plus selected fields. References may select different runtime variants.']});
const designNames=['bgTertiary','blue_450','colorAccent','ic_launcher_background','labelsPrimary','lightBgPrimary','lightLabelsPrimary','splashscreen_background','white','kdc_corner_large','kdc_corner_medium','kdc_corner_normal','AppTheme','AppBaseTheme','Theme.kimi','Theme.Splash','NightAdjusted.Theme.Splash'];
write('app-resource-anchors.json',{source:'resources.arsc',sha256:hash(arsc),evidenceLevel:'B: resource declarations; usage/appearance unverified',resources:resources.filter(r=>designNames.includes(r.name)),fontMembers:entries.filter(e=>/\.(?:ttf|otf|woff2?)$/i.test(e.name)).map(e=>({name:e.name,size:e.size,centralDirectoryOffset:e.centralOffset})),limitations:['Names are selected as candidate app resources, not proof of app ownership or visible use.','Android resource table contains no decoded font type definitions for this APK; font files can instead be packaged as assets.']});
// DEX identifiers retain exact tables/offsets. These are not decompiled instructions.
function uleb(b,p){let v=0,s=0,start=p;do{const x=b[p++];v|=(x&127)<<s;if(!(x&128))return [v>>>0,p];s+=7;}while(p-start<5);throw new Error('ULEB128 overflow');}
function parseDex(name){
  const b=member(name),magic=b.toString('ascii',0,8);if(!magic.startsWith('dex\n'))throw new Error('Not standard DEX: '+name);
  const strings=[],stringOffsets=[],types=[];
  for(let i=0;i<b.readUInt32LE(56);i++){const off=b.readUInt32LE(b.readUInt32LE(60)+i*4);const [,start]=uleb(b,off);let end=b.indexOf(0,start);strings.push(b.toString('utf8',start,end));stringOffsets.push(off);}
  for(let i=0;i<b.readUInt32LE(64);i++)types.push(strings[b.readUInt32LE(b.readUInt32LE(68)+i*4)]);
  const prototypes=[];
  for(let i=0;i<b.readUInt32LE(72);i++){const p=b.readUInt32LE(76)+i*12,params=b.readUInt32LE(p+8),args=[];if(params)for(let j=0;j<b.readUInt32LE(params);j++)args.push(types[b.readUInt16LE(params+4+j*2)]);prototypes.push({returnType:types[b.readUInt32LE(p+4)],parameters:args});}
  const methods=[];
  for(let i=0;i<b.readUInt32LE(88);i++){const p=b.readUInt32LE(92)+i*8;methods.push({index:i,class:types[b.readUInt16LE(p)],name:strings[b.readUInt32LE(p+4)],prototype:prototypes[b.readUInt16LE(p+2)],sourceOffset:p});}
  const packagePrefix='L'+String(manifest.attributes.package).replace(/\./g,'/')+'/';
  const classes=[];
  for(let i=0;i<b.readUInt32LE(96);i++){
    const p=b.readUInt32LE(100)+i*32,desc=types[b.readUInt32LE(p)],source=b.readUInt32LE(p+16),superIndex=b.readUInt32LE(p+8),dataOff=b.readUInt32LE(p+24);
    const c={descriptor:desc,super:superIndex===0xffffffff?null:types[superIndex],sourceFile:source===0xffffffff?null:strings[source],accessFlags:hex(b.readUInt32LE(p+4)),classDefOffset:p,classDataOffset:dataOff,methods:[]};
    if(dataOff){
      let q=dataOff,sf,inf,dm,vm;[sf,q]=uleb(b,q);[inf,q]=uleb(b,q);[dm,q]=uleb(b,q);[vm,q]=uleb(b,q);
      for(let k=0;k<sf+inf;k++){[,q]=uleb(b,q);[,q]=uleb(b,q);}
      for(const count of [dm,vm]){let idx=0;for(let k=0;k<count;k++){let diff,flags,code;[diff,q]=uleb(b,q);idx+=diff;[flags,q]=uleb(b,q);[code,q]=uleb(b,q);if(desc.startsWith(packagePrefix)||/^Lkimi\//.test(desc))c.methods.push({...methods[idx],accessFlags:hex(flags),codeOffset:code});}}
    }
    classes.push(c);
  }
  const own=classes.filter(c=>c.descriptor.startsWith(packagePrefix)||/^Lkimi\//.test(c.descriptor));
  const candidatePattern=/chat|conversation|search|history|setting|login|auth|profile|voice|audio|image|camera|attach|upload|file|document|markdown|feedback|share|onboard|prompt|theme|language|agent|payment|member|subscription|shortcut/i;
  const candidates=own.filter(c=>candidatePattern.test(c.descriptor)||c.methods.some(m=>candidatePattern.test(m.name)));
  write(name.replace('.dex','')+'-class-inventory.json',{source:name,sha256:hash(b),magic,counts:{strings:strings.length,types:types.length,methods:methods.length,classes:classes.length,ownClasses:own.length},boundary:{ownPrefixes:[packagePrefix,'Lkimi/'],methodCoverage:'Only classes under these prefixes include method inventories; no bytecode instructions decompiled.'},classes});
  write(name.replace('.dex','')+'-own-candidates.json',{source:name,evidenceLevel:'C: feature candidates inferred only from class/method names; B: identifiers and source offsets',classes:candidates});
  const interesting=strings.map((text,index)=>({index,offset:stringOffsets[index],text})).filter(r=>/^https?:\/\//.test(r.text)||(/(?:chat|kimi|login|search|voice|onboard|theme|history|attach|feedback|markdown)/i.test(r.text)&&r.text.length>=8&&r.text.length<=300));
  write(name.replace('.dex','')+'-string-candidates.json',{source:name,evidenceLevel:'B: string pool values; behavior and reachability unknown',coverage:'Selected by exact regex in tools/analyze-apk-static.cjs; not all strings',strings:interesting});
  return {source:name,sha256:hash(b),strings:strings.length,types:types.length,methodIds:methods.length,classes:classes.length,ownClasses:own.length,candidateClasses:candidates.length,ownNamespaces:[...new Set(own.map(c=>c.descriptor.split('/').slice(0,-1).join('/')))]};
}
const dex=entries.filter(e=>/^classes(?:\d+)?\.dex$/.test(e.name)).map(e=>parseDex(e.name));
write('static-analysis-summary.json',{apk:{path:apkPath,sha256:hash(apk),size:apk.length},manifest:identity,resources:{packages:resourcePackages,definitions:resources.length,byType:Object.fromEntries([...new Set(resources.map(r=>r.type))].map(t=>[t,resources.filter(r=>r.type===t).length]))},dex,limitations:['This Node script does not execute the application, access the network, verify signing, or use Java, JADX, Android SDK or an emulator.','DEX parsing inventories identifier/class tables and encoded method declarations; no instruction semantics or call graph.','Some DEX strings use modified UTF-8; Node UTF-8 decoding may replace unusual embedded NUL or surrogate encodings.','Resources include third-party defaults and do not prove visual usage.','Manifest permissions are requests, not evidence that permissions were granted or exercised.',...limits]});
process.stdout.write(JSON.stringify({apkSha256:hash(apk),package:manifest.attributes.package,versionName:manifest.attributes['android:versionName'],versionCode:manifest.attributes['android:versionCode'],resourceDefinitions:resources.length,dex: dex.map(({source,classes,ownClasses,candidateClasses})=>({source,classes,ownClasses,candidateClasses})),limitations:limits},null,2)+'\n');
