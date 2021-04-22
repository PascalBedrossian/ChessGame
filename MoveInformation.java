public class MoveInformation
{
    int oldX;
    int newX;
    int oldY;
    int newY;

    /**
     * Construct a new object MoveInformation
     * 
     * @param oldX the old x coordinate
     * @param oldY the old y coordinate
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     */
    public MoveInformation(int oldX, int oldY, int newX, int newY){
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    /**
     * Construct a new object MoveInformation
     */
    public MoveInformation(){
        oldX = 0;
        oldY = 0;
        newX = 1;
        newY = 1;
    }

    /**
     * Return a String representation of the move information
     */
    public String toString() {
        return (getCharLabel(oldX + 1) + (oldY + 1) + " to " + getCharLabel(newX + 1) + (newY + 1));
    }

    /**
     * Method that converts x number position to character label
     * 
     * @param i an int representing the letter of a tile
     * @return a String
     */
    private String getCharLabel(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 64)) : null;
    }

    /**
     * Gets the old x coordinate
     * 
     * @return an int
     */
    public int getOldX(){
        return this.oldX;
    }

    /**
     * Gets the old y coordinate
     * 
     * @return an int
     */
    public int getOldY(){
        return this.oldY;
    }

    /**
     * Gets the new x coordinate
     * 
     * @return an int
     */
    public int getNewX(){
        return this.newX;
    }

    /**
     * Gets the new y coordinate
     * 
     * @return an int
     */
    public int getNewY(){
        return this.newY;
    }

    /**
     * Calculate newX - oldX
     * 
     * @return an int
     */
    public int getGapX(){
        return this.newX - this.oldX;
    }

    /**
     * Calculate newY - oldY
     * 
     * @return an int
     */
    public int getGapY() {
        return this.newY - this.oldY;
    }
}