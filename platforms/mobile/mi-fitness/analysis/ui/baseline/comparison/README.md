# 运行截图对照

最终交付入口是 [final/index.html](final/index.html)，人工结论在 [final/findings.md](final/findings.md)，对应 `reference-accepted` 和 APK `4a62b1255795edbb422990a8198e3320f98b8cdf592a4966a42c18ef8c9d3b5b`。本目录默认 `index.html` 与报告保留早期迭代，不能作为最终参考。

全部六张图片齐备后运行：

```powershell
python .\compare.py
```

需要 Python 3 与 Pillow。本机已有 Python 的相对用户路径是 `$env:USERPROFILE\.cache\codex-runtimes\codex-primary-runtime\dependencies\python\python.exe`。脚本位置决定默认输入目录，不依赖当前工作目录。输入是 `../runtime/{health-day,mine-day,settings-day}/screen.png` 和 `../reference/` 下同名文件；画布必须全为 1080×2400，脚本不会缩放输入。

运行后直接打开 `index.html`。它通过本地 `report-data.js` 获取生成的数据，无需服务器、CDN 或网络；图像全部是相对路径。`report.json` 提供相同的机器可读内容。修改 `differences.json` 后须重新运行脚本，才能刷新离线嵌入的人工差异记录。

每对输出全画布并排图、全画布 50% 等权叠图、RGB 绝对差值原图及其 3 倍显示图。差异和区域测量只取 `[0,96,1080,2334)`，范围外填黑；并排和叠图保留整图上下文。注意原版内容从 y96 开始，当前参考 AOSP decor 从 y136 开始，因此固定 ROI 的 y96..136 仍可能包含参考系统栏像素，须单独解释。原 PNG 不被修改，输入哈希在处理前后复核，A/D两套哈希均与各自真实 REA 捕获记录核对。

十个组件的派生裁剪由 `component-crops.json` 指定，并写入离线页面的组件区。它记录从规格、层级/像素中选出的原始组件 bounds 和带上下文的共享裁剪框；两侧使用同一坐标，不会通过各自平移消除真实位置偏差。这些是全页实测截图的派生图，不是独立组件运行，也不增加来源状态覆盖。

后续改动参考时，请保留旧图。新增参考根目录仍包含三个同名页面子目录，截图的 REA JSON 放该目录根部。可另存最终对照：

```powershell
python .\compare.py --final-reference-override '..\reference-accepted' --output-dir '.\final'
```

`--final-reference-override` 和 `--output-dir` 按当前命令目录解析，绝对路径也可。新目录自动取得离线 HTML 模板；全部图片和证据链接重新计算相对路径。每页人工记录绑定 `reviewReferenceSha256`：参考图改变时，旧记录自动移到 `priorManualReview`，状态为 `stale-needs-review`，不会冒充新图的人工检查结果。更新新输出目录的 `differences.json` 再用相同命令重跑可确认新复核。

辅助数据包括若干预定精确 RGB 的连通区域边界，以及深色文字/图标候选框和几何候选配对位移。这里的白色区域可能是页面、卡片或其他内容，颜色本身不能确定语义；候选配对也可能将不同标签错误对应。字体基线、文字内容、控件全边界需结合层级和源码复核，不能把像素算法当成 OCR 或已验证的实现规格。

`differences.json` 中逐页记录已知差异、影响和未知。初始 `pending` 表示还未人工复核；空的差异列表不代表没有差异。图片对照不验证点击、返回、动画、手势、状态保留或异步行为，相关证据见基线的行为记录。这里不计算相似度或完成度百分比。
