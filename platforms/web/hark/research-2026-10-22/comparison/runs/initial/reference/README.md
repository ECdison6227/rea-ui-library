# Hark 独立组件参考（D）

这是一个已实际运行的小型跨业务切片：用中性的 Fieldnotes 社区观察笔记，验证 Hark 的文字角色、材质、连续圆角、输入、富卡和阅读层能否迁移。全部页面与交互属于 **D 独立实现**；引用的原字体、glyph 和路径函数保留 **B 原资产/源码** 身份。它不是当前 Hark 原站，不替代登录后 A 取证，也不是 `/Users/cjr/Desktop/hark-clone` 的完整重建。

## 运行

Node.js 22.13+，无运行依赖，无安装步骤。由本目录执行：

```sh
npm run dev
```

打开 `http://127.0.0.1:4393/research-2026-10-22/reference/`；对照页在 `http://127.0.0.1:4393/research-2026-10-22/comparison/`。可用 `PORT` 改端口。服务只监听 `127.0.0.1`，只提供本参考、对照、已保存字体/glyph，以及两张已检查的历史中性截图；不连接原厂接口，不读取账号 profile。

## 可交互的组件

| 组件 | 可操作状态 | 实施边界 |
| --- | --- | --- |
| 导航 | Notes、Library、Projects、浏览器 Back；草稿/线程 DOM/滚动保留 | D 路由与 420px 集合列；不复刻原 Home 并列面板 |
| Composer | 空、单行、多行、219px 上限、回复对象、pending、error、retry、cancel | 默认 512×48 / 窄屏 358×48，15/19，bottom32；700ms 本地存入模拟，不调用 AI/网络 |
| 材质 | 暗/浅 fill、stroke、composer blur50；气泡无 blur | 路径几何直接取原 helper，DOM/ResizeObserver/scroll 宿主为 D |
| 摘要 | 紧凑预览 → 独立阅读 dialog → Escape/关闭返回；复制全文 | 非卡内 max-height 展开；native dialog 提供 modal/focus containment |
| 富代码卡 | 真实溢出、展开/收起、Mono400、独立回复对象及取消 | 仅本地 JSON 样本；不是完整 renderer 分发器 |
| 搜索 | 搜已有样本与本次新保存笔记；命中定位/焦点；空状态 | D 本地内存索引，不连接原站 |
| 主题 | Light、Dark、System、Auto，减少动画 | System 跟随 media；Auto 明示固定 Light 的 D fallback，不假装天气算法 |

在 Appearance 勾选 **Fail the next save**，输入任意中性文字并保存，可以检验失败保草稿 → Retry。保存期间的取消按钮会使当前任务失效，700ms 到达后不会补插消息。刷新会清除本页草稿和新笔记；无跨刷新持久化承诺。

## 直接复用与 D 补足

- 原文件相对引用：GT Standard L 400/500/600、Hark Serif 300/500、GT Standard Mono400；11 个历史 `glyph-*.svg` 原路径，不用 lucide 重画。每个文件的本地路径、字节和 SHA256 见 [assets-reuse.json](assets-reuse.json)。字体声明绑定是 D；本参考的真实 `document.fonts` 状态另见运行记录，未使用的 face 可以保持 unloaded。
- [corner-path.mjs](assets/corner-path.mjs) 的几何 helper 是已捕获原连续圆角算法的精确前缀，常量 `1.528665` 等没有近似改写；仅加 ES module export。[来源切片与 SHA](assets/corner-path-provenance.json) 保留 H15 日期。宿主对 fill/stroke 的观察、滚动平移、模块入口属于 D，不是整段原 React 组件的原样运行。
- 保留 H15 基线：正文15/19；composer48/24/blur50；摘要详情40/52/500、18/25.2/400与strong600；摘要预览 base290、>=1024时424。当前 App 是否仍沿用，留给 root 的当前 A/B 取证。
- 明确 D：固定单色背景（未移植原天空 shader）、中性品牌与内容、导航/搜索/本地保存、native dialog/焦点策略、预览标题 GT15/500、阅读 h2 用可得 L600（历史 computed 为700）、窄屏阅读标题32/41.6、dialog enter220ms和立即关闭、错误与取消策略。完整差异见 [comparison/README.md](../comparison/README.md)。

## 实测与复跑

参考运行之后，收尾检查修正了本地服务对编码路径中父目录片段的处理；5项真实HTTP路径/方法检查见 [server-path-check.json](../comparison/server-path-check.json)。UI代码、样式和资产未改；原13项UI运行及其当时实现SHA保持原样，新增服务检查单独记录。

[reference-run.json](../comparison/reference-run.json) 记录一次隔离 Playwright/Chromium 本地运行：13 项有意义交互检查、8 个原样 viewport PNG、每图 CSS viewport/DPR/主题/滚动/焦点/状态/字体/几何/采集区间和 SHA256。所有 D 截图均需结合 `pixelReview` 看实际审阅状态，不把测试成功等同于当前原版一致。原版未在此次参考任务中运行。

校验使用已安装 Playwright，不下载依赖。设置已有 module 文件路径与 Chromium 可执行文件，例如：

```sh
PLAYWRIGHT_MODULE=/path/to/playwright/index.mjs \
CHROME_EXECUTABLE=/path/to/chrome \
npm run verify
```

运行器自己启动随机本机端口及独立浏览器进程，不附着当前浏览器，拦截任何非本机请求。它会覆写 `comparison/reference-run.json` 与 D 截图；复跑后的图会重新标为 pending，须再看像素。真正系统 IME、手机触摸、软键盘、安全区、跨刷新保存、原后端/图片栈/链接栈/媒体/表格/文件渲染均未由此验证。

入口：[设计规范](../../DESIGN_SPEC.md) · [组件](../../components/catalog.json) · [动效](../../motion/catalog.json) · [接入合同](../../ADAPTATION_GUIDE.md) · [实测对照](../comparison/README.md)。
