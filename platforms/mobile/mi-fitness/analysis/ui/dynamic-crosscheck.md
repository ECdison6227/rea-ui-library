# 设备 Tab 登录出现与取消：动态交叉复核

**结论：未登录用户可以停留在设备页并看见空态。此次切换时出现的登录页来自账户认证广播链；现有证据不支持把“设备 Tab”实现成必须登录才能浏览的硬门槛。**

复核范围仅覆盖本次未登录、无绑定设备的模拟器会话，不输入账号、不执行绑定。主进程为 4271，设备服务进程为 4304；UTC 日期为 2026-10-15。除读取已保存截图/层级及现有 DEX/Java 外，本次仅只读导出了相关时间片的 adb logcat，没有再操作 UI、启动 Java 或建立新 REA 会话。

## 已观察事实

| 时间（UTC） | 证据 | 可确认内容 |
| --- | --- | --- |
| 11:51:06.949–07.062 | `runtime/events/open-device-tab.json` | 在底部设备标签区域执行一次点击，坐标 `(675,2240)`。 |
| 11:51:07.441 | `runtime/hierarchy/08-device.json` | 层级含「小米账号登录」「请输入手机号」「下一步」「密码登录」，为小米账号 SDK 登录页。此时手机号仍为空。 |
| 11:51:41.625 | `runtime/hierarchy/09-login-keyboard.json` | 聚焦输入框后的登录层级，仍未输入账号。 |
| 11:52:08.802–08.960 | `runtime/events/close-login-keyboard.json` | 第一次 Back，用于关闭已打开的键盘。没有单独的“第一次 Back 后”层级，因此不将这一步单独扩写为页面跳转。 |
| 11:52:09.342–09.465 | `runtime/events/leave-login.json` | 第二次 Back，退出登录流程。 |
| 11:52:09.800 | `runtime/hierarchy/09b-after-login-back.json` | 顶部「设备」，正文「扫一扫」「添加设备」；底部「设备」节点 `selected=true`。未登录即可见设备空态，并保持当前标签。 |

`runtime/captures/08-device-login/screen.png` 实际为全黑帧；它本身不能证明登录页长什么样，也不能仅据此确定黑帧原因。上表登录文字与控件结论来自 UI 层级，而不是对黑帧的视觉解释。

## MainActivity 能确认的边界

1. `native-sources/com/xiaomi/fitness/main/MainActivity.java:374` 的 `onTabSelected` 在普通模式下直接 `showFragment`、更新当前 Fragment，再调用 `shouldShowLoginWhenTokenInvalid` 与选中色更新；没有针对设备索引的 `if (!isLogin) return` 或导航替换。
2. `:1294` 的 `shouldShowLoginWhenTokenInvalid` 仅在已经登录时更新 Token；未登录时直接结束。因此这个名字不能作为“切换设备时强制登录”的证据。
3. `:1358` 的 `showLoginDialogIfNeed` 需要“当前未登录且全局 `MainExtKt.getShowLoginDialog()==true`”；显示后清除该标记。当前导出的代码只有 `onCreate`（`:1792`）和 `onNewIntent`（`:1888`）调用它，普通 `onResume` 与 Tab 回调没有调用。
4. `:1177` 的 OAuth AppLink 另有 `accountLogin()` 分支。本次事件是普通坐标点击；相关时间片未见 MainActivity 重建、`onNewIntent` 或 OAuth AppLink 处理日志，不能把该分支套到本次操作。
5. Tab 额外的 `SimplifiedChineseModel.setTabClickListener` 经 DEX 复核是隐私基础模式下转隐私页的处理，其回调无账户登录调用。它也不构成普通模式下的设备页登录门槛。

## 本次实际启动登录的机制

保留的 `device-login-logcat.txt` 给出以下紧邻时间序列：

- 第 14–16 行：11:51:07.043 设备父 Fragment 可见；11:51:07.052 设备服务执行 `EcoDeviceManagerRemoteImpl refreshEcoList`。
- 第 18–20 行：11:51:07.059 服务进程 `Account` 首次请求；11:51:07.062 `getServiceToken` 记录 `account==null`，sid 为 `miothealth`。
- 第 232–233 行：11:51:07.386 `LoginComponent` 收到 `action_login`；11:51:07.418 系统启动 `LoginSupportActivity`。
- 第 294、302–304、370 行：LoginSupportActivity 的 `loginIntent=null`，随后本地账号为空，账号 SDK 返回 AccountLoginActivity 的启动 intent。

静态调用关系与该时间序列吻合：

```text
DeviceTabParentFragmentV4.onVisible
  -> DeviceTabParentViewModel.updateDevices
  -> OtherDeviceLoaderManagerImpl.updateDevices / refreshEcoDevices
  -> EcoDeviceManagerRemoteImpl.refreshEcoList
  -> EcoDeviceContainer.requestEcoBindDeviceList / getEcoDevices
  -> ElectronicScaleRequest.getEcoDevices

TokenManagerImpl.getToken：本地账号缺失
  -> notifyAuthenticate(intent, loginPolicy)
  -> BroadcastsKt.broadcastLogin（action_login）
  -> LoginComponent.receiver
  -> LoginSupportActivity
  -> 小米账号 SDK
```

其中 TokenManagerImpl 的 DEX 控制流进一步确认：`notifyAuthenticate` 只在 `loginPolicy==2` 时继续，其他策略直接返回；随后用 `mRefreshing.compareAndSet(false,true)` 抑制并发重复广播，并等待登录结果。`BroadcastsKt.broadcastLogin` 的 action 字面量就是 `action_login`。同一时间片主进程另有 `loginPolicy=3` 的请求日志，不能把这些策略 3 请求误归因成主动登录广播。

相关原始方法操作数、文件偏移和 DEX SHA-256 保存在 `device-login-token-crosscheck.json`；`notifyAuthenticate` 位于 `classes9.dex` 文件偏移 `2671704`，其开头为 `const/4 v0,2; if-eq v5,v0 -> continue; return`（v5 是第三个入参 loginPolicy）。`device-login-static-crosscheck.json` 保存设备 Fragment 与 SimplifiedChineseModel 的有限静态复核。

认证广播、LoginSupportActivity 与 SDK 的启动已由运行日志直接确认。设备列表刷新是紧邻发生且有静态路径支持的触发上下文；本轮没有抓取方法级调用栈，因此不进一步声称已唯一归属到某个 HTTP URL 或断言所有渠道/账号状态均会在首次点设备时自动登录。

## 对实现规格的修正

将设备页建模为可以独立显示的 `guest + empty` 页面；账号登录是可取消的覆盖流程。取消后保持设备 Tab，显示扫一扫与添加设备。需要账号的新增、扫码、设备列表请求等后续操作应分别记录自己的认证条件，不能以这一次自动跳转推导整页禁止访问。

现有 `native-pages.md` 的“设备为空→扫码/添加引导”结构经动态层级支持。新增的动态差异是：该空态在首次进入时可能被后台认证链启动的登录页面暂时覆盖，但取消登录后仍然可见。是否每次重进都触发、是否只针对首次请求以及节流/网络失败后的重试行为，尚未通过重复场景验证。
