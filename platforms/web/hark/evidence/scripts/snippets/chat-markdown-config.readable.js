// Readability-only formatting of exact raw evidence. Source offsets/SHA: chat-markdown-config.meta.json
const D5 = { remarkPlugins: X0, skipHtml: !0, controls: !1, lineNumbers: !1 },
  j5 = {
    ...D5,
    remarkPlugins: [
      ...X0,
      function () {
        const e = this.parser;
        if (!e) return;
        const t = U0(4e3);
        this.parser = (n, r) => {
          const s = t.get(n);
          if (s) return structuredClone(s);
          const i = e(n, r);
          return (t.set(n, structuredClone(i)), i);
        };
      },
    ],
    parseMarkdownIntoBlocksFn: $0,
  };
function P5(e, t) {
  return s.jsx(B0, { ...(t ? j5 : D5), components: h3, children: e });
}
