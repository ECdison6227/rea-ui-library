const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const parser = require('C:/Users/edsjrcheng/AppData/Roaming/npm/node_modules/rea-agents/node_modules/@babel/parser');
const root = path.resolve(__dirname, '../../..');
const dir = path.join(root, 'analysis/ui/rn-evidence');
fs.mkdirSync(dir, {recursive:true});
const files = {
 sport:'c86a7eb68dd32b3c-index.android.bundle',
 mine:'fc87206bd4201baa-index.android.bundle',
 dial:'b48567af188568ad-index.android.bundle',
 common:'42856e26522e2704-index.android.bundle'
};
const pick = (n,s) => n ? s.slice(n.start,n.end) : null;
function walk(n, cb) { if(!n || typeof n !== 'object')return; if(n.type)cb(n); for(const [k,v] of Object.entries(n)){if(['loc','start','end','extra'].includes(k))continue;if(Array.isArray(v))for(const t of v)walk(t,cb);else if(v && typeof v==='object')walk(v,cb);} }
function value(n,s){if(!n)return null; if(['StringLiteral','NumericLiteral','BooleanLiteral'].includes(n.type))return n.value; if(n.type==='NullLiteral')return null; if(n.type==='ObjectExpression')return Object.fromEntries(n.properties.map(p=>p.type==='ObjectProperty'?[p.key.name??p.key.value,value(p.value,s)]:['...'+pick(p.argument,s),'spread'])); if(n.type==='ArrayExpression')return n.elements.map(t=>value(t,s)); return {$code:pick(n,s)};}
function scan(name){
 const file=path.join(root,'analysis/assets/extracted',files[name]);const all=fs.readFileSync(file,'utf8');const lines=all.split(/\r?\n/);const mods=[];
 for(let i=0;i<lines.length;i++){
  const s=lines[i];if(!s.startsWith('__d('))continue;
  let ast;try{ast=parser.parse(s,{sourceType:'script'});}catch(e){mods.push({line:i+1,error:String(e)});continue;}
  const call=ast.program.body[0].expression; const factory=call.arguments[0]; const id=call.arguments[1]?.value;
  const rec={id,line:i+1,bytes:Buffer.byteLength(s),deps:call.arguments[2]?.elements.map(n=>n.value)||[],exports:[],strings:[],routes:[],styleSheets:[],assetNames:[],calls:[]}; const st=new Set();
  walk(factory,n=>{
   if(n.type==='StringLiteral')st.add(n.value);
   if(n.type==='AssignmentExpression' && n.left.type==='MemberExpression' && n.left.object.type==='Identifier' && n.left.object.name===factory.params[5]?.name){rec.exports.push(n.left.property.name??n.left.property.value);}
   if(n.type==='CallExpression' && n.callee.type==='MemberExpression'){
    const method=n.callee.property.name??n.callee.property.value;
    if(method==='defineProperty'&&n.arguments[0]?.name===factory.params[5]?.name&&n.arguments[1]?.value)rec.exports.push(n.arguments[1].value);
    if(method==='create' && n.arguments[0]?.type==='ObjectExpression')rec.styleSheets.push({callee:pick(n.callee,s),styles:value(n.arguments[0],s),code:pick(n,s)});
    if(['timing','spring','sequence','parallel','delay','withTiming','withSpring','withDelay','withSequence'].includes(method))rec.calls.push({method,code:pick(n,s)});
   }
   if(n.type==='ObjectProperty'&&n.key.name==='name'&&n.value.type==='StringLiteral'&&s.includes('registerAsset'))rec.assetNames.push(n.value.value);
  });
  rec.exports=[...new Set(rec.exports)].filter(x=>x!=='__esModule'); rec.strings=[...st].filter(x=>!['__esModule','default','function','object','string','undefined','prototype','constructor','value','enumerable','configurable','writable','Symbol.iterator','@@iterator','@@asyncIterator','@@toStringTag'].includes(x));rec.routes=rec.strings.filter(x=>/^miwear\//.test(x));
  mods.push(rec);
 }
 return {name,file:path.relative(root,file),sha256:crypto.createHash('sha256').update(all).digest('hex'),modules:mods};
}
const mode=process.argv[2]||'index';
if(mode==='index'){
 for(const n of Object.keys(files)){
  const obj=scan(n);fs.writeFileSync(path.join(dir,n+'-modules.json'),JSON.stringify(obj,null,2));console.log(n,obj.modules.length,obj.modules.filter(x=>x.error).length,'parse errors');
 }
}else if(mode==='select'){
 const name=process.argv[3],ids=process.argv.slice(4);const obj=JSON.parse(fs.readFileSync(path.join(dir,name+'-modules.json')));const lines=fs.readFileSync(path.join(root,obj.file),'utf8').split(/\r?\n/);for(const id of ids){const rec=obj.modules.find(x=>String(x.id)===id);if(!rec)throw Error('Missing '+id);fs.writeFileSync(path.join(dir,`${name}-${id}.js`),lines[rec.line-1]+'\n');fs.writeFileSync(path.join(dir,`${name}-${id}.json`),JSON.stringify(rec,null,2));console.log(name,id,'line',rec.line,'deps',JSON.stringify(rec.deps));}
}else if(mode==='query'){
 const name=process.argv[3],re=new RegExp(process.argv[4]||'.','i');const obj=JSON.parse(fs.readFileSync(path.join(dir,name+'-modules.json')));for(const rec of obj.modules){const hay=JSON.stringify({...rec,styleSheets:[],calls:[]});if(re.test(hay))console.log(JSON.stringify({id:rec.id,line:rec.line,exports:rec.exports,strings:rec.strings.filter(s=>re.test(s)).slice(0,40),routes:rec.routes,styles:rec.styleSheets.map(s=>Object.keys(s.styles)),assets:rec.assetNames}));}
}else if(mode==='detail'){
 const name=process.argv[3],ids=process.argv.slice(4);const obj=JSON.parse(fs.readFileSync(path.join(dir,name+'-modules.json')));const lines=fs.readFileSync(path.join(root,obj.file),'utf8').split(/\r?\n/);
 for(const id of ids){const rec=obj.modules.find(x=>String(x.id)===id);if(!rec)throw Error('Missing '+id);const s=lines[rec.line-1];const ast=parser.parse(s);const factory=ast.program.body[0].expression.arguments[0];const aliases={},elements=[],animationCalls=[],numericStyleProperties=[];
  walk(factory,n=>{if(n.type==='VariableDeclarator'&&n.id.type==='Identifier'&&n.init){const refs=[];walk(n.init,t=>{if(t.type==='CallExpression'&&t.callee.name===factory.params[1]?.name&&t.arguments[0]?.type==='MemberExpression'&&t.arguments[0].object.name===factory.params[6]?.name){refs.push(rec.deps[t.arguments[0].property.value]);}});if(refs.length===1)aliases[n.id.name]=refs[0];}});
  walk(factory,n=>{
   if(n.type==='CallExpression'){
    const c=n.callee.type==='SequenceExpression'?n.callee.expressions.at(-1):n.callee;
    if(c.type==='MemberExpression'&&c.property.name==='createElement')elements.push({type:pick(n.arguments[0],s),module:aliases[n.arguments[0]?.object?.name],props:value(n.arguments[1],s),children:n.arguments.slice(2).map(t=>t.type==='StringLiteral'?t.value:{$code:pick(t,s).slice(0,300)})});
    if(c.type==='MemberExpression'&&/^(timing|spring|sequence|parallel|delay|withTiming|withSpring|withDelay|withSequence|interpolate|setTimeout)$/.test(c.property.name)||c.type==='Identifier'&&c.name==='setTimeout')animationCalls.push(pick(n,s));
   }
   if(n.type==='ObjectProperty'&&/^(fontSize|fontFamily|fontWeight|lineHeight|letterSpacing|duration|damping|stiffness|mass|friction|tension|delay|borderRadius|height|width|margin|padding)/.test(n.key.name??n.key.value))numericStyleProperties.push({key:n.key.name??n.key.value,value:value(n.value,s)});
  });
  const detail={bundle:name,id,line:rec.line,aliases,elements,animationCalls,numericStyleProperties};fs.writeFileSync(path.join(dir,`${name}-${id}-detail.json`),JSON.stringify(detail,null,2));
  const text=factory.body.body.map(n=>n.type==='FunctionDeclaration'&&pick(n,s).includes('Generator is already running')?'/* Babel regenerator helper omitted from readable view; exact source retained in .js */':pick(n,s)).join('\n\n');fs.writeFileSync(path.join(dir,`${name}-${id}-logic.txt`),text+'\n');
  console.log(name,id,JSON.stringify({elements:elements.length,animationCalls}));
 }
}
