/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

/**
 *
 * @author timothy
 */
public class Transition {
    private String symbol;
    private State origin;
    private State destination;

    public Transition(){}
    public Transition (State origin, String symbol, State destination){
        this.origin = origin;
        this.symbol = symbol;
        this.destination = destination;
    }

    public String getSymbol(){
        return symbol;
    }
    public State getDestination(){
        return destination;
    }
    public State getOrigin(){
        return origin;
    }
}
