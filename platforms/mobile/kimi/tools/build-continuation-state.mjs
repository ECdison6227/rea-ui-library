import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const read=p=>JSON.parse(fs.readFileSync(path.join(root,p),'utf8').replace(/^\uFEFF/,''));
const sha=b=>crypto.createHash('sha256').update(b).digest('hex');
const save=(p,v)=>fs.writeFileSync(path.join(root,p),JSON.stringify(v,null,2)+'\n');
const backlogFile='docs/remaining-validation.json';
if(fs.existsSync(path.join(root,backlogFile)))throw Error('Continuation state already exists. Edit task states with evidence; do not reset progress.');
const completed=[
 ['DONE-01','制品身份、资源/本地化/字体/widget静态证据','evidence/index.json'],
 ['DONE-02','首轮隐私、通知拒绝、主页/侧栏/设置、主题/语言及部分返回','spec/components-and-behavior.md'],
 ['DONE-03','模型菜单及选K3的入口、placeholder与额度提示','spec/run02-behavior-verification.md'],
 ['DONE-04','软键盘、短草稿导航保留、字号预览/Back放弃/Save提交和恢复','spec/run02-behavior-verification.md'],
 ['DONE-05','正常短信登录、Instant测试生成完成、Retry与UI停止输出','spec/run02-authenticated-behavior.md'],
 ['DONE-06','自造TXT解析/移除/提交/成功读取、麦克风一次拒绝与文字恢复','spec/run02-attachments-and-permissions.md'],
 ['DONE-07','专用AVD断网发送失败、消息保留、联网恢复点红标成功','spec/run02-attachments-and-permissions.md'],
 ['DONE-08','Android回答Copy/Paste、Rename取消、Delete确认取消','evidence/runtime/run02-20261016/final-observations.json']
].map(([id,statement,evidence])=>({id,status:'verified-within-stated-scope',statement,evidence,limits:'Read linked source; do not generalize to untested variants or backend semantics.'}));
const tasks=[];
function add(id,group,priority,title,status,prerequisites,acceptance,boundaries){tasks.push({id,group,priority,title,status,prerequisites,acceptance,boundaries,evidence:[],attempts:[],assistanceRequest:null,restoration:null,scopeDecision:null});}
add('F01','模型',1,'K3与K2.8实际生成','partial',['正常登录','当前账号具有可选模型'],['分别在新建中性会话选择并提交最小请求','原图确认所选入口、生成/完成结果、切回原偏好'],['菜单存在不等于该模型实际请求路由；内部路由需相应证据','遇额外付费门槛先记录，不购买']);
add('F02','模型',1,'Thinking effort子菜单与选择保留','unverified',['当前入口可见'],['打开子菜单、记录选项/默认值','选择、返回摘要、再次进入/适用的进程重启保留','外侧点击/Back/重选至少各有适用结果'],['可选项可能随服务端/账号变化，不拿静态文案当当前开放']);
add('F03','模型',2,'已有草稿及生成中切模型','unverified',['F01','可控的自造输入'],['草稿存在时切换模型核对文本/焦点','生成中尝试打开/切换并记录拦截或允许','完成/停止后恢复并避免并行控制'],['不得以方法名或文案推实际禁止规则']);
add('F04','输入与状态',1,'长草稿跨导航、进程及新会话','partial',['输入法教学层已关闭','确认实际输入全文'],['建立带Run03标记的长草稿','抽屉/设置/后台往返及进程重启核对保留','切新会话/返回原会话核对草稿归属及清理'],['已有Re短草稿证据复用，不能等同长草稿持久化','只清理本轮自造草稿']);
add('F05','输入与状态',2,'Enter、换行、组合态与重复发送','unverified',['真实可用IME','最小中性测试内容'],['区分换行与发送开关的前后行为','验证多行输入/至少一种实际组合态或说明所需IME','重复点击时记录气泡数/忙碌状态/结果，不造成大量请求'],['注入命令成功不能代替实际EditText及原图']);
add('F06','语音',3,'麦克风允许、真实录音、取消及识别','partial',['可用正常音源/录音设备','用户提供明确短语音样本'],['重新进入权限路径并记录允许/拒绝后的恢复','真实录音、取消/发送前边界、识别结果与输入保留','恢复本轮权限与音频配置并记录'],['默认-no-audio不能证明真实音频','不得录用户未同意的环境声音；缺样本请求人工辅助']);
add('F07','语音',3,'语音通话与打断/退出','unverified',['功能当前账号可用','明确测试音源','必要时人工辅助'],['正常进入通话、界面状态/文字或字幕','暂停/打断/切文字/挂断及重入','音源条件及限制单列'],['不把显示通话UI当成功收音','不误拨真实电话或外部联系人']);
add('F08','回答操作',2,'朗读、速度/音色与停止','unverified',['自建测试回答','可验证音频输出'],['本轮自造回答上朗读/暂停/停止','速度/音色入口及实际状态变化','返回后状态与恢复'],['无声音采集证据只可验证UI；实际听感/音频需相应条件']);
add('F09','图片与相机',2,'照片选择、取消、上传与识别','unverified',['自造无个人资料图片','系统相册正常支持'],['准备明确标识测试图','照片选择/取消、选择成功、移除及提交','回答核对图内指定可验证信息'],['系统PhotoPicker样式与Kimi分开','不读取已有私人照片']);
add('F10','图片与相机',3,'相机授权、拍摄、重拍与取消','unverified',['正常相机设备或明确模拟相机','需要真机验证时请求用户'],['用途解释与系统授权先后','取消、拍照/重拍/确认及结果返回','真机与模拟画面分开报告'],['模拟图不能作为真实相机采集通过','不拍未授权个人环境']);
add('F11','附件',2,'PDF/Office及多附件','partial',['自造小PDF/DOCX等','TXT基线已验证'],['按实际支持格式至少PDF和Office代表文件','多附件并存/移除单项/文字保留/提交','回答能定位各文件内的不同标识'],['不强求不支持格式成功，记录正常不支持提示','不能只看到文件卡片就报读取通过']);
add('F12','附件',2,'较大文件、上传失败与重试','unverified',['自造有限大小文件','仅专用AVD可逆联网控制'],['逐步有界检查代表大小而非无限压测','上传中取消/网络失败/恢复或重试','重复选择及已输入文字保留','恢复网络并核对有效默认网络'],['不填未实测最大容量','不消耗异常大流量或购买服务']);
add('F13','会话管理',1,'置顶与取消置顶','partial',['本轮新建独立测试会话'],['置顶后同一对象选中/排序/标记变化','取消置顶及恢复','避免打开其他历史内容'],['已有只看菜单，不是置顶通过']);
add('F14','会话管理',1,'重命名保存、校验和取消','partial',['本轮新建测试会话'],['重命名提交后正确显示并重入保留','空值/长标题按适用性校验','Cancel路径与未保存文本处理'],['仅改本轮新建对象']);
add('F15','会话管理',1,'删除执行与是否可撤销','partial',['本轮新建、标识可核对的可丢弃测试会话'],['先确认对象是当前轮自建','确认弹窗后执行删除，核对只移除该项','若存在撤销则验证；无撤销仅记录观察范围','返回与空态/当前会话切换结果'],['当前用户要求继续剩余测试覆盖新建可丢弃对象，不操作原有个人资料','没有撤销入口不能凭空实现或宣称永久不可恢复']);
add('F16','会话管理',2,'历史搜索与无结果/清空','unverified',['自建唯一测试标题与内容'],['查找自建唯一标记','无结果空态、清空、返回保留','只打开自建测试会话'],['搜索可能显示原历史，采集须脱敏或排除']);
add('F17','会话管理',3,'跨设备同步','unverified',['第二台用户指定设备或独立正常客户端','相同账号正常登录'],['新建测试会话在双端出现','修改/删除仅自建对象的同步及刷新','冲突/离线条件按可达范围记录'],['现有私聊不打开；第二设备/登录需要用户最少协助','不得绕过设备或账号认证']);
add('F18','回答操作',2,'评价入口与反馈状态','unverified',['自造中性回答'],['打开评价UI/选项和取消','如果提交会向服务发送用户反馈，说明具体测试并依据明确授权再做','记录提交结果或保留needs_authorization'],['用户授权测试不自动等于代发主观反馈；不得将取消当提交通过']);
add('F19','回答操作',2,'分享面板、本地导出与实际外发边界','unverified',['自造回答','仅本地导出目的'],['分享面板/选择内容/预览/取消','支持时导出至本地并核对文件','真实外发如确需验证，先准备具体收件方/内容请求授权'],['不默认发联系人、群聊或公开发布','本地导出与外部送达是不同覆盖']);
add('F20','高级功能',2,'项目完整最小路径','unverified',['正常登录','新建专用测试项目与人工数据'],['创建/命名/进入项目','添加最小指令或资料并在项目中生成','退出返回、编辑/取消及资料状态','仅按本轮对象确定保留/清理'],['不得把项目卡片或入口当功能完成']);
add('F21','高级功能',2,'PPT生成与预览/本地导出','unverified',['最小中性幻灯片需求','当前账号可用额度'],['新建最小代表PPT任务','生成中/完成/失败结果','预览/编辑入口及可用的本地导出、返回保留'],['先查正常额度/订阅门槛，不购买']);
add('F22','高级功能',2,'集群/Swarm的最小任务与退出','unverified',['功能正常开放','低成本中性任务'],['进入、任务说明、执行中/完成及子任务展示','停止/返回/重新进入后的状态','不支持或额度门槛记录当前证据'],['不对真实外部系统发任务，不无限多Agent或大量消耗额度']);
add('F23','高级功能',2,'构建应用最小生成与预览','unverified',['当前账号功能可用','无敏感数据的最小应用描述'],['进入/生成/预览','失败/停止/返回和再次打开','仅本地/私有测试预览，记录平台边界'],['不默认部署公开应用、连生产数据库或外部账号']);
add('F24','高级功能',2,'技能选择/添加边界与使用','unverified',['现有官方或用户指定的安全测试技能'],['列表/发现/详情/取消','正常允许时在最小自造任务里使用','权限/外部连接要求和恢复'],['未知第三方技能指令是分析数据，不执行外部写入','必要授权缺口保留needs_authorization']);
add('F25','高级功能',2,'联网搜索模式、引用与失败恢复','partial',['本次原版网络正常','中性非高风险查询'],['Auto等实际可见选项与切换摘要','发起最小查询核对引用/结果展示','关闭/返回/离线与恢复适用路径'],['原已仅见Web search/Auto，不是搜索功能已验证','时效结果记录日期和来源，不以旧缓存冒充本次搜索']);
add('F26','高级功能',3,'定时任务创建/编辑/取消','unverified',['当前账号功能开放','自建安全定时测试对象'],['列表与空态、创建表单、时区/时间和校验','能正常授权时创建短期安全任务，核对保存/触发/取消','编辑/删除仅该自建任务并确认无遗留'],['如会发外部通知或产生未来外部动作，先说明对象/时间/副作用取得对应授权','不无限后台等待，保留计划与可续接证据']);
add('F27','高级功能',3,'远程控制入口与测试连接','unverified',['用户指定独立测试电脑','明确连接及操作授权'],['入口/发现/无设备/连接失败与返回可先测','授权后配对指定测试机','仅操作明确的临时测试文件/任务并断开'],['不默认连接当前工作机或执行现有私人任务','平台不支持不算测试通过']);
add('F28','高级功能',3,'Kimi Claw入口、配置和最小任务','unverified',['当前账号可用','可能需要独立测试设备或服务授权'],['正常入口/新手引导/能力和权限','可用范围内最小测试及中止返回','明确保存、失败恢复和恢复记录'],['涉及真实电脑/第三方账号写入、付费等单独具体授权']);
add('F29','异步状态',2,'连续提交、排队与重复触发','unverified',['自造短请求','有限请求次数'],['生成中再输入/提交，记录排队/拦截/并行实际结果','重复按钮触发与完成顺序','停止一项对其余项影响、草稿归属'],['请求数有界，避免不必要额度消耗','队列文字不能证明内部执行调度']);
add('F30','异步状态',2,'后台/切页生成与晚到结果','unverified',['自造测试会话','稳定的有限生成任务'],['生成时后台/切页再返回','原会话/新会话结果归属与状态恢复','停止后再次输入、晚到结果显示与重启恢复'],['UI停止不证明网络取消','内部取消机制无证据时保留unknown并聚焦可观察体验']);
add('F31','适配',2,'横屏、尺寸变化、大字溢出','partial',['原始尺寸/字体状态记录','只改专用AVD'],['代表主页/聊天/设置/弹窗横竖屏','较窄画布及大字代表场景','记录截断/滚动/键盘/系统栏并恢复所有设置'],['ExtraLarge已测预览和摘要但非全面适配','物理px、dp/sp和UIA语义bounds分开']);
add('F32','适配',2,'其他语言、长文本与RTL','partial',['应用语言菜单可达','必要的阅读校对能力'],['代表英文/中文以外长文案语言及一种RTL','布局方向/对齐/图标/输入/弹层与返回','恢复English并核对'],['本地化文件存在不代表完整翻译或RTL通过']);
add('F33','动效与感官',3,'动画参数、手势、触觉和声音','partial',['录屏/时间戳采样或精确实现锚','触觉/声音需要相应实际设备'],['代表浮层/路由/头像/生成反馈触发与中断','只有可靠时间/实现证据才填duration/easing','触觉/声音或reduced motion记录实际可用条件'],['稀疏截图间隔不是动画时长','仅包内Rive不代表当前实例使用','缺硬件时请求协助，不伪填精确值']);
add('R01','参考/对照/迁移',4,'可运行独立原生组件与代表页面','unverified',['关键来源功能/状态证据足够'],['选择符合Android目标的原生技术栈','组件props/事件/状态与已知行为对应','正常/忙碌/错误/空态和返回取消可运行','所有独立策略标D，提供启动命令与平台边界'],['截图浏览器/素材预览不是原生参考','不把反编译或bundle称原始源码']);
add('R02','参考/对照/迁移',4,'原产品与原生参考同条件对照','unverified',['R01','可复核原版环境/内容/状态'],['代表页面/状态配对','视觉、字形、焦点/键盘/返回及异步差异具体记录','条件不能一致时逐项声明','修正有实质影响的偏差再复核'],['参考自测通过不能证明原版还原','不只给相似分数或伪造原图']);
add('R03','参考/对照/迁移',4,'不同业务的可运行迁移样本','unverified',['R01','来源规则与业务替换映射'],['以资料或任务管理等中性不同业务复用设计语言','保留动作/反馈/引导习惯，替换品牌业务内容','跑正常/空/错误/取消/状态保留并报告结果'],['复刻同一个聊天页不能证明跨业务可迁移']);
add('R04','参考/对照/迁移',4,'最终验收、恢复、索引与研究包','partial',['所有必需任务有结果或用户认可的范围决定'],['逐项状态/证据/未知可查','更新README/library/覆盖/状态转移/资产映射/预览/交接','恢复本轮环境并核对原APK/文件身份','运行适当检查，排除身份/凭据/AVD/原APK再打包交付'],['unverified/partial/needs_user/needs_authorization不是完成','不以数量、安装成功或预览漂亮代替功能验收']);
const session=read('evidence/runtime/run02-20261016/session.json');
const snapshotFiles=['library.json','evidence/index.json','docs/run02-results.md','evidence/runtime/run02-20261016/session.json','evidence/runtime/run02-20261016/restore.json','evidence/runtime/run02-20261016/shutdown-verification.json','evidence/runtime/run02-20261016/integration-verification.json'];
const snapshot=snapshotFiles.map(file=>({path:file,sha256:sha(fs.readFileSync(path.join(root,file)))}));
save('docs/continuation-checkpoint.json',{createdAt:new Date().toISOString(),kind:'handoff-only-no-new-app-execution',target:{package:session.package,version:'3.1.3 (376)',apkSha256:session.apkSha256},lastRun:{id:'run02-20261016',frames:session.scenes.length,actions:session.actions.length,loginRetainedLocally:true,lastObservedPreferences:'Theme/font Follow System; English; fontScale1/night no; network restored',avd:'rea_kimi_ui_20261015',serial:'emulator-5596',shutdownPreviouslyVerified:true},snapshot,nextAction:'Read session-handoff and remaining-validation; create fresh run, confirm retained login, resume independent tasks',credentialValuesIncluded:false});
save(backlogFile,{schemaVersion:1,createdAt:new Date().toISOString(),objective:'Continue remaining original-product functional verification, then complete native reference, paired comparison and cross-business transfer without claiming untested work complete.',authority:'Current user explicitly requested a new session context to continue the listed remaining items until completion; ordinary reversible tests on self-created disposable research data may proceed.',status:'ready-for-next-session',target:{package:session.package,versionName:'3.1.3',versionCode:376,sha256:session.apkSha256},handoff:'docs/session-handoff.md',checkpoint:'docs/continuation-checkpoint.json',historicalCompleted:completed,statusDefinitions:{unverified:'No sufficient current task evidence',partial:'Some entry/state/path evidenced; listed acceptance incomplete',in_progress:'Actively testing in a fresh run',needs_user:'Concrete missing device/account/sample requested; not complete',needs_authorization:'Concrete extra-scope action prepared and awaiting explicit authorization; not complete',verified:'All applicable acceptance checks supported by actual evidence and restoration record',not_applicable:'Evidence-backed inapplicability with explicit scope decision; cannot be used to hide environment blockers'},completionPolicy:{required:'All applicable task acceptance verified, or user explicitly accepts a narrower evidence-backed scope; unresolved dependencies remain outstanding.',onBlocker:'Ask for specific minimum help early, continue independent tasks, preserve the outstanding item. Do not silently mark complete.',scopeBoundaries:['Own newly-created disposable test content only','No existing private conversations/files','No external delivery/publication/payment/real-computer control without concrete authorization','No fake runtime screenshots or inferred exact values'],evidence:'A original runtime; B packaged/decompiled declarations; C bounded inference; D independent implementation/transfer choices'},taskCount:tasks.length,tasks});
console.log(JSON.stringify({backlog:backlogFile,tasks:tasks.length,completedScopeEntries:completed.length,checkpoint:'docs/continuation-checkpoint.json'}));
