# Hark 原版 App 历史字体、图标与 UI 代码资源

本目录只取自 H15 原版保存的 CSS、DOM 和发布脚本。原采集日期为上海2026-10-15；资源页窗口保留UTC2026-10-14T16:52:40.588Z–16:52:42.901Z。此次是2026-10-22的静态复核和derived抽取，没有GUI、浏览器、网络、登录、配置、原件或克隆修改，也没有用D克隆资源补作原版。

读取的总库提交为d7f718d10558a32cf378b1d30bc279538fa3bf12，旧来源revision为b5b49eb46494b86d56c21654ea585ad379d9b9d7。原JS是捕获保留的Debugger源码UTF8，不等于完整部署源代码/源映射；每个资源保留旧URL、build文件名、源SHA、精确Unicode/UTF8字节范围和抽取SHA。

机器入口：[historical-app-assets.json](historical-app-assets.json)。原版来源边界、所有生成成员和逐资源绑定都在JSON内；本说明不代替这些限定。

## 已恢复的实际资源

| 项目 | 数量 | 可核验边界 |
| --- | ---: | --- |
| 原font-face声明 | 33 | 19个规范族/回退声明 + 14个旧别名；保存逐声明原CSS字节 |
| 唯一远程字体URL | 21 | 原src声明；当前访问/字节身份未请求 |
| H15字体二进制 | 0 | 原249成员没有WOFF/WOFF2/TTF/OTF；loaded不能恢复原字体文件 |
| 历史computed字体绑定 | 108 | 只保留字体/行高/字距及JSON字段出处，不复制账号/消息文字 |
| 命名glyph | 74 | 原main的cv表 + lv包装；解码原字符串并注明xmlns序列化添加 |
| 原DOM SVG | 17个字节变体 / 50次出现 | 三份原DOM的精确SVG字节；原组件/状态祖先关联另存 |
| 内嵌data-URI SVG | 8 | 仅完整静态literal解码；动态生成前缀不算原件 |
| SVG文件合计 | 105 | 以上资源加6个完整SVG字符串；XML均可解析，不等于当前像素验收 |
| SVG工厂代码 | 124 | 原始JS函数字节、root/shape属性、默认参数、调用候选；不把动态children扁平化 |
| 原UI精确片段 | 32 | 复用H15原定位/哈希/REA ID，按原字节再存 |
| 已解析资源owner | 11 | 先复用已知owner，再按一次asset marker清单补5个owner |
| 原脚本成员inventory | 36 | 不是整个站点脚本完整性证明；4个通用SVG marker来源仍未恢复固定资源 |
| 设置入口 / 缺失lazy页面 | 27 / 20 | 27个inline JSX入口，20个screen chunk没有保存；其他nested/index/guarded形式保留原子树 |
| 逐文件derived成员 | 432 | 全部源范围/字节/SHA见generatedFiles；不包含原字体二进制 |

## 字体声明与真实绑定

规范命名使用GT Standard L300/400/500/600/700及italic、GT Standard Mono400/500、Hark Serif300/500及italic、GT Standard L Fallback400/500/600。旧cTNWVXWS.css还保留ivar-display、gt-standard-m-standard、gt-standard-l-standard和hark-serif别名。

必须保留family与CSS作用域：旧gt-standard-m-standard700指向Medium，旧gt-standard-l-standard700也指向Medium；这是真实声明，不能统一改成600或把别名当规范族。规范GT Standard L Semibold仍是600。文件名、CSS注册字重与字体name table是三个不同事实；H15没有二进制name table可核。

| 原family | weight / style | 原字体文件或local来源 | 原声明抽取 |
| --- | --- | --- | --- |
| GT Standard L | 300 / normal | GT-Standard-L-Standard-Light.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-300-normal-c8173896df89.css) |
| GT Standard L | 300 / italic | GT-Standard-L-Standard-Light-Oblique.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-300-italic-eff785d91517.css) |
| GT Standard L | 400 / normal | GT-Standard-L-Standard-Regular.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-400-normal-96a35b701459.css) |
| GT Standard L | 400 / italic | GT-Standard-L-Standard-Regular-Oblique.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-400-italic-6b3b9baa9c0b.css) |
| GT Standard L | 500 / normal | GT-Standard-L-Standard-Medium.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-500-normal-edab5b0a6955.css) |
| GT Standard L | 500 / italic | GT-Standard-L-Standard-Medium-Oblique.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-500-italic-a7dec56d8064.css) |
| GT Standard L | 600 / normal | GT-Standard-L-Standard-Semibold.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-600-normal-6b60c4c80207.css) |
| GT Standard L | 600 / italic | GT-Standard-L-Standard-Semibold-Oblique.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-600-italic-ccece1a75fac.css) |
| GT Standard L | 700 / normal | GT-Standard-L-Standard-Bold.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-700-normal-ac615261ca45.css) |
| GT Standard L | 700 / italic | GT-Standard-L-Standard-Bold-Oblique.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-700-italic-1ed28fa2f898.css) |
| GT Standard Mono | 400 / normal | GT-Standard-Mono-Standard-Regular.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-Mono-400-normal-ba3e43cdec6e.css) |
| GT Standard Mono | 500 / normal | GT-Standard-Mono-Standard-Medium.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-Mono-500-normal-fbe33aa03825.css) |
| Hark Serif | 300 / normal | HarkSerif-Light.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-Hark-Serif-300-normal-a6fe0f391753.css) |
| Hark Serif | 300 / italic | HarkSerif-LightItalic.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-Hark-Serif-300-italic-b90294e84380.css) |
| Hark Serif | 500 / normal | HarkSerif-Medium.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-Hark-Serif-500-normal-78c51921dc6b.css) |
| Hark Serif | 500 / italic | HarkSerif-MediumItalic.woff2 | [CSS](historical-assets/font-declarations/B9bSKZIz-Hark-Serif-500-italic-f3ceeae05282.css) |
| GT Standard L Fallback | 400 / normal | local("Helvetica Neue"),local("Arial") | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-Fallback-400-normal-366c9af6e145.css) |
| GT Standard L Fallback | 500 / normal | local("Helvetica Neue"),local("Arial") | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-Fallback-500-normal-3d0209038c7c.css) |
| GT Standard L Fallback | 600 / normal | local("Helvetica Neue"),local("Arial") | [CSS](historical-assets/font-declarations/B9bSKZIz-GT-Standard-L-Fallback-600-normal-b0398bb1adc4.css) |
| ivar-display | 400 / normal | ivar-display-regular.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-ivar-display-400-normal-4896a16fd58f.css) |
| gt-standard-m-standard | 400 / normal | GT-Standard-M-Standard-Regular.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-m-standard-400-normal-47b7abdd0745.css) |
| gt-standard-m-standard | 400 / italic | GT-Standard-M-Standard-Regular-Oblique.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-m-standard-400-italic-3fc9d0db7620.css) |
| gt-standard-m-standard | 700 / normal | GT-Standard-M-Standard-Medium.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-m-standard-700-normal-48c1f9cbf773.css) |
| gt-standard-m-standard | 700 / italic | GT-Standard-M-Standard-Medium-Oblique.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-m-standard-700-italic-228a57b2f270.css) |
| gt-standard-l-standard | 400 / normal | GT-Standard-L-Standard-Regular.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-l-standard-400-normal-596b77027f8e.css) |
| gt-standard-l-standard | 400 / italic | GT-Standard-L-Standard-Regular-Oblique.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-l-standard-400-italic-9aa7f83696e0.css) |
| gt-standard-l-standard | 500 / normal | GT-Standard-L-Standard-Medium.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-l-standard-500-normal-aafa20ed4211.css) |
| gt-standard-l-standard | 600 / normal | GT-Standard-L-Standard-Semibold.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-l-standard-600-normal-660b55bbb2b0.css) |
| gt-standard-l-standard | 700 / normal | GT-Standard-L-Standard-Medium.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-l-standard-700-normal-dfc6eb5707e5.css) |
| gt-standard-l-standard | 700 / italic | GT-Standard-L-Standard-Medium-Oblique.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-gt-standard-l-standard-700-italic-267d70214227.css) |
| hark-serif | 300 / normal | HarkSerif-Light.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-hark-serif-300-normal-512bc1aae791.css) |
| hark-serif | 300 / italic | HarkSerif-LightItalic.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-hark-serif-300-italic-d773bc93e449.css) |
| hark-serif | 500 / normal | HarkSerif-Medium.woff2 | [CSS](historical-assets/font-declarations/cTNWVXWS-hark-serif-500-normal-9cbce549af79.css) |

21个完整原URL见JSON fontCoverage.remoteFontUrls。字体文件本轮没有抓取；这项需要root正常取得文件后用本轮采集时间和SHA登记，不能填成2026-10-15原字节。

历史document.fonts有33条，其中9条loaded（含3条local fallback）。loaded只描述字体资源状态。108个computed样本分别来自16:55:42.653Z、16:59:54.737Z和17:29:39.582Z；上海日期都是10月15日，不把UTC10月14日另算一轮。

可实施的已观测角色仍是：普通正文/输入GT Standard L15px/19px/400，专用代码GT Standard Mono14/20/400，摘要标题Hark Serif40/52/500、正文L18/25.2/400、strong600。引用pre和普通code card分开；单个CJK字符的实际fallback仍未知，computed字体栈不证明每个glyph由第一字体绘制。

原DvrONiVz保留nt→ht→yt/bt的face绑定与ot角色预设，以及ft几何、dt材质、lt阴影/blur、ct扫光参数。[face映射](historical-assets/code/DvrONiVz-design-constant-ht-71c65be6bc06.raw.js)与[角色预设](historical-assets/code/DvrONiVz-design-constant-ot-67c38dcfc781.raw.js)均为原字节；预设1.3行高不替代实际正文19px。

## 全部74个原版命名glyph

原cv表提供完整path markup；lv默认size16、viewBox0 0 16 16、root fill none。多数path自己填currentColor。color可走primary/secondary/custom与mode映射，默认继承；源码有spread/style覆盖，不假称所有实例实际16px或同一色。以下是derived SVG包装，JSON同时保留字符串原范围、lv原范围、源码文件SHA和序列化变化。

| 原glyph键 | 原几何SVG |
| --- | --- |
| account | [SVG](historical-assets/svg/glyph-account.svg) |
| add | [SVG](historical-assets/svg/glyph-add.svg) |
| alert | [SVG](historical-assets/svg/glyph-alert.svg) |
| apps | [SVG](historical-assets/svg/glyph-apps.svg) |
| attachment | [SVG](historical-assets/svg/glyph-attachment.svg) |
| back | [SVG](historical-assets/svg/glyph-back.svg) |
| calendar | [SVG](historical-assets/svg/glyph-calendar.svg) |
| card-1 | [SVG](historical-assets/svg/glyph-card-1.svg) |
| card | [SVG](historical-assets/svg/glyph-card.svg) |
| chat | [SVG](historical-assets/svg/glyph-chat.svg) |
| check | [SVG](historical-assets/svg/glyph-check.svg) |
| chevron | [SVG](historical-assets/svg/glyph-chevron.svg) |
| close | [SVG](historical-assets/svg/glyph-close.svg) |
| computer | [SVG](historical-assets/svg/glyph-computer.svg) |
| copy | [SVG](historical-assets/svg/glyph-copy.svg) |
| dot | [SVG](historical-assets/svg/glyph-dot.svg) |
| down | [SVG](historical-assets/svg/glyph-down.svg) |
| download | [SVG](historical-assets/svg/glyph-download.svg) |
| drop | [SVG](historical-assets/svg/glyph-drop.svg) |
| edit | [SVG](historical-assets/svg/glyph-edit.svg) |
| enter-1 | [SVG](historical-assets/svg/glyph-enter-1.svg) |
| enter | [SVG](historical-assets/svg/glyph-enter.svg) |
| enterright | [SVG](historical-assets/svg/glyph-enterright.svg) |
| expand | [SVG](historical-assets/svg/glyph-expand.svg) |
| eye | [SVG](historical-assets/svg/glyph-eye.svg) |
| file | [SVG](historical-assets/svg/glyph-file.svg) |
| fingerprint | [SVG](historical-assets/svg/glyph-fingerprint.svg) |
| folder | [SVG](historical-assets/svg/glyph-folder.svg) |
| gear | [SVG](historical-assets/svg/glyph-gear.svg) |
| heart | [SVG](historical-assets/svg/glyph-heart.svg) |
| home | [SVG](historical-assets/svg/glyph-home.svg) |
| info | [SVG](historical-assets/svg/glyph-info.svg) |
| key | [SVG](historical-assets/svg/glyph-key.svg) |
| keyboard | [SVG](historical-assets/svg/glyph-keyboard.svg) |
| link-out | [SVG](historical-assets/svg/glyph-link-out.svg) |
| link | [SVG](historical-assets/svg/glyph-link.svg) |
| list | [SVG](historical-assets/svg/glyph-list.svg) |
| location | [SVG](historical-assets/svg/glyph-location.svg) |
| lock | [SVG](historical-assets/svg/glyph-lock.svg) |
| mail | [SVG](historical-assets/svg/glyph-mail.svg) |
| map | [SVG](historical-assets/svg/glyph-map.svg) |
| memory | [SVG](historical-assets/svg/glyph-memory.svg) |
| mic | [SVG](historical-assets/svg/glyph-mic.svg) |
| minimize | [SVG](historical-assets/svg/glyph-minimize.svg) |
| more | [SVG](historical-assets/svg/glyph-more.svg) |
| new | [SVG](historical-assets/svg/glyph-new.svg) |
| next | [SVG](historical-assets/svg/glyph-next.svg) |
| pause | [SVG](historical-assets/svg/glyph-pause.svg) |
| person | [SVG](historical-assets/svg/glyph-person.svg) |
| photo | [SVG](historical-assets/svg/glyph-photo.svg) |
| photos | [SVG](historical-assets/svg/glyph-photos.svg) |
| pin | [SVG](historical-assets/svg/glyph-pin.svg) |
| play | [SVG](historical-assets/svg/glyph-play.svg) |
| point | [SVG](historical-assets/svg/glyph-point.svg) |
| prev | [SVG](historical-assets/svg/glyph-prev.svg) |
| refresh | [SVG](historical-assets/svg/glyph-refresh.svg) |
| reply | [SVG](historical-assets/svg/glyph-reply.svg) |
| research | [SVG](historical-assets/svg/glyph-research.svg) |
| robot | [SVG](historical-assets/svg/glyph-robot.svg) |
| schedule | [SVG](historical-assets/svg/glyph-schedule.svg) |
| search | [SVG](historical-assets/svg/glyph-search.svg) |
| security | [SVG](historical-assets/svg/glyph-security.svg) |
| share | [SVG](historical-assets/svg/glyph-share.svg) |
| sidebar | [SVG](historical-assets/svg/glyph-sidebar.svg) |
| sign-out | [SVG](historical-assets/svg/glyph-sign-out.svg) |
| stop | [SVG](historical-assets/svg/glyph-stop.svg) |
| text-select | [SVG](historical-assets/svg/glyph-text-select.svg) |
| thumbs-down | [SVG](historical-assets/svg/glyph-thumbs-down.svg) |
| thumbs-up | [SVG](historical-assets/svg/glyph-thumbs-up.svg) |
| trash | [SVG](historical-assets/svg/glyph-trash.svg) |
| up | [SVG](historical-assets/svg/glyph-up.svg) |
| voice | [SVG](historical-assets/svg/glyph-voice.svg) |
| volume | [SVG](historical-assets/svg/glyph-volume.svg) |
| web | [SVG](historical-assets/svg/glyph-web.svg) |

## 原DOM中的组件与状态关联

下面保留原祖先CSS类、role、data-testid和expanded/pressed/selected/layout等状态。账号/消息ID与私人文字不复制。每个SVG还与原JS工厂的几何字段比较；相同geometry只是候选身份，不证明唯一constructor、当前运行或presentation相同。

| SVG | 原viewBox / width×height | 控件/状态线索 | 匹配原工厂候选 |
| --- | --- | --- | --- |
| [c382d8b402](historical-assets/svg/dom-c382d8b4027636645ddd.svg) | 0 0 16 16 / 16×16 | aria-expanded=true | Lpe |
| [1de874c59d](historical-assets/svg/dom-1de874c59d58dc662d4d.svg) | 0 0 24 24 / 20×20 | 原CSS/祖先见JSON | Lj |
| [2ce932aba4](historical-assets/svg/dom-2ce932aba4aa84ea3f6d.svg) | 0 0 16 16 / 16×16 | aria-expanded=false; data-testid=flow-row-content | tL |
| [6fa8f93e8e](historical-assets/svg/dom-6fa8f93e8ee01ab4b896.svg) | 0 0 16 16 / 16×16 | data-testid=flow-row-content | ide |
| [bcc6fb2650](historical-assets/svg/dom-bcc6fb265082fe378d59.svg) | 0 0 16 16 / 16×16 | data-testid=message-list; data-testid=code-card; genericAriaLabel=回复 | x4 |
| [d591e7c5c2](historical-assets/svg/dom-d591e7c5c2f9e58936dd.svg) | 0 0 16 16 / 16×16 | data-testid=message-list; data-testid=rich-content-photos; data-layout=pile; genericAriaLabel=Show all photos; aria-expanded=false | K3 |
| [44e2af2ce7](historical-assets/svg/dom-44e2af2ce7e520eb5c48.svg) | 0 0 16 16 / 16×16 | 原CSS/祖先见JSON | hB |
| [f4f5e2dd13](historical-assets/svg/dom-f4f5e2dd13309f3a852e.svg) | 0 0 16 16 / 13×13 | 原CSS/祖先见JSON | jn |
| [edd559c0ac](historical-assets/svg/dom-edd559c0ac24a4d6e621.svg) | 0 0 16 16 / 12×12 | 原CSS/祖先见JSON | jn |
| [9c06884a53](historical-assets/svg/dom-9c06884a53703f46ba6b.svg) | 0 0 16 16 / 16×16 | 原CSS/祖先见JSON | jn |
| [60e491b794](historical-assets/svg/dom-60e491b7947480e8f3f7.svg) | 0 0 16 16 / 16×16 | data-testid=code-card; genericAriaLabel=展开; data-testid=card-disclosure; aria-expanded=false; data-testid=reply-quote | _oe |
| [b6cca489b3](historical-assets/svg/dom-b6cca489b3d8df9b8a96.svg) | 0 0 16 16 / 16×16 | 原CSS/祖先见JSON | jn |
| [b58080af34](historical-assets/svg/dom-b58080af347fdb76b6ee.svg) | 0 0 16 16 / 16×16 | data-testid=message-list; data-testid=message-bubble; data-testid=website-card; data-testid=website-open; data-testid=code-card; genericAriaLabel=展开; data-testid=card-disclosure; aria-expanded=false; data-testid=reply-quote | K3 |
| [cbd2935d83](historical-assets/svg/dom-cbd2935d83a43f505466.svg) | 0 0 16 16 / 16×16 | data-testid=message-list; data-testid=message-bubble | sB |
| [630bb97d89](historical-assets/svg/dom-630bb97d8935c418395e.svg) | 0 0 16 16 / 16×16 | aria-expanded=false | ide |
| [27f0cadf7c](historical-assets/svg/dom-27f0cadf7c1b524ecd48.svg) | 0 0 16 16 / 16×16 | data-testid=send-button | SF |
| [1e605385df](historical-assets/svg/dom-1e605385df69e1fb64fb.svg) | 0 0 16 16 / 16×16 | genericAriaLabel=关闭 | Cd |

17个DOM变体均有几何相同的已保留工厂候选。root和shape的viewBox、d、circle/rect、stroke、fill、linecap/linejoin、transform、clip等属性逐元素在svgGeometry中列出。SVG工厂另保留124份原代码和非literal属性清单；当前Color/CSS/variant/反应式尺寸未知时保持unknown，不用近似图标替换。

## 内嵌品牌与其它SVG

| 来源上下文 | SVG | 来源 |
| --- | --- | --- |
| doordash, mastercard, amex, discover | [SVG](historical-assets/svg/data-uri-BT2rDYKB-eedd012d42ee459a.svg) | BT2rDYKB.js |
| Delta | [SVG](historical-assets/svg/data-uri-BT2rDYKB-96398424a8f11fd8.svg) | BT2rDYKB.js |
| visa, amex | [SVG](historical-assets/svg/data-uri-BT2rDYKB-b3c052ec86f12b65.svg) | BT2rDYKB.js |
| mastercard | [SVG](historical-assets/svg/data-uri-BT2rDYKB-bb1ff48a731d60a0.svg) | BT2rDYKB.js |
| amex | [SVG](historical-assets/svg/data-uri-BT2rDYKB-9cd91224ace0a38d.svg) | BT2rDYKB.js |
| discover | [SVG](historical-assets/svg/data-uri-BT2rDYKB-7aa3c40c4ff1db36.svg) | BT2rDYKB.js |
| 原工具/renderer内嵌资源 | [SVG](historical-assets/svg/data-uri-main-DxHtaWdF-36e2ae30091f8948.svg) | main-DxHtaWdF.js |
| 原工具/renderer内嵌资源 | [SVG](historical-assets/svg/data-uri-BFQtfC87-2e81295d9c8687cd.svg) | BFQtfC87.js |

doordash、Delta及visa/mastercard/amex/discover来自原静态map或literal上下文，保存完整几何与颜色；不证明本轮曾执行订单、航班或支付。动态SVG转PNG前缀、borderColor生成SVG和运行时HTML/widget不冒充已恢复完整文件。

## 内置组件、菜单与行为代码入口

以下32个文件与旧精确片段字节完全一致，sourcePage Evidence ID为ev_4d515902647d4a1d0224c3da682c34a39f9ed44ca7a978ac725a13eddf73e13f，sourceExport Evidence ID为ev_f59cba60a4e30603b5f93e546735322e749383df1dc84d0c3d9c761afd214537。源码发布与旧原站操作分别保留，不把客户端分发字段写成服务端完整schema。

| 原定位id | 原字节代码 |
| --- | --- | --- |
| cards-normalization | [源片段](historical-assets/code/core-cards-normalization.raw.js) |
| cards-render-dispatch | [源片段](historical-assets/code/core-cards-render-dispatch.raw.js) |
| links-preview-pipeline | [源片段](historical-assets/code/core-links-preview-pipeline.raw.js) |
| link-stack-counts-expansion | [源片段](historical-assets/code/core-link-stack-counts-expansion.raw.js) |
| link-stack-drag-flip | [源片段](historical-assets/code/core-link-stack-drag-flip.raw.js) |
| link-stack-grid-motion | [源片段](historical-assets/code/core-link-stack-grid-motion.raw.js) |
| card-reply-context-expand | [源片段](historical-assets/code/core-card-reply-context-expand.raw.js) |
| card-code | [源片段](historical-assets/code/core-card-code.raw.js) |
| card-summary-modal | [源片段](historical-assets/code/core-card-summary-modal.raw.js) |
| card-text-template | [源片段](historical-assets/code/core-card-text-template.raw.js) |
| card-swipe-reply | [源片段](historical-assets/code/core-card-swipe-reply.raw.js) |
| stream-reveal-buffer | [源片段](historical-assets/code/core-stream-reveal-buffer.raw.js) |
| stream-sse-owner | [源片段](historical-assets/code/core-stream-sse-owner.raw.js) |
| stream-message-patch-reducer | [源片段](historical-assets/code/core-stream-message-patch-reducer.raw.js) |
| stream-records-compat | [源片段](historical-assets/code/core-stream-records-compat.raw.js) |
| stream-feed-manager | [源片段](historical-assets/code/core-stream-feed-manager.raw.js) |
| stream-manager-link | [源片段](historical-assets/code/core-stream-manager-link.raw.js) |
| stream-tab-sync | [源片段](historical-assets/code/core-stream-tab-sync.raw.js) |
| scroll-pinning | [源片段](historical-assets/code/core-scroll-pinning.raw.js) |
| stop-tool-optimistic | [源片段](historical-assets/code/core-stop-tool-optimistic.raw.js) |
| activity-phases | [源片段](historical-assets/code/core-activity-phases.raw.js) |
| website-artifact-preview | [源片段](historical-assets/code/core-website-artifact-preview.raw.js) |
| file-viewer-dispatch | [源片段](historical-assets/code/core-file-viewer-dispatch.raw.js) |
| markdown-component-map | [源片段](historical-assets/code/core-markdown-component-map.raw.js) |
| chat-markdown-config | [源片段](historical-assets/code/core-chat-markdown-config.raw.js) |
| chat-live-text-preparation | [源片段](historical-assets/code/core-chat-live-text-preparation.raw.js) |
| chat-text-rendering | [源片段](historical-assets/code/core-chat-text-rendering.raw.js) |
| table-card-model | [源片段](historical-assets/code/core-table-card-model.raw.js) |
| html-file-preview | [源片段](historical-assets/code/core-html-file-preview.raw.js) |
| activity-stop-confirmation | [源片段](historical-assets/code/core-activity-stop-confirmation.raw.txt) |
| api-base-url | [源片段](historical-assets/code/core-api-base-url.raw.js) |
| link-motion-preference | [源片段](historical-assets/code/core-link-motion-preference.raw.js) |

可直接复用的原逻辑包括presentation归一化/renderer、summary独立dialog、code/template溢出判定、回复/菜单/长按290ms、链接图栈布局/展开/滚动锁、表格sort/CSV、文件viewer/HTML sandbox、活动phases/Stop确认回滚、流式文本及滚动。旧A只覆盖部分状态；当前表格曾三次点击未排序，工具Stop未执行，不提升为verified。

几何ft声明cardRadius24、padding20、按钮高度38/44/48、radius12、glyph16/10；ct声明扫光1.4s、streak84、angle−15、day/night opacity .85/.072。lt.harkBlur100是helper默认，实际composer旧computed为50，消息blur0；JS材质配方与CSS .3/.7/.618不能混成同一个最终值。

连续圆角[原算法](historical-assets/code/continuous-corner-algorithm.raw.js)保留24默认、1.528665曲线系数、ResizeObserver与尺寸驱动fill/stroke。它是源片段而非独立可直接执行模块；依赖/实例尺寸仍按原代码与DOM解释。

## 设置UI与缺失lazy成员

真实Settings子树：[route subtree](historical-assets/code/settings-route-subtree-fa0b374ddb46.raw.js)。App根Zve：[原根组件](historical-assets/code/app-root-Zve-27909a9c7b30.raw.js)。路由alias we是从BT2rDYKB的Q导入，已排除同名局部变量；不根据名字错误绑定到别的组件。

20个inline页面wrapper的真实chunk/导出如下。没有这些页面字节时，不能把规范模板或D克隆菜单写成原页源码。完整子树还含index/identifier/guarded路由；27条投影不是全站或所有权限路由总数。

| 原路由 | 原导出 | 缺失原chunk |
| --- | --- | --- |
| profile | ProfileSettingsScreen | ./D-FyrG8H.js |
| profile/password | ChangePasswordScreen | ./BNg-bYgv.js |
| system-preferences | SystemPreferencesScreen | ./CL335wWi.js |
| system-preferences/theme | ThemeSettingsScreen | ./CO6K1XLL.js |
| system-preferences/language | LanguageSettingsScreen | ./C952SVCg.js |
| system-preferences/data | DataManagementScreen | ./BuarI72O.js |
| help/report | ReportIssueScreen | ./03DQWFVF.js |
| help/about | AboutScreen | ./BMAxWeeU.js |
| connected-accounts | ConnectedAccountsScreen | ./DSZra1Ea.js |
| connected-accounts/add-login | AddLoginScreen | ./DWY8sn-g.js |
| connected-accounts/logins/:domain | ManagedLoginDetailScreen | ./DFD5A71_.js |
| connected-cards | ConnectedCardsScreen | ./CEjsbXeA.js |
| connected-cards/add | AddCardScreen | ./-q73BlzU.js |
| connected-cards/:secretId | CardDetailScreen | ./BlAnMcL9.js |
| security | SecuritySettingsScreen | ./CxYEciCx.js |
| security/sessions | ActiveSessionsScreen | ./Bq3JmtZ6.js |
| scheduled-tasks | ScheduledTasksScreen | ./CH2aj2ej.js |
| scheduled-tasks/:taskId | ScheduledTaskDetailScreen | ./B3VP5Yx6.js |
| billing | BillingScreen | ./BnxNOxPe.js |
| billing/auto-topup | AutoTopupScreen | ./BsXT42EJ.js |

原preload数组[150项原字节](historical-assets/code/module-preload-dependencies-ff80f5683527.raw.json)和各wrapper的索引已关联到preloadDependencies。仅保留原specifier；没有执行browser/import.meta或远程解析，不将同名文件的候选匹配提升为模块绑定证明。

## 新的有界静态REA回执

REA6.5.0只分析DvrONiVz.js与main-DxHtaWdF.js两个指定源；512MiB heap、每文件30s、每文件1100/总2200 semantic节点。实际两个文件解析成功、无parse failure；34个inventory成员未分析，application与semantic覆盖partial，semantic图truncated，110072个semantic unknown留在原父Evidence。不提高预算重复生产，也不把图中未返回关系叫不存在。

真实父Evidence：ev_2203a0563d5cf7b7f1c7b76c9c9a0ef162303bdbffe3f29d54a34c1c47851865。summary投影：ev_e239a4a9837ae1392f8cd45a2848623a2285eb5a20c845853d309314fb708f28；模块身份page：ev_c7c9558fbf79616664fdb37a9fe8f916f236063165d921b61ca98339b87ba433。原始回执和请求保存在JSON newStaticRea。它们是2026-10-22对旧字节的静态分析，不是当前原站A。

其余已知owner与asset marker新增owner使用已安装Babel parser的inert AST/literal解析，脚本不执行；不会把本地普通解析伪装成REA Evidence ID。

## 仍缺的原生信息与接续

- 21个字体URL的实际原文件；H15历史二进制不存在，root新取得的文件要保存本轮时间、来源和SHA。
- 20个缺失页面chunk及其UI依赖；只读路由/flag声明不证明账号权限、入口展示或页面执行。
- 运行时widget/生成HTML/SVG/外部metadata图像、不同状态与响应式生成的图形；现有固定资源清单不能穷尽这些资源。
- SVG的当前Color、CSS、spread props、cache/branch、尺寸与transform执行；source/default与实际running分别核验。
- 实际字符的fallback、当前原站字体绑定、关键状态/动效/键盘/触控；静态恢复不能替代root正常登录后的原站核验。

静态资产子任务完成。所需原件未补造，当前运行不宣称通过；原249成员保持原字节，所有新增成员只在historical-app-assets两份入口和historical-assets目录中。
