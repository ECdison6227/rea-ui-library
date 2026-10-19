# Hark 交互卡片与流式实现：已发布代码证据

日期：2026-10-15。范围：当前登录页面捕获的 JavaScript，**仅静态分析**；本文未操纵浏览器，未把模型生成的 Markdown、HTML、JSON 或提示文本当作原生卡片证据。运行时截图、点击和实际网络观察应以主报告的独立证据为准。

## 证据身份与可复查方式

源页面 REA Evidence ID：`ev_4d515902647d4a1d0224c3da682c34a39f9ed44ca7a978ac725a13eddf73e13f`。脚本导出 Evidence ID：`ev_f59cba60a4e30603b5f93e546735322e749383df1dc84d0c3d9c761afd214537`。导出依据见 [manifest](../evidence/scripts/hark-assets/manifest.json) 与 [REA bundle](../evidence/rea/03-assets-bundle.json)。

32 个精确片段，共 102,418 字节，见 [索引](../evidence/scripts/snippets/card-stream-snippet-index.json)。每个片段包含 `.raw.js` / `.raw.txt` 原始字节、`.readable.js` 可读版本、`.meta.json` 源文件 SHA-256、片段 SHA-256、Unicode code point 与 UTF-8 字节范围。范围均为 **0-based / end-exclusive**；可读版本经过 Prettier 格式化，原始定位以 `.raw.*` 与 metadata 为准。提取脚本：[extract-card-snippets.py](../evidence/scripts/extract-card-snippets.py)。可读代码保留压缩变量名，不是源映射还原。

| 源脚本 | 字节数 | SHA-256 |
|---|---:|---|
| BT2rDYKB.js | 1,716,548 | `03775ebceae2834e6556c5e136a6b46cf5b90050becd1ed31663dc303f762faa` |
| main-DxHtaWdF.js | 2,130,304 | `1779ffcf4dd3aae3db73cd2b11a2f6434c2218e674f5f6104363d935253e463a` |
| DovrAg42.js | 11,442 | `b63c10cd569d9430c5b2a340034ac116254bd3084af4ab2b6c8657a751768233` |
| BpSmQN_8.js | 59,110 | `7b425166b363571f02038fed800b22e69dfdfc0dd8a51e326f25c47386561e57` |
| Bz0NRe01.js | 287081 | `cab867f43b460df83f3d75b08ce3135fb9b90f0a1beb3dc18a37fcd6461d6272` |

导出仅包括捕获保留的脚本；不包含原始网络 body、cookie/storage value、源映射或所有 worker。捕获在附加时才开始，部分 origin 与证据字段被策略过滤。脚本存在证明分支已发布，**不能证明本次运行进入该分支**。

## 原生卡片分发与数据形态

核心链路：`message → HO(message) → PO[presentation.kind] / Markdown fallback → Zge[normalized.kind]`。这是一套真实 renderer 映射，不是从聊天样本文字猜测。`HO` 仅对 assistant 进行富内容分类；没有文本且仅图片附件时先走 `attached_images`。显式 presentation 归一化成功优先，否则尝试链接、完整 code fence、单个 Markdown table 等识别。证据：[归一化](../evidence/scripts/snippets/cards-normalization.readable.js)、[render dispatch](../evidence/scripts/snippets/cards-render-dispatch.readable.js)。归一化片段源字符范围 `[490226,495690)`；分发 `[1640735,1642681)`。

| 归一化 kind | 真实数据形态 / 产生方式 | renderer / 静态交互 |
|---|---|---|
| `text` | 常规 content；未满足专用格式 | 原生富卡分发返回 null，由聊天文本层渲染 |
| `link` / `links` | URL；`links[] = {url,label?,title?,image?}`，过滤无效 URL / 去重 | `epe`；metadata 异步更新、1/2/3/≥4 各自布局、外链 / 展开 |
| `images` | `images[] = {url,caption?}`；或仅含 Markdown 图片的内容 | `Ude`；图片尺寸解析、栈布局与图库 |
| `attached_images` | 无正文、附件全部是可显示图片 | `Kde`；附件解析为图片后共用图库 |
| `summary` | `summary = {markdown,title?,source?}`，正文 trim 后非空 | `Gge`；压缩预览，点击 / Enter / Space 打开 summary 状态与详细视图 |
| `code` | `code = {source,language?,title?}`；或完整 fenced code | `$pe`；`<pre>`、实际溢出检测、展开 / 折叠、复制 / 回复 |
| `text_template` | `text_template = {body,title?}` | `Kge`；`<pre>` 保留格式、展开 / 折叠、复制 / 回复 |
| `table` | `table = {title?,columns[],rows[][],ornament?,row_sites?}`；或单表 Markdown | `Sme`；测量、裁切、排序、CSV、回复 |
| `product_grid` | table 某列有效 Markdown 图片，非空值均为图片 | `xge`；当前 dispatch 的 `as="list"`，不可仅凭名字认定展示网格 |
| `flight_list` | table 存在 flights / flight number 等标题列，行值可识别航空公司 / 航班 | `Fme`；价格、链接、airline、其他字段转成航班列表 |
| `order_summary` | merchant、line_items、total、destination、breakdown、payment、legs、passengers、optional action(id,label) | `uge`；订单 / 行程摘要及 optional action |
| `declined` | `messageIds[]`、presentation title / subtitle | `Gpe`；关联消息可展开显示拒绝原因 |

这是从消费者代码归纳的字段形态，**不是完整服务端协议 schema**。URL 校验、trim、空数组检查等能证明客户端归一化要求；不能据此断言服务端只接受这些字段。`product_grid`、`flight_list` 是客户端 table 识别后的 kind，不必要求服务器直接下发同名 presentation。

`plan`、`todo` 未在上述 renderer 分发表中找到。`document` 虽出现在 main 的 presentation 统计 allowlist，未在已捕获的 `PO/Zge` 中找到对应专用 renderer，不能仅用 allowlist 断言 document 原生卡片已出现。

## 链接栈、卡片回复与详情交互

链接数据 `epe` 会先结合显式 title / label / image 与 preview 缓存构建卡片，再异步补入 metadata、记住失效图片并更新原有栈。图源逐级回退；单个没有图片的链接可采用 `face="short"`。证据：[links-preview-pipeline](../evidence/scripts/snippets/links-preview-pipeline.readable.js)。

`eP` 的 `dataset.layout`：1 张 `one`、2 张 `two`、3 张 `three`、其余 `pile`。pile 才创建 “Show all links/photos” 按钮。展开会建立同高度 holder，把原栈移入 overlay，设置 `is-spread` / `aria-expanded=true`，transcript 标记 `is-behind`，scroller 标记 `is-locked`，清除其他 scope。收起通过 `stack:collapse` 恢复 DOM 位置、aria 与滚动；overlay 外部 pointerdown 或 Escape 可触发收起。展开目标是两列布局，随视口按比例缩放，resize 120ms 后重新计算。证据：[计数与展开](../evidence/scripts/snippets/link-stack-counts-expansion.readable.js)、[网格动画](../evidence/scripts/snippets/link-stack-grid-motion.readable.js)。

顶层 `data-depth=0` 卡片接收 pointer drag；普通短点击移动 ≤6px 时打开 URL / 图片。偏移经 log 阻尼，写入 `--push-*` / `--cast-*`；翻动阈值为归一化位移 ≥0.55 或释放速度 ≥600px/s，动画使用分片 mesh 与 spring。`mj` 的定义为 `prefers-reduced-motion: reduce`，翻动分支在该偏好下禁用。它不是 touch / coarse-pointer 判定。证据：[拖动翻牌](../evidence/scripts/snippets/link-stack-drag-flip.readable.js)；[motion preference 定义](../evidence/scripts/snippets/link-motion-preference.readable.js)。

普通 card 共用 `Fpe(messageId)` 回复：保存回复动画来源，然后设置 composer reply target `{type:"message",messageId}`。`Bpe` 构建 context menu，支持复制、optional 下载、回复、展开 / 折叠；点击 a/input/textarea/select 与正在选择文本时不抢占。长按 helper 默认 290ms。`Xpe` 包装 swipe reply；表格横向有实际溢出时避开表格滚动目标。证据：[回复与菜单](../evidence/scripts/snippets/card-reply-context-expand.readable.js)、[swipe](../evidence/scripts/snippets/card-swipe-reply.readable.js)。

code / text template 使用 `scrollHeight > clientHeight + 1` 判断溢出；每张卡 `expanded` 初值 false，非溢出不展示多余展开操作。summary 预览最多保留 5 个非空 Markdown block，预览去除图片与可点击链接；点击主体在没有文本选区时写入 `lde.summary`，source link 独立 stopPropagation。证据：[code](../evidence/scripts/snippets/card-code.readable.js)、[summary](../evidence/scripts/snippets/card-summary-modal.readable.js)、[text template](../evidence/scripts/snippets/card-text-template.readable.js)。

表格 card model 维护 sort、order、clip、grid/scroll shape；点击排序依次 `desc → asc → null`，菜单提供按当前 order 生成 Markdown / HTML / CSV。它通过测量和字体加载事件决定 grid 或 scroll，不能从表格截图静态推断所有屏幕尺寸下的样式。证据：[table model](../evidence/scripts/snippets/table-card-model.readable.js)。

## 文件、HTML 与活动计划属于其他渲染层

文件查看器 `CI` 按 `kind.viewer` 分发：image、video、audio、pdf、docx、pptx、spreadsheet、delimited、markdown、html、code、text、unsupported。因此文件预览能力真实存在，不能把聊天普通 Markdown 文档当作其 UI 已执行。证据：[文件查看器](../evidence/scripts/snippets/file-viewer-dispatch.readable.js)。

HTML 文件 `html → JC → QC` 获取内容后使用 `iframe sandbox="allow-scripts" srcDoc=content`，按钮切换到 HTML source viewer。该证据证明文件层存在 HTML 预览 / 源码切换，未证明聊天 `presentation.kind="html"`。Markdown 消息 renderer 配置 `skipHtml:true`，两者行为不同。证据：[HTML 文件预览](../evidence/scripts/snippets/html-file-preview.readable.js)、[聊天 Markdown](../evidence/scripts/snippets/chat-markdown-config.readable.js)。

网站 artifact 有独立 `website-card` / `website-hero` renderer；只有 projectId 存在时允许打开，点击时无文本选区才使用 toolUseId / dispatchId 进入项目预览，图片加载失败会隐藏 hero。证据：[网站 artifact](../evidence/scripts/snippets/website-artifact-preview.readable.js)。

活动计划取自当前 tool panel 的 `phases`。`Ioe` 计算 completed count / total，`voe` 展示可关闭的 phases overlay。这证明原生活动计划 UI，与聊天内容写出的待办清单不是同一种数据源。证据：[活动 phases](../evidence/scripts/snippets/activity-phases.readable.js)。

Stop UI 先开启确认状态；确认后筛选 pending / running / waiting_for_input / awaiting_input 的 dispatchId，对每个调用 `AM`。`AM` 先乐观写 panel.status=cancelled 或 message.jobStatus=cancelled + isStreaming=false，再执行 `messages.stopTool.mutate({conversationId,dispatchId})`；失败回滚。已确认的是 tool cancel 路径，未在该范围建立“任意文本生成全局 Stop”协议。证据：[确认片段](../evidence/scripts/snippets/activity-stop-confirmation.raw.txt)、[取消 mutation](../evidence/scripts/snippets/stop-tool-optimistic.readable.js)。

## 流式网络、文本处理与自动滚动

已发布连接逻辑使用 **credentialed EventSource**。`Ore.connect` 构造 `new EventSource(url,{withCredentials:true})`；收到消息 / ping 重置 silence watchdog，error 后关闭当前 source、probe 状态并重连，401 验证 session loss，conversation feed 403 可停止。base helper `Y()` 是当前 origin + `/api`，因此 URL 为 `https://hark.com/api/sync/conversation?conversationId=…&v=2&manager=…&mode=…`；独立 user events 为 `https://hark.com/api/user/events?manager=…&mode=…`。默认 EventSource message 承载 JSON；另监听自定义 `ping` event，未找到专门 token 命名事件。证据：[API base](../evidence/scripts/snippets/api-base-url.readable.js)。连接默认尝试名为 `hark-connections` 的 SharedWorker，worker 被拒绝 / 无回应等时回退 `in_tab` manager。这不是默认 WebSocket token delta 机制。证据：[SSE owner](../evidence/scripts/snippets/stream-sse-owner.readable.js)、[manager link](../evidence/scripts/snippets/stream-manager-link.readable.js)。

conversation feed JSON.parse 后处理 `snapshot` 或 `patches`。snapshot 存储 records；patches 根据 `seq` 与原 patchSeq 查缺口，应用 records 后同步给 tabs。发生解析 / apply failure 或 seq gap 时限流 resync/redial；捕获 streaming assistant row 以统计 interruption。主 tab 同步再应用到 confirmed / pending projection。兼容旧 message_add 形式时转为 `entry_add(entry:"message")`。证据：[feed](../evidence/scripts/snippets/stream-feed-manager.readable.js)、[兼容层](../evidence/scripts/snippets/stream-records-compat.readable.js)、[tab](../evidence/scripts/snippets/stream-tab-sync.readable.js)。

`message_update` 合并 `fields` 到消息；`isStreaming=false` 时清除 thought / narration。因此静态代码证明流式状态通过 record patches 更新，**不能证明服务端每次发送的 content 是 token 片段还是累计完整文本，也未保存实际 SSE body**。证据：[patch reducer](../evidence/scripts/snippets/stream-message-patch-reducer.readable.js)。

普通聊天文本走 `a6 → n6 → P5 → Streamdown(B0)`。isStreaming 时去掉尾部未闭合 Markdown delimiter（如 code fence、强调）以及未闭合 `[` 后缀，phase 从 thinking 到 streaming 再 settled；最终 settled 配置带缓存 parser / block parsing。HTML 关闭、controls / lineNumbers 关闭；h1–h6、paragraph、blockquote、list、inline code、table 等映射到应用 class；图片与外链有专门 client。证据：[live text preparation](../evidence/scripts/snippets/chat-live-text-preparation.readable.js)、[text rendering](../evidence/scripts/snippets/chat-text-rendering.readable.js)、[Markdown component map](../evidence/scripts/snippets/markdown-component-map.readable.js)。

另有 `w2` RAF 字符 reveal helper，默认 stream 90 chars/s、reveal 30 chars/s、完成后的 drain multiplier 3、catch-up multiplier 4，每帧 dt 上限 100ms，支持 rewrite snap、document.hidden、instant 配置。已找到 callers 是 web codegen、terminal、web_fetch、web_search、write_file 等 tool 活动显示；**不可把该常量当作本次普通聊天气泡实际输出速率**。证据：[reveal buffer](../evidence/scripts/snippets/stream-reveal-buffer.readable.js)。

滚动 helper `bae` 判定 near bottom 为 difference ≤70px；锁定底部时 `overflow-anchor:none`，脱离后 auto。上滚 / 向上 wheel / 文本选区可退出锁定；内容 ResizeObserver 在仍锁定时补偿 tail row 位移并追随内容增长；detached / paused 支持避免强行追随。返回 scrollToBottom / stopScroll / isAtBottom 等状态给聊天容器使用。证据：[scroll pinning](../evidence/scripts/snippets/scroll-pinning.readable.js)。

## 尚未建立的证据

- 分发 / schema 覆盖的是本次捕获版本、可见脚本；其他权限、账号、lazy chunk 或 worker 分支不完整。
- 网络原始 SSE payload、实际服务器 token 节奏、断线恢复执行与 worker 真正启动情况，需要运行时证据。
- order action、外链打开、安全校验、网站项目打开、真实附件预览等静态可执行分支，不代表本次都触发过。
- todo 未找到专用 native kind；plan 已找到活动 phases。document 统计 allowlist、文件 viewer 与 chat presentation 三个层次须分别说明。
- 本报告不评价运行时字体、CSS token、背景或已点击交互结果；这些由独立 design / browser 证据覆盖。
