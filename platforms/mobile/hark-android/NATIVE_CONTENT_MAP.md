# 继续逆向后能看到什么

**能读取不少真实界面结构，不必等原应用成功登录才能推进设计。** 本轮从已有 APK 与 JADX 输出继续定点追踪，新增 10 条结构化发现与 22 份真实 REA 回执；没有重新反编译整个包，也没有把授权门后的未知界面假称已运行。

来源仍是 `com.hark.android 1.0.0-36`，base SHA-256 `462218f35bf38795d74f8fb1fa034f5397ad71c1cdf7bcaba18a62896e4b3967`。下面均是静态 B；迁移实现与后面的 Web 手机参考为 D。

## 页面与导航结构

| 部分 | 已确认声明 | 还不能据此确定 |
| --- | --- | --- |
| Chat / Projects | MainSection placement=TabBar，顺序0/1 | 屏幕坐标、具体选中样式与转换动画 |
| Home | placement=NavIcon，与Chat共用main conversation输入模式；进入输入目标为assistant | 不能把它直接称作第三个底部Tab |
| Settings | placement=Avatar；该section的composer为Hidden | 头像点击后的实际页面动画 |
| 设置目标 | profile、memory、scheduled_tasks、connected_accounts、connected_cards、security、theme、system_preferences、about、subscription、plans、report_issue、active_sessions | 枚举存在不代表当前账号都显示；Connected Cards依赖callback存在 |
| 其他模型 | ProjectConversation、MediaViewer、CuaFullscreen、ScheduledTaskDetail、Accounts、Billing、SettingsPage | 这些是页面模型/分析事件候选，不是逐页运行或全路由完成证明 |

这比单独看到 HOME/CHAT/PROJECTS/SETTINGS 枚举更可靠：`fm0.a / cob.a / ig7.a` 的构造分配明确区分 TabBar、NavIcon 与 Avatar。不能据此画成四个相同底部Tab。

## 附件菜单可以做得更具体

`Camera → Photos → Files` 是输入控件上的**锚定弹出菜单**。定位器左右限制距窗口至少16dp，顶部至少12dp，优先放在锚点上方16dp：`y=max(12dp, anchor.top−16dp−popup.height)`；横向按布局方向对齐再钳制到窗口。

| 参数 | 包内声明 B |
| --- | --- |
| 菜单最小宽度 / 表面圆角 | 180dp / 24dp |
| 行最小触控高 / 视觉行最小高 | 44dp / 38dp |
| 行水平padding / 图文gap / icon | 12dp / 12dp / 16dp |
| disabled整体alpha | 0.35 |
| 按下形状圆角 | 16dp |
| 按下动画 | 150ms，cubic-bezier(.42,0,.58,1)，静态声明而非帧测 |

行选择的处理器先关闭菜单，再调用相应选择动作；Photos 使用系统媒体选择契约，Files 声明 MIME `*/*`。授权系统、相机和文件读取并未在本轮实际操作。旧独立参考的底部 sheet 保持 D，不能继续称作这份原生附件菜单的实测外形。

## 输入与失败反馈

输入状态把 conversationReady、canSend、sendInProgress、引导、附件、听写及语音通话分开。听写活动集合为 Starting/Listening/Stopping，模型另外含 Idle/Failed/AccessDenied/PermissionDenied。

局部布局可直接复用：引导 Intro 的发送禁用alpha为.4，其他引导分支.3；加号触控高48dp，常规宽32dp，在听写入口可见且未活动时30dp；末端gap按分支为0/4/8dp，加号icon16dp。具体条件与源文件SHA在结构化记录中保留，不把匿名boolean强行命名。

失败消息重试行可定位到 `u49.g`：标签12sp、图标18dp、水平padding8dp、间距4dp；重试或动画活动时禁用重复激活，并有“retrying”状态说明。它证明局部呈现/激活约束，不证明完整网络重试语义。

## 动态卡片为什么无法只靠 APK 看全

Hermes 包不是普通 JavaScript 文本。本轮按匹配的官方格式核验为 **v98，1,608,712 bytes，6,461函数、8,897字符串**，字符串边界和文件尾SHA1均通过。这是独立静态格式解析，不虚构新的 REA Evidence ID。

原生 `bqf.a` 向 `HarkAgenticWidgetContent` 传入 `cardId / renderId / bodyTSX / environment(colorScheme, variables) / styleSheetSHA / styleSheetURL / contentHorizontalPadding`。宿主是透明的内容高度适配容器，宽度依原生约束精确测量，高度依内容回传；runtime支持版本1–3。

因此包内能看到**卡片引擎、桥接协议和容器约束**，却不能列举所有运行时传入的TSX正文与样式。没有访问这些样式URL或用户内容。Vivic可借鉴容器与内容层级，继续接自己的已校验声明式Card renderer。

## 还原边界和下一步

主输入布局 `sp2.e` 的工具返回虽然标成complete，但实际正文包含 ConstructorVisitor/Method generation error，并混入后续方法；该回执明确标为不可用，不把它当完整布局源码。`sp2.b` 的有效输出是smali回退，另一布局构造有重复代码警告，只采纳可靠局部关系。

现在足够制作独立的导航壳、锚定菜单、输入控件变体、重试行及内容高度卡片容器。原生整页像素、IME/系统返回、大字、字体回退、真实背景合成与动态卡片内容仍未知。因此原生研究继续partial，但**Web手机适配已可以独立实施**，见 [运行参考与落点映射](WEB_TO_MOBILE.md)。

机器可读：[10项发现](evidence/native-content-findings.json)、[22份REA回执元数据](evidence/rea-deepening-receipts.json)。原APK、反编译实现和完整Hermes字符串表仍为私有原件；公开的是已审设计事实、哈希、真正的Evidence ID及限制，不是canonical Evidence bundle或原源码镜像。
