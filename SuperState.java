import java.util.ArrayList;

public class SuperState {
    private int label;
    private boolean isInitial;
    private boolean isFinal;

    public SuperState(){
        isInitial = false;
        isFinal = false;
        label = -1;
    }

    public void setLabel(int label){
        this.label = label;
    }
    public void setIsInitial(boolean value){
        this.isInitial = value;
    }
    public void setIsfinal(boolean value){
        this.isFinal = value;
    }
    public boolean getIsInitial(){
        return isInitial;
    }
    public boolean getIsfinal(){
        return isFinal;
    }
    public int getLabel(){
        return label;
    }

}
