# Run03 原版录屏与独立帧审阅

两份视频都来自专用AVD中的原版Kimi和本轮自造聊天。这里是留存视频的离线解码/审核，未再次控制设备，也不是参考实现D的运行证明。视频、原始capture和逐帧PNG保持不变；contact sheet另存为派生图。

| 视频 | 已解码视频sample/帧 | 原始视频timebase | 首/末帧PTS | sample时间跨度 |
| --- | --- | --- | --- | --- |
| [concurrency-01](../evidence/runtime/run03-20261016/motion/concurrency-01/screenrecord.mp4) | 63 / 63 | 1/90000秒 | 0 / 1219890 ticks，即0 / 13.554333秒 | stts合计13.820144秒；mdhd声明13.820133秒，相差1 tick，分别保留 |
| [motion-01](../evidence/runtime/run03-20261016/motion/motion-01/screenrecord.mp4) | 28 / 28 | 1/90000秒 | 0 / 314381 ticks，即0 / 3.493122秒 | 3.667378秒 |

使用本机Chrome 155的WebCodecs H.264解码。MP4 sample table提供原始DTS/PTS整数ticks，帧索引同时保留timebase和用于解码器接口的四舍五入微秒值；没有按固定FPS重采样。全63/28帧分别经过逐格contact sheet审核，8/13张关键帧另看原尺寸；菜单视频的guard.png、全部guard.xml非空文本及两份capture/decoder metadata也单独复核。可见内容限于本轮Research Run03自造消息、数对/标识回答、模型菜单及通用Gboard控件，未发现身份问候、个人头像、QR码或私人历史抽屉。

## 并发录屏的实际覆盖

[帧索引](../evidence/runtime/run03-20261016/motion/concurrency-01/browser-decoded/frame-index.json)、[独立审核](../evidence/runtime/run03-20261016/motion/concurrency-01/review.json)、[全帧联系图1](../evidence/runtime/run03-20261016/motion/concurrency-01/review-sheets/sheet-1.png)、[2](../evidence/runtime/run03-20261016/motion/concurrency-01/review-sheets/sheet-2.png)、[3](../evidence/runtime/run03-20261016/motion/concurrency-01/review-sheets/sheet-3.png)。

| 帧 | PTS秒 | 实际可见内容 |
| --- | --- | --- |
| 1 | 0 | C旧回答已完成的482–500末尾与工具栏，输入器为空 |
| 18 | 4.471122 | C请求已重新进入生成形态，停止方块和“Send messages anytime…”可见 |
| 31 | 7.269233 | C仍为Thinking；D完整文本留在编辑器，向上发送箭头及软键盘可见 |
| 63 | 13.554333 | C为Thinking摘要；D仍在未提交编辑器中，发送箭头仍可见 |

视频支持“C生成时可以编辑并保留第二条D草稿、显示发送入口”。它没有可见覆盖D提交后的新气泡/输入器清空、Queue计数或checkedStop结果，不能据此判定第二请求已排队或暂停成功。原会话动作448–453的Retry→精确D发送→checkedStop另有host记录；其中submit-exact的startedAt包含UIA守卫，不能当作点击派发时刻。D点击只能由代码顺序约束在10:18:12.504–10:18:13.117 UTC之间；host时间、guest时钟、视频PTS未校准，不能把录像请求开始时间直接加到PTS上。

录屏请求18秒，wrapper跨度约65.718秒；二者都不是实际已编码媒体sample跨度，更不是动画时长。后续独立原图中的Queue或完成结果由其各自证据承担，不补入此视频的缺帧或缺失动作。

## 模型菜单录屏的实际覆盖

[帧索引](../evidence/runtime/run03-20261016/motion/motion-01/browser-decoded/frame-index.json)、[独立审核](../evidence/runtime/run03-20261016/motion/motion-01/review.json)、[全帧联系图1](../evidence/runtime/run03-20261016/motion/motion-01/review-sheets/sheet-1.png)、[2](../evidence/runtime/run03-20261016/motion/motion-01/review-sheets/sheet-2.png)。

| 帧 | PTS秒 | 实际可见内容 |
| --- | --- | --- |
| 10 | 0.918133 | 菜单尚未出现在采样帧内 |
| 11 | 1.121078 | 半透明菜单出现，可见K3/K2.8/Instant等条目 |
| 15 | 1.332122 | 菜单清晰，背景内容软化，Instant仍选中 |
| 21 | 2.048778 | 菜单仍清晰可见 |
| 22 | 2.284933 | 菜单进入半透明退出采样形态 |
| 23 | 2.463778 | 菜单已不在采样帧内 |
| 28 | 3.493122 | 同一自造聊天与Instant/High保留 |

28帧只分辨出一个可见开启→退出区间。host记录包含打开、外侧关闭、重开和快速外侧关闭，但没有分辨出两个独立可见循环或中途反转。快速关闭的host派发间隔目标为80毫秒，实际为1182.9249毫秒；这不是动画duration。未把稀疏帧间隔解读成准确起止，没有拟合或声称唯一easing、spring、触觉或声音参数。

## 精确字节交付范围

[run03-motion-safe-files.json](run03-motion-safe-files.json)列出两份视频及106项已审核原始/派生文件的逐文件路径、字节数和SHA-256。打包脚本只允许这些精确字节；新motion、修改过的文件、日志、HTML生成中间物和浏览器临时profile不自动进入交付。原capture里的pending备注保留采集时原文，后续独立审阅在review及allowlist中记录，不改写原始采集事实。

此MD索引与允许清单不意味着约定的全部功能或动效验收完成。准确animation duration/easing/spring和这次未覆盖的中断结论仍未测。
