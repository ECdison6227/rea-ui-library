# 在总库新增设计研究

本库是统一维护入口，按原版平台放入`platforms/web|desktop|mobile/<id>/`。默认研究视觉设计、组件/状态、布局组合、关键动效与通用交互；原业务功能作样本。独特业务页的设计应提取，实际付款/同步算法等不成为默认验收。

## 新研究：Design Package v1

先读根AGENTS及[格式协议](../skills/rea-design-transfer/references/design-package.md)。使用已安装`rea-design-transfer`或本库锁定副本。

```sh
node skills/rea-design-transfer/scripts/design-package.mjs init platforms/mobile/my-product --id my-product --name "My Product" --platform mobile --runtime android --version "实际版本" --identity "实际包名/制品身份"
```

默认入口：design-package.json、DESIGN_SPEC.md，以及tokens、components、layouts、interactions、motion、assets、evidence和参考/对照/迁移指南。初始化是draft，无证据、无运行通过。研究后填实际原件/来源/SHA与A/B/C/D，保留历史采集时间和核心/可选缺口；不要把模板直接入库。

有真实REA研究输出并完成本轮可支持研究后，导出profile并登记：

```sh
node skills/rea-design-transfer/scripts/design-package.mjs validate platforms/mobile/my-product/design-package.json
node skills/rea-design-transfer/scripts/design-package.mjs profile platforms/mobile/my-product/design-package.json --root platforms/mobile/my-product --output .runtime/my-product.profile.json
npm run study:add -- --profile .runtime/my-product.profile.json
npm test
npm run build
npm run verify
```

profile导出要求included且可追溯的REA源研究声明/原件和实际方法；检查器不能认证作者声明或取代像素复核。partial可以如实入库，ready必须有可实施核心设计及对照/跨领域验证。用户接受某些业务边界不改变原coverage。

visibility:public只表示可在目录显示该profile，不代表原始证据获准公开，本仓库继续为私有。副本/原件不能带嵌套.git、凭据、账号profile、依赖缓存或未经审核的私人画面；封面选用实际查看且已审核的图。总库不会从浏览器启动来源应用。

## 已有或旧格式研究

原有来源保留自己的目录、技术栈和证据。无design-package.json时，按现有README、spec、tokens、对照和边界读取。可以新增标准适配入口/模块，但不重写原件、把聚合当新A或自动抬高readiness。

旧格式profile可参照[catalog模板](../catalog/study.template.json)，保留实际研究入口与spec。不能仅因有普通UI代码就作为REA来源登记。迁移样例跟随来源，不增加原版数量；无桌面研究可保持空分类。

## 修改与推送

新增与更新都先检查Git差异、来源AGENTS、分类、入口与分享范围。修改已登记研究时显式执行：

```sh
npm run study:lock -- --id my-product
npm run build
npm run verify
```

普通启动/build/verify只核对已锁字节，不自动接受变化。确认后按已有授权提交推送；保持仓库私有性。界面计数动态来自登记表，无需改固定数量。

历史迁入来源的完整Git历史在legacy/hark/main、legacy/mi-fitness/main、legacy/mirasim/main标签；旧URL只是出处，日常使用本总库platforms目录。
