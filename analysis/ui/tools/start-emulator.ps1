$ErrorActionPreference = 'Stop'
$androidUiRoot = Join-Path $env:USERPROFILE '.rea\toolchains\android'
$env:JAVA_HOME = Join-Path $androidUiRoot 'jdk-21'
$env:ANDROID_SDK_ROOT = Join-Path $androidUiRoot 'sdk'
$env:ANDROID_USER_HOME = Join-Path $androidUiRoot 'android-user'
$env:ANDROID_AVD_HOME = Join-Path $androidUiRoot 'avd'
$env:Path = (Join-Path $env:USERPROFILE 'AppData\Local\Microsoft\WinGet\Packages\Google.PlatformTools_Microsoft.Winget.Source_8wekyb3d8bbwe\platform-tools') + ';' + $env:Path
$androidUiLogRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..\runtime'))
$androidUiEmulator = Join-Path $env:ANDROID_SDK_ROOT 'emulator\emulator.exe'
$androidUiArguments = @('-avd','rea_mihealth_ui_20261015','-port','5580','-no-window','-no-audio','-no-snapshot','-no-boot-anim','-gpu','swiftshader','-memory','4096')
$androidUiProcess = Start-Process -FilePath $androidUiEmulator -ArgumentList $androidUiArguments -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $androidUiLogRoot 'emulator.stdout.log') -RedirectStandardError (Join-Path $androidUiLogRoot 'emulator.stderr.log')
@{ launcherPid = $androidUiProcess.Id; serial = 'emulator-5580'; avd = 'rea_mihealth_ui_20261015'; emulator = $androidUiEmulator; arguments = $androidUiArguments; startedAt = (Get-Date).ToUniversalTime().ToString('o') } | ConvertTo-Json | Set-Content -LiteralPath (Join-Path $androidUiLogRoot 'emulator-process.json') -Encoding UTF8
Write-Output "Headless emulator launched: PID $($androidUiProcess.Id), emulator-5580"
