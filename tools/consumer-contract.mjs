/** Additional library admission bar for applying a style to an already-working product. */
import fs from 'node:fs';import path from 'node:path';
export function checkConsumerPackage(file){
 const root=path.dirname(path.resolve(file)),read=f=>JSON.parse(fs.readFileSync(path.join(root,f),'utf8').replace(/^\uFEFF/,'')),errors=[];let p;
 try{p=JSON.parse(fs.readFileSync(file,'utf8').replace(/^\uFEFF/,''));}catch(e){return {status:'failed',errors:[e.message]};}
 const check=(ok,msg)=>{if(!ok)errors.push(msg);};
 check(p.readiness?.status!=='draft','A draft/empty template is not an applicable design package.');
 check(p.readiness?.supportedUses?.length>0,'Describe supported target uses.');
 for(const kind of ['tokens','components','layouts','interactions','motion']){
  let items;try{items=read(p.entries[kind]).items;}catch(e){errors.push(kind+': '+e.message);continue;}
  check(Array.isArray(items)&&items.length>0,'Usable package requires nonempty '+kind);if(!Array.isArray(items))continue;
  const core=items.filter(x=>x.priority==='core');check(core.length>0,'No core '+kind+' implementation guidance');
  for(const x of core){check(['specified','adaptation-required'].includes(x.implementation?.status),'Core '+x.id+' is blocked rather than usable in the advertised scope.');check(!!x.implementation?.decision,'Core '+x.id+' needs explicit implementation/adaptation decision.');if(kind==='components'){check(x.slots?.length>0&&x.states?.length>0,'Component '+x.id+' needs real slots/states.');check(!!(x.events?.length||x.referenceContract?.eventsInOrder?.length),'Component '+x.id+' needs user events.');}if(kind==='interactions')check(x.transitions?.length>0,'Interaction '+x.id+' needs state transitions.');}
  if(kind==='motion')check(core.some(x=>Number.isFinite(x.duration?.value)&&x.duration.value>=0&&x.easing?.value!==null&&['chosen','observed','declared'].includes(x.duration.basis)),'Motion needs at least one usable timing/curve, with unknown source values separated from explicit target D alternatives.');
 }
 for(const key of ['spec','reference','comparison','adaptation'])try{check(fs.statSync(path.join(root,p.entries[key])).size>80,'Consumer entry too empty: '+key);}catch(e){errors.push('Missing consumer entry '+key);}
 check(['verified','partial'].includes(p.validation?.reference?.status),'Provide bounded reference/comparison evidence.');
 check(['verified','partial'].includes(p.validation?.crossDomain?.status),'Provide cross-business evidence or explicitly bounded partial adaptation.');
 return {status:errors.length?'failed':'passed-consumer-contract',errors,meaning:'Checks implementation guidance, not original-product completeness or target runtime parity.'};
}
