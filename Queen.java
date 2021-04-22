public class Queen extends Piece
{
    /**
     * Construct a new Queen Object
     * 
     * @param color the color of the piece 
     * @param x the x coordinate of the piece
     * @param y the y coordinate of the piece
     */
    public Queen(boolean color, int x, int y){
        // this calls the constructor of Piece
        super(color, x, y);
    }

    /**
     * Collect all the moves the Queen can do
     * and store them in an array of type ListOfMoves
     * 
     * @return an array of type ListOfMoves
     */
    protected ListOfMoves[] getMoves(){
        ListOfMoves[] m = 
        { 
            ListOfMoves.UP,
            ListOfMoves.UP_RIGHT,
            ListOfMoves.RIGHT,
            ListOfMoves.DOWN_RIGHT,
            ListOfMoves.DOWN,
            ListOfMoves.DOWN_LEFT,
            ListOfMoves.LEFT,
            ListOfMoves.UP_LEFT 
        };
        return m;
    }

    /**
     * Determine if this piece has single moves or multiple
     * 
     * @return a boolean true if it has single moves and false otherwise
     */
    protected boolean hasSingleMove(){
        return false;
    }

    /**
     * Gives the String representation of this piece
     * 
     * @return a String
     */
    protected String getName(){
        return "Q";
    }
}