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
