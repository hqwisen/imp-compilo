import java.util.Stack;
import java.util.logging.Logger;


public abstract class ImpCompilo {

    private static Logger log = Logger.getLogger("ImpCompilo");

    public ImpCompilo() {
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


    public void pushbackWord() {
        pushback(length());
    }


    public abstract String text();

    public abstract int line();

    public abstract int column();

    public abstract int length();

    public abstract void pushback(int number);

    public abstract void changeState(int state);
}
