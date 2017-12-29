import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CodeGenerator {
    private static final Logger log;

    static {
        log = ImpCompilo.log;
    }

    private TreeNode ast;
    private int instructionCount, ifCount, loopCount;
    private Map<String, Boolean> allocaVars;

    public CodeGenerator(TreeNode ast) {
        this.ast = ast;
        this.instructionCount = 1;
        this.ifCount = 0;
        this.loopCount = 0;
        this.allocaVars = new HashMap<>();
    }

    public String buildInit() {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream("init.ll")));
            String line = br.readLine();
            while (line != null) {
                result += line + "\n";
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void generate() {
        String code = "";
        code += buildInit();
        String generatedLL = "define void @main(){\n";
        for (String line : generateCode(ast).split("\n")) {
            generatedLL += "\t" + line + "\n";
        }
        generatedLL += "}";
        code += "\n; Generated LL below\n" + generatedLL;
        log.info("Generated AST instructions:");
        log.info(generatedLL);
        System.out.println(code);
    }

    public String generateCode(TreeNode code) {
        return generateCode(code, true);
    }

    public String generateCode(TreeNode code, boolean ret) {
        String instructions = "";
        for (TreeNode child : code.getChildren()) {
            switch (child.getValue()) {
                case "<Assign>":
                    instructions += generateAssign(child);
                    break;
                case "<Print>":
                    instructions += generatePrint(child);
                    break;
                case "<Read>":
                    instructions += generateRead(child);
                    break;
                case "<If>":
                    instructions += generateIf(child);
                    break;
                case "<While>":
                    instructions += generateWhile(child);
                    break;
                case "<For>":
                    instructions += generateFor(child);
                    break;
                default:
                    throw new ImpCompiloException("Generation not impl. "
                            + child.getValue());
            }
        }
        if (ret) {
            instructions += "\nret void";
        }
        instructions += "\n";
        return instructions;
    }


    public String generateExprArith(TreeNode exprArith) {
        String instruction = "";
        String value = exprArith.getValue();
        int left, right;
        switch (value) {
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
                try {
                    Integer.parseInt(value); // if value is digit do nothing
                } catch (NumberFormatException e) {
                    // if value is var, load it
                    instruction += "%" + instructionCount +
                            " = load i32, i32* %" + value + "\n";
                    value = "%" + instructionCount;
                    instructionCount++;
                }
                instruction += "%" + instructionCount + " = add i32 0, " + value;
        }
        instructionCount++;
        return instruction + "\n";
    }


    public String generateSimpleCond(TreeNode simpleCond) {
        String instruction = "";
        String value = simpleCond.getChild(1).getValue();
        String left, right;
        try {
            Integer.parseInt(simpleCond.getChildValue(0));
            left = simpleCond.getChildValue(0);
        } catch (NumberFormatException e) {
            instruction += "%" + instructionCount +
                    " = load i32, i32* %" + simpleCond.getChildValue(0) + "\n";
            left = "%" + instructionCount;
            instructionCount++;
        }
        try {
            Integer.parseInt(simpleCond.getChildValue(2));
            right = simpleCond.getChildValue(2);
        } catch (NumberFormatException e) {
            instruction += "%" + instructionCount +
                    " = load i32, i32* %" + simpleCond.getChildValue(2) + "\n";
            right = "%" + instructionCount;
            instructionCount++;
        }

        switch (value) {
            case "=":
                instruction += "%" + instructionCount + " = icmp eq i32 "
                        + left + ", " + right;
                break;
            case "<>":

                instruction += "%" + instructionCount + " = icmp ne i32 "
                        + left + ", " + right;
                break;
            case ">":
                instruction += "%" + instructionCount + " = icmp sgt i32 "
                        + left + ", " + right;
                break;
            case ">=":
                instruction += "%" + instructionCount + " = icmp sge i32 "
                        + left + ", " + right;
                break;
            case "<":
                instruction += "%" + instructionCount + " = icmp slt i32 "
                        + left + ", " + right;
                break;
            case "<=":
                instruction += "%" + instructionCount + " = icmp sle i32 "
                        + left + ", " + right;
                break;
            default:
                throw new ImpCompiloException("Unknown <Comp> " + value);
        }
        instructionCount++;
        return instruction;
    }


    public String generateCond(TreeNode cond) {
        String instruction = "";
        String value = cond.getValue();
        int left, right;
        switch (value) {
            case "and":
                instruction += generateCond(cond.getChild(0));
                left = instructionCount - 1;
                instruction += generateCond(cond.getChild(1));
                right = instructionCount - 1;
                instruction += "%" + instructionCount + " = add i1 %" + left
                        + ", %" + right + "\n";
                instructionCount++;
                instruction += "%" + instructionCount + " = icmp  eq i1 %"
                        + (instructionCount - 1) + ", 2"; // both true
                instructionCount++;
                break;
            case "or":
                instruction += generateCond(cond.getChild(0));
                left = instructionCount - 1;
                instruction += generateCond(cond.getChild(1));
                right = instructionCount - 1;
                instruction += "%" + instructionCount + " = add i1 %" + left
                        + ", %" + right + "\n";
                instructionCount++;
                instruction += "%" + instructionCount + " = icmp  uge i1 %"
                        + (instructionCount - 1) + ", 1"; // at least on true
                instructionCount++;
                break;
            default: // <SimpleCond> of the <AtomCond>
                instruction += generateSimpleCond(cond);
        }
        return instruction + "\n";
    }

    public String generateAssign(String varName, TreeNode exprArith) {
        String instructions = "";
        instructions += generateExprArith(exprArith);
        if (!allocaVars.containsKey(varName)) {
            instructions += "%" + varName + " = alloca i32\n";
            allocaVars.put(varName, true);
        }
        instructions += "store i32 %" + (instructionCount - 1) + ", i32* %" + varName + "\n";
        return instructions;
    }

    public String generateAssign(TreeNode assign) {
        log.info("Generating LLVM for <Assign>");
        String instructions = "; " + assign + "\n";
        String varName = assign.getChild(0).getConcreteValue();
        TreeNode exprArith = assign.getChild(1);
        instructions += generateAssign(varName, exprArith);
        return instructions;


    }

    public String generatePrint(TreeNode print) {
        log.info("Generating LLVM for <Print>");
        String varName = print.getChild(0).getConcreteValue();
        String instructions = "";
        instructions += "call void @println(i32* %" + varName + ")" + "\n";
        return instructions;
    }

    public String generateRead(TreeNode readNode) {
        log.info("Generating LLVM for <Read>");
        String varName = readNode.getChild(0).getConcreteValue();
        String instructions = "";
        instructions += "%" + instructionCount +
                " = call i32 @readInt()" + "\n";
        instructionCount++;
        if (!allocaVars.containsKey(varName)) {
            instructions += "%" + varName + " = alloca i32\n";
            allocaVars.put(varName, true);
        }
        instructions += "store i32 %" + (instructionCount - 1)
                + ", i32* %" + varName + "\n";
        return instructions;
    }

    public String generateIf(TreeNode ifNode) {
        log.info("Generating LLVM for <If>");
        ifCount++;
        String instructions = "; " + ifNode + "ifCount=" + ifCount + "\n";
        int localIfCount = ifCount;
        String ifTrue = "iftrue" + localIfCount;
        String ifFalse = "iffalse" + localIfCount;
        String ifContinue = "ifcontinue" + localIfCount;
        instructions += generateCond(ifNode.getChild(0));
        instructions += "br i1 %" + (instructionCount - 1) + "," +
                " label %" + ifTrue + ", label %" + ifFalse + "\n";
        instructions += ifTrue + ":\n";
        instructions += generateCode(ifNode.getChild(1), false);
        // jump to ifContinue
        instructions += "br label %" + ifContinue + "\n";
        instructions += ifFalse + ":\n";
        instructions += generateCode(ifNode.getChild(2), false);
        // jump to ifContinue
        instructions += "br label %" + ifContinue + "\n";
        instructions += ifContinue + ":\n";
        return instructions;
    }

    public String generateWhile(TreeNode whileNode) {
        log.info("Generating LLVM for <While>");
        loopCount++;
        String instructions = "; " + whileNode + "loopCount="
                + loopCount + "\n";
        int localLoopCount = loopCount;
        String ifTrue = "beginLoop" + localLoopCount;
        String ifFalse = "endLoop" + localLoopCount;

        instructions += generateCond(whileNode.getChild(0));
        instructions += "br i1 %" + (instructionCount - 1) + "," +
                " label %" + ifTrue + ", label %" + ifFalse + "\n";
        instructions += ifTrue + ":\n";
        instructions += generateCode(whileNode.getChild(1), false);
        instructions += generateCond(whileNode.getChild(0));
        instructions += "br i1 %" + (instructionCount - 1) + "," +
                " label %" + ifTrue + ", label %" + ifFalse + "\n";
        instructions += ifFalse + ":\n";
        return instructions;
    }

    private String generateFor(TreeNode forNode) {
        log.info("Generating LLVM for <For>");
        loopCount++;
        String instructions = "; " + forNode + "loopCount="
                + loopCount + "\n";
        int localLoopCount = loopCount;
        String ifTrue = "beginLoop" + localLoopCount;
        String ifFalse = "endLoop" + localLoopCount;
        // Intialize var
        String varName = forNode.getChild(0).getConcreteValue();
        TreeNode initExprArith = forNode.getChild(1);
        TreeNode stepExprArith = forNode.getChild(2);
        TreeNode thresholdExprArith = forNode.getChild(3);
        TreeNode codeNode = forNode.getChild(4);
        instructions += generateAssign(varName, initExprArith);

        // Condition from < to (outside br)
        instructions += generateExprArith(thresholdExprArith);
        instructions += "%" + instructionCount +
                " = load i32, i32* %" + varName + "\n";
        instructionCount++;
        instructions += "%" + instructionCount + " = icmp  slt i32 "
                + "%" + (instructionCount - 1) + ", %"
                + (instructionCount - 2) + "\n";
        instructionCount++;
        instructions += "br i1 %" + (instructionCount - 1) + "," +
                " label %" + ifTrue + ", label %" + ifFalse + "\n";

        instructions += ifTrue + ":\n";
        instructions += generateCode(codeNode, false);

        // varName + by
        instructions += generateExprArith(stepExprArith);
        instructions += "%" + instructionCount +
                " = load i32, i32* %" + varName + "\n";
        instructionCount++;
        instructions += "%" + instructionCount + " = add i32 %"
                + (instructionCount - 1)
                + ", %" + (instructionCount - 2) + "\n";
        instructionCount++;
        instructions += "store i32 %" + (instructionCount - 1) +
                ", i32* %" + varName + "\n";

        // Condition from < to (inside br)
        instructions += generateExprArith(thresholdExprArith);
        instructions += "%" + instructionCount +
                " = load i32, i32* %" + varName + "\n";
        instructionCount++;
        instructions += "%" + instructionCount + " = icmp  slt i32 "
                + "%" + (instructionCount - 1) +
                ", %" + (instructionCount - 2) + "\n";
        instructionCount++;
        instructions += "br i1 %" + (instructionCount - 1) + "," +
                " label %" + ifTrue + ", label %" + ifFalse + "\n";

        instructions += ifFalse + ":\n";


        return instructions;
    }


}

