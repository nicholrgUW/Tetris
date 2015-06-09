/* TCSS 305 - Project Tetris */
package view;

import java.awt.EventQueue;


/**
 * Main method to launch the GUI.
 * 
 * @author Robbie Nichols eibbor08@uw.edu
 * @version Autumn 2014
 *
 */
public final class GUIMain {
    /**
     * Constructs GUIMain.
     */
    private GUIMain() {
        throw new IllegalStateException();
    }
    /**
     * Launches the Tetris GUI.
     * 
     * @param theArgs The foobargs.
     */
    public static void main(final String[] theArgs) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
        
    }

}
