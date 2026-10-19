# 在总库新增REA分析

本库是唯一维护入口。按被研究产品的实际平台放置，网页使用手机宽度或原生参考使用浏览器预览都不改变原版平台。只纳入有目标身份、REA研究入口、规则/证据输出的分析；普通产品和UI实验不单独纳入。

1. 把已审研究资料直接放进 `platforms/web/<id>/`、`platforms/desktop/<id>/` 或 `platforms/mobile/<id>/`。保留原始证据字节、技术栈和运行方式。原图的标注、加工另存。不要复制嵌套.git、凭据、私有账号数据、运行缓存或依赖目录；旧仓库如需删除，先备份完整历史和附加资料。
2. 复制 [profile模板](../catalog/study.template.json) 到本机.runtime，填写目标版本/URL或包身份、REA方法与研究入口、原版平台、文档入口、已覆盖范围、迁移可用性、未知和启动方式。`visibility: public`仅表示允许目录展示该profile，不表示原始证据可以公开；本仓库仍为私有。原始敏感内容必须预先筛除或独立保存。封面可不填；如添加缩略图，先查看原图，再设置reviewed。
3. 登记并生成逐文件清单：

```sh
npm run study:add -- --profile .runtime/my-study.json
npm test
npm run build
npm run verify
```

4. 检查git diff、分类、入口和范围后，提交并推送总库。界面计数与平台索引自动取自登记表，无需改固定数量。

修改已有分析时，先审查变更和来源AGENTS，再运行 `npm run study:lock -- --id <id>` 更新该分析的文件清单，随后执行build与verify。普通启动和构建只校验已有哈希，不会悄悄把文件变更认成已审核。新增或修改profile后，build会刷新imports.lock。文件清单是完整性检查，不会把历史截图变成新实测，也不会把未测功能标为通过。

完整历史：迁入的三个旧库可在 `legacy/hark/main`、`legacy/mi-fitness/main`、`legacy/mirasim/main` 标签下查看。旧GitHub URL仅作为历史出处；日常阅读与更新都使用总库platforms目录。
