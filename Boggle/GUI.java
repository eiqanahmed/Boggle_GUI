package Boggle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import Braille.BrailleConstructor;
import Braille.BrailleLetterException;
import State.Braille_State;
import State.Eng_Alpha_State;
import State.Braille_or_Eng;

public class GUI implements ActionListener {
    private String attempt = "";
    private Braille_or_Eng braille_or_eng = new Braille_or_Eng();
    private Braille_State braille_state = new Braille_State();
    private Eng_Alpha_State eng_alpha_state = new Eng_Alpha_State();
    private ArrayList<String> foundWords = new ArrayList<String>();
    
    private JFrame guiframe;
    
    private JLabel title;
    private JLabel scoreLabel;
    private JLabel messagesLabel;
    
    private JButton restart;
    private JButton endTurn;
    private JButton brailleBoard;
    private JButton newAttempt;
    private JButton Submit;
    private JButton[][] gridButtons;
    
    private JTextArea Attempts;
    private JTextArea addedWordsArea;
    
    private JPanel GUIgrid;
    
    private final int BOARD_SIZE = 4;
    private BoggleGrid grid;
    private int score = 0;
    
    private int lastRow = -1;
    private int lastColumn = -1;
    private boolean[][] used;
    
    private BoggleStats gamestats;
    private BrailleConstructor braille;

    /**
     * Constructor
     */
    public GUI() {
        this.gamestats = new BoggleStats();
    }

    /**
     * Runs the game for the user to play.
     */
    public void humanMove() throws IOException, BrailleLetterException{

        guiframe = new JFrame("Boggle");
        guiframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.eng_alpha_state.current_state(this.braille_or_eng);
        game(guiframe.getContentPane());
        guiframe.setVisible(true);
        guiframe.setSize( 800, 600 );
        guiframe.setResizable(false);
    }
    
    /**
     * Sets up GUI
     * @param pane shows the GUI
     */
    public void game(Container pane) throws IOException, BrailleLetterException{
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 0.5;

        c.weightx = 0.25;
        c.gridx = 0;

        title = new JLabel("Boggle");
        c.gridy = 0;
        pane.add(title, c);
        
        scoreLabel = new JLabel("Score: 0");
        c.gridy = 1;
        pane.add(scoreLabel, c);
        
        messagesLabel = new JLabel("Enter word: ");
        c.gridy = 2;
        c.fill = GridBagConstraints.VERTICAL;
        pane.add(messagesLabel, c);
        c.fill = GridBagConstraints.NONE;
        
        restart = new JButton("Restart");
        restart.addActionListener(this);

        c.gridy = 3;
        pane.add(restart, c);
        
        endTurn = new JButton("End Turn");
        endTurn.addActionListener(this);

        c.gridy = 4;
        pane.add(endTurn, c);
        
        brailleBoard = new JButton("Braille");
        brailleBoard.addActionListener(this);

        c.gridy = 5;
        pane.add(brailleBoard, c);
        
        c.gridx = 1;
        c.weightx = 0.75;
        
        // Need to find all words in the boggle grid (call findAllWords somewhere)
        GUIgrid = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        gridButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
        grid = new BoggleGrid(BOARD_SIZE);
        this.used = new boolean[grid.numCols()][grid.numRows()];
        String letters = grid.randomizeLetters(BOARD_SIZE);
        int curr = 0;
        for(int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                grid.board[i][j] = letters.charAt(curr);
                curr++;
            }
        }
        
        findAllWords(grid.allWords, grid.Dictionary, grid);
        System.out.println(grid.allWords.keySet());
        
        if (this.braille_or_eng.state.toString() == "Eng Alpha State") {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    gridButtons[i][j] = new JButton(Character.toString(grid.getCharAt(i, j)));
                    gridButtons[i][j].addActionListener(this);
                    GUIgrid.add(gridButtons[i][j]);
                }
            }
        }
        else{
            for( int i = 0; i < BOARD_SIZE; i++ )
            {
                for( int j = 0; j < BOARD_SIZE; j++ )
                {
                    BrailleConstructor new_braille_word = new BrailleConstructor(grid.getCharAt( i, j ));
                    gridButtons[i][j] = new JButton(new_braille_word.braille_translation);
                    gridButtons[i][j].addActionListener( this );
                    GUIgrid.add(gridButtons[i][j]);
                }
            }
        } 
        
        c.gridx = 1;
        c.gridheight = 3;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        pane.add(GUIgrid, c);
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        
        newAttempt = new JButton("New word");
        newAttempt.addActionListener(this);
        c.gridy = 4;
        pane.add(newAttempt, c);

        Attempts = new JTextArea(1, 1);
        Attempts.setEditable(false);
        Attempts.setBorder(BorderFactory.createEtchedBorder());
        
        c.gridy = 5;
        c.fill = GridBagConstraints.BOTH;
        pane.add(Attempts, c);
        c.fill = GridBagConstraints.NONE;
        
        Submit = new JButton("Submit");
        Submit.addActionListener(this);

        c.gridx = 1;
        c.gridy = 6;
        pane.add(Submit, c);
        
        c.gridx = 2;
        c.weightx = 0.25;

        JLabel ftitle = new JLabel("Found Words:");
        c.gridy = 1;
        pane.add(ftitle, c);
        
        addedWordsArea = new JTextArea(10, 16);
        addedWordsArea.setEditable(false);
        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 2;
        Insets in = new Insets(25, 25, 25, 25);
        addedWordsArea.setMargin(in);
        addedWordsArea.setBorder(BorderFactory.createEtchedBorder());
        pane.add(addedWordsArea, c);
    }

/**
     * listens for specific user actions
     * @param e ActionEvent that occurred
     */
public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    
    if((JButton)source == endTurn)
        {
            computerMove();
            this.gamestats.summarizeRound();
            System.exit(0);
    }
    else if((JButton)source == restart){
            String message = "Restart?";

            int n = JOptionPane.showConfirmDialog(
                    guiframe,
                    message,
                    "Restart game",
                    JOptionPane.YES_NO_OPTION );
            if(n == JOptionPane.YES_OPTION ){
                try {
                    restart();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (BrailleLetterException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } 
    
    else if((JButton)source == newAttempt)
        {
            reset();
        }
    
    else if((JButton)source == Submit)
        {
            if( attempt.equals("") )
            {
                messagesLabel.setText("Enter a word!");
            }
            else if( found(attempt) )
            {
                messagesLabel.setText("You have already used " + attempt + "!");
            }
            else
            {
                if(attempt.length() < 3)
                {
                    messagesLabel.setText("Attempt must be longer 3 letters.");
                }
                else
                {
                    if(grid.allWords.containsKey(attempt)) {
                        if (attempt.length() < 3) {
                            JOptionPane.showMessageDialog(null, "Invalid Word");
                        } else {
                            score += attempt.length() - 2;
                            scoreLabel.setText("Score: " + Integer.toString(score));
                            this.gamestats.addWord(attempt, BoggleStats.Player.Human);


                            messagesLabel.setText(attempt + " is a word!");
                            if (this.braille_or_eng.state.toString() == "Eng Alpha State") {
                                addedWordsArea.insert(attempt + "\n", 0);
                            }
                            else{
                                try {
                                    String new_attempt = new BrailleConstructor(attempt).toString_str_constructor()  + "--------------------------------------------------\n";
                                    addedWordsArea.insert(new_attempt + "\n", 0);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                } catch (BrailleLetterException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                            foundWords.add(attempt);
                        }
                    }
                    else
                    {
                        messagesLabel.setText(attempt + " is not a word!");
                    }
                }
            }
            reset();

        }
    
    if ((JButton)source == brailleBoard){
            guiframe.dispose();
            guiframe = new JFrame("Braille Boggle");
            guiframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.braille_state.current_state(this.braille_or_eng);
            try {
                game(guiframe.getContentPane());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (BrailleLetterException ex) {
                throw new RuntimeException(ex);
            }

            guiframe.setVisible(true);
            guiframe.setSize(800, 600);
            guiframe.setResizable(false);
        }
    for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if( (JButton)source == gridButtons[i][j]){
                    if(((lastRow != -1 && lastColumn != -1 ) && (Math.abs( lastRow - i) <= 1 && Math.abs(lastColumn - j) <= 1 && !used[i][j]))
                            || (lastRow == -1 && lastColumn == -1)) {
                        Attempts.insert( Character.toString( grid.getCharAt(i, j )), attempt.length());
                        attempt += grid.getCharAt(i, j);
                        lastRow = i;
                        lastColumn = j;
                        used[i][j] = true;
                        gridButtons[i][j].setBackground(new Color( 255, 0, 0));
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Invalid Move!");
                    }
                    break;
                }
            }
        }

    }
/* Check to see if the user has already entered a word twice
     * @param string word to check for
     * @return boolean indicating if the word already exists or not
     */
    
public boolean found(String string){
        for( String str: foundWords) {
            if (string.equals(str))
                return true;
        }

        return false;
    }  

/**
     * Restarts game
     */
public void restart() throws IOException, BrailleLetterException {
    foundWords.clear();
    addedWordsArea.setText("");

    score = 0;
    scoreLabel.setText("Score: 0");

    messagesLabel.setText("Enter word: ");

    reset();

    String message = String.format("A new board wll be generated.");
    int n = JOptionPane.showConfirmDialog(
            guiframe,
            message,
            "New Board",
            JOptionPane.YES_NO_OPTION);
    if (n == JOptionPane.YES_OPTION) {
        grid = new BoggleGrid(BOARD_SIZE);
        String letters = grid.randomizeLetters(BOARD_SIZE);
        int curr = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                grid.board[i][j] = letters.charAt(curr);
                curr++;
            }
        }
        findAllWords(grid.allWords, grid.Dictionary, grid);
        System.out.println(grid.allWords.keySet());
        if (this.braille_or_eng.state.toString() == "Eng Alpha State") {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    gridButtons[i][j].setText(Character.toString(grid.getCharAt(i, j)));
                }
            }
        }
        else{
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    BrailleConstructor new_braille_word = new BrailleConstructor(grid.getCharAt( i, j ));
                    gridButtons[i][j].setText(new_braille_word.braille_translation);
                }
            }
        }
    }
}
    
   /**
     * Resets current attempt to nothing.
     */ 
public void reset() {
    lastRow = -1;
    lastColumn = -1;

    attempt = "";
    Attempts.setText("");

    used = new boolean[BOARD_SIZE][BOARD_SIZE];
    for(int i = 0; i < BOARD_SIZE; i++)
    {
        for(int j = 0; j < BOARD_SIZE; j++)
        {
            gridButtons[i][j].setBackground(restart.getBackground());
        }
    }
}
    
private void findAllWords(Map<String, ArrayList<Position>> allWords, BoggleDict boggleDict, BoggleGrid boggleGrid) {
        HashSet<Position> pl = new HashSet<>();
        String str = "";
        boolean[][] exists = new boolean[boggleGrid.numRows()][boggleGrid.numCols()];
        for (int row = 0; row < boggleGrid.numRows(); row++) {
            for (int col = 0; col < boggleGrid.numCols(); col++) {
                findAllWordshelper(pl, allWords, boggleDict, boggleGrid, row, col, str, exists);
            }

        }
    }

    private void findAllWordshelper(HashSet<Position> p_list, Map<String, ArrayList<Position>> allWords, BoggleDict boggleDict, BoggleGrid boggleGrid, int i, int j, String str, boolean exists[][]) {
        Position Point = new Position(i, j);
        exists[i][j] = true;
        p_list.add(Point);
        str = str + boggleGrid.getCharAt(i, j);
        if (boggleDict.isPrefix(str.toLowerCase())) {

            if (boggleDict.containsWord(str.toLowerCase()) && str.length() >= 4) {
                if (!allWords.containsKey(str.toUpperCase())) {
                    ArrayList<Position> list = new ArrayList<Position>(p_list);
                    allWords.put(str, list);

                }
            }
            if (boggleDict.isPrefix(str.toLowerCase())) {
                for (int row = i - 1; row <= i + 1 && row < boggleGrid.numRows(); row++) {
                    for (int col = j - 1; col <= j + 1 && col < boggleGrid.numCols(); col++) {
                        if (row >= 0 && col >= 0 && !exists[row][col]) {
                            findAllWordshelper(p_list, allWords, boggleDict, boggleGrid, row, col, str, exists);
                        }
                    }
                }
            }

        }
        str = "";
        exists[i][j] = false;
    }
                       
    public void computerMove() {
        for (String word: grid.allWords.keySet()) {
            if (!this.gamestats.getPlayerWords().contains(word)) {
                this.gamestats.addWord(word, BoggleStats.Player.Computer);
            }
        }
    }
}
