# 小米运动健康：可迁移产品风格库

给没有聊天背景的 Agent 与你的自有软件源码一起使用：迁移视觉语言、交互策略、用户引导和通用软件行为。健康算法、穿戴设备、小米账号和会员业务不默认移植。

**主交付是 Android 原生三页与十组件基线。** 原版运行证据、实施规格、独立可运行参考和逐项对照分别保存。视觉与行为残留偏差见覆盖表；不是整个 App 的全量克隆。

| 阅读/操作 | 入口 |
|---|---|
| 实际覆盖与未知 | [baseline/coverage.json](baseline/coverage.json) |
| 原版与参考三页、十组件 | [最终离线对照](baseline/comparison/final/index.html) |
| 构建/安装原生参考 | [工程说明](android-reference/README.md) · 调试APK需本地构建，未随Git分发 |
| 视觉、事件、行为规格 | [规格](baseline/spec.md) · [机器契约](baseline/contracts.json) · [通用/业务行为](baseline/behaviors.md) |
| 其他业务如何复用 | [资料管理示例](transfer-example/README.md) |
| 图标、字体、插画与文案 | [资产库](asset-library/README.md) · [基线资产清单](android-reference/assets-manifest.json) |
| 直接给下个 Agent | [执行提示词](docs/next-agent-prompt.md) |

## 运行

在本目录执行：

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File android-reference/build.ps1
adb -s emulator-5580 install -r android-reference/build/style-reference.apk
adb -s emulator-5580 shell am start -S -W -n study.style.reference/.MainActivity --es scene health --es theme day
```

`scene` 可取 `health`、`mine`、`settings`、`components`、`transfer`。`theme` 为 `day` 或 `night`；最终逐项对照限定浅色。序列号换成你的设备；其他电脑依赖参数与复测命令见工程 README。直接查看对照 HTML 不需 Android 工具。

## 来源与证据

APK：`com.mi.health` **3.57.0 / 357000**，SHA-256 `2a80fc5a87353c2c6ef3c01fa0085862fe45e4ddb9c2d272cb3780841b5889a6`。欢迎页“4.0”是文案。我的页选 **mine 11.202.21** 五入口；内置四入口只作历史资料，见 [运行包差异](runtime-rn-crosscheck.md)。

对照环境：Android 15 / API35 Google APIs，1080×2400物理px，440dpi（density 2.75），fontScale 1，中文浅色，未登录、无设备、空数据。健康/设置以原生证据为主，我的正文为YRN；同名旧XML不能当当前正文。

**A** 原 App 观察；**B** 包内/捕获代码与资源；**C** 推断；**D** 独立实现/迁移选择。参考运行验证 D，不能升级成原版 A。历史图仍是 historical；`baseline/runtime` 是本轮 current 原版采集，原图保留原字节与真实REA输出。

## 接入目标项目

1. 先读规格、契约、行为与对照，再检查目标栈、导航、状态所有者和已有组件。
2. 建立“来源规则→目标源码位置→保留/替换/适配→依据→验证”映射，先 tokens/组件，后页面。
3. 复用层级、留白、字体角色、表面关系、事件和状态保留；替换业务词、图标含义、数据、账号及平台服务。未知行为按目标需求作 D 决策。
4. 运行目标并保留对照。保持已有业务正确性与数据，不将研究参考的“范围边界”占位框接入最终产品。

MIUI smooth/adaptive 圆角、完整动画、触觉、逐字 OEM 字体、登录后/真实设备/有数据图表、横屏/折叠屏仍有未验证项。详情页与认证SDK不在三页重建范围。资料管理为本地会话fixture。准确边界见覆盖表与 [实现适配](baseline/implementation-notes.md)。

## 进一步追证

[原生 tokens](design-tokens.json)、[CSS映射](tokens.css)、[RN规则](rn-pages.json) 保留语境与来源。[资源快照映射](evidence/source-snapshot-map.json) 与 [本轮补充映射](baseline/source-snapshot-map.json) 将旧 `analysis/phase2/apktool/res/` 路径映射到库内；也可运行 `node tools/resolve-source.cjs <原记录路径>`。生效完整bundle在 `runtime/active-bundles/`，精选源码在 `native-sources/`、`native-extra/`、`rn-evidence/`。

`contracts.json` 是语义迁移规格，宽于本基线Java参考的实际API。可直接调用/需要适配/未实现的字段逐项见 [Java API覆盖](android-reference/java-api.md)，禁止据契约字段存在宣称对应状态已运行。

[旧网页研究台](preview/index.html) 可浏览tokens与历史原图，其通用模板不作为原生还原验收。旧 `components.json` 九组件清单是静态附录，本轮十组件以 `baseline/contracts.json` 为准。旧文档“未连接设备”描述当时阶段，不覆盖之后的运行证据。
