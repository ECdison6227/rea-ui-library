// Readability-only formatting of exact raw evidence. Source offsets/SHA: website-artifact-preview.meta.json
function Mn(r) {
  const l = s.c(39),
    { toolUse: d, heroSrc: u } = r,
    { _: m } = e(),
    { projectName: f, projectId: p } = C(d),
    h = S({ failed: !1, projectId: p, status: d.status }),
    b = Boolean(p),
    [g, x] = t.useState(!1);
  let y;
  l[0] !== b || l[1] !== d.dispatchId || l[2] !== d.toolUseId
    ? ((y = (e) => {
        b &&
          !window.getSelection()?.toString() &&
          (T(d.toolUseId ?? d.dispatchId ?? ""),
          A(M(e), "website", d.dispatchId));
      }),
      (l[0] = b),
      (l[1] = d.dispatchId),
      (l[2] = d.toolUseId),
      (l[3] = y))
    : (y = l[3]);
  const v = y,
    j = c,
    _ = !b && qn;
  let w, k, I;
  (l[4] !== _ ? ((w = a(zn, _)), (l[4] = _), (l[5] = w)) : (w = l[5]),
    l[6] !== v
      ? ((k = (e) => v(e.currentTarget)), (l[6] = v), (l[7] = k))
      : (k = l[7]),
    l[8] !== g || l[9] !== u
      ? ((I =
          u &&
          !g &&
          n.jsx("img", {
            className: Tn,
            src: u,
            alt: "",
            draggable: !1,
            loading: "lazy",
            decoding: "async",
            "data-testid": "website-hero",
            onError: () => x(!0),
          })),
        (l[8] = g),
        (l[9] = u),
        (l[10] = I))
      : (I = l[10]));
  const N = An,
    F = An,
    U = q,
    L = z(f) || m({ id: "kveQAJ" });
  let E, $, D, K;
  (l[11] !== m || l[12] !== h
    ? ((E = m(h ? { id: "J12FgJ" } : { id: "PQAuAL" })),
      (l[11] = m),
      (l[12] = h),
      (l[13] = E))
    : (E = l[13]),
    l[14] !== E
      ? (($ = n.jsx("span", { className: R, children: E })),
        (l[14] = E),
        (l[15] = $))
      : ($ = l[15]),
    l[16] !== U || l[17] !== $ || l[18] !== L
      ? ((D = n.jsx(U, { title: L, children: $ })),
        (l[16] = U),
        (l[17] = $),
        (l[18] = L),
        (l[19] = D))
      : (D = l[19]),
    l[20] !== D || l[21] !== F.text
      ? ((K = n.jsx("div", { className: F.text, children: D })),
        (l[20] = D),
        (l[21] = F.text),
        (l[22] = K))
      : (K = l[22]));
  const O = !b;
  let V, W, H, Q, Z;
  return (
    l[23] !== m
      ? ((V = m({ id: "IAwDz9" })), (l[23] = m), (l[24] = V))
      : (V = l[24]),
    l[25] === Symbol.for("react.memo_cache_sentinel")
      ? ((W = n.jsx(B, { size: i.icon.md })), (l[25] = W))
      : (W = l[25]),
    l[26] !== O || l[27] !== V
      ? ((H = n.jsx(o, {
          color: "secondary",
          variant: "solid",
          className: P,
          disabled: O,
          "aria-label": V,
          "data-testid": "website-open",
          children: W,
        })),
        (l[26] = O),
        (l[27] = V),
        (l[28] = H))
      : (H = l[28]),
    l[29] !== K || l[30] !== H || l[31] !== N.row
      ? ((Q = n.jsxs("div", { className: N.row, children: [K, H] })),
        (l[29] = K),
        (l[30] = H),
        (l[31] = N.row),
        (l[32] = Q))
      : (Q = l[32]),
    l[33] !== j || l[34] !== Q || l[35] !== w || l[36] !== k || l[37] !== I
      ? ((Z = n.jsxs(j, {
          className: w,
          "data-testid": "website-card",
          onClick: k,
          children: [I, Q],
        })),
        (l[33] = j),
        (l[34] = Q),
        (l[35] = w),
        (l[36] = k),
        (l[37] = I),
        (l[38] = Z))
      : (Z = l[38]),
    Z
  );
}
const Rn = t.memo(function (e) {
    const t = s.c(9),
      { face: r } = e,
      i = l(F);
    let o, c, u, m, f;
    return (
      t[0] === Symbol.for("react.memo_cache_sentinel")
        ? ((o = a(U, L, "_1layf820")), (t[0] = o))
        : (o = t[0]),
      t[1] === Symbol.for("react.memo_cache_sentinel")
        ? ((c = n.jsx(d, {})), (t[1] = c))
        : (c = t[1]),
      t[2] !== r ? ((u = E(r)), (t[2] = r), (t[3] = u)) : (u = t[3]),
      t[4] !== u
        ? ((m = n.jsx($.Provider, {
            value: rn,
            children: n.jsx(D, { tenses: u, phase: "idle", oneLine: !0 }),
          })),
          (t[4] = u),
          (t[5] = m))
        : (m = t[5]),
      t[6] !== i || t[7] !== m
        ? ((f = n.jsxs("div", {
            ref: i,
            className: o,
            "data-reply-card-face": "",
            children: [c, m],
          })),
          (t[6] = i),
          (t[7] = m),
          (t[8] = f))
        : (f = t[8]),
      f
    );
  }),
  Bn = t.memo(function (e) {
    const t = s.c(7),
      { icon: r, label: o } = e;
    let a, c, l;
    return (
      t[0] !== r
        ? ((a = n.jsx(r, {
            size: i.icon.xs,
            className: "ampdl81",
            "aria-hidden": !0,
          })),
          (t[0] = r),
          (t[1] = a))
        : (a = t[1]),
      t[2] !== o
        ? ((c = n.jsx("span", { className: "ampdl82", children: o })),
          (t[2] = o),
          (t[3] = c))
        : (c = t[3]),
      t[4] !== a || t[5] !== c
        ? ((l = n.jsxs("span", { className: "ampdl80", children: [a, c] })),
          (t[4] = a),
          (t[5] = c),
          (t[6] = l))
        : (l = t[6]),
      l
    );
  }),
  Pn = t.memo(function (e) {
    const t = s.c(5),
      { cardId: r, title: i, face: o } = e;
    if (null != o) {
      let e;
      return (
        t[0] !== o
          ? ((e = n.jsx(Rn, { face: o })), (t[0] = o), (t[1] = e))
          : (e = t[1]),
        e
      );
    }
    let a;
    return (
      t[2] !== r || t[3] !== i
        ? ((a = n.jsx(Fn, { cardId: r, title: i })),
          (t[2] = r),
          (t[3] = i),
          (t[4] = a))
        : (a = t[4]),
      a
    );
  });
