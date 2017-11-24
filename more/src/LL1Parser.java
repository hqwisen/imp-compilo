import java.io.FileNotFoundException;
import java.io.FileReader;

public class LL1Parser {

    private final static String JAR = "dist/parser.jar";

    private String grammarFile, tableFile;

    public LL1Parser(String grammarFile, String tableFile){
        this.grammarFile = grammarFile;
        this.tableFile = tableFile;
    }

    public static void main(String[] args) {
        System.out.println("Running parser");
        if (args.length != 1) {
            System.out.println("Usage:  java -jar " + JAR + " file.imp\n");
            System.exit(0);
        }
        FileReader source = Scanner.file(args[0]);
        if(source == null){
            System.out.println("File '" + args[0] + "' was not found");
        }else {
            Scanner scanner = new GeneratedScanner(source);
            scanner.scan();
            scanner.postScan();
        }
    }

}
