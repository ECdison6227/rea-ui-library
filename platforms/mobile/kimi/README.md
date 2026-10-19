# Kimi风格包

适合聊天、内容生成、文档编辑，以及有草稿、预览、保存取消和异步反馈的产品。复用界面关系与用户逻辑，保留你的模型/业务接口、品牌和数据。

- [设计规范](DESIGN_SPEC.md) / [机器入口](design-package.json)
- [组件、动画和截图浏览](design-guide.html) / [AI实施指南](ADAPTATION_GUIDE.md)
- [完整安全原版图库](run03.html) / [原生比较](comparison/native-run03.md)
- [参考工程](reference/README.md) / [Fieldnotes迁移验收](transfer-example/run03-acceptance.md)

包含七类核心交互组件、四种布局、Run03状态转移、原生几何、widget局部token和明确D动画方案。来源Android3.1.3(376)；257套可分发原版场景、71套独立参考场景保留原身份。

当前partial：精确字体和原版曲线仍有未知。用户已接受相似UI与核心流程的用途；历史37项清单仍保留20项verified及其余未测/部分，不把接受范围改成实测通过。

## 校验与运行

从本目录执行消费者完整性检查，不需要原APK或研究AVD：

```sh
node tools/verify-run03.mjs --distributed --check --require-restored
```

构建独立参考需要现有Android SDK35/JDK21：

```powershell
powershell -NoProfile -File tools/build-native-reference.ps1 -AndroidToolchain C:/path/to/android-toolchain
```

[验证模式](docs/run03-verification-modes.md) · [行为规则](spec/run03-behavior-and-transfer.md) · [动效观察](docs/run03-motion-research.md) · [保留缺口](docs/run03-coverage.md)

以上检查不控制原App。参考是本地fixture；六秒完成、存储、取消epoch属于D选择，正式产品应接入真实业务，勿保留研究占位。
