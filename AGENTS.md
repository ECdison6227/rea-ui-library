# REA UI Library

本目录是用户已确认的统一REA研究与参考项目库，只整合REA研究产物，按网页、桌面、手机分类；各来源保留自己的风格、技术栈、Git 历史和启动方式。上级 Kimi 研究的续测指令不适用于本总库的目录/界面开发；不要因此启动模拟器或重跑原包分析。

先读 README.md、catalog/sources.json、docs/architecture.md、docs/adding-studies.md。platforms 下全部是本总库直接维护的普通目录，不再建立子模块或独立分析仓库。在修改来源源码前读取其 AGENTS.md；普通目录/元数据整合不修改来源原图、资产或原始证据。新增研究放到原版对应的平台分类，登记 profile 并生成文件哈希清单；prepare/verify 不自动接受资料变更。旧仓库历史保留为 legacy/<id>/main 标签并并入总库历史。

区分研究覆盖、迁移可用性和用户接受范围。Hark 是 Web 研究；Mirasim 是手机混合应用，其浏览器适配器不验证原生权限。Kimi 采用已审分发包，保留原版与独立参考的区别，用户已接受未测边界以开展 UI/核心交互实现。普通产品、单独UI实验和迁移应用不独立入库；来源自己的迁移样本仍作为研究交付保留。未找到桌面REA研究时保持空分类。

private/、catalog/private.local.json、.runtime/ 不得进入Git提交或Pages构建。本地服务只监听127.0.0.1；Pages只发布目录与已审缩略图。用户于2026-10-20因套餐限制明确要求“直接公开仓库”，授权将此库及已有历史公开并启用Pages；该指令取代先前保持私有的要求。新入库资料仍需审查，不把公开仓库当作提交私人原件/凭据的授权。不从浏览器启动来源应用、安装依赖、提交表单或调用原厂接口。

新研究必须Design Package v1，并通过tools/consumer-contract.mjs的实施内容检查。固定design-package.json/DESIGN_SPEC.md及模块逻辑入口；核心组件必须有slots/events/states，交互有状态转移，核心动效有源参数或明确D替代。仅Hark/Mirasim以legacy-research保留旧格式；小米/Kimi/多邻国按标准包维护。原业务全测不作为设计门槛，原件与未知状态不改写。

用户通常先完成功能，再选App让AI拉取本库应用风格。消费AI应读取标准入口，沿用目标技术栈，实际实现视觉、动画和通用用户状态逻辑并运行对照；保留目标业务、数据和品牌。见docs/apply-a-style.md。此次用户已明确授权多邻国入库、升级小米/Kimi、修改入库规则/README并提交推送，随后授权公开总库并启用Pages自动发布。

来源历史文档中的rea-style-library调用名映射到当前rea-design-transfer，不要求安装两份技能；不要为改名改写原研究证据。

验证：npm test、npm run build、npm run verify。总库远端ECdison6227/rea-ui-library按所有者要求公开；main推送通过检查后自动更新Pages。此前用户授权迁入后删除三个旧分析仓库的结果见docs/migrations/2026-10-19，当前任务不再删除任何仓库。新技能名rea-design-transfer，技能源现为ECdison6227/rea-design-transfer；本库vendor副本与skills/source.lock.json保持一致，当前用户已明确授权升级本机及同步这两个仓库。
