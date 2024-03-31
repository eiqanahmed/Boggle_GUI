package State;

public interface State {
    public default void current_state(){

    }

    void current_state(Braille_or_Eng brailleorEng);
}
