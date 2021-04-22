
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Tile extends Button
{
    private int x;
    private int y;
    private Piece piece; //piece currently on Tile

    /**
     * Construct a new Tile Object
     * 
     * @param whiteColor a boolean that determines if the Tile is white or black
     * @param x an int coordinate
     * @param y an int coordinate
     */
    public Tile(boolean whiteColor, int x, int y){
        // create a button with empty string
        super();
        this.x = x;
        this.y = y;
        this.piece = null;
        this.getStyleClass().add("chessTile");

        if (whiteColor)
            this.getStyleClass().add("chessWhiteTile");
        else
            this.getStyleClass().add("chessBlackTile");
    }

    /**
     * Determine if this Tile is occupied
     * 
     * @return a boolean true if the Tile is occupied and false otherwise
     */
    public boolean isTileOccupied(){
        return (this.piece != null);
    }

    /**
     * Give the Piece located on this Tile
     * 
     * @return a Piece
     */
    public Piece getPiece(){
        return this.piece;
    }

    /**
     * Removes the piece from the Tile and return it
     * 
     * @return the removed Piece
     */
    public Piece removePiece(){
        Piece temporary = this.piece;
        setPiece(null);
        return temporary;
    }

    /**
     * Sets a piece on the Tile and draws its image
     * 
     * @param piece a Piece
     */
    public void setPiece(Piece piece){
        this.piece = piece;

        if (this.piece != null)
            this.setGraphic(new ImageView(piece.getImage()));
        else
            this.setGraphic(new ImageView());
    }

    /**
     * Gives the color of a Piece on a Tile
     * 
     * @return a String
     */
    public String getPieceColor(){
        if (getPiece() != null)
            return getPiece().getColor();
        else // space empty
            return "";
    }

    /**
     * Set the x to xIn
     * 
     * @param xIn an int
     */
    public void setX(int xIn){
        this.x = xIn;
    }

    /**
     * Get the x coordinate of this Tile
     * 
     * @return an int
     */
    public int getX(){
        return this.x;
    }

    /**
     * Set the y to yIn
     * 
     * @param yIn an int
     */
    public void setY(int yIn){
        this.y = yIn;
    }

    /**
     * Get the y coordinate of this Tile
     * 
     * @return an int
     */
    public int getY(){
        return this.y;
    }
}