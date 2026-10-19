# 环境、复现与恢复

本轮复用用户以前研究中保留的 `.rea/toolchains/android`：JDK21、JADX MCP0.7.1、Android SDK与API35 Google APIs x86_64镜像，后者报告支持ARM64转译。没有下载或升级全局工具，没有修改Windows可选功能，没有重启电脑。

## 工具和连接

当前会话没有暴露 REA MCP tools，但已有 `rea` CLI 6.5.0，使用真实CLI结果保留 Evidence。初始 doctor 的 Codex registration 为 aligned，不代表本会话已经连接MCP；因此没有重复 setup 或改写客户端配置。

JDK/JADX初始未在进程环境中配置。找到现有工具后，仅在相关子进程设置 JAVA_HOME、REA_JADX_MCP_JAR。实际引擎版本、哈希、命令见 [架构与资源](../spec/architecture-and-resources.md)。

REA archive inventory成功；其全包提取因Windows大小写冲突拒绝。另用 [extract-assets.ps1](../tools/extract-assets.ps1) 保存资源原字节并为冲突文件增加后缀，清单保留原路径。不能用“提取成功”替代原生运行证据。

新AVD位于工作区根 `.runtime/avd`，名字 `rea_kimi_ui_20261015`；最终使用5596端口。前期5582与另一独立汽水研究冲突，停止的仅为命令行明确指向本轮 Kimi AVD 的 launcher/qemu 进程。旧5582设备与安装尝试作为诊断保存，不属于Kimi的有效场景；不继续操作另一研究实例。

首次新系统出现System UI ANR，选择Wait后恢复。原APK默认流式安装返回拒绝，使用普通 ADB `install --no-streaming` 成功；版本随后由 REA inspect-adb-package 核对。没有改签名、改APK、绕过认证或关闭系统安全机制。

## 复现

已有同一局部工具链时，从工作区执行：

```powershell
& ./research/kimi-android/tools/start-emulator.ps1
node ./research/kimi-android/tools/android-session.mjs status
```

新机器需提供同等SDK/JDK/镜像；脚本支持AndroidToolchain/StateDirectory/Port/AvdName参数，不把本机绝对路径当作库依赖。新AVD不会随研究库压缩包分发。

等待 `sys.boot_completed=1`、package/window服务可用，并用 `adb -s <serial> emu avd name`核对专用AVD后再装包：

```powershell
adb -s emulator-5596 install --no-streaming ./Kimi.apk
node ./research/kimi-android/tools/android-session.mjs launch
node ./research/kimi-android/tools/android-session.mjs capture fresh-scene-id '实际目的'
```

每次capture使用新ID，原图不覆盖。KIMI_ADB、KIMI_ADB_SERIAL、KIMI_AVD_NAME可适配设备，操作前仍需核对对应的研究身份。当前脚本只直接支持有AVD名称的模拟器，不将真机冒充AVD。

批量动作必须在前一步失败后停止：

```powershell
function Invoke-KimiStep {
  & node ./research/kimi-android/tools/android-session.mjs @args
  if ($LASTEXITCODE -ne 0) { throw 'Android step failed' }
}
```

截图先于UIA，采集非原子。早期UIAutomator出现null-root却退出码为0，可能读到上次device XML；09和27的层级不用于当前状态证明。脚本已改为先删除自己创建的单个临时XML，要求dump确认新文件，失败时只保留原图。历史原件不改，复核排除依据另存。

## 本轮变更与恢复边界

新建的是测试AVD，没有读取或覆盖小米健康/汽水的用户资料。原Kimi.apk始终未修改；未登录、未填手机号/验证码/密码，未发聊天、反馈或其他外部消息。

测试了应用内Dark mode与简体中文。结束前恢复初始Follow System主题和English语言；具体验证场景、退出与保留项以 [restore.json](restore.json) 为准。fontScale、系统night、系统语言未改，字号与功能管理开关未修改。

首次协议同意与通知拒绝记录保留在新的测试安装里；不清除数据来伪装未操作。该AVD供继续研究使用，已安装工具与原APK保留，不卸载用户工具或删除资料。结束只关闭本轮专用模拟器，不停止共享ADB服务器及其他实例。

没有执行GitHub push或其他发布；交付包含研究资料与原件，不包含原APK、模拟器磁盘、缓存、账号profile或调试密钥。研究库与原包哈希关联；需要重新解析原包时由使用者提供同哈希APK。
