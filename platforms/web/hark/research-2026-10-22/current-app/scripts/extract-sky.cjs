const fs = require('node:fs');
const path = require('node:path');
const crypto = require('node:crypto');
const acorn = require('/Users/cjr/.npm/_npx/0aa74679bec75e15/node_modules/acorn');
const root = path.resolve(__dirname, '..');
const oldRoot = path.resolve(root, '../..');
const hash = b => crypto.createHash('sha256').update(b).digest('hex');
const range = (text, start, end) => ({ utf16CodeUnits: { start, endExclusive: end }, utf8Bytes: { start: Buffer.byteLength(text.slice(0, start)), endExclusive: Buffer.byteLength(text.slice(0, end)) } });
const htmlBytes = fs.readFileSync(path.join(root,'resources/wallpaper_core.html'));
const html = htmlBytes.toString('utf8');
const oldBytes = fs.readFileSync(path.join(oldRoot,'evidence/scripts/wallpaper_core.html'));
const oldHtml = oldBytes.toString('utf8');
function parse(html) {
  const scripts = [...html.matchAll(/<script\b([^>]*)>([\s\S]*?)<\/script\s*>/gi)];
  const first = scripts[0];
  const start = first.index + first[0].indexOf('>') + 1;
  const script = first[2], ast = acorn.parse(script, { ecmaVersion:'latest',sourceType:'script' });
  const vars = new Map();
  for(const n of ast.body) if(n.type==='VariableDeclaration') for(const d of n.declarations) vars.set(d.id.name,d);
  return { script, start, ast, vars, scripts };
}
const current = parse(html), old = parse(oldHtml);
const source = node => ({ path:'resources/wallpaper_core.html',url:'https://hark.com/wallpaper_core.html',sha256:hash(htmlBytes),htmlRange:range(html,current.start+node.start,current.start+node.end),scriptRange:range(current.script,node.start,node.end) });
function staticValue(n) { if(n.type==='Literal')return n.value;if(n.type==='TemplateLiteral'&&!n.expressions.length)return n.quasis[0].value.cooked;return undefined; }
const shaderDir = path.join(root,'sky');fs.mkdirSync(shaderDir,{recursive:true});
fs.writeFileSync(path.join(shaderDir,'wallpaper-engine.raw.js'),current.script);
const shaders=[];
for(const [name,file] of [['VERT','vertex.glsl'],['FRAG','fragment.glsl']]) {
  const node=current.vars.get(name),oldNode=old.vars.get(name);
  const value=staticValue(node.init),oldValue=staticValue(oldNode.init),bytes=Buffer.from(value);
  fs.writeFileSync(path.join(shaderDir,file),bytes);
  shaders.push({name,path:'sky/'+file,bytes:bytes.length,sha256:hash(bytes),source:source(node.init),rawDeclaration:current.script.slice(node.start,node.end),historical:{sourcePath:'evidence/scripts/wallpaper_core.html',sourceSha256:hash(oldBytes),decodedBytes:Buffer.byteLength(oldValue),decodedSha256:hash(Buffer.from(oldValue)),sameDecodedBytes:value===oldValue},uniformDeclarations:[...value.matchAll(/\buniform\s+(\w+)\s+(\w+)(?:\s*\[\s*(\d+)\s*\])?\s*;/g)].map(m=>({type:m[1],name:m[2],arrayLength:m[3]?Number(m[3]):null,shaderUtf16Start:m.index})),classification:'B exact static JS string/template decode; no GL execution'});
}
const lutNode=current.vars.get('LUT_PNG'),lutValue=staticValue(lutNode.init),oldLut=staticValue(old.vars.get('LUT_PNG').init);
if(!/^data:image\/png;base64,[A-Za-z0-9+/=]+$/.test(lutValue))throw Error('Unexpected LUT literal');
const lutBytes=Buffer.from(lutValue.slice(lutValue.indexOf(',')+1),'base64');
if(lutBytes.subarray(0,8).toString('hex')!=='89504e470d0a1a0a')throw Error('Invalid LUT PNG');
fs.writeFileSync(path.join(shaderDir,'owner-lut.png'),lutBytes);
const lut={path:'sky/owner-lut.png',bytes:lutBytes.length,sha256:hash(lutBytes),width:lutBytes.readUInt32BE(16),height:lutBytes.readUInt32BE(20),bitDepth:lutBytes[24],colorType:lutBytes[25],source:source(lutNode.init),sourceDataUriSha256:hash(Buffer.from(lutValue)),historical:{sameDataUriBytes:lutValue===oldLut,pngSha256:hash(Buffer.from(oldLut.slice(oldLut.indexOf(',')+1),'base64'))},classification:'B exact embedded PNG data-URI decode; no recolor or resize'};
const keyNames=new Set(['state','LUT_AMT_DEFAULT','FADE_DUR','FADE_EPS','FADE_HELD_MAX','FRAME_MIN_MS','PRESETS','SCENES','GLOW','SKY2']);
const definitions=[];
for(const n of current.ast.body) {
  if(n.type==='FunctionDeclaration')definitions.push({name:n.id.name,kind:n.type,source:source(n),rawSource:current.script.slice(n.start,n.end)});
  else if(n.type==='VariableDeclaration')for(const d of n.declarations)if(keyNames.has(d.id.name))definitions.push({name:d.id.name,kind:d.init.type,source:source(d),rawSource:current.script.slice(d.start,d.end),staticLiteralValue:staticValue(d.init)});
}
const sky=JSON.parse(fs.readFileSync(path.join(root,'sky-static.json')));
const wrapperText=fs.readFileSync(path.join(root,'resources/BbFz-0Fv.js'),'utf8');
const wrapperAst=acorn.parse(wrapperText,{ecmaVersion:'latest',sourceType:'module'});
for(const n of wrapperAst.body)if(n.type==='VariableDeclaration')for(const d of n.declarations)if(d.id.name==='I'){
  const bytes=Buffer.from(wrapperText),raw=wrapperText.slice(d.start,d.end);
  if(!sky.sources.some(s=>s.symbol==='I'&&s.source?.path==='resources/BbFz-0Fv.js'))sky.sources.push({symbol:'I',source:{path:'resources/BbFz-0Fv.js',url:'https://assets.hark.com/assets/BbFz-0Fv.js',sha256:hash(bytes),range:range(wrapperText,d.start,d.end)},rawSource:raw,classification:'B declared light/dark hour targets; source controller conditions govern whether they execute'});
}
sky.status='current shader, LUT, engine and loader archived; shader/LUT byte comparison with H15 completed';
sky.wallpaperAcquisition='wallpaper-browser-acquisition.json';
sky.failedCredentialFreeAcquisition='wallpaper-acquisition.json';
sky.wallpaper={path:'resources/wallpaper_core.html',bytes:htmlBytes.length,sha256:hash(htmlBytes),scriptCount:current.scripts.length,firstScript:{path:'sky/wallpaper-engine.raw.js',bytes:Buffer.byteLength(current.script),sha256:hash(Buffer.from(current.script)),htmlRange:range(html,current.start,current.start+current.script.length),sameBytesAsH15:current.script===old.script},historicalHtml:{path:'evidence/scripts/wallpaper_core.html',bytes:oldBytes.length,sha256:hash(oldBytes),entireHtmlEqual:htmlBytes.equals(oldBytes)},ignoredScripts:current.scripts.slice(1).map(s=>({htmlRange:range(html,s.index,s.index+s[0].length),reason:'CDN anti-bot script; not sky rendering code, not executed or followed'}))};
sky.shaders=shaders;sky.lut=lut;sky.engineDefinitions=definitions;
sky.historicalShaderBoundary='Current decoded vertex/fragment shader and embedded PNG have independently compared hashes below. The separately acquired complete HTML retains its own hash and CDN boilerplate; no blanket version identity claim.';
sky.reuse={entry:'sky/wallpaper-engine.raw.js + exact GLSL + owner-lut.png',hostAdaptation:'Wrap GPU rendering and freeze scene/weather/time/location inputs in a clearly D adapter. Original engine contains weather/API/network paths; no original fetch or business endpoint was executed by this extractor.',appearanceBoundary:'Theme surface paint and sky simulation hour are separate controls; changing CSS theme does not imply switching day/night sky.',geometry:'Exact uniform layout and CPU palette/scene functions are retained in engineDefinitions; use declared shader inputs, not a generic gradient substitute.'};
fs.writeFileSync(path.join(root,'sky-static.json'),JSON.stringify(sky,null,2)+'\n');
console.log(JSON.stringify({htmlBytes:htmlBytes.length,htmlSha256:hash(htmlBytes),engineBytes:Buffer.byteLength(current.script),sameEngineAsH15:current.script===old.script,shaders:shaders.map(s=>({name:s.name,bytes:s.bytes,sha256:s.sha256,sameAsH15:s.historical.sameDecodedBytes})),lut,definitions:definitions.length}));
