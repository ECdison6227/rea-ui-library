# 单页组件参考

在包根运行 `npm start`，打开 http://127.0.0.1:4173/reference/ 。HTML/CSS/ES modules；服务只用Node内置库，无安装或构建步骤。

- `components.js`：语义化组件与内容插槽。
- `source-content.js`：源公开页文案、图片路径、轮播短句。
- `styles.css`：原字体、原值及响应式。
- `app.js`：滚动/焦点、头像视频、轮播和阅读筛选示例。

参考不实现登录。Continue保留草稿并滚到介绍，Header/Footer动作返回输入。未发送身份或POST，也不持久化输入。导览外观保留，其实际动作打开源公开页；其他外链也只在用户主动点击时访问。

`?example=reading` 用同组件承载阅读收藏，提供本地筛选和空结果。减少动画设置停止动画并立即滚动。源触控长按阈值来自声明；本轮未测试真实触控设备或读屏器。

浏览器检查：先运行服务，再执行 `npm run qa`。默认复用本机已安装REA的Playwright和Chrome；其他机器可通过 `PLAYWRIGHT_PATH` 与 `CHROME_PATH` 指向已有工具。仅运行参考页不需要这些QA依赖。

原版配对和差异见 [comparison](../comparison/README.md)，入口见 [design-package.json](../design-package.json)。
