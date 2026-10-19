import assert from 'node:assert/strict';
import test from 'node:test';
import {compareVersions,parseRelease,selectUpdate,initialUpdateState,reduceUpdate} from './update-policy.ts';
import {notificationAction,reliabilitySteps} from './permission-policy.ts';
import type {Release} from './update-policy.ts';

const stable:Release={version:'0.0.498',url:'https://updates.example/app.apk',sha256:'a'.repeat(64),size:2048};
const feed={schemaVersion:1,stable:{android:stable},android:{...stable,version:'0.0.499'}};

test('release channel selection does not substitute beta for stable',()=>{
  assert.equal(selectUpdate(feed,'0.0.497','stable')?.version,'0.0.498');
  assert.equal(selectUpdate(feed,'0.0.497','beta')?.version,'0.0.499');
  assert.equal(selectUpdate({android:stable},'0.0.497','stable'),null);
  assert.equal(selectUpdate(feed,'0.0.499'),null);
});
test('incomplete or unsafe release metadata cannot be selected',()=>{
  for(const change of [{url:'http://updates.example/app.apk'},{sha256:'bad'},{size:0},{size:Infinity},{version:''}])
    assert.equal(parseRelease({stable:{android:{...stable,...change}}}),null);
  assert.equal(parseRelease({...feed,schemaVersion:2}),null);
});
test('documents the actual comparison limitation for prereleases',()=>{
  assert.equal(compareVersions('v0.0.498','0.0.497'),1);
  assert.equal(compareVersions('1.0','1.0.0'),0);
  assert.equal(compareVersions('1.0.0-beta.2','1.0.0'),0);
});
test('a background check cannot replace an active download with another release',()=>{
  const state=reduceUpdate(reduceUpdate(initialUpdateState(),{type:'check-found',release:stable,at:1}),{type:'download-start'});
  const next=reduceUpdate(state,{type:'check-found',release:{...stable,version:'0.0.500'},at:2});
  assert.equal(next.phase,'downloading');assert.equal(next.release?.version,stable.version);assert.equal(next.checkedAt,2);
  assert.equal(reduceUpdate(next,{type:'check-none',at:3}).phase,'downloading');
});
test('checksum failure leaves no installable file',()=>{
  const state=reduceUpdate(reduceUpdate(initialUpdateState(),{type:'check-found',release:stable,at:1}),{type:'download-start'});
  const failed=reduceUpdate(state,{type:'download-failed',reason:'checksum'});
  assert.equal(failed.error,'checksum');assert.equal(failed.file,null);
  assert.equal(reduceUpdate(failed,{type:'install-start'}).phase,'error');
});
test('return from installer means ready, without inventing install success',()=>{
  let state=reduceUpdate(initialUpdateState(),{type:'check-found',release:stable,at:1});
  state=reduceUpdate(state,{type:'download-start'});
  state=reduceUpdate(state,{type:'download-done',file:'/cache/updates/app.apk'});
  state=reduceUpdate(state,{type:'install-start'});
  assert.equal(state.phase,'installing');
  state=reduceUpdate(state,{type:'install-dismissed'});
  assert.equal(state.phase,'ready');assert.equal(state.file,'/cache/updates/app.apk');
});
test('denied notification permission routes to settings instead of re-prompting',()=>{
  assert.equal(notificationAction('denied'),'open-settings');assert.equal(notificationAction('prompt'),'request');
});
test('reliability guidance is conditional and does not claim vendor settings granted',()=>{
  const context={keepAliveSupported:true,batteryExempt:false,vendorAutostartAvailable:true,channelSilencingAdvised:false,channelAlreadyNudged:false};
  assert.deepEqual(reliabilitySteps(context),['battery','autostart','channel']);
  assert.deepEqual(reliabilitySteps({...context,keepAliveSupported:false}),[]);
  assert.deepEqual(reliabilitySteps({...context,batteryExempt:null,vendorAutostartAvailable:false}),[]);
  assert.deepEqual(reliabilitySteps({...context,channelAlreadyNudged:true}),['battery','autostart']);
});
