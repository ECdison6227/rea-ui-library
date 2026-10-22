#!/usr/bin/env python3
"""Bounded, credential-free acquisition of explicit current public media locators.

Reads captured public evidence only. It never requests an application/API route.
The local evidence, not this script, establishes page/component bindings.
"""
from __future__ import annotations

import argparse
import datetime as dt
import hashlib
import html
from html.parser import HTMLParser
import json
from pathlib import Path
import re
import subprocess
import urllib.error
import urllib.parse
import urllib.request

HERE = Path(__file__).resolve().parent
STUDY = HERE.parent
PUBLIC = STUDY / "public"
MAX_FILES = 110
MAX_BYTES = 1_000_000_000
MAX_SINGLE_FILE_BYTES = 400_000_000
MEDIA_RE = re.compile(r'https?://[^\\\s<>"\x27`]+')
EXT_RE = re.compile(r'\.(?:png|jpg|jpeg|webp|avif|gif|mp4|mov|webm)(?:\?|$)', re.I)
ICON_ID = "c2637663473cf26d5d679cde0db07eb9b63aff47"


def utc():
    return dt.datetime.now(dt.timezone.utc).isoformat().replace("+00:00", "Z")


def sha(b):
    return hashlib.sha256(b).hexdigest()


def write_json(path, obj):
    path.write_text(json.dumps(obj, ensure_ascii=False, indent=2) + "\n")


class Document(HTMLParser):
    def __init__(self):
        super().__init__(convert_charrefs=True)
        self.active = False
        self.script_start = 0
        self.scripts = []
        self.media = []
        self.element_index = 0

    def handle_starttag(self, tag, attrs):
        self.element_index += 1
        attrs = dict(attrs)
        self.active = tag == "script"
        if tag in {"img", "source", "video", "audio"}:
            self.media.append({"tag": tag, "elementIndex": self.element_index,
                               "attributes": attrs, "classification": "A-captured-DOM"})

    def handle_endtag(self, tag):
        if tag == "script":
            self.active = False

    def handle_data(self, data):
        if self.active:
            self.scripts.append(data)


def flight_records(doc):
    """Decode JSON transport strings and JSON records without executing scripts."""
    stream = ""
    for script in doc.scripts:
        prefix = "self.__next_f.push("
        if script.startswith(prefix):
            try:
                item = json.loads(script[len(prefix):-1])
                if len(item) > 1 and isinstance(item[1], str):
                    stream += item[1]
            except (ValueError, TypeError):
                pass
    # Flight text records contain arbitrary line breaks. Find only JSON-decodable
    # records and consume the whole value, preventing matches inside that value.
    decoder = json.JSONDecoder()
    record_re = re.compile(r'([0-9a-f]+):(?=[\[{"])')
    cursor = 0
    records = []
    while match := record_re.search(stream, cursor):
        try:
            obj, used = decoder.raw_decode(stream[match.end():])
        except ValueError:
            cursor = match.end()
            continue
        records.append((match.group(1), match.start(), obj))
        cursor = match.end() + used
    return stream, records


def walk(obj, path="", ancestors=()):
    yield path, obj, ancestors
    if isinstance(obj, dict):
        for key, value in obj.items():
            yield from walk(value, path + "/" + str(key), ancestors + (obj,))
    elif isinstance(obj, list):
        for key, value in enumerate(obj):
            yield from walk(value, path + "/" + str(key), ancestors)


def permitted(url):
    p = urllib.parse.urlsplit(url)
    return p.scheme == "https" and not p.username and not p.password and (
        (p.hostname == "cdn.sanity.io" and
         re.fullmatch(r'/(?:images|files)/w2mhvsaj/production/[a-f0-9]+(?:-\d+x\d+)?\.[a-z0-9]+', p.path))
        or (p.hostname == "hark.com" and p.path in {"/images/avatar.png", "/images/site-image.jpg"})
    )


def collect():
    assets = {}
    sources = []
    bindings = []
    missing = []
    exclusions = []
    parse_receipts = []

    def add(url, ref):
        if not isinstance(url, str) or not EXT_RE.search(url):
            return
        url = html.unescape(url).replace("\\/", "/")
        if ICON_ID in url or "favicon" in url:
            exclusions.append({"url": url, "reason": "icon; excluded from this media-only pass"})
            return
        if not permitted(url):
            exclusions.append({"url": url, "reason": "outside explicit allowed public-static URL boundary"})
            return
        original = urllib.parse.urlunsplit(urllib.parse.urlsplit(url)._replace(query="", fragment=""))
        item = assets.setdefault(original, {"url": original, "variants": {}, "sourceRefs": []})
        variant = item["variants"].setdefault(url, {"url": url, "query": urllib.parse.parse_qs(urllib.parse.urlsplit(url).query), "sourceRefs": []})
        if ref not in variant["sourceRefs"]:
            variant["sourceRefs"].append(ref)
        if ref not in item["sourceRefs"]:
            item["sourceRefs"].append(ref)

    source_paths = [*sorted((PUBLIC / "pages").glob("*.html")),
                    *sorted((PUBLIC / "resources").glob("*.js")),
                    *sorted((PUBLIC / "resources").glob("*.css")),
                    PUBLIC / "home-inventory.json", PUBLIC / "static-analysis.json"]
    # An independent coordinator may add a fresh anonymous homepage snapshot here.
    source_paths += sorted((HERE / "source").glob("*.html")) if (HERE / "source").exists() else []
    for path in source_paths:
        raw = path.read_bytes()
        text = raw.decode("utf-8")
        rel = str(path.relative_to(STUDY))
        source = {"path": rel, "bytes": len(raw), "sha256": sha(raw)}
        receipt_file = path.with_suffix(".json")
        if path.suffix == ".html" and receipt_file.exists():
            try:
                receipt_doc = json.loads(receipt_file.read_text())
                receipt = receipt_doc.get("receipt", receipt_doc)
                source["originalCaptureWindow"] = {k: receipt[k] for k in ["startedAt", "completedAt"] if k in receipt}
                if "capturedAt" in receipt:
                    source["originalCaptureWindow"]["capturedAt"] = receipt["capturedAt"]
                source["receiptPath"] = str(receipt_file.relative_to(STUDY))
            except ValueError:
                pass
        sources.append(source)
        # Retain a binding snippet at each first URL occurrence per raw source.
        normalized = html.unescape(text).replace('\\"', '"').replace("\\/", "/")
        seen = set()
        for match in MEDIA_RE.finditer(normalized):
            url = match.group(0).rstrip(");,]")
            if url in seen:
                continue
            seen.add(url)
            add(url, {"path": rel, "sha256": source["sha256"],
                      "normalizedCharacterOffset": match.start(),
                      "snippet": normalized[max(0, match.start()-120):match.end()+120],
                      "classification": "B-declared-in-captured-source"})

        if path.suffix == ".css":
            for m in re.finditer(r'url\((?:["\x27])?(/images/[^)"\x27]+)', text):
                add("https://hark.com" + m.group(1), {"path": rel, "sha256": source["sha256"],
                    "rawCharacterOffset": m.start(), "snippet": text[max(0,m.start()-80):m.end()+80],
                    "classification": "B-declared-css-asset"})

        if path.suffix == ".html":
            doc = Document()
            doc.feed(text)
            stream, records = flight_records(doc)
            parse_receipts.append({"path": rel, "decodedFlightCharacters": len(stream),
                                   "decodedJsonRecords": len(records), "domMediaElements": len(doc.media)})
            for element in doc.media:
                server_response = path.parent == HERE / "source"
                element["classification"] = "B-server-HTML-declaration" if server_response else "A-inherited-captured-DOM"
                attrs = element["attributes"]
                if element["tag"] in {"source", "video"} and not attrs.get("src"):
                    missing.append({"path": rel, **element,
                                    "meaning": ("src absent/empty in anonymously fetched server HTML; not a runtime DOM observation" if server_response
                                                else "src absent/empty at original DOM capture; hydration declaration is separate evidence")})
            for record, offset, obj in records:
                for key, value, ancestors in walk(obj):
                    if not isinstance(value, dict):
                        continue
                    if "src" in value and ("mimeType" in value or "originalFilename" in value):
                        context = [{k: a[k] for k in ["_type", "_key", "name", "heading", "prompt", "assetType", "layout"] if k in a}
                                   for a in ancestors if any(k in a for k in ["_type", "_key", "name", "heading", "prompt"])]
                        ref = {"path": rel, "sha256": source["sha256"], "flightRecord": record,
                               "flightRecordOffset": offset, "jsonPointer": key,
                               "classification": "B-decoded-server-declaration", "declaredFile": value,
                               "context": context[-5:]}
                        bindings.append(ref)
                        add(value.get("src"), ref)
                    if "slides" in value and isinstance(value["slides"], list):
                        for index, slide in enumerate(value["slides"]):
                            bindings.append({"kind": "carousel-slide", "path": rel,
                                "sha256": source["sha256"], "flightRecord": record,
                                "jsonPointer": key + "/slides/" + str(index), "slideIndex": index,
                                "classification": "B-decoded-server-declaration", "declaration": slide})
        if path.name == "home-inventory.json":
            inventory = json.loads(text)
            for index, item in enumerate(inventory.get("media", [])):
                ref = {"path": rel, "sha256": source["sha256"], "jsonPointer": "/media/"+str(index),
                       "classification": "A-inherited-current-page-observation", **item}
                if item.get("src"):
                    add(item["src"], ref)
                elif item.get("tag") in {"VIDEO", "SOURCE"}:
                    missing.append({**ref, "meaning": "src null at original home DOM capture; not evidence that the CMS has no asset"})

    items = []
    for url, item in sorted(assets.items()):
        item["variants"] = list(item["variants"].values())
        item["id"] = Path(urllib.parse.urlsplit(url).path).stem
        item["kind"] = "video" if EXT_RE.search(url).group(0).lower().lstrip(".").split("?")[0] in {"mp4", "mov", "webm"} else "image"
        item["path"] = "originals/" + Path(urllib.parse.urlsplit(url).path).name
        if urllib.parse.urlsplit(url).hostname == "hark.com":
            item["path"] = "originals/hark-" + Path(urllib.parse.urlsplit(url).path).name
        item["originalLocatorMethod"] = "explicit base URL retained from captured source; query variants preserved separately"
        if not any(v["url"] == url for v in item["variants"]):
            item["originalLocatorMethod"] = "same explicit public CDN asset path with presentation query removed; no API/path inference"
        items.append(item)
    return {"sources": sources, "items": items, "bindings": bindings,
            "missingSrc": missing, "exclusions": list({json.dumps(x, sort_keys=True): x for x in exclusions}.values()),
            "parseReceipts": parse_receipts}


class StaticRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        if not permitted(newurl):
            raise urllib.error.URLError("redirect outside allowed public-static boundary")
        return super().redirect_request(req, fp, code, msg, headers, newurl)


def inspect_file(path, kind):
    if kind == "image":
        from PIL import Image
        with Image.open(path) as im:
            im.load()
            return {"width": im.width, "height": im.height, "format": im.format,
                    "mode": im.mode, "frames": getattr(im, "n_frames", 1), "method": "Pillow header/payload decoding"}
    proc = subprocess.run(["/opt/homebrew/bin/ffprobe", "-v", "error", "-show_format", "-show_streams", "-of", "json", str(path)],
                          capture_output=True, text=True, timeout=45, check=True)
    probe = json.loads(proc.stdout)
    video = next((s for s in probe.get("streams", []) if s.get("codec_type") == "video"), {})
    return {"width": video.get("width"), "height": video.get("height"),
            "durationSeconds": float(probe.get("format", {}).get("duration", 0)),
            "codec": video.get("codec_name"), "pixelFormat": video.get("pix_fmt"),
            "frameRate": video.get("avg_frame_rate"),
            "audioStreams": sum(s.get("codec_type") == "audio" for s in probe.get("streams", [])),
            "method": "local ffprobe -show_format -show_streams", "rawProbe": probe}


def acquire(inventory):
    previous_path = HERE / "manifest.json"
    previous = json.loads(previous_path.read_text()) if previous_path.exists() else {}
    previous_items = {x["url"]: x for x in previous.get("items", [])}
    # Account for every retained file before downloading anything, including
    # files that will occur later in the priority order during a resumed pass.
    count = 0
    total = 0
    for old in previous_items.values():
        if old.get("acquisition", {}).get("status") == "saved":
            old_path = HERE / old["path"]
            payload = old_path.read_bytes()
            if len(payload) != old["bytes"] or sha(payload) != old["sha256"]:
                raise RuntimeError("existing original hash/size mismatch: " + str(old_path))
            total += len(payload)
            count += 1
    # The standard environment-only proxy selection can discard the active
    # macOS system proxy when NO_PROXY alone is set. Reuse the configured normal
    # system HTTPS route; no proxy credentials are read or recorded.
    system_proxy_fn = getattr(urllib.request, "getproxies_macosx_sysconf", None)
    system_proxy = system_proxy_fn() if system_proxy_fn else {}
    proxy = system_proxy.get("https")
    handlers = [StaticRedirect()]
    proxy_route = None
    if proxy:
        proxy_url = urllib.parse.urlsplit(proxy if "://" in proxy else "http://" + proxy)
        if proxy_url.username or proxy_url.password:
            raise RuntimeError("credential-bearing system proxy outside this acquisition boundary")
        proxy_route = f"{proxy_url.hostname}:{proxy_url.port} (existing macOS system HTTPS proxy)"
        handlers.append(urllib.request.ProxyHandler({"https": proxy_url.geturl()}))
    opener = urllib.request.build_opener(*handlers)
    # Resolve all images first, then the actual observed home video, then other videos.
    def priority(item):
        home = any(r["path"] == "public/home-inventory.json" for r in item["sourceRefs"])
        home_declaration = any(r["path"] == "current-public-media/source/home.html" for r in item["sourceRefs"])
        if item["kind"] == "image":
            group = 0
        elif home:
            group = 1
        elif home_declaration and item["url"].endswith(".webm"):
            group = 2
        elif item["url"].endswith(".mov"):
            group = 4
        else:
            group = 3
        return (group, item["url"])
    items = sorted(inventory["items"], key=priority)
    # Checkpoints must retain later-in-order old receipts, so an interrupted
    # resumed run cannot lose the identity of files it has already kept.
    for item in items:
        old = previous_items.get(item["url"], {})
        if old.get("acquisition", {}).get("status") == "saved":
            item.update({k: old[k] for k in ["bytes", "sha256", "contentType", "acquisition", "metadata"]})
    (HERE / "originals").mkdir(exist_ok=True)
    for item in items:
        dest = HERE / item["path"]
        old = previous_items.get(item["url"], {})
        if old.get("acquisition", {}).get("status") == "saved" and dest.exists():
            payload = dest.read_bytes()
            if sha(payload) != old["sha256"]:
                raise RuntimeError("existing original hash mismatch: " + str(dest))
            item.update({k: old[k] for k in ["bytes", "sha256", "contentType", "acquisition", "metadata"]})
            continue
        capture = {"method": "anonymous urllib HTTPS GET; no Cookie/Authorization/client profile", "startedAt": utc(),
                   "networkRoute": proxy_route or "default system route"}
        if old.get("acquisition"):
            capture["previousAttempts"] = old["acquisition"].get("previousAttempts", []) + [
                {k:v for k,v in old["acquisition"].items() if k != "previousAttempts"}]
        if count >= MAX_FILES:
            item["acquisition"] = {**capture, "status": "budget-skipped", "reason": f"{MAX_FILES} saved media file ceiling"}
            continue
        try:
            request = urllib.request.Request(item["url"], headers={"User-Agent": "Hark-Public-Design-Research/1.0", "Accept": "*/*"})
            with opener.open(request, timeout=45) as response:
                capture.update({"httpStatus": response.status, "finalUrl": response.url,
                                "responseHeaders": {k.lower(): v for k,v in response.headers.items()
                                                    if k.lower() in {"content-type", "content-length", "etag", "last-modified", "date", "accept-ranges"}}})
                size = int(response.headers.get("Content-Length", "0"))
                if size > MAX_SINGLE_FILE_BYTES:
                    item["acquisition"] = {**capture, "status": "budget-skipped", "reason": "single media file exceeds 400000000 bytes; coordinator decision required", "completedAt": utc()}
                    print("SINGLE_FILE_BOUNDARY", item["id"], size, flush=True)
                    continue
                if size and total + size > MAX_BYTES:
                    item["acquisition"] = {**capture, "status": "budget-skipped", "reason": f"declared response would exceed {MAX_BYTES} retained bytes", "completedAt": utc()}
                    print("BUDGET", item["id"], size, flush=True)
                    continue
                payload = bytearray()
                while chunk := response.read(1024 * 1024):
                    if total + len(payload) + len(chunk) > MAX_BYTES:
                        raise RuntimeError(f"stream would exceed {MAX_BYTES} retained bytes; partial not saved")
                    if len(payload) + len(chunk) > MAX_SINGLE_FILE_BYTES:
                        raise RuntimeError("stream would exceed 400000000 single-file bytes; coordinator decision required, partial not saved")
                    payload.extend(chunk)
                if size and len(payload) != size:
                    raise RuntimeError("Content-Length mismatch")
                content_type = response.headers.get_content_type()
            dest.write_bytes(payload)
            item.update({"bytes": len(payload), "sha256": sha(payload), "contentType": content_type,
                         "metadata": inspect_file(dest, item["kind"]),
                         "acquisition": {**capture, "status": "saved", "completedAt": utc()}})
            total += len(payload)
            count += 1
            print("SAVED", item["kind"], item["id"], len(payload), flush=True)
        except Exception as error:
            if isinstance(error, urllib.error.HTTPError):
                capture.update({"httpStatus": error.code, "finalUrl": error.url})
            item["acquisition"] = {**capture, "status": "failed", "error": str(error), "completedAt": utc()}
            print("FAILED", item["id"], str(error), flush=True)
        # Preserve progress in case the subprocess or network is interrupted.
        inventory["items"] = items
        write_json(previous_path, inventory)
    inventory["items"] = sorted(items, key=lambda x: x["url"])
    inventory["summary"] = {"savedFiles": count, "savedBytes": total,
                            "discoveredOriginals": len(items), "declaredVariants": sum(len(x["variants"]) for x in items),
                            "missingSrcRecords": len(inventory["missingSrc"]),
                            "carouselSlideBindings": sum(x.get("kind") == "carousel-slide" for x in inventory["bindings"]),
                            "statuses": {status: sum(x.get("acquisition", {}).get("status") == status for x in items)
                                         for status in ["saved", "failed", "budget-skipped"]}}
    return inventory


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--inventory-only", action="store_true")
    args = parser.parse_args()
    inventory = collect()
    inventory.update({"schemaVersion": 1, "kind": "current-public-media-static-acquisition", "generatedAt": utc(),
        "scope": {"source": "captured 2026-10-22 public HTML/inventory and registered JS/CSS", "apiRequests": False,
                  "browserControl": False, "credentials": False, "dynamicCarouselTest": False,
                  "budget": {"mediaFiles": MAX_FILES, "mediaBytes": MAX_BYTES, "singleFileReviewBytes": MAX_SINGLE_FILE_BYTES,
                             "history": "Initial 100-file/250000000-byte planning budget raised by coordinator to 110 response files/1000000000 bytes: 92 explicit public locators plus 15 declared-WebP content-negotiated representations. No further Accept matrix."},
                  "note": "Public CDN originals only; current-app and old clone media are not acquisition sources."},
        "classification": "B: captured page media declarations and static GET payloads; A only inherited, explicitly named page inventory/DOM observations", "runtimeClaims": []})
    if args.inventory_only:
        write_json(HERE / "inventory.json", inventory)
        print(json.dumps({"assets": len(inventory["items"]), "bindings": len(inventory["bindings"]), "missing": len(inventory["missingSrc"])}))
    else:
        inventory = acquire(inventory)
        supplementary_path = HERE / "negotiated-representations.json"
        if supplementary_path.exists():
            supplementary = json.loads(supplementary_path.read_text())
            inventory["supplementaryRepresentations"] = {
                "manifest": "negotiated-representations.json", "purpose": "15 explicitly declared WebP locators requested with Accept: image/webp; original default PNG responses retained separately",
                "responseFiles": len(supplementary["items"]),
                "bytes": sum(i["bytes"] for i in supplementary["items"]),
                "combinedTotalResponseFiles": len(inventory["items"]) + len(supplementary["items"]),
                "combinedTotalMediaBytes": inventory["summary"]["savedBytes"] + sum(i["bytes"] for i in supplementary["items"])}
        write_json(HERE / "manifest.json", inventory)
        print(json.dumps(inventory["summary"]))


if __name__ == "__main__":
    main()
