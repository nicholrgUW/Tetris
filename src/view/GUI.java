/* TCSS 305 - Project Tetris */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Block;
import model.Board;
import model.Piece;

/**
 * The GUI that contains and organizes all the visual and action components.
 * 
 * @author Robbie Nichols eibbor08@uw.edu
 * @version Autumn 2014
 *
 */
public class GUI implements Observer, KeyListener {
    
    /** Default grid size for a game of Tetris. */
    protected static final Dimension DEFAULT_GRID = new Dimension(10, 20);
    
    /** Default timer interval. */
    private static final int DEFAULT_TIMER = 750;
    
    /** Default timer decrement. */
    private static final int DEFAULT_TIMER_DEC = 100;

    /** Minimum size the game can be shrunk to. */
    private static final Dimension MIN_SIZE = new Dimension(420, 480);
    
    /** The game board display. */
    private final GameBoardPanel myGameBoard;
    
    /** The preview panel. */
    private final PreviewPanel myPreviewPanel;
    
    /** The game timer. */
    private final Timer myGameTimer;
    
    /** The JFrame container for all other components. */
    private final JFrame myFrame;
    
    /** The actual game board from the model package. */
    private final Board myBoard;
    
    /** Displays and tallies the current score. */
    private final ScorePanel myScorePanel;
    
    /** The menu bar class, not the menu itself. */
    private final MenuBar myMenu;
    
    /** Boolean indicating whether or not the game is paused. */
    private boolean myIsPaused;
    
    /** The current timer decrement for level changes. */
    private int myCurrentDec;
    
    /** GUI constructor. */
    public GUI() {
        myBoard = new Board(); //default constructor
        myGameBoard = new GameBoardPanel();
        myPreviewPanel = new PreviewPanel();
        myScorePanel = new ScorePanel();
        myGameTimer = new Timer(DEFAULT_TIMER, new TimerAction());
        myFrame = new JFrame("Tetris: Autumn 2014");
        myMenu = new MenuBar(myFrame);
        myIsPaused = false;     
        myCurrentDec = DEFAULT_TIMER_DEC;
        setup();
    }

    /** Private helper method for the constructor. */
    private void setup() {

        myFrame.setMinimumSize(MIN_SIZE);

        myFrame.setJMenuBar(myMenu.getTheMenu());
        
        final JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(myScorePanel.getScoreDisplay(), BorderLayout.NORTH);
        sidePanel.add(myPreviewPanel, BorderLayout.CENTER);

        myFrame.setLayout(new FlowLayout());
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.add(myGameBoard);        
        myFrame.add(sidePanel);
        myFrame.addKeyListener(this);
        myGameTimer.start();    
        
        final FocusListener focus = new FocusListener() {
            @Override
            public void focusGained(final FocusEvent theArg) {
                //does nothing
            }
            @Override
            public void focusLost(final FocusEvent theArg) {
                if (!myIsPaused) {
                    togglePause();
                }            
            }            
        };
        
        final ComponentAdapter componentAdapter = new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent theEvent) {
                final Dimension temp = myFrame.getSize();
                myGameBoard.resizeTetrisBoard(temp.width, temp.height);    
            }
        };
        final WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowStateChanged(final WindowEvent theEvent) {
                final Dimension temp = myFrame.getSize();
                myGameBoard.resizeTetrisBoard(temp.width, temp.height);
            }
        };
        myFrame.addFocusListener(focus);
        myFrame.addWindowStateListener(windowAdapter);
        myFrame.addComponentListener(componentAdapter);
        myBoard.addObserver(this);     
        myMenu.addObserver(this);
        final Dimension temp = myFrame.getSize();
        myGameBoard.resizeTetrisBoard(temp.width, temp.height);
        
        myFrame.pack();     
        myFrame.setVisible(true);
    }

    /**
     * Updates the GUI when the Observable (the model package) dispatches an Observable 
     * object.
     * 
     * @param theObservable The Observable received from the model package.
     * @param theObject The Object received from the model package.
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if ("MenuBar".equals(theObservable.getClass().getSimpleName())) {
            if ("java.lang.Boolean".equals(theObject.getClass().getTypeName())) {
                menuUpdate((boolean) theObject);
            } else if ("java.lang.Integer".equals(theObject.getClass().getTypeName())) {
                modeUpdate((int) theObject);
            }
            
        }
        if (myBoard.isGameOver()) {
            myGameBoard.displayOver(true);
            myGameTimer.stop();
        } else if (!myMenu.isGameEnded()) {
            if (!myGameTimer.isRunning()) {
                myGameBoard.displayOver(false);
            }
            myPreviewPanel.setPreview(myBoard.getNextPiece());
            myPreviewPanel.repaint();
            
            if (myBoard.getCurrentPiece() != null) {
                final Piece newPiece = myBoard.getCurrentPiece();
                myGameBoard.setCurrentPiece(newPiece);                
            }

            if (myBoard.getFrozenBlocks() != null) {
                final List<Block[]> newFrozen = myBoard.getFrozenBlocks();
                myGameBoard.setFrozen(newFrozen);
                myScorePanel.setFrozen(newFrozen);
                myGameBoard.repaint();
            }
        }
        myScorePanel.tallyLineClear();
        accelerateTimer();
    }
    
    /**
     * Changes the game mode.
     * 
     * @param theMode Int representing the game mode.
     */
    private void modeUpdate(final int theMode) {
        final int easy = 1000;
        final int killer = 300;
        if (MenuBar.GAME_PEACEFUL == theMode) {
            myGameTimer.setDelay(easy);
            myCurrentDec = 0;
        } else if (MenuBar.GAME_NORMAL == theMode) {
            myGameTimer.setDelay(DEFAULT_TIMER);
            myCurrentDec = DEFAULT_TIMER_DEC;
        } else if (MenuBar.GAME_HARD == theMode) {
            myGameTimer.setDelay(killer);
            myCurrentDec = DEFAULT_TIMER_DEC;
        }
    }
    
    /**
     * Updates the menu if the game has been ended.
     * 
     * @param theOver Boolean indicating if 'end game' has been clicked on the menu.
     */
    private void menuUpdate(final boolean theOver) {
        if (theOver) {
            myGameBoard.setOver(true);
            myGameTimer.stop();
        } else if (!theOver) {
            myBoard.newGame(DEFAULT_GRID.width, DEFAULT_GRID.height, null);
            myGameBoard.setOver(false);
            myScorePanel.reset();
            myGameTimer.setDelay(DEFAULT_TIMER);
            if (myIsPaused) {
                togglePause();
            }
        }
    }
    
    /** Increases the game timer based on the number of lines cleared.  */
    private void accelerateTimer() {
        final int lines = myScorePanel.getLinesCleared() / (int) DEFAULT_GRID.getHeight();
        if (lines > 0) {
            final int newDelay = DEFAULT_TIMER - (lines * myCurrentDec);
            if (newDelay > 0) {
                myGameTimer.setDelay(newDelay);
            }
        }
    }

    @Override
    public void keyTyped(final KeyEvent theEvent) {
        final String key = Character.toString(theEvent.getKeyChar());
        //ignores case -- I was unable to get the KeyEvent.VK_LETTER to work correctly.
        if (!myMenu.isGameEnded()) {
            if ("p".equalsIgnoreCase(key)) {
                togglePause();
            } else if (!myIsPaused) {
                if ("a".equalsIgnoreCase(key)) {
                    myBoard.moveLeft();
                } else if ("d".equalsIgnoreCase(key)) {
                    myBoard.moveRight();
                } else if ("s".equalsIgnoreCase(key)) {
                    myBoard.moveDown();
                } else if ("w".equalsIgnoreCase(key)) {
                    myBoard.hardDrop();
                } else if (" ".equalsIgnoreCase(key)) {
                    myBoard.rotateCW();
                }
            }
        }
    }
    
    /**
     * Toggles the pause on and off. If the game is paused, this method unpauses it. If the 
     * game is unpaused, this method pauses it.
     */
    private void togglePause() {
        if (myGameTimer.isRunning()) {
            myGameTimer.stop();
            myIsPaused = true;
            myGameBoard.setPause(true);
        } else {
            myGameTimer.start();
            myIsPaused = false;
            myGameBoard.setPause(false);
        }
    }

    /**
     * Does nothing.
     * 
     * @param theEvent The KeyEvent.
     */
    @Override
    public void keyPressed(final KeyEvent theEvent) {
        //does nothing
    }
    /**
     * Does nothing.
     * @param theEvent The KeyEvent.
     */
    @Override
    public void keyReleased(final KeyEvent theEvent) {
        //does nothing
    }
    
    /**
     * Action class for each time the Timer dispatches an action.
     * @author Robbie Nichols
     * @version Autumn 2014
     */
    @SuppressWarnings("serial")
    private class TimerAction extends AbstractAction {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myBoard.step();            
        }
    }
}
