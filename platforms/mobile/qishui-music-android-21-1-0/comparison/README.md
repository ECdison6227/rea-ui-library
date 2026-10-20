# 原版、原生参考与跨业务对照

[A04原图](../evidence/runtime/screens/04-privacy-guide/screen.png)与[D-R01原生参考](runtime/screens/r01-privacy/screen.png)同API35/1080×2400/440dpi/fontScale1.0，历史6组tree bounds一致；标题D墨迹下移6px/右缘窄7px，按钮字形、粗体链接、fade/分隔/轮廓有差异。来源内容相同，时钟与包装层不同，结论partial。

0.2.0已实际Android运行，[量化对照](native-calibration-20261020.md)保留[完整数据](native-calibration-20261020.json)及[scene301图像](../evidence/runtime/screens/301-native-calibration-guide/screen.png)。六组bounds一致，标题垂直墨迹偏移6px→0、次动作墨迹边界对齐，fade最大通道差33→1；字形和轮廓仍有差异。候选1536MiB/1核与历史资源分段不同，不比较精确性能或动效。[学习切片D-T02](runtime/screens/t02-learning-workspace/screen.png)是旧的不同业务样本；[本轮Web切片](../reference/portable/index.html)26项D检查通过，不能替代原版验证。

原版时长/曲线、字体唯一归因、Android系统大字/IME/TalkBack及V05/P07恢复仍未完成，见[本轮处置](../COMPLETION_20261020.md)。原始私人画面与未选证据公开省略，仅保存SHA和原因。