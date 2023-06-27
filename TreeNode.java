import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TreeNode {
    private String value;
    private TreeNode leftNode;
    private TreeNode rightNode;
    private int index;
    private boolean nullable;
    private Set<TreeNode> firstPos;
    private Set<TreeNode> lastPos;
    private Set<TreeNode> followPos;

    public TreeNode(String value, TreeNode leftNode, TreeNode rightNode, int index) {
        this.value = value;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.index = index;
    }

    public TreeNode(String value, int index) {
        this.value = value;
        this.leftNode = null;
        this.rightNode = null;
        this.index = index;
        this.firstPos = new HashSet();
        this.lastPos = new HashSet();
        this.followPos = new HashSet();
    }

    public TreeNode() {
        this.value = null;
        this.leftNode = null;
        this.rightNode = null;
        this.index = -1;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TreeNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(TreeNode leftNode) {
        this.leftNode = leftNode;
    }

    public TreeNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(TreeNode rightNode) {
        this.rightNode = rightNode;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Set<TreeNode> getFirstPos() {
        return firstPos;
    }

    public void setFirstPos(Set<TreeNode> firstPos) {
        this.firstPos = firstPos;
    }

    public Set<TreeNode> getLastPos() {
        return lastPos;
    }

    public void setLastPos(Set<TreeNode> lastPos) {
        this.lastPos = lastPos;
    }

    public Set<TreeNode> getFollowPos() {
        return followPos;
    }

    public void setFollowPos(Set<TreeNode> followPos) {
        this.followPos = followPos;
    }

    public String getFirstPosToString(){
        String output = "";
        for (TreeNode node: firstPos)
            output += String.valueOf(node.getIndex())+ "  ";
        return output;
    }
    public String getLastPosToString(){
        String output = "";
        for (TreeNode node: lastPos)
            output += String.valueOf(node.getIndex()) + "  ";
        return output;
    }
    public String getFollowPosToString(){
        String output = "";
        for (TreeNode node: followPos)
            output += String.valueOf(node.getIndex()) + "  ";
        return output;
    }
}
