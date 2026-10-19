# Run02 静态候选与待实测问题

本页供 `evidence/runtime/run02-20261016` 继续采样时定位文字，不记录任何新的运行结果。**27 项文案均为 B（包内原文），主题选择、语义解释和问题为 C（研究候选）**；每项出现条件、资源到控件的绑定和可达性均未知。由 root 唯一操控设备，本次分工只读取现有资源与静态 JSON，没有运行 GUI、设备命令、登录、录音或网络切换。

完整可查记录：[`run02-copy-candidates.json`](../evidence/static/run02-copy-candidates.json)，辅助编号 `LOCAL-RUN02-COPY-001`，不是 REA Evidence ID。其中每项含英文/中文原文、原键、占位符、原始行、行号、源文件 SHA-256、原行/解码原文 SHA-256及同文异键候选。复现命令为 `node tools/build-run02-copy-candidates.mjs`，工具只分析已有派生资料，不重复解析整包。

## 原文来源与准确位置

目标 APK SHA-256：`9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332`。

- 英文取自未限定目录 [`values/strings.commonMain.cvr`](../assets/original/assets/composeResources/kimi.shared.generated.resources/values/strings.commonMain.cvr)，SHA-256 `800e9077ace472c85af705291f1fde777b52e4e35f96ff50c909c7236d245805`；对应 [`default.json`](../assets/derived/copy/default.json)。未限定目录本身不证明运行语言回退顺序。
- 中文取自 [`values-zh/strings.commonMain.cvr`](../assets/original/assets/composeResources/kimi.shared.generated.resources/values-zh/strings.commonMain.cvr)，SHA-256 `3b164e0813649ed8550488e318ad4d7b7bc889a87ae8f8445eb4960d2eea9da4`；对应 [`zh.json`](../assets/derived/copy/zh.json)。本表所选键在两份原 CVR 的行号相同。
- 本轮重新核对所选 54 条资源的原文件哈希、原行哈希和 UTF-8→Base64 往返，一致。核对成功只证明派生记录对应本地原件，不把 B 提升为 A。

表中“EN / 中文”全部为原文，`\n` 表示真实换行；字号说明与权限说明较长，在表后展开，不省略 JSON 中的全文。

| 编号 | 主题 | 资源键 | 原行 | EN / 中文原文 |
| --- | --- | --- | ---: | --- |
| 01 | 模型 | `model_select_previous_model` | 943 | Previous models / 过往模型 |
| 02 | 模型 | `model_select_thinking_level` | 944 | Thinking effort / 思考等级 |
| 03 | 模型 | `model_select_new_chat` | 942 | New chat / 新会话 |
| 04 | 模型 | `queue_model_switch_unavailable` | 1497 | Model switching unavailable / 会话进行中不支持切换模型 |
| 05 | 草稿 | `chat_text_field_hint` | 292 | Ask anything… / 尽管问… |
| 06 | 草稿 | `create_new_chat` | 466 | New chat / 开启新会话 |
| 07 | 草稿 | `already_at_new_chat` | 158 | You are already in a new chat / 当前已是新会话 |
| 08 | 字号 | `font_setting` | 644 | Font size / 文字大小 |
| 09 | 字号 | `font_display_hint2` | 639 | 见下方完整说明 |
| 10 | 字号 | `font_follow_system_switch_hint` | 642 | When enabled, font size will follow the system settings / 开启后字体大小将跟随系统设置 |
| 11 | 字号 | `save` | 1823 | Save / 保存 |
| 12 | 字号 | `cancel` | 245 | Cancel / 取消 |
| 13 | 字号 | `setting_chat_toast_saved` | 1901 | Settings saved / 设定已保存 |
| 14 | 语音 | `description_input_mode_voice` | 519 | Switch to voice input / 切换至语音输入 |
| 15 | 语音 | `permission_popup_audio_title` | 1256 | Kimi Requests Microphone Permission / Kimi 申请麦克风权限说明 |
| 16 | 语音 | `permission_popup_audio_content` | 1255 | 见下方完整说明 |
| 17 | 语音 | `chat_voice_hint_release_to_send` | 299 | Release to send / 松开发送 |
| 18 | 语音 | `chat_voice_cancel_input` | 297 | Cancel / 取消输入 |
| 19 | 语音 | `chat_voice_hint_edit_text` | 298 | Edit text / 编辑文字 |
| 20 | 离线 | `chat_error_screen_title` | 262 | Network loading failed\nPlease check your network and try again / 网络加载失败\n请检查网络后再重试 |
| 21 | 离线 | `connection_error_and_retry` | 450 | Connection error. Tap to retry / 连接异常，请点击重试 |
| 22 | 离线 | `network_unavailable` | 990 | Network is unavailable… / 当前网络不可用… |
| 23 | 离线 | `network_restored_and_normal` | 989 | Network restored, connection normal / 网络已恢复，连接正常 |
| 24 | 登录 | `please_to_login_and_chat_with_kimi` | 1278 | Log in to chat with Kimi / 登录后与 Kimi 畅聊 |
| 25 | 登录 | `login_btn` | 777 | Log In / 登录 |
| 26 | 登录 | `auth_error_unlogin` | 186 | User not logged in / 用户未登录 |
| 27 | 登录 | `auth_error_user_cancelled_login` | 187 | User cancelled login / 用户取消登录 |

`font_display_hint2` 英文原文：`Drag the slider below, or enable "Follow System" to set the font size for Kimi chats. Tap Save in the top-right corner when done`；中文：`拖动下面的滑块，或者开启跟随系统设置，可设置 kimi 对话中的文字大小，选择后点击右上角保存。`。它支持“源文案要求显式保存”这一 B 结论，不能证明预览是否实时改变全局字号、取消是否回滚或保存是否跨重启持久化。

`permission_popup_audio_content` 英文原文：`For voice input recognition. If you do not grant this permission, you will not be able to use this feature`；中文：`用于语音输入识别场景，若不授予该权限，你将无法使用该功能`。它是产品用途/后果说明，不是 Android 系统权限弹窗的原文，也不证明一定先于登录或系统请求出现。

## 这轮应核对的行为

| 范围 | 具体对照 | 需要保存的 A 证据 | 静态资料不能回答的部分 |
| --- | --- | --- | --- |
| 模型选择 | 打开、重选、返回；如果存在忙碌态，观察能否切换；未登录与离线时入口是否可达 | 原始画面、层级、所选项、动作及后续场景；输入草稿前后值 | 默认模型、服务端列表、额度限制、切换是否创建会话及保存作用域 |
| 普通输入草稿 | 使用不含隐私的标记短文，打开/关闭抽屉、模型层、设置并返回；分别观察新会话与原会话 | 输入框内容、键盘状态、返回路径、新旧会话身份 | 本地保存与内存保存、跨会话归属、进程退出/重启持久化；本轮未测到就保留未知 |
| 字号保存/取消 | 记录初值；改动后直接导航返回，再次进入；与明确点 Save 分开比较；有真实取消入口才测试取消 | 选择值/跟随系统状态、预览、返回后值、保存反馈与重新进入 | 系统 fontScale 不变时画面相同不证明开关无效；不能由通用 cancel 键推造取消按钮 |
| 语音/麦克风 | 区分进入语音模式、产品说明、系统权限与登录层；先观察入口/拒绝/取消路径 | 逐个弹层的包名、文字、按钮、动作顺序、返回后草稿与输入模式 | 权限说明不证明已申请或录音；松开发送文案不证明请求提交，转写未出现就不能声称可编辑已测 |
| 离线失败 | 记录哪个请求/页面触发错误；重试、重复点击、恢复网络后观察自动恢复与手动刷新 | 失败前状态、错误文字/容器、按钮状态、联网恢复后场景与输入保留 | 网络不可用、远程设备离线与服务端失败是不同条件，不把通用网络键强绑所有错误 |
| 登录门槛 | 分开检查输入、打开模型、发送、语音等入口；关闭/返回登录层，观察原场景恢复 | 门槛触发动作、真实登录层、关闭路径、原草稿和已选模型 | 不从 `Log in to chat` 推成全局门槛；不把 provider/验证码提交和真实认证成功写成已验证 |

对“发送”和“登录”只记录本轮实际执行到的层级，不把键名或文字承诺当作副作用已完成。无需为了静态资料中的任意提示扩大到付款、发布、删除、真实账号认证或非本轮语音通话。

## 可复查的 DEX 字符串锚

以下均来自之前已保存的字符串候选记录，包含原 DEX string index 和 string_data_item offset；**B 是字符串存在，C 是它提示的调查方向**。没有反编译调用者或证明实际资源绑定。JSON 另保留候选文件哈希、JSON pointer、原 DEX SHA-256。`classes2.dex` SHA-256 为 `379811d9db48b68a739766c12c876f6962c0e84e6785026bb30b6e00ddc5c195`；`classes3.dex` 为 `6eea0169d02e3a8717f08326185ce414898734c5ceb5328b6eed779980343648`。

| 辅助锚 | 保存文件 / JSON文本行 | 原 DEX index / offset | 原字符串 | 仍未知 |
| --- | --- | --- | --- | --- |
| DEX-01 | `classes2-string-candidates.json` / 13279 | 44261 / 11897821 | `defaultChatModel` | 默认模型值、存储、控件关系 |
| DEX-02 | `classes3-string-candidates.json` / 5504 | 33735 / 6429875 | `model_select_new_chat` | 生成资源标识符与行为的引用关系 |
| DEX-03 | `classes3-string-candidates.json` / 7589 | 39939 / 6581461 | `string:model_select_new_chat` | 同上；不是事件记录 |
| DEX-04 | `classes2-string-candidates.json` / 2344 | 6518 / 11164918 | `ChatModel-draft` | 是否为普通输入草稿、是否持久化与恢复 |
| DEX-05 | `classes3-string-candidates.json` / 2479 | 26258 / 6241685 | `composeResources/kimi.shared.generated.resources/drawable/followsystem.xml` | 资源路径存在不等于字号开关行为 |
| DEX-06 | `classes2-string-candidates.json` / 6319 | 37869 / 11700594 | `VoiceBottomBar` | 组件状态、手势与权限时序 |
| DEX-07 | `classes3-string-candidates.json` / 434 | 22337 / 6138960 | `VoiceBottomBar onDispose: voiceRecording=` | 日志式文本不证明离开界面停止录音 |
| DEX-08 | `classes2-string-candidates.json` / 34 | 401 / 10999885 | ` isLogin=` | 状态字符串不证明门槛覆盖范围 |

保存文件入口：[`classes2-string-candidates.json`](../evidence/source/classes2-string-candidates.json)、[`classes3-string-candidates.json`](../evidence/source/classes3-string-candidates.json)。行号以当前保存文件为准，并由完整文件哈希约束；DEX offset 是原字符串位置，不是调用者位置或代码地址。

## 有界的负面结果与归因限制

对 `default`、`zh` 两份 CVR 的键名运行 `/draft/i` 查询，零匹配。这只表示这些资源键没有这个命名，不证明没有草稿功能。`ChatModel-draft` 也只够指导观察，不够确认草稿保存策略。`queue_editing_description_unsaved` 等队列编辑文字没有混入普通输入草稿结论，发布草稿相关类名也不替代普通聊天状态证据。

读取已保存的三份类清单（classes/classes2/classes3 分别 10,298 / 14,540 / 5,663 类），查询 descriptor 同时含 `moonshot` 且匹配 `FontSize|FontSetting|ChatModel|ModelSelect|InputDraft|VoiceInput|VoiceBottom|LoginScreen|NetworkError`，零匹配。大量类名已混淆；这不是功能不存在的证据。清单的方法覆盖本来就仅限其 `ownPrefixes`，本轮没有把名字线索扩写成代码调用链。

同文异键必须保留：`New chat` 对应 `create_new_chat` 与 `model_select_new_chat`；`Thinking effort` 还对应远控思考等级标题；`Font size` 至少对应 `font_setting` 与 `setting_font_size_title`；`Save` 有 `save`、`publish_save`、`text_save`；`Cancel` 的其他同文键多达 27 个。即使后续截图出现这些文字，也只能增加 A 文本证据，不能自动唯一绑定本表所选键。

中英也有适用条件差异：`queue_model_switch_unavailable` 的中文明确“会话进行中”，英文只是“Model switching unavailable”。现场条件应由实际动作和状态确定，不按任一语言补出未观察的状态机。后续只给已观察项附 run02 原图、操作和状态转移链接；没有出现的候选继续标 pending，不作为失败或反例。
