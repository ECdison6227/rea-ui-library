# 迁移指南

先读 [design-package.json](design-package.json) 的范围与就绪状态。核心是Muse公开首页的视觉及页内交互，不包含登录后的业务规则。

## 可直接接入

- [styles.css](reference/styles.css)：原值映射的字体、颜色、文字角色、几何和响应式。
- [components.js](reference/components.js)：Header、Hero、SectionIntro、MediaSection、FeatureRow、ReadingLinks、Footer。
- [assets/source](assets/source/)：真实字体、标志、插画、头像视频；通过manifest选取用途。
- [assets/curated](assets/curated/)：保留源path并具备独立SVG namespace的图标。

字体、品牌及插画直接复用是本地参考决定。本次未取得额外公开分发许可；换品牌时保留层级与构图，按目标可用资源替换并重新对照。

## 接入步骤

1. 审查目标的核心任务、数据和已有资产；能复用的组件先薄封装。
2. 先映射文字角色与颜色，解决实际字体、Hero换行、输入宽高。
3. 验证单个Hero/输入切片，再接图文、媒体与信息链接。
4. 为控件连接目标的真实动作与反馈；缺乏源证据的状态明确作为D设计。
5. 匹配画布、DPR、内容、字体加载和交互状态，检查窄屏、焦点、长文本与减少动画。

## 业务映射

| 原模式 | 阅读示例 | UI理由 |
| --- | --- | --- |
| Muse/Meta品牌 | Folio与独立书本符号 | 保留层级，替换身份 |
| 手机或邮箱输入 | 阅读列表搜索 | 同样的窄入口连接实际查询 |
| Continue | Find a story | 动作名描述目标结果 |
| 六组功能介绍 | 三条阅读内容 | 保留图文关系，精简内容 |
| 账号提交 | 本地筛选、计数、空结果 | 不引入本轮排除的认证 |
| 插画中的产品界面 | 阅读封面卡 | 不克隆图片内的业务 |

示例地址 `/reference/?example=reading`。搜 `creative` 保留一条；不存在的词显示空结果；清空提交恢复全部。草稿在页内滚动期间保留，刷新重置。此D切片验证复用关系，不证明源业务。

## 实现替代与限制

| 项目 | 做法 |
| --- | --- |
| 字体 | 原WOFF2，CSS消费者别名 |
| Meta字标 | 原SVG和原mask比例 |
| 图标 | 原path + 独立namespace/语义wrapper |
| Header阈值 | 原实测端点；参考插值为D |
| 轮播 | 原时长、错峰和曲线；Web Animations替代GSAP |
| 媒体滚动 | 原scale/Y/start/end，RAF平滑近似scrub |
| 视频 | 头像原件；长片采用帧/封面和源页面链接 |
| 提交 | 本轮不登录；参考Continue仅页内导航 |

组件接口、token名称、example参数不是原App API。保留CSS px/rem/em原单位，不把网页数值直接当成设备物理像素。目标可以按UI逻辑合并重复入口、精简文案、补真实状态，同时保留核心任务和数据路径；新增按钮必须有效。

实际检查和差异见 [comparison/README.md](comparison/README.md)。目标后端、公开发布和新增页面不在本轮内。
