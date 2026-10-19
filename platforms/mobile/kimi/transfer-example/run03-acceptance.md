# Fieldnotes 不同业务迁移 · Run03 实测

**R03约定的最小不同业务样本已完成实际原生验收。** 这是一项D独立迁移验证，与 [原版A↔D对照](../comparison/native-run03.md) 分开。完整条件、操作时间、精确PNG/XML/capture SHA及R01–R03逐项验收保存在 [run03-acceptance.json](../reference/run03-acceptance.json)。

运行的是 `org.research.kimistyle` 最终APK `057196b83df26f4ae47da0181d1af7c89fda2423f92b9bfb147c7575837ca140`，同一专用Android15/API35模拟器、1080×2400、420dpi、fontScale1.0、英语、浅色。Java View/EditText/Dialog/Canvas和SharedPreferences；无WebView、网络、账号或同步服务。所有输入仅为新建可丢弃fixture，没有访问原账号历史或文件。

业务从聊天改为**本地资料笔记标题管理**。Fieldnotes页面没有Kimi品牌、模型选择、吉祥物或回答工具栏；保存/删除的是独立App内的一条本地笔记。模型能力、原业务算法与后端不在迁移目标中。

| 保留的设计/交互习惯 | Fieldnotes实现与验证 | 替换及独立边界 |
| --- | --- | --- |
| 主次文字、留白、圆角白色分组 | 保存卡片分出标题、Saved on this device说明和相邻Edit/Delete | 笔记标题取代消息/回答，色值/字重/尺寸映射为D |
| 空态说明和清楚的下一步 | “Make room for your next idea”与Add a note | 文案为D；本地新建替代登录/聊天发现 |
| 编辑指导和保留输入 | “You can return later; your draft stays here.”，Return/New和无scene重启留存 | 明确SharedPreferences为D，不推断原版草稿存储 |
| 忙碌、附近取消、失败后显式重试 | Saving… / Cancel saving；“Your draft is safe.” / Retry saving | 6000ms本地回调、错误fixture及epoch取消全为D |
| 风险动作确认 | Delete the selected 1 note；Cancel保留，Delete清空 | 只改自己的单条笔记；未实现撤销或同步 |
| 当前值设置和预览/保存区分 | 共用Appearance、Theme和Font；原生键盘避让 | 字号Save回聊天是参考的原版配对路线，正式笔记产品应自行决定保存后目的地 |

## 实际执行结果

| 路径 | 观察到的结果 | 安全原图 |
| --- | --- | --- |
| 空态→Add a note | 显示原因说明、New和Add a note，能进入编辑器 | [043空态](../evidence/reference/run03-20261016/scenes/nr03-043-notes-empty/screen.png) |
| 空标题保存与返回 | 字段出现红色错误图标；聚焦后显示Enter a note title原生提示；Back回库 | [044校验](../evidence/reference/run03-20261016/scenes/nr03-044-note-validation/screen.png) · [063提示+键盘](../evidence/reference/run03-20261016/scenes/nr03-063-note-validation-message/screen.png) · [045返回](../evidence/reference/run03-20261016/scenes/nr03-045-note-back-library/screen.png) |
| 草稿导航保留 | Run03 transfer draft经Return to notes→New仍在 | [046](../evidence/reference/run03-20261016/scenes/nr03-046-note-draft-navigation/screen.png) |
| 草稿进程保留 | stop→launch，不传scene；从Chat导航Fieldnotes→New后同草稿仍在 | [047](../evidence/reference/run03-20261016/scenes/nr03-047-note-draft-process/screen.png) |
| 正常保存 | 实际Save note，等待7秒后Return；库中出现Run03 transfer draft与Saved on this device | [048](../evidence/reference/run03-20261016/scenes/nr03-048-note-saved/screen.png) |
| 已保存内容进程保留 | 再次stop→launch，不传scene；导航库后保存卡片仍在 | [049](../evidence/reference/run03-20261016/scenes/nr03-049-note-saved-process/screen.png) |
| held忙碌与取消 | Saving…和Cancel saving可见；取消后标题仍在，原Research reading list保存卡片保留 | [050忙碌](../evidence/reference/run03-20261016/scenes/nr03-050-note-held-busy/screen.png) · [051取消](../evidence/reference/run03-20261016/scenes/nr03-051-note-held-cancel/screen.png) · [052旧内容](../evidence/reference/run03-20261016/scenes/nr03-052-note-cancel-existing/screen.png) |
| 真实保存取消与晚到 | 实际Save后fresh Cancel saving，等待7秒；草稿仍在，返回库仍是Research reading list，New又恢复取消的草稿 | [053晚到后草稿](../evidence/reference/run03-20261016/scenes/nr03-053-note-real-cancel-late/screen.png) · [054未提交](../evidence/reference/run03-20261016/scenes/nr03-054-note-not-committed/screen.png) · [055重入](../evidence/reference/run03-20261016/scenes/nr03-055-note-cancel-reenter/screen.png) |
| 错误→重试 | 显示Couldn't save this note. Your draft is safe.与保留标题；Retry saving后7秒返回库，标题成功提交 | [056错误](../evidence/reference/run03-20261016/scenes/nr03-056-note-error/screen.png) · [057恢复](../evidence/reference/run03-20261016/scenes/nr03-057-note-retry-saved/screen.png) |
| 删除取消→确认 | Cancel保留原卡片；再次Delete并确认后回空态 | [058确认](../evidence/reference/run03-20261016/scenes/nr03-058-note-delete-dialog/screen.png) · [059保留](../evidence/reference/run03-20261016/scenes/nr03-059-note-delete-cancel/screen.png) · [060清空](../evidence/reference/run03-20261016/scenes/nr03-060-note-deleted/screen.png) |
| 保存中进程中断 | 实际Save后stop→launch，不传scene；回编辑器为保留草稿的错误态；显式Retry后成功 | [061恢复策略](../evidence/reference/run03-20261016/scenes/nr03-061-note-interrupted-save/screen.png) · [062重试](../evidence/reference/run03-20261016/scenes/nr03-062-note-interrupted-retry/screen.png) |

实际取消路径中，Save动作完成到Cancel动作完成为4.459秒，取消到晚到后PNG为11.033秒。它跨过独立实现6000ms回调的期限，并用**不同的旧保存标题**证明未提交，而非仅看按钮恢复。此为主机动作边界，非精确Android回调或动画时长；held busy自身没有待到期回调，未用held场景代替真实取消验证。

截图先、UIA后，均非原子。每张PNG实际查看，另读XML、身份与SHA。063的原生错误弹层文字可见于像素，但不在目标package节点摘要中；两种证据分别保留。原图未改，分享仅允许 [已审精确字节](../docs/native-reference-safe-scenes.json)。

## 可迁移范围与限制

这个样本已证明空态引导、输入保留、正常保存、忙碌/取消、错误恢复及确认机制可以组合到不同业务。笔记数据模型、校验、取消epoch、持久化、进程中断恢复均为D策略，不是原版内部逻辑。

样本只有一个标题槽位，未实现多笔记、搜索、正文附件、远端同步、版本冲突或撤销。保存完成后编辑器清空且仍停留原页，没有独立成功横幅；需Return/Back看到保存卡片。保存中输入仍可编辑，完成使用当时的draft；未声称输入快照语义。全页重建可能重置焦点/选区。英语、当前模拟器/字号已测；TalkBack、RTL、真实设备、其他系统版本和原版精确字体/动效未验收。

最终执行restore，仅清理独立参考fixture；设置恢复Follow System/Follow System/English，主页Instant High和空输入，随后停止D并交回唯一设备控制权。原账号和用户要求保留的原项目/内容未操作，AVD最终关闭由主代理另行记录。
