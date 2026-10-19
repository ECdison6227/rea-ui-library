import {execFileSync} from 'node:child_process';
import {ROOT} from './catalog.mjs';
import {prepare} from './prepare.mjs';
execFileSync('git',['config','--local','core.longpaths','true'],{cwd:ROOT,windowsHide:true});
console.log(JSON.stringify({status:'ready',storage:'single-repository',...prepare(),globalGitConfigurationChanged:false}));
