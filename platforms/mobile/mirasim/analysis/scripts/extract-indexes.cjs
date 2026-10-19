const fs=require('fs');
const parser=require('../tools/node_modules/@babel/parser');
const source=fs.readFileSync('analysis/formatted/app.js','utf8');
const ast=parser.parse(source,{sourceType:'module'});
const translations={},plugins=[],functions=[],endpoints=[],apiTemplates=[];
function visit(node){
  if(!node||typeof node!=='object')return;
  if(node.type==='ObjectProperty'&&node.key?.type==='StringLiteral'&&node.value?.type==='StringLiteral'){
    const key=node.key.value;
    if(key.startsWith('mobile.'))(translations[key]??=[]).push({value:node.value.value,line:node.loc.start.line});
  }
  if(node.type==='CallExpression'&&node.callee.type==='Identifier'&&node.callee.name==='Us'&&node.arguments[0]?.type==='StringLiteral')plugins.push({name:node.arguments[0].value,line:node.loc.start.line});
  if(node.type==='FunctionDeclaration'&&node.id&&node.loc.start.line>=189000)functions.push({name:node.id.name,line:node.loc.start.line,endLine:node.loc.end.line});
  if(node.type==='StringLiteral'&&(/^(?:https?|wss?):\/\//.test(node.value)||/^\/v\d\//.test(node.value)))endpoints.push({value:node.value,line:node.loc.start.line});
  if(node.type==='TemplateLiteral'){
    const template=node.quasis.map(part=>part.value.raw).join('${…}');
    if(/\/(auth\/|v[12]\/)/.test(template))apiTemplates.push({template,line:node.loc.start.line});
  }
  for(const [key,value] of Object.entries(node)){
    if(['loc','extra','start','end','comments','tokens'].includes(key))continue;
    if(Array.isArray(value))value.forEach(visit);else if(value&&typeof value==='object')visit(value);
  }
}
visit(ast);
for(const [name,value] of Object.entries({translations,plugins,functions,endpoints,apiTemplates}))fs.writeFileSync(`analysis/evidence/frontend-${name}.json`,JSON.stringify(value,null,2)+'\n');
console.log({translationKeys:Object.keys(translations).length,plugins:plugins.length,mobileFunctions:functions.length,endpointLiterals:endpoints.length});
