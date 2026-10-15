# 参考实现的独立适配

`android-reference` 是 D 独立原生 View 工程。`StyleKit` 负责组件，`Tokens` 负责本基线语义颜色，`MainActivity` 负责组合、事件与fixture。它不是原作者源码。

| D适配 | 依据与边界 | 迁移要求 |
|---|---|---|
| 原生View重建YRN我的 | A页面与生效bundle B；未使用RN引擎 | 目标已有RN时优先保留原栈与RN token |
| 顶部14.54545dp修正 | 原版内容始于y96，本参考AOSP decor始于y136，抵消40物理px | 是该模拟器适配值。换设备用WindowInsets重算，禁止当通用padding |
| 中文sans-serif | mipro系统族未在模拟器注册；姓名400校准该环境字形 | B仍为20/500；D映射400不能反写来源。OEM逐字字体unknown |
| 普通roundRect | 健康摘要15dp资源，RN分区18dp | 与MIUI smooth/adaptive曲线不等价，需看实际对照 |
| 健康两列内侧padding | A首卡[33..540)、次卡[568..1047)，D保留该手机样本 | 不推广到RN/资料页；横屏/折叠只有B，运行未知 |
| 健康彩虹零态 | B宽/7.2和宽/7.2/6.5几何，D静态绘制 | 非零数据、动画/中断未重建；时间声明B不冒充A测量 |
| 前置条件sheet | A快捷/周报询问登录可取消，D原生Dialog近似形状 | 确认发账号边界事件。认证SDK不在三页范围，无真实认证 |
| 详情事件 | A guest可进入彩虹/睡眠详情 | 发open-detail并展示明确范围边界；接目标真实路由 |
| 摘要指标校验 | B `DataHeaderV2View` 按type执行needCheckUserInfo后goDataPage，不是统一登录 | 参考只发详情边界；实际用户信息校验与活动次数详情交由目标适配，不伪造统一guard |
| 通知开关 | A稳定on/off同时换文案；早期认证为混杂 | D受控requestChange→owner决定→commit；不修改系统通知权限 |
| 页面保留 | A普通返回/重选保留滚动 | D缓存根页View；进程回收/重启完整恢复未承诺 |
| 滚动触感 | 原版有MIUI SpringBack/原生与RN各自滚动路径 | D用Android ScrollView，关闭原生overscroll；未还原MIUI回弹/阻尼，不把可滚动称手感一致 |
| 资料管理 | D会话数据、700ms、失败/重试、generation忽略晚到结果 | 不代表健康后端。接目标已有仓库、鉴权与取消策略 |

最终运行与APK身份见 `tests/`、`reference-accepted/` 和对照最终版；`reference/`、`reference-final/` 早期图片仅为迭代历史，不与新APK混为同一版本。选择记录见`selection.json`。

构建最低API26，API28字体weight重载与API29系统栏contrast调用已有版本保护；实际运行只验证API35。API26/27字体回退属于D兼容路径，尚未在这些版本运行，不宣称已经验证低版本显示。
