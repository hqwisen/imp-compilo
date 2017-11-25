import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LL1Parser {

    private final static String JAR = "dist/parser.jar";

    private String grammarFile, tableFile;
    private FileReader source;
    private Scanner scanner;
    private List<String> variables, terminals;
    private Map<Integer, List<String>> rules;
    private Map<String, Map<String, Integer>> actionTable;

    /**
     * Set up a scanner. grammarFile and tableFile should be csv file
     * available in the resources directory.
     * The lines of the grammarFile should be:
     *  - All the variables of the grammar
     *  - All the terminals of the grammar
     *  - One line per rule (in order)
     *  The tableFile should only contain the variables as row key,
     *  the bottom of the table (with the match and accepted) will be
     *  implicitly implemented.
     * @param sourceFile IMP source file
     * @param grammarFile csv file containing the grammar
     * @param tableFile csv file containing the action file
     */
    public LL1Parser(FileReader sourceFile, String grammarFile, String tableFile){
        this.source = source;
        this.grammarFile = grammarFile;
        this.tableFile = tableFile;
        this.scanner = new GeneratedScanner(this.source);
        this.rules = new HashMap<>();
        this.actionTable = new HashMap<>();
        buildGrammar();
        buildActionTable();
        System.out.println(this.actionTable);
        System.out.println();
        System.out.println(this.rules);
    }

    /**
     * Return a BufferedReader to read a (csv) file
     * @param resource name of the ressource to read
     * @return the reader of the resource
     */
    public  BufferedReader getResourceReader(String resource){
        return new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream(resource)));
    }

    /**
     * Set up the grammar variables, terminals and rules from the grammarFile.
     * variables and terminals are only lists with the respective elements of the grammar.
     * The rules map contains the left side and right side, with the rule number as the key.
     * Since it is a Context Free Grammar, the first element of the list of a rule
     * is the left side, and the following elements is the right side.
     */
    private void buildGrammar(){
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
                for(int i = 0; i < line.length; i++){
                    this.rules.get(ruleNumber).add(line[i]);
                }
                Scanner.log.fine(ruleNumber + " → " + this.rules.get(ruleNumber).toString());
                readLine = reader.readLine();
                ruleNumber++;
            }
        } catch (IOException e) {
            Scanner.log.severe("An error occured while reading " + this.grammarFile);
            e.printStackTrace();
        }
    }

    /**
     * Initial the action table with the variables as the
     * keys of the first dimension, and the terminals
     * as the keys of the second dimension of the maps.
     * Initialization should be run after the grammar
     * as been build. The initial values of the
     * action table are 0. Note that the value 0
     * will be used as a detection of a syntax error.
     */
    private void initActionTable(){
        for(String variable: this.variables){
            this.actionTable.put(variable, new HashMap<>());
            for(String terminal : this.terminals){
                this.actionTable.get(variable).put(terminal, 0);
            }
        }
    }

    /**
     * Set up the action table from the tableFile.
     * The action table is accessed by a variable as
     * the first key, and a terminal as the second key.
     * It returns the rule to be processed by the LL1Parser.
     * If there is no rule to process, i.e. a syntax error,
     * the value is set to 0.
     */
    private void buildActionTable(){
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
                // If there is no rule number it is not necessary to add it to actionTable
                String variable = line[0];
                for(int i = 1; i < line.length; i++){
                    if(line[i].length() > 0) {
                        Integer value = Integer.parseInt(line[i]);
                        this.actionTable.get(variable).put(terminals[i], value);
                    }
                }
                readLine = reader.readLine();
            }
        } catch (IOException e) {
            Scanner.log.severe("An error occured while reading " + this.tableFile);
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage:  java -jar " + JAR + " file.imp\n");
            System.exit(0);
        }
        FileReader source = Scanner.file(args[0]);
        if(source == null){
            System.out.println("File '" + args[0] + "' was not found");
        }else {
            LL1Parser parser = new LL1Parser(source,"grammar.csv", "actionTable.csv");
        }
    }

}
