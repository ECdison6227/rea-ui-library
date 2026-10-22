# Hark 历史基线与独立 D 参考对照

当前补充：[登录App A/D对照与条件](current-app.md)。已新增当前原站桌面/窄屏摘要A和实际字体/几何/时序；与D的画布、内容、背景及实现差异逐项保留，不提升为整页等价。

本轮已制作并运行 [Fieldnotes 独立参考](../reference/README.md)，不再是“参考尚未制作”。**D 运行通过；当前原版 A / D 等价对照仍为 partial。** 这里的两组原版图来自 2026-10-15 上海历史采集，2026-10-22 重新看像素；读旧图和做 D 交互都没有产生新的原版运行。当前公开站页面与登录后 App 的本轮覆盖仍由上层规范/evidence 入口分别记录，不能借本目录扩大。

## 实际交付

- [本机对照页](index.html)：历史摘要与窄屏图、D 对应状态及额外 D 状态。通过参考的本机服务打开，见 [启动方法](../reference/README.md)。
- [reference-run.json](reference-run.json)：13 项 D 检查、8 个原样 viewport PNG、实现字节 SHA、真实采集区间、CSS px/DPR、主题/上下文/焦点/滚动/状态与 computed 几何。
- [comparison.json](comparison.json)：历史 A 身份、原件 SHA、历史 computed、D 配对与差异；两个不同内容/来源条件没有计算伪精度相似度。
- [assets-reuse.json](../reference/assets-reuse.json)：直接引用字体/glyph 与原 helper 来源；不将 D 绑定写成当前原 App 绑定。

## 按规则核对

| 规则/组件 | 原依据 | D 实测/具体差异 |
| --- | --- | --- |
| 普通正文 | H15 CSS15/19/400，上下14.5/左右16，user/agent 填充不同；无 composer 同等 blur | 暗/浅及窄屏 body15/19，普通 fill backdrop-filter none；不同英语内容、气泡换行不能作为同文像素一致 |
| Composer | H15 computed1440×900：x464/y820/w512/h48；fill .62，host blur50；CSS bottom32、max219 | 桌面测得相同 x/y/w/h，窄屏390×844测得x16/y764/w358/h48；多行86，最长219并内部滚动。键盘仅 synthetic composition，OS键盘未测 |
| 连续圆角 | H15 原 path helper、fill/stroke 图层、radius24、curveFactor1.528665 | helper 字节精确保留，D宿主按 border-box resize/scroll重算；不是普通radius近似。原完整React宿主、touch路径和每个材质分支未复制 |
| 摘要预览 | H15 `.qvlc2z0` base290/max100%，>=1024 `.rfqo3s0` 覆盖 min424；标题源 `.qvlc2z2` Serif20/500 | D桌面424、窄屏290；D预览用GT15/500、标记图标、72px header与固定短预览，信息密度不同；512仅主内容带 |
| 独立阅读层 | 历史15-summary-open.png；computed title x392/y188/w656/h52，40/52/500；正文18/25.2/400、strong600 | D独立dialog/可滚动720px sheet与656px文字带；title x392/y178（比历史高10px），sheet y102（历史图y140），上方额外D类别。title40/52/500与body18/25.2相同；h2 D600与历史700不同 |
| 窄屏阅读 | 历史390图是聊天；没有这张历史摘要窄屏配对原件 | D358px sheet，title32/41.6，body18/25.2；可读性与滚动测试仅证明D适配，不能称原窄屏摘要已测 |
| 代码富卡 | H15代码Mono14/20/400；展开/收起与独立reply已有历史依据 | D直接Mono400，长行水平滚动、实际内容可展开，reply对象与draft分离；D全卡宽512/header70与源实例差异、样例JSON内容及展开阈值保留D身份 |
| 主题 | 历史浅/深变量、800ms/.45,0,.55,1，auto/system原控制器B | D浅/深无clone末尾浅色覆盖问题；System跟media，Auto明示Light fallback；固定背景非原天气/shader。减少动画0ms已测，800ms是采用声明值，不是测得当前原动画 |
| 导航与异步 | H13/H15有限结构与D目标接口，不证明现版持久化 | D420集合列、真实hash导航/Back、草稿与DOM/滚动保留；新笔记可搜索；pending防重、failure保草稿、Retry、Cancel无延迟插入。全部为D逻辑测试 |

## 运行检查与像素审阅

隔离 Chromium，英文中性内容，桌面1440×900和窄屏390×844，DPR1；窄屏是桌面浏览器 CSS viewport 模拟。运行器只允许其随机 loopback origin，外部请求0，page/console/HTTP错误0。截图是逐项状态采集，非原子快照；每图区间与 SHA 看 JSON。原版历史原件的 mtime 不用来编造采集时间。

检查覆盖：空输入禁用、48→86→219多行、synthetic IME不误提交、导航/Back保草稿、摘要独立层与焦点/滚动/Escape返回、四主题与reduced-motion、失败重试/防重、搜索新消息与空态、取消失效任务、代码展开/回复取消，以及两个画布的边界。一次早期运行发现 type=search 原生 Escape 先清空查询导致弹层未关闭，D增加显式dialog Escape关闭；自动化同步等待close完成。最终运行记录只表示修正后的实际测试字节。

截图逐图查看的真实时间、SHA绑定与所见问题记录在 `reference-run.json` 的 `pixelReview`；最终8图均为 reviewed，已逐图实际查看。没有把旧clone截图或本地参考图放进当前原站A证据。历史原件为：[摘要](../../evidence/screenshots/15-summary-open.png)、[窄屏](../../evidence/screenshots/27-mobile-chat.png)、[computed](../../evidence/styles/rich-computed-styles.json)。

## 剩余边界

固定D背景主动省略完整天空源码/实时输入；原页面正文、账号与私有资产未复制。本切片不覆盖原App全业务、原后端、天气、文件渲染、图片/链接栈、表格、语音、触摸/软键盘/系统IME。公共页面族的当前素材/参数不由此参考回填；当前App的原版取证由root单独进行并登记。本地D验收不扩大这些当前A结论。
