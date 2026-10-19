// Readability-only formatting of exact raw evidence. Source offsets/SHA: chat-text-rendering.meta.json
function n6(e) {
  const t = n.c(65),
    {
      message: r,
      displaySegments: i,
      displayText: o,
      clampContent: a,
      clampMaxHeight: l,
      clampMode: c,
      bubbleClassName: u,
      bubbleRef: d,
      renderTextViewport: p,
      runPhase: h,
      disableSettleMorph: f,
      suppressNarration: m,
      settleFade: g,
    } = e,
    v = void 0 !== a && a,
    y = void 0 !== f && f,
    b = void 0 !== m && m,
    _ = void 0 !== g && g,
    x = o ?? r.content;
  let w;
  t[0] !== i || t[1] !== x
    ? ((w = i ?? bB(x)), (t[0] = i), (t[1] = x), (t[2] = w))
    : (w = t[2]);
  const k = w,
    E = void 0 !== l ? l : v ? U5 : void 0;
  let T;
  t[3] !== k || t[4] !== r
    ? ((T = q4(r, k)), (t[3] = k), (t[4] = r), (t[5] = T))
    : (T = t[5]);
  const S = T;
  let I;
  var A;
  t[6] !== S ||
  t[7] !== r.content ||
  t[8] !== r.forwardedTranscript ||
  t[9] !== r.role
    ? ((I =
        "user" === r.role && "forwardedTranscript" === S
          ? (((A = r.forwardedTranscript) && 0 !== A.entries.length
              ? {
                  fromMs: A.from,
                  toMs: A.to,
                  entries: A.entries.map((e) => ({
                    role: e.role,
                    content: e.content,
                  })),
                }
              : null) ??
            (function (e) {
              const t = e.trim(),
                n = D4.exec(t);
              if (null === n) return null;
              const r = `</${n[1]}>`;
              if (!t.endsWith(r)) return null;
              const s = n[2] ?? "",
                i = F4(s, "from"),
                o = F4(s, "to");
              if (null === i || null === o) return null;
              const a = [];
              let l = t.slice(n[0].length, t.length - r.length);
              for (; l.length > 0;) {
                const e = j4.exec(l),
                  t = e?.[1],
                  n = e?.[2];
                if (null === e || void 0 === t || void 0 === n) return null;
                (a.push({ role: t, content: P4(n) }),
                  (l = l.slice(e[0].length)));
              }
              return 0 === a.length ? null : { fromMs: i, toMs: o, entries: a };
            })(r.content))
          : null),
      (t[6] = S),
      (t[7] = r.content),
      (t[8] = r.forwardedTranscript),
      (t[9] = r.role),
      (t[10] = I))
    : (I = t[10]);
  const C = I;
  if (!$4(r, S)) return null;
  if ("user" === r.role) {
    if ("forwardedTranscript" === S) {
      const e = C;
      if (e) {
        let n;
        return (
          t[11] !== u || t[12] !== d || t[13] !== e
            ? ((n = s.jsx(L4, { delta: e, className: u, bubbleRef: d })),
              (t[11] = u),
              (t[12] = d),
              (t[13] = e),
              (t[14] = n))
            : (n = t[14]),
          n
        );
      }
    }
    let e;
    return (
      t[15] !== u ||
      t[16] !== d ||
      t[17] !== c ||
      t[18] !== E ||
      t[19] !== r.content ||
      t[20] !== r.secrets ||
      t[21] !== p
        ? ((e = s.jsx(e6, {
            content: r.content,
            secrets: r.secrets,
            clampMaxHeight: E,
            clampMode: c,
            className: u,
            bubbleRef: d,
            renderTextViewport: p,
          })),
          (t[15] = u),
          (t[16] = d),
          (t[17] = c),
          (t[18] = E),
          (t[19] = r.content),
          (t[20] = r.secrets),
          (t[21] = p),
          (t[22] = e))
        : (e = t[22]),
      e
    );
  }
  const N = void 0 !== h && void 0 === E,
    R = !r.isStreaming;
  switch (S) {
    case "streaming": {
      const e = r.narration?.line;
      if (b || !e) return null;
      if (N) {
        let n;
        return (
          t[23] !== u || t[24] !== d || t[25] !== e || t[26] !== h
            ? ((n = s.jsx(F2, {
                phase: h,
                narrative: e,
                className: u,
                surfaceRef: d,
              })),
              (t[23] = u),
              (t[24] = d),
              (t[25] = e),
              (t[26] = h),
              (t[27] = n))
            : (n = t[27]),
          n
        );
      }
      let n;
      return (
        t[28] !== e
          ? ((n = s.jsx(Q5, { line: e })), (t[28] = e), (t[29] = n))
          : (n = t[29]),
        n
      );
    }
    case "empty":
      return null;
    case "scheduledTask": {
      let e;
      return (
        t[30] !== u ||
        t[31] !== r.content ||
        t[32] !== r.source ||
        t[33] !== r.task_name ||
        t[34] !== R
          ? ((e = s.jsx(K5, {
              taskName: r.task_name,
              source: r.source,
              content: r.content,
              settled: R,
              className: u,
            })),
            (t[30] = u),
            (t[31] = r.content),
            (t[32] = r.source),
            (t[33] = r.task_name),
            (t[34] = R),
            (t[35] = e))
          : (e = t[35]),
        e
      );
    }
    case "segmented": {
      let e;
      return (
        t[36] !== u ||
        t[37] !== d ||
        t[38] !== c ||
        t[39] !== x ||
        t[40] !== E ||
        t[41] !== p ||
        t[42] !== R
          ? ((e = s.jsx(Z5, {
              displayText: x,
              settled: R,
              clampMaxHeight: E,
              clampMode: c,
              className: u,
              bubbleRef: d,
              renderTextViewport: p,
            })),
            (t[36] = u),
            (t[37] = d),
            (t[38] = c),
            (t[39] = x),
            (t[40] = E),
            (t[41] = p),
            (t[42] = R),
            (t[43] = e))
          : (e = t[43]),
        e
      );
    }
    case "forwardedTranscript":
    case "text": {
      if (N && !y) {
        const e = r.narration?.line;
        let n, i;
        return (
          t[44] !== x || t[45] !== R
            ? ((n = P5(x, R)), (t[44] = x), (t[45] = R), (t[46] = n))
            : (n = t[46]),
          t[47] !== u ||
          t[48] !== d ||
          t[49] !== h ||
          t[50] !== e ||
          t[51] !== n
            ? ((i = s.jsx(F2, {
                phase: h,
                narrative: e,
                className: u,
                surfaceRef: d,
                testId: "message-text",
                children: n,
              })),
              (t[47] = u),
              (t[48] = d),
              (t[49] = h),
              (t[50] = e),
              (t[51] = n),
              (t[52] = i))
            : (i = t[52]),
          i
        );
      }
      const e = r.role,
        n = _ && b5;
      let i, o;
      return (
        t[53] !== u || t[54] !== n
          ? ((i = en(u, n)), (t[53] = u), (t[54] = n), (t[55] = i))
          : (i = t[55]),
        t[56] !== d ||
        t[57] !== c ||
        t[58] !== x ||
        t[59] !== E ||
        t[60] !== r.role ||
        t[61] !== p ||
        t[62] !== R ||
        t[63] !== i
          ? ((o = s.jsx(J5, {
              displayText: x,
              settled: R,
              role: e,
              clampMaxHeight: E,
              clampMode: c,
              className: i,
              bubbleRef: d,
              renderTextViewport: p,
            })),
            (t[56] = d),
            (t[57] = c),
            (t[58] = x),
            (t[59] = E),
            (t[60] = r.role),
            (t[61] = p),
            (t[62] = R),
            (t[63] = i),
            (t[64] = o))
          : (o = t[64]),
        o
      );
    }
  }
}
