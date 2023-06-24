/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementaci�n de los algoritmos b�sicos de aut�matas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

/**
 *
 * @author timothy
 */
public class SubAfn {
    private State initialState;
    private State finalState;
    public SubAfn(State initialState, State finalState){
        this.initialState = initialState;
        this.finalState = finalState;
    }

    public State getInitialState(){
        return initialState;
    }
    public State getFinalState(){
        return finalState;
    }
}
