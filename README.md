# REA UI Library

按**网页、电脑端、手机端**组织的REA产品风格研究与参考项目库。每份来源保留自己的设计语言、原始资料、Git版本、技术栈和运行方式。

总仓库：[ECdison6227/rea-ui-library](https://github.com/ECdison6227/rea-ui-library)，当前为私有仓库；原有来源仓库保持不变。

## 打开统一入口

需要Node.js 22.13+和Git；总目录无第三方运行依赖。

```sh
git clone --recurse-submodules https://github.com/ECdison6227/rea-ui-library.git
cd rea-ui-library
npm run dev
```

打开 **http://127.0.0.1:4318/**。支持平台筛选、关键词搜索、研究文档、最多三个来源比较，以及复制给Agent的迁移任务。端口冲突会明确报错，可用 `npm run dev -- --port 4320` 指定空闲端口。

本地首次副本已带齐三个Git子模块及Kimi快照，直接运行 `npm run dev` 即可。已有克隆可用 `git submodule update --init --recursive --depth 1` 补齐来源。启动总入口不启动原应用、模拟器、来源后端或账号服务。

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

## 技能升级草案

[升级技能](skills/rea-style-library/SKILL.md) · [具体改进与验证边界](docs/skill-review.md) · [针对原GitHub仓库的补丁](docs/skill-upgrade.patch)。

保留A观察/B声明/C推断/D独立实现，新增目标/研究覆盖/迁移就绪/交付接受的区分、阶段停止条件、有界依赖复试、早期参考配对以及严格REA入库。原仓库：[rea-style-library](https://github.com/ECdison6227/rea-style-library)，补丁基线 `42c82c4ac70a6a92e3748897051d70b883d29fac`。本机全局安装的技能尚未修改，GitHub原仓库未推送改动。

## 验证与静态导出

```sh
npm test
npm run verify
npm run build
```

`dist/`仅含统一目录、精选已审缩略图和技能审查说明，不复制来源的原始证据或私有项目。静态目录中的远端文档指向锁定Git版本；Kimi本地快照需要获取总库后阅读。本地服务器有文档查看功能；公开静态导出不提供该本机API。

Hark原始截图/DOM含既有聊天，默认不开放原始证据查看器。需要在自己的电脑上查看时运行 `npm run dev:private`，仍仅监听127.0.0.1。不要把该模式直接绑定公网。

[架构与更新](docs/architecture.md) · [来源盘点与入库依据](docs/inventory.md) · [验证](docs/validation.md) · [索引与版本锁](catalog/imports.lock.json)。私有总仓库提供统一入口；原始来源仓库保持独立，原skill仓库的改进仍以可审查补丁交付。
