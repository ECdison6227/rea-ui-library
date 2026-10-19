# REA UI Library

按**网页、电脑端、手机端**组织的REA产品风格研究与参考项目库。每份来源保留自己的设计语言、原始资料、Git版本、技术栈和运行方式。

总仓库：[ECdison6227/rea-ui-library](https://github.com/ECdison6227/rea-ui-library)，当前为私有仓库，也是后续分析的唯一维护入口。已纳入资料直接存放在本库；无需旧仓库或子模块。旧历史随总库保留，迁移与删除结果见[迁移记录](docs/migrations/2026-10-19/imports.json)。

## 打开统一入口

需要Node.js 22.13+和Git；总目录无第三方运行依赖。

```sh
git -c core.longpaths=true clone https://github.com/ECdison6227/rea-ui-library.git
cd rea-ui-library
npm run sources:init
npm run dev
```

打开 **http://127.0.0.1:4318/**。支持平台筛选、关键词搜索、研究文档、最多三个来源比较，以及复制给Agent的迁移任务。端口冲突会明确报错，可用 `npm run dev -- --port 4320` 指定空闲端口。

普通 clone 就带齐全部研究。`npm run sources:init` 仅设置本仓库的Git长路径并校验本地文件，不访问旧仓库，不修改全局Git配置或系统注册表。启动总入口不启动原应用、模拟器、来源后端或账号服务。

| 平台 | 当前来源 | 主要可复用内容 |
| --- | --- | --- |
| 网页 | [Hark](platforms/web/hark/README.md) | 流式内容、交互卡片、CSS变量、表格与图片交互 |
| 电脑端 | [空分类说明](platforms/desktop/README.md) | 当前没有符合范围的独立桌面REA研究 |
| 手机端 | [小米运动健康](platforms/mobile/mi-fitness/analysis/ui/README.md) | 卡片与设置、Android原生参考、逐项配对和迁移示例 |
| 手机端 | [Mirasim](platforms/mobile/mirasim/README.md) | React/Capacitor前端、深浅主题、权限与更新接口边界 |
| 手机端 | [Kimi](platforms/mobile/kimi/README.md) | 输入、草稿、异步、保存取消、原生参考与Fieldnotes迁移 |

仅收REA研究产物。普通产品、单文件UI实验和单独迁移应用不纳入来源计数；研究自身的transfer-example仍保留。Mirasim属于手机混合应用，浏览器模拟桥接不证明Android系统行为。Hark即使有手机宽度截图，仍是网页研究。

## 在自己的项目里使用

1. 在入口选择来源，检查版本、覆盖及保留边界。
2. 阅读该来源的规范、状态契约、对照和参考。选中的来源独立使用，不默认混合全部设计语言。
3. 点“复制迁移任务”，填写你的项目路径；让Agent先审查业务、品牌、目标平台与已有代码，再建立规则映射并实现。
4. 在目标产品里验证代表页面的正常、空、忙碌、错误、取消和返回状态，再校准视觉细节。

Kimi的“相似UI与核心用户流程”目标已由用户于2026-10-19接受；原始37项清单仍保留20项verified和剩余未测/部分验证。这是约定目标可收尾，不是把未测功能改成通过。

## 设计提取与迁移技能

新技能名为 **`rea-design-transfer`（REA 设计提取与迁移）**，技能源仓库地址保留为[rea-style-library](https://github.com/ECdison6227/rea-style-library)。本库的[技能副本](skills/rea-design-transfer/SKILL.md)及同步锁用于离线读取，改动先回到技能源；[改进与验证边界](docs/skill-review.md)记录具体范围。

默认围绕视觉元素、组件/状态、布局组合、关键动效和通用交互研究；不穷举原业务功能或恢复后端、支付、同步、播放内核。已有功能和普通UI的产品可以直接从目录复制迁移任务，按选定规范实施，保留自己的业务、数据、品牌和技术栈。

新研究统一交付 **Design Package v1**：`design-package.json` → `DESIGN_SPEC.md` → tokens/组件/布局/交互/动效/资产/证据及参考/对照/接入入口。详见[格式协议](skills/rea-design-transfer/references/design-package.md)与[新增指南](docs/adding-studies.md)。原版coverage、迁移readiness和约定交付分别报告；空模板不成为ready，原业务未测比例也不替代设计验收。

已有四份研究继续按原格式使用，不覆盖其原件或自动提高覆盖。按需新增适配入口后可登记designPackage字段。最初升级补丁保留在[历史记录](docs/history/skill-upgrade-initial.patch)，不再作为当前安装方法。

## 验证与静态导出

```sh
npm test
npm run verify
npm run build
```

`dist/`仅含统一目录、精选已审缩略图和技能审查说明，不复制来源的原始证据或私有项目。静态目录的文档链接统一指向本总库内的研究目录；本地服务器有文档查看功能，静态导出不提供本机API。各研究的完整文件清单与SHA保存在catalog/manifests，历史出处单独保存。

Hark原始截图/DOM含既有聊天，默认不开放原始证据查看器。需要在自己的电脑上查看时运行 `npm run dev:private`，仍仅监听127.0.0.1。不要把该模式直接绑定公网。

## 后续新增UI分析

直接把REA研究放进 `platforms/web/<id>/`、`platforms/desktop/<id>/` 或 `platforms/mobile/<id>/`，按[新增指南](docs/adding-studies.md)登记。以后不需要再创建单独分析仓库。

[架构与更新](docs/architecture.md) · [来源盘点与入库依据](docs/inventory.md) · [验证](docs/validation.md) · [索引与版本锁](catalog/imports.lock.json)。技能与研究库独立维护，已选来源的版本与限制随迁移任务传递。
