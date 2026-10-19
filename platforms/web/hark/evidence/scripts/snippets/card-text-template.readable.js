// Readability-only formatting of exact raw evidence. Source offsets/SHA: card-text-template.meta.json
function Kge(t) {
  const r = n.c(36),
    { body: i, title: o, replyMessageId: a, linkUrl: l } = t,
    { _: c } = d(),
    { expanded: u, toggle: p } = zpe(a),
    h = e.useRef(null),
    { replyRef: f, onReply: m } = Fpe(a),
    g = qpe(h, u, i),
    v = g || u;
  let y, b;
  (r[0] !== u || r[1] !== v || r[2] !== p
    ? ((y = { expanded: u, expandable: v, onToggle: p }),
      (r[0] = u),
      (r[1] = v),
      (r[2] = p),
      (r[3] = y))
    : (y = r[3]),
    r[4] !== i || r[5] !== m || r[6] !== a || r[7] !== y
      ? ((b = { copyText: i, expand: y, onReply: m, messageId: a }),
        (r[4] = i),
        (r[5] = m),
        (r[6] = a),
        (r[7] = y),
        (r[8] = b))
      : (b = r[8]));
  const _ = Bpe(b),
    x = u && xpe;
  let w, k;
  (r[9] !== x
    ? ((w = en(_pe, "_1qc6wrt0", x)), (r[9] = x), (r[10] = w))
    : (w = r[10]),
    r[11] !== c || r[12] !== o
      ? ((k = o ?? c({ id: "_K2CvV" })), (r[11] = c), (r[12] = o), (r[13] = k))
      : (k = r[13]));
  const E = Npe(void 0, l) ? "two-line" : "one-line",
    T = g || u;
  let S, I;
  (r[14] !== u ||
  r[15] !== l ||
  r[16] !== k ||
  r[17] !== E ||
  r[18] !== T ||
  r[19] !== p
    ? ((S = s.jsx(Dpe, {
        title: k,
        bar: E,
        linkUrl: l,
        disclosure: "mark",
        expanded: u,
        expandable: T,
        onToggleExpand: p,
      })),
      (r[14] = u),
      (r[15] = l),
      (r[16] = k),
      (r[17] = E),
      (r[18] = T),
      (r[19] = p),
      (r[20] = S))
    : (S = r[20]),
    r[21] === Symbol.for("react.memo_cache_sentinel")
      ? ((I = s.jsx("div", { className: Ipe })), (r[21] = I))
      : (I = r[21]));
  const A = g && Ape;
  let C, N, R, O;
  return (
    r[22] !== A
      ? ((C = en("_1qc6wrt1", A)), (r[22] = A), (r[23] = C))
      : (C = r[23]),
    r[24] !== i || r[25] !== C
      ? ((N = s.jsx("pre", {
          ref: h,
          className: C,
          "data-testid": "text-template-body",
          children: i,
        })),
        (r[24] = i),
        (r[25] = C),
        (r[26] = N))
      : (N = r[26]),
    r[27] !== _ || r[28] !== f || r[29] !== N || r[30] !== w || r[31] !== S
      ? ((R = s.jsxs(yn, {
          ref: f,
          className: w,
          tabIndex: 0,
          "data-artifact-card": !0,
          "data-testid": "text-template-card",
          ..._,
          children: [S, I, N],
        })),
        (r[27] = _),
        (r[28] = f),
        (r[29] = N),
        (r[30] = w),
        (r[31] = S),
        (r[32] = R))
      : (R = r[32]),
    r[33] !== m || r[34] !== R
      ? ((O = s.jsx(Xpe, { onReply: m, children: R })),
        (r[33] = m),
        (r[34] = R),
        (r[35] = O))
      : (O = r[35]),
    O
  );
}
