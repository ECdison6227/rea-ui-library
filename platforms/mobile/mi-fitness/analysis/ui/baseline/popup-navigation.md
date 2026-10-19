# 弹窗、跳转、返回与状态矩阵

本补充与 [spec.md](spec.md)、[contracts.json](contracts.json) 一起使用；机器可读字段和源码hash/行号在 [popup-navigation.json](popup-navigation.json)。这份矩阵逐入口规定弹窗与导航，**没有一条“全App统一返回/统一关闭弹窗”规则**。来源身份为APK3.57.0、mine更新包11.202.21；本轮b系列为A/current，旧首启及系统主题试验为A/historical，源码为B/historical并于本轮复核。C为推断，D为独立参考/迁移选择。

## 最重要的区别

- **A+B** 我的快捷入口和周报在guest下先显示RN“是否现在登录”确认，取消/遮罩/系统Back的结果见下表。它和原生 `AccountLoginActivity` 是两个界面。
- **B** 当前`GlobalModalProvider`位于`mi-631`（捕获bundle:639），不是`mi-422`；后者是请求失败后的地区guest fallback。Provider用FIFO队列，当前显示一项，不是每来一个弹窗就覆盖或同时叠两层。
- **A+B** 隐私二次确认设置`setCanceledOnBackPressed(false)`，历史实际Back未关闭；不能套用我的登录确认的可关闭规则。
- **A** 普通My→Settings→Back保留滚动、重选My也保留。**A historical** Settings时切系统night造成重建后返回Health，是另一条路径；不把它写成每次Back都回Health。

## 弹窗矩阵

| 弹窗/界面 | 何时出现 | 明确取消 | 系统Back | 点击遮罩 | 确认后的顺序 |
|---|---|---|---|---|---|
| My RN LoginModal | **A current** b34我的活动、b36周报；**B** 五快捷入口、受限菜单、guest任务中心及健康问诊均走LoginModal | **A current** b35取消回Mine顶端、主要元素坐标不变 | **A current** b39开活动guard，b40系统Back回Mine顶端；**B** onRequestClose→closeModal | **A current** b38点(540,1000)关闭，回Mine顶端 | **B** closeModal先执行，后`setTimeout1000ms`调showLoginPage。1000ms没有提升为A实测 |
| 原生AccountLoginActivity | **B** guest资料挂载/点击直接调bridge；**A current b44/b45** RN确认正向后实际进入账号登录，31节点 | SDK专门取消按钮unknown | **A current b46** 系统Back返回Mine顶端131节点，没有确认弹窗残留 | 不按popup mask建模：它是原生完整页面 | 登录成功、输入保存、原intent继续均unknown；本次不输入凭据 |
| My AddIcon锚定菜单 | **A current b47** 出现“添加设备/扫一扫”；**B** 点击置local isOpen=true，锚点measure提供top | 无专门取消项 | **A current b49** 关闭回Mine131节点 | **A current b51** 点外(540,1000)关闭回Mine131节点 | **A b53** guest选添加设备到SDK登录；**B** 先关闭菜单，再检查login/privacy/native push；iOS200ms不当Android实测 |
| 首启隐私二次确认 | **A historical+B** Privacy协议未勾选时按同意 | **B** negative dismiss | **A historical+B** 不关闭 | unknown：调用处未指定touchOutside，未追CommonDialog默认 | **B** handlePrivacyAgree再dismiss；后续进入地区/首页受配置影响 |
| Health分享素材准备 | **B** 素材缺失显示下载进度；存在则直接进分享页 | unknown | unknown | unknown | **B** true→dismiss并进入分享；null→dismiss与短失败提示。取消下载/晚到结果未知 |
| 独立参考的详情边界、认证、编辑/错误提示 | **D** 各自本地adapter触发 | 按目标工程定义与实测 | 按目标工程定义与实测 | 按目标工程定义与实测 | **D** 不能拿这些界面填原App二级UI的A覆盖 |

对应A：[b34](../runtime/hierarchy/b34-activity-guard.json)、[b35](../runtime/hierarchy/b35-guard-cancelled.json)、[b36](../runtime/hierarchy/b36-weekly-guard.json)、[b38](../runtime/hierarchy/b38-mask-result.json)、[b40](../runtime/hierarchy/b40-back-result.json)。原版确认图：[shortcut-guard](runtime/shortcut-guard/screen.png)。原图只供视觉，取消结果必须结合操作与后续hierarchy。

## 叠加、重复与生命周期：不能由一次关闭推成统一规则

| 项目 | 来源事实/未知 | 实现含义 |
|---|---|---|
| 多请求 | **B mi-631** `current!=null`或external已开时push；`closeModal`用`shift()`推进下一项；本Provider只渲染一个current | 要复用该行为就实现FIFO；“关闭”可能显示下一条而非隐藏全部 |
| 重复触发 | **B** Provider没有按key去重；key存储不等于去重。**A current** b41打开后约200ms b42再点原快捷坐标，落在遮罩，最终回Mine；没有观察到可见叠层 | b42证明触摸被当前modal消费/关闭，不证明并发openModal不会排队；同类多请求压力测试仍unknown |
| 300ms节流 | **B mi-490** 仅RN SettingItem为leading-only300ms；共享登录guard本身未显示同样节流 | 不能用一个300ms全局规则替代每种动作的防重入 |
| 后台再回来 | **A current** b36周报guard在b37恢复后仍可见；b47添加菜单在b48恢复后仍保留两项与位置 | 只证明这两次后台/恢复；不证明process death或重建保存 |
| 跨页/跨tab | **B** Provider在MineTab内；调用处未传external检查，默认Promise.resolve(false)；没有显式route-leave清队列 | 不能声称与原生SDK共享同一弹窗栈；未测跨tab退出时队列存活 |
| 关闭后 | **B** 无pending时current ref=null、visible=false；有pending时替换current并维持可见；render-state组件字段未显式置null | 精确隐藏挂载与同类型实例复用不能用“组件名像”判断；运行unknown |
| 退出与延时 | **B mi-484** 登录按钮的1000ms timeout没有局部clearTimeout或卸载guard | 点击后马上离开是否仍打开原生账号页unknown；不要伪称原版取消了晚到意图 |
| 主题/进程重建 | Provider只见useState/useRef，没有序列化队列；本组没有modal主题切换A | unknown。目标自己加保存/取消属于D，不能反写为来源事实 |

`IOScrollView`也不是“自动保存所有路由滚动”的证据：更新包`mi-259:267 → mi-269:277 → mi-267:275`封装RN ScrollView，只记录observer的contentOffset、转发onScroll/onLayout和scrollTo等方法，没有持久化恢复偏移的代码。其observer初始offset=0也不是实际原生滚动重置指令。返回是否保留要逐路径看A和宿主生命周期。

## 导航与状态矩阵

“栈层”说明可确认的route/overlay关系；没有完整task dump时不编造数字深度。`—`为此状态没有输入器；`?`为unknown。表内“回原位”只对相应A路径成立，详细字段分类见JSON。

| 来源与入口 | 目标/栈关系 | Back或取消结果 | 滚动 | 选择/输入 | 主题重建 |
|---|---|---|---|---|---|
| My滚动菜单→App设置 | **B** native SettingActivity push；数字task depth? | **A current b16** 回My | **A** App设置label前后`[143,831][943,906]` | **A** My保持选中；输入— | **A historical** 系统night试验回Health；设置值恢复? |
| My滚动→重选当前My | **A** 同一root tab；无可见新页 | 未进入新页；root Back另测 | **A b17** 同锚点 | **A** My保持选中；输入— | ? |
| My顶端→活动 | **A+B** LoginModal overlay，不执行原活动route | **A** 取消、遮罩、Back均回My顶端 | **A** 当前顶端主要锚点一致；未证明滚动菜单的取消 | **A** My选中；输入— | ? |
| My顶端→周报 | **A+B** 同LoginModal | **A** 遮罩回My；后台回来仍在guard | **A** 资料/快捷/周报位置一致 | **A** My选中；输入— | ? |
| LoginModal→登录 | **A current b44/b45** 进入SDK登录；**B** 先关闭再1000ms请求；真实task depth? | **A b46** 系统Back回My，没有guard残留 | **A** 本路径回Mine顶端，profile/shortcut/weekly坐标一致 | **A** My选中；SDK输入?（未输入） | ? |
| My guest资料挂载/点击 | **B** 直接原生登录请求；不是一定先RN确认 | **A historical** 取消后可浏览 | ? | 身份更新/输入? | ? |
| Health→hero | **A b19** 活力指标详情，日期2026-10-15 | b20已发Back，精确配对回页状态待补 | ? | **A** 进入日期；改日期再回?；输入— | ? |
| Health→睡眠空卡 | **A b21** 睡眠详情，无数据/佩戴解释 | b22已发Back，精确配对回页状态待补 | ? | **A** 进入日期；改粒度再回?；输入— | ? |
| root主tab→其他tab | **B** root切换，不推成每个tab压栈 | **A historical** root Back到Launcher | 跨tab恢复?，不能借同tab A泛化 | 重启/主题选择? | ? |
| My→系统权限/关于 | **B** 各自native push，guest可达声明 | 各自Back未A | ? | 输入— | ? |
| My受限菜单项 | **B** LoginModal；不是立刻目的页 | **B** provider关闭；滚动菜单A未单列 | ? | My预期保留仅D；输入— | ? |
| My→任务中心（guest） | **B** 更新包`mi-623:631`，UnLoginTaskCenterEntrance的SettingItem直接openModal(LoginModal) | 此入口取消/Back未A | ? | 输入— | ? |
| My→健康问诊（guest） | **B** 更新包`mi-546:554`，UnLoginHealthConsultationNew的onPress直接openModal(LoginModal) | 此入口取消/Back未A | ? | 输入— | ? |
| My→App版本 | **B** 更新包`mi-488:496 → mi-489:497`，点击跟踪后调CommonBridge.checkAppUpgrade；选中链没有登录guard | bridge后弹窗/更新去向及Back未验证 | ? | 输入— | ? |
| My头部AddIcon | **A b47** 两项local菜单；**B** 独立local状态、非GlobalModalProvider队列 | **A b49/b51** Back/点外回Mine | **A** 当前顶端锚点一致 | **A** My选中；输入— | ? |
| My AddIcon→添加设备 | **A b53/b54** guest进入SDK登录31节点；**B** local menu先close再login check | **A b55** 系统Back回Mine131节点，不重开Add菜单 | **A** 本路径顶端profile/shortcut/weekly锚点恢复 | **A** My选中；SDK输入?；未输入 | ? |
| My AddIcon→扫一扫 | **B** local menu先close再login check/privacy/native route | 此选项未A | ? | 菜单无保存选择字段；输入? | ? |
| Privacy未勾→同意 | **A historical+B** 二次确认 | **A** Back不关闭，negative只B | ? | checkbox取消后?；输入— | ? |
| Main root→系统Back | **A historical** Launcher获焦点；task是否移除? | 重开进入Main、未回Welcome | 重开位置? | exact tab?；输入— | ? |

没有“返回统一reset”或“返回统一preserve”。上表未知不能用参考实现的页面缓存去补：目标源码已有的路由、输入、筛选、草稿、请求和保存状态必须审查后分别映射。

JSON为每个已声明的受限菜单项、五个快捷入口保留单独route行，即使共享guard也不借“活动已测”把其他入口升级为A。当前矩阵限定于列明入口和历史首启样本；健康下方未进入的卡、远端运营服务与SDK深层流程保留unknown，不能读成全App所有入口已完成。

App版本与受限菜单必须分开：`UpdateSettingItem`挂载时请求`/app/v1/version/check_upgrade`来控制新版提醒点，点击时走原生升级bridge；错误catch返回false。不能因相邻受限菜单需登录，就给App版本也加登录guard；bridge真实弹窗和更新行为本组未A。

## 迁移时让Agent作出的明确选择

**D** 把overlay owner、route stack owner、领域状态owner分开。每个popup写trigger、cancel、Back、mask、queue/dedup、重复动作、跨页、后台/退出、延时/取消和焦点恢复；每条route写来源、目标、push/replace/tab、返回后的scroll/selection/input及配置变化。保留来源中已测的普通返回与当前tab重选行为。

对目标产品决定的队列去重、跨页清理、延时取消、保存/草稿、输入恢复和错误重试，标D并实际运行验证；不要为了忠实抄一个未测或可能有缺陷的生命周期。原生详情边界和独立认证sheet只是D接线示范，不算复现了所有原版popup。数量不是本矩阵完成条件；真正的闭环在对应操作、状态与源码可追溯。

当前独立原生参考对应`StyleKit.confirmSheet`、`MainActivity.showAddMenu`和`authBoundary`。确认队列无去重，Add菜单用平台PopupWindow关闭/返回。`externalModal`暂停/继续排队和退出后`alive`检查属于 **D可靠性适配**：源MineTab没有传external checker、源LoginModal没有局部timer清理，不能将参考的补充机制写成原版已验证行为。扫一扫只演示未验证目的边界。最终参考测试与截图对照应读取覆盖表中的`reference-final`和`comparison/final`，旧APK通过的结果不代替最终构建验证。
