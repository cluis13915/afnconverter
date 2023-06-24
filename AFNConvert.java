/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author timothy
 */
public class AFNConvert {
    private int contStates = -1;
    private Stack<SubAfn> afnStack;
    private SubAfn auxAfn;
    private State auxState1, auxState2;
    private ArrayList<String> symbols;
    private ArrayList<State> states;
    private State initialState;
    private ArrayList<State> finalStates;
    private ArrayList<Transition> transitions;

    public AFNConvert(){}

    private SubAfn fundamentalExp(String symbol){
        // Se crea dos estados y se hace la transición con el simbolo
        auxState1 = new State(contStates+1, true, false);
        auxState2 = new State(contStates+2, false, true);
        auxState1.addTransition(symbol, auxState2);
        states.add(auxState1);
        states.add(auxState2);
        contStates += 2;        // Aumento del contador de estados
        // Se crea un subautomata y se devuelve
        auxAfn = new SubAfn(auxState1, auxState2);
        return auxAfn;
    }
    private SubAfn operateAND(SubAfn afn1, SubAfn afn2){
        afn1.getFinalState().addTransition("&", afn2.getInitialState());
        afn1.getFinalState().setIsfinal(false);
        afn2.getInitialState().setIsInitial(false);
        auxAfn = new SubAfn(afn1.getInitialState(), afn2.getFinalState());
        return auxAfn;
    }
    // Metodo que crea un OR con dos subAutomatas que recibe y devuelve el nuevo automata formado
    private SubAfn operateOR(SubAfn afn1, SubAfn afn2){
        // Un nuevo estado hace transicion por medio de Epsilon con los automatas recibidos
        auxState1 = new State(contStates+1, true, false);
        auxState1.addTransition("&", afn1.getInitialState());
        auxState1.addTransition("&", afn2.getInitialState());
        afn1.getInitialState().setIsInitial(false);
        afn2.getInitialState().setIsInitial(false);
        states.add(auxState1);
        // Los dos automatas recibidos transicionan por medio de Epsilon a un nuevo estado
        auxState2 = new State(contStates+2, false, true);
        afn1.getFinalState().addTransition("&", auxState2);
        afn2.getFinalState().addTransition("&", auxState2);
        afn1.getFinalState().setIsfinal(false);
        afn2.getFinalState().setIsfinal(false);
        states.add(auxState2);
        contStates += 2;        // Aumento del contador de estados
        // Se crea el nuevo automata y se devuelve
        auxAfn = new SubAfn(auxState1, auxState2);
        return auxAfn;
    }

    private SubAfn operateKleene(SubAfn afn1){
        // Se crean los dos estados y se hacen las transiciones correspondientes
        auxState1 = new State(contStates+1, true, false);
        auxState1.addTransition("&", afn1.getInitialState());
        auxState2 = new State(contStates+2, false, true);
        afn1.getFinalState().addTransition("&", auxState2);
        afn1.getFinalState().addTransition("&", afn1.getInitialState());
        auxState1.addTransition("&", auxState2);
        afn1.getInitialState().setIsInitial(false);
        afn1.getFinalState().setIsfinal(false);
        states.add(auxState1);
        states.add(auxState2);
        contStates += 2;        // Aumento del contador de estados
        // Se crea el nuevo automata y se devuelve
        auxAfn = new SubAfn(auxState1, auxState2);
        return auxAfn;
    }

    public AFN convert(String regexp){
        AFN afn = new AFN();

        states = new ArrayList();
        afnStack = new Stack();
        SubAfn auxAfn1;
        String s, sp;

        // Encontrar los simbolos de la expresion regular
        this.searchSymbols(regexp);

        // Conversion de la regexp
        for (int i=0; i<regexp.length(); i++){
            s = String.valueOf(regexp.charAt(i));
            if (symbols.contains(s)){
                afnStack.push(fundamentalExp(s));
            }
            else if(s.equals(".")){
                auxAfn1 = afnStack.pop();
                afnStack.push(operateAND(afnStack.pop(), auxAfn1));
            }
            else if(s.equals("|")){
                auxAfn1 = afnStack.pop();
                afnStack.push(operateOR(afnStack.pop(), auxAfn1));
            }
            else if(s.equals("*")){
                afnStack.push(operateKleene(afnStack.pop()));
            }
        }
        // Encontrar los componentes del AFNConvert
        this.searchInitialState();
        this.searchFinalStates();
        this.searchTransitions();

        // Formar el AFN
        afn.setSymbols(symbols);
        afn.setStates(states);
        afn.setInitialState(initialState);
        afn.setFinalStates(finalStates);
        afn.setTransitions(transitions);
        // Retornar el AFN
        return afn;
    }

    // Metodo para obtener todas las transiciones
    private void searchTransitions(){
        transitions = new ArrayList<Transition>();
        for (int i = 0; i<states.size(); i++){
            for (int j = 0; j<states.get(i).getTransitions().size(); j++){
                transitions.add(states.get(i).getTransitions().get(j));
            }
        }
    }

    // Metodo para encontrar todos los simbolos del alfabeto
    private void searchSymbols(String regexp){
        String specialChars = "|.*&";
        symbols = new ArrayList();
        // Simbolos del alfabeto
        for (String s: regexp.split(""))
            if (!specialChars.contains(s) && !symbols.contains(s))
                symbols.add(s);
    }

    // Metodo para obtener todos los estados de aceptacion
    private void searchFinalStates(){
        finalStates = new ArrayList<State>();
        for (int i = 0; i<states.size(); i++)
            if (states.get(i).getIsfinal())
                finalStates.add(states.get(i));
    }
    // Metodo para encontrar el estado inicial. Teoricamente, deberia ser solo uno.
    private void searchInitialState(){
        for (int i = 0; i<states.size(); i++)
            if (states.get(i).getIsInitial())
                initialState = states.get(i);
    }

}
