/** Compare retained runtime scene 03 with retained CVR text. Never controls a device. */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const sceneDirectory = 'evidence/runtime/scenes/03-kimi-privacy';
const readJson = file => JSON.parse(fs.readFileSync(path.join(root, file), 'utf8'));
const hash = bytes => crypto.createHash('sha256').update(bytes).digest('hex');
const capture = readJson(`${sceneDirectory}/capture.json`);
const screenshotBytes = fs.readFileSync(path.join(root, capture.screenshot.path));
const hierarchyBytes = fs.readFileSync(path.join(root, capture.hierarchy.path));
if (hash(screenshotBytes) !== capture.screenshot.sha256 || hash(hierarchyBytes) !== capture.hierarchy.sha256) throw new Error('Runtime source identity mismatch');
const xmlDecode = value => value.replace(/&(?:amp|lt|gt|quot|apos|#\d+|#x[0-9A-Fa-f]+);/g, token => {
  const named = { '&amp;': '&', '&lt;': '<', '&gt;': '>', '&quot;': '"', '&apos;': "'" };
  if (named[token]) return named[token];
  const hex = token.startsWith('&#x');
  return String.fromCodePoint(Number.parseInt(token.slice(hex ? 3 : 2, -1), hex ? 16 : 10));
});
const nodes = [...hierarchyBytes.toString('utf8').matchAll(/<node\b[^>]*>/g)].map((m, ordinal) => ({ ordinal, ...Object.fromEntries([...m[0].matchAll(/([\w:-]+)="([^"]*)"/g)].map(a => [a[1], xmlDecode(a[2])])) }));
const textNodes = nodes.filter(n => n.text).map(n => ({ sourceNodeOrdinal: n.ordinal, text: n.text, class: n.class, package: n.package, resourceId: n['resource-id'], bounds: n.bounds, clickableOnTextNode: n.clickable, enabled: n.enabled, classification: 'A', sourcePath: capture.hierarchy.path, sourceFileSha256: capture.hierarchy.sha256 }));
const resources = readJson('assets/derived/copy/index.json').resources.map(r => ({ ...r, data: readJson(r.output) }));
const defaultEntries = resources.find(r => r.locale === 'default').data.entries;
const get = key => { const entry = defaultEntries.find(e => e.key === key); if (!entry) throw new Error(`Missing key ${key}`); return entry; };
const publicEntry = entry => ({ locale: entry.locale, key: entry.key, text: entry.text, sourcePath: entry.sourcePath, sourceLine: entry.sourceLine, rawSourceLine: entry.rawSourceLine, sourceFileSha256: entry.sourceFileSha256, sourceLineSha256Utf8: entry.sourceLineSha256Utf8, decodedTextSha256Utf8: entry.sha256Utf8, classification: 'B', leadingWhitespace: /^\s/.test(entry.text), trailingWhitespace: /\s$/.test(entry.text), bindingAtRuntime: 'unknown; text correspondence alone is not code/resource binding evidence' });
const body = textNodes.find(n => n.text.startsWith('Terms of Service'));
if (!body) throw new Error('Expected body text absent');
const fragmentKeys = ['user_service', 'and', 'privacy_policy', 'privacy_page_content_2'];
const fragments = fragmentKeys.map(get);
const joinedWithoutSeparator = fragments.map(e => e.text).join('');
const joinedWithSpaces = fragments.map(e => e.text).join(' ');
const boundaries = [];
let offset = 0;
for (let i = 0; i < fragments.length - 1; i++) {
  offset += fragments[i].text.length;
  const left = joinedWithoutSeparator[offset - 1], right = joinedWithoutSeparator[offset];
  boundaries.push({ leftKey: fragments[i].key, rightKey: fragments[i + 1].key, offsetUtf16: offset, leftCharacter: left, rightCharacter: right, separatorInRawFragments: '', classifications: { originalFragmentEdges: 'B', analyticalJoin: 'C' }, observation: 'Original component strings have no edge whitespace; separator-free analytical join matches this UIA boundary. Runtime construction not traced.' });
}
const labels = ['Welcome to Kimi', 'Terms of Service', 'and', 'Privacy Policy', 'Disagree', 'Agree'];
const matches = labels.map(text => {
  const candidates = resources.flatMap(r => r.data.entries.filter(e => e.kind === 'string' && e.text === text).map(publicEntry));
  const isBodyFragment = ['Terms of Service', 'and', 'Privacy Policy'].includes(text);
  return { text, runtimeRepresentation: isBodyFragment ? 'substring of one merged body TextView; no separately exposed link node' : 'exact text node', runtimeNodes: textNodes.filter(n => isBodyFragment ? n === body : n.text === text), comparison: 'exact text equality or exact substring within the captured body', candidateCountWithinSearchedCvrCorpus: candidates.length, candidates, resourceBindingStatus: candidates.length === 1 ? 'unique text candidate within the 17 decoded CVR files; runtime binding still unverified' : 'ambiguous text candidates; no unique resource-key attribution' };
});
const result = {
  schemaVersion: 1,
  evidenceId: 'LOCAL-RUNTIME-COPY-001',
  origin: 'current',
  checkedAtUtc: new Date().toISOString(),
  method: 'Read-only comparison of retained screenshot/UIA/capture.json against 17 decoded CVR files; no runtime action and no REA Evidence ID',
  command: 'node tools/crosscheck-runtime-copy.mjs',
  sourceApkSha256: capture.apkSha256,
  searchBoundary: '17 Compose strings.commonMain.cvr files only. Exact ordinary string equality for label candidates; selected fragment concatenation for the body. Does not search resources.arsc, code binding, server text or dependency text.',
  scenes: [{
    id: capture.id,
    capturePath: `${sceneDirectory}/capture.json`,
    package: capture.package,
    serial: capture.serial,
    avd: capture.avd,
    capturedAt: capture.capturedAt,
    conditions: capture.conditions,
    screenshot: capture.screenshot,
    hierarchy: capture.hierarchy,
    visualInspection: { classification: 'A', method: 'Agent inspected original retained screen.png with view_image; original image unchanged', observations: ['Image is nonblank and shows Kimi privacy consent dialog', 'Title Welcome to Kimi and buttons Disagree / Agree are visible', 'Terms of Service and Privacy Policy are visually blue fragments in a body paragraph', 'Text at ServiceandPrivacy and PolicyUnderstand has no visible inserted word spacing, consistent with UIA text'], limits: ['No new precise color/font/spacing measurements made', 'Link click targets and actions not tested in this crosscheck'] },
    textNodes,
    matches,
    bodyComparison: {
      runtimeBody: body,
      fragments: fragments.map(publicEntry),
      joinedWithoutSeparator,
      exactlyMatchesRuntimeUia: joinedWithoutSeparator === body.text,
      joinedWithSpaces,
      spaceJoinMatchesRuntimeUia: joinedWithSpaces === body.text,
      boundaries,
      alternatePackagedCombinedLabel: publicEntry(get('user_service_and_privacy_agreement')),
      combinedLabelExactPrefixOfRuntimeBody: body.text.startsWith(get('user_service_and_privacy_agreement').text),
      classifications: { observedRuntimeText: 'A', packagedOriginalFragments: 'B', interpretationOfJoining: 'C' },
      conclusion: 'The observed body can be reproduced exactly by separator-free joining of these four unqualified resource texts. The originals do not contain edge whitespace. This is correspondence, not proof of the runtime concatenation code, locale selection or root cause.',
    },
    counterexamplesToOvergeneralization: [
      { key: 'onboarding_welcome_description', record: publicEntry(get('onboarding_welcome_description')), note: 'Contains Welcome to Kimi but is not the observed exact title; keep onboarding description separate from this consent title' },
      { keys: ['agree', 'agree_and_continue'], note: 'Both default resources decode to Agree. UIA lacks a resource-id on the label; text alone cannot distinguish them. Their zh texts differ, but that does not prove which key this English screen used.' },
      { key: 'user_service_and_privacy_agreement', note: 'A combined label with spaces exists in the package, but it is not an exact prefix of this captured merged body. This does not establish whether any code path references it.' },
    ],
    unknowns: ['Actual resource key references and composition code', 'Application locale selection and fallback order; captured systemLocales value is the string null', 'Binding of the visible Agree label to agree or agree_and_continue', 'Actual link URL, click targets and navigation', 'Agree/Disagree effects, dismissal, Back handling, focus/reading order, persistence and subsequent launches', 'Other platforms, builds, locales, themes and font scales'],
  }],
};
const output = path.join(root, 'evidence/static/runtime-copy-crosscheck.json');
fs.mkdirSync(path.dirname(output), { recursive: true });
fs.writeFileSync(output, JSON.stringify(result, null, 2) + '\n');
console.log(JSON.stringify({ output: 'evidence/static/runtime-copy-crosscheck.json', scene: capture.id, verifiedRuntimeSourceHashes: true, bodyExactlyMatchesSeparatorFreeJoin: joinedWithoutSeparator === body.text, matches: matches.map(m => ({ text: m.text, candidates: m.candidates.map(c => `${c.locale}:${c.key}:${c.sourceLine}`) })) }, null, 2));
