/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

import java.util.ArrayList;

/**
 *
 * @author timothy
 */
public class StateAFD_DC extends SuperState{
    private ArrayList<TreeNode> nodes;
    private boolean marked;
    private ArrayList<TransitionAfd_DC> transitions;

    public StateAFD_DC(){
        nodes = new ArrayList();
        transitions = new ArrayList();
    }
    public StateAFD_DC(int label, boolean isInitial, boolean isFinal){
        super.setLabel(label);
        super.setIsInitial(isInitial);
        super.setIsfinal(isFinal);
        nodes = new ArrayList();
        transitions = new ArrayList();
        marked = false;
    }

    public void addTransition(String symbol, StateAFD_DC destination){
        TransitionAfd_DC transition = new TransitionAfd_DC(this, symbol, destination);
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

    public ArrayList<TransitionAfd_DC> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<TransitionAfd_DC> transitions) {
        this.transitions = transitions;
    }

}
