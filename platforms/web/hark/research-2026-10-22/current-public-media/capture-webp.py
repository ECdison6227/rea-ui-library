#!/usr/bin/env python3
"""Capture only the 15 declared-WebP locators with explicit Accept: image/webp."""
import json
from pathlib import Path
import subprocess
import urllib.parse
import urllib.request

from acquire import HERE, MAX_BYTES, MAX_FILES, inspect_file, permitted, sha, utc, write_json


def main():
    base = json.loads((HERE / "manifest.json").read_text())
    target = HERE / "negotiated-representations.json"
    old = json.loads(target.read_text()) if target.exists() else {}
    old_by_id = {i["id"]: i for i in old.get("items", [])}
    system = getattr(urllib.request, "getproxies_macosx_sysconf", lambda: {})()
    proxy = system.get("https")
    if not proxy or urllib.parse.urlsplit(proxy).username or urllib.parse.urlsplit(proxy).password:
        raise RuntimeError("normal credential-free system proxy unavailable")
    candidates = [i for i in base["items"] if i["kind"] == "image" and i["url"].endswith(".webp")]
    if len(candidates) != 15:
        raise RuntimeError("expected exactly 15 already-declared WebP locators")
    result = {"schemaVersion": 1, "classification": "B: anonymous content-negotiated responses of existing explicit public CDN URLs",
              "scope": "Exactly the 15 declared-WebP locators; no new URL/query/Accept matrix", "generatedAt": utc(),
              "requestHeaders": {"Accept": "image/webp"}, "items": []}
    total = sum(i["bytes"] for i in base["items"])
    count = len(base["items"])
    (HERE / "negotiated-webp").mkdir(exist_ok=True)
    for item in candidates:
        if not permitted(item["url"]):
            raise RuntimeError("outside explicit allowed static boundary")
        variant_id = item["id"] + "--accept-image-webp"
        prior = old_by_id.get(variant_id)
        if prior and prior.get("acquisition", {}).get("status") == "saved":
            payload = (HERE / prior["path"]).read_bytes()
            if len(payload) != prior["bytes"] or sha(payload) != prior["sha256"]:
                raise RuntimeError("existing negotiated representation mismatch")
            result["items"].append(prior)
            total += len(payload)
            count += 1
            continue
        path = "negotiated-webp/" + Path(item["path"]).name
        dest = HERE / path
        capture = {"startedAt": utc(), "method": "normal anonymous curl HTTPS GET; existing configured system proxy; no redirects/cookies/Authorization/profile",
                   "requestHeaders": {"Accept": "image/webp"}, "networkRoute": "127.0.0.1:7897 (existing macOS system HTTPS proxy)"}
        probe = HERE / "content-negotiation-probe.bin"
        if item["id"] == "32ef649cc584d5d0011aa37658ce29137b4f7ca1-1112x668" and probe.exists():
            receipt = json.loads((HERE / "content-negotiation-probe.json").read_text())
            payload = probe.read_bytes()
            if sha(payload) != receipt["sha256"]:
                raise RuntimeError("negotiation probe integrity mismatch")
            capture.update({"startedAt": receipt["startedAt"], "completedAt": receipt["completedAt"], "httpStatus": receipt["curl"]["status"],
                            "curlReceipt": receipt["curl"], "sourceReceipt": "content-negotiation-probe.json"})
            probe.replace(dest)
            receipt["payloadRetention"] = path
            receipt["payloadMovedWithoutByteChanges"] = True
            write_json(HERE / "content-negotiation-probe.json", receipt)
            response_type = receipt["curl"]["contentType"]
        else:
            fmt = '{"status":%{http_code},"contentType":"%{content_type}","bytes":%{size_download},"httpVersion":"%{http_version}"}'
            command = ["curl", "--silent", "--show-error", "--fail", "--proxy", proxy, "--proto", "=https",
                       "--connect-timeout", "12", "--max-time", "90", "--retry", "1", "--retry-all-errors",
                       "--max-filesize", "10000000", "--header", "Accept: image/webp", "--output", str(dest), "--write-out", fmt, item["url"]]
            proc = subprocess.run(command, capture_output=True, text=True, timeout=195)
            receipt = json.loads(proc.stdout)
            capture.update({"completedAt": utc(), "httpStatus": receipt.get("status"), "curlReceipt": receipt})
            if proc.returncode or receipt.get("status") != 200:
                raise RuntimeError("WebP content negotiation failed: " + item["url"] + " " + proc.stderr)
            response_type = receipt["contentType"]
            payload = dest.read_bytes()
        metadata = inspect_file(dest, "image")
        if metadata["format"] != "WEBP" or response_type != "image/webp":
            raise RuntimeError("declared WebP representation was not obtained")
        total += len(payload)
        count += 1
        if total > MAX_BYTES or count > MAX_FILES:
            raise RuntimeError("coordinator approved response budget exceeded")
        result["items"].append({"id": variant_id, "baseLocatorId": item["id"], "url": item["url"], "path": path,
            "bytes": len(payload), "sha256": sha(payload), "contentType": response_type, "metadata": metadata,
            "acquisition": {**capture, "status": "saved"}, "sourceRef": {"path": "manifest.json", "locatorId": item["id"]},
            "defaultResponse": {k: item[k] for k in ["path", "bytes", "sha256", "contentType"]},
            "purpose": "Match the explicitly declared image/webp media encoding while retaining the original default PNG response separately."})
        write_json(target, result)
        print("SAVED_WEBP", variant_id, len(payload), flush=True)
    result["summary"] = {"locators": len(base["items"]), "defaultResponses": len(base["items"]),
                         "negotiatedWebpResponses": len(result["items"]), "totalResponseFiles": count,
                         "totalMediaBytes": total, "negotiatedWebpBytes": sum(i["bytes"] for i in result["items"])}
    write_json(target, result)
    print(json.dumps(result["summary"]))


if __name__ == "__main__":
    main()
