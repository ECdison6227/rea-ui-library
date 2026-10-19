// Readability-only formatting of exact raw evidence. Source offsets/SHA: link-stack-counts-expansion.meta.json
function eP(e, t, n, r = {}) {
  const s = t.length,
    i = "photo" === n,
    o = document.createElement("div");
  ((o.className = "link-stack"), o.setAttribute("role", "group"));
  const a = i ? "photo" : "link";
  (o.setAttribute("aria-label", `${s} ${a}${1 === s ? "" : "s"}`),
    i && (o.dataset.kind = "photo"),
    i &&
      (o.dataset.shapes = t
        .map((e) => {
          return (t = e.w / e.h) > 1.05
            ? "landscape"
            : t < 0.95
              ? "portrait"
              : "square";
          var t;
        })
        .join("-")),
    (o.dataset.layout =
      1 === s ? "one" : 2 === s ? "two" : 3 === s ? "three" : "pile"),
    i ||
      1 !== s ||
      "short" !== t[0].face ||
      ((o.dataset.face = "short"),
      o.style.setProperty("--stack-h", "var(--short-h)")));
  const l = i ? getComputedStyle(e.scopeEls[0]) : null,
    c = l ? parseFloat(l.getPropertyValue("--photo-area")) || 53e3 : 0,
    u = (l && parseFloat(l.getPropertyValue("--photo-even"))) || 0,
    d = (function (e) {
      const t = getComputedStyle(e);
      return {
        maxW: parseFloat(t.getPropertyValue("--photo-max-w")) || 290,
        maxH: parseFloat(t.getPropertyValue("--photo-max-h")) || 371,
      };
    })(o),
    p = i && 1 === s;
  if (i) {
    const e = t,
      n = (e) => (p ? Tj(d, e) : Sj(d, e, c, u)),
      r = Math.max(...e.map((e) => n(e.w / e.h).w));
    (o.style.setProperty("--photo-ref-w", `${r}px`),
      o.style.setProperty("--card-w", `${r}px`));
  }
  const h = "pile" === o.dataset.layout;
  if (h) {
    const t = document.createElement("button");
    ((t.type = "button"),
      (t.className = "link-stack-expand"),
      t.setAttribute("aria-label", `Show all ${a}s`),
      (t.innerHTML = e.expandIcon),
      o.appendChild(t));
  }
  const f =
    "three" === o.dataset.layout ? [2, 0, 1] : [...Array(s).keys()].reverse();
  if (
    (i &&
      (function (e, t) {
        Ij.set(e, t);
      })(o, t),
    p)
  ) {
    const e = t[0];
    o.style.setProperty("--stack-h", `${Tj(d, e.w / e.h).h}px`);
  }
  if (
    (f.forEach((e, n) => {
      if (n === f.length - 1) {
        const e = document.createElement("div");
        ((e.className = "link-stack-cast"), o.appendChild(e));
      }
      o.appendChild(
        i
          ? (function (e, t, n, r, s, i) {
              const o = document.createElement("article");
              ((o.className = "link-card"),
                (o.dataset.depth = String(t)),
                (o.dataset.kind = "photo"),
                (o.dataset.photo = String(t)),
                (o.dataset.title = e.title),
                (o.dataset.full = e.src2x ?? e.src));
              const a = e.w / e.h;
              o.dataset.ratio = String(a);
              const l = s ? Tj(i, a) : Sj(i, a, n, r);
              (o.style.setProperty("--w", `${l.w}px`),
                o.style.setProperty("--h", `${l.h}px`));
              const c = document.createElement("div");
              c.className = "link-card-media";
              const u = document.createElement("img");
              if (
                ((u.src = e.src),
                e.src2x && (u.srcset = `${e.src} 1x, ${e.src2x} 2x`),
                (u.alt = e.alt),
                (u.draggable = !1),
                c.appendChild(u),
                o.appendChild(c),
                0 === t)
              ) {
                const e = document.createElement("div");
                ((e.className = "link-card-sheen"), o.appendChild(e));
              }
              return o;
            })(t[e], e, c, u, p, d)
          : (function (e, t) {
              const n = document.createElement("a");
              if (
                (kj.set(n, e),
                (n.className = "link-card"),
                (n.dataset.depth = String(t)),
                (n.dataset.url = e.url),
                (n.href = e.url),
                (n.target = "_blank"),
                (n.rel = "noopener noreferrer"),
                (n.draggable = !1),
                "short" === e.face)
              ) {
                n.dataset.face = "short";
                const t = document.createElement("div");
                return (
                  (t.className = "link-card-mark"),
                  wj(t, e),
                  n.append(t, Ej(e.data)),
                  n
                );
              }
              const r = document.createElement("div");
              if (
                ((r.className = "link-card-media"),
                vj(n, r, e),
                n.append(r, Ej(e.data)),
                0 === t)
              ) {
                const e = document.createElement("div");
                ((e.className = "link-card-sheen"), n.appendChild(e));
              }
              return n;
            })(t[e], e),
      );
    }),
    h && i)
  ) {
    const e = o.querySelector('.link-card[data-depth="0"]');
    e && Cj(o, e);
  }
  return (
    r.still ||
      (function (e, t) {
        const n = t.dataset.layout;
        "pile" === n
          ? (Uj(e, t),
            (function (e, t) {
              const n = t.querySelector(".link-stack-expand");
              if (!n) return;
              const r = n;
              let s = null;
              const i = () => t.classList.contains("is-spread");
              let o = null,
                a = null;
              const l = () => [...t.querySelectorAll(".link-card")].reverse();
              (r.setAttribute("aria-expanded", "false"),
                r.addEventListener("click", function () {
                  if (i()) return;
                  !(function (e) {
                    const t = [...Pj].filter((t) => t.stack === e);
                    t.length &&
                      (t.forEach((e) => e.land()),
                      e.classList.add("is-measuring"),
                      e.offsetWidth,
                      e.classList.remove("is-measuring"));
                  })(t);
                  const n = l(),
                    c = Qj(n);
                  ((s = t.closest(".transcript")),
                    (a = document.createElement("div")),
                    (a.className = "link-stack-holder"),
                    (a.style.height = `${t.offsetHeight}px`),
                    t.replaceWith(a));
                  const u = a.getBoundingClientRect(),
                    d =
                      parseFloat(
                        getComputedStyle(t).getPropertyValue("--stack-inset-x"),
                      ) || 0;
                  (t.style.setProperty("--ground-x", `${u.left + d}px`),
                    t.style.setProperty(
                      "--ground-y",
                      `${u.top + u.height / 2}px`,
                    ),
                    e.overlay.appendChild(t),
                    e.overlay.classList.add("is-open"),
                    t.classList.add("is-spread"),
                    r.setAttribute("aria-label", `Hide all ${pj(t)}s`),
                    r.setAttribute("aria-expanded", "true"),
                    s?.classList.add("is-behind"),
                    e.scroller.classList.add("is-locked"),
                    e.scopeEls.forEach((e) => e.classList.add("is-cleared")),
                    e.onClear?.(!0),
                    e.onGridOpen?.(!0));
                  const p = Kj(n);
                  o = Xj(
                    t,
                    n,
                    c,
                    p,
                    (e) => e,
                    !0,
                    () => {
                      t.classList.add("is-settled");
                    },
                  );
                }),
                t.addEventListener("stack:collapse", function () {
                  if (!i()) return;
                  if (t.classList.contains("is-awaiting")) return;
                  (o?.(),
                    t.classList.remove("is-settled"),
                    e.onGridOpen?.(!1),
                    e.scopeEls.forEach((e) => e.classList.remove("is-cleared")),
                    e.onClear?.(!1));
                  const n = l(),
                    c = Qj(n);
                  t.classList.remove("is-spread");
                  const u = n.map((e) => [e.style.transform, e.style.opacity]);
                  (n.forEach((e) => {
                    ((e.style.transform = ""), (e.style.opacity = ""));
                  }),
                    a && a.replaceWith(t));
                  const d = Qj(n);
                  (a && t.replaceWith(a),
                    e.overlay.appendChild(t),
                    t.classList.add("is-spread"),
                    n.forEach((e, t) => {
                      ((e.style.transform = u[t][0]),
                        (e.style.opacity = u[t][1]));
                    }),
                    s?.classList.remove("is-behind"));
                  const p = n.length;
                  o = Xj(
                    t,
                    n,
                    c,
                    d,
                    (e) => p - 1 - e,
                    !1,
                    () => {
                      (n.forEach((e) => {
                        ((e.style.transform = ""), (e.style.opacity = ""));
                      }),
                        t.classList.remove("is-spread"),
                        t.style.removeProperty("--spread"),
                        t.style.removeProperty("--ground-x"),
                        t.style.removeProperty("--ground-y"),
                        a?.replaceWith(t),
                        (a = null),
                        e.overlay.classList.remove("is-open"),
                        e.scroller.classList.remove("is-locked"),
                        r.setAttribute("aria-label", `Show all ${pj(t)}s`),
                        r.setAttribute("aria-expanded", "false"));
                    },
                  );
                }),
                Jj(e));
              let c = 0;
              const u = () => {
                i() &&
                  t.classList.contains("is-settled") &&
                  (window.clearTimeout(c), (c = window.setTimeout(d, 120)));
              };
              function d() {
                if (!i()) return;
                const e = l();
                (o?.(),
                  t.classList.remove("is-settled"),
                  (o = Xj(
                    t,
                    e,
                    Qj(e),
                    Kj(e),
                    (e) => e,
                    !0,
                    () => {
                      t.classList.add("is-settled");
                    },
                  )));
              }
              (window.addEventListener("resize", u, { passive: !0 }),
                e.disposers.push(() => {
                  (window.clearTimeout(c),
                    window.removeEventListener("resize", u),
                    o?.());
                }));
            })(e, t))
          : "two" === n
            ? (function (e, t) {
                const n = t.querySelector('.link-card[data-depth="0"]'),
                  r = t.querySelector('.link-card[data-depth="1"]'),
                  s = () => Mj(t, "--row2-b-arc-peak", 152);
                let i = 0,
                  o = 0,
                  a = !1;
                function l(e) {
                  const l = t.dataset.focus ?? null;
                  l !== e &&
                    (null === e
                      ? delete t.dataset.focus
                      : (t.dataset.focus = e),
                    n &&
                      l &&
                      e &&
                      l !== e &&
                      (n.classList.remove("is-yielding"),
                      n.offsetWidth,
                      n.classList.add("is-yielding"),
                      clearTimeout(o),
                      (o = window.setTimeout(
                        () => n.classList.remove("is-yielding"),
                        Mj(t, "--row2-a-arc-time", 380),
                      ))),
                    r &&
                      ("1" === e
                        ? ((a = !1),
                          clearTimeout(i),
                          r.classList.remove("is-leaving"),
                          (i = window.setTimeout(
                            () => r.classList.add("is-front"),
                            s(),
                          )))
                        : a ||
                          (r.classList.contains("is-front")
                            ? ((a = !0),
                              clearTimeout(i),
                              r.classList.remove("is-leaving"),
                              r.offsetWidth,
                              r.classList.add("is-leaving"),
                              (i = window.setTimeout(() => {
                                (r.classList.remove("is-front"),
                                  (i = window.setTimeout(() => {
                                    (r.classList.remove("is-leaving"),
                                      (a = !1));
                                  }, s())));
                              }, s())))
                            : (clearTimeout(i),
                              r.classList.remove("is-leaving")))));
                }
                const c = Oj(e, t);
                (t.addEventListener(
                  "pointermove",
                  (e) => {
                    const n = c.current();
                    if (!n) return;
                    const r = t.getBoundingClientRect();
                    l(Rj(n, t, e.clientX - r.left, e.clientY - r.top));
                  },
                  { passive: !0 },
                ),
                  t.addEventListener("pointerleave", () => l(null)));
              })(e, t)
            : "three" === n &&
              (function (e, t) {
                const n = [...t.querySelectorAll(".link-card")],
                  r = new Map(n.map((e) => [e.dataset.depth, e])),
                  s = n[n.length - 1],
                  i = () => Mj(t, "--row3-arc-peak", 160),
                  o = () => Mj(t, "--row3-arc-time", 400),
                  a = new Map(),
                  l = (e, t, n) => {
                    (clearTimeout(a.get(e)), a.set(e, window.setTimeout(n, t)));
                  },
                  c = new Set(),
                  u = new Set(),
                  d = new Map(),
                  p = new Map();
                function h(e) {
                  const h = t.dataset.focus ?? null;
                  if (h === e) return;
                  null === e ? delete t.dataset.focus : (t.dataset.focus = e);
                  const f = h ? r.get(h) : null,
                    m = !!f && f.classList.contains("is-front");
                  if (
                    (f &&
                      e &&
                      h !== e &&
                      !m &&
                      !u.has(f) &&
                      (u.add(f),
                      f.classList.remove("is-yielding"),
                      f.offsetWidth,
                      f.classList.add("is-yielding"),
                      clearTimeout(p.get(f)),
                      p.set(
                        f,
                        window.setTimeout(() => {
                          (f.classList.remove("is-yielding"), u.delete(f));
                        }, o()),
                      )),
                    n.forEach((t) => {
                      t.dataset.depth !== e &&
                        (function (e) {
                          c.has(e) ||
                            (clearTimeout(a.get(e)),
                            e.classList.contains("is-front")
                              ? (c.add(e),
                                e.classList.remove("is-leaving"),
                                e.offsetWidth,
                                e.classList.add("is-leaving"),
                                l(e, i(), () => {
                                  (e.classList.remove("is-front"),
                                    l(e, o() - i(), () => {
                                      (e.classList.remove("is-leaving"),
                                        c.delete(e));
                                    }));
                                }))
                              : e.classList.remove("is-leaving"));
                        })(t);
                    }),
                    null !== e)
                  ) {
                    const t = r.get(e);
                    t &&
                      t !== s &&
                      ((g = t),
                      c.delete(g),
                      clearTimeout(a.get(g)),
                      g.classList.remove("is-leaving"),
                      g.classList.remove("is-arriving"),
                      g.offsetWidth,
                      g.classList.add("is-arriving"),
                      clearTimeout(d.get(g)),
                      d.set(
                        g,
                        window.setTimeout(
                          () => g.classList.remove("is-arriving"),
                          o(),
                        ),
                      ),
                      l(g, i(), () => g.classList.add("is-front")));
                  }
                  var g;
                }
                const f = Oj(e, t),
                  m = () => Mj(t, "--row3-intent-dwell", 22);
                let g = 0,
                  v = null,
                  y = 0,
                  b = 0,
                  _ = 0,
                  x = 0;
                function w() {
                  if (
                    Math.hypot(y - _, b - x) < Dj(t, "--row3-intent-slack", 5.5)
                  )
                    return ((g = 0), void h(v));
                  ((_ = y), (x = b), (g = window.setTimeout(w, m())));
                }
                (t.addEventListener(
                  "pointermove",
                  (e) => {
                    const n = f.current();
                    if (!n) return;
                    const r = t.getBoundingClientRect();
                    !(function (e, n, r) {
                      if (
                        ((v = e),
                        (y = n),
                        (b = r),
                        e === (t.dataset.focus ?? null))
                      )
                        return (clearTimeout(g), void (g = 0));
                      g || ((_ = n), (x = r), (g = window.setTimeout(w, m())));
                    })(
                      Rj(n, t, e.clientX - r.left, e.clientY - r.top),
                      e.clientX,
                      e.clientY,
                    );
                  },
                  { passive: !0 },
                ),
                  t.addEventListener("pointerleave", () => {
                    (clearTimeout(g), (g = 0), (v = null), h(null));
                  }));
              })(e, t);
      })(e, o),
    o.addEventListener("click", (t) => {
      const n = t.target?.closest("a.link-card");
      n instanceof HTMLAnchorElement &&
        n.href &&
        (t.preventDefault(), hj(e, n));
    }),
    o
  );
}
