# Run03 续接上下文

更新：2026-10-16T13:20:50.884Z。当前清单和验收依据为 [remaining-validation.json](remaining-validation.json)，最新结果 [run03-results.md](run03-results.md)，画面 [run03.html](../run03.html)。旧交接保存在 [run02 快照](session-handoff-run02.md)。

37 项中 verified 20；needs_user 15；needs_authorization 1；partial 1；unverified 0。 F01、F06、F07、F08、F10、F17、F21、F22、F23、F24、F25、F27、F28、F29、F30、F33、R04 仍未销项。用户已要求把设备与账号受限项保留未完成，这不是缩减研究范围。

## 已做与不得重做

本轮 run03-20261016 已实际执行原版与独立原生参考工作。原 APK 为 Kimi 3.1.3/376，41,038,830 bytes，SHA-256 9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332。不要重复全量 APK/JADX/资产解析，也不要向首轮、run02 或冻结 run03 追加动作或覆盖原图。下一次实际研究使用全新 KIMI_RUN_ID。tools/kimi-run03.ps1 和当前 native-reference-session.mjs 固定本轮，不能直接用于新轮追加；原版操作从设置新环境变量后直接调用通用 android-session.mjs 开始，D与汇总工具若需新轮应先增加隔离入口。

先读 [行为规则](../spec/run03-behavior-and-transfer.md)、[逐帧审核](run03-evidence-audit.json)、[来源对照](../comparison/native-run03.md)、[Fieldnotes](../transfer-example/run03-acceptance.md) 与 [D 验收](../reference/run03-acceptance.json)。R01=verified、R02=verified、R03=verified；D 只证明独立实现，不替 F 项验证。

## 用户答复与授权

- 用户暂不能提供语音、真实拍摄和第二设备，要求保留未完成；不要重复索要同一资源，也不能转成 not_applicable。未来语音短句可用“Run03，今天晴天，测试结束”。
- 用户暂不能恢复当前账号可用性。K3/K2.8/Slides/Build容量限制、Swarm订阅门槛、后续Instant容量错误均已有实测；不要无限重发。实际技能、引用、队列暂停/恢复与晚到分支仍未完。
- 唯一一次对自建 RUN03-INSTANT-OK 的 Like 和可用撤销已执行，授权已用完；其他评价需具体授权。
- Claw 需要已配置的可丢弃隔离实例及具体任务授权，先前异步询问未有肯定答复。不得创建付费云资源、公开发布、邀请、外发或连接真实工作电脑。
- 普通可逆研究与本轮自建可丢弃对象可自主执行；既有私人会话、文件不得操作。

## 保留内容与删除

Research Run03 Life 已重新核对正文及精确 Rename 标题后删除；Delete 先取消再确认，结果切到空主页。274 提交标题查询后只见另一自建聊天；不是 No results，也不证明精确搜索算法。未见 Undo 只限该采集窗口。随后执行了搜索词清理与退出动作，未留后置状态帧，不将其回执作为清空结果证明。

保留 Research Run03 Lab 项目、文件 run03-project-data.txt（LAB-7832）、规则尾标 PROJECT-RUN03-5928，以及另一自建 Run03图像形状聊天，供未来同步样本使用。两个自建定时任务均已停用/删除，171 为最终空列表。勿按列表位置定位任何对象。

## 专用设备与恢复

固定 AVD rea_kimi_ui_20261015，serial emulator-5596，端口5596/5597；另有5580，绝对不操作。单一设备同时只允许一位控制者。恢复登录保存在本机 AVD；不要清数据、退出账号或重装原版。工具链在用户 .rea/toolchains/android。启动用 tools/start-emulator.ps1，核对 AVD/boot/package/版本后才动作。默认无窗口无音频，不能把模拟素材当真实设备验证。

恢复与退出的逐字段结果见本轮 restore、final-device-state 和 shutdown-verification。是否全部一致以记录和 require-restored 检查为准。

恢复文件：[restore](../evidence/runtime/run03-20261016/restore.json)、[final-device-state](../evidence/runtime/run03-20261016/final-device-state.json)、[shutdown-verification](../evidence/runtime/run03-20261016/shutdown-verification-retry2.json)、[最终 APK 身份](../evidence/runtime/run03-20261016/artifact-identity-final.json)。初始 system_locales 为 null，后续曾规范化成 en-US；以最终逐字段差异/恢复记录为准，不能仅按可见英语假称字段完全一致。设备时区 GMT，主机 Asia/Shanghai。

## 已知失败与安全门控

截图与 UIA 非原子，菜单挂载延迟、null-root、键盘教育层与坐标连续点击已造成失败。208 并非拼音 composing；221/225 不证1.5x，234/235才证切换与恢复。193只开停止确认，195两回答已完成；237–239被系统教育遮挡，248–250才是窄窗应用。Arabic-first去运输前缀失败，不能沿用为已验证输入工具。

发生过一次误入既有私人会话，未输入/提交，已退出并留 incidents 记录。不得声称从未进入私人内容。身份、私人历史和导出身份/QR整套只保留本机，不复制内容到报告或 ZIP。18 个源场景被明确排除，253属缺UIA的不完整证据；safe名单默认拒绝新/未审场景。

## 继续顺序

只推进依赖已获得的未完成ID。未来恢复服务后用最小自造请求补成功产物/引用/队列停止与晚到；获得设备后补真实音频/相机/双端/远程/触觉。Claw只在已授权隔离实例上开展。独立部分可同时做离线复核。每项更新验收、原图、返回/异常、恢复与状态，不能将受限或准备好当完成。

变更后重新生成报告和安全预览，更新索引，再运行 local/distributed/projection 与实际ZIP字节检查，交付新包。不要运行首轮 finalize-library.mjs 或 summarize-run02.mjs 覆盖最新索引。
