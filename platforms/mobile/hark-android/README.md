# Hark Android：移动端设计研究

适用于下一步手机控制器的视觉系统与组件规划。先读 [Design Package v1](design-package.json) → [设计规范](DESIGN_SPEC.md) → [迁移指南](ADAPTATION_GUIDE.md)。这份研究独立于 [历史 Hark Web 研究](../../web/hark/README.md)，保留旧文件和证据。

来源为 **com.hark.android / 1.0.0-36 / 100000036**。用户的 ARMv7 XAPK 与本轮取得的 ARM64 XAPK 含有完全相同的 base APK。Android 原生壳使用 Compose，包内另有 React Native/Hermes widget runtime，不能据此把整个手机应用当作网页。

当前 **partial**：98项有作用域token、5类组件、2类布局、3类交互、3项动效记录，以及一个可运行的独立领域参考。35个颜色角色有light/dark双表；13档原生自定义排版保留sp；发送按钮保留dp及compact分支。核心原版页面、键盘、返回和动效对照仍需正常Google Play安装及Hark登录。

在本包目录执行：

~~~sh
node reference/serve.mjs
# http://127.0.0.1:4327
node --test reference/model.test.mjs
~~~

参考使用虚构的野外调查任务，可体验主题、详情、完成/撤销、输入、忙碌、失败重试、取消及晚到结果保护。它不是原版Hark，也未接入Vivic。原字体、品牌资产、原包和反编译代码不分发；参考字体回退与几何/动效D选择均有标记。

实际来源、哈希、REA Evidence ID、方法和限制见 [证据目录](evidence/index.json) 与 [已审来源回执](evidence/rea-selected-observations.json)。环境与续接见 [研究记录](RESEARCH_STATUS.md)。
