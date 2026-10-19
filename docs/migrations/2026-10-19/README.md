# REA研究内迁记录

总库是唯一维护入口；三个旧仓库不再作为子模块或运行依赖。新研究直接加入对应platforms目录，见[新增指南](../../adding-studies.md)。

已推送实现提交 `6fb5a216de30232c9a70d55c47863139ca2a1c84`，其中保留了三个旧仓库的完整历史。普通GitHub克隆已验证：全部13610个研究文件逐字节一致，无.gitmodules、gitlink或外部objects依赖，旧提交均为总库历史祖先且legacy标签存在。目录8项测试、浏览器8场景（1440/390）、静态导出、技能9项契约及Kimi分发包校验通过。本次不改变来源技术覆盖，也未执行原App。

| 旧仓库 | 总库入口 | 原文件数 | 历史标签 | 远端删除 |
| --- | --- | --- | --- | --- |
| hark-ui-research | [Hark](../../../platforms/web/hark/README.md) | 249 | legacy/hark/main | 用户手动删除，已核验 |
| ui-phone-mi-fitness | [小米运动健康](../../../platforms/mobile/mi-fitness/analysis/ui/README.md) | 8737 | legacy/mi-fitness/main | 用户手动删除，已核验 |
| ui-phone-mirasim | [Mirasim](../../../platforms/mobile/mirasim/README.md) | 1194 | legacy/mirasim/main | 用户手动删除，已核验 |

Kimi的3430个已审分发文件继续随总库保存，不对应上述三个旧仓库。Skill原仓库、其他普通产品、本机原包/AVD/私有研究原件不在删除范围。

用户已于2026-10-19手动删除上述三个旧分析仓库。2026-10-19 06:33:15 UTC以ECdison6227本人认证核验：三个仓库查询均返回404，完整的账号所有仓库列表中也不存在其名称及原仓库ID，排除了仅重命名或匿名不可见的情况。rea-ui-library与rea-style-library查询均返回200；总库继续为私有，skill原仓库继续为公开。删除前的API 403仅作为历史尝试保留，当前迁移与旧库清理已完成，详见[机器可读结果](imports.json)。

删除前已建立完整bare镜像、独立git bundle并通过bundle verify/fsck；三个旧库均只有main分支，无标签、Issue、PR、Release附件、LFS或Wiki内容。备份位于原工作区.runtime/github-ui-inventory-20261019/consolidation-backup，bundle的SHA记录在imports.json；总库自身的legacy标签和合并历史也能独立回溯旧提交。原文中的历史GitHub地址保留为来源记录；活跃目录入口全部使用总库路径。

验证报告留在本机.runtime/consumer-verification.json与.runtime/browser-qa.json，避免把本机路径和浏览器运行状态混入静态导出。
