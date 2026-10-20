package study.qishui.reference;

/** D reference state machine. A inputs: 04/05/06/07/08; no source code claim. */
public final class ConsentFlow {
    public enum State { GUIDE, REMINDER, ACCEPTED, EXIT }
    private State state;
    public ConsentFlow(boolean accepted) { state = accepted ? State.ACCEPTED : State.GUIDE; }
    public State state() { return state; }
    public boolean modal() { return state == State.GUIDE || state == State.REMINDER; }
    public void showReminder() { state = State.REMINDER; }
    public void showGuide() { state = State.GUIDE; }
    public State decline() { if (state == State.GUIDE) state = State.REMINDER; else if (state == State.REMINDER) state = State.EXIT; return state; }
    public State accept() { if (modal()) state = State.ACCEPTED; return state; }
    public State back() { return state; } // Observed Back retains the source first-use modal.
}
