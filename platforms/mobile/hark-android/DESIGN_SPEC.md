# Hark Android 设计规范

本规范区分 **原版静态声明B** 与 **独立实施选择D**。已可复用原生颜色、自定义排版及局部发送控件几何；其他组件是明确标记的参考契约。用户目标是以后做相似手机控制App，本轮不复制Hark服务或账号机制。

## 来源与证据

包名com.hark.android，版本1.0.0-36，versionCode100000036，minSdk32、targetSdk37。API32对应 **Android12L**，不能笼统理解为所有Android12兼容。

base SHA-256：462218f35bf38795d74f8fb1fa034f5397ad71c1cdf7bcaba18a62896e4b3967。
ARM64 XAPK：78c0185432d32a3f196522f131e57f31e81aac5612e0fc22697661c844a2ddfc。
用户原ARMv7包是另一XAPK，base字节相同。Apksigner35.0.0核对APK v3、SourceStamp及split同证书；签名SHA-1与下载页一致。签名通过是字节/签名一致性结论，不是恶意软件检测或Play授权通过。

REA6.5.0实际执行提取、资源解码、APK身份及类/方法检查。原结果本地保留；公开回执只含审过的字段与原结果哈希，**不是canonical Evidence bundle**。主要真实REA ID：

| 范围 | Evidence ID |
| --- | --- |
| APK元数据/覆盖 | ev_7e29ebc87892c12773596333a063604332667e63b7015dd81703671b59c57432 |
| 颜色初始化 | ev_dd218f56e7d6a009738b77db216b39f969e12bdce71551605b153e89db848925 |
| 颜色角色对应 | ev_20e09962a1cafb6489a4a98f86604a8d4b09db732d9419debfd483ae946b6886 |
| 排版初始化 | ev_68530285dbcb19e38e82eb66ff04bfae02885ee285acf85feb0fc27968d79b7f |
| 局部发送控件 | ev_918f194eefe0bf95ce323b42968fd6ea9491d86ebe952dd570ffafafd1553b92 |

辅助JADX1.5.6全包浏览报告340个反编译错误；没有声称全包还原。这里只用成功复核的定向方法作为数值规格；初始化回退smali的返回类型保留。看到资源/标签/状态名不等于已运行。

## 颜色与材质

原生HarkColors有35字段、两套主题，角色对应来自jq6.toString，数值来自kq6初始化，theme分支另由eu6复核。完整值见[tokens](tokens/tokens.json)。RGBA保留源alpha，不能先铺白底后当源色。

| 角色 | Light B | Dark B |
| --- | --- | --- |
| textPrimary | #1d1d1f | #f5f5f7 |
| textSecondary / Tertiary | ink alpha .5 / .3 | ink alpha .5 / .3 |
| strokePrimary / Secondary | ink alpha .14 / .07 | ink alpha .14 / .07 |
| regularSurface | white alpha .6675 | #1d1d1f alpha .6175 |
| regularSolid | white alpha .96 | #1d1d1f alpha .96 |
| user bubble | white alpha .3 | #1d1d1f alpha .3 |
| assistant bubble | white alpha .98 | #1d1d1f alpha .7 |
| orange / green | #ff4d00 / #00a665 | #ff4d00 / #00a665 |
| red / redOn | #da2f35 / #f1555b | #f1555b / #da2f35 |

某个角色名不自动证明它绑定所有卡片。每屏背景合成、模糊与连续曲线裁切待A。参考渐变、16pxblur、普通24px圆角均为D；**没有把旧Web的WebGL背景当作Android事实**。

## 排版与字体

rr6绑定GT Standard L light/regular/medium/semibold/bold为300/400/500/600/700，另有Mono regular/medium；sr6绑定Hark Serif light/medium及italic。原件存在与family声明可核对，实际每节点/CJK字形fallback待运行。没有原字体再分发许可，保留来源/hash并withheld。

| 原生自定义角色B | size/lineHeight sp | weight |
| --- | --- | --- |
| displayLarge | 48/58 | 400 |
| displayMedium | 32/38，letterSpacing-.02em | 400 |
| headlineLarge | 24/29 | 400 |
| headlineMedium | 20/24 | 400 |
| titleLarge | 20/24 | 500 |
| titleMedium/bodyLarge/labelLarge | 15/18 | 500/400/500 |
| bodyMedium | 14/18 | 400 |
| titleSmall/bodySmall/labelMedium | 13/16 | 500/400/500 |
| labelSmall | 12/12 | 500 |

displaySmall/headlineSmall在此构造默认掩码中保留Material默认，不能扩成全部自定义。旧Web15/19或15/19.5不能替代本版原生15/18。参考将sp到CSSpx采用D的1:1起点，字体采用Arial/Helvetica；均不是Android物理像素换算，字形/fontScale/font padding/fallback待校准。

## 局部几何

sp2.c的regular发送触区52×48dp、visible ink52×32dp；compact触区48×48dp、ink32×32dp。箭头16dp，spinner18dp/stroke2dp。触区和可见胶囊分开，不能推广为全产品常量。外部enabled/alpha控制禁用外观，不假设统一透明度。

参考gutter20、group-gap16、card radius24、sheet radius28、header60均为D。真实顶栏、safe area、IME、手势返回等待A。Android目标用WindowInsets/IME，浏览器env(safe-area-inset-*)不证明原生适配。

## 组件、布局与核心交互

1. 发送控件B/partial：独立hit target、ink、arrow/spinner、sending描述；运行触控、禁用与转换待测。
2. 底部输入D：草稿/输入指引/send/busy/cancel/error/retry；独立reducer保证重复发送无效、失败保留、取消使旧结果失效。原ChatComposerState含conversationReady/sendInProgress/canSend/attachments/voice，只证明静态模型存在，不能证明这些D时序保证是Hark原行为。
3. 内容卡D：source/title/summary/structured rows/主次动作/详情。跨领域为野外调查任务，完成/撤销只改本地合成数据。
4. 选择sheet D：真实dialog，选择、遮罩、关闭和焦点恢复；原生返回/拖拽/叠层未知。
5. 移动导航D：正文滚动与固定composer分开；配对/画布/连接为目标业务。

slots/states/events/tokenRefs/unknowns见[组件](components/catalog.json)，组合见[layouts](patterns/layouts.json)，状态转移见[interactions](patterns/interactions.json)。参考无附件、语音、支付、连接器；指引按钮说明范围，不做空功能按钮。

## 动效

原生发送方法只证实arrow/spinner分支，时长/周期/spring未测，源记录为null/unknown。参考sheet选D的220ms/cubic-bezier(.2,.8,.2,1)，translateY24→0，opacity0→1；按压120ms/ease-out；spinner900ms是参考选择。减少动画关闭位移与旋转。参考参数/采样间隔都不变成原版测量。

## 资产、就绪与边界

可直接使用JSON角色/单位、独立CSS/reducer和合成样例。原字体许可可用时优先接入并校准；当前原件与反编译代码不上传。包含Lucide ISC许可，不证明每个图标都是Lucide；目标已有Lucide时优先复用并记录映射。参考基础箭头/加号是D，不是原品牌资产。

当前partial因为原生核心UI与关键动效未完成登录后的A对照，并非未穷尽原业务。现有Google APIs AVD真实运行遇Play授权门；已准备官方Play镜像与独立干净AVD，由用户正常Google/Hark登录。等待不视为拒绝。后续对chat/composer/card/sheet/settings补代表证据；不以支付/全部连接器/后端算法作为设计完成条件。
