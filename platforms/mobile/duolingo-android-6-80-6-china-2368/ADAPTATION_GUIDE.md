# 将多邻国风格用于已有软件

先读取design-package.json和DESIGN_SPEC.md，再审查目标项目。复用面/底沿/描边、文字层级、选择联动主动作、解释→选择→继续、错误确认与重试。保留目标品牌、数据、导航和业务。

1. 从tokens/tokens.json的core角色开始，Android dp/sp不直接当CSS px。
2. 按components/catalog.json映射组件，再按patterns/layouts.json组织页面。
3. 按patterns/interactions.json处理默认/选中/禁用/忙碌/错误、Back、保存取消；业务生命周期由目标决定。
4. 读取motion/catalog.json，来源时序未知时采用明确D曲线/时长，包含退出、中断和减少动画。
5. 运行真实目标页面，核对字体、布局、键盘、长文、大字、焦点和动效；做前后与来源代表状态对照。

[原生参考](reference/README.md) · [来源对照](comparison/source-reference.md) · [本轮运行比较](comparison/native-revision.html) · [分发边界](docs/DISTRIBUTION.md)。
