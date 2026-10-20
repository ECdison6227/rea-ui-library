# Kimi Web · 可迁移设计包

适合借鉴暖白工作区、侧栏导航、浮动输入与底托、轻量菜单、项目辅助卡、富文本和可搜索目录。原版是网页，不替代总库中独立保存的Android Kimi。

当前对声明范围为 **ready**：62项tokens、17类组件、7种布局、10组交互、7组动效。正常登录后的代表页面已补齐；16项新增参考检查及首轮22项检查有各自记录。原版全部后台业务不属于本次设计验收。

## 给AI的入口

读取 design-package.json → DESIGN_SPEC.md → 所需模块 → ADAPTATION_GUIDE.md；登录后部分另见 AUTHENTICATED_DESIGN.md。优先直接使用assets/reusable、reference/source-icons.js和可用组件样式；保留目标核心业务、数据和技术栈，按UI逻辑轻量增删功能与文案，记录D适配。字体沿用实测系统栈；没有把未出现的字体文件或图表库标成已复用。

## 可运行参考

在本目录执行 `npm start`，打开 http://127.0.0.1:4173/ 。零第三方运行依赖，不请求Kimi后台。

- index.html：浏览入口。
- reference/index.html?view=source：主页组件参考；view=transfer为Reading Room。
- reference/workspace.html：集合、笔记和发现阅读，含真实本地保存、取消、重命名、添加和搜索。
- reference/components.html、reference/gallery.html：组件状态与媒体布局。
- comparison/index.html、comparison/authenticated.html：首轮与登录后对照。

公开图包含自建中性研究内容；新品牌产品应替换Kimi字标。文件夹PNG中的浅色Kimi水印保留原貌，也可选择同包无品牌Folder SVG。

## 范围与证据

2026-10-20采集，网页release标记c42d8c1。首轮1440×1000/DPR1；登录后1455×878/DPR1.75；通道分次采集。A实际观察、B静态声明、C推断、D目标选择分别记录。动作接口为D，不是原App API。

未触发真实网络故障，未点击停止验证后台取消/竞态，也未测移动OS/软键盘、第三方授权、收费和所有业务分支。主要动效采用B声明与明确D实现选择，不声称逐帧还原。

## 公开分发

已按所有者指令完成文件级审查。私人历史、凭据、浏览器profile、登录QR与完整原始DOM不分发；原件哈希与遗漏原因见PUBLICATION_REVIEW.json。公开REA文件是标明的投影，真实Evidence ID保留，未制造新运行。原始私有留存不因入库而删除；公开库也不授予上游品牌/资产的额外使用权。

[设计规范](DESIGN_SPEC.md) · [迁移指南](ADAPTATION_GUIDE.md) · [完成核查](COMPLETION_REVIEW.md) · [公开审查](PUBLICATION_REVIEW.json)
