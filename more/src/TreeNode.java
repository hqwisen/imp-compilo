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

    public void setValue(String value) {
        this.value = value;
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    /**
     * Set a new child at the index. This will reference a new object
     * at index, with EPSILON.
     * The child will not be epsilon, but a child pointing to epsilon.
     * This is implemented like this to follow the concept of the grammar,
     * that make a variable point to epsilon.
     * @param index
     */
    public void setEmptyChild(int index){
        TreeNode child = new TreeNode(LL1Parser.EPSILON);
        child.addChild(LL1Parser.EPSILON); // necessary, to allow LL1Parser.removeEpsilonNode
        children.set(index, child);
    }


//    public TreeNode pushLeft(String value) {
//        TreeNode child = new TreeNode(value);
//        children.add(0, child);
//        return child;
//    }

    public void pushLeft(TreeNode child) {
        children.add(0, child);
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

    public String toString() {
        return "T(" + getValue() + ")";
    }

    public String getChildValue(int i) {
        return getChild(i).getValue();
    }

    public TreeNode getChild(int i) {
        return children.get(i);
    }

}