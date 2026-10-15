$ErrorActionPreference = 'Stop'
$androidUiRoot = Join-Path $env:USERPROFILE '.rea\toolchains\android'
$androidUiSdkRoot = Join-Path $androidUiRoot 'sdk'
$env:JAVA_HOME = Join-Path $androidUiRoot 'jdk-21'
$env:ANDROID_SDK_ROOT = $androidUiSdkRoot
$androidUiManager = Join-Path $androidUiSdkRoot 'cmdline-tools\latest\bin\sdkmanager.bat'
$androidUiLog = Join-Path $PSScriptRoot '..\runtime\sdk-install.log'
$androidUiOldErrorPreference = $ErrorActionPreference
$ErrorActionPreference = 'Continue'
1..20 | ForEach-Object { 'y' } | & $androidUiManager "--sdk_root=$androidUiSdkRoot" 'emulator' 'system-images;android-35;google_apis;x86_64' > $androidUiLog 2>&1
$androidUiExitCode = $LASTEXITCODE
$ErrorActionPreference = $androidUiOldErrorPreference
@{ exitCode = $androidUiExitCode; sdkRoot = $androidUiSdkRoot; packages = @('emulator','system-images;android-35;google_apis;x86_64') } | ConvertTo-Json | Set-Content -LiteralPath (Join-Path $PSScriptRoot '..\runtime\sdk-install-result.json') -Encoding UTF8
exit $androidUiExitCode
