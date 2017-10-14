import java.util.Stack;
import java.util.logging.Logger;


public abstract class ImpCompilo {

    public class CodeState {

        private int parentState;

        public CodeState(int parentState) {
            this.parentState = parentState;
        }

        public int getParentState() {
            return this.parentState;
        }
    }

    private static Logger log = Logger.getLogger("ImpCompilo");
    private Stack<CodeState> codeStack;

    public ImpCompilo() {
        this.codeStack = new Stack<>();
    }

    public Symbol symbol(LexicalUnit lexicalUnit) {
        String value = text();
        Symbol symbolObject = new Symbol(lexicalUnit, line(), column(), value);
       /*if(lexicalUnit == LexicalUnit.VARNAME && !identifiers.containsKey(value)) {
           identifiers.add(value, yyline);
           // log something
       }*/
        System.out.println(symbolObject);
        return symbolObject;
    }



    public void endCodeState() {
        CodeState newState = codeStack.pop();
        // Pushback to <end> token
        pushback(length());
        changeState(newState.getParentState());
    }

    public void pushbackWord(){
        pushback(length());
    }

    public void pushCodeState(int parentState){
        codeStack.push(new CodeState(parentState));
    }

    public abstract String text();

    public abstract int line();

    public abstract int column();

    public abstract int length();

    public abstract void pushback(int number);

    public abstract void changeState(int state);
}
