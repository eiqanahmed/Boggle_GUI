package Braille;

import BrailleDictionaryObject.BrailleDictionaryObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BrailleConstructor {
    //Alphabetic representation of character.
    public char character;
    public String word;
    public List<List<String>> translation = new ArrayList<>();
    //Braille representation of character
    public String braille_translation = "";
    //Map holding braille representation of the alphabet
    public Map<Character, List<String>> map = new HashMap<>();
    
    /**
     * Constructor.
     */
    public BrailleConstructor(char character) throws IOException, BrailleLetterException {
        this.map = BrailleDictionaryObject.getsingleobject().getmap();
        this.character_translation(character);
        this.character = character;
    }
    public BrailleConstructor(String word) throws IOException, BrailleLetterException {
        this.map = BrailleDictionaryObject.getsingleobject().getmap();
        this.word = word;
        this.translateLine(this.word);
        this.line_translation();
    }

    /**
     * Translate an ASCII line to a line of Braille.
     * To translate a line, iterate over the line and translate each character.
     * Then, concatenate translated characters to form lines of Braille as List<String> objects.
     * (e.g. The string "ab" should result in -> {"1010", "0010", "0000"}.
     * This corresponds to character a ->  {"10", "00", "00"} concatenated with b ->  {"10", "10", "00"}).
     * Store each line in this.translation (which is a List of List<String> objects).
     *
     * @param input: the ASCII line of text to be translated.
     */
    public void translateLine(String input) throws BrailleLetterException {
        List<String> newStr = new ArrayList<String>();
        String lowercase = input.toLowerCase();
        for (int i = 0; i < input.length(); i++) {
            List<String> val = new ArrayList<String>();
            char c = lowercase.charAt(i);
            if (this.map.containsKey(c)){
                val = translateChar(c);
                if (newStr.size() == 0){
                    newStr.add("");
                    newStr.add("");
                    newStr.add("");
                }
                for (int j = 0; j < val.size(); j++) {
                    newStr.set(j, newStr.get(j) + val.get(j));
                }
            } else {
                this.translation.clear();
                throw new BrailleLetterException("Invalid Character");
            }
        }
        this.translation.add(newStr);
    }
    /**
     * Translate an ASCII character to a single Braille letter with positions as follows
     *      0 3
     *      1 4
     *      2 5
     *
     * Each position should contain a zero or a one.
     *
     * @param c: the ASCII character to be translated.
     * @return a Braille character translation
     */
    public List<String> translateChar(char c) {
        List<String> vals = new ArrayList<String>();
        vals = map.get(c);
        return vals;
    }

    /**
     * Translates character into braille character.
     */
    public void character_translation(char character) {
        for (String component_braille : this.map.get(character)) {
            this.braille_translation = this.braille_translation + "<html>" + component_braille + "<br />";
        }
    }
    public void line_translation() {
        for (List<String> list : this.translation) {
            String braille = "";
            for (String component_braille: list) {
                braille += component_braille;
            }
            this.braille_translation = this.braille_translation + braille;
        }
        System.out.println(this.braille_translation);
    }
    public String toString_str_constructor() {
        String retval = "";
        for (List<String> l: this.translation) {
            retval += l.get(0) + "\n" + l.get(1) + "\n" + l.get(2); //three rows for every one line
            retval += "\n";

        }
        return retval;
    }
}

