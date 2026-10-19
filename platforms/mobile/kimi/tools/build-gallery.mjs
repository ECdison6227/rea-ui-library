/**
 * Build an offline research/asset browser. This is not an app reconstruction.
 * Only Node core modules are used. Source assets are read and SHA-256 checked,
 * never rewritten. Run: node research/kimi-android/tools/build-gallery.mjs
 */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import vm from 'node:vm';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const manifestPath = path.join(root, 'assets/manifest.json');
const manifestBytes = fs.readFileSync(manifestPath);
const manifest = JSON.parse(manifestBytes);
const hash = bytes => crypto.createHash('sha256').update(bytes).digest('hex');
const relative = file => path.relative(root, file).replaceAll('\\', '/');
const escaped = value => String(value).replace(/[&<>"']/g, char => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[char]));
const jsonForHtml = value => JSON.stringify(value).replaceAll('<', '\\u003c').replaceAll('\u2028', '\\u2028').replaceAll('\u2029', '\\u2029');
const readOptional = file => fs.existsSync(path.join(root, file)) ? JSON.parse(fs.readFileSync(path.join(root, file), 'utf8')) : null;
const writeJson = (file, data) => { fs.mkdirSync(path.dirname(file), { recursive: true }); fs.writeFileSync(file, JSON.stringify(data, null, 2) + '\n'); };

function xmlUnescape(value) {
  return value.replace(/&(#x[\da-fA-F]+|#\d+|amp|quot|apos|lt|gt);/g, (_, entity) => {
    if (entity[0] === '#') return String.fromCodePoint(parseInt(entity.slice(entity[1] === 'x' ? 2 : 1), entity[1] === 'x' ? 16 : 10));
    return ({ amp: '&', quot: '"', apos: "'", lt: '<', gt: '>' })[entity];
  });
}
function parseAttributes(text) {
  const attrs = {};
  const attr = /([\w:.-]+)\s*=\s*("[^"]*"|'[^']*')/g;
  let cursor = 0;
  for (const match of text.matchAll(attr)) {
    if (text.slice(cursor, match.index).trim()) throw new Error('unparsed-attribute');
    if (attrs[match[1]] !== undefined) throw new Error('duplicate-attribute');
    attrs[match[1]] = xmlUnescape(match[2].slice(1, -1));
    cursor = match.index + match[0].length;
  }
  if (text.slice(cursor).trim()) throw new Error('unparsed-attribute');
  return attrs;
}
function numeric(value, fallback, options = {}) {
  if (value === undefined) return fallback;
  if (!/^[+-]?(?:\d+\.?\d*|\.\d+)(?:[eE][+-]?\d+)?$/.test(value)) throw new Error('nonliteral-number');
  const n = Number(value);
  if (!Number.isFinite(n) || (options.positive && n <= 0)) throw new Error('invalid-number');
  if (options.alpha && (n < 0 || n > 1)) throw new Error('invalid-alpha');
  return n;
}
function literalColor(value, fallback = { color: '#000000', alpha: 0 }) {
  if (value === undefined) return fallback;
  if (!/^#[\da-fA-F]{3,8}$/.test(value)) throw new Error('nonliteral-color');
  const h = value.slice(1);
  if (h.length === 3) return { color: '#' + [...h].map(x => x + x).join(''), alpha: 1 };
  if (h.length === 4) return { color: '#' + [...h.slice(1)].map(x => x + x).join(''), alpha: parseInt(h[0] + h[0], 16) / 255 };
  if (h.length === 6) return { color: value, alpha: 1 };
  if (h.length === 8) return { color: '#' + h.slice(2), alpha: parseInt(h.slice(0, 2), 16) / 255 };
  throw new Error('unsupported-color');
}
function checkedAttributes(attrs, allowed) {
  for (const attr of Object.keys(attrs)) {
    if (attr.startsWith('xmlns:')) continue;
    if (!allowed.includes(attr)) throw new Error('unsupported-attribute:' + attr);
  }
}

/** Restricted literal VectorDrawable subset: no themes, references, tint, clips,
 * gradients, RTL mirroring or trim paths. No guessed substitute is produced. */
function vectorToSvg(bytes) {
  let xml = bytes.toString('utf8').replace(/^\uFEFF/, '');
  if (!xml.trimStart().startsWith('<')) return { reason: 'binary-xml-no-browser-preview' };
  if (!/<vector(?:\s|>)/.test(xml)) return { reason: 'not-an-android-vector' };
  const disallowed = [
    [/<(?:aapt:attr|gradient)(?:\s|>)/, 'gradient-not-converted'],
    [/<clip-path(?:\s|>)/, 'clip-path-not-converted'],
    [/android:tint\s*=/, 'tint-not-converted'],
    [/android:trimPath\w*\s*=/, 'trim-path-not-converted'],
    [/android:autoMirrored\s*=\s*["']true["']/, 'rtl-mirroring-not-converted'],
    [/<!DOCTYPE|<!ENTITY/, 'declarations-not-supported'],
  ];
  for (const [re, reason] of disallowed) if (re.test(xml)) return { reason };
  xml = xml.replace(/<!--[\s\S]*?-->/g, '').replace(/<\?xml[\s\S]*?\?>/g, '').trim();
  try {
    let cursor = 0, rootSeen = false, rootClosed = false;
    const stack = [], result = [];
    let dimensions;
    for (const token of xml.matchAll(/<([^>]+)>/g)) {
      if (xml.slice(cursor, token.index).trim()) throw new Error('text-content-not-supported');
      cursor = token.index + token[0].length;
      const close = token[1].startsWith('/');
      const selfClosing = /\/\s*$/.test(token[1]);
      const content = token[1].replace(/^\//, '').replace(/\/\s*$/, '').trim();
      const tagMatch = /^(\S+)([\s\S]*)$/.exec(content);
      const [, tag, rest] = tagMatch;
      if (!['vector', 'group', 'path'].includes(tag)) throw new Error('unsupported-element:' + tag);
      if (close) {
        if (rest.trim() || stack.pop() !== tag) throw new Error('unbalanced-xml');
        result.push(tag === 'vector' ? '</svg>' : tag === 'group' ? '</g>' : '</path>');
        if (tag === 'vector') rootClosed = true;
        continue;
      }
      if (rootClosed || (!rootSeen && tag !== 'vector')) throw new Error('invalid-root');
      if (rootSeen && stack.at(-1) !== 'vector' && stack.at(-1) !== 'group') throw new Error('invalid-parent');
      const attrs = parseAttributes(rest);
      const a = name => attrs['android:' + name];
      if (tag === 'vector') {
        if (rootSeen) throw new Error('nested-vector');
        checkedAttributes(attrs, ['android:width', 'android:height', 'android:viewportWidth', 'android:viewportHeight', 'android:name', 'android:alpha', 'android:autoMirrored']);
        const vw = numeric(a('viewportWidth'), undefined, { positive: true });
        const vh = numeric(a('viewportHeight'), undefined, { positive: true });
        if (vw === undefined || vh === undefined) throw new Error('missing-viewport');
        const dimension = value => { if (!/^\d+(?:\.\d+)?(?:dp|dip|px)$/.test(value ?? '')) throw new Error('unsupported-dimension'); return parseFloat(value); };
        dimensions = { widthDeclaration: a('width'), heightDeclaration: a('height'), viewportWidth: vw, viewportHeight: vh };
        result.push('<svg xmlns="http://www.w3.org/2000/svg" width="' + dimension(a('width')) + '" height="' + dimension(a('height')) + '" viewBox="0 0 ' + vw + ' ' + vh + '" opacity="' + numeric(a('alpha'), 1, { alpha: true }) + '">');
        rootSeen = true;
      } else if (tag === 'group') {
        checkedAttributes(attrs, ['android:name', 'android:rotation', 'android:pivotX', 'android:pivotY', 'android:translateX', 'android:translateY', 'android:scaleX', 'android:scaleY']);
        const px = numeric(a('pivotX'), 0), py = numeric(a('pivotY'), 0), tx = numeric(a('translateX'), 0), ty = numeric(a('translateY'), 0);
        result.push('<g transform="translate(' + (tx + px) + ' ' + (ty + py) + ') rotate(' + numeric(a('rotation'), 0) + ') scale(' + numeric(a('scaleX'), 1) + ' ' + numeric(a('scaleY'), 1) + ') translate(' + -px + ' ' + -py + ')">');
      } else {
        checkedAttributes(attrs, ['android:name', 'android:pathData', 'android:fillColor', 'android:fillAlpha', 'android:fillType', 'android:strokeColor', 'android:strokeAlpha', 'android:strokeWidth', 'android:strokeLineCap', 'android:strokeLineJoin', 'android:strokeMiterLimit']);
        const d = a('pathData');
        if (!d || /[^\s\d.,+\-eEmMzZlLhHvVcCsSqQtTaA]/.test(d)) throw new Error('unsupported-path-data');
        const fill = literalColor(a('fillColor')), stroke = literalColor(a('strokeColor'));
        const fillRule = a('fillType') ?? 'nonZero';
        if (!['evenOdd', 'nonZero'].includes(fillRule)) throw new Error('unsupported-fill-rule');
        const cap = a('strokeLineCap') ?? 'butt', join = a('strokeLineJoin') ?? 'miter';
        if (!['butt', 'round', 'square'].includes(cap) || !['miter', 'round', 'bevel'].includes(join)) throw new Error('unsupported-stroke-shape');
        result.push('<path d="' + escaped(d) + '" fill="' + fill.color + '" fill-opacity="' + fill.alpha * numeric(a('fillAlpha'), 1, { alpha: true }) + '" fill-rule="' + (fillRule === 'evenOdd' ? 'evenodd' : 'nonzero') + '" stroke="' + stroke.color + '" stroke-opacity="' + stroke.alpha * numeric(a('strokeAlpha'), 1, { alpha: true }) + '" stroke-width="' + numeric(a('strokeWidth'), 0) + '" stroke-linecap="' + cap + '" stroke-linejoin="' + join + '" stroke-miterlimit="' + numeric(a('strokeMiterLimit'), 4) + '">');
      }
      if (selfClosing) {
        result.push(tag === 'vector' ? '</svg>' : tag === 'group' ? '</g>' : '</path>');
        if (tag === 'vector') rootClosed = true;
      } else stack.push(tag);
    }
    if (!rootSeen || !rootClosed || stack.length || xml.slice(cursor).trim()) throw new Error('incomplete-xml');
    return { svg: result.join('\n') + '\n', dimensions };
  } catch (error) { return { reason: error.message }; }
}

function imageMime(bytes) {
  if (bytes.subarray(0, 8).equals(Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]))) return 'image/png';
  if (bytes[0] === 255 && bytes[1] === 216 && bytes[2] === 255) return 'image/jpeg';
  if (/^GIF8[79]a/.test(bytes.toString('ascii', 0, 6))) return 'image/gif';
  if (bytes.toString('ascii', 0, 4) === 'RIFF' && bytes.toString('ascii', 8, 12) === 'WEBP') return 'image/webp';
  return null;
}

const derived = [], skipped = [], assets = [];
const fonts = [];
for (const asset of manifest.assets) {
  const sourceFile = path.resolve(root, asset.path);
  if (!sourceFile.startsWith(root + path.sep)) throw new Error('Asset escapes research directory: ' + asset.path);
  const bytes = fs.readFileSync(sourceFile);
  const sourceHash = hash(bytes);
  if (sourceHash !== asset.sha256 || bytes.length !== asset.bytes) throw new Error('Source asset checksum mismatch: ' + asset.path);
  const row = { id: asset.id, type: asset.type, path: asset.path, sourceEntry: asset.sourceEntry, sha256: sourceHash, bytes: bytes.length, scope: asset.scope, usageStatus: asset.usageStatus, classification: asset.sourceClassification ?? 'B' };
  if (asset.type === 'image') {
    const mime = imageMime(bytes);
    if (mime) { row.preview = 'data:' + mime + ';base64,' + bytes.toString('base64'); row.previewKind = 'packaged-image'; }
    else row.previewReason = 'unrecognized-image-format';
  } else if (asset.type === 'xml-resource') {
    const converted = vectorToSvg(bytes);
    if (converted.svg) {
      const svgBytes = Buffer.from(converted.svg);
      const output = path.join(root, 'assets/derived/vector-svg', asset.id + '-' + path.basename(asset.sourceEntry, '.xml') + '.svg');
      fs.mkdirSync(path.dirname(output), { recursive: true });
      fs.writeFileSync(output, svgBytes);
      row.preview = 'data:image/svg+xml;base64,' + svgBytes.toString('base64');
      row.previewKind = 'derived-vector'; row.derivedPath = relative(output);
      row.derivedSha256 = hash(svgBytes);
      derived.push({ assetId: asset.id, sourcePath: asset.path, sourceEntry: asset.sourceEntry, sourceSha256: sourceHash, derivedPath: row.derivedPath, derivedSha256: row.derivedSha256, method: 'literal-vector-subset-to-svg', sourceClassification: 'B', derivedClassification: 'D', runtimeVerified: false, dimensions: converted.dimensions });
    } else {
      row.previewReason = converted.reason;
      skipped.push({ assetId: asset.id, sourcePath: asset.path, sourceSha256: sourceHash, reason: converted.reason });
    }
  }
  if (asset.type === 'font' && asset.scope === 'kimi-compose') fonts.push({ ...row, fontData: 'data:' + (/\.otf$/i.test(asset.sourceEntry) ? 'font/otf' : 'font/ttf') + ';base64,' + bytes.toString('base64') });
  assets.push(row);
}

const vectorManifest = { schemaVersion: 1, sourceApkSha256: manifest.sourceApkSha256, sourceManifestSha256: hash(manifestBytes), purpose: 'Research asset preview only; not a Kimi screen, native reference implementation or runtime evidence', generator: 'tools/build-gallery.mjs', conversionScope: 'Literal vector/group/path attributes, transforms, colors, alpha, strokes and fill rules only. Android dimensions retained as declarations; numeric SVG preview units do not establish dp-to-CSS-px equivalence.', excluded: 'Gradients, clip paths, tints, RTL mirroring, trim paths, resource/theme references, unknown attributes, unsupported elements and binary XML receive original-file links only.', derived, skipped };
writeJson(path.join(root, 'assets/derived/vector-svg/manifest.json'), vectorManifest);

const copyIndex = readOptional('assets/derived/copy/index.json');
const fontMetadata = readOptional('assets/derived/fonts/metadata.json');
const widgetTokens = readOptional('tokens/widget-foundation.json');
const cssAsset = assets.find(a => a.sourceEntry.endsWith('/widget_foundation.css'));
const css = cssAsset ? fs.readFileSync(path.join(root, cssAsset.path), 'utf8') : '';
const chosenColors = ['--seo-chat-Colors-KMBlue', '--seo-chat-Colors-Red', '--seo-chat-Colors-Green', '--seo-chat-Colors-Yellow', '--seo-chat-Bg-Primary', '--seo-chat-Bg-Secondary', '--seo-chat-Labels-Primary', '--seo-chat-Labels-Secondary'];
function widgetDeclarations(theme) {
  if (widgetTokens) return widgetTokens.declarations[theme];
  const re = theme === 'dark' ? /:root\.dark\s*\{([^{}]*)\}/ : /:root\s*\{([^{}]*)\}/;
  const body = re.exec(css);
  if (!body) return [];
  const start = body.index + body[0].indexOf('{') + 1;
  return [...body[1].matchAll(/(--[\w-]+)\s*:\s*([^;]+);/g)].map(m => ({ name: m[1], value: m[2].trim(), sourceLine: css.slice(0, start + m.index).split('\n').length }));
}
const colorPanels = ['light', 'dark'].map(theme => {
  const declarations = widgetDeclarations(theme);
  const colors = chosenColors.map(name => declarations.find(d => d.name === name)).filter(d => d && /^(#[a-fA-F0-9]{3,8}|rgba?\([\d\s.,%]+\))$/.test(d.value));
  return '<div class="color-panel ' + theme + '"><h3>' + (theme === 'light' ? '浅色声明' : '深色声明') + '</h3><div class="swatches">' + colors.map(d => '<div class="swatch"><span style="background:' + escaped(d.value) + '"></span><div><code>' + escaped(d.name.replace('--seo-chat-', '')) + '</code><small>' + escaped(d.value) + ' · L' + d.sourceLine + '</small></div></div>').join('') + '</div></div>';
}).join('');

const scopeNames = { 'kimi-compose': 'Kimi Compose', 'content-renderer-dependency': '内容渲染依赖', 'android-resource-unresolved-owner': 'Android 资源 · 归属待核', 'supporting-asset-unresolved-owner': '辅助资源 · 归属待核' };
const typeNames = { image: '包内图片', 'xml-resource': 'XML / Vector', animation: 'Rive 动效', font: '字体', 'localized-copy': '本地化文案', 'renderer-code': '渲染代码', other: '其他' };
const counts = key => Object.fromEntries([...new Set(assets.map(a => a[key]))].map(value => [value, assets.filter(a => a[key] === value).length]));
const scopeCounts = counts('scope'), typeCounts = counts('type');
const previewCount = assets.filter(a => a.preview).length;
const localeAssets = assets.filter(a => a.type === 'localized-copy');
const locales = localeAssets.map(a => {
  const q = /\/(values(?:-[^/]+)?)\//.exec(a.sourceEntry)?.[1] ?? 'unknown';
  const locale = q === 'values' ? 'default' : q.slice(7).replace(/-r([A-Z]{2})$/, '-$1');
  return { ...a, locale, qualifier: q, derived: copyIndex?.resources?.find(r => r.locale === locale) };
});
const fontStyle = fonts.map((font, i) => '@font-face{font-family:"asset-font-' + i + '";src:url("' + font.fontData + '");font-display:swap}').join('\n');
const fontCards = fonts.map((font, i) => {
  const meta = fontMetadata?.fonts?.find(f => f.sourcePath === font.path);
  return '<article class="font-card"><div class="font-example" style="font-family:asset-font-' + i + ',monospace">Ag 0123456789<br>The quick brown fox.</div><b>' + escaped(path.basename(font.sourceEntry)) + '</b><p>' + escaped(meta ? [meta.family, meta.subfamily, 'weight ' + meta.os2?.weightClass].join(' · ') : '包内字体文件') + '</p><small>浏览器试排；不证明 Android 字体选择或逐字回退</small><a href="' + escaped(font.path) + '">原件 ↗</a></article>';
}).join('');
const localeRows = locales.map(a => '<tr><td><b>' + escaped(a.locale) + '</b></td><td><code>' + escaped(a.qualifier) + '</code></td><td>' + (a.derived?.entries ?? '未生成') + '</td><td><a href="' + escaped(a.path) + '">CVR 原件</a>' + (a.derived?.output ? ' · <a href="' + escaped(a.derived.output) + '">解码 JSON</a>' : '') + '</td></tr>').join('');
const riveRows = assets.filter(a => a.type === 'animation').map(a => '<a class="rive-item" href="' + escaped(a.path) + '"><span>RIV</span><b>' + escaped(path.basename(a.sourceEntry)) + '</b><small>' + (a.bytes / 1024).toFixed(1) + ' KB</small></a>').join('');
const optionalDocs = ['README.md', 'docs/coverage.md', 'tokens/widget-foundation.json', 'assets/derived/fonts/metadata.json', 'assets/derived/copy/index.json'].filter(p => fs.existsSync(path.join(root, p))).map(p => '<a href="' + escaped(p) + '">' + escaped(p) + ' ↗</a>').join('');

const html = `<!doctype html>
<html lang="zh-CN"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><meta name="color-scheme" content="light"><title>Kimi Android · 资源研究台</title>
<style>
:root{font-family:Inter,"Segoe UI","Microsoft YaHei",sans-serif;color:#192c3c;background:#f3f5f7;font-size:14px;line-height:1.6}*{box-sizing:border-box}html{scroll-behavior:smooth}body{margin:0}a{color:#28588c;text-decoration:none}a:hover{text-decoration:underline}button,input,select{font:inherit}button,select{cursor:pointer}button:disabled{cursor:default;opacity:.45}button,input,select{border:1px solid #d7dfe6;border-radius:9px;background:#fff;padding:9px 12px;color:inherit}button:hover{border-color:#7095b7}button:focus-visible,input:focus-visible,select:focus-visible,a:focus-visible{outline:3px solid #8bbbe0;outline-offset:3px}header{background:#142d43;color:#fff;padding:48px max(30px,calc((100vw - 1380px)/2));border-bottom:5px solid #74b4b0}header .eyebrow{color:#b6d8dc;font-size:12px;letter-spacing:.12em}header h1{font-size:32px;line-height:1.3;margin:9px 0 13px;letter-spacing:-.7px}header p{max-width:850px;color:#d3dfe8;margin:0}header .badges{display:flex;gap:8px;margin-top:23px;flex-wrap:wrap}.badge{font-size:11px;border:1px solid #b7c6d5;padding:3px 8px;border-radius:20px;display:inline-block}.badges .badge{border-color:#527086;color:#e7f5ff}.layout{max-width:1440px;margin:auto;display:grid;grid-template-columns:190px minmax(0,1fr);gap:38px;padding:32px 30px 60px}aside{align-self:start;position:sticky;top:24px;display:grid;gap:4px}aside a{padding:9px 12px;border-radius:8px;color:#42586b;font-size:13px}aside a:hover{background:#e4ebf1;text-decoration:none}aside small{color:#7a8e9e;padding:10px 12px}.content{min-width:0}section{scroll-margin-top:25px;margin-bottom:40px}h2{font-size:21px;margin:0 0 8px;letter-spacing:-.4px}h3{margin:0 0 15px;font-size:14px}p{margin:0 0 15px}.muted{color:#667c8e;font-size:13px}.notice{background:#e7eff5;border-left:3px solid #5b87a9;border-radius:0 9px 9px 0;padding:15px 18px;font-size:13px;color:#355671;margin-bottom:22px}.stats{display:grid;grid-template-columns:repeat(5,1fr);gap:12px;margin:22px 0}.stat{background:#fff;border:1px solid #dfe6eb;border-radius:12px;padding:17px}.stat b{display:block;font-size:27px;line-height:1.3;color:#233f55}.stat small{color:#748594;font-size:11px}.scope-list{display:flex;gap:8px;flex-wrap:wrap}.scope-chip{background:#e5ebf0;color:#4f6678;padding:5px 10px;border-radius:7px;font-size:11px}.toolbar{display:flex;gap:9px;flex-wrap:wrap;background:#fff;border:1px solid #dfe6eb;border-radius:12px;padding:14px;margin:18px 0 8px}.toolbar input{flex:1;min-width:190px}.toolbar select{max-width:220px}.toolbar button{font-size:12px}.gallery-meta{display:flex;justify-content:space-between;align-items:center;gap:10px;margin-bottom:15px;font-size:12px;color:#708395}.grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(170px,1fr));gap:12px}.asset{background:#fff;min-width:0;border:1px solid #dfe6eb;border-radius:11px;overflow:hidden}.thumb{height:120px;background-color:#f6f7f8;background-image:linear-gradient(45deg,#e8ecef 25%,transparent 25%),linear-gradient(-45deg,#e8ecef 25%,transparent 25%),linear-gradient(45deg,transparent 75%,#e8ecef 75%),linear-gradient(-45deg,transparent 75%,#e8ecef 75%);background-size:14px 14px;background-position:0 0,0 7px,7px -7px,-7px 0;display:flex;align-items:center;justify-content:center;padding:18px;overflow:hidden}.grid.dark-preview .thumb{background-color:#2a343d;background-image:linear-gradient(45deg,#313d47 25%,transparent 25%),linear-gradient(-45deg,#313d47 25%,transparent 25%),linear-gradient(45deg,transparent 75%,#313d47 75%),linear-gradient(-45deg,transparent 75%,#313d47 75%)}.thumb img{max-width:100%;max-height:100%;object-fit:contain}.thumb .placeholder{font-size:12px;color:#6c8191;border:1px dashed #99aab6;border-radius:7px;padding:9px 15px;background:#f3f6f8}.asset-info{padding:12px}.asset-name{font-size:12px;font-weight:650;display:block;word-break:break-all;line-height:1.4;min-height:33px}.asset-tag{font-size:10px;display:inline-block;color:#416c79;background:#e6f1f1;border-radius:4px;padding:1px 6px;margin:8px 0 3px}.asset-tag.derived{background:#f1eadf;color:#8b6736}.asset-tag.unavailable{background:#eceff3;color:#748392}.asset-sub{display:block;color:#80909d;font-size:10px;margin-bottom:7px}.asset details{font-size:10px;color:#677c8c}.asset details summary{cursor:pointer}.asset details p{margin:7px 0;overflow-wrap:anywhere}.asset code{font-size:9px}.asset-links{font-size:11px;margin-top:8px;display:flex;gap:10px}.pagination{display:flex;align-items:center;justify-content:center;gap:14px;margin:20px 0}.empty{padding:55px 10px;text-align:center;color:#788e9e;border:1px dashed #b9c9d5;border-radius:12px}.two-columns{display:grid;grid-template-columns:1fr 1fr;gap:15px}.color-panel{border:1px solid #dfe5e9;border-radius:12px;padding:21px;background:#fff}.color-panel.dark{background:#20272d;color:#e4eaef;border-color:#20272d}.swatches{display:grid;gap:12px}.swatch{display:flex;gap:11px;align-items:center}.swatch>span{height:35px;width:35px;border-radius:8px;border:1px solid #a9b5bf;flex:none}.swatch code{font-size:11px}.swatch small{display:block;font-size:10px;color:#71828e}.dark .swatch small{color:#9eaebb}.font-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(260px,1fr));gap:12px}.font-card{padding:20px;border:1px solid #dfe5e9;background:#fff;border-radius:12px}.font-example{font-size:24px;line-height:1.4;color:#233d52;min-height:90px;overflow:hidden}.font-card b{font-size:12px}.font-card p{color:#6c8190;font-size:11px;margin:6px 0}.font-card small{display:block;color:#81919e;font-size:10px}.font-card a{display:block;font-size:11px;margin-top:10px}.rive-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(220px,1fr));gap:8px}.rive-item{border:1px solid #dfe5e9;background:#fff;border-radius:9px;padding:11px;display:flex;align-items:center;gap:10px;min-width:0}.rive-item span{color:#577592;font-size:10px;background:#e9eef2;padding:5px;border-radius:5px}.rive-item b{font-size:11px;overflow-wrap:anywhere;min-width:0}.rive-item small{margin-left:auto;white-space:nowrap;font-size:10px;color:#8b98a4}table{border-collapse:collapse;width:100%;background:#fff;font-size:12px}td,th{padding:11px 15px;text-align:left;border-bottom:1px solid #e6ebef}th{font-size:11px;color:#708798;background:#eaf0f4}td code{font-size:11px}.table-wrap{border:1px solid #dfe5e9;border-radius:12px;overflow:auto}.links{display:flex;flex-wrap:wrap;gap:10px 20px;margin:18px 0;font-size:12px}.provenance{padding:19px;border:1px solid #dfe5e9;border-radius:10px;background:#fff;font-size:11px;color:#657b8d;overflow-wrap:anywhere}.provenance p{margin:5px 0}.provenance code{font-size:10px}footer{color:#8a9ba8;font-size:11px;border-top:1px solid #dce5ec;padding-top:18px}noscript{display:block;padding:20px;background:#fff2dc}button[aria-pressed="true"]{background:#e5edf4;border-color:#8ba6bd}@media(max-width:1000px){.layout{grid-template-columns:1fr;gap:12px}aside{position:static;display:flex;flex-wrap:wrap}aside small{display:none}.stats{grid-template-columns:repeat(3,1fr)}}@media(max-width:650px){header{padding:30px 20px}header h1{font-size:25px}.layout{padding:20px 15px}.two-columns{grid-template-columns:1fr}.stats{grid-template-columns:repeat(2,1fr)}.stat:last-child{grid-column:span 2}.grid{grid-template-columns:repeat(2,minmax(0,1fr))}.toolbar select{max-width:100%;flex:1;min-width:0}.toolbar input{width:100%;flex-basis:100%}.font-example{font-size:22px}}
${fontStyle}
</style></head><body>
<header><div class="eyebrow">REA STYLE LIBRARY / PACKAGED RESOURCE STUDY</div><h1>Kimi Android · 资源研究台</h1><p>从当前 APK 提取的图形、字体、动效、本地化与内联卡片声明。用来核对来源、寻找素材、浏览静态证据。</p><div class="badges"><span class="badge">本地可离线打开</span><span class="badge">B · 包内声明</span><span class="badge">D · 浏览器派生预览</span><span class="badge">原件 SHA-256 已逐一核验</span></div></header>
<div class="layout"><aside><small>资源研究目录</small><a href="#overview">01　范围与证据</a><a href="#gallery">02　素材浏览</a><a href="#widget">03　Widget 声明</a><a href="#fonts">04　字体文件</a><a href="#motion">05　Rive 动效</a><a href="#copy">06　本地化资源</a><a href="#source">07　溯源与交接</a></aside><main class="content">
<section id="overview"><h2>先确定这些材料能说明什么</h2><p class="muted">这份页面是研究报告与素材浏览器；不是 Kimi 的真实屏幕，也不是原生参考重建或运行验收。</p><div class="notice">图片直接来自包内原件。简单 Android vector 经受限转换另存 SVG，标为「派生预览」；渐变、裁剪、tint、资源引用、未知属性和二进制 XML 仅提供原件。文件存在、名称、配色或浏览器显示都不能证明它在当前 Android 页面中生效。</div><div class="stats"><div class="stat"><b>${assets.length.toLocaleString('en-US')}</b><small>已保留的原始资产</small></div><div class="stat"><b>${scopeCounts['kimi-compose']}</b><small>Kimi Compose 范围</small></div><div class="stat"><b>${previewCount}</b><small>可见静态预览</small></div><div class="stat"><b>${typeCounts.animation ?? 0}</b><small>Rive 动效原件</small></div><div class="stat"><b>${locales.length}</b><small>本地化资源目录</small></div></div><div class="scope-list">${Object.entries(scopeCounts).map(([scope, count]) => '<span class="scope-chip">' + escaped(scopeNames[scope] ?? scope) + ' · ' + count + '</span>').join('')}</div></section>
<section id="gallery"><h2>素材浏览</h2><p class="muted">默认浏览 Kimi Compose。依赖资源与归属未核资源保留独立范围；图片背景切换只影响研究台预览。</p><div class="toolbar"><input id="search" type="search" placeholder="搜索名称、包内路径或 SHA-256" aria-label="搜索资源"><select id="scope" aria-label="资源范围"><option value="kimi-compose">Kimi Compose</option><option value="">全部范围</option>${Object.entries(scopeNames).filter(([k]) => k !== 'kimi-compose').map(([k, v]) => '<option value="' + escaped(k) + '">' + escaped(v) + '</option>').join('')}</select><select id="type" aria-label="资源类型"><option value="visual">图形素材（图片 / XML）</option><option value="">全部类型</option><option value="vector">可转换的 Vector</option><option value="skipped">未预览的 XML</option>${Object.entries(typeNames).map(([k, v]) => '<option value="' + escaped(k) + '">' + escaped(v) + '</option>').join('')}</select><button id="surface" aria-pressed="false">深色预览底</button></div><div class="gallery-meta"><span id="resultCount" role="status" aria-live="polite"></span><span>每页 60 项 · 图片原色</span></div><noscript>素材筛选需要 JavaScript。所有源文件仍可通过 <a href="assets/manifest.json">资产清单</a> 定位。</noscript><div id="grid" class="grid"></div><div class="pagination"><button id="prev">上一页</button><span id="pageInfo" class="muted"></span><button id="next">下一页</button></div></section>
<section id="widget"><h2>Widget 声明 · 仅限内联内容</h2><p class="muted"><code>widget_foundation.css</code> 文件头声明注入 <code>show_widget</code> 包装 HTML。下列色块是其中直接声明的值；不推定为 Android 原生全局主题、实际计算样式或当前深色模式。</p><div class="two-columns">${colorPanels}</div><div class="links">${cssAsset ? '<a href="' + escaped(cssAsset.path) + '">完整 CSS 原件 ↗</a>' : ''}${widgetTokens ? '<a href="tokens/widget-foundation.json">带源码行号的 Tokens JSON ↗</a>' : ''}</div></section>
<section id="fonts"><h2>字体文件 · ${typeCounts.font} 份</h2><p class="muted">其中 6 份位于 Kimi Compose 字体目录，另 ${typeCounts.font - fonts.length} 份属于内容渲染依赖。下面内嵌原字体做浏览器试排；不能据此确认 Android 的字重分配、逐字回退、字形光栅化或使用页面。</p><div class="font-grid">${fontCards}</div></section>
<section id="motion"><h2>Rive 动效 · ${typeCounts.animation} 份</h2><p class="muted">保留原始 .riv；没有猜测预览帧、触发、时长、状态机输入或中断方式。名称仅用来定位文件。</p><div class="rive-grid">${riveRows}</div></section>
<section id="copy"><h2>本地化资源 · ${locales.length} 个目录</h2><p class="muted">目录限定符只证明包内资源存在，不证明运行时可选语言、选择顺序或回退规则。default 为无语言限定目录；解码 JSON 中保留键、原文、源码行和占位符候选。${copyIndex ? ' 本轮已解码 ' + copyIndex.entryCount.toLocaleString('en-US') + ' 条资源记录。' : ''}</p><div class="table-wrap"><table><thead><tr><th>语言限定符</th><th>包内目录</th><th>记录数</th><th>来源 / 派生</th></tr></thead><tbody>${localeRows}</tbody></table></div></section>
<section id="source"><h2>溯源与交接</h2><p class="muted">此 HTML 内嵌所有可预览图形与 6 份 Compose 字体，可以单文件浏览。原件、JSON 和文档链接需要随研究目录一起保存。</p><div class="links"><a href="assets/manifest.json">原始资产清单 ↗</a><a href="assets/derived/vector-svg/manifest.json">Vector 转换 / 跳过清单 ↗</a><a href="tools/build-gallery.mjs">本页生成脚本 ↗</a>${optionalDocs}</div><div class="provenance"><p><b>源 APK SHA-256</b><br><code>${escaped(manifest.sourceApkSha256)}</code></p><p><b>资产清单 SHA-256</b><br><code>${hash(manifestBytes)}</code></p><p>原图内嵌 ${assets.filter(a => a.previewKind === 'packaged-image').length} 份；简单 Vector 派生 ${derived.length} 份；XML 未预览 ${skipped.length} 份。每份 SVG 均记录源件哈希与派生哈希；原件未修改。</p><p>重建命令：<code>node research/kimi-android/tools/build-gallery.mjs</code>（从工作区根目录运行；仅需 Node.js，无需安装依赖或启动服务）。</p><p>浏览器字体试排、SVG 预览和研究台交互属于独立展示选择 D。包内事实属于 B。原生运行观察 A 请查主研究报告及原始截图记录。</p></div></section>
<footer>Kimi Android 资源研究 · 本页生成于 ${new Date().toISOString()} · 静态材料的适用范围与运行证据分别保留</footer>
</main></div>
<script id="asset-data" type="application/json">${jsonForHtml(assets)}</script>
<script>
'use strict';
const assets = JSON.parse(document.getElementById('asset-data').textContent);
const scopeNames = ${jsonForHtml(scopeNames)};
const typeNames = ${jsonForHtml(typeNames)};
const reasonNames = {'binary-xml-no-browser-preview':'二进制 XML，保留原件','not-an-android-vector':'非 Vector XML，保留原件','gradient-not-converted':'含渐变，未转换','clip-path-not-converted':'含裁剪，未转换','tint-not-converted':'含 tint，未转换','trim-path-not-converted':'含 trim path，未转换','rtl-mirroring-not-converted':'含 RTL 镜像，未转换'};
const refs = Object.fromEntries(['search','scope','type','surface','resultCount','grid','prev','next','pageInfo'].map(id => [id,document.getElementById(id)]));
let currentPage = 0;
const pageSize = 60;
const fmtBytes = n => n < 1024 ? n + ' B' : n < 1048576 ? (n/1024).toFixed(1) + ' KB' : (n/1048576).toFixed(2) + ' MB';
function element(tag, text, className) {const e=document.createElement(tag);if(text!==undefined)e.textContent=text;if(className)e.className=className;return e;}
function link(text, href) {const e=element('a',text);e.href=href;return e;}
function filteredAssets() {
 const search=refs.search.value.trim().toLowerCase(),scope=refs.scope.value,type=refs.type.value;
 return assets.filter(a => (!scope||a.scope===scope) && (!search||[a.sourceEntry,a.path,a.id,a.sha256].some(v=>v.toLowerCase().includes(search))) && (!type||(type==='visual'?['image','xml-resource'].includes(a.type):type==='vector'?a.previewKind==='derived-vector':type==='skipped'?a.type==='xml-resource'&&!a.preview:a.type===type)));
}
function card(a) {
 const article=element('article',undefined,'asset');
 const thumb=element('div',undefined,'thumb');
 if(a.preview){const img=element('img');img.src=a.preview;img.alt=a.sourceEntry.split('/').pop();img.loading='lazy';img.decoding='async';thumb.append(img);}else{thumb.append(element('span',a.type==='animation'?'RIV':a.type==='font'?'FONT':a.type==='xml-resource'?'XML':typeNames[a.type]||a.type,'placeholder'));}
 article.append(thumb);
 const info=element('div',undefined,'asset-info');
 const name=element('span',a.sourceEntry.split('/').pop(),'asset-name');name.title=a.sourceEntry;info.append(name);
 const derived=a.previewKind==='derived-vector';
 const tagText=derived?'D · 派生 SVG 预览':a.previewKind==='packaged-image'?'B · 包内图片':'B · 原件留存';
 info.append(element('span',tagText,'asset-tag '+(derived?'derived':!a.preview?'unavailable':'')));
 info.append(element('small',(scopeNames[a.scope]||a.scope)+' · '+fmtBytes(a.bytes),'asset-sub'));
 const details=element('details'),summary=element('summary','来源与状态');details.append(summary);
 details.append(element('p',a.sourceEntry));
 details.append(element('p','状态：包内保留；实际使用、主题与页面仍需运行记录核对。'));
 if(derived)details.append(element('p','简单 Vector 经受限转换；浏览器预览不验证 Android 原生栅格、密度与显示状态。'));
 if(a.previewReason)details.append(element('p','未预览：'+(reasonNames[a.previewReason]||a.previewReason)));
 const hash=element('p');hash.append(element('code','SHA-256 '+a.sha256));details.append(hash);
 if(a.derivedSha256){const d=element('p');d.append(element('code','派生 SHA-256 '+a.derivedSha256));details.append(d);}
 info.append(details);const links=element('div',undefined,'asset-links');links.append(link('原件 ↗',a.path));if(a.derivedPath)links.append(link('派生 SVG ↗',a.derivedPath));info.append(links);article.append(info);return article;
}
function render(reset=false) {
 if(reset)currentPage=0;
 const matches=filteredAssets(),pageCount=Math.max(1,Math.ceil(matches.length/pageSize));
 currentPage=Math.min(currentPage,pageCount-1);
 refs.grid.replaceChildren();
 const fragment=document.createDocumentFragment();
 for(const a of matches.slice(currentPage*pageSize,(currentPage+1)*pageSize))fragment.append(card(a));
 refs.grid.append(fragment);
 if(!matches.length){const empty=element('div','没有匹配资源，请调整范围或搜索词。','empty');empty.style.gridColumn='1/-1';refs.grid.append(empty);}
 refs.resultCount.textContent='找到 '+matches.length+' 项 / 全部 '+assets.length+' 项';
 refs.pageInfo.textContent=(currentPage+1)+' / '+pageCount;
 refs.prev.disabled=currentPage===0;refs.next.disabled=currentPage>=pageCount-1;
}
refs.search.addEventListener('input',()=>render(true));refs.scope.addEventListener('change',()=>render(true));refs.type.addEventListener('change',()=>render(true));
refs.prev.addEventListener('click',()=>{currentPage--;render();document.getElementById('gallery').scrollIntoView();});
refs.next.addEventListener('click',()=>{currentPage++;render();document.getElementById('gallery').scrollIntoView();});
refs.surface.addEventListener('click',()=>{const active=refs.surface.getAttribute('aria-pressed')!=='true';refs.surface.setAttribute('aria-pressed',String(active));refs.grid.classList.toggle('dark-preview',active);refs.surface.textContent=active?'浅色预览底':'深色预览底';});
render();
</script></body></html>`;

// Parse the generated browser script before writing; independent of any GUI.
const browserScripts = [...html.matchAll(/<script>([\s\S]*?)<\/script>/g)];
for (const [, code] of browserScripts) new vm.Script(code, { filename: 'index.html:inline-script' });
fs.writeFileSync(path.join(root, 'index.html'), html);
const report = { schemaVersion: 1, generatedAt: new Date().toISOString(), sourceApkSha256: manifest.sourceApkSha256, sourceManifestSha256: hash(manifestBytes), output: 'index.html', htmlSha256: hash(html), htmlBytes: Buffer.byteLength(html), sourceAssetsChecked: assets.length, sourceHashesVerified: true, sourceFilesWritten: 0, embeddedImages: assets.filter(a => a.previewKind === 'packaged-image').length, derivedVectors: derived.length, skippedXml: skipped.length, embeddedComposeFonts: fonts.length, riveOriginals: typeCounts.animation, localeResources: locales.length, previewClassification: 'B packaged bytes; D vector conversion and browser presentation; not runtime evidence', guiOrNativeRuntimeVerified: false, skipReasonCounts: Object.fromEntries([...new Set(skipped.map(a => a.reason))].map(reason => [reason, skipped.filter(a => a.reason === reason).length])), optionalDataFound: { copyIndex: Boolean(copyIndex), fontMetadata: Boolean(fontMetadata), widgetTokens: Boolean(widgetTokens) }, browserScriptsSyntaxChecked: browserScripts.length };
writeJson(path.join(root, 'assets/derived/gallery-build-report.json'), report);
console.log(JSON.stringify(report, null, 2));
