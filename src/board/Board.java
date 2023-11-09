package board;

import javafx.scene.layout.GridPane;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

import java.util.Arrays;
import java.util.ArrayList;

public class Board extends GridPane {

	private Tile[][] tiles = new Tile[8][8];

	private Tile activeTile = null;// keep track of active tile

	private int[] whiteKingPosition = new int[2];
	private int[] blackKingPosition = new int[2];

	private boolean pawnEnPassant = false;// Keep track of if enPassant available
	private int[] enPassantCoordinates = new int[2];// gets me the coordinates of enPassant Tile

	ArrayList<Board> listOfBoards;
	private int count;// for turns
	private int numberOfMoves;// same then count but still increment for moves history
	private boolean turnEnabled;// for turn enabled or disabled by user
	private boolean illegalDisplayEnabled;// for pop ups enabled or disabled by user
	private boolean colorOrder = true;// to keep track of which color is on bottom of board
	private boolean setAutomatic = false;// to keep track of if rotation is set manual or auto

	/**
	 * Creates a new Board Object to be used in copies
	 */
	public Board() {
		tiles = new Tile[8][8];
		activeTile = null;
		whiteKingPosition = new int[2];
		blackKingPosition = new int[2];
		pawnEnPassant = false;
		enPassantCoordinates = new int[2];
		listOfBoards = new ArrayList<Board>();
		count = 0;
		turnEnabled = false;
		illegalDisplayEnabled = false;
		numberOfMoves = 0;
	}

	/**
	 * Construct a new Board object
	 * 
	 * @param notCopy
	 */
	public Board(boolean notCopy) {

		super();// Gridpane constructor
		tiles = new Tile[8][8];
		activeTile = null;
		whiteKingPosition = new int[2];
		blackKingPosition = new int[2];
		pawnEnPassant = false;
		enPassantCoordinates = new int[2];
		listOfBoards = new ArrayList<Board>();
		count = 0;
		numberOfMoves = 0;
		// initialize 8x8 array of tiles
		for (int x = 0; x < tiles[0].length; x++) {
			for (int y = 0; y < tiles[1].length; y++) {
				boolean light = ((x + y) % 2 != 0); // checking for Tile's colors
				tiles[x][y] = new Tile(light, x, y);
				// I add tiles to ensure bottom left coordinates is 0,0
				this.add(tiles[x][y], x, 7 - y);
				// Gets values into event handler
				final int xVal = x;
				final int yVal = y;
				// Everytime a Tile is clicked, methods will be called and will run
				tiles[x][y].setOnAction(e -> onTileClick(xVal, yVal));
			}
		}
		// put pieces in start positions
		this.setupStartPositions();
	}

	/**
	 * Define the starting piece positions
	 */
	public void setupStartPositions() {
		// For white pieces
		this.tiles[0][0].setPiece(new Rook(true, 0, 0));
		this.tiles[1][0].setPiece(new Knight(true, 1, 0));
		this.tiles[2][0].setPiece(new Bishop(true, 2, 0));
		this.tiles[3][0].setPiece(new Queen(true, 3, 0));
		this.tiles[4][0].setPiece(new King(true, 4, 0));
		this.tiles[5][0].setPiece(new Bishop(true, 5, 0));
		this.tiles[6][0].setPiece(new Knight(true, 6, 0));
		this.tiles[7][0].setPiece(new Rook(true, 7, 0));

		// For white pawns
		for (int i = 0; i < this.tiles[0].length; i++)
			this.tiles[i][1].setPiece(new Pawn(true, i, 1));

		// For black pieces
		this.tiles[0][7].setPiece(new Rook(false, 0, 7));
		this.tiles[1][7].setPiece(new Knight(false, 1, 7));
		this.tiles[2][7].setPiece(new Bishop(false, 2, 7));
		this.tiles[3][7].setPiece(new Queen(false, 3, 7));
		this.tiles[4][7].setPiece(new King(false, 4, 7));
		this.tiles[5][7].setPiece(new Bishop(false, 5, 7));
		this.tiles[6][7].setPiece(new Knight(false, 6, 7));
		this.tiles[7][7].setPiece(new Rook(false, 7, 7));

		// For black pawns
		for (int i = 0; i < this.tiles[0].length; i++)
			this.tiles[i][6].setPiece(new Pawn(false, i, 6));

		// Set the coordinates of black king to be able to follow up check status
		blackKingPosition[0] = 4;
		blackKingPosition[1] = 7;

		// Set the coordinates of white king to be able to follow up check status
		whiteKingPosition[0] = 4;
		whiteKingPosition[1] = 0;

		// resetting everything for use in restart method in GUI class
		this.setActiveTile(null);
		activeTile = null;
		turnEnabled = true;
		illegalDisplayEnabled = true;
		count = 0;
		listOfBoards = new ArrayList<Board>();
		setAutomatic = false;
		colorOrder = true;
		numberOfMoves = 0;
		pawnEnPassant = false;
	}

	/**
	 * Clears the board from all the pieces
	 */
	public void clearBoard() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				tiles[x][y].setPiece(null);
			}
		}
	}

	/**
	 * Method that runs everytime a user click on a Tile and checks the convenient
	 * situation of the game with the moves, and will process a move if everything
	 * applies.
	 * 
	 * @param x an int coordinate
	 * @param y an int coordinate
	 */
	private void onTileClick(int x, int y) {
		Tile clickedTile = tiles[x][y];
		// if a piece is selected and the user didn't click on allied piece
		if (activeTile != null && activeTile.getPiece() != null
				&& clickedTile.getPieceColor() != activeTile.getPieceColor()) {
			MoveInformation p = new MoveInformation(activeTile.getX(), activeTile.getY(), x, y);
			// update the chess board
			boolean status = this.processMove(p, true);
			if (status && turnEnabled) {
				count++;
			}
			// decouples Tile from Tile on board
			this.setActiveTile(null);
		} else {
			// if there's a piece on the selected square when no active square
			if (tiles[x][y].getPiece() != null) {
				// make active square clicked square
				this.setActiveTile(tiles[x][y]);
			}
		}
	}

	/**
	 * Method that copies the Board
	 * 
	 * @return a copy of the Board
	 */
	public Board copyBoard() {
		Board b = new Board();

		for (int x = 0; x < tiles[0].length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				b.tiles[x][y] = new Tile(true, x, y);
			}
		}
		// clone the pieces
		for (int x = 0; x < tiles[0].length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				if (tiles[x][y].getPiece() != null) {
					b.tiles[x][y].setPiece((Piece) tiles[x][y].getPiece().clone());
				}
			}
		}
		b.whiteKingPosition = new int[2];
		b.whiteKingPosition[0] = whiteKingPosition[0];
		b.whiteKingPosition[1] = whiteKingPosition[1];
		b.blackKingPosition = new int[2];
		b.blackKingPosition[0] = blackKingPosition[0];
		b.blackKingPosition[1] = blackKingPosition[1];
		b.numberOfMoves = numberOfMoves;
		b.count = count;
		b.listOfBoards = new ArrayList<Board>();
		for (Board tmp : listOfBoards) {
			b.listOfBoards.add(tmp);
		}

		return b;
	}

	/**
	 * Method that checks if the king can move on a specific location. It basically
	 * check if its a legal move
	 * 
	 * @param color the color of the king
	 * @return a boolean
	 */
	private boolean isKingMoveLegal(boolean color, MoveInformation m) {
		boolean legalMove = false;

		Board tempBoard = this.copyBoard();
		if (tempBoard.processKingOnCopy(m)) {// the move has been processed
			ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
			if (tempArrayList.isEmpty()) {// if empty, there is no checks
				legalMove = true;
			}
		}
		return legalMove;
	}

	/**
	 * Method that checks if the King can move after being checked on any possible
	 * location on the board
	 * 
	 * @param color a boolean that determines the color of the king
	 * @return a boolean
	 */
	private boolean canKingMove(boolean color) {
		int kingX;
		int kingY;
		boolean canMove = false;

		// get the proper king coordinates
		if (color) {
			kingX = whiteKingPosition[0];
			kingY = whiteKingPosition[1];
		} else {
			kingX = blackKingPosition[0];
			kingY = blackKingPosition[1];
		}
		for (int x = 0; x < tiles[0].length && !canMove; x++) {
			for (int y = 0; y < tiles[0].length && !canMove; y++) {
				MoveInformation moveToTry = new MoveInformation(kingX, kingY, x, y);
				Board tempBoard = this.copyBoard();
				if (tempBoard.processKingOnCopy(moveToTry)) {// check if any move is valid on the copy
					ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
					if (tempArrayList.isEmpty()) {// check if on copy there is a move
						canMove = true;
					}
				}
			}
		}
		return canMove;
	}

	/**
	 * Method that update the copied board for a king only, and checks if by doing
	 * all possible moves for king is still in check or not, or if its a legal move
	 * 
	 * @param m a MoveInformation
	 * @return a boolean
	 */
	private boolean processKingOnCopy(MoveInformation m) {
		boolean valid = false;
		Tile oldTile = tiles[m.getOldX()][m.getOldY()];
		Tile newTile = tiles[m.getNewX()][m.getNewY()];
		Piece selectedPiece = oldTile.getPiece();
		// in case the king is in check do not allow the castle move.
		int distance = m.getNewX() - m.getOldX();

		if (oldTile.getPiece() != null) {
			if (isMoveInList(m) && Math.abs(distance) != 2) {
				newTile.setPiece(oldTile.removePiece());
				valid = true;
			}
			if (valid && !selectedPiece.color) { // black color
				// update king coord on the copied board
				blackKingPosition[0] = m.getNewX();
				blackKingPosition[1] = m.getNewY();
			} else if (valid) { // white color
				// update king coord on the copied board
				whiteKingPosition[0] = m.getNewX();
				whiteKingPosition[1] = m.getNewY();
			}
		}
		return valid;
	}

	/**
	 * Method that determines if any piece can defend its own king by eating the
	 * opponent attacking piece in a check situation
	 * 
	 * @param color      a boolean for the color of the player trying to eat the
	 *                   opponent piece
	 * @param checkTiles an ArrayList of Tile
	 * @return a boolean
	 */
	private boolean canAnyPieceEatOpp(boolean color, ArrayList<Tile> checkTiles) {
		boolean itCanEat = false;
		Tile attackingTile = checkTiles.get(0);
		int oppX = attackingTile.getX();
		int oppY = attackingTile.getY();

		// going over board and if piece on tile, try a move to opponent coordinates
		for (int x = 0; x < tiles[0].length && !itCanEat; x++) {
			for (int y = 0; y < tiles[0].length && !itCanEat; y++) {
				if (tiles[x][y].getPiece() != null && tiles[x][y].getPiece().color != color) {
					MoveInformation moveToTry = new MoveInformation(x, y, oppX, oppY);
					Board tempBoard = this.copyBoard();
					if (tempBoard.processMove(moveToTry, false)) {
						// even if its legal, by doing it it might open another threat to king
						ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
						if (tempArrayList.isEmpty()) {// no threat to king
							itCanEat = true;
						}
					}
				}
			}
		}
		return itCanEat;
	}

	/**
	 * Method that determines if any piece can block the check from opponent by
	 * trying all the possible legal moves, and see if its still a check for the
	 * king.
	 * 
	 * @param color a boolean representing the color of the king to protect
	 * @return a boolean
	 */
	private boolean canAnyPieceProtect(boolean color) {
		boolean canProtect = false;
		// Going through the whole board and checking if any
		// Piece can stop the check situation
		for (int x = 0; x < tiles[0].length && !canProtect; x++) {
			for (int y = 0; y < tiles[0].length && !canProtect; y++) {
				if (tiles[x][y].getPiece() != null && tiles[x][y].getPiece().color == color
						&& tiles[x][y].getPiece().getClass() != King.class) {
					for (int x1 = 0; x1 < tiles[0].length && !canProtect; x1++) {
						for (int y1 = 0; y1 < tiles[0].length && !canProtect; y1++) {
							MoveInformation moveToTry = new MoveInformation(x, y, x1, y1);
							Board tempBoard = this.copyBoard();
							if (tempBoard.processMove(moveToTry, false)) {// legal
								ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
								if (tempArrayList.isEmpty()) {// no threat to king
									canProtect = true;
								}
							}
						}
					}
				}
			}
		}
		return canProtect;
	}

	/**
	 * Method to analyze the board and see if its a checkmate
	 * 
	 * @param color      a Boolean that determine the color of the King
	 * @param checkTiles an ArrayList that contains the checkTiles
	 * @return a boolean
	 */
	private boolean isCheckMate(boolean color, ArrayList<Tile> checkTiles) {
		boolean checkMate = true;
		if (checkTiles.isEmpty()) {
			checkMate = false;
		}
		// if check by 2 pieces, only way to escape is to move the king
		// cannot eat or block both attacking pieces in one move.
		else if (checkTiles.size() > 1) {
			if (canKingMove(color))
				checkMate = false;
		} else {// attacked by one piece
			if (canKingMove(color) || canAnyPieceEatOpp(color, checkTiles) || canAnyPieceProtect(color)) {
				checkMate = false;
			}
		}
		return checkMate;
	}

	/**
	 * Method for castleDoneLegally White color that checks if the castle is being
	 * blocked by an opponent piece
	 * 
	 * @param color a boolean that represent the piece color
	 * @param right a boolean that represent if its on the right or left
	 * @return a boolean
	 */
	private boolean castleBlockedW(boolean color, boolean right) {
		boolean castleBlocked = false;
		if (right) {
			Board tempBoard = this.copyBoard();
			for (int x = 4; x < 6; x++) {
				MoveInformation m = new MoveInformation(x, 0, x + 1, 0);
				if (tempBoard.processKingOnCopy(m)) {// valid
					ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
					if (!tempArrayList.isEmpty()) {// check if on copy there is a block
						castleBlocked = true;
					}
				}
			}
		} else {// left
			Board tempBoard = this.copyBoard();
			for (int x = 4; x > 2; x--) {
				MoveInformation m = new MoveInformation(x, 0, x - 1, 0);
				if (tempBoard.processKingOnCopy(m)) {// valid
					ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
					if (!tempArrayList.isEmpty()) {// check of on copy there is a block
						castleBlocked = true;
					}
				}
			}
		}
		return castleBlocked;
	}

	/**
	 * Method for castleDoneLegally Black color that checks if the castle is being
	 * blocked by an opponent piece
	 * 
	 * @param color a boolean that represente the piece color
	 * @param right a boolean that represent if its on the right or left
	 * @return a boolean
	 */
	private boolean castleBlockedB(boolean color, boolean right) {
		boolean castleBlocked = false;
		if (right) {
			Board tempBoard = this.copyBoard();
			for (int x = 4; x < 6; x++) {
				MoveInformation m = new MoveInformation(x, 7, x + 1, 7);
				if (tempBoard.processKingOnCopy(m)) {// valid
					ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
					if (!tempArrayList.isEmpty()) {// check if on copy there is a block
						castleBlocked = true;
					}
				}
			}
		} else {// left
			Board tempBoard = this.copyBoard();
			for (int x = 4; x > 2; x--) {
				MoveInformation m = new MoveInformation(x, 7, x - 1, 7);
				if (tempBoard.processKingOnCopy(m)) {// valid
					ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
					if (!tempArrayList.isEmpty()) {// check if on copy there is a block
						castleBlocked = true;
					}
				}
			}
		}
		return castleBlocked;
	}

	/**
	 * Method that checks if a piece is blocking the castle for its opponent, which
	 * means that the piece has a legal move in one of the Tiles in which the king
	 * will be passing through or landing on while doing the castle.
	 * 
	 * @param color a boolean that represents the color of the piece
	 * @param right a boolean that represent if its on the right or left
	 * @return a boolean
	 */
	private boolean castleDoneLegally(boolean color, boolean right) {
		boolean castleNotBlocked = true;

		if (color) {// White
			if (castleBlockedW(color, right)) {
				castleNotBlocked = false;
			}
		} else {// Black
			if (castleBlockedB(color, right)) {
				castleNotBlocked = false;
			}
		}
		return castleNotBlocked;
	}

	/**
	 * Method that checks if the castling can be done with the rook and make sure
	 * that both pieces did not move already in the game and that no opponent piece
	 * is blocking the castle
	 * 
	 * @param oldTile a Tile that represent the oldTile on the board
	 * @param newTile a Tile that represent the newTile on the board
	 * @return a boolean
	 */
	private boolean castlingTry(Tile oldTile, Tile newTile) {
		boolean castlingAvailable = false;
		Piece selectedPiece = oldTile.getPiece();// king piece
		Piece whiteRookR = tiles[7][0].getPiece();// right W-rook
		Piece whiteRookL = tiles[0][0].getPiece();// left W-rook
		Piece blackRookR = tiles[7][7].getPiece();// right B-rook
		Piece blackRookL = tiles[0][7].getPiece();// left B-rook

		if (selectedPiece.color) { // White King
			if (newTile.getX() == 6 && whiteRookR != null && !whiteRookR.hasMoved && tiles[5][0].getPiece() == null) { // right
																														// and
																														// no
																														// pieces
																														// between
				if (castleDoneLegally(selectedPiece.color, true)) {// no checks onProcess
					newTile.setPiece(oldTile.removePiece());
					tiles[5][0].setPiece(tiles[7][0].removePiece());
					castlingAvailable = true;
				} else {// illegal
					GUI.displayIllegalCastling();
				}
			}
			if (newTile.getX() == 2 && whiteRookL != null && !whiteRookL.hasMoved && !castlingAvailable
					&& tiles[1][0].getPiece() == null && tiles[3][0].getPiece() == null) {// left and no pieces between
				if (castleDoneLegally(selectedPiece.color, false)) {// no checks onProcess
					newTile.setPiece(oldTile.removePiece());
					tiles[3][0].setPiece(tiles[0][0].removePiece());
					castlingAvailable = true;
				} else {// illegal
					GUI.displayIllegalCastling();
				}
			}
		} else if (!selectedPiece.color) { // Black king
			if (newTile.getX() == 6 && blackRookR != null && !blackRookR.hasMoved && tiles[5][7].getPiece() == null) { // right
																														// and
																														// no
																														// piece
																														// between
				if (castleDoneLegally(selectedPiece.color, true)) {// no checks onProcess
					newTile.setPiece(oldTile.removePiece());
					tiles[5][7].setPiece(tiles[7][7].removePiece());
					castlingAvailable = true;
				} else {// illegal
					GUI.displayIllegalCastling();
				}
			}
			if (newTile.getX() == 2 && blackRookL != null && !blackRookL.hasMoved && !castlingAvailable
					&& tiles[1][7].getPiece() == null && tiles[3][7].getPiece() == null) {// left and no pieces between
				if (castleDoneLegally(selectedPiece.color, false)) {// no checks onProcess
					newTile.setPiece(oldTile.removePiece());
					tiles[3][7].setPiece(tiles[0][7].removePiece());
					castlingAvailable = true;
				} else {// illegal
					GUI.displayIllegalCastling();
				}
			}
		}
		return castlingAvailable;
	}

	/**
	 * Method that displays the status of a king after a move have been done
	 */
	private void displayKingStatus() {
		ArrayList<Tile> checkTilesW = isKingChecked(true);
		ArrayList<Tile> checkTilesB = isKingChecked(false);
		if (!checkTilesW.isEmpty()) {// if there is a check on white
			if (isCheckMate(true, checkTilesW))// checkmate
				GUI.displayCheckMateInformation("White");
			else {
				GUI.displayCheckInformation("White");
			}
		} else if (!checkTilesB.isEmpty()) {// if there is a check on black
			if (isCheckMate(false, checkTilesB))// checkmate
				GUI.displayCheckMateInformation("Black");
			else {
				GUI.displayCheckInformation("Black");
			}
		}
	}

	/**
	 * Check if everything applies then process a move after it has been made by a
	 * player and set the updates of the board
	 * 
	 * @param p         a MoveInformation
	 * @param toDisplay a boolean that determines if display the Alert is on the
	 *                  current board and that the method is being runned on a
	 *                  copied board or actual board.
	 * @return a boolean
	 */
	private boolean processMove(MoveInformation p, boolean toDisplay) {

		boolean done = false;
		boolean illegalMove = false;
		boolean continueGame = false;
		Tile oldTile = tiles[p.getOldX()][p.getOldY()];
		Tile newTile = tiles[p.getNewX()][p.getNewY()];
		Piece selectedPiece = oldTile.getPiece();

		// Creating these ArrayLists now is the status before a move have been done
		ArrayList<Tile> checkTilesW = isKingChecked(true);// White in check if not empty
		ArrayList<Tile> checkTilesB = isKingChecked(false);// Black in check if not empty
		boolean color = selectedPiece.color;

		if (turnEnabled) {// user has turn enabled
			boolean turnCheck = (count % 2 == 0);// determines who's turn it is
			if ((turnCheck && color) || (!turnCheck && !color)) {// if correct player is playing
				continueGame = true;
			}
		} else {
			continueGame = true;
		} // turn was disabled so just continue game normally

		if (isMoveInList(p) && continueGame) {
			if (selectedPiece.getClass() == Pawn.class) {
				if (pawnProcessingMove(p, newTile, oldTile, toDisplay)) {
					done = true;
				}
			} else if (selectedPiece.getClass() == King.class) {
				// if its a double tile move from king, check castle method
				if ((Math.abs(p.getNewX() - p.getOldX()) == 2)) {
					if (color && checkTilesW.isEmpty()) {// white king and not in check
						if (castlingTry(oldTile, newTile)) {// then allow castling
							done = true;
							pawnEnPassant = false;
						}
					} else if (!color && checkTilesB.isEmpty()) {// black king and not in check
						if (castlingTry(oldTile, newTile)) {// then allow castling
							done = true;
							pawnEnPassant = false;
						}
					}
				} else if (Math.abs(oldTile.getX() - newTile.getX()) != 2) { // normal move
					if (isKingMoveLegal(color, p)) {// if move won't occur in check threat
						newTile.setPiece(oldTile.removePiece());
						done = true;
						pawnEnPassant = false;
					}
				}
				// Updating King coordinates
				if (!color && done) { // black color
					blackKingPosition[0] = p.getNewX();
					blackKingPosition[1] = p.getNewY();
				} else if (done) { // white color
					whiteKingPosition[0] = p.getNewX();
					whiteKingPosition[1] = p.getNewY();
				}
			} else {// neither a pawn or king
				if (pieceSafeMoveTest(p, color, false)) {
					newTile.setPiece(oldTile.removePiece());
					done = true;
					pawnEnPassant = false;
				} else {
					illegalMove = true;
				}
			}
		} else if (IsPawnEnPassanLegal(p) && continueGame) { // enPassant movement for pawns
			if (pieceSafeMoveTest(p, color, true)) {// own king not in check after move processed
				newTile.setPiece(oldTile.removePiece());
				tiles[enPassantCoordinates[0]][enPassantCoordinates[1]].removePiece();
				done = true;
				pawnEnPassant = false;
			} else {
				illegalMove = true;
			}
		}
		if (done) {
			selectedPiece.setHasMoved(true);
			selectedPiece.x = p.getNewX();
			selectedPiece.y = p.getNewY();
			numberOfMoves++;
			if (setAutomatic) {// change rotation automatically
				this.changeSide();
			}
		}
		if (toDisplay && continueGame) {// not being runned in a copied board and turn for this player
			displayKingStatus();// new ArrayList for checkKing status
			if (done) {
				Board b = this.copyBoard();
				listOfBoards.add(b);
			}
			if (illegalMove && illegalDisplayEnabled) {// if its an illegal move and display enabled
				GUI.displayIllegalInfo();
			}
		}
		return done;
	}

	/**
	 * Method that checks if processing a piece move do not occur in check in
	 * player's own king, and process the move on a copied board to check the
	 * alternative result.
	 * 
	 * @param p             a MoveInformation
	 * @param color         a boolean representing the color of king
	 * @param enPassantCase a boolean representing if its a enPassant move
	 */
	private boolean pieceSafeMoveTest(MoveInformation p, boolean color, boolean enPassantCase) {
		boolean isSafeMove = false;

		Board tempBoard = this.copyBoard();
		Tile oldTile = tempBoard.tiles[p.getOldX()][p.getOldY()];
		Tile newTile = tempBoard.tiles[p.getNewX()][p.getNewY()];
		// Since this method is being runned, it means that the move
		// is initially in the ListOfMoves.
		newTile.setPiece(oldTile.removePiece());
		if (enPassantCase) {// it is a enPassant case with pawns
			tempBoard.tiles[enPassantCoordinates[0]][enPassantCoordinates[1]].removePiece();
		}
		ArrayList<Tile> tempArrayList = tempBoard.isKingChecked(color);
		if (tempArrayList.isEmpty()) {// if no check
			isSafeMove = true;
		}
		return isSafeMove;
	}

	/**
	 * Method that process a pawn move if its a valid move
	 * 
	 * @param p         a MoveInformation
	 * @param newTile   representing the new tile to move to
	 * @param oldTile   representing the old tile to move to
	 * @param toDisplay a boolean representing if its on copy or actual board
	 * @return a boolean
	 */
	private boolean pawnProcessingMove(MoveInformation p, Tile newTile, Tile oldTile, boolean toDisplay) {
		Piece choicePiece = null;
		boolean moveProcessed = false;
		boolean color = oldTile.getPiece().color;

		if (p.getNewY() == 7) { // White pawnPromotion
			if (pieceSafeMoveTest(p, true, false)) {// Safe move that do not occur own king in check
				choicePiece = GUI.pawnPromotion(p.getNewX(), p.getNewY(), color);
				if (choicePiece != null) {
					newTile.setPiece(choicePiece);
					oldTile.removePiece();
					pawnEnPassant = false;
					moveProcessed = true;
				}
			} else if (toDisplay && illegalDisplayEnabled) {
				GUI.displayIllegalInfo();
			}
		} else if (p.getNewY() == 0) { // Black pawnPromotion
			if (pieceSafeMoveTest(p, false, false)) {// safe move that do not occur own king in check
				choicePiece = GUI.pawnPromotion(p.getNewX(), p.getNewY(), color);
				if (choicePiece != null) {
					newTile.setPiece(choicePiece);
					oldTile.removePiece();
					pawnEnPassant = false;
					moveProcessed = true;
				}
			} else if (toDisplay && illegalDisplayEnabled) {
				GUI.displayIllegalInfo();
			}
		} else {// move not on the end of the board
			if (oldTile.getPiece().color) {// white piece
				if (pieceSafeMoveTest(p, true, false)) {// safe move that do not occur own king in check
					newTile.setPiece(oldTile.removePiece());
					moveProcessed = true;
				} else if (toDisplay && illegalDisplayEnabled) {
					GUI.displayIllegalInfo();
				}
			} else {// black piece
				if (pieceSafeMoveTest(p, false, false)) {// safe move that do not occur own king in check
					newTile.setPiece(oldTile.removePiece());
					moveProcessed = true;
				} else if (toDisplay && illegalDisplayEnabled) {
					GUI.displayIllegalInfo();
				}
			}
			if (Math.abs(newTile.getY() - oldTile.getY()) == 2) {// pawn moved 2 tiles
				// set enPassant to true and store piece coordinates
				pawnEnPassant = true;
				enPassantCoordinates[0] = newTile.getX();
				enPassantCoordinates[1] = newTile.getY();
			} else {// did not move 2 tiles so reset enPassant boolean
				pawnEnPassant = false;
			}
		}
		return moveProcessed;
	}

	/**
	 * Method that determines if a pawn can attack enPassant
	 * 
	 * @param m a MoveInformation
	 * @return a boolean
	 */
	private boolean IsPawnEnPassanLegal(MoveInformation m) {
		boolean validMove = false;

		if (pawnEnPassant) {// If last move was of 2 tiles for a pawn
			boolean pieceColor = tiles[m.getOldX()][m.getOldY()].getPiece().color;
			int x = enPassantCoordinates[0];// store coordinates
			int y = enPassantCoordinates[1];// store coordinates
			Tile tileToAttack = tiles[x][y];// store as a Tile

			if (tileToAttack.getPiece().color != pieceColor) {// not the same color
				if (m.getNewX() == tileToAttack.getX()
						&& (m.getOldX() == tileToAttack.getX() - 1 || m.getOldX() == tileToAttack.getX() + 1)) { // both
																													// pawns
																													// are
																													// next
																													// to
																													// each
																													// others
					if (pieceColor) {// dealing with white pawn
						if (m.getNewY() == tileToAttack.getY() + 1 && m.getOldY() == tileToAttack.getY()) {// move goes
																											// positive
																											// in y
							validMove = true;
						}
					} else if (m.getNewY() == tileToAttack.getY() - 1 && m.getOldY() == tileToAttack.getY()) {// black
																												// pawn
																												// goes
																												// negative
																												// in y
						validMove = true;
					}
				}
			}
		}
		return validMove;
	}

	/**
	 * Method that determines if a move made on the chess board is a move within the
	 * available list of moves
	 * 
	 * @param p a MoveInformation
	 * @return a boolean
	 */
	private boolean isMoveInList(MoveInformation p) {
		Tile oldTile;
		Tile newTile;
		Piece piece;
		ListOfMoves[] moves;

		if (p == null) {// if MoveInformation is null
			return false;
		}
		try {// Check if oldTile in range
			oldTile = tiles[p.getOldX()][p.getOldY()];
		} catch (NullPointerException e) {
			return false;
		}
		try {// Check if newTile in range
			newTile = tiles[p.getNewX()][p.getNewY()];
		} catch (NullPointerException e) {
			return false;
		}
		if (!oldTile.isTileOccupied()) {// Check if oldTile is empty
			return false;
		}

		piece = oldTile.getPiece();
		moves = piece.getMoves();
		boolean matchesPieceMoves = false;

		// variables for Pieces that move more than 1 base move (Bishop, Rook, Queen)
		int multiMoveCount;
		int stretchedMoveX;
		int stretchedMoveY;
		// I labeled the loop to be able to break out more easily
		MoveLoop: for (ListOfMoves m : moves) {
			multiMoveCount = 1;
			if (piece.hasSingleMove() == false) {
				multiMoveCount = 8;
			}
			boolean hasCollided = false;
			for (int c = 1; c <= multiMoveCount; c++) {
				// if it hit a piece of opponent's color, check next move
				if (hasCollided) {
					break;
				}
				// stretches move to see if it matches the move made
				stretchedMoveX = m.getX() * c;
				stretchedMoveY = m.getY() * c;
				Tile tempTile;
				// If out of bounds, go to next move of the piece
				try {
					tempTile = tiles[p.getOldX() + stretchedMoveX][p.getOldY() + stretchedMoveY];
				} catch (Exception e) {
					break;
				}
				// handles piece collision and capturing
				if (tempTile.isTileOccupied()) {
					hasCollided = true;
					boolean piecesSameColor = tempTile.getPiece().getColor() == oldTile.getPiece().getColor();
					// stops checking this move if pieces are the same color
					if (piecesSameColor) {
						break;
					}
				}
				// if stretched move matches made move
				if (p.getGapX() == stretchedMoveX && p.getGapY() == stretchedMoveY) {
					matchesPieceMoves = true;
					if (pawnCheckingMove(p) == false) {
						return false;
					}
					break MoveLoop;
				}
			}
		}
		if (!matchesPieceMoves) {
			return false;
		}
		return true;
	}

	/**
	 * Determine if the piece is a pawn and if it is allowed to move legally
	 * 
	 * @param p a MoveInformation
	 * @return a boolean true if its valid and false otherwise
	 */
	private boolean pawnCheckingMove(MoveInformation p) {
		// this should only be called in moveIsValid, so checks are done there
		Tile oldTile = tiles[p.getOldX()][p.getOldY()];
		Tile newTile = tiles[p.getNewX()][p.getNewY()];
		Piece piece = oldTile.getPiece();
		boolean isPawn = false;
		boolean pawnChecked = true;

		// If it's not a pawn, it passes
		if (!piece.getName().equals("pawn")) {
			isPawn = true;
			;
		}
		if (!isPawn) {
			if (p.getGapX() == 0) {
				// black is negative 1, white is positive 1
				int colorMod = p.getGapY() / Math.abs(p.getGapY());
				// if there's a piece in the way for a straight move, don't allow move
				for (int c = 1; c <= Math.abs(p.getGapY()); c++) {
					if (tiles[p.getOldX()][p.getOldY() + (c * colorMod)].isTileOccupied()) {
						pawnChecked = false;
					}
				}
			} else {// if it's a diagonal move
				// if the target square doesn't have an opposing piece, don't allow move
				if ((!newTile.isTileOccupied()) || piece.getColor() == newTile.getPiece().getColor()) {
					pawnChecked = false;
				}
			}
		}
		return pawnChecked;
	}

	/**
	 * Determines if the King is in check
	 * 
	 * @param color a String that determines the color of the king
	 * @return the arrayList that contains all the tiles that has a valid move
	 *         towards opponent king.
	 */
	private ArrayList<Tile> isKingChecked(boolean color) {
		int kingX;
		int kingY;
		ArrayList<Tile> checkTiles = new ArrayList<Tile>();

		if (color) {// white king coordinates
			kingX = whiteKingPosition[0];
			kingY = whiteKingPosition[1];
		} else {// black king coordinates
			kingX = blackKingPosition[0];
			kingY = blackKingPosition[1];
		}

		for (int x = 0; x < tiles[0].length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				if (tiles[x][y].isTileOccupied() && tiles[x][y].getPiece().color != color) {
					// if there is a piece and not same color then the king we are analyzing
					MoveInformation moveInfo = new MoveInformation(x, y, kingX, kingY);
					if (isMoveInList(moveInfo))// if move is valid towards the king
						checkTiles.add(tiles[x][y]);// not empty therefore king is checked
				}
			}
		}
		return checkTiles;
	}

	/**
	 * Get the Tile on these coordinates
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 * @return a Tile
	 */
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}

	/**
	 * Set up the colors of the tiles on the board
	 * 
	 * @param t a Tile
	 */
	private void setActiveTile(Tile t) {
		// Remove style from old active tile
		if (this.activeTile != null)
			this.activeTile.getStyleClass().removeAll("chessActiveTile");

		this.activeTile = t;

		// Add style to new active tile
		if (this.activeTile != null)
			this.activeTile.getStyleClass().add("chessActiveTile");
	}

	/**
	 * Method that sets the turnEnabled boolean
	 */
	public void setTurn(boolean enable) {
		this.turnEnabled = enable;
	}

	/**
	 * Method that sets the illegalDisplayEnabled boolean
	 */
	public void setIllegalDisplayEnabled(boolean enable) {
		this.illegalDisplayEnabled = enable;
	}

	/**
	 * Method that gets the turn in the game
	 * 
	 * @return an int count
	 */
	public int getTurn() {
		return this.count;
	}

	/**
	 * Method that gets the turnEnabled boolean
	 * 
	 * @return a boolean
	 */
	public boolean getTurnEnabled() {
		return this.turnEnabled;
	}

	/**
	 * Gets the activeTile
	 * 
	 * @return a Tile
	 */
	public Tile getActiveTile() {
		return this.activeTile;
	}

	/**
	 * Method that switches the players on the board everytime the turn is changed
	 */
	public void changeSide() {
		// clear all tiles from the board
		this.getChildren().clear();
		// if colorOrder, then white are on bottom so rotate
		// and put white on top and black on bottom
		for (int x = 0; x < tiles[0].length && colorOrder; x++) {
			for (int y = 0; y < tiles[0].length && colorOrder; y++) {
				this.add(tiles[7 - x][7 - y], x, 7 - y);
			}
		}
		// else, its the opposite
		for (int x = 0; x < tiles[0].length && !colorOrder; x++) {
			for (int y = 0; y < tiles[0].length && !colorOrder; y++) {
				this.add(tiles[7 - x][7 - y], 7 - x, y);
			}
		}
		colorOrder = !colorOrder;
	}

	/**
	 * Method to set Automatically the rotation of the board
	 */
	public void setAutomatic() {
		this.setAutomatic = true;
	}

	/**
	 * Method to set Manually the rotation of the board
	 */
	public void setManually() {
		this.setAutomatic = false;
	}

	/**
	 * Method to get the setAutomatic boolean instance
	 */
	public boolean getAutomatic() {
		return this.setAutomatic;
	}

	/**
	 * Method that gets the ColorOrder to know how the board is currently rotated
	 */
	public boolean getColorOrder() {
		return this.colorOrder;
	}
}