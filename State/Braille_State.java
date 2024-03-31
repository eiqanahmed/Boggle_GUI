package State;

public class Braille_State implements State{
    @Override
    public void current_state(Braille_or_Eng brailleorEng) {
        State.super.current_state();
        brailleorEng.setState(this);

    }
    @Override
    public String toString() {
        return "Braille State";
    }
}
