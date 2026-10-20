# Kimi Web 设计规范

来源：[Kimi英文公开首页](https://www.kimi.com/en)及同站Inspiration，2026-10-20。源平台web；Windows Chrome155；首轮DPR1，登录后DPR1.75。状态 **ready**：公开界面与登录后代表设计已有规格、资源和参考；未测业务边界单列。

## 1. 证据规则与覆盖

A=现场运行；B=捕获源码/DOM资产声明；C=推断；D=独立参考与迁移选择。记录ID只是本包命名；只有REA真实返回的长ev_ ID叫REA Evidence ID。

| 范围 | 来源覆盖 | 可实施性 |
|---|---|---|
| 主页导航、输入、底托、快捷入口 | A + B | 主要几何已同画布配对 |
| 模式菜单、工具面板、账号菜单 | A + B，部分状态 | 组件参考可运行；键盘补充D |
| 登录弹层外观与Esc保留草稿 | A | 可复用结构，QR与认证不迁移 |
| 系统暗色 | A色值/截图 | 主题tokens与参考可用 |
| Inspiration分类/卡片/hover | A | 布局参考可用，媒体/断点D |
| 390/1024/1440视口 | A桌面UA | 窄屏规则可用，移动OS未测 |
| 声明动效 | B，运行时长未测 | 可实施，不能称实测还原 |
| 会话正文/生成控件/项目/插件 | A，登录后已补证 | 代表布局参考与复用资源见新增章节；网络错误仍未知 |

REA真实场景：`ev_637d252dde4533c9a06ce5a42f4765227077efc55808e5314b29c02d49c93b56`；完整保留evidence/raw/rea-desktop.json。独立采集提供精确区间，REA缺精确时钟则用宽区间，不伪造时刻。PNG、DOM与样式是顺序采集，非原子快照。

## 2. 设计原则

1. **让输入成为视觉中心**：可见操作收在768px列内，大块留白导向输入。
2. **通过表面层级形成结构**：暖白底/侧栏、白画布、轻灰底托、白浮层；深色只用于高优先动作。
3. **形状有层次**：输入24、浮层16、行10、表单12、标签pill；不把所有组件统一圆角。
4. **颜色用于小范围反馈**：蓝色勾选、引导链接、工具点；默认主体维持黑灰。
5. **显示必要的动作**：常驻基本动作；hover/focus才展现卡片次动作；窄屏保留横向浏览。

## 3. 色彩与表面

| 角色 | 亮色 | 暗色 | 证据 |
|---|---|---|---|
| 页面底色与侧栏 | #fbfaf9 | #181817 | A token ground |
| 主画布 | #fff | #121212 | A token paper |
| 次级容器 | #f5f5f5 | #1f1f1f | A token surface |
| 带透明度底托 | #f5f5f5e6 | #1f1f1fe6 | A token underlay |
| 浮层表面 | #fff | #292929 | A token popover |
| 主文字 | #000000e6 | #ffffffd6 | A token text-primary |
| 次文字 | #0009 | #ffffff8f | A token text-secondary |
| 辅助文字 | #00000073 | #ffffff6b | A token text-tertiary |
| 禁用文字 | #0000004d | #ffffff42 | A token text-disabled |
| 轻量悬停填充 | #00000008 | #ffffff0d | A token fill-hover |
| 选中填充 | #0000000d | #ffffff1a | A token fill-selected |
| 细分隔与描边 | #00000021 | #ffffff1f | A token separator |
| 局部强调与勾选 | #1783ff | #1a88ff | A token accent |
| 登录遮罩 | #0009 | #0009 | A token mask |

alpha是源码值，不能把合成后的灰色当成跨背景不变的颜色。输入边框亮色默认#0000000f，focus/hover变#00000017；阴影0 5px 16px -4px #00000012，focus叠加0 4px 12px #00000008。浮层是0.5px inset描边 + 0 4px 16px #0000001a。

## 4. 排版与图标

UI字体栈：`-apple-system, BlinkMacSystemFont, "Segoe UI", system-ui, Roboto, "Noto Sans", Ubuntu, Cantarell, "Helvetica Neue", sans-serif, Arial, "PingFang SC", "Source Han Sans SC", "Microsoft YaHei UI", "Microsoft YaHei", "Noto Sans CJK SC"`。

- 输入正文16/24/400；导航、按钮、菜单主标签14/20/400；说明/分区12/18/400。
- 登录表单列标题16/24/400；界面标题角色18/26/500由声明支持；正文不要套用品牌字标的粗大字形。
- CDP有效字形样本：模式文字Segoe UI，顶栏登录Segoe UI Semibold；编辑器/导航的样本为空，不能断言它们的逐字回退。
- @font-face中存在但未加载的MiSans VF、Geist Mono、KaTeX不证明用于主体UI。字体文件不随研究分发；目标用系统栈。
- 原站常用可见图标18px，品牌和动画SVG保留原件。当前优先接入16个原始SVG；缺项的D图标使用18px盒、1.8stroke，不把D路径冒充原站。原SVG还保留局部0.2s~1s的声明动画，未测运行触发时轴。

## 5. 几何与页面组合

亮色1440×1000基线：侧栏240宽；主画布margin:6px 6px 6px 240px，12圆角，1px极弱边框。Windows滚动条预留15px，使有效内容中心x829.5。输入矩形x445.5/y420/w768/h114。

首页列内关系：

| 元素 | 规格 |
|---|---|
| 顶栏 | 实际52px；右侧32px高按钮，间距8 |
| 标识盒 | 400×82，距输入32 |
| 输入容器 | 最大768，初高114，角24，边1 |
| 输入文字区 | 12px 16px 0内距，最小48，max-height170 |
| 下方动作行 | 36高，上下margin8，左右padding8；窄屏4 |
| 发送 | 36×36，28px箭头盒，padding4 |
| 底托 | max728、每侧内收20；margin-top:-36；padding42px 8px 6px；底角24 |
| 快捷入口 | 36高，图标18，圆角20，0.5px细边，桌面gap12；底托后32 |
| 弹层 | 观察666.5×508，外角20、内卡16、顶栏72；表单44高12角 |
| 画廊 | 顶栏52 + 分类52；左右16、卡间16；观察5列/263宽、卡角16 |

这些是有条件的规格。666.5弹层宽度、263列宽不是全产品常量。目标按业务文本与视口适配。

## 6. 组件地图与状态

### WorkspaceNavigation · cmp-navigation

插槽：brand / primaryAction / navigationItems / sections / history / account。

- expanded [A]：240px暖白侧栏；每行36px，左右8px外距；主动作42px。
- collapsed [A]：侧栏移出左边，主画布左距变6px。
- narrow-open [A]：390px视口时侧栏覆盖主内容。
- focus-declared [B]：1px inset 主色焦点描边，轻灰底。

未知：历史批量管理、深层项目树和完整触摸行为未测；登录项目代表样本已补。

### LayeredComposer · cmp-composer

插槽：editor / leadingTools / modeSelector / submit / contextUnderlay。

- empty [A]：114px高白色容器；占位符16/24，发送按钮黑色15% alpha。
- filled [A]：有文字时发送按钮黑色90%左右，白箭头。
- focus [A]：更深边框、叠加0 4px 12px阴影。
- dark [A]：容器#1f1f1f；发送使用白84% alpha。
- loading-target [D]：目标可显示36px停止/加载按钮，原站未证实。

未知：真实生成中的停止、晚到结果、网络错误与取消行为。

### CircularPrimaryAction · cmp-submit

插槽：icon。

- disabled [A]：36×36圆形、浅灰底、白箭头。
- enabled [A]：黑90% alpha底，箭头28px盒。
- dark-enabled [A]：白84% alpha底，深色箭头。

未知：原站发送结果未执行。

### ContextUnderlay · cmp-context

插槽：project / plugins。

- default [A]：宽度比输入小40px；margin-top:-36px；padding:42px 8px 6px。
- tools-open [A]：工具菜单占据输入下方空间，底托不可见。

未知：跨会话项目选择的持久化；私有项目选择器原图不分发。

### IconLabelPill · cmp-shortcut

插槽：icon / label。

- default [A]：36高，20圆角，0.5px描边，18px图标。
- hover-declared [B]：填充Fills-F1；文字变Primary。
- pressed-declared [B]：Fills-F1-active。
- focus-declared [B]：2px outline offset2或父链接内描边。
- disabled-declared [B]：Quaternary文字，cursor:not-allowed。

范围：仅代表已列状态，未声称所有平台/业务分支。

### DescribedOptionMenu · cmp-option-menu

插槽：guide / options / check / divider / secondaryChoice。

- open [A]：290px宽，8px内边距，16圆角，标题14/20，说明12/18。
- selected [A]：所选行右侧蓝勾；文字不使用大面积蓝色背景。
- submenu [A]：hover Thinking effort 显示Standard/High子菜单。
- escape-submenu [A]：第一次Esc关闭子菜单，父菜单仍保留。

未知：完整原版键盘选项遍历、点击模式后的跨会话持久化。

### WideToolPanel · cmp-tools

插槽：toolRows / icons / descriptions。

- open [A]：输入下方8px，宽度与768px输入框一致；行标题与描述横排。
- closed [A]：Esc收起，底托和快捷入口恢复。

未知：上传、连接器授权和子面板内部状态。

### FramedModal · cmp-modal

插槽：header / brand / body / columns / fields / footer。

- open [A]：60%黑遮罩；666.5×508px居中外壳；72px顶栏；白色内容卡。
- disabled-form [A]：44px表单；未填写时登录按钮灰色。
- dismissed [A]：Esc关闭；输入草稿保留。

未知：原版焦点陷阱、遮罩关闭、表单错误、验证码与窄屏原版登录布局。

### AccountActionMenu · cmp-account-menu

插槽：rows / icons / trailingChevron。

- open [A]：侧栏底部上方，224px宽，8px内距，4个36px菜单行。

未知：首次进入自动展开条件与跨会话策略。

### MediaCard · cmp-gallery

插槽：media / typeBadge / hoverAction。

- default [A]：16px圆角，轻描边，媒体原比例；左上32px类型圆徽。
- hover [A]：左上类型名扩展、右上Remix按钮出现。

未知：详情和真实Remix结果、全触摸替代交互。

### PillCategoryTab · cmp-category-tab

插槽：label。

- default [A]：透明底，36px高，水平padding12px。
- selected [A]：灰3%填充，100px圆角；选中分类改变URL query和卡片内容。

未知：真实浏览器后退是否保持瀑布流滚动位置。

### TargetFeedback · cmp-feedback-target

插槽：message / action / progress。

- empty [D]：浅灰容器内居中提示下一步。
- success [D]：底部深灰Toast，role=status。
- loading [D]：16px spinner，0.8s线性循环，reduced-motion停用长动画。
- invalid [D]：44px字段搭配红边与贴近字段错误文案。

未知：原站网络失败/重试；空项目、生成过渡与插件无结果已另有A。


整理后的事件和slots均为D迁移接口。详细tokenRefs、几何、事件和实现选择见components/catalog.json。

## 7. 通用交互规则

- 输入有字时启用发送；空输入保持浅灰禁用。已完成两条中性UI样本提交；正文与生成状态另有A证据。
- 工具面板打开时占据底托/快捷区；Esc后恢复。模式菜单使用蓝勾，说明排在标题下；hover effort显示子选项，首次Esc只关子菜单。
- 未登录点击项目或插件入口出现登录门槛。Esc关闭后草稿保留。对请求是否取消没有证据。
- 点击Design分类改变query并更新卡片/输入上下文；后退恢复和请求竞态未测。
- D参考补方向键/Enter选择、focus-visible、菜单返回焦点、aria-modal、Tab边界和live status。这些没有被写成原站已测能力。
- D新业务创建只写本地localStorage；数据保留/清空、pending和error要接入目标真实业务。

## 8. 动效

| 触发 | 属性 | 声明时间 | 声明曲线 |
|---|---|---:|---|
| 侧栏折叠/展开 | transform / 主区margin | 300ms | ease-in-out |
| 菜单进入 | opacity + scale .85→1 | 150ms | cubic-bezier(0,0,.2,1) |
| 菜单退出 | opacity + scale | 150ms | cubic-bezier(.4,0,1,1) |
| 输入focus/hover | border-color / shadow | 300ms | ease |
| 普通控件反馈 | fill / border / text | 150ms | ease |
| 快捷入口显现 | opacity | 240ms | ease-out |
| 快捷入口滑入 | translateY 14→overshoot -4→0 | 320ms | cubic-bezier(.49,.45,.8,1.18) |

快捷项index×45ms延迟。以上为B声明，没有原版录屏逐帧测量。源reduced-motion分支移除或缩短动画；参考D统一极短时长和零延迟。运行中断、反复触发和唯一spring未归因；目标新状态覆盖旧动画。

## 9. 适配与可访问性

390×844桌面UA样本：主输入x19/w337；底托w297；快捷入口横向滚动，gap8，上距24；侧栏覆盖打开。568px窄屏分支和840px布局分支来自CSS。原版移动UA/触摸、safe-area、大字和软键盘未测，目标必须自行检查。

原版DOM有部分role、aria-haspopup/expanded；这不等于读屏已验证。D参考使用真正button/textarea、方向键选择、Escape、2px焦点框、Tab约束与状态播报。目标建议保持至少44px触摸命中范围，同时保留36px可见控件盒（D适配）。低对比辅助字适合弱信息；目标关键说明须核对对比度，不能盲用Quaternary。

## 10. 资产、验证与未完成项

资产入口assets/USAGE.md；文件SHA见assets/manifest.json。原图与D截图分目录；comparison/geometry.json提供同画布x/y/w/h差异。参考执行22项通过，跨业务阅读清单和画廊可运行。

登录后核心代表类型已补充；未测网络错误/取消与移动平台保留边界。恢复方式见evidence/RESUME.md。未研究的AI算法/收费/同步协议不影响当前视觉模块使用。

原站所有原件默认private；公开版经过文件级分发审查，私人原件不随库发布。新业务需替换Kimi字标、账号/隐私条款、备案和图库内容。

## 11. 登录后补充


用户于本轮完成正常登录，随后补了项目、会话与插件样本。已创建一个中性项目 Design Transfer Sample、两条UI研究消息，并保存了一段中性项目说明；不导出私人历史与凭据。

环境：1455×878 CSS px / DPR1.75 / Windows Chrome；第一轮匿名样本1440×1000 / DPR1仍保留。截图、DOM与样式顺序采集，不能当原子快照。

## 新增可迁移规则

- 项目：主列约784、底部768输入；右侧独立Instructions/Files卡，20圆角；标题20/28/500，直接复用80px文件夹原件。
- 说明编辑：560px弹层，16角、24内距、灰色多行编辑；Cancel重新打开后草稿为空，Add后原卡显示说明。
- 会话：正文720列；H1 22/36/600，H2 20/32/600；表格14/22，表头600，单元格padding10px 16px 10px 0；代码独立灰标题条与Copy。
- 生成：实际观察到send-button-container loading → disabled stop → 普通禁用发送，方形停止图形；没有点击停止验证后端取消。
- 插件/技能：800px内容列、搜索+分类pill、双列卡；无匹配查询显示No matching plugins found；Skills页有上传引导和来源/用户数卡。没有安装、授权或上传。

## 直接复用

assets/reusable保存16个原始SVG、两张项目PNG、原始变量和精选组件声明；reference/source-icons.js优先返回原SVG，D路径只做缺项回退。components.css仅去除Vue data-v作用域、保留media/supports分支并略过未取得URL资源的规则，具体见css-provenance。字体直接保留实测系统栈，没有发现可导出的实际字体文件；没有声称复用了未出现的图表库。

## 小型业务适配 D

项目→阅读集合，Instructions→Reading goal，文件/会话区→阅读条目与笔记；插件Install→Add reading。新增动作真实写入原阅读清单的localStorage；重命名、保存/取消和无结果反馈可执行。省略外部授权、会员入口和AI生成，避免不属于阅读业务的控件。

参考：reference/workspace.html，三个视图Collection / Notes / Discover。复用源CSS、字体栈、SVG和插画；正文内容与业务事件为D。对照见comparison/authenticated.html；验证见authenticated-verification.json。

## 未知与边界

网络错误、停止的取消语义、请求竞态与真实移动平台仍未知；原版coverage逐项保留partial，目标替代标D。迁移ready仅指已声明的代表UI和可运行路径，不表示原产品所有功能已测。
