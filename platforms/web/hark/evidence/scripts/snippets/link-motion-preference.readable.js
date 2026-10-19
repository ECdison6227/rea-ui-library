// Readability-only formatting of exact raw evidence. Source offsets/SHA: link-motion-preference.meta.json
const aj = {
    "resizer.otstatic.com": (e) => (
      e.searchParams.set("width", String(1280)),
      e.searchParams.set("height", String(720)),
      e.toString()
    ),
  },
  lj = "stack:photo-open",
  cj = "stack:image-dead",
  uj = { close: null },
  dj = (e, t, n) => Math.min(n, Math.max(t, e));
function pj(e) {
  return "photo" === e.dataset.kind ? "photo" : "link";
}
function hj(e, t) {
  e.openLink
    ? e.openLink(t.href, t)
    : window.open(t.href, "_blank", "noopener,noreferrer");
}
const fj = tm,
  mj =
    "undefined" != typeof window && "function" == typeof window.matchMedia
      ? window.matchMedia("(prefers-reduced-motion: reduce)")
      : null,
  gj = new WeakMap();
