import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Scanner {

    private final static String JAR = "dist/scanner.jar";

    private HashMap<String, Integer> identifiers;
    private List<Symbol> symbols;

    public Scanner() {
        this.identifiers = new HashMap<>();
        this.symbols = new ArrayList<>();
    }

    /**
     * Create a new symbol based on the current state (text, line, column).
     * The symbols is added to {@link Scanner#symbols} list.
     * If the lexical unit is {@link LexicalUnit#VARNAME},
     * and the VarName is encountered for the first time, the value
     * and the line of the symbol are added to {@link Scanner#identifiers}
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
        return symbol;
    }

    /**
     * Check if an identifier had already been encountered during the scan.
     *
     * @param identifier key (identifier) to test
     * @return true if identifier is in {@link Scanner#identifiers},
     * false otherwise.
     */
    public boolean idAlreadyScan(String identifier) {
        return identifiers.containsKey(identifier);
    }

    /**
     * Add key: identifier, value: idLine to {@link Scanner#identifiers}.
     * idLine is incremented before adding the {@link Scanner#identifiers}.
     *
     * @param identifier identifier to add
     * @param idLine     line where the identifier was encountered.
     */
    public void addIdentifier(String identifier, int idLine) {
        if (!idAlreadyScan(identifier)) {
            ImpCompilo.log.fine("Adding identifier: " + identifier + " line " + idLine);
            identifiers.put(identifier, idLine + 1);
        } else {
            ImpCompilo.log.fine("The identifier " + identifier +
                    " has already been added.");
        }
    }

    /**
     * Process post-analyze procedure:
     * Outputs:
     * - Prints all scanned symbols (token) (using {@link symbol#toString} to stdout).
     * - a new line
     * - 'Identifiers' line to separate from the tokens
     * - and a line per identifier with the corresponding
     * first-encountering line.
     */
    public void postScan() {
        for (Symbol symbol : symbols) {
            System.out.println(symbol.toString());
        }
        System.out.println();
        System.out.println("Identifiers");
        for (String key : identifiersKeysSorted()) {
            System.out.println(key + " " + identifiers.get(key));
        }
    }

    /**
     * Sort the identifiers alphabetically, and return an array
     * of String identifiers.
     *
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
     * Run the lexer and return the next token
     *
     * @return The list of symbols/tokens scanned.
     */
    public List<Symbol> scan() {
        try {
            while (!atEOF()) lex();
        } catch (IOException e) {
            String message = "An error occured while running the scanner.";
            ImpCompilo.log.severe(message);
            e.printStackTrace();
        }
        return symbols;
    }

    /**
     * Return the list of scanned symbols.
     *
     * @return {@link Scanner#symbols} list.
     */
    public List<Symbol> getSymbols() {
        return this.symbols;
    }

    /**
     * Return the current match ERE value.
     *
     * @return the current match text string.
     */
    public abstract String text();

    /**
     * Return the current scanned line, starting at 0.
     *
     * @return the line number
     */
    public abstract int line();

    /**
     * Return the current scanned column, starting at 0.
     *
     * @return the column number
     */
    public abstract int column();

    /**
     * Return the length of the current {@link Scanner#text}.
     *
     * @return {@link Scanner#text} size.
     */
    public abstract int length();

    /**
     * Change the current state of the lexer to state.
     *
     * @param state new state
     */
    public abstract void changeState(int state);

    /**
     * Return true if the scanner is at EOF
     *
     * @return true if at EOF, false otherwise
     */
    public abstract boolean atEOF();

    /**
     * Resumes scanning until the next regular expression is matched,
     * the end of input is encountered or an I/O-Error occurs.
     * This based should use the yylex method of the generated scanner by JFlex.
     *
     * @return the next token
     * @throws IOException if there is an error while reading next token.
     */
    public abstract Symbol lex() throws IOException;

    public static void main(String[] args) {
        // Display the usage when the number of arguments is wrong (should be 1)
        if (args.length != 1) {
            System.out.println("Usage:  java -jar " + JAR + " file.imp\n");
            System.exit(0);
        }
        FileReader source = ImpCompilo.file(args[0]);
        // The lexer generated by JFlex
        final Scanner scanner = new GeneratedScanner(source);
        try{
            scanner.scan();
            scanner.postScan();
        }catch(ImpCompiloException e){
            ImpCompilo.error(e);
        }
    }
}
