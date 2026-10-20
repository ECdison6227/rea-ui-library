# GitHub Pages 自动更新

Pages 是轻量选型目录；完整设计包从GitHub仓库拉取。

仓库原套餐不支持私有仓库Pages；所有者于2026-10-20明确选择“直接公开仓库”。当前采用公开仓库与GitHub Actions Pages发布，无单独展示仓库。公开范围包括已有文件和历史；新增资料仍按入库规则审查。

## 新研究怎样出现在网页

1. 完成标准包和分发成员审查，通过`study:add`登记；更新已有研究则显式`study:lock`。
2. 将研究、catalog和文件锁一起推送到`main`。
3. `Publish library catalog`工作流自动运行测试、build和verify；通过后只上传`dist/`并部署到Pages。
4. 已打开的网页每60秒检查发布版本；切回网页或点击“检查更新”也会检查。新目录自动出现，保留当前平台/搜索/比较选择；已删除来源从比较选择中移除。

更新需先完成Actions部署，通常耗时数分钟；部署完成后网页在下一次检查时载入。界面代码变更会显示“刷新网页”，避免在阅读详情时强制刷新。

## 范围与权限

Pages只发布目录元数据、技能说明和已审核缩略图。不会把`platforms/`、原始证据、APK、工具缓存或`.runtime/`上传到Pages。资料按钮链接到GitHub里的相应设计包；复制给AI的指令直接拉取选定目录。

浏览器不接收GitHub令牌，不调用本机API。Pages设置的Build and deployment使用GitHub Actions；不依赖付费私有仓库Pages能力。

## 部署与排查

工作流：`.github/workflows/pages.yml`，支持main推送和手动`workflow_dispatch`。构建产物包含`data/version.json`，记录来源数量、目录摘要、界面版本、构建时间和提交SHA。

- 页面没有更新：查看Actions是否通过，以及Pages设置的Build and deployment是否为GitHub Actions。
- 刚发布读到不一致文件：网页保留旧目录，下次检查再载入；不会显示半份更新。
- 断网：保留当前已载入目录，恢复连接后再次检查。
- 格式或哈希检查失败：先修复研究/文件锁；工作流不自动接受变更，也不发布失败构建。

本地检查：`npm test`、`npm run build`、`npm run verify`。静态页面必须同时在`/rea-ui-library/`子路径验证，不能只测localhost根路径。
