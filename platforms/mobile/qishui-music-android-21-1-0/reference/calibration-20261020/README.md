# 原生校准候选0.2.0（D）

从现有0.1.0参考复制的独立修订，原目录和原APK保持不变。候选对标题/动作文字墨迹、分隔与fade作明确D调整，来源为已有对照的具体偏差；不是恢复出的原版字体参数。

运行PowerShell build.ps1构建。APK仍用study.qishui.reference包名，versionCode2；应在自有测试profile中安装，不影响原com.luna.music。本机旧debug key仅本地复用，不随交付。

编译、9条D状态断言与签名校验分别保留；后续实际Android运行与像素改善见下节。原版唯一字体归因及残余字形/轮廓差异仍未解决，D调整见calibration.json。

## 后续实际运行

候选已在API35、1080×2400、440dpi、fontScale1.0实际运行并采集scene301。与历史A04的[新量化对照](../../comparison/native-calibration-20261020.md)记录了标题/次动作墨迹和fade改善，也保留残余字形/轮廓差异。前段说明构建检查的边界；新的运行结果仍是D局部partial，不证明原App恢复或唯一字体归因。
