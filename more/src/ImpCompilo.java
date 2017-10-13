import java.util.logging.Logger;
import java.util.logging.Level;


public abstract class ImpCompilo{

    private Logger log = Logger.getLogger("ImpCompilo");

    private int lastCodeState;

    /*public ImpCompilo(){

    }*/

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
       lastCodeState = currentState;
       changeState(newState);
   }

   public void endState(){
       pushback(length());
       changeState(lastCodeState);
   }

    public abstract String text();
    public abstract int line();
    public abstract int column();
    public abstract int length();
    public abstract void pushback(int number);
    public abstract void changeState(int state);
}
