# 来源与原生参考：四个局部状态对照

课程未选、选中、构建占位及返回清空已在独立原生参考中真实运行并与来源逐图核对。**部分颜色、位置和局部状态转换吻合，角色、字体外观、菜单入口、气泡与构建页排版仍有明确差异；这不是完整还原。** [并排查看原图](index.html) · [结构化测量](source-pairs.json) · [原生实现边界](../reference/README.md)

## 身份、环境与证据边界

来源是 `com.duolingo 6.80.6-china (2368)`，APK SHA-256 `ebdbcbdaa0d30f0141dbfc31953e650b28b5fd35b7faf7787751223661deaab8`。四张来源图采集于 **2026-10-15 15:23–15:25 UTC**，本次按 **historical A** 复用并在 2026-10-16 重新看像素、读取 UI 树与核验 PNG 哈希。原 `capture.json.origin=current` 表示当时采集，本次复核没有把它们改成新运行证据。

参考是 D 实现 `org.reastyle.nativeexample 1.0 (1)`，本组采集 APK SHA-256 `6e5e1e4882b4d5b19e91bdda0d89938f603b246d95c0c41db29a0163399953ca`。此后仅修正问候页气泡裁剪，最终交付d8ff版及其运行验收另见 reference-runtime.md（未分发的历史辅助资料），不能把本组旧构建原图改标为新APK。四张图采集于 **2026-10-16 04:24–04:26 UTC**，本次结论为 **current A-of-D**：证明独立参考实际显示和响应，不证明来源运行。参考后端是本地确定性 fixture、无网络；来源使用真实后端、独立游客状态。`r01-source-course` 是错页，按采集者标记排除。

两边均为 `duolingo_research_api31 / emulator-5580`、AOSP Android 12 API31 x86_64、无 Google Play Services，PNG `720×1280`、`320 dpi`，英文、浅色、竖屏，系统状态栏高 `48px`，应用可见区域至 `y=1184`。**来源 fontScale 始终是 null／未知；参考是 1.0，不能声称字体缩放已匹配。** 同一个 AVD 与若干相同 bounds 不能消除字体、目标 SDK、后端和采集时刻的差异。

PNG 与 `ui.xml` 分别采集，非原子快照；动画帧、滚动条和树的时间差都保留。以下坐标为原 PNG 的物理像素，UI bounds 使用 `[left,top][right,bottom]`，不混写为 dp。所有 PNG 保持原件；本页没有相似度百分比，也没有新 REA Evidence ID。

## 四对状态与交互

| 配对 | 来源 historical A | 参考 current A-of-D | 已验证范围 |
|---|---|---|---|
| 未选 | [05](../evidence/runtime/05-course-picker/screen.png) | [r02](../evidence/reference-runtime/r02-source-course-valid/screen.png) | Spanish/French/German 均未选，Continue `enabled=false`。仅由禁用属性与外观确认，未在此组测试点击禁用按钮。 |
| 选中 | [06](../evidence/runtime/06-course-selected/screen.png) | [r03](../evidence/reference-runtime/r03-source-selected/screen.png) | 同一实例点击 Spanish 后，卡片浅蓝面／蓝边／蓝字，Continue `enabled=true`；其余两项未选。 |
| 继续 | [07](../evidence/runtime/07-after-course-choice/screen.png) | [r04](../evidence/reference-runtime/r04-source-building/screen.png) | 点击启用的 Continue 后实际出现 `COURSE BUILDING…` 和课程说明。参考仅为可返回的占位状态；没有真实构建或自动成功。 |
| 系统返回 | [08](../evidence/runtime/08-course-return/screen.png) | [r05](../evidence/reference-runtime/r05-source-return-reset/screen.png) | 系统 Back 后回到课程列表，Spanish 选择清空且 Continue 再次禁用。没有证明请求取消、晚到结果、滚动位置保留或其他草稿规则。 |

历史来源 `07/08` 的 `action` 仍有采集前预期名称；`observedScene/inspectionNote`、原图与树已纠正为构建页／返回清空，本对照以实际画面为准。参考 r02→r03→r04→r05 是同一实例的连续操作。动作记录（未分发的历史辅助资料）

## 可复核的一致与偏差

| 项目 | 具体观察 |
|---|---|
| 位置吻合的局部 | 进度条 UI bounds 均为 `[96,112][688,144]`；主按钮 bounds 均为 `[32,1048][688,1152]`；底部动作区从 `y=1016` 起。相同位置仅证明这些几何锚吻合。 |
| 色值吻合的局部 | 固定坐标采样得到两边背景 `#fffffd`，进度轨道和禁用面 `#e5e5e5`，绿色面 `#58cc02`／lip `#58a700`，选中卡面 `#ddf4ff`／边 `#84d8ff`；文本实色还有 `#4b4b4b`／选中 `#1cb0f6`。采样位置与每图结果见 JSON，不推广为全图色彩一致。 |
| 角色与动效 | 来源课程页为抱书和铅笔的角色，05/06 的眼睛状态不同；参考是张开翅膀的固定正面姿态。来源构建帧为侧倾、有音符和地面线的角色；参考是闭眼举翅、有星点的静态图，尺寸与位置也不同。当前截图只能证明帧差异，不能测动画 duration、节奏、声音或触觉。 |
| 气泡 | 来源气泡容器和参考带气泡背景的 TextView 都报告 `[248,203][666,366]`，但不是同一种树节点。`y=320` 上，来源可见左边框为 `x=280..283`，参考为 `274..277`，气泡主体向左多出 `6px`；指向箭头形状也不同。文字仍是两行，但文字内边距与实色墨迹位置不同。 |
| 母语菜单入口 | 来源 `For English speakers` 右侧可见上箭头，树中标题 `clickable=true`；参考没有箭头，标题 `clickable=false`。参考没有重建这个母语／课程菜单入口，不能把列表单选通过扩展为该菜单可用。 |
| 字体外观 | 参考标题与课程名更宽，笔画外观也有差别。以 `#4b4b4b` 实色像素、不含抗锯齿边缘的包围框作诊断：来源 Spanish `x=180..305`，参考 `180..316`；来源 French `181..286`，参考 `181..295`；来源分区标题 `35..367`，参考 `35..400`。这不是 font family 或精确 weight 的测量，来源字体选择仍未知；参考主动选择 DIN 属于 D。 |
| 列表几何 | 来源分区标题 bounds `[32,409][688,534]`，参考 `[32,410][688,535]`；三张完整卡均向下偏 `1px`：Spanish 来源 `[32,534][688,662]`，参考 `[32,535][688,663]`，French/German 同样偏移。旗图 bounds 来源 Spanish `[64,564][146,628]`，参考 `[64,563][146,627]`，反而向上 `1px`。 |
| 卡片底边 | 在 `x=400` 的无文字列，来源 Spanish 下边为 `y=654..661`（8px），参考为 `653..662`（10px）；顶部边框均 4px，但参考从 `535` 起，来源从 `534` 起。未将单个采样列推广成圆角全周尺寸。 |
| 禁用按钮几何 | 尽管主按钮 UI bounds 相同，在 `x=500`，来源未选／返回的禁用面从 `y=1056` 起，参考从 `1048` 起，底端均至 `1151`。启用状态两边在该列均为绿色 `1048..1143`、深绿 lip `1144..1151`。 |
| Back 图形与语义 | 来源为带横杆的左箭头 ImageView，bounds `[16,96][80,160]`；参考为字符 `‹` 的 TextView，bounds `[8,80][88,176]`。两边均有 `content-desc=Back`，但形状、节点类型和报告的边界不同；树 bounds 不等于已测实际触摸命中区。 |
| 选择语义 | 来源选中卡为 `LinearLayout, selected=true, checkable=false`；参考暴露为 `RadioButton, selected=true, checked=true, checkable=true`。视觉状态接近不代表无障碍语义相同；本次未测试 TalkBack。 |
| 构建标题和正文 | 标题来源 bounds `[0,693][720,752]`，参考 `[64,653][656,712]`，参考向上 `40px` 且容器更窄。正文来源 `[64,816][656,992]`，参考 `[64,776][656,974]`，顶部向上 `40px`、高度多 `22px`。两边当前均为三行，断行同在 `million`／`learning` 后；来源 `20 million people` 局部加粗，参考没有相同加粗，字宽与行距不同。气泡当前也均为两行；这些短文案的断行吻合不能扩展为长字／大字布局通过。 |
| 返回帧差异 | r05 右侧出现灰色 ScrollView 滚动条，来源08原图没有；来源08进度填充比参考返回帧略长且有亮斑。两边帧时刻不同，不能从这些静帧反推完整动效时间或把滚动条认作持续存在。 |

## 尚未覆盖

本页比较四个英文浅色状态，未比较隐私、年龄、问候、十组件独立样本、Paperlane 跨业务流程、键盘输入、焦点、TalkBack、横屏、大字、中文或深色。没有覆盖完整首课、主页、账号与设置、错误恢复、网络中断、权限、真实课程构建、动画同步及声音触觉。十组件与跨业务验证应分别查看对应结果，不能由本页推定通过。

可交接结论是：**参考的课程选择→继续→构建占位→系统返回清空这一局部路径已运行，状态与若干 token／几何锚得到对照支持；完整视觉、交互入口和动态行为尚有上述差异。**
