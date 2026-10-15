# 原生页面结构、交互与可迁移规则

本文件以 APK 解出的布局/资源及反编译 Java 为依据。`原包事实`表示静态代码或资源可直接支持；`迁移建议`是用于其他业务的实现选择。尚无实机交互记录，因此不把静态可达分支描述成每台设备上都会出现的页面，也不把 dp 当成截图像素。

## 页面骨架（原包事实）

| 层级 | 可复刻结构 | 直接证据 |
| --- | --- | --- |
| 主壳 | 垂直容器：占满剩余空间的内容 FrameLayout → 细分割线 → 固定底部 TabLayout；没有滑动指示条或涟漪 | `res/layout/main_activity_main.xml` |
| 底部标签项 | 图标上、标签下，整体垂直居中；图标右上有默认隐藏的红点；标签 11sp、Medium、与图标间距 3dp | `res/layout/main_view_tab.xml` |
| 健康页 | 顶部大标题/子标签 → 弹性滚动容器 → 数据总览 → 可选公告 → 健康卡片列表 → 可选支付宝触碰服务 → 运营内容列表 | `res/layout/fragment_data_v2.xml` |
| 设备父页 | 顶部 HomeHeaderTabTitleView → 占满剩余高度的 ViewPager；背景 `device_page_background`，顶留白 35dp | `res/layout/device_fragment_device_tab_parent_v4.xml` |
| 设备子页 | 纵向 RecyclerView；单独的设备状态、表盘入口、提示条、设置行、解除绑定等项目布局 | `res/layout/device_fragment_device_tab_v4.xml`、`device_item_*_v4.xml` |
| 添加设备 | GridLayoutManager 网格；每格是设备图、居中名字（最多两行），有分类标签项目 | `res/layout/device_activity_add_device.xml`、`device_item_device.xml`、`device_item_device_label.xml` |
| 首次欢迎 | 全屏渐变与球形插画；中下方品牌/版本、宣传语；底部单一主操作「立即体验」 | `res/layout/login_activity_welcome.xml`、`values-zh-rCN/strings.xml` |
| 隐私说明 | 上方可滚动正文和权限说明、协议/可选项；底部固定提示、协议勾选和同意/退出或基础模式动作 | `res/layout/login_activity_privacy.xml`、`values-zh-rCN/strings.xml` |

布局中的 `main_activity.xml` 只有 `market_home_content_view` 容器，不能把它当成主导航壳。旧 `main_fragment_homepage.xml` 包含 SwipeRefreshLayout 与折叠 AppBar；当前 `HealthV2Fragment` 对应的结构应优先以 `fragment_data_v2.xml` 为准，不能混用成一个首页。

`MainTab.java:15–42` 直接定义主导航索引为健康 0、运动 1、设备 2、我的 3。`MainActivity.java:370` 的重复点击回调为空；正常切换调用 `showFragment` 并更新选中标签颜色。没有“再次点击当前 tab 自动滚动到顶”的证据。`MainActivity.java:1725` 的根返回处理最终调用 `finish()`；不能默认根返回会先切换回健康页或要求双击退出。

已通过 DEX 静态字段值与 `public.xml` 关联确认原生主导航选中色是 `colorPrimary`，默认 `#FF612E`、夜间 `#F04D18`；未选中是 `main_tab_text_color_normal`（默认 `#4D000000`，夜间引用 `white_45`）。映射保存在 `native-resource-map.json`，可由 `tools/map-native-resources.cjs` 重建。

运动和我的当前 V4 入口是 YRN 页面容器：`SportTabV4Fragment.SPORT_HOME_PATH = sport/SportHome`，`MineFragmentV4.MINE_HOME_PATH = mine/MineTab`。因此 APK 中 `sport_tab_v4_page.xml`、`mine_fragment_v4.xml` 的存在只证明资源被打包，不能证明当前容器使用这些 XML；相应正文结构应跟踪 RN 页面。旧 MineFragment 也不能与 MineFragmentV4 合并为一张页面规格。

## 健康总览与卡片（原包事实）

`fragment_data_v2.xml` 中 HomeHeaderTabTitleView 位于滚动内容之外。滚动区用 SpringBackLayout 包裹 NestedScrollView，内部 RecyclerView 只负责卡片排列；因此复刻时应保持一个主要纵向滚动所有者，避免每个卡片列表各自滚动。

`HealthV2Fragment.java:1028` 根据显示形态设置网格列数：普通手机 2 列、展开的折叠屏 3 列、平板横屏 4 列，项目装饰使用 10dp 间距。`initRefreshLayout` 创建 34dp LoadAction，手动下拉会触发 `triggerDataSync(true)`；页面可见时 `onVisible` 触发 `triggerDataSync(false)`。手动刷新还请求 Web 用户信息。首屏因此包含可刷新行为，但它不是 XML 中普通 SwipeRefreshLayout 的旧分支。

`health_main_header_data_view2_v2.xml` 的层次是：269dp 高的背景区域、居中 200dp 宽的 ThreeTargetView、210×80dp 倒影占位、靠右的分享/卡路里换算视觉，以及从顶部 168dp 处开始的三列目标卡。三列等宽，每项显示指标；下方用 1px 分隔线隔开活动次数入口。这里的「三指标总览 + 底部补充入口」是结构事实，具体环的绘制、颜色/动画来自自定义 View，不能仅由 XML 推出。

`health_layout_card_data_item.xml` 将大数值与小单位基线对齐；标签在下一行，异常文字默认隐藏，箭头默认隐藏。卡片要独立支持“有值/无值”“正常/异常”“可钻取/只展示”，不能把红色异常文案和右箭头设成所有卡片的常驻装饰。`health_layout_data_item_footer_v2.xml` 提供居中的「管理健康功能」入口与右箭头。

公告和支付宝触碰入口在首页 XML 中均默认 `gone`；是否实际出现取决于程序条件。健康数据卡片不能凭 APK 中所有健康资源推断为同屏全部显示。

源码进一步确认首页实际调用 `com.xiaomi.fitness.v2.HealthDataTypeUtilsV2.getHomeDisplayTypeList`，随后赋予排序号；未展示项目被标为 `-1`。旧 `HealthDataTypeUtils` 的顺序不能直接代替这个 V2 列表。卡片进入详情时携带 `did`、数据所属日期 `local_date` 与指标 `sport_type`；账号提示卡走登录/资料完善分支，一部分指标进入前也会检查个人资料（`HealthV2Fragment.java:603,839,1152`）。这支持“从哪张卡进入，就保持哪个对象与日期”的上下文传递规则。

补充 V2 生成规则（`native-extra/com/xiaomi/fitness/v2/HealthDataTypeUtilsV2.java:342–457`）：先读取 `HEALTH_CARD_SORT` 保留用户排序，再补入尚未存在的候选项目。基础候选顺序为睡眠→心率→血压→心电→血糖（非 Play）→血氧→体重→皮肤温度→女性健康（女性账户提前）→压力→听音（非 Play 且大陆）→元气值→PAI→身体能量→女性健康（其他账户在后）→健康检测。随后综合用户手动开关、是否属于默认显示类型、是否有历史数据、已绑定设备能力筛选。女性健康在其他账户上可保留为关闭的可管理条目；健康检测要求支持设备或已有数据等条件。这个候选顺序不是任何账号下的最终首屏列表。保存管理结果时把启用列表和用户手动选择列表分别写回设置，并在未登录时保留本地手动状态（`:885`）。

总览的三环及倒影点击都进入目标详情。卡路里换算/分享入口会先检查分享背景素材：素材存在直接进入分享页，缺失则显示下载进度，成功进入、失败给出短提示（`DataHeaderV2View.java:254–294`）。因此“点击入口→检查前置素材→进度→原目标”是直接有证据的可复用模式，适合迁移到需准备文件或模板的功能。

## 设备页的组件规则（原包事实）

设备信息不是一个巨大的固定表单，而是列表项目组合：

- 设备状态视图，顶部 8dp、底部 16dp。
- 表盘/封面入口，纵向外距 12dp。
- 提醒设置提示条，顶距 12dp；运营提示条内部可再使用纵向列表。
- 右箭头两行设置项和开关两行设置项共用近似尺寸；左内边距 14dp、右内边距 10dp。两类项目都声明 `allowClickWhenDisabled=true`，表明“不可直接执行”与“完全无点击反馈”是不同状态；具体提示还需代码确定。
- 解除绑定是页面末尾独立按钮，位于浅灰背景区，顶距 12dp，与普通设置行分开。

空设备引导卡 `device_item_add_new_v4.xml` 使用循环自动播放的 Lottie（180×140dp、顶距 52dp），下面是同宽并排的「扫一扫」次操作与「添加设备」主操作；两按钮之间 12dp，左右各 28dp，底内边距 28dp。动画资源存在不能证明动画时长；不要自行写成已观察值。

`DeviceTabParentFragmentV4.java:225` 只将 `index>2 && deviceCount>0` 的提供方放入设备父页，按索引排序；全部为空时回退到穿戴设备提供方 `index=3`。`initTitleView` 同时区分穿戴设备 `index=3` 与其他设备 `index=4` 的数量来决定标题模式。因此顶部是设备类别子标签，不能把它误做成每个已绑定设备各一个标签。

`DeviceTabV4ViewModel.java:1921` 的无设备分支分三种：有当前设备→显示设备内容并连接/同步；没有当前设备但有配件→只显示配件与佩戴模式；两者都无→添加/扫码引导卡加无设备运营区。`showDevice` 按设备能力组合状态卡、配件、提醒设置、表盘（WearOS 排除）、运营区、商城/功能卡、设置项。设置项按 `isSupportFeatureCardV4` 过滤，不能让所有型号显示完全一样的设置列表。

同一个设备状态入口在已连接时执行同步；在状态 1/4/9 时发起强制重连并计入手动尝试；状态 8 进入选择常用应用处理（`DeviceTabV4ViewModel.java:1415`）。连接权限与定位开关提示有非强制 48 小时节流，显式强制操作可越过节流（`:1338,1352`）。这属于避免自动提示反复打断用户的具体实现事实；不要把 48 小时解释成系统权限本身的限制。

## 首次进入分支（原包事实）

`SplashActivity.startNextActivity` 按以下优先级读取持久状态：

1. `welcomeFinish=false`：进入 WelcomeActivity。
2. 未同意隐私且未进入隐私基础模式：进入 PrivacyActivity。
3. 隐私基础模式：进入主界面，`key_tab=INDEX_SPORT`。
4. 未选择地区：进入 RegionSelectActivity。
5. 其余按渠道、地区、V4 开关及广告资格进入主界面、开屏广告或其他初始化分支。

这不是“必须先登录才能进入主页”的证据。外部 intent 还可携带 `showLoginDialog`、`bindNew`，由主流程继续处理。首次流程通过 IntentRelay 保留入口参数，适合深链/外部入口完成引导后继续。

必须分开两个概念：`PrivacyEasyModePreference.isEasyMode()` 是隐私基础模式；GuidePersonalizeActivity 的「个性主题/简洁主题」是首页形象偏好。中文资源明确说简洁主题不显示人物形象，可后续在个人中心设置中调整。不能把“简洁主题”实现成隐藏健康、设备、账号服务的基础模式。

`PrivacyActivity.java:156` 的“同意”并非只依赖按钮禁用：已勾选协议直接继续，未勾选时显示二次协议对话框；对话框设置 `setCanceledOnBackPressed(false)`。同意后写入时间、清除基础模式标记，并根据地区/渠道/模式前往地区页、主界面或外观引导。`dealEnterEasyModeAction` 把地区设为中国大陆并进入运动 tab。中国大陆、韩国和通用隐私 UI 有条件分支，不能把 XML 中所有勾选项同时拼到一个中国版页面。外观引导只在非 Play、未知外观模式且未启用 V4 的分支出现（WelcomeActivity/PrivacyActivity）；它是兼容路径，不能当成所有用户的 V4 首次必经页。

地区页从已有状态或 IP 结果恢复当前国家/地区；尚无有效 CountryInfo 时下一步禁用，有有效选择才启用（`RegionSelectActivity.java:294,310,333`）。这是“输入结果有效性决定主动作可用”这一交互约束的明确实例。

## 可迁移到其他业务的交互规范（迁移建议）

| 场景 | 可复用规范 | 适用于其他业务的例子 |
| --- | --- | --- |
| 主导航 | 3–5 个稳定一级区域；点击切换内容，保持各自列表滚动位置和局部状态；二级页面不再堆一级标签 | 健康/运动/设备/我的可替换为概览/任务/资产/我的 |
| 页面头部 | 大标题承担定位；少量同级子对象做轻量标签；右侧唯一常用动作（如添加）；筛选等低频动作放次级入口 | 设备子标签可替换为多个项目、空间或账户 |
| 纵向信息组织 | 核心总览先出现，随后是可钻取卡片，再是管理入口与服务推荐；每一张卡只表达一种主任务 | 三环可替换为预算/进度/健康度，但保留三列指标及小单位体系 |
| 首次进入 | 把欢迎、隐私、地区/工作区、登录、个性偏好分成独立持久状态；完成一次即跳过；完成后恢复原入口 | 深链进入项目时，完成必要设置后进入该项目 |
| 权限 | 以具体功能触发权限请求；先解释收益，拒绝后保留当前页面并给出可重试入口；永久拒绝时才引导系统设置 | 点扫码时请求相机，点附近设备时请求蓝牙 |
| 空态 | 保留页面身份，用一个轻量插画/动画说明状态；最多一个主动作、一个替代动作；添加后进入真实数据状态 | 无设备→添加/扫码；无项目→新建/导入 |
| 卡片状态 | `loading / empty / ready / stale / error / disabled` 应独立；过期数据可以继续显示并标注更新时间；异常提醒不覆盖正常结构 | 网络失败但本地有数据时，不把整个首页替换为空白错误页 |
| 设置行 | 行点击进入详情，开关只做二元切换；禁用行仍可给“需要连接/权限”的解释；文本说明放第二行，状态放尾部 | 资产离线时点击依然解释如何重新连接 |
| 返回 | 系统返回/左上返回退出当前最深层；关闭弹窗只关闭弹窗；底部切换不堆叠主区域历史；有未保存编辑时给放弃/继续编辑 | 编辑排序后返回先处理未保存状态 |
| 确认取消 | 普通可逆操作直接执行并反馈；删除、解绑等需要标题+后果+明确动作名；取消保持原状态；提交中禁止重复触发 | “删除项目”比“确定”更明确 |
| 同步与连接 | 连接中、同步中、成功、失败、需授权分别呈现；同步失败应可在上下文重试；结果反馈结束后恢复稳定内容 | 资产同步中不让按钮持续留在可连点状态 |
| 成功反馈 | 短时反馈后回到任务上下文；只有下一步确实需要用户处理时才用模态提示 | 添加资产后直接显示资产详情/总览 |

以上建议用于把原包的层级和节奏迁移到新业务，不等同于已经证实原 APK 的每条行为。弹性滚动和 Lottie 已有资源证据；转场类型、弹窗按钮排序、触觉反馈、动画时长和返回保留状态仍需动态验证。

## 实现时应拆开的状态

建议用独立字段，而不是单一“已初始化”布尔值：`welcomeFinished`、`privacyAccepted`、`privacyBasicMode`、`regionSelected`、`authenticated`、`profileComplete`、`appearanceMode`、`selectedPrimaryTab`、`selectedDeviceId`、`deviceConnectionState`、`syncState`、`permissionState`、`featureAvailability`。原包已直接证明其中欢迎、隐私、基础模式、地区、主tab与形象偏好等状态是独立概念；其他命名属于迁移实现建议。

## 验证缺口

未连接实机，尚不能确认当前账号/机型上的功能开关、首屏卡片具体顺序、远端运营区、跳转和过渡动画、暗色主题下每一组件实际色值、禁用行点击提示、蓝牙错误分支弹窗的具体组合。静态证据足够建立组件/页面/状态规格，不能宣称已完成逐像素、逐交互复现。
