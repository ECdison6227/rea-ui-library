# Android 原生参考

这是独立编写的研究参考，包名为 `study.style.reference`，与原 App 分开安装。使用 Android 原生 View，最低 Android 8.0/API 26，目标和编译平台 API 35；不依赖 Gradle、AndroidX 或第三方库。

## 构建

在 Windows PowerShell 5.1 或 PowerShell 7 中运行：

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1
```

脚本优先使用 `ANDROID_SDK_ROOT` / `ANDROID_HOME` 与 `JAVA_HOME`，随后尝试当前用户 Android Studio SDK 和 REA 局部工具目录。换到其他电脑时可显式传路径：

```powershell
.\build.ps1 -SdkRoot 'D:\Android\Sdk' -JavaHome 'D:\Java\jdk-21'
```

需要 JDK 21，以及 Android SDK 的固定组件 `platforms;android-35`、`build-tools;35.0.0`。已有 SDK 缺组件时，在其 command-line tools 目录用官方管理器安装，并正常接受许可证：

```powershell
sdkmanager --sdk_root='D:\Android\Sdk' 'platforms;android-35' 'build-tools;35.0.0'
```

`-BuildToolsVersion`、`-CompileSdk`、`-MinSdk`、`-TargetSdk` 可覆盖默认值。改变这些参数会改变参考运行环境，应重新做原版对照。脚本只改变当前进程环境；不写用户/系统环境变量。

Windows AAPT2 无法可靠处理当前仓库名中的中文路径，因此脚本自动把输入放入系统临时目录的唯一子目录，完成签名和校验后复制结果回工程并清理暂存。若另一台电脑的临时目录本身含非 ASCII 字符，可传 `-BuildWorkRoot 'D:\Temp'`。构建失败时暂存目录保留，位置记在 `build/build.log`。

构建顺序是 `aapt2 compile/link → javac → d8 → zipalign → apksigner`。结果为 `build/style-reference.apk`，签名和对齐会在构建末尾验证；日志与输入哈希分别在 `build/build.log` 和 `build/build-info.json`。`.local/debug.keystore` 是仅用于本地研究的调试签名，每次构建复用。不得将它当作发布签名。

`javac --release 8 -classpath android.jar` 使用 JDK 的 Java 8 编译签名，并引入 Android API；D8 保持默认的语言 desugaring，根据 `--min-api` 处理 lambda 等语言特性。此工程不提供额外的 Java 标准库 API desugaring，因此新实现使用 Java API 时仍需遵守 Android 最低版本的支持范围。

## 实现入口

原生实现放在 `src/study/style/reference/MainActivity.java`；资源在 `res/`，可选资产放在 `assets/`，构建自动打入。`ReferenceTheme` 提供无 ActionBar 的基础；页面 Activity 负责按页面和主题设置背景、状态栏、导航栏和明暗图标。

主题显式关闭 API 35 的强制 edge-to-edge，以便按观察样本保持系统栏占位；这是一项参考环境选择。原 App 在本次 Android 15 模拟器的系统栏外观由页面实际运行证据校对，不能从主题文件推断原 App 内部实现。

原版截图、交互证据、页面/组件规格和差异表见上一级研究入口。Android 构建成功只证明参考可构建，不证明与原版一致。

## 安装、打开和复测

```powershell
adb -s emulator-5580 install -r build/style-reference.apk
adb -s emulator-5580 shell am start -S -W -n study.style.reference/.MainActivity --es scene mine --es theme day
```

页面 `scene`：`health`、`mine`、`settings`、`components`、`transfer`。主题 `theme`：`day` / `night`。最终三页与十组件对照只验浅色，夜间值存在不能当作夜间已校准。

`MainActivity.java` 组合页面和本地业务适配，`StyleKit.java` 具有十组件及队列式前置条件sheet，`Tokens.java` 是语义色映射。代码不把页面截图当作可点击界面。图标、字体和背景的来源见 `assets-manifest.json`，完整资产另见 `../asset-library/`。

在仓库根目录复跑实际点击测试：

```powershell
$env:ANDROID_SERIAL = 'emulator-5580'
node analysis/ui/android-reference/verify-runtime.cjs
```

脚本只驱动 `study.style.reference`，保存新鲜UIAutomator层级、ADB操作和参考事件。`ADB` 可覆盖adb路径，`ANDROID_SDK_ROOT`可覆盖SDK路径。结果写 `../baseline/tests/result.json`，关联实际APK SHA；它证明参考行为可运行，不证明来源行为相同。现有首轮失败保留在 `tests/first-run-failed`：IME改变弹窗位置导致自动点击坐标过期，后续脚本等待稳定输入布局再读取坐标。

资料管理数据是当前会话fixture。二级详情和认证SDK用清楚标明的事件边界；接入产品时必须换成目标自己的路由与平台服务。特定模拟器的inset修正、字体映射、形状近似、持久化边界见 `../baseline/implementation-notes.md`。
