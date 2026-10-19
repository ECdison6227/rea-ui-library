param([string]$Apk = (Join-Path $PSScriptRoot '../../../Kimi.apk'))
$ErrorActionPreference = 'Stop'
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
Add-Type -AssemblyName System.IO.Compression.FileSystem
$libraryRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$zip = [IO.Compression.ZipFile]::OpenRead([IO.Path]::GetFullPath($Apk))
$used = [Collections.Generic.HashSet[string]]::new([StringComparer]::OrdinalIgnoreCase)
$rows = [Collections.Generic.List[object]]::new()
$apkHash = (Get-FileHash -LiteralPath $Apk -Algorithm SHA256).Hash.ToLowerInvariant()
try {
  foreach ($entry in $zip.Entries) {
    if ($entry.FullName -notmatch '^(assets|res)/' -or $entry.FullName.EndsWith('/')) { continue }
    $relative = 'assets/original/' + $entry.FullName
    if (-not $used.Add($relative)) {
      $nameHash = [Security.Cryptography.SHA256]::Create()
      try { $suffix = [Convert]::ToBase64String($nameHash.ComputeHash([Text.Encoding]::UTF8.GetBytes($entry.FullName))).Replace('/','_').Replace('+','-').Substring(0,12) } finally { $nameHash.Dispose() }
      $relative += '.__case_' + $suffix
      if (-not $used.Add($relative)) { throw "Duplicate logical entry: $($entry.FullName)" }
    }
    $destination = [IO.Path]::GetFullPath((Join-Path $libraryRoot $relative))
    if (-not $destination.StartsWith($libraryRoot + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) { throw 'Unsafe archive path' }
    [IO.Directory]::CreateDirectory([IO.Path]::GetDirectoryName($destination)) | Out-Null
    $inputStream = $entry.Open()
    $outputStream = [IO.File]::Create($destination)
    try { $inputStream.CopyTo($outputStream) } finally { $inputStream.Dispose(); $outputStream.Dispose() }
    $extension = [IO.Path]::GetExtension($entry.FullName).ToLowerInvariant()
    $kind = switch -Regex ($extension) {
      '^\.(ttf|otf|woff2?)$' { 'font'; break }
      '^\.(riv|lottie)$' { 'animation'; break }
      '^\.cvr$' { 'localized-copy'; break }
      '^\.(png|webp|jpg|jpeg|gif|svg)$' { 'image'; break }
      '^\.(css|html|js|mjs)$' { 'renderer-code'; break }
      '^\.xml$' { 'xml-resource'; break }
      default { 'other' }
    }
    $scope = if ($entry.FullName -match '^assets/composeResources/kimi\.shared') { 'kimi-compose' } elseif ($entry.FullName -match '^assets/org/|katex|mermaid') { 'content-renderer-dependency' } elseif ($entry.FullName -match '^res/') { 'android-resource-unresolved-owner' } else { 'supporting-asset-unresolved-owner' }
    $rows.Add([PSCustomObject]@{
      id=('asset-{0:d4}' -f $rows.Count); type=$kind; path=$relative; sourceEntry=$entry.FullName;
      sourceApkSha256=$apkHash; sha256=(Get-FileHash -LiteralPath $destination -Algorithm SHA256).Hash.ToLowerInvariant();
      bytes=$entry.Length; scope=$scope; usageStatus='packaged-not-runtime-verified';
      sourceClassification='B'; origin='current'; runtimeUseEvidence=@(); themeAndState='unconfirmed';
      purpose= if ($scope -eq 'kimi-compose') { 'Kimi Compose packaged asset; exact screen mapping pending runtime' } else { 'Packaged resource; owner and runtime use require confirmation' }
    })
  }
} finally { $zip.Dispose() }
$result = [PSCustomObject]@{ schemaVersion=1; generatedAt=[DateTime]::UtcNow.ToString('o'); sourceApkSha256=$apkHash; count=$rows.Count; extractionMethod='System.IO.Compression.ZipArchive; collision-safe names; bytes unchanged'; assets=@($rows) }
$json = ConvertTo-Json -InputObject $result -Depth 7
[IO.File]::WriteAllText((Join-Path $libraryRoot 'assets/manifest.json'), $json, [Text.UTF8Encoding]::new($false))
Write-Output ($rows | Group-Object type | Select-Object Name,Count | ConvertTo-Json)
