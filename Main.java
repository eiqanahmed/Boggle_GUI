package Boggle;

import Braille.BrailleLetterException;

public class Main {
    public static void main( String[] args ) throws Exception, BrailleLetterException {
        GUI gui = new GUI();
        gui.humanMove();
    }
}
