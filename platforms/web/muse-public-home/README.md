# Muse 公开首页 · 设计包

只研究 [muse.ai](https://muse.ai/) 这一张公开页面及其页内滚动内容。遵照本次要求，没有登录或提交账号信息。

**入口：** [设计规范](DESIGN_SPEC.md) · [机器可读设计包](design-package.json) · [迁移指南](ADAPTATION_GUIDE.md) · [原版对照](comparison/README.md)

```sh
npm start
```

打开 **http://127.0.0.1:4173/reference/**。运行参考页只需要 Node.js，不需要安装前端依赖。服务仅监听本机地址。

参考页包含品牌首屏、头像视频和轮播短句、圆角输入、胶囊按钮、页内滚动、六组交替图文、媒体封面、信息链接与页脚。原字体、标志、插画及图标几何直接复用。登录外观保留为原页样本；参考页的 Continue 仅滚动到介绍区，输入不发送也不持久化。

同组件的 [阅读收藏示例](http://127.0.0.1:4173/reference/?example=reading) 用于验证跨业务复用，包含本地筛选与空结果状态；它不是额外研究的产品页面。

| 内容 | 位置 |
| --- | --- |
| 颜色、字体、尺寸 | [tokens/tokens.json](tokens/tokens.json) |
| 组件与状态 | [components/catalog.json](components/catalog.json) |
| 布局 / 交互 | [patterns](patterns/) |
| 动效 | [motion/catalog.json](motion/catalog.json) |
| 资产用途与哈希 | [assets/manifest.json](assets/manifest.json) |
| 证据与分级 | [evidence/index.json](evidence/index.json) |
| 可运行组件 | [components.js](reference/components.js)、[styles.css](reference/styles.css) |
| 浏览器检查 | [qa-results.json](comparison/qa-results.json) |

```sh
npm run verify
# 服务已启动，且本机具有 Playwright/Chrome 时：
npm run qa
```

`verify` 校验协议、路径、哈希与实现版本；`qa` 检查实际渲染和交互。截图保留采集条件，像素相似不代替业务验收。

就绪范围是这一张公开页面及其组件。完整宣传片、登录后 UI、真实触摸设备、其他浏览器/语言不在本次验证范围。原始采集和每次参考验证的截图保留；本包已按用户要求审查并加入公开的 REA UI Library；入库记录见 integration/README.md。
