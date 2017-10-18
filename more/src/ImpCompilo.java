import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class ImpCompilo {

    private static Logger log = Logger.getLogger("ImpCompilo");
    private HashMap<String, Integer> identifiers = new HashMap<>();

    static{
        log.setLevel(Level.OFF);
    }

    public ImpCompilo() {
    }

    public Symbol symbol(LexicalUnit lexicalUnit) {
        String value = text();
        Symbol symbolObject = new Symbol(lexicalUnit, line(), column(), value);
        if (lexicalUnit == LexicalUnit.VARNAME) {
            addIdentifier(value, line());
        }
        // The println method uses the toString automatically
        System.out.println(symbolObject);
        return symbolObject;
    }

    public boolean idAlreadyScan(String value) {
        return identifiers.containsKey(value);
    }

    public void addIdentifier(String identifier, int idLine) {
        if(!idAlreadyScan(identifier)) {
            log.info("Adding identifier: " + identifier + " line " + idLine);
            identifiers.put(identifier, idLine);
        }else{
            log.fine("The identifier " + identifier + " has already been added.");
        }
    }

    public void pushbackWord() {
        pushback(length());
    }

    public void postScan() {
        System.out.println();
        System.out.println("Identifiers");
        for (Entry<String, Integer> entry : identifiers.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public abstract String text();

    public abstract int line();

    public abstract int column();

    public abstract int length();

    public abstract void pushback(int number);

    public abstract void changeState(int state);
}
