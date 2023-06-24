/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author timothy
 */
public class AFDMinimized_DC {

    private ArrayList<String> symbols;
    private ArrayList<StateAfdMinimized_DC> states;
    private StateAfdMinimized_DC initialState;
    private ArrayList<StateAfdMinimized_DC> finalStates;
    private ArrayList<TransitionAfdMinimized_DC> transitions;
    private int cont = 0;

    // Método Constructor
    public AFDMinimized_DC(){
        symbols = new ArrayList();
        states = new ArrayList();
        finalStates = new ArrayList();
        transitions = new ArrayList();
    }

    // Metodos de entrada y acceso
    public void setSymbols(ArrayList<String> symbols){
        this.symbols = symbols;
    }
    public void setStates(ArrayList<StateAfdMinimized_DC> states){
        this.states = states;
    }
    public void setInitialState(StateAfdMinimized_DC initialState){
        this.initialState = initialState;
    }
    public void setFinalStates(ArrayList<StateAfdMinimized_DC> finalStates){
        this.finalStates = finalStates;
    }
    public void setTransitions(ArrayList<TransitionAfdMinimized_DC> transitions){
        this.transitions = transitions;
    }
    public ArrayList<String> getSymbols(){
        return symbols;
    }
    public ArrayList<StateAfdMinimized_DC> getStates(){
        return states;
    }
    public StateAfdMinimized_DC getInitialState(){
        return initialState;
    }
    public ArrayList<StateAfdMinimized_DC> getFinalStates(){
        return finalStates;
    }
    public ArrayList<TransitionAfdMinimized_DC> getTransitions(){
        return transitions;
    }
    public boolean thereIsTransition(TransitionAfdMinimized_DC trans){
        for (TransitionAfdMinimized_DC t: transitions)
            if (t.getOrigin().equals(trans.getOrigin()) && t.getSymbol().equals(trans.getSymbol()) && t.getDestination().equals(trans.getDestination()))
                return true;
        return false;
    }



    public void refreshAFD(){
        this.searchInitialStates();
        this.searchFinalStates();
        this.findTransitionsForState();
    }

    // Metodo que encuentra las transiciones para cada estado
    private void findTransitionsForState(){
        for (StateAfdMinimized_DC s1: states)
            for (StateAfdMinimized_DC s2: states)
                for (TransitionAfdMinimized_DC t: transitions)
                    if (t.getOrigin().equals(s1) && t.getDestination().equals(s2))
                        s1.addTransition(t.getSymbol(), s2);
    }

    // Metodo para encontrar los estados finales del AFD
    private void searchInitialStates(){
        // Encontrar los estados finales
        for (StateAfdMinimized_DC s1: states)
            if (s1.getIsInitial())
                    this.initialState = s1;
    }

    // Metodo para encontrar los estados finales del AFD
    private void searchFinalStates(){
        // Encontrar los estados finales
        for (StateAfdMinimized_DC s1: states)
            for (StateAFD_DC s2: s1.getSetStates())
                if (s2.getIsfinal() && !finalStates.contains(s1)){
                    s1.setIsfinal(true);
                    finalStates.add(s1);
                }
    }



    // Metodos que retornan los atributos del AFN como tal
    public int getInitialStateToString(){
        return initialState.getLabel();
    }
    public ArrayList<Integer> getFinalStateToString(){
        ArrayList<Integer> output = new ArrayList();
        for (StateAfdMinimized_DC state: finalStates)
            output.add(state.getLabel());
        return output;
    }
    // Metodo para obtener todos los estados en un arreglo
    public ArrayList<Integer> getStatesToString(){
        ArrayList<Integer> output = new ArrayList<Integer>();
        for (int i = 0; i<states.size(); i++)
            output.add(states.get(i).getLabel());
        return output;
    }
    // Metodo para obtener todas las transiciones
    private ArrayList<String> getTransitionsToString(){
        ArrayList<String> output = new ArrayList<String>();
        for (TransitionAfdMinimized_DC transition: transitions)
            output.add("(" + String.valueOf(transition.getOrigin().getLabel()) + ", " + transition.getSymbol() + ", " + String.valueOf(transition.getDestination().getLabel()) + ")");
        return output;
    }

    // Metodo para obtener el afn completo
    public ArrayList<String> afdDescription(){
        ArrayList<String> output = new ArrayList();
        output.add("ESTADOS: " + this.getStatesToString());
        output.add("SIMBOLOS: " + symbols.toString());
        output.add("INICIO: " + this.getInitialStateToString());
        output.add("ACEPTACION: " + this.getFinalStateToString());
        output.add("TRANCISIONES: " + this.getTransitionsToString());
        return output;
    }

    // Metodo para obtenner el codigo para el grafo del AF
    public ArrayList<String> getDigraphCode(){
        ArrayList<String> output = new ArrayList();
        output.add("digraph G");
        output.add("{");
        output.add("rankdir = LR;");
        for (StateAfdMinimized_DC s: states){
            if (s.getIsInitial()){
                output.add("init [shape = point];");
                output.add("init -> " + s.getLabel() + " [label=\"start\"];");
                output.add(String.valueOf(s.getLabel()) + ";");
                if (s.getIsfinal())
                    output.add(String.valueOf(s.getLabel()) + " [shape = doublecircle];");
            }
            else if (s.getIsfinal())
                output.add(String.valueOf(s.getLabel()) + " [shape = doublecircle];");
            else
                output.add(String.valueOf(s.getLabel()) + ";");
        }
        for (TransitionAfdMinimized_DC t: transitions)
            output.add(String.valueOf(t.getOrigin().getLabel()) + " -> " + String.valueOf(t.getDestination().getLabel()) + " [label=\"" + t.getSymbol() + "\"];");
        output.add("}");
        return output;
    }


        //************** SIMULACION DEL AFD **************
    public boolean simulateAFD(String string){
        Simulater simulater = new Simulater();
        boolean accept = simulater.simulateAFD(string);
        return accept;
    }

    /*============================================================================
     * Clase enbebida que posee todas las herramientas para la simulacion del AFD
     =============================================================================*/
    private class Simulater{

        // Metodo para simular el AFN
        public boolean simulateAFD(String string){
            HashSet<StateAfdMinimized_DC> currentStates = new HashSet();
            HashSet<StateAfdMinimized_DC> auxStates  = new HashSet();

            // El estado inicial es el estado inicial del automata
            currentStates.add(initialState);

            // Recorrer la cadena recibida
            for (int i=0; i<string.length(); i++){
                //System.out.println("Evaluando " + String.valueOf(string.charAt(i)));
                auxStates.clear();
                auxStates.addAll(currentStates);    // Hacer una copia de los estados actuales para el ciclo FOR
                currentStates.clear();      // Vaciar currentStates para poder almacenar los nuevos estados

                // Para los estados actuales, verificar transiciones con el simbolo.
                currentStates.addAll(this.move(auxStates,String.valueOf(string.charAt(i))));
            }

            // Si alguno de los estados finales es de aceptacion, la cadena es aceptada por el AFN
            for (StateAfdMinimized_DC s2: currentStates){
                if (s2.getIsfinal())
                    return true;
            }
            return false;
        }

        //Hace las transiciiones de un conjunto de estados con un simbolo. Devuelve los estados alcanzados.
        private HashSet<StateAfdMinimized_DC> move(HashSet<StateAfdMinimized_DC> states, String string){
            HashSet<StateAfdMinimized_DC> moveResult = new HashSet();
            // Verificar para todas las transiciones del estado recibido
            for (StateAfdMinimized_DC s1: states){
                for (TransitionAfdMinimized_DC t: s1.getTransitions()){
                    // Si hay una transicion con el simbolo recibido
                    if (t.getSymbol().equals(string)){
                        //System.out.println("Transición de " + string + " encontrada.");
                        moveResult.add(t.getDestination());
                    }
                }
            }
            return moveResult;
        }
    }

}
