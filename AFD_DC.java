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
public class AFD_DC {

    private ArrayList<String> symbols;
    private ArrayList<StateAFD_DC> states;
    private StateAFD_DC initialState;
    private ArrayList<StateAFD_DC> finalStates;
    private ArrayList<TransitionAfd_DC> transitions;

    // Método Constructor
    public AFD_DC(){
        symbols = new ArrayList();
        states = new ArrayList();
        finalStates = new ArrayList();
        transitions = new ArrayList();
    }

    // Metodos de entrada y acceso
    public void setSymbols(ArrayList<String> symbols){
        this.symbols = symbols;
    }

    public void setStates(ArrayList<StateAFD_DC> states){
        this.states = states;
    }

    public void setInitialState(StateAFD_DC initialState){
        this.initialState = initialState;
    }

    public void setFinalStates(ArrayList<StateAFD_DC> finalStates){
        this.finalStates = finalStates;
    }

    public void setTransitions(ArrayList<TransitionAfd_DC> transitions){
        this.transitions = transitions;
    }

    public ArrayList<String> getSymbols(){
        return symbols;
    }

    public ArrayList<StateAFD_DC> getStates(){
        return states;
    }

    public StateAFD_DC getInitialState(){
        return initialState;
    }

    public ArrayList<StateAFD_DC> getFinalStates(){
        return finalStates;
    }

    public ArrayList<TransitionAfd_DC> getTransitions(){
        return transitions;
    }

    public void refreshAFD(){
        this.searchInitialStates();
        this.searchFinalStates();
        this.findTransitionsForState();
    }

    // Metodo que encuentra las transiciones para cada estado
    private void findTransitionsForState(){
        for (StateAFD_DC s1: states)
            for (StateAFD_DC s2: states)
                for (TransitionAfd_DC t: transitions)
                    if (t.getOrigin().equals(s1) && t.getDestination().equals(s2))
                        s1.addTransition(t.getSymbol(), s2);
    }

    // Metodo para encontrar los estados finales del AFD
    private void searchInitialStates(){
        // Encontrar los estados finales
        for (StateAFD_DC s1: states)
            if (s1.getIsInitial())
                    this.initialState = s1;
    }

    // Metodo para encontrar los estados finales del AFD
    private void searchFinalStates(){
        // Encontrar los estados finales
        for (StateAFD_DC s1: states)
            for (TreeNode s2: s1.getNodes())
                if (s2.getValue().equals("#")){
                    s1.setIsfinal(true);
                    finalStates.add(s1);
                    break;
                }
    }

    // Metodos que retornan los atributos del AFN como tal
    public int getInitialStateToString(){
        return initialState.getLabel();
    }

    public String getFinalStateToString(){
        String output = "";

        for (StateAFD_DC state: finalStates)
            // Si ya tiene valor, agregamos una coma antes del estado. Si no, agregamos solo el estado.
            output += (output.length()>0) ? ("," + state.getLabel()) : state.getLabel();

        return output;
    }

    // Metodo para obtener todas las transiciones
    private ArrayList<String> getTransitionsToString(){
        ArrayList<String> output = new ArrayList<String>();
        String symbolTransitions;

        for (int i = 0; i<symbols.size(); i++) {
            symbolTransitions = "0";

            for (TransitionAfd_DC transition: transitions) {
                if (transition.getSymbol() == symbols.get(i)) {
                    symbolTransitions += "," + String.valueOf(transition.getDestination().getLabel());
                }
            }

            // output.add("(" + String.valueOf(transition.getOrigin().getLabel()) + ", " + transition.getSymbol() + ", " + String.valueOf(transition.getDestination().getLabel()) + ")");
            output.add(symbolTransitions);
        }

        return output;
    }

    // Metodo para obtener la representacion del AFD
    public ArrayList<String> afdDescription(){
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
            HashSet<StateAFD_DC> currentStates = new HashSet();
            HashSet<StateAFD_DC> auxStates  = new HashSet();

            // Move del estado inicial
            auxStates.add(initialState);
            currentStates.addAll(this.move(auxStates, String.valueOf(string.charAt(0))));

            // Recorrer la cadena recibida
            for (int i=1; i<string.length(); i++){
                //System.out.println("Evaluando " + String.valueOf(string.charAt(i)));
                auxStates.clear();
                auxStates.addAll(currentStates);    // Hacer una copia de los estados actuales para el ciclo FOR
                currentStates.clear();      // Vaciar currentStates para poder almacenar los nuevos estados

                // Para los estados actuales, verificar transiciones con el simbolo.
                currentStates.addAll(this.move(auxStates,String.valueOf(string.charAt(i))));
            }

            // Si alguno de los estados finales es de aceptacion, la cadena es aceptada por el AFN
            for (StateAFD_DC s2: currentStates){
                if (s2.getIsfinal())
                    return true;
            }
            return false;
        }

        //Hace las transiciiones de un conjunto de estados con un simbolo. Devuelve los estados alcanzados.
        private HashSet<StateAFD_DC> move(HashSet<StateAFD_DC> states, String string){
            HashSet<StateAFD_DC> moveResult = new HashSet();
            // Verificar para todas las transiciones del estado recibido
            for (StateAFD_DC s1: states){
                for (int i = 0; i<s1.getTransitions().size(); i++){
                    // Si hay una transicion con el simbolo recibido
                    if (s1.getTransitions().get(i).getSymbol().equals(string)){
                        //System.out.println("Transición de " + string + " encontrada.");
                        moveResult.add(s1.getTransitions().get(i).getDestination());
                    }
                }
            }
            return moveResult;
        }
    }

    /*============================================================================
     * ALGORITMO DE HPCROFT PARA LA MINIMIZACION DEL AFD
     =============================================================================*/

    public AFDMinimized_DC minimizeAFD(){
        ArrayList<ArrayList<StateAFD_DC>> P = new ArrayList();
        ArrayList<ArrayList<StateAFD_DC>> Ds;
        ArrayList<ArrayList<ArrayList<StateAFD_DC>>> L = new ArrayList();
        ArrayList<StateAFD_DC> auxL = new ArrayList();
        ArrayList<StateAFD_DC> subset1 = new ArrayList();
        ArrayList<StateAFD_DC> subset2 = new ArrayList();

        // Paso1: La particion P inicial se compone de los conjuntos "estados de aceptacion" y "estados de no aceptacion"
        for (int i=0; i<states.size(); i++)
            if (states.get(i).getIsfinal())
                subset1.add(states.get(i));
            else
                subset2.add(states.get(i));

        // Ingreso de los conjuntos iniciales a la particion
        if (subset2.size() != 0)
            P.add(subset2);
        if (subset1.size() != 0)
            P.add(subset1);

        // Paso 2: Para cada grupo g en la particion P
        ArrayList<StateAFD_DC> g;
        int pointer = 0;
        boolean finish = false;
        while (!finish){
            g = P.get(pointer);
            for (StateAFD_DC s: g){    // Para cada estado s en el grupo g
                Ds = new ArrayList();
                for (String symbol: symbols){      // Para cada simbolo a del alfabeto
                    // t = Transicion(s,a)
                    StateAFD_DC t = new StateAFD_DC();
                    for (TransitionAfd_DC trans: s.getTransitions())
                        if (trans.getSymbol().equals(symbol))
                            t = trans.getDestination();
                    // Para cada grupo h en la particion P
                    for (ArrayList<StateAFD_DC> h: P)
                        if (h.contains(t))  // Si t & h
                            Ds.add(h);      // Agregar h al conjunto Ds
                }
                L.add(Ds);  // Agregar Ds a la lista L
                auxL.add(s);    // Agregar s a la lista auxiliar para L

            }

            ArrayList<ArrayList<StateAFD_DC>> K = new ArrayList();
            int i = 0;          // Sea i = 0
            // Mientras que la lista L no este vacia
            while (!L.isEmpty()){
                Ds = L.get(0);          // Tomar un conjunto Dx en la Lista L
                K.add(new ArrayList());
                K.get(i).add(auxL.get(0));        // Copiar el estado x correspondiente a Dx a un conjunto Ki
                L.remove(0);       // Sacar a Dx de L
                auxL.remove(0);     // Remover su estado correspondiente

                // Para cada conjunto Dy que queda en la lista L
                ArrayList<ArrayList<ArrayList<StateAFD_DC>>> aux = new ArrayList();
                for (ArrayList<ArrayList<StateAFD_DC>> jj: L)
                    aux.add(jj);
                for (ArrayList<ArrayList<StateAFD_DC>> Dy: aux){
                    if (Dy.equals(Ds)){
                        K.get(i).add(auxL.get(aux.indexOf(Dy)));    // Meter el estado y correspondiente a Dy a Ki
                        L.remove(Dy);           // Sacar a Dy de L
                        auxL.remove(auxL.get(aux.indexOf(Dy)));     // Eliminar tambien su estado correspondiente
                    }
                }
                i++;        // i = i+1
            }

            // Si K0 != g entonces
            if (!K.isEmpty())
                if (!K.get(0).equals(g)){
                    int index = P.indexOf(g);
                    P.remove(g);        // Remover g de la particion P
                    for (ArrayList<StateAFD_DC> Ki: K){   // Ingresar todos los Ki en lugar de g en la particion P
                        P.add(index, Ki);
                        index++;
                    }
                }
                // El puntero no cambia si una particion se dividio en mas pariticiones
                else{
                    pointer++;
                    if (pointer == P.size())    // Si el puntero ya llego al final del conjunto particionado, entonces se termina la descomposicion
                        finish = true;
                }
        }   // Volver al paso 2 y reiniciar el ciclo

        // Paso 3: Inicializar un AFD
        AFDMinimized_DC afdMinimized = new AFDMinimized_DC();

        // Paso 4: Para cada grupo J en la partición P agregar un estado nuevo al AFD
        StateAfdMinimized_DC state;
        int i = 0;
        for (ArrayList<StateAFD_DC> J: P){
            // ...agregar un estado nuevo al AFD
            i++;
            state = new StateAfdMinimized_DC(i, false, false);
            state.getStates().addAll(J);

            for (StateAFD_DC s: J){
                // El grupo J que contenga al estado inicial del AFD original generará el estado inicial del nuevo AFD
                if (s.getIsInitial()){
                    state.setIsInitial(true);
                    break;
                }
                /* Cualquier grupo J que contenga estados de aceptación del AFD original generará un estado
                de aceptación del AFD nuevo */
                if (s.getIsfinal()){
                    state.setIsfinal(true);
                    break;
                }
            }
            // Agregar el nuevo estado al AFD minimizado

            afdMinimized.getStates().add(state);
        }


        /* Para cada símbolo α en el alfabeto, habrá una transición del grupo J en la partición a otro
        grupo L en la partición si algún estado en J tiene transición con α a algún estado en L*/


        // Para cada estado del AFD minimizado...
        for (StateAfdMinimized_DC st1: afdMinimized.getStates()){
            // Para cada uno de los estados antiguos que componen el nuevo estado
            for (StateAFD_DC st11: st1.getStates()){
                // Para cada uno de las transiciones que tiene dicho estado
                for (TransitionAfd_DC t: st11.getTransitions()){
                    // Verificar para todos los simbolos
                    for (String symbol: symbols){
                        // Buscar transiciones hacia los otros estados del AFD minimizado
                        for (StateAfdMinimized_DC st2: afdMinimized.getStates()){
                            if (t.getSymbol().equals(symbol) && st2.getStates().contains(t.getDestination())){
                                TransitionAfdMinimized_DC transition = new TransitionAfdMinimized_DC(st1, symbol, st2);
                                if (!afdMinimized.thereIsTransition(transition))        // Si esa transicion ya esta, ya no se agrega
                                    afdMinimized.getTransitions().add(transition);
                            }
                        }
                    }
                }
            }
        }
        afdMinimized.setSymbols(symbols);  // Los simbolos del AFD minimizado son los mismos del AFD original
        afdMinimized.refreshAFD();      // Se encuentra los demas componentes

        return afdMinimized;    // Retorno del AFD minimizado
    }

}
