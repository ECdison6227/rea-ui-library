import fs from 'node:fs/promises';
import {hash,saveJson} from './browser.mjs';
const pairs=[['desktop','evidence/raw/rea-desktop.png','comparison/reference-desktop.png'],['mobile','comparison/settled-probe-source-mobile.png','comparison/reference-mobile.png'],['feature','evidence/raw/scroll-desktop-scroll-2100.png','comparison/reference-scroll-2100.png']];
for(const [name,source,reference] of pairs){
  for(const [kind,file] of [['source',source],['reference',reference]]){
    const bytes=await fs.readFile(file);
    await saveJson(`comparison/artifacts/${name}-${kind}.json`,{sha256:hash(bytes),bytes:bytes.length,media_type:'image/png',data_base64:bytes.toString('base64')});
  }
}
console.log(JSON.stringify({pairs:pairs.map(([name,source,reference])=>({name,source,reference}))}));
