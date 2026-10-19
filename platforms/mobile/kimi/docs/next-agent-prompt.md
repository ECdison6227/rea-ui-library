# 下一位 Agent 的执行要求

先读 [session-handoff](session-handoff.md)、[remaining-validation](remaining-validation.json)、[run03-results](run03-results.md)、[library](../library.json)。最新是 run03，历史原件不追加不覆盖。37 项中 verified 20；needs_user 15；needs_authorization 1；partial 1；unverified 0。 F01、F06、F07、F08、F10、F17、F21、F22、F23、F24、F25、F27、F28、F29、F30、F33、R04 仍未销项。用户已要求把设备与账号受限项保留未完成，这不是缩减研究范围。

继续研究时先核目标3.1.3/376与专用5596 AVD；使用新run、单一设备控制者、自建可丢弃数据。沿用 rea-style-library / reverse-engineer-anything。不要重做全包解析、重复询问用户已说不可提供的设备/账号，也不要在登录页、容量提示或独立fixture上宣布全部完成。

若将本库用于其他软件：先审查目标项目的品牌、功能、导航、输入和异步模型；读 [行为规则](../spec/run03-behavior-and-transfer.md)、[组件契约](../reference/component-contracts.json)、[来源映射](../reference/source-mapping.json)；建立来源规则→目标组件/状态→保留/适配/替换映射，再实施并检查差异。保留目标业务与品牌，另存新文案/图标，不覆盖研究原件。

参考为 Java Android View 原生 D，有运行包和独立验收；[原版配对](../comparison/native-run03.md) 与 [不同业务验收](../transfer-example/run03-acceptance.md) 分开读。不能将系统字体、6秒fixture、epoch取消、持久化策略、色值或尺寸称为原版精确实现。widget CSS有明确作用域，不提升成全局原生token。

按验收条件补真实结果、返回/取消/错误/保留及恢复。下一次获得设备或服务后再补相应未完成项目。外发、付款、公开发布、真实电脑和Claw实例的授权边界见交接。最后运行验证、恢复自己的设备并交付新包；文件完整性通过不等于研究全量通过。
