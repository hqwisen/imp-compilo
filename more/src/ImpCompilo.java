import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImpCompilo {

    public static final Logger log;

    static {
        log = Logger.getLogger("ImpCompilo Log");
        log.setLevel(Level.OFF);
    }
    /**
     * Return a {@link java.io.FileReader} based on the filename given.
     * This function stops the execution of the program!
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

    public static void error(ImpCompiloException e){
        error(e.getMessage());
    }

    /**
     * Output the message in stderr,
     * and exit with error code 1.
     * It also log the message with SEVERE level.
     * @param message error message
     */
    public static void error(String message){
        ImpCompilo.log.severe(message);
        System.err.println(message);
        System.exit(1);
    }


    public static void main(String[] args){
        System.out.println("Compiling source file !");
        
    }

}
