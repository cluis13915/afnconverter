import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AFDMinimized {
    private ArrayList<String> symbols;
    private ArrayList<StateAfdMinimized> states;
    private StateAfdMinimized initialState;
    private ArrayList<StateAfdMinimized> finalStates;
    private ArrayList<TransitionAfdMinimized> transitions;
    private int cont = 0;

    // Método Constructor
    public AFDMinimized() {
        symbols = new ArrayList();
        states = new ArrayList();
        finalStates = new ArrayList();
        transitions = new ArrayList();
    }

    // Metodos de entrada y acceso
    public void setSymbols(ArrayList<String> symbols) {
        this.symbols = symbols;
    }

    public void setStates(ArrayList<StateAfdMinimized> states) {
        this.states = states;
    }

    public void setInitialState(StateAfdMinimized initialState) {
        this.initialState = initialState;
    }

    public void setFinalStates(ArrayList<StateAfdMinimized> finalStates) {
        this.finalStates = finalStates;
    }

    public void setTransitions(ArrayList<TransitionAfdMinimized> transitions) {
        this.transitions = transitions;
    }

    public ArrayList<String> getSymbols() {
        return symbols;
    }

    public ArrayList<StateAfdMinimized> getStates() {
        return states;
    }

    public StateAfdMinimized getInitialState() {
        return initialState;
    }

    public ArrayList<StateAfdMinimized> getFinalStates() {
        return finalStates;
    }

    public ArrayList<TransitionAfdMinimized> getTransitions() {
        return transitions;
    }

    public boolean thereIsTransition(TransitionAfdMinimized trans) {
        for (TransitionAfdMinimized t: transitions)
            if (t.getOrigin().equals(trans.getOrigin()) && t.getSymbol().equals(trans.getSymbol()) && t.getDestination().equals(trans.getDestination()))
                return true;
        return false;
    }

    public void refreshAFD() {
        this.searchInitialStates();
        this.searchFinalStates();
        this.findTransitionsForState();
    }

    // Metodo que encuentra las transiciones para cada estado
    private void findTransitionsForState() {
        for (StateAfdMinimized s1: states)
            for (StateAfdMinimized s2: states)
                for (TransitionAfdMinimized t: transitions)
                    if (t.getOrigin().equals(s1) && t.getDestination().equals(s2))
                        s1.addTransition(t.getSymbol(), s2);
    }

    // Metodo para encontrar los estados finales del AFD
    private void searchInitialStates() {
        // Encontrar los estados finales
        for (StateAfdMinimized s1: states)
            if (s1.getIsInitial())
                    this.initialState = s1;
    }

    // Metodo para encontrar los estados finales del AFD
    private void searchFinalStates() {
        // Encontrar los estados finales
        for (StateAfdMinimized s1: states)
            for (StateAFD s2: s1.getSetStates())
                if (s2.getIsfinal() && !finalStates.contains(s1)) {
                    s1.setIsfinal(true);
                    finalStates.add(s1);
                }
    }

    // Metodos que retornan los atributos del AFN como tal
    public int getInitialStateToString() {
        return initialState.getLabel();
    }

    public String getFinalStateToString() {
        String output = "";

        for (StateAfdMinimized state: finalStates)
            // Si ya tiene valor, agregamos una coma antes del estado. Si no, agregamos solo el estado.
            output += (output.length()>0) ? ("," + state.getLabel()) : state.getLabel();

        return output;
    }

    // Metodo para obtener todos los estados en un arreglo
    public ArrayList<Integer> getStatesToString() {
        ArrayList<Integer> output = new ArrayList<Integer>();
        for (int i = 0; i<states.size(); i++)
            output.add(states.get(i).getLabel());
        return output;
    }

    // Metodo para obtener todas las transiciones
    private ArrayList<String> getTransitionsToString() {
        ArrayList<String> output = new ArrayList<String>();
        String symbolTransitions;

        for (int i = 0; i<symbols.size(); i++) {
            symbolTransitions = "0";

            for (TransitionAfdMinimized transition: transitions) {
                if (transition.getSymbol() == symbols.get(i)) {
                    symbolTransitions += "," + String.valueOf(transition.getDestination().getLabel());
                }
            }

            // output.add("(" + String.valueOf(transition.getOrigin().getLabel()) + ", " + transition.getSymbol() + ", " + String.valueOf(transition.getDestination().getLabel()) + ")");
            output.add(symbolTransitions);
        }

        return output;
    }

    // Metodo para obtener la representacion del AFD minimizado
    public ArrayList<String> afdDescription() {
        ArrayList<String> output = new ArrayList();
        // Unimos los simbolos por una coma.
        output.add(String.join(",", symbols));
        // Le sumamos 1 por el estado 0.
        output.add(String.valueOf(this.states.size() + 1));
        output.add(this.getFinalStateToString());

        for (String transitionsString: this.getTransitionsToString()) {
            output.add(transitionsString);
        }

        return output;
    }

    // Metodo para obtenner el codigo para el grafo del AF
    public ArrayList<String> getDigraphCode() {
        ArrayList<String> output = new ArrayList();
        output.add("digraph G");
        output.add("{");
        output.add("rankdir = LR;");
        for (StateAfdMinimized s: states) {
            if (s.getIsInitial()) {
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
        for (TransitionAfdMinimized t: transitions)
            output.add(String.valueOf(t.getOrigin().getLabel()) + " -> " + String.valueOf(t.getDestination().getLabel()) + " [label=\"" + t.getSymbol() + "\"];");
        output.add("}");
        return output;
    }


        //************** SIMULACION DEL AFD **************

    public boolean simulateAFD(String string) {
        Simulater simulater = new Simulater();
        boolean accept = simulater.simulateAFD(string);
        return accept;
    }

    /*============================================================================
     * Clase enbebida que posee todas las herramientas para la simulacion del AFD
     =============================================================================*/
    private class Simulater{

        // Metodo para simular el AFN
        public boolean simulateAFD(String string) {
            HashSet<StateAfdMinimized> currentStates = new HashSet();
            HashSet<StateAfdMinimized> auxStates  = new HashSet();

            // El estado inicial es el estado inicial del automata
            currentStates.add(initialState);

            // Recorrer la cadena recibida
            for (int i=0; i<string.length(); i++) {
                //System.out.println("Evaluando " + String.valueOf(string.charAt(i)));
                auxStates.clear();
                auxStates.addAll(currentStates);    // Hacer una copia de los estados actuales para el ciclo FOR
                currentStates.clear();      // Vaciar currentStates para poder almacenar los nuevos estados

                // Para los estados actuales, verificar transiciones con el simbolo.
                currentStates.addAll(this.move(auxStates,String.valueOf(string.charAt(i))));
            }

            // Si alguno de los estados finales es de aceptacion, la cadena es aceptada por el AFN
            for (StateAfdMinimized s2: currentStates) {
                if (s2.getIsfinal())
                    return true;
            }
            return false;
        }

        //Hace las transiciiones de un conjunto de estados con un simbolo. Devuelve los estados alcanzados.
        private HashSet<StateAfdMinimized> move(HashSet<StateAfdMinimized> states, String string) {
            HashSet<StateAfdMinimized> moveResult = new HashSet();
            // Verificar para todas las transiciones del estado recibido
            for (StateAfdMinimized s1: states) {
                for (TransitionAfdMinimized t: s1.getTransitions()) {
                    // Si hay una transicion con el simbolo recibido
                    if (t.getSymbol().equals(string)) {
                        //System.out.println("Transición de " + string + " encontrada.");
                        moveResult.add(t.getDestination());
                    }
                }
            }
            return moveResult;
        }
    }
}
