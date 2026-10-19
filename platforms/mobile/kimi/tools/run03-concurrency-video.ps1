$ErrorActionPreference='Stop'
[Console]::OutputEncoding=[Text.UTF8Encoding]::new($false)
$taskRoot=[IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$taskAdb=(Get-Command adb).Source
$taskSerial='emulator-5596'
$taskAvd=(& $taskAdb -s $taskSerial shell getprop ro.boot.qemu.avd_name).Trim()
if($taskAvd -cne 'rea_kimi_ui_20261015'){throw 'Wrong AVD'}
$taskUi=(& (Join-Path $PSScriptRoot 'kimi-run03.ps1') ui-brief) -join "`n"
if($taskUi -notmatch '\(499, 502\)' -or $taskUi -notmatch '"desc":"Retry"' -or $taskUi -match '"class":"android.widget.EditText"'){throw 'Require visible completed own 500-pair answer and compact empty composer'}
$taskDestination=Join-Path $taskRoot 'evidence/runtime/run03-20261016/motion/concurrency-01'
if(Test-Path -LiteralPath $taskDestination){throw 'Video ID exists'}
New-Item -ItemType Directory -Path $taskDestination|Out-Null
$taskRemote='/sdcard/run03-concurrency-01.mp4'
$taskStart=[DateTime]::UtcNow.ToString('o')
$taskVideo=Start-Process -FilePath $taskAdb -ArgumentList @('-s',$taskSerial,'shell','screenrecord','--time-limit','18',$taskRemote) -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $taskDestination 'screenrecord.stdout.log') -RedirectStandardError (Join-Path $taskDestination 'screenrecord.stderr.log')
$taskStop='not-attempted'
try{
 Start-Sleep -Milliseconds 500
 & (Join-Path $PSScriptRoot 'kimi-run03.ps1') tap-last-label Retry 'Retry only current own completed CONCUR-RUN03-8941 answer during video'
 & (Join-Path $PSScriptRoot 'kimi-run03.ps1') tap 450 2180 'Focus composer immediately after Retry'
 & (Join-Path $PSScriptRoot 'kimi-run03.ps1') key KEYCODE_PASTE 'Guest clipboard contains only prewritten own queued D fixture'
 & (Join-Path $PSScriptRoot 'kimi-run03.ps1') submit-exact 'Research Run03 queued D. Reply only QUEUE-RUN03-9072. No tools.'
 & (Join-Path $PSScriptRoot 'kimi-run03.ps1') capture r03-192-concurrency-video 'Continuous video covers Retry, second draft and exact Send without model reasoning-label assumption'
 try{& (Join-Path $PSScriptRoot 'kimi-run03.ps1') stop-generation;$taskStop='checked-stop-executed'}catch{$taskStop='fresh-stop-guard-refused; no stop tap'}
 & (Join-Path $PSScriptRoot 'kimi-run03.ps1') capture r03-193-concurrency-after-stop 'Actual result after safe checked Stop attempt; no backend cancellation inference'
}finally{
 if(-not $taskVideo.HasExited){$taskVideo.WaitForExit(30000)|Out-Null}
 if(-not $taskVideo.HasExited){throw 'Recorder still running; inspect owned process'}
 & $taskAdb -s $taskSerial pull $taskRemote (Join-Path $taskDestination 'screenrecord.mp4')|Out-Null
 if($LASTEXITCODE -ne 0){throw 'Video pull failed'}
 $taskFile=Get-Item -LiteralPath (Join-Path $taskDestination 'screenrecord.mp4')
 $taskRecord=[ordered]@{classification='A';runId='run03-20261016';serial=$taskSerial;avd=$taskAvd;package='com.moonshot.kimichat';startedAt=$taskStart;completedAt=[DateTime]::UtcNow.ToString('o');method='Original app Android screenrecord; source wrapper action log; actual original backend';video='evidence/runtime/run03-20261016/motion/concurrency-01/screenrecord.mp4';bytes=$taskFile.Length;sha256=(Get-FileHash -LiteralPath $taskFile.FullName -Algorithm SHA256).Hash.ToLowerInvariant();stopAttempt=$taskStop;privacyReview='pending full video review';timebase='MP4 media PTS must be decoded independently; host time is not animation duration';scope='Own CONCUR request retry plus exact queued D only; no external transmission beyond test requests'}
 [IO.File]::WriteAllText((Join-Path $taskDestination 'capture.json'),($taskRecord|ConvertTo-Json -Depth 5),[Text.UTF8Encoding]::new($false))
 $taskRecord|ConvertTo-Json -Depth 5
}
