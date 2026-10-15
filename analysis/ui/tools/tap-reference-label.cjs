const fs=require('fs'),path=require('path'),cp=require('child_process');
const [state,event,label]=process.argv.slice(2);if(!label)throw Error('state-id event-id label required');
const x=JSON.parse(fs.readFileSync(path.resolve('analysis/ui/runtime/hierarchy',state+'.json'),'utf8'));
const matches=x.nodes.filter(n=>n.package==='study.style.reference'&&(n.text===label||n['content-desc']===label));
const n=matches.find(n=>n.clickable==='true')||matches[0];if(!n)throw Error('Missing reference label: '+label);
const b=n.bounds.match(/\d+/g).map(Number);if(b[2]<=b[0]||b[3]<=b[1])throw Error('Offscreen bounds');
const args=['analysis/ui/tools/adb-event.cjs',event,'shell','input','tap',String(Math.round((b[0]+b[2])/2)),String(Math.round((b[1]+b[3])/2))];
process.stdout.write(cp.execFileSync(process.execPath,args,{encoding:'utf8',windowsHide:true}));
