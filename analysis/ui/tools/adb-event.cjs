const fs=require('node:fs'),path=require('node:path'),cp=require('node:child_process');
const root=path.resolve('analysis/ui/runtime'),serial='emulator-5580';
const adb=path.join(process.env.LOCALAPPDATA,'Microsoft/WinGet/Packages/Google.PlatformTools_Microsoft.Winget.Source_8wekyb3d8bbwe/platform-tools/adb.exe');
const [id,...args]=process.argv.slice(2);if(!id||!args.length)throw Error('Use event-id then adb arguments, fixed emulator-5580 only');
const identity=cp.execFileSync(adb,['-s',serial,'shell','getprop','ro.boot.qemu.avd_name'],{encoding:'utf8',windowsHide:true}).trim();if(identity!=='rea_mihealth_ui_20261015')throw Error('Unexpected emulator identity: '+identity);
const started=new Date().toISOString();const result=cp.spawnSync(adb,['-s',serial,...args],{encoding:'utf8',windowsHide:true,maxBuffer:8*1024*1024,timeout:180000});
const record={id,serial,avd:identity,started,finished:new Date().toISOString(),args,exitCode:result.status,stdout:result.stdout||'',stderr:result.stderr||'',error:result.error?.message||null};
fs.mkdirSync(path.join(root,'events'),{recursive:true});fs.writeFileSync(path.join(root,'events',id+'.json'),JSON.stringify(record,null,2));
console.log(JSON.stringify(record,null,2));process.exitCode=result.status||0;
