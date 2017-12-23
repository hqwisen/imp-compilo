import sun.reflect.generics.tree.Tree;

import java.util.logging.Logger;

public class CodeGenerator {
    private static final Logger log;
    static{
        log = ImpCompilo.log;
    }

    TreeNode ast;
    int instructionCount;

    public CodeGenerator(TreeNode ast) {
        this.ast = ast;
        this.instructionCount = 1;
    }

    private void generate(){
        generateCode(ast);
    }
    public void generateCode(TreeNode code) {
        for(TreeNode child : code.getChildren()){
            switch (child.getValue()){
                case "<Assign>":
                    generateAssign(child);
                    break;
                case "<Print>":
                    generatePrint(child);
                    break;
                default:
                    throw new ImpCompiloException("Generation not impl. "
                            + child.getValue());
            }
        }
    }


    public String generateExprArith(TreeNode exprArith){
        String instruction = "";
        String value = exprArith.getValue();
        int left, right;
        switch (value){
            case "*":
                instruction += generateExprArith(exprArith.getChild(0));
                left = instructionCount - 1;
                instruction += generateExprArith(exprArith.getChild(1));
                right = instructionCount - 1;
                instruction += "%" + instructionCount + " = mul i32 %" + left
                        + ", %" + right;
                break;
            case "/":
                instruction += generateExprArith(exprArith.getChild(0));
                left = instructionCount - 1;
                instruction += generateExprArith(exprArith.getChild(1));
                right = instructionCount - 1;
                instruction += "%" + instructionCount + " = sdiv i32 %" + left
                        + ", %" + right;
                break;
            case "-":
                instruction += generateExprArith(exprArith.getChild(0));
                left = instructionCount - 1;
                instruction += generateExprArith(exprArith.getChild(1));
                right = instructionCount - 1;
                instruction += "%" + instructionCount + " = sub i32 %" + left
                        + ", %" + right;
                break;
            case "+":
                instruction += generateExprArith(exprArith.getChild(0));
                left = instructionCount - 1;
                instruction += generateExprArith(exprArith.getChild(1));
                right = instructionCount - 1;
                instruction += "%" + instructionCount + " = add i32 %" + left
                        + ", %" + right;
                break;
            default:
                instruction += "%" + instructionCount + " = add i32 0, " + value;
        }
        instructionCount++;
        return instruction + "\n";
    }

    public void generateAssign(TreeNode assign) {
        log.info("Generating LLVM for <Assign>");
        String varName = assign.getChild(0).getConcreteValue();
        String code = "";
        code += generateExprArith(assign.getChild(1));
        code += "%" + varName + " = alloca i32\n";
        code += "store i32 %" + (instructionCount - 1) + ", i32* %" + varName + "\n";
        System.out.println(code);
    }

    public void generatePrint(TreeNode print) {
        log.info("Generating LLVM for <Print>");
        String varName = print.getChild(0).getConcreteValue();
        String code = "";
        code += "call void @println(i32* %" + varName + ")";
        System.out.println(code);
    }


}
