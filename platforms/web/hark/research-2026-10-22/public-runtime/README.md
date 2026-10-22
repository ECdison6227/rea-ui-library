# 当前公开站运行补证

[index.json](index.json)保留真实捕获区间、各图viewport/DPR/主题/语言/状态、SHA和逐图审阅状态。16张前台/套餐/FAQ图已由root实际看像素；8张后台诊断图仍pending，不拿后台冻结样本拟合动效。

| 问题 | 实际结果 | 原件 |
| --- | --- | --- |
| 3D轮播键盘 | surface是可聚焦group，左右箭头切换；正常前台0→1→0；中断反向后回0 | home-foreground-receipt.json、timelines/foreground-* |
| 窄屏/减少动画 | 390×844/DPR1正常0→1；reduce时1→2，card0只采到两种矩阵，视频paused/time0 | foreground-carousel-narrow-*.png 与对应时序 |
| reduced idle | 约6.47s保持中心2与固定矩阵；仍聚焦surface，不能单独归因reduce | timelines/foreground-reduced-idle.json |
| 原版字体 | intro为GTStandard-LMedium/83glyph；代表feature/prose标题为HarkSerif1-Light；prompt为GTStandard-LRegular/95glyph | captures/foreground-*-intro.json、foreground-carousel-desktop-before.json的platformFonts |
| 窄屏套餐 | 默认pro²，可切Free/pro²/pro³；键盘激活有可见焦点环；未购买 | pricing-narrow-receipt.json、captures/pricing-narrow-* |
| FAQ | 实际点击false→true→false，答案展开/收起，窄屏像素已看 | faq-restore-receipt.json、captures/faq-narrow-* |

正常前台轮播的中心视频readyState4并播放；这不等于全站每个媒体就绪。下方外部YouTube出现登录/机器人验证提示，原样保留限制，没有登录或绕过。前一轮后台rAF冻结/视频ready0与改条件后的前台运行分开，采样不是FPS或唯一spring拟合。

真实REA记录：[passive](rea/passive-home.json)（ev_dd68162ecae1b69b5d1664ce8a85b764afdfa3911048d14b06533c224a261c68）；[键盘scenario](rea/scenario-home-keyboard.json)（ev_b77b8c6d7155252d3f18191d67a2fd34e3690741de0cf25e0cfba0da89ca362f）。初次宽泛选择器失败也保留原收据。独立CDP实验有本地ID，不伪造REA ID。

最后清除了视口/媒体覆盖并返回首页，当前实际窗口1440×646/DPR2、reduced-motion=false；保留专用Chrome进程。该实际窗口尺寸不冒充早先1440×702，也不影响另一个正常用户Chrome会话的恢复记录。
