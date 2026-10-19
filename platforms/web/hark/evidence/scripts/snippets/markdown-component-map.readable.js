// Readability-only formatting of exact raw evidence. Source offsets/SHA: markdown-component-map.meta.json
const p3 = {
  p: (e) => s.jsx("p", { ...e, className: "_10dfkk10" }),
  h1: (e) => s.jsx("h1", { ...e, className: "_10dfkk11" }),
  h2: (e) => s.jsx("h2", { ...e, className: "_10dfkk12" }),
  h3: (e) => s.jsx("h3", { ...e, className: "_10dfkk13" }),
  h4: (e) => s.jsx("h4", { ...e, className: "_10dfkk14" }),
  h5: (e) => s.jsx("h5", { ...e, className: "_10dfkk15" }),
  h6: (e) => s.jsx("h6", { ...e, className: "_10dfkk16" }),
  blockquote: (e) => s.jsx("blockquote", { ...e, className: "_10dfkk17" }),
  hr: (e) => s.jsx("hr", { ...e, className: "_10dfkk18" }),
  strong: (e) => s.jsx("strong", { ...e, className: "_10dfkk19" }),
  em: (e) => s.jsx("em", { ...e, className: "_10dfkk1d" }),
  del: (e) => s.jsx("del", { ...e, className: "_10dfkk1e" }),
  ul: (e) => s.jsx("ul", { ...e, className: "_10dfkk1a" }),
  ol: ({ className: e, start: t, style: n, ...r }) =>
    s.jsx("ol", {
      ...r,
      start: t,
      style: c3(t, n),
      className: e ? `${J2} ${e}` : J2,
    }),
  li: (e) => s.jsx("li", { ...e, className: "_10dfkk1c" }),
  input: (e) =>
    "checkbox" === e.type
      ? s.jsx("input", { ...e, className: "_10dfkk1f", disabled: !0 })
      : s.jsx("input", { ...e }),
  pre: (e) => s.jsx("pre", { ...e, className: "_10dfkk1o" }),
  inlineCode: ({ className: e, children: t, ...n }) =>
    s.jsx("code", { ...n, className: e ? `${e3} ${e}` : e3, children: t }),
  code: ({ className: e, children: t, ...n }) =>
    s.jsx("code", { ...n, className: e, children: t }),
  a: (e) => s.jsx(l3, { ...e, target: "_blank", rel: "noopener noreferrer" }),
  img: (e) => s.jsx(r3, { ...e }),
  table: (e) =>
    s.jsx("div", {
      className: "_10dfkk1g",
      children: s.jsx("table", { ...e, className: "_10dfkk1h" }),
    }),
  thead: (e) => s.jsx("thead", { ...e, className: "_10dfkk1i" }),
  tbody: (e) => s.jsx("tbody", { ...e, className: "_10dfkk1j" }),
  th: (e) => s.jsx("th", { ...e, className: "_10dfkk1k" }),
  tr: (e) => s.jsx("tr", { ...e, className: "_10dfkk1m" }),
  td: ({ children: e, ...t }) =>
    s.jsx("td", { ...t, className: "_10dfkk1l", children: d3(e) }),
};
