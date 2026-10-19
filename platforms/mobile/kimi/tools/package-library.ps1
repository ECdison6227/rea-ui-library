param([string]$OutputPath,[switch]$CheckOnly)
$ErrorActionPreference='Stop'
[Console]::OutputEncoding=[Text.UTF8Encoding]::new($false)
$taskRoot=[IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$taskRunId='run03-20261016'
$taskRunPrefix='evidence/runtime/'+$taskRunId
$taskSafeFile='docs/run03-safe-scenes.json'
$taskAuditFile='docs/run03-evidence-audit.json'
$taskReferenceSafeFile='docs/native-reference-safe-scenes.json'
$taskMotionSafeFile='docs/run03-motion-safe-files.json'
$taskNativeApk='reference/build/native-reference.apk'
$taskAllowedExact=[Collections.Generic.Dictionary[string,string]]::new([StringComparer]::Ordinal)
$taskAllowedRun03Ids=[Collections.Generic.HashSet[string]]::new([StringComparer]::Ordinal)
$taskAllowedReferenceIds=[Collections.Generic.HashSet[string]]::new([StringComparer]::Ordinal)
$taskAllowedMotionIds=[Collections.Generic.HashSet[string]]::new([StringComparer]::Ordinal)
$taskAllowedMotionFiles=[Collections.Generic.HashSet[string]]::new([StringComparer]::Ordinal)
$taskMotionSafeSha=$null

function Resolve-TaskPath([string]$Relative){
 if([string]::IsNullOrWhiteSpace($Relative) -or $Relative.Contains([char]92) -or $Relative -match '(^|/)\.\.(/|$)|^[\\/]|:'){throw 'Unsafe relative distribution path'}
 $taskResolved=[IO.Path]::GetFullPath((Join-Path $taskRoot $Relative))
 if(-not $taskResolved.StartsWith($taskRoot+[IO.Path]::DirectorySeparatorChar,[StringComparison]::OrdinalIgnoreCase)){throw 'Distribution source escapes research root'}
 return $taskResolved
}
function Read-TaskJson([string]$Relative){return ([IO.File]::ReadAllText((Resolve-TaskPath $Relative))|ConvertFrom-Json)}
function Get-TaskHash([string]$Relative){return (Get-FileHash -LiteralPath (Resolve-TaskPath $Relative) -Algorithm SHA256).Hash.ToLowerInvariant()}
function Add-ExactScene($Entry,[string]$Prefix,[string]$Classification){
 $taskId=[string]$Entry.id
 if($taskId -notmatch '^[a-z0-9][a-z0-9-]{0,95}$' -or $taskId -match 'private'){throw 'Invalid/private scene in distribution allowlist'}
 foreach($taskKind in @('screenshot','hierarchy','capture')){
  $taskName=switch($taskKind){'screenshot'{'screen.png'} 'hierarchy'{'hierarchy.xml'} 'capture'{'capture.json'}}
  $taskExpectedPath=$Prefix+'/scenes/'+$taskId+'/'+$taskName
  $taskPart=$Entry.$taskKind
  if($taskPart.path -cne $taskExpectedPath -or $taskPart.sha256 -notmatch '^[a-f0-9]{64}$'){throw "Allowlist lacks exact path/hash: $taskId $taskKind"}
  if((Get-TaskHash $taskExpectedPath) -cne $taskPart.sha256){throw "Reviewed bytes changed: $taskId $taskKind"}
  if($taskAllowedExact.ContainsKey($taskExpectedPath)){throw 'Duplicate exact distribution path'}
  $taskAllowedExact.Add($taskExpectedPath,[string]$taskPart.sha256)
 }
 $taskCapture=Read-TaskJson ($Prefix+'/scenes/'+$taskId+'/capture.json')
 $taskExpectedPackage=if($Classification -eq 'A'){'com.moonshot.kimichat'}else{'org.research.kimistyle'}
 if($taskCapture.id -cne $taskId -or $taskCapture.classification -cne $Classification -or $taskCapture.package -cne $taskExpectedPackage -or $taskCapture.atomicCapture -ne $false){throw 'Reviewed capture identity/classification differs'}
}

# Run03 source pixels/UIA/captures enter only through the exact offline audit.
$taskSafe=Read-TaskJson $taskSafeFile
$taskAudit=Read-TaskJson $taskAuditFile
$taskSafeSha=Get-TaskHash $taskSafeFile
$taskAuditSha=Get-TaskHash $taskAuditFile
if($taskSafe.runId -cne $taskRunId -or $taskAudit.runId -cne $taskRunId){throw 'Wrong run03 review identity'}
if($taskSafe.auditSource -cne $taskAuditFile -or $taskSafe.auditSourceSha256 -cne $taskAuditSha){throw 'Stale run03 allowlist: refresh the offline evidence audit first'}
$taskAllowedExact.Add($taskSafeFile,$taskSafeSha)
$taskAllowedExact.Add($taskAuditFile,$taskAuditSha)
if(@($taskSafe.integrityFailures).Count -gt 0){throw 'Run03 allowlist reports source integrity failures'}
foreach($taskScene in $taskSafe.scenes){
 $taskId=[string]$taskScene.id
 $taskNotes=@($taskAudit.scenes|Where-Object{$_.id -ceq $taskId})
 if($taskNotes.Count -ne 1){throw 'Source scene has no unique independent review'}
 $taskNote=$taskNotes[0]
 if($taskNote.pixelReview -cne 'reviewed' -or $taskNote.uiaReview -cne 'reviewed' -or $taskNote.captureReview -cne 'reviewed' -or $taskNote.privacy.status -cne 'clear' -or $taskNote.distribution -cne 'allow'){throw 'Source scene is unreviewed or private'}
 if($taskScene.screenshot.sha256 -cne $taskNote.exactScreenshotSha256 -or $taskScene.hierarchy.sha256 -cne $taskNote.exactHierarchySha256 -or $taskScene.capture.sha256 -cne $taskNote.exactCaptureSha256){throw 'Run03 allowlist differs from reviewed exact bytes'}
 if(-not $taskAllowedRun03Ids.Add($taskId)){throw 'Duplicate safe run03 ID'}
 Add-ExactScene $taskScene $taskRunPrefix 'A'
}
if($taskAllowedRun03Ids.Count -ne @($taskSafe.safeSceneIds).Count){throw 'Safe run03 ID list differs from exact scene entries'}
foreach($taskId in $taskSafe.safeSceneIds){if(-not $taskAllowedRun03Ids.Contains([string]$taskId)){throw 'Safe ID has no exact file entries'}}

# Optional D allowlist: {runId,classification:"D",scenes:[{id,
# pixelReview:"reviewed",privacyReview:"clear",screenshot:{path,sha256},
# hierarchy:{path,sha256},capture:{path,sha256}}]}.
# Without it all reference scene files remain excluded.
if(Test-Path -LiteralPath (Resolve-TaskPath $taskReferenceSafeFile)){
 $taskReferenceSafe=Read-TaskJson $taskReferenceSafeFile
 $taskAllowedExact.Add($taskReferenceSafeFile,(Get-TaskHash $taskReferenceSafeFile))
 if($taskReferenceSafe.runId -cne $taskRunId -or $taskReferenceSafe.classification -cne 'D'){throw 'Wrong independent reference allowlist identity'}
 foreach($taskScene in $taskReferenceSafe.scenes){
  if($taskScene.pixelReview -cne 'reviewed' -or $taskScene.privacyReview -cne 'clear'){throw 'Independent reference scene is unreviewed'}
  if(-not $taskAllowedReferenceIds.Add([string]$taskScene.id)){throw 'Duplicate reviewed reference ID'}
  Add-ExactScene $taskScene ('evidence/reference/'+$taskRunId) 'D'
 }
}
# Motion is a separate exact-byte gate, never an extension of source scene IDs.
if(Test-Path -LiteralPath (Resolve-TaskPath $taskMotionSafeFile)){
 $taskMotionSafe=Read-TaskJson $taskMotionSafeFile
 if($taskMotionSafe.schemaVersion -ne 1 -or $taskMotionSafe.runId -cne $taskRunId -or $taskMotionSafe.package -cne 'com.moonshot.kimichat' -or $taskMotionSafe.defaultAllow -ne $false){throw 'Wrong/default-allow motion distribution identity'}
 $taskMotionSafeSha=Get-TaskHash $taskMotionSafeFile
 $taskAllowedExact.Add($taskMotionSafeFile,$taskMotionSafeSha)
 if($taskMotionSafe.researchIndex.path -cne 'docs/run03-motion-research.md' -or $taskMotionSafe.researchIndex.sha256 -cne (Get-TaskHash 'docs/run03-motion-research.md')){throw 'Motion research index differs from the reviewed manifest'}
 $taskAllowedExact.Add('docs/run03-motion-research.md',[string]$taskMotionSafe.researchIndex.sha256)
 foreach($taskRecording in $taskMotionSafe.recordings){
  if($taskRecording.id -notmatch '^[a-z0-9][a-z0-9-]{0,79}$' -or $taskRecording.id -match 'private' -or $taskRecording.classification -cne 'A' -or $taskRecording.package -cne 'com.moonshot.kimichat' -or $taskRecording.privacyReview -cne 'clear' -or $taskRecording.distribution -cne 'allow-reviewed-exact-files' -or $taskRecording.allDecodedFramesReviewed -ne $true){throw 'Unreviewed/private motion recording in allowlist'}
  if(-not $taskAllowedMotionIds.Add([string]$taskRecording.id)){throw 'Duplicate reviewed motion ID'}
 }
 foreach($taskMotionFile in $taskMotionSafe.files){
  $taskMotionId=[string]$taskMotionFile.recordingId
  $taskMotionBase=$taskRunPrefix+'/motion/'+$taskMotionId
  $taskMotionPattern='^'+[regex]::Escape($taskMotionBase)+'/(screenrecord\.mp4|capture\.json|review\.json|guard\.png|guard\.xml|browser-decoded/frame-index\.json|browser-decoded/frame-\d{4}\.png|review-sheets/sheet-\d+\.png)$'
  if(-not $taskAllowedMotionIds.Contains($taskMotionId) -or $taskMotionFile.path -cnotmatch $taskMotionPattern -or $taskMotionFile.sha256 -notmatch '^[a-f0-9]{64}$' -or $taskMotionFile.reviewStatus -cne 'reviewed' -or $taskMotionFile.privacyReview -cne 'clear'){throw 'Motion allowlist contains a new, unsafe or non-exact path'}
  $taskMotionInfo=Get-Item -LiteralPath (Resolve-TaskPath $taskMotionFile.path)
  if($taskMotionInfo.Attributes -band [IO.FileAttributes]::ReparsePoint){throw 'Motion allowlist source is a reparse point'}
  if($taskMotionInfo.Length -ne $taskMotionFile.bytes -or (Get-TaskHash $taskMotionFile.path) -cne $taskMotionFile.sha256){throw 'Reviewed motion bytes changed'}
  if(-not $taskAllowedMotionFiles.Add([string]$taskMotionFile.path)){throw 'Duplicate reviewed motion file'}
  $taskAllowedExact.Add([string]$taskMotionFile.path,[string]$taskMotionFile.sha256)
 }
 if($taskAllowedMotionIds.Count -ne $taskMotionSafe.counts.recordings -or $taskAllowedMotionFiles.Count -ne $taskMotionSafe.counts.files){throw 'Motion allowlist counts differ'}
 foreach($taskRecording in $taskMotionSafe.recordings){
  $taskMotionBase=$taskRunPrefix+'/motion/'+$taskRecording.id
  foreach($taskPartName in @('video','capture','review','frameIndex')){
   $taskFileName=switch($taskPartName){'video'{'screenrecord.mp4'} 'capture'{'capture.json'} 'review'{'review.json'} 'frameIndex'{'browser-decoded/frame-index.json'}}
   $taskPart=$taskRecording.$taskPartName;$taskExpectedPath=$taskMotionBase+'/'+$taskFileName
   if($taskPart.path -cne $taskExpectedPath -or -not $taskAllowedMotionFiles.Contains($taskExpectedPath) -or $taskPart.sha256 -cne $taskAllowedExact[$taskExpectedPath]){throw 'Motion recording role is not bound to one exact approved file'}
  }
  $taskMotionCapture=Read-TaskJson ($taskMotionBase+'/capture.json')
  $taskMotionReview=Read-TaskJson ($taskMotionBase+'/review.json')
  $taskMotionIndex=Read-TaskJson ($taskMotionBase+'/browser-decoded/frame-index.json')
  $taskRegisteredVideoSha=if($taskMotionCapture.video -is [string]){$taskMotionCapture.sha256}else{$taskMotionCapture.video.sha256}
  if($taskMotionCapture.classification -cne 'A' -or $taskMotionCapture.package -cne 'com.moonshot.kimichat' -or $taskMotionCapture.runId -cne $taskRunId -or $taskMotionCapture.serial -cne 'emulator-5596' -or $taskMotionCapture.avd -cne 'rea_kimi_ui_20261015' -or $taskRegisteredVideoSha -cne $taskRecording.video.sha256){throw 'Motion original capture/video identity differs'}
  if($taskMotionReview.id -cne $taskRecording.id -or $taskMotionReview.distribution.status -cne 'allow-reviewed-exact-files' -or $taskMotionReview.privacy.distributionReview -cne 'clear' -or $taskMotionReview.pixelReview.everyDecodedFrameReviewedInContactSheets -ne $true -or $taskMotionReview.source.sha256 -cne $taskRecording.video.sha256 -or $taskMotionReview.source.captureSha256 -cne $taskRecording.capture.sha256 -or $taskMotionReview.source.frameIndexSha256 -cne $taskRecording.frameIndex.sha256){throw 'Motion review is incomplete or bound to different source bytes'}
  if($taskMotionIndex.source.sha256 -cne $taskRecording.video.sha256 -or $taskMotionIndex.source.originalRuntimeMetadataMatches -ne $true -or $taskMotionIndex.demux.sampleCount -ne $taskRecording.sourceVideoSamples -or @($taskMotionIndex.frames).Count -ne $taskRecording.decodedFrames -or $taskRecording.sourceVideoSamples -ne $taskRecording.decodedFrames){throw 'Motion source samples/decoded frames differ'}
  foreach($taskFrame in $taskMotionIndex.frames){
   $taskFramePath=$taskMotionBase+'/browser-decoded/'+$taskFrame.file
   if(-not $taskAllowedMotionFiles.Contains($taskFramePath) -or $taskAllowedExact[$taskFramePath] -cne $taskFrame.sha256){throw 'Decoded motion frame lacks exact approval'}
  }
  foreach($taskSheet in $taskMotionReview.pixelReview.contactSheets){if(-not $taskAllowedMotionFiles.Contains([string]$taskSheet.path) -or $taskAllowedExact[[string]$taskSheet.path] -cne $taskSheet.sha256){throw 'Reviewed motion contact sheet differs'}}
  if($taskMotionCapture.guardArtifacts){
   if($taskMotionReview.guardReview.status -cne 'reviewed' -or $taskMotionReview.guardReview.privacyReview -cne 'clear'){throw 'Motion source guard is not reviewed'}
   foreach($taskGuardKind in @('screenshot','hierarchy')){
    $taskGuardName=if($taskGuardKind -eq 'screenshot'){'guard.png'}else{'guard.xml'};$taskGuardPath=$taskMotionBase+'/'+$taskGuardName
    if(-not $taskAllowedMotionFiles.Contains($taskGuardPath) -or $taskAllowedExact[$taskGuardPath] -cne $taskMotionCapture.guardArtifacts.($taskGuardKind+'Sha256') -or $taskMotionReview.guardReview.($taskGuardKind+'Sha256') -cne $taskAllowedExact[$taskGuardPath]){throw 'Motion guard exact bytes differ'}
   }
  }
 }
}
$taskBuild=Read-TaskJson 'reference/build/build-result.json'
$taskNativeSha=Get-TaskHash $taskNativeApk
if($taskBuild.classification -cne 'D' -or $taskBuild.package -cne 'org.research.kimistyle' -or $taskBuild.apk -cne $taskNativeApk -or $taskBuild.sha256 -cne $taskNativeSha){throw 'Final reference APK differs from its build record'}
$taskAllowedExact.Add($taskNativeApk,$taskNativeSha)
$taskAllowedExact.Add('reference/build/build-result.json',(Get-TaskHash 'reference/build/build-result.json'))

# Any generated run03 preview must bind to the same reviewed exact bytes.
if(Test-Path -LiteralPath (Resolve-TaskPath 'run03.html')){
 $taskGallery=Read-TaskJson ($taskRunPrefix+'/gallery-verification.json')
 if($taskGallery.runId -cne $taskRunId -or $taskGallery.unknownScenesIncluded -ne $false -or $taskGallery.htmlSha256 -cne (Get-TaskHash 'run03.html')){throw 'Run03 gallery lacks an exact safe build verification'}
 $taskAllowedExact.Add('run03.html',[string]$taskGallery.htmlSha256)
 $taskAllowedExact.Add(($taskRunPrefix+'/gallery-verification.json'),(Get-TaskHash ($taskRunPrefix+'/gallery-verification.json')))
 foreach($taskEntry in $taskGallery.included){
  $taskId=[string]$taskEntry.id
  if(-not $taskAllowedRun03Ids.Contains($taskId)){throw 'Run03 gallery contains an unreviewed/private scene'}
  foreach($taskKind in @('screenshot','hierarchy','capture')){
   $taskName=switch($taskKind){'screenshot'{'screen.png'} 'hierarchy'{'hierarchy.xml'} 'capture'{'capture.json'}}
   $taskExpected=$taskAllowedExact[$taskRunPrefix+'/scenes/'+$taskId+'/'+$taskName]
   if($taskEntry.($taskKind+'Sha256') -cne $taskExpected){throw 'Run03 gallery contains different source bytes'}
  }
 }
}
function Get-DistributionDecision([string]$Relative){
 if($Relative.Contains([char]92) -or $Relative -match '(^|/)\.\.(/|$)|^[\\/]|:'){return 'unsafe-path'}
 if($Relative -match '(^|/)(\.runtime|\.git|\.gradle|node_modules|\.avd|[^/]+\.avd|avd|android-user|\.private)(/|$)'){return 'private-runtime-or-tool-cache'}
 if($Relative -match '\.(img|qcow2|vmdk|vdi|class|dex|jar|keystore|jks|key|pem|pfx|p12|idsig|lock|tmp|bak|log)$'){return 'disk-key-or-temporary-build'}
 if($Relative -match '\.apk$' -and $Relative -cne $taskNativeApk){return 'original-or-other-apk'}
 if($Relative -match '^reference/build/' -and $Relative -cne $taskNativeApk -and $Relative -cne 'reference/build/build-result.json'){return 'temporary-native-build'}
 if($Relative -match '^evidence/runtime/run02-20261016/scenes/r02-(18-post-login-guide|19-prompt-ready)/'){return 'run02-private-scene'}
 if($Relative.StartsWith($taskRunPrefix+'/scenes/',[StringComparison]::Ordinal)){
  if($taskAllowedExact.ContainsKey($Relative)){return 'allow'}else{return 'run03-not-exactly-reviewed'}
 }
 if($Relative -match '^evidence/reference/[^/]+/scenes/'){
  if($taskAllowedExact.ContainsKey($Relative)){return 'allow'}else{return 'reference-not-exactly-reviewed'}
 }
 if($Relative.StartsWith($taskRunPrefix+'/motion/',[StringComparison]::Ordinal)){
  if($taskAllowedMotionFiles.Contains($Relative)){return 'allow'}else{return 'motion-not-exactly-reviewed'}
 }
 if($Relative -match '^evidence/runtime/run[^/]+/scenes/' -and $Relative -notmatch '^evidence/runtime/run02-20261016/scenes/'){return 'unknown-run-not-reviewed'}
 if($Relative.StartsWith($taskRunPrefix+'/',[StringComparison]::Ordinal) -and $Relative -notmatch ('^'+[regex]::Escape($taskRunPrefix)+'/[^/]+\.json$')){return 'unreviewed-run03-side-file'}
 if($Relative -match '^evidence/runtime/(emulator-|launch-)'){return 'local-emulator-launch-debug'}
 return 'allow'
}
$taskIncluded=[Collections.Generic.List[object]]::new()
$taskExcluded=@{}
$taskExcludedRun03Ids=[Collections.Generic.HashSet[string]]::new([StringComparer]::Ordinal)
$taskKnownPrivateRun03Ids=[Collections.Generic.HashSet[string]]::new([StringComparer]::Ordinal)
$taskKnownIncompleteRun03Ids=[Collections.Generic.HashSet[string]]::new([StringComparer]::Ordinal)
$taskKnownWithheldRun03Ids=[Collections.Generic.HashSet[string]]::new([StringComparer]::Ordinal)
foreach($taskWithheldEntry in $taskSafe.withheld){
 [void]$taskKnownWithheldRun03Ids.Add([string]$taskWithheldEntry.id)
 $taskWithheldNote=@($taskAudit.scenes|Where-Object{$_.id -ceq $taskWithheldEntry.id})[0]
 $taskPrivacyStatus=[string]$taskWithheldNote.privacy.status
 $taskIsPrivate=$taskWithheldEntry.id -match 'private' -or $taskPrivacyStatus -match 'identity|history|credentials|personal-avatar|qr-code' -or $taskWithheldNote.privacy.identityVisible -eq $true -or $taskWithheldNote.privacy.privateHistoryVisible -eq $true -or $taskWithheldNote.privacy.credentialsVisible -eq $true
 if($taskIsPrivate){[void]$taskKnownPrivateRun03Ids.Add([string]$taskWithheldEntry.id)}
 elseif($taskPrivacyStatus -match 'incomplete|missing|unavailable' -or [string]$taskWithheldNote.uiaReview -match 'unavailable|missing'){[void]$taskKnownIncompleteRun03Ids.Add([string]$taskWithheldEntry.id)}
}
foreach($taskFile in (Get-ChildItem -LiteralPath $taskRoot -Recurse -File)){
 $taskRelative=$taskFile.FullName.Substring($taskRoot.Length).TrimStart([char]92,[char]47).Replace([char]92,[char]47)
 $taskDecision=if($taskFile.Attributes -band [IO.FileAttributes]::ReparsePoint){'reparse-point'}else{Get-DistributionDecision $taskRelative}
 if($taskDecision -ne 'allow'){
  $taskExcluded[$taskDecision]=1+[int]$taskExcluded[$taskDecision]
  if($taskRelative.StartsWith($taskRunPrefix+'/scenes/',[StringComparison]::Ordinal)){[void]$taskExcludedRun03Ids.Add($taskRelative.Substring(($taskRunPrefix+'/scenes/').Length).Split([char]47)[0])}
  continue
 }
 $taskIncluded.Add([pscustomobject]@{File=$taskFile;Relative=$taskRelative})
}
$taskPrivateRun03Excluded=@($taskExcludedRun03Ids|Where-Object{$taskKnownPrivateRun03Ids.Contains($_) -or $_ -match 'private'}).Count
$taskIncompleteRun03Excluded=@($taskExcludedRun03Ids|Where-Object{$taskKnownIncompleteRun03Ids.Contains($_)}).Count
$taskOtherWithheldRun03Excluded=@($taskExcludedRun03Ids|Where-Object{$taskKnownWithheldRun03Ids.Contains($_) -and -not $taskKnownPrivateRun03Ids.Contains($_) -and -not $taskKnownIncompleteRun03Ids.Contains($_) -and $_ -notmatch 'private'}).Count
$taskWithheldRun03Excluded=$taskPrivateRun03Excluded+$taskIncompleteRun03Excluded+$taskOtherWithheldRun03Excluded
$taskUnreviewedRun03Excluded=$taskExcludedRun03Ids.Count-$taskWithheldRun03Excluded
$taskProbes=[ordered]@{
 originalApkExcluded=((Get-DistributionDecision 'Kimi.apk') -ne 'allow')
 finalReferenceApkAllowed=((Get-DistributionDecision $taskNativeApk) -eq 'allow')
 nativeClassesExcluded=((Get-DistributionDecision 'reference/build/classes/Example.class') -ne 'allow')
 nativeIntermediateApkExcluded=((Get-DistributionDecision 'reference/build/native-reference-unsigned.apk') -ne 'allow')
 runtimeExcluded=((Get-DistributionDecision '.runtime/native-reference-build/debug.keystore') -ne 'allow')
 avdDiskExcluded=((Get-DistributionDecision 'avd/research.avd/userdata-qemu.img') -ne 'allow')
 run02IdentityExcluded=((Get-DistributionDecision 'evidence/runtime/run02-20261016/scenes/r02-18-post-login-guide/screen.png') -ne 'allow')
 run03PrivateExcluded=((Get-DistributionDecision ($taskRunPrefix+'/scenes/r03-05-effort-relaunch-private/screen.png')) -ne 'allow')
 run03UnknownExcluded=((Get-DistributionDecision ($taskRunPrefix+'/scenes/r03-unreviewed-probe/screen.png')) -ne 'allow')
 run03TempUiaExcluded=((Get-DistributionDecision ($taskRunPrefix+'/unreviewed-ui.xml')) -ne 'allow')
 referenceUnknownExcluded=((Get-DistributionDecision 'evidence/reference/run03-20261016/scenes/unreviewed-probe/screen.png') -ne 'allow')
 motionUnknownExcluded=((Get-DistributionDecision ($taskRunPrefix+'/motion/unreviewed-probe/screenrecord.mp4')) -ne 'allow')
 motionLogExcluded=((Get-DistributionDecision ($taskRunPrefix+'/motion/motion-01/debug.log')) -ne 'allow')
 motionHtmlIntermediateExcluded=((Get-DistributionDecision ($taskRunPrefix+'/motion/motion-01/review-sheets/sheet-1.html')) -ne 'allow')
 motionBrowserCacheExcluded=((Get-DistributionDecision ($taskRunPrefix+'/motion/motion-01/browser-profile/Cache/data.bin')) -ne 'allow')
}
if($taskAllowedMotionIds.Count -gt 0){$taskProbes.reviewedMotionVideoAllowed=((Get-DistributionDecision ($taskRunPrefix+'/motion/'+@($taskAllowedMotionIds)[0]+'/screenrecord.mp4')) -eq 'allow')}
if(@($taskProbes.Values|Where-Object{$_ -ne $true}).Count -gt 0){throw 'Distribution policy self-check failed'}
if(@($taskIncluded|Where-Object{$_.Relative -ceq 'README.md'}).Count -ne 1 -or @($taskIncluded|Where-Object{$_.Relative -ceq $taskNativeApk}).Count -ne 1){throw 'Required research entry or final reference APK missing'}
if($CheckOnly){
 [ordered]@{status='checked-no-archive-created';includedFiles=$taskIncluded.Count;reviewedRun03Scenes=$taskAllowedRun03Ids.Count;reviewedReferenceScenes=$taskAllowedReferenceIds.Count;reviewedMotionRecordings=$taskAllowedMotionIds.Count;reviewedMotionFiles=$taskAllowedMotionFiles.Count;motionSafeAllowlistSha256=$taskMotionSafeSha;run03UnreviewedScenesExcluded=$taskUnreviewedRun03Excluded;run03WithheldScenesExcluded=$taskWithheldRun03Excluded;run03PrivateScenesExcluded=$taskPrivateRun03Excluded;run03IncompleteScenesExcluded=$taskIncompleteRun03Excluded;run03OtherWithheldScenesExcluded=$taskOtherWithheldRun03Excluded;allowlistSnapshotPending=@($taskSafe.pending).Count;excludedByReason=$taskExcluded;referenceApkSha256=$taskNativeSha;safeAllowlistSha256=$taskSafeSha;policyProbes=$taskProbes;note='Read-only packaging preflight; no device execution or research acceptance claim'}|ConvertTo-Json -Depth 5
 exit 0
}
if([string]::IsNullOrWhiteSpace($OutputPath)){throw 'Supply a fresh -OutputPath, or use -CheckOnly for read-only preflight'}
$taskOutput=[IO.Path]::GetFullPath($OutputPath)
if(Test-Path -LiteralPath $taskOutput){throw 'Use a fresh archive path'}
$taskManifest=[IO.Path]::ChangeExtension($taskOutput,'.manifest.json')
if(Test-Path -LiteralPath $taskManifest){throw 'Use a fresh archive manifest path'}
if($taskOutput.StartsWith($taskRoot+[IO.Path]::DirectorySeparatorChar,[StringComparison]::OrdinalIgnoreCase)){throw 'Archive must be outside the input tree'}
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem
$taskStream=[IO.File]::Create($taskOutput)
$taskZip=[IO.Compression.ZipArchive]::new($taskStream,[IO.Compression.ZipArchiveMode]::Create,$false)
try{
 foreach($taskItem in $taskIncluded){[IO.Compression.ZipFileExtensions]::CreateEntryFromFile($taskZip,$taskItem.File.FullName,('kimi-android/'+$taskItem.Relative),[IO.Compression.CompressionLevel]::Optimal)|Out-Null}
}finally{$taskZip.Dispose();$taskStream.Dispose()}
$taskZip=[IO.Compression.ZipFile]::OpenRead($taskOutput)
try{
 $taskCount=$taskZip.Entries.Count
 $taskReadmePresent=@($taskZip.Entries|Where-Object{$_.FullName -ceq 'kimi-android/README.md'}).Count -eq 1
 $taskInvalid=$false
 foreach($taskEntry in $taskZip.Entries){
  if(-not $taskEntry.FullName.StartsWith('kimi-android/',[StringComparison]::Ordinal)){$taskInvalid=$true;break}
  $taskRelative=$taskEntry.FullName.Substring(13)
  if((Get-DistributionDecision $taskRelative) -ne 'allow'){$taskInvalid=$true;break}
  if($taskAllowedExact.ContainsKey($taskRelative)){
   $taskEntryStream=$taskEntry.Open();$taskHasher=[Security.Cryptography.SHA256]::Create()
   try{$taskActual=([BitConverter]::ToString($taskHasher.ComputeHash($taskEntryStream))).Replace('-','').ToLowerInvariant()}finally{$taskHasher.Dispose();$taskEntryStream.Dispose()}
   if($taskActual -cne $taskAllowedExact[$taskRelative]){$taskInvalid=$true;break}
  }
 }
}finally{$taskZip.Dispose()}
if(-not $taskReadmePresent -or $taskInvalid -or $taskCount -ne $taskIncluded.Count){throw 'Archive layout, exact byte or privacy policy check failed; do not distribute this archive'}
$taskRecord=[ordered]@{file=[IO.Path]::GetFileName($taskOutput);bytes=(Get-Item -LiteralPath $taskOutput).Length;sha256=(Get-FileHash -LiteralPath $taskOutput -Algorithm SHA256).Hash.ToLowerInvariant();entries=$taskCount;entryReadmePresent=$taskReadmePresent;invalidPathsOrPrivateRuntimeIncluded=$taskInvalid;reviewedRun03Scenes=$taskAllowedRun03Ids.Count;reviewedReferenceScenes=$taskAllowedReferenceIds.Count;reviewedMotionRecordings=$taskAllowedMotionIds.Count;reviewedMotionFiles=$taskAllowedMotionFiles.Count;motionSafeAllowlist=$taskMotionSafeFile;motionSafeAllowlistSha256=$taskMotionSafeSha;run03UnreviewedScenesExcluded=$taskUnreviewedRun03Excluded;run03WithheldScenesExcluded=$taskWithheldRun03Excluded;run03PrivateScenesExcluded=$taskPrivateRun03Excluded;run03IncompleteScenesExcluded=$taskIncompleteRun03Excluded;run03OtherWithheldScenesExcluded=$taskOtherWithheldRun03Excluded;referenceApk='kimi-android/'+$taskNativeApk;referenceApkSha256=$taskNativeSha;safeAllowlist=$taskSafeFile;safeAllowlistSha256=$taskSafeSha;excludedByReason=$taskExcluded;verifiedAt=[DateTime]::UtcNow.ToString('o')}
$taskRecord|ConvertTo-Json -Depth 5|Set-Content -LiteralPath $taskManifest -Encoding UTF8
$taskRecord|ConvertTo-Json -Depth 5
