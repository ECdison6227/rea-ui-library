$ErrorActionPreference = 'Stop'
$androidUiRoot = Join-Path $env:USERPROFILE '.rea\toolchains\android'
$env:JAVA_HOME = Join-Path $androidUiRoot 'jdk-21'
$env:ANDROID_SDK_ROOT = Join-Path $androidUiRoot 'sdk'
$env:ANDROID_USER_HOME = Join-Path $androidUiRoot 'android-user'
$env:ANDROID_AVD_HOME = Join-Path $androidUiRoot 'avd'
New-Item -ItemType Directory -Path $env:ANDROID_USER_HOME,$env:ANDROID_AVD_HOME -Force | Out-Null
$androidUiAvdName = 'rea_mihealth_ui_20261015'
$androidUiAvdPath = Join-Path $env:ANDROID_AVD_HOME ($androidUiAvdName + '.avd')
if (-not (Test-Path -LiteralPath (Join-Path $androidUiAvdPath 'config.ini'))) {
    $ErrorActionPreference = 'Continue'
    'no' | & (Join-Path $env:ANDROID_SDK_ROOT 'cmdline-tools\latest\bin\avdmanager.bat') create avd --name $androidUiAvdName --package 'system-images;android-35;google_apis;x86_64' --device 'pixel_7' --path $androidUiAvdPath *> (Join-Path $PSScriptRoot '..\runtime\avd-create.log')
    $androidUiAvdExit = $LASTEXITCODE
    $ErrorActionPreference = 'Stop'
    if ($androidUiAvdExit -ne 0) { throw "avdmanager exited $androidUiAvdExit" }
}
$androidUiConfig = Join-Path $androidUiAvdPath 'config.ini'
$androidUiValues = [ordered]@{}
foreach ($androidUiLine in Get-Content -LiteralPath $androidUiConfig) {
    if ($androidUiLine -match '^([^=]+)=(.*)$') { $androidUiValues[$Matches[1].Trim()] = $Matches[2].Trim() }
}
$androidUiValues['hw.lcd.width'] = '1080'
$androidUiValues['hw.lcd.height'] = '2400'
$androidUiValues['hw.lcd.density'] = '440'
$androidUiValues['hw.ramSize'] = '4096'
$androidUiValues['hw.cpu.ncore'] = '4'
$androidUiValues['hw.keyboard'] = 'yes'
$androidUiValues['showDeviceFrame'] = 'no'
$androidUiValues['hw.gpu.enabled'] = 'yes'
$androidUiValues['hw.gpu.mode'] = 'swiftshader'
$androidUiValues['skin.dynamic'] = 'yes'
$androidUiValues.GetEnumerator() | ForEach-Object { '{0}={1}' -f $_.Key,$_.Value } | Set-Content -LiteralPath $androidUiConfig -Encoding ASCII
@{ name = $androidUiAvdName; path = $androidUiAvdPath; sdkRoot = $env:ANDROID_SDK_ROOT; userHome = $env:ANDROID_USER_HOME; avdHome = $env:ANDROID_AVD_HOME; width = 1080; height = 2400; densityDpi = 440; api = 35 } | ConvertTo-Json | Set-Content -LiteralPath (Join-Path $PSScriptRoot '..\runtime\avd.json') -Encoding UTF8
Write-Output "AVD ready: $androidUiAvdName"
