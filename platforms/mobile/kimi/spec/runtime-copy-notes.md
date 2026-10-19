# 已捕获隐私弹窗的文案对照

本页仅对照已有场景 `03-kimi-privacy` 的原始截图、UIA 层级与打包文案，没有操控模拟器，也没有测试链接或同意/不同意动作。文字实际显示记为 **A**，CVR 原文记为 **B**，资源关联候选与拼接解释记为 **C**。匹配文字不能替代代码中的资源引用证据。

## 来源及运行条件

- 辅助记录：[`runtime-copy-crosscheck.json`](../evidence/static/runtime-copy-crosscheck.json)，编号 `LOCAL-RUNTIME-COPY-001`，不是 REA Evidence ID。
- 原始截图：[`03-kimi-privacy/screen.png`](../evidence/runtime/scenes/03-kimi-privacy/screen.png)，SHA-256 `bb571ebaa5e6d8298838e4d4afceba5d792b653127811206c75c76f0acbe1c3c`。已直接看图，确认为可读的 Kimi 隐私同意弹窗，非空白采集。
- UIA：[`hierarchy.xml`](../evidence/runtime/scenes/03-kimi-privacy/hierarchy.xml)，SHA-256 `5340b794cfbbe10331c76d4f36d57fb39de5609ac8f3a45340deefc52cf6948c`。
- 条件来源：[`capture.json`](../evidence/runtime/scenes/03-kimi-privacy/capture.json)。采集时间 `2026-10-15T15:16:30.556Z`，包名 `com.moonshot.kimichat`，APK SHA-256 `9637623695b36eaba1639a6c346310ef05b2a9d478332dcbc5e934cc94dd8332`；AVD `rea_kimi_ui_20261015`，serial `emulator-5596`；1080×2400 物理像素、density 420、fontScale 1.0、nightMode no。
- `systemLocales` 在采集记录中为字符串 `null`，没有足够资料确定系统/应用语言选择及回退顺序。这里能确定的是界面显示英文；不能把它扩大为默认语言选择机制已经验证。
- B 来源为 [`values/strings.commonMain.cvr`](../assets/original/assets/composeResources/kimi.shared.generated.resources/values/strings.commonMain.cvr)，SHA-256 `800e9077ace472c85af705291f1fde777b52e4e35f96ff50c909c7236d245805`。解码数据位于 [`default.json`](../assets/derived/copy/default.json)。精确匹配已检索全部 17 个已解码 CVR；检索不包含 ARSC、服务端文字和依赖库文案。
- 复核脚本：[`crosscheck-runtime-copy.mjs`](../tools/crosscheck-runtime-copy.mjs)，在研究库根目录运行 `node tools/crosscheck-runtime-copy.mjs`。脚本只读取保留资料，检查截图/UIA 哈希并生成上述交叉记录。

## 可定位的文字及歧义

| A：界面文字 | B：未限定目录资源键 / 原 CVR 行号 | C：关联范围 |
| --- | --- | --- |
| `Welcome to Kimi` | `welcome_to_kimi` / 2322 | 17 个 CVR 中唯一精确文本候选；仍未确认代码引用 |
| `Terms of Service` | `user_service` / 2247 | 正文合并 TextView 的子串，非独立 UIA 链接节点 |
| `and` | `and` / 159 | 正文中的连接词；原文是三个字母，首尾均没有空格 |
| `Privacy Policy` | `privacy_policy` / 1347 | 正文合并 TextView 的子串，非独立 UIA 链接节点 |
| `Understand our services and how we protect your information. By tapping Agree, you consent to the above agreements` | `privacy_page_content_2` / 1344 | 与正文后半段精确对应，原文首尾无空格或换行 |
| `Disagree` | `disagree` / 532 | 17 个 CVR 中唯一精确文本候选；实际点击结果本页未测 |
| `Agree` | `agree_and_continue` / 149；`agree` / 150 | 两个原文完全相同，UIA 标签没有 resource-id，不能唯一归因 |

对 Agree 的歧义，中文资源分别是“同意并继续”和“同意”。这可以帮助后续核查其他语言或代码引用，但不能用中文差异反推这张英文界面必然使用哪个键。

`onboarding_welcome_description`（行 1232）是 `Welcome to Kimi. See what I can do.`，与实际标题不同；因此这张截图支持 `welcome_to_kimi` 的精确文本关联，不支持把所有 `onboarding_*` 流程都视为已运行验证。

## 链接与正文之间的空格

UIA 把协议链接及说明合并为同一个 `android.widget.TextView`，其 `text` 原文是：

```text
Terms of ServiceandPrivacy PolicyUnderstand our services and how we protect your information. By tapping Agree, you consent to the above agreements
```

A：原图中 `Terms of Service` 与 `Privacy Policy` 为蓝色片段，连接词和说明文字为深色，视觉上同样可见 `ServiceandPrivacy` 与 `PolicyUnderstand` 的相邻连接；后者跨行前后的排版不改变缺少词间分隔这一文本事实。这里不从截图新增精确颜色、字号或字体结论。UIA 没有分别暴露两个链接的独立节点；蓝色外观也不能确认目标 URL 或点击行为。

B：四个对应原资源的首尾均没有空格或换行：

```text
user_service           = "Terms of Service"
and                    = "and"
privacy_policy         = "Privacy Policy"
privacy_page_content_2 = "Understand our services and how we protect your information. By tapping Agree, you consent to the above agreements"
```

C：在分析脚本中按上述顺序、使用空分隔符连接四个原文，结果与 UIA 正文逐字符相等。在它们之间加空格则不相等。三个边界分别是 `e|a`（Service / and）、`d|P`（and / Privacy）、`y|U`（Policy / Understand）。这证明“原文片段没有携带边界空格”与“画面/层级文本没有相应空格”同时成立，不能证明运行代码确实直接拼接，也不能证明缺陷的具体责任位置。

包内另有 `user_service_and_privacy_agreement`（行 2246），原文为 `Terms of Service and Privacy Policy`，包含空格；它并不是这次 UIA 正文的精确前缀。这一差异提示必须保留资源粒度和当前场景，不能宣称整个包都没有正确英文空格，也不能据此断言这个组合键从未使用。

## 对风格库的影响

本场景为欢迎/隐私同意弹窗提供了实际文案样本；之前的包内文案研究仍然有效，但只有本页列出的文字在本次特定环境下获得 A 显示证据。它没有验证麦克风权限、完整个性化引导、空态、失败恢复或危险动作流程。

迁移时，边界缺少空格应作为来源差异记录保留，不写成应继承的产品风格。若独立参考实现使用英文，应将连接符/分隔符作为本地化完整句子的一部分或显式处理，并标为 D 实现选择；这是本研究的建议，不是来源实现的还原结论。

仍待补证：具体资源绑定、正文构建代码、语言回退、链接可点击范围及目标、同意/不同意之后的行为、Back/外点关闭、焦点与阅读顺序、选择持久化、再次启动、其他主题和字体缩放。不得仅凭本次文字对应关系补齐这些状态转移。
