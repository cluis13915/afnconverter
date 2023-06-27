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
