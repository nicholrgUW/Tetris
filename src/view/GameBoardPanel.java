/* TCSS 305 - Project Tetris */
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JPanel;

import model.AbstractPiece;
import model.Block;
import model.Piece;


/**
 * The game panel class. Displays the current state of the Tetris board and pieces.
 * 
 * @author Robbie Nichols eibbor08@uw.edu
 * @version Autumn 2014
 *
 */
@SuppressWarnings("serial")
public class GameBoardPanel extends JPanel {
    
    /** Default background color for the game panel. */
    protected static final Color DEFAULT_BACKGROUND = Color.white;
    
    /**
     * Default block length. The size is derived from a standard game of 10 by 20 squares, so
     * block length would be one tenth of the game panel width. 
     */
    protected static final int DEFAULT_BLOCK_SIZE = 30;
    
    /** Stroke size of the border. */
    protected static final int DEFAULT_BORDER = 4;
    
    /** List of frozen blocks to paint. */
    private List<Block[]> myFrozen;
    
    /** Current block size. */
    private int myBlockSize;
    
    /** Current piece in play. */
    private Piece myPiece;
    
    /** Boolean indicating if the game has ended. True if the game is over, otherwise false. */
    private boolean myOver;
    
    /** Boolean indicating if the game is paused. True if paused, otherwise false. */
    private boolean myPause;
    
    /** Constructor for the game panel. */
    public GameBoardPanel() {
        super(true);
        myOver = false;
        myPause = false;
        setup();
    }
    
    /** Private helper method for the constructor. */
    private void setup() {
        setBackground(DEFAULT_BACKGROUND);

    }
    /**
     * Changes myOver to true if the game is over, false if the game is not over.
     * 
     * @param theBool Boolean specifying if the game is over.
     */
    public void displayOver(final boolean theBool) {
        myOver = theBool;
    }
    /**
     * Paints the game board.
     * 
     * @param theGraphics the Graphics component.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {       
        super.paintComponent(theGraphics);
        final Graphics2D graphics2d = (Graphics2D) theGraphics;
        final int fontSize = 24;
        final Font font = new Font(null, Font.BOLD, fontSize);     
        graphics2d.setFont(font);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                     RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (myPiece != null) {
            drawPiece(graphics2d);
        }        
        if (myFrozen != null) {
            drawFrozen(graphics2d);
        }
        drawBorder(graphics2d);
        
        //sets the color to black for displaying text.
        graphics2d.setColor(Color.BLACK);
        //Paints the end game text to inform the player the game is over.
        if (myOver) {
            final String over = "YOU LOOSE";
            final Rectangle2D bounds = font.getStringBounds(over,
                                                            graphics2d.getFontRenderContext());
            final int y = getPreferredSize().height / 2;
            final int x = getPreferredSize().width / 2 - (int) bounds.getWidth() / 2;
            graphics2d.drawString(over, x, y);

        }
        //Paints the pause game text to let the player know the game is paused.
        if (myPause && !myOver) {
            final String pause = "Take your time...";
            final Rectangle2D bounds = font.getStringBounds(pause,
                                                            graphics2d.getFontRenderContext());
            final int y = getPreferredSize().height / 2;
            final int x = getPreferredSize().width / 2 - (int) bounds.getWidth() / 2;
            graphics2d.drawString(pause, x, y);
        }      
    }

    
    /**
     * Draws a border around the edge of the game panel.
     * 
     * @param theGraphics The graphics component to draw with.
     */
    private void drawBorder(final Graphics2D theGraphics) {
        theGraphics.setStroke(new BasicStroke(DEFAULT_BORDER));
        theGraphics.setColor(Color.LIGHT_GRAY);
        theGraphics.drawRect(1, 1, this.getWidth() - 2, getHeight() - 2);

    }

    /**
     * Draws the current piece. Updates every timer cycle or when it is moved by the player.
     * 
     * @param theGraphics The graphics component to draw with.
     */
    private void drawPiece(final Graphics2D theGraphics) {
        final Color pieceColor = ((AbstractPiece) myPiece).getBlock().getColor();
        
        theGraphics.setStroke(new BasicStroke(1));
        final int[][] coords = ((AbstractPiece) myPiece).getBoardCoordinates();
        for (int i = 0; i < coords.length; i++) {
            final int x = coords[i][0] * myBlockSize + DEFAULT_BORDER;
            final int y = this.getPreferredSize().height - DEFAULT_BORDER 
                                            - (coords[i][1] + 1) * myBlockSize;
            // doesn't draw clipped pieces
            if (y > DEFAULT_BORDER - myBlockSize) {
                theGraphics.setColor(pieceColor);
                theGraphics.fillRect(x, y, myBlockSize, myBlockSize);
                theGraphics.setColor(Color.GRAY);
                theGraphics.drawRect(x, y, myBlockSize, myBlockSize);            
            }
        }
    }

    /**
     * Draws the frozen blocks.
     * 
     * @param theGraphics The graphics component to draw with.
     */
    private void drawFrozen(final Graphics2D theGraphics) {
        theGraphics.setStroke(new BasicStroke(1));
        
        for (int yAxis = 0; yAxis < myFrozen.size(); yAxis++) {
            final Block[] blocks = myFrozen.get(yAxis);
            for (int xAxis = 0; xAxis < blocks.length; xAxis++) {
                
                if (blocks[xAxis] != Block.EMPTY) {
                    final int x = xAxis * myBlockSize + DEFAULT_BORDER;
                    final int y = getHeight() - DEFAULT_BORDER - (yAxis * myBlockSize)
                                                    - myBlockSize;
                    
                    theGraphics.setColor(blocks[xAxis].getColor());
                    theGraphics.fillRect(x, y, myBlockSize, myBlockSize);
                    theGraphics.setColor(Color.BLACK);
                    theGraphics.drawRect(x, y, myBlockSize, myBlockSize);
                }
            }
        }
    }
    
    
    /**
     * Draws the piece as it moves down the board.
     * 
     * @param thePiece The piece currently in play. 
     */
    public void setCurrentPiece(final Piece thePiece) {
        myPiece = thePiece;
    }
    
    /**
     * Draws frozen blocks from the passed List.
     * 
     * @param theFrozen The list of frozen blocks to draw.
     */
    public void setFrozen(final List<Block[]> theFrozen) {
        myFrozen = theFrozen;
    }
    
    /**
     * Resizes the Tetris game board.
     * 
     * @param theX The new x of the JFrame containing this panel.
     * @param theY The new y of the JFrame containing this panel.
     */
    public void resizeTetrisBoard(final int theX, final int theY) {
        final int xGrid = 10;
        final int yGrid = 20;
        final int offset = DEFAULT_BORDER * 2;  
        //Total amount of pixels the border takes up by height/width.
        
        final int buffer = 20; 
        //Buffer space to make sure the Tetris board is never cut off by the window.
        
        //this subtracts the width of the preview panel to make sure there is enough room for 
        //the preview panel not getting pushed off the screen.
        int x = theX - PreviewPanel.ENTIRE_SIZE.width - buffer;
        int y = theY - buffer;        
        
        if (x < y / 2) {
            y = x * 2;
        } else {
            x = y / 2;
        }
        
        x = x - DEFAULT_BORDER - buffer;
        myBlockSize = (x - offset) / xGrid;        
        x = myBlockSize * xGrid + offset;
        y = myBlockSize * yGrid + offset;
        
        this.setPreferredSize(new Dimension(x, y));
    }
    /**
     * Sets the game state as over if the parameter is true.
     * 
     * @param theBool The boolean setting game state.
     */
    public void setOver(final boolean theBool) {
        myOver = theBool;
        repaint();
    }
    
    /**
     * Sets the game state to paused or play. True pauses; false unpauses.
     * 
     * @param theBool The boolean setting the pause state.
     */
    public void setPause(final boolean theBool) {
        myPause = theBool;
        repaint();
    }
}
