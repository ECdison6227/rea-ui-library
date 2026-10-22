#!/usr/bin/env python3
"""Verify retained current public media, exact source hashes and local bindings."""
import datetime as dt
import hashlib
import json
from pathlib import Path
import sys

HERE = Path(__file__).resolve().parent
STUDY = HERE.parent


def main():
    j = json.loads((HERE / "manifest.json").read_text())
    errors = []
    total = 0
    count = 0
    results = []
    for source in j["sources"]:
        p = STUDY / source["path"]
        payload = p.read_bytes()
        ok = len(payload) == source["bytes"] and hashlib.sha256(payload).hexdigest() == source["sha256"]
        if not ok:
            errors.append("source hash/size mismatch: " + source["path"])
        results.append({"path": source["path"], "kind": "source", "ok": ok})
    urls = set()
    for item in j["items"]:
        if item["url"] in urls:
            errors.append("duplicate original URL: " + item["url"])
        urls.add(item["url"])
        if not item["sourceRefs"]:
            errors.append("unbound original: " + item["url"])
        if item.get("acquisition", {}).get("status") != "saved":
            continue
        p = HERE / item["path"]
        payload = p.read_bytes()
        ok = len(payload) == item["bytes"] and hashlib.sha256(payload).hexdigest() == item["sha256"]
        if not ok:
            errors.append("original hash/size mismatch: " + item["path"])
        metadata = item["metadata"]
        if not (metadata.get("width", 0) > 0 and metadata.get("height", 0) > 0):
            errors.append("missing pixel dimensions: " + item["path"])
        if item["kind"] == "video" and metadata.get("durationSeconds", 0) <= 0:
            errors.append("missing positive video duration: " + item["path"])
        if item["kind"] == "image":
            from PIL import Image
            with Image.open(p) as im:
                im.load()
                if [im.width, im.height] != [metadata["width"], metadata["height"]]:
                    errors.append("image dimensions mismatch: " + item["path"])
        count += 1
        total += len(payload)
        results.append({"path": item["path"], "kind": item["kind"], "ok": ok})
    if count > 100 or total > 1_000_000_000:
        errors.append("media budget exceeded")
    if count != j["summary"]["savedFiles"] or total != j["summary"]["savedBytes"]:
        errors.append("summary count/bytes mismatch")
    if {str(p.relative_to(HERE)) for p in (HERE/"originals").glob("*")} != {i["path"] for i in j["items"] if i.get("acquisition", {}).get("status") == "saved"}:
        errors.append("extra or missing originals outside manifest")
    negotiated = json.loads((HERE/"negotiated-representations.json").read_text())
    negotiated_bytes = 0
    for item in negotiated["items"]:
        p = HERE / item["path"]
        payload = p.read_bytes()
        ok = len(payload) == item["bytes"] and hashlib.sha256(payload).hexdigest() == item["sha256"]
        if not ok:
            errors.append("negotiated representation hash/size mismatch: " + item["path"])
        if item["url"] not in urls or item["acquisition"]["requestHeaders"] != {"Accept": "image/webp"}:
            errors.append("negotiated representation outside declared URL/Accept boundary")
        from PIL import Image
        with Image.open(p) as im:
            im.load()
            if im.format != "WEBP" or [im.width,im.height] != [item["metadata"]["width"], item["metadata"]["height"]]:
                errors.append("negotiated WebP format/dimensions mismatch")
        negotiated_bytes += len(payload)
        results.append({"path": item["path"], "kind": "negotiated-image-webp", "ok": ok})
    if len(negotiated["items"]) != 15:
        errors.append("15 declared WebP responses were not retained")
    if count + len(negotiated["items"]) > 110 or total + negotiated_bytes > 1_000_000_000:
        errors.append("combined response budget exceeded")
    if {str(p.relative_to(HERE)) for p in (HERE/"negotiated-webp").glob("*")} != {i["path"] for i in negotiated["items"]}:
        errors.append("extra or missing negotiated WebP files")
    slides = json.loads((HERE/"carousel-bindings.json").read_text())["slides"]
    if len(slides) != 10 or [s["slideIndex"] for s in slides] != list(range(10)):
        errors.append("current homepage carousel does not have expected 10 unique ordered declarations")
    for slide in slides:
        for role in ["video", "safariVideo", "poster"]:
            if slide[role]["url"] not in urls:
                errors.append(f"slide {slide['slideIndex']} {role} URL absent from inventory")
    report = {"verifiedAt": dt.datetime.now(dt.timezone.utc).isoformat(), "passed": not errors,
              "savedFiles": count, "savedBytes": total, "sourceFiles": len(j["sources"]),
              "negotiatedWebpFiles": len(negotiated["items"]), "totalMediaResponseFiles": count + len(negotiated["items"]),
              "totalMediaBytes": total + negotiated_bytes,
              "carouselBindings": len(slides), "errors": errors, "items": results,
              "boundary": "Integrity/decoding/source binding checks only; no source-app playback or carousel interaction tested."}
    (HERE/"verification.json").write_text(json.dumps(report, indent=2)+"\n")
    print(json.dumps({k:v for k,v in report.items() if k != "items"}))
    return 0 if not errors else 1


if __name__ == "__main__":
    sys.exit(main())
