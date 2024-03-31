package BrailleDictionaryObject;

import Braille.BrailleConstructor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BrailleDictionaryObject {
    private static BrailleDictionaryObject brailleDictionaryObject = new BrailleDictionaryObject();
    private Map<Character, List<String>> map;
    private void BrailleDionaryObject() throws IOException {
    }
    private void initializeMap() throws IOException {
        String line;
        String filename = "./BrailleDictionary.txt";
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null){
                if (Character.toString(line.charAt(1)).isBlank() == false) {
                    line = line.replaceAll(" ", "");
                }
                else{
                    line = " " + line.replaceAll(" ", "");
                }
                char character = line.charAt(0);
                List<String> ASCII_REP = new ArrayList<>(Arrays.asList((line.replace(Character.
                        toString(character), "").replaceFirst(",", "")).split(",")));
                this.map.put(character, ASCII_REP);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static BrailleDictionaryObject getsingleobject() throws IOException {
        if (brailleDictionaryObject.map == null)
            brailleDictionaryObject.map = new HashMap<>();
            brailleDictionaryObject.initializeMap();
        return brailleDictionaryObject;
    }
    public Map<Character, List<String>> getmap(){
        return this.map;
    }


}
