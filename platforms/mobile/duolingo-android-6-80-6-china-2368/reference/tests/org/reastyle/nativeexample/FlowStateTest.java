package org.reastyle.nativeexample;

/** JVM assertions cover behavior, including cancellation and late completion. No device is touched. */
public final class FlowStateTest {
    private static int checks;
    private static void check(boolean value, String label) {
        checks++; if (!value) throw new AssertionError(label);
    }
    public static void main(String[] args) {
        FlowState f = new FlowState();
        f.open("source-course");
        check(!f.startCourse(), "No course cannot submit");
        f.selectCourse(0);
        check(f.canContinueCourse(), "Selection enables continue");
        check(f.startCourse(), "Selected course starts building");
        check(!f.startCourse(), "Duplicate submit cannot reenter");
        f.back();
        check(f.screen == FlowState.Screen.COURSE && f.course == -1, "Back from building clears source selection");
        f.open("source-privacy");
        check(f.back() && f.screen == FlowState.Screen.PRIVACY, "Privacy consumes Back");
        f.open("transfer-home");
        check(!f.startTransfer(), "No items cannot submit");
        f.toggleItem(0); f.name = "  ";
        check(!f.startTransfer() && !f.validation.isEmpty(), "Blank name is recoverable inline validation");
        f.name = "Reading";
        check(f.startTransfer(), "Valid collection starts");
        long old = f.pendingGeneration();
        check(!f.startTransfer(), "Transfer busy cannot duplicate submit");
        f.back();
        check(f.items == 1 && f.name.equals("Reading"), "Transfer Back retains draft");
        check(!f.completeTransfer(old, false), "Canceled generation ignores late result");
        f.startTransfer(); long current = f.pendingGeneration();
        check(f.completeTransfer(current, true), "Local failure fixture completes");
        check(f.screen == FlowState.Screen.TRANSFER_ERROR && f.items == 1, "Failure retains selected items");
        check(f.startTransfer(), "Retry starts a fresh generation");
        check(!f.completeTransfer(current, false), "Old generation cannot complete retry");
        check(f.completeTransfer(f.pendingGeneration(), false), "Retry generation completes");
        check(f.screen == FlowState.Screen.TRANSFER_DONE, "Successful local operation reaches done");
        f.open("source-selected"); f.selectCourse(1); f.selectCourse(1);
        check(f.course == 1, "D: course reselection stays selected");
        f.open("transfer-home"); f.toggleItem(0); f.toggleItem(0);
        check(f.items == 0, "D: transfer multiselect can deselect");
        System.out.println("PASS " + checks + " state-contract assertions");
    }
}
