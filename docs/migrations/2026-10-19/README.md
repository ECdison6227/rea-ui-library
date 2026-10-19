# REA研究内迁记录

总库是唯一维护入口；三个旧仓库不再作为子模块或运行依赖。新研究直接加入对应platforms目录，见[新增指南](../../adding-studies.md)。

已推送实现提交 `6fb5a216de30232c9a70d55c47863139ca2a1c84`，其中保留了三个旧仓库的完整历史。普通GitHub克隆已验证：全部13610个研究文件逐字节一致，无.gitmodules、gitlink或外部objects依赖，旧提交均为总库历史祖先且legacy标签存在。目录8项测试、浏览器8场景（1440/390）、静态导出、技能9项契约及Kimi分发包校验通过。本次不改变来源技术覆盖，也未执行原App。

| 旧仓库 | 总库入口 | 原文件数 | 历史标签 | 远端删除 |
| --- | --- | --- | --- | --- |
| hark-ui-research | [Hark](../../../platforms/web/hark/README.md) | 249 | legacy/hark/main | 403，未删除 |
| ui-phone-mi-fitness | [小米运动健康](../../../platforms/mobile/mi-fitness/analysis/ui/README.md) | 8737 | legacy/mi-fitness/main | 403，未删除 |
| ui-phone-mirasim | [Mirasim](../../../platforms/mobile/mirasim/README.md) | 1194 | legacy/mirasim/main | 403，未删除 |

Kimi的3430个已审分发文件继续随总库保存，不对应上述三个旧仓库。Skill原仓库、其他普通产品、本机原包/AVD/私有研究原件不在删除范围。

用户已授权完整迁入后删除上述三个旧分析仓库。GitHub删除API均返回403；当前Git授权scope为gist、repo、workflow，缺少[删除仓库要求的delete_repo权限](https://docs.github.com/en/rest/repos/repos#delete-a-repository)。这不影响总库使用；需要账号所有者在各仓库Settings的Danger Zone完成删除，或为现有授权补足删除权限后再执行。未将失败记成已删除，详见[机器可读结果](imports.json)。

删除前已建立完整bare镜像、独立git bundle并通过bundle verify/fsck；三个旧库均只有main分支，无标签、Issue、PR、Release附件、LFS或Wiki内容。备份位于原工作区.runtime/github-ui-inventory-20261019/consolidation-backup，bundle的SHA记录在imports.json；总库自身的legacy标签和合并历史也能独立回溯旧提交。原文中的历史GitHub地址保留为来源记录；活跃目录入口全部使用总库路径。

验证报告留在本机.runtime/consumer-verification.json与.runtime/browser-qa.json，避免把本机路径和浏览器运行状态混入静态导出。
