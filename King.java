import java.util.ArrayList;

public class King extends Piece
{
    /**
     * Construct a new King Object
     * 
     * @param color the color of the piece
     * @param x the x coordinate of the piece
     * @param y the y coordinate of the piece
     */
    public King(boolean color, int x, int y){
        // this calls the constructor of Piece
        super(color, x, y);
    }

    /**
     * Collect all the moves the King can do and store them in 
     * an array of type
     * ListOfMoves
     * 
     * @return an array of type ListOfMoves
     */
    protected ListOfMoves[] getMoves() {
        ListOfMoves[] moves = {};

        ArrayList<ListOfMoves> kingMoves = new ArrayList<ListOfMoves>();

        kingMoves.add(ListOfMoves.UP);
        kingMoves.add(ListOfMoves.UP_RIGHT);
        kingMoves.add(ListOfMoves.RIGHT);
        kingMoves.add(ListOfMoves.DOWN_RIGHT);
        kingMoves.add(ListOfMoves.DOWN);
        kingMoves.add(ListOfMoves.DOWN_LEFT);
        kingMoves.add(ListOfMoves.LEFT);
        kingMoves.add(ListOfMoves.UP_LEFT);

        if(!hasMoved){
            kingMoves.add(ListOfMoves.KING_RIGHT);
            kingMoves.add(ListOfMoves.KING_LEFT);
        }

        moves = kingMoves.toArray(moves);
        return moves;
    }

    /**
     * Determine if this piece has single moves of multiple
     * 
     * @return a boolean true if it has single moves and false otherwise
     */
    protected boolean hasSingleMove(){
        return true;
    }

    /**
     * Gives the String repersentation of this piece
     * 
     * @return a String
     */
    protected String getName(){
        return "K";
    }

    /**
     * Checks if the King can attack a piece without getting attacked in return
     * 
     * @param mv 
     * @return a boolean
     */
    protected boolean canAttack(MoveInformation mv){
        return false;
    }
}