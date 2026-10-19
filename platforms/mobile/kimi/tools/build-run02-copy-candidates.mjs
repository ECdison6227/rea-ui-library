/** Focused read-only analysis of existing CVR and DEX-derived JSON; no APK reparse or device use. */
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const hash = value => crypto.createHash('sha256').update(value).digest('hex');
const read = file => JSON.parse(fs.readFileSync(path.join(root, file), 'utf8'));
const sourceApkSha256 = read('assets/derived/copy/index.json').sourceApkSha256;
const locales = Object.fromEntries(['default', 'zh'].map(locale => [locale, read(`assets/derived/copy/${locale}.json`)]));
const originalCache = new Map();
function copyEntry(locale, key) {
  const entry = locales[locale].entries.find(e => e.key === key);
  if (!entry || entry.kind !== 'string') throw new Error(`Missing ordinary string ${locale}:${key}`);
  if (!originalCache.has(entry.sourcePath)) {
    const bytes = fs.readFileSync(path.join(root, entry.sourcePath));
    originalCache.set(entry.sourcePath, { sha256: hash(bytes), lines: bytes.toString('utf8').split(/\r?\n/) });
  }
  const original = originalCache.get(entry.sourcePath);
  if (original.sha256 !== entry.sourceFileSha256 || original.lines[entry.sourceLine - 1] !== entry.rawSourceLine || hash(entry.rawSourceLine) !== entry.sourceLineSha256Utf8) throw new Error(`Source mismatch ${locale}:${key}`);
  if (Buffer.from(entry.text, 'utf8').toString('base64') !== entry.rawSourceLine.split('|')[2]) throw new Error(`Roundtrip mismatch ${locale}:${key}`);
  return { locale, key, text: entry.text, placeholders: entry.placeholders, sourcePath: entry.sourcePath, sourceLine: entry.sourceLine, sourceFileSha256: entry.sourceFileSha256, rawSourceLine: entry.rawSourceLine, sourceLineSha256Utf8: entry.sourceLineSha256Utf8, decodedTextSha256Utf8: entry.sha256Utf8, evidenceLevel: 'B', sameTextOtherKeysInThisLocale: locales[locale].entries.filter(e => e.key !== key && e.text === entry.text).map(e => ({ key: e.key, sourceLine: e.sourceLine })), occurrenceConditions: 'unknown; run02 candidate only, no screen/control/trigger binding established' };
}
const definitions = [
  ['model', 'model_select_previous_model', '候选模型选择层是否提供过往模型；未登录与离线时仍否可打开和关闭？'],
  ['model', 'model_select_thinking_level', '是否有思考等级控件；打开/重选/返回之后的值由谁保存？'],
  ['model', 'model_select_new_chat', '切换是否只影响当前会话，或要求新会话；输入草稿是否保留？'],
  ['model', 'queue_model_switch_unavailable', '什么状态下切换被拦截；中文指定会话进行中，英文只说不可用，实际前置条件待测。'],
  ['draft', 'chat_text_field_hint', '以不含隐私的短文本填入普通输入框，开关抽屉/设置/模型层和返回后检查保留；此资源只证明占位文字。'],
  ['draft', 'create_new_chat', '建立新会话入口与返回旧会话是否分别保留草稿；未发送文本不要误记为已发送消息。'],
  ['draft', 'already_at_new_chat', '空白新会话再次点击新建是否提示；有草稿的新会话重复操作是否覆盖草稿？'],
  ['font', 'font_setting', '字体大小入口的实际标签、页面与导航层级是什么；与 setting_font_size_title 的相同英文避免单键归因。'],
  ['font', 'font_display_hint2', '原文要求点击右上 Save；拖动后只返回/关闭，与明确保存相比是否影响聊天字号？'],
  ['font', 'font_follow_system_switch_hint', '跟随系统开关是否禁用滑块；系统字号为1.0时不能据外观相同判定设置无效。'],
  ['font', 'save', '明确保存后是否退出、提示、持久化；该英文有多个同文键，不能凭标签唯一绑定 save。'],
  ['font', 'cancel', '若出现取消，仅在真实入口存在时测试；没有取消按钮时记录系统返回/导航返回，不假造取消控件。'],
  ['font', 'setting_chat_toast_saved', '是否出现 Settings saved 提示；记录出现位置和可见时间，不由 toast 键名推定组件类型或时长。'],
  ['voice', 'description_input_mode_voice', '语音入口是切换模式还是立即录音；它与登录门槛和麦克风请求哪个先触发？'],
  ['voice', 'permission_popup_audio_title', '首次进入是否先出现产品说明，再弹 Android 系统请求；已拒绝时再次进入怎样？'],
  ['voice', 'permission_popup_audio_content', '用途与拒绝后果是否实际出现；拒绝一次后的文字、输入模式和已有草稿如何变化？'],
  ['voice', 'chat_voice_hint_release_to_send', '出现松开发送提示的实际手势条件；没有执行发送时仅观察入口及取消，勿推定提交成功。'],
  ['voice', 'chat_voice_cancel_input', '是否有取消输入入口，取消后是否回到原模式并保留之前的文字草稿？'],
  ['voice', 'chat_voice_hint_edit_text', '是否在转写后可编辑；未观察到转写结果则保留未验证，不强行扩展到语音通话。'],
  ['offline', 'chat_error_screen_title', '离线时哪个页面显示加载失败；是否有独立重试按钮及保留内容，而非猜测全屏错误容器？'],
  ['offline', 'connection_error_and_retry', '连接错误能否点击重试；重试忙碌、重复点击、恢复网络后的结果和输入保留如何？'],
  ['offline', 'network_unavailable', '断网时显示此文案还是其他本地/服务端错误；不要把所有网络失败统一归到同一键。'],
  ['offline', 'network_restored_and_normal', '恢复联网是否自动消除错误或仍需刷新；是否实际出现恢复提示而非仅状态恢复？'],
  ['login', 'please_to_login_and_chat_with_kimi', '哪些入口在未登录状态被阻止；打开模型/输入文字/发送/语音分别检查，不从此文字推成全局门槛。'],
  ['login', 'login_btn', '观察真实登录入口和可关闭方式；不输入用户手机号/验证码，不调用真实账号完成流程。'],
  ['login', 'auth_error_unlogin', '被拦截时出现产品登录层、toast还是返回错误；该通用鉴权文本不证明聊天发送路径使用它。'],
  ['login', 'auth_error_user_cancelled_login', '取消登录/返回后恢复到原场景还是首页；文字、草稿、已选模型和键盘状态是否保留？'],
];
const candidates = definitions.map(([topic, key, question], i) => ({ id: `RUN02-COPY-${String(i + 1).padStart(2, '0')}`, topic, key, selectionClassification: 'C', runtimeStatus: 'pending; candidate not proof of observation', question, resources: { default: copyEntry('default', key), zh: copyEntry('zh', key) } }));
const dexFiles = ['classes', 'classes2', 'classes3'];
const inventories = Object.fromEntries(dexFiles.map(prefix => [prefix, read(`evidence/source/${prefix}-class-inventory.json`)]));
const stringPoolFiles = Object.fromEntries(dexFiles.map(prefix => {
  const file = `evidence/source/${prefix}-string-candidates.json`;
  const raw = fs.readFileSync(path.join(root, file), 'utf8');
  return [prefix, { file, raw, sha256: hash(Buffer.from(raw)), data: JSON.parse(raw) }];
}));
const anchorDefinitions = [
  ['model', 'classes2', 'defaultChatModel', '标识符仅提示可能有默认模型状态；默认值、作用域、保存位置与控件绑定均未知。'],
  ['model', 'classes3', 'model_select_new_chat', '资源键字符串存在；没有跟踪引用它的方法。'],
  ['model', 'classes3', 'string:model_select_new_chat', 'Compose生成资源键样式字符串存在；不构成UI事件契约。'],
  ['draft', 'classes2', 'ChatModel-draft', '提示输入草稿研究方向；不证明普通聊天草稿持久化或恢复规则。'],
  ['font', 'classes3', 'composeResources/kimi.shared.generated.resources/drawable/followsystem.xml', '打包资源路径字符串；不证明开关组件或字号计算策略。'],
  ['voice', 'classes2', 'VoiceBottomBar', '组件名样式标识符；无法凭名称确认实际状态机、手势、麦克风权限时序。'],
  ['voice', 'classes3', 'VoiceBottomBar onDispose: voiceRecording=', '日志式文本含dispose与recording词；不证明离开时实际停止录音。'],
  ['login', 'classes2', ' isLogin=', '日志式状态字符串；不证明登录门槛覆盖范围或导航行为。'],
];
const staticAnchors = anchorDefinitions.map(([topic, prefix, text, limitation], i) => {
  const source = stringPoolFiles[prefix];
  const position = source.data.strings.findIndex(s => s.text === text);
  if (position < 0) throw new Error(`Missing exact DEX anchor ${text}`);
  const entry = source.data.strings[position];
  const search = `"text": ${JSON.stringify(text)}`;
  const characterOffset = source.raw.indexOf(search);
  if (characterOffset < 0) throw new Error(`Missing source JSON text ${text}`);
  return { id: `RUN02-DEX-${String(i + 1).padStart(2, '0')}`, topic, sourceDex: source.data.source, sourceDexSha256: inventories[prefix].sha256, stringIndex: entry.index, stringDataOffset: entry.offset, text: entry.text, evidenceLevel: 'B', interpretationClassification: 'C', sourceRecordPath: source.file, sourceRecordSha256: source.sha256, sourceJsonPointer: `/strings/${position}`, sourceJsonTextLine: source.raw.slice(0, characterOffset).split('\n').length, limitation, xrefOrCaller: 'not traced', runtimeBinding: 'unknown' };
});
const classNameQuery = /FontSize|FontSetting|ChatModel|ModelSelect|InputDraft|VoiceInput|VoiceBottom|LoginScreen|NetworkError/;
const classSearch = dexFiles.map(prefix => {
  const file = `evidence/source/${prefix}-class-inventory.json`;
  const data = inventories[prefix];
  return { sourcePath: file, sourceFileSha256: hash(fs.readFileSync(path.join(root, file))), sourceDex: data.source, sourceDexSha256: data.sha256, classCount: data.classes.length, predicate: 'descriptor contains moonshot AND matches /FontSize|FontSetting|ChatModel|ModelSelect|InputDraft|VoiceInput|VoiceBottom|LoginScreen|NetworkError/', matches: data.classes.filter(c => /moonshot/.test(c.descriptor) && classNameQuery.test(c.descriptor)).map(c => ({ descriptor: c.descriptor, classDefOffset: c.classDefOffset })), limitation: 'Bounded descriptive-name query only. Most classes are obfuscated; zero matches do not establish absence of an implementation. Method inventories only cover ownPrefixes declared by saved parser.' };
});
const draftKeySearch = { sourcePaths: ['assets/derived/copy/default.json', 'assets/derived/copy/zh.json'], predicate: 'key matches /draft/i', matches: Object.entries(locales).flatMap(([locale, document]) => document.entries.filter(e => /draft/i.test(e.key)).map(e => ({ locale, key: e.key, sourceLine: e.sourceLine }))), limitation: 'Key-name query only; ordinary input draft state can exist without any user-facing draft string.' };
const result = {
  schemaVersion: 1,
  evidenceId: 'LOCAL-RUN02-COPY-001',
  createdAtUtc: new Date().toISOString(),
  origin: 'current',
  sourceApkSha256,
  taskScope: 'Run02 static questions for model selection, ordinary input draft, font save/cancel, voice/microphone, offline failure and login gates',
  runtimeSessionDirectoryForFutureCrosscheck: 'evidence/runtime/run02-20261016',
  method: 'Read existing default/zh CVR derivations and saved DEX class/string candidates. Verify selected CVR source files/lines/hashes and UTF-8 base64 roundtrip. No APK reparse, decompilation, device/GUI control or installation.',
  command: 'node tools/build-run02-copy-candidates.mjs',
  boundary: 'B labels and identifiers are static declarations. Topic assignments and questions are C research candidates. All runtime occurrence conditions and bindings remain unknown in this file; later A evidence must be linked explicitly.',
  candidateCount: candidates.length,
  topicCounts: Object.fromEntries([...new Set(candidates.map(c => c.topic))].map(topic => [topic, candidates.filter(c => c.topic === topic).length])),
  candidates,
  staticAnchors,
  boundedSearches: { descriptiveClassNames: classSearch, draftNamedCopyKeys: draftKeySearch },
  unknowns: ['Feature reachability under logged-out/online/offline state', 'Resource-key bindings to actual controls and app locale choice', 'Model list, model defaults, trial/quota availability and remote feature flags', 'Draft lifetime across overlay/navigation/new chat/process death and storage ownership', 'Font preview versus committed state, explicit save, cancel/back, persistence and system font interaction', 'Order of login gate, product microphone explanation and Android permission request', 'Voice gesture cancellation, recognition, text restoration and actual request cancellation', 'Offline loading/sending/retry state and network recovery', 'Authentication-provider behavior and effects of dismissing login'],
};
const output = path.join(root, 'evidence/static/run02-copy-candidates.json');
fs.mkdirSync(path.dirname(output), { recursive: true });
fs.writeFileSync(output, JSON.stringify(result, null, 2) + '\n');
console.log(JSON.stringify({ output: 'evidence/static/run02-copy-candidates.json', candidateCount: result.candidateCount, topicCounts: result.topicCounts, exactDexStringAnchors: staticAnchors.length, verifiedSelectedCvrRecords: candidates.length * 2, draftNamedCopyKeyMatches: draftKeySearch.matches.length, descriptiveClassNameMatches: classSearch.reduce((n, q) => n + q.matches.length, 0) }, null, 2));
