package org.reastyle.nativeexample;

/** Pure state model. A/B rules and D-only choices are mapped in component-contracts.json. */
public final class FlowState {
    public enum Screen { LAUNCHER, PRIVACY, AGE, WELCOME, COURSE, BUILDING, COMPONENTS,
        TRANSFER, TRANSFER_WORKING, TRANSFER_ERROR, TRANSFER_DONE }
    public Screen screen = Screen.LAUNCHER;
    public int course = -1;
    public int items = 0;
    public String name = "My collection";
    public String validation = "";
    public long generation = 0;

    public void open(String scene) {
        generation++;
        course = -1;
        items = 0;
        validation = "";
        name = "My collection";
        if ("source-privacy".equals(scene)) screen = Screen.PRIVACY;
        else if ("source-age".equals(scene)) screen = Screen.AGE;
        else if ("source-welcome".equals(scene)) screen = Screen.WELCOME;
        else if ("source-course".equals(scene)) screen = Screen.COURSE;
        else if ("source-selected".equals(scene)) { screen = Screen.COURSE; course = 0; }
        else if ("source-building".equals(scene)) { screen = Screen.BUILDING; course = 0; }
        else if ("components".equals(scene)) screen = Screen.COMPONENTS;
        else if ("transfer-selected".equals(scene)) { screen = Screen.TRANSFER; items = 3; }
        else if ("transfer-working".equals(scene)) { screen = Screen.TRANSFER_WORKING; items = 3; }
        else if ("transfer-error".equals(scene)) { screen = Screen.TRANSFER_ERROR; items = 3; }
        else if ("transfer-done".equals(scene)) { screen = Screen.TRANSFER_DONE; items = 3; }
        else if ("transfer-empty".equals(scene)) { screen = Screen.TRANSFER; name = ""; }
        else if ("transfer-home".equals(scene)) screen = Screen.TRANSFER;
        else screen = Screen.LAUNCHER;
    }

    public boolean isTransfer() { return screen.ordinal() >= Screen.TRANSFER.ordinal(); }
    public boolean canContinueCourse() { return screen == Screen.COURSE && course >= 0; }
    public void selectCourse(int index) { if (screen == Screen.COURSE && index >= 0 && index < 5) course = index; }
    public boolean startCourse() {
        if (!canContinueCourse()) return false;
        screen = Screen.BUILDING;
        generation++;
        return true;
    }
    public void toggleItem(int bit) {
        if (screen == Screen.TRANSFER && bit >= 0 && bit < 3) items ^= 1 << bit;
    }
    public int itemCount() { return Integer.bitCount(items); }
    public boolean startTransfer() {
        if (screen != Screen.TRANSFER && screen != Screen.TRANSFER_ERROR) return false;
        if (items == 0) return false;
        if (name.trim().isEmpty()) { validation = "Give your collection a name."; return false; }
        validation = "";
        screen = Screen.TRANSFER_WORKING;
        generation++;
        return true;
    }
    public long pendingGeneration() { return generation; }
    public boolean completeTransfer(long token, boolean fail) {
        if (screen != Screen.TRANSFER_WORKING || token != generation) return false;
        screen = fail ? Screen.TRANSFER_ERROR : Screen.TRANSFER_DONE;
        return true;
    }
    /** false means the Activity can finish; privacy consumes Back without dismissing. */
    public boolean back() {
        generation++;
        if (screen == Screen.PRIVACY || screen == Screen.AGE) return true;
        if (screen == Screen.BUILDING) { screen = Screen.COURSE; course = -1; return true; }
        if (screen == Screen.COURSE) { screen = Screen.WELCOME; course = -1; return true; }
        if (screen == Screen.WELCOME) { screen = Screen.LAUNCHER; return true; }
        if (screen == Screen.TRANSFER_WORKING || screen == Screen.TRANSFER_ERROR || screen == Screen.TRANSFER_DONE) {
            screen = Screen.TRANSFER; return true; // D: retain the user's collection draft.
        }
        if (screen == Screen.TRANSFER || screen == Screen.COMPONENTS) { screen = Screen.LAUNCHER; return true; }
        return false;
    }
}
