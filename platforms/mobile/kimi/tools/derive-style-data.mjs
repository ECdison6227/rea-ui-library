/** Read-only analysis of extracted APK assets; outputs only assets/derived and tokens. */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const resourceRoot = path.join(root, 'assets/original/assets/composeResources/kimi.shared.generated.resources');
const sourceApkSha256 = JSON.parse(fs.readFileSync(path.join(root, 'assets/manifest.json'), 'utf8')).sourceApkSha256;
const sha256 = data => crypto.createHash('sha256').update(data).digest('hex');
const relative = file => path.relative(root, file).replaceAll('\\', '/');
function save(file, data) {
  fs.mkdirSync(path.dirname(file), { recursive: true });
  fs.writeFileSync(file, JSON.stringify(data, null, 2) + '\n');
}
function placeholders(text) {
  // Candidates only: printf tokens, doubled-percent escapes, and brace substitutions.
  return [...text.matchAll(/%(?:\d+\$)?[-#+ 0,(<]*\d*(?:\.\d+)?(?:[tT][a-zA-Z]|[a-zA-Z%])|\{\{?[^{}\n]+\}?\}/g)]
    .map(m => ({ token: m[0], offsetUtf16: m.index, kind: m[0] === '%%' ? 'escaped-percent' : m[0][0] === '%' ? 'printf-candidate' : 'brace-candidate' }));
}
const categories = {
  onboarding: /onboard|welcome|guide|introduc|tutorial|first_|tip_|tips|hint|discover|start_|new_user/i,
  empty: /empty|no_result|no_chat|no_file|no_content|not_found|nothing/i,
  permission: /permission|authorize|authorization|allow_|microphone|camera|notification|storage|access/i,
  error: /error|fail|invalid|retry|network|timeout|unavailable|exceed|over_limit/i,
  destructive: /delete|remove|clear_|logout|log_out|deactivat|cancel_account|withdraw|reset/i,
};
const copyIndex = [];
for (const directory of fs.readdirSync(resourceRoot).filter(n => /^values(?:-|$)/.test(n)).sort()) {
  const file = path.join(resourceRoot, directory, 'strings.commonMain.cvr');
  if (!fs.existsSync(file)) continue;
  const data = fs.readFileSync(file);
  const sourceFileSha256 = sha256(data);
  const lines = data.toString('utf8').split(/\r?\n/);
  if (lines[0] !== 'version:0') throw new Error(`Unsupported CVR header in ${file}`);
  const locale = directory === 'values' ? 'default' : directory.slice(7).replace(/-r([A-Z]{2})$/, '-$1');
  const entries = [];
  const keys = new Set();
  for (let i = 1; i < lines.length; i++) {
    if (!lines[i]) continue;
    const fields = lines[i].split('|');
    if (fields.length !== 3 || !['string', 'plurals'].includes(fields[0])) throw new Error(`Unparsed line ${file}:${i + 1}`);
    const [kind, key, encoded] = fields;
    if (keys.has(key)) throw new Error(`Duplicate key ${file}:${i + 1}`);
    keys.add(key);
    const decode = encodedText => {
      if (!/^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$/.test(encodedText)) throw new Error('Invalid base64');
      const bytes = Buffer.from(encodedText, 'base64');
      const text = new TextDecoder('utf-8', { fatal: true }).decode(bytes);
      return { text, placeholders: placeholders(text), sha256Utf8: sha256(bytes) };
    };
    const entry = { kind, key, locale, sourcePath: relative(file), sourceLine: i + 1, rawSourceLine: lines[i], sourceLineSha256Utf8: sha256(lines[i]), sourceFileSha256, classification: 'B', origin: 'current', occurrenceConditions: 'unknown; packaged resource, runtime use and trigger not verified', categoryCandidates: Object.entries(categories).filter(([, re]) => re.test(key)).map(([k]) => k) };
    if (kind === 'string') Object.assign(entry, decode(encoded));
    else entry.quantities = encoded.split(',').map(item => {
      const colon = item.indexOf(':');
      const quantity = item.slice(0, colon);
      if (colon < 1 || !/^(ZERO|ONE|TWO|FEW|MANY|OTHER)$/.test(quantity)) throw new Error(`Bad plural ${key}`);
      return { quantity, ...decode(item.slice(colon + 1)) };
    });
    entries.push(entry);
  }
  const output = `assets/derived/copy/${locale}.json`;
  const summary = { locale, resourceQualifier: directory, sourcePath: relative(file), sourceFileSha256, output, entries: entries.length, strings: entries.filter(e => e.kind === 'string').length, plurals: entries.filter(e => e.kind === 'plurals').length, quantityVariants: entries.reduce((n, e) => n + (e.quantities?.length ?? 0), 0), categoryCandidateCounts: Object.fromEntries(Object.keys(categories).map(k => [k, entries.filter(e => e.categoryCandidates.includes(k)).length])) };
  copyIndex.push(summary);
  save(path.join(root, output), { schemaVersion: 1, evidenceId: 'LOCAL-COPY-001', sourceApkSha256, classification: 'B', origin: 'current', ...summary, languageInterpretation: locale === 'default' ? 'Unqualified resources; sampled text is English. Does not prove locale selection or fallback order.' : 'Resource directory qualifier; does not prove this locale is selectable in the running app.', format: 'Compose CVR version:0, base64 payload decoded as UTF-8', categoryMethod: 'Key-name regex candidates only; curated examples and limitations in spec/content-guidance.md', entries });
}
save(path.join(root, 'assets/derived/copy/index.json'), { schemaVersion: 1, evidenceId: 'LOCAL-COPY-001', sourceApkSha256, classification: 'B', origin: 'current', resources: copyIndex, entryCount: copyIndex.reduce((n, x) => n + x.entries, 0), unknowns: ['Runtime resource selection and fallback', 'Whether each key is reachable, used, or gated', 'Actual display, interpolation, plural selection and rich-text rendering', 'Actual trigger, dismissal, focus and return paths'] });
const curatedKeys = {
  onboarding: ['onboarding_welcome_description', 'onboarding_bn02_occupation_title', 'onboarding_bn04_skip', 'onboarding_bn05_saved_message', 'onboarding_bn07_prefilled_message'],
  empty: ['memory_space_empty', 'project_empty_chat', 'remote_device_add_empty_title', 'remote_device_add_empty_description', 'remote_device_add_empty_refresh', 'task_center_empty_title', 'task_center_empty_desc', 'task_center_empty_via_chat'],
  permission: ['permission_popup_audio_title', 'permission_popup_audio_content', 'permission_popup_camera_content', 'permission_popup_lbs_content', 'open_notification_settings_description'],
  error: ['chat_error_screen_title', 'connection_error_and_retry', 'onboarding_bn06_save_failed', 'profile_name_update_empty', 'account_delete_verify_error_inline'],
  destructive: ['delete_chat_tip', 'queue_management_description_clear_consequence', 'close_will_be_invalid', 'account_delete_confirm_content', 'account_delete_confirm_desc', 'account_delete_pending_dialog_keep'],
};
const exampleLocales = ['zh', 'default'];
const exampleEntries = Object.fromEntries(exampleLocales.map(locale => [locale, JSON.parse(fs.readFileSync(path.join(root, `assets/derived/copy/${locale}.json`), 'utf8')).entries]));
save(path.join(root, 'assets/derived/copy/examples.zh-en.json'), { schemaVersion: 1, evidenceId: 'LOCAL-COPY-001', sourceApkSha256, classification: 'B', selectionClassification: 'C', origin: 'current', method: 'Manually selected semantic examples from decoded keys; categories are research labels, not observed UI grouping', occurrenceConditions: 'unknown for every example; no runtime trigger or state transition is established', examples: Object.entries(curatedKeys).flatMap(([category, keys]) => keys.map(key => ({ category, key, locales: Object.fromEntries(exampleLocales.map(locale => { const entry = exampleEntries[locale].find(e => e.key === key); if (!entry) throw new Error(`Missing curated key ${locale}:${key}`); return [locale, entry]; })) }))) });

const cssFile = path.join(resourceRoot, 'files/widget_foundation.css');
const cssBytes = fs.readFileSync(cssFile);
const css = cssBytes.toString('utf8');
const cssLines = css.split(/\r?\n/);
const cssSource = { evidenceId: 'LOCAL-WIDGET-001', sourcePath: relative(cssFile), sourceFileSha256: sha256(cssBytes), sourceApkSha256, classification: 'B', origin: 'current', scope: 'show_widget inline content', occurrenceConditions: 'CSS header declares injection in show_widget wrapper HTML; actual execution not verified' };
const themeDeclarations = {};
for (const [theme, selector] of [['light', ':root'], ['dark', ':root.dark']]) {
  const re = new RegExp(selector.replace(/[.*+?^${}()|[\]\\]/g, '\\$&') + '\\s*\\{([^{}]*)\\}', 'g');
  const match = re.exec(css);
  if (!match) throw new Error(`Missing ${selector}`);
  const bodyStart = match.index + match[0].indexOf('{') + 1;
  const declarations = [];
  for (const m of match[1].matchAll(/(^|\n)[ \t]*([\w-]+)\s*:\s*([^;]+);/g)) {
    const offset = bodyStart + m.index + m[0].indexOf(m[2]);
    const line = css.slice(0, offset).split('\n').length;
    declarations.push({ selector, theme, name: m[2], value: m[3].trim(), sourceLine: line, rawSourceLine: cssLines[line - 1], sourceLineSha256Utf8: sha256(cssLines[line - 1]), references: [...m[3].matchAll(/var\((--[\w-]+)/g)].map(x => x[1]), ...cssSource });
  }
  themeDeclarations[theme] = declarations;
}
save(path.join(root, 'tokens/widget-foundation.json'), { schemaVersion: 1, ...cssSource, unitPolicy: 'Original CSS px/ms/rgba and font stacks retained; no native dp/sp conversion', interpretation: 'Packaged widget declaration only. Does not establish global native Android theme, effective computed style, selected fonts, animations or rendered states.', inheritance: 'CSS :root.dark overrides the same :root element. Unchanged :root aliases remain and resolve through var() according to the CSS cascade; not a runtime measurement.', declarations: themeDeclarations, declarationCounts: Object.fromEntries(Object.entries(themeDeclarations).map(([k, v]) => [k, v.length])), unknowns: ['Whether widget runtime loads these bytes', 'Actual dark-class toggle', 'Effective app-level styles and overrides', 'Supported renderer and viewport', 'Font files selected for any glyph', 'Which motion tokens are consumed'] });

function u16(b, o) { return b.readUInt16BE(o); }
function i16(b, o) { return b.readInt16BE(o); }
function u32(b, o) { return b.readUInt32BE(o); }
function fixed(b, o) { return b.readInt32BE(o) / 65536; }
function fontMetadata(file) {
  const b = fs.readFileSync(file);
  const tableCount = u16(b, 4);
  const tables = Array.from({ length: tableCount }, (_, i) => { const o = 12 + 16 * i; return { tag: b.toString('ascii', o, o + 4), checksum: u32(b, o + 4).toString(16).padStart(8, '0'), offset: u32(b, o + 8), length: u32(b, o + 12) }; });
  for (const t of tables) if (t.offset + t.length > b.length) throw new Error(`Bad font table ${file}`);
  const table = name => tables.find(t => t.tag === name);
  const n = table('name');
  const names = [];
  if (n) {
    const count = u16(b, n.offset + 2), storage = n.offset + u16(b, n.offset + 4);
    for (let i = 0; i < count; i++) {
      const o = n.offset + 6 + i * 12;
      const platformId = u16(b, o), encodingId = u16(b, o + 2), languageId = u16(b, o + 4), nameId = u16(b, o + 6), length = u16(b, o + 8), offset = storage + u16(b, o + 10);
      const raw = b.subarray(offset, offset + length);
      // Unicode / Windows UTF-16BE; Macintosh records are not guessed as UTF-8.
      const utf16 = platformId === 0 || platformId === 3;
      const text = utf16 ? Buffer.from(raw).swap16().toString('utf16le') : platformId === 1 && encodingId === 0 && raw.every(x => x < 128) ? raw.toString('ascii') : null;
      names.push({ nameId, platformId, encodingId, languageId, fileOffset: offset, bytes: length, text, rawBase64: raw.toString('base64'), decoding: utf16 ? 'UTF-16BE' : text !== null ? 'ASCII subset of MacRoman' : 'unresolved' });
    }
  }
  const nameText = id => names.find(n => n.nameId === id && n.platformId === 3 && n.languageId === 1033)?.text ?? names.find(n => n.nameId === id && n.text !== null)?.text ?? null;
  const fvar = table('fvar');
  const axes = [];
  const instances = [];
  if (fvar) {
    const o = fvar.offset, axesOffset = o + u16(b, o + 4), axisCount = u16(b, o + 8), axisSize = u16(b, o + 10), instanceCount = u16(b, o + 12), instanceSize = u16(b, o + 14);
    for (let i = 0; i < axisCount; i++) {
      const a = axesOffset + i * axisSize, nameId = u16(b, a + 18);
      axes.push({ tag: b.toString('ascii', a, a + 4), min: fixed(b, a + 4), default: fixed(b, a + 8), max: fixed(b, a + 12), flags: u16(b, a + 16), nameId, name: nameText(nameId) });
    }
    for (let i = 0; i < instanceCount; i++) {
      const a = axesOffset + axisCount * axisSize + i * instanceSize;
      instances.push({ nameId: u16(b, a), name: nameText(u16(b, a)), flags: u16(b, a + 2), coordinates: Object.fromEntries(axes.map((axis, j) => [axis.tag, fixed(b, a + 4 + j * 4)])) });
    }
  }
  const c = table('cmap');
  const cmaps = [];
  const unicodePoints = new Set();
  if (c) {
    for (let i = 0; i < u16(b, c.offset + 2); i++) {
      const r = c.offset + 4 + i * 8, platformId = u16(b, r), encodingId = u16(b, r + 2), offset = c.offset + u32(b, r + 4), format = u16(b, offset);
      const isUnicode = platformId === 0 || (platformId === 3 && [1, 10].includes(encodingId));
      const points = new Set();
      const add = (cp, glyph) => { if (glyph !== 0 && cp <= 0x10ffff && !(cp >= 0xd800 && cp <= 0xdfff)) points.add(cp); };
      let parsed = true;
      if (format === 0) for (let cp = 0; cp < 256; cp++) add(cp, b[offset + 6 + cp]);
      else if (format === 4) {
        const segments = u16(b, offset + 6) / 2, endBase = offset + 14, startBase = endBase + 2 * segments + 2, deltaBase = startBase + 2 * segments, rangeBase = deltaBase + 2 * segments;
        for (let j = 0; j < segments; j++) {
          const end = u16(b, endBase + 2 * j), start = u16(b, startBase + 2 * j), delta = i16(b, deltaBase + 2 * j), range = u16(b, rangeBase + 2 * j);
          for (let cp = start; cp <= end && cp !== 0xffff; cp++) {
            let glyph = range === 0 ? (cp + delta) & 0xffff : u16(b, rangeBase + 2 * j + range + 2 * (cp - start));
            if (range && glyph) glyph = (glyph + delta) & 0xffff;
            add(cp, glyph);
          }
        }
      } else if (format === 6 || format === 10) {
        const first = format === 6 ? u16(b, offset + 6) : u32(b, offset + 12), count = format === 6 ? u16(b, offset + 8) : u32(b, offset + 16), base = offset + (format === 6 ? 10 : 20);
        for (let j = 0; j < count; j++) add(first + j, u16(b, base + 2 * j));
      } else if (format === 12 || format === 13) {
        const groups = u32(b, offset + 12);
        for (let j = 0; j < groups; j++) {
          const o = offset + 16 + j * 12, start = u32(b, o), end = u32(b, o + 4), firstGlyph = u32(b, o + 8);
          for (let cp = start; cp <= end; cp++) add(cp, firstGlyph + (format === 12 ? cp - start : 0));
        }
      } else parsed = false;
      const sorted = [...points].sort((a, b) => a - b);
      const ranges = [];
      for (const cp of sorted) {
        const last = ranges.at(-1);
        if (last && cp === last[1] + 1) last[1] = cp;
        else ranges.push([cp, cp]);
      }
      if (isUnicode && parsed) for (const cp of points) unicodePoints.add(cp);
      cmaps.push({ platformId, encodingId, format, fileOffset: offset, isUnicode, parsed, mappedNonzeroGlyphCodepointCount: parsed ? sorted.length : null, ranges: parsed ? ranges.map(([start, end]) => ({ start: `U+${start.toString(16).toUpperCase().padStart(4, '0')}`, end: `U+${end.toString(16).toUpperCase().padStart(4, '0')}` })) : null });
    }
  }
  const os2 = table('OS/2'), post = table('post'), maxp = table('maxp');
  const samples = ['A', 'a', '0', '中', '文', '你', '好', '，', '。', 'あ', '한', '😀', '∑'];
  return { evidenceId: 'LOCAL-FONT-001', classification: 'B', origin: 'current', sourcePath: relative(file), sourceFileSha256: sha256(b), sourceApkSha256, bytes: b.length, sfntVersionHex: b.subarray(0, 4).toString('hex'), tables, family: nameText(1), subfamily: nameText(2), fullName: nameText(4), postscriptName: nameText(6), typographicFamily: nameText(16), typographicSubfamily: nameText(17), names, os2: os2 ? { version: u16(b, os2.offset), weightClass: u16(b, os2.offset + 4), widthClass: u16(b, os2.offset + 6), fsType: u16(b, os2.offset + 8), fsSelection: os2.length >= 64 ? u16(b, os2.offset + 62) : null } : null, post: post ? { version: fixed(b, post.offset), italicAngle: fixed(b, post.offset + 4), isFixedPitch: u32(b, post.offset + 12) } : null, glyphCount: maxp ? u16(b, maxp.offset + 4) : null, fvar: { present: Boolean(fvar), axes, instances }, cmap: { records: cmaps, unicodeUnionMappedNonzeroGlyphCodepointCount: unicodePoints.size, sampleCoverage: samples.map(character => ({ character, codepoint: `U+${character.codePointAt(0).toString(16).toUpperCase()}`, inParsedUnicodeCmap: unicodePoints.has(character.codePointAt(0)) })) }, runtimeFamilySelection: 'unknown', perGlyphFallback: 'unknown; cmap is packaged coverage, not runtime font selection or shaping', occurrenceConditions: 'Packaged font; screen, weight assignment and runtime use not verified' };
}
const fonts = fs.readdirSync(path.join(resourceRoot, 'font')).filter(x => /\.(ttf|otf)$/i.test(x)).sort().map(name => fontMetadata(path.join(resourceRoot, 'font', name)));
save(path.join(root, 'assets/derived/fonts/metadata.json'), { schemaVersion: 1, evidenceId: 'LOCAL-FONT-001', classification: 'B', origin: 'current', sourceApkSha256, scope: 'Six Compose resource fonts only; excludes jlatexmath dependency fonts', method: 'Built-in Node Buffer SFNT name, cmap, fvar, OS/2, post, maxp parser; no font installed or application executed', limitations: ['cmap formats 0/4/6/10/12/13 parsed; unsupported records explicitly listed', 'cmap coverage does not establish shaping, variation-selector support, locale behavior or font fallback', 'Names are declarations; native UI assignments, CSS selection, and glyph rasterization are unverified', 'No font license interpretation; retained name records are original metadata'], fonts });
console.log(JSON.stringify({ copyLocales: copyIndex.length, copyEntries: copyIndex.reduce((n, x) => n + x.entries, 0), cssDeclarations: Object.fromEntries(Object.entries(themeDeclarations).map(([k, v]) => [k, v.length])), fonts: fonts.map(f => ({ file: path.basename(f.sourcePath), family: f.family, subfamily: f.subfamily, weight: f.os2?.weightClass, fvar: f.fvar.present, unicodeCodepoints: f.cmap.unicodeUnionMappedNonzeroGlyphCodepointCount })) }, null, 2));
