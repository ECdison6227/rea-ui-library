# 新 session 完整上下文 · 2026-10-16

用户要求：**继续实际测试尚未验证的项目，直到约定范围完成；需要用户辅助时及时告诉他，同时推进独立工作。** 这是前两轮研究后的交接快照。本次交接仅核对状态、记录清单，没有重新启动App或执行新功能测试。

## 阅读顺序

1. 本文与 [remaining-validation.json](remaining-validation.json)：状态、任务ID、前置与验收。
2. [run02-results.md](run02-results.md) 与 [run02.html](../run02.html)：最新实测结果及41张安全原图。
3. [模型/草稿/字号](../spec/run02-behavior-verification.md)、[生成与停止](../spec/run02-authenticated-behavior.md)、[附件/权限/离线恢复](../spec/run02-attachments-and-permissions.md)。
4. [library.json](../library.json)、[证据索引](../evidence/index.json)、[sharing-scope.md](sharing-scope.md)。

`coverage.md`主要是首轮快照，不要把旧的“登录后未测”当当前结论。首轮/run02的origin=current是各自采集时的标记；对新session都是历史证据，复查不产生新实测。

## 目标和本机状态

| 项目 | 已核对状态 |
| --- | --- |
| 工作区 | `C:\Users\edsjrcheng\Desktop\kimiuiphone` |
| 原APK | 根目录`Kimi.apk`，41,038,830 bytes，未修改 |
| 包/版本 | `com.moonshot.kimichat`，3.1.3 / code376 |
| SHA-256 | `9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332` |
| 专用AVD | `rea_kimi_ui_20261015`；serial `emulator-5596`；端口5596/5597 |
| AVD目录 | 工作区`.runtime/avd`、`.runtime/android-user`；不随ZIP分发 |
| 系统 | Android15/API35 Google APIs x86_64，支持ARM64转译；1080×2400物理px，420dpi，density2.625 |
| 最终偏好 | 主题Follow System、字号Follow System、English/en-US；系统fontScale1.0、night=no |
| 网络 | Wi-Fi、移动数据均恢复1；关闭前确认有效默认网络 |
| 登录 | 用户协助正常手机号/短信登录，状态保留在本地AVD，未退出；下次启动核对是否仍有效 |
| 权限 | 麦克风首次申请拒绝一次，未授权录音；首轮通知拒绝记录保留 |
| 当前进程 | run02已关闭Kimi及专用AVD；交接时ADB无5596，另有5580，后者不是Kimi目标 |

恢复事实：[run02 restore](../evidence/runtime/run02-20261016/restore.json)、[final-device-state](../evidence/runtime/run02-20261016/final-device-state.json)、[shutdown-verification](../evidence/runtime/run02-20261016/shutdown-verification.json)。旧PID不能用于停止新进程。

## 已完成，直接复用证据

首轮完成REA inventory、JADX入口/manifest、资源保真提取和哈希、17本地化目录37,046条键、字体元数据、widget CSS及基础组件。运行覆盖隐私/通知/主页/侧栏/设置/深浅主题/语言和进程重启保留。34张采集原图、24条转移；1430原件资源、390派生SVG。

run02有45张采集图、102动作、44份UIA，41安全图进预览。编号01–46中**没有23**，失败的Copy查找没有产生该场景，不能补造。

- 模型菜单K3/K2.8/Instant/Thinking effort可见；Instant选K3后入口与placeholder变化；额度说明和Got it序列。
- 真实软键盘、两字符未发草稿Re，隐藏键盘/抽屉/设置字号保存后保留；Re也经历登录及模拟器重启仍在，但长文本跨进程策略未完整隔离。
- 字号本地Extra Large预览；Back放弃未存值；Save后**直接回主页**，重入摘要Extra Large；最终恢复Follow System。
- 正常登录后Instant/High：Reply only OK→OK。生成中方形停止入口，完成后回答工具栏。
- 自建计数回答Retry后，新鲜UIA确认生成态再点停止→Output stopped。同页Python工具失败只记录并存，不把失败归因为停止，也不证明后端请求已取消。
- 自造408B TXT：系统选择器→Parsing→TXT/408B→移除→重选→发送→准确返回文件内KIMI-UI-20261016。原文与REA local/device SHA一致。
- Hold to talk按住后同帧App用途说明+Android录音授权；拒绝回原聊天，可切回文字。
- 专用AVD断网后短消息发送失败，气泡保留、红重试、Network is unavailable；恢复网络点红标→offline-test-OK。
- 最新自建回答Copy→Android Paste→未发送草稿offline-test-OK，随后只清理该草稿。未访问主机剪贴板。
- 真正会话More：Read aloud Off/Pin/Rename/Delete；Rename编辑框→Cancel，Delete确认→Cancel。没有提交重命名、置顶或执行删除。

自造文件：[research-upload.txt](../assets/derived/test-fixtures/research-upload.txt)；设备副本`/sdcard/Download/kimi-ui-research-upload.txt`；SHA `80457db5c02ca9764712d9ba87bcb7989aa3a2499961bd3595d8eda2f019463f`。真实REA推送在`evidence/rea/run02-push-fixture.json`。

自建会话有Reply only OK、自动标题Count 1-1000 & Fixture ID附近的研究会话。账号原有私人历史确实存在，未打开。下一轮优先新建带Run03标记的测试内容，不按列表位置盲点。

## 恢复运行与新run

已有用户局部工具链`.rea/toolchains/android`：`jdk-21`、`jadx`、`jadx-mcp/jadx-headless-mcp-0.7.1-all.jar`、`apktool`、`sdk`。REA CLI6.5.0、ADB37.0.1可用。初始JDK/JADX不在PATH，不能据此说没装。前会话未暴露REA MCP tools，采用真实CLI；不要重复setup或全局安装来证明连接，按当前实际skill/schema使用。

从工作区PowerShell启动**原AVD**，不重装/清数据/新建另一份来丢失登录：

```powershell
& ./research/kimi-android/tools/start-emulator.ps1
```

5596占用先查命令行及AVD名：同一实例已运行就复用，不启动两份。其他研究也在同机运行，不按第一个ADB设备选目标，不停止共享adb服务器。端口改变需同步KIMI_ADB_SERIAL，仍核对AVD名。

等sys.boot_completed=1、package/window服务正常，核对`adb -s emulator-5596 emu avd name`和已装版本。boot/install成功不等于进入Kimi成功。

新一轮必须使用未占用的run ID，**不要调用kimi-run02.ps1写历史**：

```powershell
$ErrorActionPreference = 'Stop'
$env:KIMI_RUN_ID = 'run03-20261016'
$env:KIMI_ADB_SERIAL = 'emulator-5596'
$env:KIMI_AVD_NAME = 'rea_kimi_ui_20261015'
$env:KIMI_CAPTURE_SETTLE_MS = '2000'
function Invoke-Kimi {
  & node ./research/kimi-android/tools/android-session.mjs @args
  if ($LASTEXITCODE -ne 0) { throw 'Kimi step failed; stop dependent actions' }
}
Invoke-Kimi status
Invoke-Kimi launch
Invoke-Kimi ui
# 核对实际界面及隐私，再使用全新capture ID。
```

先读android-session.mjs实现再使用：已有capture/ui/tap-label/tap-last-label/text/text-slow/key/swipe/stop-generation/network-off/network-restore等。新session默认accountMode=no-login，即使本地已登录也不会自动识别；核实后更新新run字段，不改历史。认证输入必须绕开记录arguments的text/text-slow，只走不保留凭据的临时流程。

锁按run目录防重入，两个不同run仍会抢同一设备，因此设备只由一人控制。失败后先观察再继续，不顺着预设坐标连点。stale lock要确认原PID已死，不能随意移除活锁。

启动脚本默认`-no-window -no-audio`；真实语音或人工登录需要可见/音频配置时，解释具体影响、保留状态，只停止自己的AVD。上次用户允许可见登录但说无法输入，之后由用户提供信息正常完成；旧验证码没有保存、不能复用。过期时请求最小协助，人工操作期间停点击及凭据capture。

## 已知陷阱

1. System UI ANR出现过，Wait恢复；系统层不是Kimi错误态。
2. Gboard手写笔教学层会收到部分按键，UIA仍能返回下层Kimi旧草稿。早期长输入只出现Re不能推产品截断。每次提交前核对原图+EditText；逐字输入也需复核，实际te拼写不能改成命令原意。
3. 截图先于UIA，非原子。run02的20截图生成/后UIA完成；28截图Kimi/后UIA DocsUI；29空Downloads图/后UIA有文件；30Parsing图/后UIA就绪。2秒等待是采集选择，不是动效时长。
4. null-root可能退出码0；脚本已先清自己临时XML并确认新文件。失败时hierarchy=null，不复用旧文件。
5. 菜单可能像素可见但UIA只返回下层；label找不到不证明不存在。像素确认后才用当前坐标。
6. 同位置生成时方形stop、结束时语音图标。第一次晚停实际切了Hold to talk，只有Retry后fresh guard才证实停止。stop-generation守卫只匹配已观察英文状态，语言/版本改变需调整。
7. 未登录侧栏齿轮进设置；**登录后头像/名称入口**进设置，旧齿轮位置变成新会话。
8. APK有res/7p.xml与res/7P.xml，Windows全量提取冲突；已保真后缀处理，无需重做。
9. widget CSS只是show_widget内联层，不是Compose全局主题；字体文件、Latin覆盖、运行回退分开记录。

## 剩余范围、授权和完成

精确ID见 [remaining-validation.json](remaining-validation.json)。覆盖用户刚问到的九组功能及独立参考/对照/迁移。建议先其他模型/思考强度→自建会话管理/长草稿→图片/多附件/失败→项目/PPT/构建/集群/技能/搜索→并发/后台/晚到→适配动效→需用户音源/相机/双端→原生参考及双重验证；可按依赖调整。

每项补入口、正常结果、返回/取消、适用的失败恢复、状态保留；包内文案、按钮可见或点击成功都不足以verified。昂贵功能用最小代表样本，不为凑数购买额度；当前账号未开放/订阅门槛要有证据并请求具体决定。

任务状态定义在清单。needs_user/needs_authorization不是完成；及时提最小需求并推进其他项。not_applicable需证据和适用边界，不可用“本机没麦克风”销项。只有用户明确改变范围才能把要求移出。

已授权目标内可逆研究、正常账号和新建中性测试聊天/附件/项目。用户现在要求把剩余项继续测完，**仅对本轮新建、确认身份且可丢弃测试会话**可完成重命名、置顶/取消及删除自身验证，不重复询问常规测试；用户既有会话文件不碰。分享面板/本地导出可准备；向他人实际发送/公开发布、邀请、付费、真实电脑远程连接/执行需具体额外授权，不能把“全测”当无限权限。

真正收音/识别与真实拍照需要可用设备、明确人工样本。先诊断已有正常模拟硬件与自造素材，不能用模拟素材声称验证真机。远程/双端功能在用户指定的独立测试设备与资料上操作，不能默认连接当前工作电脑。

原生参考、来源对照和跨业务样本是功能之后的独立交付阶段，不在这轮交接里偷偷取消。按原生平台选栈，截图浏览器不是参考。A/B来源→规范→D实现→具体差异和迁移验收，三者各自报告。

## 隐私、验证和交付

手机号/验证码没有写研究文件，别从聊天抄进上下文。登录仅在本机AVD。run02的18/19含问候身份，留本机不入预览/ZIP，package-library.ps1明确过滤；新轮出现新的身份页时相应扩大过滤，不只依靠旧规则。根`.runtime/`私有调试资料不能打包。

verify-library.mjs检查首轮，verify-run02.mjs检查run02；通过不等于run03通过。build-run02-gallery安全名单也是旧run专用。**不要重跑finalize-library.mjs**，它是首轮生成器，会覆盖最新索引/restore；summarize-run02.mjs也只属run02。为新run扩展对应审核、汇总与验证。

最新交付包根目录Kimi-3.1.3-research-run02-20261016.zip只含截至run02资料；交接和待办是在它之后新增，当前工作区是续接依据。

最终逐项更新清单、README/library、覆盖、状态转移、安全画面、参考/对照/迁移、检查和恢复。恢复本轮网络/主题/字号/旋转，保留用户登录和明确保留内容，停止自己的AVD，原APK哈希不变。发布不是默认收尾。全部约定任务实测完成或有用户认可的不适用范围才算完成；人工/授权/硬件依赖没解决就保留未完成，不能以漂亮演示或文件数量替代验收。
