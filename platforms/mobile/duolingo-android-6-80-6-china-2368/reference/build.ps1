param(
    [string]$JdkPath,
    [string]$SdkPath,
    [string]$BuildToolsVersion = '35.0.0',
    [string]$CompilePlatform = 'android-35'
)
$ErrorActionPreference = 'Stop'
$refRoot = [IO.Path]::GetFullPath($PSScriptRoot)
if (-not $JdkPath) { $JdkPath = Join-Path $env:USERPROFILE '.rea/toolchains/android/jdk-21' }
if (-not $SdkPath) { $SdkPath = Join-Path $env:USERPROFILE '.rea/toolchains/android/sdk' }
$JdkPath = [IO.Path]::GetFullPath($JdkPath)
$SdkPath = [IO.Path]::GetFullPath($SdkPath)
$buildTools = Join-Path $SdkPath ('build-tools/' + $BuildToolsVersion)
$androidJar = Join-Path $SdkPath ('platforms/' + $CompilePlatform + '/android.jar')
$java = Join-Path $JdkPath 'bin/java.exe'
$javac = Join-Path $JdkPath 'bin/javac.exe'
$jar = Join-Path $JdkPath 'bin/jar.exe'
$keytool = Join-Path $JdkPath 'bin/keytool.exe'
$aapt = Join-Path $buildTools 'aapt2.exe'
$zipalign = Join-Path $buildTools 'zipalign.exe'
$d8Jar = Join-Path $buildTools 'lib/d8.jar'
$signerJar = Join-Path $buildTools 'lib/apksigner.jar'
$toolFiles = @($java,$javac,$jar,$keytool,$aapt,$zipalign,$d8Jar,$signerJar,$androidJar)
foreach ($toolFile in $toolFiles) { if (-not (Test-Path -LiteralPath $toolFile -PathType Leaf)) { throw "Missing tool: $toolFile. Supply -JdkPath and -SdkPath; no global installation is attempted." } }
function Invoke-Checked([string]$Program,[string[]]$Arguments) {
    $oldPreference = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    & $Program @Arguments
    $commandExit = $LASTEXITCODE
    $ErrorActionPreference = $oldPreference
    if ($commandExit -ne 0) { throw ('Command failed (' + $commandExit + '): ' + $Program) }
}
function Read-ToolVersion([string]$Program,[string[]]$Arguments) {
    $oldPreference = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    $versionOutput = & $Program @Arguments 2>&1
    $commandExit = $LASTEXITCODE
    $ErrorActionPreference = $oldPreference
    if ($commandExit -ne 0) { throw ('Tool query failed (' + $commandExit + '): ' + (($versionOutput | ForEach-Object { $_.ToString() }) -join "`n")) }
    return (($versionOutput | ForEach-Object { $_.ToString() }) -join "`n")
}
$out = Join-Path $refRoot 'build'
$classes = Join-Path $out 'classes'
$dex = Join-Path $out 'dex'
$generated = Join-Path $out 'generated'
$testClasses = Join-Path $out 'test-classes'
$packagerClasses = Join-Path $out 'packager-classes'
foreach ($dir in @($out,$classes,$dex,$generated,$testClasses,$packagerClasses)) { [IO.Directory]::CreateDirectory($dir) | Out-Null }

# No recursive cleanup and no environment mutation. Every generated file stays inside reference/build.
Write-Output 'Compiling native resources...'
$compiled = Join-Path $out 'compiled-res.zip'
$resourcesApk = Join-Path $out 'resources.ap_'
Invoke-Checked $aapt @('compile','--dir',(Join-Path $refRoot 'res'),'-o',$compiled)
Invoke-Checked $aapt @('link','-o',$resourcesApk,'-I',$androidJar,'--manifest',(Join-Path $refRoot 'AndroidManifest.xml'),'--java',$generated,'--min-sdk-version','29','--target-sdk-version','31',$compiled)

Write-Output 'Compiling Java and state-contract assertions...'
$sourceFiles = @(Get-ChildItem -LiteralPath (Join-Path $refRoot 'src') -Recurse -Filter '*.java' | ForEach-Object FullName)
$transferSourceRoot = [IO.Path]::GetFullPath((Join-Path $refRoot '../transfer-example/src'))
$sourceFiles += @(Get-ChildItem -LiteralPath $transferSourceRoot -Recurse -Filter '*.java' | ForEach-Object FullName)
$generatedFiles = @(Get-ChildItem -LiteralPath $generated -Recurse -Filter '*.java' | ForEach-Object FullName)
Invoke-Checked $javac (@('-J-Duser.language=en','-encoding','UTF-8','--release','8','-Xlint:-options','-classpath',$androidJar,'-d',$classes) + $sourceFiles + $generatedFiles)
$stateFile = Join-Path $refRoot 'src/org/reastyle/nativeexample/FlowState.java'
$testFile = Join-Path $refRoot 'tests/org/reastyle/nativeexample/FlowStateTest.java'
Invoke-Checked $javac @('-J-Duser.language=en','-encoding','UTF-8','--release','8','-Xlint:-options','-d',$testClasses,$stateFile,$testFile)
$testOutput = Read-ToolVersion $java @('-cp',$testClasses,'org.reastyle.nativeexample.FlowStateTest')
if ($LASTEXITCODE -ne 0 -or $testOutput -notmatch 'PASS 20 state-contract assertions') { throw ('State checks failed: ' + $testOutput) }
Write-Output $testOutput

Write-Output 'Dexing and packaging...'
$classJar = Join-Path $out 'classes.jar'
Invoke-Checked $jar @('--create','--file',$classJar,'-C',$classes,'.')
Invoke-Checked $java @('-cp',$d8Jar,'com.android.tools.r8.D8','--lib',$androidJar,'--min-api','29','--output',$dex,$classJar)
$unsigned = Join-Path $out 'native-reference-unsigned.apk'
$assetsRoot = [IO.Path]::GetFullPath((Join-Path $refRoot 'assets'))
# Use JDK ZIP so STORED is explicit; .NET NoCompression can still produce method8 blocks.
$packagerSource = Join-Path $refRoot 'tools/PackageApk.java'
Invoke-Checked $javac @('-J-Duser.language=en','-encoding','UTF-8','--release','8','-Xlint:-options','-d',$packagerClasses,$packagerSource)
Invoke-Checked $java @('-cp',$packagerClasses,'PackageApk',$resourcesApk,$unsigned,$assetsRoot,$dex)
Add-Type -AssemblyName System.IO.Compression.FileSystem
Add-Type -AssemblyName System.IO.Compression
$aligned = Join-Path $out 'native-reference-aligned.apk'
Invoke-Checked $zipalign @('-f','4',$unsigned,$aligned)

# Public local debug identity, never a user or production signing key.
$keystore = Join-Path $out 'reference-debug.p12'
if (-not (Test-Path -LiteralPath $keystore)) {
    Invoke-Checked $keytool @('-genkeypair','-keystore',$keystore,'-storetype','PKCS12','-alias','reference-debug','-storepass','android','-keypass','android','-dname','CN=REA Local Reference, O=Local Research, C=ZZ','-keyalg','RSA','-keysize','2048','-validity','3650')
}
$apk = Join-Path $out 'native-reference.apk'
Invoke-Checked $java @('-jar',$signerJar,'sign','--ks',$keystore,'--ks-key-alias','reference-debug','--ks-pass','pass:android','--key-pass','pass:android','--out',$apk,$aligned)
Invoke-Checked $zipalign @('-c','4',$apk)
$signatureOutput = Read-ToolVersion $java @('-jar',$signerJar,'verify','--verbose','--print-certs',$apk)
if ($LASTEXITCODE -ne 0 -or $signatureOutput -notmatch 'Verifies') { throw ('APK signature check failed: ' + $signatureOutput) }
$signatureOutput | Set-Content -LiteralPath (Join-Path $out 'signature-verification.txt') -Encoding UTF8
$methodVerification = Read-ToolVersion $java @('-cp',$packagerClasses,'PackageApk','--verify',$apk,$assetsRoot)
$methodVerification | Set-Content -LiteralPath (Join-Path $out 'apk-zip-method-verification.txt') -Encoding UTF8

# Verify the signed APK's actual bytes, not just input files or successful aapt output.
$archiveChecks = @()
$signedZip = [IO.Compression.ZipFile]::OpenRead($apk)
try {
    foreach ($entry in $signedZip.Entries) {
        if ($entry.FullName.Contains('\')) { throw ('Signed APK has noncanonical ZIP separator: ' + $entry.FullName) }
    }
    $requiredAssets = @(Get-ChildItem -LiteralPath $assetsRoot -Recurse -File)
    foreach ($assetFile in $requiredAssets) {
        $memberName = 'assets/' + $assetFile.FullName.Substring($assetsRoot.Length+1).Replace('\','/')
        $entry = $signedZip.GetEntry($memberName)
        if (-not $entry) { throw ('Signed APK is missing asset: ' + $memberName) }
        $stream = $entry.Open()
        $sha = [Security.Cryptography.SHA256]::Create()
        try { $memberHash = ([BitConverter]::ToString($sha.ComputeHash($stream))).Replace('-','').ToLowerInvariant() } finally { $stream.Dispose(); $sha.Dispose() }
        $sourceHash = (Get-FileHash -LiteralPath $assetFile.FullName -Algorithm SHA256).Hash.ToLowerInvariant()
        if ($memberHash -ne $sourceHash) { throw ('Signed APK asset hash mismatch: ' + $memberName) }
        $archiveChecks += [ordered]@{path=$memberName;bytes=$entry.Length;compressedBytes=$entry.CompressedLength;zipMethod='STORED(0), verified by PackageApk';sha256=$memberHash;sourceSha256=$sourceHash;result='passed'}
    }
    $dexEntry = $signedZip.GetEntry('classes.dex')
    if (-not $dexEntry -or $dexEntry.Length -eq 0) { throw 'Signed APK is missing nonempty classes.dex' }
    $archiveChecks += [ordered]@{path='classes.dex';bytes=$dexEntry.Length;result='present-nonempty'}
    $signedMembers = @($signedZip.Entries | ForEach-Object FullName)
} finally { $signedZip.Dispose() }
$archiveReport = [ordered]@{schemaVersion=1;apkSha256=(Get-FileHash -LiteralPath $apk -Algorithm SHA256).Hash.ToLowerInvariant();noncanonicalSeparators=0;checks=$archiveChecks;members=$signedMembers}
$archiveReport | ConvertTo-Json -Depth 8 | Set-Content -LiteralPath (Join-Path $out 'apk-contents-verification.json') -Encoding UTF8
Write-Output ('PASS signed APK canonical paths, ' + $requiredAssets.Length + ' exact asset hashes and classes.dex')

$toolIdentity = @()
foreach ($toolFile in $toolFiles) { $toolIdentity += [ordered]@{ name=[IO.Path]::GetFileName($toolFile); path=$toolFile; sha256=(Get-FileHash -LiteralPath $toolFile -Algorithm SHA256).Hash.ToLowerInvariant() } }
$inputs = @()
foreach ($inputFile in @((Get-Item -LiteralPath (Join-Path $refRoot 'AndroidManifest.xml')),(Get-Item -LiteralPath (Join-Path $refRoot 'build.ps1'))) + @(Get-ChildItem -LiteralPath (Join-Path $refRoot 'src'),(Join-Path $refRoot 'res'),(Join-Path $refRoot 'assets'),(Join-Path $refRoot 'tests'),(Join-Path $refRoot 'tools') -Recurse -File)) {
    $inputs += [ordered]@{ path=$inputFile.FullName.Substring($refRoot.Length+1).Replace('\','/'); sha256=(Get-FileHash -LiteralPath $inputFile.FullName -Algorithm SHA256).Hash.ToLowerInvariant() }
}
foreach ($inputFile in @(Get-ChildItem -LiteralPath $transferSourceRoot -Recurse -File)) {
    $inputs += [ordered]@{ path=('../transfer-example/src/' + $inputFile.FullName.Substring($transferSourceRoot.Length+1).Replace('\','/')); sha256=(Get-FileHash -LiteralPath $inputFile.FullName -Algorithm SHA256).Hash.ToLowerInvariant() }
}
$report = [ordered]@{
    schemaVersion=1; classification='D'; builtAtUtc=[DateTime]::UtcNow.ToString('o'); result='passed';
    packageName='org.reastyle.nativeexample'; minSdk=29; targetSdk=31; compilePlatform=$CompilePlatform;
    javaVersion=(Read-ToolVersion $java @('-version')); javacVersion=(Read-ToolVersion $javac @('-version'));
    aapt2Version=(Read-ToolVersion $aapt @('version')); d8Version=(Read-ToolVersion $java @('-cp',$d8Jar,'com.android.tools.r8.D8','--version'));
    buildToolsVersion=$BuildToolsVersion; stateChecks=$testOutput; signatureVerification='signature-verification.txt'; archiveVerification='apk-contents-verification.json';
    tools=$toolIdentity; inputs=$inputs; apk=[ordered]@{path='native-reference.apk';sha256=(Get-FileHash -LiteralPath $apk -Algorithm SHA256).Hash.ToLowerInvariant();bytes=(Get-Item -LiteralPath $apk).Length};
    limitations=@('Build and JVM state checks only; device runtime and source comparison are separate.','Debug signing key is a public local sample identity.','Source fonts and vectors are included only for private source-reference study; Paperlane scenes use no source brand assets.','A fresh debug key and ZIP timestamps can change APK byte identity; reproducible here means a documented rebuild, not byte-identical output.')
}
$report | ConvertTo-Json -Depth 10 | Set-Content -LiteralPath (Join-Path $out 'build-report.json') -Encoding UTF8
Write-Output ('BUILD PASSED: ' + $apk)
Write-Output ('SHA256: ' + $report.apk.sha256)
