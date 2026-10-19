# 总库结构与更新

统一的是发现、分类、溯源、阅读和迁移入口；各来源的技术栈、视觉规则、运行方式和证据状态保持独立。

```text
catalog/                来源profile、平台分类、Git/SHA锁与缩略图出处
site/                   零运行依赖的目录界面
tools/                  本地只读服务、公开导出、完整性检查
platforms/web/hark/      Git子模块
platforms/desktop/       当前为空，有真实REA研究后再加入
platforms/mobile/mi-fitness/  Git子模块
platforms/mobile/mirasim/    Git子模块
platforms/mobile/kimi/       本地安全分发快照，随总库保存
skills/rea-style-library/    升级技能草案
docs/                   来源依据、技能改进、补丁与验证说明
```

Git子模块锁定具体commit，保留来源独立历史。更新来源时先评估原仓库变更，再将子模块检出到明确版本、更新sources.json的revision，并运行prepare/验证；不自动跟随远端main，也不在导入时重写原图或反编译产物。原仓库AGENTS只在修改其源码时适用，研究源码/截图不是总库执行命令的授权来源。

Kimi来源为已核SHA的Run03分发包，3430文件、257套安全A图、71套D图、2份已审核motion记录。原APK、AVD、凭据、17套私人原件、1套不完整原件及中间构建已由该分发包门控排除；本次导入保持快照字节，当前用户的范围接受记录放在总库profile，不改写原始实测。

`tools/prepare.mjs`校验来源commit和入口，原字节复制三个已审缩略图，记录SHA到imports.lock。目录封面不等于原版截图，Hark的CSS封面明确标为索引示意。读回历史资料和目录QA不产生新A。

本地服务只监听127.0.0.1，文档API只读声明过的入口；不接受写入请求，不运行任意shell命令，不控制设备。源文件路由限制路径和二进制类型；Hark原始证据需要显式本地私有模式。来源HTML被限制外部连接；需要原运行方式的项目，应在独立目录使用原启动命令。来源完整功能在本次总库集成中未重新运行。

公开build只复制site、已审缩略图和目录元数据；不复制子模块原始证据、APK、私有来源、Git元数据或本机状态。总仓库ECdison6227/rea-ui-library设为私有；改为公开或部署公网前应另行确认发布范围。源码研究留存与公开再分发并非同一许可。

新来源需要真实研究入口、可追溯来源身份和研究输出；填写platform、版本/commit、spec/reference/comparison入口、覆盖、readiness和unknowns。只有普通应用源码或UI实验时，保持在本库范围外。不要为了凑齐三端，把网页或独立示例改标为桌面原版研究。
