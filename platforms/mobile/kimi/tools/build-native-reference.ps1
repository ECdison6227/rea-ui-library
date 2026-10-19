param(
  [string]$AndroidToolchain = (Join-Path $env:USERPROFILE '.rea/toolchains/android')
)
$ErrorActionPreference = 'Stop'
[Console]::OutputEncoding = [Text.UTF8Encoding]::new($false)
$taskRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../reference'))
$taskSdk = Join-Path $AndroidToolchain 'sdk'
$taskJdk = Join-Path $AndroidToolchain 'jdk-21'
$taskBuildTools = Join-Path $taskSdk 'build-tools/35.0.0'
$taskAndroidJar = Join-Path $taskSdk 'platforms/android-35/android.jar'
$taskOutput = Join-Path $taskRoot 'build'
$taskPrivate = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../../../.runtime/native-reference-build'))
$env:JAVA_HOME = $taskJdk
$env:PATH = (Join-Path $taskJdk 'bin') + ';' + $env:PATH
foreach ($taskFile in @((Join-Path $taskJdk 'bin/javac.exe'),$taskAndroidJar,(Join-Path $taskBuildTools 'aapt2.exe'),(Join-Path $taskBuildTools 'd8.bat'),(Join-Path $taskBuildTools 'apksigner.bat'))) {
  if (-not (Test-Path -LiteralPath $taskFile)) { throw "Existing tool missing: $taskFile. No automatic tool installation." }
}
foreach ($taskDirectory in @($taskOutput,(Join-Path $taskOutput 'classes'),(Join-Path $taskOutput 'dex'),(Join-Path $taskOutput 'tests'),$taskPrivate)) { New-Item -ItemType Directory -Path $taskDirectory -Force | Out-Null }
$taskSources = @(Get-ChildItem -LiteralPath (Join-Path $taskRoot 'src') -Filter '*.java' -Recurse | ForEach-Object { $_.FullName })
& (Join-Path $taskJdk 'bin/javac.exe') -encoding UTF-8 --release 8 -classpath $taskAndroidJar -d (Join-Path $taskOutput 'classes') @taskSources
if ($LASTEXITCODE -ne 0) { throw 'Native Java compilation failed' }
& (Join-Path $taskJdk 'bin/jar.exe') cf (Join-Path $taskOutput 'classes.jar') -C (Join-Path $taskOutput 'classes') .
if ($LASTEXITCODE -ne 0) { throw 'Class archive creation failed' }
& (Join-Path $taskBuildTools 'd8.bat') --lib $taskAndroidJar --min-api 26 --output (Join-Path $taskOutput 'dex') (Join-Path $taskOutput 'classes.jar')
if ($LASTEXITCODE -ne 0) { throw 'DEX compilation failed' }
& (Join-Path $taskBuildTools 'aapt2.exe') link -I $taskAndroidJar --manifest (Join-Path $taskRoot 'AndroidManifest.xml') -o (Join-Path $taskOutput 'native-reference-unsigned.apk')
if ($LASTEXITCODE -ne 0) { throw 'Manifest packaging failed' }
& (Join-Path $taskJdk 'bin/jar.exe') uf (Join-Path $taskOutput 'native-reference-unsigned.apk') -C (Join-Path $taskOutput 'dex') classes.dex
if ($LASTEXITCODE -ne 0) { throw 'DEX packaging failed' }
& (Join-Path $taskBuildTools 'zipalign.exe') -f 4 (Join-Path $taskOutput 'native-reference-unsigned.apk') (Join-Path $taskOutput 'native-reference-aligned.apk')
if ($LASTEXITCODE -ne 0) { throw 'APK alignment failed' }
$taskKeystore = Join-Path $taskPrivate 'debug.keystore'
if (-not (Test-Path -LiteralPath $taskKeystore)) {
  & (Join-Path $taskJdk 'bin/keytool.exe') -genkeypair -keystore $taskKeystore -storepass android -keypass android -alias androiddebugkey -dname 'CN=Independent Local Research,O=Local Research,C=ZZ' -keyalg RSA -keysize 2048 -validity 3650
  if ($LASTEXITCODE -ne 0) { throw 'Local debug key creation failed' }
}
& (Join-Path $taskBuildTools 'apksigner.bat') sign --ks $taskKeystore --ks-pass pass:android --key-pass pass:android --out (Join-Path $taskOutput 'native-reference.apk') (Join-Path $taskOutput 'native-reference-aligned.apk')
if ($LASTEXITCODE -ne 0) { throw 'APK signing failed' }
$taskSignature = & (Join-Path $taskBuildTools 'apksigner.bat') verify --verbose (Join-Path $taskOutput 'native-reference.apk')
if ($LASTEXITCODE -ne 0) { throw 'Signature validation failed' }
$taskModelSource = Join-Path $taskRoot 'src/org/research/kimistyle/ReferenceState.java'
$taskTestSource = Join-Path $taskRoot 'test/org/research/kimistyle/ReferenceStateTest.java'
& (Join-Path $taskJdk 'bin/javac.exe') -encoding UTF-8 -d (Join-Path $taskOutput 'tests') $taskModelSource $taskTestSource
if ($LASTEXITCODE -ne 0) { throw 'State test compilation failed' }
$taskTestResult = & (Join-Path $taskJdk 'bin/java.exe') -cp (Join-Path $taskOutput 'tests') org.research.kimistyle.ReferenceStateTest
if ($LASTEXITCODE -ne 0) { throw 'State invariant test failed' }
$taskRecord = [ordered]@{
  schemaVersion = 1
  builtAt = [DateTime]::UtcNow.ToString('o')
  classification = 'D'
  package = 'org.research.kimistyle'
  implementation = 'Java Android View; no WebView; no external dependencies'
  compileSdk = 35
  targetSdk = 34
  minSdk = 26
  apk = 'reference/build/native-reference.apk'
  sha256 = (Get-FileHash -Algorithm SHA256 -LiteralPath (Join-Path $taskOutput 'native-reference.apk')).Hash.ToLowerInvariant()
  bytes = (Get-Item -LiteralPath (Join-Path $taskOutput 'native-reference.apk')).Length
  stateTests = $taskTestResult
  signatureVerification = $taskSignature
  runtimeValidation = 'Not executed by build script. Device controller must capture actual behavior separately.'
}
$taskRecord | ConvertTo-Json -Depth 5 | Set-Content -LiteralPath (Join-Path $taskOutput 'build-result.json') -Encoding UTF8
$taskRecord | ConvertTo-Json -Depth 5
