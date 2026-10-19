import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const dir=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'../assets/derived/test-fixtures/run03');
const stream='BT /F1 20 Tf 50 740 Td (Run03 PDF fixture) Tj 0 -40 Td (PDF code: VIOLET-5821) Tj ET';
const bodies=['<< /Type /Catalog /Pages 2 0 R >>','<< /Type /Pages /Kids [3 0 R] /Count 1 >>','<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>','<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>',`<< /Length ${Buffer.byteLength(stream)} >>\nstream\n${stream}\nendstream`];
let pdf='%PDF-1.4\n',offsets=[0];
for(let i=0;i<bodies.length;i++){offsets.push(Buffer.byteLength(pdf));pdf+=`${i+1} 0 obj\n${bodies[i]}\nendobj\n`;}
const start=Buffer.byteLength(pdf);pdf+=`xref\n0 6\n0000000000 65535 f \n`+offsets.slice(1).map(x=>String(x).padStart(10,'0')+' 00000 n \n').join('')+`trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n${start}\n%%EOF\n`;
fs.writeFileSync(path.join(dir,'run03-document.pdf'),pdf);
let large='Run03 finite attachment fixture. LARGE code: SILVER-9364.\n';
while(Buffer.byteLength(large)<2*1024*1024)large+='Neutral research row with no personal content.\n'.repeat(1000);
fs.writeFileSync(path.join(dir,'run03-large.txt'),large);
fs.writeFileSync(path.join(dir,'run03-cancel-large.txt'),large.replace('SILVER-9364','COPPER-7295'));
fs.writeFileSync(path.join(dir,'run03-project-data.txt'),'Research Run03 project data. PROJECT_DATA_CODE=LAB-7832. A square has four sides.\n');
fs.writeFileSync(path.join(dir,'run03-corrupt.pdf'),'Run03 deliberately invalid PDF; disposable fixture.');
