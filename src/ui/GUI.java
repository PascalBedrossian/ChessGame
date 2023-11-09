package ui;

import java.util.ArrayList;
import java.util.Optional;

import board.Board;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pieces.Bishop;
import pieces.Knight;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/**
 * I learned a lot from my chess project. I was first worried about how I was
 * going to make a GUI, but I ended up figuring it out and still have a lot more
 * to learn. My main class for my project that takes care of the flow of the
 * game is my board class. This project took me a considerable time to achieve,
 * but I am happy I learned a lot and gained knowledge from this project. This
 * project was so fun and taught me a lot of new stuff, that I will probably
 * continue implementing new features in my project later on that I didn't have
 * time to implement during the semester. It was a very nice experience that
 * helped me understand OOD even more.
 * 
 * @author Pascal Bedrossian
 */

public class GUI extends Application {
	public static void main(String[] args) {
		try {
			launch(args);
			System.exit(0);
		} catch (Exception error) {
			error.printStackTrace();
			System.exit(0);
		}
	}

	private Board board;
	private BorderPane root;
	private MenuBar menuBar;
	private Stage mainStage;
	private String css;
	private Scene mainScene;

	@Override
	public void start(Stage mainStage) {
		initializeGui(mainStage);
	}

	/**
	 * Method that initialize the GUI
	 * 
	 * @param mainStage
	 */
	private void initializeGui(Stage mainStage) {
		mainStage.setTitle("Chess Game");
		mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/app_icon.png")));

		css = "/sheet-for-styles.css";

		root = new BorderPane();
		mainScene = new Scene(root);
		mainStage.setScene(mainScene);
		mainScene.getStylesheets().add(css);// add stylesheet
		board = new Board(true);// drawing chess board
		root.setCenter(board);
		menuBar = generateMenuBar();// adding menuBars
		root.setTop(menuBar);
		mainStage.show();
	}

	/**
	 * Method that allow the user to chose his piece for promotion
	 * 
	 * @param newX  x coordinates of the location to promote a Piece
	 * @param newY  y coordinates of the location to promote a Piece
	 * @param color a boolean that determines the color for promotion
	 * @return a Piece chosen by the user
	 */
	public static Piece pawnPromotion(int newX, int newY, boolean color) {
		Piece desiredPiece = null;
		ArrayList<String> choices = new ArrayList<String>();
		choices.add("Queen");
		choices.add("Bishop");
		choices.add("Knight");
		choices.add("Rook");
		ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", choices);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText("Pawn Promotion");
		dialog.setContentText("Choose your Piece:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (result.get() == "Queen")
				desiredPiece = new Queen(color, newX, newY);
			else if (result.get() == "Bishop")
				desiredPiece = new Bishop(color, newX, newY);
			else if (result.get() == "Knight")
				desiredPiece = new Knight(color, newX, newY);
			else if (result.get() == "Rook")
				desiredPiece = new Rook(color, newX, newY);
		}
		return desiredPiece;
	}

	/**
	 * Method that Quit the program
	 */
	public void onQuit() {
		Platform.exit();
		System.exit(0);
	}

	/**
	 * Method to disable the turn in game
	 */
	public void enableTurn() {
		board.setTurn(true);
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Turn Status");
		infoAlert.setHeaderText(null);
		infoAlert.setContentText("Turn is now enabled");
		infoAlert.showAndWait();
	}

	/**
	 * Method to disable the turn in game
	 */
	public void disableTurn() {
		board.setTurn(false);
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Turn Status");
		infoAlert.setHeaderText(null);
		infoAlert.setContentText("Turn is now disabled");
		infoAlert.showAndWait();
	}

	/**
	 * Method to enable Illegal moves Alert
	 */
	public void enableIllegalAlerts() {
		board.setIllegalDisplayEnabled(true);
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Illegal Display Status");
		infoAlert.setHeaderText(null);
		infoAlert.setContentText("Illegal moves alerts are now enabled");
		infoAlert.showAndWait();
	}

	/**
	 * Method to disable Illegal moves Alert
	 */
	public void disableIllegalAlerts() {
		board.setIllegalDisplayEnabled(false);
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Illegal Display Status");
		infoAlert.setHeaderText(null);
		infoAlert.setContentText("Illegal moves alerts are now disabled");
		infoAlert.showAndWait();
	}

	/**
	 * Method to display who's turn it is
	 */
	public void whoTurn() {
		int turn = board.getTurn();
		boolean turnCheck = (turn % 2 == 0);
		boolean turnEnabled = board.getTurnEnabled();
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Turn Status");
		infoAlert.setHeaderText(null);
		if (turnCheck && turnEnabled) {
			infoAlert.setContentText("It's White Player's turn");
		} else if (turnEnabled) {
			infoAlert.setContentText("It's Black Player's turn");
		} else {
			infoAlert.setContentText("Turn was disabled, I can't follow up");
		}
		infoAlert.showAndWait();
	}

	/**
	 * Displays an explanation of the program for users.
	 */
	public void displayAbout() {
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("About this program");
		infoAlert.setHeaderText(null);
		infoAlert.setContentText(
				"Programmed by Pascal Bedrossian.\n" + "Project for Object Oriented Design CS360 class Spring 2020.\n"
						+ "Completely working chess Game with all rules applied\n\n");
		infoAlert.showAndWait();
	}

	/**
	 * Method that display an illegal message Alert for castling
	 */
	public static void displayIllegalCastling() {
		Alert infoAlert = new Alert(AlertType.WARNING);
		infoAlert.setTitle("Move Information");
		infoAlert.setContentText("Illegal castling. Try something else!");
		infoAlert.showAndWait();
	}

	/**
	 * Method that displays an illegal message Alert
	 */
	public static void displayIllegalInfo() {
		Alert infoAlert = new Alert(AlertType.WARNING);
		infoAlert.setTitle("Move Information");
		infoAlert.setContentText("Illegal move. Try another one!");
		infoAlert.showAndWait();
	}

	/**
	 * Method that displays an alert of check
	 * 
	 * @param color a String that determines the color of the king
	 */
	public static void displayCheckInformation(String color) {
		Alert infoAlert = new Alert(AlertType.WARNING);
		infoAlert.setTitle("Check Status");
		infoAlert.setContentText(color + " king is in Check");
		infoAlert.showAndWait();
	}

	/**
	 * Method that displays an alert of checkMate
	 * 
	 * @param color a String that determines the color of the king
	 */
	public static void displayCheckMateInformation(String color) {
		Alert infoAlert = new Alert(AlertType.WARNING);
		infoAlert.setTitle("Check Status");
		infoAlert.setContentText(color + " king is in CheckMate");
		infoAlert.showAndWait();
	}

	/**
	 * Method to restart the Game
	 */
	private void restartGame() {
		board.clearBoard();
		if (!board.getColorOrder()) {// black are on bottom
			board.changeSide();
		}
		board.setupStartPositions();
	}

	/**
	 * Method to be called when the user wants to rotate the Board
	 */
	public void rotateBoardManually() {
		if (board.getAutomatic()) {// if it was set to automatic
			board.setManually();// set it to manual
			Alert infoAlert = new Alert(AlertType.INFORMATION);
			infoAlert.setTitle("Rotation Status");
			infoAlert.setHeaderText(null);
			infoAlert.setContentText("Rotation is now set to Manual!");
			infoAlert.showAndWait();
			board.changeSide();// rotate the board
		} else {// it is set to manual already
			board.changeSide();
		}
	}

	/**
	 * Method that will set the boolean setAutomatic in my Board class to true so it
	 * will rotate the Board automatically.
	 */
	public void rotateBoardAutomatic() {
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Rotation Status");
		infoAlert.setHeaderText(null);
		infoAlert.setContentText("Rotation is now set to Automatic!");
		if (!board.getAutomatic()) {// if it is already set to Automatic, do not show alert
			board.setAutomatic();
			infoAlert.showAndWait();
		}
	}

	/**
	 * Method to display the options for a user after Surrendering or game finished
	 * 
	 * @param color a String that represent the player who surrendered
	 */
	public void displayOptions(String color) {
		Alert displayOptions = new Alert(AlertType.CONFIRMATION);
		displayOptions.setTitle("Game Status");
		displayOptions.setHeaderText(null);
		if (color == "Black") {
			displayOptions.setContentText("White player has Won!\n" + "Select an option:");
		} else {
			displayOptions.setContentText("Black player has Won!\n" + "Select an option:");
		}

		ButtonType restartGame = new ButtonType("Restart");
		ButtonType quitGame = new ButtonType("Quit");
		ButtonType showGame = new ButtonType("Analyze Game");

		displayOptions.getButtonTypes().setAll(restartGame, quitGame, showGame);
		Optional<ButtonType> result = displayOptions.showAndWait();

		if (result.get() == restartGame)
			restartGame();
		else if (result.get() == quitGame)
			onQuit();
		else if (result.get() == showGame) {
		}
	}

	/**
	 * Method that prompt the user to surrender or cancel his action.
	 */
	public void surrenderGame() {
		Alert newSurrenderAlert = new Alert(AlertType.CONFIRMATION);
		newSurrenderAlert.setTitle("Surrender Option");
		newSurrenderAlert.setHeaderText(null);
		newSurrenderAlert.setContentText("Who is Surrendering?");

		ButtonType blackSurrender = new ButtonType("Black");
		ButtonType whiteSurrender = new ButtonType("White");
		ButtonType cancelButton = new ButtonType("Cancel");

		newSurrenderAlert.getButtonTypes().setAll(blackSurrender, whiteSurrender, cancelButton);
		Optional<ButtonType> result = newSurrenderAlert.showAndWait();

		if (result.get() == blackSurrender)
			displayOptions("Black");
		else if (result.get() == whiteSurrender)
			displayOptions("White");
		else if (result.get() == cancelButton) {
		}
	}

	/**
	 * Generate the menu bar
	 * 
	 * @return a MenuBar
	 */
	private MenuBar generateMenuBar() {
		MenuBar menuBar = new MenuBar();

		Menu gameMenu = new Menu("Game");
		menuBar.getMenus().add(gameMenu);

		MenuItem menuItemQuit = new MenuItem("Quit");
		menuItemQuit.setOnAction(e -> onQuit());
		gameMenu.getItems().add(menuItemQuit);

		Menu menuHelp = new Menu("Help");
		menuBar.getMenus().add(menuHelp);

		MenuItem menuItemAbout = new MenuItem("About");
		menuItemAbout.setOnAction(e -> displayAbout());
		menuHelp.getItems().add(menuItemAbout);

		MenuItem menuRestart = new MenuItem("Restart");
		menuRestart.setOnAction(e -> restartGame());
		gameMenu.getItems().add(menuRestart);

		MenuItem menuSurrender = new MenuItem("Surrender");
		menuSurrender.setOnAction(e -> surrenderGame());
		gameMenu.getItems().add(menuSurrender);

		Menu menuTurn = new Menu("Turn");
		menuBar.getMenus().add(menuTurn);

		MenuItem turnEnable = new MenuItem("Enable");
		turnEnable.setOnAction(e -> enableTurn());
		menuTurn.getItems().add(turnEnable);

		MenuItem turnDisable = new MenuItem("Disable");
		turnDisable.setOnAction(e -> disableTurn());
		menuTurn.getItems().add(turnDisable);

		MenuItem playerTurn = new MenuItem("Who's Turn?");
		playerTurn.setOnAction(e -> whoTurn());
		menuTurn.getItems().add(playerTurn);

		Menu menuDisplay = new Menu("Display");
		menuBar.getMenus().add(menuDisplay);

		MenuItem displayDisabled = new MenuItem("Disable Illegal Alerts");
		displayDisabled.setOnAction(e -> disableIllegalAlerts());
		menuDisplay.getItems().add(displayDisabled);

		MenuItem displayEnabled = new MenuItem("Enable Illegal Alerts");
		displayEnabled.setOnAction(e -> enableIllegalAlerts());
		menuDisplay.getItems().add(displayEnabled);

		Menu menuChangeSide = new Menu("Rotation");
		menuBar.getMenus().add(menuChangeSide);

		MenuItem setAutomatic = new MenuItem("Automatic");
		setAutomatic.setOnAction(e -> rotateBoardAutomatic());
		menuChangeSide.getItems().add(setAutomatic);

		MenuItem setManually = new MenuItem("Manually");
		setManually.setOnAction(e -> rotateBoardManually());
		menuChangeSide.getItems().add(setManually);

		return menuBar;
	}

	/**
	 * Get method for board
	 * 
	 * return this Board
	 */
	public Board getBoard() {
		return this.board;
	}
}