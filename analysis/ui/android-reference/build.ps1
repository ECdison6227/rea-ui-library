param(
    [string]$SdkRoot,
    [string]$JavaHome,
    [string]$BuildWorkRoot = [IO.Path]::GetTempPath(),
    [string]$BuildToolsVersion = '35.0.0',
    [int]$CompileSdk = 35,
    [int]$MinSdk = 26,
    [int]$TargetSdk = 35
)

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version 2
$projectRoot = [IO.Path]::GetFullPath($PSScriptRoot)

function Select-Root([string[]]$Candidates, [string]$RequiredChild, [string]$Description) {
    foreach ($candidate in $Candidates) {
        if (-not [string]::IsNullOrWhiteSpace($candidate)) {
            $resolved = [IO.Path]::GetFullPath($candidate)
            if (Test-Path -LiteralPath (Join-Path $resolved $RequiredChild)) { return $resolved }
        }
    }
    throw "$Description not found. Pass the corresponding parameter; see README.md."
}

if (-not $SdkRoot) {
    $SdkRoot = Select-Root @(
        $env:ANDROID_SDK_ROOT,
        $env:ANDROID_HOME,
        $(if ($env:LOCALAPPDATA) { Join-Path $env:LOCALAPPDATA 'Android\Sdk' }),
        $(if ($env:USERPROFILE) { Join-Path $env:USERPROFILE '.rea\toolchains\android\sdk' })
    ) "platforms\android-$CompileSdk\android.jar" 'Android SDK'
}
if (-not $JavaHome) {
    $JavaHome = Select-Root @(
        $env:JAVA_HOME,
        $(if ($env:USERPROFILE) { Join-Path $env:USERPROFILE '.rea\toolchains\android\jdk-21' })
    ) 'bin\javac.exe' 'JDK'
}
$SdkRoot = [IO.Path]::GetFullPath($SdkRoot)
$JavaHome = [IO.Path]::GetFullPath($JavaHome)
$env:JAVA_HOME = $JavaHome
$env:Path = (Join-Path $JavaHome 'bin') + [IO.Path]::PathSeparator + $env:Path
$buildTools = Join-Path $SdkRoot "build-tools\$BuildToolsVersion"
$androidJar = Join-Path $SdkRoot "platforms\android-$CompileSdk\android.jar"
$tools = @{
    aapt2 = (Join-Path $buildTools 'aapt2.exe')
    d8 = (Join-Path $buildTools 'd8.bat')
    zipalign = (Join-Path $buildTools 'zipalign.exe')
    apksigner = (Join-Path $buildTools 'apksigner.bat')
    javac = (Join-Path $JavaHome 'bin\javac.exe')
    keytool = (Join-Path $JavaHome 'bin\keytool.exe')
}
foreach ($toolName in $tools.Keys) {
    if (-not (Test-Path -LiteralPath $tools[$toolName])) { throw "Missing $toolName at $($tools[$toolName])" }
}
if (-not (Test-Path -LiteralPath $androidJar)) { throw "Missing platform: $androidJar" }
if (-not (Test-Path -LiteralPath (Join-Path $projectRoot 'src\study\style\reference\MainActivity.java'))) {
    throw 'MainActivity.java is absent; add the reference implementation before building.'
}

$buildRoot = Join-Path $projectRoot 'build'
$scratchParent = [IO.Path]::GetFullPath($BuildWorkRoot)
$workRoot = [IO.Path]::GetFullPath((Join-Path $scratchParent ('rea-style-reference-' + [Guid]::NewGuid().ToString('N'))))
$requiredWorkPrefix = $scratchParent.TrimEnd('\') + '\'
# All generated files go in a unique directory. AAPT2 on Windows cannot reliably
# open this repository's Chinese path; staging avoids passing it to native tools.
if (-not $workRoot.StartsWith($requiredWorkPrefix, [StringComparison]::OrdinalIgnoreCase)) { throw "Unsafe generated output path: $workRoot" }
foreach ($directory in @($buildRoot, $workRoot, (Join-Path $workRoot 'gen'), (Join-Path $workRoot 'classes'), (Join-Path $workRoot 'dex'), (Join-Path $projectRoot '.local'))) {
    New-Item -ItemType Directory -Path $directory -Force | Out-Null
}
$inputRoot = Join-Path $workRoot 'input'
New-Item -ItemType Directory -Path $inputRoot -Force | Out-Null
foreach ($inputName in @('src', 'res', 'assets', 'AndroidManifest.xml')) {
    $inputPath = Join-Path $projectRoot $inputName
    if (Test-Path -LiteralPath $inputPath) { Copy-Item -LiteralPath $inputPath -Destination $inputRoot -Recurse -Force }
}
$logPath = Join-Path $buildRoot 'build.log'
Set-Content -LiteralPath $logPath -Value "Build started $([DateTime]::UtcNow.ToString('o'))" -Encoding UTF8
Add-Content -LiteralPath $logPath -Value "Temporary staging: $workRoot (retained if the build fails)" -Encoding UTF8

function Invoke-BuildTool([string]$Executable, [string[]]$Arguments) {
    Add-Content -LiteralPath $logPath -Value ("`n> " + [IO.Path]::GetFileName($Executable) + ' ' + ($Arguments -join ' ')) -Encoding UTF8
    # Windows PowerShell treats a native stderr line as an ErrorRecord. Keep warnings
    # in the log, then use the native exit code as the authoritative result.
    $previousPreference = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    try {
        $toolOutput = & $Executable @Arguments 2>&1
        $toolExitCode = $LASTEXITCODE
    } finally { $ErrorActionPreference = $previousPreference }
    foreach ($line in $toolOutput) {
        $lineText = $line.ToString()
        Add-Content -LiteralPath $logPath -Value $lineText -Encoding UTF8
        Write-Host $lineText
    }
    if ($toolExitCode -ne 0) { throw "Build tool failed ($toolExitCode): $Executable; see $logPath" }
}

$compiledRes = Join-Path $workRoot 'resources.zip'
$baseApk = Join-Path $workRoot 'base.apk'
$alignedApk = Join-Path $workRoot 'aligned.apk'
$signedApk = Join-Path $workRoot 'style-reference.apk'
$apkPath = Join-Path $buildRoot 'style-reference.apk'
Invoke-BuildTool $tools.aapt2 @('compile', '--dir', (Join-Path $inputRoot 'res'), '-o', $compiledRes)
$linkArguments = @('link', '-o', $baseApk, '-I', $androidJar, '--manifest', (Join-Path $inputRoot 'AndroidManifest.xml'), '--java', (Join-Path $workRoot 'gen'), '-R', $compiledRes, '--min-sdk-version', "$MinSdk", '--target-sdk-version', "$TargetSdk", '--auto-add-overlay')
if (Test-Path -LiteralPath (Join-Path $inputRoot 'assets')) { $linkArguments += @('-A', (Join-Path $inputRoot 'assets')) }
Invoke-BuildTool $tools.aapt2 $linkArguments
$javaSources = @(Get-ChildItem -LiteralPath (Join-Path $inputRoot 'src'), (Join-Path $workRoot 'gen') -Recurse -File -Filter '*.java' | Sort-Object FullName | ForEach-Object { $_.FullName })
# Use argument files for Java/D8 so longer generated source lists cannot exceed
# cmd.exe limits; argument-file paths use forward slashes to preserve Windows paths.
$javacResponse = Join-Path $workRoot 'javac-sources.txt'
$javacSourceLines = @($javaSources | ForEach-Object { '"' + $_.Replace('\', '/') + '"' })
[IO.File]::WriteAllLines($javacResponse, $javacSourceLines, (New-Object Text.UTF8Encoding($false)))
# android.jar exposes Android's public API stubs, including an incomplete
# LambdaMetafactory stub that javac cannot use as its Java 8 bootstrap runtime.
# Compile against the JDK's Java 8 signatures and add Android APIs as classpath;
# D8 below performs the default language desugaring for the selected min API.
Invoke-BuildTool $tools.javac @('-J-Duser.language=en', '-encoding', 'UTF-8', '--release', '8', '-classpath', $androidJar, '-d', (Join-Path $workRoot 'classes'), "@$javacResponse")
$classFiles = @(Get-ChildItem -LiteralPath (Join-Path $workRoot 'classes') -Recurse -File -Filter '*.class' | Sort-Object FullName | ForEach-Object { $_.FullName })
$d8Response = Join-Path $workRoot 'd8-classes.txt'
$d8ClassLines = @($classFiles | ForEach-Object { $_.Replace('\', '/') })
[IO.File]::WriteAllLines($d8Response, $d8ClassLines, (New-Object Text.UTF8Encoding($false)))
Invoke-BuildTool $tools.d8 @('--lib', $androidJar, '--min-api', "$MinSdk", '--output', (Join-Path $workRoot 'dex'), "@$d8Response")
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem
$apkArchive = [IO.Compression.ZipFile]::Open($baseApk, [IO.Compression.ZipArchiveMode]::Update)
try {
    foreach ($dexFile in (Get-ChildItem -LiteralPath (Join-Path $workRoot 'dex') -Filter '*.dex' -File)) {
        [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($apkArchive, $dexFile.FullName, $dexFile.Name, [IO.Compression.CompressionLevel]::Optimal) | Out-Null
    }
} finally { $apkArchive.Dispose() }
Invoke-BuildTool $tools.zipalign @('-f', '-p', '4', $baseApk, $alignedApk)
$savedKeyStore = Join-Path $projectRoot '.local\debug.keystore'
$keyStore = Join-Path $workRoot 'debug.keystore'
if (Test-Path -LiteralPath $savedKeyStore) { Copy-Item -LiteralPath $savedKeyStore -Destination $keyStore }
else {
    Invoke-BuildTool $tools.keytool @('-J-Duser.language=en', '-genkeypair', '-keystore', $keyStore, '-storepass', 'android', '-alias', 'androiddebugkey', '-keypass', 'android', '-dname', 'CN=Android Debug,O=Local Reference,C=CN', '-validity', '10000', '-keyalg', 'RSA', '-keysize', '2048')
    Copy-Item -LiteralPath $keyStore -Destination $savedKeyStore
}
Invoke-BuildTool $tools.apksigner @('sign', '--ks', $keyStore, '--ks-key-alias', 'androiddebugkey', '--ks-pass', 'pass:android', '--key-pass', 'pass:android', '--out', $signedApk, $alignedApk)
Invoke-BuildTool $tools.apksigner @('verify', '--verbose', '--print-certs', $signedApk)
Invoke-BuildTool $tools.zipalign @('-c', '-p', '4', $signedApk)
Copy-Item -LiteralPath $signedApk -Destination $apkPath -Force
$hashRoots = @((Join-Path $inputRoot 'src'), (Join-Path $inputRoot 'res'))
if (Test-Path -LiteralPath (Join-Path $inputRoot 'assets')) { $hashRoots += (Join-Path $inputRoot 'assets') }
$buildInfo = [ordered]@{
    builtAtUtc = [DateTime]::UtcNow.ToString('o')
    package = 'study.style.reference'
    apk = 'build/style-reference.apk'
    sha256 = (Get-FileHash -LiteralPath $apkPath -Algorithm SHA256).Hash.ToLowerInvariant()
    sizeBytes = (Get-Item -LiteralPath $apkPath).Length
    compileSdk = $CompileSdk
    minSdk = $MinSdk
    targetSdk = $TargetSdk
    buildTools = $BuildToolsVersion
    javaLanguageRelease = 8
    d8Desugaring = 'default enabled; min-api controls Android language desugaring'
    sdkRoot = $SdkRoot
    javaHome = $JavaHome
    recipeSha256 = (Get-FileHash -LiteralPath (Join-Path $projectRoot 'build.ps1') -Algorithm SHA256).Hash.ToLowerInvariant()
    inputSha256 = @((Get-ChildItem -LiteralPath $hashRoots -Recurse -File) + (Get-Item -LiteralPath (Join-Path $inputRoot 'AndroidManifest.xml')) | Sort-Object FullName | ForEach-Object { [ordered]@{ path = $_.FullName.Substring($inputRoot.Length + 1).Replace('\', '/'); sha256 = (Get-FileHash -LiteralPath $_.FullName -Algorithm SHA256).Hash.ToLowerInvariant() } })
}
$buildInfo | ConvertTo-Json -Depth 6 | Set-Content -LiteralPath (Join-Path $buildRoot 'build-info.json') -Encoding UTF8
# Delete only the unique scratch directory created above, after validating its
# final absolute path against the explicitly selected scratch parent.
if ($workRoot.StartsWith($requiredWorkPrefix, [StringComparison]::OrdinalIgnoreCase) -and [IO.Path]::GetFileName($workRoot).StartsWith('rea-style-reference-')) {
    Remove-Item -LiteralPath $workRoot -Recurse -Force
}
Write-Output "APK: $apkPath"
Write-Output "SHA-256: $($buildInfo.sha256)"
