import test from 'node:test';import assert from 'node:assert/strict';import fs from 'node:fs';import path from 'node:path';import os from 'node:os';
import {checkConsumerPackage} from '../tools/consumer-contract.mjs';
test('consumer admission rejects report-only and source-unknown motion without target timing',()=>{
 const dir=fs.mkdtempSync(path.join(os.tmpdir(),'rea-consumer-contract-'));try{
 const p={readiness:{status:'partial',supportedUses:['Synthetic test']},entries:{},validation:{reference:{status:'partial'},crossDomain:{status:'partial'}}};
 for(const k of ['spec','reference','comparison','adaptation']){p.entries[k]=k+'.md';fs.writeFileSync(path.join(dir,p.entries[k]),'Synthetic test content. '.repeat(8));}
 const rec={id:'test',priority:'core',implementation:{status:'specified',decision:'Synthetic D behavior'}};
 const items={tokens:[rec],components:[{...rec,slots:['label'],states:[{id:'default'}],events:['press']}],layouts:[rec],interactions:[{...rec,transitions:[{from:'idle',to:'done'}]}],motion:[{...rec,duration:{value:null,basis:'unknown'},easing:{value:null}}]};
 for(const [k,v]of Object.entries(items)){p.entries[k]=k+'.json';fs.writeFileSync(path.join(dir,p.entries[k]),JSON.stringify({items:v}));}const file=path.join(dir,'design-package.json');fs.writeFileSync(file,JSON.stringify(p));
 assert.ok(checkConsumerPackage(file).errors.some(e=>e.includes('Motion needs')));
 items.motion[0].duration={value:180,basis:'chosen'};items.motion[0].easing={value:'linear'};fs.writeFileSync(path.join(dir,p.entries.motion),JSON.stringify({items:items.motion}));assert.equal(checkConsumerPackage(file).status,'passed-consumer-contract');
 fs.writeFileSync(path.join(dir,p.entries.components),JSON.stringify({items:[]}));assert.ok(checkConsumerPackage(file).errors.some(e=>e.includes('nonempty components')));
 }finally{if(!path.resolve(dir).startsWith(path.resolve(os.tmpdir())+path.sep)||!path.basename(dir).startsWith('rea-consumer-contract-'))throw Error('Unsafe cleanup');fs.rmSync(dir,{recursive:true,force:true});}
});
