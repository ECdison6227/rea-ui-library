#!/usr/bin/env node
'use strict';
// Compare the independent Node AXML decoder against the recorded REA/JADX
// package evidence. This does not re-run either producer or execute the APK.
const fs = require('node:fs');
const path = require('node:path');
const assert = require('node:assert/strict');
const root = path.resolve(__dirname, '..');
const read = p => JSON.parse(fs.readFileSync(path.join(root, p), 'utf8').replace(/^\uFEFF/, ''));
const local = read('evidence/source/manifest-identity.json');
const evidence = read('evidence/rea/android-package.json');
const rea = evidence.normalized_result;
assert.equal(local.manifest.package, rea.package_name);
assert.equal(local.manifest['android:versionName'], rea.version_name);
assert.equal(String(local.manifest['android:versionCode']), rea.version_code);
assert.equal(String(local.sdk[0]['android:minSdkVersion']), rea.min_sdk);
assert.equal(String(local.sdk[0]['android:targetSdkVersion']), rea.target_sdk);
assert.deepEqual(local.permissions.map(p => p['android:name']).sort(), [...rea.permissions].sort());
assert.ok(rea.manifest.text.includes(local.launchers[0].attributes['android:name']));
const result = {
  checks: ['package', 'versionName', 'versionCode', 'minSdk', 'targetSdk', 'complete uses-permission name set', 'launcher name in decoded REA manifest'],
  status: 'all passed',
  reaEvidenceId: evidence.evidence_id
};
fs.writeFileSync(path.join(root, 'evidence/source/metadata-crosscheck.json'), JSON.stringify(result, null, 2) + '\n');
process.stdout.write(JSON.stringify(result) + '\n');
