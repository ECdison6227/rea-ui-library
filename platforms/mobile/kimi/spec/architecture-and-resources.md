# Android 入口、主题与资源锚点

本页记录 APK 的静态证据。**B** 表示包内声明、标识符或反编译结构；**C** 表示由这些证据形成的候选解释。静态存在不等于运行中出现，颜色、尺寸与字体还需要逐个页面的截图或布局证据确认。

## 制品身份

| 项目 | B：包内声明 |
| --- | --- |
| 文件 | `Kimi.apk`，41,038,830 bytes |
| SHA-256 | `9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332` |
| 包名 | `com.moonshot.kimichat` |
| 版本 | `versionName=3.1.3`，`versionCode=376` |
| SDK | `minSdkVersion=26`，`targetSdkVersion=36`，`compileSdkVersion=37` |
| Application | `com.moonshot.kimichat.KimiApplication` |
| Launcher | `com.moonshot.kimichat.MainActivity`，`MAIN` + `LAUNCHER`，`exported=true` |
| Launcher 窗口 | `Theme.Splash`，`launchMode=1`，`windowSoftInputMode=0x10` |

来源：[本地 AXML 解码](../evidence/source/manifest-identity.json)、[完整声明树](../evidence/source/manifest-decoded.json)，以及 [REA/JADX 独立解码](../evidence/rea/android-package.json)，Evidence `ev_f54862ab42ec203f39f537bb2c0380f5944c173f698f357002a896e29718b04e`。两个解码路径的包名、版本、SDK、完整权限名集合一致；见[交叉核对结果](../evidence/source/metadata-crosscheck.json)。本地生成的 XML 已通过 .NET XML 解析。Launcher 位于原始 `AndroidManifest.xml` byte offset **60284**，原始 XML line **366**。

Manifest 共声明 40 个 activity/activity-alias、41 条 uses-permission；其中相机、录音、媒体读取、通知、定位、生物识别与多个厂商推送权限仅说明请求范围。授权状态、实际使用和具体提示时机需要运行时证据。`allowBackup=false`、`usesCleartextTraffic=false` 也只是静态配置，未做网络策略或签名验证。

## 主入口与 Compose 线索

`MainActivity.onCreate(Bundle)` 有完整 Java 反编译结果，无 smali 回退或截断。来源：[REA 方法证据](../evidence/rea/main-activity-oncreate.json)，Evidence `ev_6a94a0cef894d2a9637a894c900d26cc48773427e5effead1165efc25298f222`；可读副本：[main-activity-oncreate.java.txt](../evidence/source/main-activity-oncreate.java.txt)。精确方法原型 `(Landroid/os/Bundle;)V`，overload index **0**。原始 DEX 位置：`classes2.dex` method index **16184**，code_item offset **3783584**；类 class_def offset **1516688**。

该方法的 B 级结构包括：

- 调用 `super.onCreate`，根据 `screenLayout & 15` 是否大于等于 3 选择请求方向 `-1` 或 `1`。
- 尝试通过反射把 `ScrollableTabRowMinimumTabWidth` 和 `HorizontalTextPadding` 写为 `0f`，均有异常处理。这只能证明存在兼容处理，不能证明每次成功或所有标签页无内边距。
- 启动若干混淆名的生命周期协程，再调用 `ln.n.h(this, new j3.c(new v2(0, this), true, 1927369060))`，随后分发当前 Intent。

[MainActivity 成员清单](../evidence/rea/main-activity-class.json)，Evidence `ev_fe47d402e256ace59096aba220301b23b588ea636c302885a2f980c8c3648baa`，保留 9 个方法与 4 个字段。[内容入口成员清单](../evidence/rea/content-entry-class.json)，Evidence `ev_da9fe71e2aea83a9b02d539788346998937885508b1457afd54a6ac7a658a655`，把 `ln.n.h` 精确为 `(Lj/i;Lj3/c;)V`、overload index 0。

继续聚焦两级安装函数后，B 级调用链明确为：

```text
MainActivity.onCreate(Bundle)
  → ln.n.h(j.i, j3.c)
  → e.b.a(d.l, j3.c)
  → ComposeView.setContent(cVar)
  → Activity.setContentView(composeView, layoutParams)  [需要新建时]
```

`ln.n.h` 仅将参数包装进 `ns.b` 后传给 `e.b.a`；后者复用现有 `ComposeView`，否则新建 `ComposeView`、设置内容和生命周期 owner，再安装到 Activity。由此可以确认**主入口的静态实现安装 Compose 内容**，不再只依赖文件名推断。未从混淆类名推断导航结构、页面层级或主题状态机。

来源：[ln.n.h 方法](../evidence/rea/content-entry-setcontent.json)，Evidence `ev_263ec29b0e00173a87fd75c912f8f1f09b8d0019efe5c9812173360e05f31eef`；[e.b 成员清单](../evidence/rea/activity-content-class.json)，Evidence `ev_3536671d8f0399f53e9cc5f323197a4241357a69e06fb753103f24ad8e0605ad`；[e.b.a 内容安装方法](../evidence/rea/activity-content-setcontent.json)，Evidence `ev_1352896304865e17159c34c6878abfd2ab52c39a56381f10ae065567c24dd8a0`。两个方法均为完整 Java 结果，无回退或截断，overload index 均为 0。`e.b.a` 精确原型 `(Ld/l;Lj3/c;)V`。

## Native 主题链

以下为 `resources.arsc` 中的 B 级继承与引用：

```text
Application: AppTheme → AppBaseTheme → Theme.AppCompat.DayNight.NoActionBar
MainActivity: Theme.Splash → NightAdjusted.Theme.Splash → Theme.SplashScreen
Theme.Splash: postSplashScreenTheme → Theme.kimi
Theme.kimi → NightAdjusted.Theme → Theme.AppCompat.DayNight.NoActionBar
```

`AppBaseTheme` 只覆盖 `0x010100ae` 为 `Animation.Activity.LeftRight`，并不能据此说明整个产品的页面转场。默认和 night 主题父项不同。`NightAdjusted.Theme.Splash` 在 API 31、33 变体中引用不同的日/夜启动图标；这些是启动窗口声明，不是聊天界面的完整主题。

| 资源 | ID | 配置/值 | `resources.arsc` byte offset |
| --- | --- | --- | --- |
| `AppBaseTheme` | `0x7f120009` | parent `0x7f120143` | 2582644 |
| `AppTheme` | `0x7f12000a` | parent `0x7f120009` | 2582672 |
| `Theme.Splash` | `0x7f120154` | post theme `Theme.kimi` | 2592848 |
| `Theme.kimi` | `0x7f120158` | parent `0x7f1200e3` | 2593064 |
| `splashscreen_background` | `0x7f06008d` | 默认 `#FFFFFFFF` | 1695380 |
| `splashscreen_background` | `0x7f06008d` | night (`uiMode=0x20`) `#FF000000` | 1696292 |

来源：[资源锚点](../evidence/source/app-resource-anchors.json)与[全部资源定义](../evidence/source/resources-decoded.json)。资源表 SHA-256：`d380cbfdb54c999f1e9eb2ee8b841fbb46278325981f054e360daa63c0a4eda7`。JSON 的 config 保存完整原始 `ResTable_config` 字节及部分解码字段，未模拟 Android 的完整配置匹配算法。

## 颜色与尺寸候选

这些命名资源值得与实际页面核对，**不能直接提升为全产品 token**。它们可能属于旧页面、厂商 SDK、依赖库或仅用于局部原生窗口。

| B：资源名 | ID | 声明值 | `resources.arsc` byte offset |
| --- | --- | --- | --- |
| `bgTertiary` | `0x7f060023` | `#FF262626` | 1694868 |
| `blue_450` | `0x7f060026` | `#FF3375FF` | 1694884 |
| `colorAccent` | `0x7f060037` | `#FF39A6FF` | 1694932 |
| `labelsPrimary` | `0x7f060065` | `#D6FFFFFF` | 1695028 |
| `lightBgPrimary` | `0x7f060066` | `#FFFFFFFF` | 1695044 |
| `lightLabelsPrimary` | `0x7f06006a` | `#E5000000` | 1695060 |
| `kdc_corner_large` | `0x7f070091` | 16 dp | 1699448 |
| `kdc_corner_medium` | `0x7f070092` | 10 dp | 1699464 |
| `kdc_corner_normal` | `0x7f070093` | 12 dp | 1699480 |

颜色为 Android `#AARRGGBB` 顺序。`kdc_*` 的组件归属和使用位置尚不明。`material_*`、`abc_*`、`emui_*` 等依赖默认常量保留在原始证据中，未当作 Kimi 品牌色。

资源表共解码 **39,983 条配置变体、4,134 个不同资源 ID**；其中颜色 67 条/66 ID、尺寸 91 条/75 ID、style 281 条/258 ID、string 37,652 条/2,672 ID。大量本地化导致变体数较高。REA 的 `resource_count=1639` 来自另一种引擎统计口径，不能与资源表条目数直接比较。

## 字体

APK 中 `assets/composeResources/kimi.shared.generated.resources/font/` 精确存在以下 6 个字体成员：

- `MiSansLatin-Regular.otf`、`MiSansLatin-Medium.otf`、`MiSansLatin-Demibold.otf`
- `GeistMono-Regular.ttf`、`GeistMono-Italic.ttf`
- `PixelifySans-Regular.ttf`

来源：[资源锚点 fontMembers](../evidence/source/app-resource-anchors.json)，含每个 ZIP 成员的大小和 central-directory offset。`resources.arsc` 未解码出 font 类型定义；上述字体是 Compose assets。另有 jlatexmath 公式字体，不能等同于正文 UI 字体。B 级证据仅证明打包存在；正文、代码、装饰数字的字重和映射仍需调用点或运行时证据。

## 功能入口候选与边界

Manifest 与 DEX 同时保留以下组件名，可以作为后续场景定位锚点；功能解释仍标为 C：

| B：包内组件 | C：后续可调查的问题 | Manifest offset |
| --- | --- | --- |
| `auth.AuthActivity3` | 登录/鉴权流程 | 59724 |
| `media.MediaSelectActivity` | 附件/媒体选择 | 59944 |
| `ui.KimiFilePreviewActivity` | 文件预览的关闭、分享与错误态 | 61484 |
| `table.KimiLandscapeTableActivity` | 宽表格横屏展示 | 61344 |
| `webview.KimiWebViewActivity` | Web 内容与原生容器关系 | 61204 |
| `route.ExternalLinkActivity`、`share.image` alias | 系统分享入口与图像文本导入 | 62364 / 67344 |
| `chat.rtc.service.KimiCallService` | 语音/实时通话状态与后台服务 | 69144 |

`KimiWebView` 类继承 `android.webkit.WebView`，并保留 `getJsBridge`、`getAssetLoader`、`getWebPageState` 等方法名，属于 B 级标识符证据；桥接消息格式、实际页面范围与行为尚未跟踪。

原始 3 个 DEX 的 class_def 总数为 **30,501**；仅 38 个类保留 `com/moonshot/kimichat/` 或 `kimi/` 前缀，其余多为混淆或依赖类。JADX package overview 报告 26,752 个类，应保留其引擎口径，不把它等同于原始 DEX class_def 数。SourceFile 大量为 `r8-map-id-…`，并非可恢复的原始 Kotlin 文件名。标识符清单不能证明功能覆盖完整。

来源：[classes2 自有命名候选](../evidence/source/classes2-own-candidates.json)及 `classes*-class-inventory.json`。所有候选清单均保留原始 DEX 文件名、SHA、class_def offset、method index、code offset 和原型。未进行全包 Java 反编译。

## 可复现工具与命令

静态解析脚本：[analyze-apk-static.cjs](../tools/analyze-apk-static.cjs)。仅用 Node 内置 `fs`、`zlib`、`crypto`；此轮 Node **v24.21.0**。直接按 ZIP 的精确成员名读取，避免 Windows 的大小写文件名碰撞，不执行应用、不安装全局工具。

```powershell
node research/kimi-android/tools/analyze-apk-static.cjs Kimi.apk research/kimi-android/evidence/source
node research/kimi-android/tools/verify-static-metadata.cjs
```

REA 聚焦复现脚本：[inspect-android-focused.ps1](../tools/inspect-android-focused.ps1)，仅设置并恢复当前进程的 `JAVA_HOME` 和 `REA_JADX_MCP_JAR`。本机已有工具最初未加入 PATH，工具链实际存在，无需重新安装或改全局设置。

| 工具 | 此轮记录 |
| --- | --- |
| REA CLI | 6.5.0 |
| Java | Temurin OpenJDK `21.0.12.1+1-LTS` |
| JavaHome | `C:/Users/edsjrcheng/.rea/toolchains/android/jdk-21` |
| JADX MCP | `0.7.1`，source revision `5844800a486d2248fa949b74c9896285f5b54de2` |
| JADX JAR | `C:/Users/edsjrcheng/.rea/toolchains/android/jadx-mcp/jadx-headless-mcp-0.7.1-all.jar` |
| JADX JAR SHA-256 | `6e5eacf500b64292bfb73c49797c1958f6ee44646e43e868039ae7feb573ff75` |
| Worker / heap limit | 1 / 8140 MiB |

所有 REA JSON 保留完整 Evidence，而非终端截断预览。方法反编译仍可能受混淆与 JADX 恢复限制；尚未验证签名、动态加载、反射调用覆盖或系统组件真实执行。运行时观察应记录在对应页面证据中，不能由本页静态数据代替。
