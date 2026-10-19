$ErrorActionPreference='Stop'
$taskOut=[IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../assets/derived/test-fixtures/run03'))
New-Item -ItemType Directory -Path $taskOut -Force | Out-Null
Add-Type -AssemblyName System.Drawing
Add-Type -AssemblyName System.IO.Compression
$taskBmp=[Drawing.Bitmap]::new(1000,700)
$taskG=[Drawing.Graphics]::FromImage($taskBmp)
$taskG.Clear([Drawing.Color]::White)
$taskFont=[Drawing.Font]::new('Arial',48,[Drawing.FontStyle]::Bold)
$taskG.DrawString('RUN03 IMAGE 7429',$taskFont,[Drawing.Brushes]::Black,50,40)
$taskG.FillRectangle([Drawing.Brushes]::Blue,80,200,200,200)
$taskG.FillEllipse([Drawing.Brushes]::Red,420,200,200,200)
$taskG.DrawString('2 shapes',$taskFont,[Drawing.Brushes]::Black,50,500)
$taskBmp.Save((Join-Path $taskOut 'run03-image.png'),[Drawing.Imaging.ImageFormat]::Png)
$taskG.Dispose();$taskBmp.Dispose();$taskFont.Dispose()
$taskDocx=Join-Path $taskOut 'run03-office.docx'
if(Test-Path -LiteralPath $taskDocx){throw 'Do not overwrite fixture'}
$taskZip=[IO.Compression.ZipArchive]::new([IO.File]::Create($taskDocx),[IO.Compression.ZipArchiveMode]::Create)
$taskParts=@{
 '[Content_Types].xml'='<?xml version="1.0" encoding="UTF-8"?><Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types"><Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/><Default Extension="xml" ContentType="application/xml"/><Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/></Types>'
 '_rels/.rels'='<?xml version="1.0" encoding="UTF-8"?><Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships"><Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/></Relationships>'
 'word/document.xml'='<?xml version="1.0" encoding="UTF-8"?><w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"><w:body><w:p><w:r><w:t>Run03 Office research fixture. Office code: ORANGE-3196.</w:t></w:r></w:p><w:sectPr/></w:body></w:document>'
}
foreach($taskPart in $taskParts.GetEnumerator()){
 $taskEntry=$taskZip.CreateEntry($taskPart.Key)
 $taskWriter=[IO.StreamWriter]::new($taskEntry.Open(),[Text.UTF8Encoding]::new($false))
 $taskWriter.Write($taskPart.Value);$taskWriter.Dispose()
}
$taskZip.Dispose()
& node (Join-Path $PSScriptRoot 'create-run03-pdf.mjs')
Get-ChildItem -LiteralPath $taskOut -File | ForEach-Object { [pscustomobject]@{name=$_.Name;bytes=$_.Length;sha256=(Get-FileHash -LiteralPath $_.FullName -Algorithm SHA256).Hash.ToLowerInvariant()} } | ConvertTo-Json | Set-Content -LiteralPath (Join-Path $taskOut 'manifest.json') -Encoding UTF8
