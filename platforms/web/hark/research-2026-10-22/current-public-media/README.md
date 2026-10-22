# Hark 当前公开网页媒体原件

此补充包解决 2026-10-22 公开网站研究中“有媒体 URL、缺本地原文件”的缺口。范围是已保存的 19 个公开子页、首页观察清单、已保存公开 JS/CSS，以及本轮正常无凭据 GET 取得的首页 HTML；不读取账号 profile，不访问 `api.hark.com`，不使用历史 clone 媒体来填当前版本。

**本轮结果：92/92 个已证实公开媒体定位符均已取得默认响应（36 张图片、56 个视频），并补齐 15 份已声明 WebP 的内容协商响应，共 107 个媒体响应文件、440,893,743 bytes，无下载遗漏。** 48 个来源文件与全部原件的完整性校验通过；最大单件 80,272,546 bytes。首页 10 个轮播项的全部 20 个视频、10 个 poster，以及 5 个 feature 的 10 个视频、5 个 poster 均可从本地直接引用。

## 阅读入口

- [manifest.json](manifest.json)：逐文件 URL、抓取时间、HTTP 状态、响应类型、bytes、SHA256、尺寸、视频时长/编码、完整来源片段与 query 变体。`declaredFile` 原样保留 crop/hotspot/format 等字段。
- [carousel-bindings.json](carousel-bindings.json)：首页 10 个 slides 的顺序、prompt、WebM、Safari MOV、WebP poster、holdMs、face、recording.card 和 prompt 定位。
- [feature-bindings.json](feature-bindings.json)：首页 5 个图文 feature 的标题与视频/备用视频/poster 对应关系。
- [gaps.json](gaps.json)：下载失败/预算跳过、原捕获的空 src、CMS 明确空的 file/poster，以及未测边界。
- [negotiated-representations.json](negotiated-representations.json) / [representation-audit.json](representation-audit.json)：15 个 `.webp` URL 在默认 `Accept: */*` 下实际返回 PNG，显式 `Accept: image/webp` 下返回 WebP；两份真实响应分别保留 bytes/SHA/Accept。使用相应绑定中的 `preferredPath` 复用声明的 WebP 编码。
- [verification.json](verification.json)：原件与引用来源的 bytes/SHA、图片解码、媒体尺寸、视频时长、预算和 slide 引用完整性校验结果。
- [files.sha256.json](files.sha256.json)：此补充包的逐文件 SHA256 锁（不包含锁本身）。
- [source/home.html](source/home.html) 与 [source/home.json](source/home.json)：本轮首页文本及原抓取收据；源 SHA 为 `45e20b3243c56c7e1aaa48de8b40f6a4aeb2d37242f09569ad40c10ce1c792ab`，抓取时间 `2026-10-22T15:39:55.896Z`。
- `images-contact-sheet-*.jpg` / [image-review-index.json](image-review-index.json)：由未修改原图生成的检视拼图及 tile→原件映射。

## 证据与预算

首页原捕获中的 48 条 video/source 空 src 仍是该时刻的 DOM 事实；本轮解码公开服务端 HTML 中的媒体声明，补足了 10 个轮播 slides 与 5 个 feature 的静态资源绑定。解码仅用 JSON 解析，没有执行页面脚本。这些声明和本轮媒体 GET payload 为 B；继承的原页面 DOM 观察明确标为 A。新 GET 首页 HTML 中的空属性属于 B 服务端声明，不冒充运行 DOM，也不产生新的动态轮播实测。

默认请求的原始响应字节保存于 `originals/`，15 份独立 WebP 协商响应保存于 `negotiated-webp/`。文件名保留来源 URL，不能从 `.webp` 后缀推断 `originals/` 内的实际二进制格式；以响应 MIME、Pillow 检测和协商清单为准。这里的原件指未修改的 CDN 响应字节，不声称已证明 CMS 最初上传文件的字节。

下载只请求已捕获文本中的明确公开静态 URL；Sanity presentation query 的变体逐一登记，下载默认响应时仅去掉 query，不猜文件 ID 或 API 路径。图标与字体由其他资产研究负责，不计入此媒体清单。初始 250MB 工作预算不足以取得已证实原件，协调者按用户完整资产要求将本次上限提高为 **110 个媒体响应文件、1,000,000,000 bytes**，仅针对 92 个明确定位符以及其中 15 个已声明 WebP 的协商响应；单文件超过 400MB 则先记录长度与用途再由协调者判断。辅助 JSON、脚本与检视拼图不计入原媒体预算。

普通直连曾发生连接重置。成功下载复用本机已配置的 macOS HTTPS 代理；13 个 Python TLS/read 仍失败的资源通过同一路由的 curl 正常 GET 补齐。收据记录 route 和前次失败，不读取代理凭据。源站 403/连接失败不会保存错误页伪装成图片或视频。一个成功的协议对照 probe 直接保留为原件，其开始时间未记录、完成时间按文件 mtime 明确标注，不重复抓取来伪装精确时间。

## 复核

在仓库根目录运行：

```sh
python3 platforms/web/hark/research-2026-10-22/current-public-media/summarize.py
python3 platforms/web/hark/research-2026-10-22/current-public-media/verify.py
```

`acquire.py --inventory-only` 仅重建静态清单；不带该参数会请求明确的公开静态媒体并在预算内续取，已有原件先校 SHA。`retry-curl.py` 是同一正常代理路由的缺件续取入口，当前 92 件均已存时不发下载请求。`capture-webp.py` 只负责上述 15 份声明 WebP 响应，已有文件先校 SHA。本轮不继续穷举所有 query 尺寸或 Accept 编码矩阵。

`verification.json` 只证明本包文件和静态绑定完整性，不证明浏览器的媒体选择、播放、轮播导航、动画时序或所有网页状态。`holdMs` 与 ffprobe 时长分别保留，二者不自动等同。源媒体的公开可访问性也不建立下游再分发许可。
