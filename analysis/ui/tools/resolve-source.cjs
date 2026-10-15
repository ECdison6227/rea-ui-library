const fs=require('fs'),path=require('path');
const ui=path.resolve(__dirname,'..'),requested=process.argv[2];
if(!requested)throw Error('Pass a source path recorded in this library');
const normalized=requested.replaceAll('\\','/').replace(/:\d+$/,'');
const local=normalized.startsWith('analysis/ui/')?path.join(ui,normalized.slice('analysis/ui/'.length)):path.resolve(ui,'../..',normalized);
if(fs.existsSync(local)){console.log(local);process.exit(0);}
for(const manifest of ['baseline/source-snapshot-map.json','evidence/source-snapshot-map.json']){
 const x=JSON.parse(fs.readFileSync(path.join(ui,manifest),'utf8').replace(/^\uFEFF/,''));const row=x.files.find(x=>x.original===normalized);if(row){const portable=path.join(ui,row.portable);if(fs.existsSync(portable)){console.log(portable);process.exit(0);}}
}
console.error('Source not in the portable selected evidence: '+requested);process.exitCode=1;
