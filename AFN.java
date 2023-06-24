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
public class AFN{
    private ArrayList<String> symbols;
    private ArrayList<State> states;
    private State initialState;
    private ArrayList<State> finalStates;
    private int cont = 0;
    private ArrayList<Transition> transitions;

    // Método Constructor
    public AFN(){}

    // Metodos de entrada y acceso
    public void setSymbols(ArrayList<String> symbols){
        this.symbols = symbols;
    }
    public void setStates(ArrayList<State> states){
        this.states = states;
    }
    public void setInitialState(State initialState){
        this.initialState = initialState;
    }
    public void setFinalStates(ArrayList<State> finalStates){
        this.finalStates = finalStates;
    }
    public ArrayList<String> getSymbols(){
        return symbols;
    }
    public ArrayList<State> getStates(){
        return states;
    }
    public State getInitialState(){
        return initialState;
    }
    public ArrayList<State> getFinalStates(){
        return finalStates;
    }
    public void setTransitions(ArrayList<Transition> transitions){
        this.transitions = transitions;
    }
    public ArrayList<Transition> getTransitions(){
        return transitions;
    }

    // Metodos que retornan los atributos del AFN como tal
    public int getInitialStateToString(){
        return initialState.getLabel();
    }
    public ArrayList<Integer> getFinalStateToString(){
        ArrayList<Integer> output = new ArrayList<Integer>();
        for (SuperState state: finalStates)
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
        for (Transition transition: transitions)
            output.add("(" + String.valueOf(transition.getOrigin().getLabel()) + ", " + transition.getSymbol() + ", " + String.valueOf(transition.getDestination().getLabel()) + ")");
        return output;
    }

    // Metodo para obtener el afn completo
    public ArrayList<String> afnDescription(){
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
        for (State s: states){
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
        for (Transition t: transitions)
            output.add(String.valueOf(t.getOrigin().getLabel()) + " -> " + String.valueOf(t.getDestination().getLabel()) + " [label=\"" + t.getSymbol() + "\"];");
        output.add("}");
        return output;
    }

    // Metodo para simular el AFN
    public boolean simulateAFN(String string){
        Simulater simulater = new Simulater();
        boolean accept = simulater.simulateAFN(string);
        return accept;
    }

    // Metodo para convertir el AFN a un AFD por Construccion de Subconjuntos
    public AFD covertToAFD(){
        Converter converter = new Converter();
        AFD afd = converter.afnToAfd(this);
        return afd;
    }


    /*============================================================================
     * Clase enbebida que posee todas las herramientas para la simulacion del AFN
     =============================================================================*/

    private class Simulater{
        private HashSet<State> statesVisited;
        private HashSet<State> currentStates;
        private HashSet<State> statesTpDelete;

        public Simulater(){
            statesVisited = new HashSet();
            currentStates = new HashSet();
            statesTpDelete = new HashSet();
        }

        // ************* Simulacion del AFN ************* //
        public boolean simulateAFN(String string){
            HashSet<State> auxStates  = new HashSet<State>();

            // Hacer E-Closure del estado inicial
            auxStates.add(initialState);
            currentStates.addAll(this.eClosure(auxStates, new HashSet<State>()));

            // Recorrer la cadena recibida
            for (int i=0; i<string.length(); i++){
                //System.out.println("Evaluando " + String.valueOf(string.charAt(i)));
                auxStates.clear();
                auxStates.addAll(currentStates);    // Hacer una copia de los estados actuales para el ciclo FOR
                currentStates.clear();      // Vaciar currentStates para poder almacenar los nuevos estados

                // Para los estados actuales, verificar transiciones con el simbolo.
                currentStates.addAll(this.move(auxStates,String.valueOf(string.charAt(i))));

                // Guardar los nuevos estados en la variable auxiliar para hacer el e-Closure
                auxStates.clear();
                auxStates.addAll(currentStates);
                // Guardar los nuevos estados en currentStates
                currentStates.clear();
                currentStates.addAll(this.eClosure(auxStates, new HashSet<State>()));
            }

            // Si alguno de los estados finales es de aceptacion, la cadena es aceptada por el AFN
            for (State s2: currentStates){
                if (s2.getIsfinal())
                    return true;
            }
            return false;
        }


        //Hace las transiciiones de un estado con un simbolo. Devuelve los estados alcanzados.
        private HashSet<State> move(HashSet<State> states, String string){
            HashSet<State> moveResult = new HashSet<State>();
            // Verificar para todas las transiciones del estado recibido
            for (State s1: states){
                for (int i = 0; i<s1.getTransitions().size(); i++){
                    // Si hay una transicion con el simbolo recibido
                    if (s1.getTransitions().get(i).getSymbol().equals(string)){
                        //System.out.println("Transición de " + string + " encontrada.");
                        statesVisited.add(s1.getTransitions().get(i).getDestination());
                        moveResult.add(s1.getTransitions().get(i).getDestination());
                    }
                }
            }
            return moveResult;
        }


        // Metodo para hacer el e-Closure de un conjunto de estados recibidos. Devuelve los estados alcanzados.
        private HashSet<State> eClosure(HashSet<State> newStates, HashSet<State> eClosureResult){
            HashSet<State> auxNewStates = new HashSet<State>();
            // Hacer e-Closure para los estados recibidos
            for (State state: newStates){
                //System.out.println("Haciendo e-closure de: " + state.getLabel());
                // Verificar para todas las transiciones del estado actual
                if (state.getTransitions().size() == 0)
                    eClosureResult.add(state);
                else {
                    for (int i=0; i<state.getTransitions().size(); i++){
                        //System.out.println("    Transicion de _" + state.getLabel() + "_ con _" + state.getTransitions().get(i).getDestination().getLabel() + "_");
                        statesVisited.add(state);
                        // Si la transicion es con Epsilon
                        if (state.getTransitions().get(i).getSymbol().equals("&")){
                            //System.out.println("        Transicion con & encontrado...");
                            auxNewStates.add(state.getTransitions().get(i).getDestination());   // Agregar el nuevo estado alcanzado
                            this.eClosure(auxNewStates, eClosureResult);        // Hacer e-Closure de este estado
                        }
                        // Si el estado no tiene transicion con Epsilon, es un estado final.
                        else {
                            eClosureResult.add(state);
                        }
                    }
                }
            }
            return eClosureResult;
        }
    }

    /*============================================================================
     * Clase enbebida que convierte el AFN a AFD
     =============================================================================*/

    private class Converter {

        // Conjunto de estados visitados
        private HashSet<State> statesVisited;

        public Converter(){
            statesVisited = new HashSet<State>();
        }

        //************** CONVERSION DE UN AFN A UN AFD **************
        public AFD afnToAfd(AFN afn){
            AFD afd = new AFD();
            //Estructuras de almacenamiento
            HashSet<State> auxStates1  = new HashSet<State>();
            ArrayList<StateAFD> dStates = new ArrayList<StateAFD>();
            StateAFD U = new StateAFD(1, true, false);
            //Variables auxiliares
            String s;
            int cont = 1;

            afd.setSymbols(afn.getSymbols());       // Los simbolos del afd seran los mismos del afn
            auxStates1.add(afn.getInitialState());      // Estado inicial

            //System.out.print("\neClosure del estado inicial: ");
            //this.print(this.eClosure2(auxStates1, new HashSet<State>()));

            //El estado inicial del afd sera el eClosure del estado inicial del afn
            U.setSetStates(this.eClosure2(auxStates1, new HashSet<State>()));
            U.setIsMarked(false);
            U.setIsInitial(true);
            dStates.add(U);

            boolean isUnmarked = true;
            int id = 0;
            //Ciclo para la conversion del afn
            while (isUnmarked){
                //System.out.println("\nNueva iteracion...");

                //Verificar las transiciones para el conjunto de estados actuales
                for (int i=0; i<afd.getSymbols().size(); i++){
                    s = afn.getSymbols().get(i);
                    U = new StateAFD(0, false, false);
                    //U = eClosure(move(T,s))
                    //System.out.print("\nMove de U ");
                    //this.print(this.eClosure2(move(dStates.get(id).getSetStates(),s), new HashSet<State>()));
                    U.setSetStates(this.eClosure2(move(dStates.get(id).getSetStates(),s), new HashSet<State>()));
                    U.setLabel(cont+1);

                    //Verificar si el nuevo estado no esta en dStates
                    boolean contains = false;
                    for (int j=0; j<dStates.size(); j++){
                        if (dStates.get(j).getSetStates().equals(U.getSetStates())){
                            contains = true;
                            dStates.get(id).addTransition(s, dStates.get(j));
                            break;
                        }
                    }

                    // Si U no esta en dStates lo agregamos
                    if (!contains){
                        //System.out.println("dStates no contiene al estado " + U.getLabel());
                        U.setIsMarked(false);
                        cont++;
                        dStates.add(U);
                        //Dtran[T,s] = U. U se inserto en la ultima posicion de dStates.
                        dStates.get(id).addTransition(s, dStates.get(dStates.size()-1));
                    }
                    //else{
                        //System.out.println("dStates ya contiene al estado " + U.getLabel());
                    //}
                }

                //Cuando ya se hayan hecho todas las transiciones, marcar el nuevo estado del afd
                dStates.get(id).setIsMarked(true);

                // Busqueda de un estado no marcado
                //System.out.println("Lenght de dStates: " + dStates.size());
                isUnmarked = false;
                for (int i=0; i<dStates.size(); i++){
                    //System.out.println("getIsMarked de " + dStates.get(i).getLabel() + ": " + dStates.get(i).getIsMarked());
                    if (!dStates.get(i).getIsMarked()){
                        //System.out.println("El estado " + dStates.get(i).getLabel() + " esta sin marcar.");
                        id = i;
                        isUnmarked = true;
                        break;
                    }
                }
                //System.out.println("Estado isUnmarked: " + isUnmarked);
            }

            //Agregar todos los estados del afd
            afd.setStates(dStates);
            //Buscar los demas componentes del afd
            afd.refreshAFD();
            //Retornar el AFD
            return afd;
        }

        //Hace las transiciiones de un estado con un simbolo. Devuelve los estados alcanzados.
        private HashSet<State> move(HashSet<State> states, String string){
            HashSet<State> moveResult = new HashSet<State>();
            // Verificar para todas las transiciones del estado recibido
            for (State s1: states){
                for (int i = 0; i<s1.getTransitions().size(); i++){
                    // Si hay una transicion con el simbolo recibido
                    if (s1.getTransitions().get(i).getSymbol().equals(string)){
                        //System.out.println("Transición de " + string + " encontrada.");
                        statesVisited.add(s1.getTransitions().get(i).getDestination());
                        moveResult.add(s1.getTransitions().get(i).getDestination());
                    }
                }
            }
            return moveResult;
        }

        // Metodo para hacer el e-Closure de un conjunto de estados recibidos. Devuelve los estados alcanzados.
        private HashSet<State> eClosure2(HashSet<State> newStates, HashSet<State> eClosureResult){
            HashSet<State> auxNewStates = new HashSet<State>();
            // Hacer e-Closure para los estados recibidos
            for (State state: newStates){
                eClosureResult.add(state);
                //System.out.println("Haciendo e-closure de: " + state.getLabel());
                // Verificar para todas las transiciones del estado actual
                if (state.getTransitions().size() == 0)
                    eClosureResult.add(state);
                else {
                    for (int i=0; i<state.getTransitions().size(); i++){
                        //System.out.println("    Transicion de _" + state.getLabel() + "_ con _" + state.getTransitions().get(i).getDestination().getLabel() + "_");
                        statesVisited.add(state);
                        // Si la transicion es con Epsilon
                        if (state.getTransitions().get(i).getSymbol().equals("&")){
                            //System.out.println("        Transicion con & encontrado...");
                            auxNewStates.add(state.getTransitions().get(i).getDestination());   // Agregar el nuevo estado alcanzado
                            this.eClosure2(auxNewStates, eClosureResult);        // Hacer e-Closure de este estado
                        }
                        // Si el estado no tiene transicion con Epsilon, es un estado final.
                        else {
                            eClosureResult.add(state);
                        }
                    }
                }
            }
            return eClosureResult;
        }
    }
}

