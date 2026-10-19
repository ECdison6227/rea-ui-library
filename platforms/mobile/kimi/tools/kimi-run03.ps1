$ErrorActionPreference='Stop'
[Console]::OutputEncoding=[Text.UTF8Encoding]::new($false)
$env:KIMI_RUN_ID='run03-20261016'
$env:KIMI_ADB_SERIAL='emulator-5596'
$env:KIMI_AVD_NAME='rea_kimi_ui_20261015'
$env:KIMI_CAPTURE_SETTLE_MS='1500'
& node (Join-Path $PSScriptRoot 'android-session.mjs') @args
if($LASTEXITCODE -ne 0){throw 'Kimi run03 action failed; inspect before dependent actions'}
