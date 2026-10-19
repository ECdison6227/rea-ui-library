// Readability-only formatting of exact raw evidence. Source offsets/SHA: table-card-model.meta.json
function kme(t, r) {
  const s = n.c(51),
    { replyMessageId: i, receipt: o } = r,
    [a, l] = e.useState(null),
    c = e.useRef(null),
    u = e.useRef(null),
    { replyRef: d, onReply: p } = Fpe(i),
    {
      body: h,
      setBody: f,
      setScroller: m,
      scrollRef: g,
    } = (function () {
      const t = n.c(3),
        r = e.useRef(null),
        [s, i] = e.useState(null);
      let o;
      t[0] === Symbol.for("react.memo_cache_sentinel")
        ? ((o = (e) => {
            (i(e), (r.current = e));
          }),
          (t[0] = o))
        : (o = t[0]);
      const a = o;
      let l;
      return (
        t[1] !== s
          ? ((l = { body: s, setBody: i, setScroller: a, scrollRef: r }),
            (t[1] = s),
            (t[2] = l))
          : (l = t[2]),
        l
      );
    })(),
    v = (function (e, t, r) {
      const s = n.c(26),
        i = void 0 === t ? null : t,
        o = void 0 === r ? null : r;
      let a, l, c, u, d, p, h, f, m, g, v, y;
      if (s[0] !== o || s[1] !== i || s[2] !== e) {
        const t = i
            ? (function (e, t) {
                return {
                  ...e,
                  headers: Vhe(e.headers, t.at),
                  align: Vhe(e.align, t.at),
                  shares: e.shares ? Vhe(e.shares, t.at) : null,
                  rows: e.rows.map((e) => Vhe(e, t.at)),
                };
              })(e, i)
            : e,
          n = t.ownIndex ? t.headers.slice(1) : t.headers,
          r = t.rows.map((e) => (t.ownIndex ? e.slice(1) : e)),
          y = (function (e) {
            const t = e.findIndex((e) => "link" === e.kind);
            return t > 0 ? t : null;
          })(BR(n, r)),
          b = Vhe(n, y),
          _ = r.map((e) => Vhe(e, y)),
          x = BR(b, _);
        ((u = b),
          (d = _),
          (p = x),
          (h =
            null === y
              ? null
              : (function (e, t) {
                  return e.map((e) => MR(e[t] ?? "")?.[0] ?? null);
                })(r, y)),
          (f = (function (e, t) {
            return e.shares
              ? Vhe(e.ownIndex ? e.shares.slice(1) : e.shares, t)
              : null;
          })(t, y)),
          (m = t.ownIndex ? t.rows.map(Whe) : null),
          (g = i?.photos ?? null),
          (v = o),
          (a = (function ({ numbered: e, faced: t, marked: n }) {
            return [
              ...(e ? ["min-content"] : []),
              ...(t ? [`${56 + lhe}px`] : []),
              ...(n ? [`${20 + che}px`] : []),
            ];
          })({
            numbered: t.ownIndex,
            faced: null !== i,
            marked: null === i && null !== o,
          })),
          (l = b.map((e, t) => cR(_.map((e) => e[t] ?? "")))),
          (c = b.map((e, t) => !1)),
          (s[0] = o),
          (s[1] = i),
          (s[2] = e),
          (s[3] = a),
          (s[4] = l),
          (s[5] = c),
          (s[6] = u),
          (s[7] = d),
          (s[8] = p),
          (s[9] = h),
          (s[10] = f),
          (s[11] = m),
          (s[12] = g),
          (s[13] = v));
      } else
        ((a = s[3]),
          (l = s[4]),
          (c = s[5]),
          (u = s[6]),
          (d = s[7]),
          (p = s[8]),
          (h = s[9]),
          (f = s[10]),
          (m = s[11]),
          (g = s[12]),
          (v = s[13]));
      return (
        s[14] !== a ||
        s[15] !== l ||
        s[16] !== c ||
        s[17] !== u ||
        s[18] !== d ||
        s[19] !== p ||
        s[20] !== h ||
        s[21] !== f ||
        s[22] !== m ||
        s[23] !== g ||
        s[24] !== v
          ? ((y = {
              headers: u,
              rows: d,
              columns: p,
              hrefs: h,
              shares: f,
              indices: m,
              faces: g,
              marks: v,
              lead: a,
              money: l,
              sortable: c,
            }),
            (s[14] = a),
            (s[15] = l),
            (s[16] = c),
            (s[17] = u),
            (s[18] = d),
            (s[19] = p),
            (s[20] = h),
            (s[21] = f),
            (s[22] = m),
            (s[23] = g),
            (s[24] = v),
            (s[25] = y))
          : (y = s[25]),
        y
      );
    })(t, r.photos, r.marks),
    y = (function (e, t) {
      const r = n.c(4);
      let s;
      return (
        r[0] !== e.columns || r[1] !== e.rows || r[2] !== t
          ? ((s = (function (e, t, n) {
              const r = e.map((e, t) => t);
              if (!t) return r;
              const s = n[t.column];
              if (!s) return r;
              const i = "desc" === t.direction ? -1 : 1,
                o = (n) => (e[n]?.[t.column] ?? "").trim();
              return r.sort((e, t) => {
                const n = o(e),
                  r = o(t),
                  a = wR(n),
                  l = wR(r);
                if (a || l) return a && l ? e - t : a ? 1 : -1;
                const c = The(n, s),
                  u = The(r, s),
                  d = null !== c && null !== u ? c - u : She.compare(n, r);
                return 0 === d ? e - t : i * d;
              });
            })(e.rows, t, e.columns)),
            (r[0] = e.columns),
            (r[1] = e.rows),
            (r[2] = t),
            (r[3] = s))
          : (s = r[3]),
        s
      );
    })(v, a),
    b = o ?? null;
  let _;
  s[0] !== i ? ((_ = () => AP(i)), (s[0] = i), (s[1] = _)) : (_ = s[1]);
  const {
    expanded: x,
    openCard: w,
    controlFor: k,
  } = (function (t, r, s) {
    const i = n.c(16),
      [o, a] = e.useState(!1);
    let l;
    i[0] !== t || i[1] !== r
      ? ((l = r ? r.folds : !ahe(t.rows.length)),
        (i[0] = t),
        (i[1] = r),
        (i[2] = l))
      : (l = i[2]);
    const c = l;
    let u;
    i[3] !== s || i[4] !== o
      ? ((u = () => {
          o || s();
        }),
        (i[3] = s),
        (i[4] = o),
        (i[5] = u))
      : (u = i[5]);
    const d = u,
      p = !c || o;
    let h, f, m;
    return (
      i[6] !== d
        ? ((h = () => {
            (d(), a(!0));
          }),
          (i[6] = d),
          (i[7] = h))
        : (h = i[7]),
      i[8] !== c || i[9] !== d || i[10] !== o
        ? ((f = (e) => ({
            expandable: c && (e || o),
            toggleExpanded: () => {
              (d(), a(phe));
            },
          })),
          (i[8] = c),
          (i[9] = d),
          (i[10] = o),
          (i[11] = f))
        : (f = i[11]),
      i[12] !== p || i[13] !== h || i[14] !== f
        ? ((m = { expanded: p, openCard: h, controlFor: f }),
          (i[12] = p),
          (i[13] = h),
          (i[14] = f),
          (i[15] = m))
        : (m = i[15]),
      m
    );
  })(v, b, _);
  let E;
  s[2] !== v.columns || s[3] !== v.lead || s[4] !== v.shares || s[5] !== t.raw
    ? ((E = {
        gridRef: u,
        frameRef: c,
        columns: v.columns,
        shares: v.shares,
        lead: v.lead,
        content: t.raw,
      }),
      (s[2] = v.columns),
      (s[3] = v.lead),
      (s[4] = v.shares),
      (s[5] = t.raw),
      (s[6] = E))
    : (E = s[6]);
  const { template: T, shape: S } = gme(E),
    I = t.raw,
    A = !b && "scroll" === S;
  let C;
  s[7] !== x || s[8] !== b || s[9] !== t
    ? ((C = { gridRef: u, table: t, receipt: b, expanded: x }),
      (s[7] = x),
      (s[8] = b),
      (s[9] = t),
      (s[10] = C))
    : (C = s[10]);
  const N = (function (t) {
      const r = n.c(13),
        { gridRef: s, table: i, receipt: o, expanded: a } = t,
        l = i.raw,
        c = i.rows.length,
        [u, d] = e.useState(null),
        p = null === o;
      let h, f, m;
      return (
        r[0] !== c || r[1] !== a || r[2] !== s || r[3] !== p
          ? ((h = () => {
              const e = s.current;
              if (!e || !p || a) return;
              const t = () =>
                d(
                  (function (e, t) {
                    const n = getComputedStyle(e),
                      r = n.gridTemplateRows.split(" ").map(parseFloat);
                    if (r.some((e) => !Number.isFinite(e))) return null;
                    if (r.length < t) return null;
                    const s = parseFloat(n.rowGap) || 0,
                      i = (e, t) => e + t + s,
                      o = r.length - t,
                      a = Math.floor(ohe),
                      l = r.slice(o),
                      c = r.slice(0, o).reduce(i, 0),
                      u = l.slice(0, a).reduce(i, 0),
                      d = (l[a] ?? 0) * (ohe - a);
                    return ihe + c + u + d;
                  })(e, c),
                );
              t();
              const n = new ResizeObserver(t);
              return (n.observe(e), () => n.disconnect());
            }),
            (r[0] = c),
            (r[1] = a),
            (r[2] = s),
            (r[3] = p),
            (r[4] = h))
          : (h = r[4]),
        r[5] !== c || r[6] !== l || r[7] !== a || r[8] !== s || r[9] !== p
          ? ((f = [s, l, c, p, a]),
            (r[5] = c),
            (r[6] = l),
            (r[7] = a),
            (r[8] = s),
            (r[9] = p),
            (r[10] = f))
          : (f = r[10]),
        e.useLayoutEffect(h, f),
        p && null !== u
          ? u
          : (r[11] !== o
              ? ((m = (function (e) {
                  return e ? 228 : 360;
                })(o)),
                (r[11] = o),
                (r[12] = m))
              : (m = r[12]),
            m)
      );
    })(C),
    R = b?.ledger.length ?? 0;
  let O;
  s[11] !== h ||
  s[12] !== x ||
  s[13] !== g ||
  s[14] !== A ||
  s[15] !== N ||
  s[16] !== R ||
  s[17] !== t.raw
    ? ((O = {
        body: h,
        scrollRef: g,
        expanded: x,
        content: I,
        faded: A,
        collapsedCap: N,
        ledgerLines: R,
      }),
      (s[11] = h),
      (s[12] = x),
      (s[13] = g),
      (s[14] = A),
      (s[15] = N),
      (s[16] = R),
      (s[17] = t.raw),
      (s[18] = O))
    : (O = s[18]);
  const L = (function (t) {
    const r = n.c(15),
      {
        body: s,
        scrollRef: i,
        expanded: o,
        content: a,
        faded: l,
        collapsedCap: c,
        ledgerLines: u,
      } = t,
      d = o ? Xe : Ze;
    let p;
    r[0] !== s || r[1] !== c || r[2] !== a || r[3] !== o || r[4] !== d
      ? ((p = {
          body: s,
          expanded: o,
          collapsedCap: c,
          content: a,
          transition: d,
        }),
        (r[0] = s),
        (r[1] = c),
        (r[2] = a),
        (r[3] = o),
        (r[4] = d),
        (r[5] = p))
      : (p = r[5]);
    const { clipped: h, clipHeight: f, measured: m } = vme(p);
    let g;
    r[6] !== s || r[7] !== a || r[8] !== o
      ? ((g = { body: s, expanded: o, content: a }),
        (r[6] = s),
        (r[7] = a),
        (r[8] = o),
        (r[9] = g))
      : (g = r[9]);
    const v = (function (t) {
      const r = n.c(1),
        { body: s, expanded: i, content: o } = t,
        [a, l] = e.useState(!1);
      let c;
      return (
        r[0] === Symbol.for("react.memo_cache_sentinel")
          ? ((c = (e) => {
              const t = e.offsetHeight;
              e.setAttribute(yme, "");
              const n = e.offsetHeight;
              (e.removeAttribute(yme), 0 !== n && l(n > t));
            }),
            (r[0] = c))
          : (c = r[0]),
        ise(i ? null : s, o, c),
        a
      );
    })(g);
    !(function (t, r) {
      const s = n.c(4);
      let i, o;
      (s[0] !== r || s[1] !== t
        ? ((i = () => {
            const e = t.current;
            if (!e) return;
            const n = (t, n) => {
              const r = le({ [rfe]: `${t}px`, [sfe]: `${n}px` });
              for (const [s, i] of Object.entries(r))
                void 0 !== i && e.style.setProperty(s, i);
            };
            if (!r) return void n(0, 0);
            const s = () => {
              const t = e.scrollWidth - e.clientWidth - e.scrollLeft,
                r = bme;
              n(r(e.scrollLeft), r(t));
            };
            (s(), e.addEventListener("scroll", s, { passive: !0 }));
            const i = new ResizeObserver(s);
            return (
              i.observe(e),
              () => {
                (e.removeEventListener("scroll", s), i.disconnect());
              }
            );
          }),
          (o = [t, r]),
          (s[0] = r),
          (s[1] = t),
          (s[2] = i),
          (s[3] = o))
        : ((i = s[2]), (o = s[3])),
        e.useLayoutEffect(i, o));
    })(i, l);
    const y = !o && u > 0,
      b = h || y,
      _ = h || y || v || !m;
    let x;
    return (
      r[10] !== f || r[11] !== m || r[12] !== b || r[13] !== _
        ? ((x = { clipped: b, clipHeight: f, measured: m, withheld: _ }),
          (r[10] = f),
          (r[11] = m),
          (r[12] = b),
          (r[13] = _),
          (r[14] = x))
        : (x = r[14]),
      x
    );
  })(O);
  let M;
  s[19] !== L.withheld || s[20] !== k
    ? ((M = k(L.withheld)), (s[19] = L.withheld), (s[20] = k), (s[21] = M))
    : (M = s[21]);
  const { expandable: D, toggleExpanded: j } = M,
    P = b ? "order" : S;
  let F, B;
  (s[22] !== D || s[23] !== x || s[24] !== j
    ? ((F = { expanded: x, expandable: D, onToggle: j }),
      (s[22] = D),
      (s[23] = x),
      (s[24] = j),
      (s[25] = F))
    : (F = s[25]),
    s[26] !== p || s[27] !== y || s[28] !== i || s[29] !== F || s[30] !== t
      ? ((B = { table: t, order: y, expand: F, onReply: p, messageId: i }),
        (s[26] = p),
        (s[27] = y),
        (s[28] = i),
        (s[29] = F),
        (s[30] = t),
        (s[31] = B))
      : (B = s[31]));
  const H = (function (e) {
    const t = n.c(23),
      { table: r, order: s, expand: i, onReply: o, messageId: a } = e;
    let l;
    t[0] !== s || t[1] !== r
      ? ((l = RO(r, s)), (t[0] = s), (t[1] = r), (t[2] = l))
      : (l = t[2]);
    const c = l;
    let u;
    t[3] !== s || t[4] !== r
      ? ((u = () =>
          (function (e, t) {
            return `<table><thead><tr>${e.headers.map((e) => `<th>${xhe(e)}</th>`).join("")}</tr></thead><tbody>${OO(
              e,
              t,
            )
              .map(
                (e) =>
                  `<tr>${e.map((e) => `<td>${xhe(e)}</td>`).join("")}</tr>`,
              )
              .join("")}</tbody></table>`;
          })(r, s)),
        (t[3] = s),
        (t[4] = r),
        (t[5] = u))
      : (u = t[5]);
    const d = u;
    let p;
    t[6] !== a || t[7] !== s || t[8] !== r
      ? ((p = () => {
          const e = new Blob([whe(r, s)], { type: "text/csv;charset=utf-8" });
          !(function (e, t) {
            const n = NN(e);
            if (!n) return void t();
            const r = zF({
              artifact_type: "file",
              origin: "assistant_generated",
              action: "download",
              surface: "inline",
              conversation_id: n.conversationId,
              message_id: n.message.id,
            });
            try {
              t();
            } catch (s) {
              throw (r("failed", s), s);
            }
            r("saved");
          })(a, () =>
            khe(
              e,
              (function (e) {
                const t = (e ?? "").replace(/[\\/:*?"<>|\s\p{Cc}]+/gu, " ");
                return `${
                  Array.from(t)
                    .slice(0, 100)
                    .join("")
                    .replace(/^[\s.]+|[\s.]+$/g, "") || "table"
                }.csv`;
              })(r.title),
            ),
          );
        }),
        (t[6] = a),
        (t[7] = s),
        (t[8] = r),
        (t[9] = p))
      : (p = t[9]);
    const h = p;
    let f;
    t[10] !== i.expandable || t[11] !== i.onToggle
      ? ((f = i.expandable ? () => Rpe(i.onToggle) : null),
        (t[10] = i.expandable),
        (t[11] = i.onToggle),
        (t[12] = f))
      : (f = t[12]);
    const m = ome(f);
    let g;
    t[13] !== d ||
    t[14] !== c ||
    t[15] !== i ||
    t[16] !== a ||
    t[17] !== h ||
    t[18] !== o
      ? ((g = {
          copyText: c,
          copyHtml: d,
          onDownload: h,
          expand: i,
          onReply: o,
          messageId: a,
        }),
        (t[13] = d),
        (t[14] = c),
        (t[15] = i),
        (t[16] = a),
        (t[17] = h),
        (t[18] = o),
        (t[19] = g))
      : (g = t[19]);
    const v = Bpe(g);
    let y;
    return (
      t[20] !== m || t[21] !== v
        ? ((y = { tap: m, pressMenu: v }),
          (t[20] = m),
          (t[21] = v),
          (t[22] = y))
        : (y = t[22]),
      y
    );
  })(B);
  let z;
  s[32] === Symbol.for("react.memo_cache_sentinel")
    ? ((z = (e) =>
        l((t) =>
          (function (e, t) {
            return e?.column !== t
              ? { column: t, direction: "desc" }
              : "desc" === e.direction
                ? { column: t, direction: "asc" }
                : null;
          })(t, e),
        )),
      (s[32] = z))
    : (z = s[32]);
  const U = (function (e, t) {
    const r = n.c(4),
      s = ye() ?? !1;
    let i;
    return (
      r[0] !== t || r[1] !== s || r[2] !== e
        ? ((i = (n) => ({
            layout: !s && "position",
            layoutDependency: `${t}:${e?.column ?? ""}:${e?.direction ?? ""}`,
            transition: Je(_me(e, t), n),
          })),
          (r[0] = t),
          (r[1] = s),
          (r[2] = e),
          (r[3] = i))
        : (i = r[3]),
      i
    );
  })(a, x);
  let q;
  return (
    s[33] !== L ||
    s[34] !== D ||
    s[35] !== x ||
    s[36] !== v ||
    s[37] !== p ||
    s[38] !== w ||
    s[39] !== y ||
    s[40] !== b ||
    s[41] !== d ||
    s[42] !== f ||
    s[43] !== m ||
    s[44] !== a ||
    s[45] !== H ||
    s[46] !== U ||
    s[47] !== P ||
    s[48] !== T ||
    s[49] !== j
      ? ((q = {
          openCard: w,
          ...L,
          receipt: b,
          model: v,
          order: y,
          shape: P,
          template: T,
          expanded: x,
          expandable: D,
          toggleExpanded: j,
          ...H,
          sort: a,
          onSort: z,
          rowMotion: U,
          replyRef: d,
          onReply: p,
          clipRef: c,
          gridRef: u,
          setScroller: m,
          setBody: f,
        }),
        (s[33] = L),
        (s[34] = D),
        (s[35] = x),
        (s[36] = v),
        (s[37] = p),
        (s[38] = w),
        (s[39] = y),
        (s[40] = b),
        (s[41] = d),
        (s[42] = f),
        (s[43] = m),
        (s[44] = a),
        (s[45] = H),
        (s[46] = U),
        (s[47] = P),
        (s[48] = T),
        (s[49] = j),
        (s[50] = q))
      : (q = s[50]),
    q
  );
}
