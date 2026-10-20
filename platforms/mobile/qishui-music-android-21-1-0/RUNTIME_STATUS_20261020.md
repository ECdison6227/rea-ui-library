# 原版运行与恢复边界

保留登录profile，低资源1536MiB/1核分段在系统恢复后仍发生源App原生崩溃。用户释放宿主资源后，启动前可用内存4273MiB，以3072MiB/2核、host GPU/Vulkan关闭复验；系统Launcher和新鲜层级稳定，原App仍SIGABRT，Cannot process signal11，未进入功能页面。栈涉及ARM64转译，不能唯一归因内存，也不能把独立D参考成功运行当成原App恢复。

专用5592已关闭，profile保留、config.ini前后SHA一致；其他项目设备未控制。本次公开整理不启动设备，不新增原功能通过。需要能正常运行同版本、访问同一账户的健康Android环境，优先原生ARM64设备；不循环相同失败配置。

V05确切scene291研究收藏和P07原值及跨重启恢复仍待验证。历史功能账本26 verified、28 pending、12 blocked、1 failed、1 not_applicable保持不变。原版关键动效时序/中断/退出、Android系统大字/IME/TalkBack尚未验证。

原生0.2.0的实际运行与局部量化结果见[对照](comparison/native-calibration-20261020.md)。本页是由本地恢复记录整理的说明；原始崩溃日志、环境记录及私人上下文未公开，相关证据保留metadata与原SHA。
