# 将Kimi设计迁移到另一个业务

先读design-package.json和DESIGN_SPEC.md。保留核心业务、数据边界与可访问性；按目标UI逻辑轻量增删功能和文案，并优先直接复用可用资源。登录后已补的类型按AUTHENTICATED_DESIGN.md使用，其他未测业务仍不应推导。

| 原始设计角色 | 目标阅读清单 | 必须保留 | 必须替换 / 适配 |
|---|---|---|---|
| 会话侧栏 | 资料导航与集合 | 240宽、36行、弱选中、分区 | 业务路由、图标、权限与真实历史 |
| AI输入 | 快速添加阅读条目 | 768上限、114初高、24圆角、16/24文字 | 提交语义、校验、持久化 |
| 底托项目/插件 | 集合/阅读状态 | 每侧20内收、向上36覆盖 | 选择器内容、权限、超长标签 |
| 模型菜单 | 录入方式/优先级 | 16容器角、10行角、说明层级、勾选 | 选项和业务效果 |
| 工具面板 | 添标题、链接、笔记 | 匹配输入宽度、描述行 | 实际工具后端 |
| 登录弹层 | 创建集合 | 暖白壳+白卡、44字段、动作层级 | QR/认证→普通表单；窄屏单列 |
| 灵感画廊 | 阅读封面浏览 | 16间距/圆角、类别标签、hover动作 | 品牌素材、媒体、列断点 |

## 执行顺序

1. 固定目标路由和行为测试，记录目标功能不可被视觉替换破坏。
2. 接入tokens/theme.css：先表面和文字，再几何；不要全量导入原站CSS。
3. 把catalog组件包装成目标框架组件；保留slots与状态接口D标记。
4. 首页先核对输入列宽、导航密度与大块留白；对照相同画布。
5. 加菜单和弹层的关闭/返回/草稿保留；给目标真实加载/失败设计D方案。
6. 用目标最长文案、键盘、窄屏与减少动画验证。仅当新增差异影响设计时补原版证据。

## D接口建议

LayeredComposer: draft, placeholder, disabled, pending, error, contextLabel, modeLabel; onDraftChange/onSubmit/onOpenTools/onSelectContext。
DescribedOptionMenu: options[{id,label,description,disabled}], selectedId, onSelect/onDismiss。
FramedModal: title/body/footer, open, dismissible, onSubmit/onDismiss；关闭后恢复触发器焦点。
WorkspaceNavigation: items/sections, selectedId, collapsed, narrowOpen。

这些是目标接口，不是Kimi公开API。原版请求取消/生成停止/晚到结果没有证据，不替目标后端作保证。

## 给未来Agent的任务入口

“读取`platforms/web/kimi/design-package.json`与DESIGN_SPEC，使用当前版本中readiness支持的设计范围。先审查目标产品，再迁移视觉和通用体验；保留目标业务。未证实原版状态按D选择记录。对照同画布；不把参考运行当原站证据；品牌和原始封面替换。”

本轮没有GitHub仓库目标或推送授权，结果保存在当前项目，不自动上传原件。

## 登录后更新

本轮已正常登录并补完代表样本，当前迁移状态ready。以AUTHENTICATED_DESIGN.md和最新design-package.json为准；上文尚未补证的历史边界若与本次A冲突，以具体新证据为准。

优先直接复用assets/reusable的原始SVG、插画、字体栈和组件CSS；reference/workspace.html提供集合、笔记与发现页面。D仅标业务映射、必要包装和缺项替代。原始文件夹PNG含Kimi浅色水印，作为私有参考保留，新品牌可使用同包Folder SVG。
