// Readability-only formatting of exact raw evidence. Source offsets/SHA: stream-records-compat.meta.json
function fg(e) {
  return "records" === e.shape
    ? e
    : "snapshot" === e.type
      ? {
          type: "snapshot",
          shape: "records",
          data: pg(e.data),
          hasMore: e.hasMore,
          oldestCursor: e.oldestCursor,
          repliedMessages: e.repliedMessages,
        }
      : {
          type: "patches",
          shape: "records",
          data: e.data.map((e) =>
            (function (e) {
              switch (e.op) {
                case "message_add":
                  return {
                    op: "entry_add",
                    entry: "message",
                    id: e.messageId,
                    afterId: e.afterId,
                    value: e.value,
                  };
                case "message_update":
                  return {
                    op: "message_update",
                    id: e.messageId,
                    fields: e.fields,
                  };
                case "tool_add":
                  return { op: "tool_add", id: e.messageId, value: e.value };
                case "tool_update":
                  return {
                    op: "tool_update",
                    id: e.messageId,
                    dispatchId: e.dispatchId,
                    fields: e.fields,
                  };
                case "panel_add":
                  return { op: "panel_add", id: e.messageId, value: e.value };
                case "panel_update":
                  return {
                    op: "panel_update",
                    id: e.messageId,
                    dispatchId: e.dispatchId,
                    fields: e.fields,
                  };
                case "panel_remove":
                  return {
                    op: "panel_remove",
                    id: e.messageId,
                    dispatchId: e.dispatchId,
                  };
                default:
                  return e;
              }
            })(e),
          ),
          repliedMessages: e.repliedMessages,
          seq: e.seq,
        };
}
