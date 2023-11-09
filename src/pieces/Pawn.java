package pieces;

import java.util.ArrayList;

public class Pawn extends Piece
{
    /**
     * Construct a new Pawn Object
     * 
     * @param color the color of the piece
     * @param x the x coordinate of the piece
     * @param y the y coordinate of the piece
     */
    public Pawn(boolean color, int x, int y){
        // this calls the constructor of Piece
        super(color, x, y);
    }

    /**
     * Collect all the moves the pawn can do and store
     * them in an array of type ListOfMoves
     * 
     * @return an array of type ListOfMoves
     */
    protected ListOfMoves[] getMoves(){
        boolean isWhite = this.color;

        ListOfMoves[] moves = {};

        if (isWhite) {//white pawn, so moving from coordinates 0 to 7, so up
            ArrayList<ListOfMoves> whiteMoves = new ArrayList<ListOfMoves>();
            // move forward
            whiteMoves.add(ListOfMoves.UP);
            // to capture diagonally
            whiteMoves.add(ListOfMoves.UP_RIGHT);
            whiteMoves.add(ListOfMoves.UP_LEFT);
            if (!hasMoved) {// if it didn't move, allow double forward move
                whiteMoves.add(ListOfMoves.DOUBLE_UP);
            }
            moves = whiteMoves.toArray(moves);
        } else {//black pawn, so moving from coordinates 7 to 0, so down
            ArrayList<ListOfMoves> blackMoves = new ArrayList<ListOfMoves>();
            // move forward
            blackMoves.add(ListOfMoves.DOWN);
            // to capture diagonally
            blackMoves.add(ListOfMoves.DOWN_RIGHT);
            blackMoves.add(ListOfMoves.DOWN_LEFT);
            if (!hasMoved) {// if it didn't move, allow double forward move
                blackMoves.add(ListOfMoves.DOUBLE_DOWN);
            }
            moves = blackMoves.toArray(moves);
        }
        return moves;
    }

    /**
     * Determine if this piece has single moves or multiple
     * 
     * @return a boolean true if it has single moves and false otherwise
     */
    protected boolean hasSingleMove(){
        return true;
    }

    /**
     * Gives the Sring representation of this piece
     * 
     * @return a String
     */
    protected String getName(){
        return "pawn";
    }
}