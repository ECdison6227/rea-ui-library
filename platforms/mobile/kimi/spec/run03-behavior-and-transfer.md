# Run03 行为规则与迁移边界

来源固定为 Kimi Android 3.1.3（376），专用 API35 AVD，原应用与真实后端。完整状态在 [37 项清单](../docs/remaining-validation.json)，逐帧像素与后采 UIA 在 [审核记录](../docs/run03-evidence-audit.json)。本文只归纳已观察路径。A 为原版运行，B 为包内声明，C 为有边界的推断，D 为独立实现选择。

## 输入、返回和状态归属

| 已观察状态与操作 | 实际结果（A） | 实现接入与未知 |
| --- | --- | --- |
| 自建聊天内 230 字符草稿；抽屉、设置、后台、force-stop 后重入 | 同一草稿精确保留；新聊天为空，返回原聊天恢复 | 按聊天分配草稿所有者。参考用 SharedPreferences 属于 D；不推断原版存储结构。F04，13/27/28/32/36/37 |
| 45 字草稿，切到标为 New chat 的 K3，再返回原聊天 | 原草稿仍在 | 模型菜单可能同时切换上下文，不能把所有切模型都实现成就地清空。F03 |
| 实际生成中打开模型菜单并选 K3 New chat；切回 Instant 后重入原聊天 | 原输出保留至 240 行 | 请求结果归属原聊天。不能由最终结果推断后台取消或路由。186/187 |
| Keyboard Send 开启，点击实际 Gboard Send | 多行请求提交并得到自造标记 | 软件开关与 IME 动作一起验证；不能用 adb Enter 代替。F05 |
| Keyboard Send 关闭，点击实际 return 键 | 只插入一个 LF，未创建消息 | 回车与提交分开处理。200/201 |
| 临时简体拼音输入 ceshi，选中文候选 | 210 有下划线 ce shi 和候选，211 提交为“测试” | 未读取 composing span，不推广其他输入法；未发送，临时布局已移除。208 是失败尝试 |
| 历史搜索后进入聊天，再点 Navigation | 返回搜索上下文 | 不按图标名字假定永远打开抽屉；返回栈由入口决定。F16 |

这些路径的精确输入、哈希与动作在 [原版 session](../evidence/runtime/run03-20261016/session.json)。截图和 UIA 非原子，状态转移 JSON 只做有边界的采集区间关联，不把中间动作回执当结果。

## 异步、失败与恢复

| 前置与动作 | 实际结果（A） | 迁移契约 |
| --- | --- | --- |
| 自造 PNG，Photos 取消/重入/选择/移除/重选并发送 | 正确读出 7429、蓝方形、红圆形 | 附件列表与草稿分别持有状态；单附件移除不应顺带清空其他内容。49–58 |
| 同时选择自造 PDF、DOCX，移除 PDF 后重选并发送 | 返回两份文件各自的正确代码 | 以附件身份更新单项；当前结果不证明任意 Office/PDF 格式。59–65 |
| 2,115,058 字节 TXT；第二份不同哈希文件在有效断网后选择 | Upload failed，原卡片可重试；联网后进入 Uploading 73%，关闭移除 | 上传重试保留对象；UI 移除不等于后端已取消。首份实际内容读取成功。66–80 |
| 网络搜索 Auto/Off 选择及断网重试 | 开关摘要保留；联网重试得到 WEB-RECOVER-OK | 网络恢复与“检索引用正确”分别验收。搜索工具过程、裸 URL、占位文案不能充当引用结果。F25 未完成 |
| 生成时继续输入第二消息 | 192 明确 Queue / 1 message queued | 区分输入草稿、已入队和正在输出。193 只打开 Stop confirmation；195 两个回答均已完成，不能称暂停成功。F29 未完成 |
| 之后 Instant、K3/K2.8 最小请求遇容量限制 | 没有可用成功生成路径补停止/晚到分支 | 保留原文、失败原因与重试入口；参考本地 epoch 拒绝晚到属于 D，不能写成已验证原版后端机制 |

原版历史停止成功路径仍见 [run02](run02-authenticated-behavior.md)。这一历史 A 不能替代本轮带队列停止与晚到分支。

## 保存、即时选择与风险动作

| 路径 | 结果（A） | 适用边界 |
| --- | --- | --- |
| 字号预览→Back；字号预览→Save | Back 放弃；Save 提交并直接回聊天，重入显示应用值 | 不把字体页做成即时提交；见 run02 字号证据及 D 对照 |
| 音色 Tintin→Luna→返回摘要→恢复 Tintin | 摘要真实改变并恢复；Auto-play OFF | 只证软件选择。234 明确切 1.5x、235 恢复 1.0x；221/225 不证 1.5x，真实听感缺设备 |
| 自建会话 Pin/Unpin，Rename/空值/长值/Cancel | 菜单状态切换；提交名称保留，空值停留，长样本只接受 20 字符，Cancel 放弃 | 此字段样本界限不推广成全 App 限制。F13/F14 |
| 精确重绑本轮 Life 标题，Delete→Cancel→重开→Delete | 返回新空主页；查询提交后可见另一自建聊天，无 Life | 只删除这个可丢弃对象。采集区间未见 Undo；不声称永久不可恢复。269–274 |
| 对自建 RUN03-INSTANT-OK 提交获准的一次 Like，点可用撤销 | Liked answer→Rate answer | 单次授权已消耗；UI 状态恢复不证明服务器删除历史评价。F18 |
| Share→Word / Generate image→Save to album | 系统分享面板可取消；本地图片确实保存 | 未向外发送。导出图片包含账号身份/QR，整份仅本机留存。F19 |

## 项目、任务与上下文引导

新建 `Research Run03 Lab`，Instructions 保存精确 70 字符；编辑取消后恢复原值。Library 的自造 82 字节 TXT 提供从未在会话中使用的 `LAB-7832`，实际回答同时给出 `PROJECT_DATA_CODE=LAB-7832` 与规则尾标 `PROJECT-RUN03-5928`。这证明本轮最小项目的文件/规则路径，未验证任意项目协作或跨设备同步。项目保留供未来同步样本使用（F20）。

Once 定时任务实际经历保存、Run now、设备 GMT 时区 09:35 自动触发，两个独立结果均为 `TIMER-RUN03-6418`。第二个自建活动任务修改标题并保存，再停用和删除。171 最终空列表，两个任务无遗留；不扩展到所有周期、通知渠道（F26）。

Slides、Build 自动切 K3；最小请求均返回容量/订阅优先提示。Swarm 明示 Moderato/Plus 门槛。theme-kit 有详情、取消与选用入口，未取得实际执行结果；Claw 只到部署页，未创建云资源、连接真实电脑、邀请或付款。这些入口说明与失败态是已观察 A，整体功能仍未完成。

## 适配、语言和反馈

F31 已实测 800×1800 / fontScale 1.3 以及 2400×1080 横屏。清除 Android 教育遮挡后，横屏为兼容窄窗和黑边留白（248–250），未全宽重排。237–239 的遮挡帧不能证明应用布局。恢复竖屏时出现 Kimi ANR，Wait 无恢复，停止/重启应用后恢复；原因未知，不归因给某一个配置项。此处 verified 代表研究路径已完成，不表示原版没有适配缺陷。

F32 的 Deutsch / Arabic 经应用菜单实际生效。Arabic 字形连写，但整体导航骨架未镜像，部分 Chat settings 仍英文。261/263 是带 Latin 运输前缀的混合文本，Arabic-first 去前缀尝试失败。已清理未发草稿并恢复 English、主题/字号 Follow System 与显示基线。迁移到 RTL 应定义自己的镜像策略并重新验收，不能照搬为完整 RTL 支持。

F33 录屏保留媒体 PTS 和逐帧 SHA。只能分辨一个菜单开闭区间和生成中编辑；80ms 目标动作实际派发间隔 1182.9249ms，不能证明快速中断。duration / easing / spring 保持未知。系统三动画尺度设 0 时字体路由与 Back 可运行，已恢复；触觉与声音仍需真实设备。见 [motion 研究](../docs/run03-motion-research.md)。

## 从规则到 D 实现

[组件契约](../reference/component-contracts.json) 明确 draft、status、applied/pending preference、request epoch 和事件顺序；[来源映射](../reference/source-mapping.json) 连接 A/B 与 D。Java Android View 的颜色、间距、字号、Canvas 图标、SharedPreferences、6 秒本地完成与 epoch 取消均为 D。系统字体与原版字形链没有等价声明。

[原版配对](../comparison/native-run03.md) 分别报告 capacity、Appearance 等条件和差异；[Fieldnotes 迁移](../transfer-example/run03-acceptance.md) 验证不同业务的空/正常/错误/取消/保留。二者各自验收，参考自测不替原版通过。资产原件和变体继续从 [manifest](../assets/manifest.json)、[字体](../assets/derived/fonts/metadata.json)、[文案](../assets/derived/copy/index.json) 定位；中性业务替换另存，不覆盖研究原件。
