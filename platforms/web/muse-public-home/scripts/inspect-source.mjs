import fs from 'node:fs';
const css=fs.readFileSync('assets/source/_next/static/chunks/263nuo7jc7gf7.css','utf8');
const js=fs.readFileSync('assets/source/_next/static/chunks/1ls5_g6blqauj.js','utf8');
const args=process.argv.slice(2);
if(args[0]==='vars') {
  for(const name of args.slice(1)) console.log(JSON.stringify({name,values:[...css.matchAll(new RegExp(name+'\\s*:\\s*([^;}]+)','g'))].map(m=>m[1])}));
} else if(args[0]==='css') {
  const needles=args.slice(1);
  for(const needle of needles){
    let index=0,count=0;
    while((index=css.indexOf(needle,index))!==-1 && count++<15){
      const excerpt=css.slice(Math.max(0,css.lastIndexOf('}',index)+1),Math.min(css.length,css.indexOf('}',index)+1));
      if(excerpt.length<2000) console.log(excerpt);
      index+=needle.length;
    }
  }
} else if(args[0]==='js') {
  for(const needle of args.slice(1)){
    const index=js.indexOf(needle);
    console.log(JSON.stringify({needle,index,source:index<0?'':js.slice(Math.max(0,index-1200),index+2200)}));
  }
} else {
  const m=JSON.parse(fs.readFileSync(`evidence/raw/${args[0]||'desktop'}-measurements.json`));
  console.log(JSON.stringify({viewport:m.viewport,page:m.page}));
  for(const n of m.all.filter(n=>['h1','h2','h3','button','input','section','article'].includes(n.tag))){
    const s=n.style;
    console.log(JSON.stringify({tag:n.tag,id:n.id,name:n.ariaLabel||n.text.slice(0,70),class:n.class,rect:n.rect,style:{fontSize:s.fontSize,fontWeight:s.fontWeight,lineHeight:s.lineHeight,color:s.color,background:s.backgroundColor,padding:s.padding,margin:s.margin,gap:s.gap,radius:s.borderRadius}}));
  }
}
