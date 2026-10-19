package org.research.kimistyle;

/** Tests D state invariants, never proof of original-product behavior. */
public final class ReferenceStateTest {
    private static int assertions = 0;
    private static void check(boolean condition, String message) {
        assertions++; if (!condition) throw new AssertionError(message);
    }
    public static void main(String[] args) {
        ReferenceState s = new ReferenceState();
        check(s.submit() == -1, "empty submit is blocked");
        s.draft = "Reply only OK"; long first = s.submit();
        check(s.draft.isEmpty() && s.prompt.equals("Reply only OK"), "submit moves draft to message");
        check(s.submit() == -1, "repeat submit while busy blocked");
        s.draft = "draft during output"; s.stop();
        check(!s.complete(first), "late completion rejected after cancel");
        check(s.draft.equals("draft during output"), "stop preserves new draft");
        s.simulateFailure = true; long failure = s.retry(); s.complete(failure);
        check(s.status.equals("error") && s.prompt.equals("Reply only OK"), "error retains message");
        s.simulateFailure = false; long retry = s.retry(); s.complete(retry);
        check(s.answer.equals("OK") && s.draft.equals("draft during output"), "retry success preserves composer");
        s.openFont(); s.pendingFont = "Extra Large"; s.abandonFont();
        check(s.font.equals("Follow System") && s.pendingFont.equals(s.font), "Back abandons font preview");
        s.openFont(); s.pendingFont = "Extra Large"; s.saveFont();
        check(s.font.equals("Extra Large") && s.page.equals("chat"), "font save commits and returns home");
        s.documentDraft = "Research reading list"; long doc = s.saveDocument(); s.cancelDocument();
        check(!s.finishDocument(doc) && !s.documentDraft.isEmpty(), "document cancel preserves draft and rejects late result");
        s.simulateFailure = true; doc = s.saveDocument(); s.finishDocument(doc);
        check(s.documentStatus.equals("error") && !s.documentDraft.isEmpty(), "document error keeps draft");
        s.simulateFailure = false; doc = s.saveDocument(); s.finishDocument(doc);
        check(s.document.equals("Research reading list") && s.documentDraft.isEmpty(), "document retry commits once");
        s.draft = "Another request"; long chat = s.submit(); s.documentDraft = "Independent note"; doc = s.saveDocument();
        check(s.complete(chat) && s.finishDocument(doc), "independent domains do not invalidate one another");
        s.resetChat(); s.prompt="Run03 mdel K2.8 test. Reply only RUN03-K28-OK."; s.capacityError=true; s.simulateFailure=true; s.status="error";
        long capacity=s.retry();s.complete(capacity);
        check(s.status.equals("error") && s.answer.equals(ReferenceState.CAPACITY_MESSAGE) && s.prompt.startsWith("Run03 mdel"), "capacity fixture retry retains the exact source sample and error");
        s.resetChat();check(!s.capacityError && !s.simulateFailure, "new chat clears local capacity failure fixture");
        System.out.println("PASS " + assertions + " state invariants (D implementation only)");
    }
}
