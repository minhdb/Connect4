package model;

import java.io.Serializable;


/**
 * This class encapsulates the rows, columns, and color information of the move.
 * An instance of this class is created and sent by model to update the view.
 * 
 *
 */

public class Connect4MoveMessage implements Serializable {		
	public static int YELLOW = 1;
    public static int RED = 2;

    private static final long serialVersionUID = 1L;
    private int row;
    private int col;
    private int color;

   /**
    *  Constructs a connect4 message object with the given parameters.
    *  
    *  @param row An integer for the row.
    *  @param col An integer for the column.
    *  @param color an integer 1 or 2 representing colors.
    */
    public Connect4MoveMessage(int row, int col, int color) {
    	this.row = row;
    	this.col = col;
    	this.color = color;
    }

    /**
     * 
     * @return The row number of this move message.
     */
    public int getRow() { return row; }

    /**
     * @return The column number of this move message.
     */
    public int getColumn() { return col; }

    /**
     * @return The color number either 1 or 2 for this move message.
     */
    public int getColor() { return color; }
	
}
