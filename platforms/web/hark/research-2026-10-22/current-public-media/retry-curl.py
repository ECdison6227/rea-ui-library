#!/usr/bin/env python3
"""Resume only missing exact public static payloads using the normal system proxy."""
import datetime as dt
import json
from pathlib import Path
import subprocess
import urllib.parse
import urllib.request

from acquire import HERE, MAX_BYTES, MAX_FILES, MAX_SINGLE_FILE_BYTES, inspect_file, permitted, sha, utc, write_json


def main():
    manifest_path = HERE / "manifest.json"
    j = json.loads(manifest_path.read_text())
    system = getattr(urllib.request, "getproxies_macosx_sysconf", lambda: {})()
    proxy = system.get("https")
    if not proxy:
        raise RuntimeError("normal configured macOS HTTPS proxy unavailable")
    proxy_parts = urllib.parse.urlsplit(proxy)
    if proxy_parts.username or proxy_parts.password:
        raise RuntimeError("credential-bearing proxy outside this task")
    route = f"{proxy_parts.hostname}:{proxy_parts.port} (existing macOS system HTTPS proxy)"
    for i in j["items"]:
        if i.get("acquisition", {}).get("status") == "saved":
            payload = (HERE / i["path"]).read_bytes()
            if len(payload) != i["bytes"] or sha(payload) != i["sha256"]:
                raise RuntimeError("existing original mismatch: " + i["path"])
    saved = [i for i in j["items"] if i.get("acquisition", {}).get("status") == "saved"]
    count, total = len(saved), sum(i["bytes"] for i in saved)
    for item in j["items"]:
        if item.get("acquisition", {}).get("status") == "saved":
            continue
        if not permitted(item["url"]):
            raise RuntimeError("outside explicit public-static boundary")
        old = item.get("acquisition", {})
        capture = {"method": "anonymous curl HTTPS GET; no cookies/Authorization/profile; redirects not followed",
                   "networkRoute": route, "startedAt": utc(),
                   "previousAttempts": old.get("previousAttempts", []) + [{k:v for k,v in old.items() if k != "previousAttempts"}]}
        temp = HERE / ("pending-" + Path(item["path"]).name)
        dest = HERE / item["path"]
        # Keep the successful, already requested protocol-comparison probe rather
        # than requesting the same 577727-byte payload a second time.
        probe = HERE / "probe.mov"
        if item["id"] == "812627e8e4a717a227a3c8b0fb5ae9fc31724b9e" and probe.exists():
            capture.update({"startedAt": None, "completedAt": dt.datetime.fromtimestamp(probe.stat().st_mtime, dt.timezone.utc).isoformat(),
                            "captureTimeLimit": "Probe completedAt is local file mtime; start was not instrumented.",
                            "httpStatus": 200, "finalUrl": item["url"], "probeReceipt": "curl stdout: status=200 type=video/quicktime bytes=577727"})
            payload = probe.read_bytes()
            if len(payload) != 577727:
                raise RuntimeError("probe length differs from recorded curl output")
            probe.replace(temp)
            result_code, result_type = 0, "video/quicktime"
        else:
            fmt = '{"status":%{http_code},"contentType":"%{content_type}","bytes":%{size_download},"finalUrl":"%{url_effective}","httpVersion":"%{http_version}"}'
            command = ["curl", "--silent", "--show-error", "--fail", "--proxy", proxy,
                       "--proto", "=https", "--connect-timeout", "12", "--max-time", "240",
                       "--retry", "1", "--retry-all-errors", "--max-filesize", str(MAX_SINGLE_FILE_BYTES),
                       "--output", str(temp), "--write-out", fmt, item["url"]]
            result = subprocess.run(command, capture_output=True, text=True, timeout=510)
            result_code = result.returncode
            try:
                receipt = json.loads(result.stdout)
            except ValueError:
                receipt = {"stdoutParseError": True}
            result_type = receipt.get("contentType")
            capture.update({"completedAt": utc(), "httpStatus": receipt.get("status"),
                            "curlReceipt": receipt, "exitCode": result.returncode})
            if result.stderr:
                capture["transportMessages"] = result.stderr.strip()
            payload = temp.read_bytes() if temp.exists() else b""
        if result_code or capture.get("httpStatus") != 200:
            item["acquisition"] = {**capture, "status": "failed", "error": "curl transfer failed; partial/error body not retained"}
            if temp.exists():
                temp.unlink()
            print("FAILED", item["id"], capture.get("httpStatus"), flush=True)
        elif total + len(payload) > MAX_BYTES or len(payload) > MAX_SINGLE_FILE_BYTES or count >= MAX_FILES:
            item["acquisition"] = {**capture, "status": "budget-skipped", "reason": "coordinator-approved retention limit"}
            temp.unlink()
            print("BUDGET", item["id"], len(payload), flush=True)
        else:
            metadata = inspect_file(temp, item["kind"])
            temp.replace(dest)
            # ffprobe observed the temporary path; retain it as raw command
            # output rather than changing that observation to the final name.
            item.update({"bytes": len(payload), "sha256": sha(payload), "contentType": result_type,
                         "metadata": metadata, "acquisition": {**capture, "status": "saved"}})
            count += 1
            total += len(payload)
            print("SAVED", item["id"], len(payload), flush=True)
        write_json(manifest_path, j)
    j["summary"] = {"savedFiles": count, "savedBytes": total, "discoveredOriginals": len(j["items"]),
                    "declaredVariants": sum(len(i["variants"]) for i in j["items"]),
                    "missingSrcRecords": len(j["missingSrc"]),
                    "carouselSlideBindings": sum(b.get("kind") == "carousel-slide" for b in j["bindings"]),
                    "statuses": {s: sum(i.get("acquisition", {}).get("status") == s for i in j["items"])
                                 for s in ["saved", "failed", "budget-skipped"]}}
    j["generatedAt"] = utc()
    write_json(manifest_path, j)
    print(json.dumps(j["summary"]))


if __name__ == "__main__":
    main()
