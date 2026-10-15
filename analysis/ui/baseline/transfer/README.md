# 跨业务原生运行证据

当前交付APK SHA-256为`4a62b1255795edbb422990a8198e3320f98b8cdf592a4966a42c18ef8c9d3b5b`。

| 当前样例状态 | 实际画面 | 采集元数据 |
|---|---|---|
| 空态 | [accepted-empty](accepted-empty/screen.png) | [meta](accepted-empty.meta.json) |
| 保存Design Notes后 | [accepted-ready](accepted-ready/screen.png) | [meta](accepted-ready.meta.json) |
| 本地模拟更新失败 | [accepted-error](accepted-error/screen.png) | [meta](accepted-error.meta.json) |

原生操作在`../../runtime/events/accepted-transfer-*`及hierarchy同前缀文件中；精确保存/取消/失败重试检查在`../tests/result.json`，与交付APK哈希一致。图像已实际检查，未将失败/过渡期的hierarchy当作有效稳定状态；另有ready-settled复查。

其他没有accepted前缀的图是早期迭代或校验过程资料，元数据保留其各自APK身份，不能混成当前版本。全部样例属于D本地会话fixture，不验证原健康App的后端与编辑行为。
