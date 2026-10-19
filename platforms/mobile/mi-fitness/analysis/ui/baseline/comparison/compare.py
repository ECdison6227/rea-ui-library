#!/usr/bin/env python3
"""Create auditable, offline image comparisons. Requires Pillow; no OCR or network."""
from __future__ import annotations

import argparse
import hashlib
import json
import os
from datetime import datetime, timezone
from pathlib import Path

from PIL import Image, ImageChops, ImageFilter

PAGES = ("health-day", "mine-day", "settings-day")
LABELS = {"health-day": "健康概览", "mine-day": "我的", "settings-day": "应用设置"}
CANVAS = (1080, 2400)
ROI = (0, 96, 1080, 2334)  # Pillow convention: right and bottom are exclusive.
PALETTE = {
    "white-surface": "#FFFFFF",
    "rn-page": "#F6F7F9",
    "native-page": "#FAFAFA",
    "brand-day": "#FF612E",
    "switch-track-candidate": "#E6E6E6",
    "secondary-ink-candidate": "#999999",
}


def sha256(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def load_json(path: Path):
    return json.loads(path.read_text(encoding="utf-8-sig"))


def evidence_for(runtime: Path, page: str, output: Path) -> dict:
    """Use the real retained Evidence record, never fabricate an evidence ID."""
    for path in sorted(runtime.glob("*.rea.json")):
        wrapper = load_json(path)
        record = wrapper.get("structuredContent", wrapper)
        if not record.get("evidence_id"):
            for block in wrapper.get("content", []):
                if block.get("type") == "text":
                    try:
                        candidate = json.loads(block["text"])
                        if candidate.get("evidence_id"):
                            record = candidate
                            break
                    except (ValueError, KeyError):
                        pass
        result = record.get("normalized_result") or {}
        captured_path = str(result.get("local_path", "")).replace("\\", "/")
        directory = str((record.get("parameters") or {}).get("output_directory", "")).replace("\\", "/")
        if captured_path.endswith(f"/{page}/screen.png") or directory.endswith(f"/{page}"):
            return {
                "evidenceId": record.get("evidence_id"),
                "record": Path(os.path.relpath(path, output)).as_posix(),
                "expectedSha256": result.get("sha256"),
                "provider": record.get("provider"),
                "captureTimeUtc": record.get("captured_at") or record.get("created_at"),
                "recordLimitations": record.get("limitations", []),
                "origin": "current",
            }
    return {"evidenceId": None, "record": None, "expectedSha256": None,
            "captureTimeUtc": None, "unknown": "No matching retained REA capture record found."}


def color_mask(image: Image.Image, color: str, tolerance: int = 0) -> Image.Image:
    difference = ImageChops.difference(image, Image.new("RGB", image.size, color))
    red, green, blue = difference.split()
    maximum = ImageChops.lighter(ImageChops.lighter(red, green), blue)
    mask = maximum.point(lambda value: 255 if value <= tolerance else 0)
    outside = Image.new("L", image.size)
    outside.paste(mask.crop(ROI), ROI[:2])
    return outside


def components(mask: Image.Image, min_pixels: int = 12) -> list[dict]:
    """4-connected components using row runs; bounding boxes are physical px."""
    width, height = mask.size
    raw = mask.tobytes()
    parent, statistics = [], []

    def root(index):
        while parent[index] != index:
            parent[index] = parent[parent[index]]
            index = parent[index]
        return index

    def union(first, second):
        first, second = root(first), root(second)
        if first != second:
            parent[second] = first
        return first

    previous = []
    for y in range(ROI[1], min(ROI[3], height)):
        row_start, row_end = y * width, (y + 1) * width
        cursor, current, previous_index = row_start, [], 0
        while cursor < row_end:
            start = raw.find(b"\xff", cursor, row_end)
            if start < 0:
                break
            end = raw.find(b"\x00", start, row_end)
            if end < 0:
                end = row_end
            left, right = start - row_start, end - row_start
            index = len(parent)
            parent.append(index)
            statistics.append((left, y, right, y + 1, right - left))
            while previous_index < len(previous) and previous[previous_index][1] <= left:
                previous_index += 1
            overlap = previous_index
            while overlap < len(previous) and previous[overlap][0] < right:
                union(index, previous[overlap][2])
                overlap += 1
            current.append((left, right, index))
            cursor = end
        previous = current
    merged = {}
    for index, (left, top, right, bottom, pixels) in enumerate(statistics):
        key = root(index)
        old = merged.get(key)
        if old is None:
            merged[key] = [left, top, right, bottom, pixels]
        else:
            old[0], old[1] = min(old[0], left), min(old[1], top)
            old[2], old[3], old[4] = max(old[2], right), max(old[3], bottom), old[4] + pixels
    return [{"bboxPx": box[:4], "pixelArea": box[4],
             "bboxDp": [round(number / 2.75, 3) for number in box[:4]]}
            for box in sorted(merged.values(), key=lambda item: (item[1], item[0]))
            if box[4] >= min_pixels]


def extract_regions(image: Image.Image) -> dict:
    flat = {}
    for name, color in PALETTE.items():
        regions = components(color_mask(image, color), min_pixels=20)
        # Small isolated antialiasing pixels are excluded. No role is inferred
        # from a color alone; e.g. a white region may be page, card or glyph.
        regions = [r for r in regions if (r["bboxPx"][2] - r["bboxPx"][0]) >= 8
                   and (r["bboxPx"][3] - r["bboxPx"][1]) >= 5]
        flat[name] = {"exactRgb": color, "tolerancePerChannel": 0,
                      "interpretation": "color component only; semantic role needs source/hierarchy",
                      "regions": regions[:120], "totalQualifyingRegions": len(regions)}
    channels = image.split()
    maximum = ImageChops.lighter(ImageChops.lighter(channels[0], channels[1]), channels[2])
    ink = maximum.point(lambda value: 255 if value <= 110 else 0)
    cropped = Image.new("L", image.size)
    cropped.paste(ink.crop(ROI), ROI[:2])
    expanded = cropped.filter(ImageFilter.MaxFilter(9))
    candidates = []
    for region in components(expanded, min_pixels=60):
        left, top, right, bottom = region["bboxPx"]
        # Refit to original ink so the 4px morphological expansion is not
        # reported as an observed text edge.
        fitted = cropped.crop((left, top, right, bottom)).getbbox()
        if fitted is None:
            continue
        bounds = [left + fitted[0], top + fitted[1], left + fitted[2], top + fitted[3]]
        w, h = bounds[2] - bounds[0], bounds[3] - bounds[1]
        if 10 <= h <= 160 and w >= 10:
            candidates.append({"bboxPx": bounds,
                               "bboxDp": [round(n / 2.75, 3) for n in bounds],
                               "classification": "text-or-icon-ink candidate; no OCR/semantic identification"})
    return {"flatColorRegions": flat, "inkRegions": candidates,
            "inkMethod": "RGB channel maximum <=110, 9px square dilation, refit to undilated ink; text and icons are not distinguished"}


def candidate_displacements(original: list[dict], reference: list[dict]) -> list[dict]:
    possibilities = []
    for oi, first in enumerate(original):
        a = first["bboxPx"]
        aw, ah = a[2] - a[0], a[3] - a[1]
        for ri, second in enumerate(reference):
            b = second["bboxPx"]
            bw, bh = b[2] - b[0], b[3] - b[1]
            dx, dy = (b[0] + b[2] - a[0] - a[2]) / 2, (b[1] + b[3] - a[1] - a[3]) / 2
            if abs(dx) <= 80 and abs(dy) <= 80 and max(aw, bw) <= 2 * min(aw, bw) and max(ah, bh) <= 2 * min(ah, bh):
                cost = abs(dx) + abs(dy) + abs(bw - aw) / 2 + abs(bh - ah) / 2
                possibilities.append((cost, oi, ri))
    used_original, used_reference, result = set(), set(), []
    for _, oi, ri in sorted(possibilities):
        if oi in used_original or ri in used_reference:
            continue
        used_original.add(oi)
        used_reference.add(ri)
        a, b = original[oi]["bboxPx"], reference[ri]["bboxPx"]
        delta = [b[index] - a[index] for index in range(4)]
        result.append({"originalRegion": oi, "referenceRegion": ri,
                       "originalBboxPx": a, "referenceBboxPx": b,
                       "edgeDeltaPx": delta, "edgeDeltaDp": [round(n / 2.75, 3) for n in delta],
                       "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"})
    return sorted(result, key=lambda item: (item["originalBboxPx"][1], item["originalBboxPx"][0]))


def save_image(image: Image.Image, path: Path) -> dict:
    image.save(path)
    return {"path": "generated/" + path.name, "sha256": sha256(path), "canvasPx": list(image.size)}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--baseline", type=Path, default=Path(__file__).resolve().parent.parent)
    parser.add_argument("--final-reference-override", type=Path, help="Reference root containing the three same-named page directories; never overwrites captures")
    parser.add_argument("--output-dir", type=Path, help="Separate report directory, e.g. comparison/final; preserves previous reports")
    options = parser.parse_args()
    baseline = options.baseline.resolve()
    template_root = Path(__file__).resolve().parent
    comparison = options.output_dir.resolve() if options.output_dir else template_root
    reference_root = options.final_reference_override.resolve() if options.final_reference_override else baseline / "reference"
    for page in PAGES:
        for category in ("runtime", "reference"):
            path = (reference_root if category == "reference" else baseline / "runtime") / page / "screen.png"
            if not path.is_file():
                parser.error(f"Missing {path}; capture all six originals/references before running.")
    comparison.mkdir(parents=True, exist_ok=True)
    generated = comparison / "generated"
    generated.mkdir(exist_ok=True)
    differences_path = comparison / "differences.json"
    if not differences_path.is_file():
        differences_path = template_root / "differences.json"
    differences = load_json(differences_path) if differences_path.is_file() else {"pages": {}}
    report = {
        "schemaVersion": 1, "generatedAtUtc": datetime.now(timezone.utc).isoformat(),
        "revision": "final" if comparison.name == "final" else "early-iteration",
        "comparisonRoiPx": list(ROI), "roiConvention": "left/top inclusive, right/bottom exclusive; fixed crop excludes y<96 and y>=2334; actual reference AOSP decor begins at136, so y96..136 can still include reference system-bar pixels",
        "conditions": {"canvasPx": list(CANVAS), "density": 2.75, "densityDpi": 440,
                       "locale": "zh-CN", "theme": "day", "fontScale": 1,
                       "conditionSource": "task baseline; dimensions independently checked; other settings require capture/environment records"},
        "limitations": ["No similarity score is produced.", "Ink boxes are not OCR or font baselines.",
                        "Geometric candidate pairing is not verified semantic correspondence.",
                        "Exact RGB regions identify flat-color pixels, not full rounded-control geometry.",
                        "Static screenshots cannot verify motion, gestures, state retention or async behavior.",
                        "REA screenshot records alone do not bind the foreground window to the APK; use the runtime identity evidence alongside them."],
        "pages": []}
    contracts_path = baseline / "contracts.json"
    if contracts_path.is_file():
        report["declaredSourceIdentity"] = load_json(contracts_path).get("source")
    for page in PAGES:
        paths = {"original": baseline / "runtime" / page / "screen.png", "reference": reference_root / page / "screen.png"}
        hashes = {key: sha256(path) for key, path in paths.items()}
        evidence = evidence_for(baseline / "runtime", page, comparison)
        reference_evidence = evidence_for(reference_root, page, comparison)
        if evidence.get("expectedSha256") and hashes["original"] != evidence["expectedSha256"]:
            raise ValueError(f"Original hash disagrees with retained REA evidence: {page}")
        if reference_evidence.get("expectedSha256") and hashes["reference"] != reference_evidence["expectedSha256"]:
            raise ValueError(f"Reference hash disagrees with retained REA evidence: {page}")
        reference_capture = None
        meta_path = reference_root / f"{page}.meta.json"
        if meta_path.is_file():
            meta = load_json(meta_path)
            if meta.get("sha256") and meta["sha256"] != hashes["reference"]:
                raise ValueError(f"Reference metadata hash mismatch: {page}")
            for key, expected in {"theme": "day", "locale": "zh-CN", "fontScale": 1, "density": 2.75, "width": 1080, "height": 2400}.items():
                if key in meta and meta[key] != expected:
                    raise ValueError(f"Reference environment mismatch: {page} {key}={meta[key]!r}, expected {expected!r}")
            reference_capture = {key: meta.get(key) for key in ("classification", "origin", "scene", "theme", "locale", "fontScale", "density", "width", "height", "apkSha256", "captureInterval", "sha256", "reaEvidenceId", "referenceHierarchy")}
            reference_capture["record"] = Path(os.path.relpath(meta_path, comparison)).as_posix()
            reference_capture["focusedWindow"] = ((meta.get("window") or {}).get("structuredContent") or {}).get("normalized_result")
        images = {}
        metadata = {}
        for key, path in paths.items():
            with Image.open(path) as opened:
                if opened.size != CANVAS:
                    raise ValueError(f"{path}: expected {CANVAS}, received {opened.size}; never silently resize.")
                metadata[key] = {"path": Path(os.path.relpath(path, comparison)).as_posix(),
                                 "sha256": hashes[key], "canvasPx": list(opened.size), "sourceMode": opened.mode,
                                 "pngMetadata": {name: str(value) for name, value in opened.info.items()}}
                images[key] = opened.convert("RGB")
        pair = Image.new("RGB", (CANVAS[0] * 2, CANVAS[1]))
        pair.paste(images["original"], (0, 0))
        pair.paste(images["reference"], (CANVAS[0], 0))
        overlay = Image.blend(images["original"], images["reference"], 0.5)
        absolute = ImageChops.difference(images["original"], images["reference"])
        difference = Image.new("RGB", CANVAS)
        difference.paste(absolute.crop(ROI), ROI[:2])
        amplified = difference.point(lambda value: min(value * 3, 255))
        region_data = {key: extract_regions(image) for key, image in images.items()}
        page_difference = differences.get("pages", {}).get(page, {})
        item = {"id": page, "label": LABELS[page], "sources": metadata, "originalEvidence": evidence, "referenceEvidence": reference_evidence,
                "referenceCaptureMetadata": reference_capture,
                "images": {"sideBySide": save_image(pair, generated / f"{page}-side-by-side.png"),
                           "overlay": save_image(overlay, generated / f"{page}-overlay.png"),
                           "difference": save_image(amplified, generated / f"{page}-difference-x3.png"),
                           "rawDifference": save_image(difference, generated / f"{page}-difference-raw.png")},
                "overlayBlendWeight": 0.5, "differenceDisplayAmplification": 3,
                "knownDifferences": page_difference.get("knownDifferences", []),
                "matchedDetails": page_difference.get("matchedDetails", []),
                "unknowns": page_difference.get("unknowns", []),
                "manualReviewStatus": page_difference.get("reviewStatus", "pending"),
                "regions": region_data,
                "inkPositionCandidates": candidate_displacements(region_data["original"]["inkRegions"], region_data["reference"]["inkRegions"])}
        reviewed_hash = page_difference.get("reviewReferenceSha256")
        reviewed_original_hash = page_difference.get("reviewOriginalSha256")
        item["manualReviewInputMatches"] = (reviewed_hash == hashes["reference"] and (not reviewed_original_hash or reviewed_original_hash == hashes["original"])) if reviewed_hash else None
        if reviewed_hash and item["manualReviewInputMatches"] is False:
            item["priorManualReview"] = page_difference
            item["knownDifferences"], item["matchedDetails"] = [], []
            item["unknowns"] = ["An input PNG changed since manual review; prior findings are retained as historical data, and the current screenshots require review."]
            item["manualReviewStatus"] = "stale-needs-review"
        crop_config_path = template_root / "component-crops.json"
        item["components"] = []
        if crop_config_path.is_file():
            for component in load_json(crop_config_path)["components"]:
                if component["page"] != page:
                    continue
                box = component["cropBoxPx"]
                cropped = {key: image.crop(box) for key, image in images.items()}
                extent = cropped["original"].size
                paired = Image.new("RGB", (extent[0] * 2, extent[1]))
                paired.paste(cropped["original"], (0, 0))
                paired.paste(cropped["reference"], (extent[0], 0))
                name = component["id"]
                item["components"].append({**component, "classification": "derived crop: A original runtime vs D reference runtime",
                    "sourceSha256": hashes, "originalEvidenceId": evidence.get("evidenceId"),
                    "referenceEvidenceId": reference_evidence.get("evidenceId"),
                    "images": {"sideBySide": save_image(paired, generated / f"component-{name}-side-by-side.png"),
                        "overlay": save_image(Image.blend(cropped["original"], cropped["reference"], 0.5), generated / f"component-{name}-overlay.png"),
                        "difference": save_image(ImageChops.difference(cropped["original"], cropped["reference"]).point(lambda v: min(v * 3, 255)), generated / f"component-{name}-difference-x3.png")}})
        report["pages"].append(item)
        for key, path in paths.items():
            if sha256(path) != hashes[key]:
                raise RuntimeError(f"Input changed during comparison: {path}; discard this run and retry on stable captures.")
    encoded = json.dumps(report, ensure_ascii=False, indent=2)
    (comparison / "report.json").write_text(encoded + "\n", encoding="utf-8")
    # A script assignment works directly under file:// without fetch/CORS or a server.
    (comparison / "report-data.js").write_text("window.COMPARISON_REPORT = " + encoded.replace("</", "<\\/") + ";\n", encoding="utf-8")
    if comparison != template_root:
        template = (template_root / "index.html").read_text(encoding="utf-8")
        if report["revision"] == "final":
            start, end = "<!-- revision-banner-start -->", "<!-- revision-banner-end -->"
            if start in template and end in template:
                before, rest = template.split(start, 1)
                _, after = rest.split(end, 1)
                template = before + start + '\n<aside style="padding:16px;background:#e8f5ed;border:2px solid #247a45;border-radius:12px;margin-bottom:20px"><strong>当前：最终三页 / 十组件图像对照</strong><p>参考 APK 与每页截图哈希、实际差异和未知见下方。最终表示交付快照，不表示与原版完全同等。</p><a href="../index.html">查看早期迭代（非最终依据）</a></aside>\n' + end + after
        (comparison / "index.html").write_text(template, encoding="utf-8")
    print(f"Wrote {comparison / 'index.html'} and report.json; source PNG hashes verified unchanged.")


if __name__ == "__main__":
    main()
