$ErrorActionPreference='Stop'
[Console]::OutputEncoding=[Text.UTF8Encoding]::new($false)
$env:KIMI_RUN_ID='run02-20261016'
$env:KIMI_CAPTURE_SETTLE_MS='2000'
& node (Join-Path $PSScriptRoot 'android-session.mjs') @args
if($LASTEXITCODE -ne 0){throw 'Kimi research action failed; stop dependent actions'}
