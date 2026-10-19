# 运行中组件与行为：首轮可复用基线

本页是 Kimi Android **3.1.3 (376)** 的部分运行研究，覆盖隐私引导、未登录主页、导航抽屉、设置和部分外观选项。**A** 为本轮原 APK 的真实运行画面/UIA/已记录操作；**C** 为基于有限画面形成的迁移候选规则。表中的 props 是对可见数据的归一化描述，不声称是原代码的 API。内部状态所有者、受控/非受控机制均未确定。

原 APK SHA-256：`9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332`。运行条件为独立 AVD `rea_kimi_ui_20261015` / `emulator-5596`、未登录、原应用网络、无 fixture。采集画布 **1080×2400 物理 px**，系统密度 **420 dpi**，即 `density=2.625`，系统夜间模式 no。早期画面文案为英文，08–19记录app locale `en-US`，20–23切到`zh-CN`；`buildLocale`仍`en-US`，`systemLocales`字段仍为`null`，两者分别保留，不把null解释成一次实测的英语系统语言设置。`fontScale=1.0` 不能确定 family、字号、字重或行高。

截图与 UIA dump 为先后采集，不能视为同一时刻。已实际查看下表原图和相应 hierarchy；capture.json 中仍保留采集时的原始待复核标记。本页记录本次像素复核结果，不改原图或 session。

## 场景与证据

| 场景 | 本次查看到的状态 | 原始证据 |
| --- | --- | --- |
| 03-kimi-privacy | 灰色背景覆盖上的白色隐私确认面板 | [原图](../evidence/runtime/scenes/03-kimi-privacy/screen.png)、[UIA](../evidence/runtime/scenes/03-kimi-privacy/hierarchy.xml)、[身份/哈希](../evidence/runtime/scenes/03-kimi-privacy/capture.json) |
| 04-privacy-disagree | 系统 launcher，非 Kimi 页面 | [原图](../evidence/runtime/scenes/04-privacy-disagree/screen.png)、[记录](../evidence/runtime/scenes/04-privacy-disagree/capture.json) |
| 05-privacy-reentry | 重开后再次显示同一隐私确认内容 | [原图](../evidence/runtime/scenes/05-privacy-reentry/screen.png)、[记录](../evidence/runtime/scenes/05-privacy-reentry/capture.json) |
| 06-after-agree | 隐私面板仍可见，整体更暗的过渡采样帧 | [原图](../evidence/runtime/scenes/06-after-agree/screen.png)、[记录](../evidence/runtime/scenes/06-after-agree/capture.json) |
| 07-notification-permission | Kimi 主页上出现 Android 通知权限弹窗 | [原图](../evidence/runtime/scenes/07-notification-permission/screen.png)、[UIA](../evidence/runtime/scenes/07-notification-permission/hierarchy.xml)、[记录](../evidence/runtime/scenes/07-notification-permission/capture.json) |
| 08-guest-home | 未登录主页、空输入器、工具入口行 | [原图](../evidence/runtime/scenes/08-guest-home/screen.png)、[UIA](../evidence/runtime/scenes/08-guest-home/hierarchy.xml)、[记录](../evidence/runtime/scenes/08-guest-home/capture.json) |
| 09-navigation-drawer | 名称虽含 drawer，实际截图仍为主页 | [原图](../evidence/runtime/scenes/09-navigation-drawer/screen.png)、[记录](../evidence/runtime/scenes/09-navigation-drawer/capture.json) |
| 10-composer-focus | 名称虽含 focus，实际为打开的导航抽屉；未出现键盘 | [原图](../evidence/runtime/scenes/10-composer-focus/screen.png)、[UIA](../evidence/runtime/scenes/10-composer-focus/hierarchy.xml)、[记录](../evidence/runtime/scenes/10-composer-focus/capture.json) |
| 11-settings | 未登录设置页 | [原图](../evidence/runtime/scenes/11-settings/screen.png)、[UIA](../evidence/runtime/scenes/11-settings/hierarchy.xml)、[记录](../evidence/runtime/scenes/11-settings/capture.json) |
| 12-appearance | 浅色外观页：Theme settings / Font size 均为Follow System，Language为English | [原图](../evidence/runtime/scenes/12-appearance/screen.png)、[UIA](../evidence/runtime/scenes/12-appearance/hierarchy.xml)、[记录](../evidence/runtime/scenes/12-appearance/capture.json) |
| 13-theme-selector | 主题单选页，Follow System有蓝色勾选 | [原图](../evidence/runtime/scenes/13-theme-selector/screen.png)、[UIA](../evidence/runtime/scenes/13-theme-selector/hierarchy.xml)、[记录](../evidence/runtime/scenes/13-theme-selector/capture.json) |
| 14-dark-theme | 同一主题页已变暗，蓝色勾选移至Dark mode | [原图](../evidence/runtime/scenes/14-dark-theme/screen.png)、[UIA](../evidence/runtime/scenes/14-dark-theme/hierarchy.xml)、[记录](../evidence/runtime/scenes/14-dark-theme/capture.json) |
| 15-dark-appearance | 系统返回后仍为深色，Theme settings摘要为Dark mode | [原图](../evidence/runtime/scenes/15-dark-appearance/screen.png)、[UIA](../evidence/runtime/scenes/15-dark-appearance/hierarchy.xml)、[记录](../evidence/runtime/scenes/15-dark-appearance/capture.json) |
| 16-font-size | 名称含font-size，实际仍是Appearance，不能作为字号页证据 | [原图](../evidence/runtime/scenes/16-font-size/screen.png)、[记录](../evidence/runtime/scenes/16-font-size/capture.json) |
| 17-language-selector | 名称含language，实际是Feature management；两个开关均开 | [原图](../evidence/runtime/scenes/17-language-selector/screen.png)、[UIA](../evidence/runtime/scenes/17-language-selector/hierarchy.xml)、[记录](../evidence/runtime/scenes/17-language-selector/capture.json) |
| 18-font-size-options | 真正字号页：说明/预览、Follow System开关、四档滑杆与右上确认图标 | [原图](../evidence/runtime/scenes/18-font-size-options/screen.png)、[UIA](../evidence/runtime/scenes/18-font-size-options/hierarchy.xml)、[记录](../evidence/runtime/scenes/18-font-size-options/capture.json) |
| 19-language-options | 真正语言选择页，English蓝色对号 | [原图](../evidence/runtime/scenes/19-language-options/screen.png)、[UIA](../evidence/runtime/scenes/19-language-options/hierarchy.xml)、[记录](../evidence/runtime/scenes/19-language-options/capture.json) |
| 20-language-change | 点简体中文后，标题改为“语言切换”，简体中文蓝色对号 | [原图](../evidence/runtime/scenes/20-language-change/screen.png)、[UIA](../evidence/runtime/scenes/20-language-change/hierarchy.xml)、[记录](../evidence/runtime/scenes/20-language-change/capture.json) |
| 21-chinese-return | 多次系统Back后为中文深色抽屉，右侧仅露主页窄条 | [原图](../evidence/runtime/scenes/21-chinese-return/screen.png)、[UIA](../evidence/runtime/scenes/21-chinese-return/hierarchy.xml)、[记录](../evidence/runtime/scenes/21-chinese-return/capture.json) |
| 22-chinese-home | 系统Back关闭抽屉后的完整中文深色主页，中央蓝环有两只白眼睛 | [原图](../evidence/runtime/scenes/22-chinese-home/screen.png)、[UIA](../evidence/runtime/scenes/22-chinese-home/hierarchy.xml)、[记录](../evidence/runtime/scenes/22-chinese-home/capture.json) |
| 23-composer-keyboard | 输入器焦点/展开采样帧，有光标但截图没有可见软键盘；不能按文件名称为键盘页 | [原图](../evidence/runtime/scenes/23-composer-keyboard/screen.png)、[UIA](../evidence/runtime/scenes/23-composer-keyboard/hierarchy.xml)、[记录](../evidence/runtime/scenes/23-composer-keyboard/capture.json) |

操作和时间的原始来源为 [session.json](../evidence/runtime/session.json)。这里只引用已存在记录，不自行控制设备。03 的采集区间为 `2026-10-15T15:16:24.670Z`–`15:16:30.556Z`；07/08/10/11 分别在 `15:19:23.850Z`、`15:19:52.139Z`、`15:21:55.115Z`、`15:22:58.295Z` 完成截图。时间均为 UTC；精确截图/层级哈希与后续 dump 的时间保留在各 capture.json。

## 组件契约：可见数据、已发生事件、未知

| 组件 | A：可观察 props / 结构与当前状态 | A：已测试动作与结果 | 尚未验证 |
| --- | --- | --- | --- |
| **01 隐私面板** | 03/05：白色圆角面板，居中标题 `Welcome to Kimi`、居中协议/解释文字、底部双动作；背景呈灰色覆盖 | 03 点 Disagree 后 04 为 launcher；重开出现 05；05 点 Agree 后先见 06，再出现 07 | 遮罩点击、系统返回、默认焦点/焦点圈、可访问性朗读顺序、长文/大字/其他语言、同意后跨进程保存期限 |
| **02 协议链接样式文本** | 03：`Terms of Service`、`Privacy Policy` 为蓝色，置于解释文字同一段，跨行折行；UIA 将整段合为一个 TextView，未暴露独立 clickable 链接节点 | 未点击两段文字 | 链接目标、打开方式、返回位置、点击范围、访问失败状态；不能仅因蓝色就认为已验证可点击 |
| **03 分隔双动作行** | 03：上边横分隔、两列中间竖分隔；左 `Disagree` 深色、右 `Agree` 蓝色且视觉更强调；两侧均无实心按钮填充；UIA 父 View 可点且 enabled | 左侧已测试退出前台到 launcher；右侧已测试继续流程 | 按压色、重复点击处理、忙碌时禁用、动作顺序对其他对话框是否一致 |
| **04 圆形图标按钮** | 08：顶部左汉堡、右带叉扬声器；10：抽屉齿轮；11：返回箭头。图标深色、浅色圆形承载，部分周围可见柔和阴影。08 UIA 描述为 `Navigation button`、`Enable voice announcement` | 菜单点击后早帧09仍为主页，晚帧10为抽屉，归因见下表；抽屉齿轮点击后11为设置 | 图标按压/focus/disabled、返回箭头的结果、播报实际开关与持久化、命中区域是否含可见阴影 |
| **05 模型胶囊** | 08：顶部白色圆角胶囊，`Instant` 深色并视觉更重，`High` 灰色；UIA 父区域 clickable/enabled | 未测试模型区域点击 | 它是单个选择器还是两个独立入口、选中态、菜单、计费/登录拦截、切换后的草稿或内容保留 |
| **06 工具 chip 行** | 08：浅灰圆角 chip，左线性图标、右短标签；可见 `New project`、`Slides`、`Swarm`，`Build` 在右边缘被裁切。UIA 各父 View clickable/enabled | 未点击工具 chip、未滑动该行 | 横向滚动手势、顺序定制、登录前置条件、选中/忙碌/错误、重复点击和目标页 |
| **07 输入器** | 08：底部白色大圆角容器；中间 `EditText` 当前空值、`focused=false`，占位 `Ask anything. Images work too.`；左加号、右语音按钮，下方灰色 `Generated by Kimi AI`。22为深色中文实例，占位“尽管问，带图也行” | 早期focus尝试后10实际为抽屉，不能算成功；后来22点输入器→23可见蓝色光标、容器增高、欢迎语消失，后采UIA为`focused=true` | 截图23仍无可见软键盘；实际编辑/输入法组合态、回车/换行、发送按钮显现、草稿、长度限制、提交和取消生成未测 |
| **08 附加输入按钮** | 08：输入器左侧加号，浅灰圆形底；UIA Button 描述 `More input options`，enabled/clickable | 未点击 | 附件菜单的组成、相机/相册/文件权限时机、文件数/大小、上传进度、删除/重试和输入保留；不能由加号直接认定支持的附件类型 |
| **09 语音输入按钮** | 08：输入器右侧声波图标，浅灰圆形底；UIA Button 描述 `Switch to voice input`，enabled/clickable | 未点击 | 录音按住/点击机制、麦克风权限、录制态、取消手势、转写/失败/超时、与顶部播报按钮的关联 |
| **10 导航抽屉** | 10：左侧浅灰大面板；右侧留出主页窄条，主页卡片有圆角边缘与阴影。上部为头像/`Log in` 和齿轮，随后 `Scheduled tasks`、`Kimi Claw`，分组标签 `Project`/`Chat history` 与 `New project`。21是相同结构的中文深色实例 | 10齿轮→11设置；20后连续系统Back最终21回到打开的抽屉；21系统Back→22关闭抽屉回主页；首次打开归因未隔离 | 遮罩/右侧窄条点击、滑动关闭、其他浮层叠加时Back优先级、列表滚动位置、登录后结构与选中态；未追踪是否复用相同组件实例 |
| **11 未登录空态与动作** | 10：历史区无记录，灰色解释 `After logging in, you can sync history across multiple platforms`，下方黑底白字胶囊 `Log in`；顶部也有头像+`Log in` 行。21文案为“登录后可以同步多端历史记录”与“去登录”，CTA变为浅色填充、深色字 | 未点击两处登录入口；仅确认主题/语言改变后再次可见 | 两入口是否等效、登录方式/取消/错误、登录后历史同步、重试与返回；未把空历史解释为网络请求成功 |
| **12 设置列表** | 11：返回圆按钮+居中 `Settings`；浅灰页底、白色大圆角分组。每行左线性图标/标签、右灰色箭头；分隔线从文字列起。首组 Notifications/Appearance/Feature management；Help Center 组含 Help Center/Send Feedback/About Kimi/Check for updates；更新行右侧 `3.1.3`；底部浅灰全宽 `Log In` 及两行模型备案文本 | 抽屉齿轮→设置；Appearance行→12；后续Feature management页17已见 | 浅灰 Log In 在UIA中 enabled/clickable，**不能称为禁用按钮**；更新、外链和反馈结果未知；外观子页详见下节 |

主页中央是**蓝环/头像动效的采样帧，加载语义未证实**：08/09只有环的帧，22/23可见环中两只白眼睛；22还与中文欢迎语同现。不能据此认定网络加载、忙碌遮罩或网络失败，也没有测量动效周期、时长或语音关联。所有当前 enabled/clickable/focusable 值都来自 UIA 的那一帧语义，不证明点击必然产生业务结果。

23特别体现非原子采集边界：截图中输入器仍接近底部，而后采UIA的EditText已报告`[74,1228][1006,1353]`，两者位置不一致。`focused=true`保留为后采UIA的事实；不把该bounds套到截图、推断键盘高度或加入稳定几何token。

### Android 通知权限：平台适配边界

07 的圆角面板包含钟形图标、`Allow Kimi to send you notifications?` 与竖排 `Allow` / `Don’t allow` 两个按钮。UIA package 明确为 **`com.google.android.permissioncontroller`**，资源 ID 为 `permission_allow_button` / `permission_deny_button`。这是系统权限 UI，不应把其淡紫色、字号或按钮圆角迁移成 Kimi 组件 token。

本轮只验证 Don’t allow：点击后进入未登录主页08，仍可观察导航、模型入口、工具行和输入器。未验证 Allow、下次启动的重复提示、Android 设置中的重新授权、永久拒绝或其他系统版本。这里可迁移的 C 候选是“拒绝可选权限后仍提供基础入口”，适用范围仅为本次通知权限拒绝到主页；不是对相机、录音或定位权限的通用结论。

### 外观设置：单选、开关与显式保存

12 的外观列表使用“左标签—右侧当前值—箭头”结构，当前值是进入子页前可见的状态摘要。13主题页有 `Follow System`、`Light mode`、`Dark mode` 三行，每行左图标、标签，当前项尾部蓝色对号。UIA中13仅Follow System为`checkable=true/checked=true`；14仅Dark mode为`checked=true`。选中行同时报告`clickable=false`，其他项为true，不能将这种语义差异推广为所有列表行的实现。

点击Dark mode后，14已在当前页显示黑色页底、深灰卡片、浅色文字和图标，蓝色对号移到Dark mode；**没有额外Save动作**。随后系统Back回15，深色仍在且摘要为Dark mode。14/15的capture条件仍是系统`nightMode=no`，说明这里是应用选择覆盖系统当前浅色，而非系统夜间模式发生变化。只确认此次选择及一次返回中的状态保留，未验证重启持久化、系统主题切换、重复点当前选项或Light mode/Follow System切换后的效果。下一帧已生效不能给出准确延时。

17实际为Feature management，能看到 `Vibration feedback`、`Use keyboard send button` 两个蓝色开关。UIA均报告`checkable/checked/enabled/clickable=true`。说明文字声称开启后键盘换行键改成发送键；这只是可见引导信息，**没有实际键盘发送或振动验证**。未触动开关，关闭态、重启保存、取消或权限影响未知。

18字号页与主题页的保存机制不同：画面提供示例对话、调整说明、`Follow System`开关、`Small / Standard / Large / Extra Large`滑杆，右上有对号按钮，说明文本明确称其为Save。开关当前开启，滑杆UIA为SeekBar且`enabled=true`；不能因为画面弱化就称禁用。此次没有改字号、拖动或按Save，系统字体比例仍1.0；不得把预览文字或档位当作已测sp值。返回时是否放弃未保存更改也未知。

可迁移的C候选是“设置列表显示当前值；即时主题选择和需要确认的字号编辑采用各自保存模式”。它不支持“所有设置自动保存”或“所有设置都需要Save”的全局规范。

### 语言选择与返回保留

19是真正的语言列表，标题`Language Selection`，语言项使用自身名称，English尾部有蓝色对号；该页14个可见项包含阿拉伯文、简/繁体中文、法/德文、印尼文、意大利文、日/韩文、巴西葡语、俄语、拉美西语和泰文。仅列出当前可见项，不保证这是所有支持语言，也未测试RTL布局。

action 21点击简体中文后，20的标题已改为“语言切换”，蓝色对号移至简体中文，无额外Save动作；记录的appLocales同时由`[en-US]`变为`[zh-CN]`。`buildLocale`仍为`en-US`，系统night仍no。19/20的文字节点并未暴露相应checked状态，对号选中属于像素观察，不能虚构radio语义。随后三次系统Back的最终截图21为中文深色抽屉：“定时任务”“项目”“新建项目”“历史会话”“去登录”等已本地化，`Kimi Claw`仍保留品牌名称。该实例的登录CTA为浅色填充配深色文字，与10浅色主题的黑底CTA形成对照。

本轮证明主题与语言选择跨这些返回动作仍可见，不能证明跨重启持久化、切换过程中所有后台任务取消/重启、未保存草稿保留或完整主页内容已更新。21右侧只露主页窄条，不作为中文完整主页截图。

## 已发生的转移

状态名和辅助编号由研究记录定义，不是原应用路由名。表中动作序号是 session.actions 的 **1-based** 下标。捕获间隔和工具运行耗时不代表动效 duration。

| 起点 | 实际动作 / 前置条件 | 后续观测 | 能确认的副作用 / 保留 | 边界 |
| --- | --- | --- | --- | --- |
| 03 隐私面板 | action 3：点 `[360,1415]`，Disagree | 04 系统 launcher | Kimi 界面退出前台 | 不能证明进程终止、数据被删除或永远不可用 |
| 04 launcher | action 4：重新 launch MainActivity | 05 再次隐私面板 | 此次拒绝没有跳过下一次协议确认 | 仅同一安装的一次重开，不推断所有重装/升级规则 |
| 05 隐私面板 | action 5：点 `[720,1415]`，Agree | 06 暗化过渡帧；随后07通知权限 | 协议流程继续到系统通知请求 | 06不是失败状态证据；不推断等待时长/重入机制 |
| 07 通知权限 | action 6：点 `[540,1480]`，Don’t allow | 08 未登录主页 | 通知拒绝后基础主页可见 | 未测试任何生成或账号能力是否可用 |
| 08 未登录主页 | action 7：点 `[100,194]` 菜单 | 09仍是主页 | 点击已发出；该早帧没有显示抽屉 | 不能据此判定菜单无响应或计算动画时长 |
| 09 主页早帧 | 中间有 action 8：点 `[540,2180]`，原意为输入框 focus | 10实际为抽屉，无键盘 | 抽屉最终出现；**输入 focus 未证实** | 菜单延迟、采集时差及中间点击未隔离，不能把输入器点击记为打开抽屉的确定事件 |
| 10 抽屉 | action 9：点 `[815,225]` 齿轮 | 11 设置页 | 未登录状态可到达设置 | 返回目标、抽屉状态和页内状态保留未测试 |
| 11 设置 | action 10：点 `[420,540]` Appearance行 | 12 外观页 | 当前摘要Theme/Font均Follow System、语言English | 不推断默认值适用于所有安装或账号 |
| 12 外观 | action 11：点 `[450,370]` Theme settings | 13 主题选择 | 当前Follow System选中 | 选中项重选未测 |
| 13 主题选择 | action 12：点 `[420,668]` Dark mode | 14 当前页变暗、Dark mode勾选 | 无额外Save，应用主题已在本次采样生效；系统night仍no | 精确延时、持久化与其他选项结果未知 |
| 14 深色主题页 | action 13：系统Back | 15 深色外观页 | 摘要Dark mode，主题跨这次返回保留 | 未测进程重建或重启 |
| 16 实际为外观页 | action 14：系统Back；action 15：点 `[420,665]` | 17 实际为Feature management | 可见两个开启的开关 | 原动作说明写language，但实际页面不可据此改名为语言页 |
| 17 Feature management | action 16系统Back、17重开Appearance、18点Font size | 18 真正字号页 | 可见预览与编辑控件，尚未编辑 | 跨多个操作只记录到达，不把中间页当已截图状态 |
| 18 字号页 | action 19系统Back、20点Language | 19 语言页 | 未改字号；English蓝勾 | 未保存编辑的取消机制没有测试，因为没有编辑 |
| 19 语言页 | action 21点 `[420,675]` 简体中文 | 20 标题中文、蓝勾简体中文 | 无额外Save，appLocales为zh-CN | 其他语言、RTL和重启持久化未测 |
| 20 中文语言页 | actions 22/23/24：三次系统Back | 21 中文深色抽屉 | 应用深色与中文跨这组返回保留，抽屉处于打开状态 | 不推断中间每页的动画、重建、滚动或焦点机制；不是完整主页截图 |
| 21 中文深色抽屉 | action 25：系统Back | 22 中文深色完整主页 | 抽屉关闭，主题与语言仍可见 | 不推断其他浮层叠加时Back优先级 |
| 22 中文深色主页 | action 26：点 `[500,2175]` 输入器 | 23输入器增高、有光标，欢迎语消失；后采UIA焦点true | 本次输入焦点已确认 | 截图无可见软键盘，且后采UIA几何已变化；不推断键盘已弹或其高度 |

机器可读副本见 [observed-transitions.json](../evidence/runtime/observed-transitions.json)。它保留确认程度和未知，而不是把未测边画成已实现状态机。

## 本轮可以使用的几何锚点

这些是 **UIA 语义矩形**，不是圆角轮廓、阴影边缘、字形包围盒或保证的所有设备尺寸。逻辑换算只在该设备 `px / 2.625` 成立；采用四舍五入展示，不据此增加新精度。机器可读实例见[native-observed.json](../tokens/native-observed.json)，保留19个实例的原bounds、实际node.text/contentDescription、场景及源哈希；每项均标`promotedToGlobalToken=false`。[提取脚本](../tools/extract-native-geometry.mjs)只读已有UIA和capture文件，不控制设备，也不填造应用级颜色。

| 组件与场景 | UIA bounds（物理px） | 语义矩形换算 |
| --- | --- | --- |
| 03 Disagree / Agree | `[186,1354][540,1480]` / `[541,1354][895,1480]` | 各354×126px，约134.86×48dp |
| 08 导航按钮 | `[37,131][163,257]` | 126×126px = 48×48dp |
| 08 右上播报按钮 | `[917,131][1043,257]` | 126×126px = 48×48dp |
| 08 模型胶囊父区域 | `[190,131][483,257]` | 293×126px，约111.62×48dp；另有嵌套点击区域 |
| 08 第一个工具 chip | `[42,1948][372,2074]` | 330×126px，约125.71×48dp |
| 08 输入器外层 | `[32,2101][1048,2269]` | 1016×168px，约387.05×64dp |
| 08 EditText | `[211,2122][900,2248]` | 689×126px，约262.48×48dp |
| 08 加号 / 语音 | `[53,2122][179,2248]` / `[900,2122][1026,2248]` | 各126×126px = 48×48dp |
| 10 历史区 Log in 动作 | `[354,1537][564,1663]` | 210×126px = 80×48dp |
| 11 Notifications 行 | `[42,318][1038,465]` | 996×147px，约379.43×56dp |
| 11 Log In 行 | `[42,1505][1038,1631]` | 996×126px，约379.43×48dp |

文本 TextView 的 height 不等于字号或行高；本轮不提供 sp、字距、字重数值、圆角数值、阴影参数或 hover/pressed 色。UIA 文本 bounds 和实际视觉对齐有差异时以两者各自来源保留，不强行统一成布局 token。

## 可迁移规则与替换边界

以下为 **C：候选迁移规则**，不是对作者意图的判断；落实为新产品时属于 D 独立选择，应重新与目标平台核对。

| 候选规则 | 依据 / 适用条件 | 例外与替换边界 |
| --- | --- | --- |
| 给高频小动作独立圆形承载，组合到轻量工具区；让文字描述与图标语义可区分 | 08 顶部导航/播报、底部加号/语音，11返回；UIA多处有描述 | 10设置齿轮缺少描述，不能照搬其可访问性缺口；图标/文案换成新业务含义 |
| 工具入口使用低强调的“图标+短标签”胶囊，主内容区域保留留白 | 08 工具chip位于输入器上方，中央问候和大片空白 | 本轮只覆盖未登录空主页，不能直接套用信息密集列表或长回答页 |
| 区分接受协议、账号引导与普通设置动作的强调方式，并随主题保持文字/填充对比 | 03右侧蓝色文字Agree；10浅色主题黑色实心Log in；21深色主题浅色实心去登录；11浅灰全宽Log In但语义enabled | 不是单一全局“所有主按钮黑色/蓝色”规则；颜色与优先级需按语境和主题映射，当前未提取精确应用色token |
| 用空态解释受限原因和下一步，把主导航与登录引导并置 | 10历史区的跨平台同步说明+Log in，同时保留其他导航入口；11可未登录进入 | 不证明所有功能匿名可用；不能迁移Kimi同步承诺、品牌名或原业务栏目 |
| 设置用图标、正文、尾部箭头的单行模式分组，分隔线从文本列开始 | 11两组列表，版本值作为行内次要数据 | 未验证子页导航和返回栈；系统权限页面风格归Android平台，而非该列表系统 |
| 设置列表显示当前值，选择页使用尾部对号；即时主题选择与字号Save明确区分 | 12/13/14/15/18；Dark无需Save，字号文字说明要求Save | 主题只测试Dark一次及返回；字号未编辑保存；不能推广为所有偏好自动持久化 |
| 语言列表以语言自身名称帮助识别，选中后当前页面直接更新文案 | 19 English蓝勾→20简体中文蓝勾与中文标题；21中文抽屉 | 只测英语→简体中文；不证明其他语言字形、RTL、长文本或整个产品翻译完整 |
| 对可选系统权限的拒绝保留基础入口 | 07 Don’t allow→08 | 仅通知权限这一条实测；相机/录音/存储/定位另测 |

## 交互覆盖与下一轮补证

| 维度 | 状态 | 当前边界 |
| --- | --- | --- |
| 可见结构、文案、当前UIA语义 | 部分已验证 | 本页12类及部分设置控件，竖屏/未登录；英文与部分中文、浅色主页/抽屉和深色设置/抽屉 |
| 隐私拒绝、重开、同意与通知拒绝 | 已验证一次路径 | 无跨重启持久化、外侧关闭和系统返回测试 |
| 抽屉打开 | 部分 | 状态已见，单个动作和动画时机归因不足 |
| 抽屉齿轮→设置 | 已验证一次路径 | 返回/重选/保留未测 |
| 输入焦点 | 部分已验证 | 22→23有光标、输入器展开，后采UIA焦点true；截图无可见软键盘且几何非原子 |
| 编辑/提交/生成/停止/错误恢复 | 未验证 | 没有实际输入、发送或生成 |
| 模型、工具chip、附件、语音、登录 | 部分 | 模型/语音未进入；附件面板、拍照入口登录门槛及返回已补证，未认证 |
| 主题切换 | 部分已验证 | Follow System→Dark，无额外Save，Back后摘要及一次进程重启保留；设备重启/其他主题联动未测 |
| 语言切换 | 部分已验证 | English→简体中文即时更新，appLocales=zh-CN，一次进程重启保留；其他语言/RTL未测 |
| 字号缩放、横屏/窄屏 | 未验证 | 仅到达字号页、未编辑；不能把420dpi单一画布推成响应式规范 |
| pressed/focus/disabled/loading/error | 未验证或语义不明 | 灰色不等于disabled；蓝环不等于loading；没有hover测试 |
| 动画/手势/触觉/声音 | 未测 | 不从稀疏截图间隔推出duration/easing/spring |
| 参考实现、来源对照、不同业务迁移 | 本页未交付 | 本页不能作为完整风格库验收依据 |

后续补证见下节；表中较早覆盖边界由补证逐条更新。字号保存/取消、其他语言/RTL与登录后页面仍须独立研究。同一设备只由主采集者操作。

## 本轮后续补证：附件、登录门槛与进程重启

这些条目由主采集者在上述文档完成后逐张查看原图补入，仍为部分验证。

| 场景与操作 | 实际结果 | 事件契约可用部分 | 保留的未知 |
| --- | --- | --- | --- |
| 23输入聚焦后系统Back，再点主页“+”→24 | 遮罩后的主页前方出现底部面板：拍照、照片、本地文件、微信文件；下方技能及联网搜索/自动 | 输入能力按底部面板渐进披露，首页不直接展开所有入口；数据可归一化为attachmentItems、skillsEntry、searchModeSummary | 面板外侧关闭、拖拽、重复点击、每个入口结果未全测；不从单帧填sheet动画参数 |
| 24点“拍照”→25 | 进入登录页，有微信登录、手机号登录、未选择的协议控件与返回入口 | 本次未登录/相机入口先经过身份门槛；不把它写成已经请求相机权限 | 照片/本地文件是否同样要求登录未知；未测真实登录、授权或拍照 |
| 25系统Back→26 | 回到中文深色空白主页，附件面板当前帧未保留 | 登录门槛有正常返回路径；主页仍可浏览 | 请求取消、登录完成后的返回目标/原动作续执行未知 |
| force-stop + MainActivity启动→27→28 | 27为白色K标识启动帧；28为中文深色主页，出现“去登录” | 简体中文和Dark mode跨本次进程停止/重启保留；隐私协议本次未再次要求 | 没有重启设备/清数据/升级/换账号；不推持久化存储实现；启动帧间隔不等于闪白或动画时长 |

证据：[24附件面板](../evidence/runtime/scenes/24-input-options/screen.png)、[25登录门槛](../evidence/runtime/scenes/25-camera-entry/screen.png)、[26返回](../evidence/runtime/scenes/26-back-from-login/screen.png)、[27启动](../evidence/runtime/scenes/27-relaunch-persistence/screen.png)、[28重启后主页](../evidence/runtime/scenes/28-relaunch-settled/screen.png)。精确哈希、采集条件和操作顺序位于对应capture.json和session.json。

早期09与27的UIAutomator曾输出null-root，可能读到上次文件，因此它们的UIA不用于证明同一帧控件；截图保持原样。采集脚本之后增加了新XML确认和按可见文字查找点击，身份或前置状态不能确认时停止后续动作。29的“恢复主题”尝试实际仍在登录页，不构成恢复证明；最终恢复以32/33及restore记录为准。
