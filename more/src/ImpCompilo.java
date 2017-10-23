import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class ImpCompilo {

    private static Logger log;
    private HashMap<String, Integer> identifiers;
    private List<Symbol> symbols;

    static{
        log = Logger.getLogger("ImpCompilo");
        log.setLevel(Level.OFF);
    }

    public ImpCompilo() {
        this.identifiers = new HashMap<>();
        this.symbols = new ArrayList<>();
    }

    public Symbol symbol(LexicalUnit lexicalUnit) {
        String value = text();
        Symbol symbol = new Symbol(lexicalUnit, line(), column(), value);
        if (lexicalUnit == LexicalUnit.VARNAME) {
            addIdentifier(value, line());
        }
        symbols.add(symbol);
        // The println method uses the toString automatically
        System.out.println(symbol);
        return symbol;
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
        for(String key: identifiersKeysSorted()){
                System.out.println(key + " " + identifiers.get(key));
        }
    }

    public String[] identifiersKeysSorted(){
        String[] keys = identifiers.keySet()
                        .toArray(new String[identifiers.size()]);
        Arrays.sort(keys);
        return keys;
    }
    
    public List<Symbol> getSymbols(){
        return this.symbols;
    }
    public abstract String text();

    public abstract int line();

    public abstract int column();

    public abstract int length();

    public abstract void pushback(int number);

    public abstract void changeState(int state);
}
