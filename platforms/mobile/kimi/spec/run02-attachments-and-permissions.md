# Run02：附件、麦克风权限与离线恢复的实际样本

本页独立复核安全场景 27–40 的原始截图、UIA 和 `capture.json`，以及限定的安全 action 记录、自造 fixture 与保留的推送/网络资料。**A** 表示实际画面/层级和限定的前后状态；**B** 表示包内 CVR 原文；**C** 表示解释或未确认的归因；迁移时另作的实现决定应标 **D**。没有查看 18/19 身份页，没有驱动设备、读取私人文件或更改运行 session。

记录入口：[`attachment-permission-review.json`](../evidence/runtime/run02-20261016/attachment-permission-review.json)，辅助编号 `LOCAL-RUN02-ATTACHMENT-PERMISSION-001`，不是 REA Evidence ID。14 张原图均已通过 `view_image` 直接看过，截图与 UIA SHA-256 均与各自采集记录一致；10 项原 UI 文字对照到 14 个 CVR 候选键（含同文异键）。JSON 新增 35–40 的 6 条 A 事实、12 条前后转移及精确 action 引用。工具 [`review-run02-attachments.mjs`](../tools/review-run02-attachments.mjs) 可只读复现来源检查及 JSON 输出。

## 条件与非原子采集

目标为包 `com.moonshot.kimichat`，APK SHA-256 `9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332`，AVD `rea_kimi_ui_20261015` / serial `emulator-5596`；本段为用户辅助登录后的账户模式。画布 1080×2400 物理像素、density 420、fontScale 1.0、浅色；`buildLocale=en-US`，应用 locale 为 `[en-US]`，系统 locale 字段仍为字符串 `null`。

全部 14 次记录都是 `atomicCapture=false`：先截图，后抓 UIA 和窗口信息。不能把后一时刻的文字/包名强行套到前一张图片，不能用两次采集间隔估计解析或动画时长。

| 场景 / 原图 | 截图所见（A） | 后续 UIA / 窗口（A） | 采用边界 |
| --- | --- | --- | --- |
| [27 附件层](../evidence/runtime/run02-20261016/scenes/r02-27-auth-attachments/screen.png) | 既有聊天变暗，底部白色大圆角面板；Camera、Photos、Local file 图标块与部分 WeChat 块；下方 Web search / Auto / 右箭头 | Kimi 包；水平滚动容器暴露上述来源标签及 WeChat files | 可采面板组合；没有点开相机、照片、微信或搜索设置，不能补出其行为 |
| [28 文件选择入口](../evidence/runtime/run02-20261016/scenes/r02-28-file-picker/screen.png) | 仍是 Kimi 聊天和输入框，附件面板已收起 | 已转为 `com.google.android.documentsui` 的 PickActivity；Recent、Recent files、No items | 系统选择器交接由较晚 UIA 证明；这张图不能作为系统选择器截图 |
| [29 Downloads](../evidence/runtime/run02-20261016/scenes/r02-29-downloads-picker/screen.png) | Android Downloads、文件类型 chips，文件区在早帧尚无可见条目 | 系统包已暴露 `kimi-ui-research-upload.txt`、`408 B` 和预览语义 | 保留“早图无条目/后层级有条目”差异；不判为最终空态或发现失败 |
| [30 选中文件](../evidence/runtime/run02-20261016/scenes/r02-30-attachment-selected/screen.png) | Kimi 输入区出现文件卡、`Parsing`、卡片 × 和黑色发送箭头 | 同一文件已显示 `TXT, 408.0 B`，并有 `Send message` 语义 | 解析中只由图证明，元数据态来自后层级；不合成同一时刻，不证明上传完成 |
| [31 文件卡稳态](../evidence/runtime/run02-20261016/scenes/r02-31-attachment-ready/screen.png) | 文件卡为 `TXT, 408.0 B`；×、发送箭头与输入占位仍可见 | 同步到相同元数据态；UIA 字节哈希与 30 的较晚 UIA 一样 | 证实可见元数据卡；不证明服务端已提取正文或已发送消息 |
| [32 移除卡后](../evidence/runtime/run02-20261016/scenes/r02-32-attachment-removed/screen.png) | 文件卡和发送箭头消失，原会话仍在，输入区回到空占位及语音入口 | 未出现文件标题、TXT 元数据、`Send message` 节点；原测试对话标记仍在 | 只覆盖待发卡移除与聊天保留；不证明设备文件删除或远端文件清理 |
| [33 麦克风请求](../evidence/runtime/run02-20261016/scenes/r02-33-microphone-gate/screen.png) | 同一张原图中，上方黑色 Kimi 用途说明与前景 Android 录音权限框同时存在；底层 Hold to talk 可见 | 顶层仅为 `com.google.android.permissioncontroller`，含三个授权选择 | 两层共存有同帧像素证据；UIA 不含上方产品说明，不能由此否定其存在或推定先后时序 |
| [34 拒绝后](../evidence/runtime/run02-20261016/scenes/r02-34-microphone-denied/screen.png) | 两个权限层消失，返回既有会话；保持 `Hold to talk` 与键盘图标 | Kimi 包，`Hold to talk`、`Switch to text input`，原测试对话标记仍在 | 覆盖一次拒绝后的状态；未验证授权、真正录音、转写或永久拒绝恢复 |
| [35 重新选取](../evidence/runtime/run02-20261016/scenes/r02-35-attachment-reselected/screen.png) | 同一自造 TXT 再次出现在待发卡，类型/大小仍是 `TXT, 408.0 B`；输入文字为空 | 文件标题、元数据及发送语义对应 | 只证明重选结果，不推测复用缓存或重新上传 |
| [36 输入提问](../evidence/runtime/run02-20261016/scenes/r02-36-attachment-prompt/screen.png) | 文件卡与实际输入 `Reply with the Fixture ID from te attached file.` 共存 | UIA 为同一文本，确实是 `te` | 不润色成 `the`；不推成产品文本长度限制或某种输入法缺陷 |
| [37 提交结果](../evidence/runtime/run02-20261016/scenes/r02-37-attachment-submitted/screen.png) | 文件卡和实际提问进入聊天；原截图已经显示 `Thinking complete` 与 `KIMI-UI-20261016`；输入框回到占位 | 后 UIA 也有同一回答 | 不是只靠后 UIA 宣称图中完成；不测响应/流式时长 |
| [38 稳态文件回答](../evidence/runtime/run02-20261016/scenes/r02-38-file-id-complete/screen.png) | 再次显示同一标识符回答及原文件/提问 | UIA 精确等于本地自造 fixture 的 Fixture ID | 确认一个自造 TXT 标识符读取链路；不推广为所有文档的解析正确性 |
| [39 离线发送](../evidence/runtime/run02-20261016/scenes/r02-39-offline-submit/screen.png) | 自造用户消息 `Reply only offline-test-OK` 保留，左侧红色循环重试图标，输入区显示 `Network is unavailable…` | UIA 保留消息和错误文本；没有为该红标独立暴露 Retry 标签 | 证明本次失败态及消息保留，不证明服务端收到/取消、队列持久化或输入框草稿保留 |
| [40 恢复并重试](../evidence/runtime/run02-20261016/scenes/r02-40-retry-after-network/screen.png) | 原消息后出现 `Thinking complete` 和 `offline-test-OK`；红失败标不再可见，普通占位恢复 | UIA 有精确回复与原上下文 | 证明恢复开关并点红标后的成功回答，不证明自动重试策略、请求去重或后端取消 |

30、31、35、36 左侧还出现一条竖向悬浮工具条。Root 已通过另一份像素复核确认它是外部系统键盘 / Gboard 层；本分工没有访问该私有支持图。其颜色、图标、位置与行为均不纳入 Kimi 组件。

## 附件的输入、状态与动作契约

本次使用的是本库明确选定的自造 fixture [`research-upload.txt`](../assets/derived/test-fixtures/research-upload.txt)，属于 D 测试数据，408 字节，SHA-256 `80457db5c02ca9764712d9ba87bcb7989aa3a2499961bd3595d8eda2f019463f`。第 2 行为 `Fixture ID: KIMI-UI-20261016`，后文另有同一 Expected identifier；不含账号、私人消息或密钥。此扩展复核只读取该文件，未查看任何私人文件。

保留的 REA 推送记录 [`run02-push-fixture.json`](../evidence/rea/run02-push-fixture.json) 实际 Evidence ID 为 `ev_bf69dc0bfce8ac8a334b466d6f6548be395a323b032083f00cadef39e9ab267b`，操作 `push_adb_file`，目标 `/sdcard/Download/kimi-ui-research-upload.txt`。其 local/device SHA-256 均与上述原件一致，bytes=408，overwrite=false。此证据只证明文件推送身份；它没有目标 APK 绑定，不能单独证明应用读到文件。

27–34 单独仍只证明选择/待发卡/移除；35–38 新增实际发送及回答一致性。系统显示 `408 B`，Kimi 卡显示无扩展名标题 `kimi-ui-research-upload` 和 `TXT, 408.0 B`；在实际提问中没有直接提供预期 ID，37、38 的原图和 UIA 均返回 `KIMI-UI-20261016`，与所推送自造文本一致。这使本次自造 TXT 的“选择→提问→发送→标识符回答”成为 A 级流程样本；仍不揭示后端抽取、存储或网络内部实现，也不保证其他文件的正确性。

| 当前状态 → 触发 → 结果 | 已有依据 | 仍未知 |
| --- | --- | --- |
| Kimi 附件面板 → Local file → Android 系统选择器 | 操作者动作说明、27 画面/层级与 28 较晚 UIA/窗口 | 启动 intent、MIME filter、文件权限策略、取消选择器后的返回状态 |
| Downloads 中选择自造文本 → Kimi 输入区待发文件卡 | 操作者说明、29 较晚文件项、30 Parsing 原图 | 文件数/尺寸上限、其他类型、上传失败/重试、取消正在解析的副作用 |
| Parsing → 显示 TXT/大小 | 30 早图和较晚 UIA、31 稳态图；两次 actionsBeforeCapture 均为 65 | 精确切换时间、耗时、解析在客户端还是服务端、真正可发送条件 |
| 稳态文件卡 → × → 无待发卡 | 操作者说明，31→32、action count 65→66；旧对话仍在，发送箭头/语义消失 | 是否清理已上传文件、undo、瞬间提示、具有非空文字草稿时如何处理 |

卡片是输入区的待发送对象，× 与发送箭头在同一输入组合中出现；本样本的用户输入文字为空，因此只能确认文件卡移除后空输入状态的变化，不能推广为“移除附件保留所有文字草稿”已验证。

31 的 UIA `Send message` 子语义节点为 `enabled=true`、`clickable=false`。同图中发送箭头可见，不能由子节点的 clickable 标记宣布按钮不可用，也不能由 enabled 标记宣布后端发送必定成功；点击可能归属父容器，当前没有跟踪事件实现。后续 action 75 的实际点击及 37/38 的回答另行证明这一具体输入组合成功发送并取得预期回答。

## 安全动作的精确索引与离线恢复

以下索引均为 [`session.json`](../evidence/runtime/run02-20261016/session.json) 中 `actions` 数组的 **0 起算索引**；例如 action 75 是第 76 条记录。`actionsBeforeCapture=76` 是总数，不是 action index。本复核只抽取安全区段 `actions[60..84]`，JSON 保存这些记录、各自 canonical JSON SHA-256 及 `/actions/N` 指针，没有复制早期身份相关动作或修改 session。

| 对应场景 | actions 索引 | 实际操作记录 |
| --- | --- | --- |
| 27 | 60 | More input options |
| 28 | 61 | Local file |
| 29 | 62、63 | Show roots，Downloads |
| 30 | 64 | 仅选择自造文件 |
| 31 | 无新增 | 等待后再次采样；前后 action count 同为 65 |
| 32 | 65 | 点击待发卡 ×，UIA 名称为 Off |
| 33 | 66、67、68 | Back 退出空输入焦点，Switch to voice input，同点 900ms swipe 形式的按住输入 |
| 34 | 69 | 系统 Don’t allow |
| 35 | 70–73 | Switch to text input、附件入口、Local file、重新选择自造文件 |
| 36 | 74 | 输入标识符提问；命令目标文字有 `the`，真实画面/UIA 却是 `te`，不解释缺字原因 |
| 37 | 75 | Send message |
| 38 | 无新增 | 与 37 均在 action 75 后，稳定复核回答 |
| 39 | 76–81 | 聚焦、输入 `Reply only offline-test-OK`、隐藏 IME、只关闭专用 AVD Wi-Fi/data（79/80）、发送（81） |
| 40 | 82、83、84 | 恢复 Wi-Fi/data，再点击观察到的红重试标记（84） |

网络基线 [`network-baseline.json`](../evidence/runtime/run02-20261016/network-baseline.json) 记录 Wi-Fi/data 均为 `1`；[`network-restored.json`](../evidence/runtime/run02-20261016/network-restored.json) 在本次重试前记录两项恢复为 `1`，与基线匹配。Root 另报告关闭后看到 `Active default network: none`；这两个 JSON 本身只有开关状态，本复核未独立取得该 connectivity dump，来源在复核 JSON 中单独标为操作者观察转述。

39 证明离线提交之后消息以用户气泡保留，并出现红重试图标和 `Network is unavailable…`；原附件对话上下文仍在。该红图标在 UIA 中没有独立命名，不能把同页旧回答下的另一个 `Retry` 语义节点误认为失败消息重试入口。Action 84 按像素观察定位的红标坐标点击，40 取得精确回复 `offline-test-OK`，可记录这一次“恢复网络→点失败标→成功回答”。

恢复开关与点击之间没有单独采样，因此不能判断自动重试是否也曾发生，更不能推断后端网络取消、请求身份、去重、收费、服务端是否收到离线请求或队列跨重启持久化。这里保留的是**消息气泡**，不是未发送输入草稿。最终环境恢复核验由 root 另行完成；本页不把单步网络开关快照冒充完整恢复审计。

## 麦克风用途说明与 Android 系统请求

33 的上层产品说明标题为 `Kimi Requests Microphone Permission`，正文说明语音输入识别用途及不授权无法使用该功能。黑色说明卡属于当前 Kimi 语境的产品用途层；这里通过原图及包内精确文案对应确认可见样本，没有获得其独立 UIA 控件或代码绑定。

前景系统框写 `Allow Kimi to record audio?`，提供 `While using the app`、`Only this time`、`Don’t allow`。UIA 包为 `com.google.android.permissioncontroller`，资源 ID 分别指向 `permission_allow_foreground_only_button`、`permission_allow_one_time_button`、`permission_deny_button`。这是 Android 系统能力，图标、浅紫表面、圆角和按钮堆叠不能作为 Kimi 设计 token 导出。

安全 action 67/68 记录切入语音并按住触发请求，action 69 选择 `Don’t allow`。33 同帧证明产品说明和系统框共存；34 证明拒绝后两个层均不再可见，保留原聊天并仍处于 Hold to talk 输入模式。32→33 的 action count 为 66→69，存在多个中间动作；本页不补造语音切换的每一帧，不声称产品说明必定先显示若干毫秒。

拒绝一次后没有观察到文字转写或录音进度，不能称为语音能力通过验证。再次请求、永久拒绝、系统设置恢复、一次性授权、while-using 授权、录音取消和转写编辑都待补。产品说明也没有被 UIA 暴露，焦点/朗读顺序和模态语义仍然未知。

## 最多 10 项实际文案对应

以下对应的**实际显示或无障碍说明为 A**；CVR 候选原文为 B。A 级文本对应并不等于代码中确实读取该键；同文异键保留为多个候选，不用页面语义擅自消除。

原件为未限定目录 [`values/strings.commonMain.cvr`](../assets/original/assets/composeResources/kimi.shared.generated.resources/values/strings.commonMain.cvr)，SHA-256 `800e9077ace472c85af705291f1fde777b52e4e35f96ff50c909c7236d245805`；中文来自 [`values-zh/strings.commonMain.cvr`](../assets/original/assets/composeResources/kimi.shared.generated.resources/values-zh/strings.commonMain.cvr)，SHA-256 `3b164e0813649ed8550488e318ad4d7b7bc889a87ae8f8445eb4960d2eea9da4`。JSON 中保留每个候选原键、原文、源行、原行及全部哈希；所选 28 条中英文源记录经过原件哈希/行/Base64 往返核验。中文仅作为 B 对照，本次没有运行中文界面。

| A：所见英文 / 场景 | B：资源键及原 CVR 行 | B：中文对照 | A 对应方式与歧义 |
| --- | --- | --- | --- |
| Camera / 27 | `more_actions_camera` / 950 | 拍照 | 原图及 Kimi UIA |
| Photos / 27 | `album` / 153；`remote_file_upload_source_photo` / 1645 | 照片；相册 | 同文异键，保留两个 |
| Local file / 27 | `more_actions_local_files` / 951；`project_upload_type_file` / 1429 | 均为本地文件 | 同文异键，保留两个 |
| Web search / 27 | `more_actions_search` / 956；`okc_tool_name_web_search` / 1138 | 联网搜索；网页搜索 | 同文异键，保留两个 |
| Auto / 27 | `more_actions_search_auto` / 953 | 自动 | 原图及 Kimi UIA；未改变此值 |
| Parsing / 30 | `parsing` / 1251；`remote_file_upload_status_parsing` / 1648 | 均为解析中 | 只在早截图可见，后 UIA 已是元数据；保留两个键 |
| Kimi Requests Microphone Permission / 33 | `permission_popup_audio_title` / 1256 | Kimi 申请麦克风权限说明 | 原图产品说明；顶层 UIA 无此文本 |
| For voice input recognition. If you do not grant this permission, you will not be able to use this feature / 33 | `permission_popup_audio_content` / 1255 | 用于语音输入识别场景，若不授予该权限，你将无法使用该功能 | 原图多行人工读图与原文对应 |
| Hold to talk / 33、34 | `press_and_speak` / 1338 | 按住说话 | 两张原图及 34 Kimi UIA |
| Network is unavailable… / 39 | `network_unavailable` / 990 | 当前网络不可用… | 原图及 UIA；后者为 Unicode 省略号，与原文精确对应 |

系统 DocumentsUI 的 Downloads、Recent、No items、类型 chips 和系统权限框的三个选项没有加入 Kimi CVR 映射或迁移语言规则。WeChat files 虽在 27 的 UIA 完整暴露，原图只显示部分块，本页未扩展其资源或业务行为。

## 可采用的规则及边界

A 样本支持的组合是：输入区扩展面板分组展示附件来源与搜索能力；文件先作为可移除待发卡进入输入区，状态文字由 Parsing 变为类型/大小；一个自造 TXT 发送后取得内容中的预期标识符；离线失败保留用户消息并提供可重试标记；麦克风使用有产品用途说明和系统授权层，拒绝后恢复会话且保留语音输入模式。C 归纳为“输入区承载暂存内容和可恢复的请求状态”，但不能推广到所有文件、权限、网络异常或未登录流程。

D 迁移时应使用目标平台的文件选择器与权限 API，保留明确用途解释、待发卡状态及局部移除入口。系统界面的样式由平台负责；原生尺寸/字体、转场、异步取消、上传文件生命周期和焦点规则需要独立实现并注明选择。本页没有测量颜色、圆角、dp/sp、字体回退或动效时长，不填入精确值。
