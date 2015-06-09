/* TCSS 305 - Project Tetris */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

/**
 * Creates the menu bar for Tetris.
 * 
 * @author Robbie Nichols eibbor08@uw.edu
 * @version Autumn 2014
 */

public class MenuBar extends Observable {
    
    /** Constant representing the peaceful game mode. */
    public static final int GAME_PEACEFUL = 0;
    
    /** Constant representing the normal game mode. */
    public static final int GAME_NORMAL = 1;
    
    /** Constant representing the hard game mode. */
    public static final int GAME_HARD = 2;

    /** The JMenuBar. */
    private final JMenuBar myMenuBar;
    
    /** The file menu. */
    private final JMenu myFileMenu;
    
    /** The help menu. */
    private final JMenu myHelpMenu;
    
    /** The game mode menu. */
    private final JMenu myGameMode;
    
    /** The outer JFrame. */
    private final JFrame myFrame;
    
    /** Boolean indicating whether the end game button has been pushed. */
    private boolean myIsGameEnded;
    
    /** The current game mode. */
    private int myCurrentMode;
    
    /**
     * MenuBar constructor.
     * 
     * @param theFrame A reference to the JFrame containing all the visual elements of Tetris.
     */
    public MenuBar(final JFrame theFrame) {
        super();
        myMenuBar = new JMenuBar();
        myFrame = theFrame;
        myFileMenu = new JMenu("File");
        myHelpMenu = new JMenu("Help");
        myGameMode = new JMenu("Game Modes");
        myIsGameEnded = false;
        myCurrentMode = GAME_NORMAL;
        setup();
    }
    /**
     * Private helper method. Sets initial desired state of menu items.
     */
    private void setup() {
        final JMenuItem exit = new JMenuItem("Exit");
        final JMenuItem endGame = new JMenuItem("End Game");
        final JMenuItem newGame = new JMenuItem("New Game");    
        final JMenuItem about = new JMenuItem("About");
        final JMenuItem scoring = new JMenuItem("Scoring System");
        final JRadioButtonMenuItem peaceful = new JRadioButtonMenuItem("Peaceful");
        final JRadioButtonMenuItem normal = new JRadioButtonMenuItem("Normal");
        final JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard");
        final ButtonGroup group = new ButtonGroup();
        
        myFileMenu.setMnemonic(KeyEvent.VK_F);
        myGameMode.setMnemonic(KeyEvent.VK_G);
        myHelpMenu.setMnemonic(KeyEvent.VK_H);
        
        exit.setMnemonic(KeyEvent.VK_X);
        about.setMnemonic(KeyEvent.VK_A);
        scoring.setMnemonic(KeyEvent.VK_S);
        
        newGame.setMnemonic(KeyEvent.VK_N);  
        endGame.setMnemonic(KeyEvent.VK_E);
        newGame.setEnabled(false);
        
        peaceful.setMnemonic(KeyEvent.VK_P);
        normal.setMnemonic(KeyEvent.VK_N);
        hard.setMnemonic(KeyEvent.VK_H);
        
        group.add(peaceful);
        group.add(normal);
        group.add(hard);
        
        addGameActions(peaceful, normal, hard);
        
        addFileActions(endGame, newGame, exit);
        
        addHelpActions(about, scoring);       
     
        myFileMenu.add(newGame);
        myFileMenu.add(endGame);
        myFileMenu.add(exit);
        
        myGameMode.add(peaceful);
        myGameMode.add(normal);
        myGameMode.add(hard);
        
        myHelpMenu.add(about);
        myHelpMenu.add(scoring);
        
        myMenuBar.add(myFileMenu);
        myMenuBar.add(myGameMode);
        myMenuBar.add(myHelpMenu);
    }
    
    /**
     * Adds actions to the game mode menu items.
     * 
     * @param thePeaceful The peaceful radio button.
     * @param theNormal The normal radio button.
     * @param theHard The hard radio button.
     */
    private void addGameActions(final JRadioButtonMenuItem thePeaceful, final 
                                JRadioButtonMenuItem theNormal, final JRadioButtonMenuItem 
                                theHard) {
        
        thePeaceful.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myCurrentMode = GAME_PEACEFUL;
                setChanged();
                notifyObservers(myCurrentMode);                
            }
        });
        theNormal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myCurrentMode = GAME_NORMAL;
                setChanged();
                notifyObservers(myCurrentMode);                
            }
        });
        theHard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myCurrentMode = GAME_HARD;
                setChanged();
                notifyObservers(myCurrentMode);                
            }
        });
    }
    
        
    /**
     * Helper method that adds actions to the File menu JFileItems.
     * 
     * @param theEndgame The end game JMenuItem.
     * @param theNewgame The new game JMenuItem.
     * @param theExit The exit JMenuItem.
     */
    private void addFileActions(final JMenuItem theEndgame, final JMenuItem theNewgame, 
                                final JMenuItem theExit) {
        
        //new game functionality.
        theNewgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                theEndgame.setEnabled(true);
                theNewgame.setEnabled(false);
                myIsGameEnded = false;
                setChanged();
                notifyObservers(myIsGameEnded);
            }
        });  
        
        //end game functionality.
        theEndgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myIsGameEnded = true;
                theNewgame.setEnabled(true);
                theEndgame.setEnabled(false);
                setChanged();
                notifyObservers(myIsGameEnded);
            }
        });       
        
        //exit functionality.
        theExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
            }
        });        

    }
    
    /**
     * Adds actions to the Help menu JMenuItems.
     * 
     * @param theAbout The about JMenuItem.
     * @param theScoring The scoring JMenuItem.
     */
    private void addHelpActions(final JMenuItem theAbout, final JMenuItem theScoring) {
        //Scoring system taken from the original Nintendo Tetris. 
        //See http://tetriswiki.com/Scoring        
        
        final String aboutText = "Tetris, Autumn 2014\nTCSS 305D\nRobbie Nichols";
        //I admit this is really messy.
        final String scoringText = "Clearing a line earns 40 points.\nTwo lines earns"
                                        + " 100 points.\nThree lines earns 300 points.\nFour "
                                        + "lines earns 1200 points. \n\nEvery 20 lines cleared"
                                        + " increases\nthe game speed. Each increase\nin "
                                        + "difficulty doubles the points\nearned from clearing"
                                        + " a line.\n\nChanging game modes does not effect\n"
                                        + "the scoring system, instead it only\nchanges the "
                                        + "speed of the game.\nPeaceful does not speed up as "
                                        + "the\ndifficulty increases.";

        theAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                JOptionPane.showMessageDialog(theAbout, aboutText, theAbout.getText(), 
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        theScoring.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                JOptionPane.showMessageDialog(theScoring, scoringText, "Scoring", 
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    /**
     * Returns the a JMenuBar with functionality.
     * 
     * @return The a JMenuBar with functionality.
     */
    public JMenuBar getTheMenu() {
        return myMenuBar;
    }
    
    /**
     * Returns boolean indicating whether the end game button has been clicked.
     * 
     * @return Boolean indicating whether the end game button has been clicked.
     */
    public boolean isGameEnded() {
        return myIsGameEnded;
    }
}
