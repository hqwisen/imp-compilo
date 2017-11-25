import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * NOTE We sometimes refers to terminals when we should refer
 * to LexicalUnit. Usually when terminals are being used
 * as index keys (for rules or actionTable), they are
 * actually LexicalUnits.
 */
public class LL1Parser {

    private final static int SYNTAX_ERROR = 0;
    private final static int ACCEPT = -1;
    private final static int MATCH = -2;

    private final static String EPISLON = "epsilon";

    private final static String JAR = "dist/parser.jar";

    private final Scanner scanner;
    private String grammarFile, tableFile;
    private List<String> variables, terminals;
    private Map<Integer, List<String>> rules;
    private Map<String, Map<String, Integer>> actionTable;
    private Stack<String> stack;
    private List<Integer> leftMostDerivation;
    // Following vars are used during the parsing
    private Integer tokenIndex;
    private Symbol token;
    private List<Symbol> scannedTokens;
    private boolean parsing;

    /**
     * Set up a scanner. grammarFile and tableFile should be csv file
     * available in the resources directory.
     * The lines of the grammarFile should be:
     * - All the variables of the grammar
     * - All the terminals of the grammar
     * - One line per rule (in order)
     * The tableFile should only contain the variables as row key,
     * the bottom of the table (with the match and accepted) will be
     * implicitly implemented.
     * We suppose that the grammarFile contains per rule, at least
     * the left side and one element on the right side (even epsilon).
     * @param sourceFile  IMP source file
     * @param grammarFile csv file containing the grammar
     * @param tableFile   csv file containing the action file
     */
    public LL1Parser(FileReader source, String grammarFile, String tableFile) {
        this.scanner = new GeneratedScanner(source);
        this.grammarFile = grammarFile;
        this.tableFile = tableFile;
        this.rules = new HashMap<>();
        this.actionTable = new HashMap<>();
        this.stack = new Stack<>();
        this.leftMostDerivation = new ArrayList<>();
        buildGrammar();
        buildActionTable();
        // Parsing state variables
        this.tokenIndex = -1;
        this.token = null;
        this.scannedTokens = null;
        this.parsing = false;
    }

    /**
     * Return a BufferedReader to read a (csv) file
     *
     * @param resource name of the ressource to read
     * @return the reader of the resource
     */
    public BufferedReader getResourceReader(String resource) {
        return new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream(resource)));
    }

    /**
     * Set up the grammar variables, terminals and rules from the grammarFile.
     * variables and terminals are only lists with the respective elements
     * of the grammar. The rules map contains the left side and right side,
     * with the rule number as the key.
     * Since it is a Context Free Grammar, the first element of the list of
     * a rule is the left side, and the following elements is the right side.
     * If the right side if @{@link LL1Parser#EPISLON}, then only
     * the left side is set. This will avoid to push epsilon on the stack
     * when parsing.
     */
    private void buildGrammar() {
        Scanner.log.info("Building grammar from '" + this.grammarFile + "'");
        BufferedReader reader = getResourceReader(this.grammarFile);
        try {
            this.variables = Arrays.asList(reader.readLine().split(","));
            this.terminals = Arrays.asList(reader.readLine().split(","));
            String[] line = null;
            String readLine = reader.readLine();
            Integer ruleNumber = 1; // First rule is 1; 0 is syntax error
            while (readLine != null) {
                line = readLine.split(",");
                // First index of line is the left side
                // the following values are the tokens of the right side
                this.rules.put(ruleNumber, new ArrayList<String>());
                if(line[1].equals(EPISLON)){
                    // add only left-side, since epsilon is empty word
                    this.rules.get(ruleNumber).add(line[0]);
                }else{
                    for (int i = 0; i < line.length; i++) {
                        this.rules.get(ruleNumber).add(line[i]);
                    }
                }
                Scanner.log.fine(ruleNumber + " → " +
                        this.rules.get(ruleNumber).toString());
                readLine = reader.readLine();
                ruleNumber++;
            }
        } catch (IOException e) {
            Scanner.log.severe("An error occured while reading "
                    + this.grammarFile);
            e.printStackTrace();
        }
    }

    /**
     * Initial the action table with the variables as the
     * keys of the first dimension, and the terminals
     * as the keys of the second dimension of the maps.
     * Initialization should be run after the grammar
     * as been build. The initial values of the
     * action table are 0. Note that the value 0 (SYNTAX_ERROR)
     * will be used as a detection of a syntax error.
     */
    private void initActionTable() {
        for (String variable : this.variables) {
            this.actionTable.put(variable, new HashMap<>());
            for (String terminal : this.terminals) {
                this.actionTable.get(variable).put(terminal, SYNTAX_ERROR);
            }
        }
    }

    /**
     * Set up the action table from the tableFile.
     * The action table is accessed by a variable as
     * the first key, and a terminal as the second key.
     * It returns the rule to be processed by the LL1Parser.
     * If there is no rule to process, i.e. a syntax error,
     * the value is set to 0 (SYNTAX_ERROR).
     */
    private void buildActionTable() {
        initActionTable();
        Scanner.log.info("Building action table from '" + this.tableFile + "'");
        BufferedReader reader = getResourceReader(this.tableFile);
        try {
            String[] line = null;
            // First row is the columns i.e. the terminals
            // The are needed to be able to index the values correctly
            String[] terminals = reader.readLine().split(",");
            String readLine = reader.readLine();
            while (readLine != null) {
                line = readLine.split(",");
                // First element is the variable
                // The other value are the rule number
                // If there is no rule number it is not necessary
                // to add it to actionTable
                String variable = line[0];
                for (int i = 1; i < line.length; i++) {
                    if (line[i].length() > 0) {
                        Integer value = Integer.parseInt(line[i]);
                        this.actionTable.get(variable).put(terminals[i], value);
                    }
                }
                readLine = reader.readLine();
            }
        } catch (IOException e) {
            Scanner.log.severe("An error occured while reading "
                    + this.tableFile);
            e.printStackTrace();
        }

    }

    /**
     * Return the rule from the action table based on the
     * terminals/variables given as parameter.
     * We used the same notation as given in the Lecture Notes.
     * If a & l are both terminals the action table doesn't contained anything.
     * The methods will return:
     * - The ruleNumber to apply if a is a variable and l a terminal
     * - MATCH if a & l are terminal and the same
     * - ACCEPT if a & l are both the terminal LexicalUnit.END
     * - SYNTAX_ERROR for all other cases
     * @return the action to make while parsing
     */

    public Integer M(String a, String l) {
        // TODO instead of those conditions, make a table
        Integer action = null;
        if(isVariable(a)){
            action = this.actionTable.get(a).get(l);
        }
        // ruleNumber is null if a is terminal or a and l are variables
        if (action == null) {
            if (isTerminal(a) && isTerminal(l) && a.equals(l)) {
                if(a.equals(LexicalUnit.END.getValue())){
                    action = ACCEPT;
                }else{
                    action = MATCH;
                }
            }else{
                action = SYNTAX_ERROR;
            }
        }
        Scanner.log.info("Action for M(" + a + ", " + l + ") is " + action);
        return action;
    }

    /**
     * Return true if value is terminal, false otherwise.
     * Note that the value is considered terminal, only
     * if it is in {@link LL1Parser#terminals} list.
     * It doesn't check any regexp.
     * @return true if value is a terminal, false otherwise
     */
    public boolean isTerminal(String value) {
        return terminals.contains(value);
    }

    /**
     * Return true if value is a variable, false otherwise.
     * Note that the value is considered variable, only
     * if it is in {@link LL1Parser#variables} list.
     * @return true if value is a terminal, false otherwise
     */
    public boolean isVariable(String value) {
        return variables.contains(value);
    }

    /**
     * Return the start symbol of the grammar, extracted from the rules.
     * start symbol is the left side of first rule
     * It is logical that the grammar as at least one rule
     * If not it will raise a NullPointerException.
     * @throws NullPointerException if there is no rules
     * @return start symbol of the grammar
     */
    public String getStartSymbol() {
        return this.rules.get(1).get(0);
    }

    /**
     * Return the symbol value as a String.
     * This method is implemented to avoid changing the Symbol class.
     *
     * @param symbol the symbol with the value
     * @return the String value of the symbol
     */
    public String lexicalUnitOf(Symbol symbol) {
        return symbol.getType().getValue();
    }

    /**
     * Set the next token to parse in {@link LL1Parser#token}.
     * It also returns it.
     * If no more token available, null is set.
     * @return the next token to parse, null otherwise.
     */
    private Symbol nextToken(){
        tokenIndex++;
        if(tokenIndex < scannedTokens.size()){
            token = scannedTokens.get(tokenIndex);
        }else{
            token = null;
        }
        Scanner.log.info("Next token is " + token.getValue());
        return token;
    }

    private void accept(Symbol symbol){
        Scanner.log.info("Accept(" + symbol.getValue() + ")");
        parsing = false;
    }

    private void match(Symbol symbol){
        Scanner.log.info("Match(" + symbol.getValue() + ")");
        stack.pop();
        nextToken();
    }

    private void syntaxError(Symbol symbol){
        Scanner.log.info("SyntaxError(" + symbol.getValue() + ")");
        parsing = false;
    }

    private void pushRule(Integer ruleNumber){
        List<String> elems = rules.get(ruleNumber);
        for(int i = elems.size() - 1; i > 0; i--){
            stack.push(elems.get(i));
        }
    }

    private void produce(Symbol symbol, Integer ruleNumber){
        Scanner.log.info("Produce(" + symbol.getValue() + ", "
                         + ruleNumber.toString() + ")");
        stack.pop();
        pushRule(ruleNumber);
        leftMostDerivation.add(ruleNumber);
    }

    public void printLeftMostDerivation(){
        for(Integer rule : leftMostDerivation){
            System.out.print(rule + " ");
        }
        System.out.println();
    }

    public void parse() {
        stack.push(getStartSymbol());
        scannedTokens = scanner.scan();
        nextToken();
        parsing = token != null;
        while(parsing) {
            Integer ruleNumber = M(stack.peek(), lexicalUnitOf(token));
            switch(ruleNumber){
                case ACCEPT: accept(token);
                    break;
                case MATCH: match(token);
                    break;
                case SYNTAX_ERROR: syntaxError(token);
                    break;
                default: produce(token, ruleNumber);
                    break;
            }
            Scanner.log.info("Stack: " + stack);
        }
        Scanner.log.info("Parsing finished.");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage:  java -jar " + JAR + " file.imp\n");
            System.exit(0);
        }
        FileReader source = Scanner.file(args[0]);
        if (source == null) {
            System.out.println("File '" + args[0] + "' was not found");
        } else {
            LL1Parser parser = new LL1Parser(source, "grammar.csv",
                    "actionTable.csv");
            parser.parse();
            parser.printLeftMostDerivation();
        }
    }

}
