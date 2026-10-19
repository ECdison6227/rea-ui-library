# Hark 聊天界面：排版、背景与设计变量

本报告依据当前项目保存的浏览器 DOM、CDP 样式表、computed styles 与已发布 JavaScript。所有静态定位使用原始文件中从 0 开始的字符偏移，结束位置不包含在内；压缩文件通常只有一行，字符偏移比行号更精确。静态声明、实测值、解释性推断分别标记，不把已加载资源中的每个组件当作已执行功能。

## 结论与证据边界

- **实测**：普通助手正文与输入框是 `GT Standard L`，`15px / 19px`，字重 `400`。链接卡标题 `15px / 19.5px`、`500`；来源文本同字号、`400`。初次采样 `body` 是 `16px`，不能把组件的 `15px` 推广为所有页面文字。
- **实测 + 静态**：页面背景由 `#sky-root` 中的 WebGL `#gl` canvas 承载。聊天消息、卡片、输入框叠在天空背景上，以带透明度的填充、独立边框层、连续曲线圆角建立层级。主背景不是单个 CSS 渐变。
- **静态**：色彩主题有原始语义变量；尺寸变量的一部分经构建压缩为 `--mr6c8q*`，另一部分保留 `--card-w` 等原名。已导出 **544 个带原选择器的声明**，并保留作用域。
- **静态 + 实测**：`border-radius:24px` 常与动态 `clip-path:path(...)` 同时存在，实际外形由连续曲线裁切定义。仅复制标准圆角会遗漏细节。
- **未验证**：浅色主题的完整实测、所有字号预设的使用覆盖与每个天气/天空参数分支的运行覆盖。完整 wallpaper HTML 和 shader 已保存并静态核对。此报告不声称这些静态分支均已运行。

主要证据：[样式清单](../evidence/styles/manifest.json)、[初始 DOM](../evidence/dom/02-chat-initial.html)、[页面与字体清单](../evidence/dom/02-page-inventory.json)、[第一批计算样式](../evidence/styles/computed-styles.json)、[定向计算样式](../evidence/styles/targeted-computed-styles.json)、[初始截图](../evidence/screenshots/02-chat-initial.png)。样式清单记录 CSS 来源 URL、字节数与 SHA-256；脚本来源在 [脚本清单](../evidence/scripts/hark-assets/manifest.json)。

脚本资源导出的 REA Evidence ID `ev_f59cba60a4e30603b5f93e546735322e749383df1dc84d0c3d9c761afd214537`，捕获状态 `policy_filtered` / `attach_limited`。REA 附加前的请求不可见，来源无法归属的脚本和不在允许来源内的资源可能被排除；因此此处静态分析覆盖“已保存脚本”，不是整个站点的代码完整性证明。CDP CSS 与定向 computed styles 为另存原始证据，不将 REA 未保留的 DOM/网络内容当作已有。

## 文字排版

### 字体体系

**静态声明**：`B9bSKZIz.css` 中 `.mr6c8q0` 的字体栈为：

```css
--mr6c8q1c: "GT Standard L", "GT Standard L Fallback",
  -apple-system, BlinkMacSystemFont, sans-serif;
```

同文件提供 `GT Standard L` 的 300、400、500、600、700 五档、每档 normal/italic；`GT Standard Mono` 提供 400、500；`Hark Serif` 提供 300、500 和 italic。Fallback 用 Helvetica Neue/Arial，并调整 size/ascent/descent/line-gap，说明字体切换时行框稳定性受到照顾（目的为推断，参数为原声明）。默认字体声明见 [B9bSKZIz.css](../evidence/styles/B9bSKZIz.css)，字符 `9086` 起；完整 `@font-face` 在同文件。

**运行字体清单**：初始页面 `document.fonts` 记录 L 的 400 normal/italic、500 normal、600 normal 已加载；Mono 400、Hark Serif 500 已加载。这能说明浏览器字体资源状态，不能单凭 loaded 判断某一节点实际使用它。实测节点的字体族来自 computed styles。

**静态原始语义预设**：`DvrONiVz.js` 保留 `body1`、`h1`、`captions1` 等对象字段，构造函数计算 `lineHeight=round(1.3*size, 3)`。它们是代码中的命名预设，不等于聊天气泡最后应用的行高。详见 [design-tokens.json](design-tokens.json) 的 `staticJavascriptConstants.typography`，内含源文件和精确偏移。

| 原预设 | 字号 | 构造行高 | 原 face / 字重映射 |
|---|---:|---:|---|
| `h0` | 48 | 62.4 | medium / 500 |
| `h1` | 32 | 41.6 | regular / 400 |
| `h2` | 24 | 31.2 | medium / 500 |
| `h3` | 20 | 26 | medium / 500 |
| `body1` / `body1Medium` / `body1Bold` | 15 | 19.5 | regular / medium / semibold |
| `body2` / `body2Medium` / `body2Bold` | 14 | 18.2 | regular / medium / semibold |
| `captions1` / `captions1Medium` / `mono1` | 12 | 15.6 | regular / medium / mono |
| `harkSerifH2` / emphasized | 24 | 31.2 | serifMedium / serifLightItalic |
| `harkSerifBody1` / emphasized | 16 | 20.8 | serifMedium / serifLightItalic |

### 聊天气泡、输入框与卡片

[Bgegb_jl.css](../evidence/styles/Bgegb_jl.css) 字符 `66182` 起定义气泡，通用外观继承 [CTg21sq_.css](../evidence/styles/CTg21sq_.css) 字符 `2973` 的 `._13nvxnx0`。最终气泡自身把通用的 `font-weight:500; line-height:1.2` 覆盖为 `400;19px`。

```css
._151xtfx0 {
  max-width:min(85%,min(424px,calc(100vw - 120px)));
  font-weight:400; line-height:19px;
}
._151xtfx1 { min-height:48px; padding-top:14.5px; padding-bottom:14.5px; }
._151xtfx4 { --continuous-fill:var(--bubble-user-bg); align-self:flex-end; }
._151xtfx5 { --continuous-fill:var(--bubble-agent-bg); align-self:flex-start; }
```

普通气泡是 `15px`、`19px` 行高、左右 `16px`、上下 `14.5px` 内边距、最小高度 `48px`；默认最大宽度 `424px`，同时受视口与父容器百分比限制。定向采样实测 user/agent host 宽 `424px`、上述排版与 padding；所采 agent `max-width` 为 `100%`，说明另有实例级规则放宽了上限，最终宽度仍应按节点测量。助手与用户主要通过左右对齐、填充透明度区分。来源为静态声明，普通助手正文 `15/19/400` 已由第一批 computed samples 的 `_10dfkk10` 节点证实。

输入框见 [cTNWVXWS.css](../evidence/styles/cTNWVXWS.css) 字符 `88163`：`15px`（继承通用输入样式）、`19px`、`400`；左右 padding 分别 `16px/8px`，上下 `14.5px`；`max-height:219px`、`overflow-y:auto`。初次实测 textarea 高度 `48px`、样式与上述一致。textarea 自身透明、圆角 0；玻璃外观由其兄弟背景层承担。

链接卡正文区是 `15px / 1.3`，标题 `500`、最多两行，来源一行省略。**实测来源也是 15px**；虽然作用域声明了 `--card-label-size:13px`，所采样来源选择器并未使用该变量。出处：[Bgegb_jl.css](../evidence/styles/Bgegb_jl.css) 字符 `31464–32007` 与 computed samples `link-card-title` / `link-card-source`。

通用富内容卡的标题、副标题均 `15px`，默认 line-height normal，紧凑头部变体改成 `19px`，标题可变 `500`；头部 `70px`，变体 `51/72px`，左右 padding `16px`。代码卡正文采用 `GT Standard Mono`，`14px / 20px`，内边距 `0 16px 16px`，预览限高 `412px`、宽上限 `550px`，底部 mask 渐隐。源：[Bgegb_jl.css](../evidence/styles/Bgegb_jl.css)，字符 `110645–113745`。后续定向采样 `code-pre` 已证实 `GT Standard Mono / 14px / 20px / 400`、`0 16px 16px`。其 `code-fill` 测得 `rgba(29,29,31,.62)`、无 backdrop blur，宿主透明且 radius `24px`；浏览器序列化透明度会显示 `.62`，原变量是 `.618`。初次 label=`code` 的 `pre._1qc6wrt1` 是另一种引用/富文本 pre（`14/18.2`），不用于代码卡结论。

### Markdown 规则

原 CSS 中普通段落 `margin:0`，非最后一段 `margin-bottom:16px`。标题等级的默认字号是 `20/16/15/14/13/12px`，h4–h6 明确 `600`；引用条左边框 `2px`、缩进 `16px`；列表缩进 `24px`、项间距 `8px`。表格字号 `14px / 1.4`、单元格 padding `8px`，表头 `600`，外层水平滚动并含 `12px` 圆角。行内 code 为 `.8em`、`2px 4px`、`4px` 圆角、系统 monospace 字体栈；普通 pre 外层 `8px` 内边距、`12px` 圆角，与独立代码卡不同。出处：[Bgegb_jl.css](../evidence/styles/Bgegb_jl.css) 字符 `59504–61429`。

第一批 computed 中某些 h2 是 `14/18.2/600`，来自引用/嵌套容器的额外覆盖，因此以上标题表只是默认静态规则。复刻时应按组件作用域采样。

## 背景与材质

### 天空背景的实际层级

初始 DOM 含 `#sky-root`、`#gl`、`#skyfreeze`、`#skyph`、`#glfail`。`#gl` 在该 DOM 中是 `visibility:visible`，实际像素缓冲 `2520×1232`（不是 CSS 视口尺寸）；`#skyfreeze` 标记 `data-armed=true`。样式见 [inline-style-sheet-81409-4.css](../evidence/styles/inline-style-sheet-81409-4.css)：

```css
body:has(>#sky-root){background:transparent}
#sky-root{position:fixed;top:0;left:0;width:100%;height:100dvh;
  overflow:hidden;pointer-events:none;z-index:-1;isolation:isolate;contain:paint}
#gl,#skyfreeze{position:absolute;top:0;left:0;width:100%;height:100%;
  display:block;visibility:hidden}
#skyph{position:absolute;inset:0;background:var(--sky-placeholder,none);
  transition:opacity .8s ease}
```

页面 `html` 提供黑色底，body 透明，天空固定在内容后方，不截获 pointer；canvas 绘制成功后 JS 会将 gl 显示、placeholder 透明度置 0。第一批 computed 中 sky-root/canvas 是 `1440×812`、透明 CSS 背景，这与 canvas 自行绘制内容相容，不表示页面没有背景。

**静态引擎入口**：[DvrONiVz.js](../evidence/scripts/hark-assets/files/origins/6c67863ee7f43ffbb455e39ca855380e532f4f91833be0b294da3a4afa592701/assets/DvrONiVz.js) 字符 `1028` 起 `wallpaperUrl:"/wallpaper_core.html"`；[Dl79dO-7.js](../evidence/scripts/hark-assets/files/origins/6c67863ee7f43ffbb455e39ca855380e532f4f91833be0b294da3a4afa592701/assets/Dl79dO-7.js) 字符 `9548–13410` 的 `selfBoot()`：

1. 为 WebGL/WebGL2 强制 `preserveDrawingBuffer:true`，记录绘制序号。
2. 装配 freeze 2D canvas 与 resize handler。
3. fetch wallpaper HTML，提取 `<script>` 并执行，要求发布 `window.hark.applyScene`。
4. 注入代理 fetch，将 weather/air quality/geocode/IP locate/NASA skyglow 路径映射到同源 `/api/sky/*`。
5. 双 requestAnimationFrame 后显示 canvas，隐去 placeholder。
6. 保存宽 `48px` 的 WebP `.8` 缩略图至 `hark-sky-placeholder-v2`，在 pagehide/hidden 时更新。

### 完整 wallpaper 源码补证

后续同源浏览器 fetch 取得 HTTP 200 的 [wallpaper_core.html](../evidence/scripts/wallpaper_core.html)，**275,427 字符**，SHA-256 `6cdacc4cd85f288d8aef046746ca3814b9c0bf2217baa8fd3ab86346881e66e0`。已按实际语句核对 shader 与控制链；未把文件头注释中的介绍当成功能执行证据。源行号、字符范围、16 段相关原代码另存 [wallpaper-source-excerpts.json](../evidence/analysis/wallpaper-source-excerpts.json)。完整 GLSL 分别提取为 [vertex](../evidence/scripts/wallpaper-vertex.glsl) / [fragment](../evidence/scripts/wallpaper-fragment.glsl)，fragment 有 **47 个 uniform 声明**。

**静态渲染构成**：文件第 760–808 行创建 WebGL（`antialias:false,alpha:false,powerPreference:low-power`），编译/link VERT 和 FRAG，以 `[-1,-1,3,-1,-1,3]` 单个全屏三角形绘制。fragment 第 217–309 行使用 hash→quintic value-noise→四 octave FBM，再以时间/风向与两层 domain warp 扰动四段垂直色带 `uC3→uC2→uC1→uC0`。因此背景的柔和色雾有明确的 GPU 结构，不能仅用一张普通 linear-gradient 完整替代。

**颜色与细节**：`buildFrame()`（1575–1607 行）先根据本地小时算太阳几何、月亮高度/照度和 aerosol optical depth，再通过 `SKY2.stops(...)` 产生四段颜色；之后用天气、云量、能见度、辐射和 AQI 修正云层、雾霾等参数。fragment 中云层/雾、太阳/月亮 glow、cirrus、光晕/光束、降水、polarizer band、twilight purple 等均有独立 uniform 或条件分支。这些是可核实的代码分支，**并不表示此次截图启用了每一种现象**。

输出阶段（712–746 行）按顺序做 vignette → `33³` LUT（纹理条 `1089×33`）→ 夜间 blue floor → 静态像素 grain + dither。`LUT_AMT_DEFAULT=1` 且文件包含 baked LUT image，但实际纹理上传成功没有单独运行测量。夜间 floor 在 CPU 按太阳高度 −6°至 −12°逐渐启用（2059–2065 行），用 `col = nf + col*(1-nf)` 提亮；默认 `state.night.floor=.2`。这是深蓝暗部而非绝对黑的一个实际来源，不将该默认当作当前实例已测参数。

**尺寸、节奏与切换**：`resize()`（868–879 行）把 DPR 限制到 **1.75**；初始 1440×704、DPR 2 与 canvas 2520×1232 正好相符。细节单位通过 `scene.aspect=1`、`scene.crop=.15` 在宽/高之间计算，天空仍按整幅 uv 绘制。循环（2192–2233 行）使用 **31ms** 门限而非每次 RAF 都绘制，`dt` 上限 `.1s`；目标变化超过 `FADE_EPS=.02` 时两遍 draw 用 `SRC_ALPHA/ONE_MINUS_SRC_ALPHA` 做 **2.5s** crossfade。`setHour()` 主动清掉 fade baseline，因此时间 scrub 跟随调用直接切换；以上为静态逻辑，实际帧率与时长尚未计时验证。

**实际控制 API**（2687–2742 行）：`setLive`、`setHour`、`setDate`、`setTimeFlow(0…120)`、`setSim`、`setWind`、`setAqi`、`setCloud`、`setAod`、`setLutAmt(0…1)`、`setSkyglow`，另有 cirrus/rays/halo/purity/scene/cloudLit/afterglow/night/dogs/corona 的字段调整；`applyScene` 写入地点、时区、天气模拟和时间，随后 refresh。`setSim(null)` 清风、AQI、cloud、AOD overrides。shader 初始 `density=.9,flow=1.7,speed=2.5,grain=.018` 已原名保存到 token JSON；是源码默认，未额外探测当前状态。

**状态到 UI 主题**：每秒 `tick()`（2771–2842 行）经 CPU LUT mirror 与 floor 计算四段颜色、bottom-weighted luminance，并通过太阳 sunrise/dayLen 算 `day`，输出 `status`；外层 auto-theme 使用 `day`。不要把输出了 `lum` 推断成当前 Web 主题直接按 luminance 切换。boot（2848–2874 行）声明天气每 **10 分钟**刷新、页面可见后刷新、status 每秒更新；reduce-motion 将 speed 设 `.08`，保留较慢运动。这些频率是代码定时配置，网络请求完整性与每个 API 控件是否出现在聊天 UI 中仍未证明。

### 主题与天空日夜关联

同 `DvrONiVz.js` 字符 `1310–3920` 保留主题决策：URL/session embedder theme → 持久 themeOverride；支持 `light`、`dark`、`system`、`auto`。system 使用 `prefers-color-scheme`；auto 根据 sky-place.day 或保存的 skyAppearance 推导。实时且非 simulation 状态更新 skyAppearance；auto 同步文档 appearanceAttribute。`Dl79dO-7.js` 的显式 light/dark 分支把天空小时设为 **13/23**，暂停 live/timeFlow（静态分支，未实测切换）。

初始 DOM `data-theme="dark"`；根变量实测与默认 dark 值一致。浅色值是捕获 CSS 声明，未将其当作此次已渲染状态。

### 消息、卡片与输入框填充

[B9bSKZIz.css](../evidence/styles/B9bSKZIz.css) 根主题块（dark 字符 `0`，light 字符 `2312` 起）与实测根变量一致：

| 原变量 | dark 静态值（已测根变量） | light 静态值 | 解释（推断） |
|---|---|---|---|
| `--text-primary` | `rgb(245,245,247)` | `rgb(29,29,31)` | 主文字 |
| `--text-secondary` | `rgba(245,245,247,.5)` | `rgba(29,29,31,.5)` | 次级文字 |
| `--stroke-primary` | `rgba(245,245,247,.14)` | `rgba(29,29,31,.14)` | 主分隔/边框 |
| `--bubble-user-bg` | `rgba(29,29,31,.3)` | `rgba(255,255,255,.3)` | 用户气泡 |
| `--bubble-agent-bg` | `rgba(29,29,31,.7)` | `rgba(255,255,255,.98)` | 助手气泡 |
| `--composer-bg` | `rgba(29,29,31,.618)` | `rgba(255,255,255,.55)` | 输入玻璃层 |
| `--regular-bg` | `rgba(29,29,31,.618)` | `rgba(255,255,255,.668)` | 通用卡片材质 |
| `--regular-solid-bg` | `rgba(29,29,31,.96)` | `rgba(255,255,255,.96)` | 展开/飞起较不透明材质 |
| `--thin-bg` | `rgba(29,29,31,.615)` | `rgba(255,255,255,.55)` | 薄材质 |
| `--color-attention` / `--system-orange` | `#FF4D00` | `#FF4D00` | 强操作强调 |

定向采样 user/agent 的 fill 子层分别测得 `rgba(29,29,31,.3)` / `.7`，均无 backdrop blur，直接证实 dark 消息填充。输入玻璃 `._7ol3wd3` 明确 `blur(50px)`、`24px` radius、`1px` stroke；CSS 宿主测到透明是连续裁切机制导致。聊天气泡声明不含相同的 backdrop blur；不能把 composer/card 的 50px 普遍加到气泡。

JS 还保存原材质层配方，如 `harkRegularLight` 为白色 `.05 plus-lighter` + `.65 color-dodge`，dark 为 `1D1D1F .55 normal` + `.15 soft-light`。这些配方经过 helper 可合并为普通透明色，也有 DOM `.material-fill` 多层。初始首页面板实测 DOM 是 light fill opacity 0、dark fill opacity 1；dark fill `rgba(29,29,31,.62)`。源：[DvrONiVz.js](../evidence/scripts/hark-assets/files/origins/6c67863ee7f43ffbb455e39ca855380e532f4f91833be0b294da3a4afa592701/assets/DvrONiVz.js) 字符 `5908–6910`，DOM `data-fill`。

**差异不可忽略**：JS `chatBubbleUserDark` 保存 `.5 soft-light`，当前 CSS `--bubble-user-bg` 是 `.3`。不能把二者视作同一最终呈现值；此次聊天复刻应以实测 scope 与 CSS 填充为准。

### 连续曲线圆角

[Bgegb_jl.css](../evidence/styles/Bgegb_jl.css) 字符 `2052–3390` 注册不继承的 `--continuous-*` 属性、设置 `data-corner-path` 宿主透明，将 fill/stroke 绘在独立 span。它们不是可忽略的装饰：输入框、文字气泡、代码卡的 DOM 均有这些子层。

[Bz0NRe01.js](../evidence/scripts/hark-assets/files/origins/6c67863ee7f43ffbb455e39ca855380e532f4f91833be0b294da3a4afa592701/assets/Bz0NRe01.js) 字符 `280850–285300`：默认 radius `24`，使用 `1.528665` 等曲线参数构造 Bézier path；ResizeObserver 按真实 border-box 更新 fill 和 evenodd stroke 裁切。尺寸改变后重新算路径，滚动子层也同步 translate。静态逻辑与初始 DOM 保存的 `clip-path:path(...)` 相互证实。

## 变量、布局与响应式

### 原变量与语义映射

[design-tokens.css](design-tokens.css) 可直接查看原始选择器/名称/值；[design-tokens.json](design-tokens.json) 给每个定义保存来源、字符偏移、推断标签与 CSS 使用示例。原始 CSS 变量名称没有被替换为自造命名。`.mr6c8q0` 与 `.rh8xfp0` 的作用域被保留，不能假设在 `:root` 自动存在。定向采样 `token-scope` 的原 `.mr6c8q0` 包含全部 `--mr6c8q*` 变量，已附在 JSON 的 `runtimeElementSamples`；根变量采样不含这些作用域变量。

`--mr6c8q*` 分组可从使用位置推断：

| 原名范围/例子 | 值 | 推断与限制 |
|---|---|---|
| `--mr6c8q1…--mr6c8qf` | 2,4,6,8,10,12,14,16,20,24,28,32,44,48,64px | 多用于 spacing；具体字段名未恢复 |
| `--mr6c8qr…--mr6c8q13` | 10,12,13,14,15,20,24,28,32,40,48,60,64px | 字号尺度，某些变量此次未见使用 |
| `--mr6c8q1d…--mr6c8q1h` | 300,400,500,600,700 | 字重 |
| `--mr6c8q1i…--mr6c8q1m` | 1,1.2,1.32,1.16,1.4 | 行高倍率 |
| `--mr6c8q1s…--mr6c8q1y` | 4,8,12,16,20,24,9999px | 圆角，`1y` 用在 pill button |
| `--mr6c8q28…--mr6c8q2c` | 4,8,16,20,24px | blur/filter，按具体使用确认 |
| `--mr6c8q2i…--mr6c8q2l` | 1s,.15s,.2s,.3s ease | 过渡时间 |
| `--mr6c8q2m` | 68px | 页顶高度/顶部 inset 使用，名称语义推断 |
| `--mr6c8q2w…--mr6c8q38` | 0,1,10,20,30,40,50,1000,2000,3000,4000,5000,9000 | z-index 层级 |

`--mode-dur:.8s`、`--mode-ease:cubic-bezier(.45,0,.55,1)` 控制主题/material 过渡。按压常为 `.97` scale，`.12s ease-out`。链接卡作用域另有 spring `.42s`、settle `.26s` 与 `900px` perspective，完整原变量已导出；动作行为由交互报告进一步验证。

### 布局尺寸

**静态 CSS**：聊天 transcript 包裹宽上限 `768px`，左右 padding `clamp(16px,calc((100% - 512px)/2),128px)`，在宽屏形成 **512px 内容带**；普通 bubble 上限 `424px`，独立 rich card 可以填满内容带。行 gap `12px`，相邻 rich entries 额外 `12px`。message-list 顶部 inset 默认 `96px`，底部 `chat-input-height + 32px`；scrollbar 隐藏，overflow-anchor none。首页 panel 列默认 `420px`。出处：[Bgegb_jl.css](../evidence/styles/Bgegb_jl.css) 字符 `73973–74944`；[cTNWVXWS.css](../evidence/styles/cTNWVXWS.css) 字符 `103778–104955`。

第一批实测：composer-container 与 code-card 宽 **512px**，普通正文常宽 **392px**（424 minus 左右 16）；此时 body rect **1440×812**。初始 inventory 的 viewport 是 **1440×704**，属于不同采样时刻；坐标和高度不可跨快照混用。后续定向采样固定 **1440×900、DPR 1**，message-list 从 x=`420` 开始、宽 `1020`、padding `96px 0 114px`；composer 从 x=`674` 开始、宽 `512`、y=`820`、高 `48`，离视口底 `32px`。宽屏的 420px 首页列与剩余聊天区因此得到实际几何证据。

链接卡变量原值：card 宽 `282px`、radius `24px`、media `272×153px`、media radius `20px`、正文宽 `248px`、gap `12px`；photo 最大宽 `290px`、高 `371px`、radius `18px`。这些是默认上限/预设，不是所有卡片最终尺寸。图片通过比例与 stack 动态 inline vars 修改宽高。出处：[Bgegb_jl.css](../evidence/styles/Bgegb_jl.css) `.rh8xfp0` 字符 `5914`。

### 响应式、触控与减少动效

**以下为静态覆盖；390px窄屏基础几何已另行实测，未验证所有媒体查询状态**：

- `max-width:620px` 改 `.rh8xfp0` 默认 column/bubble 宽为 `100vw-32px` / `100vw-96px`。
- `max-width:1023px` 的 message-list 顶部变体 padding-top `136px`；`max-width:600px` skeleton/content 采用左右 `16px`。具体 selector 适用状态须同时保留。
- `pointer:coarse` 限制某些消息 selection；`pointer:fine` 提供 hover timestamp（75ms 延迟）。`html.touch` 下 homepage panel `.material-content` 直接使用 regular background、隐藏原 fill layers，说明触控路径存在独立优化。
- 多个 `prefers-reduced-motion:reduce` 把 animation 取消或时间降至 `1ms`；恢复这些分支有助于复现真实 CSS。
- 输入框底部 `max(32px,env(safe-area-inset-bottom))`，keyboard-open 时 `16px`；html/root 用 safe-area 和触控视口变量管理可视区域。

精确查询规则已保存到 [design-rules.json](../evidence/analysis/design-rules.json)，包含 295 条带原始字符定位的相关规则；这个文件只做有界提取，完整样式仍以上游捕获文件为准。

## 使用与未解决项

复刻时优先读取相应组件的 computed style，尤其 fill 子层、卡片正文与字体继承；CSS exports 保留作用域后再有意识地选择 theme，连续圆角按实际组件尺寸生成。不要将 dark 快照、静态 light 配置和不同 viewport 几何混成一个实测状态。

当前证据支持文字排版、dark 色彩、玻璃层机制、默认设计尺度，以及完整已发布 wallpaper 的 shader/控制/主题数据链静态分析。浅色实测、移动窄屏、当前 shader uniform 值、实际帧率与各材质/天气分支的完整运行覆盖仍需额外证据。

## 后续定向运行补充

[摘要详情/玻璃层计算样式](../evidence/styles/rich-computed-styles.json) 证实 composer-glass 的 `blur(50px)`、24px radius；其 fill 为 `rgba(29,29,31,.62)`。摘要详情的内容标题是 Hark Serif `40px/52px/500`，正文 `18px/25.2px/400`，strong 为600，正文内 h2 为 `16px/22.4px/700`。dialog中另有无关标题节点24px，不能混入正文分级。

[390×844窄屏几何](../evidence/styles/27-mobile-layout.json) 的 transcript铺满390px，composer在x=16、y=764、宽358、高48、距底32；消息字体15px/19px，document无横向溢出。该快照只验证基础布局，不验证触摸硬件、软键盘、安全区、每一种卡片窄屏行为或主题切换。
