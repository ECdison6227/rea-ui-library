# Hark Web 设计规范

本包接续历史 REA 研究，新增当前登录 App、公开站资源和独立组件参考。**字体、图标和明确声明的媒体已保存；原版全部业务和状态并未因此变成已测。** 迁移就绪状态保留 partial，具体差异可直接用于实施决策。

入口：[设计包](design-package.json)、[组件](components/catalog.json)、[tokens](tokens/tokens.json)、[布局](patterns/layouts.json)、[交互](patterns/interactions.json)、[动效](motion/catalog.json)、[资产](research-2026-10-22/assets-manifest.json)、[证据](research-2026-10-22/evidence-index.json)。

## 当前来源与研究条件

| 范围 | 身份与证据 | 条件 |
| --- | --- | --- |
| 登录 App 当前 A | main-BgE0mqJD.js；[32组运行观察](research-2026-10-22/authenticated-runtime/observations.json) | 正常复用用户已登录 Chrome，2026-10-22 15:04—15:48 UTC；1440×668 / 390×844 CSS px，DPR2，中文 |
| App 当前 B | [资源与精确源码](research-2026-10-22/current-app/README.md) | 256/256 已发现的允许 literal 静态 URL，外加 wallpaper，共257原件；计算型URL和执行可达性另论 |
| 当前公开站 | deployment dpl_43vpACitAoHemg5zATXXCP8hBYHa；[内容地图](research-2026-10-22/SITE_CONTENT_MAP.md) | 首页与19个公开页面的正文、HTML、声明和代表截图；分次采集不冒充原子快照 |
| 当前公开媒体 B | [媒体清单](research-2026-10-22/current-public-media/README.md) | 92个明确定位符，36图/56视频；编码协商响应分开留存 |
| H15 / H13 历史 | [历史审查](research-2026-10-22/historical-audit.md) | 保留原采集时间和身份；不提升为当前运行 |
| 独立参考 D | [启动与检查](research-2026-10-22/reference/README.md) | HTML/CSS/JS本地资料工作区；13项检查、8张已审图；不是原版服务 |

A 为实际原版观察，B 为原字节/静态声明，C 为推断，D 为独立实现。当前 App A 的本地索引不是 REA Evidence ID；公开站原 REA 返回的真实 ID 单独保留。账户原件在本地 private，规范不包含账户姓名、凭据、字段值、项目ID或私人聊天。研究结束时已恢复原窗口尺寸、自动主题、展开的首页，并清除研究草稿；未发送消息、购买、提交反馈或更改账户连接。

## 视觉语言与文字

聊天以紧凑正文、清楚的内容带、类型化富卡和透明材质组成。阅读层增加字号与留白；设置以分组行和独立详情承载操作。天空、玻璃表面与文字层分别控制，不能把主题当成固定背景图片。

| 角色 | 当前可实施规格 | 来源 |
| --- | --- | --- |
| 普通聊天 / 输入 | GT Standard L，15px / 19px / 400 | 输入为当前计算样式A；普通消息按当前CSS B；原字体B |
| 主导航 | GT Standard L，15px / 500；按钮70×44 | 当前 A |
| 代码卡 | GT Standard Mono，14px / 20px / 400；pre padding 0 16px 16px | 当前 A |
| 摘要阅读标题（桌面） | Hark Serif，40px / 52px / 500 | 当前 A |
| 摘要阅读标题（390px） | Hark Serif，32px / 41.6px / 500 | 当前 A |
| 阅读正文 | GT Standard L，18px / 25.2px / 400 | 两视口当前 A |
| 阅读 h2 | GT Standard L，16px / 22.4px / 700 | 当前 A |
| 设置标题 | GT Standard L，32px / 42.24px / 400 | 当前 A |
| 主题单选行文字 | GT Standard L，15px / 19.8px / 400 | 当前 A |
| 营销展示文字 | Hark Serif；保留营销作用域的400/700 face绑定 | 当前注册 CSS B，不能改写成 App 的300/500 |
| 营销正文 | GT Standard M，保留原 rem、媒体断点与 CSS | 当前 CSS B |

字体入口为 [App font-inventory](research-2026-10-22/current-app/font-inventory.json) 和 [官网19份字体](research-2026-10-22/public/resources/declared-fonts-fetched-manifest.json)。App 33条 face 声明对应21份原字体，15份与官网同字节，合并为25个不同字体字节身份。字体文件名、CSS weight 与 OS/2 weight 分开保留。Hark Serif 的原始 cmap 数为71，排除 .notdef 后有效70字符；缺直/弯 apostrophe 和中文，必须保留回退。App逐字实际平台字体仍未完整归因。

## 颜色与表面

实际主题变量见运行索引的 theme-auto / theme-dark / theme-light，原声明见 [theme-tokens](research-2026-10-22/current-app/theme-tokens.json)。浅色主文字为 rgb(29,29,31)，部分一般文字变量为黑；深色主文字为 rgb(245,245,247)。辅助文字以50% alpha区分，stroke及fill保留源alpha。

浅色用户泡白 .3、助手泡白 .98、composer白 .55、regular白 .668、solid白 .96。深色使用原对应变量，避免把黑色预混结果替换原透明值。fill/stroke是独立层，host透明不等于无填充。

连续圆角保留原几何算法及尺寸更新；普通 radius24 是有差异的 D 降级。[独立参考](research-2026-10-22/reference/README.md) 已复用原连续圆角 helper。普通消息不继承 composer 的50px模糊。当前摘要预览观测 backdrop-filter:blur(50px)、名义 radius24、padding8，并有0 8px 40px rgba(29,29,31,.12)阴影。

## 布局与组件

| 组件 | 当前代表规格 / 状态 | 边界 |
| --- | --- | --- |
| 聊天内容带 | 桌面512px；390px左右各16px | 首页展开时主区域随420px侧栏重排 |
| composer | 桌面512×48；窄屏总宽358；空态听写、有内容显示发送 | textarea宽度与尾部按钮分开；两行桌面高67，窄屏样本86，受换行影响 |
| 摘要预览 | 桌面424×约181；390px预览宽290 | 512是宿主内容带，不是摘要卡宽 |
| 摘要阅读 | 桌面容器720宽，当前1440×668时x360/y140、高504；padding48px 32px | 窄屏变成全宽阅读sheet；不是卡片内扩展 |
| 设置 | 桌面行548×56；主题单选548×59.796875，padding20 | 详情与总览的返回行为分开 |
| 全局搜索 | combobox，loading使用aria-busy；594×48输入带、626×80外壳、50px背景模糊 | role=dialog的零尺寸portal根不能当视觉面板边界 |
| 附件菜单 | dialog内“上传文件”“拍照”；Escape可退出 | 未上传、未请求摄像头权限 |
| 项目 | 独立项目路由、左侧项目入口；“新项目”进入/projects/new标题编辑 | 未发送、未提交新项目；默认标题为未命名项目 |
| 新面板引导 | 创建入口打开示例提示输入，背景模糊，提供换示例与发送 | 未实际创建面板；空文本不必然等于普通空输入模式 |
| 反馈 | Bug/其他单选、300px正文输入、附件/提交操作 | 当前空表单的提交按钮仍enabled；未执行提交验证 |
| 方案 | 独立/plans，有关闭与升级动作 | 只观察，不执行购买 |

37个组件族均有 slots/events/states 等实施接口；接口命名标D。当前运行范围不能自动覆盖某组件的全部状态。图片栈、链接、表格、文件、任务阶段等继续保留各自历史 A、当前 B 与未重跑边界；不能由同包存在某 renderer 推断该版本的全部交互已执行。

## 关键交互

- 输入真实中性多行草稿后，发送按钮出现；切到项目再回聊天，草稿完整保留。本轮最后已清空。
- 全局搜索真实经历 aria-busy=true→false，并显示唯一中性查询的无结果文案；Escape后输入被卸载。实际焦点返回的完整矩阵仍未知。
- 摘要从已有“REA UI 样例”打开独立阅读层，Escape关闭；两视口均查看了真实像素。私有源画面与规范分开。
- 系统设置为自动、跟随系统、深色、浅色四个 radio。实际切换已核对。自动先观察到浅色/白天，后观察到深色/夜间；不是同时冻结的天气对照，也不是D按固定小时切主题。
- 设置内搜索“主题”实际过滤出主题入口；账户、钱包、安全、资料、定时任务、反馈、关于、方案等只读取通用结构，不变更账户状态。
- 未观察的保存失败、晚到结果、删除/断开与权限矩阵保持未知。目标实现可用 [参考的实际失败/重试/取消](research-2026-10-22/comparison/reference-run.json)，但分类必须是D。

## 动效

摘要的源控制链绑定外层 ._2za4zx8，声明200ms easeOut，opacity0→1、blur(4px)→0，结束filter:none。[准确源码](research-2026-10-22/current-app/summary-static.json) 与 [运行逐帧](research-2026-10-22/authenticated-runtime/observations.json) 分开。实际点击后53.2ms首样本opacity0，306.1ms首次观察到1；这是离散事件相对时间，不能当拟合duration或实测FPS。内层静止不代表外层无动画。

原主题材质800ms/cubic-bezier(.45,0,.55,1)、按下120ms ease-out及其作用域保留在动效目录。3D轮播由自写rAF spring与WAAPI控制：track T=.5s、ζ=.85，rise .4/.85，bubble-enter .35/.55；dt≤.05并两次half-step。T不是固定视觉时长。投影radius=2.81S、perspective=6.4S、angle=-wrappedDistance×.4181494661921708rad。提示enter的49点采样、60ms opacity、300ms文字和exit delay100+240ms分别保留，不能混成单个时长。

当前天空的GLSL/LUT与H15同字节，但CPU引擎新增fadeHeld最多3层、rebaseFade与可选setHour crossfade。FADE_DUR=2.5s、FRAME_MIN_MS=31、LUT_AMT_DEFAULT=1均为声明，不是当前设备FPS。着色器参数、所有天气、freeze/fallback及跨设备性能仍未全测。

## 字体、图标和媒体直接复用

[公开站运行补证](research-2026-10-22/public-runtime/README.md) 已保存16张实际审阅的状态图：正常前台轮播键盘next/prev、中断反向、390px与reduce；套餐三选一/键盘焦点；FAQ真实展开/收起。正常next中心0→1、prev1→0；reduce next1→2仅采到两种card0矩阵、视频暂停在0。6.47秒reduce/focus组合下静止不作单因果归因；后台冻结样本和外部YouTube登录验证限制单独保留，采样不是FPS。

原版CDP字体报告补到具体节点：官网介绍GTStandard-LMedium（83 glyph），代表feature/prose标题HarkSerif1-Light，轮播prompt GTStandard-LRegular（95 glyph）。这是指定节点与环境的实测，不外推全部字形或App回退。

- [当前App归档](research-2026-10-22/current-app/README.md)：189 JS、46 CSS、21 WOFF2、1 HTML；main表162/162和递归literal依赖256/256均保留。74命名字形、160 SVG根/157 owner及sprite提供实际源范围和SHA，不能把root数量当独立图标数量。
- [公开媒体](research-2026-10-22/current-public-media/README.md)：92个明确定位符，36图/56视频，默认响应440,082,145 bytes；10张轮播卡、5个feature都有绑定。新增协商WebP响应按Accept/实际MIME分开，不能按URL扩展名认定格式。
- holdMs、视频编码时长、暂停/循环和CSS裁切是不同事实。源crop/hotspot为null不证明没有CSS裁切；记录中的null src也不应被猜测补成URL。
- 原字体、SVG、shader和媒体优先直接复用；工厂代码需要原依赖或薄包装。品牌与资产许可未建立，本地研究保留不授予再发布权。

## 独立参考与迁移

在 research-2026-10-22/reference 执行 npm run dev，打开127.0.0.1:4393。本地资料工作区验证了多行/IME合成保护、草稿、返回、异步保存/失败/重试/取消、搜索新内容、独立阅读、代码回复、主题与减少动画。它不调用原版服务。

[当前修正](research-2026-10-22/comparison/reference-update-current.json) 已直接接原GT L Bold700、摘要Serif20/26/500，并对齐桌面阅读层top140、手机top62/全宽/bottom0。13项检查、8图及7项本地HTTP/字体检查通过。仍保留D额外类别造成标题低28px、独立toolbar、窄屏内边距、dialog220ms、固定背景/Auto浅色等差异；不等于原版像素/所有状态等价。跨业务已运行，精确迁移就绪仍partial。

## 仍未证实的边界

当前核心设计已有可实施参数、原资产和运行参考。尚未完整重新执行当前版本的图库/链接栈/文件/回复/错误/焦点和reduced-motion矩阵；App逐字字体归因、全部天气与性能、OS键盘/触控和后台行为另列。真实支付、外部授权、安全变更与反馈提交不在本次默认验收中。

历史分析细节保留在 [归档规范](research-2026-10-22/spec-before-auth-resume.md) 和历史审查中；其中“登录待完成”“字体未收齐”“参考未制作”等是先前状态，以本规范和当前清单为准。原始证据未改写。
