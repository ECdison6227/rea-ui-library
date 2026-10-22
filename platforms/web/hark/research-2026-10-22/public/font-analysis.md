# Hark 当前字体原件：19份载荷与绑定分析

本轮19/19字体原件均通过实际SHA、字节数、wOF2签名和本机既有FontKit 2.0.4解析检查，合计 **965,256 bytes**；此前7份字节全部保持一致。完整入口为 [font-payload-inspection.json](font-payload-inspection.json)，原件清单为 [declared-fonts-fetched-manifest.json](resources/declared-fonts-fetched-manifest.json)。

源deployment为 `dpl_43vpACitAoHemg5zATXXCP8hBYHa`。协调研究在真实公开页面上下文读取原件的时间为 **2026-10-22T13:03:13.228Z—13:03:18.029Z**；CSS声明另来自先前已注册样式，首页加载观察又是另一阶段。本worker只解析已有文件，未联网、操作GUI或安装字体。解析事实为 **B**，样本覆盖归纳为 **C**；[asset-gallery.html](asset-gallery.html) 是直接引用原件的 **D资产目录**，其画面不成为原Hark的A证据。

已稳定的 [static-analysis.md](static-analysis.md) 与 [static-analysis.json](static-analysis.json) 保持原SHA；该快照的7份获取条件由本独立附录补全为19份。实际页面fallback／字重合成／字体选择仍由原页面运行证据补充。

## CSS绑定、文件内部名称与重量

CSS family/weight/style取原@font-face，载荷名称与OS/2取实际字体表。二者必须分别记录：Hark Serif light CSS400对应OS/2 **200**、medium CSS700对应 **500**；GT Standard M/L semibold CSS700对应OS/2 **600**。不要因内部名称或usWeightClass重写源CSS绑定。

| CSS family | CSS weight/style | 文件 fullName | OS/2 weight | 原文件 |
| --- | --- | --- | --- | --- |
| gt-standard-s-standard | 400/normal | GT Standard S Regular | 400 | [gt-standard-s-standard-regular.1v94rzj-843fw.woff2](resources/gt-standard-s-standard-regular.1v94rzj-843fw.woff2) |
| gt-standard-s-standard | 500/normal | GT Standard S Medium | 500 | [gt-standard-s-standard-medium.1z-favcayrd64.woff2](resources/gt-standard-s-standard-medium.1z-favcayrd64.woff2) |
| gt-standard-mono-standard | 400/normal | GT Standard Mono Regular | 400 | [gt-standard-mono-standard-regular.2iqqf_4_8fw_b.woff2](resources/gt-standard-mono-standard-regular.2iqqf_4_8fw_b.woff2) |
| hark-serif | 400/normal | Hark Serif 1 Light | 200 | [hark-serif-light.3qkalivet-2sm.woff2](resources/hark-serif-light.3qkalivet-2sm.woff2) |
| hark-serif | 400/italic | Hark Serif 1 Light Italic | 200 | [hark-serif-light-italic.0fhdxjby66gtz.woff2](resources/hark-serif-light-italic.0fhdxjby66gtz.woff2) |
| hark-serif | 700/normal | Hark Serif 1 Medium | 500 | [hark-serif-medium.0scjs-gvhp6fa.woff2](resources/hark-serif-medium.0scjs-gvhp6fa.woff2) |
| hark-serif | 700/italic | Hark Serif 1 Medium Italic | 500 | [hark-serif-medium-italic.1bqquesg4op7h.woff2](resources/hark-serif-medium-italic.1bqquesg4op7h.woff2) |
| gt-standard-m-standard | 400/normal | GT Standard M Regular | 400 | [gt-standard-m-standard-regular.3ix3tcgbm6t_u.woff2](resources/gt-standard-m-standard-regular.3ix3tcgbm6t_u.woff2) |
| gt-standard-m-standard | 400/italic | GT Standard M Regular Oblique | 400 | [gt-standard-m-standard-regular-oblique.1xth6p8rdfsxv.woff2](resources/gt-standard-m-standard-regular-oblique.1xth6p8rdfsxv.woff2) |
| gt-standard-m-standard | 500/normal | GT Standard M Medium | 500 | [gt-standard-m-standard-medium.2ekeevu0e95ee.woff2](resources/gt-standard-m-standard-medium.2ekeevu0e95ee.woff2) |
| gt-standard-m-standard | 500/italic | GT Standard M Medium Oblique | 500 | [gt-standard-m-standard-medium-oblique.30h-c7l8age-f.woff2](resources/gt-standard-m-standard-medium-oblique.30h-c7l8age-f.woff2) |
| gt-standard-m-standard | 700/normal | GT Standard M Semibold | 600 | [gt-standard-m-standard-semibold.11vxm560ke5j4.woff2](resources/gt-standard-m-standard-semibold.11vxm560ke5j4.woff2) |
| gt-standard-m-standard | 700/italic | GT Standard M Semibold Oblique | 600 | [gt-standard-m-standard-semibold-oblique.2azw358mnlp_0.woff2](resources/gt-standard-m-standard-semibold-oblique.2azw358mnlp_0.woff2) |
| gt-standard-l-standard | 400/normal | GT Standard L Regular | 400 | [gt-standard-l-standard-regular.09tdz1i3kmgj1.woff2](resources/gt-standard-l-standard-regular.09tdz1i3kmgj1.woff2) |
| gt-standard-l-standard | 400/italic | GT Standard L Regular Oblique | 400 | [gt-standard-l-standard-regular-oblique.29wq21_vse72v.woff2](resources/gt-standard-l-standard-regular-oblique.29wq21_vse72v.woff2) |
| gt-standard-l-standard | 500/normal | GT Standard L Medium | 500 | [gt-standard-l-standard-medium.1788_3rwow92_.woff2](resources/gt-standard-l-standard-medium.1788_3rwow92_.woff2) |
| gt-standard-l-standard | 500/italic | GT Standard L Medium Oblique | 500 | [gt-standard-l-standard-medium-oblique.22w6f_6egokz2.woff2](resources/gt-standard-l-standard-medium-oblique.22w6f_6egokz2.woff2) |
| gt-standard-l-standard | 700/normal | GT Standard L Semibold | 600 | [gt-standard-l-standard-semibold.3dc3_751vyn1r.woff2](resources/gt-standard-l-standard-semibold.3dc3_751vyn1r.woff2) |
| gt-standard-l-standard | 700/italic | GT Standard L Semibold Oblique | 600 | [gt-standard-l-standard-semibold-oblique.0hhyumsaypw9d.woff2](resources/gt-standard-l-standard-semibold-oblique.0hhyumsaypw9d.woff2) |

每个postScriptName、familyName、fullName、preferredFamily／subfamily及原件SHA在JSON `items[].names`／`sha256`。GT的CSS italic变体内部名是Oblique，载荷italicAngle/fsSelection独立保留；没有通过名字推断浏览器实际选择。

## 字体设计单位与行框边界

| 文件族 | UPM | hhea ascent/descent/gap | OS/2 typo ascent/descent/gap | glyphs /有效codepoints |
| --- | --- | --- | --- | --- |
| GT S/M/L（14脸） | 1000 | 970/−190/0 | 970/−190/0 | 每脸964/728 |
| GT Mono | 1000 | 970/−190/0 | 970/−190/0 | 680/491 |
| Hark Serif normal（2脸） | 1000 | 1000/−200/250 | 750/−200/250 | 每脸85/70 |
| Hark Serif italic（2脸） | 1000 | 1000/−200/250 | 750/−200/250 | 每脸93/70 |

全部数值为未缩放字体设计单位。Hark Serif的hhea与OS/2 typoAscender存在250-unit差异，fsSelection.useTypoMetrics为true；FontKit reported ascent使用1000，但这不证明Chromium／系统最终行框选择哪套度量，也不等同CSS px或line-height。原界面baseline需要实际画面对照。19份均无fvar/variationAxes，属于所保存的静态脸。

JSON同时给原始cmap枚举和glyphId映射。Hark四脸raw枚举71、Mono raw492，其中U+FFFF映射glyph0/.notdef，故有效非.notdef分别为70、491；GT S/M/L是728有效项。前一快照的71是原始枚举长度，本附录进一步分开有效映射，避免把sentinel当可显示字符。

## 文字、特殊标点与符号覆盖

Hark Serif有效集合完整包含A–Z、a–z、0–9，普通space/comma/hyphen/period，以及U+00A0、U+0131、U+0237、U+0307。它缺少直引号U+0027/U+0022、弯引号U+2018/2019/201C/201D、colon/semicolon、感叹号、问号、&、/、@、en/em dash、ellipsis、bullet、©、货币等所检查标点。标题中的标点不是由这四份Serif cmap直接提供，具体fallback字体需A取证。

GT S/M/L的14脸有效codepoint集合彼此相同；英文及所列法语、德语、西语、葡语、意语、土语、波兰语代表字符均通过本报告明确样本的成员检查。越南语代表样本仅10/44；俄语、希腊语、阿拉伯语、希伯来语、Devanagari、CJK/Hiragana/Hangul代表样本均0。Mono拥有同类拉丁样本，但有效集合较小；不把这些样本结果称为整种语言完整支持。所有样本文字、要求codepoints、缺项逐一列在 `languageSamples`。

GT S/M/L还含 **59个BMP私用区映射**、本报告1F300–1FAFF范围的 **46个补充平面符号候选**及圈字等字形；它们的UI归属未知，不能因此叫作Hark当前图标集。所有字形仍在原字体载荷中，目录可逐codepoint查看。19文件只见CFF等轮廓表，未见COLR/CPAL/CBDT/CBLC/sbix/SVG色彩图形表；这不证明实际网页emoji使用哪份字体。Mono检查的十个数字cmap advance均600，post.isFixedPitch却为0，保留这两个事实，不泛化为所有字形或已启用numeral特征。

## GSUB／GPOS声明

| 文件族 | GSUB feature tags | GPOS feature tags |
| --- | --- | --- |
| GT S/M/L | aalt, case, ccmp, dnom, frac, liga, locl, numr, ordn, pnum, sinf, ss01–ss07, subs, sups, tnum, zero | kern, mark, mkmk |
| GT Mono | aalt, case, ccmp, dnom, frac, liga, locl, numr, ordn, sinf, ss01–ss07, subs, sups, zero | mark, mkmk |
| Hark Serif normal | aalt, ccmp, ss01–ss06 | kern, mark, mkmk |
| Hark Serif italic | aalt, ccmp, ss01–ss08 | kern, mark, mkmk |

完整 `openType.GSUB/GPOS` 保存每个featureRecord、lookup索引、script/defaultLangSys/languageRecord、required feature和lookup类型计数；`fontFeatureNames`保存name表标签。GT有DFLT/latn及10个语言记录，Hark只有latn/默认LangSys。标签存在不证明当前网页启用其效果。对已解析829条当前CSS规则的定点检查未找到font-feature-settings/font-variation-settings/font-kerning/font-variant等显式声明，浏览器默认与继承状态仍由A解释。

FontKit的lookup数组为lazy对象，报告只序列化可确认的标量／索引关系，不把整张表当成已经验证的任意复杂shaping行为。所有原件解析成功；完整表行为与实际渲染不是这次静态解析的验收范围。

## 资源目录与后续原版取证

[asset-gallery.html](asset-gallery.html) 直接引用19个本地WOFF2和11个源代码中可转写的SVG默认字面量结构，提供字体选择、字号、OpenType标签和codepoint查看。所有SVG路径、颜色、group transform和顺序来自已保存源码；目录调用提供空外部props，这个D调用不等于原版实例props。不能转成字面量SVG的表达只保留代码链接。

优先补原页面：Serif标题中的U+0027/U+2019和©/—等标点；GT/Serif混排的实际平台字体与glyph count；CSS700映射600/500文件时的真实字体选择／是否合成；hhea/OS2差异下的baseline；手机与桌面条件。原版fallback由CSS.getPlatformFontsForNode等来源明确的A证据补充，目录的D渲染不会代替它。
