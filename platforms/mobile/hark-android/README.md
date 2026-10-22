# Hark Android：移动端设计研究

适用于下一步手机控制器的视觉系统与组件规划。先读 [Design Package v1](design-package.json) → [设计规范](DESIGN_SPEC.md) → [迁移指南](ADAPTATION_GUIDE.md)。这份研究独立于 [历史 Hark Web 研究](../../web/hark/README.md)，保留旧文件和证据。

来源为 **com.hark.android / 1.0.0-36 / 100000036**。用户的 ARMv7 XAPK 与本轮取得的 ARM64 XAPK 含有完全相同的 base APK。Android 原生壳使用 Compose，包内另有 React Native/Hermes widget runtime，不能据此把整个手机应用当作网页。

当前 **partial**：125项有作用域token、9类组件、3类布局、4类交互、4项动效记录。35个颜色角色有light/dark双表，13档原生排版保留sp。续接逆向新增实际导航结构、锚定附件菜单、输入变体、重试行及动态卡片宿主；具体见 [原包内容地图](NATIVE_CONTENT_MAP.md)。核心原版运行仍受既有Play地区/授权条件限制。

**Web元素可以用于手机。** 已将库内390×844 Web样本与原生声明分别映射，做成第二份可运行参考，见 [Web→手机](WEB_TO_MOBILE.md)。它包含概览/对话/记录、模板、输入、检索、失败重试、取消和主题，7项新浏览器场景通过。采用Web的15/19px、玻璃输入和内容层级，不把它称作Android原版UI。

在本包目录执行：

~~~sh
node reference/serve.mjs
# http://127.0.0.1:4327
# http://127.0.0.1:4327/web-mobile.html
node --test reference/model.test.mjs
~~~

参考使用虚构的野外调查任务，可体验主题、详情、完成/撤销、输入、忙碌、失败重试、取消及晚到结果保护。它不是原版Hark，也未接入Vivic。原字体、品牌资产、原包和反编译代码不分发；参考字体回退与几何/动效D选择均有标记。

实际来源、哈希、REA Evidence ID、方法和限制见 [证据目录](evidence/index.json) 与 [已审来源回执](evidence/rea-selected-observations.json)。环境与续接见 [研究记录](RESEARCH_STATUS.md)。

深化资料另含 [10项结构化发现](evidence/native-content-findings.json) 和 [22份新增REA回执元数据](evidence/rea-deepening-receipts.json)。一份有方法生成错误的主输入布局回执已明确排除，不凭工具的complete标签声称完整源码还原。Hermes原始字符串与动态卡片内容未公开；当前公开内容为可实施设计事实与独立参考。
