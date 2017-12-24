import java.util.ArrayList;
import java.util.List;

/**
 * Class inspired from:
 * https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
 */

public class TreeNode {

    private String value, concreteValue;
    private List<TreeNode> children;

    public TreeNode(String value) {
        this(value, new ArrayList<>());
    }
    public TreeNode(String value, String concreteValue) {
        this(value);
        this.concreteValue = concreteValue;
    }

    public TreeNode(String value, List<TreeNode> children) {
        this.value = value;
        this.children = children;
        this.concreteValue = null;
    }

    public TreeNode addChild(String value, String concreteValue) {
        TreeNode child = new TreeNode(value, concreteValue);
        return addChild(child);
    }
    public TreeNode addChild(String value) {
        TreeNode child = new TreeNode(value);
        return addChild(child);
    }

    public TreeNode addChild(TreeNode child){
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

    public String getConcreteValue() {
        return concreteValue;
    }

    public void setConcreteValue(String concreteValue) {
        this.concreteValue = concreteValue;
    }

    public TreeNode removeChild(int index) {
        return children.remove(index);
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
        setChild(index, child);
    }

    /**
     * set empty child where child is.
     * @param child child to replace by empty node.
     */
    public void setEmptyChild(TreeNode child){
        int childIndex = getChildren().indexOf(child);
        TreeNode newChild = new TreeNode(LL1Parser.EPSILON);
        newChild.addChild(LL1Parser.EPSILON); // necessary, to allow LL1Parser.removeEpsilonNode
        setChild(childIndex, newChild);
    }


    public void changeChild(TreeNode oldChild, TreeNode newChild) {
        int index = getChildren().indexOf(oldChild);
        setChild(index, newChild);
    }

    public void setChild(int index, TreeNode child){
        children.set(index, child);
    }

    public TreeNode pushLeft(String value) {
        TreeNode child = new TreeNode(value);
        children.add(0, child);
        return child;
    }

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
        String value = this.concreteValue == null ? this.value : this.concreteValue;
        System.err.println(prefix + (isTail ? "└── " : "├── ") + value);
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

    public static void switchChildren(TreeNode firstNode, TreeNode secondNode) {
        String firstValue = firstNode.getValue();
        String secondValue = secondNode.getValue();
        List<TreeNode> firstChildren = firstNode.getChildren();
        List<TreeNode> secondChildren = secondNode.getChildren();
        if(firstChildren.contains(secondNode)){
            firstNode.setEmptyChild(secondNode);
        }
        if (secondChildren.contains(firstNode)){
            secondNode.setEmptyChild(firstNode);
        }
        firstNode.setChildren(secondChildren);
        firstNode.setValue(secondValue);
        secondNode.setChildren(firstChildren);
        secondNode.setValue(firstValue);
    }

    public int numberOfChildren() {
        return children.size();
    }

    public void addChildAsValue(TreeNode child) {
        addChild(child.getValue());
    }
}