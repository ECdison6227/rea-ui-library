# Hark 续查：本地字体、图标与媒体资产审计

审计时间：2026-10-22T15:03:18.191448+00:00。仅检查既有文件及其明确来源链；未联网、未控制浏览器、未运行目标代码。路径均以 `platforms/web/hark` 为基准。

## 已核实的资源

主 `assets-manifest.json` 有 215 条记录，其中 101 条带包内路径，涉及 88 个唯一文件，hash/bytes 全部吻合。历史 `historical-app-assets.json` 的 432 个派生文件也全部吻合；其中 344 个 verbatim 文件逐字节匹配原始来源范围。

当前 public 字体 19/19 均完整落盘，总计 965,256 bytes。逐项与下载收据、payload 检查记录比对 SHA/字节数，并检查 WOFF2 header length 全部通过。字体表元数据复用此前 fontkit 2.0.4 解析，绑定的是本次已再次核实的精确字节；本次没有新增字体解码器或安装字体。

| 字体 | CSS weight/style | payload OS/2 weight | bytes | 有效 codepoints |
| --- | --- | --- | ---: | ---: |
| `gt-standard-s-standard-regular.1v94rzj-843fw.woff2` | 400 / normal | 400 | 63700 | 728 |
| `gt-standard-s-standard-medium.1z-favcayrd64.woff2` | 500 / normal | 500 | 63676 | 728 |
| `gt-standard-mono-standard-regular.2iqqf_4_8fw_b.woff2` | 400 / normal | 400 | 32976 | 491 |
| `hark-serif-light.3qkalivet-2sm.woff2` | 400 / normal | 200 | 6320 | 70 |
| `hark-serif-light-italic.0fhdxjby66gtz.woff2` | 400 / italic | 200 | 7392 | 70 |
| `hark-serif-medium.0scjs-gvhp6fa.woff2` | 700 / normal | 500 | 6312 | 70 |
| `hark-serif-medium-italic.1bqquesg4op7h.woff2` | 700 / italic | 500 | 7420 | 70 |
| `gt-standard-m-standard-regular.3ix3tcgbm6t_u.woff2` | 400 / normal | 400 | 63600 | 728 |
| `gt-standard-m-standard-regular-oblique.1xth6p8rdfsxv.woff2` | 400 / italic | 400 | 66400 | 728 |
| `gt-standard-m-standard-medium.2ekeevu0e95ee.woff2` | 500 / normal | 500 | 64484 | 728 |
| `gt-standard-m-standard-medium-oblique.30h-c7l8age-f.woff2` | 500 / italic | 500 | 66816 | 728 |
| `gt-standard-m-standard-semibold.11vxm560ke5j4.woff2` | 700 / normal | 600 | 64452 | 728 |
| `gt-standard-m-standard-semibold-oblique.2azw358mnlp_0.woff2` | 700 / italic | 600 | 66628 | 728 |
| `gt-standard-l-standard-regular.09tdz1i3kmgj1.woff2` | 400 / normal | 400 | 62732 | 728 |
| `gt-standard-l-standard-regular-oblique.29wq21_vse72v.woff2` | 400 / italic | 400 | 64968 | 728 |
| `gt-standard-l-standard-medium.1788_3rwow92_.woff2` | 500 / normal | 500 | 62772 | 728 |
| `gt-standard-l-standard-medium-oblique.22w6f_6egokz2.woff2` | 500 / italic | 500 | 65480 | 728 |
| `gt-standard-l-standard-semibold.3dc3_751vyn1r.woff2` | 700 / normal | 600 | 63260 | 728 |
| `gt-standard-l-standard-semibold-oblique.0hhyumsaypw9d.woff2` | 700 / italic | 600 | 65868 | 728 |

字体应从 `research-2026-10-22/public/resources/declared-fonts-fetched-manifest.json` 取得 URL、SHA 与 local path，再沿 `public/declared-fonts.json` 的 CSS 绑定接入；不能按字体文件名或 payload weight 反推网页 CSS weight。L/M Semibold 被网页映射 CSS700，payload600；Hark Serif Light 为 CSS400/payload200，Medium 为 CSS700/payload500。

Hark Serif 的 raw characterSet=71 包含 U+FFFF→.notdef，实际有效覆盖仅 70；直引号 U+0027、弯引号 U+2019 等均不在字节中。GT sans 有效覆盖728、Mono491；这些字节没有汉字覆盖。既有 `public/font-bindings/home-runtime-fonts.json` 的节点34、36分别在 “Rest assured it's…” 与 “Thanks, it’s custom” 返回 Songti SC 1 glyph。API不返回逐字符归属；将其具体归因于两个 apostrophe 是 C 推断。该 sidecar 是既有 A，不是本次运行证据。

历史可复用图标集中在 `historical-app-assets.json`：74 个命名 `glyph-*.svg` 已核对来源 literal、body digest 与成品几何；124 个工厂保留完整代码和动态 props，17 个历史 DOM SVG（50 次出现）都有源切片及匹配 factory 几何，另有8个完整 data-URI 解码文件。105个 SVG 均可解析为 XML。74 个命名资源的完整名称、路径、SHA与来源范围在本审计 JSON 的 `icons.historicalNamedGlyphs.items`；复用它们无需重画。

17 个历史 DOM SVG 全部保留 HTML 导出形成的小写 `viewbox`。作为独立 SVG/XML 文件缩放时，应另外生成明确标为派生的 `viewBox` 正规化副本；不要改动证据原件。命名字形本身已有正确的 `viewBox`。

| 当前公开页图标 | Owner | Shape 数 |
| --- | --- | ---: |
| `hark-logo` | `1_4hko300imeg.js:Logo` | 1 |
| `play` | `15bfdjo3hjyv9.js:IconPlay` | 1 |
| `icon-error` | `15bfdjo3hjyv9.js:S` | 1 |
| `google` | `15bfdjo3hjyv9.js:Icon` | 4 |
| `icon-arrow-out` | `15bfdjo3hjyv9.js:sQ` | 1 |
| `checkmark` | `15bfdjo3hjyv9.js:oQ` | 1 |
| `app-download-dismiss` | `406ziev2ozark.js:anonymous` | 1 |
| `cookie` | `406ziev2ozark.js:o` | 5 |
| `google-play` | `406ziev2ozark.js:l` | 5 |
| `hark-wordmark` | `406ziev2ozark.js:t_` | 1 |
| `apple` | `42be7a_n4bgx7.js:IconApple` | 1 |

上表11个 SVG 的根构造与 owner sourceText 均再次逐字节核对通过。源形状/props/引用范围在 `public/static-analysis.json#svgDeclarations`；这些是 B 声明，不能自动写成所有当前控件已运行。

## 必须保留的缺口

1. 主资产清单未追上已完成采集：12个当前字体仍标 unavailable；完整原件已在资源目录。不要再以“当前字体只取到7份”做结论。主清单也未直接登记历史432派生资源，需让消费者明确发现 `historical-app-assets.json`。本次仅报告，未改主包。
2. H15 历史字体确实缺原件：33个声明含21个唯一远程URL；原采集0个字体字节。`historical-font-acquisition.json` 保存的21次补抓全为403。当前19份字节来自另一部署/URL，不是已证实的历史原件。
3. 当前媒体没有完整本地资产链：首页有9个唯一非空src，另48个媒体节点src为空；研究目录没有独立源图片或视频文件。截图不能当作原媒体；null src 不证明懒加载视频已抓全。主清单提到的外部 hark-clone 字体/图片/视频仍是 withheld，来源绑定未完成，本次没有读取那个目录。
4. 图标有明确作用域：74个命名源glyph仅5个有已捕获源码调用点，0个直接匹配本次清单历史DOM；17个DOM SVG均匹配工厂几何。不能把命名源注册表等同于已验证运行清单。20个旧设置懒加载screen、4个marker-only模块和3个动态data URI仍未恢复完整资产。
5. 当前19份字体可支持本研究所见拉丁文字，但跨中文与其他语种需要目标平台明确D回退方案；字体实际synthetic weight、逐字符fallback和跨系统字形仍有边界。

## 来源与校验入口

- [机器审计记录](asset-audit-resumed.json)：每项字体、命名图标、源范围、SHA、文件完整性、全部verbatim比对结果。
- `public/resources/declared-fonts-fetched-manifest.json` / `public/font-payload-inspection.json`：当前19字体的下载与元数据链。
- `public/font-bindings/home-runtime-fonts.json`：继承的Chrome平台字体报告及时间/视口边界。
- `historical-app-assets.json`：历史图标、工厂、静态几何、原字体声明与DOM来源。
- `historical-font-acquisition.json`：旧21字体URL的403收据。
- `public/home-inventory.json` / `public/resources-manifest.json`：实际观察URL与媒体加载边界。

没有改动历史证据、主 design package、资源原件或任何其他来源。本审计不新增原厂资源再分发许可。
