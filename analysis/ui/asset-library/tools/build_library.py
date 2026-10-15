"""Preserve image/font/vector/text specimens without executing target code.

Requires Python 3; Pillow is optional and used only for image dimensions.
Run from any directory. The source APK and decoded resources are read-only.
"""
from __future__ import annotations

import hashlib
import io
import json
import re
import tarfile
import zipfile
import xml.etree.ElementTree as ET
from collections import Counter, defaultdict
from datetime import datetime, timezone
from pathlib import Path, PurePosixPath

try:
    from PIL import Image
except ImportError:
    Image = None

HERE = Path(__file__).resolve().parent.parent
ROOT = HERE.parents[2]
DECODED = ROOT / "analysis/phase2/apktool/res"
APK = ROOT / "base.apk"
NATIVE_MANIFEST = ROOT / "analysis/ui/android-reference/assets-manifest.json"
IMAGE_EXTS = {".png", ".webp", ".jpg", ".jpeg", ".gif", ".avif", ".svg", ".bmp", ".ico", ".heic", ".heif", ".apng"}
FONT_EXTS = {".otf", ".ttf", ".woff", ".woff2"}
VIDEO_EXTS = {".mp4", ".webm", ".mov", ".m4v", ".avi", ".mkv"}
NOW = datetime.now(timezone.utc).isoformat()
EXPECTED_APK_SHA = "2a80fc5a87353c2c6ef3c01fa0085862fe45e4ddb9c2d272cb3780841b5889a6"


def sha(b: bytes) -> str:
    return hashlib.sha256(b).hexdigest()


def rel(p: Path) -> str:
    return p.relative_to(ROOT).as_posix()


def safe(path: str) -> str:
    p = PurePosixPath(path.replace("\\", "/"))
    if p.is_absolute() or ".." in p.parts:
        raise ValueError("Unsafe archive member: " + path)
    return p.as_posix()


def detected_kind(path: str, b: bytes):
    ext = PurePosixPath(path).suffix.lower()
    if b.startswith(b"\x89PNG\r\n\x1a\n"):
        return "image", "PNG"
    if b[:3] == b"\xff\xd8\xff":
        return "image", "JPEG"
    if b[:6] in (b"GIF87a", b"GIF89a"):
        return "image", "GIF"
    if b[:4] == b"RIFF" and b[8:12] == b"WEBP":
        return "image", "WEBP"
    if b[:4] in (b"OTTO", b"\x00\x01\x00\x00", b"ttcf", b"wOFF", b"wOF2") or ext in FONT_EXTS:
        return "font", {b"OTTO": "OpenType", b"ttcf": "TrueTypeCollection", b"wOFF": "WOFF", b"wOF2": "WOFF2"}.get(b[:4], "TrueType")
    if b[4:8] == b"ftyp" and any(t in b[:40] for t in (b"avif", b"avis", b"heic", b"heix", b"mif1")):
        return "image", "AVIF/HEIF"
    if ext == ".svg" or (b"<svg" in b[:2000] and b"http://www.w3.org/2000/svg" in b[:3000]):
        return "image", "SVG"
    if ext in IMAGE_EXTS:
        return "image", ext[1:].upper()
    if ext in VIDEO_EXTS or (b[4:8] == b"ftyp" and not any(t in b[:40] for t in (b"avif", b"heic"))):
        return "video", "container-index-only"
    if ext == ".json":
        try:
            obj = json.loads(b)
            if isinstance(obj, dict) and isinstance(obj.get("layers"), list) and all(k in obj for k in ("v", "fr", "ip", "op", "w", "h")):
                if all(isinstance(obj[k], (int, float)) for k in ("fr", "ip", "op", "w", "h")):
                    return "animation", "Lottie-schema"
        except (ValueError, UnicodeError):
            pass
    return None, None


entries = []
by_sha = defaultdict(list)
claimed_storage_paths = {}
storage_path_adjustments = []
videos = []
skipped_json = []
archive_errors = []
containers = []
texts = []
dependencies = []
native = json.loads(NATIVE_MANIFEST.read_text(encoding="utf-8"))
native_by_sha = defaultdict(list)
for item in native["entries"]:
    native_by_sha[item["sha256"]].append(item["file"])


def add(path: str, b: bytes, source: dict, kind: str, fmt: str, provenance="unknown", provenance_basis=None):
    path = safe(path)
    digest = sha(b)
    requested_path = path
    previous = claimed_storage_paths.get(path.casefold())
    if previous is not None and previous != digest:
        original = PurePosixPath(path)
        path = str(original.with_name(original.stem + ".case-" + digest[:12] + original.suffix))
        storage_path_adjustments.append({"requestedPath": requested_path, "storedPath": path, "reason": "APK contains distinct members whose paths collide on Windows case-insensitive filesystems. Original source path is preserved in source metadata."})
    claimed_storage_paths[path.casefold()] = digest
    dest = HERE / path
    dest.parent.mkdir(parents=True, exist_ok=True)
    dest.write_bytes(b)
    item = {
        "id": "asset-" + hashlib.sha256(path.encode()).hexdigest()[:16],
        "file": path,
        "source": source,
        "sha256": digest,
        "bytes": len(b),
        "kind": kind,
        "format": fmt,
        "provenance": provenance,
        "provenanceBasis": provenance_basis or "Not established; bundled presence, filename, and visual style alone do not prove origin.",
        "nativeReferenceAssets": native_by_sha.get(digest, []),
        "reuseStatus": "preserved specimen; not an approved universal style rule",
    }
    if path != requested_path:
        item["storagePathAdjustment"] = {"requestedPath": requested_path, "reason": "Case-insensitive filesystem collision; deterministic content-hash suffix preserves both original byte sequences."}
    if kind == "image" and Image is not None:
        try:
            with Image.open(io.BytesIO(b)) as im:
                item.update(widthPx=im.width, heightPx=im.height, frames=getattr(im, "n_frames", 1), mode=im.mode)
        except Exception as exc:
            item["dimensionStatus"] = "unknown: " + type(exc).__name__
    elif kind == "image" and fmt == "SVG":
        try:
            el = ET.fromstring(b)
            item.update(declaredWidth=el.get("width"), declaredHeight=el.get("height"), viewBox=el.get("viewBox"))
        except ET.ParseError:
            item["dimensionStatus"] = "unknown: SVG parsing failed"
    if kind == "animation":
        obj = json.loads(b)
        item["animationSchema"] = {k: obj[k] for k in ("v", "fr", "ip", "op", "w", "h")}
        item["animationSchema"]["layerCount"] = len(obj["layers"])
        item["animationSchema"]["externalImagePaths"] = [{"u": x.get("u"), "p": x.get("p")} for x in obj.get("assets", []) if isinstance(x, dict) and "p" in x]
        item["animationSchema"]["limitation"] = "Schema identification only; no claim of runtime use or successful playback. Embedded image bytes remain in the JSON."
    entries.append(item)
    by_sha[digest].append(item)
    return item


generic_exact = {"取消", "确定", "确认", "保存", "删除", "重试", "返回", "下一步", "完成", "设置", "帮助", "搜索", "登录", "允许", "拒绝", "关闭", "退出", "Cancel", "OK", "Save", "Delete", "Retry", "Back", "Next", "Done", "Settings", "Help", "Search", "Sign in", "Allow", "Deny", "Close", "Exit"}
health_words = re.compile(r"睡眠|心率|血氧|血压|卡路里|跑步|步数|健身|锻炼|体脂|心电|运动健康|(?i:heart rate|blood oxygen|blood pressure|calories|workout|body fat|electrocardiogram)")


def text_scope(value: str):
    if value.strip() in generic_exact:
        return "generic", "Exact short-label allowlist; semantic portability only, context still needs review."
    if health_words.search(value):
        return "health", "Explicit health-domain term in text; classification is an analyst rule, not original design intent."
    return "unknown", "No reviewed classification; keep context-dependent copy unchanged in source specimens."


def index_text(path: Path, b: bytes):
    try:
        doc = ET.fromstring(b)
    except ET.ParseError as exc:
        archive_errors.append({"source": rel(path), "error": str(exc)})
        return
    for el in doc:
        if el.tag not in {"string", "plurals", "string-array", "integer-array", "array"}:
            continue
        values = []
        if el.tag == "string":
            values.append({"text": "".join(el.itertext()), "attributes": dict(el.attrib)})
        else:
            values.extend({"text": "".join(x.itertext()), "attributes": dict(x.attrib)} for x in el if x.tag == "item")
        combined = "\n".join(v["text"] for v in values)
        scope, basis = text_scope(combined)
        texts.append({"sourceFile": "original/res/" + path.relative_to(DECODED).as_posix(), "name": el.get("name"), "resourceType": el.tag, "attributes": dict(el.attrib), "values": values, "businessScope": scope, "provenance": "unknown"})


apk_bytes = APK.read_bytes()
assert sha(apk_bytes) == EXPECTED_APK_SHA, "Target APK identity changed"
z = zipfile.ZipFile(io.BytesIO(apk_bytes))
zip_by_crc = defaultdict(list)
for zi in z.infolist():
    zip_by_crc[(zi.CRC, zi.file_size)].append(zi)

# Copy named decoded resources, never rewriting XML, raster pixels or font bytes.
for src in sorted(DECODED.rglob("*")):
    if not src.is_file():
        continue
    folder = src.parent.name
    ext = src.suffix.lower()
    is_text_locale = folder == "values" or folder.startswith("values-zh") or folder.startswith("values-en") or folder.startswith("values-b+zh") or folder.startswith("values-b+en")
    copy_text = is_text_locale and src.name in {"strings.xml", "plurals.xml", "arrays.xml"}
    # Preserve all value definitions as dependencies, but text translations only in requested locales.
    value_dependency = folder.startswith("values") and ext == ".xml" and src.name not in {"strings.xml", "plurals.xml", "arrays.xml"}
    drawable_xml = folder.startswith(("drawable", "mipmap", "color", "font", "anim", "interpolator", "transition")) and ext == ".xml"
    regular = ext in IMAGE_EXTS or ext in FONT_EXTS
    raw_candidate = folder.startswith(("raw", "drawable", "mipmap", "font")) and not regular and not drawable_xml
    if not (copy_text or value_dependency or drawable_xml or regular or raw_candidate):
        continue
    b = src.read_bytes()
    kind, fmt = detected_kind(src.name, b)
    if copy_text:
        kind, fmt = "text-resource", "Android-values-XML"
        index_text(src, b)
    elif value_dependency or drawable_xml:
        kind, fmt = "resource-dependency", "Android-resource-XML"
        try:
            doc = ET.fromstring(b)
            if doc.tag.rsplit("}", 1)[-1] == "vector":
                kind, fmt = "vector", "Android-VectorDrawable-XML"
        except ET.ParseError:
            pass
    if kind is None:
        if ext == ".json":
            skipped_json.append({"source": rel(src), "reason": "Not recognized as Lottie schema"})
        continue
    import zlib
    exact_paths = [x.filename for x in zip_by_crc[(zlib.crc32(b), len(b))] if z.read(x) == b]
    item = add("original/res/" + src.relative_to(DECODED).as_posix(), b, {"kind": "apktool-decoded-resource", "path": rel(src), "evidenceClass": "B", "origin": "historical", "revalidatedAt": NOW, "bytePreservation": "Exact decoded source bytes", "exactAPKByteMatches": exact_paths, "note": "Decoded XML and compiled nine-patch images may differ from encoded APK members; do not claim raw-APK byte equality unless exactAPKByteMatches is nonempty."}, kind, fmt)
    if ext == ".xml":
        refs = sorted(set(re.findall(rb"@(?:[a-zA-Z0-9_.]+:)?[a-zA-Z0-9_]+/[a-zA-Z0-9_.$]+", b)))
        dependencies.append({"file": item["file"], "references": [x.decode("ascii") for x in refs], "resolutionStatus": "Reference declarations preserved; see dependency summary. Android framework references remain platform dependencies."})


def process_member(path: str, b: bytes, source: dict, dest: str, depth=0):
    if depth > 4:
        archive_errors.append({"source": path, "error": "Archive nesting exceeds four levels"})
        return
    kind, fmt = detected_kind(path, b)
    # Explicit localization directory + requested language + string-valued JSON;
    # this exception preserves copy assets without collecting arbitrary business JSON.
    is_localization = "/localizedString/" in path and re.fullmatch(r"(?:en|zh)(?:[-_][A-Za-z0-9-]+)?\.json", PurePosixPath(path).name)
    if is_localization:
        try:
            localized = json.loads(b)
            if isinstance(localized, dict):
                kind, fmt = "text-resource", "RN-localization-JSON"
                def leaves(obj, prefix=""):
                    if isinstance(obj, dict):
                        for key, value in obj.items():
                            yield from leaves(value, prefix + ("." if prefix else "") + str(key))
                    elif isinstance(obj, str):
                        yield prefix, obj
                for name, value in leaves(localized):
                    scope, _basis = text_scope(value)
                    texts.append({"sourceFile": dest, "name": name, "resourceType": "RN-localization-json", "attributes": {}, "values": [{"text": value}], "businessScope": scope, "provenance": "unknown"})
        except (ValueError, UnicodeError):
            pass
    if kind == "video":
        videos.append({"source": source, "bytes": len(b), "sha256": sha(b), "format": fmt, "preservation": "Indexed only; recover original member from base.apk. No duplicate video copy."})
        return
    if kind:
        digest = sha(b)
        # Already-preserved decoded raw image bytes need not be stored again under obfuscated names.
        if source.get("kind") == "apk-member" and path.startswith("res/") and digest in by_sha:
            for item in by_sha[digest]:
                item.setdefault("additionalAPKPaths", []).append(path)
            return
        provenance = "SDK" if source["path"].startswith("assets/plugin_rn_sdk/") else "unknown"
        basis = "Original APK path explicitly places the file under assets/plugin_rn_sdk/. SDK packaging provenance only; artist identity and custom modifications remain unknown." if provenance == "SDK" else None
        add(dest, b, source, kind, fmt, provenance, basis)
        return
    suffix = PurePosixPath(path).suffix.lower()
    if suffix == ".json":
        skipped_json.append({"source": source, "reason": "Not recognized as Lottie schema; not copied"})
    if suffix == ".zip" or b.startswith(b"PK\x03\x04"):
        try:
            archive = zipfile.ZipFile(io.BytesIO(b))
            containers.append({"source": path, "kind": "zip", "bytes": len(b), "sha256": sha(b), "members": len(archive.infolist())})
            for info in archive.infolist():
                if not info.is_dir():
                    n = safe(info.filename)
                    process_member(path + "!" + n, archive.read(info), {"kind": "nested-apk-member", "path": source["path"] + "!" + n, "evidenceClass": "B", "origin": "current", "extractedAt": NOW}, dest + ".contents/" + n, depth + 1)
        except (ValueError, zipfile.BadZipFile) as exc:
            archive_errors.append({"source": path, "error": str(exc)})
    elif suffix in {".tar", ".tgz", ".gz"} or (len(b) > 265 and b[257:262] == b"ustar"):
        try:
            archive = tarfile.open(fileobj=io.BytesIO(b), mode="r:*")
            members = archive.getmembers()
            containers.append({"source": path, "kind": "tar", "bytes": len(b), "sha256": sha(b), "members": len(members)})
            for info in members:
                if info.isfile():
                    n = safe(info.name)
                    member = archive.extractfile(info)
                    process_member(path + "!" + n, member.read(), {"kind": "nested-apk-member", "path": source["path"] + "!" + n, "evidenceClass": "B", "origin": "current", "extractedAt": NOW}, dest + ".contents/" + n, depth + 1)
        except (ValueError, tarfile.TarError) as exc:
            archive_errors.append({"source": path, "error": str(exc)})


# Inspect APK resources and assets by image/font signatures, not just filename extension.
# Native libraries/DEX/resource table/manifest are not copied into this media library.
for info in z.infolist():
    if info.is_dir() or not info.filename.startswith(("res/", "assets/")):
        continue
    b = z.read(info)
    process_member(info.filename, b, {"kind": "apk-member", "apk": "base.apk", "path": info.filename, "evidenceClass": "B", "origin": "current", "extractedAt": NOW}, "original/apk-encoded/" + safe(info.filename))

# Retain the exact CDN bytes already matched against original app cache. This is a separate runtime-origin supplement.
for src_entry in native["entries"]:
    if src_entry["source"]["kind"] != "code-declared-public-cdn":
        continue
    source_path = NATIVE_MANIFEST.parent / src_entry["file"]
    b = source_path.read_bytes()
    assert sha(b) == src_entry["sha256"]
    kind, fmt = detected_kind(source_path.name, b)
    item = add("runtime-selected/" + src_entry["file"], b, {"kind": "runtime-cache-and-code-declared-cdn", "path": rel(source_path), "cdn": src_entry["source"], "runtimeCacheMatch": src_entry.get("runtimeCacheMatch"), "evidenceClass": "B", "origin": "current", "note": "Selected observed-page supplement; not a claim that all remote assets were enumerated."}, kind, fmt, "firstparty", "Exact URL is served by Xiaomi's declared asset host and referenced by the app's captured Mine module; means delivery provenance, not proof of artist identity or a license grant.")
    item["historicalPageObservations"] = src_entry.get("observedIn", [])

# Resolve local resource references against preserved named Android resources.
named = defaultdict(list)
values_defs = defaultdict(list)
for item in entries:
    path = PurePosixPath(item["file"])
    if str(path).startswith("original/res/"):
        folder = path.parent.name
        if not folder.startswith("values"):
            name = path.name.rsplit(".", 1)[0]
            if name.endswith(".9"):
                name = name[:-2]
            named[folder.split("-")[0] + "/" + name].append(item["file"])
        elif path.suffix == ".xml":
            try:
                for el in ET.fromstring((HERE / item["file"]).read_bytes()):
                    name = el.get("name")
                    if name:
                        tag = el.get("type") if el.tag == "item" else el.tag
                        tag = {"string-array": "array", "integer-array": "array"}.get(tag, tag)
                        values_defs[str(tag) + "/" + name].append(item["file"])
            except ET.ParseError:
                pass
unresolved = []
for dep in dependencies:
    resolutions = []
    for ref in dep["references"]:
        key = ref[1:]
        if ":" in key:
            resolutions.append({"reference": ref, "status": "platform-or-namespaced-dependency"})
            continue
        candidates = sorted(set(named.get(key, []) + values_defs.get(key, [])))
        status = "preserved" if candidates else "not-in-media-copy"
        record = {"reference": ref, "status": status, "files": candidates}
        resolutions.append(record)
        if not candidates:
            unresolved.append({"from": dep["file"], "reference": ref})
    dep["resolved"] = resolutions

counts = Counter(x["kind"] for x in entries)
font_files = [x for x in entries if x["kind"] == "font"]
summary = {
    "generatedAt": NOW,
    "target": {"package": "com.mi.health", "versionName": "3.57.0", "apk": "base.apk", "sha256": EXPECTED_APK_SHA},
    "scope": "All recognizable APK image/font assets, named decoded image/vector/dependency resources, default/English/Chinese text resources, schema-confirmed Lottie JSON and selected exact runtime-cache CDN images. Videos index only. Shipping presence is not a universal design fact.",
    "counts": dict(counts),
    "files": len(entries),
    "bytes": sum(x["bytes"] for x in entries),
    "uniqueContentBytes": sum(group[0]["bytes"] for group in by_sha.values()),
    "provenanceCounts": dict(Counter(x["provenance"] for x in entries)),
    "textResourceCount": len(texts),
    "textScopeCounts": dict(Counter(x["businessScope"] for x in texts)),
    "videoFilesIndexed": len(videos),
    "fontFiles": [{"file": x["file"], "sha256": x["sha256"]} for x in font_files],
    "nativeReferenceManifest": "../android-reference/assets-manifest.json",
    "dependencyReferenceCount": sum(len(x["references"]) for x in dependencies),
    "unresolvedLocalReferences": len(unresolved),
    "unresolvedDrawableAndAnimationLocalReferences": sum(not x["from"].startswith("original/res/values") for x in unresolved),
    "storagePathAdjustments": storage_path_adjustments,
    "limitations": ["Most artwork provenance is unknown; no automatic first-party attribution from resource prefixes.", "English resides primarily in default values; this decoded build has no values-en folder. Chinese includes values-zh and regional qualifiers.", "Android XML is the decoded representation, preserved byte-for-byte from the named decoded source. Nine-patch or decoded XML may differ from raw APK encoding.", "Media dimensions do not declare dp/sp display size. Runtime use and component geometry require the separate style research evidence.", "Font file presence does not prove the runtime per-glyph fallback face. MiSansLatin does not supply complete CJK coverage.", "Remote assets beyond the selected runtime-cache sample are not enumerable from this APK-only preservation pass.", "Non-Lottie business/config JSON is not copied into this library.", "This preservation pass does not establish copyright ownership or reuse licensing."],
}

HERE.mkdir(parents=True, exist_ok=True)
(HERE / "inventory.json").write_text(json.dumps({"summary": summary, "entries": entries}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
(HERE / "texts.jsonl").write_text("".join(json.dumps(x, ensure_ascii=False) + "\n" for x in texts), encoding="utf-8")
(HERE / "text-index-schema.json").write_text(json.dumps({"format": "One JSON object per line in texts.jsonl", "sourceFile": "Byte-exact preserved XML. Look up inventory.json by file for original path and SHA-256.", "sourceEvidenceClass": "B", "businessScopeEvidenceClass": "C", "scopeRules": {"generic": "Exact short-label allowlist: " + ", ".join(sorted(generic_exact)), "health": "Explicit health-domain term matched by the documented build script; analyst classification, not author intent.", "unknown": "No reviewed classification; preserve context-dependent copy."}, "limitations": ["Copying a string does not approve its meaning for another product.", "The JSON index is a lookup representation. Original XML is authoritative for spans, placeholders, escaping, attributes and quantity rules.", "Most resource text ownership and SDK provenance are unknown; do not infer first-party authorship from name prefixes." ]}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
(HERE / "resource-dependencies.json").write_text(json.dumps({"dependencies": dependencies, "unresolved": unresolved}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
(HERE / "video-index.json").write_text(json.dumps({"videos": videos, "note": "No duplicate video media copied. Paths identify original APK members and nested archive paths."}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
(HERE / "animations.json").write_text(json.dumps({"lottie": [x for x in entries if x["kind"] == "animation"], "unrecognizedJson": skipped_json, "archiveContainersInspected": containers, "errors": archive_errors}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
hash_errors = [x["file"] for x in entries if sha((HERE / x["file"]).read_bytes()) != x["sha256"]]
(HERE / "verification.json").write_text(json.dumps({"at": NOW, "sourceApkSha256": sha(APK.read_bytes()), "sourceApkUnchanged": sha(APK.read_bytes()) == EXPECTED_APK_SHA, "copiedFilesChecked": len(entries), "copyHashErrors": hash_errors, "archiveErrors": archive_errors, "allCopiesByteExactToDeclaredSource": not hash_errors, "runtimeRenderingTest": "not performed by preservation script; refer to native reference comparisons", "countsAreNotDesignCoverage": True}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

# Add reciprocal media-library links without changing the native reference behavior.
for item in native["entries"]:
    item["assetLibrary"] = {"inventory": "../asset-library/inventory.json", "matchingFiles": ["../asset-library/" + x["file"] for x in by_sha.get(item["sha256"], [])]}
native["assetLibrary"] = {"entry": "../asset-library/README.md", "inventory": "../asset-library/inventory.json", "scope": "APK media/text/vector preservation and selected runtime-cache images; presence is separate from validated design-rule coverage."}
native["verification"] = {
    "at": NOW,
    "assetHashesVerified": all(sha((NATIVE_MANIFEST.parent / x["file"]).read_bytes()) == x["sha256"] for x in native["entries"]),
    "entriesChecked": len(native["entries"]),
    "nativeBitmapEntriesWithExactAPKByteMatches": sum(x["source"]["kind"] == "apk-resource-original-bytes" and bool(x["source"].get("archivePaths")) for x in native["entries"]),
    "cdnEntriesWithExactRuntimeCacheByteMatches": sum("runtimeCacheMatch" in x for x in native["entries"]),
    "sourceVectorFiles": sum(x["source"]["kind"] == "apktool-decoded-vector" for x in native["entries"]),
    "observedInScope": "Links identify historical original-page observations, not fresh runtime captures or individual pixel identity tests.",
}
NATIVE_MANIFEST.write_text(json.dumps(native, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print(json.dumps(summary, ensure_ascii=False))
