#!/usr/bin/env python3
"""Generate reviewable media bindings and a local image contact sheet."""
import datetime as dt
import hashlib
import json
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

HERE = Path(__file__).resolve().parent


def write_json(path, obj):
    path.write_text(json.dumps(obj, ensure_ascii=False, indent=2) + "\n")


def main():
    j = json.loads((HERE / "manifest.json").read_text())
    assets = {i["url"]: i for i in j["items"]}
    representation_path = HERE / "negotiated-representations.json"
    representations = json.loads(representation_path.read_text()) if representation_path.exists() else {"items": []}
    declared_encoding = {i["url"]: i for i in representations["items"] if i.get("acquisition", {}).get("status") == "saved"}

    def local_file(url):
        i = assets.get(url)
        if not i:
            return {"url": url, "status": "null-or-not-in-inventory"}
        result = {k: i[k] for k in ["url", "id", "path", "bytes", "sha256", "contentType", "metadata"] if k in i} | {
            "status": i.get("acquisition", {}).get("status", "not-attempted")}
        if url in declared_encoding:
            alt = declared_encoding[url]
            result.update({"preferredPath": alt["path"], "preferredContentType": alt["contentType"],
                           "declaredEncodingRepresentation": {k: alt[k] for k in ["id", "path", "bytes", "sha256", "contentType", "metadata"]},
                           "defaultResponseNote": "Base path retains the default Accept */* PNG response. preferredPath preserves the requested declared WebP representation."})
        return result

    slides = []
    for b in j["bindings"]:
        if b.get("kind") != "carousel-slide":
            continue
        d = b["declaration"]
        slides.append({"slideIndex": b["slideIndex"], "key": d.get("_key"), "prompt": d.get("prompt"),
            "sourceRef": {k: b[k] for k in ["path", "sha256", "flightRecord", "jsonPointer", "classification"]},
            "metadata": json.loads(d["metadata"]) if isinstance(d.get("metadata"), str) else d.get("metadata"),
            "video": local_file(d.get("videoAsset", {}).get("file", {}).get("src")),
            "safariVideo": local_file(d.get("videoAssetMac", {}).get("file", {}).get("src")),
            "poster": local_file(d.get("videoAsset", {}).get("imageFile", {}).get("src")),
            "safariPosterDeclaration": d.get("videoAssetMac", {}).get("imageFile"),
            "runtimeTest": False})
    write_json(HERE / "carousel-bindings.json", {"classification": "B: decoded current public homepage server declaration; not carousel runtime",
        "sourceReceipt": "source/home.json", "slides": slides,
        "note": "HoldMs, crop geometry and platform variants describe authored content; timing, platform selection and seek behavior have not been exercised in this pass."})

    features = []
    for b in j["bindings"]:
        if b.get("path") != "current-public-media/source/home.html" or not b.get("declaredFile", {}).get("src"):
            continue
        context = b.get("context", [])
        feature = next((c for c in context if c.get("_type") == "feature"), None)
        if feature:
            features.append({"sourceRef": {k: b[k] for k in ["path", "sha256", "flightRecord", "jsonPointer", "classification"]},
                             "feature": feature, "asset": local_file(b["declaredFile"]["src"]),
                             "declaredFile": b["declaredFile"]})
    write_json(HERE / "feature-bindings.json", {"classification": "B: declared current homepage media mapping", "bindings": features})
    write_json(HERE / "gaps.json", {
        "downloadGaps": [{"url": i["url"], "kind": i["kind"], "acquisition": i.get("acquisition", {}),
                          "boundTo": sorted(set(r["path"] for r in i["sourceRefs"]))}
                         for i in j["items"] if i.get("acquisition", {}).get("status") != "saved"],
        "capturedMissingSrc": j["missingSrc"],
        "declaredNullFiles": [b for b in j["bindings"] if "declaredFile" in b and not b["declaredFile"].get("src")],
        "presentationVariantBytes": "Original payloads are saved. Declared query variants are catalogued, not exhaustively downloaded.",
        "declaredEncodingGap": {"status": "resolved" if len(declared_encoding) == 15 else "partial",
                                "manifest": "negotiated-representations.json", "locators": len(declared_encoding),
                                "retainedDefaultPngResponses": 15, "retainedNegotiatedWebpResponses": len(declared_encoding),
                                "method": "same exact URLs with explicit Accept: image/webp; no additional encoding/query matrix"},
        "dynamicCarousel": "Not tested by this pass; static bindings/holdMs do not prove playback.",
        "coverage": "Bounded to 19 saved public subpages, one current public homepage, home inventory, and registered public JS/CSS; not an assertion about all assets held by Hark.",
        "privateMedia": "Not requested; no application API, authenticated profile or historical clone media used."})

    images = [i for i in j["items"] if i["kind"] == "image" and i.get("acquisition", {}).get("status") == "saved"]
    images += [{**i, "sourceRefs": [], "id": i["baseLocatorId"] + "--WebP"}
               for i in representations["items"] if i.get("acquisition", {}).get("status") == "saved"]
    font_path = Path("/System/Library/Fonts/Supplemental/Arial.ttf")
    font = ImageFont.truetype(str(font_path), 12) if font_path.exists() else ImageFont.load_default()
    tile_w, tile_h, cols = 340, 250, 4
    pages = []
    for page_index in range(0, len(images), 20):
        group = images[page_index:page_index+20]
        sheet = Image.new("RGB", (tile_w*cols, tile_h*((len(group)+cols-1)//cols)), "#dededb")
        draw = ImageDraw.Draw(sheet)
        entries = []
        for index, item in enumerate(group):
            x = (index % cols)*tile_w
            y = (index // cols)*tile_h
            # Checkerboard exposes transparency without changing the source.
            for tx in range(0, tile_w, 12):
                for ty in range(0, 210, 12):
                    draw.rectangle((x+tx,y+ty,x+tx+11,y+ty+11), fill="#f8f8f8" if (tx//12+ty//12)%2 == 0 else "#eeeeee")
            with Image.open(HERE / item["path"]) as im:
                im = im.convert("RGBA")
                im.thumbnail((tile_w-12, 200))
                sheet.paste(im, (x+(tile_w-im.width)//2, y+(210-im.height)//2), im)
            label = item["id"][:38]
            names = [r.get("declaredFile", {}).get("originalFilename") for r in item["sourceRefs"]]
            original_name = next((n for n in names if n), label)
            draw.text((x+6,y+214), original_name[:45], font=font, fill="#222222")
            draw.text((x+6,y+232), f'{item["metadata"]["width"]} × {item["metadata"]["height"]} | {label[:20]}', font=font, fill="#222222")
            entries.append({"tileIndex": index, "id": item["id"], "path": item["path"], "sha256": item["sha256"]})
        dest = HERE / f"images-contact-sheet-{page_index//20+1}.jpg"
        sheet.save(dest, quality=92)
        pages.append({"path": dest.name, "entries": entries,
                      "classification": "D: derived inspection sheet of unchanged B source files"})
    write_json(HERE / "image-review-index.json", {"pages": pages, "sourceFilesUnmodified": True,
              "reviewStatus": "contact-sheets-generated; pixel review recorded separately"})
    print(json.dumps({"slides": len(slides), "featureFiles": len(features), "imageContactSheets": len(pages)}))


if __name__ == "__main__":
    main()
