# Hark 聊天界面调查覆盖

目标：hark.com当前已登录浏览器版本。方法：REA MCP、浏览器MCP/CDP、实际交互与已发布资源静态证据。

| 原问题 | 状态 | 结果入口 |
|---|---|---|
| 交互卡片类型、结构、状态 | 已回答（捕获版本），服务端全schema未验证 | card-code-analysis.md |
| 正文、标题、代码、表格排版 | 已实测组件，暗色与窄屏基础布局 | design-analysis.md |
| 背景与材质 | 可见canvas、完整HTML/shader和fill层已提取；天气分支运行不完整 | design-analysis.md |
| 设计变量 | 544原作用域声明和计算值已导出 | design-tokens.css/json |
| 流式协议、增量、滚动、停止 | SSE已实测，样例无逐token增量；Stop/断线恢复仅代码 | runtime-analysis.md |
| 卡片交互 | 展开/回复/图栈/下载/预览已实测；表格排序未触发 | runtime-analysis.md |

结论与未知项见主报告和findings-ledger.json。登录由用户完成；凭据留在项目外profile。原始截图/DOM包含授权可见账号内容，网络只保留去值形态。
