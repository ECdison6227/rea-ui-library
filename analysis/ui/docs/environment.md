# 环境、复现与恢复

原 APK始终保留在工作区根 `base.apk`，SHA-256为 `2a80fc5a87353c2c6ef3c01fa0085862fe45e4ddb9c2d272cb3780841b5889a6`。原 App与参考 `study.style.reference` 分别安装，未改原 APK、未登录、未输入账号/验证码/密码、未绑定穿戴设备。

原版A来源是项目专用AVD `rea_mihealth_ui_20261015`（serial `emulator-5580`），API35 Google APIs x86_64 + ARM64 translation，1080×2400、440dpi、fontScale1、zh-CN。参考在同设备同条件浅色运行。SDK/JDK位于用户REA局部目录；构建脚本允许独立参数，不要求复制作者绝对路径。字体声明与实际回退分开记录。

项目AVD保留已完成的欢迎/隐私/中国大陆地区状态；飞行模式用于冻结mine11.202.21等已下载页面包。运行时离线，不称真实后端/登录/健康业务验证。仅在该userdebug AVD临时启用adbd root读取目标bundle和图片缓存，未读账号数据库；结束时复原为非root，并关闭App与模拟器。实际恢复记录见 `restore.json`，未恢复/保留项逐项列出。

系统font_scale最后恢复1.0，系统night为no，通知示例切换在原App恢复on。未修改Windows可选功能、未重启主机。Android官方SDK组件/JDK/JADX等保留供继续研究；用户已要求安装所需工具，没有卸载它们。

本地复现可运行 `tools/start-emulator.ps1`，其AVD名字/端口是本工作区约定；其他电脑需自行创建支持原APK ARM64的模拟器。参考APK本身是普通Java/Dex，支持API26+，不需要ARM64模拟。`verify-runtime.cjs`支持更换ADB与序列号，但视觉对照必须重新记录尺寸、density、字体和主题。

原版截图/录屏保持不变；新对照裁图、叠图、差异图都标为派生。真实SDK账号窗口有SECURE标志，黑帧从可用视觉证据排除；只保存允许读取的hierarchy和窗口事实，没有绕过截图保护。

资料可以作为本地仓库审查，未执行GitHub push、发布、账号消息或外部生产写入。原包与全量反编译工作文件仍留本机；便携包只包含所需研究库、证据、资产与独立参考。
