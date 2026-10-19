# Run03 本地与交付验证模式

这些检查验证留存字节、身份、声明与分发边界，不执行App，不接触ADB，也不把文件数量当作功能验收。

```powershell
# 研究主机：全部留存源场景与原APK都必须在本地，原要求保持严格。
node tools/verify-run03.mjs --check

# 解压后的 kimi-android 目录：只要求精确审核允许的交付字节。
node tools/verify-run03.mjs --distributed --check

# 发布前在研究主机模拟同样的消费者文件树，不建立ZIP。
node tools/check-run03-distributed-projection.mjs

# 有实际ZIP后，校验ZIP路径及成员解压后的A/D/motion精确SHA。
node tools/verify-run03.mjs --distributed --check --archive C:/path/to/research.zip
```

脚本从自身位置定位研究目录，工作目录可以不同。消费者不需要未随包提供的根Kimi.apk，也不需要被整套排除的private/incomplete原图。`--distributed`明确只核对原包的既定版本、包名与hash声明，不声称重新读取原APK字节；独立D参考APK仍须真实存在并验证SHA。

原版session中仍保留被排除场景的路径、hash和动作边界作为来源记录。这些引用不等于交付了对应原图。只有`run03-safe-scenes.json`中明确withheld/pending的遗漏才可解释；新出现却没有任何遗漏声明的源场景会使distributed预检失败，不能默默跳过。全部允许A场景必须具备三件套且精确hash匹配；D的公开集合使用其独立safe list，原capture中的D身份、非原子、装包hash、时间序、PNG/UIA检查保持严格。

`run03-distribution-omissions.json`另外登记已被打包策略排除、但任务仍引用的本地临时辅助XML。它只声明未分发的文件路径/SHA/原因，不是source审核清单，也不把该文件变成公开原图证据。local仍要求它的留存字节；distributed报告它是local-only而不读它。其余未知缺失文件继续失败。

`withheld`统一表示不分发，细分为private、incomplete和其他原因。缺失UIA的253可以是incomplete而不含身份；摘要不再把所有withheld都称为private。

在完整本地工作区使用`--distributed --check`时，未分发原件可以仍然存在，报告会明确`looseWorkspaceContainsExcludedRawFiles=true`，但不读取它们或把它们当成公开证据。实际ZIP分发安全必须另用`--archive`：此模式逐一检查成员local/central路径、存储或deflate解压后的内容hash，要求全部精确A/D/motion/构建声明存在，并拒绝私有、不完整、未审、未知motion、原APK、AVD、密钥和构建中间件。

projection预检在工作区`.runtime`下新建隔离的消费者形状目录，只复制元数据和允许文件，明确不放原APK及排除场景，运行只读消费者验证后校验目标路径并删除该临时目录。它不改变原图、不修改审核sidecar、不生成归档。scopeComplete及实际验收仍取决于原清单和独立审核；这些命令不会替用户销项。

最终恢复使用[恢复与关闭契约](run03-final-restoration.md)。`--require-restored`会额外交叉核对专用AVD身份、显示/动画/IME、网络、三个权限完整flags、App偏好证据、独立final APK证明和关闭记录；初始system locale key存在性未知的限制必须保留。`node tools/check-run03-restoration-contract.mjs`只运行合成正反例和不可覆盖检查，不接触设备或写入研究证据。
