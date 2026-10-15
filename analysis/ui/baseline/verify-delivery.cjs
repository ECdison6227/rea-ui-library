const fs=require('fs'),path=require('path'),crypto=require('crypto'),assert=require('assert');
const ui=path.resolve(__dirname,'..'),root=path.resolve(ui,'../..');
const read=f=>JSON.parse(fs.readFileSync(path.join(ui,f),'utf8').replace(/^\uFEFF/,''));
const hash=p=>crypto.createHash('sha256').update(fs.readFileSync(p)).digest('hex');
const coverage=read('baseline/coverage.json'),tests=read('baseline/tests/result.json');
assert.equal(tests.failed,0);assert.equal(tests.apkSha256,hash(path.join(ui,'android-reference/build/style-reference.apk')));assert.equal(coverage.referenceTests.apkSha256,tests.apkSha256);
assert.equal(coverage.pages.length,3);assert.equal(coverage.components.length,10);
for(const p of coverage.pages){assert.equal(hash(path.join(ui,p.sourceRuntime.image)),p.sourceRuntime.sha256);assert.equal(read(p.reference.metadata).apkSha256,tests.apkSha256);}
for(const s of Object.values(read('baseline/contracts.json').sources)){
 const p=s.portable?path.resolve(root,s.portable):path.resolve(root,s.path);assert(fs.existsSync(p),'portable source missing '+p);assert.equal(hash(p),s.sha256,'source changed '+p);
}
const asset=read('asset-library/inventory.json');for(const a of asset.entries)assert.equal(hash(path.join(ui,'asset-library',a.file)),a.sha256,'asset changed '+a.file);
for(const a of read('android-reference/assets-manifest.json').entries)assert.equal(hash(path.join(ui,'android-reference',a.file)),a.sha256,'reference asset changed '+a.file);
const report=read('baseline/comparison/final/report.json');
for(const p of report.pages){assert.equal(p.manualReviewInputMatches,true,'final manual review stale');assert(!/stale|pending/.test(p.manualReviewStatus),'final review pending');}
const docs=['README.md','android-reference/README.md','android-reference/java-api.md','baseline/spec.md','baseline/behaviors.md','baseline/popup-navigation.md','baseline/implementation-notes.md','transfer-example/README.md','asset-library/README.md','docs/next-agent-prompt.md','docs/environment.md'];
const missing=[];
for(const f of docs){let text=fs.readFileSync(path.join(ui,f),'utf8');assert(!/\?\?\?/.test(text),'encoding damage '+f);for(const m of text.matchAll(/\]\(([^)]+)\)/g)){let target=m[1].replace(/^<|>$/g,'').split('#')[0];if(!target||/^(https?:|mailto:)/.test(target))continue;const p=path.resolve(path.dirname(path.join(ui,f)),target);if(!fs.existsSync(p))missing.push({f,target});}}
assert.deepStrictEqual(missing,[],'broken handoff links');
for(const f of ['baseline/contracts.json','baseline/popup-navigation.json'])assert(!fs.readFileSync(path.join(ui,f),'utf8').includes('??'),'encoding damage '+f);
const summary={at:new Date().toISOString(),passed:true,sourceApkHashUnchanged:fs.existsSync(path.join(root,'base.apk'))?hash(path.join(root,'base.apk'))==='2a80fc5a87353c2c6ef3c01fa0085862fe45e4ddb9c2d272cb3780841b5889a6':null,portableSourcesAndAssetsHashVerified:true,finalComparisonReviewed:true,referenceTestApkMatchesDeliveredApk:true,missingPrimaryLinks:missing,scope:'Three light guest portrait pages and ten components. Runtime tests verify D implementation only. Full parity remains partial per differences and unknowns.'};
fs.writeFileSync(path.join(__dirname,'verification.json'),JSON.stringify(summary,null,2));console.log(JSON.stringify(summary,null,2));
