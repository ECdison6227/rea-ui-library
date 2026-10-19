# Mirasim APK 逆向研究与开发参考

分析对象：`mirasim-0.0.497.apk`。本目录包含 REA 证据、可阅读的前端/Android 反编译结果、实际前端截图、离线预览和后续开发参考。

**核心结论：这是 React + Capacitor 的混合应用。界面主要由打包在 APK 中的 HTML/JS/CSS 绘制，Android 原生插件负责通知、后台连接、APK 更新与系统外观适配。**

## 从这里看

1. [逆向总报告](analysis/reports/01-overview.md)：结论、范围、证据和未验证部分。
2. [UI 设计拆解](analysis/reports/02-ui-design.md)：主题、色值、字号、组件、布局、动效和页面导航。
3. [应用更新完整链路](analysis/reports/03-updates.md)：版本源、轮询、状态机、下载、校验、安装授权和系统安装器。
4. [通知、权限与后台保活](analysis/reports/04-notifications-permissions.md)：申请时机、拒绝处理、通知通道、电池优化、自启动、后台长连接。
5. [架构与新项目实现蓝图](analysis/reports/05-architecture.md)：已观察到的架构与建议的新项目目录，各自明确区分。
6. [证据与复现说明](analysis/reports/06-evidence-and-reproduction.md)：REA 用法、哈希、生成命令、局限和真机验证清单。

## 打开界面研究台

```sh
python3 analysis/scripts/preview-server.py
```

浏览器访问 **http://127.0.0.1:4174**。研究台提供页面截图和交互预览，原始页面资源直接取自 APK。

预览适配器会阻止对原厂服务器的请求。Android 专用入口使用模拟桥接；主界面使用虚拟配对资料，只展示空态。它们用于检查布局与界面分支，不能验证系统权限弹窗、真实登录、后台送达或安装升级。

## 后续开发可直接参考的材料

- [`analysis/reference/design-tokens.css`](analysis/reference/design-tokens.css)：从实际浏览器计算样式导出的浅色/深色设计变量。
- [`analysis/reference/ports.ts`](analysis/reference/ports.ts)：平台能力的类型边界。
- [`analysis/reference/update-policy.ts`](analysis/reference/update-policy.ts)：版本源解析、版本比较、检查间隔和更新状态转换的独立参考实现。
- [`analysis/reference/permission-policy.ts`](analysis/reference/permission-policy.ts)：通知权限及引导决策参考。
- [`analysis/reference/README.md`](analysis/reference/README.md)：参考代码的使用范围和验证方式。

这些文件是研究提炼出的开发参考，不是原作者的 TypeScript 源码，也不是一套已经能够编译发布的新 Android 工程。

## 从 GitHub 下载后使用

仓库保留 APK、分析资料和工具依赖清单，未包含本机安装的 Node 依赖、JDK 和 JADX。仅浏览报告与截图不需要安装这些工具；启动界面研究台只需 Python 3。

完整前端 REA 证据以 `analysis/evidence/rea-frontend-all.json.gz` 保存，需要原始 JSON 时解压：

```sh
gzip -dk analysis/evidence/rea-frontend-all.json.gz
```

需要运行分析和验证脚本时，先恢复 Node 依赖，再按复现报告准备 Java 与 JADX：

```sh
npm ci --prefix analysis/tools
```

## 文件分层

| 目录 | 内容 |
| --- | --- |
| `analysis/extracted/apk/assets/public/` | APK 中原样提取的前端资源 |
| `analysis/formatted/app.js`、`app.css` | 便于阅读和引用行号的前端构建产物 |
| `analysis/formatted/java/` | 应用自有 Java 类及相关通知、存储插件的 JADX 输出 |
| `analysis/decoded/` | 解码后的 Android XML 和资源表 |
| `analysis/evidence/` | REA JSON/Smali、APK 清单、提取索引、验证结果 |
| `analysis/preview/` | 截图、离线预览适配器和研究台 |
| `analysis/reference/` | 设计变量和独立参考代码 |
| `analysis/tools/` | 项目局部安装的 REA/JADX/JDK 等依赖 |

分析没有调用原厂账号接口进行登录、发送验证码或下载新版 APK，也没有在 Android 设备上安装或修改此 APK。原 APK 保留不变。
