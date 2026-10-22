# 研究续接与环境

2026-10-22开始，Hark Android1.0.0-36。原包不可变；ARM64与用户ARMv7的base相同，无需重复全包分析。工具REA6.5.0/JADX-headless0.7.1/JADXCLI1.5.6/Apktool3.0.3/build-tools35.0.0/ADB37.0.1。

完成身份、split/签名、静态角色/局部控件、独立参考与标准包。公开回执删原代码/私有路径/全量manifest；不改原件，不把投影称canonical Evidence。

实际运行路径：现有Android35 Google APIs AVD以-read-only启动，原签名ARM64split成功安装，Hark在登录前显示需要Google Play的错误。未补丁或绕过授权。随后复用SDK/JDK/emulator安装Google官方Android35 google_apis_playstore;x86_64镜像，独立创建rea_hark_play_20261022。Play商店包存在，窗口已显示Sign in；market详情目标com.hark.android。

Google/Hark由用户本人正常登录，不从聊天索取凭据或复制token。登录入口已通知主代理请用户操作，等待不视为拒绝；完成后从Play正常安装并观察代表界面。原只读AVD已结束；新AVD/原包/账号状态/SDK缓存保留本地私有目录，不进入Git。只有本任务模拟器由研究代理控制。

待补chat/composer/代表卡片/settings-theme/sheet-navigation/IME-fontScale/关键动效。每个新场景说明消除哪条设计歧义；全部支付/连接器/后台服务不作设计门槛。截图与树各自保留时间，分次采集不称原子快照；A必须实际原版，参考仍D。系统门槛截图不为核心设计增加verified。登录后优先新建中性内容并审查分发。

最终运行结果（2026-10-22）：Google账号密码正常登录、本人手机确认均完成，用户对当时Google/Google Play条款确认后代点I agree；未修改账号恢复信息，可选设备备份关闭。官方Hark详情明确“This item is not available in your country”（原界面用缩写isn’t），当前账号地区无Install。相同原签名ARM64本地安装成功，REA确认版本1.0.0-36/100000036，正常启动后Google Play显示“Get this app from Play”授权门。没有改包、伪造安装来源、改账号地区或绕过授权。此为具体原厂/账号可用性障碍，继续条件是可正常从Play取得Hark的设备/账号，再进行本人Hark登录。原生核心partial；唯一新A仅为[启动环境诊断](evidence/android-play-licensing-gate.png)，不提高核心设计覆盖。一次性私有凭据文件已删除，没有凭据或账号截图进入库。
