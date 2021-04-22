import javafx.scene.image.Image;

public abstract class Piece implements Cloneable
{
    protected boolean hasMoved;
    protected Image image;
    protected boolean color;
    protected int x;
    protected int y;

    /**
     * Construct a new Piece Object
     * 
     * @param color the color of the piece
     * @param x the x coordinate of the piece
     * @param y the y coordinate of the piece
     */
    public Piece(boolean color, int x, int y){
        
        this.color = color;
        hasMoved = false;
        this.x = x;
        this.y = y;

        String location = "Ressources/pieces/";
        String filename = this.getColor() + "_" + this.getName() + ".png";
        this.image = new Image(location + filename, 70, 70, true, true);
    }

    /**
     * Method that clones the Piece
     * 
     * @return an Object
     */
    public Object clone() {
        Object ans = null;

        try {
            ans = super.clone();
        } catch (CloneNotSupportedException e) {}

        return ans;
    }

    /**
     * Determine if this Piece has moved since the beginning
     * 
     * @return a boolean
     */
    public boolean getHasMoved(){
        return this.hasMoved;
    }

    /**
     * Change the instance variable hasMoved
     * 
     * @param isTrue a boolean
     */
    public void setHasMoved(boolean isTrue){
        this.hasMoved = isTrue;
    }

    /**
     * Gets the image of a chess Piece
     * 
     * @return an Image
     */
    public Image getImage(){
        return this.image;
    }

    /**
     * Get the color of a Piece
     * 
     * @return a String
     */
    public String getColor(){
        if (this.color == true)
            return "White";
        else
            return "Black";
    }

    /**
     * determine if a piece is white or not
     * 
     * @return a boolean
     */
    public boolean isWhite(){
        return this.color;
    }

    /**
     * Give a String representation of a Piece and its color
     * 
     * @return a String
     */
    public String toString(){
        return (this.getName() + " " + this.getColor());
    }

    /**
     * Gives the x coordinate of a Piece
     * 
     * @return an int
     */
    public int getX(){
        return this.x;
    }

    /**
     * Gives the y coordinate of a Piece
     * 
     * @return an int
     */
    public int getY(){
        return this.y;
    }

    /**
     * Change the x coordinate of a Piece
     * 
     * @param x2 an int
     */
    public void setX(int x2){
        this.x = x2;
    }

    /**
     * Change the y coordinate of a Piece
     * 
     * @param y2 an int
     */
    public void setY(int y2){
        this.y = y2;
    }

    /**
     * Abstract method common to all Pieces classes
     * Gives the list of moves
     * 
     * @return an array of type ListOfMoves
     */
    protected abstract ListOfMoves[] getMoves();
    
    /**
     * Abstract method common to all Pieces classes
     * Determine if a Piece use a
     * single or multiple move
     * 
     * @return a boolean true if it uses single Move and false otherwise
     */
    protected abstract boolean hasSingleMove();

    /**
     * Abstract method common to all Pieces classes
     * Gives the String representation of a Piece 
     * in a subclass
     * 
     * @return a String
     */
    protected abstract String getName();
}