# Run02 登录后：提交、完成与停止输出

本页独立复核本轮登录后的安全测试样本 **20、21、22、24、25、26**，不修改此前报告。原 APK 为 Kimi Android **3.1.3 (376)**，包名`com.moonshot.kimichat`，SHA-256 `9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332`。用户已通过正常登录完成认证，本文不记录手机号、验证码、默认昵称或账号标识，也未查看含身份信息的18/19样本。

**A** 为已实际查看的原图/UIA或已记录操作，**C**为可迁移候选。认证完成本身来自主采集者交接说明，本文只研究其后的中性自建会话。所选截图条件均为1080×2400物理px、420dpi、fontScale=1.0、英文浅色、appLocales=en-US、buildLocale=en-US、systemLocales=null、系统night mode=no。使用原应用网络，无测试后端fixture；这不等于验证了全部后台机制。

操作号引用[run02 session](../evidence/runtime/run02-20261016/session.json)的actions数组，**1-based**。安全场景与转移的机器可读副本为[auth-reviewed-transitions.json](../evidence/runtime/run02-20261016/auth-reviewed-transitions.json)。本地AUTH-T编号不是REA Evidence ID。截图和UIA依次获取，不能当作原子快照；20存在明确的状态推进差异。

## 安全样本复核

| 原图 | A：像素与后采UIA | 能支持 / 不能支持 |
| --- | --- | --- |
| [20 first-submit](../evidence/runtime/run02-20261016/scenes/r02-20-first-submit/screen.png) | 用户气泡完整为`Reply only OK`；截图底部右侧黑圆内白色方块、占位`Send messages anytime…`，回答区为等待形态。**后采UIA已变为Thinking complete / OK / 操作栏** | 能确认提交文本与生成阶段曾显示，不能将本capture全体文件视为同一个生成状态 |
| [21 first-complete](../evidence/runtime/run02-20261016/scenes/r02-21-first-complete/screen.png) | `Thinking complete`可折叠样式行、回答`OK`、回答操作栏；输入器恢复`Ask anything. Images work too.`，方块停止控件不在 | 一次完整中性请求得到可见结果；没有测完成前是否可再次提交、后台请求标识或精确耗时 |
| [22 conversation-menu](../evidence/runtime/run02-20261016/scenes/r02-22-conversation-menu/screen.png) | 点More后的早帧仍是同一聊天与OK，UIA也仍为聊天；右上按钮有浅色按压外观 | **菜单内容未证实**，不能按目录名列出菜单项或声称已打开 |
| [24 generating-sequence](../evidence/runtime/run02-20261016/scenes/r02-24-generating-sequence/screen.png) | 完整用户气泡`Count 1 to 1000, one per line.`；Thinking行、生成占位和白方块停止外观；UIA仍把该ImageView描述为`Send message` | 生成状态可确认；可访问性描述与视觉功能不一致，不能只靠旧desc或旧坐标判断当前动作 |
| [25 stop-result](../evidence/runtime/run02-20261016/scenes/r02-25-stop-result/screen.png) | 已出现可滚动`text`结果卡，当前视窗可见1至20和回答操作栏；输入器是`Hold to talk`，右侧键盘图标、UIA描述`Switch to text input` | 这次“停止”点击晚到，实际为语音输入待按住态；没有`Output stopped`。不能称stop成功，也不能从前20项证明1000项都生成 |
| [26 checked-stop](../evidence/runtime/run02-20261016/scenes/r02-26-checked-stop/screen.png) | 回答区明确`Output stopped`；工具过程框同时含`Create a file containing numbers 1 to 1000`与`Failed to execute Python code`；输入器恢复空文本态 | UI输出停止结果已证实；工具错误与停止仅同帧并存，发生顺序和因果未知；左侧Gboard浮条排除出Kimi组件 |

原图同目录的capture.json与hierarchy.xml保留时刻、hash与条件。具体时间边界：20截图`2026-10-16T03:32:19.330Z`，采集结束`03:32:31.145Z`；21截图`03:33:31.085Z`。20后采UIA已经完成，故不能用20→21的截图间隔当生成时长。所有时间为UTC。

## 提交、完成与回答工具栏

action51定位`Send message`后提交自建中性请求，20用户气泡完整显示`Reply only OK`，其结果与早先仅输入`Re`的情况不同。21确认回答为`OK`，Thinking complete行在回答之前，输出后的工具栏在回答下方。工具栏UIA描述依次包含`read_aloud`、`Copy`、`Rate answer`、`Retry`、`Share`；其中Rate answer容器在该UIA中`clickable=false`，不补造评分弹层。Copy、朗读、评分、分享都只是入口可见；**没有已验证的剪贴板内容或成功提示**。

action52点右上`More`后，22未显示菜单。随后action53的说明写“关闭More菜单”，但主采集者实际观察是系统Back返回空主页；该空主页未纳入本页安全截图集。因此保留为**操作者报告的导航结果**，不把动作说明当菜单存在的证据。之后Copy定位尝试没有匹配且未执行，不能补造一个Copy动作序号或记为功能通过。

可迁移的 **C候选** 是“生成中和完成后在输入器同一尾部位置切换控件；完成后将答案工具就近放在内容下方”。例外是状态会在采集与点击间变化，24的停止外观仍带`Send message`描述；迁移实现应使用当前生成状态驱动动作，不能复刻这一语义歧义或依赖旧坐标。

## 两次停止尝试必须分开

**第一次未验证成功。** actions54/55准备并完整输入计数请求，action56提交；24截图为生成中。action57在较晚时刻点旧的尾部控件坐标，25已经是结果卡和`Hold to talk`。主采集者判断该点击切到了语音待按住模式，现有图/UIA支持这一最终状态；但没有点击前同一瞬时帧，不能给出精确完成时刻。此例不能当作停止成功、取消后输出部分列表或后台仍继续生成的证明。也没有按住录音、请求麦克风权限或产生录音。

**第二次有当前状态守卫。** action58先回文字输入，action59定位`Retry`重试自建计数答案。action60在新取得的UIA中同时确认：

- `Thinking`和生成占位`Send messages anytime…`存在；
- 恰有一个`content-desc=Send message`、class为`android.widget.ImageView`的尾部候选；
- 本次候选bounds为`[911,2168][1037,2294]`，取中心`[974,2231]`后才点击。

守卫原始证据为[stop-check-1792121967622.xml](../evidence/runtime/run02-20261016/stop-check-1792121967622.xml)，实现片段见[android-session.mjs](../tools/android-session.mjs)。这证明进行过一次新的语义状态核对，仍不意味着检查与点击之间绝无竞态。26随后同时在像素与UIA出现`Output stopped`，因此本次 **UI输出停止已验证**。网络请求是否真正取消、计费是否停止、服务端任务是否继续、晚到结果如何处理均未知。

26工具过程框的Python执行失败是独立可见反馈。本轮未捕捉失败与stop的精确先后，也未核对工具错误码，不能写“stop导致Python失败”或“Python失败导致停止”。Retry这一次触发了新的生成状态，但其他错误的重试、输入保留和成功恢复不在覆盖范围。

## 输入环境与系统层边界

主采集者在私有环境截图识别了Gboard `Try out your stylus`教学层，随后关闭。该私有截图与身份页均未被本复核读取或复制。26左侧带删除、发送、表情和菜单图标的浮条由主采集者识别为Gboard系统输入层，本文不把它登记为Kimi浮动工具栏；其截图内保留是系统环境事实。

先前只出现`Re`和用户“无法输入”的报告与这类输入环境干扰相符，但不能把系统教学层认定为所有早先不完整输入的唯一原因。现有安全样本只直接证明，在系统层恢复之后，本轮完整`Reply only OK`和计数请求能够出现在Kimi用户气泡并提交；不验证任意长文本、中文IME组合态、回车换行或键盘发送设置。

## 转移与覆盖

| 辅助编号 | 起点 / 操作 | 后续观测 | 归因边界 |
| --- | --- | --- | --- |
| AUTH-T01 | action51提交完整中性请求 | 20截图生成；20后采UIA与21为OK完成态 | 提交前私有样本不读取；结果用户气泡证明完整请求进入会话 |
| AUTH-T02 | 20→21，无新增动作 | 完成行+OK+工具栏，普通空输入器 | 仅状态推进，不测精确时长 |
| AUTH-T03 | 21 action52 More | 22仍为聊天，菜单未见 | 未验证菜单打开/内容 |
| AUTH-T04 | 22 action53 Back | 操作者报告空主页；随后Copy尝试无匹配未执行 | 无安全截图直接证明该主页；无Copy动作索引 |
| AUTH-T05 | actions54/55/56输入并提交计数请求 | 24 Thinking+生成占位+方块 | 完整请求已见，不证明后台算法 |
| AUTH-T06 | 24后action57旧坐标点击 | 25完成样式结果卡+Hold to talk | stop未成功验证，实际进入语音待按住态 |
| AUTH-T07 | 25 actions58回文字、59 Retry、60当前状态检查后点stop | 26 Output stopped | UI取消已见；网络取消未知；工具失败不造因果 |

本页已覆盖一次成功回答、一种生成中UI、一次晚到停止误操作和一次有当前状态守卫的UI停止结果。尚未验证Copy/Share/朗读/评分、More菜单、所有停止边界、后台取消、登录/凭据流程、附件、实际录音、失败重试到成功、会话重开与持久化。它是登录后行为的部分证据增补，不能代替完整风格库或功能验收。
