# 小米运动健康 · 视觉、动画与交互规格

复用柔和分组表面、宽松信息层级、概览→详情、主导航与设置行。健康指标/账号/设备逻辑由目标业务替换。

本规格面向“已有功能的软件改风格”。来源3.57.0 / Mine11.202.21；原始素材、时间与证据保留。当前partial表示有已知还原差异，不代表不能用于明确范围的实施。

## 实施阅读顺序

1. [视觉角色](design/tokens.json)：先映射core角色，保留单位和作用域。
2. [组件](design/components.json)与[布局](design/layouts.json)：实现插槽、状态与内容自适应。
3. [交互](design/interactions.json)：返回、保存取消、草稿、忙碌与重试。
4. [动画](design/motion.json)：明确源声明/观察/目标选择，落实中断与减少动画。
5. [参考](analysis/ui/android-reference/README.md)、[对照](analysis/ui/baseline/comparison/final/index.html)、[迁移指南](ADAPTATION_GUIDE.md)。

## 视觉规则

- 原生卡片16dp与RN分区18dp分开；不能统一为单一圆角。
- 页面/导航/卡片保存day/night角色；健康大标题32sp，设置行16sp；我的正文来自Mine11.202.21运行bundle。
- 十组件有明确slots/events/controlledState与使用场景；完整原契约另保留。
- 按来源同环境比较字体/inset，再将native/YRN差异映射到目标。

## 组件地图

| 组件 | 结构与关键状态 |
| --- | --- |
| TopBar | {"health":{"titleSp":32,"fontFamily":"sans-serif-light","titleBoundsPx":[72,151,248,280]},"mine":{"title":" ","titleSizeLogical":32,"paddingStartDp":26,"paddingEndDp":20,"actionsLogical":36},"settings":{"titleSp":32,"fontFamily":"sans-serif (resolved resource string)","titleBoundsPx":[71,301,247,430],"backBoundsPx":[55,158,187,268],"actionBarBoundsPx":[0,136,1080,457]}} |
| BottomNav | {"heightDp":66.66998,"topPaddingDp":2,"labelSp":11,"labelStyle":"FontMedium = mipro-regular + bold","iconToLabelGapDp":3,"indicator":"none","ripple":"transparent","daySurface":"#FFFFFF","nightSurface":"#242424","selectedDay":"#FF612E","selectedNight":"#F04D18","boundsPx":[0,2151,1080,2334],"systemBottomInset":"separate; current 66 physical px"} |
| ProgressHero | {"visualWidthDp":200,"reflectionSizeDp":[210,80],"summaryTopMarginDp":168,"outerInsetDp":12,"summaryPaddingTopDp":16,"columns":3,"columnGapDp":8,"labelSp":14,"labelFont":"MiSansLatin Medium with CJK fallback","valueSp":24,"valueFont":"MiSansLatin SemiBold","valueIncludeFontPadding":false,"targetSp":12,"targetStyle":"FontDemiBold = mipro-medium + bold","dividerPhysicalPx":1,"visualBoundsPx":[265,335,815,610],"reflectionBoundsPx":[251,610,829,830],"summaryBoundsPx":[33,797,1047,1319]} |
| MetricCard | {"columnsPhone":2,"gapDp":10,"iconDp":26,"titleSp":17,"titleStyle":"FontMedium","titleTopMarginDp":6,"valueSp":18,"descriptionSp":13,"emptyExplanationSp":14,"leftVisibleBoundsPx":[33,1620,540,2150],"rightVisibleBoundsPx":[568,1620,1047,2150],"heightStatus":"visible portion only; clipped by fixed navigation; do not use 530px as full card height"} |
| ProfileHeader | {"outerPageInsetDp":12,"innerHorizontalDp":13,"topMarginDp":10,"avatarDp":50,"avatarRadiusDp":25,"textStartGapDp":12,"textVerticalPaddingDp":2,"nameSizeLogical":20,"nameWeight":500,"descriptionSizeLogical":12,"descriptionWeight":400,"lines":1,"ellipsis":"tail","descriptionSeparator":"  /  ","nameBoundsPx":[239,306,404,380],"descriptionBoundsPx":[239,380,404,424]} |
| ShortcutGrid | {"baselineCount":5,"baselineOrder":["activity","course","friend","prize","order"],"outerPageInsetDp":12,"additionalRowInsetDp":0,"topMarginDp":8,"topPaddingDp":18,"bottomMarginDp":5,"iconDp":24,"iconBottomMarginDp":6,"labelSizeLogical":14,"labelWeight":400,"labelTopPaddingDp":2,"labelBottomPaddingDp":10,"labelLines":1,"distribution":"flex1 centered each","entryBoundsPx":[[33,505,236,673],[236,505,439,673],[439,505,641,673],[641,505,844,673],[844,505,1047,673]]} |
| SurfaceCard | {"nativeCommonRadiusDp":16,"rnMenuRadiusDp":18,"daySurface":"#FFFFFF","nightSurface":"#191919","rnMenuTopMarginDp":12,"rnMenuVerticalPaddingDp":4,"sourceDrawableStates":"pressed/disabled/default shape fill same in native drawable; host effects unknown","shadow":"no global shadow token inferred"} |
| ChevronRow | {"native":{"titleSp":16,"titleStyle":"mipro-regular + bold","descriptionSp":12,"descriptionStyle":"mipro-regular","rightValueSp":14,"titleMaxLines":2,"descriptionMaxLines":4,"rightValueMaxLines":2,"rightValueMaxWidthDp":96,"verticalTextMarginDp":13.329987,"optionalLeadingIconDp":30,"iconToTextGapDp":16},"rnMenu":{"leadingIconDp":26,"titleSizeLogical":16,"titleWeight":"bold=true resolves to 700, despite explicit500","paddingStartDp":14,"paddingEndDp":10,"titleHorizontalPaddingDp":12,"rowVerticalMarginDp":16,"arrowDp":28,"throttleMs":300,"throttleLeading":true,"throttleTrailing":false},"settingsSingleBoundsPx":[[72,493,1008,640],[72,751,1008,898]],"settingsTwoLineBoundsPx":[[72,953,1008,1141],[72,1329,1008,1517]],"heightRule":"wrap_content; these measured states are not universal fixed heights"} |
| SwitchRow | {"titleSp":16,"titleStyle":"mipro-regular + bold","descriptionSp":12,"switchWidthDp":45,"switchHeightDp":25,"switchBoundsPx":[884,1200,1008,1269],"rowBoundsPx":[72,1141,1008,1329],"onDay":"#FF612E","onNight":"#F04D18","semanticState":"on via visual + content-desc 当前已开启; checked=false is unreliable on this custom widget","offDescription":"你可能错过重要消息通知，点击开关允许应用通知","onDescription":"关闭通知权限，可能会错过重要通知哦","offVisual":"see current original PNG, color sampling in comparison"} |
| PrimaryButton | {"styleChain":["Button.Common.Large.Positive","Button.Common.Primary","FontMedium.17sp"],"fontSp":17,"fontFamily":"mipro-regular","textStyle":"bold","textColor":"#FFFFFF","minWidthDp":336,"hostHeightDp":46.659973,"measuredBoundsPx":[78,2132,1002,2260],"measuredExtentDp":[336,46.545],"bottomMarginDp":27,"dayBackground":"#FF612E","nightBackground":"#F04D18","shape":"MIUI AdaptRoundButtonDrawable, smooth=true; normal radius16/capsule36 are parameters, exact mode/curve unknown","otherButtonWarning":"CommonBtn is another style with 15sp; do not use its font size on this baseline instance"} |

每个组件的states、inputs、events、unknowns与目标决策在JSON中，不能将D接口当作原App API。长文/窄屏先重排，sp随字号，图标保持独立命中区。按钮/开关忙碌时保留视觉反馈，不因为防重而吞掉取消/重试。

## 动画与用户逻辑

来源精确duration/easing缺失时保留null；同时给出可执行D方案，而不是要求消费AI猜参数。D方案实现后须录制目标结果，对照原版代表帧/状态。事务状态与动画状态分开：取消动画不自动取消业务，旧请求结果不得覆盖新输入。

- **登录/前置条件sheet进入**：240ms，cubic-bezier(0.2,0,0,1)；Target-only fallback; outside/Back returns focus, queue policy belongs to target.
- **卡片按下/释放**：100ms，linear；Do not confuse native/RN300ms click throttle with animation duration; scrolling cancels press.
- **开关状态提交**：160ms，cubic-bezier(0.2,0,0,1)；Controlled switch commits only confirmed state; pending/error separate, failure restores prior state.

## 保留边界

- 精确OEM字体、平滑圆角和完整动效仍有差异；target-motion明确为D选择。
- 部分历史PNG未记录快门时间；旧A身份在原记录保留，新索引不伪造时间。
- 非零图表、完整编辑表单和所有尺寸/读屏状态未覆盖，目标需按实际用途适配。

原详细规格继续可查：[历史研究规格](analysis/ui/baseline/spec.md)。本次新增标准适配不改写原图或把旧采集标为新运行；源coverage与目标实现完成程度分别报告。
