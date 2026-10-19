# 同一套组件迁移到资料管理

样例在同一个 Android APK 内运行，复用 `StyleKit`、`Tokens` 和原生页面容器：

```powershell
adb -s emulator-5580 shell am start -S -W -n study.style.reference/.MainActivity --es scene transfer --es theme day
```

实现为 `MainActivity.java` 的 `transferPage()`、`settings(true)`、`editDocument()`、`refreshDocuments()`，组件为 `StyleKit.java`。证据见 `../baseline/transfer/` 和 `../baseline/tests/result.json`。数据只在当前会话保留。

[查看最终原生截图：空态→有数据→失败恢复](index.html)。这些是D独立样例，不是原App截图。每张均关联最终APK SHA，旧迭代图片保留但不作为当前版本。

| 来源习惯 | 样例保留 | 替换/独立决策 |
|---|---|---|
| 轻标题、尾部添加、底导航 | 资料标题、添加资料、图标文字关系 | 资料/收藏/共享/我的；后两区不扩充业务 |
| 浅灰页面、白分区、内容标题加重 | 总数、资料行、偏好设置 | 不移植三环、卡路里、设备、会员 |
| 空态有下一步 | 添加第一份资料的主动作 | 文案与业务原因是D |
| 进入子任务后返回上下文 | 偏好设置返回仍见已保存资料 | 示例工作区/共享不是实际同步权限 |
| 主次动作与取消 | 添加/重命名可保存取消，空名称内联错误 | 编辑器、IME、校验与保存是D，来源未测此业务 |
| 异步恢复 | 忙碌禁重复提交，失败保留数据并重试 | 700ms与故障开关为fixture，不是原App时长/算法 |

实测路径：添加→空名称保存仍留在编辑器→输入名称保存→再次添加并取消→偏好设置返回→模拟更新失败→保留原资料并重试。它验证这些组件可以支撑别的业务，不证明健康App的保存逻辑。

下个Agent把组合接到目标已有路由、数据、账号与平台服务，保持目标正确功能与数据，替换fixture。不要将“参考范围外”的研究占位框当作产品功能。
