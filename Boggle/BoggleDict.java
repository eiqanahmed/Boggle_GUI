package Boggle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

    /**
     * The Boggle.Dictionary 
     * The Boggle.Dictionary will contain lists of words that are acceptable for Boggle
     */
    public class BoggleDict {

        /**
         * set of legal words for Boggle
         */
        private TreeSet<String> legalWords;

        /**
         * Class constructor
         *
         * @param filename the file containing a list of legal words.
         */
        public BoggleDict(String filename) {
            String line = "";
            int wordcount = 0;
            this.legalWords = new TreeSet<String>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(filename));
                while ((line = br.readLine()) != null) {
                    if (line.strip().length() > 0) {
                        legalWords.add(line.strip());
                        wordcount++;
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Initialized " + wordcount + " words in the Boggle.Dictionary.");
            ;
        }

        /*
         * Checks to see if a provided word is in the dictionary.
         *
         * @param word  The word to check
         * @return  A boolean indicating if the word has been found
         */
        public boolean containsWord(String word) {
            String element = word.toLowerCase();
            return this.legalWords.contains(element);
        }

        /*
         * Checks to see if a provided string is a prefix of any word in the dictionary.
         *
         * @param str  The string to check
         * @return  A boolean indicating if the string has been found as a prefix
         */
        public boolean isPrefix(String str) {
            String new_str = str.toLowerCase();
            for (String word: this.legalWords) {
                if (word.startsWith(new_str)) {
                    return true;
                }
            }
            return false;
        }
    }
