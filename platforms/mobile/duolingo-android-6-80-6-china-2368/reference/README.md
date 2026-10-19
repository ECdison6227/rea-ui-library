# 原生参考

独立Java Android Views工程，包含课程选择、组件、Paperlane及D动效。当前APK版本0.2，实际SHA见build/build-report.json。

在本目录执行：

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -JdkPath C:/path/to/jdk-21 -SdkPath C:/path/to/android-sdk
```

需要Android SDK35/build-tools35.0.0与JDK21，不包含这些依赖。支持scene-index.json中的scene、font、language与reduceMotion参数。运行前选择自己的测试设备。

[实际比较](../comparison/native-revision.html) · [组件契约](component-contracts.json) · [已签名APK](build/native-reference.apk)。历史构建截图按各自SHA解释，不因当前APK存在而改写其身份。
