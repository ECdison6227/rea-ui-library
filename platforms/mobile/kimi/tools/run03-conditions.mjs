import fs from 'node:fs';
import path from 'node:path';
import {execFileSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const dir=path.join(root,'evidence/runtime/run03-20261016');
const adb=process.env.KIMI_ADB||path.join(process.env.USERPROFILE,'.rea/toolchains/android/sdk/platform-tools/adb.exe');
const serial='emulator-5596',avd='rea_kimi_ui_20261015',pkg='com.moonshot.kimichat';
const [command]=process.argv.slice(2);
if(!['baseline','landscape','landscape-forced','portrait','narrow-large','reduced-motion','language-de','language-ar','restore','restore-display','inspect'].includes(command))throw Error('Unknown condition command');
const lockFile=path.join(dir,'.operation.lock');let lock=fs.openSync(lockFile,'wx');fs.writeFileSync(lock,String(process.pid));
process.on('exit',()=>{try{fs.closeSync(lock);fs.unlinkSync(lockFile);}catch{}});
const run=a=>execFileSync(adb,['-s',serial,...a],{encoding:'utf8',timeout:30000,windowsHide:true}).trim();
let observedAvd=run(['emu','avd','name']).split(/[\r\n]+/).map(s=>s.trim()).find(Boolean);
if(!observedAvd)observedAvd=run(['shell','getprop','ro.boot.qemu.avd_name']);
if(observedAvd!==avd)throw Error('Wrong AVD');
const keys=[['system','accelerometer_rotation'],['system','user_rotation'],['system','font_scale'],['global','window_animation_scale'],['global','transition_animation_scale'],['global','animator_duration_scale']];
const read=()=>({serial,avd,at:new Date().toISOString(),settings:Object.fromEntries(keys.map(([n,k])=>[k,run(['shell','settings','get',n,k])])),size:run(['shell','wm','size']),density:run(['shell','wm','density']),appLocales:run(['shell','cmd','locale','get-app-locales',pkg]),ime:run(['shell','settings','get','secure','default_input_method']),deviceDate:run(['shell','date','+%Y-%m-%dT%H:%M:%S%z']),deviceTimezone:run(['shell','getprop','persist.sys.timezone']),wmUserRotation:run(['shell','wm','user-rotation']),wmFixedToUserRotation:run(['shell','wm','fixed-to-user-rotation'])});
const baseFile=path.join(dir,'conditions-baseline.json');
const wmBaseFile=path.join(dir,'wm-rotation-baseline.json');
if(command==='baseline'){if(fs.existsSync(baseFile))throw Error('Baseline already retained');fs.writeFileSync(baseFile,JSON.stringify(read(),null,2)+'\n');}
else if(command==='restore'||command==='restore-display'){
 const b=JSON.parse(fs.readFileSync(baseFile));if(b.serial!==serial||b.avd!==avd)throw Error('Wrong baseline');
 let wmBaseline;
 if(fs.existsSync(wmBaseFile)){
  wmBaseline=JSON.parse(fs.readFileSync(wmBaseFile));if(wmBaseline.serial!==serial||wmBaseline.avd!==avd||!(/^(free|lock [0-3])$/.test(wmBaseline.userRotation))||!(/^(default|enabled|disabled|enabled_if_no_auto_rotation)$/.test(wmBaseline.fixed)))throw Error('Invalid WM baseline');
  run(['shell','wm','fixed-to-user-rotation',wmBaseline.fixed]);run(['shell','wm','user-rotation',...wmBaseline.userRotation.split(' ')]);
 }
 for(const [n,k]of keys)run(['shell','settings',b.settings[k]==='null'?'delete':'put',n,k,...(b.settings[k]==='null'?[]:[b.settings[k]])]);
 const override=b.size.match(/Override size: (\d+x\d+)/);run(['shell','wm','size',override?override[1]:'reset']);
 const locales=b.appLocales.match(/\[([^\]]*)\]/)?.[1];if(locales===undefined)throw Error('Unknown baseline app locales');
 if(command==='restore')run(['shell','cmd','locale','set-app-locales',pkg,'--user','0',...(locales?['--locales',locales]:[])]);
 const actual=read();actual.restorationScope=command==='restore'?'display, app locale and IME service':'display and IME service only; app locale remains under test';actual.baselineMatch=keys.every(([,k])=>actual.settings[k]===b.settings[k])&&actual.size===b.size&&actual.density===b.density&&(command==='restore-display'||actual.appLocales===b.appLocales)&&actual.ime===b.ime&&(!wmBaseline||(actual.wmUserRotation===wmBaseline.userRotation&&actual.wmFixedToUserRotation===wmBaseline.fixed));
 fs.writeFileSync(path.join(dir,command==='restore'?'conditions-restored.json':'conditions-display-restored.json'),JSON.stringify(actual,null,2)+'\n');if(!actual.baselineMatch)throw Error('Restoration differs');
}else if(command!=='inspect'){
 if(!fs.existsSync(baseFile))throw Error('Record baseline first');
 if(command==='landscape-forced'){
  if(!fs.existsSync(wmBaseFile))fs.writeFileSync(wmBaseFile,JSON.stringify({serial,avd,at:new Date().toISOString(),userRotation:run(['shell','wm','user-rotation']),fixed:run(['shell','wm','fixed-to-user-rotation'])},null,2)+'\n');
  run(['shell','wm','user-rotation','lock','1']);run(['shell','wm','fixed-to-user-rotation','enabled']);
 }
 else if(command.startsWith('language-')){run(['shell','cmd','locale','set-app-locales',pkg,'--user','0','--locales',command==='language-de'?'de-DE':'ar']);}
 else {
 const changes=command==='landscape'?[['system','accelerometer_rotation','0'],['system','user_rotation','1']]:command==='portrait'?[['system','accelerometer_rotation','0'],['system','user_rotation','0']]:command==='narrow-large'?[['system','accelerometer_rotation','0'],['system','user_rotation','0'],['system','font_scale','1.3']]:[['global','window_animation_scale','0'],['global','transition_animation_scale','0'],['global','animator_duration_scale','0']];
 for(const [n,k,v]of changes)run(['shell','settings','put',n,k,v]);
 if(command==='narrow-large')run(['shell','wm','size','800x1800']);
 }
}
const result={command,...read()};
const log=path.join(dir,'conditions-events.json');const events=fs.existsSync(log)?JSON.parse(fs.readFileSync(log)):[];events.push(result);fs.writeFileSync(log,JSON.stringify(events,null,2)+'\n');console.log(JSON.stringify(result));
