/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author timothy
 */
public class StateAFD extends SuperState {
    private ArrayList<State> states;
    private boolean isMarked;
    private ArrayList<TransitionAfd> transitions;

    public StateAFD(){}
    public StateAFD(int label, boolean isInitial, boolean isFinal){
        super.setLabel(label);
        super.setIsInitial(isInitial);
        super.setIsfinal(isFinal);
        transitions = new ArrayList();
        states = new ArrayList();
    }

    public void addTransition(String symbol, StateAFD destination){
        TransitionAfd transition = new TransitionAfd(this, symbol, destination);
        transitions.add(transition);
    }
    public ArrayList<TransitionAfd> getTransitions(){
        return transitions;
    }

    public void setStates(ArrayList<State> states){
        this.states = states;
    }
    public ArrayList<State> getStates(){
        return states;
    }

    public void setSetStates(HashSet<State> states){
        this.states.addAll(states);
    }
    public HashSet<State> getSetStates(){
        HashSet<State> s = new HashSet<State>();
        s.addAll(states);
        return s;
    }


    public void setIsMarked(boolean value){
        isMarked = value;
    }
    public boolean getIsMarked(){
        return isMarked;
    }

    public void showStates(){
        String out = "Estados de " + this.getLabel() + ": ";
        for (int i=0; i<states.size(); i++){
            out += states.get(i).getLabel() + ", ";
        }
        System.out.println(out);
    }

}
