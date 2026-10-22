# 对照与实际差异

当前对照为定向Android B声明与独立D参考映射。原生核心页面被Google Play可用性/授权阻断，尚未到Hark登录，**没有同屏像素相似度或原版动效通过结论**。系统授权错误与Play登录页仅为环境诊断，不假装核心UI。

| 条件 | 原版 | 参考 | 已知差异 |
| --- | --- | --- | --- |
| 身份 | Android com.hark.android100000036 | Field kit合成任务 | 不同领域/品牌，无外部服务 |
| 颜色 | 35角色×2主题B | CSS scoped roles | 每屏背景/合成待测；参考渐变/blur为D |
| 正文 | GTStandardL15/18sp400B | Arial15/18CSSpxD | 字形/fontScale/fallback不同 |
| 发送 | 52×48dp触区/52×32dp ink B | 52×48CSSpx/52×32CSSpxD | 单位/平台差异，触控真机待测 |
| 动效 | 原版时长/曲线unknown | spinner900ms/sheet220msD | 无原版对应测量 |
| 返回/取消 | 未完整观察 | dialog焦点恢复/草稿保留/代际D | D测试不改变原版覆盖 |

浏览器实际冒烟结果和截图记录见reference-verification.json（生成后）。读取旧文件不新增A；后续匹配条件的原生截图/参考配对保留旧版与当前边界。

实际参考验证：2项reducer测试、5项浏览器场景通过；390×844浅色/深色sheet、320×700减少动画截图已亲眼核对。无页面异常或外部请求。所有截图均D，不作为原版A。可直接查看[浅色](reference-light-390.png)、[深色sheet](reference-dark-sheet-390.png)、[窄屏减少动画](reference-reduced-motion-320.png)。

补充A仅记录原签名包启动后被Google Play授权门拦截的[环境画面](../evidence/android-play-licensing-gate.png)，时间区间08:48:22–08:48:24 UTC。Google登录已完成，当前账号地区无法从Play取得Hark；不再将“等待用户验证”当作当前原因。此图不是Hark核心组件，不用于宣称参考与原版配对通过。

## Web元素用于手机的独立参考

旧Web原件仍位于 `platforms/web/hark`，固定到da3029fb，原采集日期2026-10-15。本轮查看了27-mobile-chat原图，使用其几何与已有计算样式；没有重跑原Web应用或重新赋予A采集时间。

[深色390](web-mobile-dark-390.png)、[浅色设置面板390](web-mobile-light-sheet-390.png)、[320减少动画](web-mobile-reduced-320.png) 是新独立参考D，使用合成任务与记录。实际查看了像素：390下输入外框x16/w358/h48/bottom32，正文15/19；320和横屏没有页面横溢。浅色/320截图处于展开详情与已有本地记录的状态，不与原Web单条消息截图做相似度分数比较。

原Web与目标仍有具体差异：GT字体替换、WebGL天空替换、连续曲线变普通圆角、空输入语音按钮改为文字发送、附件动作变本地记录模板、面板和业务流程D选择。7项实际浏览器场景见 [验证记录](web-mobile-verification.json)；不是Android原生运行、真实软键盘或目标服务器功能验收。
