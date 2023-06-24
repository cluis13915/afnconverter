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
public class State extends SuperState{

    private ArrayList<Transition> transitions;

    public State(){}
    public State(int label, boolean isInitial, boolean isFinal){
        super.setLabel(label);
        super.setIsInitial(isInitial);
        super.setIsfinal(isFinal);
        transitions = new ArrayList();
    }
    public void addTransition(String symbol, State destination){
        Transition transition = new Transition(this, symbol, destination);
        transitions.add(transition);
    }

    public ArrayList<Transition> getTransitions(){
        return transitions;
    }

}
