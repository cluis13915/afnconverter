/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

/**
 *
 * @author timothy
 */
public class TransitionAfd_DC {
    private String symbol;
    private StateAFD_DC origin;
    private StateAFD_DC destination;

    public TransitionAfd_DC(){}
    public TransitionAfd_DC (StateAFD_DC origin, String symbol, StateAFD_DC destination){
        this.origin = origin;
        this.symbol = symbol;
        this.destination = destination;
    }

    public String getSymbol(){
        return symbol;
    }
    public StateAFD_DC getDestination(){
        return destination;
    }
    public StateAFD_DC getOrigin(){
        return origin;
    }

}
