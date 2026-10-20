# 汽水音乐设计包入库审查

来源：com.luna.music 21.1.0 / 100211030，原APK SHA-256为3c538482c5166ac629eb2baaa31262f8cd9791dd7dc9401e64ab00d787c61039。平台mobile，真实历史REA来源、原采集时间和SHA保留；当前目录整合不产生原版功能通过。

用户在获知原版复验仍崩溃后，最新明确指示“检查没问题的话可以推送rea ui 库上去了”，并要求继续。这取代先前等待原版剩余验证后再发布的顺序限制。授权用于发布如实标为partial的资料；不将研究、原版恢复或功能验证改为完成。

标准包含434项tokens、122项组件/族/契约、9项布局、302项交互记录、244项动效记录。这些是模型数量，不是功能覆盖率。Web跨业务资料清单有26项有界D检查，包含窄屏、文字放大、草稿、校验、删除、重试、键盘、焦点与减少动画。

原生D0.2.0已实际Android运行并完成局部量化对照：六组bounds一致、标题垂直墨迹偏移6px→0、次动作边界对齐、fade最大通道差33→1。字形/轮廓仍有差异；资源分段不同，不推定原版字体或精确性能。候选早期“已编译未运行”的快照已刷新。

原App释放宿主资源后以3072MiB/2核复验仍发生原生崩溃。原版动效、Android系统字号/IME/TalkBack及V05/P07恢复未完成；历史功能账本26/28/12/1/1不变。专用模拟器已关闭、profile与原配置保留，不循环相同失败配置，不控制其他项目设备。

公开分发为独立审查的77个文件、8张已审图。原APK、私人账户画面/profile、签名key、凭据、工具/SDK/AVD缓存及未选素材不提交；692条证据原件省略，保留来源metadata及SHA。17,490条未选文案仅保留metadata和文本SHA。仅generic A04隐私面板、独立D参考与Web资料清单截图公开。

来源入口：[设计包](../platforms/mobile/qishui-music-android-21-1-0/README.md) · [公开成员审查](../platforms/mobile/qishui-music-android-21-1-0/PUBLIC_EXPORT_REVIEW.json) · [量化对照](../platforms/mobile/qishui-music-android-21-1-0/comparison/native-calibration-20261020.md)。

提交前执行实际成员/哈希与相对链接检查、标准包及消费者契约、npm test、npm run build、npm run verify，并验证/rea-ui-library/子路径。普通build/verify不自动接受字节变更；显式study:lock仅刷新本来源。main推送后由既有Pages工作流校验和部署；Pages只含轻量目录与已审缩略图，完整设计包从公开仓库读取。
