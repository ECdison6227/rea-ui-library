// Readability-only formatting of exact raw evidence. Source offsets/SHA: chat-live-text-preparation.meta.json
function a6(t) {
  const r = n.c(47),
    { message: i, gestureScope: o, ownScope: a, showTimestampOnHover: l } = t,
    c = void 0 !== l && l,
    u = (function (t) {
      const r = n.c(17),
        s = !!t.isStreaming,
        [i, o] = e.useState(s);
      let a;
      (s && !i && o(!0),
        r[0] !== i || r[1] !== s || r[2] !== t
          ? ((a = (function ({ isLiveRun: e, isStreaming: t, hasText: n }) {
              if (e) return n ? (t ? "streaming" : "settled") : "thinking";
            })({ isLiveRun: i, isStreaming: s, hasText: U4(t) })),
            (r[0] = i),
            (r[1] = s),
            (r[2] = t),
            (r[3] = a))
          : (a = r[3]));
      const l = a;
      let c;
      r[4] !== s || r[5] !== t.content
        ? ((c = s
            ? (function (e) {
                if (!e) return e;
                const t = e.match(/(```|\*\*\*|\*\*|__|~~|`|\*|_)\s*$/);
                if (t && void 0 !== t.index) return e.slice(0, t.index);
                const n = e.lastIndexOf("[");
                return n > e.lastIndexOf("]") ? e.slice(0, n) : e;
              })(t.content)
            : t.content),
          (r[4] = s),
          (r[5] = t.content),
          (r[6] = c))
        : (c = r[6]);
      const u = c;
      let d;
      r[7] !== u ? ((d = bB(u)), (r[7] = u), (r[8] = d)) : (d = r[8]);
      const p = d;
      let h, f;
      return (
        r[9] !== p || r[10] !== t
          ? ((h = q4(t, p)), (r[9] = p), (r[10] = t), (r[11] = h))
          : (h = r[11]),
        r[12] !== p || r[13] !== u || r[14] !== l || r[15] !== h
          ? ((f = {
              displayText: u,
              displaySegments: p,
              runPhase: l,
              contentType: h,
            }),
            (r[12] = p),
            (r[13] = u),
            (r[14] = l),
            (r[15] = h),
            (r[16] = f))
          : (f = r[16]),
        f
      );
    })(i),
    d = (function (t, r) {
      const s = n.c(6),
        { live: i } = t;
      let o;
      s[0] !== r || s[1] !== i
        ? ((o = (e) => i.subscribe(r, e)), (s[0] = r), (s[1] = i), (s[2] = o))
        : (o = s[2]);
      const a = o;
      let l;
      return (
        s[3] !== r || s[4] !== i
          ? ((l = () => i.get(r)), (s[3] = r), (s[4] = i), (s[5] = l))
          : (l = s[5]),
        e.useSyncExternalStore(a, l, y5)
      );
    })(o, i.id),
    p = !!i.isStreaming;
  let h;
  r[0] !== i || r[1] !== c
    ? ((h = c ? z5(i) : null), (r[0] = i), (r[1] = c), (r[2] = h))
    : (h = r[2]);
  const f = h,
    m = p ? null : f;
  let g, v;
  (r[3] !== t || r[4] !== u
    ? ((g = s.jsx(u6, { ...t, text: u })), (r[3] = t), (r[4] = u), (r[5] = g))
    : (g = r[5]),
    r[6] !== d ||
    r[7] !== o.coarsePointer ||
    r[8] !== p ||
    r[9] !== i ||
    r[10] !== t.reactionBadge ||
    r[11] !== t.timestampNotes ||
    r[12] !== g ||
    r[13] !== u ||
    r[14] !== m ||
    r[15] !== f
      ? ((v = s.jsx(c6, {
          message: i,
          text: u,
          coarsePointer: o.coarsePointer,
          gesture: d,
          timestampValue: f,
          timestamp: m,
          isStreaming: p,
          reactionBadge: t.reactionBadge,
          timestampNotes: t.timestampNotes,
          messageContent: g,
        })),
        (r[6] = d),
        (r[7] = o.coarsePointer),
        (r[8] = p),
        (r[9] = i),
        (r[10] = t.reactionBadge),
        (r[11] = t.timestampNotes),
        (r[12] = g),
        (r[13] = u),
        (r[14] = m),
        (r[15] = f),
        (r[16] = v))
      : (v = r[16]));
  const y = v,
    b = a?.attach,
    _ = d?.selectable && "_151xtfxz";
  let x;
  r[17] !== t.wrapperClassName || r[18] !== _
    ? ((x = en(x5, _, t.wrapperClassName)),
      (r[17] = t.wrapperClassName),
      (r[18] = _),
      (r[19] = x))
    : (x = r[19]);
  const w = !!m;
  let k;
  r[20] !== o.token || r[21] !== i.id || r[22] !== w || r[23] !== u.contentType
    ? ((k = (function ({ id: e, scope: t, kind: n, timestampShown: r }) {
        return { [V4]: e, [W4]: t, [Y4]: n, [Q4]: r ? "" : void 0 };
      })({ id: i.id, scope: o.token, kind: u.contentType, timestampShown: w })),
      (r[20] = o.token),
      (r[21] = i.id),
      (r[22] = w),
      (r[23] = u.contentType),
      (r[24] = k))
    : (k = r[24]);
  const E = a?.handlers;
  let T, S, I, A;
  return (
    r[25] !== y || r[26] !== i || r[27] !== t
      ? ((T =
          "assistant" === i.role
            ? s.jsxs(s.Fragment, {
                children: [
                  v6(i) && s.jsx(y6, { ...t }),
                  y,
                  s.jsx(x6, { ...t }),
                ],
              })
            : s.jsxs(s.Fragment, {
                children: [r6(i) && s.jsx(s6, { message: i }), y],
              })),
        (r[25] = y),
        (r[26] = i),
        (r[27] = t),
        (r[28] = T))
      : (T = r[28]),
    r[29] !== i || r[30] !== t
      ? ((S =
          (function (e) {
            return !!e.sendFailed || !!e.sendQueued;
          })(i) && s.jsx(O6, { ...t })),
        (r[29] = i),
        (r[30] = t),
        (r[31] = S))
      : (S = r[31]),
    r[32] !== d ||
    r[33] !== o.coarsePointer ||
    r[34] !== i ||
    r[35] !== t.isThreadOverlayOpen ||
    r[36] !== t.onReply ||
    r[37] !== u.contentType
      ? ((I =
          d &&
          s.jsx(l6, {
            gesture: d,
            message: i,
            onReply: t.onReply,
            isThreadOverlayOpen: t.isThreadOverlayOpen ?? !1,
            kind: u.contentType,
            coarsePointer: o.coarsePointer,
          })),
        (r[32] = d),
        (r[33] = o.coarsePointer),
        (r[34] = i),
        (r[35] = t.isThreadOverlayOpen),
        (r[36] = t.onReply),
        (r[37] = u.contentType),
        (r[38] = I))
      : (I = r[38]),
    r[39] !== T ||
    r[40] !== S ||
    r[41] !== I ||
    r[42] !== b ||
    r[43] !== x ||
    r[44] !== k ||
    r[45] !== E
      ? ((A = s.jsxs("div", {
          ref: b,
          className: x,
          "data-testid": "message-bubble",
          ...k,
          ...E,
          children: [T, S, I],
        })),
        (r[39] = T),
        (r[40] = S),
        (r[41] = I),
        (r[42] = b),
        (r[43] = x),
        (r[44] = k),
        (r[45] = E),
        (r[46] = A))
      : (A = r[46]),
    A
  );
}
