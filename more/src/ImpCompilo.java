//import org.bytedeco.javacpp.BytePointer;
//import org.bytedeco.javacpp.Pointer;
//import org.bytedeco.javacpp.PointerPointer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//import static org.bytedeco.javacpp.LLVM.*;
//import static org.bytedeco.javacpp.LLVM.LLVMDisposeBuilder;
//import static org.bytedeco.javacpp.LLVM.LLVMDisposeExecutionEngine;

public class ImpCompilo {

    public static final String JAR = "compiler.jar";
    public static final String GRAMMARCSV = "grammar.csv";
    public static final String ACTIONTABLECSV = "actionTable.csv";

    public static final Logger log;

    static {
        log = Logger.getLogger("ImpCompilo Log");
        log.setLevel(Level.FINE);
    }

    /**
     * Return a {@link java.io.FileReader} based on the filename given.
     * This function stops the execution of the program!
     *
     * @param filename file to read
     * @return the FileReader of the file, null if the file doesn't exist
     */
    public static FileReader file(String filename) {
        FileReader source = null;
        try {
            source = new FileReader(filename);
        } catch (FileNotFoundException e) {
            error("File '" + filename + "' not found.");
        }
        return source;
    }

    public static void error(ImpCompiloException e) {
        error(e.getMessage());
    }

    /**
     * Output the message in stderr,
     * and exit with error code 1.
     * It also log the message with SEVERE level.
     *
     * @param message error message
     */
    public static void error(String message) {
        ImpCompilo.log.severe(message);
        System.err.println(message);
        System.exit(1);
    }


    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:  java -jar " + JAR + " file.imp");
            System.exit(0);
        }
        FileReader source = ImpCompilo.file(args[0]);
        LL1Parser parser = new LL1Parser(source, GRAMMARCSV, ACTIONTABLECSV);
        try {
            parser.parse();
        } catch (ImpCompiloException e) {
            ImpCompilo.error(e);
        }
        //parser.printDerivationTree();
        //parser.printDerivationTree();
        TreeNode ast = parser.getAST();
        ast.print();
        CodeGenerator generator = new CodeGenerator(ast);
        generator.generate();

    }
}
