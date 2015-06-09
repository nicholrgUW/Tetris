package view;

import java.awt.Color;
import java.awt.Font;

import java.util.List;

import javax.swing.JTextArea;

import model.Block;

/**
 * 
 * @author Robbie Nichols
 * @version Autumn 2014
 */
public class ScorePanel {

    /** Keeps track of the score. */
    private int myScore;
    
    /** The list of frozen block arrays. */
    private List<Block[]> myFrozen;
    
    /** The Score Display. */
    private final JTextArea myScoreDisplay;
    
    /** The number of lines cleared in the current game. */
    private int myLinesCleared;
    
    /** The current number of lines on the board. */
    private int myCurrentLines;
    
    /** Score Panel constructor. */
    public ScorePanel() {
        myScore = 0;
        myLinesCleared = 0;
        myScoreDisplay = new JTextArea();
        
        setup();
    }

    /** Private helper method for constructor. */
    private void setup() {
        final int scoreFontSize = 18;
        myScoreDisplay.setFont(new Font(Font.MONOSPACED, Font.BOLD, scoreFontSize));
        myScoreDisplay.setEditable(false);
        myScoreDisplay.setEnabled(false);
        myScoreDisplay.setDisabledTextColor(Color.BLACK);
        myScoreDisplay.setOpaque(false);
        updateScore();
    }    
    
    /** Updates the text in the Score Display. */
    private void updateScore() {
        myScoreDisplay.setText(String.format("Score: %d\nLines: %d\nLevel: %d", myScore, 
                                             myLinesCleared, myLinesCleared 
                                             / (int) GUI.DEFAULT_GRID.getHeight() + 1));
    }

    /**
     * Returns the Score Display.
     * 
     * @return The Score Display.
     */
    public JTextArea getScoreDisplay() {       
        return myScoreDisplay;
    }

    
    /**
     * Tallies the score from lines cleared.
     */
    public void tallyLineClear() {
        final int[] linePts = {40, 100, 300, 1200};
        final int size = myFrozen.size();
        final int maxLinesCleared = 4;  //can't clear more than 4 lines at once in this version
                                        //of Tetris.        
        if (size < myCurrentLines) {
            final int lines = myCurrentLines - size;
            final int level = myLinesCleared / (int) GUI.DEFAULT_GRID.getHeight() + 1;
            myLinesCleared += lines;
            for (int i = 0; i < maxLinesCleared; i++) {
                if (lines == i + 1) {
                    myScore += level * linePts[i];
                }
            }
        }
        updateScore();
        myCurrentLines = size;
    }
    
    /**
     * 
     */
    public void reset() {
        myScore = 0;
        myLinesCleared = 0;
        myCurrentLines = 0;
        updateScore();
    }

    
    /**
     * 
     * @return the number of lines that have been cleared this game.
     */
    public int getLinesCleared() {
        return myLinesCleared;
    }
    
    /**
     * 
     * @param theList The list of frozen block arrays.
     */
    public void setFrozen(final List<Block[]> theList) {
        myFrozen = theList;
    }
}
