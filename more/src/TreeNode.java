import java.util.ArrayList;
import java.util.List;

/**
 * Class inspired from:
 * https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
 */

public class TreeNode<E> {

    private E value;
    private List<TreeNode<E>> children;
    private int ruleNumber;

    public TreeNode(E value, int ruleNumber) {
        this(value, ruleNumber, new ArrayList<>());
    }

    public TreeNode(E value, int ruleNumber, List<TreeNode<E>> children) {
        this.value = value;
        this.children = children;
        this.ruleNumber = ruleNumber;
    }

    public TreeNode<E> addChild(E value) {
        TreeNode<E> child = new TreeNode<E>(value, 0);
        children.add(child);
        return child;
    }

    public void setChildren(TreeNode<E> child) {
        children = new ArrayList<>();
        children.add(child);
    }

    public int getRule() {
        return ruleNumber;
    }

    public void setRule(int ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public E getValue() {
        return value;
    }

    public List<TreeNode<E>> getChildren() {
        return children;
    }

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + value);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .print(prefix + (isTail ? "    " : "│   "), true);
        }
    }
}