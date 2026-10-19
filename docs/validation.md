# 本次整合验证

本文件记录统一目录和技能草案的验证，不产生新的原产品运行观察。三个Git来源锁定原提交；Kimi保持已审分发快照字节，Git属性关闭该快照的文本换行转换。

- 7项Node测试通过：REA入库及空桌面类别、多关键词和平台筛选、用户接受范围与研究覆盖分离、不同条件比较、声明文档访问与原始证据边界、只读HTTP及路径限制、静态导出遇未知旧文件时拒绝覆盖/发布。
- 隔离headless Chromium实测通过：4份研究加载，桌面空态，手机分类与草稿搜索，详情/真实本地文档，Esc仅关闭上层文档，跨来源比较，技能审查入口。1440px及390px布局没有横向溢出。未移动用户前台窗口，未控制原App或模拟器。
- 三张缩略图已查看并按原字节复制，SHA记录在catalog/imports.lock.json；Hark封面是明确标记的目录示意，不标成原版截图。
- 公开静态build只含目录、精选缩略图和技能说明；未复制来源原始证据、私有项目、Git元数据或原APK。
- 导入后的Kimi快照使用自身消费者验证器检查通过：257套可分发A、71套D、恢复记录和原APK身份声明一致；未读取未分发原件或根原包，scopeComplete仍为false。该校验不重测Kimi功能。
- 技能草案通过YAML/相对引用与9组合成契约测试。独立情境审读区分：用户接受UI边界可结束相应交付；用户明确全测仍保留必要缺口；仅收REA研究时排除普通UI工程。审读发现的catalog/仅评估模式适用歧义已修正。详情在skill-review.md。

复现：

```sh
npm test
npm run build
npm run verify
node tools/browser-qa.mjs
node --test skills/rea-style-library/scripts/check-manifest.test.mjs
```

浏览器检查使用本机已有Chromium和独立临时profile；其他电脑可用环境变量REA_QA_CHROME指定已安装浏览器。不会安装或更新浏览器。报告和截图保存在总库.runtime，不随Git提交。

来源自己的原生工程没有在本次分类任务中重新编译；macOS、Android权限/真实服务、逐字字体、所有原始交互等仍按来源报告的覆盖与未知处理。静态目录的远程文档链接仍需要GitHub可访问，完整本地资料需初始化子模块。
