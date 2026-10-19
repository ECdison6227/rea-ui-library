import test from 'node:test';
import assert from 'node:assert/strict';
import {checkSkillSync} from '../tools/check-skill-sync.mjs';
test('offline skill vendor matches the exact upstream committed bytes and inventory',()=>{const r=checkSkillSync();assert.equal(r.status,'passed');assert(r.files>0);assert.equal(r.networkAccessed,false);});
