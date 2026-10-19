# REA UI技能改进审查

这是可审查的本地草案，未替换全局已安装skill、提交或推送。来源是 `ECdison6227/rea-style-library` 的 `42c82c4ac70a6a92e3748897051d70b883d29fac`，隔离分支为 `codex/workflow-improvements-20261019`。升级副本在 [skills/rea-style-library](../skills/rea-style-library/SKILL.md)，原仓库路径补丁在 [skill-upgrade.patch](skill-upgrade.patch)。

本总库按用户最新范围只收REA研究：Hark网页，以及小米运动健康、Mirasim、Kimi手机研究。普通UI实现不因此取得REA研究身份；来源中的迁移例子仍附属于该研究。没有匹配REA研究的桌面类别可留空。

## 改什么及为什么

Kimi的真实研究暴露了范围、运行节奏和交接成本之间的张力。37项清单本身不必然错误，用户明确要求全测时仍应完成或如实保留阻塞；此次改进避免把“迁移UI与核心交互”的一般目标自动扩成全功能验收。一次产品的故障不能证明普遍规律，因此升级未写入Kimi的场景编号、设备端口、固定重试次数或某次inset数值。

| 原流程容易产生的行为 | 升级行为 | 理由 |
|---|---|---|
| 长研究维度被展开成所有状态都必测 | 入口先记录目标/验收/停止条件；默认UI与核心交互，明确全测另按用户清单 | 深度研究与功能穷举是不同任务 |
| 原业务未测项使整库一直“不能完成”，或为交付把未测改verified | 分开research coverage、migration readiness、agreed delivery；接受边界有用户指令依据 | 可用规则可以交接，未测事实保持不变 |
| 同类账号/容量/设备失败被每轮重新尝试和催问 | 按风险设有界预算；记录依赖、已请求/延后及继续条件，独立项目继续 | 把依赖等待与研究进展分开，保留明确全测要求 |
| “证据足够后再重建”被理解成所有研究完成后才首次运行参考 | 局部A/B充分即做可运行D切片；早配对输入、返回、系统栏/安全区、IME、焦点和缩放 | 避免通用布局问题到最后才集中暴露 |
| 每轮复制并硬编码大量run、捕获、清单和分发工具 | 以配置复用target/run/artifact/capture/coverage/distribution；旧格式适配；附最小模板和通用离线检查器 | 降低续接成本，不要求统一所有平台驱动 |
| 桌面浏览器页面、原生客户端和参考工程混为一个平台 | 按原版实际平台分类，运行栈另列，保留Git版本/启动方式及独立profile | 统一检索不等于重写工程或统一风格 |
| 有UI或同作者的普通工程被当作REA产物 | 入库先核实可追溯研究身份，普通实现/实验不自动加入，空类别不充数 | 符合用户“只管REA出来的项目”的范围 |

入口从原68行/4081字符收敛为短入口，详细阶段、依赖与记录规则移至按需引用的workflow。原研究、交付和迁移三份reference同步调整，避免与短入口互相冲突。新增细节不要求每轮全部读取。

## 可审查内容

- [SKILL.md](../skills/rea-style-library/SKILL.md)：目标路由、三条状态轴、早期可运行切片、核心证据边界。
- [workflow.md](../skills/rea-style-library/references/workflow.md)：阶段闸门、依赖复试、REA入库资格、平台与Git身份、可复用run/分发和续接。
- [research-manifest.json](../skills/rea-style-library/assets/research-manifest.json)：尚无证据的空模板。空数组表示未记录，不表示运行通过。
- [check-manifest.mjs](../skills/rea-style-library/scripts/check-manifest.mjs)：无第三方依赖，只读核对声明结构、引用、真实字节和精确分发选择；不会连接设备、Git、网络或更改状态。
- [check-manifest.test.mjs](../skills/rea-style-library/scripts/check-manifest.test.mjs)：合成正反例；内容不是实际原版观察。

`agents/openai.yaml` 未修改，配置与原基线一致，`allow_implicit_invocation: true` 保留。现有用户授权优先级及“skill本身不授予外部操作权限”的边界保留。未安装/更新全局skill、没有操作ADB/模拟器或修改Kimi证据。

## 验证

使用仓库Node YAML校验器与本机已有YAML依赖，未安装Python或全局工具。校验器改为遍历全部Markdown本地引用，以覆盖新增workflow中的链接。

在原skill仓库根运行：

```sh
node tools/validate-skill.mjs
node --test skill/rea-style-library/scripts/check-manifest.test.mjs
node skill/rea-style-library/scripts/check-manifest.mjs skill/rea-style-library/assets/research-manifest.json
```

结构/YAML/调用配置和本地引用检查通过；合成测试9组通过，覆盖A/D混用、实际复核与时间边界、REA入库声明、用户接受范围但研究仍未测、明确全测不能遗漏必要条件、私有原件的local/distributed区别、精确分发固定值、篡改/重复ID/越界路径。空模板只报告 `passed-integrity-only`、零运行capture、readiness=not_assessed、delivery=in_progress，不被转成验收完成。

补丁以原仓库根为目标，包含 `skill/rea-style-library/...`、`tools/validate-skill.mjs`，以及同步后的README、通用研究Prompt、改造软件Prompt和验证说明；已在对应基线的隔离Git index执行apply检查通过，没有暂存内容或提交。在基线克隆也可先运行 `git apply --check /path/to/skill-upgrade.patch`。补丁包含新增文件，无需先覆盖已安装skill。

## 实际局限

这些检查没有重新运行Kimi或其他原产品，也没有证明升级后Agent在所有真实任务里都会正确停止。阶段与重试仍需结合用户要求和实际风险判断。任何“用户明确接受”的引用仍需人或Agent核实原指令，检查器无法认证授权；公开分发的名单也不能代替实际ZIP成员审查。

通用manifest是可选互操作结构，不强制改写旧研究格式。脚本只检查已声明文件，无法发现未声明但被误打包的内容；平台控制/捕获驱动、GUI单控制者协调、像素复核、账号有效性和实际后端语义仍由具体项目负责。合成测试不提供新的A证据，也不代替独立真实情境试用。

## 人工入口同步

原仓库README和两份复制Prompt已同步，不再保留固定“三页/十组件”的取样建议或“所有研究结束后才做参考”的顺序。README说明默认UI与核心交互、三条状态轴、阶段/依赖、早期配对与REA入库资格，并介绍可选manifest/检查器。通用研究Prompt给出可独立复制的目标/验收/排除边界、取样维度、早期D切片、非原子与单控制者、可复用run和有界依赖处理。改造软件Prompt同步目标范围、profile资格/平台、早期可运行迁移和分别验收。

三处都保留用户明确全测优先、未测不改verified、明确接受才调整范围，以及不自动发布远端的边界。多来源入口只把可追溯REA研究作为既有成果，普通UI工程不自动入库，已有迁移示例附属于来源；原平台类别可以留空。验证说明同时区分原版独立试用与本次9组合成检查，不把文档同步称为真实产品回归。用户实际提供的Prompt仍优先，不能由skill悄悄缩小。

独立情境审读发现一处模式歧义：通用推进的原版运行、D切片和参考验收没有显式免除catalog/纯资料整理/仅评估。最终语义修订已在短入口、工作流、迁移指南、README及两份Prompt明确：这些模式只核对既有身份、索引引用与消费者依赖；除非用户另要求，不新增目标GUI运行或制作D，未验证事实保持原状。需要实施的用户请求仍不能自行降成文档。修订后的技能副本与补丁已同步并重新校验，未进行真实App回归。
