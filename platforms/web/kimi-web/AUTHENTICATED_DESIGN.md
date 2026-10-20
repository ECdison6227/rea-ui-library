# 登录后补充研究与直接复用

用户于本轮完成正常登录，随后补了项目、会话与插件样本。已创建一个中性项目 Design Transfer Sample、两条UI研究消息，并保存了一段中性项目说明；不导出私人历史与凭据。

环境：1455×878 CSS px / DPR1.75 / Windows Chrome；第一轮匿名样本1440×1000 / DPR1仍保留。截图、DOM与样式顺序采集，不能当原子快照。

## 新增可迁移规则

- 项目：主列约784、底部768输入；右侧独立Instructions/Files卡，20圆角；标题20/28/500，直接复用80px文件夹原件。
- 说明编辑：560px弹层，16角、24内距、灰色多行编辑；Cancel重新打开后草稿为空，Add后原卡显示说明。
- 会话：正文720列；H1 22/36/600，H2 20/32/600；表格14/22，表头600，单元格padding10px 16px 10px 0；代码独立灰标题条与Copy。
- 生成：实际观察到send-button-container loading → disabled stop → 普通禁用发送，方形停止图形；没有点击停止验证后端取消。
- 插件/技能：800px内容列、搜索+分类pill、双列卡；无匹配查询显示No matching plugins found；Skills页有上传引导和来源/用户数卡。没有安装、授权或上传。

## 直接复用

assets/reusable保存16个原始SVG、两张项目PNG、原始变量和精选组件声明；reference/source-icons.js优先返回原SVG，D路径只做缺项回退。components.css仅去除Vue data-v作用域、保留media/supports分支并略过未取得URL资源的规则，具体见css-provenance。字体直接保留实测系统栈，没有发现可导出的实际字体文件；没有声称复用了未出现的图表库。

## 小型业务适配 D

项目→阅读集合，Instructions→Reading goal，文件/会话区→阅读条目与笔记；插件Install→Add reading。新增动作真实写入原阅读清单的localStorage；重命名、保存/取消和无结果反馈可执行。省略外部授权、会员入口和AI生成，避免不属于阅读业务的控件。

参考：reference/workspace.html，三个视图Collection / Notes / Discover。复用源CSS、字体栈、SVG和插画；正文内容与业务事件为D。对照见comparison/authenticated.html；验证见authenticated-verification.json。

## 未知与边界

网络错误、停止的取消语义、请求竞态与真实移动平台仍未知；原版coverage逐项保留partial，目标替代标D。迁移ready仅指已声明的代表UI和可运行路径，不表示原产品所有功能已测。
