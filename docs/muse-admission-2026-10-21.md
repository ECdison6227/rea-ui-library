# Muse公开首页入库

用户明确要求将已完成的单页设计包推送到本库。本次新增普通目录`platforms/web/muse-public-home`，不建子模块、不重跑原站、不登录。

保留37项tokens、10个组件、3组布局、5组交互、7项动效、33项资产与45项证据及原始时间/哈希；核心条目新增明确D实施决定和消费者事件以满足入库契约。

[公开分发审查](../platforms/web/muse-public-home/integration/README.md)核对了268份来源文件，并将分发模式改为reviewed-public。原源图、参考截图、字体和插画字节保持不变。源提交认证、长宣传片、真实触控/读屏器、跨引擎/DPR/语言的未测边界保留。

目录使用已审原版公开首屏作为缩略图；Pages仍只构建目录及缩略图，完整设计包由本Git仓库分发。验证依次执行包完整性/消费者契约、npm test、npm run build、npm run verify。推送与远端结果以本次提交和Actions记录为准。
