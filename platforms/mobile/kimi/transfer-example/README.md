# Fieldnotes · neutral document-management transfer

Run03 actual device validation is now complete for this minimal sample: [path results and safe original images](run03-acceptance.md), [exact capture/XML/SHA audit and R03 acceptance](../reference/run03-acceptance.json). The final APK is `057196b83df26f4ae47da0181d1af7c89fda2423f92b9bfb147c7575837ca140`. Normal save, empty/blank validation, error/retry, actual cancellation after a real pending save, draft/saved process retention, interrupted-save recovery and deletion Cancel/confirm were exercised. This D transfer result is separate from [original-product comparison](../comparison/native-run03.md).

This sample uses a different business: keeping local reference notes. It shares the native APK and components in [reference](../reference/README.md), so no browser or second build is needed:

```powershell
adb -s emulator-5596 shell am start -f 0x20000000 -n org.research.kimistyle/.MainActivity --es scene documents-empty
```

Only the designated controller should execute this command after verifying the research AVD identity. The sample never opens another app's records or files.

| Source habit (historical A) | Fieldnotes reuse (D) | Business replacement / boundary |
| --- | --- | --- |
| Rounded grouped surfaces, clear primary/secondary text, limited blue accent | Note cards, editor guidance, current-value Appearance rows | Kimi brand, mascot and model names are absent from the Fieldnotes screens |
| Nearby contextual actions beneath content | Edit / Delete live beside the saved note | No AI answer toolbar or model capability is implied |
| Empty-state explanation and one clear action | “Make room for your next idea” / “Add a note” | Local note creation replaces login/chat discovery |
| Pending operation uses a clear busy state and a nearby cancel action | Saving / Cancel saving | Local 6-second callback replaces backend upload |
| Retained input and explicit retry after failure | “Couldn't save this note. Your draft is safe.” / Retry saving | Error fixture is local and intentionally recoverable |
| Cancelable destructive confirmation | Delete selected note with Cancel / Delete | Only the disposable reference note is changed |
| Settings show current value and distinguish immediate from saved choices | Shared Appearance / Theme / Font detail | Save-to-chat remains the reference's source-parity route; a production notes app should choose its own return destination |

The document editor and library are a single-note minimal sample. All note validation, draft persistence, interrupted-save recovery and epoch cancellation rules are D policies. The provenance is [source-mapping.json](../reference/source-mapping.json), and all props/events/states are in [component-contracts.json](../reference/component-contracts.json).

To reproduce the now-recorded device paths, use `documents-empty`, `documents-normal`, `documents-edit`, `documents-busy`, `documents-error` as described in the reference README. The linked acceptance report shows each actual result, including process restart **without** a scene extra and real 6-second save cancellation followed by a late-result wait. Held fixtures and state tests are reported separately. Build-time state tests alone still do not complete R03 or establish source fidelity. The controller restored reference defaults and stopped the reference after the Run03 checks; source-account restoration and AVD shutdown are separately owned by the main controller.
