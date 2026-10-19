/** Device-controller-only, read-only final observations. Never restores or closes a device. */
import fs from 'node:fs';
import path from 'node:path';
import {execFileSync} from 'node:child_process';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const dir=path.join(root,'evidence/runtime/run03-20261016');
const serial='emulator-5596',avd='rea_kimi_ui_20261015',pkg='com.moonshot.kimichat';
const adb=process.env.KIMI_ADB||path.join(process.env.USERPROFILE,'.rea/toolchains/android/sdk/platform-tools/adb.exe');
const [command,token='']=process.argv.slice(2);
if(!['snapshot','shutdown-check'].includes(command)||process.argv.length>4||!/^([a-z0-9]+(-[a-z0-9]+)*)?$/.test(token))throw Error('Use snapshot [unique-suffix] or shutdown-check [unique-suffix]');
const file=(command==='snapshot'?'final-device-state':'shutdown-verification')+(token?'-'+token:'')+'.json';
const out=path.join(dir,file);
if(fs.existsSync(out))throw Error('Refusing to overwrite '+file+'; use a unique suffix');
const lockFile=path.join(dir,'.operation.lock');
const lock=fs.openSync(lockFile,'wx');fs.writeFileSync(lock,String(process.pid));
process.on('exit',()=>{try{fs.closeSync(lock);fs.unlinkSync(lockFile);}catch{}});
const call=args=>execFileSync(adb,args,{encoding:'utf8',timeout:30000,maxBuffer:12*1024*1024,windowsHide:true}).trim();
const run=args=>call(['-s',serial,...args]);
const startedAt=new Date().toISOString();
let record;
if(command==='snapshot'){
 let observed=run(['emu','avd','name']).split(/[\r\n]+/).map(s=>s.trim()).find(Boolean);
 if(!observed)observed=run(['shell','getprop','ro.boot.qemu.avd_name']);
 if(observed!==avd)throw Error('Refusing a different AVD at '+serial);
 if(run(['shell','getprop','sys.boot_completed'])!=='1')throw Error('Dedicated AVD is not booted');
 const keys=[['system','accelerometer_rotation'],['system','user_rotation'],['system','font_scale'],['global','window_animation_scale'],['global','transition_animation_scale'],['global','animator_duration_scale'],['global','wifi_on'],['global','mobile_data']];
 const settings=Object.fromEntries(keys.map(([namespace,key])=>[key,run(['shell','settings','get',namespace,key])]));
 const localesGet=run(['shell','settings','get','system','system_locales']);
 const localeRows=run(['shell','settings','list','system']).split(/[\r\n]+/).filter(s=>s.startsWith('system_locales='));
 if(localeRows.length>1)throw Error('Ambiguous system_locales key');
 const connectivity=run(['shell','dumpsys','connectivity']);
 const defaultNetworkLine=connectivity.split(/[\r\n]+/).map(s=>s.trim()).find(s=>/^Active default network:/.test(s))||null;
 const activeId=defaultNetworkLine?.match(/^Active default network: (\d+)$/)?.[1]||null;
 // Capabilities must belong to this exact active network, rather than any validated network in the dump.
 const activeLines=activeId?connectivity.split(/[\r\n]+/).filter(s=>new RegExp('(?:network\\{'+activeId+'\\}|network\\{netId='+activeId+'\\})').test(s)):[];
 const activeDefaultNetworkValidated=activeLines.some(s=>/\bVALIDATED\b/.test(s));
 const packageDump=run(['shell','dumpsys','package',pkg]);
 const permissions=['POST_NOTIFICATIONS','CAMERA','RECORD_AUDIO'].map(name=>{
  const permission='android.permission.'+name;
  const matches=[...packageDump.matchAll(new RegExp('^\\s*'+permission.replaceAll('.','\\.')+': granted=(true|false), flags=\\[([^\\]]*)\\]','gm'))];
  if(matches.length!==1)throw Error('Missing or ambiguous runtime permission '+permission);
  return {permission,granted:matches[0][1]==='true',flags:matches[0][2].split('|').map(s=>s.trim()).filter(Boolean).sort()};
 });
 record={schemaVersion:2,kind:'run03-final-read-only-state',runId:'run03-20261016',serial,avd,package:pkg,startedAt,
  settings,size:run(['shell','wm','size']),density:run(['shell','wm','density']),night:run(['shell','cmd','uimode','night']),
  wmUserRotation:run(['shell','wm','user-rotation']),wmFixedToUserRotation:run(['shell','wm','fixed-to-user-rotation']),
  appLocales:run(['shell','cmd','locale','get-app-locales',pkg]),ime:run(['shell','settings','get','secure','default_input_method']),
  locale:{systemLocalesGet:localesGet,systemLocalesKeyPresent:localeRows.length===1,systemLocalesStoredValue:localeRows[0]?.slice('system_locales='.length)??null,buildLocale:run(['shell','getprop','ro.product.locale']),persistedLocale:run(['shell','getprop','persist.sys.locale'])},
  network:{wifi:settings.wifi_on,data:settings.mobile_data,defaultNetworkLine,activeDefaultNetworkValidated,validationMethod:'Exact active network id must occur on a NetworkAgentInfo line with VALIDATED; no fallback to an unrelated network'},
  permissions,foreground:run(['shell','dumpsys','activity','activities']).split(/[\r\n]+/).map(s=>s.trim()).filter(s=>s.includes('topResumedActivity=')),
  limitation:'Read-only shell observations are sequential, not atomic. App UI preferences and source APK bytes require separate retained evidence.',completedAt:new Date().toISOString()};
}else{
 const listing=call(['devices']);
 const serials=listing.split(/[\r\n]+/).map(s=>s.trim().split(/\s+/)[0]).filter(s=>/^emulator-\d+$/.test(s));
 if(process.platform!=='win32')throw Error('Shutdown process verification is implemented for this Windows research host only');
 const ps="$ErrorActionPreference='Stop'; $owned=@(Get-CimInstance Win32_Process | Where-Object { $_.Name -match '^(emulator|qemu-system-[^.]+)\\.exe$' -and ($_.CommandLine -match '(?:-avd\\s+\"?rea_kimi_ui_20261015(?:\"|\\s|$)|@rea_kimi_ui_20261015(?:\\s|$)|-port\\s+5596(?:\\s|$)|-ports\\s+5596,5597(?:\\s|$))') } | ForEach-Object { [pscustomobject]@{pid=$_.ProcessId; name=$_.Name} }); ConvertTo-Json -InputObject $owned -Compress";
 const remainingOwnedProcesses=JSON.parse(execFileSync('powershell.exe',['-NoProfile','-NonInteractive','-Command',ps],{encoding:'utf8',timeout:30000,windowsHide:true}).replace(/^\uFEFF/,'').trim());
 if(!Array.isArray(remainingOwnedProcesses))throw Error('Unexpected owned-process result');
 record={schemaVersion:2,serial,dedicatedAvd:avd,startedAt,checkedAt:new Date().toISOString(),serialStillListed:serials.includes(serial),remainingOwnedProcesses,otherEmulatorSerialsObserved:serials.filter(s=>s!==serial),method:'Read-only adb devices plus Windows process identity match for dedicated AVD/5596; this command stops nothing and never calls kill-server'};
}
fs.writeFileSync(out,JSON.stringify(record,null,2)+'\n',{flag:'wx'});
console.log(JSON.stringify({file:'evidence/runtime/run03-20261016/'+file,...record},null,2));
if(command==='snapshot'&&!record.network.activeDefaultNetworkValidated)process.exitCode=1;
if(command==='shutdown-check'&&(record.serialStillListed||record.remainingOwnedProcesses.length))process.exitCode=1;
