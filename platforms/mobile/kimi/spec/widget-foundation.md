# show_widget 内联内容基础样式

本页的颜色、排版和动效数值来自 APK 内 CSS 声明，等级为 **B**。它们的作用域是 **show_widget inline content**，不能作为 Android 原生主界面的全局主题、尺寸或字体结论。尚未通过运行确认这份 CSS 是否加载、是否被覆盖、暗色类何时切换、哪些变量实际参与渲染。

## 证据入口与来源边界

- 辅助编号 `LOCAL-WIDGET-001`，不是 REA Evidence ID。
- 原 CSS：[`files/widget_foundation.css`](../assets/original/assets/composeResources/kimi.shared.generated.resources/files/widget_foundation.css)。SHA-256：`62dae2d909235495a709d067eb261c79a0a134f3f24738518e6fadb95ff9ec27`。
- 原文件头部第 2 行写明“注入 show_widget 内联卡片的包装 HTML(见 ShowWidgetBlock.kt)”。这只是包内注释声明；本页没有声称已拿到或验证该 Kotlin 文件。
- 可导入的完整声明：[`tokens/widget-foundation.json`](../tokens/widget-foundation.json)。每条保留 selector、原变量名、原值、引用的变量、源行、原行哈希、源文件哈希、APK 哈希、B 等级与作用域。
- 提取工具：[`derive-style-data.mjs`](../tools/derive-style-data.mjs)。输出 light 119 条、dark 44 条声明，分别包含 118 / 43 个自定义属性以及 `color-scheme`。
- 核验：[`style-data-verification.json`](../evidence/static/style-data-verification.json) 已检查全部 163 条声明对应原行、原值及哈希，以及六字体的源文件哈希、表目录、187 条 name 原字节、OS/2 字重和 fvar 是否存在。没有使用独立字体引擎交叉验证 cmap，也没有进行运行字体验证。

CSS 文件头还声明它合并了设计 token 真值和别名，但研究库没有据此假定完整上游样式包存在，也没有拿这份内联内容资源替代尚未提取的原生 Compose 主题。

## 主题与语义别名

`:root` 为默认声明；`:root.dark` 只覆盖部分值。根据 CSS 语义，同一根元素带 `dark` 类时，仍保留未被覆盖的 `:root` 声明，别名中的 `var()` 会指向被覆盖的真值。这是对声明的解释，不是运行时 computed style 测量。

| 原变量 | `:root` 值 / 行 | `:root.dark` 值 / 行 |
| --- | --- | --- |
| `--seo-chat-Colors-KMBlue` | `#1783ff` / 16 | `#1a88ff` / 135 |
| `--seo-chat-Colors-Red` | `#ff3849` / 18 | `#ff4756` / 137 |
| `--seo-chat-Colors-Green` | `#16c456` / 19 | `#32ff7d` / 138 |
| `--seo-chat-Bg-Primary` | `#ffffff` / 26 | `#121212` / 145 |
| `--seo-chat-BgGp-Secondary` | `#ffffff` / 29 | `#1f1f1f` / 148 |
| `--seo-chat-Labels-Primary` | `rgba(0, 0, 0, 0.9)` / 31 | `rgba(255, 255, 255, 0.84)` / 150 |
| `--seo-chat-Labels-Secondary` | `rgba(0, 0, 0, 0.6)` / 34 | `rgba(255, 255, 255, 0.56)` / 153 |
| `--seo-chat-Separators-S1` | `rgba(0, 0, 0, 0.13)` / 47 | `rgba(255, 255, 255, 0.12)` / 166 |

原有别名包括 `--kimi-color-text-primary` → `--seo-chat-Labels-Primary`、`--kimi-color-surface` → `--seo-chat-Bg-Primary`、`--kimi-color-accent` → `--seo-chat-Colors-KMBlue`、`--kimi-color-danger` → `--seo-chat-Colors-Red`。这些链接本身是 B 级源码声明；将它们映射为其他产品的 token 名称属于 D 选择。使用时保留原始别名，不把 RGBA 先混合为某个背景色的十六进制结果。

CSS 中存在 `hover` 与 `active` 变量，证明有这些命名和值，不能据此断言 Android 触摸页面的状态行为、桌面 hover 能力或任何控件已消费这些变量。图表色也只在此作用域保留，不套用到未知页面。

## 排版、尺寸与 reset 声明

`--font-sans`（第 14 行）原值为 `Inter, -apple-system, BlinkMacSystemFont, "SF Pro Text", "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif`。`--font-mono`（第 15 行）原值为 `"SF Mono", Monaco, "Fira Code", "Courier New", monospace`。它们是 CSS 字体栈声明，不证明其中任何字体已安装、被嵌入或实际用于某个字符。尤其不能将它们与下面六个 Compose 字体的存在直接合并成同一个运行字体体系。

| 原变量组 | 字号 | 行高 | 原单位 / 源行 |
| --- | ---: | ---: | --- |
| `--seo-chat-ui-B1-*` | 15 | 22 | CSS px / 57–58 |
| `--seo-chat-markdown-H1-*` | 20 | 32 | CSS px / 59–60 |
| `--seo-chat-markdown-H2-*` | 18 | 28 | CSS px / 61–62 |
| `--seo-chat-markdown-B1-*` | 16 | 26 | CSS px / 63–64 |
| `--seo-chat-markdown-B3-*` | 14 | 22 | CSS px / 65–66 |
| `--seo-chat-markdown-code-*` | 14 | 22 | CSS px / 67–68 |

第 185–197 行的 `html, body` 声明包含宽度 `100%`、透明背景、横向 overflow 隐藏、字号 `16px`、`line-height: 1.5`、`font-synthesis: none` 和字体渲染相关属性。第 198 行 `body` padding 为 `8px`，第 184 行全元素使用 `border-box`；第 199 行表单控件继承 font，第 201 行禁用按钮光标为 `not-allowed`。这些是内联网页基础规则，不证明原生页面边距是 `8dp`、按钮触摸面积、原生无障碍字号或文本最终布局。

保留 CSS px，不做 px→dp/sp/物理像素换算；当前未获得 WebView viewport、devicePixelRatio、Android density、系统字体缩放、缩放设置与实际渲染尺寸。

## 动效声明及 reduced motion

| 原名 | 原值 | 源行 |
| --- | --- | --- |
| `--t-micro` / `--t-fast` / `--t-normal` / `--t-slow` | `60ms` / `120ms` / `200ms` / `300ms` | 83–86 |
| `--motion-popover-duration` / `--motion-popover-offset` | `140ms` / `4px` | 93–94 |
| `--motion-dialog-duration` / `--motion-dialog-exit-duration` | `180ms` / `135ms` | 95–96 |
| `--motion-view-duration` / `--motion-panel-duration` / `--motion-list-duration` | `180ms` / `240ms` / `180ms` | 97–99 |
| `--ease-standard` / `--ease-motion-standard` | `cubic-bezier(0.4, 0, 0.2, 1)` | 89 / 91 |
| `--ease-motion-panel` | `cubic-bezier(0.32, 0.72, 0, 1)` | 92 |

第 176–183 行的 `prefers-reduced-motion: reduce` 媒体查询把所有元素及其 before/after 的 animation/transition duration 设为 `0.01ms !important`、animation iteration 为 `1 !important`、scroll-behavior 为 `auto !important`。这个声明只提供 B 级可访问性策略线索。运行环境是否匹配该媒体查询、JS 动画是否遵循它、能否中断、退场属性、delay 和触发条件仍然未知。仅凭 token 名称不能宣称对话框确实播放了 180ms 动画，也不能据此反推原生 spring 参数。

## 六个打包字体的元数据

辅助编号 `LOCAL-FONT-001`，完整元数据在 [`assets/derived/fonts/metadata.json`](../assets/derived/fonts/metadata.json)，原件位于 [`font/`](../assets/original/assets/composeResources/kimi.shared.generated.resources/font/)。它们是另一个包内资源证据范围，不是上方 CSS 字体栈的运行验证。

采用 Node Buffer 读取 SFNT 目录，以及 `name`、`cmap`、`fvar`、`OS/2`、`post`、`maxp`。完整保留 name 记录的原字节 Base64、platform/encoding/language/name ID、文件位置和可解码文字；Unicode/Windows name 记录按 UTF-16BE 解码，非 ASCII 的 Macintosh 记录不猜测编码。本次所有 cmap 记录均为已支持的 format 4 或 6，表中的覆盖数只统计 Unicode cmap 中映射到非零 glyph 的码点并去重，不是字形质量或 shaping 测量。

| 原文件 | family / subfamily 声明 | OS/2 weightClass | Unicode cmap 码点数 | `post.isFixedPitch` | `fvar` |
| --- | --- | ---: | ---: | ---: | --- |
| `GeistMono-Regular.ttf` | Geist Mono / Regular | 400 | 796 | 1 | 无 |
| `GeistMono-Italic.ttf` | Geist Mono / Italic | 400 | 795 | 1 | 无 |
| `MiSansLatin-Regular.otf` | MiSans Latin / Regular | 330 | 380 | 0 | 无 |
| `MiSansLatin-Medium.otf` | MiSans Latin Medium / Regular | 380 | 389 | 0 | 无 |
| `MiSansLatin-Demibold.otf` | MiSans Latin Demibold / Regular | 450 | 380 | 0 | 无 |
| `PixelifySans-Regular.ttf` | Pixelify Sans / Regular | 400 | 574 | 0 | 无 |

MiSans Medium/Demibold 还声明 typographic family 为 `MiSans Latin`、typographic subfamily 分别为 `Medium` / `Demibold`。文件名、family、typographic name 和 OS/2 weight 是不同字段；尤其不能将 330/380/450 擅自写成 400/500/600，更不能直接推定 Compose 中如何为它们指定 FontWeight。Geist Italic 的 post italic angle 为 `-12`，其余五个为 `0`。无 `fvar` 只证明这六个具体文件没有 fvar 轴声明，不代表产品其他字体或平台字体没有可变轴。

六份字体对样本 `A`、`a`、`0` 均有 Unicode cmap 映射；对 `中`、`文`、`你`、`好`、`，`、`。`、`あ`、`한`、`😀` 均无映射。这里没有逐字运行字体结果；中文、日文、韩文及 emoji 实际采用什么 fallback、字重如何合成、行高如何计算，都仍为未知。迁移时不应把仅打包 MiSans Latin 视为已覆盖中文。jlatexmath 目录下的数学字体没有纳入这六份应用 Compose 字体的表格，不能静默混为界面字体。

## 可用的迁移候选和补证要求

C 候选：这个 widget 资源使用语义别名连接背景、文本、分隔线、强调与状态色，浅深主题分别保留灰阶和颜色值，排版区分基础 UI 与 Markdown。D 建议：网页内联内容可以先直接接入保留的 CSS 并按新的业务语义映射；原生实现需要另行选择与校验原生单位、字体、系统缩放和交互能力。

补证时首先在真实目标中找到能触发 `show_widget` 的内容，保存浅色和暗色原始截图及真实页面身份；确认渲染容器及 CSS 加载路径后，采集 computed style、viewport 与 DPR，再核对变量覆盖、中文和代码字符的运行字体。对一个真实可触发的过渡记录开始/结束属性、duration、delay、easing、中断及退出；测试 reduced motion 时记录系统设置与媒体查询结果。达到这些条件前，本页只可作为包内资源基线，不宣称完成原产品的视觉或行为还原。
