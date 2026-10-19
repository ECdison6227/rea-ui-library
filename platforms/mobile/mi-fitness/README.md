# 小米运动健康风格包

适合卡片式概览、分组设置、清晰导航和受控弹层。将它用于资料、设备、任务或统计业务，保留你的品牌、数据与服务。

- [设计规范](DESIGN_SPEC.md) / [机器入口](design-package.json)
- [组件、动画和截图浏览](design-guide.html) / [AI实施指南](ADAPTATION_GUIDE.md)
- [原生参考与构建](analysis/ui/android-reference/README.md)
- [原版配对](analysis/ui/baseline/comparison/final/index.html) / [跨业务示例](analysis/ui/transfer-example/README.md)

包含三页、十组件、34组弹层/导航规则、51项源动效候选及明确D动画方案。来源3.57.0、Mine11.202.21；主要对照限定中文浅色、未登录空数据。

当前partial：OEM字体、平滑圆角与完整运动仍有差异。按已知规则和D选择实施后，应在目标软件中验证；无需为健康或账号业务全测停住风格迁移。

## 构建参考

```powershell
cd analysis/ui/android-reference
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -SdkRoot C:/path/to/android-sdk -JavaHome C:/path/to/jdk-21
```

调试APK需本地构建，未随Git分发。源码、原版/参考截图和验收记录仍保留。[历史研究入口](analysis/ui/README.md)。
