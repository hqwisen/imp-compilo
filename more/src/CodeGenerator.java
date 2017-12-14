import org.bytedeco.javacpp.*;

// Headers required by LLVM
import java.io.FileReader;

import static org.bytedeco.javacpp.LLVM.*;

public class CodeGenerator {

    // FIXME make sure that the jar is create
    private static final String JAR = "dist/part3.jar";

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
        parser.printDerivationTree();

    /*    System.out.println("Starting compiler");
        System.out.println(args.length);
        example();*/
    }


    public static void example() {
        BytePointer error = new BytePointer((Pointer) null); // Used to retrieve messages from functions
        LLVM.LLVMLinkInMCJIT();
        LLVMInitializeNativeAsmPrinter();
        LLVMInitializeNativeAsmParser();
        LLVMInitializeNativeDisassembler();
        LLVMInitializeNativeTarget();
        LLVMModuleRef mod = LLVMModuleCreateWithName("fac_module");
        LLVMTypeRef[] fac_args = {LLVMInt32Type()};
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
        LLVMValueRef[] call_fac_args = {n_minus};
        LLVMValueRef call_fac = LLVMBuildCall(builder, fac, new PointerPointer(call_fac_args), 1, "fac(n - 1)");
        LLVMValueRef res_iffalse = LLVMBuildMul(builder, n, call_fac, "n * fac(n - 1)");
        LLVMBuildBr(builder, end);

        LLVMPositionBuilderAtEnd(builder, end);
        LLVMValueRef res = LLVMBuildPhi(builder, LLVMInt32Type(), "result");
        LLVMValueRef[] phi_vals = {res_iftrue, res_iffalse};
        LLVMBasicBlockRef[] phi_blocks = {iftrue, iffalse};
        LLVMAddIncoming(res, new PointerPointer(phi_vals), new PointerPointer(phi_blocks), 2);
        LLVMBuildRet(builder, res);

        LLVMVerifyModule(mod, LLVMAbortProcessAction, error);
        LLVMDisposeMessage(error); // Handler == LLVMAbortProcessAction -> No need to check errors


        LLVMExecutionEngineRef engine = new LLVMExecutionEngineRef();
        if (LLVMCreateJITCompilerForModule(engine, mod, 2, error) != 0) {
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


    public void assign(String varName, Integer value) {


    }


}
