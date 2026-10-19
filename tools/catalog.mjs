import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {migrationPrompt} from '../site/logic.js';
export const ROOT=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
export const PLATFORMS={web:'网页',desktop:'电脑端',mobile:'手机端'};
export const hash=b=>crypto.createHash('sha256').update(b).digest('hex');
export function contained(root,relative){
 if(typeof relative!=='string'||relative.includes('\\')||relative.includes('\0')||relative.split('/').some(s=>s==='..'||s.startsWith('.'))||path.isAbsolute(relative)||relative.includes(':'))throw Error('Invalid relative source path');
 const resolved=path.resolve(root,relative);if(!resolved.startsWith(path.resolve(root)+path.sep))throw Error('Path outside source root');
 if(fs.existsSync(resolved)){const real=fs.realpathSync(resolved);if(!real.startsWith(fs.realpathSync(root)+path.sep))throw Error('Source link escapes root');}
 return resolved;
}
export function definitions(){return JSON.parse(fs.readFileSync(path.join(ROOT,'catalog/sources.json'),'utf8'));}
export function validateDefinitions(data){
 const ids=new Set(),errors=[];
 if(data.schemaVersion!==1||!Array.isArray(data.sources))return ['Invalid catalog schema'];
 for(const s of data.sources){
  if(!/^[a-z0-9]+(?:-[a-z0-9]+)*$/.test(s.id)||ids.has(s.id))errors.push('Invalid/duplicate id: '+s.id);ids.add(s.id);
  if(!PLATFORMS[s.platform]||s.kind!=='research'||s.visibility!=='public')errors.push('Not an eligible public REA research source: '+s.id);
  if(!s.root?.startsWith('platforms/'+s.platform+'/'))errors.push('Platform/root mismatch: '+s.id);
  if(!s.name||!s.coverage||!s.readiness||!s.limits?.length||!s.artifacts?.length)errors.push('Missing scope/coverage/artifacts: '+s.id);
  if(s.integrity?.manifest!=='catalog/manifests/'+s.id+'.json'||!Number.isInteger(s.integrity?.files)||s.integrity.files<1||!/^[a-f0-9]{64}$/.test(s.integrity?.treeSha256||''))errors.push('Missing/invalid content integrity: '+s.id);
  if(!s.sourceVersion||!s.provenance||!Array.isArray(s.tags)||!Array.isArray(s.stack)||!Array.isArray(s.features)||!Array.isArray(s.palette)||!s.run)errors.push('Missing study profile: '+s.id);
  if(s.revision&&!/^[a-f0-9]{40}$/.test(s.revision))errors.push('Invalid historical revision: '+s.id);
  try{const base=contained(ROOT,s.root);for(const a of s.artifacts){if(!a.label||!a.role)errors.push('Incomplete artifact: '+s.id);const f=contained(base,a.path);if(!fs.existsSync(f))errors.push('Missing artifact: '+s.id+'/'+a.path);}if(s.thumbnail){if(s.thumbnail.reviewed!==true||!fs.existsSync(contained(base,s.thumbnail.path)))errors.push('Unreviewed/missing thumbnail: '+s.id);}if(s.designPackage){const p=JSON.parse(fs.readFileSync(contained(base,s.designPackage),'utf8').replace(/^\uFEFF/,''));if(p.format!=='rea-design-package'||p.formatVersion!==1||p.id!==s.id||p.source?.platform!==s.platform||p.readiness?.status!==s.readiness)errors.push('Design package identity/platform/readiness differs: '+s.id);for(const key of ['readme','spec'])if(!fs.existsSync(contained(base,p.entries?.[key])))errors.push('Design package entry missing: '+s.id+'/'+key);}}catch(e){errors.push(s.id+': '+e.message);}
 }
 return errors;
}
export function getCatalog({privateMode=false,staticMode=false}={}){
 const raw=definitions(),errors=validateDefinitions(raw);if(errors.length)throw Error(errors.join('\n'));
 const sources=raw.sources.map(s=>({
  ...s,origin:'historical',aggregationDoesNotCreateRuntimeEvidence:true,sourceRepositoryUrl:raw.repository.url,sourceRef:raw.repository.ref,
  thumbnail:s.thumbnail?{...s.thumbnail,url:'./assets/thumbs/'+s.id+path.extname(s.thumbnail.path)}:null,
  artifacts:s.artifacts.map(a=>({...a,available:!a.restricted||privateMode,
   url:staticMode?raw.repository.url+'/blob/'+raw.repository.ref+'/'+s.root+'/'+a.path.split('/').map(encodeURIComponent).join('/'):'/source/'+s.id+'/'+a.path.split('/').map(encodeURIComponent).join('/'),
   document:!staticMode&&/\.(md|json|css|ts|swift)$/i.test(a.path)})),
  sourceRevisionUrl:raw.repository.url+'/tree/'+raw.repository.ref+'/'+s.root
 }));
 return {schemaVersion:1,owner:raw.owner,goal:raw.goal,evidencePolicy:raw.evidencePolicy,apiEnabled:!staticMode,privateMode,
  platforms:Object.entries(PLATFORMS).map(([id,name])=>({id,name,count:sources.filter(s=>s.platform===id).length})),sources};
}
export function sourceById(id){return definitions().sources.find(s=>s.id===id);}
export function readArtifact(id,relative,{privateMode=false}={}){
 const s=sourceById(id);if(!s)throw Error('Unknown source');
 const a=s.artifacts.find(a=>a.path===relative);if(!a||(a.restricted&&!privateMode))throw Error('Artifact is not available in this mode');
 const f=contained(contained(ROOT,s.root),a.path),stat=fs.statSync(f);if(stat.size>1024*1024)throw Error('Open this larger artifact in its own viewer');
 return {source:s.id,label:a.label,path:a.path,content:fs.readFileSync(f,'utf8'),sha256:hash(fs.readFileSync(f))};
}
export function sourceFile(id,relative,{privateMode=false}={}){
 const s=sourceById(id);if(!s)throw Error('Unknown source');
 if(s.id==='hark'&&!privateMode&&(relative.startsWith('evidence/')||relative==='reports/evidence-gallery.html'))throw Error('Raw Hark evidence is local-private only');
 if(/\.(apk|dmg|exe|zip|img|qcow2|jks|keystore|key|pem|log)$/i.test(relative))throw Error('This binary or local file is not served by the catalog');
 return contained(contained(ROOT,s.root),relative);
}
export function makePrompt(s){const repo=definitions().repository;return migrationPrompt({...s,sourceRepositoryUrl:repo.url,sourceRef:repo.ref});}
