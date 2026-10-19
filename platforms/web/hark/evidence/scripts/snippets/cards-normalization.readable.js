// Readability-only formatting of exact raw evidence. Source offsets/SHA: cards-normalization.meta.json
const jO = () => null,
  PO = {
    text: jO,
    link: jO,
    links: (e) =>
      (function (e) {
        const t = [];
        for (const n of e ?? []) {
          if (!nm(n.url) || t.some((e) => e.url === n.url)) continue;
          const e = FO(n.label),
            r = FO(n.title),
            s = nm(n.image) ? n.image : void 0;
          t.push({
            url: n.url,
            ...(e ? { label: e } : {}),
            ...(r ? { title: r } : {}),
            ...(s ? { image: s } : {}),
          });
        }
        return t.length > 0 ? { kind: "links", links: t } : null;
      })(e.links),
    table: (e) =>
      e.table
        ? (function (e) {
            const t = (function ({ title: e, columns: t, rows: n }) {
              if (0 === t.length) return null;
              const r = n.map((e) => qN(e, t.length)),
                s = {
                  title: FO(e) ?? null,
                  subtitle: null,
                  raw: "",
                  headers: t,
                  align: t.map(() => "left"),
                  shares: null,
                  rows: r,
                  ownIndex: XN(t, r),
                };
              return {
                ...s,
                raw: RO(
                  s,
                  r.map((e, t) => t),
                ),
              };
            })(e);
            return t
              ? BO(t, {
                  ornament: FO(e.ornament),
                  marks: KR(e.row_sites, t.rows.length) ?? void 0,
                })
              : null;
          })(e.table)
        : null,
    images: (e) =>
      (function (e) {
        const t = [];
        for (const n of e ?? []) {
          if (!nm(n.url)) continue;
          const e = FO(n.caption);
          t.push(
            e ? { url: n.url, alt: e, title: e } : { url: n.url, alt: "" },
          );
        }
        return t.length > 0 ? { kind: "images", images: t } : null;
      })(e.images),
    summary: (e) => {
      return e.summary && (t = e.summary).markdown.trim()
        ? {
            kind: "summary",
            markdown: t.markdown,
            title: FO(t.title),
            origin: FO(t.source),
          }
        : null;
      var t;
    },
    code: (e) =>
      e.code
        ? {
            kind: "code",
            source: e.code.source,
            language: FO(e.code.language) ?? null,
            title: FO(e.code.title),
          }
        : null,
    text_template: (e) =>
      e.text_template
        ? {
            kind: "text_template",
            body: e.text_template.body,
            title: FO(e.text_template.title),
          }
        : null,
    order_summary: (e) =>
      e.order_summary
        ? (function (e) {
            if (0 === e.line_items.length) return null;
            const t = (function (e) {
              const t = FO(e?.id),
                n = FO(e?.label);
              return t && n ? { id: t, label: n } : void 0;
            })(e.action);
            return {
              kind: "order_summary",
              order:
                ((n = e),
                {
                  merchant: yO(n.merchant) ?? n.merchant,
                  site: YR(n.site),
                  destination: yO(n.destination),
                  orderNumber: yO(n.order_number),
                  items: n.line_items.map(TO),
                  lines: (n.breakdown ?? []).flatMap((e) => {
                    const t = yO(e.label),
                      n = yO(e.amount);
                    return t && n ? [{ label: t, amount: n }] : [];
                  }),
                  payment: AO(n.payment),
                  total: yO(n.total) ?? n.total,
                  legs: EO(n.legs),
                  passengers:
                    ((r = n.passengers),
                    Array.isArray(r)
                      ? r.flatMap(
                          (e) =>
                            (function (e) {
                              if (!vO(e)) return null;
                              const t = yO(e.name);
                              return t ? { name: t, seat: yO(e.seat) } : null;
                            })(e) ?? [],
                        )
                      : []),
                }),
              ornament: FO(e.ornament),
              ...(t ? { action: t } : {}),
            };
            var n, r;
          })(e.order_summary)
        : null,
    declined: (e) => ({ kind: "declined", messageIds: e.messageIds ?? [] }),
    image_attachment: jO,
    video_attachment: jO,
    file_attachment: jO,
  };
function FO(e) {
  const t = e?.trim();
  return t || void 0;
}
function BO(e, t = {}, n = e.rows.length) {
  const r = (function (e) {
    const t = (function (e) {
      if (0 === e.rows.length) return null;
      for (let t = 0; t < e.headers.length; t += 1) {
        const n = e.rows.map((e) => (e[t] ?? "").trim()),
          r = n.filter((e) => null !== MO(e)),
          s = n.filter((e) => "" !== e && null === MO(e));
        if (r.length > 0 && 0 === s.length) return t;
      }
      return null;
    })(e);
    return null === t
      ? null
      : { at: t, photos: e.rows.map((e) => MO(e[t] ?? "")) };
  })(e);
  if (r) return { kind: "product_grid", table: e, photos: r, ...t };
  const s = (function (e, t = e.rows.length) {
    const n = e.rows.slice(0, t);
    if (0 === n.length) return null;
    const r = e.headers.findIndex(
      (e, t) => rO.test(GN(e)) && n.every((e) => null !== uO(aO(e[t]))),
    );
    return -1 === r ? null : r;
  })(e, n);
  return null !== s
    ? { kind: "flight_list", table: e, flightColumn: s, ...t }
    : { kind: "table", table: e, ...t };
}
function HO(e) {
  if ("assistant" !== e.role) return { kind: "text" };
  if (Fm(e))
    return { kind: "attached_images", attachments: e.attachments ?? [] };
  const t = e.presentation,
    n =
      t && ((r = t.kind), Object.prototype.hasOwnProperty.call(PO, r))
        ? (function (e) {
            return PO[e.kind](e);
          })(t)
        : null;
  var r;
  if (n) return n;
  const s = e.content ?? "",
    i = !0 === e.isStreaming,
    o = t?.kind;
  return o && "text" !== o
    ? (function (e, t, n) {
        switch (e) {
          case "link":
          case "links":
            return UO(t) ?? { kind: "text" };
          case "images": {
            const e = (function (e) {
              const t = (e ?? "")
                .split("\n")
                .map((e) => e.trim())
                .filter((e) => "" !== e);
              if (0 === t.length) return null;
              const n = [];
              for (const r of t) {
                const e = FN.exec(r);
                if (!e) return null;
                const t = BN(e[2] ?? "");
                if (!t) return null;
                const s = void 0 === e[3] ? void 0 : zN(e[3]);
                n.push({
                  url: t,
                  alt: zN(e[1] ?? ""),
                  ...(s ? { title: s } : {}),
                });
              }
              return n;
            })(t);
            return e ? { kind: "images", images: e } : { kind: "text" };
          }
          case "code": {
            const e = jN(t);
            return e ? qO(e) : { kind: "text" };
          }
          case "table":
            return zO(t, n) ?? { kind: "text" };
          default:
            return { kind: "text" };
        }
      })(o, s, i)
    : (function (e, t) {
        const n = UO(e);
        if (n) return n;
        const r = jN(e);
        return r ? qO(r) : (zO(e, t) ?? { kind: "text" });
      })(s, i);
}
function zO(e, t) {
  const n = (e) => (t ? e.rows.length - 1 : e.rows.length),
    r = KN(e);
  if (r) return BO(r, {}, n(r));
  const s = (function (e) {
    if (e.includes(nR)) return null;
    const t = e.split("\n"),
      n = (function (e) {
        const t = [];
        let n = 0;
        for (; n < e.length - 1;) {
          if (!oR(e[n]) || !YN(e[n + 1] ?? "")) {
            n++;
            continue;
          }
          let r = n + 2;
          for (; r < e.length && oR(e[r]);) r++;
          (t.push({ start: n, end: r }), (n = r));
        }
        return t;
      })(t),
      r = n[0];
    if (void 0 === r || n.length > 1) return null;
    const s =
        (function (e, t) {
          let n = t - 1;
          for (; n >= 0 && "" === e[n]?.trim();) n--;
          return n >= 0 && WN.test(e[n]?.trim() ?? "") ? n : null;
        })(t, r.start) ?? r.start,
      i = KN(t.slice(s, r.end).join("\n"));
    if (null == i) return null;
    const o = [...t.slice(0, s), ...t.slice(r.end)];
    if (o.some((e) => rR.test(e))) return null;
    const a = (function (e) {
      return e
        .map((e) => tR(e.trim().replace(sR, "").replace(iR, "")).trim())
        .filter((e) => "" !== e)
        .join(" ");
    })(o);
    return (function (e) {
      return "" !== e && e.split(/\s+/).length <= 40;
    })(a)
      ? { table: i, prose: a }
      : null;
  })(e);
  if (!s) return null;
  const i = s.prose ? { ornament: s.prose } : {};
  return BO(s.table, i, n(s.table));
}
function UO(e) {
  const t = (function (e) {
    const t = MN((e ?? "").trim());
    return !t || DN(t) ? null : t;
  })(e);
  if (t) return { kind: "link", url: t };
  const n = (function (e) {
    const t = (e ?? "")
      .split("\n")
      .map((e) => e.trim())
      .filter((e) => "" !== e);
    if (t.length < 2) return null;
    const n = [];
    for (const r of t) {
      const e = MN(r);
      if (!e || DN(e)) return null;
      n.includes(e) || n.push(e);
    }
    return n;
  })(e);
  return n ? { kind: "links", links: n.map((e) => ({ url: e })) } : null;
}
function qO(e) {
  return {
    kind: "code",
    source: e.code,
    language: e.language,
    title: e.title ?? void 0,
  };
}
