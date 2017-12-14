import java.util.ArrayList;
import java.util.List;

/**
 * Class inspired from:
 * https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
 */

public class TreeNode {

    private String value;
    private List<TreeNode> children;

    public TreeNode(String value) {
        this(value, new ArrayList<>());
    }

    public TreeNode(String value, List<TreeNode> children) {
        this.value = value;
        this.children = children;
    }

    public TreeNode addChild(String value) {
        TreeNode child = new TreeNode(value);
        children.add(child);
        return child;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public List<TreeNode> getChildren() {
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