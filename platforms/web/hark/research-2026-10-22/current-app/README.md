# Hark 当前登录版 App：资源归档与实施绑定

采集延续2026-10-22研究，具体时间见各份UTC收据。原版入口为 `https://hark.com/chat`。本子任务只读静态发布资源，没有读取账号内容、本机浏览器 profile 或业务 API。原始 main 由协调者提供，依赖首先通过无 cookie HTTP GET取得；56项被CDN拒绝后，由唯一浏览器操作者通过原页面正常 `fetch(credentials:'omit')` 和固定ID本地POST补齐。公开 wallpaper HTML的403及后续200收据也均保留。

## 覆盖与预算

| 内容 | 本轮保存 |
| --- | ---: |
| JavaScript | 189文件 |
| CSS | 46文件，含实际观察的全部10个 loaded CSS |
| 原字体 WOFF2 | 21文件 / 1,029,736 bytes |
| 主 vite 依赖表 | 162/162完整 URL 已保存 |
| main及递归依赖预算 | 200/200文件，19,950,422 bytes，0失败 |
| 扩大预算后补齐的依赖 | 56文件 / 5,857,490 bytes |
| 追加公开 wallpaper HTML | 1文件 / 277,309 bytes |
| 合计原始资源 | 257文件 / 26,085,221 bytes |

初轮200文件后出现的56个依赖现已全部补齐。总预算扩大为600静态文件/80MB后，实际在257文件/26.09MB提前闭合：256/256已发现的允许assets/fonts静态URL均已保存，另有1份公开wallpaper HTML，未保留依赖为0。直连56次及显式用户HTTPS代理43次403收据原样保留；没有绕认证。随后56份原件正常从页面fetch后导出，临时接收器已关闭。

JS依赖来自Acorn完整import及vite数组字面量；URL严格限制origin/path/扩展，没有正则误取后缀子串或跟随重定向。字体只取完整font-face URL token。新增56模块单独解析后没有新未保留依赖、SVG根构造或真实shader literal候选，不重复分析原有200文件。

补齐项包括29个locale与pseudo locale、代码高亮语言/主题/引擎、native host桥、音频/边界细化支持及1个card懒加载组件。原本main162表中的summary/projects/files/settings组件始终齐备。完整字节闭包仅适用于已发现的**允许literal静态依赖**，不等同于计算型URL、运行可达性或业务后端完整性。

## 字体

`font-inventory.json` 保存33份完整 font-face 声明及21份去重字体的 URL、SHA、字节数、CSS绑定、metadata与 coverage。所有字体通过 WOFF2 签名及完整长度核验，并以既有 fontkit 2.0.4 重新惰性解析。

15个原 App 字体与 public19 字节完全一致；新增6份为 GT L Light / Light Oblique / Bold / Bold Oblique、GT Mono Medium、Ivar Display Regular。字体可直接复用，不需近似替换。新采集补齐的是**当前 URL 返回的字节**；旧403收据仍为其原时点事实，不能据此声称恢复了2026-10-15当时的字体字节。

主 App 家族使用有空格的 `GT Standard L`、`GT Standard Mono`、`Hark Serif`：L300/400/500/600/700及 italic，Mono400/500，Serif CSS300/500。Serif Light payload OS/2为200，仍需保留原 CSS300；Medium为500。另一组小写家族别名有不同映射，例如 GT M Medium→CSS700；不能与主 App 家族混用。精确别名和声明来源范围已记录。

原字节中 GT sans 覆盖728个有效 codepoint、Mono Regular491、Hark Serif70（raw71含 U+FFFF .notdef）。中文等回退需目标平台明确提供。当前真实文字角色以协调者的计算样式证据为准；文件存在和声明本身不证明每处文字实际用了它。

## 图标与主题

`icons.json` 保留74个命名字形、160个 SVG 根构造、157个精确源码 owner。74个命名字形 decoded body 均与 H15 相同；每项包含当前 source SHA、UTF16/UTF8范围、原字面量、decoded body及独立 SVG。`named-glyph-sprite.svg` 是从原几何生成的派生 sprite，容器包装不冒充原厂文件。

main 中 `gy` 注册表由 `fy` 渲染：默认16px、viewBox `0 0 16 16`、fill none、data-glyph=name；继承 currentColor，primary/secondary/custom paint通过主题映射。保留 size/color动态 props与 aria-hidden。缓存/分支型 SVG 未被擅自摊平，完整 owner在 JSON 中。Vendor来源也保留文件作用域，160份源码不等于160个当前可见控件。

`theme-tokens.json` 提取254个带自定义属性的规则、544个唯一变量，每条保留选择器、条件、原片段、SHA和UTF16/UTF8范围。`resources/C87UuXAZ.css` 的 `:root` 与 `.theme-light` 各有66个核心变量；默认深色由 root 声明，浅色由 theme-light覆盖，不可将不同scope压平。

| 表面角色 | :root | .theme-light |
| --- | --- | --- |
| text-primary | rgb(245,245,247) | rgb(29,29,31) |
| bubble-agent-bg | rgba(29,29,31,.7) | rgba(255,255,255,.98) |
| composer-bg | rgba(29,29,31,.618) | rgba(255,255,255,.55) |
| regular-bg | rgba(29,29,31,.618) | rgba(255,255,255,.668) |

## 天空原件与 H15 比较

`resources/wallpaper_core.html` SHA为 `e39dc6405a15d9da720ed258842f320e3e88f8ea7866581146aa65c3f8775af0`。从首个 script 惰性提取完整引擎、GLSL与 LUT；第二段 CDN 防护脚本未执行，也未访问其 URL。

| 资产 | 精确 decoded bytes | 与 H15 原 HTML对应字符串 |
| --- | ---: | --- |
| vertex.glsl | 71 | 同字节 |
| fragment.glsl | 33,953 | 同字节 |
| owner-lut.png | 71,459，1089×33 RGB | 同字节 |

旧单独 GLSL 文件比 HTML原字符串多一个末尾换行；本轮保留原字符串，不补换行。LUT为33³、1089×33 slice strip，已直接解码原 PNG并查看像素。`sky-static.json` 保存 shader uniform声明、LUT SHA、当前 loader绑定及59个 CPU函数/常量片段。

**CPU引擎确有变化**，见 `sky/engine-vs-h15.diff`：新增 fadeHeld最多3层、rebaseFade()保留溶解中的中间画面、setHour(h,fade)允许一次跳转选择 crossfade、tick用合成后的 fadeBase产生 UI颜色。FADE_DUR=2.5s、FADE_EPS=.02、FRAME_MIN_MS=31、LUT_AMT_DEFAULT=1。shader/LUT相同不代表整套引擎未变。

真实静态宿主链：main导入 `BbFz-0Fv.js` 的 E→C组件→`CIfk9j87.js` 的 Tp sky host→默认 `/wallpaper_core.html`。C组件在特定条件满足时可按 I={light:13,dark:23}调用 setHour；是否运行受当前选择与状态控制。CSS表面主题和天空模拟时间是不同控制，不能由 html类名推断天空已变夜。

迁移时可直接使用原 GLSL、LUT与 CPU场景函数，以明确 D宿主适配冻结 time/weather/location/scene输入。原引擎含天气和网络路径；本审计未执行这些 fetch。

## Summary：静态实体与动效宿主

`summary-static.json` 保存24段精确源码和26条 CSS规则。`pX`读取 summary状态，以 `YC` modal宿主打开 `bX` reader；web使用连续表面，phone使用 div。reader Markdown明确传 `animated:false`。

Summary未覆盖 YC的 entrance，因此走默认 fade。main的 mg从 `DfbDlILr.js` export U导入，即 `Dr={...Rr,transition:{duration:.2,ease:"easeOut"}}`。Rr声明 opacity0 / blur4px→opacity1 / blur0px，完成后 filter none；退出回到 opacity0 / blur4px。**参数作用于外层 `._2za4zx8`，内层 `._1v8i1ba4`仍保持静态几何。**

协调者已报告：900ms采样中内层从78ms起一直720×504、opacity1、transform none。这不覆盖外层 opacity/filter，不能推出没有动画。上述200ms是B级声明，实际执行、减少动画及中断仍需原版宿主采样；这里不据此宣称 view-transition或伪元素动画。

## 接手入口与剩余边界

- `manifest.json`：归档和派生文件 SHA清单；`validation.json`：字节、来源范围、SVG及 shader/LUT校验。
- `acquisition.json`：全部取得过程、依赖边与当前零缺件闭包；`dependency-browser-ingest.json`：仅新增56模块的解析结果；`browser-fetch-provenance.json`与`browser-transfer-receipt.json`：正常fetch/原字节转存链。
- 56项都有接收器完整原字节收据；48项另有工具直接返回的源200/接收201记录，8项所在批次工具返回超时，源status不单独冒充已返回。完整原字节和解析结果均已核实，没有重复请求超时批次。
- `font-inventory.json`、`icons.json`、`theme-tokens.json`、`sky-static.json`、`summary-static.json`：精确实施绑定。
- 核心CSS、字体、命名字形、主题、天空着色及CPU控制已可实施，已发现的允许静态依赖字节缺件为0；这不替代当前交互、无障碍、GPU或全部懒加载运行验证。
- 原有 public/historical及历史证据未改动。资源留存不建立额外再分发许可。
