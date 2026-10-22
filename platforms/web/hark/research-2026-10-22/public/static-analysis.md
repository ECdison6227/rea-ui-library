# Hark 当前公开首页：静态客户端分析

本报告研究本轮已捕获的公开首页客户端，给出可实施的布局、组件、交互、动效与资产入口。机器入口是 [static-analysis.json](static-analysis.json)。当前首页的模块对应关系由页面观察与源码共同推导；打包内的全部组件没有被当成已执行功能。

## 来源与证据边界

- 来源：`https://hark.com/`；deployment：`dpl_43vpACitAoHemg5zATXXCP8hBYHa`。
- 首页原 REA：`ev_372b36210620b0b72e42ff99acdc1e91775e5f054040645c146c86985fd38885`，采集窗为 **2026-10-22T12:24:40.071Z—12:24:43.488Z**；声明 observation_ms=500，并非动画时长。继承视口为 **1440×702 CSS px，DPR=2**。
- 本 worker 只读取已采集文件，未控制浏览器、运行目标代码或发起新网站／原厂接口请求。23 个 Next.js 脚本共 **6,627,244 bytes**，实际 SHA 与 [resources-manifest.json](resources-manifest.json) 全部一致。
- 三份 CSS 由协调研究在真实 `/manifesto` 页面注册表中读取，时间为 **12:42:35.741Z—12:42:36.073Z**；三个 URL 与首页已观察 URL 完全一致。文件身份是 **CDP CSS.getStyleSheetText 保存的解码文本 SHA**，不是 HTTP 原始响应 SHA。见 [注册样式清单](resources/manifesto-registered-styles-manifest.json)。分页面、分次采集保留，CSS 声明不提升为首页计算样式。
- **A** 为继承的实际页面观察；**B** 为已捕获源码／注册样式声明；**C** 为对应关系与公式计算；本 worker 未创建 D 参考实现。本次当前首页资料不改写 2026-10-15 登录后聊天的历史研究。

为核验当前包装结构，仅对 `15bfdjo3hjyv9.js` 与 `406ziev2ozark.js` 做了一次有界 REA 静态分析。完整 parent 为 `ev_1dec1ea59287c1e5979db93684246cd86b88c72f2718bcafd5388a99f7ac69ab`，summary 为 `ev_a1ca3cdb9e1d37f6ba63a50e575c925edde2b3fce9f37f22b46163a926a15853`。工具读了 523,814 bytes、成功解析两文件、没有解析失败，但这个 Turbopack 形态没有恢复模块（modules=0）；应用／语义图为 partial，语义上限 6000/file、12000 total，保留 30234 semantic unknowns。未为扩大输出重复分析。精确 factory、注册表、class map 则另以本地 Acorn 惰性语法解析定位；其来源字节范围写入 JSON，不冒充新的 REA 或运行证据。回执只存于本地 `.runtime/hark-2026-10-22/`。

## 当前首页的内容与路由组织

A 首页观察与 B 源码对应出以下组合，**组件名称的实例对应标 C**：

| 页面样本 | 声明组件 | 可迁移结构 |
| --- | --- | --- |
| `hark pro`、产品介绍、Google／Apple／标识输入 | App | 视觉媒体＋中央登录列＋底部条款；Learn more |
| `What Hark can do` 功能示例 | FeatureVideoCarousel | 三维卡片轨道、裁切的视频面、独立提示气泡、键盘焦点面 |
| 五段介绍：日常任务、Handoff、安全／批准、Action Buttons、Panels | Feature ×5 | 标题／正文和媒体交替；可选标签、图像或透明视频 |
| 安全与 Manifesto 引导 | CtaBox ×2 | 背景媒体、居中大标题、链接动作 |
| `Sign up for Hark` | SignUp | 背景、描述、独特形状的表单面板 |
| Company／Resources／Legal／下载／Social | Footer | 分组链接、商店链接、社交与版权 |
| 可访问树 Canvas | Sky | 天空 live／freeze 双 canvas、占位与失败层 |

声明主入口为 [15bfdjo3hjyv9.js](resources/15bfdjo3hjyv9.js) 的 Sections（module 537700）：按照 CMS `_type` 首字母大写选择组件；需要 slug，空项／无 type／未知 type 不渲染；每段绑定 SectionContext 和 Section wrapper。它还设置 page type、动态壁纸、header/footer 隐藏等 `data-*` 条件。

同包注册了 **24 种 section**，其中含 Hero、FeatureTicker、PricingV2、Values、Team、JobListings 和文章模块。它们是 B 声明，不能据此声称当前首页运行这些模块或全部对应路由可达。4.3MB 的 `0icmk032-jbsa.js` 主要含 Sanity Studio／依赖内容；其中的字符串不归为首页文案或功能。

A 首页 href 指向 `/`、`/manifesto`、`/careers`、`/articles`、`/security`、`/pricing`、`/using-hark`、`/contact-hark`、`/terms`、`/privacy-policy`、`/other-policies`，以及 App Store、Play Store、Instagram、X、LinkedIn。这里只证明链接存在；其他页面的实际覆盖由协调研究提供。认证还声明跳向 `/login?email=…`／`?phone=…`、OAuth 成功 `/chat` 和错误 `/login?oauth_provider=…`。

页面中的能力、加密和安全文字是产品介绍样本，本 worker 没有验证其后台实现。

## 排版与单位

当前来源保留两套尺度，不能把全页面按固定 10px/rem 处理：

- B 根字号：小于 768px 使用 `clamp(62.5%,2.66667vw,100%)`；≥768px 使用 `max(62.5%,.694444vw)`。若浏览器默认字号为 16px，390px 窄屏的根字号按公式 **C≈10.4px**；1440px 宽屏 **C≈10px**，这是算式结果，不是运行测量。
- B 首页 App 独立 `--app-auth-unit:11.5px`；仅在 **screen width≥1060px 且 height≥693px** 时改成 10px。它不依赖上面的 rem 缩放。

通用 Text 的 B 角色来自 [2024m-gqsxckw.css](resources/2024m-gqsxckw.css)：

| 角色 | family／weight | 窄屏字号 → ≥768px | 行高 |
| --- | --- | --- | --- |
| display-1 | Hark Serif 400 | 4.4rem →8rem | 1.12 |
| display-2 | Hark Serif 400 | 3.6rem →6.4rem | 1.12 |
| display-3 | Hark Serif 400 | 3.2rem →5.6rem | 1.12 |
| display-4 | Hark Serif 400 | 3.2rem →4.4rem | 1.12 |
| display-5 | Hark Serif 400 | 2.4rem →3.2rem | 1.12 |
| display-6 | GT Standard L 350→400 | 2rem →2.4rem | 1.32 |
| hark-48 | Hark Serif 400 | 4.8rem | 1.3 |
| body-primary | GT Standard M 400 | 1.8rem | 1.4 |
| body-secondary | GT Standard M 400 | 1.4rem | 1.4 |
| body-tertiary | GT Standard M 400 | 1.2rem | 1.12 |
| cta | GT Standard M 400 | 1.4rem | 1 |

通用 display-5 字距为 −.024rem／−.032rem，body-tertiary 为 .012rem。当前 App 又覆盖了其标题为 **Serif 700、4.8×auth unit、1.3 行高**，`pro` accent 为 400；介绍为 **GT Standard L 500、2.1×unit**，条款为 **GT Standard L 400、1.2×unit**。App 表单 provider 固定 15px／500，输入用 GT L 500、1.5×unit。这些局部覆盖优先于通用 Text 角色。

CSS 有 **19 份 @font-face**，包括 GT S、Mono、M、L 的具体字重／斜体及 Hark Serif 400／700／斜体。当前首页 REA 只观察到 **7 个已加载字体 URL**：GT L regular/medium、GT M regular/medium、Serif light/light-italic/medium。JSON `fontAssets` 逐一给出精确 URL 与是否在首页加载；协调研究现已保存其中7份原件，本 worker按清单核验SHA并用本机既有FontKit读取字体表；另外12份仍是URL级声明。逐字运行fallback与350字重实际文件选择保留未知。

## 布局、表面与状态

所有下列数值为 **B 声明**，完整 selector、媒体条件、属性、源文件行列与 SHA 在 JSON `styleGroups`。

App 最小高 100svh，padding20px，gap20px。视觉媒体默认隐藏；上述宽／高条件满足时显示，aspect **769/984**，radius **24px**，宽度为 `min(100vw - 520px,50vw - 20px,78.1504dvh - 31.2602px)`。登录列宽 **42×unit**；main 用三行网格居中 auth 并将条款放底部；height≤640px 改 flex／safe center。

App providers 为 **48px 高、24px radius、20px 水平 padding、12px icon gap、15px 字号**。输入高 **4.8×unit**、radius **2.4×unit**、水平 padding2×unit，placeholder 居中且 ink30% alpha，输入文本 start 对齐。focus 是 2px 光环；provider focus outline2px／offset2px；按下 scale **.97**，wash／scale **120ms ease-out**。disabled opacity **.4**。错误层使用洗白表面、blur **50px**；通用 sign-up 错误则为 `#ff4d00` 15% 面、1px 边界、1.2rem radius。

Feature 内容 max **135.2rem**，正文 wrapper53rem／main45rem／heading43rem。媒体 aspect **742/582**、radius **2rem**、≥768px max74.2rem。小屏纵排，桌面横排，`media-first` 桌面反向。透明视频变体取消背景／outline 并允许 overflow visible；它以原视频 recording 和 framing 插槽显示，不是近似绘制图。

CtaBox 面板 min-height **51.2rem**，radius2rem，居中标题与动作。底部 SignUp form 则是 **corner-shape:superellipse(1.15)**＋radius **6.6rem**＋padding **4.4rem**，max-width **33.5rem／40rem desktop**。不要把这两个容器混为同一种圆角。

全局 spacing：小屏 .4／1.2／1.6／2.4／4.4rem；桌面 .8／1.6／2.4／4.4／8rem。默认文字／中性表面由 `#141414`、白、`#f3f5fa`、灰阶和 black/white scrim 8%／12% 组成。暗色分支受 `data-theme=dark` 或无动态壁纸时的系统色彩条件控制；App／Pricing 按钮有局部明色覆盖。来源 alpha、color-mix 与色彩条件原样保留，没有先混算成单一 RGB。

Footer 窄屏链接 column-count2、桌面 flex 分组；footer-links-width40rem。Header 声明高7.6／9.2rem、sticky、z1000、六层渐变 mask blur（.04／.08／.16／.32／.64／1.28rem）。App 覆盖、隐藏标志和滚动状态控制 header；首页捕获虽有 HEADER，却没有其 Menu／主链接动作的可访问节点，不能仅凭 CSS 断言某一个具体隐藏标志为 true。

## 认证与通用交互

B 表单共享 hook 的状态为 idle／submitting／continuing／error，使用 pending ref 与 honeypot 抑制重复。trim 后 email 用正则验证并 lower-case；电话支持 `00→+`，默认地区 US，必须 isValid。有效标识直接 location.assign 登录 URL，当前首页代码不承担验证码页面实现。

**上方与下方空输入的行为明确不同：** App 的 Continue 在空 trimmed 输入时 disabled；底部 SignUp 的 Continue 只随 busy 禁用，空输入点击后显示“Enter a mobile number or an email address.” role=alert。这与原 REA 的上方 disabled=true、下方 focusable 状态一致；点击结果仍只是 B，不被标为本 worker 已实测，也未把此差异叫作缺陷。

Google／Apple 点击进入 submitting；全部 provider disabled，选中的 provider 用 loading 状态。成功 callback 指向 /chat，错误 callback 指向 /login?oauth_provider；catch 释放 pending，显示相应错误；pageshow 重置到 idle。输入使用 required、aria-invalid/describedby，form aria-busy，错误 role=alert。当前 auth 后台和实际 OAuth 未由本 worker执行。

App Learn more 用 **1s power2.inOut** ScrollTo section bottom，reduced-motion 时 duration0；滚动上移后 wrapper hidden＋inert。通用 Link 的同页 anchor／返回顶部也声明 **1s power2.inOut**，但该 helper 没有 reduce guard；不能全局声称所有滚动都关闭动画。

## 核心三维轮播

实现入口为 [15bfdjo3hjyv9.js](resources/15bfdjo3hjyv9.js) **byte350829—363521** 的 FeatureVideoCarousel，独立的 r4 spring／r9 sampler 在 JSON 给出精确来源。它是自写 rAF 弹簧＋WAAPI；Swiper、GSAP 在同包出现不证明它们控制此轮播。

| 对象 | B 源 pair：周期 T／阻尼比 ζ | C 推导 stiffness／damping |
| --- | --- | --- |
| track／退出气泡 sampler | .5s／.85 | 157.9136704／21.3628300 |
| card rise | .4s／.85 | 246.7401100／26.7035376 |
| 进入气泡 sampler | .35s／.55 | 322.2727968／19.7471538 |

`ω=2π/T`，`k=ω²`，`c=2ζω`；每帧 dt 限 .05s，做两次 half-step Euler：`v+=(-k*(x-target)-c*v)*dt; x+=v*dt`。它没有由这些 T 值直接证明的固定运行时长。

三维几何：

- radius **C=2.81×S**，perspective **E=6.4×S**；
- angle = **−wrappedDistance×.4181494661921708 rad**；
- depth = **−C+max(0,rise)×E×(1−.8771929824561404)**；
- card transform = `translateZ(Cpx) rotateY(angle) translateZ(depthpx)`；
- abs(distance)3.3→3.8 用 smoothstep 渐隐，source metadata 决定 face、crop 与 prompt anchor；
- M 在 stage width<600 或低横屏时为1，否则1.12。S 用 stage 宽度与投影公式计算，并有限幅／低屏高修正；完整算式在 JSON，不将任何一个 S 数字当成全产品常量。

提示气泡为 **#efefec、24×Mpx radius、14.5×M／16×Mpx padding、15×Mpx font／19×Mpx line-height**，三层阴影分别 y1/blur2/alpha1a、y3/blur8/alpha12、y8/blur40/alpha1f。

进入：scale **.3→1**；支持 CSS `linear()` 时，用49点 spring采样、duration=`1000*ln(1000)/(ζ×2π/T)`，**C≈699.620ms**；fallback 为 **600ms cubic-bezier(.3,1.5,.5,1)**。bubble opacity60ms，text opacity300ms，文字 easing `(.4,0,.2,1)`。

退出：bubble opacity1→0、blur0→2px，**delay100ms＋duration240ms**；text220ms；scale1→.8、translateY8×Mpx，源 sampler pair .5/.85，对应 **C≈646.708ms**，fallback600ms `(.25,1,.5,1)`。**opacity onfinish 会隐藏并取消尚在进行的所有 prompt 动画**，因此 scale 的完整 sampler 时长不能当成视觉退出时长；340ms 也只是声明相加，并非墙钟实测。重选／切换会取消原 prompt animation，保留中断机制。

交互：primary left pointer、**6 CSS px** 拖动门槛；触摸横向量须大于纵向的 **1.25×**；wheel 横向判定同1.25、**200ms** 结束，Ctrl-wheel 放行。release velocity clamp±30 slides/s，target=`round(x+.2*velocity)`；左右键 target±1。surface 使用 `pan-y pinch-zoom`，focus outline2px、offset−6px；提示层 aria-hidden，另有 polite/atomic 文案公告。

自动推进等待 metadata.holdMs；video failure fallback **3800ms**，最近用户操作间隔须>**2500ms**。离屏、document.hidden、reduce、focus-visible、当前卡 hover 均有暂停条件。视频 near≤1.5 slides 才加载，away>.5 重置；cleanup 取消监听／rAF／timer／WAAPI／observer 和 source。**当前每页 metadata 实值与视频长度未包含在 worker 的保存脚本数据中，需要实例补证。**

## 天空、媒体与其他声明

B Sky 根节点 sticky、z−2、高100vh、负 margin；仅动态壁纸条件显示。body 渐变在第一屏＋轮播后逐渐混到 surface；place-holder opacity transition **.8s**。bootstrap 请求 /wallpaper_core.html，提取其中 script 执行，再给 live canvas visible、占位 opacity0；本 worker 未执行这个过程，也没有收到其 shader 正文。

wrapper 声明帧 pacing：常规 **48ms**，reduce **2000ms**，软件 renderer **60000ms**；键盘／滚轮手势 hold2000ms、coasting3000ms、最近输入窗口15000ms，resize redraw budget30。placeholder 为48px宽、保留 aspect 的 WebP q.8，保存字符串≤65536chars。这些不构成实测 FPS 或用户位置／天气结果。原 REA 内归属 `https://hark.com/` 的 inline bootstrap 可对照同一套常量。

透明 Feature 视频用两 observer：near margin200px预加载，实际可见才 play；document.hidden／reduce 停止，requestVideoFrameCallback 确认一帧后标 data-playing。错误恢复 poster。普通 VideoCover 则是另一套策略，声明 muted／loop／playsInline／preload auto 与 viewport pause；不能把两种媒体宿主的生命周期混合。

可选 B 类型也已留入口：FeatureTicker 为22.9×18.1rem／gap1rem、scale1.2、300ms `(.42,0,.58,1)`、30px/s drift、.64s速度缓冲与 inertia；Hero 为scroll-scrub inset .8／1.2rem、corners2.4rem、64／96px滚动 span、scrub.8。这两类没有被认定为当前首页样本。Hero 源 tween duration64/96 属于 scrubbed timeline，不能记作64/96秒运行动画。

Cookie prompt 入场.3s并在reduce关闭；generic loading spinner仍声明.6s linear infinite，当前 reduce 规则只关闭部分 transition，未关闭该 keyframe。此类未覆盖的 reduce 差异保留，供目标实现明确选择。

## 直接复用入口与待补证

- **字体：** JSON `fontAssets` 给精确19条URL／face绑定；所需原件应原样采集、哈希后接入。
- **SVG：** `1_4hko300imeg.js` Logo556664；`15bfdjo3hjyv9.js` IconPlay654266、provider Google；Apple500933。路径已嵌入源 factory，可提取，不必默认重画。
- **组件与内容：** Text637008／Button763149、Sections537700、Image755253／VideoCover240148／PortableText945774／Link454929。class map、CSS状态、e.i依赖关系需包装到目标工程；当前发版 factory 不等于可直接 import 的原始React源码。
- **图片／视频：** JSON `mediaAssetLocators` 保存首页实际源URL；Image默认q75/WebP、sizes100vw，保留 crop/hotspot与SVG unoptimized规则。视频 source与poster的直接入口分别记录。
- **动效：** 核心 carousel 的 spring与WAAPI足以避免猜测；要匹配当前实例，仍需 populated slide metadata、源媒体长度和画面对照。

核心待补证为：当前实例的carousel metadata与视频时轴、wallpaper core及运行帧／主题、桌面与窄屏实际匹配样式、hover／focus／reduce／中断、认证error绘制和正常登录后的新组件。源字体角色和局部几何现在已有B实施入口；逐字fallback与浏览器现代CSS支持是独立未知。所有未知都不能由参考实现的运行结果替代原版事实。



## 深化补证：完整字体变体与文件身份

19份@font-face全量如下。声明来自当前CSS；“已保存”来自协调研究在/terms普通页面上下文于12:49:04.940Z—12:49:05.009Z获取先前已观察字体URL的原件。详情见[原件清单](resources/observed-fonts-manifest.json)和JSON fontAssets.localOriginal。此处没有用未获取文件冒充字体原件。

| CSS family | weight / style | 精确来源文件 | 本轮原件 |
| --- | --- | --- | --- |
| gt-standard-s-standard | 400 / normal | [gt-standard-s-standard-regular.1v94rzj-843fw.woff2](https://hark.com/_next/static/media/gt-standard-s-standard-regular.1v94rzj-843fw.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-s-standard | 500 / normal | [gt-standard-s-standard-medium.1z-favcayrd64.woff2](https://hark.com/_next/static/media/gt-standard-s-standard-medium.1z-favcayrd64.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-mono-standard | 400 / normal | [gt-standard-mono-standard-regular.2iqqf_4_8fw_b.woff2](https://hark.com/_next/static/media/gt-standard-mono-standard-regular.2iqqf_4_8fw_b.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| hark-serif | 400 / normal | [hark-serif-light.3qkalivet-2sm.woff2](resources/hark-serif-light.3qkalivet-2sm.woff2) | 已保存、SHA已核 |
| hark-serif | 400 / italic | [hark-serif-light-italic.0fhdxjby66gtz.woff2](resources/hark-serif-light-italic.0fhdxjby66gtz.woff2) | 已保存、SHA已核 |
| hark-serif | 700 / normal | [hark-serif-medium.0scjs-gvhp6fa.woff2](resources/hark-serif-medium.0scjs-gvhp6fa.woff2) | 已保存、SHA已核 |
| hark-serif | 700 / italic | [hark-serif-medium-italic.1bqquesg4op7h.woff2](https://hark.com/_next/static/media/hark-serif-medium-italic.1bqquesg4op7h.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-m-standard | 400 / normal | [gt-standard-m-standard-regular.3ix3tcgbm6t_u.woff2](resources/gt-standard-m-standard-regular.3ix3tcgbm6t_u.woff2) | 已保存、SHA已核 |
| gt-standard-m-standard | 400 / italic | [gt-standard-m-standard-regular-oblique.1xth6p8rdfsxv.woff2](https://hark.com/_next/static/media/gt-standard-m-standard-regular-oblique.1xth6p8rdfsxv.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-m-standard | 500 / normal | [gt-standard-m-standard-medium.2ekeevu0e95ee.woff2](resources/gt-standard-m-standard-medium.2ekeevu0e95ee.woff2) | 已保存、SHA已核 |
| gt-standard-m-standard | 500 / italic | [gt-standard-m-standard-medium-oblique.30h-c7l8age-f.woff2](https://hark.com/_next/static/media/gt-standard-m-standard-medium-oblique.30h-c7l8age-f.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-m-standard | 700 / normal | [gt-standard-m-standard-semibold.11vxm560ke5j4.woff2](https://hark.com/_next/static/media/gt-standard-m-standard-semibold.11vxm560ke5j4.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-m-standard | 700 / italic | [gt-standard-m-standard-semibold-oblique.2azw358mnlp_0.woff2](https://hark.com/_next/static/media/gt-standard-m-standard-semibold-oblique.2azw358mnlp_0.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-l-standard | 400 / normal | [gt-standard-l-standard-regular.09tdz1i3kmgj1.woff2](resources/gt-standard-l-standard-regular.09tdz1i3kmgj1.woff2) | 已保存、SHA已核 |
| gt-standard-l-standard | 400 / italic | [gt-standard-l-standard-regular-oblique.29wq21_vse72v.woff2](https://hark.com/_next/static/media/gt-standard-l-standard-regular-oblique.29wq21_vse72v.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-l-standard | 500 / normal | [gt-standard-l-standard-medium.1788_3rwow92_.woff2](resources/gt-standard-l-standard-medium.1788_3rwow92_.woff2) | 已保存、SHA已核 |
| gt-standard-l-standard | 500 / italic | [gt-standard-l-standard-medium-oblique.22w6f_6egokz2.woff2](https://hark.com/_next/static/media/gt-standard-l-standard-medium-oblique.22w6f_6egokz2.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-l-standard | 700 / normal | [gt-standard-l-standard-semibold.3dc3_751vyn1r.woff2](https://hark.com/_next/static/media/gt-standard-l-standard-semibold.3dc3_751vyn1r.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |
| gt-standard-l-standard | 700 / italic | [gt-standard-l-standard-semibold-oblique.0hhyumsaypw9d.woff2](https://hark.com/_next/static/media/gt-standard-l-standard-semibold-oblique.0hhyumsaypw9d.woff2?dpl=dpl_43vpACitAoHemg5zATXXCP8hBYHa) | 仅声明、待获取 |

FontKit读取是B文件表证据，不是页面glyph运行观察：GT M/L的regular/medium均UPM1000、ascent970/descent−190、964glyphs/728codepoints；Hark Serif light/medium为85glyphs、light-italic93glyphs，但均只有71codepoints，且无U+2019弯单引号。Serif UPM1000、ascent1000/descent−200、lineGap250。其CSS400对应light文件内部OS/2weight200，CSS700对应medium内部weight500。必须分别保留CSS请求、文件元数据与运行绑定，不能替换成一个重量数字。弯引号标题的实际fallback需要平台字体A取证。完整cmap和names/metrics见JSON fontFileInspection。

## 深化补证：Hark SVG声明与使用入口

以下11项保留原始factory/函数代码、SVG调用、shape字面量、每个shape字节范围和真实源SHA，位于JSON svgDeclarations。它们都是B声明／C实例候选；不是新绘制SVG，也没有被称为原版当前运行截图。全23脚本的精确Hark data-component检索仅命中四个已解析UI chunk，Sanity vendor图标全集不进入此清单。

| 图标／标志 | viewBox | SVG调用字节区间（end exclusive） | 声明使用 |
| --- | --- | --- | --- |
| hark-logo | 0 0 37 33 | 1_4hko300imeg.js 9398–9567 | App/Hero/AppDownloadBanner/Footer logo calls; current instance binding needs DOM confirmation. |
| play | 0 0 24 24 | 15bfdjo3hjyv9.js 13079–13270 | Media/FloatingCta play affordance; not established as present in current home default state. |
| icon-error | 0 0 16 16 | 15bfdjo3hjyv9.js 31720–31895 | Auth error helper k; default homepage error state not observed. |
| google | 0 0 24 24 | 15bfdjo3hjyv9.js 32923–33125 | Google provider icon E used by App and SignUp auth. |
| icon-arrow-out | 0 0 14 14 | 15bfdjo3hjyv9.js 236390–236569 | JobListings Apply action helper sQ; not homepage runtime coverage. |
| checkmark | 0 0 16 16 | 15bfdjo3hjyv9.js 251870–252066 | Pricing plan feature list o3; not homepage runtime coverage. |
| app-download-dismiss | 0 0 16 16 | 406ziev2ozark.js 1856–2083 | Platform AppDownloadBanner close action; mobile eligibility not observed here. |
| cookie | 0 0 24 24 | 406ziev2ozark.js 4454–4658 | CookieBanner prompt; eligibility not observed in supplied home AX. |
| google-play | 0 0 24 24 | 406ziev2ozark.js 7483–7681 | Footer Play Store affordance. |
| hark-wordmark | 0 0 1019 343 | 406ziev2ozark.js 100670–100849 | Header homepageLink; current header visibility flag needs runtime. |
| apple | 0 0 24 24 | 42be7a_n4bgx7.js 120561–120753 | Google/Apple provider E and Footer/App Store glyph. |

Google provider的App变体额外将viewBox改为4 4 16 16；Apple使用3×auth-unit的外框，通用provider图标框是1.8×unit。这是来源的几何差异。Footer商店glyph、品牌logo/wordmark与错误/Apply/checkmark各自归属，不把它们互换。动态媒体和登录App的其余图标仍需对应原版制品补证。

## 深化补证：精确状态与动效控制器

JSON uiControllerDeclarations现保存85个精确function/class原片段，其中包括24个section注册项。下表列出影响当前UI的主要控制器；每项有sourceText及字节边界，其他可选section仍为B。

| 控制器 | 文件／字节区间 | 用途 |
| --- | --- | --- |
| e3 | 15bfdjo3hjyv9.js 154716–156233 | 认证状态／重入／标识验证 |
| e9 | 15bfdjo3hjyv9.js 156252–157508 | 上方空值禁用策略 |
| de | 15bfdjo3hjyv9.js 256771–258408 | 下方busy／错误策略 |
| FeatureVideoCarousel | 15bfdjo3hjyv9.js 350829–363521 | 3D轨道／prompt／手势／可视性／视频生命周期 |
| r4 | 15bfdjo3hjyv9.js 180844–181160 | 半步弹簧积分 |
| r9 | 15bfdjo3hjyv9.js 181160–181521 | 49点linear sampler／fallback |
| tN | 15bfdjo3hjyv9.js 163612–166824 | 透明视频frame-confirmed与poster回退 |
| Header | 406ziev2ozark.js 101346–105088 | 菜单／重入／focus trap／重叠 |
| useCookieConsent | 406ziev2ozark.js 3073–3261 | consent订阅与accept/decline入口 |
| Link | 42be7a_n4bgx7.js 96067–97120 | 同路径anchor／top跳转 |
| v | 42be7a_n4bgx7.js 229496–231931 | sky-frame-scheduler |
| R | 42be7a_n4bgx7.js 233476–240527 | sky boot／dispatch／mirrors／placeholder |

原版研究仍partial：正常登录当前受阻；这份B原片段和任何D切片都不会把登录UI、完整原版icon集合或运行中断状态提升为ready。
