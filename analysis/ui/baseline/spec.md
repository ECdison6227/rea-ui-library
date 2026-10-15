# 三页、十组件实现规格

这是可迁移风格库的固定来源基线。先读 [README](../README.md)，运行和原版对照以 [对照入口](comparison/index.html) 与 [覆盖表](coverage.json) 为准。本文说明“应实现什么”，不以文档存在证明已还原。机器契约见 [contracts.json](contracts.json)，事件和业务边界见 [behaviors.md](behaviors.md)。

## 身份与证据规则

来源是 `com.mi.health` APK 3.57.0 / 357000，SHA-256 `2a80fc5a87353c2c6ef3c01fa0085862fe45e4ddb9c2d272cb3780841b5889a6`。本基线选健康概览、更新版我的、原生设置。我的必须使用运行缓存 **mine 11.202.21**，SHA-256 `4f8012fb1ed726d3ef11f068ff7f42b6e74def6059eddaff3426b4dbb7a06958`，不能混入内置 11.190.11 的四入口顺序。

分类逐项使用：**A** 原 App 运行观察；**B** 包内或已捕获代码；**C** 推断；**D** 独立参考/迁移选择。本文既有原图 A 采于 2026-10-15，`origin: historical`；本轮重新看图不会将它升级为 current。本轮b系列操作标为 `origin: current` 并单列时间和文件。原 PNG 的精确 capture 时间没有全部写入旧封装，保留 unknown；`contracts.json` 另外记录关联 hierarchy 的 UTC 时间，不将其冒充截图快门时间。2026-10-15T12:51:44Z 静态复核身份、像素和源码锚；设备由主任务单一控制者操作。

共同运行条件：Android 15 / API 35 Google APIs 模拟器，1080×2400 **物理 px**，440 dpi，density 2.75，fontScale 1，zh-CN，普通模式、未登录、无绑定穿戴设备/个人数据。逻辑画布约 392.727×872.727 dp。下述 bounds 原点均为整屏左上；换 dp 除以 2.75，包含 Android 像素取整。UIAutomator 的文本 bounds 是 View/行盒，**不是字号或字形边界**。

| 页面 | A 浅色原图 | A 深色原图 | 当前可确认状态 |
|---|---|---|---|
| health | [06](../runtime/captures/06-entry-after-region/screen.png) | [15](../runtime/captures/15-health-night/screen.png) | 三目标为 0；未登录卡；睡眠/心率空态 |
| mine | [21](../runtime/captures/21-mine-day-updated/screen.png) | [17](../runtime/captures/17-mine-night/screen.png) | 五入口，未登录资料与运营模块；两个主题同一 mine 包 |
| settings | [13](../runtime/captures/13-app-settings/screen.png) | [14](../runtime/captures/14-settings-night/screen.png) | 地区中国大陆；通知开；设备相关隐藏项未显示 |

三页来源图有日夜样本；这不表示独立参考的日夜实现均通过对照。实际验证范围只读 `coverage.json`。登录态、有数据图表、真机字体、完整动画时间、横屏/折叠屏运行均未在这组 A 中验证。

## 跨页面视觉关系

1. **A+B** 大标题轻、内容标题明显加重；标题角色分开。健康标题 `32sp sans-serif-light`；设置展开标题 `32sp sans-serif`；我的页头是单个空格，不能补“我的”。资料姓名为 20/500；设置原生行标题 `16sp mipro-regular + bold`。不是所有标题统一 700。
2. **A+B** 内容以独立面分组，主导航留在底部。RN 页底日间 `#F6F7F9`、夜间黑；原生设置日间白、夜间黑；导航日间白、夜间 **#242424**。RN 和原生上下文不能合并为同一个 page-bg。
3. **B** 原生通用卡片 16dp 圆角；RN 设置分区 18dp。圆角角色独立，不能用一个半径覆盖全部表面；MIUI 主按钮使用 adaptive/smooth drawable，不等价于普通 roundRect。
4. **A+B** 主动作日间 `#FF612E`、夜间 `#F04D18`；卡片日间白、夜间 `#191919`。健康数据的红/黄/蓝/绿不是通用成功/警告等级。
5. **D** 迁移时保留层级、密度、组件组合、状态与动作位置；替换业务文案、图标、单位、服务和路由。不要把健康三环当每种产品的必选装饰。

字体文件、family 声明和最终回退是三件事。包内 MiSansLatin 不覆盖完整中文。当前模拟器没有注册 mipro/MiSans 系统家族；中文逐字实际 face 未确认。原生参考使用同一 Android 环境可减少平台差异，但不能由此声称与小米手机逐字相同。RN 不声明统一 lineHeight；不得套全局 1.5。详情见 [字体证据](../runtime/font-environment.json) 和 [设计系统](../design-system.md)。

## 页面结构与几何基线

### health：概览、摘要与空态

**A** 原图 06，hierarchy `06-health.json`（2026-10-15T11:49:16.355Z）。顶部健康标题 bounds `[72,151,248,280]`；添加设备触区 `[926,166,1025,265]`；纵向主体从 y335 到2150；底部导航 y2151..2334，系统手势区另算。

**B** 当前 `HealthV2Fragment` 绑定 `DataHeaderV2View` 和纵向卡片网格，`health_main_header_data_view2_v2.xml` 描述 hero。源码手机2列/展开折叠3列/平板横屏4列、网格10dp；只有手机2列得到本组 A 支持。保持一个主纵向滚动所有者；不要给每张指标卡嵌套纵向滚动。

| 部分 | A 整屏物理 bounds | B 尺度与文字 |
|---|---|---|
| 三环绘图区 | `[265,335,815,610]` | ThreeTargetView 宽200dp；内部形状/动效不是 XML 提供的 |
| 倒影区 | `[251,610,829,830]` | 210×80dp；不把占位图当作真实反射算法 |
| 食物分享入口 | `[854,555,1080,797]` | 饼干图56×46dp；x0 为 Latin SemiBold / 最多14sp、自适应 |
| 三指标卡面 | `[33,797,1047,1319]` | 顶距hero168dp；外侧12dp；内部顶部16dp |
| 三列 | `[72,841,380,1075]` / `[402,841,710,1075]` / `[732,841,1041,1075]` | 等权三列，列间8dp；label14sp MiSansLatin Medium，value24sp MiSansLatin SemiBold，无font padding；target12sp `FontDemiBold` |
| 分隔 | `[72,1136,1008,1137]` | 高 **1物理px**，不是1dp；距上22dp |
| 活动次数行 | `[72,1137,1008,1319]` | 图标与正文后接箭头；label14sp，活动统计含义属健康业务 |
| 未登录提示 | `[33,1358,1047,1592]` | title/desc/头像/箭头；进入账号服务不是空指标卡自动强制登录 |
| 左/右空指标卡 | `[33,1620,540,2150]` / `[568,1620,1047,2150]` | 截图下沿被固定导航截住；不是完整卡片高度。图标26dp；title17sp FontMedium；empty说明14sp FontNormal |

**A** 可见指标文字为卡路里0/400千卡、步数0/6000步、中高强度0/30分钟、活动次数0次。**健康专属**：阈值、单位、三环算法、食物等价、健康卡排序与设备能力门槛。**可迁移**：重点概览→可钻取摘要→空态下一步→管理入口，相关对象/日期作为进入详情的上下文。大值/小单位基线的通用模式可用，但上表24sp hero不能与普通 `health_layout_card_data_item.xml` 的20sp混用。

**A current** b19点击hero进入“活力指标”、b21点击睡眠空卡进入“睡眠”，两者都显示2026-10-15日期；该guest状态可直接进入，不能加成强制登录。三页参考只实现进入事件及可返回的 **D 二级边界说明**，未还原这些详情页的视觉/内容；它既不冒充原版详情，也不自动否定本次三页的视觉对照，二者在覆盖表分开报告。

### mine：身份、快捷入口和分组服务

**A+B** 采用五入口更新包：活动→课程→亲友→奖品→订单。页头仍占32sp标题行但文本为空格，右侧消息与添加设备。外层 `IOScrollView paddingHorizontal:12`；资料区再加13dp水平内距。头像50×50dp/r25；头像后12dp；姓名20/500单行省略；描述12/400单行省略，多个描述用 ` | ` 连接。

**A** 原图21的姓名 bounds `[239,306,404,380]`，描述 `[239,380,404,424]`。五个入口触区 x分别33..236、236..439、439..641、641..844、844..1047，y505..673；图标为66×66px=24×24dp，y505..571；标签y588..673。不要把 TextView含padding的85px高当字体字号。

**B** 更新 `mi-621.QuickActions`：mt8dp、pt18dp、mb5dp、space-between；没有旧版行内水平14dp。`mi-622.EntranceItem`：flex1、居中、图标后6dp、label14/400、pt2dp/pb10dp、单行。主题只换资源，不控制入口数量。**D** 在其他业务可以改为3~5个条目配置，超过可读宽度时采用目标产品明确选择的换行/横滑策略；原版更多数量的策略 unknown。

**A+B** 后续是会员运营卡、左高右双卡的功能区、任务/健康咨询和分组菜单。保留两列与右侧上下卡10dp间距的组合方式；会员价格/权益/图案和医疗服务不迁移。RN menu surface圆角18dp、mt12dp、py4dp；行icon26dp、主文16 bold、左右padding14/10dp、文字左右12dp、arrow28dp。原版行点击leading-only300ms节流仅适用于这个组件，不能推广为所有按钮的节流。

### settings：可增长行与固定底部动作

**A** 实际是 MIUI 展开 ActionBar，不是旧 `widget_top_bar.xml` 的20sp栏。ActionBar bounds `[0,136,1080,457]`，返回触区 `[55,158,187,268]`，展开标题 `[71,301,247,430]`。标题为32sp（B `Miuix.AppCompat.TextAppearance.WindowTitle.Expand` → `miuix_font_size_title1`；family最终声明sans-serif）。标题滚动收缩的过程、时长与中断没有本组动态验证。

**B** `about_setting_activity.xml`：内容水平padding26dp；上方滚动容器，下方bottom固定退出动作；内容顶部margin13dp。地区后20dp间距和细分隔，分隔后20dp到云服务，云服务后20dp到双接授权。隐藏功能不保留空白占位。**A** 当前可见项如下：

| 区域 | A bounds px | 对实现的意义 |
|---|---|---|
| 内容区 | `[72,457,1008,2132]` | 左右约26dp；滚动与固定底按钮分属不同容器 |
| 地区单行 | `[72,493,1008,640]` | 无leading icon；本状态实际高53.455dp，不硬编码所有行60dp |
| 分隔 | `[72,695,1008,696]` | XML声明0.30000007dp，当前取整为1px |
| 云服务单行 | `[72,751,1008,898]` | 前后分区间距独立 |
| 双接授权双行 | `[72,953,1008,1141]` | 约68.364dp；label16sp mipro-regular+bold，description12sp mipro-regular |
| 通知开关行 | `[72,1141,1008,1329]` | description12sp；switch `[884,1200,1008,1269]`约45.091×25.091dp |
| 无障碍双行 | `[72,1329,1008,1517]` | 可增长文本区域，不用截图行高锁死长文案 |
| 退出登录按钮 | `[78,2132,1002,2260]` | **336dp**宽、46.545dp实测高，横向居中；底部到应用内容底2334px留74px |

**B 关键纠正**：这里的按钮样式是 `Button.Common.Large.Positive → Button.Common.Primary → FontMedium.17sp`，**17sp、mipro-regular+bold**；`minWidth=336dp`，宿主高 `common_btn_height=46.659973dp`、底margin27dp。不能套另一套 `CommonBtn` 的15sp。当前按钮日间 #FF612E/夜间 #F04D18，图形形状由smooth/adaptive drawable决定；参考近似形状应在对照中记录。

**A historical** 开关橙色为开启、contentDescription“当前已开启”；UIAutomator `checked=false` 与语义不符，不能直接把checked当事实。**A current** 稳定guest设置中，`b14-notification-off-stable` 于2026-10-15T12:58:00.013Z点击(946,1230)，页面保留、开关变为关闭，说明变为“你可能错过重要消息通知，点击开关允许应用通知”（[操作](../runtime/events/b14-notification-off-stable.json)、[hierarchy](../runtime/hierarchy/b14-notification-off-stable.json)、[关闭原图](runtime/settings-switch-off/screen.png)）；此前b12验证on，b15发出恢复on操作。b09同点点击后碰上账号登录，b10取消后b11已为off，存在异步认证混杂，**不支持“开关必须先认证”的因果结论**，原记录保留。受控组件发`requestChange(desired)`，业务adapter回写value，不强制套用认证。**A current** b13稳定guest点击“退出登录”后没有可观察变化；不推测确认/认证流程。**未知**：真正系统通知权限授予、跨重启持久化、登录账户退出与失败恢复。不得拿本地参考演示证明这些原版分支。

受控`requestChange`/`setChecked`是参考的 **D 适配推荐**，不是对原版内部状态所有权的反编译结论。来源A只确认可见on/off转换及文案；来源B的自定义Switch声明不能单独推导其数据存储方式。

## 十组件的职责边界

| 契约ID | 固定保留的结构 | 业务/平台替换点 | 未覆盖的来源状态 |
|---|---|---|---|
| TopBar | 标题角色、两侧动作、safe inset；健康大标题/空白Mine/展开Settings是变体 | 文案、图标、路由、平台返回 | 收缩动画、横屏、焦点恢复 |
| BottomNav | 固定图文项、selected颜色、无指示条 | 模块、icon、业务红点 | 活动重建后的完整恢复、深链 |
| ProgressHero | 主视觉+等宽三摘要+辅助钻取 | 指标、单位、图形算法、分享资源 | 非零进度、绘制时间/曲线 |
| MetricCard | icon/title/value或empty说明/可选chart | 指标与详情context、状态数据 | ready/stale/error、真实健康图表 |
| ProfileHeader | 头像+单行姓名描述+尾部slot | 账号/团队身份、描述、认证适配器 | 长名、大字、登录成功后的更新 |
| ShortcutGrid | 有序等宽入口、图标24/文字14 | 条目列表、guard、route | 更多条目、窄屏换行、guard成功继续 |
| SurfaceCard | 明确表面、局部radius、插槽 | 内容、是否可点、容器密度 | 宿主按压效果/阴影动态 |
| ChevronRow | 左标题/说明+右值/arrow，native/RN两变体 | 业务值、route、前置条件 | 多行上限实测、disabled解释实测 |
| SwitchRow | 文案与独立boolean、可访问性状态；稳定guest实测on/off | 持久化、权限、失败恢复 | 真正系统权限、跨重启状态、错误恢复 |
| PrimaryButton | 独立背景/前景/标签状态；本页17sp；guest点击无可观察变化 | 动作文案、业务handler、忙碌/取消策略 | 按下持续时间、disabled/loading、登录态退出 |

`contracts.json` 的 Props/事件与参考状态机是 **D 适配接口**，即使接口中某个参数引用 A/B 尺寸也不是原版公共 API。来源只支持default，不应以自己补的disabled/loading把来源覆盖写为已测。触摸控件在桌面/Web新增hover、键盘focus是D平台适配，应进行目标平台测试。

## 实施顺序和交接检查

先读当前目标软件源码，识别其路由、数据状态和现有组件；建立“来源规则→目标位置→保留/替换/适配→证据→验证”表。先接语义tokens和上述十组件，再做目标自己的代表页。原生参考在 `android-reference`，包名 `study.style.reference`；组件实现映射以 `contracts.json` 和当前源码为准。源图和独立实现分别保存，对照应匹配theme、fontScale、locale、viewport和内容。

交付要保留目标功能和数据，给出实际运行命令及验证差异。三页静态相似不能证明业务逻辑相同；本地mock登录成功、错误重试或保存必须声明D。来源未知状态由目标需求作出显式选择，不补写成“小米的规范”。
