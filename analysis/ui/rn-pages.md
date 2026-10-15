# 第一方 RN 页面、交互与可迁移规格

这份分析直接读取 `base.apk` 内置的 sport、mine、dial、common JavaScript bundle，静态恢复 Metro/YRN 模块、路由注册、组件调用、条件分支和样式。重点是可用于实现的页面规格及复用规则。没有执行 bundle，没有设备截图、录屏或真机尺寸测量。

机器可读版本见 [rn-pages.json](rn-pages.json)。原文定位见 [rn-evidence-map.json](rn-evidence-map.json)，精选模块在 [rn-evidence](rn-evidence/)。文中的“已确认”指安装包代码证据，并不表示已看到实机画面。

**后续运行时修订：** [MineTab 交叉验证](runtime-rn-crosscheck.md)已确认页头标题为空格、快捷入口存在四项与五项两个运行变体。进程日志、新运行包 hash 与窄 AST 比较共同证实：mine 从内置 11.190.11 切换到 11.202.21，新增奖品、重排顺序并删除行内水平 padding。本文四入口属于内置版本基线；不要归因于深色主题或写成固定数量。

## 1. 证据边界与单位

| Bundle | 已解析模块 | SHA-256 |
|---|---:|---|
| sport | 1,350 | `daba176f2d9c4727959b5bb0bee65e68528fcbef3783a9cb0a117bd4b8c43f0d` |
| mine | 643 | `eb58f7d3ff59bcd96a4bb36394f9686adc159cca3ab0fb6511291785ca3b9f90` |
| dial | 719 | `cb02e657302f1e0420916c5e94439759a485e8bd4ce9da9e3dca06e1d2c42566` |
| common | 2,471 | `4c344f25be9acd7d1351516b87b51032f6a54cd80d860dd63bafda51e6fc5425` |

共 5,183 个模块全部通过 Babel AST 解析，168 个精选模块已与原 bundle 对应行逐字复核。`common` 包含 React、NativeBase 等第三方代码；导出某个组件不等于 App 实际使用它。下面的页面与模式都给出第一方调用证据。

数值必须按来源理解：

- RN `StyleSheet` 的数值是逻辑尺寸，不能称为实机像素。
- NativeBase 的 `w`、`px`、`rounded` 等 props 会查询 `sizes`、`space`、`radii`。例如 `w:9` 查得 36，`px:3` 查得 12；没有对应键的 `height:44` 回退为 44，并非全部乘以 4。
- 显式 `"12px"` 不会被当成数字 token 12。复刻时应统一转换成目标技术栈的逻辑单位，而不是直接复制所有字面量。
- 解析依据：[common-1901](rn-evidence/common-1901.js)、[common-1902](rn-evidence/common-1902.js)、[common-1912](rn-evidence/common-1912.js)、[spacing 表](rn-evidence/common-1913.js)、[圆角表](rn-evidence/common-1910.js)。

## 2. 适合迁移的视觉骨架

### 明暗主题

| 用途 | 浅色 | 深色 |
|---|---|---|
| 页面/顶栏底色 | `#F6F7F9` | `#000000` |
| 卡片/通用内容底色 `bgColor` | `#FFFFFF` | `#191919` |
| 主文字 | `#000000` | `#FFFFFF` |
| 次文字 | `#00000066` | `#FFFFFF66` |
| Tab 非选中文字 | `#00000099` | `#FFFFFF99` |
| 品牌/主动作 | `#FF612E` | `#F04D18` |
| 主动作按下 | `#e5571f` | `#cc4416` |
| 主动作光晕 | `rgba(255,97,46,0.2)` | `rgba(240,77,24,0.2)` |
| 链接 | `#0093ff` | `#0093ff` |
| 错误 | `#FF3B30` | `#FF3B30` |
| 禁用 | `#cccccc` | `#555555` |
| 浮层底色 | `#FFFFFF` | `#2C2C2E` |

来源：[sp-293 useThemeValue](rn-evidence/sport-sp-293.js)、[sp-311 语义色](rn-evidence/sport-sp-311.js)。完整表在 JSON 中。`panelColor`、Toast、Skeleton 等存在独立深色值，不能仅把浅色反相。

主题由系统驱动，`initialColorMode:"system"`、`useSystemColorMode:true`；不是只提供一个深色开关。[sp-130 Provider](rn-evidence/sport-sp-130.js)、[sp-132 theme](rn-evidence/sport-sp-132.js)

### 字体与层级

| 实际角色 | 规格 | 已确认范围 |
|---|---|---|
| 页面大标题 | Android 32 / iOS 30；light family | 运动/我的共用 TabViewContainer |
| 用户名 | 20 / 500；一行，尾部省略 | 我的用户资料 |
| 基础详情页顶栏 | 20 | BaseScreen 默认标题 |
| 卡片分区标题 | 17 / bold | SectionCard 默认值 |
| 设置行标题 | 16 / bold（700） | 我的设置菜单；bold 优先于同时传入的 500 |
| 顶部 Tab | 16；左右文字内边距 10 | 运动 TabBar |
| 快捷入口 | 14 / 400；一行 | 我的四入口 |
| 用户资料描述 | 12 / 400；` | ` 分隔 | 性别/身高/年龄 |
| 数据卡主值 | 18 / 600 | 训练指标卡 |
| Toast | 16 | 第一方 Toast hook |

明确的 Android family 映射是 `mipro-light/normal/medium/demibold/semibold/bold/heavy`，iOS 对应 PingFangSC 系列。大标题确实使用 `FONT_FAMILY.light`。[字体常量](rn-evidence/sport-sp-297.js)、[大标题调用](rn-evidence/sport-sp-724.js)

**不能把整个 App 统称为统一使用 MiSans。** 一些局部组件显式写 `MiSans`，多数 NativeBase Text 仅设置 size/weight；默认 `fonts.heading/body/mono` 为 undefined。App 还把 NativeBase Text 默认 `lineHeight` 显式设为 undefined。因此，不应推导出一个全局固定行高比例；应保留具体组件的局部行高，例如登录说明 26、会员说明 20。[sp-132](rn-evidence/sport-sp-132.js)、[common-1914](rn-evidence/common-1914.js)、[局部 MiSans 按钮](rn-evidence/sport-sp-478.js)

设置行是一个必须处理 prop 优先级的例子：`mi-490` 同时写 `bold:true` 和 `fontWeight:"500"`，NativeBase Text 实现先选 `bold ? "bold" : fontWeight`，主题中 bold=700。复刻时只读 `fontWeight:"500"` 会得到错误字重。[Text 实现](rn-evidence/common-2023.js)、[family/weight 解析](rn-evidence/common-2125.js)

### 尺寸与容器

常见内容区域左右 12，大标题另用左 26/右 20。主要分区卡片圆角 18；小训练指标卡圆角 14；Toast 圆角 12；底部登录面板圆角 28、内部按钮圆角 16。不要把这些不同层级压成同一个圆角。

SectionCard 默认白色/深色实底，圆角 18，左内边距 12，上 4、下 12；标题行上下 10，主标题 17 bold，右侧“全部”默认 14，加 28×28 的箭头。默认卡片间隔 12，部分列表通过覆写降为 10 或 0。[sp-175](rn-evidence/sport-sp-175.js)、[sp-860](rn-evidence/sport-sp-860.js)

这些是多个真实组件中的代表值，不是全 App 每处都遵循的唯一栅格。迁移时可以形成 4/6/8/10/12/14/16/18/20/24 等可用间距集，但“统一设计 token 系统”是复刻工程的整理方案。

## 3. 运动首页的实际页面树

注册链：`sp-136.Pages.SportHome → sp-453.SportHome → sp-710.default → sp-717.SportHomeContent`。这是第一方 screen 的调用链，不能用兼容原生 XML 推断当前 YRN 页面。

```text
SportHome
└─ SportHomeContent
   └─ TabViewContainer
      ├─ 大标题 + AddIcon                 basicMode 时不传标题/图标
      ├─ ScrollBar > TabBar              只有 tabs.length > 1 才出现
      └─ Animated PagerView
         └─ 每个已访问 Tab 的 ErrorBoundary
            ├─ SportTab
            ├─ PlanTab
            ├─ CourseTab
            └─ Weight 页
```

证据：[注册表](rn-evidence/sport-sp-136.js)、[screen 导出](rn-evidence/sport-sp-453.js)、[入口](rn-evidence/sport-sp-710.js)、[组件树](rn-evidence/sport-sp-717.js)、[Tab 类型分发](rn-evidence/sport-sp-731.js)

顶部类型可由服务端 `topTabs` 返回，按 `type` 匹配运动/计划/课程/减重。未登录、非中国区或失败时，默认只有一个 label 为空的 Sport tab；不能固定声称所有账号都有四个顶部 Tab。[sp-712](rn-evidence/sport-sp-712.js)、[sp-716](rn-evidence/sport-sp-716.js)

| 区域 | 结构与参数 |
|---|---|
| 顶部 Tab 区 | 高 30，上间隔 6，横向 margin 18 |
| Tab 项间距 | 默认 24 |
| 指示器 | 宽 18，高 4，圆角 2；跟随 pager 连续插值 |
| Pager | 顶部 margin 16；已访问页面保持挂载 |
| SportTab 正文 | 横向 padding 12；SectionList 分区 |

来源：[sp-729](rn-evidence/sport-sp-729.js)、[sp-438](rn-evidence/sport-sp-438.js)、[sp-439](rn-evidence/sport-sp-439.js)、[sp-893](rn-evidence/sport-sp-893.js)、[sp-860](rn-evidence/sport-sp-860.js)

已登录中国区运动页的代码顺序是：运动分类入口 → 最近运动记录 → 训练指标 → 跑团 → 运动路线（有路线或 `aiOpen` 时）→ 推荐活动 → 服务端 Banner。AndroidOut 还有条件模块。basicMode 只保留分类；未登录非 basic 保留分类、记录、指标；非中国区移除多项运营内容。[sp-906](rn-evidence/sport-sp-906.js)

交互不能只实现跳转：

- Tab 点击调用 setPage，横向拖动同步下划线位置/文字透明度；pager 非 idle 时不响应再次点击。
- AddIcon 是 36×36，打开锚定菜单。菜单宽 167.5、右侧 20，top 由锚点实际位置测出，选项为添加设备/扫一扫。点击后先关闭菜单，再验证登录，再走 native route；iOS 延迟 200 ms。
- 下拉刷新只在登录时接入 SportTab，会触发设备同步和相关数据更新，并在 finally 结束 refreshing。页面重新可见、登录事件或坐标改变还会静默刷新。
- 中国区登录态可申请位置；代码有 pre-request modal，不能直接跳成系统权限弹框。
- 指标区有多项时横向列表、单项时单卡；空指标不显示。新出现的指标延迟 2 秒后，用 500 ms 从高度 0 展开到测量高度。

对应证据：[Tab 状态](rn-evidence/sport-sp-438.js)、[AddIcon](rn-evidence/sport-sp-718.js)、[菜单](rn-evidence/sport-sp-719.js)、[图标](rn-evidence/sport-sp-722.js)、[选项](rn-evidence/sport-sp-723.js)、[刷新/位置](rn-evidence/sport-sp-893.js)、[指标渲染](rn-evidence/sport-sp-856.js)、[新内容展开](rn-evidence/sport-sp-855.js)

## 4. 我的页面：按账号/地区组合，而非一张固定长列表

当前 RN screen：`mi-136.Pages.MineTab → mi-412.MineTab → mi-423.MyProfileMapPage → RegionPageMap[region][login]`。`mi-379` 的 Mine 另有注册，不能拿它覆盖 MineTab 结构。

CN 已登录顺序：用户资料（带勋章捷径）→ 活动/课程/订单/亲友四入口 → 会员卡 → Android 支付卡 → 小习惯与周报/活力赛卡组 → 任务中心 → 广告 → 健康咨询 → 设置菜单。多个运营模块内部还会根据数据决定是否出现。海外组合明显更短。[mi-423](rn-evidence/mine-mi-423.js)、[mi-512 组件导出](rn-evidence/mine-mi-512.js)

```text
MineTab
└─ GlobalModalProvider
   └─ TabViewContainer
      ├─ MessageIcon + AddIcon
      └─ IOScrollView (paddingHorizontal:12)
         └─ 页面错误边界 > Suspense
            └─ MyProfileMapPage(region, login)
```

来源：[mi-412](rn-evidence/mine-mi-412.js)

| 组件 | 实现规格 |
|---|---|
| 用户资料行 | 外侧横向 13，上 10；头像 50×50/r25；头像与文本间隔 12；姓名 20/500；描述 12/400，单行省略 |
| 快捷入口（内置四项基线） | 图标 24×24，图标后 6；文字 14/400；等宽分格；后续运行时还有五项变体，见交叉验证 |
| 功能卡组 | 上间隔 12；两列，中间 10；右侧上下两卡间隔 10 |
| 设置分区 | 圆角 18，上间隔 12，垂直 padding 4 |
| 设置行 | 左/右 padding 14/10，my16；图标 26；文字 16/bold，左右 12；箭头 28 |
| 消息未读点 | 6×6/r3，图标右上；CN gate 与 AndroidOut gate |

来源：[资料](rn-evidence/mine-mi-624.js)、[快捷入口](rn-evidence/mine-mi-621.js)、[功能卡组](rn-evidence/mine-mi-528.js)、[设置组](rn-evidence/mine-mi-482.js)、[设置行](rn-evidence/mine-mi-490.js)、[消息入口](rn-evidence/mine-mi-632.js)

关键行为：

- 用户资料点击进入 native UserIndicatorsActivity；未登录点击走登录，未登录资料组件挂载时也会调用一次 showLoginPage。
- 四入口共用登录 guard；未登录先呈现 LoginModal，登录态才调用原 action。
- 设置行使用 `leading:true/trailing:false/wait:300` 节流，防止短时间重复压栈。
- 未登录仍可进入 App 设置、系统权限、关于；我的轨迹、数据管理、设备授权、反馈、设备共享等先走登录面板。
- 顶层资料请求失败时恢复地区对应的“未登录页面”；不是一张通用报错页。单独资料 fetch pending/failure 时保留空 UserProfileView 轮廓。

来源：[mi-623](rn-evidence/mine-mi-623.js)、[mi-628](rn-evidence/mine-mi-628.js)、[mi-510](rn-evidence/mine-mi-510.js)、[mi-483](rn-evidence/mine-mi-483.js)、[mi-491](rn-evidence/mine-mi-491.js)、[mi-422](rn-evidence/mine-mi-422.js)

## 5. 表盘市场：必须保留两个版本分支

`di-136.Pages.DialMarket → di-510`。`useEnableMarketV4()` 返回 true 用 `di-531`，false 用 `di-511`，尚未返回时渲染 null。[注册](rn-evidence/dial-di-136.js)、[分流](rn-evidence/dial-di-510.js)

| 项目 | 旧分支 di-511 | V4 分支 di-531 |
|---|---|---|
| 主分区 | 市场 / 我的，两页 | 首页 / 分类 / 我的表盘，三页 |
| 导航位置 | 顶栏 Tab + 搜索 | 底部浮动模糊栏；顶栏 back/search 或“我的”操作 |
| 翻页 | PagerView，可拖动 | `scrollEnabled:false`；菜单 `setPageWithoutAnimation` |
| 列表 | Banner/捷径/三列专题 + 两列推荐流 | 推荐逻辑行以三表盘分组并插入 Banner；其他分类三列 |
| 首次加载 | 形状适配 Skeleton | 各子页控制；不能套用旧分支顶层 loading |

来源：[旧市场](rn-evidence/dial-di-511.js)、[V4](rn-evidence/dial-di-531.js)、[旧列表](rn-evidence/dial-di-514.js)、[V4 列表](rn-evidence/dial-di-546.js)

V4 底部菜单：背景区域高 129，左右 12、上 padding27；实际栏高 69、圆角18、边线0.5；三个等分按钮；图标24、图标下4、文字14 bold。浅/深色 BlurView 的 blurAmount 为10/15。这个底栏是表盘市场的内部导航，不能直接当成整个 App 原生底部导航规格。[di-555](rn-evidence/dial-di-555.js)、[di-557](rn-evidence/dial-di-557.js)

表盘预览 Skeleton 按设备形状适配：圆 96×96/full；方 94×110/r22；长方 92×131/r13；椭圆 64×161/r32。空态仅在 Banner、快捷入口、专题与推荐流都空时呈现；不能只因推荐数组为空就覆盖有内容的页面。下拉刷新保留列表，分页 threshold0.2；离线时不呈现继续加载文字。[di-524](rn-evidence/dial-di-524.js)、[di-514](rn-evidence/dial-di-514.js)、[di-546](rn-evidence/dial-di-546.js)

V4 将多个运营弹窗放入队列，涉及“专属打包 → 常规弹窗 → 新用户活动”以及持久化关闭状态。商品点击根据设备型号/能力路由到不同 native/RN 详情。安装动作先检连接、安装流空闲、网络、版本/能力，再进入 `idle → downloading → pushing → installing → finishing`；失败时复位并给出具体 Toast/连接或空间对话框。[di-531](rn-evidence/dial-di-531.js)、[di-340](rn-evidence/dial-di-340.js)、[di-438](rn-evidence/dial-di-438.js)

## 6. 可直接落地的交互模式

| 模式 | 代码参数 | 适用边界 |
|---|---|---|
| RN 详情导航 | PushFromRight，springFriction20、springTension200 | RN Navigator；不是全原生 App 的统一转场 |
| 顶部 Tab | 指示器按 position+offset 连续移动；文字 opacity `.8→1→.8`；非 idle 拒绝点击 | 运动等实际调用此 TabBar 的场景 |
| 勋章按压 | pressIn scale0.9/100ms；pressOut 等100ms后 spring→1，friction3/tension40；跳转等300ms | 勋章对象卡；可选迁移到类似物件卡 |
| 新指标显现 | 等2000ms，再用500ms展开 height0→测量值 | 原来无指标的新数据出现 |
| 营销弹窗 | translateY 从屏高 spring→0，tension50/friction7；退场300ms；黑色0.5遮罩；按下opacity0.7 | 表盘 popup |
| Toast | 底部，2000ms；新 Toast 前关闭旧 Toast；px15/py12/r12/border0.33/elevation5；字体16 | 第一方 Toast hook |
| 登录底部面板 | 外padding12、内容24、r28；说明16/400/行高26；双按钮间隔16/r16、字17/500；关闭后1000ms打开登录 | 我的登录 guard |
| 锚定操作菜单 | 动态 top；r16、padding11；选项paddingX16/Y16.5；点遮罩关闭 | 添加设备菜单 |

来源：[RN Navigator](rn-evidence/sport-sp-130.js)、[Tab](rn-evidence/sport-sp-438.js)、[勋章](rn-evidence/mine-mi-343.js)、[指标](rn-evidence/sport-sp-855.js)、[Popup](rn-evidence/dial-di-513.js)、[Toast](rn-evidence/sport-sp-292.js)、[登录面板](rn-evidence/mine-mi-484.js)、[菜单](rn-evidence/sport-sp-720.js)

包中也有一个 `useCollapse` 默认300ms、springDamping0.8 的通用 hook，但本次没有确认所选首页调用它，不能把这个默认值写成“全 App 折叠动画规范”。[调用候选目录](rn-motion-catalog.json)保留了更多动效检索结果，候选项并未全部完成调用链判定。

## 7. 跨业务复用时应保留什么

以下是基于证据整理的实现建议，属于迁移方案，不能当作小米官方设计规范：

1. 把风格拆成页面壳、视觉 token、组件、状态机与业务内容。替换健康名词、数据和路线，保留标题层级、边距、卡片轮廓、反馈和导航行为。
2. 页面用低对比中性底，卡片用实体底色；橙色主要承担主动作与选中态。图表/运营卡的其他颜色属于业务语义，不应无差别加入全局调色盘。
3. 大标题与内容卡片分开对齐；大标题左26，主体通常左12。保留外壳与内容两个节奏，避免把所有元素锁在同一左边线。
4. 采用代表性字号层级32/20/17/16/14/12，同时保留组件局部值。不要为“统一”强加全局固定行高、MiSans 或 letter spacing；这些没有全局证据。
5. 按层级选择导航：同页内容分区可拖动、指示器随手势；表盘V4这种不同主分区点击切换；详情右推。保持返回、滚动位置、已访问页挂载和刷新事件。
6. 对每个新业务组件写出 pending、refreshing、loaded、empty、partial、offline、未登录、权限拒绝、busy、error、success 中适用的状态。显示相应反馈，不能只有静态正常态。
7. 各场景保留合适加载方式：资料页先保留轮廓、商品市场用形状 Skeleton、可选分区无数据则隐藏。不要把所有加载都替换成全屏 spinner。
8. 动效用于反馈和解释空间变化；100ms按压、300ms弹层、500ms内容出现是不同模式。页面不要处处弹跳、呼吸或交错入场。
9. 登录/地区/设备能力都作为组件组合条件，让页面壳稳定、模块可配置；服务端活动页、优惠和 Banner 不适合写死为永远存在的 UI。
10. 保留代码中已有的无障碍语义：Tab选中/未选中标签、button role、RTL箭头镜像、一行省略、屏幕阅读器适配。不要用截图式绝对定位替代真实布局。

## 8. 尚不能据此承诺的精度

实机视口、最终字形与行盒、字体是否成功加载、系统状态栏/手势区、真实滚动阻尼、native 动画、远端最新页面/活动素材，都没有通过运行时图像证明。这里可以支撑一套非常接近的设计实现起点，不能宣称已达到所有页面像素一致。

原 APK 可能使用热更新后的业务 bundle；账号、地区、设备型号、能力、登录及基础模式也会改变页面。复刻应先固定目标配置，再将截图/录屏逐状态补齐。当前静态研究没有把插件示例 HelloWorld 或未调用的通用 SDK UI 认定为 App 页面。

复核文件：[rn-verification.json](rn-verification.json)。工具脚本：[rn-scan.cjs](tools/rn-scan.cjs)、[rn-build-spec.cjs](tools/rn-build-spec.cjs)。
