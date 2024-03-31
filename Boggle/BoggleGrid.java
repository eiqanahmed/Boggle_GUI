package Boggle;

import java.util.*;

public class BoggleGrid {
    /**
     * size of grid
     */
    private int size;
    /**
     * characters assigned to grid
     */
    public char[][] board;
    /**
     * Map containing all the words on the board.
     */
    public Map<String, ArrayList<Position>> allWords = new HashMap<String, ArrayList<Position>>();


    private final String[] dice_small_grid = //dice specifications, for small and large grids
            {"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
                    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW", "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"};
    /**
     * dice used to randomize letter assignments for a big grid
     */
    private final String[] dice_big_grid =
            {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY",
                    "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT", "DHHLOR",
                    "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};

    public BoggleDict Dictionary;


    /* Boggle.BoggleGrid constructor
     * ----------------------
     * @param size  The size of the Boggle grid to initialize
     */
    public BoggleGrid(int size) {
        this.Dictionary = new BoggleDict("./wordlist.txt");
        this.size = size;
        this.board = new char[this.numRows()][this.numCols()];
    }


    /*
     * @return int the number of rows on the board
     */
    public int numRows() {
        return this.size;
    }

    /*
     * @return int the number of columns on the board (assumes square grid)
     */
    public int numCols() {
        return this.size;
    }

    /*
     * @return char the character at a given grid position
     */
    public char getCharAt(int row, int col) {
        return this.board[row][col];
    }

    public String randomizeLetters(int size) {
        StringBuilder letters = new StringBuilder();
        List<String> list;
        if (size == 5) {
            list = Arrays.asList(dice_big_grid);
            Collections.shuffle(list);
            for (String die : list) {
                Random random = new Random();
                int index = random.nextInt(((die.length() - 1) - 0) + 1) + 0;
                letters.append(die.charAt(index));
            }
        } else {
            list = Arrays.asList(dice_small_grid);
            Collections.shuffle(list);
            for (String die : list) {
                Random random = new Random();
                int index = random.nextInt(((die.length() - 1) - 0) + 1) + 0;
                letters.append(die.charAt(index));
            }
        }
        return letters.toString().toLowerCase();
    }

}


