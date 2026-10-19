// Readability-only formatting of exact raw evidence. Source offsets/SHA: cards-render-dispatch.meta.json
const Xge = "list",
  Zge = {
    text: () => null,
    link: (e) => s.jsx(epe, { links: [{ url: e.url }] }),
    links: (e) => s.jsx(epe, { links: e.links }),
    images: (e, { prose: t }) => s.jsx(Ude, { images: e.images, fallback: t }),
    attached_images: (e, { prose: t }) =>
      s.jsx(Kde, { attachments: e.attachments, fallback: t }),
    summary: (e, { message: t }) =>
      s.jsx(Gge, {
        markdown: e.markdown,
        title: e.title,
        origin: e.origin,
        replyMessageId: t.id,
        linkUrl: t.presentation?.url,
      }),
    code: (e, { message: t }) =>
      s.jsx($pe, {
        code: e.source,
        language: e.language,
        title: e.title,
        replyMessageId: t.id,
        linkUrl: t.presentation?.url,
      }),
    text_template: (e, { message: t }) =>
      s.jsx(Kge, {
        body: e.body,
        title: e.title,
        replyMessageId: t.id,
        linkUrl: t.presentation?.url,
      }),
    table: (e, { message: t }) =>
      s.jsx(Sme, {
        table: e.table,
        marks: e.marks,
        ornament: e.ornament,
        replyMessageId: t.id,
        linkUrl: t.presentation?.url,
      }),
    product_grid: (e, { message: t }) =>
      s.jsx(xge, {
        table: e.table,
        photos: e.photos,
        marks: e.marks,
        as: Xge,
        ornament: e.ornament,
        replyMessageId: t.id,
        linkUrl: t.presentation?.url,
      }),
    flight_list: (e, { message: t }) =>
      s.jsx(Fme, {
        table: e.table,
        flightColumn: e.flightColumn,
        marks: e.marks,
        ornament: e.ornament,
        replyMessageId: t.id,
        linkUrl: t.presentation?.url,
      }),
    order_summary: (e, { message: t }) =>
      s.jsx(uge, {
        order: e.order,
        ornament: e.ornament,
        replyMessageId: t.id,
        linkUrl: t.presentation?.url,
        action: e.action,
      }),
    declined: (e, { message: t }) =>
      s.jsx(Gpe, {
        title: t.presentation?.title,
        subtitle: t.presentation?.subtitle,
        messageIds: e.messageIds,
        ownerMessageId: t.id,
      }),
  };
function Jge(e, t) {
  return Zge[e.kind](e, t);
}
const eve = {
  summary: (e) =>
    s.jsx(Gge, { markdown: e.markdown, title: e.title, origin: e.origin }),
  code: (e) =>
    s.jsx($pe, { code: e.source, language: e.language, title: e.title }),
  text_template: (e) => s.jsx(Kge, { body: e.body, title: e.title }),
  table: (e) => s.jsx(Sme, { table: e.table, marks: e.marks }),
  product_grid: (e) =>
    s.jsx(xge, { table: e.table, photos: e.photos, marks: e.marks, as: Xge }),
  flight_list: (e) =>
    s.jsx(Fme, {
      table: e.table,
      flightColumn: e.flightColumn,
      marks: e.marks,
    }),
  order_summary: (e) => s.jsx(uge, { order: e.order }),
};
function tve(e) {
  return eve[e.kind]?.(e) ?? null;
}
