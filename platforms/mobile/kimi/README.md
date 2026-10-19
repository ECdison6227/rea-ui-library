# Kimi Android 产品风格研究 · Run03

目标为 **Kimi 3.1.3（376）**，包名 com.moonshot.kimichat，原 APK SHA-256：

`9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332`

本轮在保留正常登录的专用 Android15/API35 模拟器中实际验证原版，使用自建可丢弃内容与真实后端；另有独立 Java Android View 原生参考。研究范围尚未全部完成。**37 项中 verified 20；needs_user 15；needs_authorization 1；partial 1；unverified 0。** F01、F06、F07、F08、F10、F17、F21、F22、F23、F24、F25、F27、F28、F29、F30、F33、R04 仍未销项。用户已要求把设备与账号受限项保留未完成，这不是缩减研究范围。

先看 [本轮结果](docs/run03-results.md)、[安全原版画面](run03.html)、[37 项覆盖与缺口](docs/run03-coverage.md)。本轮 275 张原版采集中 257 张通过逐帧安全审核；18 张整套不分发。数量用于索引，不代表验收程度。

| 要找的内容 | 入口 |
| --- | --- |
| 当前状态及验收条件 | [remaining-validation.json](docs/remaining-validation.json) |
| 可迁移行为、返回、异步、保存、引导与边界 | [Run03 行为规则](spec/run03-behavior-and-transfer.md)、[状态与动作区间](spec/run03-observed-transitions.json) |
| 原生参考工程、构建与启动 | [reference/README.md](reference/README.md)、[独立 APK](reference/build/native-reference.apk)、[组件契约](reference/component-contracts.json)、[来源映射](reference/source-mapping.json) |
| 原版↔参考的条件及具体差异 | [同条件配对报告](comparison/native-run03.md)，R02 当前 verified |
| 不同业务迁移 | [Fieldnotes 验收](transfer-example/run03-acceptance.md)，R03 当前 verified |
| 资源原件与派生预览 | [资产清单](assets/manifest.json)、[资产预览](index.html)、[字体元数据](assets/derived/fonts/metadata.json)、[文案索引](assets/derived/copy/index.json) |
| tokens 及单位/作用域 | [原生实例几何](tokens/native-observed.json)、[widget scoped tokens](tokens/widget-foundation.json) |
| 证据与审核 | [evidence/index.json](evidence/index.json)、[像素审核](docs/run03-evidence-audit.json)、[metadata corrections](docs/run03-metadata-corrections.json) |
| 动效观察与未知参数 | [motion 研究](docs/run03-motion-research.md) |
| 使用、分发、恢复与续接 | [迁移指南](docs/transfer-guide.md)、[分享范围](docs/sharing-scope.md)、[session 交接](docs/session-handoff.md)、[下一位 Agent](docs/next-agent-prompt.md) |

A 是原产品实际观察，B 是包内/反编译声明，C 是有边界推断，D 是独立实现或加工选择。截图先于 UIA，非原子；动作成功不证明结果。历史 run 的原图与采集时间保留，本轮重读不把它们变成新 A。

本轮补齐长草稿/IME、照片与多文件、上传断网恢复、自建会话管理、单次获准 Like 与撤销、项目文件/规则、真实定时触发、语言和横屏路径。K3/K2.8、Slides/Build/Swarm 受当前账号限制，后续 Instant 也出现容量错误；语音、真实拍摄、双端、远程设备、停止/晚到等仍留未完成。Arabic 骨架未完全镜像；横屏有兼容窄窗；发生过恢复时的 Kimi ANR，均如实记录。

原生参考 R01 当前 verified，仅运行独立本地 fixture，无账号/网络/模型服务；D 的六秒完成、存储、取消 epoch、颜色和字体映射不代表原版内部实现。来源对照与 Fieldnotes 分别验收，不给虚构还原分数。

保留首轮的 1430 份包内原件、390 个派生 SVG、17 个本地化目录及其原始 SHA/来源；不重做全包解析。widget CSS 仅作用于内联内容，不是 Android 全局主题。MiSans Latin 包内存在不证明中文逐字回退。

验证与打包命令从工作区执行（均不主动操作设备）：

```powershell
node research/kimi-android/tools/verify-run03.mjs --check --require-restored
node research/kimi-android/tools/verify-run03.mjs --distributed --check --require-restored
node research/kimi-android/tools/check-run03-distributed-projection.mjs
& ./research/kimi-android/tools/package-library.ps1 -CheckOnly
```

实际 ZIP 另用 verify-run03.mjs --distributed --check --archive <ZIP> 校验成员字节。--require-complete 在依赖未解决时应失败。消费者无需原 APK、AVD 或被排除的私人原件；详见 [验证模式](docs/run03-verification-modes.md)。恢复与退出的逐字段结果见本轮 restore、final-device-state 和 shutdown-verification。是否全部一致以记录和 require-restored 检查为准。

历史结果：[run02](docs/run02-results.md) / [原图](run02.html)、[首轮原图](runtime.html)。这些是各轮快照，不能用旧“未登录/参考未实现”描述当前状态。
