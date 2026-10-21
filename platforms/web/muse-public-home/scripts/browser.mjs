import path from 'node:path';
import { pathToFileURL } from 'node:url';
import fs from 'node:fs/promises';
import { createHash } from 'node:crypto';
export const hash = bytes => createHash('sha256').update(bytes).digest('hex');
export async function launch(options={}) {
  const provider=process.env.PLAYWRIGHT_PATH || path.join(process.env.APPDATA,'npm/node_modules/rea-agents/node_modules/playwright-core/index.mjs');
  const {chromium}=await import(pathToFileURL(provider));
  const browser=await chromium.launch({headless:true,executablePath:process.env.CHROME_PATH || 'C:/Program Files/Google/Chrome/Application/chrome.exe'});
  const context=await browser.newContext({viewport:{width:1440,height:1000},deviceScaleFactor:1,locale:'en-US',timezoneId:'Asia/Shanghai',colorScheme:'light',reducedMotion:'no-preference',serviceWorkers:'block',...options});
  const page=await context.newPage();
  return {browser,context,page};
}
export async function saveJson(file,data){await fs.mkdir(path.dirname(file),{recursive:true});await fs.writeFile(file,JSON.stringify(data,null,2)+'\n');}
export async function saveCapture(page,file,details={}) {
  const startedAt=new Date().toISOString();
  await fs.mkdir(path.dirname(file),{recursive:true});
  const bytes=await page.screenshot({path:file});
  return {path:file,sha256:hash(bytes),captureInterval:{startedAt,completedAt:new Date().toISOString()},viewport:page.viewportSize(),...details};
}
