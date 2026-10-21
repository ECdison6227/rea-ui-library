import fs from 'node:fs/promises';
import path from 'node:path';
import {pathToFileURL} from 'node:url';
const parserPath=path.join(process.env.APPDATA,'npm/node_modules/rea-agents/node_modules/parse5/dist/index.js');
const {parse,serialize,serializeOuter}=await import(pathToFileURL(parserPath));
const doc=parse(await fs.readFile('evidence/raw/current-desktop.html','utf8'));
const attr=(n,key)=>n.attrs?.find(a=>a.name===key)?.value;
const all=(node,test)=>[...(test(node)?[node]:[]),...(node.childNodes||[]).flatMap(child=>all(child,test))];
const first=(node,test)=>all(node,test)[0];
const text=node=>node.nodeName==='#text'?node.value:(node.childNodes||[]).map(text).join('');
const byId=id=>first(doc,n=>attr(n,'id')===id);
const section=id=>{let n=byId(id);while(n&&n.tagName!=='section')n=n.parentNode;return n;};
const imgPath=n=>attr(n,'src');
const content={
  source:'https://muse.ai/',
  brand:'Muse',
  title:'Muse, AI that gets it done for you',
  features:all(doc,n=>attr(n,'class')?.split(' ').includes('landing-feature-row')).map((row,index)=>({
    id:attr(first(row,n=>n.tagName==='h3'),'id'),
    title:text(first(row,n=>n.tagName==='h3')),
    descriptionHtml:serialize(first(row,n=>n.tagName==='p')),
    image:imgPath(first(row,n=>n.tagName==='img'&&!attr(n,'src')?.includes('-dark'))),
    imageAlt:attr(first(row,n=>n.tagName==='img'),'alt'),
    alternate:index%2===1
  })),
  intro:{title:text(byId('landing-introduction-video-title')),description:text(first(section('landing-introduction-video-title'),n=>n.tagName==='p'))},
  walkthrough:{title:text(byId('landing-product-walkthrough-title')),description:text(first(section('landing-product-walkthrough-title'),n=>n.tagName==='p')),poster:'/images/hatch/landing/product-walkthrough-cover.png'},
  learn:{title:text(byId('landing-learn-about-title')),description:text(first(section('landing-learn-about-title'),n=>n.tagName==='p'))},
  stories:all(section('landing-learn-about-title'),n=>n.tagName==='article').map((article,index)=>({
    title:text(first(article,n=>n.tagName==='h3')),
    description:text(first(article,n=>n.tagName==='p')),
    href:new URL(attr(first(article,n=>n.tagName==='a'),'href'),'https://muse.ai/').href,
    icon:['design','shield','check'][index]
  })),
  phrases:all(doc,n=>attr(n,'class')?.split(' ').includes('landing-rebus-phrase')).slice(0,-1).map(n=>({text:attr(n,'data-landing-rebus-phrase-text'),html:serialize(n)}))
};
await fs.mkdir('reference',{recursive:true});
await fs.mkdir('assets/curated',{recursive:true});
await fs.writeFile('reference/source-content.js','// Public-page content slots. Source and capture provenance: ../evidence/index.json\nexport default '+JSON.stringify(content,null,2)+';\n');
const icons={};
function scrub(n){
  if(n.attrs)n.attrs=n.attrs.filter(a=>!a.name.startsWith('on')&&!['class','style','tabindex','data-slot'].includes(a.name));
  n.childNodes=(n.childNodes||[]).filter(c=>!['script','foreignObject'].includes(c.tagName));
  for(const c of n.childNodes)scrub(c);
}
const arrowLink=first(doc,n=>n.tagName==='a'&&attr(n,'href')==='#landing-introduction-video-title');
icons.down=first(arrowLink,n=>n.tagName==='svg');
const storyNodes=all(section('landing-learn-about-title'),n=>n.tagName==='article');
for(const [index,name] of ['design','shield','check'].entries())icons[name]=all(storyNodes[index],n=>n.tagName==='svg')[0];
icons.chevron=all(storyNodes[0],n=>n.tagName==='svg').at(-1);
icons.play=first(section('landing-product-walkthrough-title'),n=>n.tagName==='svg');
for(const [name,node] of Object.entries(icons)){
  if(!node)throw new Error('Missing original SVG '+name);
  scrub(node);
  if(!node.attrs.some(a=>a.name==='xmlns'))node.attrs.push({name:'xmlns',value:'http://www.w3.org/2000/svg'});
  await fs.writeFile(`assets/curated/${name}.svg`,serializeOuter(node));
}
console.log(JSON.stringify({features:content.features.length,stories:content.stories.length,phrases:content.phrases.map(p=>p.text),icons:Object.keys(icons)}));
