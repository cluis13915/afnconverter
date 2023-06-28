import java.util.ArrayList;

public class StateAFD extends SuperState{
    private ArrayList<TreeNode> nodes;
    private boolean marked;
    private ArrayList<TransitionAfd> transitions;

    public StateAFD() {
        nodes = new ArrayList();
        transitions = new ArrayList();
    }

    public StateAFD(int label, boolean isInitial, boolean isFinal) {
        super.setLabel(label);
        super.setIsInitial(isInitial);
        super.setIsfinal(isFinal);
        nodes = new ArrayList();
        transitions = new ArrayList();
        marked = false;
    }

    public void addTransition(String symbol, StateAFD destination) {
        TransitionAfd transition = new TransitionAfd(this, symbol, destination);
        transitions.add(transition);
    }

    public ArrayList<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public ArrayList<TransitionAfd> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<TransitionAfd> transitions) {
        this.transitions = transitions;
    }
}
