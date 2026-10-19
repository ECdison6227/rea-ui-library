const fs=require('node:fs');
const path=require('node:path');
const {Dex}=require('../../tools/dex.cjs');
const root=path.resolve(__dirname,'../../..');
const index=JSON.parse(fs.readFileSync(path.join(root,'analysis/dex-classes.json'),'utf8').replace(/^\uFEFF/,''));
const wants={ogk:['C5','l4','p'],qmk:['mb','nu','kq','ou'],tmk:['d','a','v','z','y','H','D'],gdk:['c'],b5k:['a','c']};
const publicText=fs.readFileSync(path.join(root,'analysis/phase2/apktool/res/values/public.xml'),'utf8');
const ids=new Map([...publicText.matchAll(/<public type="([^"]+)" name="([^"]+)" id="(0x[\da-f]+)"/g)].map(m=>[parseInt(m[3],16),{type:m[1],name:m[2]}]));
const rows=[];
for(const dexName of [...new Set(index.filter(c=>Object.keys(wants).some(k=>c.name==='L'+k+';')).map(c=>c.dex))]){
 const d=new Dex(fs.readFileSync(path.join(root,'analysis/phase2/apktool',dexName)),dexName);
 for(const [name,fields] of Object.entries(wants)){
  const c=d.classes.find(x=>x.name==='L'+name+';');if(!c)continue;
  const dat=d.classData(c);const staticFields=dat.fields.filter(f=>f.access&8);let p=c.staticValuesOff;if(!p)continue;
  const n=d.uleb(p);p=d.next;
  for(let i=0;i<n;i++){const r=d.value(p);p=r.next;const f=staticFields[i];if(f&&fields.includes(f.name))rows.push({class:name,field:f.name,value:r.value,hex:'0x'+r.value.toString(16),resource:ids.get(r.value)||null,dex:dexName,sourceSha256:d.sha256});}
 }
}
fs.writeFileSync(path.join(root,'analysis/ui/native-resource-map.json'),JSON.stringify(rows,null,2));
for(const r of rows)console.log(r.class+'.'+r.field,r.hex,JSON.stringify(r.resource));
