import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class AbstractScanner {

    private static Logger log;
    private HashMap<String, Integer> identifiers;
    private List<Symbol> symbols;

    static {
        log = Logger.getLogger("ImpCompilo");
        log.setLevel(Level.OFF);
    }

    public AbstractScanner() {
        this.identifiers = new HashMap<>();
        this.symbols = new ArrayList<>();
    }

    /**
     * Create a new symbol based on the current state (text, line, column).
     * The symbol is printed (using {@link symbol#toString} to stdout).
     * The symbols is added to {@link ImpCompilo#symbols} list.
     * If the lexical unit is {@link LexicalUnit#VARNAME},
     * and the VarName is encountered for the first time, the value
     * and the line of the symbol are added to {@link ImpCompilo#identifiers}
     *
     * @param lexicalUnit lexical unit associated to the symbol to create
     * @return the created symbol object
     */
    public Symbol symbol(LexicalUnit lexicalUnit) {
        String value = text();
        Symbol symbol = new Symbol(lexicalUnit, line(), column(), value);
        if (lexicalUnit == LexicalUnit.VARNAME) {
            addIdentifier(value, line());
        }
        symbols.add(symbol);
        System.out.println(symbol.toString());
        return symbol;
    }

    /**
     * Check if an identifier had already been encountered during the scan.
     * @param identifier key (identifier) to test
     * @return true if identifier is in {@link ImpCompilo#identifiers},
     * false otherwise.
     */
    public boolean idAlreadyScan(String identifier) {
        return identifiers.containsKey(identifier);
    }

    /**
     * Add key: identifier, value: idLine to {@link ImpCompilo#identifiers}.
     * idLine is incremented before adding the {@link ImpCompilo#identifiers}.
     * @param identifier
     * @param idLine     line where the identifier was encountered.
     */
    public void addIdentifier(String identifier, int idLine) {
        if (!idAlreadyScan(identifier)) {
            log.info("Adding identifier: " + identifier + " line " + idLine);
            identifiers.put(identifier, idLine + 1);
        } else {
            log.fine("The identifier " + identifier +
            " has already been added.");
        }
    }

    /**
     * Process post-analyze procedure:
     * Outputs:
     * - a new line
     * - 'Identifiers' line to separate from the tokens
     * - and a line per identifier with the corresponding
     *  first-encountering line.
     */
    public void postScan() {
        System.out.println();
        System.out.println("Identifiers");
        for (String key : identifiersKeysSorted()) {
            System.out.println(key + " " + identifiers.get(key));
        }
    }

    /**
     * Sort the identifiers alphabetically, and return an array
     * of String identifiers.
     * @return an array of String of encountered identifiers,
     * sorted alphabetically.
     */
    public String[] identifiersKeysSorted() {
        String[] keys = identifiers.keySet()
                .toArray(new String[identifiers.size()]);
        Arrays.sort(keys);
        return keys;
    }

    /**
     * Return the list of scanned symbols.
     * @return {@link ImpCompilo#symbols} list.
     */
    public List<Symbol> getSymbols() {
        return this.symbols;
    }

    /**
     * Return the current match ERE value.
     * @return the current match text string.
     */
    public abstract String text();

    /**
     * Return the current scanned line, starting at 0.
     * @return the line number
     */
    public abstract int line();

    /**
     * Return the current scanned column, starting at 0.
     * @return the column number
     */
    public abstract int column();

    /**
     * Return the length of the current {@link ImpCompilo#text}.
     * @return {@link ImpCompilo#text} size.
     */
    public abstract int length();

    /**
     * Change the current state of the lexer to state.
     * @param state new state
     */
    public abstract void changeState(int state);


    public static void main(String[] args){
        System.out.println("Hello I'm a compilator:");
        log.fine("Can you log this please ? thank you!");
    }
}
