# Kimi · 视觉、动画与交互规格

复用低噪声聊天布局、多行输入、圆角分组和显式用户状态：草稿、生成、停止、重试、预览、保存与取消。模型后端和订阅/账号逻辑保留目标自己的实现。

本规格面向“已有功能的软件改风格”。来源Android3.1.3(376) / Run03；原始素材、时间与证据保留。当前partial表示有已知还原差异，不代表不能用于明确范围的实施。

## 实施阅读顺序

1. [视觉角色](design/tokens.json)：先映射core角色，保留单位和作用域。
2. [组件](design/components.json)与[布局](design/layouts.json)：实现插槽、状态与内容自适应。
3. [交互](design/interactions.json)：返回、保存取消、草稿、忙碌与重试。
4. [动画](design/motion.json)：明确源声明/观察/目标选择，落实中断与减少动画。
5. [参考](reference/README.md)、[对照](comparison/native-run03.md)、[迁移指南](ADAPTATION_GUIDE.md)。

## 视觉规则

- 原生参考D浅色：page#F5F5F5、surface#FFFFFF、text#181818、accent#2381FF；group28dp/composer36dp/dialog30dp是独立选择。
- 原生UIA几何保留physical px、density及推导dp条件，不能当字形边界。
- widget CSS仅作用show_widget内联内容，不能套成整个Android主题。
- 系统默认sans与逐字fallback属于参考选择；长输入/键盘/焦点在目标中优先保真。

## 组件地图

| 组件 | 结构与关键状态 |
| --- | --- |
| SourceCapacityErrorFixture | 失败气泡、红重试、容量说明与下一步 |
| NativeComposer | 多行输入、草稿、附件/发送/停止，动作由非空/忙碌状态决定 |
| AnswerAndFeedback | 消息、生成、完成工具、失败与显式重试 |
| ModelOptionSheet | 当前模型/思考选项、选择与关闭，保留输入 |
| AppearanceRows | 分组设置、主题即时提交，字号预览后保存/取消 |
| DestructiveConfirmation | 遮罩、标题、确认/取消；确认前不删除 |
| DocumentLibraryAndEditor | 中性文档库→编辑→保存，取消保留草稿 |

每个组件的states、inputs、events、unknowns与目标决策在JSON中，不能将D接口当作原App API。长文/窄屏先重排，sp随字号，图标保持独立命中区。按钮/开关忙碌时保留视觉反馈，不因为防重而吞掉取消/重试。

## 动画与用户逻辑

来源精确duration/easing缺失时保留null；同时给出可执行D方案，而不是要求消费AI猜参数。D方案实现后须录制目标结果，对照原版代表帧/状态。事务状态与动画状态分开：取消动画不自动取消业务，旧请求结果不得覆盖新输入。

- **选项sheet进入/关闭**：180ms，cubic-bezier(0.2,0,0,1)；D target fallback. Preserve focus/draft, allow Back/outside dismissal, reverse from current state on interruption.
- **消息/错误反馈出现**：160ms，ease-out；Keep retained text stable during loading; status changes belong to state model, not timer-completed fake networking.
- **主题/选项选择反馈**：120ms，linear；Font preview stays uncommitted until Save; transition does not commit data.

## 保留边界

- 来源逐字字体、精确曲线/时长与参考视觉细节仍有差异；目标参数明确为D选择。
- 原参考部分重绘会影响焦点/选择；消费目标应保留组件状态并实测IME和长草稿。
- 语音、真实拍摄、系统权限等未完成部分按目标实际需要另验，不影响已明确UI样式/交互的使用。

原详细规格继续可查：[历史研究规格](spec/run03-behavior-and-transfer.md)。本次新增标准适配不改写原图或把旧采集标为新运行；源coverage与目标实现完成程度分别报告。
