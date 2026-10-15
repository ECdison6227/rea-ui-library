# 逆向总报告

## 分析对象与方法

| 项目 | 观察值 |
| --- | --- |
| 文件 | `mirasim-0.0.497.apk` |
| 大小 | 21,461,353 字节 |
| SHA-256 | `10e83b0dce86966e227577137bce6c6ea1968ff1d81413f203ab08090108d062` |
| 包名 | `ai.mirofish.mirasim` |
| versionName / versionCode | `0.0.497` / `497` |
| minSdk / targetSdk | `26` / `36` |
| 本次入口 | `MainActivity extends BridgeActivity` |
| 主要界面技术 | React，Capacitor WebView，本地 HTML/JS/CSS |
| 前端库标记 | React 构建产物自报 `19.2.8`；CSS 标头自报 Tailwind `4.3.3`；存在 Vite 生成的模块预加载标记 |

使用 **REA 6.5.0** 对前端构建产物、Android 包声明、更新类、方法和引用进行分析；使用其文档指定的 **jadx-headless-mcp 0.7.1** 补充导出可导航的 Java/XML。JAR 的 SHA-256 已与 REA 发布元数据匹配。浏览器预览直接运行 APK 内前端资源。

本报告将三种证据分开：**静态确认**是构建产物/DEX 中存在的逻辑；**浏览器确认**是离线前端实际呈现的画面；**建议实现**是根据这些观察为新项目设计的方案。浏览器确认不等于 Android 真机行为确认。

## 最值得借鉴的五点

1. **UI 与平台能力分离。** React 管理页面和状态；Capacitor 插件处理通知、更新安装、系统设置、键盘与原生窗口。七个自定义插件在 `MainActivity.onCreate` 注册，而不是全部放在静态插件 JSON 中。
2. **界面依靠语义变量保持统一。** 背景、卡片、文本、边界、强调色和动效时长使用 CSS 变量。移动端另设字号与容器规则；深浅色不是把颜色简单反转。
3. **手机更新是“自动检查 + 用户安装”。** 读取发布 JSON、比较版本、SHA-256 校验、拉起系统安装器。没有看到手机端“静默自动安装”的实现；包里的电脑端自动升级文案不能用于判断 APK 行为。
4. **通知可靠性由多层配合。** 通知运行时授权、通道设置、前台服务、电池优化和厂商设置是不同问题；原软件把它们拆成按状态显示的设置和一次性引导。
5. **后台存在独立原生连接。** `KeepAliveService` 组织 `NotifyChannel`、`MachineAttentionNotifier` 和 `WakeSources`，不把所有后台送达都寄托在 WebView 的 JS 计时器上。

## 已交付覆盖

| 范围 | 本次成果 | 证据强度/边界 |
| --- | --- | --- |
| 原始前端 | 提取所有 `assets/public` 文件，格式化主 JS/CSS，建立 482 个移动端文案键索引 | 确认是发行构建产物；没有原始 TSX、注释及项目构建配置 |
| UI | 登录、连接码、主界面空态、深浅设置、通知、更新截图及交互预览 | 真实前端；Android 桥接和配对资料为研究适配器提供 |
| 更新 | 前端策略/状态机、原生下载 Smali、安装权限和 FileProvider 调用 | 静态完整追踪；没有执行真实下载/覆盖安装 |
| 通知与保活 | 通知插件、通道、点击路由、后台长连接、凭据保存、引导策略 | 静态追踪；未做锁屏/厂商系统实测 |
| 自有原生类 | 导出 `ai.mirofish.mirasim` 包下 24 个顶层类，另导出相关库类，共 41 个顶层类文件 | 两个方法的 Java 反编译失败，另存 REA Smali；`R.java` 为资源索引 |
| Android 资源 | 解码 Manifest、XML、资源表 | 不代表恢复了原始 Gradle 项目 |
| 后端 | 列出客户端中出现的认证、网关、连接中转和更新地址 | 服务端实现不包含在 APK 中，未恢复 |

JADX 日志共加载 21,560 个含嵌套类节点；REA 包摘要与导出顶层类清单为 11,367。二者统计层级不同，不应相加或据此声称全部依赖源码均已导出。

## UI 的实际组成

它是面向电脑/空间及智能体会话的移动客户端：账号登录或扫码配对 → 选择空间 → 会话主界面；左侧会话/工作区抽屉，右侧设置与详情面板，另有聊天、文件、产物、变更和计划任务模块。复杂页面共享主 bundle 内的组件与数据状态。

以下可直接看图：

| 页面 | 图像 |
| --- | --- |
| 登录深色 | [截图](../preview/01-start-dark.png) |
| 设置深色（2x） | [截图](../preview/settings-dark/page-@2x.png) |
| 设置浅色 | [截图](../preview/settings-light/page.png) |
| 通知设置（Android 分支） | [截图](../preview/notifications-dark/page.png) |
| 更新设置（Android 分支） | [截图](../preview/update-dark/page.png) |
| 会话空态（虚拟配对） | [截图](../preview/home-offline/page.png) |
| 连接码页面 | [截图](../preview/pair-dark/page.png) |

## 不能从本次结果推断的事

- 没有原始 source map、TSX/Gradle 仓库及签名私钥，不能把格式化 JS 和反编译 Java 当成可直接重新编译的原项目。
- 未连接 Android 真机/模拟器；系统弹窗、后台服务存活时长、通知实际送达率、相机扫码与升级结果仍未验证。
- 前端可能包含桌面共享逻辑、未启用功能或版本迁移分支；每项产品行为要沿调用点判断。
- REA 的静态引用分析不证明反射或运行时所有调用都已穷尽；前端语义图也受资源预算限制，详见证据报告。

## 依据入口

- [REA 包声明](../evidence/rea-android-package.json)、[Manifest](../formatted/java/AndroidManifest.xml)。
- [原生插件注册](../formatted/java/ai/mirofish/mirasim/MainActivity.java)。
- [APK 前端入口](../extracted/apk/assets/public/index.html)、[Capacitor 配置](../extracted/apk/assets/capacitor.config.json)。
- [前端插件索引](../evidence/frontend-plugins.json)、[移动函数索引](../evidence/frontend-functions.json)。
