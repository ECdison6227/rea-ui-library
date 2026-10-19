# Run02：模型、草稿与字号行为复核

这是 `run02-20261016` 的独立运行复核，保留首轮结论与本轮条件边界。目标仍为原 APK `com.moonshot.kimichat` **3.1.3 (376)**，SHA-256 `9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332`；未登录、独立 AVD `rea_kimi_ui_20261015` / `emulator-5596`、原应用网络，无 fixture。截图/UIA为 **A、本轮current**，均已实际查看，不按目录名称判断页面。

本轮记录为1080×2400物理px、420dpi（density=2.625）、fontScale=1.0、系统night mode=no、appLocales=en-US、buildLocale=en-US，systemLocales字段为null。当前画面为英文浅色；它与首轮曾选中文/深色的画面不同，现有证据不说明中间发生了什么，不把本轮启动状态当作“首轮偏好重启丢失”的结论。所有截图与UIA均先后采集，非原子快照。时间为UTC，具体采集区间、文件hash和方法见各capture.json。

原始操作来自[本轮session.json](../evidence/runtime/run02-20261016/session.json)，本文动作号为其actions数组的**1-based**序号。可读转移副本见[reviewed-transitions.json](../evidence/runtime/run02-20261016/reviewed-transitions.json)；本地R02-T编号不属于REA Evidence ID。

## 场景复核

| 场景与原图 | 像素复核 / UIA要点 | 适用边界 |
| --- | --- | --- |
| [01 resume](../evidence/runtime/run02-20261016/scenes/r02-01-resume/screen.png) | 系统launcher上出现 `System UI isn't responding`；UIA package为android | 环境ANR，排除出Kimi组件规范；即使topResumedActivity写Kimi也不能改写像素事实 |
| [02 model-entry](../evidence/runtime/run02-20261016/scenes/r02-02-model-entry/screen.png) | Kimi主页后面可见，但系统ANR仍覆盖；UIA仍是Wait/Close app | action2的模型点击未验证；不是模型选择菜单 |
| [03 after-system-recovery](../evidence/runtime/run02-20261016/scenes/r02-03-after-system-recovery/screen.png) | 已恢复Kimi主页，顶部Instant/High；欢迎语下方有Log in；空输入器占位 `Ask anything. Images work too.` | 中央蓝环是头像/动效采样，加载语义未证实 |
| [04 model-menu](../evidence/runtime/run02-20261016/scenes/r02-04-model-menu/screen.png) | 模型点击后的早帧仍是主页；本次UIA dump失败，capture.hierarchy=null | 不能补用其他旧UIA；也不能由早帧称点击失败或算动画延时 |
| [05 model-menu-settled](../evidence/runtime/run02-20261016/scenes/r02-05-model-menu-settled/screen.png) | 大圆角菜单覆盖主页，K3/K2.8/Instant与Thinking effort；Instant旁蓝勾，High为当前摘要 | 同一次action5之后、无新增操作的后续帧，菜单可确认打开；菜单的精确出现时刻未知 |
| [06 after-k3](../evidence/runtime/run02-20261016/scenes/r02-06-after-k3/screen.png) | 选择K3后回主页，胶囊为 `Kimi K3`，空输入器占位改为 `Ask me. Task me.` | 确认选择结果和两处UI变化，未验证请求实际上路由到何模型 |
| [07 unsent-draft](../evidence/runtime/run02-20261016/scenes/r02-07-unsent-draft/screen.png) | 软键盘明确可见，输入器有光标，像素和EditText均仅 `Re`，focused=true；上方credit提醒与Got it | 命令打算输入长句，实际仅证实两字符；没有发送 |
| [08 draft-back](../evidence/runtime/run02-20261016/scenes/r02-08-draft-back/screen.png) | 键盘隐藏、欢迎语回来，输入器仍 `Re`，UIA focused=false；credit提醒仍在 | 此次Back未写入session action，且07/08采集期间存在重叠操作，归因见下文 |
| [09 draft-settled](../evidence/runtime/run02-20261016/scenes/r02-09-draft-settled/screen.png) | 后续稳定采样仍为 `Re`，键盘隐藏、credit提醒仍在 | 证明后续没有出现完整长句，不能声称完整输入成功 |
| [10 draft-after-drawer](../evidence/runtime/run02-20261016/scenes/r02-10-draft-after-drawer/screen.png) | 执行Got it与菜单/Back序列后的主页仍有 `Re`；提醒已不见 | 中间未单独截图Got it结果和抽屉开态，两个结果的逐动作时序未隔离 |
| [11 font-baseline](../evidence/runtime/run02-20261016/scenes/r02-11-font-baseline/screen.png) | 字号页Follow System为开、滑杆thumb在Standard附近，右上Save对号承载浅灰 | UIA开关checked=true；Save区域enabled=true但clickable=false，不能仅凭灰色叫disabled |
| [12 font-unsaved](../evidence/runtime/run02-20261016/scenes/r02-12-font-unsaved/screen.png) | 关闭Follow System并拖至最右后，预览文字变大/换行改变，thumb在Extra Large端；右上Save黑底白勾 | UIA开关checked=false、Save clickable=true；仅本地预览，尚不能证明保存 |
| [13 font-back-result](../evidence/runtime/run02-20261016/scenes/r02-13-font-back-result/screen.png) | 未按Save而Back后进入Appearance，Font size摘要仍Follow System | 确认此次离开没有把预览值写入摘要 |
| [14 font-reenter](../evidence/runtime/run02-20261016/scenes/r02-14-font-reenter/screen.png) | 重开字号页，Follow System再次开启、thumb回Standard附近、预览恢复基线、Save clickable=false | 本次未保存修改被放弃；未遇到离开确认弹窗 |
| [15 font-save-result](../evidence/runtime/run02-20261016/scenes/r02-15-font-save-result/screen.png) | 再关Follow System、拖到最右并按Save后，**直接到主页**；文字变大，输入仍Re，模型仍Kimi K3 | 保存结果的目标不是Appearance，内部返回栈/重建原因未知 |
| [16 font-saved-summary](../evidence/runtime/run02-20261016/scenes/r02-16-font-saved-summary/screen.png) | 从15重新经导航/设置进入Appearance，Font size摘要Extra Large | 本次保存值跨这些导航保留；未在这里证明跨进程持久化 |

每一行的同目录[层级/身份记录入口](../evidence/runtime/run02-20261016/scenes)保留原始UIA及capture.json。04明确无新UIA，不将其他场景层级当作该图证据。

## 模型菜单与选择后的反馈

05的菜单结构为每个模型名配次要解释：

- K3：`Chat & Agent, flagship all-rounder`
- K2.8：蓝色 `Preview` 与 `Well-rounded and efficient for everyday tasks`
- Instant：`Fast chat, quick replies`，当前项尾部蓝色对号
- 分隔线后为 `Thinking effort`，尾部 `High` 和箭头

UIA保留这些原文；对号的content-desc仅为`icon`，不补造选中radio语义。action6点击已定位的K3文本后，06菜单关闭、主页胶囊从Instant/High变成Kimi K3、placeholder同步变化。现有范围只验证“从当前Instant菜单选K3这一条路径”。K2.8、Thinking effort子菜单、外侧关闭、返回、重选、模型选择是否跨新会话/重启保存、已有草稿切换模型后的结果均未测试。

可迁移的 **C候选** 是“选择器在同一菜单内同时呈现短名称、适用说明、当前标记，选择结果回写到常驻入口，并让输入提示与当前能力一致”。复制到新业务时应替换模型名、能力描述和计费语义；不能从本次菜单推断所有选择器都如此组织。

## 输入、提示与保留

action7聚焦，action8命令为 `input text Research%sdraft%s20261016`，但07/08/09/10原图和UIA均只见 **`Re`**。本轮仅确认一个两字符的未发送本地草稿。为什么长串没有完整输入未知；不归因于产品长度限制、网络、权限或自动截断。

07软键盘可见，输入器内增加黑色圆形向上箭头，UIA描述 `Send message`，语音与加号仍在。它证明有非空输入时发送入口可见，不证明发送成功、登录前可生成、Enter/换行机制、IME组合态或错误恢复。没有按发送键或软键盘发送键。

07第一次出现的credit提示为独立圆角说明卡，位于输入器上方，原文 `Conversations under this model consume credits faster`，尾部 `Got it`。06尚未见此卡，07在聚焦/输入后见到；出现条件没有隔离，不能认定只由输入第一个字符触发。提醒没有遮挡编辑器或取消发送入口，但模态性、外侧点击与自动消失规则未测。

08/09键盘已经隐藏且EditText `focused=false`，`Re`仍在，欢迎语和Log in重新可见。root报告曾发送一次Back且07/08采集窗口与操作重叠；session没有该Back条目（08/09的actionsBeforeCapture仍为8）。因此“键盘隐藏和文本保留”是A观测，“由这一条Back单独造成全部布局变化”不具完整动作记录。保持缺口，不虚构动作序号、完成时间或动画时长。

09之后action9点Got it，action10点导航，action11系统Back；10最终主页仍是`Re`、credit提示不在。可确认本次记录序列没有清空这两字符。不能把没有中间截图的序列拆成“Got it立即删除提示”“抽屉曾稳态完全打开”等已隔离视觉结果。15在字号保存后直接返回主页时也仍有`Re`，由此补到本次设置导航/保存序列中的保留；仍不能扩大为跨进程、账号或设备的草稿持久化结论。

## 字号编辑：预览、放弃和保存

actions12–15经过菜单/设置/Appearance进入11字号页。UIA `Follow System`开关为checkable=true/checked=true；thumb视觉在Standard位置附近，SeekBar只报告enabled=true，没有数值。不能把Standard换算为具体sp。

action16关Follow System，action17横向拖动到最右，12同一页面预览文字变大、换行改变，保存图标从浅灰承载变为黑色。Save区域`[917,134][1043,260]`的clickable由false变true，两帧enabled均true。这支持“当前未更改基线与有待保存改动时，Save可点击语义/强调不同”，不支持单凭截图判断disabled属性。字号预览改变不说明所有页面的正文已经改变。

随后action18直接系统Back，13为Appearance，摘要Font size仍Follow System；action19重入，14又是Follow System checked=true、Standard附近thumb和基线预览，Save clickable=false。**此次未保存修改被放弃**。采样中未出现确认/丢弃对话框；没有连续录屏，不能排除所有瞬时层，只能说明本次后续状态不含该层。

actions20/21再次关Follow System并拖到最右，action22点右上对号Save；15截图直接显示主页，字形明显放大、问候换行改变，未发送`Re`与Kimi K3仍在。**实际保存去向是主页，不是Appearance**。不能把没有显式记录的内部Activity重建、返回栈清空或页面重挂载当作原因。actions23–25再次经菜单/设置打开Appearance，16摘要为Extra Large，正文/标题也更大；系统fontScale记录仍1.0，所以这是应用内字号选择的结果，不是系统字体比例改动。

本轮证明预览可放弃、显式Save提交且回主页、再次进入设置可读到Extra Large；精确sp值、全部页面适配、重启持久化还需独立证据。可迁移的 **C候选** 是区分编辑草稿与已应用偏好，Back放弃未保存选择、Save后在摘要回写；保存跳主页属于已观察到的原产品行为，迁移时是否保留应作为D选择并考虑目标产品返回路径。

## 已复核转移与归因

| 辅助编号 | 起点→结果 | 本轮action序号 | 状态 / 未知 |
| --- | --- | --- | --- |
| R02-T01 | 01系统ANR→02仍为系统ANR | 2模型坐标点击 | 非产品交互成功；点击目标受系统浮层影响 |
| R02-T02 | 02ANR→03恢复主页 | 3 Wait、4 launch | 环境恢复操作；不是Kimi错误恢复范式 |
| R02-T03 | 03主页→04早帧主页→05菜单 | 5模型胶囊 | 菜单最终可见，出现时刻与动画未测；04无UIA |
| R02-T04 | 05菜单→06 Kimi K3主页 | 6点K3 | 标题/placeholder变化已见；后端模型行为未知 |
| R02-T05 | 06空输入→07 `Re`且键盘可见 | 7聚焦、8输入命令 | 实际仅两字符，提示出现与聚焦/输入关联未隔离 |
| R02-T06 | 07键盘可见→08隐藏→09后续保持 | session无对应Back条目；操作者报告Back | 键盘/焦点/文本结果已见，动作时间归因不完整 |
| R02-T07 | 09提示+`Re`→10无提示+`Re` | 9 Got it、10导航、11 Back | 结果已确认，中间提示关闭/抽屉状态未单独取帧 |
| R02-T08 | 10主页→11字号基线 | 12导航、13齿轮、14 Appearance、15 Font size | 到达已见，中间页面未独立截图 |
| R02-T09 | 11 Follow System开→12关且最右预览 | 16开关、17滑杆 | preview与Save语义变化已见，未保存 |
| R02-T10 | 12未保存预览→13 Appearance | 18系统Back | 摘要仍Follow System |
| R02-T11 | 13 Appearance→14字号重入 | 19 Font size | 预览修改放弃，开关/滑杆/Save恢复基线 |
| R02-T12 | 14基线→15 Save后主页 | 20开关、21滑杆、22 Save | 直接回主页、字变大，Re/K3保留；内部机制未知 |
| R02-T13 | 15主页→16 Appearance摘要 | 23导航、24齿轮、25 Appearance | Extra Large摘要已确认，未在这里证明重启持久化 |

## 本轮覆盖边界

本轮新增已验证范围是模型菜单的可见结构、选K3后的UI反馈、实际软键盘与非空发送入口、两字符草稿在键盘隐藏和导航/字号保存序列后的保留、credit提示的可见与后续消失、字号本地预览/Back放弃/Save回主页及摘要Extra Large。尚无发送/生成/停止/错误重试、完整长文本输入、附件、语音、登录、跨进程草稿或偏好保存结论。没有测字体family/sp/字重、动效参数或全局颜色；蓝环继续按头像/动效采样处理，不称confirmed loading或网络失败。此文不是完整组件/产品行为验收。
