# Hark Web 设计包

这是替换 Library 原 Hark Web 条目的当前研究包。包含登录后 App、公开站、原字体/图标/媒体、组件状态与可运行的独立参考。

先读 [设计规范](DESIGN_SPEC.md) 和 [机器入口](design-package.json)。详细资料从 [研究索引](research-2026-10-22/README.md) 进入。

| 内容 | 当前交付 |
| --- | --- |
| 设计系统 | 210条token、37个组件族、12种布局、21套交互、20项动效 |
| 当前原版 | 32组登录运行记录、14类代表路由、桌面/窄屏/主题/搜索/阅读；公开轮播、套餐与FAQ |
| 字体和图标 | App21份字体、官网19份字体（15份同字节）、74命名字形与源SVG构造 |
| 源与媒体 | App257份静态原件；92个公开媒体URL、107份响应文件，保留编码协商差异 |
| 独立参考D | 13项交互检查、8张已审截图、7项HTTP/字体检查 |

- [组件与状态](components/catalog.json) · [Tokens](tokens/tokens.json) · [交互](patterns/interactions.json) · [动效](motion/catalog.json)
- [资产清单](research-2026-10-22/assets-manifest.json) · [证据索引](research-2026-10-22/evidence-index.json) · [当前运行](research-2026-10-22/authenticated-runtime/README.md)
- [字体、SVG与天空源码](research-2026-10-22/current-app/README.md) · [媒体与绑定](research-2026-10-22/current-public-media/README.md)
- [运行参考](research-2026-10-22/reference/README.md) · [原版/参考差异](research-2026-10-22/comparison/current-app.md) · [迁移接入](ADAPTATION_GUIDE.md)

参考启动：进入 `research-2026-10-22/reference` 后执行 `npm run dev`，打开 `http://127.0.0.1:4393/`。

本包为partial，表示仍有明确的还原差异和未测矩阵，不表示没有可实施规格。A原版观察、B源声明、C推断与D独立实现分别保留；D检查通过不等于原版全部业务/状态已测。原版资产许可不因研究留存而改变。

旧版研究只作[历史来源](LEGACY_RESEARCH.md)保留，Library 默认入口已切换到本标准包。账户原始画面、字段值、cookie/token和本机profile均不随此次更新上传；实际公开成员见本包 publication-review.json。
