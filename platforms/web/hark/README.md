# Hark 聊天界面研究

采集日期：2026-10-15（Asia/Shanghai）。目标：[hark.com/chat](https://hark.com/chat)。方法：REA MCP、浏览器 MCP/CDP、实际 UI 操作与已发布前端资源静态分析。

从 [主报告](reports/hark-chat-analysis.md) 开始阅读。

| 文件 | 内容 |
|---|---|
| [主报告](reports/hark-chat-analysis.md) | 六个重点、结论、交互状态与证据边界 |
| [截图对照与时序查看器](reports/evidence-gallery.html) | 七组前后截图、可拖动的文字显示时序与 SSE 事件表；可直接在浏览器打开 |
| [设计分析](reports/design-analysis.md) | 排版、材质、布局、变量、天空 shader |
| [卡片代码分析](reports/card-code-analysis.md) | 真实类型分发、字段、事件处理与流式消费链 |
| [运行实测](reports/runtime-analysis.md) | 点击前后状态、SSE 时序、未触发行为 |
| [原始变量 CSS](reports/design-tokens.css) / [JSON](reports/design-tokens.json) | 544 个保留名称、值、作用域的变量声明 |
| [结论清单](reports/findings-ledger.json) | 实测/静态/推断/未验证的机器可读记录 |
| [证据索引](evidence/index.json) | 文件字节数与 SHA-256、REA Evidence ID 定位 |

`evidence/screenshots/` 保存截图；`dom/` 保存页面与场景 DOM；`styles/` 保存浏览器实际加载的 CSS 和计算样式；`scripts/` 保存 REA 导出的 36 个已发布脚本、32 个精确逻辑片段和天空核心资源；`network/` 与 `interactions/` 保存去值后的网络结构、操作记录和研究对话时序。

浏览器登录由用户完成，凭据保留在项目之外的独立浏览器 profile。项目原始截图/DOM 包含本次授权查看的既有聊天、首页面板与文件内容；分析结论只引用与界面行为相关的证据。网络采样没有导出 Cookie、鉴权头、密码、验证码或完整 SSE 快照内容。

报告中的相对链接可在编辑器打开。重新生成索引并核验已保存资产：

```sh
python3 scripts/build-investigation-index.py
python3 scripts/verify-investigation.py
```
