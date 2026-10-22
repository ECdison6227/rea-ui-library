# Hark设计迁移接入指南

当前资源入口：App21份原字体、74命名字形、257份静态原件及当前天空在 research-2026-10-22/current-app/；官网19份字体与92媒体定位符/107响应文件已保存。先按各自CSS scope直接接原件，保留实际MIME/Accept协商。

research-2026-10-22/reference/ 是已运行的不同业务资料工作区，13项检查和8图收据在 research-2026-10-22/comparison/reference-run.json。它证明D草稿/返回/保存取消/重试/搜索/阅读/主题逻辑；具体视觉差异见 research-2026-10-22/comparison/current-app.md。下文原始合同保留，实际运行状态以当前设计包为准。

当前源研究是partial，用户本次要求继续最深原版UI与完整资产采集；本指南是D消费合同，不能证明原版研究已完成。先读design-package.json/DESIGN_SPEC.md，再按实际页面读取tokens/components/layouts/interactions/motion/assets/evidence。记录拉取的实际commit，保留目标业务、数据、品牌、技术栈和接口。

## 先取原件与作用域

| 资源/规则 | 直接入口 | 接入方式与边界 |
| --- | --- | --- |
| 当前19face字体 | research-2026-10-22/assets-manifest.json；public/resources/*.woff2；static-analysis.fontAssets | 原文件原样接入；public Hark Serif400/700、M/L semibold700。尚未下载项保持缺口，许可未建立只在授权研究范围使用 |
| H15聊天字体 | evidence/styles/B9bSKZIz.css及assets中H15URL | GT L正文15/19/400、semibold600；Hark Serif300/500；Mono14/20。不要用营销绑定覆写历史App。若目标命名为hark-app-serif等是D别名 |
| 连续圆角 | Bz0NRe01.js字符280850—285300；fill/stroke CSS | 优先提取算法到原栈中的薄helper，ResizeObserver按border-box更新；普通radius24是D降级，应保留具体差异 |
| 天空 | evidence/scripts/wallpaper_core.html与两个GLSL；当前public wrapper源码 | 原shader可复用，冻结日期/天气/地点作为D目标输入；不要将外部API当独立运行所必需。当前core同字节与runtime未知继续记录 |
| Logo/provider/play SVG | 当前static-analysis.reuseEntrypoints.icons与原JS factory | 提取实际原path并另存派生SHA；不默认重画或用lucide替代。未整理的控件包待root完整抓取 |
| 图片/视频 | assets精确sourceURL、public page media与carousel.metadata | 先原件/变体/比例/crop/framing/hold核对；原视频object-fit与裁切配置薄包装，不用近似视频补证 |
| 3D轮播 | public/resources/15bfdjo3hjyv9.js byte350829—363521；motion catalogue | 原rAF spring/WAAPI/投影优先迁移；49点linear采样与opacity完成后取消链保留。D600ms仅在可移植障碍下替代并注明 |
| 富卡/Markdown/viewer | evidence/scripts/snippets/*.raw.js及meta；components目录 | 沿用目标既有Markdown/viewer/表格栈；薄适配类型与角色。发布factory本身不是可直接import的React源码 |
| 既有D参考 | 外部/Users/cjr/Desktop/hark-clone；historical-audit映射 | 本包不包含该工程；本轮未运行也不修改。可作为另次授权实施起点，逐项修字体/浅色级联/摘要/图栈/composer差异 |

保留源单位：App CSS px不同比例缩成rem；marketing rem由源root函数决定，auth-unit又独立。目标原有全局字体、heading、button样式可能覆盖来源作用域，应通过显式容器/语义token隔离，避免把所有body改成一个face。

## D组件适配合同

| 原设计关系 | 目标接口D | 必须实际保留的行为 |
| --- | --- | --- |
| 左辅列+512内容带 | Workspace(activeContext,panels,children,composer) | 主任务/返回、辅列开关、当前位置与草稿保留 |
| 15/19普通气泡+类型富卡 | Message(body,role,status,actions) + Renderer(kind,payload) | 内容与actions都可用；不把通用blur50加到气泡 |
| 多行输入+引用 | Composer(draft,attachments,replyTarget,onSubmit) | IME、pending防重复、失败保留、Escape去引用不清draft |
| 摘要预览+独立阅读层 | Summary(preview,onOpen) + ReadingDialog(body,onClose) | 独立层、标题正文角色、局部滚动、关闭恢复trigger |
| pile/网格/复位 | AssetStack(items,count,expanded,onToggle) | 数量分支、metadata异步状态、关闭解除scroll lock |
| 表格+菜单/导出 | DataTable(columns,rows,sort,onSort,onExport) | 实际行顺序和CSV数据验证；源H15示例没有排序成功不隐去 |
| 项目/设置编辑 | Editor(committed,draft,save,cancel) | 真实保存、错误保留、取消恢复、离开未保存选择 |
| SearchOverlay | Search(query,results,status,openResult) | 真索引包含新消息，200ms debounce、最新请求胜出、关闭焦点返回 |
| 4种主题 | ThemePreference(mode,system,autoSource) | light/dark/system/auto区分，auto缺来源时D选择说明，不叫源7–18算法 |
| 认证两种表单 | Auth(variant,provider,emailOrPhone,status) | App空Continue disabled；底部空submit alert；真实目标认证回调 |
| 3D功能演示 | DemoCarousel(slides,metadata,visibility,reduce) | 键盘/drag/pause、crop/framing、source hold与动画取消；D降级保留说明 |

只添加对目标有用、能执行的状态或小功能。重复的新建/搜索/返回可合并；缺少必要的保存取消/错误重试应补齐。每次变化记录UI逻辑理由、原核心数据路径和验证，不能展示有文案却不执行的按钮。

## D跨业务切片计划：资料研究工作区

这是待实施的不同领域验证合同，尚未运行；validation.crossDomain=partial。将Hark业务样本改为一套中性资料项目管理，同时保持源的层级关系：

| 切片内容 | 来源组件/布局 | 新内容与行为D |
| --- | --- | --- |
| 主工作区 | chat-desktop/home-panels | 左“资料项目”列、右资料问答/摘要与底部输入；目标真实资料ID |
| 项目目录 | project-directory/project-row | 创建/重命名研究项目，返回保留查询/选中；保存失败可恢复 |
| 摘要阅读 | summary-card/reading-dialog | 点击资料摘要进入独立阅读层；40/52与18/25.2角色保持 |
| 附件 | file-preview/table-card | 真实CSV/文本资料加载与下载；内部横向滚动不让页面溢出 |
| 检索 | search | 搜索标题/标签/内容；打开定位到真实资料 |
| 设置 | settings-form/setting-control | 显示密度/通知/主题，保存取消与四种主题选择 |

实施时验证两种典型画布1440×900和390×844、同一主题/语言/内容、长标题/空/加载/错误/取消/retry/reduced-motion。至少完成创建项目→输入/引用→打开摘要→关闭→搜索定位→设置保存/取消的核心路径。D目标运行与原版A分别记录，不用一个相似度分数代替字形、几何、状态和动效差异。

## 原版对照与验收

先建立source A与reference D配对：版本/实际commit、theme、language、viewport CSS px、DPR、content、scroll、active state、media clock相同。逐项记录font-face/字重/字形回退、宽度/padding/行高、连续角曲线、fill/stroke/blur、素材裁切、动作状态/返回、触发/时序/中断/reduce。当前还没有新参考或跨业务运行A/D配对；源未知不因D补齐变verified。

本包局部格式/引用/SHA检查可运行：

    node skills/rea-design-transfer/scripts/design-package.mjs validate platforms/web/hark/design-package.json

这仅核互操作内容和字节；原版及目标运行仍需浏览器/像素与行为验证。root继续采集时不要运行study:lock/总库全测试去接受尚在写入的证据，待新增范围稳定且明确审查后处理。
