param(
  [string]$AndroidToolchain = (Join-Path $env:USERPROFILE '.rea/toolchains/android'),
  [string]$StateDirectory = (Join-Path $PSScriptRoot '../../../.runtime'),
  [int]$Port = 5596,
  [string]$AvdName = 'rea_kimi_ui_20261015'
)
$ErrorActionPreference = 'Stop'
[Console]::OutputEncoding = [Text.UTF8Encoding]::new($false)
$env:JAVA_HOME = Join-Path $AndroidToolchain 'jdk-21'
$env:ANDROID_SDK_ROOT = Join-Path $AndroidToolchain 'sdk'
$env:ANDROID_USER_HOME = [IO.Path]::GetFullPath((Join-Path $StateDirectory 'android-user'))
$env:ANDROID_AVD_HOME = [IO.Path]::GetFullPath((Join-Path $StateDirectory 'avd'))
foreach ($taskDir in @($env:ANDROID_USER_HOME,$env:ANDROID_AVD_HOME)) { New-Item -ItemType Directory -Path $taskDir -Force | Out-Null }
$taskEmulator = Join-Path $env:ANDROID_SDK_ROOT 'emulator/emulator.exe'
if (-not (Test-Path -LiteralPath $taskEmulator)) { throw 'Existing emulator not found. Supply -AndroidToolchain pointing to the retained local SDK/JDK; no automatic installation.' }
if (Get-NetTCPConnection -LocalPort $Port,($Port+1) -State Listen -ErrorAction SilentlyContinue) { throw 'Requested emulator port is occupied. Choose a different even port; never reuse another research instance.' }
$taskAvdIni = Join-Path $env:ANDROID_AVD_HOME ($AvdName+'.ini')
if (-not (Test-Path -LiteralPath $taskAvdIni)) {
  'no' | & (Join-Path $env:ANDROID_SDK_ROOT 'cmdline-tools/latest/bin/avdmanager.bat') create avd -n $AvdName -k 'system-images;android-35;google_apis;x86_64' -d pixel_7
  if ($LASTEXITCODE -ne 0 -or -not (Test-Path -LiteralPath $taskAvdIni)) { throw 'AVD creation failed. Check the existing API35 image; do not enable Windows features or modify another AVD.' }
}
$taskLogRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../evidence/runtime'))
$taskTimestamp = [DateTime]::UtcNow.ToString('yyyyMMddTHHmmssZ')
$taskArgs = @('-avd',$AvdName,'-port',"$Port",'-no-window','-no-audio','-no-snapshot','-no-boot-anim','-gpu','swiftshader','-memory','3072')
$taskProcess = Start-Process -FilePath $taskEmulator -ArgumentList $taskArgs -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $taskLogRoot "emulator-$taskTimestamp.stdout.log") -RedirectStandardError (Join-Path $taskLogRoot "emulator-$taskTimestamp.stderr.log")
$taskRecord = @{ startedAt=[DateTime]::UtcNow.ToString('o'); launcherPid=$taskProcess.Id; avd=$AvdName; serial="emulator-$Port"; arguments=$taskArgs; androidToolchain=$AndroidToolchain; avdHome=$env:ANDROID_AVD_HOME }
$taskRecord | ConvertTo-Json -Depth 5 | Set-Content -LiteralPath (Join-Path $taskLogRoot "launch-$taskTimestamp.json") -Encoding UTF8
$taskRecord | ConvertTo-Json -Depth 5
