# 从小米运动健康提取的可迁移原生 UI 规范

适用对象：`com.mi.health` 3.57.0 中已解包的一方 Android 原生组件与兼容布局。目标是把视觉层级、组件结构和状态表达迁移到其他业务。本文给出静态证据可以支持的部分，不能代表所有页面已完成像素校准。模拟器实测结果由本目录其他报告另行补充；本文中的 Android XML 数值始终是资源声明值。

**路由校正：当前 `SportTabV4Fragment`、`MineFragmentV4` 的代码实际承载 YRN 页面。包内同名的 `sport_tab_v4_page.xml`、`mine_fragment_v4.xml` 不能作为当前运动/我的页的渲染证据。本文中这些原生布局相关的值仅属于旧分支/兼容资源；当前 RN 页应以 RN 规范和运行证据为准。**

机器可读交付：[design-tokens.json](design-tokens.json)、[components.json](components.json)、[tokens.css](tokens.css)。抽取程序：[extract-design-system.cjs](extract-design-system.cjs)。运行 `node analysis/ui/extract-design-system.cjs` 可以重建这三份文件，过程不运行 APK。

## 1. 证据分层与使用边界

| 层级 | 含义 | 如何用于新业务 |
|---|---|---|
| 资源事实 | XML 中明确声明的颜色、单位、字体族、尺寸、selector 顺序 | 保留来源，可直接作为候选设计值 |
| 代码事实 | 一方类确实绑定组件、变更选中态或执行回调 | 作为交互状态与组件 API 的依据 |
| 推导 | 例如 30dp 图标加上下 15dp margin 得到 60dp 内容块 | 标注条件，不升格成所有行的固定高度 |
| 迁移建议 | 根据已提取视觉与结构做出的跨业务实现选择 | 在新产品中确认，不声称原 App 就如此实现 |
| 未知 | 尚未解析的运行参数、系统字体回退、实际测量结果 | 留空或使用独立配置，不补造精确值 |

本轮选取 22 份布局、25 份一方文字/按钮样式、24 份实际绑定的 selector/drawable 依赖，生成 19 个语义颜色 token 和 123 项可追溯资源。没有把 APK 中全部 MIUI、Material、AppCompat、支付、账户和市场 SDK 的默认资源都视为本 App 的规范。`phone_home_category_*` 的根节点属于 `com.xiaomi.market.watchsdk`，因此没有作为健康首页规范采纳。

`components.json` 先列便于实现的 `componentContracts`，再保留 XML 节点与源码行。`design-tokens.json` 的每项值保留原 Android 表达式、日夜引用链、限定目录及源码行；带屏宽、横屏、API 版本等 qualifier 的定义保存在 `qualifiedDefinitions`。默认 `day/night` 只解析 `values` 与 `values-night`，不能替代 Android 的完整资源配置选择器。

## 2. Token 的层次

建议新业务采用三层映射：原始值 → 语义角色 → 组件局部值。

例如：`#ff612e` → `action-primary-background` → 主按钮背景。健康卡片的睡眠紫色、运动圆环渐变等属于业务可视化层，不能覆盖所有成功/警告/选择语义。主按钮的橙色可以作为同风格主题的品牌动作色，但数据图表配色应由新业务单独定义。

### 已确认的语义颜色

下表 alpha 均按实际十六进制字节换算，不能根据资源名猜测。细微差异保留在 JSON 中。

| 语义 | 日间资源值 | 夜间资源值 | 绑定来源 |
|---|---|---|---|
| 通用主内容背景 | `#fafafa` | `#000000` | `layout/main_activity_main.xml:4`，`main_fl_content` |
| 兼容原生我的页背景 | `#f4f5f8` | `#000000` | `layout/mine_fragment_v4.xml:2`；当前 V4 为 YRN，非其屏幕事实 |
| 通用卡片面 | `#ffffff` | `#191919` | `drawable/common_card_bg_white.xml` → `common_card_bg_color` |
| 兼容原生我的页卡片面 | `#ffffff` | `#191919` | `drawable/mine_label_bg_white.xml` → `mine_white` |
| 正文主色 | `#000000` | 白色约 90.20% | `text_color`，健康摘要卡与详情标题栏 |
| 摘要说明色 | 黑色 40% | 白色约 70.20% | `text_color_40`，健康卡 `tvHomeCardDesc` |
| 底导航背景 | `#ffffff` | `#242424` | `main_tab_bg` |
| 底导航未选文字 | 黑色约 30.20% | 白色约 45.10% | `main_tab_text_color_normal` |
| 底导航选中文字 | `#ff612e` | `#f04d18` | `MainActivity.selected` → `b5k.a` → `public.xml` → `colorPrimary` |
| 页头选中文字 | 黑色 | 白色约 90.20% | `HomeHeaderTabTitleView.bindTitle` → `b6k.o` → `text_color` |
| 页头未选文字 | 黑色 40% | 白色约 50.20% | 同方法 → `b6k.f` → `home_header_title_unselect` |
| 主按钮背景 | `#ff612e` | `#f04d18` | `CommonBtn.Primary` 实际背景链 |
| 主按钮文字 | 白色 | 白色约 89.80% | `selector_common_btn_primary_text_color` 默认项 |
| 主按钮禁用文字 | 白色 40% | 白色 40% | 同 selector 的 `state_enabled=false` 项 |
| 列表标题 | 黑色 | 白色 | `selector_feature_item_title_text_color` 默认项 |
| 列表说明 | 黑色 40% | 白色约 50.20% | `selector_feature_item_subtitle_text_color` 默认项 |

特别需要保留的原始值：`black_30_transparent` 实际是 `#60000000`，alpha 为 `96/255≈37.65%`；`white_30_transparent` 实际是 `#48ffffff`，alpha 为 `72/255≈28.24%`。这些值用于禁用列表文字。相同命名后缀在不同资源中可能表示“透明度”或“不透明度”，不能套用同一算式。夜间的 `text_color_40 → white_30` 实际解析为约 70.20% 白色。

Android 八位色是 `#AARRGGBB`；CSS 八位色是 `#RRGGBBAA`。例如 Android `#99000000` 应转换为 CSS `#00000099` 或 `rgba(0,0,0,0.6)`。`tokens.css` 已按此转换。

### 空间单位

保留原始 `dp/sp`。网页中 1dp 对应 1 CSS px 只是逻辑尺寸迁移约定，不能对应设备截图的 1 物理 px；`sp` 还受系统字体缩放影响。`46.659973dp`、`66.66998dp` 这样的值来自打包资源的精度，JSON 保留原值；若新业务希望约整为 46.67/66.67，需在适配层明示，不反写成原始事实。

没有证据支持“全 App 页面都用 16dp 左右边距”。兼容原生我的页布局存在 14dp 外边距，通用按钮样式为 26dp 两侧与下边距，组件内部又使用 16dp/18dp 等。应使用组件语义 token，而不是一个万能 gutter。

## 3. 字体与文字层级

| 角色 | 可追溯声明 | 实现注意 |
|---|---|---|
| 一级页签标题 | `32sp`、`sans-serif-light` | `layout_home_header_title_item.xml:2`；页面标题不是统一粗体 |
| 详情栏标题 | `18sp`、bold、单行居中 | `widget_title_bar.xml`；两侧各预留 60dp |
| 另一套顶部栏 | `20sp`、bold、栏高 `53.329987dp` | `widget_top_bar.xml`；与上一套并存，不能无条件合并 |
| 健康摘要卡标题 | `17sp`、`FontMedium` | `health_home_card_common_title.xml` |
| 健康摘要卡数据 | `18sp`、`FontMedium` | 同布局，业务大数值不应由它推断 |
| 健康摘要卡说明 | `13sp`、`FontNormal` | 同布局；空态说明是另一个 `14sp` 节点 |
| 列表主标题 | `FontMedium.16sp` | 单行组件最多 1 行；双行组件标题最多 2 行 |
| 列表说明 | `FontRegular.12sp` | 双行组件最多 4 行，末尾省略 |
| 列表右侧值 | `FontRegular.14sp` | 双行组件最大宽度 96dp、最多 2 行 |
| 主按钮文字 | `15sp` | `CommonBtn`，会被布局或代码覆盖 |
| 底导航标签 | `11sp`、`FontMedium` | `main_view_tab.xml` |

`values/styles.xml:2588` 起的字体映射：

- `FontNormal` → `mipro-normal`，没有额外 `textStyle`。
- `FontRegular` → `mipro-regular`，没有额外 `textStyle`。
- `FontMedium` → `mipro-regular` **并声明 bold**。
- `FontDemiBold` → `mipro-medium` **并声明 bold**。
- `FontLatin*` → APK 中的 `res/font/misanslatin_*.otf`。

因此，不能只根据 `FontMedium` 的名字就把所有中文映射成 CSS `font-weight:500`。它实际请求的是某个系统字体族再加 bold，最终字形与系统字体注册和回退有关。`FontRegular.13sp` 的资源值甚至是 `13.5sp`，应读取样式条目，不能从名字抽数字。

对包内字体做了 SFNT 表解析：MiSansLatin Regular/Medium/DemiBold/SemiBold/Bold/Heavy 的 OS/2 weightClass 分别为 400/500/600/600/700/900；每份 1100 glyph。Unicode cmap 的 `0`、`A` 探针存在，“一/健/康”探针均不存在。`roboto_medium_numbers.ttf` 仅 31 glyph，`0` 存在、`A` 与上述汉字不存在。这些是有限字形探针，不是完整字符覆盖审计，但已足以说明不能用包内 Latin 字体覆盖完整中文 UI。

新业务迁移建议：中文正文与中文标题保留两个独立的字体族/字重角色；数字和拉丁文可单独使用对应字体。真实手机上的小米系统字体与非小米设备的回退要分别校准。不要在尚未测量时宣称某个通用网页字体与原版等宽、等高；行高、字间距和 Android `includeFontPadding` 也不能仅从字号反推。

## 4. 可复用组件结构

### 底部导航

`main_activity_main.xml` 为纵向结构：可伸缩内容区 → 细分割线 → Material TabLayout。声明栏高 `66.66998dp`，水平 margin 为 0，顶部 padding 为 2dp，`tabGravity=fill`，`tabIndicator=@null`，ripple 透明。每项自定义 `main_view_tab.xml`：图标和标签垂直居中成组，间距 3dp；红点初始 `gone`。图标尺寸由实际 drawable 决定，不能从 `wrap_content` 推出 24dp。

`MainActivity.java:1273` 的 `selected()` 明确为选择与取消选择设置不同文本颜色，并兼容 TextView 和 ConstraintLayout 两种自定义项。混淆资源已通过 `b5k.java` 的常量与 `values/public.xml` 映射闭合：选中为 `b5k.a=2131099886=0x7f0600ee → colorPrimary`，取消为 `b5k.c=2131101379=0x7f0606c3 → main_tab_text_color_normal`。日间橙色 #ff612e，夜间 #f04d18。

跨业务保留：固定导航层级、图标+文字的紧凑纵向构图、无下划线、状态独立。替换：一级业务标签和图标、红点触发条件。不能让新业务标签的数量变化自动改变已确认的系统 inset 处理；底部安全区另设容器负责。

### 页头

`layout_home_header_tab.xml` 采用横向标题列表和右侧 36dp 菜单图标。标题组上下 margin 为 20dp；菜单距右 20dp；标题 item 自带 16dp 起始 margin。栏本身是内容自适应高度。

`HomeHeaderTabTitleView.java:91` 的选择方法先通知旧项更新，再写入 currentSelect、通知新项更新并执行监听；`bindTitle()` 仅设置文字和选中/未选颜色。`b6k.o` 已映射为 `text_color`，`b6k.f` 映射为 `home_header_title_unselect`。单纯从这里看不到标题缩放、下划线移动或统一转场时长，不能补造一套“原版标题动画”。

### 卡片容器

`common_card_bg_white.xml` 与 `mine_label_bg_white.xml` 均声明 16dp 圆角；日间白色，夜间 #191919。pressed、disabled、default 三个分支的形状与填色相同。这说明该 drawable 自身没有按下变色，不证明宿主类没有额外遮罩或缩放。

主摘要卡头部 `health_home_card_common_title.xml`：26dp 图标 → 相距 6dp 的 17sp 标题 → 18sp 值 → 13sp 说明。空态说明为另一节点，带右箭头且默认隐藏。饮食记录按钮、鼾声状态芯片都是可选业务插槽，默认 `gone`。迁移时保留“标题/值/说明/可选操作”的结构，替换健康专属字样和图标。

### 操作按钮

`CommonBtn` 默认 `match_parent` 宽、`46.659973dp` 高、文字 15sp、两侧及底部 margin 26dp。各布局可以覆盖这些值：例如体脂秤分享页直接改为两侧 20dp、底部 27dp，不能把样式默认值当所有页面实测值。

`CommonBtn.Primary` 的背景链为：`selector_bottom_btn_positive_bg` → `common_dialog_positive_btn_smooth` + `miuix_appcompat_coloured_btn_fg_light`。关键参数：

- 背景为 `miuix.appcompat.app.AdaptRoundButtonDrawable`，`miuix_useSmooth=true`。
- 普通半径链最终为 16dp；capsule 参数 36dp。实际选用取决于 MIUI drawable 实现，不能直接认定所有按钮都是 16dp 或 36dp 普通 CSS 圆角。
- 背景内在高度为 50dp，与宿主样式 46.659973dp 是不同概念。
- layer-list 声明左右安全 padding 4dp、上下安全 padding **4sp**；单位按包内原值保留。
- 已绑定前景有 normalAlpha=0、hoveredAlpha=.06、pressedAlpha=.12；实际颜色和动态响应仍依赖前景类。禁用标签由 ColorStateList 单独控制，不等于 disabled 后整个按钮统一 opacity=.4。

网页迁移可用真实按钮元素加背景层、前景遮罩层与标签层；把精确的 MIUI 平滑边角实现列为独立适配任务。未校准时只称“近似圆角”，不伪称完全一致。

### 设置/功能列表行

单行组件：30dp 前导图标、上下各 15dp margin、图标至文字 16dp；主标题 16sp，最多 1 行、end ellipsis；右侧 14sp 值、可选提醒、右箭头。30+15×2 只能推出“有图标时约 60dp 内容块”的条件结果；根布局是 `wrap_content`。

双行组件：主标题最多 2 行、说明最多 4 行、右侧值最多 2 行且宽度不超过 96dp。文字块顶部 margin `13.329987dp`，底部 XML 同样为 `13.329987dp`。提醒点为 7dp。它是可增长的内容容器，不能硬编码为 60dp。

`RightArrowBindingSingleLineTextView`/`TwoLine` 明确暴露 icon、title、subtitle、rightText、remind、showMore 和 enabled 等绑定方法。已进一步核对 `native-extra` 中两个父类：

- `setEnable(boolean)` 把 enabled 状态传给根、箭头、前导图标、提醒和标题；双行版还传给说明与右值，单行版此方法没有同步右值。
- `setDefaultEnable(boolean)` 在上一步之外，把前导图标 alpha 设为启用 1.0 / 禁用 .25。
- `allowClickWhenDisabled` 默认 false；若显式设为 true，根处于 disabled 时仍在 `ACTION_UP` 执行 `performClick()` 并消费事件。因此“颜色灰了”与“触摸绝对不可用”是两个独立契约。
- 双行版的 `setEnable(boolean,int drawable)` 重载仅修改标题 enabled 并替换箭头图，不等价于一个参数的方法。
- 两类的 `setArrowViewPadding()` 主体为空，不能根据方法名推测自动注入某个默认边距；`setViewPadding()` 直接转发整数至 `setPadding()`，实际单位与数值由调用处决定。

视觉禁用状态、点击能力与业务含义需要分别建模。新业务中允许点击的禁用态可用于展示解锁条件或解释原因，应由业务显式选择，不能全局默许。

跨业务组件 API 建议：`leadingIcon/title/description/value/badge/trailingAction/enabled`，避免把“步数/血氧/设备”等名词写入基础组件。

## 5. 交互与动效：当前证据能说到哪里

可确认的交互表达：

1. 页头用 currentSelect 管理选择状态；数据变化通知旧、新两个项，监听回传 position。
2. 导航选择有独立文本颜色；底导航 XML 没有选中指示条与可见 ripple。
3. 列表使用独立 enabled/default ColorStateList；可选信息由组件与代码控制。
4. 隐藏节点大量使用 `gone`，隐藏时通常退出 Android 布局测量，不等于保留一个透明占位。
5. 兼容原生 `mine_fragment_v4.xml` 使用 `SpringBackLayout + NestedScrollView`，内部滚动视图设 `overScrollMode=never`，该分支弹性由外层负责；不能据此声称当前 YRN 我的页使用相同滚动实现。迁移时应避免同时叠加两套弹性。
6. 健康首页 `forbiddenRecyclerItemAnimation()` 对匹配的 itemAnimator 调用混淆方法 `V(false)`。这是一条代码事实，具体方法语义仍需对应库解析，不能夸大为“全站禁用全部动画”。
7. `DataHeaderV2View.refreshAnimation()` 调用 `ThreeTargetView.g()` 与 `k()`；当前包装方法没有公开时长、曲线、弹簧参数。

包内有大量名为 dialog/activity/fade/slide 的动画资源，包含 SDK 自带内容。仅有资源存在不能证明当前健康首页会用它。本轮没有为全 App 编造 200ms/300ms 的统一 motion token。已绑定按钮前景的 0/.06/.12 alpha 属于状态资源事实，仍不能推出动画耗时。

新业务动效建议应单独标成“实现选择”：页面进入、返回、弹层、按下反馈、异步加载、列表刷新各设独立契约；用户触发与系统数据刷新分开处理；保存焦点与滚动位置；系统减少动画设置应能降低非必要运动。最终 timing/easing、手势阈值与回弹参数以运行实测或进一步追踪对应动画类为准。

## 6. 跨业务迁移准则

| 保留的风格骨架 | 需要替换的业务内容 | 应独立配置的行为 |
|---|---|---|
| 浅灰页底、白色卡片、日夜语义映射 | 健康分类名称和色彩含义 | 夜间启用条件和系统主题同步 |
| 大而轻的一级标题、较紧凑的卡片标题与说明 | 标题文案、主数据、单位 | 文案变长与字体缩放时的换行 |
| 卡片、功能行、少量主动作的视觉层级 | 指标内容、前导图标、空态文案 | 权限、登录、空态、失败、恢复 |
| 图标+11sp文字的底导航结构 | 一级模块与图标 | 导航历史、重选、深链、返回策略 |
| 可增长双行行、右值/提醒/箭头插槽 | 业务配置值与提醒来源 | 点击可用性和后端状态 |
| 独立按钮背景、前景、文字状态层 | 动作名称 | 防重提交、等待、完成、可取消性 |

落地时先实现容器和语义 token，再实现组件 API，再接业务数据与路由。不要在每个页面内重新手写颜色；也不要把健康数据可视化的圆环/图表强塞给所有业务。统计仪表可以使用“概览值/趋势/详情入口”这一信息结构，是否需要圆环由指标含义决定。

`tokens.css` 只声明已有绑定依据的候选语义值，不包含臆测的 global shadow、统一 transition、图标尺寸或通用按钮 radius。导航选中色已经代码常量映射确认。对新业务添加值时建议使用单独命名空间并标注来源为产品选择，便于与逆向事实区分。

## 7. 仍需校准的项目

- 实际使用的布局分支、运行时代码覆盖值、手势阈值与回弹曲线。
- 导航图标资源映射、图标实际尺寸和动画；选中文字颜色已静态闭合。
- mipro 在不同系统的真实回退、中文基线、数字字重、默认 font padding 与行高。
- 状态栏、手势栏、刘海、横屏、折叠屏和系统文字放大后的实际边距。
- MIUI AdaptRoundButtonDrawable 的平滑曲线及 capsule 选择算法。
- 登录/无设备/无数据/离线等状态下哪些卡片实际可见；可见性不能只看 XML 默认值。
- 弹层、返回手势和数据更新的视觉时序；不能靠静态截图验证交互时间。

这些项目与已提取的资源事实互补。补充设备或模拟器验证后，应更新对应证据列，不把未观测状态自动当作已验证。
