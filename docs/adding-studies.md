# 入库标准：让AI能直接实施

用户完成软件功能后，选定参考App，让AI拉取本库即可应用其UI风格、动画和用户逻辑。入库验收围绕这个用途，不以原业务全功能测试比例作为门槛。

## 新增与升级要求

新来源必须采用 **Design Package v1**，放入`platforms/web|desktop|mobile/<id>/`。README先说明适合什么、怎么给AI用，再列证据和限制。平台按原版判断。

- 固定根入口、独立设计规范、身份/版本、真实REA来源及适用范围。
- 非空核心tokens、组件/状态、布局、交互、动效。组件含slots/events/states，交互有from/event/to/result。
- 可实施的视觉与动画选择。源未知保留unknown，同时给明确D替代；至少一个核心动效有数值时序与曲线。空模板、只有默认截图或只有调色板不通过。
- 参考、原版对照和跨业务接入，说明实际运行范围。partial可入库，但须让AI知道能用什么以及如何实施。
- 相对路径可解析、真实SHA、原单位、历史采集/复核时间及A/B/C/D边界。
- 审查实际分发成员，排除凭据、账号profile、私人画面、AVD/SDK/JDK缓存、嵌套.git；原件遗漏保留原因/哈希。

Hark Web已以当前Design Package v1替换默认入口并保留历史来源；Mirasim仍为明确登记的`legacy-research`旧研究参考；新增来源不再允许旧格式。小米、Kimi、多邻国从标准入口消费。历史事实与用户此前明确要求的业务清单保留，未测不改verified。

## 注册与验证

```sh
node skills/rea-design-transfer/scripts/design-package.mjs validate platforms/mobile/my-product/design-package.json
node skills/rea-design-transfer/scripts/design-package.mjs profile platforms/mobile/my-product/design-package.json --root platforms/mobile/my-product --output .runtime/my-product.profile.json
npm run study:add -- --profile .runtime/my-product.profile.json
npm test
npm run build
npm run verify
```

`study:add`执行格式与消费者契约两层检查：前者核对引用/字节，后者检查是否有可用于实施的核心内容。它们不代替真实像素/动画/交互验收。空draft或缺实施内容的包应被拒绝。

已有来源更新先审查变更，再显式`npm run study:lock -- --id <id>`。普通build/verify不自动接受字节变化。已获用户提交/推送授权则检查后执行，不重复确认。

`visibility:public`表示可进入目录展示与静态导出，不控制GitHub仓库权限。总库已按所有者要求公开，提交的文件及历史可被访问；将目录项设为private或隐藏链接不能保护已提交文件。封面需实际审核；迁移样例跟随来源，不独立增加App数量。不要创建子模块或新分析仓库。

登记/更新及文件锁一起推送main后，Pages工作流自动测试、构建、校验并发布目录。网页每60秒检查新版本，无需按新来源手改页面。Pages只上传dist的轻量允许列表，完整研究从公开仓库按需拉取。详见[Pages说明](pages.md)。
