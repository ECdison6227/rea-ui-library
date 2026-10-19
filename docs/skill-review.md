# REA 设计提取与迁移：技能与标准交付

用户于2026-10-19明确了目标：保存优秀软件的视觉元素和完整设计语言，今后在自己的软件完成功能及普通UI后，选择GitHub里的规范并实施对齐。原业务底层功能不需要深度克隆。

## 当前技能

调用名为 **rea-design-transfer**，展示名“REA 设计提取与迁移”。技能源保留在[ECdison6227/rea-style-library](https://github.com/ECdison6227/rea-style-library)；本库保存[版本锁定副本](../skills/rea-design-transfer/SKILL.md)，同步元数据在skills/source.lock.json。原rea-style-library名称停止作为当前安装目录，旧调用需要迁移；不要同时安装两个会自动触发的副本。

此前草案的三条状态轴、阶段停止条件、有界依赖复试、早期参考配对、真实REA入库及原件/分发边界继续保留。[初始补丁](history/skill-upgrade-initial.patch)仅作历史，不是当前安装方式。

## 重点调整

- 先提取主要排版角色、尺度/留白、表面/形状/图标、组件与状态、布局组合和关键动效，再补足以实施的通用交互与引导。
- 每个新增测试说明新增哪条设计信息；同类样本充分后处理核心缺口，不默认穷举所有原业务功能。
- 组件参考与一个小型跨领域切片用于证明可复用；不再要求重建原业务产品，已有成果继续复用。
- coverage、readiness和约定交付独立。未知内部字体/协议不自动阻止有明确D替代和对照的实施；真正核心设计缺口仍保留partial。
- 新研究使用Design Package v1；旧研究显式适配，不覆盖原件或改写实际状态。

## 固定消费者入口

[design-package.json协议](../skills/rea-design-transfer/references/design-package.md)配合DESIGN_SPEC.md，定位tokens、组件、布局、交互、动效、资产、证据以及参考/对照/迁移指南。格式工具可初始化draft、做只读契约/哈希检查和导出待登记profile。

目录“复制迁移任务”使用新skill名，携带来源仓库、ref、目录、设计入口与边界。读取main后由消费Agent记录实际commit；旧来源继续按原README/spec进入，普通UI工程和空模板不会自动入REA总库。

## 验证边界

技能源的合成测试覆盖三平台初始化、不覆盖用户文件、A/B/C/D、真实字节、路径、组件/布局引用、readiness与D适配、动效未知和catalog入口。独立前向试用实际用有限历史材料完成了partial包，并发现四处契约漏检；修复后原partial仍能使用，负例按预期被拒绝。

格式/引用和合成测试不认证作者声明，不证明所有平台都能自动逆向或原版动效已实测。本次未重新运行Hark、小米、Mirasim、Kimi或其他原产品，未修改platforms中的原件/研究coverage，未新增来源计数。

原始私有试用材料不进入本公共目录构建；具体发布提交、本机安装和测试结果由实际完成记录与skills/source.lock.json核对，不把文档中的命令当已执行。
