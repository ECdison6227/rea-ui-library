# Native reference acceptance paths for the sole device controller

Run03 update: these paths have now been executed by the designated controller. Current results are [reference/run03-acceptance.json](../reference/run03-acceptance.json), [A ↔ D comparison](../comparison/native-run03.md), and [Fieldnotes acceptance](../transfer-example/run03-acceptance.md). The preparation paragraph and reproducible checklist below retain their initial context; they are not the latest status. Final build SHA-256 is `057196b83df26f4ae47da0181d1af7c89fda2423f92b9bfb147c7575837ca140`; independent reference defaults were restored and the package stopped after checks.

Prepared from source/tool inspection only. The implementation worker has not used ADB. Existing retained metadata shows a successful controller install and synthetic clipboard writes; that does not establish the native chat/settings/notes acceptance paths. All reference captures are **D**, source comparisons remain separate A↔D work.

The installed build must match `reference/build/native-reference.apk`. The guarded driver checks this before interactions. Prefix every command below with:

```powershell
node research/kimi-android/tools/native-reference-session.mjs
```

| Acceptance path | Driver operations and expected D outcome |
| --- | --- |
| Known baseline | `scene restore` clears reference fixture state only; `scene chat-empty`, `capture <new-id> "Empty reference"`; view pixels for empty state and composer |
| Actual local request | `scene chat-draft`, `tap-label "Send message"`; after at least 6 seconds, capture completion with `OK`; this is local fixture execution, not a model test |
| Busy and stop | `scene chat-busy`, capture held busy state, `tap-label "Stop output"`, capture `Output stopped`; held fixture isolates the stop target from a completion race |
| Failed message and retry | `scene chat-error`, capture retained message/error, `tap-label "Retry"`; after 6 seconds capture completion; no network service is contacted |
| Draft navigation and restart | `scene chat-draft`; Navigation → Appearance → Back returns chat; capture retained draft; `stop`, then **`launch`**, capture same draft. Loading another `scene` would reset the fixture and cannot prove restart retention |
| Settings cancel/save | `scene settings`; Font size → Extra Large → Back; reopen Font size and verify Follow System remains; select Extra Large → Save and verify return to chat; reenter Appearance and confirm applied summary; finally `scene restore` |
| Immediate theme | `scene settings`; Theme settings → Dark mode; capture changed theme, Back and inspect summary; return to Follow System before other source comparisons |
| Confirmation cancel and confirm | `scene delete-dialog`; Cancel retains chat. Repeat and use Back or outside tap; only dialog closes. Repeat then Delete; reference becomes empty |
| Notes empty/validation | `scene documents-empty`; Add a note → Save note with blank field; capture field validation; Back returns library |
| Notes draft retention | `scene documents-edit`; Return to notes → New; draft remains. `stop`, `launch`, navigate Fieldnotes → New; same draft remains without reseeding a scene |
| Notes normal save | `scene documents-edit`; Save note; after 6 seconds Return to notes; capture saved title. The editor remains open after completion until Return/Back |
| Notes busy cancel | `scene documents-busy`; Cancel saving preserves the title and invalidates the pending operation; Return to notes → New retains draft |
| Notes error retry | `scene documents-error`; capture error/title, Retry saving; after 6 seconds Return to notes and capture saved title |
| Notes delete | `scene documents-normal`; Delete → Cancel preserves the note; reopen Delete → Delete clears it and shows empty guidance |
| Current source capacity pair | `scene source-capacity-error`; capture exact Run03 K2.8 input/capacity text; compare against original `r03-06-k28-request`. The unbranded footer, native font metrics, shadows, Upgrade explanation and local repeated failure remain D differences |

Use `ui` before adaptive actions; `tap-label` requires a fresh unique exact label, or exactly one explicitly clickable match when a native row also exposes a child with the same label. Multiple actionable matches still refuse a tap. `capture ID DESCRIPTION` waits 1 second, requires the actual top-resumed reference Activity, and refuses existing IDs. Screenshot precedes UIA; inspect both and record any transition between them. Never infer a result from the command succeeding. `summary` is local read-only. Keyboard Back can first hide the IME before page navigation, so inspect the resulting page rather than counting blindly.

## Packaging gate

`tools/package-library.ps1 -CheckOnly` performs a local preflight and creates no archive. Source run03 scene files must be exact entries from `docs/run03-safe-scenes.json` bound to `docs/run03-evidence-audit.json`; new, private and changed scenes are excluded. Run02's two private identity scene filters remain in force. The final independent APK and its build result are allowed; intermediate APK/JAR/DEX/classes, signing keys, AVD disks, local launch logs and `.runtime` are excluded.

Independent D scene files also remain excluded until a separate `docs/native-reference-safe-scenes.json` records their exact reviewed bytes. Schema:

```json
{
  "runId": "run03-20261016",
  "classification": "D",
  "scenes": [{
    "id": "example-reviewed-id",
    "pixelReview": "reviewed",
    "privacyReview": "clear",
    "screenshot": {"path": "evidence/reference/run03-20261016/scenes/example-reviewed-id/screen.png", "sha256": "EXACT_SHA256"},
    "hierarchy": {"path": "evidence/reference/run03-20261016/scenes/example-reviewed-id/hierarchy.xml", "sha256": "EXACT_SHA256"},
    "capture": {"path": "evidence/reference/run03-20261016/scenes/example-reviewed-id/capture.json", "sha256": "EXACT_SHA256"}
  }]
}
```

This document is a controller checklist and schema example, not a claim that these device checks have passed. No final archive has been created by the implementation worker.
