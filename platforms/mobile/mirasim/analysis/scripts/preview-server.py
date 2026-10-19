#!/usr/bin/env python3
"""Serve the extracted frontend locally; preview adapters never contact the vendor."""
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import urlsplit
import argparse

ROOT = Path(__file__).resolve().parents[2]
PUBLIC = ROOT / 'analysis/extracted/apk/assets/public'

class Handler(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=str(ROOT / 'analysis'), **kwargs)

    def do_GET(self):
        path = urlsplit(self.path).path
        if path.startswith('/app/'):
            relative = path.removeprefix('/app/') or 'index.html'
            target = (PUBLIC / relative).resolve()
            if not target.is_relative_to(PUBLIC.resolve()) or not target.is_file():
                self.send_error(404)
                return
            data = target.read_bytes()
            if relative == 'index.html':
                data = data.replace(b'<head>', b'<head><script src="/preview/adapter.js"></script>')
            self.send_response(200)
            self.send_header('Content-Type', self.guess_type(str(target)))
            self.send_header('Content-Security-Policy', "connect-src 'self'; object-src 'none'; frame-src 'none'")
            self.send_header('Cache-Control', 'no-store')
            self.send_header('Content-Length', str(len(data)))
            self.end_headers()
            self.wfile.write(data)
            return
        if path == '/':
            self.path = '/preview/index.html'
        super().do_GET()

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--port', type=int, default=4174)
    args = parser.parse_args()
    print(f'Analysis gallery: http://127.0.0.1:{args.port}', flush=True)
    ThreadingHTTPServer(('127.0.0.1', args.port), Handler).serve_forever()
