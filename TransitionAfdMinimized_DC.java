/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

/**
 *
 * @author timothy
 */
public class TransitionAfdMinimized_DC {
    private String symbol;
    private StateAfdMinimized_DC origin;
    private StateAfdMinimized_DC destination;

    public TransitionAfdMinimized_DC(){}
    public TransitionAfdMinimized_DC (StateAfdMinimized_DC origin, String symbol, StateAfdMinimized_DC destination){
        this.origin = origin;
        this.symbol = symbol;
        this.destination = destination;
    }

    public String getSymbol(){
        return symbol;
    }
    public StateAfdMinimized_DC getDestination(){
        return destination;
    }
    public StateAfdMinimized_DC getOrigin(){
        return origin;
    }
    public String toString(){
        return "("+ String.valueOf(origin.getLabel()) + ", " + symbol + ", " + String.valueOf(destination.getLabel()) + ")";
    }

}
