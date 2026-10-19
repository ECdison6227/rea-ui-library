# REA UI Library

本目录是用户已确认的统一REA研究与参考项目库，只整合REA研究产物，按网页、桌面、手机分类；各来源保留自己的风格、技术栈、Git 历史和启动方式。上级 Kimi 研究的续测指令不适用于本总库的目录/界面开发；不要因此启动模拟器或重跑原包分析。

先读 README.md、catalog/sources.json、docs/architecture.md。platforms 下的 Git 子模块属于独立来源；在修改其源码前读取来源 AGENTS.md，并使用单独分支。普通目录/元数据整合不修改来源原图、资产或原始证据。

区分研究覆盖、迁移可用性和用户接受范围。Hark 是 Web 研究；Mirasim 是手机混合应用，其浏览器适配器不验证原生权限。Kimi 采用已审分发包，保留原版与独立参考的区别，用户已接受未测边界以开展 UI/核心交互实现。普通产品、单独UI实验和迁移应用不独立入库；来源自己的迁移样本仍作为研究交付保留。未找到桌面REA研究时保持空分类。

private/、catalog/private.local.json、.runtime/ 不得进入公开构建或 Git 提交。Hark 原始证据包含既有聊天，默认不在总库公开导出。总入口只在127.0.0.1运行，不从浏览器启动来源应用、安装依赖、提交表单或调用原厂接口。

验证：npm test、npm run verify、npm run build。总库远端ECdison6227/rea-ui-library为私有；不擅自改公开或修改原仓库。原skill升级保留为草案，未推到原skill仓库。
