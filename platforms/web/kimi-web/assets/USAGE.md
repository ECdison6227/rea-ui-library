# 资产使用入口

- **新业务默认**：优先复用 `assets/reusable` 原始资源、`tokens/theme.css` 与 `reference/source-icons.js`；缺项才使用 `reference/app.js` 的D回退。主图标18px盒，线宽1.8；图标路径是独立实现，不是原站逐个复刻。
- **原站对照**：`source/kimi-wordmark.png` 是原页面内嵌PNG的原始解码字节，800×160；只在 source reference 使用。`source/inline-icons.json` 保留真实DOM SVG和嵌入keyframes，按classes检索。
- **B 样式原件**：`source/downloads.json` 记录 URL/本地路径；机器清单给每个文件 SHA256。`../evidence/raw/style-excerpts.json` 是定位入口，媒体查询上下文须回原CSS核对。
- **字体**：主UI使用系统字体栈；不要因为样式表里有MiSans VF、Geist Mono或KaTeX就声称它们用于界面正文。平台字体有效样本见 platform-fonts 证据。
- **替换**：Kimi文字标、侧栏品牌、版权/备案、商品和画廊媒体均不随业务迁移复用。原画廊封面只存在研究截图中，没有批量下载。
- **分发**：所有原站原件默认为私有研究，许可未知。公开版的发布范围见 PUBLICATION_REVIEW.json。

## 登录后更新

本轮已正常登录并补完代表样本，当前迁移状态ready。以AUTHENTICATED_DESIGN.md和最新design-package.json为准；上文尚未补证的历史边界若与本次A冲突，以具体新证据为准。

优先直接复用assets/reusable的原始SVG、插画、字体栈和组件CSS；reference/workspace.html提供集合、笔记与发现页面。D仅标业务映射、必要包装和缺项替代。原始文件夹PNG含Kimi浅色水印，作为私有参考保留，新品牌可使用同包Folder SVG。
