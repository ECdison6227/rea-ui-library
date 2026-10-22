# 独立组件参考D

零依赖HTML/CSS/ES modules，适于后续React/Capacitor薄包装。这不是原版Compose执行结果。颜色/排版/局部发送几何来自B；壳、模糊、半径、状态机和动效明确为D。

包目录执行node reference/serve.mjs，打开http://127.0.0.1:4327。可用PORT环境变量换空闲端口；服务器仅loopback，只允许4个参考文件，无账号/服务。node --test reference/model.test.mjs验证失败/取消留草稿、防重复、旧回调无效。

可体验主题、sheet、详情、完成/撤销、Enter发送/Shift+Enter换行、失败重试与取消。输入指引说明附件需目标集成。主题只用参考专用localStorage键；任务与1300ms等待均为合成。

复用入口：components.css、model.mjs、app.mjs。字体系统fallback待校准。实际浏览器测试和截图见[对照](../comparison/README.md)。浏览器测试不证明Android touch/IME/读屏/back。
