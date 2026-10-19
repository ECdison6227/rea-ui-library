# 本次整合验证

本文件记录统一目录和技能草案的验证，不产生新的原产品运行观察。三个旧Git来源已改为完整普通目录，历史提交保留在总库；全部研究用逐文件SHA清单检查，Git属性关闭platforms的文本换行转换。

- Node测试覆盖：REA入库及动态平台计数、多关键词和平台筛选、用户接受范围与研究覆盖分离、不同条件比较、声明文档访问与原始证据边界、只读HTTP及路径限制、静态导出遇未知旧文件时拒绝覆盖/发布；新增普通目录研究无需外部仓库、可自动登记且后续字节变更会被识别。
- 隔离headless Chromium实测通过：4份研究加载，桌面空态，手机分类与草稿搜索，详情/真实本地文档，Esc仅关闭上层文档，跨来源比较，技能审查入口。1440px及390px布局没有横向溢出。未移动用户前台窗口，未控制原App或模拟器。
- 三张缩略图已查看并按原字节复制，SHA记录在catalog/imports.lock.json；Hark封面是明确标记的目录示意，不标成原版截图。
- 公开静态build只含目录、精选缩略图和技能说明；未复制来源原始证据、私有项目、Git元数据或原APK。
- 导入后的Kimi快照使用自身消费者验证器检查通过：257套可分发A、71套D、恢复记录和原APK身份声明一致；未读取未分发原件或根原包，scopeComplete仍为false。该校验不重测Kimi功能。
- 内迁验证使用普通单仓库clone，不执行submodule update，不访问三个旧仓库。逐一检查全部13610个研究文件与清单一致、目录测试、静态build及Kimi消费者校验。历史legacy标签随总库保存。Windows clone使用命令级core.longpaths，不修改全局Git或注册表。消费者报告保存在.runtime/consumer-verification.json。
- 技能草案通过YAML/相对引用与9组合成契约测试。独立情境审读区分：用户接受UI边界可结束相应交付；用户明确全测仍保留必要缺口；仅收REA研究时排除普通UI工程。审读发现的catalog/仅评估模式适用歧义已修正。详情在skill-review.md。

复现：

```sh
npm test
npm run build
npm run verify
node tools/browser-qa.mjs
node --test skills/rea-design-transfer/scripts/*.test.mjs
```

浏览器检查使用本机已有Chromium和独立临时profile；其他电脑可用环境变量REA_QA_CHROME指定已安装浏览器。不会安装或更新浏览器。报告和截图保存在总库.runtime，不随Git提交。

来源自己的原生工程没有在本次分类任务中重新编译；macOS、Android权限/真实服务、逐字字体、所有原始交互等仍按来源报告的覆盖与未知处理。静态目录链接到本总库，完整本地资料随普通clone提供。

## Design Package v1与新skill联动

本次新技能名rea-design-transfer，增加标准设计包校验/profile导出和新旧入口兼容。浏览器/服务端共用迁移任务生成函数，传递规范仓库、ref、来源目录、设计入口、实际取得commit的记录要求以及原覆盖边界。

38项本地测试通过，包含原catalog/HTTP/导出检查、新格式draft不能登记、partial登记保留旧来源、modern/legacy迁移入口、技能副本提交/SHA锁以及27项技能契约测试。build与verify通过，原有4来源的13,610个研究文件逐文件核验一致；未修改它们的coverage或运行原App。后续新增测试数量以实际npm test为准。

本次另运行隔离headless浏览器检查，1440/390px目录、分类、真实文档、叠层Esc、比较和新技能说明入口通过，无横向溢出；未触及用户前台窗口、原App或模拟器。

技能独立前向试用和4个漏检修正见上游验证说明。检查通过只支持声明结构、引用和字节一致，不认证作者提供的REA元数据、不证明全平台UI还原。技能副本用skills/source.lock.json锁定上游提交和SHA，普通启动不自动联网更新。
