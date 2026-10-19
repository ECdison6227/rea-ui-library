const fs = require('node:fs/promises');
async function captureUI(page, root, name, locator) {
  const file = root + '/evidence/interactions/14-card-actions.json';
  let records = []; try { records = JSON.parse(await fs.readFile(file, 'utf8')); } catch {}
  const card = locator ? await locator.evaluate(e => ({html:e.outerHTML, rect:e.getBoundingClientRect().toJSON()})) : null;
  const state = await page.evaluate(() => ({url:location.href, at:new Date().toISOString(),
    dialogs:[...document.querySelectorAll('[role="dialog"]')].map(e => ({label:e.getAttribute('aria-label'), text:e.textContent.slice(0,5000)})),
    buttons:[...document.querySelectorAll('button')].filter(e => {const r=e.getBoundingClientRect();return r.width&&r.height&&r.bottom>0&&r.top<innerHeight;}).map(e=>({label:e.getAttribute('aria-label'),text:e.textContent?.slice(0,80),expanded:e.getAttribute('aria-expanded')}))
  }));
  const screenshot = root + '/evidence/screenshots/' + name + '.png';
  await page.screenshot({path:screenshot});
  records.push({name,...state,card}); await fs.writeFile(file,JSON.stringify(records,null,2));
  return {name,dialogs:state.dialogs.map(d=>({label:d.label,text:d.text.slice(0,180)})),buttons:state.buttons.slice(-16)};
}
module.exports = {captureUI};
