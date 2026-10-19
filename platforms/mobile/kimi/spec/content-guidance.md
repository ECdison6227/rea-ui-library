# 文案与用户引导：包内声明基线

本页只从 APK 中的 Compose 文案资源归纳候选规则，证据等级为 **B（包内声明）**；对语气、组合方式和可迁移策略的判断标为 **C（推断）** 或 **D（独立实现建议）**。这些文字没有通过运行验证，不能据此断言某个引导必然出现、某个权限弹窗何时弹出、某项后端能力已开放。

## 来源与读取入口

- 辅助证据编号 `LOCAL-COPY-001`，不是 REA 返回的 Evidence ID。APK SHA-256：`9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332`。
- 原件：[`assets/composeResources/kimi.shared.generated.resources/values*/strings.commonMain.cvr`](../assets/original/assets/composeResources/kimi.shared.generated.resources/)。原件保持不变。
- 派生索引：[`copy/index.json`](../assets/derived/copy/index.json)。中文：[`zh.json`](../assets/derived/copy/zh.json)；未限定语言目录：[`default.json`](../assets/derived/copy/default.json)。下方英文例子取自这个未限定目录；“default”是本研究的输出名称，不代表实测语言回退策略。
- 精选的中英对应记录：[`examples.zh-en.json`](../assets/derived/copy/examples.zh-en.json)。保留原键、原文、数量变体、占位符、源行和哈希，便于逐条复核。
- 生成脚本：[`tools/derive-style-data.mjs`](../tools/derive-style-data.mjs)，从研究库根目录运行 `node tools/derive-style-data.mjs`，无需安装依赖。
- 核验记录：[`style-data-verification.json`](../evidence/static/style-data-verification.json)，命令为 `node tools/verify-style-data.mjs`。37,046 个条目及 1,315 个复数分支已逐一回编码，与原 Base64 和原文件/行哈希匹配；此核验不证明运行时行为。

解码格式为 `version:0`。普通条目为 `string|key|base64`；复数条目为 `plurals|key|ONE:base64,OTHER:base64` 等数量变体。Base64 严格校验并按 UTF-8 解码；每条记录包含 1 起算的 `sourceLine`、完整 `rawSourceLine`、原文件 SHA-256、原行不含换行符的 UTF-8 SHA-256、解码后原文 SHA-256，以及占位符候选位置。全文保留换行、富文本标签、标点和原有措辞，不对其润色。

## 已覆盖与未覆盖

共 17 个语言资源目录、37,046 个键记录。一个复数键计一个记录，其多个数量分支单独保留。

| 资源限定目录 / 输出 locale | 键数 | 其中复数键 | 复数数量分支数 |
| --- | ---: | ---: | ---: |
| `values` / `default` | 2,345 | 48 | 96 |
| `values-zh` / `zh` | 2,345 | 48 | 48 |
| `values-zh-rTW` / `zh-TW` | 2,160 | 41 | 41 |
| `values-ar` / `ar` | 2,157 | 41 | 230 |
| `values-de`, `-es`, `-it`, `-pt`, `-tr` | 各 2,157 | 各 41 | 各 82 |
| `values-ru` / `ru` | 2,157 | 41 | 164 |
| `values-id`, `-in`, `-ko`, `-th`, `-vi` | 各 2,157 | 各 41 | 各 41 |
| `values-fr` / `fr` | 2,156 | 40 | 80 |
| `values-ja` / `ja` | 2,156 | 41 | 41 |

`values-id` 和 `values-in` 的原文件 SHA-256 一致（`e18e8f92379e9bdbdaa761e928780fd70ab6fcb3e77976ac02e0b7ae8ee9da19`）；保留两个目录身份，不假定运行时只使用其中一个。各目录键数不同只能证明打包内容不同，不能据此判定运行时缺文案或确认具体回退顺序。这个提取不包含 Android `resources.arsc` 中的文案、依赖库提示或服务器下发文本。

`categoryCandidates` 仅按键名进行宽泛初筛，例如 `hint`、`empty`、`permission`、`retry`、`delete`，分类会重叠，也会有误命中和漏检。以下例子经过人工语义挑选；分类属于研究判断，原文内容属于 B 证据。**下表所有例子的实际出现条件、可达性、显示容器和触发顺序均未知**；“语义线索”不是运行观察。

## 例文与条件边界

表中行号同时适用于本次 `values-zh` 与 `values` 原始 CVR；详见各派生记录的 `sourcePath` 和 `sourceLine`。

| 类别 / 语义线索（C） | 资源键 / 源行 | 中文原文（B） | 未限定目录英文原文（B） |
| --- | --- | --- | --- |
| 欢迎与能力发现 | `onboarding_welcome_description` / 1232 | 欢迎来到 Kimi，看看我能做什么 | Welcome to Kimi. See what I can do. |
| 个性化问答；中英问题含义不同 | `onboarding_bn02_occupation_title` / 1195 | 你的职业是什么 | What would you like to use Kimi for? |
| 跳过入口文字 | `onboarding_bn04_skip` / 1212 | 跳过 | Skip |
| 保存后要求检查再发送 | `onboarding_bn05_saved_message` / 1213 | 设置已保存，请检查输入框中的内容并发送。 | Your preferences are saved. Review the message below and send it when you’re ready. |
| 预填后允许编辑 | `onboarding_bn07_prefilled_message` / 1218 | 已放入输入框，你可以修改后发送。 | Added to the message box. You can edit it before sending. |
| 空态鼓励继续使用 | `memory_space_empty` / 862 | 多聊聊，Kimi 正在努力记住你的世界 | Keep chatting. Kimi is getting to know your world. |
| 空态解释价值与下一步 | `project_empty_chat` / 1390 | 给项目加点背景信息\n添加指令或文件，每个对话都会参考 | Add some context to your project\nAdd instructions or files for Kimi to reference in every conversation. |
| 无设备状态标题 | `remote_device_add_empty_title` / 1595 | 暂未发现可配对的新设备 | No available devices found |
| 无设备状态的前置条件说明 | `remote_device_add_empty_description` / 1593 | 请确保已使用同一账号登录 Kimi Work，并在 Kimi Work 上开启了远程控制 | Make sure you are signed in to Kimi Work with the same account and have enabled remote control in Kimi Work |
| 上述语义附近的动作标签；是否同时显示未知 | `remote_device_add_empty_refresh` / 1594 | 刷新 | Refresh |
| 任务空态动作标签 | `task_center_empty_via_chat` / 2071 | 通过会话创建 | Create via chat |
| 权限用途和拒绝后果 | `permission_popup_audio_content` / 1255 | 用于语音输入识别场景，若不授予该权限，你将无法使用该功能 | For voice input recognition. If you do not grant this permission, you will not be able to use this feature |
| 权限名称说明 | `permission_popup_audio_title` / 1256 | Kimi 申请麦克风权限说明 | Kimi Requests Microphone Permission |
| 权限收益 | `open_notification_settings_description` / 1237 | 打开系统通知，任务完成后将第一时间通知你 | Turn on notifications to be alerted as soon as your task is done |
| 网络失败与可行动建议 | `chat_error_screen_title` / 262 | 网络加载失败\n请检查网络后再重试 | Network loading failed\nPlease check your network and try again |
| 连接失败和重试动作 | `connection_error_and_retry` / 450 | 连接异常，请点击重试 | Connection error. Tap to retry |
| 保存失败 | `onboarding_bn06_save_failed` / 1216 | 没能保存，请重试。 | Couldn’t save your preferences. Please try again. |
| 字段校验 | `profile_name_update_empty` / 1363 | 昵称不能为空 | Nickname cannot be empty |
| 验证校验；inline 仅是键名线索 | `account_delete_verify_error_inline` / 82 | 请输入正确的验证码 | Please enter the correct code |
| 清空数量与不可恢复后果 | `queue_management_description_clear_consequence` / 31 | OTHER：是否清空 %1$d 条排队消息？清空后不可恢复 | ONE：Clear 1 queued message? This can't be undone；OTHER：Clear %1$d queued messages? This can't be undone |
| 关闭造成的编辑损失 | `close_will_be_invalid` / 415 | 关闭后所有编辑内容将会失效 | All edits will be lost if you close |
| 账号注销的不可恢复警告与输入确认文字 | `account_delete_confirm_desc` / 61 | 注销账号后，全部信息将会被移除，且无法找回。是否确认要注销账号？若确认注销，请在下方输入框中输入“已知晓并确认注销”。 | All your data will be permanently deleted and cannot be recovered. Delete your account? Type "I understand and confirm deletion" below to proceed. |

表格中的 `\n` 表示原文换行，不是可随意删去的两个字符。账号注销例子只提取提示语，不执行相应动作，也不据此确认输入校验、删除时点或可撤销机制的实际实现。

## 可迁移候选规则

| 候选规则 | 依据和判断级别 | 适用条件与替换边界 | 必须补证的行为 |
| --- | --- | --- | --- |
| 引导以可立即尝试的动作推进，个性化内容让用户检查后发送 | `onboarding_*` 例文为 B；“动作推进”归纳为 C | D 建议：针对新业务用自己的例子与预填内容，保留可编辑、可检查的表达 | 首次触发、跳过保存、自动填入与实际发送是否分开、返回是否保留输入 |
| 空态提供补充内容的理由或恢复条件 | `project_empty_chat`、`remote_device_add_empty_*` 为 B；组合规律为 C | D 建议：数据为空时说明下一步价值；依赖外部条件时说明账号或配置要求 | 标题/说明/按钮是否同屏、刷新忙碌与失败、无权限与真空数据如何区分 |
| 权限请求说明具体用途，部分文案同时解释拒绝后果 | `permission_popup_*` 为 B | D 建议：按目标平台权限能力改写用途及后果，不照搬权限种类；保持事实准确 | 请求前后的顺序、拒绝一次/永久拒绝、二次入口、跳转系统设置及返回刷新 |
| 错误提示使用短状态加下一步动作 | `chat_error_screen_title`、`connection_error_and_retry` 为 B；通用模式为 C | D 建议：仅在操作可重试时提供重试，校验尽量指出可修改字段 | 输入是否保留、重入/禁用、晚到结果、键盘焦点、错误朗读 |
| 有损动作写清受影响对象、数量与后果 | 清空 plural、编辑损失和账号注销文案为 B；组合规律为 C | D 建议：用新业务对象代替“会话/账号”，仅在真实不可恢复时这样提示 | 确认/取消、返回/外点关闭、叠层、请求失败与撤销窗口 |

这些规则不足以还原弹窗状态机。实现时必须单独标明 D 策略；不能将文案中的“可以修改后发送”直接当作已验证的按钮事件顺序，也不能将带 `toast`、`inline`、`dialog` 的键名直接当成实际组件类型证据。

## 本地化与占位符处理

原资源使用 `%1$d`、`%1$s` 等有序占位符。数量键必须保留相应语言的分支，不把中文 `OTHER` 强行复制为所有语言，也不把英文 `ONE` 的固定数字当成占位符缺失。例如清空队列的英文 ONE 分支直接写 `1`，OTHER 分支才包含 `%1$d`。脚本把这些分支各自保留；占位符字段是词法候选，不是实际格式化器契约验证。

`account_delete_confirm_content`（源行 60）包含 `%1$s` 和 `<hl>...</hl>`；标签及插值全部保留。富文本标记的可点击区域、目标链接和无障碍标签未知。跨语言文案也不总是字面对应，例如职业问题的中文与英文问题不同。原始数据保持原样，迁移时独立审核语义。

另一个需要审查的例子是 `account_delete_pending_dialog_keep`（源行 67）：中文“保持注销”，未限定目录英文“Cancel”。这是 B 级文本差异；实际按钮上下文尚未观察，不能直接宣布为产品错误，也不能把其中任意一种翻译当作通用动作命名规范。

## 后续运行采样

先使用干净的测试状态观察真实首页/首次引导，再采集一个可恢复的空态和一次普通加载失败。对麦克风或相机入口记录说明层、Android 系统请求、拒绝及返回路径；仅申请测试所需权限。涉及账号注销、付款、发布或删除私人数据的流程，用现有静态证据保留缺口，不把实际提交当作无说明的研究步骤。

每个已观察场景需要补上：目标版本与哈希、模拟器/真机、系统语言、应用语言、主题、账号状态、入口、逐步动作、出现条件、文案键候选、原始截图或录屏、确认/取消/关闭/返回、焦点及状态保留。补证后才能把对应结论提升为 A；仅看本页或解码成功不改变其证据等级。
