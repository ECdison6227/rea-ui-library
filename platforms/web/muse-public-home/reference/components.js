export const asset = sourcePath => '../assets/source/' + sourcePath.replace(/^\//,'');
const icon = name => `../assets/curated/${name}.svg`;
export const escapeHtml = value => String(value).replace(/[&<>"']/g,char=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[char]));
export const localPhrase = html => html.replace(/\s+srcset="[^"]*"/g,'').replace(/src="\//g,'src="../assets/source/');
const readingMark = '<svg class="reading-logo" viewBox="0 0 100 100" fill="none" aria-hidden="true"><path d="M24 25c13 0 20 5 26 12 6-7 13-12 26-12v48c-13 0-20 2-26 9-6-7-13-9-26-9V25Z" stroke="currentColor" stroke-width="8" stroke-linejoin="round"/><path d="M50 37v45" stroke="currentColor" stroke-width="7"/></svg>';

export function BrandMark({small=false,reading=false}={}) {
  return reading ? `<span class="${small?'header-mark':'brand-mark'}">${readingMark}</span>` : `<img class="${small?'header-mark':'brand-mark'}" src="${asset('/images/landing/brand/muse-logo.svg')}" alt="${small?'':'Muse'}" ${small?'aria-hidden="true"':''}>`;
}
export function Header({reading=false}={}) {
  return `<header class="site-header"><div class="container header-inner">${reading?'<span class="parent-brand reading-parent">Folio</span>':'<span class="parent-brand meta-mask" role="img" aria-label="Meta"></span>'}${BrandMark({small:true,reading})}<button type="button" class="button header-action" data-return>${reading?'My library':'Log in'}</button></div></header>`;
}
export function Hero(content,{reading=false}={}) {
  const avatar=reading?'<span class="avatar reading-avatar"><span>F</span></span>':`<video class="avatar avatar-idle" src="${asset('/videos/avatars/hatch_connecting.mp4')}" poster="${asset('/images/landing/brand/hatch.jpg')}" muted playsinline preload="auto" aria-hidden="true"></video><video class="avatar avatar-active" src="${asset('/videos/avatars/hatch_working.mp4')}" poster="${asset('/images/landing/brand/hatch.jpg')}" muted playsinline preload="auto" loop aria-hidden="true"></video>`;
  const phraseHtml=reading?'ideas worth coming back to':localPhrase(content.phrases[0].html);
  const measures=(reading?[{html:'ideas worth coming back to'}]:content.phrases).map(p=>`<span class="phrase-measure" aria-hidden="true">${localPhrase(p.html)}</span>`).join('');
  return `<section class="hero" aria-labelledby="hero-title"><div class="container hero-shell"><div class="hero-placement"><div class="hero-content">
    ${BrandMark({reading})}
    <h1 class="hero-title" id="hero-title"><span class="sr-only">${reading?'Folio, ideas worth coming back to':escapeHtml(content.title)}</span><span class="title-line" aria-hidden="true">${reading?'Folio':'Muse'}<span class="avatar-slot">${avatar}</span>${reading?', a home for':', AI that'}</span><span class="title-line" aria-hidden="true"><span class="phrase-stage">${measures}<span class="phrase">${phraseHtml}</span></span></span></h1>
    <div class="entry-panel"><p class="entry-title" id="entry-title">${reading?'Make room for your next good read':'Log in or create an account'}</p><form class="entry-form" aria-labelledby="entry-title" novalidate><label class="sr-only" for="entry-input">${reading?'Search your reading list':'Mobile number or email (required)'}</label><input class="text-field" id="entry-input" type="text" autocomplete="off" aria-label="${reading?'Search your reading list':'Mobile number or email'}" placeholder="${reading?'Search your reading list':'Mobile number or email'}" aria-describedby="entry-help"><p class="entry-help" id="entry-help">${reading?'A quiet place for articles, field notes, and the ideas you want to keep.':`You may receive SMS notifications from us by using your mobile number. <a href="https://www.meta.com/help/artificial-intelligence/31983416211257755/" target="_blank" rel="noreferrer">Learn more</a>`}</p><button class="button entry-submit" type="submit">${reading?'Find a story':'Continue'}</button></form></div>
  </div></div></div></section><a class="learn-cue" href="#introduction"><span>${reading?'Explore the library':'Learn more'}</span><img src="${icon('down')}" alt=""></a>`;
}
export function SectionIntro(title,description,id) {
  return `<header class="section-intro"><h2 class="section-heading" id="${id}">${escapeHtml(title)}</h2><p class="section-copy">${escapeHtml(description)}</p></header>`;
}
export function MediaSection({title,description,poster,walkthrough=false}) {
  const id=walkthrough?'walkthrough':'introduction';
  return `<section id="${id}" class="media-section ${walkthrough?'walkthrough-section':'intro-section'}" aria-labelledby="${id}-title" tabindex="-1"><div class="container">${SectionIntro(title,description,`${id}-title`)}<div class="media-stage"><div class="media-frame"><div class="media-surface" data-reveal><img src="${poster}" alt="${walkthrough?'Product designer presenting a guided tour of Muse':'A frame from the Muse introduction film'}" loading="lazy">${walkthrough?`<a class="play-button" href="https://muse.ai/#landing-product-walkthrough-title" target="_blank" rel="noreferrer" aria-label="Open the guided tour on Muse"><span><img src="${icon('play')}" alt=""></span></a>`:''}</div></div></div></div></section>`;
}
export function FeatureRow(item,{reading=false}={}) {
  const illustration=reading?`<div class="reading-card"><small>${escapeHtml(item.category)}</small><strong>${escapeHtml(item.cardTitle)}</strong><span>${escapeHtml(item.readTime)}</span></div>`:`<img src="${asset(item.image)}" alt="${escapeHtml(item.imageAlt)}" loading="lazy" decoding="async">`;
  return `<section class="feature-row${item.alternate?' is-alternate':''}" aria-labelledby="${item.id}" data-feature><div class="container feature-inner"><div class="feature-copy"><h3 class="section-heading" id="${item.id}">${escapeHtml(item.title)}</h3><p class="feature-description">${reading?escapeHtml(item.description):item.descriptionHtml}</p></div><div class="feature-art">${illustration}</div></div></section>`;
}
export function ReadingLinks(content) {
  return `<div class="container"><section class="reading-section" aria-labelledby="learn-title"><div class="container reading-inner"><div class="reading-heading"><h2 class="section-heading" id="learn-title">${escapeHtml(content.learn.title)}</h2><p class="section-copy">${escapeHtml(content.learn.description)}</p></div><div class="story-list">${content.stories.map(item=>`<article class="story"><a class="story-link" href="${escapeHtml(item.href)}" target="_blank" rel="noreferrer"><img class="story-icon" src="${icon(item.icon)}" alt=""><div class="story-content"><div class="story-text"><h3 class="story-title">${escapeHtml(item.title)}</h3><p class="story-description">${escapeHtml(item.description)}</p></div><img class="chevron" src="${icon('chevron')}" alt=""></div></a></article>`).join('')}</div></div></section><section class="end-section" aria-labelledby="end-title"><h2 class="section-heading" id="end-title">Ready to try Muse?</h2><button class="button" type="button" data-return>Log in</button></section></div>`;
}
export function Footer({reading=false}={}) {
  if(reading)return '<footer class="container site-footer"><p>Folio · A personal reading collection</p><span>Three stories. A little room to think.</span></footer>';
  return `<footer class="container site-footer"><p>© Meta 2026</p><nav class="legal-links" aria-label="Legal links"><a href="https://muse.ai/privacy" target="_blank" rel="noreferrer">Privacy Policy</a><a href="https://www.facebook.com/privacy/policies/cookies/" target="_blank" rel="noreferrer">Cookie Policy</a><a href="https://muse.ai/terms" target="_blank" rel="noreferrer">Terms</a></nav><span class="footer-brand">from <span class="meta-mask" role="img" aria-label="Meta"></span></span></footer>`;
}
