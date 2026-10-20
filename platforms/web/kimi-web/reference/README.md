# 组件参考与跨业务切片

实现：零依赖HTML/CSS/JavaScript，源代码是D独立实现，未复制Kimi应用逻辑。

- `index.html?view=source`：原站主页构图；菜单、工具、侧栏、登录门槛和草稿状态。
- `index.html?view=transfer`：Reading Room本地阅读清单；创建条目、集合、完成阅读、分类、刷新保存、JSON导出。
- `components.html`：色彩、排版、几何、按钮和反馈状态。
- `gallery.html`：原站分类+媒体卡结构的独立阅读封面样例。

从项目根 `npm start` 后通过入口打开。`npm test` 的22项断言已通过：空/非空提交、键盘菜单、Esc恢复焦点、弹层草稿保留、焦点边界、窄屏导航与无页级溢出、阅读状态筛选、集合与条目持久化、减少动画、暗色及画廊分类。完整运行结果在comparison/verification.json。

已实现的可复用结构是HTML classes与JS helper，不是原App API。迁移工程可以包装为React/Vue/Web Components，但应保留插槽、状态和token角色。

差异：当前16个图标直接复用原始SVG，缺项为18px/1.8stroke的D回退；textarea替代原站Lexical；菜单子选项内嵌；登录QR不可扫描，验证码/SSO无后端；窄屏快捷入口可滚动但未做原站两侧渐变箭头；参考增加显式关闭、Tab约束和读屏状态。

## 登录后更新

本轮已正常登录并补完代表样本，当前迁移状态ready。以AUTHENTICATED_DESIGN.md和最新design-package.json为准；上文尚未补证的历史边界若与本次A冲突，以具体新证据为准。

优先直接复用assets/reusable的原始SVG、插画、字体栈和组件CSS；reference/workspace.html提供集合、笔记与发现页面。D仅标业务映射、必要包装和缺项替代。原始文件夹PNG含Kimi浅色水印，作为私有参考保留，新品牌可使用同包Folder SVG。
