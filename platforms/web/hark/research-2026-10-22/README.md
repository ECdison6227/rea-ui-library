# Hark 当前设计研究与资源入口

已接上 2026-10-22 的研究：当前 App 登录态已核实，字体、图标、天空和公开媒体原件已归档，并新增可运行组件参考。设计迁移就绪仍为 **partial**：当前代表状态与全状态等价、独立参考与原版行为分别记录。

默认入口为包根README.md，本版替换Library原Hark Web条目。先读 [DESIGN_SPEC.md](../DESIGN_SPEC.md) 和 [design-package.json](../design-package.json)，再按需取组件、tokens、交互与动效。

完整入口导航另见 [ROUTE_INDEX.json](ROUTE_INDEX.json)：保留公开内容地图原采集身份，并补当前登录后代表路由（包括原公开地图仍标未读的/plans实际登录态观察）。

| 材料 | 入口 | 实际范围 |
| --- | --- | --- |
| 当前登录 App | [32组运行观察](authenticated-runtime/observations.json) | 桌面/窄屏、草稿导航、搜索空状态、设置、主题、摘要及准确动效宿主 |
| 当前 App 源与资产 | [资源说明](current-app/README.md) / [清单](current-app/manifest.json) | 257份原件：189 JS、46 CSS、21 WOFF2、1 HTML；已发现literal静态依赖256/256 |
| 当前字体 | [App字体](current-app/font-inventory.json) / [官网19份](public/resources/declared-fonts-fetched-manifest.json) | App21份与官网19份中15份同字节；保留不同CSS绑定及字库/weight表 |
| 当前图标 | [icons.json](current-app/icons.json) / [命名字形sprite](current-app/named-glyph-sprite.svg) | 74命名字形、160 SVG根/157 owner，精确来源范围与SHA；非“160个独立图标” |
| 天空 | [sky-static.json](current-app/sky-static.json) | shader/LUT同历史字节，当前CPU引擎新增中断淡化逻辑；静态参数不是FPS |
| 公开媒体 | [说明](current-public-media/README.md) / [清单](current-public-media/manifest.json) | 92个明确URL，36图/56视频；10slides/5features绑定及编码协商响应 |
| 公开站内容 | [内容地图](SITE_CONTENT_MAP.md) | 首页及19个公开路由；法律/新闻完整正文与代表像素的范围分开 |
| 公开站当前运行 | [16张已审状态图与时序](public-runtime/README.md) | 3D轮播键盘/中断/窄屏/reduce、套餐切换、FAQ与实际字体；后台冻结另列 |
| 组件参考 D | [启动](reference/README.md) / [运行收据](comparison/reference-run.json) | 本地资料工作区，13项检查通过、8张截图已审 |
| 原版/参考差异 | [comparison](comparison/README.md) | 字体、背景、Auto、h2、阅读层位置等明确差异 |
| 历史基线 | [historical-audit.md](historical-audit.md) | H15原REA、H13手动导出和外部clone，保留时期与身份 |

参考运行：进入本目录的 reference，执行 npm run dev，打开 http://127.0.0.1:4393。直接取原字体/图标/材质算法，再用参考验证目标自己的数据流；D参考不调用原Hark服务。

资产总入口为 [assets-manifest.json](assets-manifest.json)，证据总入口为 [evidence-index.json](evidence-index.json)。原件、协商编码、派生sprite、精确源切片和D接口有各自身份与SHA。私有账户原件在总库 private，不进入规范；本包经分发成员审查后distribution=reviewed-public，留存不授予品牌/字体/视频再分发许可。

当前仍未覆盖所有图库、文件、链接栈、回复、错误、焦点、reduced-motion和跨设备条件；App逐字实际字体、全部天气/性能、OS键盘/触控另列。没有真实支付、外部授权、安全变更或反馈提交。原版深度、迁移就绪与本轮约定范围是三个结论，不能因格式通过或D运行就宣称全站全状态完成。

先前“待登录/字体未齐/参考未制作”的规范保留在 spec-before-auth-resume.md，仅作研究过程归档；当前入口以本页与根设计规范为准。

检查结果见 [verification.json](verification.json)：Hark入库格式/路径/引用/哈希与消费者契约通过；当前参考13项交互、8图和7项HTTP/字体检查通过。发布前已补齐完整检出：总库44项测试全部通过，build和verify通过；未改动或排除其他研究来源。先前稀疏检出的失败仅作过程记录保留。
