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
    private Map<String, List<String>> rules;

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
        buildGrammar();
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
     */
    public void buildGrammar(){
        Scanner.log.info("Building grammar from '" + this.grammarFile + "'");
        BufferedReader reader = getResourceReader(this.grammarFile);
        try {
            this.variables = Arrays.asList(reader.readLine().split(","));
            this.terminals = Arrays.asList(reader.readLine().split(","));
            String[] line = null;
            String readLine = reader.readLine();
            while (readLine != null) {
                line = readLine.split(",");
                this.rules.put(line[0], new ArrayList<String>());
                readLine = reader.readLine();
                for(int i = 1; i < line.length; i++){
                    this.rules.get(line[0]).add(line[i]);
                }
                Scanner.log.fine(line[0] + " → " + this.rules.get(line[0]).toString());
            }
        } catch (IOException e) {
            Scanner.log.severe("An error occured while reading " + this.grammarFile);
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
