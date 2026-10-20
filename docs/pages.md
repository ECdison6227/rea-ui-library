# GitHub Pages 自动更新

Pages 是公开的轻量选型目录；完整设计包继续存放在私有GitHub仓库。

当前状态（2026-10-20）：代码与本地验证已完成；GitHub创建Pages接口返回422，提示当前套餐不支持这个私有仓库。尚未部署。用户需选择独立公开的目录展示仓库，或升级支持私有仓库Pages的套餐；不能为上线而擅自公开设计库。

## 新研究怎样出现在网页

1. 完成标准包和分发成员审查，通过`study:add`登记；更新已有研究则显式`study:lock`。
2. 将研究、catalog和文件锁一起推送到`main`。
3. `Publish library catalog`工作流自动运行测试、build和verify；通过后只上传`dist/`并部署到Pages。
4. 已打开的网页每60秒检查发布版本；切回网页或点击“检查更新”也会检查。新目录自动出现，保留当前平台/搜索/比较选择；已删除来源从比较选择中移除。

这不是直接监听GitHub私有仓库的实时数据库。更新需先完成Actions部署，通常耗时数分钟；部署完成后网页在下一次检查时载入。界面代码变更会显示“刷新网页”，避免在阅读详情时强制刷新。

## 范围与权限

Pages只发布目录元数据、技能说明和已审核缩略图，保持现有公开build边界。不会把`platforms/`、原始证据、私人画面、APK、工具缓存或`.runtime/`上传到Pages。原资料按钮链接到私有GitHub仓库，需已有访问权限；复制给AI的指令仍让AI拉取私有设计包。

浏览器不接收GitHub令牌，不调用本机API。仓库私有性不改变。若GitHub账户套餐/组织策略不允许私有仓库Pages，自动部署不能绕过该限制；仓库设置应由有权限的账号启用GitHub Actions发布。

## 部署与排查

工作流：`.github/workflows/pages.yml`，支持main推送和手动`workflow_dispatch`。构建产物包含`data/version.json`，记录来源数量、目录摘要、界面版本、构建时间和提交SHA。

- 页面没有更新：查看Actions是否通过，以及Pages设置的Build and deployment是否为GitHub Actions。
- 刚发布读到不一致文件：网页保留旧目录，下次检查再载入；不会显示半份更新。
- 断网：保留当前已载入目录，恢复连接后再次检查。
- 格式或哈希检查失败：先修复研究/文件锁；工作流不自动接受变更，也不发布失败构建。

本地检查：`npm test`、`npm run build`、`npm run verify`。静态页面必须同时在`/rea-ui-library/`子路径验证，不能只测localhost根路径。
