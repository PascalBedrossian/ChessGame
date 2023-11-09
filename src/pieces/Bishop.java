package pieces;

import board.ListOfMoves;

public class Bishop extends Piece
{
    /**
     * Construct a new Bishop Object
     * 
     * @param color the color of the piece 
     * @param x the x coordinate of the piece
     * @param y the y coordinate of the piece
     */
    public Bishop(boolean color, int x, int y){
        // this calls the constructor of Piece
        super(color, x, y);
    }

    /**
     * Collect all the moves the Bishop can do
     * and store them in an array of type ListOfMoves
     * 
     * @return an array of type ListOfMoves
     */
    public ListOfMoves[] getMoves(){
        ListOfMoves[] m = 
        { 
        ListOfMoves.UP_RIGHT,
        ListOfMoves.DOWN_RIGHT, 
        ListOfMoves.DOWN_LEFT, 
        ListOfMoves.UP_LEFT 
        };
        return m;
    }

    /**
     * Determine if this piece has single move or multiple
     * 
     * @return a boolean true if it has single moves and false otherwise
     */
    public boolean hasSingleMove(){
        return false;
    }

    /**
     * Gives the String representation of this piece
     * 
     * @return a String
     */
    public String getName(){
        return "B";
    }
}