# Run03 最终恢复与关闭

本说明只供当轮唯一设备控制者执行。`native_review_v2` 正在使用5596时，其他代理不得执行ADB；先完成D采集并明确移交控制。原型APK无需卸载，原版账号保持正常登录，不清数据，不打开私人历史。所有本轮最终文件使用独立名称，已存在的文件不得覆盖。

## 身份与恢复顺序

固定目标为 `emulator-5596` / `rea_kimi_ui_20261015`，包名 `com.moonshot.kimichat`。先执行 `adb -s emulator-5596 emu avd name` 并核对名称；不按设备列表第一个条目选目标。ADB路径使用已保留toolchain或 `KIMI_ADB`。`android-session.mjs` 需要 `KIMI_RUN_ID=run03-20261016`，不要给首轮/run02追加记录。

1. 根据 `conditions-baseline.json` 和 `wm-rotation-baseline.json` 运行 `node tools/run03-conditions.mjs restore`，核对该命令实际输出。另核对 `wm density` 为 `Physical density: 420` 且没有Override；若确有本轮density覆盖才执行 `wm density reset`。系统night基线为 `Night mode: no`，需要时用 `cmd uimode night no` 恢复。初始网络WiFi/data均为1；恢复已有network cycle或启用这两个专用AVD开关，随后验证真实active default network已VALIDATED。
2. `permission-baseline.json` 中三个权限均拒绝：POST_NOTIFICATIONS与RECORD_AUDIO含USER_SET，CAMERA不含USER_SET。只针对这三个权限恢复grant及标志；不要reset全包权限。对确已授权的项执行 `pm revoke --user 0 com.moonshot.kimichat android.permission.NAME`；CAMERA若新增USER_SET/USER_FIXED，用 `pm clear-permission-flags --user 0 com.moonshot.kimichat android.permission.CAMERA user-set user-fixed`。其他两个权限保留基线USER_SET；仅在确实缺失时用 `pm set-permission-flags --user 0 ... user-set`。最终必须逐项比较grant及完整flags，不能只看granted=false。拒绝/撤销不代表真实录音相机路径完成。
3. 通过原版UI核对Follow System主题/字号、English、Instant/High、Web Search Auto；原版app locale基线为 `[en-US]`。使用既有完整A场景或新建独立场景记录人工像素/UIA核对，不因点击动作成功就认为结果恢复。语音/播放速度、DocsUI网格和本轮自造草稿清理的早期恢复证据仍保留在任务清单中；如期间再次改动，应重新核对。
4. 执行下述system locale处理后，采最终只读snapshot和独立APK证明，再仅关闭5596。

## system_locales 的证据边界

早期source capture记录 `settings get system system_locales` 返回字符串 `null`；当时没有记录 `settings list system` 中该key是否存在。后期值被显式设为 `en-US`。早期的get结果无法区分key缺失与存储literal `null`，也不能单独证明实际界面语言。build locale一直是en-US，App自己的locale为en-US。

为恢复已观察到的get值，唯一设备控制者可执行 `adb -s emulator-5596 shell settings delete system system_locales`。最终snapshot同时读取get和key存在性，期望get=`null`、key不存在。报告必须保留“初始key存在性未知，因此不证明初始存储状态精确恢复”的限制，不可写成已经证实原key必然不存在。不要把literal `null` 写回setting。

## 不覆盖的最终文件

```powershell
$env:KIMI_RUN_ID = 'run03-20261016'
node tools/run03-final-state.mjs snapshot
node tools/android-session.mjs verify-apk final
```

`snapshot` 固定身份，拒绝覆盖，记录显示、rotation、动画、IME、App locale、system locale key存在性、三个权限完整flags及当前active network。它只读设备；读取为非原子顺序。网络VALIDATED必须与实际active network id出现在同条NetworkAgentInfo记录，不能借用另一条网络的VALIDATED。若Android dump布局不支持此匹配，脚本保留false并失败；先检查原因，不可直接改报告为true。可以用 `snapshot retry2` 生成新文件，之后在restore.json引用该真实路径。

`verify-apk final` 独立写 `artifact-identity-final.json` 与session的 `identityVerificationFinal`，不改 `artifact-identity.json` / `identityVerification`。最终证明与原包真实字节、设备已装APK的SHA和3.1.3/376版本一致。当前保留的非final证明时间是2026-10-16T08:44:33.682Z，不宣称找回更早已被覆盖的版本。

唯一设备控制者核对上述结果后，仅执行：

```powershell
node tools/android-session.mjs stop 'Run03 final restoration checked; preserve local login'
node tools/android-session.mjs exit-emulator
node tools/run03-final-state.mjs shutdown-check
```

`shutdown-check` 只读ADB设备列表和Windows emulator/qemu进程身份，验证5596消失且专用AVD/5596进程为空。它不关闭进程；记录中不会保留其他进程命令行。若退出尚未完成，用新的 `shutdown-check retry2` 复核，并在restore.json引用新路径。不要执行 `adb kill-server`、taskkill全部qemu/emulator、或停止其他端口实例。

## restore.json 契约

restore.json由控制者在真实核对后形成；以下为字段结构，不是已完成证明。`evidenceSceneIds` 填完整A场景ID。原图/source审核由原审核者完成，生成本记录不批准新的场景。

```json
{
  "serial": "emulator-5596",
  "avd": "rea_kimi_ui_20261015",
  "finalState": "evidence/runtime/run03-20261016/final-device-state.json",
  "shutdownVerification": "evidence/runtime/run03-20261016/shutdown-verification.json",
  "network": {"settingsMatch": true, "activeNetworkVerified": true},
  "locale": {
    "initialGet": "null",
    "initialKeyPresenceKnown": false,
    "exactStorageRestorationClaim": false,
    "limitation": "初始仅记录get=null，未记录key是否存在；最终删key恢复get观测值，不证明初始存储状态精确一致。"
  },
  "appPreferences": {
    "theme": "Follow System", "fontSize": "Follow System", "language": "English",
    "model": "Instant", "thinking": "High", "webSearch": "Auto",
    "reviewed": true, "evidenceSceneIds": ["实际外观场景ID", "实际模型和搜索场景ID"]
  },
  "account": {"loggedOut": false, "credentialsRecorded": false}
}
```

`node tools/verify-run03.mjs --check --require-restored` 对照基线、最终snapshot、最终APK证明、session和关闭记录检查身份、值、时间序与声明。它要求三个权限完整flags一致，要求已观察偏好值与具体A场景引用，并明确返回locale证据限制。检查不执行App或重新审阅像素，不代表整个remaining-validation范围完成。`--require-complete` 仍独立要求所有任务完成；缺真实设备/服务不得靠恢复记录关闭任务。
