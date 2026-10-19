# 行为契约与业务替换边界

阅读 [spec.md](spec.md) 的身份/环境约束。除明确标为current的本轮b系列操作，A来自已有原 App 运行记录（`origin: historical`）；B为已保存代码，D为独立适配方案。每条规则只在列明范围内成立。参考是否实现、是否与原版一致，由 [coverage.json](coverage.json) 逐项给出。

## 可复用行为：事实与适配接口

| ID | 来源事实 | 适用范围与反例 | D 状态所有者/实施要求 |
|---|---|---|---|
| navigation.reselect | **A** 我的已滚动后重选本tab，前后节点相同；`interaction-checks.json.reselection`，hierarchy22a/22b | 本次guest My。没有证据支持全App“重选滚回顶”或全局刷新 | Shell拥有selectedTab与每tab滚动位置；RESELECT不重置。业务确有双击回顶时单列D例外 |
| navigation.return | **A** My→Settings→Back保留My滚动锚点；hierarchy22a/22c/22d，`settingsReturn.sameScrollAnchor=true` | 普通返回。切换系统night造成重建后返回Health是另外一条观察 | Route stack保存来源页面/scroll，POP回恢复；不把推入设置实现成重建整个shell |
| navigation.root-back | **A** 根页面系统Back让NexusLauncher获得焦点；`runtime/evidence/00-root-back.json`。重新启动跳过已完成欢迎 | Android、此首启状态；没有iOS或Web证据 | Android调用平台返回。Web保持正确History，桌面按应用退出约定，均标D适配 |
| navigation.configuration | **A** Settings时系统night切换后返回Health；`interaction-checks.json.themeRecreation` | 是本次配置变化实验，不是普通Back规则 | 目标按平台保留状态；若采用更完整恢复是D修正，不写成来源已做到 |
| route.context | **B** `HealthV2Fragment.createBundle/onGoPage`携did、local_date、sport_type进入指标详情 | 健康详情路径；原版登录/档案检查因指标而异 | 通用 `{entityId, dateOrRange, viewKind}` 快照随导航传递；业务校验在领域adapter |
| refresh.intent | **B** `HealthV2Fragment.initRefreshLayout`手动触发`triggerDataSync(true)`，`onVisible`传false | Health原生页。不是每个页面都会接同一种refresh | 区分USER_REFRESH与BECAME_VISIBLE；一个刷新owner。缓存、去重、取消/晚到处理未由此源码片段验证，目标独立选D |
| readiness.prepare | **B** `DataHeaderV2View.createContentView$lambda$5`：资源存在直接进分享；缺失showDialog→download；结果true进入，null短提示失败；`...lambda$4`负责dismiss | 分享素材准备。此B不证明传输取消、重试或精确duration | `{idle, preparing, ready, error}`；准备成功继续原intent；失败保留当前上下文和重试入口。取消/晚到结果策略由目标决定为D |
| action.guard | **B** 更新mine bundle mi-510 `useLoginStatusCheck`：异步查登录；要求登录且logout时open LoginModal并return；否则调用action | 五快捷入口均经此guard。native tab切换不是MainActivity硬guard。**A**取消SDK登录后Device/My仍可浏览（dynamic-crosscheck） | Guard作用于动作而非整tab；结果为allow/explain/auth/cancel；取消恢复原页。不能假设来源modal成功必自动继续intent，此点unknown |
| action.throttle | **B** 更新mine mi-490 SettingItem `leading:true,trailing:false,wait:300` | 仅该RN菜单行；native Settings行不能自动套该值 | 可配置leading-only throttle，作用于路由入栈事件；持续异步请求的防重提交是另一个D机制 |
| row.disabled | **B** `RightArrowSingleLineTextView/TwoLineTextView.onTouchEvent`：allowClickWhenDisabled=true时ACTION_UP仍performClick；默认false | 只对显式配置此能力的行；灰色不等于绝对不可触摸 | 把`visualEnabled`、`canExecute`、`onExplainUnavailable`分开；阻断执行业务时可给出解释，不能执行本来禁止的操作 |
| switch.authority | **A** Settings通知开关为开启且描述“当前已开启”，可访问性checked字段为false | 单个自定义SwitchButton；其他控件checked仍可有意义 | 业务boolean为唯一状态源，文字/track/thumb/accessibility同步；平台权限是外部依赖，不把switch本地变色当权限已获 |
| switch.toggle | **A current** 稳定guest b12为on；b14点击后off且页面保留，说明变为“你可能错过重要消息通知，点击开关允许应用通知”；b15恢复操作已发送 | 只确认UI状态与说明；不证明真正OS通知权限或跨重启保存 | 组件发requestChange(desired)，adapter回写权威value；value、文案、可访问性同步。不要强制认证 |
| switch.confounded-probe | **A current** b09点击后有账号登录；b10取消后b11已为off；后续b14稳定操作可直接切off | 存在异步认证混杂。b09不支持“toggle必先认证”因果，不能选择性丢弃反例 | 保留操作和后果，依稳定复测实现UI切换；账号/权限规则由实际目标adapter决定 |
| session.guest-logout | **A current** b13稳定guest点退出登录后页面/节点无可观察变化 | 未登录状态不代表已登录退出流程；没有确认/错误证据 | 来源fixture可发logout intent并保持页面；目标真实session行为按自有业务实现D |
| composition.variant | **A+B** Mine日夜同为11.202.21五入口；内置11.190.11为四入口且额外14dp行padding | 地区/账号决定分区组合，theme只影响资源；不能把数量差归因于night | 配置profile独立包含region/account/version；theme不得隐式改变业务条目 |

原始动态记录入口：[interaction-checks](../runtime/interaction-checks.json)、[日志与guard交叉验证](../dynamic-crosscheck.md)、[运行说明](../runtime/运行验证.md)。这些文件曾保存的是实际App行为；本地参考测试不产生新的来源A。

## 参考状态机：D接口，不冒充原版所有分支

### 导航与状态保留

```text
Shell(selectedTab, routes, scrollByTab)
SELECT_DIFFERENT_TAB → 保存旧scroll → 选择新tab → 恢复新scroll
RESELECT_SAME_TAB → 保持scroll和当前内容
PUSH_DETAIL(context) → 压栈，保留来源snapshot
BACK_WITH_DETAIL → 出栈 → 恢复来源snapshot
BACK_AT_ROOT → 平台根返回
CONFIG_CHANGED → 按目标平台保存/恢复策略（来源本次曾重置到Health）
```

上述scroll与snapshot API是D；其中重选、普通设置返回的期望有A支持。详情context模式有B支持。不要将“维持页面”误写成“所有页面永不释放/一定KeepAlive”。

### 受前置条件保护的动作

```text
idle --press→ checking
checking --allowed→ executing
checking --needsAuth→ explaining/authPending
explaining/authPending --cancel→ idle（原页面/滚动不变）
authPending --success→ 由目标明确选择「继续原intent」或「再次点击」
executing --success→ ready/目标页
executing --error→ error（保留输入/上下文，提供可恢复动作）
```

来源B只确认guard阻止未登录action、弹登录面板，以及允许时执行；来源A确认取消后仍可浏览。成功后续意图、错误重试和输入保留没有该分支的来源A，属于目标D选择。参考工程若用本地登录fixture，界面和验证报告必须说明它是本地状态演示。

### 开关与异步提交

```text
stable(value) --requestToggle→ adapter(desiredValue)
adapter --success(authoritativeValue)→ stable(authoritativeValue)
adapter --needsPermission→ 平台说明/请求 → 回读实际权限
adapter --failure/cancel→ stable(previousAuthoritativeValue) + 可恢复提示
```

这是D可靠性接口；来源current A确认稳定guest点击可以切换on/off及同步副文案。b09异步认证是混杂探针，不能推出toggle必须认证。目标是否乐观更新、请求期间禁用、怎样处理晚到结果由目标业务决定并测试。真正权限授予、跨重启保存与失败恢复均保留unknown。

## 先引导、后平台请求：覆盖边界

**A+B** 首启Welcome→Privacy→Region→Health；协议未勾选时点同意弹二次确认，系统Back不关闭这个特定确认；Country返回保留地区。首次流程中隐私基础模式与外观简洁主题是不同状态，不能合并开关。原图01~06及`PrivacyActivity`可追溯。

**B** RN位置路径存在pre-request说明后再请求平台权限。**D** 新业务应把权限解释放在具体需要时，拒绝后保留可重试入口；但原版通知、相机、蓝牙的所有拒绝/永久拒绝分支没有逐项A，不能宣传为全App统一已验证策略。

**A** Health空指标说明“记录您的…数据”，Mine未登录说明账号状态，Device空态给添加/扫码双入口，Records空态表达无记录。这些能支持“空态有原因和下一步”的迁移候选。原版加载、空态、失败不是同一状态；刚挂载RN出现空shell不应截图标成最终空态。

## 不迁移的健康业务规则

| 原业务专属 | 可保留的通用结构 | 在任务/资料软件中的替换示例（D） |
|---|---|---|
| 卡路里/步数/中高强度与400/6000/30目标 | 三列摘要、小单位、整体到详情context | 待办/本周完成/专注分钟；目标值由自己的业务定义 |
| 三环完成率、食物等价、活动次数 | 强调主进度、辅助说明/分享准备 | 项目进度或导出摘要；没有有意义的分母则删进度图 |
| 睡眠/心率/女性健康等排序与设备能力 | 用户排序持久化、能力决定可用条目 | 用户固定的任务分组、按已接入的数据源显示工具 |
| 小米账号、穿戴绑定、个人档案前置检查 | 动作层前置条件检查、取消后留在来源 | 自有团队权限/工作区配置，不接小米认证 |
| 设备提醒48小时冷却与手动例外 | 被动提示频率与用户主动请求分开 | 目标定义自己的提示间隔，不能照搬48h |
| 会员权益、医疗问诊、表盘/奖品URL | 服务区与快捷入口组合、一个明确主动作 | 项目模板、知识库、个人设置、导出等自有服务 |
| 健康量表/检测阈值/SDK协议 | 无直接通用替代 | 不列入基础设计系统；需要时另做业务研究 |

跨业务页使用相同TopBar、Summary/MetricCard、SurfaceCard、ChevronRow、BottomNav等实现，而不是另画一张配色类似的页面。组件不可在内部写死健康名词、登录服务、单位或真实品牌图。源资产可以留作研究对照，目标采用自有品牌与可用图标。

## 明确保留的unknown

未验证：编辑/草稿/保存/撤销、完整网络失败重试与晚到结果、真登录成功、真实设备同步、健康数据算法、键盘/IME、读屏焦点恢复、完整页面进出动画参数、system reduced motion、横屏/折叠设备实际排版、OEM中文逐字face。原滚动录屏实际解码4.3774秒是采样窗口，不是任一动画duration。RN Navigator弹簧friction20/tension200仅是其源B，不能当native Settings实测参数或全局motion token。

目标项目存在这些能力时，先审查其已有正确性并保留。选择/修复目标行为标D，给验证证据；不要为了“风格统一”删除现有功能，也不要用更多报告替代缺失的来源测试。
