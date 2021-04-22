public class Knight extends Piece
{
    /**
     * Construct a new Knight Object
     * 
     * @param color the color of the piece
     * @param x the x coordinate of the piece
     * @param y the y coordinate of the piece
     */
    public Knight(boolean color, int x, int y){
        // this calls the constructor of Piece
        super(color, x, y);
    }

    /**
     * Collect all the moves the knight can do
     * and store them in an array of type ListOfMoves
     * 
     * @return an array of type ListOfMoves
     */
    protected ListOfMoves[] getMoves(){
        ListOfMoves[] m = 
        { 
            ListOfMoves.KNIGHT_LEFT_UP, 
            ListOfMoves.KNIGHT_UP_LEFT, 
            ListOfMoves.KNIGHT_UP_RIGHT,
            ListOfMoves.KNIGHT_RIGHT_UP, 
            ListOfMoves.KNIGHT_RIGHT_DOWN, 
            ListOfMoves.KNIGHT_DOWN_RIGHT,
            ListOfMoves.KNIGHT_DOWN_LEFT, 
            ListOfMoves.KNIGHT_LEFT_DOWN 
        };
        return m;
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
     * Gives the String representation of this piece
     * 
     * @return a String
     */
    protected String getName(){
        return "N";
    }
}