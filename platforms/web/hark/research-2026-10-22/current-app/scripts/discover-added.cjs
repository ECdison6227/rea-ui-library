// Parse only a newly retained module. Never import or execute target code.
const fs=require('node:fs'),acorn=require('/Users/cjr/.npm/_npx/0aa74679bec75e15/node_modules/acorn');
const postcss=require('/Users/cjr/.npm/_npx/110f701c48e68d66/node_modules/postcss');
const file=process.argv[2],url=process.argv[3],text=fs.readFileSync(file,'utf8'),dependencies=[],markers={svgRoots:0,shaderCodeLiterals:0};
const range=(a,b)=>({utf16CodeUnits:{start:a,endExclusive:b},utf8Bytes:{start:Buffer.byteLength(text.slice(0,a)),endExclusive:Buffer.byteLength(text.slice(0,b))}});
function add(value,node,method,kind='module'){dependencies.push({specifier:value,sourceRange:range(node.start,node.end),method,kind})}
function walk(node,visit){if(!node||typeof node!=='object')return;if(node.type)visit(node);for(const v of Object.values(node))if(Array.isArray(v))v.forEach(n=>walk(n,visit));else if(v&&typeof v==='object')walk(v,visit)}
if(url.endsWith('.js')){
 const ast=acorn.parse(text,{ecmaVersion:'latest',sourceType:'module'});
 for(const n of ast.body)if(['ImportDeclaration','ExportNamedDeclaration','ExportAllDeclaration'].includes(n.type)&&n.source?.value)add(n.source.value,n.source,n.type);
 walk(ast,n=>{
  if(n.type==='ImportExpression'&&n.source.type==='Literal'&&typeof n.source.value==='string')add(n.source.value,n.source,'literal-dynamic-import');
  if(n.type==='VariableDeclarator'&&n.id.type==='Identifier'&&n.id.name==='__vite__mapDeps')walk(n.init,a=>{if(a.type==='ArrayExpression'&&a.elements.length&&a.elements.every(e=>e?.type==='Literal'&&typeof e.value==='string'))for(const e of a.elements)add(e.value,e,'complete-AST-vite-dependency-table-entry')});
  if(n.type==='CallExpression'&&n.arguments[0]?.value==='svg')markers.svgRoots++;
  if(n.type==='Literal'&&typeof n.value==='string'&&/^\s*(?:#ifdef GL_ES|attribute vec2|precision highp)/.test(n.value)&&/void\s+main\s*\(/.test(n.value))markers.shaderCodeLiterals++;
 });
}else if(url.endsWith('.css')){
 const ast=postcss.parse(text);ast.walkAtRules('font-face',rule=>{for(const d of rule.nodes.filter(n=>n.type==='decl'&&n.prop==='src')){const rx=/\burl\(\s*(?:"([^"\\]*)"|'([^'\\]*)'|([^\s()'"\\]+))\s*\)/g;let m;while(m=rx.exec(d.value))add(m[1]??m[2]??m[3],{start:d.source.start.offset,end:d.source.end.offset+1},'parsed-font-face-complete-url-token','font')}});
 ast.walkAtRules('import',rule=>{const m=rule.params.match(/^\s*(?:url\(\s*)?["']([^"'\\]+)["']/);if(m)add(m[1],{start:rule.source.start.offset,end:rule.source.end.offset+1},'parsed-css-import')});
}
process.stdout.write(JSON.stringify({dependencies,markers}));
