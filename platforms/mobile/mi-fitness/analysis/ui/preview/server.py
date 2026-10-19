"""Local, read-only preview server, restricted to analysis/ui. Python standard library only."""
from argparse import ArgumentParser
from functools import partial
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import unquote, urlsplit

ROOT = Path(__file__).resolve().parent.parent


class StudyHandler(SimpleHTTPRequestHandler):
    def translate_path(self, path):
        parsed = unquote(urlsplit(path).path)
        if "\x00" in parsed:
            return str(ROOT / ".invalid-preview-path")
        target = (ROOT / parsed.lstrip("/\\")).resolve()
        try:
            target.relative_to(ROOT)
        except ValueError:
            return str(ROOT / ".invalid-preview-path")
        return str(target)

    def end_headers(self):
        self.send_header("Cache-Control", "no-store")
        self.send_header("X-Content-Type-Options", "nosniff")
        self.send_header("Referrer-Policy", "no-referrer")
        super().end_headers()


if __name__ == "__main__":
    parser = ArgumentParser(description=__doc__)
    parser.add_argument("--port", type=int, default=8766)
    args = parser.parse_args()
    handler = partial(StudyHandler, directory=str(ROOT))
    with ThreadingHTTPServer(("127.0.0.1", args.port), handler) as server:
        print(f"UI study: http://127.0.0.1:{args.port}/preview/", flush=True)
        print(f"Read-only document root: {ROOT}", flush=True)
        try:
            server.serve_forever()
        except KeyboardInterrupt:
            pass
