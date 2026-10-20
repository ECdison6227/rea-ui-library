package study.qishui.reference;
public final class ConsentFlowSelfTest {
    private static int checks;
    private static void expect(ConsentFlow.State expected,ConsentFlow.State actual){checks++;if(expected!=actual)throw new AssertionError("expected "+expected+" got "+actual);}
    public static void main(String[] args){
        ConsentFlow first=new ConsentFlow(false);expect(ConsentFlow.State.GUIDE,first.state());expect(ConsentFlow.State.GUIDE,first.back());
        expect(ConsentFlow.State.REMINDER,first.decline());expect(ConsentFlow.State.REMINDER,first.back());expect(ConsentFlow.State.EXIT,first.decline());
        expect(ConsentFlow.State.GUIDE,new ConsentFlow(false).state());
        ConsentFlow accepted=new ConsentFlow(false);expect(ConsentFlow.State.ACCEPTED,accepted.accept());expect(ConsentFlow.State.ACCEPTED,new ConsentFlow(true).state());
        ConsentFlow reminder=new ConsentFlow(false);reminder.showReminder();expect(ConsentFlow.State.ACCEPTED,reminder.accept());
        System.out.println("PASS: "+checks+" reference consent contract assertions. This does not verify source app internals.");
    }
}
