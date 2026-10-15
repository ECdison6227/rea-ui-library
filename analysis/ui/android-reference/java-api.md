# Java实现接口与语义契约的区别

`baseline/contracts.json` 定义迁移时应考虑的语义输入/状态/事件，不是可以直接实例化的Java SDK。下表为本次真实API；未实现字段由目标Agent接入已有组件或新增实现，不会因JSON有字段就视为完成。

| 语义组件 | 可调用Java入口 | 实际支持 | 需要适配/未实现 |
|---|---|---|---|
| TopBar | `topBar(title,width,back,onBack,onAdd)` | 标题、返回或添加；MainActivity接添加菜单 | 其他操作插槽、自动折叠标题、多尺寸inset |
| BottomNav | `bottomNav(width,selected,labels,onChange)` | 四项选中、点击事件；页面owner控制切换/重选保留 | 自定义项数、badge、其余两业务页 |
| ProgressHero | `MainActivity.ProgressHero` | 健康零数据静态绘制，owner绑定点击 | 非零progress、数据动画、手势详情 |
| MetricCard | `metricCard(width,title,description,asset,bars,onClick)` | 空态标题、说明、图标、占位图与点击 | contracts的context/state/renderer、真实图表、loading/error/stale |
| ProfileHeader | `profile(width,name,subtitle,onClick)` | 来宾头像、姓名、描述、勋章占位与点击 | 任意avatar、登录成功资料、远端profile |
| ShortcutGrid | `shortcutGrid(width,labels,assets,onClickIndex)` | 固定数据数组与入口事件 | 远端排序、badge、权限判断由owner接 |
| SurfaceCard | `surface()` / `surface(radius)` | 主题背景与普通圆角容器 | slots由调用者放入；busy/error/skeleton不是容器自带 |
| ChevronRow | `chevronRow(width,title,subtitle,value,icon,height,weight,onClick)` | 单/双行、尾值、图标和点击 | disabled/explain、自动多行撑高、leading throttle由owner实现 |
| SwitchRow | `switchRow(width,title,subtitle,checked,onRequestChange)` | 仅请求desired值；`setChecked(row,value)`由owner提交 | 说明文案由owner更新；系统权限/持久化/忙碌恢复未内置 |
| PrimaryButton | `primaryButton(title,enabled,onClick)` | 可点击/禁用；17sp本页变体 | busy/error、其他按钮变体/平滑曲线、未实现完整CommonBtn |

辅助 `confirmSheet(message,positive,onConfirm,onCancel)` 具有排队（无去重）、返回/外点/取消，`externalModal(true/false)`可暂停/恢复派发。源Provider对应B，选定关闭路径有A；本参考队列运行有D验证，原版压力/并发A未知。`dispose()`清队列与存活检查是D可靠性选择，不能改写成原版事实。

`MainActivity` 负责认证、导航、通知状态和资料fixture，不属于纯组件职责。详情/认证范围边界、部分未验证菜单去向保留明确标识。迁移时接自己的真实路由和服务；不得统一给所有行加账号守卫。
