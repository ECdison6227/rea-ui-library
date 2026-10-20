# 可运行参考与接入

推荐先运行[跨业务资料清单](portable/index.html)，见[使用说明](portable/README.md)。无依赖、网络或账户；26项有界D浏览器检查通过，包括新建/编辑、草稿、空值/长度、删除、重试、320px/200%文字、Tab/Escape/Enter、焦点与减少动画。

原生参考0.1.0的[源码](src/study/qishui/reference/ConsentPanel.java)和[APK](build/qishui-reference.apk)保持历史版本，已有partial像素对照。[0.2.0校准参考](calibration-20261020/README.md)已在Android实际运行，9条状态断言通过，scene301与历史A04的[量化对照](../comparison/native-calibration-20261020.md)显示局部改善。它对标题/按钮墨迹、字距和fade作D调整，字形和轮廓仍有差异，不能宣布原版字体已复原。

构建需要JDK21、Android SDK35/build-tools35.0.0；在对应reference目录执行PowerShell build.ps1。私有debug key未分发，重建生成新的本地key。可安装在自有测试设备，以study.qishui.reference/.MainActivity打开privacy/reminder/catalog；学习TransferActivity支持intro/workspace/session/complete等原场景。原版com.luna.music不受独立参考逻辑影响。

[构建身份](builds.json) · [参考成员](manifest.json) · [Web检查](../evidence/completion-20261020/portable-validation.json)。浏览器可访问性树不等于TalkBack语音；D运行不证明原Android行为。