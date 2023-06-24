/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementaci�n de los algoritmos b�sicos de aut�matas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

/**
 *
 * @author timothy
 */
public class TransitionAfd {
    private String symbol;
    private StateAFD origin;
    private StateAFD destination;

    public TransitionAfd(){}
    public TransitionAfd (StateAFD origin, String symbol, StateAFD destination){
        this.origin = origin;
        this.symbol = symbol;
        this.destination = destination;
    }

    public String getSymbol(){
        return symbol;
    }
    public StateAFD getDestination(){
        return destination;
    }
    public StateAFD getOrigin(){
        return origin;
    }

}
