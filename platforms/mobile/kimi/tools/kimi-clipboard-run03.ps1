param([Parameter(Mandatory=$true)][string]$Text)
$ErrorActionPreference='Stop'
[Console]::OutputEncoding=[Text.UTF8Encoding]::new($false)
$taskOutput=& node (Join-Path $PSScriptRoot 'set-run03-clipboard.mjs') $Text
if($LASTEXITCODE -ne 0){Write-Output $taskOutput;throw 'Guest research clipboard helper failed; stop dependent input'}
$taskResult=($taskOutput -join [Environment]::NewLine)|ConvertFrom-Json
if($taskResult.status -ne 'written' -or -not $taskResult.returnedToPreviousActivity){throw 'Guest helper did not verify writing and return'}
@{status=$taskResult.status;characters=$taskResult.characters;fixtureSha256=$taskResult.fixtureSha256;returnedToPreviousActivity=$taskResult.returnedToPreviousActivity;evidence=$taskResult.evidence}|ConvertTo-Json -Compress
