# Hark 运行实测与时序

本文件只报告实际运行结果。静态处理器与字段见 [卡片代码分析](card-code-analysis.md)，计算样式见 [设计分析](design-analysis.md)。研究用内容为虚构；除研究对话外没有修改真实待办、项目或订单。

## 操作结果

| 操作 | 实测前后 | 证据 |
|---|---|---|
| 代码卡展开/收起 | 精确 message selector 成功执行全部场景；按钮 expanded状态切换，内容区域变高后恢复 | [REA场景](../evidence/rea/05-code-bundle.json)，`ev_e41e6662d8c8987542a7ab16b5c9b52ff8e19aee54fc3606814a6da3f3dc3c8c` |
| 回复代码卡 | 回复消息状态进入输入区；Escape退出 | [回复截图](../evidence/screenshots/rea-code-06-reply-visible.png)、[输入DOM](../evidence/dom/05-reply-composer.html) |
| summary 主体点击 | 折叠预览 → 独立摘要 dialog；关闭返回对话 | [collapsed](../evidence/screenshots/14-summary-collapsed.png)、[open](../evidence/screenshots/15-summary-open.png) |
| 数值列点击3次 | 4.6/4.2/4.8原顺序保持；没有观察到排序 | [点击记录](../evidence/interactions/17-table-sorting.json) |
| table 右键 | 可见复制、下载、回复；没有触碰系统剪贴板 | [上下文菜单](../evidence/screenshots/19-table-context-menu.png) |
| table 下载 | 成功产生“虚构商品比较.csv”，保存为table-export.csv，3列3行+BOM | [下载收据](../evidence/interactions/21-table-download.json)、[CSV](../evidence/interactions/table-export.csv) |
| 两张链接卡 | `data-layout=two`，两外链，未提供pile展开按钮 | [链接截图](../evidence/screenshots/18-links-two.png) |
| 七张照片pile展开 | 原栈移入overlay、两列排布、scroller is-locked，按钮expanded=true | [展开截图](../evidence/screenshots/23-photo-stack-spread.png)、[操作记录](../evidence/interactions/14-card-actions.json) |
| Escape收起图栈 | 动画结束后spread=0、locked=false、button expanded=false | [最终状态](../evidence/interactions/24-collapse-final-state.json) |
| CSV文件打开 | 最初仅文件名，加载后表格viewer含列字母/行号/数据/下载 | [loaded截图](../evidence/screenshots/26-file-preview-loaded.png) |
| 窄屏390×844 | transcript390，composer x16/w358/h48/bottom32，font15/19，无文档横向溢出 | [几何](../evidence/styles/27-mobile-layout.json)、[截图](../evidence/screenshots/27-mobile-chat.png) |

第一轮REA场景使用宽泛 `[data-testid=code-card]`，匹配3张卡导致strict mode失败，后续动作未执行；失败记录保留在 [04-code-scenario.json](../evidence/rea/04-code-scenario.json)，不作为成功证据。第二轮使用精确message-id后7个步骤均completed，截图已独立导出。

图片收起的500ms快照仍能出现Hide/expanded=true，最终状态文件已经恢复false；不能把动画中间状态当作无法收起。下载和图片操作所在MCP调用超时，但根据文件收据与下一次UI检查确认实际已完成，未重复提交。

## 第一轮多卡生成

提示保存在 [06-research-prompt.txt](../evidence/interactions/06-research-prompt.txt)。100ms DOM探针只记录基线之后的新行；该轮动态包装内的message-id未被初版探针读取，所以assistant rowId可能null，按文本长度/高度/test-id识别。后续探针已改为查子节点ID。

| 探针相对时间 | UI变化 |
|---:|---|
| 0.66s | 用户研究提示进入对话 |
| 53.41s | 完整两句文字出现 |
| 53.64s | summary/table出现 |
| 53.82s | code/links出现 |
| 54.60s | 链接metadata补全，滚动距底约1px |

该轮Network只在page附加后启用，没有EventSource消息，不能据此认定未使用SSE。元数据仍证实POST `/api/messages/send` 200，随后 links请求unfurl及img-proxy。证据：[DOM样本](../evidence/interactions/06-stream-dom-samples.json)、[网络元数据](../evidence/network/06-stream-metadata.json)。

## 实时连接与短文字生成

CDP target inventory发现 `https://hark.com/assets/worker-C8dOtMWs.js` 的shared_worker，名为hark-connections；在其附加窗口只采到 `domain`通知形态。另一次页面reload后page target建立 `/api/user/events` 和 `/api/sync/conversation`；两者status200、mimeType=text/event-stream。

conversation初始snapshot body字节数96829，只留结构/类型/长度，未保存历史快照值。随后约15s周期出现ping。模式query保存参数名，不保存manager/conversation标识值；不能仅凭这份网络记录确认mode具体取值。证据：[worker身份](../evidence/network/07-worker-identity.json)、[worker事件](../evidence/network/10-text-stream-worker-metadata.json)、[重连网络](../evidence/network/11-reconnect-page-metadata.json)。

短提示保存在 [13-sse-prompt.txt](../evidence/interactions/13-sse-prompt.txt)。以POST发出为网络0点：

| 相对时间 | 网络事件 |
|---:|---|
| 0.404–0.439s | seq936/937，entry_add、narration_update，另有request_update |
| 10.225s | seq938 |
| 10.378s | seq939，entry_add的content形态长度82；message_update字段agentId/isStreaming/jobStatus |
| 10.456s | seq941，entry_add/narration_update |
| 12.700s | seq942，message_update字段agentId/isStreaming/jobStatus/source |

UI探针约10.63s第一次看到assistant正文，textLength86包含82字符正文与时间文字，后续正文长度没有增长。这里只证明本次完整消息分批到达和状态更新，不能证明普通文字被逐token推送。`isStreaming`值按隐私策略未保存，只保留boolean形态；完成状态的字段语义由代码进一步说明。

探针启用后约10.9s滚动距底从118px降到13px，后续趋于稳定。这里只记录自动追随现象；70px锁底阈值来自静态代码，未把一次动画距离当作阈值测量。证据：[SSE形态](../evidence/network/13-sse-metadata.json)、[DOM样本](../evidence/interactions/13-sse-dom-samples.json)。

另一条十二句普通文字提示也已完成，但探针被页面重载清除前没有保存独立全程样本；只保留prompt、progress截图与最终DOM，不用于宣称逐字时序。

## 未触发或仅静态支持

- 本次三行表格的列头点击未排序；代码排序循环和渲染条件仍是静态事实。
- 普通文字测试没有出现可验证的全局Stop。工具Stop确认、请求取消与回滚只静态分析。
- 真实订单/机票动作、账户连接、todo修改未执行；卡片文案不证明现实订单状态。
- 滑动回复、drag flip速度阈值、断网重连、所有light主题和权限分支未实测。
- 页面已有文件/照片内容用于验证当前可见viewer，原始证据留在本地，分析不评价其内容。
