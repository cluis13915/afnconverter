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
public class StateAfdMinimized_DC extends SuperState{
    private ArrayList<StateAFD_DC> states;
    private ArrayList<TransitionAfdMinimized_DC> transitions;

    public StateAfdMinimized_DC(){}
    public StateAfdMinimized_DC(int label, boolean isInitial, boolean isFinal){
        super.setLabel(label);
        super.setIsInitial(isInitial);
        super.setIsfinal(isFinal);
        transitions = new ArrayList();
        states = new ArrayList();
    }

    public void addTransition(String symbol, StateAfdMinimized_DC destination){
        TransitionAfdMinimized_DC transition = new TransitionAfdMinimized_DC(this, symbol, destination);
        transitions.add(transition);
    }
    public ArrayList<TransitionAfdMinimized_DC> getTransitions(){
        return transitions;
    }
    public void setStates(ArrayList<StateAFD_DC> states){
        this.states = states;
    }
    public ArrayList<StateAFD_DC> getStates(){
        return states;
    }

    public void setSetStates(HashSet<StateAFD_DC> states){
        this.states.addAll(states);
    }
    public HashSet<StateAFD_DC> getSetStates(){
        HashSet<StateAFD_DC> s = new HashSet();
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
