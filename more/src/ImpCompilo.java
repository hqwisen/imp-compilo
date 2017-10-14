import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Stack;


public abstract class ImpCompilo{

    public class CodeState{

        private int parentState;

        public CodeState(int parentState){
            this.parentState = parentState;
        }

        public int getParentState(){
            return this.parentState;
        }
    }

    private Logger log = Logger.getLogger("ImpCompilo");

    private Stack<CodeState> codeStack;

    public ImpCompilo(){
        this.codeStack = new Stack<>();
    }

    public Symbol symbol(LexicalUnit lexicalUnit){
       String value = text();
       Symbol symbolObject = new Symbol(lexicalUnit, line(), column(), value);
       /*if(lexicalUnit == LexicalUnit.VARNAME && !identifiers.containsKey(value)) {
           identifiers.add(value, yyline);
           // log something
       }*/
       System.out.println(symbolObject);
       return symbolObject;
   }

   public void startState(int currentState, int newState){
       codeStack.push(new CodeState(currentState));
       changeState(newState);
   }

   public void endState(){
       CodeState newState = codeStack.pop();
       pushback(length());
       changeState(newState.getParentState());
   }

    public abstract String text();
    public abstract int line();
    public abstract int column();
    public abstract int length();
    public abstract void pushback(int number);
    public abstract void changeState(int state);
}
