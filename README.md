# REA UI Library

**先把软件做好，再选一款 App 的风格，让 AI 拉取这里的设计包，直接应用到你的项目。**

这里保存可复用的视觉设计、组件、动画和用户交互规则。应用后仍使用你的业务、数据、品牌与技术栈；改变的是它们如何呈现、如何响应用户。

## 选一套风格

| 参考 App | 适合借鉴 | 入口 |
| --- | --- | --- |
| **多邻国** | 有厚度的按钮、明确选择反馈、分步引导、进度与结果、轻快动效 | [设计包](platforms/mobile/duolingo-android-6-80-6-china-2368/README.md) |
| **小米运动健康** | 概览卡片、分组设置、层级留白、导航与弹层、受控开关 | [设计包](platforms/mobile/mi-fitness/README.md) |
| **Kimi** | 多行输入、草稿、对话反馈、选项、保存取消、异步与重试 | [设计包](platforms/mobile/kimi/README.md) |
| Hark | 玻璃材质、流式内容、富内容卡片与天空背景 | [研究参考](platforms/web/hark/README.md) |
| Mirasim | 简洁分组、深浅主题、抽屉、混合应用平台边界 | [研究参考](platforms/mobile/mirasim/README.md) |

前三份采用统一 **Design Package v1**，含实施选择与明确差异；Hark、Mirasim暂保留旧研究入口。`partial`表示原版仍有未确定参数或还原差异，使用时按包内适用范围实施，不等于只能看文档。

## 直接复制给 AI

替换选定 App 和项目路径即可：

```text
我的软件功能和基础界面已经完成。请从
https://github.com/ECdison6227/rea-ui-library
拉取设计规范，把【多邻国 / 小米运动健康 / Kimi】的UI风格、动画和通用用户交互应用到我的项目：【项目路径】。

先读仓库AGENTS.md和选定App的design-package.json、DESIGN_SPEC.md、ADAPTATION_GUIDE.md，
再按需读取tokens、components、layouts、interactions、motion、资产和对照。
记录实际拉取的commit。沿用我的技术栈、业务、数据、品牌和接口。

请实际修改代码，完成排版、配色、间距、形状、组件状态、布局和动画，
以及返回、输入、草稿、保存取消、加载、错误和重试等用户逻辑。
来源不确定的参数按包内明确D方案适配，不把目标选择当成原版事实。
运行代表页面和交互，交付前后/来源对照与验证结果。不要只给计划、调色板或静态首页。
```

已安装技能时可加 `$rea-design-transfer 应用规范模式`；未安装时AI可以直接读取本库[skill](skills/rea-design-transfer/SKILL.md)。仓库已按所有者要求公开，AI可直接拉取，不需要用户提供GitHub密码或令牌。

## 只拉取选定风格

AI可以稀疏检出该来源，避免下载其他研究。以小米为例：

```sh
git -c core.longpaths=true clone --filter=blob:none --sparse https://github.com/ECdison6227/rea-ui-library.git
cd rea-ui-library
git config --local core.longpaths true
git sparse-checkout set platforms/mobile/mi-fitness skills/rea-design-transfer docs
git rev-parse HEAD
```

多邻国目录：`platforms/mobile/duolingo-android-6-80-6-china-2368`；Kimi目录：`platforms/mobile/kimi`。先读规范，再查看原图、参考、源码和资产。Android的dp/sp不要直接套成网页px；原产品的支付、健康算法或模型服务不属于风格迁移。

## 浏览与比较

[GitHub Pages 在线目录](https://ecdison6227.github.io/rea-ui-library/)随main推送自动校验、构建和发布。新研究登记并推送后无需手改网页；部署成功后，已打开的网页每分钟检查新目录，也可点击“检查更新”。[发布机制与排查](docs/pages.md)。

完整clone后，在仓库根执行：

```sh
npm run sources:init
npm run dev
```

打开 **http://127.0.0.1:4318/**。可按平台/关键词选型、查看资料、比较来源，并复制已填好来源路径的AI实施指令。需要Node.js 22.13+和Git，无第三方运行依赖。目录只读，不启动原App、模拟器或原厂服务。

## 设计包里有什么

| 内容 | AI用它做什么 |
| --- | --- |
| design-package.json、DESIGN_SPEC.md | 确认来源、版本、适用范围和视觉原则 |
| tokens、components、layouts | 实现文字、表面、几何、组件状态和页面组合 |
| interactions、motion | 实现状态变化、时序、曲线、中断、取消与减少动画 |
| assets、evidence、reference、comparison | 找素材、读原版依据、运行参考并检查差异 |
| ADAPTATION_GUIDE.md | 把风格映射到已有产品，保留业务与平台习惯 |

源观察、静态声明、推断、目标实现分别标为A/B/C/D。研究截图不等于目标已实现；AI应在你的软件里完成最后的运行验证。

## 维护与入库

新来源必须提供可用于实施的标准包，不能只上传截图、报告、空JSON或调色板。小米和Kimi已升级；Hark/Mirasim作为明确的历史研究保留。总库直接维护资料和文件锁，无子模块。

```sh
npm test
npm run build
npm run verify
```

[入库标准](docs/adding-studies.md) · [AI实施约定](docs/apply-a-style.md) · [格式协议](skills/rea-design-transfer/references/design-package.md) · [历史来源](docs/migrations/2026-10-19/imports.json)

仓库于2026-10-20按所有者明确要求公开，已有Git历史随仓库可访问。Pages静态build仍只含目录、技能说明和已审缩略图，不复制整个研究目录。后续入库仍须审查凭据、设备profile、私人画面和工具缓存，不能因为仓库公开就自动提交这些内容。
