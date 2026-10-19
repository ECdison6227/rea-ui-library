# 原 App 图像、字体与文字资产库

这是小米运动健康 `com.mi.health` **3.57.0 / 357000** 的资产保留库，原包 SHA-256 为 `2a80fc5a87353c2c6ef3c01fa0085862fe45e4ddb9c2d272cb3780841b5889a6`。原始 `base.apk` 没有修改。

**保留不等于认定它属于当前 App 的设计语言。** 包中包含产品资源、SDK 资源、旧页面资源及用途未知的资源。实际迁移先从[已运行的原生参考资产清单](../android-reference/assets-manifest.json)和[设计规格](../README.md)进入，再查本库补充素材。不要把库存数量当作运行覆盖率。

## 查找入口

| 文件/目录 | 用途 |
|---|---|
| [inventory.json](inventory.json) | 每份文件的原路径、SHA-256、字节数、类型、图像尺寸、来源分类、原生参考互链 |
| `original/res/` | 保留 decoded res 的命名目录与字节；包含位图、矢量、值定义、动画依赖与中英文文案 |
| `original/apk-encoded/` | APK 内其他图像/字体，以及与 decoded 文件字节不同的原始编码成员；嵌套压缩包用 `.contents/` 保留成员层级 |
| `runtime-selected/assets/` | 原生参考使用的 39 份远端图标；全部与原 App 图片缓存原字节匹配 |
| [texts.jsonl](texts.jsonl) | 按资源名检索文字；Android strings/plurals/arrays 与 RN 明确本地化目录的中英文条目 |
| [text-index-schema.json](text-index-schema.json) | 文案分类规则、数据字段与索引限制 |
| [resource-dependencies.json](resource-dependencies.json) | Drawable/矢量/动画/值定义的资源引用及解析结果 |
| [animations.json](animations.json) | 仅 Lottie schema 匹配的 JSON、压缩包检查记录、未匹配 JSON 与检查错误 |
| [video-index.json](video-index.json) | 视频原包位置索引；本版本识别到 0 个视频，不额外复制媒体视频 |
| [verification.json](verification.json) | 原包身份和所有复制文件的哈希检查结果 |
| [tools/build_library.py](tools/build_library.py) | 从现有原包与 decoded res 重建；不执行 App 代码 |

图像保持原字节；没有截图裁切、重采样、转码或重画。XML 保留 decoded 文件原字节，不能把 decoded XML 称为 APK 编译成员的同字节副本。原始 APK 编码图像若与 decoded 图像不同，也另行保留；`exactAPKByteMatches` 与 `additionalAPKPaths` 能追到相同字节的原包成员。

原包有仅大小写不同的资源路径，在 Windows 文件系统中会冲突。遇到不同字节的冲突成员时，存储文件名增加 `.case-<内容哈希>`；原包路径仍完整保存在 `source.path`，调整记录在 `summary.storagePathAdjustments`，两份原字节均保留。命名 decoded res 的目录继续保持原相对路径。

## 当前保留范围

当前机器索引包含 4,303 份图像、616 份 VectorDrawable XML、8 个字体文件、15 份 Lottie JSON、34 份中英文文字源文件，以及矢量/状态资源需要的值定义与动画依赖。资产本体约 21.69 MiB；机器索引和文案索引另占空间。准确统计以 `inventory.json.summary` 为准。

Android 文案保留 `values/` 的默认英文及 `values-zh*` 的简体、繁体和地区变体；此包没有独立的 `values-en` 目录。RN 文案保留内嵌 bundle 压缩包中明确 `res/localizedString/` 目录下的 `en*`、`zh*` JSON，不把任意业务 JSON 当作文字资产或动画。

包内 RN 文案对应**内嵌 bundle**；当前运行 Mine 使用 `11.202.21` 热更新包。包内文案的存在不能直接证明当前热更新页面采用该条文案，版本边界见[运行包对照](../runtime-rn-crosscheck.md)。本库只补充已捕获的 39 个运行图片缓存资源，不宣称枚举了所有远端资源。

## 来源与通用文案

`provenance` 只取 `firstparty`、`SDK`、`unknown`。39 份图片的 `firstparty` 表示原 App 模块明确引用的小米资源主机交付来源；658 份 `SDK` 文件处于原包明确的 `assets/plugin_rn_sdk/` 子树。其余不根据资源名前缀或画风猜归属，保留 `unknown`。这些标签不证明画稿作者身份，也不证明资源当前正在使用。

文案的 `businessScope` 另行分类，与来源归属分开：

- `generic`：精确命中“取消 / 保存 / 重试 / 设置”等短标签白名单，可以作为通用操作词候选，仍要核对上下文。
- `health`：文字含明确的心率、血氧、睡眠等领域词，需要改写成目标业务内容。
- `unknown`：未完成语义核对，保持未知。

分类属于 **C 分析规则**，不是原作者设计意图，也不是整页业务规则。原 XML / JSON 属于 **B 已交付声明**。使用文案时保留占位符、格式化标记、复数 quantity、span 与转义；`texts.jsonl` 用于检索，精确格式以对应 `sourceFile` 为准。

例如，可在 PowerShell 中查找通用标签或原资源名：

```powershell
rg '"businessScope": "generic"' analysis/ui/asset-library/texts.jsonl
rg '"name": "[^"\\]*sleep' analysis/ui/asset-library/texts.jsonl
```

## 组件接入边界

尺寸字段 `widthPx/heightPx` 是资源像素尺寸，不能当作 dp/sp。原生参考清单提供实际组件中的显示规格和准确绑定：

- 摘要裸火焰 / 黄鞋 / 绿人分别来自 `target_calorie` / `target_step` / `target_stand`；中高强度蓝时钟来自 `health_target_esim`。不要用 `health_data_*` 圆形详情图替代。
- `bg_data_headerview` 浅色源图为 `8×269`，深色为 `24×807`；原布局背景高度是 `269dp`。浅色源位于默认 `drawable/`，深色位于 `drawable-night-xxhdpi/`，不能统一按 xxhdpi 解释。
- `health_bg_share` 浅深色源图均为 `246×264`，来自 xxhdpi，原资源 intrinsic 为 `82×88dp`。
- 大彩虹主体是 `ThreeTargetView` 自绘，不是 `health_ic_rainbow` 小矢量。本库没有把截图当作大彩虹的实现资源。
- 字体文件存在不证明运行时每个字形使用它。MiSansLatin 不包含完整 CJK 字形，中文 fallback 与宿主系统有关；参考[字体环境证据](../runtime/font-environment.json)。

矢量、Drawable 和动画自身的本地资源引用均已保留；`styles.xml` 还引用了若干未复制的布局资源，这些在依赖索引中显式保留为 `not-in-media-copy`。本库不是可以整体导入编译的 Android 资源模块。按组件挑选资源，并按需要带上其依赖。

## 重建与检查

在仓库根目录运行：

```powershell
python analysis/ui/asset-library/tools/build_library.py
```

脚本要求原始 `base.apk`、现有 `analysis/phase2/apktool/res/` 和原生参考资产清单。Pillow 仅用于报告位图尺寸，未安装时仍保留原字节。脚本会核对原包 SHA-256、逐份检查复制哈希并刷新与原生参考的双向链接，不会修改 APK，也不会启动模拟器。生成的检查证明资产保留与来源可追溯；不证明组件动画、交互或还原效果已验收。
