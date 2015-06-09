/* TCSS 305 - Project Tetris */
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import model.AbstractPiece;
import model.Piece;

/**
 * Preview panel. Displays the next piece and the controls.
 * 
 * @author Robbie Nichols eibbor08@uw.edu
 * @version Autumn 2014
 *
 */
@SuppressWarnings("serial")
public class PreviewPanel extends JPanel {
    
    /** Default size of the preview panel. */
    protected static final Dimension DISPLAY_SIZE = new Dimension(150, 80);
    
    /** Default size of the entire panel, including where the controls are drawn. */
    protected static final Dimension ENTIRE_SIZE = new Dimension(150, 200);
    
    /** Default background color for the preview panel. */
    protected static final Color DEFAULT_BACKGROUND = Color.WHITE;
    
    /** Default border size. */
    protected static final int DEFAULT_BORDER = 4;    
    
    /** The preview piece. */
    private Piece myPreview;
    
    /** Preview panel constructor. */
    public PreviewPanel() {
        super();
        setup();
    }
    
    /** Private helper method for the constructor. */
    private void setup() {
        this.setPreferredSize(ENTIRE_SIZE);
        this.setBackground(DEFAULT_BACKGROUND);
    }
    /**
     * Paints the preview board.
     * 
     * @param theGraphics the Graphics component.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {        
        super.paintComponent(theGraphics);
        final Graphics2D graphics2d = (Graphics2D) theGraphics;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawControls(graphics2d);
        
        graphics2d.setStroke(new BasicStroke(DEFAULT_BORDER));
        graphics2d.setColor(Color.LIGHT_GRAY);
        graphics2d.drawRect(2, 2, this.getWidth() - DEFAULT_BORDER, 
                            DISPLAY_SIZE.height - DEFAULT_BORDER);        
        if (myPreview != null) {
            drawPiece(graphics2d);
        }        
        drawControls(graphics2d);
    }
    
    /**
     * Draws the piece to preview.
     * 
     * @param theGraphics The graphics component to draw with.
     */
    private void drawPiece(final Graphics2D theGraphics) {
        final Color pieceColor = ((AbstractPiece) myPreview).getBlock().getColor();
        final int blockSize  = 20;
        theGraphics.setStroke(new BasicStroke(1));
        theGraphics.setColor(Color.BLACK);
        final int[][] coords = ((AbstractPiece) myPreview).getBoardCoordinates();
        for (int i = 0; i < coords.length; i++) {
            final int offset = 40;
            final int coordOffsetX = 3;
            final int coordOffsetY = 20;
            final int x = coords[i][0] - coordOffsetX;
            final int y = coords[i][1] - coordOffsetY;
            theGraphics.setColor(pieceColor);
            theGraphics.fillRect(x * blockSize + DEFAULT_BORDER + offset, DISPLAY_SIZE.height
                                 - DEFAULT_BORDER - blockSize - y * blockSize,
                                 blockSize, blockSize);
            theGraphics.setColor(Color.gray);
            theGraphics.drawRect(x * blockSize + DEFAULT_BORDER + offset, DISPLAY_SIZE.height 
                                 - DEFAULT_BORDER - blockSize - y * blockSize, blockSize, 
                                 blockSize);
        }
    }
    /**
     * Draws the controls as strings beneath the preview area.
     * 
     * @param theGraphics The Graphics2D component to draw with.
     */
    private void drawControls(final Graphics2D theGraphics) {
        final int textY = 100;
        final int textX = 5;
        final int fontSize = 16;
        final String[] controlArray = {"Left:   ", "Right:  ", "Down:   ", "Drop:   ", 
                                       "Rotate: ", "Pause:  "};
        final String[] keys = {"a", "d", "s", "w", "space", "p"};
        
        theGraphics.setColor(Color.LIGHT_GRAY);
        theGraphics.fillRect(0, DISPLAY_SIZE.height, getWidth(), getHeight());
        theGraphics.setColor(Color.BLACK);
        theGraphics.setFont(new Font(Font.MONOSPACED, Font.BOLD, fontSize));
        theGraphics.drawString("Controls", 2, textY);
        
        for (int i = 0; i < controlArray.length; i++) {
            theGraphics.drawString(controlArray[i] + keys[i], textX, textY + fontSize 
                                   * (i + 1));
        }    
    }
    /**
     * Sets myPreviewPiece.
     * @param thePiece The next piece.
     */
    public void setPreview(final Piece thePiece) {
        myPreview = thePiece;
    }
    /**
     * Returns a reference to the myPreviewPiece.
     * @return The next piece.
     */
    public Piece getPreview() {
        return myPreview;
    }
}
