// Readability-only formatting of exact raw evidence. Source offsets/SHA: stream-manager-link.meta.json
class g {
  handlers;
  channel;
  hello = null;
  view = null;
  closed = !1;
  inTab = !1;
  lost = !1;
  replyDeadline = null;
  managerWatch = null;
  constructor(e) {
    ((this.handlers = e),
      (this.channel = this.openTrustedWorker() ?? this.openInTab()));
  }
  send(e) {
    this.closed ||
      ("hello" === e.type && ((this.hello = e), this.armReplyDeadline()),
      "view" === e.type && (this.view = e),
      this.channel.post(e));
  }
  close() {
    this.closed ||
      (this.stopWaiting(),
      this.channel.post({ type: "bye" }),
      this.channel.close(),
      (this.closed = !0));
  }
  receive(e) {
    this.closed ||
      ("managerLifetime" !== e.type
        ? "unlinked" !== e.type
          ? (this.clearReplyDeadline(),
            "versionMismatch" !== e.type
              ? this.handlers.onEvent(e)
              : this.fallBack("version_mismatch"))
          : this.lose("unlinked")
        : this.watchManager(e.lock));
  }
  openTrustedWorker() {
    if (w) return (this.reportInTab(w), null);
    try {
      return (function (e, t) {
        if ("undefined" == typeof SharedWorker) return null;
        const s = new SharedWorker(
          (function () {
            const e = new URL(p, location.href);
            return `${location.origin}${e.pathname}${e.search}`;
          })(),
          { type: "module", name: "hark-connections" },
        );
        s.addEventListener("error", t);
        const n = s.port;
        return (
          n.addEventListener("message", (t) => e(t.data)),
          n.start(),
          { post: (e) => n.postMessage(e), close: () => n.close() }
        );
      })(
        (e) => this.receive(e),
        () => this.fallBack("worker_failed"),
      );
    } catch (e) {
      return (this.reportInTab("worker_refused", e), null);
    }
  }
  armReplyDeadline() {
    this.inTab ||
      (this.clearReplyDeadline(),
      (this.replyDeadline = setTimeout(
        () => this.fallBack("worker_silent"),
        1e4,
      )));
  }
  clearReplyDeadline() {
    null !== this.replyDeadline &&
      (clearTimeout(this.replyDeadline), (this.replyDeadline = null));
  }
  watchManager(e) {
    if (this.inTab) return;
    this.managerWatch?.abort();
    const t = new AbortController();
    ((this.managerWatch = t), r(e, () => this.lose("manager_gone"), t.signal));
  }
  stopWaiting() {
    (this.clearReplyDeadline(),
      this.managerWatch?.abort(),
      (this.managerWatch = null));
  }
  lose(e) {
    this.closed ||
      this.inTab ||
      this.lost ||
      ((this.lost = !0),
      "manager_gone" === e &&
        (function (e) {
          const t = b;
          ((b = e), null !== t && e - t < 6e4 && (w = "worker_unstable"));
        })(Date.now()),
      this.handlers.onLinkLost(e));
  }
  fallBack(e) {
    this.closed ||
      this.inTab ||
      ("worker_silent" === e && (w = e),
      this.reportInTab(e),
      this.stopWaiting(),
      this.channel.post({ type: "bye" }),
      this.channel.close(),
      (this.channel = this.openInTab()),
      this.hello && this.channel.post(this.hello),
      this.view && this.channel.post(this.view));
  }
  openInTab() {
    this.inTab = !0;
    const e = (function () {
      if (S) return S;
      const e = { id: u(), mode: "in_tab" };
      return (
        (S = new m(
          (function (e) {
            const t = l(),
              s = `manager=${encodeURIComponent(e.id)}&mode=${e.mode}`;
            return {
              conversationSync: (e) =>
                `${t}/sync/conversation?conversationId=${encodeURIComponent(e)}&v=2&${s}`,
              userEvents: `${t}/user/events?${s}`,
            };
          })(e),
          e,
        )),
        S
      );
    })().connectTab((e) => queueMicrotask(() => this.receive(e)));
    return {
      post: (t) => queueMicrotask(() => e.receive(t)),
      close: () => e.close(),
    };
  }
  reportInTab(e, t) {
    this.handlers.onInTab(e);
  }
}
