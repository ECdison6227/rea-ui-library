import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const workspace=path.resolve(root,'../..');
const read=p=>JSON.parse(fs.readFileSync(path.join(root,p),'utf8').replace(/^\uFEFF/,''));
const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
const backlog=read('docs/remaining-validation.json');
const checkpoint=read('docs/continuation-checkpoint.json');
if(backlog.taskCount!==backlog.tasks.length||new Set(backlog.tasks.map(t=>t.id)).size!==backlog.tasks.length)throw Error('Task count/ID mismatch');
for(const t of backlog.tasks){if(!Object.hasOwn(backlog.statusDefinitions,t.status)||!t.acceptance.length||!t.prerequisites.length||!Array.isArray(t.evidence))throw Error('Incomplete task '+t.id);}
for(const x of checkpoint.snapshot)if(hash(fs.readFileSync(path.join(root,x.path)))!==x.sha256)throw Error('Changed checkpoint file '+x.path);
for(const d of backlog.historicalCompleted)if(!fs.existsSync(path.join(root,d.evidence)))throw Error('Missing source '+d.id);
const documents=[...['README.md','AGENTS.md','新session提示词.md'].map(p=>path.join(workspace,p)),...['docs/session-handoff.md','docs/next-agent-prompt.md','README.md'].map(p=>path.join(root,p))];
let links=0;
for(const file of documents){
 const source=fs.readFileSync(file,'utf8').replace(/```[\s\S]*?```/g,'');
 for(const m of source.matchAll(/\[[^\]]*\]\(([^\s)]+)\)/g)){
  if(/^(https?:|#|mailto:)/.test(m[1]))continue;links++;
  if(!fs.existsSync(path.resolve(path.dirname(file),decodeURIComponent(m[1].split('#')[0]))))throw Error('Bad link in '+file+': '+m[1]);
 }
}
const result={checkedAt:new Date().toISOString(),status:'passed',scope:'Handoff structure, task uniqueness/status/acceptance, completed-source existence, snapshot digests and entry links; no app execution',taskCount:backlog.tasks.length,taskGroups:[...new Set(backlog.tasks.map(t=>t.group))],checkpointHashesVerified:checkpoint.snapshot.length,linksChecked:links,credentialValuesIncluded:false,newAppTestsPerformed:false,firstPassAndRun02ActionRecordsChanged:false};
fs.writeFileSync(path.join(root,'docs/handoff-verification.json'),JSON.stringify(result,null,2)+'\n');
console.log(JSON.stringify(result,null,2));
