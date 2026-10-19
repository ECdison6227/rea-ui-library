# 离线 UI 设计研究台

这是规范研究与跨业务迁移示例，**手机模板、按钮和动效演示不是原 APK 的运行截图**。原 APK 取证图片只从 `analysis/ui/runtime/screens.json` 读取，单独显示。

## 启动

在当前工作区用已存在的 Python 3.12 运行（没有安装新包）：

```powershell
& 'C:/Users/edsjrcheng/.venv-html-to-docx/Scripts/python.exe' analysis/ui/preview/server.py --port 8766
```

浏览器打开 `http://127.0.0.1:8766/preview/`。Ctrl+C 停止服务。服务只监听 127.0.0.1，文档根目录限制在 `analysis/ui`，不开放其他工作区文件；不提供写入 API。可以用其他可用 Python 3 解释器运行同一脚本。

也可直接打开 `index.html`。颜色、字体、组件与模板交互依赖内置的 `data.js` 快照，通常仍可使用；浏览器可能禁止从 `file://` fetch 截图清单与剪贴板操作，界面会如实说明。

## 可以审阅什么

- RN 与原生分别展示的日夜语义色、原值、来源与引用链；可检索和复制。
- 包内 MiSansLatin 与当前系统字体切换；中文字体回退、字号、真实文件字重与字形探针。
- 两个跨业务模板：学习概览、团队空间。账号、首次加载、空态、内容、失败、离线、刷新结果均可切换。
- 登录门槛、锚定菜单、刷新保留旧内容、Tab 忙碌状态、公开入口与登录入口的可交互示例。
- 原生按钮的启用/禁用、功能行的视觉禁用与可解释禁用态。
- 9 个 RN 交互模式的实际参数与源模块；新增内容展开、按压反馈、Toast 的可运行演示。
- 原 APK 图片画廊，保留 state / evidenceId / source 等元数据；清单或图片缺失时不填充替代图。

## 事实与演示选择

规范数据来自 `design-tokens.json`、`components.json`、`rn-pages.json` 的只读快照。主界面来源链接回到原文件或抽取模块。

模板的业务文案、数据、通用线条图标、页面拼装、详情对话框、700ms 本地请求模拟、220ms 网页 Tab 过渡、演示安全区属于研究台选择。原版 MIUI 平滑自适应边角、mipro 中文系统字形、spring 曲线和实际设备 safe area 没有被假装成精确网页实现。勋章释放使用 300ms ease-out 近似，界面明确标注原始 spring 参数。减少动画模式会跳过非必要演示运动。

`fonts/` 只复制了 4 份包内 Latin 字体，来源、SHA-256 和大小在 `fonts/manifest.json`。中文使用系统回退。没有 CDN 或联网字体。

## 更新数据

原始规范更新后，在工作区运行：

```powershell
node analysis/ui/preview/build-preview.cjs
```

这只更新本研究台的 `data.js`、`snapshot-manifest.json`、字体副本与字体清单，不改原规范。当前快照的源文件哈希可用于确认一致性。

运行截图清单为数组：

```json
[
  {
    "id": "capture-01",
    "title": "原 APK 页面标题",
    "state": "实际观察到的账号、权限和页面状态",
    "image": "runtime/captures/01/screen.png",
    "evidenceId": "真实取证 ID",
    "source": "实际设备或模拟器取证说明"
  }
]
```

`image` 相对 `analysis/ui`。刷新画廊即可载入新清单，不需要重建离线快照。研究台不会执行 APK bundle，也不会调用 App 服务。

## 已完成的浏览器检查

使用本机已有 Chromium 与 Playwright Core 检查了 13 项行为，记录在 [verification/result.json](verification/result.json)：登录门槛和恢复动作、刷新失败保留数据、空态添加、日夜切换、禁用解释、减少动画、390px 无横向页面溢出、已有原 APK 图片清单加载、file:// 回退、零外网请求、零页面 JavaScript 错误等。

`verification/reconstructed-*.png` 仅是研究台重建模板的浏览器截图，用于检查布局；它们不属于原 APK 运行证据。后者只在 `../runtime/captures/` 与相应取证清单中。
