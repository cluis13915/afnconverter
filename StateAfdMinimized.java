import java.util.ArrayList;
import java.util.HashSet;

public class StateAfdMinimized extends SuperState{
    private ArrayList<StateAFD> states;
    private ArrayList<TransitionAfdMinimized> transitions;

    public StateAfdMinimized(){}
    public StateAfdMinimized(int label, boolean isInitial, boolean isFinal){
        super.setLabel(label);
        super.setIsInitial(isInitial);
        super.setIsfinal(isFinal);
        transitions = new ArrayList();
        states = new ArrayList();
    }

    public void addTransition(String symbol, StateAfdMinimized destination){
        TransitionAfdMinimized transition = new TransitionAfdMinimized(this, symbol, destination);
        transitions.add(transition);
    }
    public ArrayList<TransitionAfdMinimized> getTransitions(){
        return transitions;
    }
    public void setStates(ArrayList<StateAFD> states){
        this.states = states;
    }
    public ArrayList<StateAFD> getStates(){
        return states;
    }

    public void setSetStates(HashSet<StateAFD> states){
        this.states.addAll(states);
    }
    public HashSet<StateAFD> getSetStates(){
        HashSet<StateAFD> s = new HashSet();
        s.addAll(states);
        return s;
    }

    public void showStates(){
        String out = "Estados de " + this.getLabel() + ": ";
        for (int i=0; i<states.size(); i++)
            out += states.get(i).getLabel() + ", ";
        System.out.println(out);
    }

}
