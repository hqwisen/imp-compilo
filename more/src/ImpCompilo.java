import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bytedeco.javacpp.LLVM.*;
import static org.bytedeco.javacpp.LLVM.LLVMDisposeBuilder;
import static org.bytedeco.javacpp.LLVM.LLVMDisposeExecutionEngine;

public class ImpCompilo {

    public static final String JAR = "compiler.jar";
    public static final String GRAMMARCSV = "grammar.csv";
    public static final String ACTIONTABLECSV = "actionTable.csv";

    public static final Logger log;

    static {
        log = Logger.getLogger("ImpCompilo Log");
        log.setLevel(Level.OFF);
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
        List<Symbol> tokens = null;
        try {
            tokens = parser.parse();
        } catch (ImpCompiloException e) {
            ImpCompilo.error(e);
        }
        if(tokens != null){

            BytePointer error = new BytePointer((Pointer)null); // Used to retrieve messages from functions
            LLVMLinkInMCJIT();
            LLVMInitializeNativeAsmPrinter();
            LLVMInitializeNativeAsmParser();
            LLVMInitializeNativeDisassembler();
            LLVMInitializeNativeTarget();
            LLVMModuleRef mod = LLVMModuleCreateWithName("fac_module");
            LLVMTypeRef[] fac_args = { LLVMInt32Type() };
            LLVMValueRef fac = LLVMAddFunction(mod, "fac", LLVMFunctionType(LLVMInt32Type(), fac_args[0], 1, 0));
            LLVMSetFunctionCallConv(fac, LLVMCCallConv);
            LLVMValueRef n = LLVMGetParam(fac, 0);

            LLVMBasicBlockRef entry = LLVMAppendBasicBlock(fac, "entry");
            LLVMBasicBlockRef iftrue = LLVMAppendBasicBlock(fac, "iftrue");
            LLVMBasicBlockRef iffalse = LLVMAppendBasicBlock(fac, "iffalse");
            LLVMBasicBlockRef end = LLVMAppendBasicBlock(fac, "end");
            LLVMBuilderRef builder = LLVMCreateBuilder();

            LLVMPositionBuilderAtEnd(builder, entry);
            LLVMValueRef If = LLVMBuildICmp(builder, LLVMIntEQ, n, LLVMConstInt(LLVMInt32Type(), 0, 0), "n == 0");
            LLVMBuildCondBr(builder, If, iftrue, iffalse);

            LLVMPositionBuilderAtEnd(builder, iftrue);
            LLVMValueRef res_iftrue = LLVMConstInt(LLVMInt32Type(), 1, 0);
            LLVMBuildBr(builder, end);

            LLVMPositionBuilderAtEnd(builder, iffalse);
            LLVMValueRef n_minus = LLVMBuildSub(builder, n, LLVMConstInt(LLVMInt32Type(), 1, 0), "n - 1");
            LLVMValueRef[] call_fac_args = { n_minus };
            LLVMValueRef call_fac = LLVMBuildCall(builder, fac, new PointerPointer(call_fac_args), 1, "fac(n - 1)");
            LLVMValueRef res_iffalse = LLVMBuildMul(builder, n, call_fac, "n * fac(n - 1)");
            LLVMBuildBr(builder, end);

            LLVMPositionBuilderAtEnd(builder, end);
            LLVMValueRef res = LLVMBuildPhi(builder, LLVMInt32Type(), "result");
            LLVMValueRef[] phi_vals = { res_iftrue, res_iffalse };
            LLVMBasicBlockRef[] phi_blocks = { iftrue, iffalse };
            LLVMAddIncoming(res, new PointerPointer(phi_vals), new PointerPointer(phi_blocks), 2);
            LLVMBuildRet(builder, res);

            LLVMVerifyModule(mod, LLVMAbortProcessAction, error);
            LLVMDisposeMessage(error); // Handler == LLVMAbortProcessAction -> No need to check errors


            LLVMExecutionEngineRef engine = new LLVMExecutionEngineRef();
            if(LLVMCreateJITCompilerForModule(engine, mod, 2, error) != 0) {
                System.err.println(error.getString());
                LLVMDisposeMessage(error);
                System.exit(-1);
            }

            LLVMPassManagerRef pass = LLVMCreatePassManager();
            LLVMAddConstantPropagationPass(pass);
            LLVMAddInstructionCombiningPass(pass);
            LLVMAddPromoteMemoryToRegisterPass(pass);
            // LLVMAddDemoteMemoryToRegisterPass(pass); // Demotes every possible value to memory
            LLVMAddGVNPass(pass);
            LLVMAddCFGSimplificationPass(pass);
            LLVMRunPassManager(pass, mod);
            LLVMDumpModule(mod);

            LLVMGenericValueRef exec_args = LLVMCreateGenericValueOfInt(LLVMInt32Type(), 10, 0);
            LLVMGenericValueRef exec_res = LLVMRunFunction(engine, fac, 1, exec_args);
            System.err.println();
            System.err.println("; Running fac(10) with JIT...");
            System.err.println("; Result: " + LLVMGenericValueToInt(exec_res, 0));

            LLVMDisposePassManager(pass);
            LLVMDisposeBuilder(builder);
            LLVMDisposeExecutionEngine(engine);

        }

    }
}
