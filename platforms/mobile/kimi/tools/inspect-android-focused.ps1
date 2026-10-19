param(
  [string]$ApkPath = (Join-Path (Get-Location) 'Kimi.apk'),
  [string]$EvidenceDirectory = (Join-Path (Get-Location) 'research/kimi-android/evidence/rea'),
  [string]$ToolchainDirectory = 'C:/Users/edsjrcheng/.rea/toolchains/android'
)
# Existing local tools only. Environment variables are scoped to this process
# and restored. No global tool installation or configuration changes.
$ErrorActionPreference = 'Stop'
$javaBefore = $env:JAVA_HOME
$jadxBefore = $env:REA_JADX_MCP_JAR
try {
  $env:JAVA_HOME = Join-Path $ToolchainDirectory 'jdk-21'
  $env:REA_JADX_MCP_JAR = Join-Path $ToolchainDirectory 'jadx-mcp/jadx-headless-mcp-0.7.1-all.jar'
  $resolvedApk = (Resolve-Path -LiteralPath $ApkPath).Path
  New-Item -ItemType Directory -Path $EvidenceDirectory -Force | Out-Null
  function Save-ReaResult([string]$FileName, [string[]]$ReaArguments) {
    $resultLines = & rea @ReaArguments --format json
    if ($LASTEXITCODE -ne 0) { throw "REA exited with $LASTEXITCODE for $FileName" }
    $json = [string]::Join([Environment]::NewLine, [string[]]$resultLines)
    $null = ConvertFrom-Json -InputObject $json
    [IO.File]::WriteAllText((Join-Path $EvidenceDirectory $FileName), $json, [Text.UTF8Encoding]::new($false))
  }
  Save-ReaResult 'android-package.json' @('inspect-android-package', $resolvedApk)
  Save-ReaResult 'main-activity-class.json' @('inspect-android-class', $resolvedApk, 'com.moonshot.kimichat.MainActivity')
  Save-ReaResult 'main-activity-oncreate.json' @('inspect-android-method', $resolvedApk, 'com.moonshot.kimichat.MainActivity', 'onCreate', '--overload-index', '0')
  Save-ReaResult 'content-entry-class.json' @('inspect-android-class', $resolvedApk, 'ln.n')
  Save-ReaResult 'content-entry-setcontent.json' @('inspect-android-method', $resolvedApk, 'ln.n', 'h', '--overload-index', '0')
  Save-ReaResult 'activity-content-class.json' @('inspect-android-class', $resolvedApk, 'e.b')
  Save-ReaResult 'activity-content-setcontent.json' @('inspect-android-method', $resolvedApk, 'e.b', 'a', '--overload-index', '0')
} finally {
  $env:JAVA_HOME = $javaBefore
  $env:REA_JADX_MCP_JAR = $jadxBefore
}
