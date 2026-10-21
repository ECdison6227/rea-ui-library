# 原版与参考对照

以下对照只覆盖本次指定的Muse公开首页。源图是A，参考和度量报告是D。所有画布使用Chromium155.0.8059.40、浅色、英文、DPR1；没有登录。

## 实际像素度量

REA逐像素比较以每个颜色通道差值大于8作为变化阈值。比例是变化像素数，不是整体设计、业务或感知相似度评分。

| 配对 | CSS画布 | 变化像素 / 总像素 | 变化占比 | 证据 |
| --- | --- | --- | --- | --- |
| 桌面首屏 | 1440×1000 | 240 / 1440000 | 0.016667% | [原始度量](rea-desktop-pixels.json) |
| 手机首屏（稳定新上下文） | 390×844 | 32 / 329160 | 0.009722% | [原始度量](rea-mobile-pixels.json) |
| 代表图文区，scrollTop2100 | 1440×1000 | 208 / 1440000 | 0.014444% | [原始度量](rea-feature-pixels.json) |

保留的原始输出含真实REA Evidence ID、两张PNG的SHA-256及工具限制。没有把无变化阈值内的像素称为完全相同。

## 几何核对

比较Hero标题、输入与Continue的x/y/width/height。数据来自各自浏览器DOMRect，不以图片猜尺寸。

| 画布 | 三个组件的最大绝对偏差 | 记录 |
| --- | --- | --- |
| desktop | 0 CSS px | [矩形对照](geometry-desktop.json) |
| mobile | 0 CSS px | [矩形对照](geometry-mobile.json) |
| tablet | 0 CSS px | [矩形对照](geometry-tablet.json) |

## 查看配对

- 桌面：[原图](../evidence/raw/rea-desktop.png) / [参考](reference-desktop.png)
- 手机：[稳定加载原图](settled-probe-source-mobile.png) / [参考](reference-mobile.png)
- 图文：[原图](../evidence/raw/scroll-desktop-scroll-2100.png) / [参考](reference-scroll-2100.png)
- 中等宽度：[原图](../evidence/raw/scroll-tablet-home.png) / [参考](reference-tablet.png)
- 焦点：[原图](../evidence/raw/desktop-input-focus.png) / [参考填字焦点](reference-input-focus.png)，文字与头像帧不同，只比较状态和边界。
- 轮播：[原页悬停](../evidence/raw/details-avatar-hover.png) / [参考轮播](reference-headline-motion.png)，视口和时刻不同，不做像素同一性结论。

源部署版本和采集时间见[证据索引](../evidence/index.json)。手机首次resize样本与新建上下文有文字/加载状态差异，原件仍保存在evidence/raw/scroll-mobile-home.png；最终配对使用document.fonts.ready且Continue已启用的明确条件。启动禁用按钮也作为独立A状态保留。

## 明确差异

- 参考页的Continue是本地页内导航，输入不发送；源认证流程按用户要求没有运行。
- 介绍区使用实际截取的视频帧；完整片段未打包。导览保留原封面，动作链接到原公开页，不模拟未研究的播放器。
- 字体、头像短片、插画与图标path直接复用。剩余少量非零像素没有被解释成整个产品还原度。
- 原页的滚动条覆盖层未复制；参考隐藏原生滚动条。Header淡入/提示隐藏阈值和RAF媒体平滑是明确D实现。
- 动效使用源声明时间与曲线，驱动方式由GSAP转为Web Animations/RAF；没有宣称全部内部调度一致。
- 未验证登录后UI、真实触控/读屏器、其他浏览器、DPR>1和其他语言。

## 浏览器检查与迁移切片

[QA原始记录](qa-results.json)包含 19 项检查、实现文件SHA-256和每次截图时间区间。当前通过 19 项，包括三档几何、资源解码、焦点、草稿保留、减少动画、轮播、无自动外部请求，以及阅读收藏的真实筛选与空结果。

[阅读首屏](transfer-reading.png)、[筛选操作](transfer-reading-filtered.png)、[空结果操作](transfer-reading-empty.png)属于D迁移证据；运行入口为[同组件示例](http://127.0.0.1:4173/reference/?example=reading)。筛选动作的实际结果还由QA的DOM和状态断言核对，不把示例测试当原版业务测试。

每次验证原始图保存在[runs](runs/)，当前同名PNG只是最近一次结果的便捷副本。重新验证使用npm run qa；修改实现后需要重新核对适用范围。完整性校验见[validation/integrity.json](../validation/integrity.json)。
