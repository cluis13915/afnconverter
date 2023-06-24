/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

/**
 *
 * @author timothy
 */
public class TransitionAfdMinimized {
    private String symbol;
    private StateAfdMinimized origin;
    private StateAfdMinimized destination;

    public TransitionAfdMinimized(){}
    public TransitionAfdMinimized (StateAfdMinimized origin, String symbol, StateAfdMinimized destination){
        this.origin = origin;
        this.symbol = symbol;
        this.destination = destination;
    }

    public String getSymbol(){
        return symbol;
    }
    public StateAfdMinimized getDestination(){
        return destination;
    }
    public StateAfdMinimized getOrigin(){
        return origin;
    }
    public String toString(){
        return "("+ String.valueOf(origin.getLabel()) + ", " + symbol + ", " + String.valueOf(destination.getLabel()) + ")";
    }

}
