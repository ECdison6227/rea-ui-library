# Hark 历史资料与既有参考实现静态审查

复核日期：2026-10-22，Asia/Shanghai。此文件只回答历史资料能复用什么、现有参考实现覆盖什么、当前在线研究还需补什么。没有启动原站或克隆、控制浏览器、调用 Hark 接口、运行原捕获脚本或改写原件。历史截图的像素复核属于历史复核，不产生新的原版 A 运行证据。

总库本轮读取提交为 d7f718d10558a32cf378b1d30bc279538fa3bf12；Hark 旧来源 revision 为 b5b49eb46494b86d56c21654ea585ad379d9b9d7。克隆本地 HEAD 为 2d61a35caa8423439516b75488812a098754087d，但此审查依据工作区文件字节，引用文件 SHA 另见 [JSON](historical-audit.json)，不假设工作区与 HEAD 完全相同。

## 两组历史来源与 D 参考

| 标记 | 身份、时间、实际范围 | 能证明的内容 |
| --- | --- | --- |
| H15 | [旧研究入口](../README.md)，采集日期 2026-10-15，目标 hark.com/chat，用户完成登录；桌面主要 1440×900，另有 390×844；各快照仍保留自身 viewport/DPR | 旧原站 A 观察、B 已捕获 CSS/脚本、C 解释。当前是否仍一致未知 |
| H13 | /Users/cjr/Desktop/hark-clone/references/raw 的 19 份手动导出。meta.exportedAt 为 2026-10-12T17:37:48.146Z 至 17:41:16.170Z，即上海 2026-10-13 01:37:48.146 至 01:41:16.170；全部 885×778、DPR 2 | 历史手动 DOM/computed 观察与 CSSOM 声明；没有 REA Evidence ID；不可新造 ID |
| D | /Users/cjr/Desktop/hark-clone，Next.js/React/CSS 的独立课堂参考，内置虚构数据和本地 mock agent | 实现选择、组件接线与静态可用性。本轮未运行，不证明原站行为或当前参考运行通过 |

H15 的 249 是总库分发成员数，包含报告、脚本和索引，不能写成「249 条原站证据」。旧 evidence/index.json 登记 246 个成员与 9 个 REA 记录；旧 verification.json 记录 34 份可读截图、36 个导出脚本、32 个源片段、544 条 token 声明和 7 个完成的代码场景步骤。249 个原登记成员本轮逐一核对大小与 SHA-256，全部一致，共 175,869,116 字节；未重生成旧索引或文件锁。

H13 去重后是 13 个 pathname、17 个完整 URL 变体，包含 /chat?home=open 与账户筛选参数。三份 chat 导出各到 3000 个 computed 节点上限。资源列表保存 URL/类型/大小/耗时，没有 JS 正文；payload 为空。不要把导出器支持 FETCH_JS 当成此批已导出全部 JS。原导出含私人内容，审查 JSON 只保存路由、时间、几何、计数和哈希，不复制 DOM、消息、账号字段或 storage 值。

## 页面覆盖地图

| 页面或设计入口 | H15 / H13 历史覆盖 | D 实际落点 | 当前在线需要确认 |
| --- | --- | --- | --- |
| 公开首页/注册首屏 | H15 不含营销页。克隆 page-brief 描述旧公开页结构、素材与 CSS，但未保存可本地复核的原站完整截图 | src/app/landing/page.tsx；signup-form.tsx；hark.css | 当前文案、入口、hero 双列/窄屏、注册表单、页脚与外链 |
| 10 个任务演示轮播 | H13 导出不含此页；旧 page-brief 有说明，无 REA ID 或完整源 bundle | feature-carousel.tsx、carousel-data.ts、hero/ 与 carousel/ 媒体 | 实际 hold、弹簧、drag/键盘、循环边界与 reduced motion |
| 五组功能说明/安全/宣言/结尾注册 | 同上，静态资料说明；原站像素匹配仍未知 | ScrollVideo 与 landing page；features/、security/ | 五组是否仍存在、顺序、响应式、媒体裁切、海报到播放切换 |
| 已登录聊天与项目线程 | H15 重点覆盖聊天、富卡、文件、暗色；H13 有 chat 与一份 project/:id 的静态导出 | HarkWorkspace、ChatView、ProjectsView、app/chat、app/project/[slug] | 当前壳、空态、消息动作、项目标题/列表/新增/返回 |
| Home 面板 | H15 聊天左侧面板有布局/材质样本；H13 的 chat?home=open 有样本，chat 节点上限限制完整度 | HomePanels、/home、Home toggle | 面板族、菜单、移动、删除/空态、新建与取消；原业务数据不复制 |
| Projects 列表与新建 | H13 只有一个项目详情；独立列表/新增完整状态未导出 | /projects、/projects/new、ProjectsView | 列表、选中、空态、内联标题、创建反馈与离开时草稿 |
| Search | H15 没有独立完整搜索研究；H13 没有该页面导出 | HarkWorkspace 内 SearchOverlay/SearchPreview；/search | 默认/查询/空/加载/失败、结果类型、打开关闭、焦点/键盘 |
| Settings 总览 | H13 有 125 个 computed 节点；H15 研究不等于 settings 已测 | SettingsExperience；/settings | 分组、条目、面板宽度、关闭/返回、账号与套餐入口 |
| Profile | H13 78 节点 | SettingsDetailContent/ProfilePage | 字段、编辑、保存/取消/失败；历史代码仅本地 state，不证明保存 |
| Scheduled Tasks | H13 29 节点，单快照 | ScheduledTasksPage | 非空/空、开关、删除确认/恢复与反馈，勿用 D 两条虚构任务证明原站 |
| Connected Accounts | H13 全部/连接/登录信息/API keys 四个 URL 变体；461/110/378/51 节点 | AccountsPage，过滤/本地移除/连接说明弹层 | 当前账户导航、连接/断开确认和错误；无需实际外部授权 |
| Connected Cards / Add | H13 各 44 节点 | WalletPage、AddCardPage | 默认、空、表单 focus/invalid/disabled、取消；不提交真实付款资料 |
| System Preferences / Theme | H13 57/46 节点；light/dark CSS 值存在，浅色运行 computed 不存在 | ThemePage 与主题持久化 | 四选项、真实 auto/source 状态、切换动效、完整浅色 |
| Language / Data | H13 150/41 节点 | LanguagePage、DataPage | 选择后生效/返回、导出请求反馈；D 只更新本地值 |
| Security | H13 42/52 节点，两份同路由快照 | SecurityPage、TwoFactorPage、SessionsPage | 两份快照不等于所有安全子页已测；确认/取消/disabled/失败 |
| Help / Report / About | 旧 authenticated-app-brief 有文字观察；不在 H13 的 19 份导出内 | FeedbackPage、AboutPage | 页面与入口、表单状态、条款/支持链接；不发送反馈 |
| Plans | H15/H13 均无完整套餐页导出；D 价格不是当前原站事实 | PlansExperience，三档与本地选择弹层 | 当前套餐结构、文字、价格、选择/取消；不发起扣费 |
| 登录/首次使用/权限/错误 | 旧库有登录截图，但本轮未像素复核；尚无完整流程研究 | SignupForm 演示入口、局部 notice/empty 状态 | 由 root 登录协作继续，观察通用引导与真实错误；克隆演示按钮不证明原认证 |

上述 D 文件均位于 /Users/cjr/Desktop/hark-clone/src/components/sites/hark，authenticated 子目录用于工作站。源码组件存在只代表可作为实施起点；并不等于逐页通过或完整还原。克隆 docs/research/hark/screenshots 中的 17 个 JPG 都以 local- 或 comment-fix- 开头，是 D 本地/批注图，不可当原站 A 配对。

## 组件、状态与可迁移规则

| 组件族 | 历史规则与状态 | 来源 | D 复用与缺口 |
| --- | --- | --- | --- |
| 工作区/导航/左右栏 | 宽屏 Home 列 420 CSS px，聊天/输入内容带 512；普通气泡常见上限424 | [设计报告](../reports/design-analysis.md)、targeted-computed-styles.json | 外壳/路由/浮层组织可薄包装。克隆响应式与导航仍待当前对照 |
| 普通消息/多行输入 | GT Standard L，15/19/400；气泡最小48、左右16、上下14.5；user/agent 填充 .3/.7，无 composer 同等 blur | H15 F01/F03，computed 与 CSS | D 的 bubble padding=14、composer min-height=51/pill/blur20，与48/连续24/blur50不同 |
| 连续圆角与材质 | 独立 fill/stroke 子层，尺寸驱动 Bézier clip-path，nominal radius24；不能只读透明 host | H15 F04，Bz0NRe01.js 字符280850–285300、原 DOM | D 使用普通 border-radius 24/25/27，没有连续路径 helper；需要包装原算法 |
| 链接栈 | 1/2/3/≥4 各 one/two/three/pile；两链接没有展开按钮；metadata 异步补完 | H15 F07；links-preview-pipeline 与 counts-expansion 源片段；18-links-two.png | D 有解析、外链和简单栈；缺 unfurl 状态、pile 展开和原几何 |
| 图片栈/图库 | 七张 pile 实际展开为 overlay 两列；锁滚动；Escape 最终 spread0、lockedfalse、expandedfalse | H15 F10；23-photo-stack-spread.png、24-collapse-final-state.json | D ImageStackCard 按最多3张 class分支，点击逐张预览；缺 pile/overlay/滚动锁与复位 |
| 摘要预览/详情 | 摘要主体打开独立 dialog；标题 Hark Serif40/52/500；正文18/25.2/400，strong600 | H15 F02/F09；15-summary-open.png、rich-computed-styles.json | D SummaryCard 是卡内 max-height 展开。可复用数据/预览骨架，不能作为原详情行为 |
| 代码卡/回复 | 实际展开→收起、回复引用→Escape退出；代码 GT Standard Mono14/20/400；静态溢出阈值+1 | H15 F08，成功 REA 场景；card-code 与 reply 原片段 | D 有 disclosure与reply回调，但永远提供展开、系统等宽、回复变成纯文本草稿，缺独立reply状态 |
| 文本模板 | 原静态 pre 保留格式、溢出展开、复制/回复 | H15 card-text-template 源片段 B | D 是p+复制，成功1800ms。可作D起点，格式/菜单/错误仍缺 |
| 表格/商品/航班/订单 | 原 table model有desc→asc→null、grid/scroll、CSV/HTML/Markdown；商品/航班可能由table归一化；订单/declined仅静态 | H15 F07/F11/F12；table-card-model；主报告 | 当前样例三次列头点击未排序。D TableCard只有显示，无排序/菜单/CSV；不是原表格功能等价 |
| 文件查看器 | CSV加载后出现列字母/行号/下载；B分发含image/video/audio/pdf/docx/pptx/spreadsheet/delimited/markdown/html/code/text/unsupported | H15 F11/F18；file-viewer-dispatch、26-file-preview-loaded.png | D FilePreview有加载/失败、AbortController、CSV、图片、PDF/HTML、文本和PPTX文字解析；本轮未运行 |
| 网站artifact/活动计划/Stop | 独立层；网站依赖project/tool身份；phases与工具Stop确认/乐观取消/回滚 B；非专用chat todo/plan协议 | H15 F18；website-artifact-preview、activity-phases、stop-tool-optimistic | D WebsiteCard与本地面板可用；原Stop/phase/取消晚到逻辑未接线 |
| 空/加载/错误/禁用/选择/焦点 | H13提供一些静态设置/主题选中样本；H15证明卡片最终态；其他通用状态覆盖不完整 | 原 computed与交互文件，按实例解释 | D有空态chips、pending dots、notice、radio/switch、删除确认。其时间、外观、保存逻辑均D，不补写原站verified |

本轮实际查看 H15 的 [15-summary-open.png](../evidence/screenshots/15-summary-open.png) 与 [27-mobile-chat.png](../evidence/screenshots/27-mobile-chat.png) 像素。前者确认独立居中阅读表面与大衬线标题；后者确认窄屏三项顶部导航、两侧16px的输入条、不同宽度的气泡。像素与历史样式一起使用；截图不能证明事件内部逻辑或具体字符的实际字体回退。

## 动效：B 原参数与 D 选择分开

| 项目 | 历史 B/旧 A | 当前 D |
| --- | --- | --- |
| 主题/material | --mode-dur .8s；--mode-ease cubic-bezier(.45,0,.55,1)，来自捕获CSS；完整主题切换未计时 | 多处独立background/opacity过渡；不是原主题时轴 |
| 按下/卡片 | 常见 scale .97、.12s ease-out；链接栈spring .42s、settle .26s、perspective900，保留作用域 | 360/500/580/650ms 的独立卡片过渡，源绑定未知 |
| 栈拖动/翻牌 | B短点击位移≤6px；位移≥.55或释放≥600px/s翻动；长按默认290ms；resize延迟120ms；reduced-motion禁翻牌 | ImageStack不支持该手势；不能以D动画填充历史观察 |
| 消息出现 | 旧100ms DOM采样：富卡53.41–54.60s成批出现；短正文首次可见即完整82字符；网络/UI零点不同 | hark-message-appear 430ms cubic-bezier(.2,.8,.2,1)，pending dots1.2s和.15/.3s错峰，均D |
| 滚动 | B near-bottom≤70px；旧A出现增长追随与上滚入口；全部detach/paused未测 | D按钮阈值150px，send finally强制到底；与原滚动锁不同 |
| 工具文本 reveal | B 90chars/s、reveal30、dt上限100ms；caller是工具活动，不等于普通聊天速率 | mock SSE默认delta30ms；ChatView实际接非流式POST，端点存在不等于UI流式已接入 |
| 天空 | B DPR≤1.75、31ms门限、场景2.5s crossfade；reduce-motion speed .08；实际全状态时长未知 | HarkSky DPR≤1.5、40ms门限、reduce-motion静止；工作站仍CSS radial ambient |
| 浮层 | 原版关键打开/关闭像素与部分最终状态A；唯一完整曲线未建立 | 设置/套餐进入370ms cubic-bezier(.2,.8,.2,1)、退出300ms ease；Search310ms；均D |

所有数值保留 CSS px、ms/s 的单位。不同时间/视口快照不可合成「同一原子采集」。原源码定时常量不是实测FPS、网络发送频率或唯一spring拟合结论。

## 直接复用资产与代码入口

实际文件、字节、SHA、使用位置和来源限制见 [historical-audit.json](historical-audit.json)。以下克隆文件未复制进总库，消费者仅拉取总库时还拿不到这些外部路径。

| 资源 | 本地实际入口 | 复用判断 |
| --- | --- | --- |
| GT Standard L | /Users/cjr/Desktop/hark-clone/public/sites/hark/fonts/gt-standard-l-regular.woff2、gt-standard-l-medium.woff2、gt-standard-l-semibold.woff2 | 文件可直接接入；本轮只核wOF2头/长度/哈希，未解码name table或复核上游字节 |
| GT Standard M/S | 同fonts目录，M的regular/medium/semibold三份，S的regular/medium两份 | 营销文字有现成文件；工作站正文需依H15 L绑定，不因已存在M就替代L |
| Hark Serif | 同fonts目录，hark-serif-light.woff2、light-italic、medium、medium-italic | 标题/强调现成文件。Light应按历史300、Medium500注册；克隆当前400/700绑定不同 |
| GT Standard Mono | H15 B9bSKZIz.css 保留 assets.hark.com/fonts/GT-Standard-Mono-Standard-Regular.woff2 与 Medium URL | 两个审查目录都没有Mono字体实物；D代码用了系统mono。当前取到合法范围内原文件后优先复用 |
| 品牌 | public/sites/hark/brand/google.svg、apple.svg、favicon-dark.png、favicon-light.png、hark-pro-icon.webp | 本地SVG/PNG/WebP可用；Google/Apple的旧来源只有文字记录。品牌使用边界沿用原记录 |
| 通用图标 | D各tsx import lucide-react；设置服务商多为字母占位；HarkMark内联SVG在website-card.tsx | 未建立精选原站控件图标包。原DOM有SVG不能自动等于已整理/已核对的组件资产；root可直接采集当前真实SVG |
| 图表 | D message-cards.tsx/TableCard、file-preview.tsx/Spreadsheet | 它们是表格不是图表。已审报告、32片段和D组件范围内未找到可直接消费的独立chart renderer；不推断Hark全站没有图表 |
| 天空原件 | ../evidence/scripts/wallpaper_core.html、wallpaper-vertex.glsl、wallpaper-fragment.glsl；LUT烘焙图嵌在HTML中 | 原完整shader可复用，避免默认重画。完整HTML会装配天气/时间等输入，接入需冻结输入或薄包装；本轮未执行 |
| 天空/主题宿主 | 原DvrONiVz.js、Dl79dO-7.js 与 wallpaper-source-excerpts.json | 保留真实路径、字符偏移与B控制链；捕获module树缺失依赖/原浏览器绑定不可默认独立运行 |
| 连续圆角 | 原Bz0NRe01.js字符280850–285300；源DOM的clip-path与fill/stroke | 优先提取/包装原参数算法。D普通radius不能宣称几何等价 |
| 营销媒体 | 克隆public/sites/hark/hero/cover.mp4与poster；10个prompt-*.webm；carousel/*-poster.png；features/五个poster+webm；security/band.png和illustration.png | 16个视频本地已存，媒体字节/尺寸可核。精确下载URL映射脚本缺失，旧来源采集时间未知，需当前身份补证 |
| 现成组件/布局 | HarkWorkspace、ChatView、HomePanels、ProjectsView、SettingsExperience、SettingsDetailContent、FilePreview、FeatureCarousel、ScrollVideo及对应CSS | 优先薄包装这些D实现，逐项修本文差异；原业务数据、接口和品牌由消费者任务决定 |

原H15有GT L300/400/500/600/700及italic、Mono400/500、Serif300/500声明；克隆仅12份字体，不能称完整原字体集合。src/app/globals.css还把GT L/M Semibold注册为700，与H15 L600不同。Hark Serif Medium注册700会影响40/52/500详情角色的face匹配；这是D绑定问题，不是原字体文件错误的证明。

## 已发现的资料矛盾与实施问题

1. 克隆 README/component-extraction 仍夹杂已废止的420/475等字重、自动主题7–18、旧shader/浅色「缺口关闭」文字。fidelity-diff 自己也记录「浅色computed未捕获」。依据原采样和当前源码逐项判断，不能按旧结论整体继承。
2. H15完整原源码明确auto依sky-place.day/skyAppearance，system依prefers-color-scheme。H13 CSS与storage键的静态存在不支持「只有手动+系统、没有auto」。克隆7–18是D有界替代，不能标原算法。
3. D theme-tokens.css的两个65条主题块与parsed版本逐变量一致，但末尾新增.hark-app默认--text/--text-primary/--bg。与.theme-light同权重且更晚，会覆盖这三项浅色值；此为静态级联判断，尚未运行验证。
4. D摘要内展开、图片逐张预览、普通radius、composer51/pill/blur20、窄屏正文14，与H15已核的详情/图栈/连续圆角/composer48/blur50/窄屏15存在具体差异。
5. docs/component-extraction 把PPTX写成未接线，而当前FilePreview已有JSZip与文字解析。该实现仍不等于原PPTX布局渲染；以源码存在和本轮未运行分别记录。
6. asset-map.md链接scripts/download-hark-assets.py，但现克隆找不到该文件。page-brief也引用缺失design-qa.md；旧公开页源HTML/CSS/bundle没有完整保存在现references目录。不能用文字描述代替资产字节来源链或当前原版对照。
7. D本地搜索只搜snapshot.messages；ChatView新消息存在内部localMessages。新消息搜索可见性、草稿跨路由、返回持久化和失焦等通用逻辑尚需参考运行验证，不能因组件存在宣称已通过。

## 给 root 的当前在线补证清单

| 优先级 | 独立问题 | 必要证据/预期收敛 |
| --- | --- | --- |
| 核心 | 当前站点是否沿用H15/H13结构与资产 | 当前正常登录后的壳/导航/代表聊天/设置，记录版本或loaded资源hash；历史只作为比较基线 |
| 核心 | 主要文字角色、font-face与真实资产 | L400/500/600、Serif300/500、Mono400/500的当前CSS与本地文件；记录实际节点computed，CJK逐字回退可选深入 |
| 核心 | 浅色与auto/system主题 | 同页面同内容两主题像素/computed；四选项与切换状态、时序；不可用静态light变量收口 |
| 核心 | 尚缺页面族 | 公开首页全内容、Home面板、Projects列表/新建、Search、Plans、Help；每类代表状态，不因菜单多就全后端测试 |
| 核心 | 摘要/代码/链接/图片/表格的当前核心状态 | default/expanded/closed/reply/cancel/loading/error；成功与未触发分开；表格是否可排序须重新实测 |
| 核心 | 关键动效的执行绑定 | 当前卡片与浮层打开关闭/中断/reduced-motion、有时间信息的捕获；B声明和A计时分开 |
| 条件 | 窄屏与键盘/指针/焦点 | 390×844基线重比，导航与composer几何；软键盘/safe-area/触控/焦点只在实际条件下报告 |
| 条件 | 图标/可能新增图表 | 保存真实SVG、用途与变体；发现chart才追renderer/config，不默认重画 |
| 条件 | 流式与滚动的可见规则 | 少量中性研究消息；判断累计正文/批卡/状态与滚动detach，不把DOM逐字符span当token协议 |
| 可选 | 天气细支、精确性能、完整服务端schema、真实外部授权/订单/扣费 | 只在必要解释设计时深入；不能用未执行分支或Dmock补造事实 |

登录、当前浏览器唯一控制和在线补证由 root 负责。此静态子任务没有认证等待或受阻收口，也不替 root 宣布「只研究公开页」或全部研究完成。

## 原 REA 身份入口

完整来源路径与限制见 [evidence/index.json](../evidence/index.json)。下列9个ID均来自2026-10-15旧记录，本轮原样保留：

| 原操作 | 原 Evidence ID |
| --- | --- |
| list_browser_targets | ev_be70f606cc60ca800cf007399c0d5305fc897974de7af445c217e4a64ebfa62c |
| inspect_web_page（初始） | ev_ef1be7aaa0463c2529db64a2aad1099a530287426faeb9e8990f7932829b8949 |
| inspect_web_page（脚本页） | ev_4d515902647d4a1d0224c3da682c34a39f9ed44ca7a978ac725a13eddf73e13f |
| export_web_scripts（首次） | ev_57655eb906262fce9f3ddf2b21115479360aa3799dd97b63be0cd230723879de |
| capture_browser_scenario（宽泛选择器失败） | ev_993675e16c6b9235e47ceb6c9760b35e0c8832b261b22304154f18cfa2919aea |
| export_web_scripts（完整保留范围） | ev_f59cba60a4e30603b5f93e546735322e749383df1dc84d0c3d9c761afd214537 |
| capture_browser_scenario（精确选择器7步完成） | ev_e41e6662d8c8987542a7ab16b5c9b52ff8e19aee54fc3606814a6da3f3dc3c8c |
| capture_web_screenshot（最终） | ev_0e3b17452fde3a474f7e143f63f792ca0bba5e63d3ecae5e3d873f47c5bbf670 |
| inspect_web_page（最终） | ev_57579bb8e74f51e34a3ba48715282bfd30c536cd5d4a42b872bd837aa4f3817b |

原REA记录有attach前不可见、policy_filtered/attach_limited、浏览器平台身份未知、源映射未取得和未捕获依赖等限制。CDP侧存的样式/交互/下载文件另按原路径引用；不能自动把它们每一份归为某个REA操作的原始返回。原报告使用上海2026-10-15的日期；REA页面窗口另保留UTC时间：初始2026-10-14T16:51:20.592Z–16:51:22.433Z、资源页16:52:40.588Z–16:52:42.901Z、最终页17:42:41.463Z–17:42:42.413Z；最终截图captured_at为17:42:43.924Z。换算上海分别为10月15日00:51、00:52、01:42，不能把UTC日期写成另一次10月14日研究。JSON按原Evidence ID保存UTC原值、上海换算和字段出处；没有该字段的记录仍为日期级，不拿文件mtime编造时间。

## 审查结论

历史研究足以复用暗色排版、透明材质、连续圆角、512/424内容尺度、富卡状态和完整天空源码；H13补充设置与项目静态形态。现有D克隆提供现成React组件、字体和媒体，但仍需要具体校正和当前原版对照。原版研究覆盖是局部，迁移准备是partial；此静态审查任务已完成。两份输出只新增在research-2026-10-22，不改历史证据、原文件锁或既有克隆。

