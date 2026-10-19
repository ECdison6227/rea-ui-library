# 证据、复现与验证边界

## 这次实际使用了 REA

工具以项目局部 npm 依赖安装，版本 6.5.0，未执行会修改全局代理配置的 `rea setup`。Java 21 和 JADX JAR 均位于 `analysis/tools`。REA 的 CLI 能直接使用，不要求重启当前会话注册 MCP。

REA Android 官方说明：[Static Android APK analysis](https://github.com/morluto/rea/blob/main/docs/android-analysis.md)。按照其中要求下载 jadx-headless-mcp 0.7.1，并核对项目包内 `dist/android/JadxRelease.js` 指定的 SHA-256：

```text
6e5eacf500b64292bfb73c49797c1958f6ee44646e43e868039ae7feb573ff75
```

JDK 归档亦与 Adoptium 发布响应中的 checksum 比较一致。所用发布信息和本地依赖锁文件保留在 tools 目录。

## 证据索引

| 文件 | 用途 |
| --- | --- |
| `apk-inventory.json` | 原始 APK SHA-256、体积和 728 个 ZIP 条目 |
| `rea-android-package.json` | 包名/版本/SDK/权限/Manifest/引擎身份 |
| `rea-frontend-all.json.gz` | 整个前端目录的 REA 静态分析，压缩保存；解压后的 JSON 约 65MB |
| `rea-frontend-summary.json` | 上述证据的便于阅读的摘要，保留限制 |
| `rea-frontend.json` | 初次只选择主 bundle 的分析，有明确资源预算限制 |
| `rea-updater-class.json` | 更新插件成员清单 |
| `rea-updater-download.json` / `.smali` | 下载方法，Java 失败后回退 Smali |
| `rea-updater-references.json` | 更新插件静态被用引用，包含 MainActivity |
| `rea-notification-post.json` | 通知 post 方法 |
| `rea-keepalive-start.json` | KeepAliveService 启动方法 |
| `rea-attention-delivery.json` / `.smali` | 后台提醒决定投递的方法 |
| `frontend-translations.json` | 482 个 mobile.* 文案键及出现行号，重复语言值保留 |
| `frontend-functions.json` | 移动端段落 776 个函数名和行号索引 |
| `frontend-plugins.json` | 主 bundle 中字面注册的插件，不包括全部懒加载模块 |
| `frontend-endpoints.json` / `frontend-apiTemplates.json` | 字符串与动态 URL 模板索引；需人工区分文案、依赖及真实接口 |
| `preview-verification.json` | 7 个前端状态 × 3 轮检查结果 |
| `reference-tests.txt` | 独立参考逻辑测试结果 |
| `zip-case-collisions.json` | 原 ZIP 中大小写冲突条目记录 |

## 复现命令

在项目根目录执行。依赖已安装；如果移动/清理了 tools，需要根据锁文件重新准备 npm 依赖、完整 JDK 和经摘要验证的 JADX JAR。

```sh
sh analysis/scripts/rea.sh inspect-android-package mirasim-0.0.497.apk --format json

sh analysis/scripts/rea.sh inspect-android-class mirasim-0.0.497.apk ai.mirofish.mirasim.ApkUpdaterPlugin --format json

sh analysis/scripts/rea.sh inspect-android-method mirasim-0.0.497.apk ai.mirofish.mirasim.ApkUpdaterPlugin 'lambda$download$0' --format json

sh analysis/scripts/rea.sh trace-android-references mirasim-0.0.497.apk ai.mirofish.mirasim.ApkUpdaterPlugin --format json

analysis/tools/node_modules/.bin/rea analyze-javascript-application analysis/extracted/apk/assets/public --max-semantic-nodes 4096 --max-semantic-nodes-per-file 256 --format json

node analysis/scripts/extract-indexes.cjs
```

可阅读 Java 与 XML 使用相同 JADX JAR 的 API 补充导出，脚本为 `ExportJava.java`、`ExportResources.java`。它们不是 REA 的原生接口，输出不冒充 REA Evidence；REA 原始 JSON 独立保留。

```sh
analysis/tools/jdk-21.0.12.1+1/Contents/Home/bin/java -Xmx3g -cp analysis/tools/jadx-headless-mcp.jar analysis/scripts/ExportJava.java mirasim-0.0.497.apk analysis/formatted/java

analysis/tools/jdk-21.0.12.1+1/Contents/Home/bin/java -Xmx2g -cp analysis/tools/jadx-headless-mcp.jar analysis/scripts/ExportResources.java mirasim-0.0.497.apk analysis/decoded
```

`ExportJava.java` 配置 DEBUG 注释以保留反编译失败的方法指令。提高类型推导预算后更新方法仍失败，**其可读 Java 不是完整成功结果**；应读已保留 Smali。没有通过修改业务代码“修复”目标 APK。

## 文件提取完整性

原 APK 共 728 个 ZIP 条目。macOS 所在文件系统对大小写不敏感，Android 原始混淆资源名中存在 **15 组**大小写冲突；普通 unzip 不能把这些文件按原名同时保留。原 APK 和 ZIP 清单保留全部条目，`analysis/decoded` 通过 JADX 解码并恢复资源别名，绕过直接文件名冲突。

重点 UI 资源目录 `assets/public` **没有大小写冲突**。额外校验逐项比较原 ZIP 内容与提取后前端文件的 SHA-256；结果记录于 `artifact-verification.json`。不要根据 `extracted/apk/res` 声称原始混淆资源都已无损并列展开。

## REA 覆盖的准确含义

前端全目录分析读取 92 个相关文本文件，解析 91 个 JS 文件，访问约 298 万 AST 节点，零解析失败。语义关系图为 **partial/truncated**，限制为全局 4096 节点、每文件 256 节点。无解析失败不等于所有调用关系恢复完整；动态值、反射和被预算省略的关系仍未知。

Android 的 JADX 反编译结果是 DEX 的可读表示，不保证原始变量名、泛型、lambda、控制流或注释还原。应用自有代码中两个方法结构化 Java 失败；本次均另取完整 Smali。APK 的签名验证状态为 `not_performed`，没有验证安装包发布者真实性。

## UI 验证

截图工具为 oil-ui 自带 `shoot.mjs`，首个登录基线由 Playwright 截取。使用独立临时 Chromium 配置，不读取用户浏览器数据。所有新预览位于独立本机端口，原始 APK 内容不被改写，研究用适配器通过预览服务器注入。

`verify-preview.cjs` 连续运行三轮，每轮覆盖 7 个页面/主题状态：登录、连接码、虚拟配对主界面、深色设置、浅色设置、通知设置、更新设置。检查页面关键文案、实际主题、JS pageerror、横向溢出、外部网络请求；共 21/21 通过。该脚本不证明按钮背后的 Android 能力可用。

独立参考实现另有 8 项测试，覆盖稳定/Beta、无效元数据、版本比较局限、下载中检查冲突、校验失败、安装器返回状态、授权拒绝与引导条件。测试通过不能外推为 Android 插件测试通过。

参考 TypeScript 文件还通过了严格类型检查。前端文件逐项比对共 163/163 通过，52 个本地 Markdown 链接目标存在，原 APK 的 ZIP CRC 检查通过。

```sh
# 先在一个终端运行
python3 analysis/scripts/preview-server.py

# 另一个终端验证
node analysis/scripts/verify-preview.cjs
node --test analysis/reference/reference.test.ts
```

## 尚未执行的真机验证

| 场景 | 需要补的证据 |
| --- | --- |
| Android 26～32、33+ 通知授权 | 首次允许/拒绝/设置恢复的实际系统状态与截图 |
| 锁屏与后台 | 通知延迟、服务存活、断网/重连、进程重建 |
| 厂商差异 | 小米/华为/Oppo/Vivo 等当前系统的自启动入口可用性 |
| APK 更新 | 自有测试发布源、摘要不匹配、网络中断、存储不足、未知来源往返、取消安装、覆盖安装后版本 |
| 相机、文件、安全区 | 扫码授权、下载/分享结果、软键盘、系统大字、横屏、刘海与手势导航 |
| 已登录业务 | 使用授权测试账号及自有/测试服务验证会话、成员和文件完整流程 |

这些目前均标为未验证，不在本次离线预览的通过项中。

## 可选工具更新提示

分析中使用的本地 oil-ui 技能版本为 0.16.6，版本检查提示 0.22.4 可用。本次没有更新。若日后需要，检查器给出的更新命令是：

```sh
/Library/Developer/CommandLineTools/usr/bin/python3 /Users/cjr/.codex/skills/oil-ui/scripts/check_update.py --update
```
