// Run with an installed @playwright/test package; PLAYWRIGHT_PACKAGE may point to its package.json.
import { createRequire } from 'node:module';
import fs from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
const require = createRequire(process.env.PLAYWRIGHT_PACKAGE || import.meta.url);
const { chromium, expect } = require('@playwright/test');
const root = fileURLToPath(new URL('../', import.meta.url));
const base = process.env.REFERENCE_URL || 'http://127.0.0.1:4328';
const browser = await chromium.launch({ headless: true, args: ['--mute-audio'] });
const results = [], errors = [], external = [];
const check = async (name, run) => { await run(); results.push({ name, status: 'passed' }); };
try {
  const context = await browser.newContext({ viewport: { width: 390, height: 844 }, deviceScaleFactor: 1, isMobile: true, hasTouch: true });
  const page = await context.newPage();
  page.on('pageerror', error => errors.push(error.message));
  page.on('request', request => { if (new URL(request.url()).origin !== new URL(base).origin) external.push(new URL(request.url()).origin); });
  await page.goto(`${base}/web-mobile.html`);
  await check('390px layout and source-bound dimensions', async () => {
    const measured = await page.evaluate(() => {
      const input = document.querySelector('.composer'), box = input.getBoundingClientRect();
      const style = getComputedStyle(document.querySelector('.bubble'));
      return { x: box.x, y: box.y, width: box.width, height: box.height, bottom: innerHeight - box.bottom, font: style.fontSize, line: style.lineHeight, overflow: document.documentElement.scrollWidth > innerWidth };
    });
    expect(measured).toMatchObject({ x: 16, width: 358, height: 48, bottom: 32, font: '15px', line: '19px', overflow: false });
    results.push({ name: 'measured-390', classification: 'D', measured });
    await page.screenshot({ path: path.join(root, 'comparison/web-mobile-dark-390.png') });
  });
  await check('tabs and keyboard navigation preserve the local draft', async () => {
    await page.locator('#draft').fill('保留这条草稿');
    await page.getByRole('tab', { name: '记录', exact: true }).click();
    await expect(page.locator('#records')).toBeVisible(); await expect(page.locator('#composer-area')).toBeHidden();
    await page.getByRole('tab', { name: '记录', exact: true }).press('ArrowLeft');
    await expect(page.getByRole('tab', { name: '对话', exact: true })).toHaveAttribute('aria-selected', 'true');
    await expect(page.locator('#draft')).toHaveValue('保留这条草稿');
    await page.locator('#draft').fill('');
  });
  await check('template selection, local send, records search', async () => {
    await page.getByRole('button', { name: '选择记录模板', exact: true }).click();
    await page.locator('[data-template="现场观察："]').click();
    await expect(page.locator('#draft')).toBeFocused(); await expect(page.locator('#draft')).toHaveValue('现场观察：');
    await page.locator('#draft').fill('现场观察：北桥水面平静。');
    await page.getByRole('button', { name: '发送记录', exact: true }).click();
    await expect(page.locator('#cancel')).toBeVisible();
    await expect(page.locator('#messages .user-bubble')).toHaveCount(1);
    await expect(page.locator('#draft')).toHaveValue('');
    await page.getByRole('button', { name: '查找本地记录', exact: true }).click();
    await expect(page.locator('#record-search')).toBeFocused();
    await page.locator('#record-search').fill('北桥'); await expect(page.locator('.record')).toHaveCount(1);
    await page.locator('#record-search').fill('没有此内容'); await expect(page.locator('.record')).toHaveCount(0);
    await expect(page.locator('.empty-records')).toContainText('没有匹配');
    await page.getByRole('tab', { name: '对话', exact: true }).click();
  });
  await check('error retains draft and retry commits once', async () => {
    await page.getByRole('button', { name: '打开参考设置', exact: true }).click();
    await page.locator('#fail').check(); await page.getByRole('button', { name: '关闭设置', exact: true }).click();
    await page.locator('#draft').fill('错误恢复测试'); await page.getByRole('button', { name: '发送记录', exact: true }).click();
    await expect(page.locator('#feedback')).toContainText('草稿已保留'); await expect(page.locator('#draft')).toHaveValue('错误恢复测试');
    await page.getByRole('button', { name: '重试', exact: true }).click();
    await expect(page.locator('#messages .user-bubble')).toHaveCount(2); await expect(page.locator('#draft')).toHaveValue('');
  });
  await check('cancel keeps draft and no delayed completion', async () => {
    await page.locator('#draft').fill('取消测试'); await page.getByRole('button', { name: '发送记录', exact: true }).click();
    await page.getByRole('button', { name: '取消请求', exact: true }).click();
    await expect(page.locator('#draft')).toHaveValue('取消测试');
    await page.waitForTimeout(1450); await expect(page.locator('#messages .user-bubble')).toHaveCount(2);
    await page.locator('#draft').fill('');
  });
  await check('details, completion/undo and theme sheet return', async () => {
    await page.locator('#details').click(); await expect(page.locator('#card-details')).toBeVisible();
    await page.locator('#complete').click(); await expect(page.locator('#done')).toBeVisible();
    await page.locator('#complete').click(); await expect(page.locator('#done')).toBeHidden();
    await page.getByRole('button', { name: '打开参考设置', exact: true }).click();
    await page.locator('[data-theme-choice="light"]').click();
    await expect(page.locator('html')).toHaveAttribute('data-theme', 'light');
    await page.screenshot({ path: path.join(root, 'comparison/web-mobile-light-sheet-390.png') });
    await page.keyboard.press('Escape'); await expect(page.locator('#settings')).toBeFocused();
  });
  await check('320px reduced-motion and landscape do not overflow', async () => {
    await page.emulateMedia({ reducedMotion: 'reduce' }); await page.setViewportSize({ width: 320, height: 700 });
    await page.getByRole('tab', { name: '对话', exact: true }).click();
    expect(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)).toBe(true);
    await page.screenshot({ path: path.join(root, 'comparison/web-mobile-reduced-320.png') });
    await page.setViewportSize({ width: 844, height: 390 });
    expect(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)).toBe(true);
    await page.getByRole('button', { name: '打开参考设置', exact: true }).click();
    const box = await page.locator('#sheet').boundingBox(); expect(box.height).toBeLessThanOrEqual(390 * .85 + 1);
    expect(await page.locator('#sheet').evaluate(node => getComputedStyle(node).animationName)).toBe('none');
  });
  expect(errors).toEqual([]); expect(external).toEqual([]);
  await context.close();
} finally {
  await browser.close();
  await fs.writeFile(path.join(root, 'comparison/web-mobile-verification.json'), JSON.stringify({ classification: 'D', sourceRuntimeVerified: false,
    verifiedAt: new Date().toISOString(), scope: 'Independent transfer reference only; historical Web A is not rerun; no Android keyboard/hardware or backend validation.', results, errors, externalRequests: external }, null, 2) + '\n');
}
console.log(JSON.stringify({ scenarios: results.filter(result => result.status === 'passed').length, errors: errors.length, externalRequests: external.length }));
