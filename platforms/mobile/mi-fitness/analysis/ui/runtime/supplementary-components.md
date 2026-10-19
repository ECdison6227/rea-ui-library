以下补充来自实际打开的运动记录页及直接引用的 XML，供跨业务的记录/报表页实现参考。其规则不应替代 RN 顶部 Tab 或主导航规范。

源布局：`analysis/phase2/apktool/res/layout/trail_activity_record.xml`，已在 portable source snapshot 中保存。原界面见 19-records-empty-night 与 20-record-filter-night。

| 区域 | 明确声明 |
| --- | --- |
| 工具栏 | 左返回、中间运动类型筛选、右更多；横向 padding 为 size_26_67 |
| 返回图标 | dimen_round_33.33dp |
| 类型标题 | FontMedium.16sp；末尾箭头，drawablePadding 4dp |
| 更多图标 | 24dp |
| 时间范围外轨 | 高37dp，左右外距23dp，与工具栏距7dp |
| 轨内 TabLayout | 外轨内缩3dp，fill/fixed，indicator stretch，无 ripple |
| 时间范围文字 | 周/月/年声明 FontRegular.14sp；“总”单项的最终字体仍应随Tab绑定代码核对 |
| 正文 | 外轨下方 FrameLayout，填充剩余空间 |

可迁移到订单、账单、任务、习惯等记录页：把类型筛选、时间粒度和时间轴当成独立状态维度，空列表展示明确的无数据状态。原 App 本次只有“所有运动”类型可选，不能据空态截图推断完整类型清单或有数据时的图表结构。
