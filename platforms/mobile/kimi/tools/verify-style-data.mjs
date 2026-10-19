/** Corpus-wide source identity and lossless copy roundtrip check; not runtime verification. */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const hash = x => crypto.createHash('sha256').update(x).digest('hex');
const read = file => JSON.parse(fs.readFileSync(path.join(root, file), 'utf8'));
const assert = (condition, message) => { if (!condition) throw new Error(message); };
const copyIndex = read('assets/derived/copy/index.json');
let entries = 0, quantities = 0;
for (const resource of copyIndex.resources) {
  const source = fs.readFileSync(path.join(root, resource.sourcePath));
  const lines = source.toString('utf8').split(/\r?\n/);
  const document = read(resource.output);
  assert(hash(source) === resource.sourceFileSha256, `Source hash mismatch: ${resource.locale}`);
  assert(document.entries.length === lines.slice(1).filter(Boolean).length, `Line coverage mismatch: ${resource.locale}`);
  for (const entry of document.entries) {
    assert(lines[entry.sourceLine - 1] === entry.rawSourceLine, `Source line mismatch: ${entry.key}`);
    assert(hash(entry.rawSourceLine) === entry.sourceLineSha256Utf8, `Line hash mismatch: ${entry.key}`);
    const fields = entry.rawSourceLine.split('|');
    assert(fields[0] === entry.kind && fields[1] === entry.key, `Key mismatch: ${entry.key}`);
    if (entry.kind === 'string') {
      assert(Buffer.from(entry.text, 'utf8').toString('base64') === fields[2], `Copy roundtrip mismatch: ${entry.key}`);
      assert(hash(Buffer.from(entry.text, 'utf8')) === entry.sha256Utf8, `Decoded hash mismatch: ${entry.key}`);
    } else {
      const variants = Object.fromEntries(fields[2].split(',').map(item => { const colon = item.indexOf(':'); return [item.slice(0, colon), item.slice(colon + 1)]; }));
      assert(entry.quantities.length === Object.keys(variants).length, `Plural count mismatch: ${entry.key}`);
      for (const q of entry.quantities) {
        assert(Buffer.from(q.text, 'utf8').toString('base64') === variants[q.quantity], `Plural roundtrip mismatch: ${entry.key}`);
        assert(hash(Buffer.from(q.text, 'utf8')) === q.sha256Utf8, `Decoded plural hash mismatch: ${entry.key}`);
        quantities++;
      }
    }
    entries++;
  }
}
const widget = read('tokens/widget-foundation.json');
const css = fs.readFileSync(path.join(root, widget.sourcePath));
assert(hash(css) === widget.sourceFileSha256, 'CSS hash mismatch');
const cssLines = css.toString('utf8').split(/\r?\n/);
let declarations = 0;
for (const list of Object.values(widget.declarations)) for (const declaration of list) {
  assert(cssLines[declaration.sourceLine - 1] === declaration.rawSourceLine, 'CSS source line mismatch');
  assert(hash(declaration.rawSourceLine) === declaration.sourceLineSha256Utf8, 'CSS line hash mismatch');
  assert(declaration.rawSourceLine.includes(`${declaration.name}: ${declaration.value};`), 'CSS value mismatch');
  declarations++;
}
assert(declarations === cssLines.filter(line => /^\s+(?:--[\w-]+|color-scheme):/.test(line)).length, 'CSS declaration coverage mismatch');
const fontDocument = read('assets/derived/fonts/metadata.json');
let fontNameRecords = 0;
for (const font of fontDocument.fonts) {
  const bytes = fs.readFileSync(path.join(root, font.sourcePath));
  assert(hash(bytes) === font.sourceFileSha256, `Font source hash mismatch: ${font.sourcePath}`);
  assert(font.tables.length === bytes.readUInt16BE(4), 'Font table count mismatch');
  for (let i = 0; i < font.tables.length; i++) {
    const table = font.tables[i], offset = 12 + 16 * i;
    assert(table.tag === bytes.toString('ascii', offset, offset + 4), 'Font table tag mismatch');
    assert(table.offset === bytes.readUInt32BE(offset + 8) && table.length === bytes.readUInt32BE(offset + 12), 'Font table range mismatch');
    assert(table.offset + table.length <= bytes.length, 'Font table out of range');
  }
  const os2 = font.tables.find(t => t.tag === 'OS/2');
  assert(font.os2.weightClass === bytes.readUInt16BE(os2.offset + 4), 'Font weight mismatch');
  assert(font.fvar.present === font.tables.some(t => t.tag === 'fvar'), 'Font fvar presence mismatch');
  for (const record of font.names) {
    const original = bytes.subarray(record.fileOffset, record.fileOffset + record.bytes);
    assert(original.toString('base64') === record.rawBase64, 'Font name record bytes mismatch');
    fontNameRecords++;
  }
}
const result = {
  evidenceId: 'LOCAL-VERIFY-STYLE-001',
  classification: 'B',
  origin: 'current',
  checkedAtUtc: new Date().toISOString(),
  sourceApkSha256: copyIndex.sourceApkSha256,
  command: 'node tools/verify-style-data.mjs',
  result: 'passed',
  verifiedLocales: copyIndex.resources.length,
  verifiedCopyEntries: entries,
  verifiedPluralVariants: quantities,
  verifiedCssDeclarations: declarations,
  verifiedFonts: fontDocument.fonts.length,
  verifiedFontNameRecords: fontNameRecords,
  checks: ['Source file SHA-256 matches every locale, CSS and font', 'Every nonempty CVR data line represented once', 'Every decoded ordinary/plural text roundtrips to original base64', 'Source line identities and stored line/text hashes match', 'Every root custom property and color-scheme declaration accounted for', 'Font table directory and name record raw bytes match source; OS/2 weight and fvar presence match source'],
  limitations: ['This verifies derived data against local original assets, not provenance of the original APK extraction', 'Does not observe the app, typography, fallback, CSS computed styles, locale selection or UI behavior', 'Does not independently validate cmap parser correctness against another font engine or test shaping'],
};
const output = path.join(root, 'evidence/static/style-data-verification.json');
fs.mkdirSync(path.dirname(output), { recursive: true });
fs.writeFileSync(output, JSON.stringify(result, null, 2) + '\n');
console.log(JSON.stringify(result, null, 2));
