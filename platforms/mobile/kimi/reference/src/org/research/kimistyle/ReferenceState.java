package org.research.kimistyle;

/** D: independent local state policy. No original implementation is included. */
public final class ReferenceState {
    public String page = "chat", draft = "", prompt = "", answer = "", status = "empty";
    public String model = "Instant", effort = "High", theme = "Follow System";
    public String font = "Follow System", pendingFont = "Follow System";
    public String documentDraft = "", document = "", documentStatus = "empty";
    public long requestEpoch = 0;
    public long documentEpoch = 0;
    public boolean simulateFailure = false;
    public boolean capacityError = false;
    public static final String CAPACITY_MESSAGE = "Too many people are chatting with Kimi; a subscription will grant you priority access. Upgrade";

    public long submit() {
        if (status.equals("busy") || draft.trim().isEmpty()) return -1;
        prompt = draft.trim(); draft = ""; answer = ""; status = "busy";
        return ++requestEpoch;
    }
    public long retry() {
        if (prompt.isEmpty() || status.equals("busy")) return -1;
        status = "busy"; answer = ""; return ++requestEpoch;
    }
    public boolean complete(long epoch) {
        if (epoch != requestEpoch || !status.equals("busy")) return false;
        if (simulateFailure) { status = "error"; answer = capacityError ? CAPACITY_MESSAGE : "Network is unavailable"; }
        else { status = "complete"; answer = "OK"; }
        return true;
    }
    public void stop() {
        if (!status.equals("busy")) return;
        requestEpoch++; status = "stopped"; answer = "Output stopped";
    }
    public void resetChat() {
        requestEpoch++; draft = ""; prompt = ""; answer = ""; status = "empty"; capacityError = false; simulateFailure = false;
    }
    public void openFont() { pendingFont = font; page = "font"; }
    public void saveFont() { font = pendingFont; page = "chat"; }
    public void abandonFont() { pendingFont = font; page = "settings"; }
    public long saveDocument() {
        if (documentDraft.trim().isEmpty() || documentStatus.equals("busy")) return -1;
        documentStatus = "busy"; return ++documentEpoch;
    }
    public boolean finishDocument(long epoch) {
        if (epoch != documentEpoch || !documentStatus.equals("busy")) return false;
        if (simulateFailure) documentStatus = "error";
        else { document = documentDraft.trim(); documentDraft = ""; documentStatus = "complete"; }
        return true;
    }
    public void cancelDocument() {
        documentEpoch++; documentStatus = document.isEmpty() ? "empty" : "complete";
    }
}
