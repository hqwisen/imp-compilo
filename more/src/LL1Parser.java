import sun.reflect.generics.tree.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;

/**
 * REMARK:
 * We sometimes refers to terminals when we should refer
 * to LexicalUnit. Usually when terminals are being used
 * as index keys (for rules or actionTable), they are
 * actually LexicalUnits.
 */
public class LL1Parser {

    private static final Logger log;


    private final static int SYNTAX_ERROR = 0;
    private final static int ACCEPT = -1;
    private final static int MATCH = -2;

    public final static String EPSILON = "epsilon";

    private final static String JAR = "dist/parser.jar";

    private final Scanner scanner;
    private String grammarFile, tableFile;
    private List<String> variables, terminals;
    private Map<Integer, List<String>> rules;
    private Map<String, Map<String, Integer>> actionTable;
    private Stack<String> stack;
    private Stack<TreeNode> treeStack;
    private List<Integer> leftMostDerivation;
    private TreeNode derivationTree, AST;
    // Following vars are used during the parsing
    private Integer tokenIndex;
    private Symbol token;
    private List<Symbol> scannedTokens;
    private boolean parsing;

    static {
        log = ImpCompilo.log;
    }

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
     *
     * @param source      IMP source reader
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
        this.treeStack = new Stack<>();
        this.leftMostDerivation = new ArrayList<>();
        // this.derivationTree = new TreeNode("");
        this.derivationTree = null;
        this.AST = null;
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
     * If the right side is @{@link LL1Parser#EPSILON}, then only
     * the left side is set. This will avoid to push epsilon on the stack
     * when parsing.
     */
    private void buildGrammar() {
        ImpCompilo.log.info("Building grammar from '" + this.grammarFile + "'");
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
                if (line[1].equals(EPSILON)) {
                    // add only left-side, since epsilon is empty word
                    this.rules.get(ruleNumber).add(line[0]);
                } else {
                    for (int i = 0; i < line.length; i++) {
                        this.rules.get(ruleNumber).add(line[i]);
                    }
                }
                ImpCompilo.log.fine(ruleNumber + " → " +
                        this.rules.get(ruleNumber).toString());
                readLine = reader.readLine();
                ruleNumber++;
            }
        } catch (IOException e) {
            ImpCompilo.log.severe("An error occured while reading "
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
        ImpCompilo.log.info("Building action table from '" + this.tableFile + "'");
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
            ImpCompilo.log.severe("An error occured while reading "
                    + this.tableFile);
            e.printStackTrace();
        }

    }

    /**
     * Return the rule from the action table based on the
     * terminals/variables given as parameter.
     * We used the same notation as given in the Lecture Notes.
     * If a and l are both terminals the action table doesn't contained anything.
     * The methods will return:
     * - The ruleNumber to apply if a is a variable and l a terminal
     * - MATCH if a and l are terminal and the same
     * - ACCEPT if a and l are both the terminal LexicalUnit.END
     * - SYNTAX_ERROR for all other cases
     *
     * @return the action to make while parsing
     */

    public Integer M(String a, String l) {
        // TODO instead of those conditions, make a table
        Integer action = null;
        if (isVariable(a)) {
            action = this.actionTable.get(a).get(l);
        }
        // ruleNumber is null if a is terminal or a and l are variables
        if (action == null) {
            if (isTerminal(a) && isTerminal(l) && a.equals(l)) {
                if (a.equals(LexicalUnit.END.getValue())) {
                    action = ACCEPT;
                } else {
                    action = MATCH;
                }
            } else {
                action = SYNTAX_ERROR;
            }
        }
        ImpCompilo.log.info("Action for M(" + a + ", " + l + ") is " + action);
        return action;
    }

    /**
     * Return the list of expected symbols of the
     * current top of {@link LL1Parser#stack}.
     *
     * @return list of expected symbols
     */
    public List<String> expectedSymbols() {
        String top = stack.peek();
        List<String> expected = new ArrayList<>();
        for (String terminal : terminals) {
            if (M(top, terminal) != SYNTAX_ERROR) {
                expected.add(terminal);
            }
        }
        return expected;
    }

    /**
     * Return true if value is terminal, false otherwise.
     * Note that the value is considered terminal, only
     * if it is in {@link LL1Parser#terminals} list.
     * It doesn't check any regexp.
     *
     * @return true if value is a terminal, false otherwise
     */
    public boolean isTerminal(String value) {
        return terminals.contains(value);
    }

    /**
     * Return true if the value is a terminal and it is informative.
     * An terminal is considered informative if it is only used to parse
     * and has no value meaning.
     * E.G. begin, (, ), end, endif, .. are informative terminals.
     * E.G. +, -, [VarName], [Number], .. are NOT informative terminals.
     *
     * @param value a string representing a terminal
     * @return true if value is a terminal that is informative, false otherwise
     */
    public boolean isInformativeTerminal(String value) {
        return LexicalUnit.unitFromValue(value).isInformative();
    }


    /**
     * Return true if value is a variable, false otherwise.
     * Note that the value is considered variable, only
     * if it is in {@link LL1Parser#variables} list.
     *
     * @return true if value is a terminal, false otherwise
     */
    public boolean isVariable(String value) {
        return variables.contains(value);
    }

    public boolean isEpsilon(String value) {
        return value.equals(EPSILON);
    }

    /**
     * Return the start symbol of the grammar, extracted from the rules.
     * start symbol is the left side of first rule
     * It is logical that the grammar as at least one rule
     * If not it will raise a NullPointerException.
     *
     * @return start symbol of the grammar
     * @throws NullPointerException if there is no rules
     */
    public String getStartSymbol() {
        return this.rules.get(1).get(0);
    }

    public List<Symbol> getScannedTokens() {
        return scannedTokens;
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
     *
     * @return the next token to parse, null otherwise.
     */
    private Symbol nextToken() {
        tokenIndex++;
        if (tokenIndex < scannedTokens.size()) {
            token = scannedTokens.get(tokenIndex);
        } else {
            token = null;
        }
        ImpCompilo.log.info("Next token is " + token.getValue());
        return token;
    }

    /**
     * Run the accept action:
     * It actually stops the parsing.
     *
     * @param symbol
     */
    private void accept(Symbol symbol) {
        ImpCompilo.log.info("Accept(" + symbol.getValue() + ")");
        parsing = false;
    }

    /**
     * Run the match action:
     * - pop the terminal from top of the stack
     * - token is set the the next token from the input
     *
     * @param symbol
     */
    private void match(Symbol symbol) {
        ImpCompilo.log.info("Match(" + symbol.getValue() + ")");
        stack.pop();
        TreeNode node = treeStack.pop();
        // Set the value to the real value if the symbol is a VarName or a Number
        // FIXME update symbol to avoid casting
        node.setConcreteValue((String) symbol.getValue());
        nextToken();
    }

    /**
     * Prepare a SyntaxError message by showing:
     * - the unexpected symbol
     * - list of expected symbols
     * Throws a SyntaxError exception
     *
     * @param symbol the unexpected symbol
     * @throws SyntaxError
     */
    private void syntaxError(Symbol symbol) {
        ImpCompilo.log.info("SyntaxError(" + symbol.getValue() + ")");
        String message = "Syntax Error: unexpected symbol '" +
                symbol.getValue() + "' in line " +
                symbol.getLine() + ".";
        message += " Was expecting: ";
        for (String expected : expectedSymbols()) {
            message += "'" + expected + "' ";
        }
        throw new SyntaxError(message);
    }

    /**
     * Push the right-hand side of the rule on top of the stack
     * from left to right. (the left of the right-hand is on top of stack).
     *
     * @param ruleNumber
     */
    private void pushRule(Integer ruleNumber, TreeNode topTree) {
        List<String> elems = rules.get(ruleNumber);
        // Add the children to the topTree (first element is the left child)
        if (elems.size() == 1) { // Only left-hand i.e. VAR -> epsilon
            topTree.addChild(EPSILON);
        }
        for (int i = 1; i < elems.size(); i++) {
            topTree.addChild(elems.get(i));
        }
        List<TreeNode> children = topTree.getChildren();
        for (int i = elems.size() - 1; i > 0; i--) {
            stack.push(elems.get(i));
            // Children are pushed backward
            treeStack.push(children.get(i - 1)); // Children doesnt contains the left-hand side
        }
    }

    /**
     * Run the produce action:
     * - Replace the top of stack by the right-hand side of the rule
     * - add the rule to the left most derivation
     *
     * @param symbol     symbol actually parsing
     * @param ruleNumber rule to produce
     */
    private void produce(Symbol symbol, Integer ruleNumber) {
        ImpCompilo.log.info("Produce(" + symbol.getValue() + ", "
                + ruleNumber.toString() + ")");
        stack.pop();
        pushRule(ruleNumber, treeStack.pop());
        leftMostDerivation.add(ruleNumber);
    }

    /**
     * Main parsing method that initiate the stack with the start
     * symbol at the begining.
     * Will get the list of symbols from the scanner,
     * and start the parsing by reading the action table,
     * and run the corresponding action
     *
     * @return Scanned tokens.
     * @throws SyntaxError           if syntax error detected in the action table
     * @throws UnknownTokenException if the scanner throws it
     */
    public List<Symbol> parse() {
        stack.push(getStartSymbol());
        derivationTree = new TreeNode(getStartSymbol());
        treeStack.push(derivationTree);
        scannedTokens = scanner.scan();
        nextToken();
        parsing = token != null;
        while (parsing) {
            Integer ruleNumber = M(stack.peek(), lexicalUnitOf(token));
            switch (ruleNumber) {
                case ACCEPT:
                    accept(token);
                    break;
                case MATCH:
                    match(token);
                    break;
                case SYNTAX_ERROR:
                    // SyntaxError stops the parsing
                    syntaxError(token);
                    break;
                default:
                    produce(token, ruleNumber);
                    break;
            }
            ImpCompilo.log.info("Stack: " + stack);
        }
        ImpCompilo.log.info("Parsing finished.");
        return scannedTokens;
    }

    public void printLeftMostDerivation() {
        for (Integer rule : leftMostDerivation) {
            System.out.print(rule + " ");
        }
        System.out.println();
    }


    public int countVariables(List<String> list) {
        int count = 0;
        for (String value : list) {
            if (isVariable(value)) {
                count++;
            }
        }
        return count;
    }

    public void printDerivationTree() {
        derivationTree.print();
    }

    private void removeInformativeTerminals(TreeNode tree) {
        for (Iterator<TreeNode> iter = tree.getChildren().listIterator(); iter.hasNext(); ) {
            TreeNode child = iter.next();
            String value = child.getValue();
            if (isVariable(value)) {
                removeInformativeTerminals(child);
            } else if (!isEpsilon(value) && isInformativeTerminal(value)) {
                iter.remove();
            }
            if (value.equals("<IfSeq>") && child.numberOfChildren() == 0) {
                iter.remove();
            }
        }
    }

    /**
     * Remove EpsilonNodes. an EpsilonNode is a node
     * that has only one child, and the value of this child is epsilon.
     * The value of the node doesn't need to be epsilon.
     *
     * @param tree
     */
    private void removeEpsilonNodes(TreeNode tree) {
        for (Iterator<TreeNode> iter = tree.getChildren().listIterator(); iter.hasNext(); ) {
            TreeNode child = iter.next();
            if (child.getChildren().size() == 1 && isEpsilon(child.getChildValue(0))) {
                iter.remove();
            } else {
                removeEpsilonNodes(child);
            }
        }
    }

    /**
     * All child of code are instructions (assign, if etc)
     *
     * @param codeNode
     */
    private TreeNode simplifyCode(TreeNode codeNode) {
        TreeNode newNode = new TreeNode(codeNode.getValue());
        List<TreeNode> children = new ArrayList<>();
        newNode.setChildren(children);
        innerSimplifyCode(codeNode, children);
        simplifyInstructions(newNode);
        return newNode;
    }

    private void innerSimplifyCode(TreeNode node, List<TreeNode> children) {
        for (TreeNode child : node.getChildren()) {
            if ("<Instruction>".equals(child.getValue())) {
                children.add(child.getChild(0));
            } else {
                innerSimplifyCode(child, children);
            }
        }
    }

    private boolean isExprPrime(TreeNode node) {
        // FIXME avoid hardcoding exprprime
        List<String> values = new ArrayList<>();
        values.add("<ExprArithPrime>");
        values.add("<ExprProdPrime>");
        boolean result = values.contains(node.getValue());
        log.info("IsExprPrime " + node.getValue() + ": " + result);
        return result;
    }

    //    private boolean isUselessVariables(TreeNode node) {
//        // FIXME avoid hardcoding exprprime
//        List<String> values = new ArrayList<>();
//        values.add("<InstListSeq>");
//        values.add("<InstList>");
//        boolean result = values.contains(node.getValue());
//        return result;
//    }

    private TreeNode simplifyAtomOfExpr(TreeNode atomNode) {
        TreeNode child = atomNode.getChild(0);
        switch (child.getValue()) {
            case "-":
                // TODO implemented -<Atom>
                throw new ImpCompiloException("Not implemented: -<Atom>");
            case "<ExprArith>":
                return simplifyExprArith(child, true, false);
            default:
                return new TreeNode(child.getConcreteValue());
        }
    }

    private String negComp(String comp) {
        switch (comp) {
            case "<>":
                return "=";
            case "=":
                return "<>";
            case "<":
                return ">=";
            case "<=":
                return ">";
            case ">":
                return "<=";
            case ">=":
                return "<";
            default:
                throw new ImpCompiloException("Unknown comparator " + comp);
        }
    }

    private TreeNode simplifyAtomOfCond(TreeNode atomNode) {
        TreeNode newNode = new TreeNode(atomNode.getValue());
        boolean not = false;
        if (atomNode.numberOfChildren() == 2) { // negation
            atomNode.removeChild(0);
            not = true;
        }
        TreeNode simpleCond = atomNode.getChild(0);
        newNode.addChild(simplifyExprArith(simpleCond.getChild(0),
                true, false));
        String comp = simpleCond.getChild(1).getChildValue(0);
        comp = not ? negComp(comp) : comp;
        newNode.addChild(comp);
        newNode.addChild(simplifyExprArith(simpleCond.getChild(2),
                true, false));
        return newNode;
    }

    private TreeNode simplifyAtom(TreeNode atomNode, boolean isCond) {
        if (isCond) {
            return simplifyAtomOfCond(atomNode);
        } else {
            return simplifyAtomOfExpr(atomNode);

        }
    }

    private TreeNode simplifyExprProdPrime(TreeNode exprProdPrime,
                                           TreeNode leftChild, boolean isCond) {
        TreeNode newNode;
        if (exprProdPrime.numberOfChildren() == 2
                || exprProdPrime.numberOfChildren() == 3) {
            newNode = new TreeNode("");
            if (isCond) {
                newNode.setValue(exprProdPrime.getChild(0).getValue());
            } else {
                newNode.setValue(exprProdPrime.getChild(0).getChild(0).getValue());
            }
            // Atom of Upper (like Prod) <op> Atom of ProdPrime
            newNode.addChild(leftChild);
            newNode.addChild(simplifyAtom(exprProdPrime.getChild(1), isCond));
        } else {
            throw new ImpCompiloException("ExprProdPrime with" +
                    "less that 2 children");
        }
        if (exprProdPrime.numberOfChildren() == 3) { // recursive ProdPrime
            return simplifyExprProdPrime(exprProdPrime.getChild(2), newNode,
                    isCond);
        }
        return newNode;
    }

    private TreeNode simplifyExprProd(TreeNode exprProd, boolean isCond) {
        TreeNode atom = simplifyAtom(exprProd.getChild(0), isCond);
        if (exprProd.numberOfChildren() == 2) { // has ExprProdPrime
            TreeNode exprProdPrime = exprProd.getChild(1);
            return simplifyExprProdPrime(exprProdPrime, atom, isCond);
        } else {
            return atom;
        }
    }

    private TreeNode simplifyExprArith(TreeNode exprArith, boolean isPrime,
                                       boolean isCond) {
        // First.first child is ExprProd.Atom and always exist
        TreeNode newExpr = new TreeNode(exprArith.getValue());
        TreeNode subExpr = newExpr;
        TreeNode exprProd = exprArith.getChild(0);
        if (exprArith.numberOfChildren() == 2) { // has ExprArithPrime
            TreeNode exprArithPrime = exprArith.getChild(1);
            if (isPrime) {
                newExpr = new TreeNode("");
                String op;
                if (isCond) {
                    op = exprArithPrime.getChild(0).getValue();
                } else {
                    op = exprArithPrime.getChild(0).
                            getChild(0).getValue();
                }
                newExpr.setValue(op);
                subExpr = newExpr;
            } else {
                String op;
                if (isCond) {
                    op = exprArithPrime.getChild(0).getValue();
                } else {
                    op = exprArithPrime.getChild(0).
                            getChild(0).getValue();
                }
                newExpr.addChild(op);
                subExpr = newExpr.getChild(0);
            }
            exprArithPrime.removeChild(0); // remove operator
            subExpr.addChild(simplifyExprProd(exprProd, isCond));
            subExpr.addChild(simplifyExprArith(exprArithPrime, true, isCond));
        } else if (exprArith.numberOfChildren() == 1) { // only ExprProd
            if (isPrime) {
                newExpr = simplifyExprProd(exprProd, isCond);
            } else {
                subExpr.addChild(simplifyExprProd(exprProd, isCond));
            }
        }
        return newExpr;
    }

    public void simplifyInstructions(TreeNode codeNode) {
        TreeNode newChild;
        List<TreeNode> newChildren = new ArrayList<>();
        for (TreeNode child : codeNode.getChildren()) {
            switch (child.getValue()) {
                case "<Assign>":
                    child.setChild(1, simplifyExprArith(child.getChild(1),
                            true, false));
                    break;
                case "<Print>":
                case "<Read>":
                    break;
                case "<If>":
                    if (child.numberOfChildren() == 1) { // only cond
                        codeNode.setEmptyChild(child);
                        child.addChild(new TreeNode("<Code>"));
                        child.addChild(new TreeNode("<Code>"));

                    } else if (child.numberOfChildren() == 2) { // cond + code
                        TreeNode cond = child.getChild(0);
                        child.setChild(0, simplifyExprArith(cond, true, true));
                        TreeNode firstCode = child.getChild(1);
                        if ("<IfSeq>".equals(firstCode.getValue())) {
                            child.setChild(1, new TreeNode("<Code>"));
                            child.addChild(
                                    simplifyCode(firstCode.getChild(0)));
                        } else {
                            child.setChild(1, simplifyCode(firstCode));
                            child.addChild(new TreeNode("<Code>"));
                        }
                    } else {
                        TreeNode cond = child.getChild(0);
                        child.setChild(0, simplifyExprArith(cond, true, true));
                        child.setChild(1, simplifyCode(child.getChild(1)));
                        child.setChild(2,
                                simplifyCode(child.getChild(2).getChild(0)));
                    }
                    break;
                case "<While>":
                    if (child.numberOfChildren() == 1) { // only cond
                        codeNode.setEmptyChild(child);
                    } else {
                        TreeNode cond = child.getChild(0);
                        TreeNode code = child.getChild(1);
                        child.setChild(0, simplifyExprArith(cond, true, true));
                        child.setChild(1, simplifyCode(code));
                    }
                    break;
                default:
                    throw new ImpCompiloException("Unkown instruction " +
                            child.getValue() + " when simplifying instructions");
            }
        }
        removeEpsilonNodes(codeNode);
    }

    public void buildAST() {
        removeEpsilonNodes(derivationTree);
        removeInformativeTerminals(derivationTree);
        // FIXME WHAT IF derivation tree is empty ?
        derivationTree = derivationTree.getChild(0); // No need of StartSymbol
        System.err.println("######  BEFORE #######");
        derivationTree.print();
        System.err.println("######### AFTER CODE/INSTR. ##########");
        derivationTree = simplifyCode(derivationTree);
        derivationTree.print();
        AST = derivationTree;

    }

    public TreeNode getAST() {
        if (AST == null) {
            buildAST();
        }
        return AST;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:  java -jar " + JAR + " file.imp" +
                    " [printTree]");
            System.exit(0);
        }
        FileReader source = ImpCompilo.file(args[0]);
        LL1Parser parser = new LL1Parser(source, "grammar.csv",
                "actionTable.csv");
        try {
            parser.parse();
        } catch (ImpCompiloException e) {
            ImpCompilo.error(e);
        }
        if (args.length > 1 && args[1].equals("printTree")) {
            parser.printDerivationTree();
        } else {
            parser.printLeftMostDerivation();
        }
    }
}
