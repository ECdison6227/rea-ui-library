# MineTab 运行时窄交叉验证

两次模拟器观测确认：我的页头没有“我的”大标题；快捷入口从 4 项变为 5 项，同时顺序与水平内边距发生变化。进程日志确认两次观测之间加载的 mine 业务包从 **11.190.11** 变为 **11.202.21**；提取的新运行包也在源码中无条件写出了五入口并移除了行内水平 padding。因此，差异已经由业务包版本变化解释，不应归因于深色主题。

本次只读已有 hierarchy、现有精选 RN 模块、内置 bundleInfo、目标进程日志，以及父任务通过 REA 取出的新运行包。仅对新 mine 的 6 个相关模块做窄 AST/原文比较，没有展开新 sport 分析，也没有操作 UI、重启 App、重启 adbd、清理日志或展开新的 Java 分析。

结构化证据见 [runtime-rn-crosscheck.json](runtime-rn-crosscheck.json)。

## 1. 两次观测

环境：Android 15 / API 35，1080×2400 px，dpi 440（density=2.75），fontScale=1，zh-CN，模拟器 `emulator-5580`。设备时区只读查询为 GMT，logcat 时间与 hierarchy 的 UTC 时间可以对齐。

| 项目 | 11-mine-after-login-cancel | 17c-mine-night-visible |
|---|---|---|
| 采集 UTC 时间 | 2026-10-15 11:55:17 | 2026-10-15 12:03:29 |
| 登录资料 | 未登录 / 无小米账号 | 未登录 / 无小米账号 |
| 快捷入口数 | 4 | 5 |
| 从左到右 | 我的活动、我的课程、我的订单、我的亲友 | 我的活动、我的课程、我的亲友、我的奖品、我的订单 |
| 可点区域横向范围 | x72..1009 px | x33..1047 px |
| 每项宽度 | 234、234、234、235 px | 203、203、202、203、203 px |
| 总水平 inset | 约 26 dp | 12 dp |
| 图标 | 66×66 px（24×24 dp） | 66×66 px（24×24 dp） |
| 图标纵向范围 | y505..571 px | y505..571 px |
| 标签纵向范围 | y588..673 px | y588..673 px |
| 页头标题 node16 | 单个空格，`[72,151][94,268]` | 单个空格，`[72,151][94,268]` |

来源：[四项 hierarchy](runtime/hierarchy/11-mine-after-login-cancel.json)、[五项 hierarchy](runtime/hierarchy/17c-mine-night-visible.json)。这些 bounds 是本模拟器实际像素，不是跨设备固定像素；dp 值根据已知 density 换算，并含像素舍入。

页面底部原生导航仍显示“我的”，不代表内容页头应再补一个“我的”标题。

## 2. 包身份变化的直接证据

| 事件 UTC 时间 | 进程日志事实 |
|---|---|
| 11:52:14 / 11:52:18 | 加载 mine `version=11.190.11`；md5 元数据/缓存目录 `31c9f454df8a88403912b6eeed02dc60`；makeTime `20260709140144` |
| 11:55:17 | 四入口 hierarchy |
| 11:58:57 | 旧 mine 实例 `dirty=true`，随后 unloadInstanceIfNeeded |
| 12:01:34 / 12:01:36 | 加载 mine `version=11.202.21`；md5 元数据/缓存目录 `e5d8d3390f6109d6c50e9a27d0143a22`；makeTime `20260721193548` |
| 12:03:29 | 五入口 hierarchy |

旧版本与 [APK 内置 bundleInfo](evidence/builtin-bundleInfo.json) 的 mine 项完全对应：version `11.190.11`、urlmd5 `31c9f454df8a88403912b6eeed02dc60`。日志里的新包路径是：

```text
/data/user/0/com.mi.health/cache/mirn_bundles/production/miwear/mine/
e5d8d3390f6109d6c50e9a27d0143a22/index.android.bundle
```

证据：[只读目标 PID 日志筛选结果](runtime/logs/rn-bundle-log-probe.json)，PID 4271，保留 26 条 bundle 相关匹配日志；未持久化其余进程日志。此处 md5 是日志字段及缓存目录标识，与内置 urlmd5 对应，**不能当作 `index.android.bundle` 文件自身 MD5 或 SHA-256**。

父任务随后用 REA 取出运行包，新 mine 文件 SHA-256 已在本子任务重新计算并匹配：`4f8012fb1ed726d3ef11f068ff7f42b6e74def6059eddaff3426b4dbb7a06958`。REA 采集 Evidence 为 `ev_4b19bdb230e5223001a26c650fb1510cee45222ac78f7712f381ac8115c85bbb`。[新运行包](runtime/active-bundles/mine/code/index.android.bundle)、[配置](runtime/active-bundles/mine/config/buz.config.json)共同给出 `pack_version=11.202.21`、`maketime=20260721193548`，与进程日志一致。

这形成了“运行日志元数据 → 取得对应文件 → 文件 SHA-256 → 新源码 → hierarchy”的闭环，不再需要用服务端开关猜测入口数量变化。

## 3. 内置代码能支持与不能支持的规则

### 页头标题已经闭环

`mi-412.MineTab` 向 `TabViewContainer` 仅传 `icons`，不传 `title`。`mi-414.TabViewContainer` 对 undefined title 使用默认值 `" "`。两份 hierarchy 的 node16 都是单个空格，与源码一致。

来源：[mi-412](rn-evidence/mine-mi-412.js)、[mi-414](rn-evidence/mine-mi-414.js)。复刻应保留该区域高度与右侧消息/添加设备操作，标题文字留空。

### 内置 QuickActions 的内容固定为四项

调用链：`mi-412 → mi-423.MyProfileMapPage → mi-512.QuickActions → mi-620.QuickActions → mi-621.EntranceItem`。

- `mi-620` 明确写出四个 EntranceItem，顺序活动、课程、订单、亲友；没有 entries 数组、条件插入、奖品/lottery 开关。
- `mi-620` 监听语言变化后只触发重新渲染；不增加入口数量。
- `mi-621` 的主题分支只在 `icon.light` 与 `icon.dark` 之间切换，不变更 count/order。
- `mi-408.withRenderFC` 只记录日志、透传 props，不会注入入口。
- `mi-423` 在 CN 登录/未登录两种组合中都渲染 QuickActions，并没有按登录状态改四项为五项。
- 对现有内置 mine bundle 检索 `lottery`、`mine.assets.item.prize/award/lottery`、`showPrize`、`showLottery`、`奖品` 均未匹配。此检索结论只适用于该内置版本。

来源：[mi-620](rn-evidence/mine-mi-620.js)、[mi-621](rn-evidence/mine-mi-621.js)、[mi-408](rn-evidence/mine-mi-408.js)、[mi-423](rn-evidence/mine-mi-423.js)、[mi-512](rn-evidence/mine-mi-512.js)。

### 布局的稳定部分

内置入口项为 `flex:1`，内部图标/文字居中；图标24×24、图标下6；标签14/400，上padding2、下padding10、单行。行容器上margin8、上padding18、下margin5、水平padding14；外层 IOScrollView 另有水平padding12。

这能解释四项观测约26dp的左右 inset；两份 runtime 的等宽分格、24dp图标和相同纵向位置也与这一组件风格一致。五项观测的总inset变为12dp，**不是只把内置四列改成五列就能得到的完整布局**。

### 新版五项源码已经确认

| 代码项 | 内置 11.190.11 | 运行 11.202.21 |
|---|---|---|
| QuickActions 模块 | mi-620 | mi-621 |
| EntranceItem 模块 | mi-621 | mi-622 |
| 直接子项数 | 4 个 EntranceItem 调用 | 5 个 EntranceItem 调用 |
| 顺序 | activity / course / order / friend | activity / course / friend / prize / order |
| 行容器 px | `"14px"` | 删除，未另设水平 padding |
| 外层 IOScrollView | 水平12 | 水平12 |
| 数量/顺序主题分支 | 无 | 无 |

新 `mi-621` 的五个 EntranceItem 都是 HStack 的直接 createElement 子项，没有条件包裹。也没有针对奖品的服务端/异步 count gate。外围的 CN 地区组合仍决定是否显示整个 QuickActions 分区；这与分区内部固定五项是不同层级。

新 `mi-622.EntranceItem` 保留等宽、图标与标签样式以及共享登录 guard。新 `mi-406` 添加 `prize_light.webp` / `prize_dark.webp`，主题只选择图标资源。

逐字比较 factory 部分也确认：两个版本的 EntranceItem 与 TabViewContainer 完全一致（模块ID、依赖表位于 factory 外，不参与此比较）。因此入口项本身的样式与空格标题规则不需要按版本改写。

“我的奖品”源码点击路径为：先经过共享 `useLoginStatusCheck`，再记录 `clickMyPrizeTrack`，push native `com.xiaomi.fitness.webview.healthwebview.HealthWebViewActivity`，其 URL 表达式为：

```text
https:// + getHttpHost(HostType.watch) + /mobile/lotteryRecord?channelType=5
```

这确认了源码意图，未点击奖品入口验证目的页。host 由现有配置解析，不能写成一个未经记录的固定域名。

窄提取证据：[新 QuickActions](rn-evidence/active-mine-mi-621.js)、[新 EntranceItem](rn-evidence/active-mine-mi-622.js)、[新图标映射](rn-evidence/active-mine-mi-406.js)、[新 MineTab](rn-evidence/active-mine-mi-412.js)、[新标题容器](rn-evidence/active-mine-mi-414.js)、[新导出映射](rn-evidence/active-mine-mi-512.js)。模块行号、hash、AST props/子项比较保存在 [结构化交叉验证](runtime-rn-crosscheck.json)。

## 4. 用于其他业务的实现规则

以下是基于两个源码版本与观测整理的复刻建议，不是小米服务端开关规则：

1. 快捷入口使用有序配置列表，每项等宽居中；不要在样式中固定四个宽度。
2. 保留两个已闭环 fixture：11.190.11 四项顺序活动/课程/订单/亲友、总inset约26dp；11.202.21 五项顺序活动/课程/亲友/奖品/订单、总inset12dp。各自版本的入口调用是固定的，不应杜撰主题开关。
3. 把 `entries`、`order`、`rowInset` 作为独立配置，不把 theme 与“奖品是否出现”绑定。
4. 图标24dp、标签14/400及稳定纵向节奏可以共用。页头标题留空，同时保留右侧操作。
5. 新版奖品入口的源码路由及共享登录拦截已确认；目的页实际运行行为未验证，本次没有点击它。

## 5. 对原静态规格的修订范围

[rn-pages.md](rn-pages.md) 和 [rn-pages.json](rn-pages.json) 中的四快捷入口代表 **APK 内置 mine 11.190.11 基线**。涉及数量/顺序/页头标题时，以本交叉验证的双变体观测为补充；不应把四项写成运行时不可变约束。原静态提取复核文件记录的是当时的只读代码解析，不应误读为后续没有运行 App。
