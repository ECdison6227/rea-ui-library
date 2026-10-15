const fs = require('node:fs');
const path = require('node:path');
const { pathToFileURL } = require('node:url');
const assert = require('node:assert/strict');
let playwright;
try { playwright = require(process.env.PLAYWRIGHT_CORE_MODULE || 'playwright-core'); }
catch (error) {
  if (!process.env.APPDATA) throw error;
  playwright = require(path.join(process.env.APPDATA, 'npm/node_modules/rea-agents/node_modules/playwright-core'));
}
const reportRoot = path.resolve(process.argv[2] || __dirname);
const browserPath = process.env.BROWSER_PATH || [
  process.env['ProgramFiles(x86)'] && path.join(process.env['ProgramFiles(x86)'], 'Microsoft/Edge/Application/msedge.exe'),
  process.env.ProgramFiles && path.join(process.env.ProgramFiles, 'Microsoft/Edge/Application/msedge.exe'),
].find(candidate => candidate && fs.existsSync(candidate));
(async () => {
  const browser = await playwright.chromium.launch({ headless: true, ...(browserPath ? { executablePath: browserPath } : {}) });
  try {
    const page = await browser.newPage({ viewport: { width: 1440, height: 1000 } });
    const errors = [], externalRequests = [];
    page.on('pageerror', error => errors.push(error.message));
    page.on('request', request => { if (/^https?:/.test(request.url())) externalRequests.push(request.url()); });
    await page.goto(pathToFileURL(path.join(reportRoot, 'index.html')).href, { waitUntil: 'load' });
    await page.waitForFunction(() => window.COMPARISON_REPORT?.pages.length === 3);
    await page.evaluate(() => {
      for (const node of document.querySelectorAll('details')) node.open = true;
      for (const image of document.images) image.loading = 'eager';
    });
    await page.waitForFunction(() => [...document.images].length === 13 && [...document.images].every(image => image.complete && image.naturalWidth > 0));
    for (const mode of ['50% 叠图', '差异 ×3', '原始差异', '并排']) {
      for (const button of await page.getByRole('button', { name: mode, exact: true }).all()) await button.click();
      await page.waitForFunction(() => [...document.querySelectorAll('.stage img')].every(image => image.complete && image.naturalWidth > 0));
    }
    const snapshot = await page.evaluate(() => ({
      pageIds: window.COMPARISON_REPORT.pages.map(item => item.id),
      componentIds: window.COMPARISON_REPORT.pages.flatMap(item => item.components.map(component => component.id)),
      originalEvidenceIds: window.COMPARISON_REPORT.pages.map(item => item.originalEvidence.evidenceId),
      referenceEvidenceIds: window.COMPARISON_REPORT.pages.map(item => item.referenceEvidence.evidenceId),
      decodedVisibleImages: [...document.images].filter(image => image.complete && image.naturalWidth > 0).length,
    }));
    assert.equal(new Set(snapshot.componentIds).size, 10);
    assert(snapshot.originalEvidenceIds.every(Boolean));
    assert(snapshot.referenceEvidenceIds.every(Boolean));
    assert.equal(errors.length, 0);
    assert.equal(externalRequests.length, 0);
    const result = { passed: true, checkedAtUtc: new Date().toISOString(), scope: 'Offline viewer and derived image loading only; not original/reference visual equivalence', ...snapshot, errors, externalRequests };
    fs.writeFileSync(path.join(reportRoot, 'browser-check.json'), JSON.stringify(result, null, 2));
    console.log(JSON.stringify(result, null, 2));
  } finally { await browser.close(); }
})().catch(error => { console.error(error.stack); process.exitCode = 1; });
