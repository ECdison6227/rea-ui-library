import path from 'node:path';
import {execFileSync,spawnSync} from 'node:child_process';
import {ROOT,definitions} from './catalog.mjs';
const env={...process.env,GIT_TERMINAL_PROMPT:'0',GCM_INTERACTIVE:'Never'};
function git(args,cwd=ROOT){return execFileSync('git',args,{cwd,env,encoding:'utf8',windowsHide:true}).trim();}
git(['config','--local','core.longpaths','true']);
const result=spawnSync('git',['-c','core.longpaths=true','submodule','update','--init','--recursive','--depth','1'],{cwd:ROOT,env,stdio:'inherit',windowsHide:true});
if(result.error||result.status!==0)throw Error('Source initialization failed; inspect the specific repository error before continuing.');
const sources=definitions().sources.filter(s=>s.revision);
for(const s of sources){const cwd=path.join(ROOT,s.root);git(['config','--local','core.longpaths','true'],cwd);const actual=git(['rev-parse','HEAD'],cwd);if(actual!==s.revision)throw Error('The initialized source revision differs from the catalog: '+s.id);}
console.log(JSON.stringify({status:'initialized',sources:sources.length,globalGitConfigurationChanged:false,sourceWorkingTreesModified:false}));
