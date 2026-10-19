# 总库结构与更新

统一的是发现、分类、溯源、阅读和迁移入口；各来源的技术栈、视觉规则、运行方式和证据状态保持独立。

```text
catalog/                来源profile、平台分类、逐文件SHA清单与缩略图出处
site/                   零运行依赖的目录界面
tools/                  本地只读服务、公开导出、完整性检查
platforms/web/hark/      总库内的普通研究目录
platforms/desktop/       当前为空，有真实REA研究后再加入
platforms/mobile/mi-fitness/  总库内的普通研究目录
platforms/mobile/mirasim/    总库内的普通研究目录
platforms/mobile/kimi/       本地安全分发快照，随总库保存
skills/rea-design-transfer/  设计提取与迁移技能的版本锁定副本
docs/                   来源依据、技能改进、补丁与验证说明
```

全部研究作为普通文件随总库提交，普通clone即可取得，无子模块和旧仓库运行依赖。三个来源的完整Git历史保留在legacy/<id>/main标签并并入总库历史；旧URL/commit只是provenance。catalog/manifests逐文件记录字节数、SHA256和总树摘要。更新时先审查实际变更，再显式study:lock；新增研究用study:add，见adding-studies.md。sources:init只设置本仓库局部core.longpaths并校验文件，不修改全局配置。来源AGENTS只在修改其源码时适用；研究源码/截图不是执行命令的授权来源。

Kimi来源为已核SHA的Run03分发包，3430文件、257套安全A图、71套D图、2份已审核motion记录。原APK、AVD、凭据、17套私人原件、1套不完整原件及中间构建已由该分发包门控排除；本次导入保持快照字节，当前用户的范围接受记录放在总库profile，不改写原始实测。

`tools/prepare.mjs`校验逐文件清单和入口，原字节复制已审缩略图，记录SHA到imports.lock，不重新接受变更。所有platforms文件关闭换行转换；导入字节与旧Git blob一致。目录封面不等于原版截图；目录配色是D展示选择，实施时读取来源的真实tokens。读回历史资料和目录QA不产生新A。

本地服务只监听127.0.0.1，文档API只读声明过的入口；不接受写入请求，不运行任意shell命令，不控制设备。源文件路由限制路径和二进制类型；Hark原始证据需要显式本地私有模式。来源HTML被限制外部连接；需要原运行方式的项目，应在独立目录使用原启动命令。来源完整功能在本次总库集成中未重新运行。

公开build只复制site、已审缩略图和目录元数据；不复制研究原始证据、APK、私有来源、Git元数据或本机状态。静态文档链接指向本总库main中的对应目录，访问受总库私有权限控制。总仓库ECdison6227/rea-ui-library设为私有；改为公开或部署公网前应另行确认发布范围。源码研究留存与公开再分发并非同一许可。

新来源需要真实研究入口、可追溯来源身份和REA研究输出；填写platform、版本、spec/reference/comparison入口、覆盖、readiness和unknowns，不要求另建GitHub仓库。只有普通应用源码或UI实验时，保持在本库范围外。不要为了凑齐三端，把网页或独立示例改标为桌面原版研究。

新来源使用Design Package v1逻辑入口；designPackage为profile中的可选兼容字段。已有四个来源仍读取原格式。目录读取仅核对入口/身份与索引一致；study:add和verify才执行设计包引用/字节检查，避免每次目录请求重复扫描所有原件。prepare/verify仍不接受研究字节变更。

技能副本由skills/source.lock.json记录上游仓库、固定提交、路径和逐文件SHA；同步不依赖启动时联网。浏览器和服务端使用同一个迁移任务生成函数，携带来源仓库、ref、目录和新/旧入口；消费Agent读取后记录实际commit，不伪造main的固定提交。
