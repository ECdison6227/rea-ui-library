# Native Android style reference

Run03 device acceptance is now complete for the documented representative scope: [R01–R03 checks and exact evidence](run03-acceptance.json), [paired original A ↔ reference D comparison](../comparison/native-run03.md), and [Fieldnotes transfer acceptance](../transfer-example/run03-acceptance.md). Final APK SHA-256: `057196b83df26f4ae47da0181d1af7c89fda2423f92b9bfb147c7575837ca140`. Of 71 reviewed D captures, 019–071 are the final build; 001–018 preserve superseded-build repair evidence only. The actual controller verified the dedicated AVD/installed APK, executed normal/busy/error/cancel/Back/restart paths, restored reference defaults and stopped the reference. This does not establish original-product pixel or service equivalence.

This is an independent **D implementation**, written in Java with Android View, EditText, Dialog, SharedPreferences and Canvas. It contains no WebView, external UI library, original app source, account integration or network permission. It demonstrates the documented design language; it is not Kimi's original implementation or a feature-complete replacement.

Build from the workspace root:

```powershell
& ./research/kimi-android/tools/build-native-reference.ps1
```

The script reuses the retained Android SDK 35 / build tools 35.0.0 / JDK 21. Its `-AndroidToolchain` argument can point to another compatible SDK/JDK directory. It does not install software. Output: [APK](build/native-reference.apk), [build and test result](build/build-result.json). The local debug signing key is under `.runtime/native-reference-build`; exclude `.runtime` from distribution. The key is for this disposable reference package only.

Only the designated device controller may install or drive the reference. First verify the dedicated AVD name and port using the research handoff. These example commands do not select or authorize another device:

```powershell
adb -s emulator-5596 install -r ./research/kimi-android/reference/build/native-reference.apk
adb -s emulator-5596 shell am start -f 0x20000000 -n org.research.kimistyle/.MainActivity --es scene chat-complete
```

Package: `org.research.kimistyle`. Existing original-package data is isolated. Default launch restores the reference's local drafts and preferences; passing a `scene` intentionally applies a disposable fixture. Use `--es scene restore` to reset reference state only. The build script never calls ADB.

For actual D evidence, the designated controller can use the independent guarded driver:

```powershell
node ./research/kimi-android/tools/native-reference-session.mjs install
node ./research/kimi-android/tools/native-reference-session.mjs scene source-capacity-error
node ./research/kimi-android/tools/native-reference-session.mjs capture nr03-01-capacity "Native K2.8 capacity-error fixture"
node ./research/kimi-android/tools/native-reference-session.mjs tap-label "Retry failed message"
node ./research/kimi-android/tools/native-reference-session.mjs summary
```

It pins `emulator-5596` / `rea_kimi_ui_20261015`, verifies the installed reference APK hash against this local build, uses one run lock, and writes only `evidence/reference/run03-20261016`. Capture refuses an existing ID or an actual top-resumed activity outside the reference package; input commands have the same foreground guard. Screenshots and UIA remain non-atomic. `summary` and `--help` read only local data and do not call ADB. `stop` force-stops only `org.research.kimistyle`; `launch` then opens saved state without applying a fixture, which is required for process-retention checks. Other commands are `ui`, `tap-label`, `tap X Y`, `back`, and `scene NAME`. No command controls the original app, records account credentials, or chooses another device. The script was prepared without invoking ADB; device results exist only after the controller runs it and inspects the captured pixels/UIA.

| Scene | Initial state | Interactions to verify |
| --- | --- | --- |
| `chat-empty` | Empty home | Focus editor, type, send; local completion in 6 seconds |
| `chat-complete` | `Reply only OK` / `OK` | Copy; Retry; More → Delete → Cancel |
| `chat-busy` | Generation held open | Type a new draft; Stop output preserves it |
| `chat-error` | Failed message retained | Retry resolves locally in 6 seconds |
| `source-capacity-error` | Exact Run03 K2.8 capacity-error content | Red message retry repeats a local capacity failure; Upgrade explains the fixture |
| `chat-draft` | Known local draft | Navigation → Appearance → Back; restart without scene |
| `settings` | Appearance | Theme immediate; font preview Back abandons; Save applies and returns chat |
| `delete-dialog` | Completed chat + confirmation | Cancel / Back / outside close preserve chat; Delete clears this reference chat |
| `documents-empty` | Empty Fieldnotes library | Add a note; empty title validation |
| `documents-normal` | Saved note | Edit / Delete / Cancel |
| `documents-edit` | Unsaved note draft | Return and reopen, save normally, restart without scene |
| `documents-busy` | Saving held open | Cancel saving retains title and rejects late result |
| `documents-error` | Failed save, draft retained | Retry saving resolves locally in 6 seconds |

`chat-busy` and `documents-busy` are held fixtures. Actual Send, Retry and Save note create a 6-second local callback. This duration is D and is not an estimate of Kimi's runtime. `chat-error` / `documents-error` inject initial local failures; retry is an explicit fixture recovery, not a real network test. `enable-failure` makes subsequent local requests fail until another named scene is loaded. Scenario changes invalidate pending callbacks. Chat and note requests use separate epochs.

`source-capacity-error` is a source-pairing fixture for the actual Run03 text `Run03 mdel K2.8 test. Reply only RUN03-K28-OK.`. The observed typo is retained. It sets the reference to light / Follow System text scale and Kimi K2.8, hides completion-only controls, and reproduces the observed capacity message. It uses no subscription or model service. Source semantic geometry and D choices are in [native-reference-capacity-pairing.json](../docs/native-reference-capacity-pairing.json). The local reference footer stays visible; exact font/ink matching requires runtime comparison.

Use the in-app navigation panel's **Research scenarios** route to select the same scenes manually. Native controls expose stable content descriptions including `Message draft`, `Send message`, `Stop output`, `Navigation button`, `More`, `Back`, `Model selector`, `Note draft`, `Save note`, `Cancel saving`, `Retry saving`.

Read [component contracts](component-contracts.json), [A/B → D mapping](source-mapping.json), [native limitations](../docs/native-reference-status.md), and the [different-business transfer](../transfer-example/README.md). Build-time state tests validate this code only. The linked Run03 reports now provide the separate device, source-comparison and transfer checks; a successful build alone remains insufficient. The final native implementation fixes duplicated top system insets, clipped parent shadows, IME-obscured input, and a wrapped Save label. API30+ IME handling and the remaining font/icon/shadow/focus differences are explicit D choices.

## Separate synthetic clipboard research helper

The APK also contains `.ClipboardFixtureActivity`, an independent **D research tool**, separate from the reference product screens. It accepts exactly one string intent extra, `fixtureBase64`, decodes strict UTF-8, requires the text to begin with `Research Run03`, and allows at most 2,000 Unicode code points. On foreground window focus it calls Android `ClipboardManager.setPrimaryClip` once with only that supplied synthetic text, then finishes. It has no task affinity, history/recents entry, network capability, original-app data access, or clipboard-read operation. It does not open or read the host clipboard.

The designated controller can install the current APK, leave the intended destination app open, then run:

```powershell
node ./research/kimi-android/tools/native-reference-session.mjs install
node ./research/kimi-android/tools/set-run03-clipboard.mjs 'Research Run03 العربية اختبار الحافظة'
```

The script pins the same serial/AVD, checks the installed reference APK hash against the local build, acquires the same reference run lock, passes only Base64 as the intent payload, and uses a separate temporary task so the helper can finish back to the previous activity. It checks a **new matching helper status record** and the returned activity. It never reads either clipboard. The helper's private status preference and dedicated log tag retain only attempt ID, status, character/byte lengths, SHA-256, device time and PID. Script evidence under `evidence/reference/run03-20261016/clipboard-events` retains metadata only; neither source text nor Base64 payload is logged. A `written` result means the foreground write call returned successfully; actual paste and the destination field still require UI/pixel verification.

Local input validation without any ADB call:

```powershell
node ./research/kimi-android/tools/set-run03-clipboard.mjs --validate-only 'Research Run03 العربية اختبار الحافظة'
```

The build automatically compiles the additional native Activity from `reference/src`; no extra library or service is installed. This tool's instructions are intentionally absent from the reference app's product navigation.
