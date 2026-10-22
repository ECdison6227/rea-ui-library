# Hark Web 设计规范与原版取证入口

本包用于原版 UI 的深入研究、完整资产溯源和后续设计迁移，当前为 **partial**。用户最新要求是继续深挖原版字体、图标、页面和交互；现有子集、历史资料与 D 参考都不能据此宣布原版研究完成。字段覆盖与可实施选择不代表当前版源行为已验证。

固定入口为 [design-package.json](design-package.json)，组件与数值分别见 [组件](components/catalog.json)、[tokens](tokens/tokens.json)、[布局](patterns/layouts.json)、[交互](patterns/interactions.json)、[动效](motion/catalog.json)。消费者先核对所选规则的来源时期、单位、状态与 unknowns，再取 [资产清单](research-2026-10-22/assets-manifest.json) 及 [证据索引](research-2026-10-22/evidence-index.json)。

## 来源、版本与证据边界

来源身份统一为 `https://hark.com`，原版平台为 Web。当前公开网站发版标识为 `dpl_43vpACitAoHemg5zATXXCP8hBYHa`；浏览器框架为 Next.js/Turbopack，字体/排版/动画从本轮保存的注册 CSS 与 JS 取得。网站发版标识不能充当用户账户或认证身份。

| 来源时期 | 可用内容 | 时间与限制 |
| --- | --- | --- |
| 当前公开站 | 23个脚本、3份注册CSS、首页内容/资源观察、各公开路由的DOM/text/computed与截图副本 | 首页REA真实ID `ev_372b36210620b0b72e42ff99acdc1e91775e5f054040645c146c86985fd38885`；区间2026-10-22T12:24:40.071Z—12:24:43.488Z。CSS从/manifesto注册表另取，12:42:35.741Z—12:42:36.073Z，非同一原子快照 |
| H15历史登录App | 登录聊天、普通消息/输入、富卡/文件、dark材质、天空源码 | 原上海日期2026-10-15，原URL/chat；保存原REA ID和各文件哈希。不提升为2026-10-22当前运行 |
| H13历史手动导出 | 19份导出，13个pathname/17个URL变体；设置/项目详情等静态样本 | 原UTC2026-10-12T17:37:48.146Z—17:41:16.170Z；885×778 CSS px/DPR2；三chat到3000节点上限；无REA ID；原件在外部clone，不复制私人内容 |
| D既有参考 | 外部hark-clone中的Next/React/CSS、字体和视频候选 | 只做静态审查，本轮未启动、不修改；部分font绑定、主题级联、卡片行为和几何与H15有差异 |
| D本包合同 | 插槽、事件命名、缺省状态/几何/动画与跨业务映射 | 是可实施选择，尚无本轮可运行组件参考和跨业务验证 |

A 是实际原版运行并实际看过像素的证据；B 是原版静态声明；C 是映射/公式/解释；D 是独立实现/迁移。尚未看像素的当前截图仅进入 `pendingPixelReviews`，不注册为已审 A。历史日期级截图不伪造快门时间，仍可经已审历史报告追踪。参考运行不能证明原版行为。

核心 App 需要正常登录。Google正常流程在浏览器安全检查处被拒；root继续正常邮箱/手机号或平台扩展复用已有用户会话。当前认证状态与原版App新A仍待核，任务在继续；没有绕过认证，也没有把等待当作用户接受partial。

## 视觉原则

1. 文字与内容带维持清楚的密度关系：聊天普通正文15/19，富卡分型，摘要详情升为独立阅读层。营销站采用大衬线短标题、GT M/L正文、充足纵向分段。
2. 玻璃材质由透明host、独立fill/stroke和连续曲线裁切共同形成。只读host背景透明会丢失实际填充；普通消息不继承输入框blur50。
3. 天空是原WebGL/噪声/LUT渲染与占位/冻结控制的组合。优先保留原shader与输入适配；D静态渐变只能算标明差异的降级。
4. 富内容以类型驱动renderer，卡片中的状态、动作与返回路径连贯。业务内容是样本，客户端分发不是完整后端schema。
5. 当前营销与历史App保留独立font-face、单位和主题作用域。它们不能合并为一个全局字号表或固定rem比例。

## 两套字体与主要文字角色

| 场景 | 文字角色 | 源值/绑定 | 来源状态 |
| --- | --- | --- | --- |
| H15普通消息/输入 | GT Standard L | 15px/19px/400；L face300/400/500/600/700及斜体 | B声明＋历史computed结论，当前App未知 |
| H15代码卡 | GT Standard Mono | 14px/20px/400，pre内边距0 16px 16px | B声明＋历史定向computed；Mono原件待补 |
| H15摘要详情 | Hark Serif标题；GT L正文 | 40px/52px/500；18px/25.2px/400；strong600 | 历史已看像素与computed；本包D采用基线，当前App待A |
| H15 Hark Serif | light/medium及斜体 | light300，medium500 | 历史CSS绑定；禁止默认用clone400/700代替 |
| 当前公共Text | Hark Serif | display1 4.4→8rem；display2 3.6→6.4；display3 3.2→5.6；display4 3.2→4.4；display5 2.4→3.2，均400/1.12 | 当前CSS B；箭头为≥768px分支 |
| 当前公共正文 | GT Standard M | primary1.8rem/1.4/400；secondary1.4rem/1.4/400；tertiary1.2rem/1.12/400 | 当前CSS B |
| 当前首屏标题 | Hark Serif | 4.8×auth-unit/1.3/700；pro accent400 | 当前App首屏局部覆盖B，独立于H15聊天App |
| 当前首屏介绍/条款 | GT Standard L | 介绍2.1×unit/500；条款1.2×unit/400 | 当前CSS B |
| 当前认证控件 | GT L或固定provider | provider15px/500；input1.5×unit/500；48px按钮/24px圆角 | 当前App variant B |
| 当前营销face | Hark Serif light/medium；GT M/L semibold | Serif light400/medium700；M/L semibold700 | 这是当前原声明，不是H15绑定修正 |

当前静态源还完整保存24类section与85条UI controller原字节声明、11条原SVG构造及形状路径；入口见static-analysis的uiControllerDeclarations/svgDeclarations。这些是B声明，尚未证明85类UI都当前执行或11条就是全部渲染图标。

当前有19份font-face精确URL。资产文件是否实际入包、GET时间、SHA、loaded-in-home与未加载声明各自记录；字体名/头部魔数不证明字形/逐字fallback。所有原字体、字重/斜体、S/Mono及运行绑定仍须继续完整采集。GT L/M是不同角色，不能因为现成M文件方便就替换聊天L。字重350在公共display6的窄屏声明存在，其实际face选择保留未知。

已取得7份原件经静态FontKit读取：GT M/L regular/medium为728 codepoints、964glyphs、无variable axes，payload OS/2字重400/500；Hark Serif light/light-italic的内部OS/2为200、medium500，UPM1000/ascent1000/descent−200/lineGap250，每份71 mapped codepoints且缺U+2019弯引号。**CSS请求/绑定400/700与payload字重200/500必须分开**；保留源CSS与实际文件，不因内部weight或文件名改写绑定。哪些fallback供给缺字仍须CSS.getPlatformFontsForNode等原版运行A确认；源码字族字符串/静态cmap不能直接证明逐字渲染。

根rem在<768px为 `clamp(62.5%,2.66667vw,100%)`，≥768px为 `max(62.5%,.694444vw)`。浏览器默认16px时390px推算约10.4px、1440约10px，这只是 C 算式；不要静默将1rem固定成10px。首页另有auth-unit11.5px，只有screen≥1060×693时10px。CSS px、rem、auth-unit、物理截图像素与DPR均独立保留。

## 颜色、形状与材质

H15 dark主文字245/245/247、次级50% alpha、stroke14%；用户fill29/29/31的.3，助手.7，composer/regular.618，solid.96。light是原CSS声明：用户白.3、助手白.98、composer白.55、regular白.668、solid白.96。H15浅色没有完整运行computed，当前App主题仍待新A。

输入框名义radius24px、stroke1px、blur50px；文字气泡名义radius24而无同等blur。Bz0NRe01.js Unicode字符280850—285300使用1.528665等参数按border-box生成Bezier path，ResizeObserver更新fill与evenodd stroke。通用 `border-radius:24px` 是 D替代，不能宣称几何等价。

当前公共表面为白、#f3f5fa、#141414和灰阶#8994aa；brand#2e4fff，scrim为black/white8%或12%的color-mix。保留透明色/主题条件而非预混成一个RGB。功能/文章/套餐大容器通常2rem radius；SignUp则6.6rem radius加 `corner-shape:superellipse(1.15)`，两者不同。

## 组件地图与状态

所有slots/events是迁移层 D命名；源几何/声明继续标 B。当前App所有状态尚未充分观察，组件 `unknowns` 明确列出来源缺口。默认/焦点/禁用/加载/空/错误只在适用控件出现，D缺省有具体数值，且不被标为原版verified。

| 组件族 | 记录入口 | 来源范围 | 状态 |
| --- | --- | --- | --- |
| 天空背景宿主 | sky | H15/H13历史来源＋D接口 | rendering, frozen, fallback |
| 连续圆角材质 | continuous-surface | H15/H13历史来源＋D接口 | default, resized, solid, touch |
| 工作区导航与返回 | app-navigation | H15/H13历史来源＋D接口 | selected, homeOpen, narrow, overlay |
| 个性面板卡与操作 | home-panel | H15/H13历史来源＋D接口 | ready, loading, empty, editing, error |
| 项目列表与标题编辑 | project-row | H15/H13历史来源＋D接口 | default, selected, editing, empty, error |
| 搜索浮层与结果 | search | H15/H13历史来源＋D接口 | idle, searching, results, empty, error |
| 设置分组与条目 | settings-group | H15/H13历史来源＋D接口 | overview, detail, disabled, loading, error |
| 单选、开关与分段选择 | setting-control | H15/H13历史来源＋D接口 | off, on, focus, disabled, pending, error |
| 资料、账号、语言与安全表单 | settings-form | H15/H13历史来源＋D接口 | viewing, editing, invalid, saving, saved, failure |
| 连接账号/卡片/任务的管理条目 | account-row | H15/H13历史来源＋D接口 | connected, disconnected, pending, confirming, failure |
| 锚定菜单与上下文动作 | menu | H15/H13历史来源＋D接口 | closed, open, focused, disabledItem |
| 阅读弹层、设置层与确认 | dialog | H15/H13历史来源＋D接口 | closed, open, busy, error |
| 内联错误、toast与异步指示 | feedback | H15/H13历史来源＋D接口 | info, progress, success, error |
| 多行输入与发送 | composer | H15/H13历史来源＋D接口 | empty, draft, reply, pending, error |
| 回复引用条 | reply-context | H15/H13历史来源＋D接口 | none, referencing, removed, invalid |
| 用户/助手普通消息 | message | H15/H13历史来源＋D接口 | user, assistant, hover, pending, error |
| 消息富文本 | markdown | H15/H13历史来源＋D接口 | default, longText, tableOverflow, codeBlock |
| 富内容卡宿主与标题 | rich-card | H15/H13历史来源＋D接口 | default, compact, loading, error, focused |
| 代码卡与溢出展开 | code-card | H15/H13历史来源＋D接口 | collapsed, expanded, fits, replying |
| 保留格式的文本模板卡 | text-template | H15/H13历史来源＋D接口 | collapsed, expanded, copyFeedback |
| 摘要预览与独立详情 | summary-card | H15/H13历史来源＋D接口 | preview, detailOpen, closed |
| 链接预览与数量栈 | link-stack | H15/H13历史来源＋D接口 | one, two, three, pile, metadataPending, expanded |
| 图片栈与图库 | photo-stack | H15/H13历史来源＋D接口 | collapsed, spread, locked, closed, imageFailure |
| 数据表卡与下载 | table-card | H15/H13历史来源＋D接口 | default, sortDesc, sortAsc, unsorted, menuOpen, exporting |
| 文件查看器 | file-preview | H15/H13历史来源＋D接口 | loading, loaded, unsupported, failure, closed |
| 任务阶段与停止反馈 | activity | H15/H13历史来源＋D接口 | running, confirmStop, optimisticStopped, rollback, complete |
| 网站或成果预览卡 | website-artifact | H15/H13历史来源＋D接口 | ready, opening, error |
| 营销导航、移动菜单与下载横幅 | public-navigation | 当前公开B＋D接口 | top, scrolled, mobileMenu, dismissedBanner, appOverlapping |
| 注册/登录提供商与邮箱表单 | auth-form | 当前公开B＋D接口 | signup, app, disabled, loading, error, focused |
| 首屏品牌、动态天空与入口 | public-app-hero | 当前公开B＋D接口 | poster, videoReady, authReady, narrow, reducedMotion |
| 3D演示卡轮播 | feature-carousel | 当前公开B＋D接口 | idle, transitioning, dragging, holding, hidden, reducedMotion |
| 交替图文功能段 | feature-section | 当前公开B＋D接口 | default, desktop, poster, playing, transparentMedia |
| 安全/宣言横幅与注册结尾 | public-cta | 当前公开B＋D接口 | statement, signup, narrow |
| 页脚分组链接与平台入口 | public-footer | 当前公开B＋D接口 | desktop, narrow |
| 套餐卡与窄屏三选一 | pricing | 当前公开B＋D接口 | desktop, narrow, highlighted, focused, pending |
| 内容阅读、新闻卡与法律页 | article | 当前公开B＋D接口 | article, card, listing, legal |
| 帮助FAQ与职位分组展开 | accordion | 当前公开B＋D接口 | collapsed, expanded, focused, empty |

组件目录还保存具体geometry、longText、适用平台、tokenRefs、交互事件与来源路径。Home/Project/Search/Settings等D叶组件可实施，但其原版视觉深度尚未完成；不得用“已有组件接口”关闭当前App原证据缺口。

## 代表布局与响应式

| 布局 | 记录 | 覆盖 |
| --- | --- | --- |
| 宽屏聊天与Home并列 | chat-desktop | B / partial |
| 窄屏聊天 | chat-narrow | D / unverified |
| 面板网格与卡片编排 | home-panels | D / unverified |
| 项目目录与详情 | project-directory | D / unverified |
| 设置总览与详情 | settings-master-detail | D / unverified |
| 独立阅读与文件弹层 | reading-dialog | D / unverified |
| 图片栈展开与复位 | gallery-overlay | D / unverified |
| 当前公开首页内容序列 | public-home | B / partial |
| 公开正文、宣言与法律内容 | public-content | B / partial |
| 套餐三列/窄屏单列选择 | public-pricing | B / partial |
| 文章列表与详情 | public-news | B / partial |
| 帮助问答与职位分组 | support-careers | D / unverified |

宽屏聊天保留Home420px列、transcript768px、padding形成512px内容带，普通气泡上限424，composer离底32。H15 1440×900定向样本composer x674/y820/512×48；另一个Home关闭样本x464，不能混合为同一布局。390×844历史代表composer x16/y764/358×48；触控硬件、软键盘与safe-area的实际运行另需证据。

当前营销Feature max135.2rem/文本45rem/媒体742:582、max74.2rem，768px以上并列且media-first反向；CtaBox min51.2rem。Pricing在1024px以上112rem三列并突出中卡，窄屏56rem内三项selector只显示selected计划。Footer窄屏两列、桌面分组40rem区；Header7.6/9.2rem、z1000、六层blur mask。page-specific角色和完整media条件均可直接在 [static-analysis](research-2026-10-22/public/static-analysis.json) 找到。

## 通用操作与数据保留

- 输入/reply：draft、附件和结构化reply target分离；Escape去引用保留draft。IME组合期间不发送。pending防重复，失败保留本次输入。
- 阅读/文件：摘要打开独立dialog；文件按类型viewer渲染。关闭解除本层锁、恢复trigger焦点和原thread位置。未观察的取消/晚到结果用明确D目标规则处理。
- 搜索/编辑：D query debounce200ms且抑制旧结果；项目/资料表单draft与committed分开，保存失败保留draft，取消恢复旧值，离开有改动提供实际选择。
- 删除/断开：D先确认对象，取消不改数据；成功才移除，失败保留。undo只在真实目标能恢复时提供。
- 栈/表格：双链接没有展开入口，≥4 pile与7图图库样本分开；图库关闭复位spread0/lockedfalse/expandedfalse。表格source模型desc→asc→null，但H15三次示例点击顺序没变；该实例不标排序成功。
- 主题：原light/dark/system/auto四支；system按prefers-color-scheme，auto按sky-place.day/skyAppearance。源码lum输出不证明主题按亮度；D7–18不得称原算法。
- 认证：上方App空trim输入Continue disabled；底部SignUp只随busy disabled，空提交有role=alert。共享idle/submitting/continuing/error状态，provider成功跳/chat、错误跳/login?oauth_provider；实际正常认证依root协作，B路由不证明成功。

详见 [状态转移](patterns/interactions.json)。D主观选择必须在目标实际运行验证；不能把placeholder/假成功或无行为按钮当参考完成。

## 核心动效与执行绑定

| 动效 | 分类 | duration | easing/算法 |
| --- | --- | --- | --- |
| theme-material | B | 800 ms | cubic-bezier(.45,0,.55,1) |
| pressed | B | 120 ms | ease-out |
| link-stack | B | 420 ms | unknown；见原算法/D选择 |
| stack-flip | B | unknown；见sourceParameters | unknown；见原算法/D选择 |
| sky-scene-h15 | B | 2500 ms | linear |
| sky-placeholder-current | B | 800 ms | ease |
| tool-text-reveal | B | unknown；见sourceParameters | linear |
| public-button | B | 240 ms | cubic-bezier(.24,.42,.42,.92) |
| public-learn-more | B | 1000 ms | power2.inOut |
| carousel-track-current | B | unknown；见sourceParameters | unknown；见原算法/D选择 |
| carousel-prompt-enter-current | B | unknown；见sourceParameters | unknown；见原算法/D选择 |
| carousel-prompt-exit-current | B | 240 ms | linear |
| feature-video-current | B | unknown；见sourceParameters | unknown；见原算法/D选择 |
| d-overlay | D | 240 ms | cubic-bezier(.2,.8,.2,1) |
| d-message-entry | D | 220 ms | cubic-bezier(.2,.8,.2,1) |
| d-stack-alternative | D | 600 ms | cubic-bezier(.2,.8,.2,1) |
| d-carousel-alternative | D | 600 ms | cubic-bezier(.25,1,.5,1) |
| d-disclosure | D | 220 ms | ease-out |
| d-feedback | D | 160 ms | ease-out |

当前3D轮播是自写rAF spring+WAAPI，不因同包出现Swiper/GSAP就把它们说成此动画拥有者。原字节350829—363521，spring helper180803—181643：track T=.5s/ζ=.85、rise .4/.85、bubble-enter .35/.55，ω=2π/T、k=ω²、c=2ζω；每帧dt≤.05并2次half-step。T不是固定运行duration。

投影radius=2.81S、perspective=6.4S、angle=−wrappedDistance×.4181494661921708rad；depth=−radius+max(0,rise)×perspective×(1−.8771929824561404)。abs(distance)3.3→3.8 smoothstep渐隐。S按stage宽/高度投影限幅，不取一个全站常量。提示气泡15M/19M、pad14.5M/16M、radius24M、#efefec；M低横屏或<600为1，其他1.12。

enter scale.3→1的49点linear采样C≈699.620ms，不支持linear则源600ms (.3,1.5,.5,1)；opacity60ms、文字300ms。exit opacity/blur delay100+240ms，在onfinish隐藏并取消scale余动画；scale的C≈646.708ms不等于完整视觉退出。原重选/切换取消旧WAAPI，cleanup取消rAF/timer/observer/media；reduced/hidden/focus/hover/离屏都有暂停条件。metadata.holdMs和媒体长度须当前实例补证，不能用D固定hold完成源研究。

H15天空core的31ms绘制门限/DPR1.75/scene2.5s crossfade，与当前公开wrapper48ms/2000ms/60000ms pacing分开；这不是实测FPS。当前core是否同一字节尚未知。工具text reveal90chars/s/reveal30属于工具活动，不改写普通聊天的逐token事实。

## 资产复用与参考

[assets-manifest](research-2026-10-22/assets-manifest.json) 分开记录included/withheld/unavailable、真实SHA、sourceURL、face/crop/变体和组件用途。直接复用优先级：原字体→原SVG→原shader/连续圆角→原媒体→原组件/动画算法薄包装。发布factory需要依赖适配，不能直接import不存在的未打包源码。

本任务新增的可运行参考尚未制作；既有外部hark-clone不可从总库单独拉取，且未本轮复跑。具体差异和接手见 [reference](research-2026-10-22/reference/README.md)、[comparison](research-2026-10-22/comparison/README.md)、[ADAPTATION_GUIDE](ADAPTATION_GUIDE.md)。D参考的完成不提升原版覆盖。

## 当前公开路由取证地图

以下是本包生成时已保存page JSON。当前首页另有独立home-inventory/REA输出。captures是DOM/computed/PNG顺序收集的真实区间，不伪造统一快门。完整文本与裁切图分开；像素审阅以root独立ledger为准。

| 路由 | 标题 | capture interval UTC | 审阅边界 |
| --- | --- | --- | --- |
| /acceptable-usage-policy | Acceptable Usage Policy | Hark | 2026-10-22T12:58:36.916Z — 2026-10-22T12:58:40.469Z | 待独立pixel-reviews确认；text/HTML可读 |
| /articles/at-and-t-and-hark-formalize-strategic-partnership | Hark and AT&T Partner on Connectivity for Future AI Devices | Hark | 2026-10-22T12:52:09.052Z — 2026-10-22T12:52:12.430Z | 待独立pixel-reviews确认；text/HTML可读 |
| /articles/hark-announces-usd700-million-fundraising-round | Hark announces $700 million fundraising round | Hark | 2026-10-22T12:58:20.772Z — 2026-10-22T12:58:25.188Z | 待独立pixel-reviews确认；text/HTML可读 |
| /articles/introducing-hark-handoff | Introducing Hark Handoff | Hark | 2026-10-22T12:58:15.775Z — 2026-10-22T12:58:20.514Z | 待独立pixel-reviews确认；text/HTML可读 |
| /articles/introducing-hark-pro | Introducing Hark Pro | Hark | 2026-10-22T12:51:59.700Z — 2026-10-22T12:52:04.465Z | 待独立pixel-reviews确认；text/HTML可读 |
| /articles/hark-announces-multi-year-partnership-with-nvidia | Hark Announces Multi-Year Partnership with NVIDIA to Advance Personalized AI supported by Gigawatt-Scale Compute | Hark | 2026-10-22T12:52:04.707Z — 2026-10-22T12:52:08.952Z | 待独立pixel-reviews确认；text/HTML可读 |
| /articles | News | Hark | 2026-10-22T12:46:06.727Z — 2026-10-22T12:46:10.732Z | 待独立pixel-reviews确认；text/HTML可读 |
| /careers | Careers | Hark | 2026-10-22T12:46:10.955Z — 2026-10-22T12:46:14.696Z | 待独立pixel-reviews确认；text/HTML可读 |
| /contact-hark | Contact | 2026-10-22T12:46:14.800Z — 2026-10-22T12:46:17.869Z | 待独立pixel-reviews确认；text/HTML可读 |
| /content-generation-and-publication-policy | Content Generation and Publication Policy | Hark | 2026-10-22T12:58:40.564Z — 2026-10-22T12:58:44.107Z | 待独立pixel-reviews确认；text/HTML可读 |
| /cookies-policy | Cookies Policy | Hark | 2026-10-22T12:58:25.283Z — 2026-10-22T12:58:28.650Z | 待独立pixel-reviews确认；text/HTML可读 |
| /manifesto | Manifesto | Hark | 2026-10-22T12:39:51.579Z — 2026-10-22T12:39:57.539Z | 待独立pixel-reviews确认；text/HTML可读 |
| /non-user-privacy-notice | Non-User Privacy Notice | Hark | 2026-10-22T12:58:33.203Z — 2026-10-22T12:58:36.813Z | 待独立pixel-reviews确认；text/HTML可读 |
| /other-policies | Other Policies | Hark | 2026-10-22T12:50:20.298Z — 2026-10-22T12:50:23.352Z | 待独立pixel-reviews确认；text/HTML可读 |
| /pricing | Pricing | Hark | 2026-10-22T12:42:39.821Z — 2026-10-22T12:42:42.880Z | 待独立pixel-reviews确认；text/HTML可读 |
| /privacy-policy | Privacy Policy | Hark | 2026-10-22T12:50:16.390Z — 2026-10-22T12:50:20.139Z | 待独立pixel-reviews确认；text/HTML可读 |
| /security | Security | Hark | 2026-10-22T12:42:36.253Z — 2026-10-22T12:42:39.711Z | 待独立pixel-reviews确认；text/HTML可读 |
| /terms | Terms of Service | Hark | 2026-10-22T12:50:12.547Z — 2026-10-22T12:50:16.251Z | 待独立pixel-reviews确认；text/HTML可读 |
| /using-hark | Using Hark Pro | Hark | 2026-10-22T12:42:42.984Z — 2026-10-22T12:42:46.328Z | 待独立pixel-reviews确认；text/HTML可读 |

## 核心缺口与继续条件

- 当前Hark Serif已取得原件每份仅71 mapped codepoints，缺U+2019；CSS400/700与payload OS/2 200/500分开，真实缺字fallback与逐glyph绑定仍需原版runtime A，不能以静态font-family收口。
- 用户要求原版UI最深取证和完整字体/图标/媒体资产；当前包是持续研究入口，subset/历史B/D迁移选择均不完成该范围。
- 当前登录后核心App尚无正常认证运行A；Google正常登录在浏览器安全检查被拒，root继续正常邮箱/手机号或现有用户会话入口；未绕过认证，不能按等待超时结束。
- 当前App导航/Home/Projects/Search/Settings/Plans/Help/安全与账号管理的原版几何、字体绑定、状态、关闭/返回、错误与焦点均未建立完整新A；H15/H13仅历史基线。
- 当前公开页桌面/窄屏/横屏的逐节点排版、焦点/hover/按下/disabled/loading/error与布局配对仍需root审阅；未阅截图不写reviewed。
- 当前公开CSS19份字体face只一部分带真实GET原件；所有字重/斜体/S/Mono必须收齐并核SHA、name/glyph角色及运行face绑定，不能仅凭font-family字符串完成。
- 原始控制图标、Logo/provider/play等SVG虽有精确factory入口，完整渲染SVG与变体清单、实际文件SHA及组件绑定尚待补；外部lucide/字母占位是D。
- 当前媒体poster/视频的完整来源、变体、crop/hotspot/frame尺寸、每slide metadata/holdMs/真实视频长度尚未完整入包；首页初始懒加载图不是全媒体完成证据。
- 当前天空core原件与H15完整shader的字节对应、实际uniform/首帧/freeze/fallback/主题与帧pacing执行仍未知；包装定时常量不是FPS实测。
- 核心3D轮播已有B弹簧/投影/WAAPI/gesture声明，但尚无当前原版各状态、reduced-motion、hover/focus/中断、完整时轴A验证；C曲线求值不提升A。
- 登录App浅色/深色/auto/system同内容配对与实际切换未完成；营销font-face400/700不得覆盖历史App300/500。
- 组件参考和当前原版A/D条件配对尚未制作/运行；既有外部hark-clone只是未复跑D起点，存在字体/级联/摘要/图库/composer等已审差异。
- 不同业务切片只有D接口和映射方案，尚无执行与数据路径验证；不称跨业务verified。

原版研究覆盖、迁移可实施性和用户此次约定完成是三个独立结论：本包有非空可实施规范；原版当前深度仍partial；用户要求的最深取证继续进行。下一步由root补正常登录、全部资产/页面族、实际像素/状态/动画范围与A/D对照，再局部校验新增引用。不得由格式通过、待登录或D运行直接改成ready。
