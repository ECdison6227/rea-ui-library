import content from './source-content.js';
import {Header,Hero,MediaSection,FeatureRow,ReadingLinks,Footer,asset,localPhrase} from './components.js';
const reading = new URLSearchParams(location.search).get('example') === 'reading';
const items = [
  {id:'field-notes',title:'A little attention changes the everyday.',description:'Short field notes about looking closely, noticing more, and carrying a notebook into the world.',category:'FIELD NOTES',cardTitle:'The art of paying attention',readTime:'8 MIN READ · SAVED TODAY',alternate:false},
  {id:'creative-work',title:'Good ideas need somewhere to grow.',description:'Essays on creative work, a slower pace, and giving unfinished thoughts enough time to become something useful.',category:'CREATIVE WORK',cardTitle:'Leave space for an idea',readTime:'6 MIN READ · SAVED YESTERDAY',alternate:true},
  {id:'places',title:'Take the long way home.',description:'Stories about walking, finding new places, and feeling a little more at home in the city.',category:'PLACES',cardTitle:'A different way around',readTime:'11 MIN READ · SAVED THIS WEEK',alternate:false}
];
const app=document.getElementById('app');
app.innerHTML=Header({reading})+`<div class="page-scroll" role="region" aria-label="${reading?'Folio reading library':'Muse landing page'}">${Hero(content,{reading})}<article>${reading?`<section class="media-section intro-section" id="introduction" tabindex="-1" aria-labelledby="collection-title"><div class="container section-intro"><h2 class="section-heading" id="collection-title">A few things worth keeping</h2><p class="section-copy">Come back to the things that stay with you.</p><p class="result-count" role="status">3 stories in your collection</p></div></section><div id="reading-results">${items.map(item=>FeatureRow(item,{reading:true})).join('')}</div>`:MediaSection({...content.intro,poster:asset('introduction-still.png')})+content.features.map(item=>FeatureRow(item)).join('')+MediaSection({...content.walkthrough,poster:asset(content.walkthrough.poster),walkthrough:true})+ReadingLinks(content)}</article>${Footer({reading})}</div><p class="sr-only" id="page-status" role="status"></p>`;
if(reading)document.title='Folio — reading collection';
const scroller=app.querySelector('.page-scroll');
const cue=app.querySelector('.learn-cue');
const input=app.querySelector('.text-field');
const intro=app.querySelector('#introduction');
const reduced=matchMedia('(prefers-reduced-motion: reduce)');
let scrollFrame=0;
function cancelScroll(){cancelAnimationFrame(scrollFrame);scrollFrame=0;}
function scrollToPosition(y,onComplete){
  cancelScroll();
  if(reduced.matches){scroller.scrollTop=y;onComplete?.();return;}
  const start=scroller.scrollTop,at=performance.now();
  const tick=now=>{
    const t=Math.min(1,(now-at)/800);
    const eased=t<.5?4*t*t*t:1-Math.pow(-2*t+2,3)/2;
    scroller.scrollTop=start+(y-start)*eased;
    if(t<1)scrollFrame=requestAnimationFrame(tick);else{scrollFrame=0;onComplete?.();}
  };
  scrollFrame=requestAnimationFrame(tick);
}
for(const event of ['wheel','touchstart'])scroller.addEventListener(event,cancelScroll,{passive:true});
scroller.addEventListener('keydown',event=>{if(['ArrowDown','ArrowUp','PageDown','PageUp','Home','End',' '].includes(event.key))cancelScroll();});
function goToIntroduction(){scrollToPosition(intro.offsetTop-60,()=>intro.focus({preventScroll:true}));}
cue.addEventListener('click',event=>{event.preventDefault();goToIntroduction();});
app.querySelectorAll('[data-return]').forEach(button=>button.addEventListener('click',()=>scrollToPosition(0,()=>input.focus({preventScroll:true}))));
app.querySelector('.entry-form').addEventListener('submit',event=>{
  event.preventDefault();
  if(reading){
    const query=input.value.trim().toLowerCase();
    const matches=items.filter(item=>Object.values(item).some(value=>String(value).toLowerCase().includes(query)));
    app.querySelector('#reading-results').innerHTML=matches.map(item=>FeatureRow(item,{reading:true})).join('');
    app.querySelector('.result-count').textContent=matches.length?`${matches.length} ${matches.length===1?'story':'stories'} in your collection`:'No stories found. Try a different word.';
  }
  app.querySelector('#page-status').textContent=reading?'Your reading collection has been filtered.':'Showing the Muse introduction.';
  goToIntroduction();
});
const surfaces=[...app.querySelectorAll('[data-reveal]')];
let mediaFrame=0;
const mediaProgress=surfaces.map(()=>0);
function refreshScroll(){
  const y=scroller.scrollTop;
  app.style.setProperty('--header-opacity',String(Math.min(1,Math.max(0,(y-80)/120))));
  app.classList.toggle('has-scrolled',y>200);
  cue.classList.toggle('is-hidden',y>40);
  cue.tabIndex=y>40?-1:0;
  cue.setAttribute('aria-hidden',String(y>40));
  if(!mediaFrame)mediaFrame=requestAnimationFrame(updateMedia);
  if(y>400)resetPhrases();
}
function updateMedia(){
  mediaFrame=0;
  let unsettled=false;
  surfaces.forEach((surface,index)=>{
    const stage=surface.parentElement;
    const rect=stage.getBoundingClientRect();
    const height=scroller.clientHeight;
    const endDistance=.25*height+rect.height*.5;
    const target=reduced.matches||innerWidth<768?1:Math.max(0,Math.min(1,(.9*height-rect.top)/endDistance));
    mediaProgress[index]+=(target-mediaProgress[index])*.18;
    if(Math.abs(target-mediaProgress[index])<.0001)mediaProgress[index]=target;else unsettled=true;
    const p=mediaProgress[index];
    surface.style.transform=`translateY(${40*(1-p)}px) scale(${.9+.1*p})`;
    surface.closest('.intro-section')?.style.setProperty('--reveal',String(p));
  });
  if(unsettled)mediaFrame=requestAnimationFrame(updateMedia);
}
scroller.addEventListener('scroll',refreshScroll,{passive:true});
window.addEventListener('resize',refreshScroll);

const phraseStage=app.querySelector('.phrase-stage');
let phrase=app.querySelector('.phrase');
let cycleTimer=0,phraseIndex=0,phraseAnimations=[],cycleStarted=false,phraseGeneration=0;
function resetPhrases(){
  phraseGeneration++;
  clearTimeout(cycleTimer);cycleTimer=0;cycleStarted=false;
  for(const animation of phraseAnimations)animation.cancel();phraseAnimations=[];
  phraseIndex=0;
  if(!reading){
    phraseStage.querySelectorAll('.phrase').forEach(el=>el.remove());
    phrase=document.createElement('span');phrase.className='phrase';
    phrase.innerHTML=localPhrase(content.phrases[0].html);phraseStage.append(phrase);
  }
}
function schedulePhrase(){
  if(reading||reduced.matches||document.hidden||scroller.scrollTop>400)return;
  cycleTimer=setTimeout(changePhrase,phraseIndex===0?3250:2500);
}
async function changePhrase(){
  if(reduced.matches||scroller.scrollTop>400)return resetPhrases();
  const generation=phraseGeneration;
  const outgoing=phrase;
  const words=[...phrase.querySelectorAll('[data-landing-rebus-word-index]')];
  const distance=parseFloat(getComputedStyle(phrase).fontSize)*.3;
  const animations=words.map((word,index)=>word.animate([{opacity:1,transform:'translateY(0)'},{opacity:0,transform:`translateY(${-distance}px)`}],{duration:150,delay:index*30,easing:'cubic-bezier(.75,0,1,1)',fill:'forwards'}));
  phraseIndex=(phraseIndex+1)%content.phrases.length;
  const incoming=document.createElement('span');incoming.className='phrase';incoming.innerHTML=localPhrase(content.phrases[phraseIndex].html);phraseStage.append(incoming);
  phraseAnimations=[...animations,...[...incoming.querySelectorAll('[data-landing-rebus-word-index]')].map((word,index)=>word.animate([{opacity:0,transform:`translateY(${distance}px)`},{opacity:1,transform:'translateY(0)'}],{duration:200,delay:150+index*30,easing:'cubic-bezier(0,0,.25,1)',fill:'both'}))];
  try{await Promise.all(phraseAnimations.map(animation=>animation.finished));}catch{return;}
  if(generation!==phraseGeneration)return;
  outgoing.remove();phrase=incoming;
  schedulePhrase();
}
const avatar=app.querySelector('.avatar-slot');
const idleVideo=app.querySelector('.avatar-idle');
const activeVideo=app.querySelector('.avatar-active');
function startCycle(){
  if(reading||reduced.matches||scroller.scrollTop>400)return;
  if(!cycleStarted){cycleStarted=true;schedulePhrase();idleVideo?.play().catch(()=>{});}
}
function playAvatar(){startCycle();if(!reduced.matches)activeVideo?.play().catch(()=>{});}
avatar.addEventListener('pointerenter',playAvatar);
avatar.addEventListener('pointerleave',()=>activeVideo?.pause());
let holdTimer=0;
avatar.addEventListener('pointerdown',event=>{if(event.pointerType==='touch')holdTimer=setTimeout(()=>{avatar.classList.add('is-pressed');playAvatar();},400);});
function releaseAvatar(){clearTimeout(holdTimer);avatar.classList.remove('is-pressed');if(matchMedia('(hover:none)').matches)activeVideo?.pause();}
window.addEventListener('pointerup',releaseAvatar);window.addEventListener('pointercancel',releaseAvatar);
window.addEventListener('pointermove',startCycle,{passive:true});
document.addEventListener('visibilitychange',()=>{if(document.hidden){resetPhrases();idleVideo?.pause();activeVideo?.pause();}});
reduced.addEventListener('change',()=>{resetPhrases();if(reduced.matches){idleVideo?.pause();activeVideo?.pause();}refreshScroll();});
refreshScroll();
