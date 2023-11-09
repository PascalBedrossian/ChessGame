package board;

public enum ListOfMoves
{
    UP (0,1),
    UP_RIGHT(1, 1),
    RIGHT(1, 0),
    DOWN_RIGHT(1, -1),
    DOWN(0,-1),
    DOWN_LEFT(-1, -1),
    LEFT(-1, 0),
    UP_LEFT(-1, 1),
    
    KING_RIGHT(2, 0),
    KING_LEFT(-2, 0),

    KNIGHT_LEFT_UP(-2, 1),
    KNIGHT_UP_LEFT(-1, 2),
    KNIGHT_UP_RIGHT(1, 2),
    KNIGHT_RIGHT_UP(2, 1),
    
    KNIGHT_RIGHT_DOWN(2, -1),
    KNIGHT_DOWN_RIGHT(1, -2),
    KNIGHT_DOWN_LEFT(-1, -2),
    KNIGHT_LEFT_DOWN(-2, -1),
    
    DOUBLE_UP(0, 2),
    DOUBLE_DOWN(0, -2);

    private int x;
    private int y;

    /**
     * Construct an enum of this class
     * 
     * @param x an int coordinate
     * @param y an int coordinate
     */
    private ListOfMoves(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Determine if the coordinates given by a move are equal
     * 
     * @param move of type ListOfMoves
     * @return a boolean true if they are equal and false otherwise
     */
    public boolean areCoordinatesEqual(ListOfMoves move){
        return (this.x == move.getX()) && (this.y == move.getY());
    }

    /**
     * Determine if the coordinates of an enum and x and y are equal
     * 
     * @param x an int
     * @param y an int
     * @return a boolean true if they are equal and false otherwise
     */
    public boolean areCoordinatesEqual(int x, int y) {
        return (this.x == x) && (this.y == y);
    }

    /**
     * Set the x coordinate
     * 
     * @param x an int
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the x coordinates
     * 
     * @return an int
     */
    public int getX(){
        return this.x;
    }

    /**
     * Set the y coordinate
     * 
     * @param y an int
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the y coordinates
     * 
     * @return an int
     */
    public int getY(){
        return this.y;
    }
}