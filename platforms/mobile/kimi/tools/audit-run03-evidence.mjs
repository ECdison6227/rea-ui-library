/** Recheck the explicit, pixel-reviewed run03 allowlist without device operations.
 * Run: node research/kimi-android/tools/audit-run03-evidence.mjs [--check]
 * New or changed scenes always remain excluded until a reviewer inspects their
 * pixels, UIA and capture, then updates docs/run03-evidence-audit.json.
 * --check makes no writes. Any unreviewed/inconsistent scene exits with code 1.
 */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const runId = 'run03-20261016';
const runPath = `evidence/runtime/${runId}`;
const auditPath = 'docs/run03-evidence-audit.json';
const outputPath = 'docs/run03-safe-scenes.json';
const forcedWithheld = new Set(['r03-05-effort-relaunch-private']);
const hash = bytes => crypto.createHash('sha256').update(bytes).digest('hex');
const readJson = file => JSON.parse(fs.readFileSync(path.join(root, file), 'utf8').replace(/^\uFEFF/, ''));
const insideRoot = relative => {
  const absolute = path.resolve(root, relative);
  if (!absolute.startsWith(root + path.sep)) throw Error('Source escapes research root');
  return absolute;
};
const audit = readJson(auditPath);
if (audit.runId !== runId || audit.schemaVersion !== 1) throw Error('Wrong audit identity');
const sessionBytes = fs.readFileSync(path.join(root, runPath, 'session.json'));
const session = JSON.parse(sessionBytes.toString('utf8').replace(/^\uFEFF/, ''));
const notes = new Map(audit.scenes.map(scene => [scene.id, scene]));
if (notes.size !== audit.scenes.length) throw Error('Duplicate review IDs');
const sessionScenes = new Map(session.scenes.map(scene => [scene.id, scene]));
const directories = fs.readdirSync(path.join(root, runPath, 'scenes'), {withFileTypes:true})
  .filter(entry => entry.isDirectory()).map(entry => entry.name);
const ids = [...new Set([...directories, ...sessionScenes.keys()])]
  .sort((a,b) => a.localeCompare(b, 'en', {numeric:true}));
const allowed = [], withheld = [], pending = [], integrityFailures = [];

for (const id of ids) {
  const note = notes.get(id);
  const capturePath = `${runPath}/scenes/${id}/capture.json`;
  const scene = sessionScenes.get(id);
  if (forcedWithheld.has(id) || /private/i.test(id) || note?.distribution === 'withhold') {
    withheld.push({id, reason:note?.privacy?.reason ?? 'Private scene: distribution prohibited', sourceFilesExcluded:true});
    continue;
  }
  if (!note) { pending.push({id, reason:'No explicit pixel review; excluded by default'}); continue; }
  if (note.pixelReview !== 'reviewed' || note.uiaReview !== 'reviewed' || note.captureReview !== 'reviewed' ||
      note.privacy?.status !== 'clear' || note.distribution !== 'allow') {
    pending.push({id, reason:'Pixel/UIA/capture/privacy review is incomplete; excluded by default'}); continue;
  }
  if (!scene || !fs.existsSync(insideRoot(capturePath))) {
    integrityFailures.push({id, reason:'Missing scene registration or capture'}); continue;
  }
  const captureBytes = fs.readFileSync(insideRoot(capturePath));
  const capture = JSON.parse(captureBytes.toString('utf8').replace(/^\uFEFF/, ''));
  // Treat explicit privacy designations as a stop, not a generic word match:
  // "no existing private files selected" describes absence and is not a flag.
  // Exact pixel/UIA/capture/privacy review is still required for every allow.
  if (/^\s*PRIVATE\b|\bexclude distribution\b|含身份|私人(?:主页|抽屉|历史)/i.test(capture.description ?? '')) {
    integrityFailures.push({id, reason:'Capture marks private content; cannot allow automatically'}); continue;
  }
  const checks = [
    ['screenshot', scene.screenshot, capture.screenshot, note.exactScreenshotSha256],
    ['hierarchy', scene.hierarchy, capture.hierarchy, note.exactHierarchySha256],
  ];
  const errors = [];
  for (const [kind, sessionPart, capturePart, expected] of checks) {
    if (!sessionPart?.path || !capturePart?.path || sessionPart.path !== capturePart.path || !expected) {
      errors.push(`${kind} is missing or changed`); continue;
    }
    const actual = hash(fs.readFileSync(insideRoot(sessionPart.path)));
    if (actual !== expected || actual !== sessionPart.sha256 || actual !== capturePart.sha256) errors.push(`${kind} hash differs from reviewed bytes`);
  }
  if (hash(captureBytes) !== note.exactCaptureSha256) errors.push('capture hash differs from reviewed bytes');
  if (capture.id !== id || capture.runId !== runId || capture.atomicCapture !== false) errors.push('capture identity or declared non-atomic method differs');
  if (errors.length) { integrityFailures.push({id, reason:errors.join('; ')}); continue; }
  allowed.push({id, screenshot:scene.screenshot, hierarchy:scene.hierarchy,
    capture:{path:capturePath, sha256:note.exactCaptureSha256},
    pixelSummary:note.pixelSummary, screenshotAndUiaAtomic:false,
    privacyReview:'clear; reviewed exact image, hierarchy and capture bytes'});
}

for (const id of notes.keys()) if (!ids.includes(id)) integrityFailures.push({id, reason:'Reviewed scene no longer exists'});
const blocked = pending.length > 0 || integrityFailures.length > 0;
const result = {
  schemaVersion:1, runId, generatedAt:new Date().toISOString(),
  auditSource:auditPath, auditSourceSha256:hash(fs.readFileSync(path.join(root, auditPath))),
  sessionSnapshotSha256:hash(sessionBytes),
  policy:'Only explicitly reviewed exact bytes may enter a preview. New, changed, private or incomplete scenes are excluded. PNG and UIA are non-atomic observations.',
  defaultPreviewGate:blocked ? 'blocked-pending-review' : 'reviewed-scenes-only',
  defaultPreviewAllowed:!blocked,
  safeSceneIds:allowed.map(scene => scene.id), scenes:allowed, withheld, pending, integrityFailures,
  counts:{discovered:ids.length, safe:allowed.length, withheld:withheld.length, pending:pending.length, integrityFailures:integrityFailures.length},
};
if (!process.argv.includes('--check')) fs.writeFileSync(path.join(root, outputPath), JSON.stringify(result, null, 2) + '\n');
console.log(JSON.stringify({runId, output:process.argv.includes('--check') ? 'check-only' : outputPath,
  defaultPreviewGate:result.defaultPreviewGate, counts:result.counts,
  safeSceneIds:result.safeSceneIds, withheld:withheld.map(scene => scene.id), pending, integrityFailures}, null, 2));
if (blocked) process.exitCode = 1;
