package jackaroo.view;

import engine.Game;
import engine.board.Board;
import engine.board.Cell;
import engine.board.CellType;
import engine.board.SafeZone;
import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;
import model.Colour;
import model.card.Card;
import model.card.standard.Seven;
import model.card.standard.Standard;
import model.card.wild.Wild;
import model.player.Marble;
import model.player.Player;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public class GameController {

	
	
	
	private static final int CELL_SIZE = 42;
	private static final int BOARD_GRID_SIZE = 15; 

	
	
	
	
	
	private static final int[][] TRACK_GRID = {
			
			{ 7, 0 },
			{ 6, 0 },
			{ 5, 0 },
			{ 4, 0 },
			{ 3, 0 },
			{ 2, 0 },
			{ 1, 0 },
			{ 0, 0 }, 
			{ 0, 1 },
			{ 0, 2 },
			{ 0, 3 },
			{ 0, 4 },
			{ 0, 5 },
			{ 0, 6 },
			{ 0, 7 }, 
			{ 0, 8 }, { 0, 9 },
			{ 0, 10 },
			{ 0, 11 },
			{ 0, 12 },
			{ 0, 13 },
			{ 0, 14 }, 
			{ 1, 14 }, { 2, 14 }, { 3, 14 },
			{ 4, 14 },
			{ 5, 14 },
			{ 6, 14 },
			{ 7, 14 }, 
			{ 8, 14 }, { 9, 14 }, { 10, 14 }, { 11, 14 },
			{ 12, 14 },
			{ 13, 14 },
			{ 14, 14 }, 
			{ 14, 13 }, { 14, 12 }, { 14, 11 }, { 14, 10 }, { 14, 9 },
			{ 14, 8 },
			{ 14, 7 }, 
			{ 14, 6 }, { 14, 5 }, { 14, 4 }, { 14, 3 }, { 14, 2 }, { 14, 1 },
			{ 14, 0 }, 
			{ 13, 0 }, { 12, 0 }, { 11, 0 }, { 10, 0 }, { 9, 0 }, { 8, 0 }, 
																			
																			
																			
																			
			{ 8, 1 }, { 8, 2 }, { 8, 3 }, { 8, 4 }, { 8, 5 }, { 8, 6 }, 
																		
																		
																		
			{ 9, 6 }, { 10, 6 }, { 11, 6 }, { 12, 6 }, { 13, 6 }, 
																	
																	
																	
																	
			{ 13, 7 }, { 13, 8 }, { 13, 9 }, { 13, 10 }, 
															
															
			{ 12, 10 }, { 11, 10 }, { 10, 10 }, { 9, 10 }, 
															
															
			{ 8, 10 }, { 8, 11 }, { 8, 12 }, { 8, 13 }, 
														
			{ 7, 13 }, { 6, 13 }, { 5, 13 }, { 4, 13 }, 
														
			{ 4, 12 }, { 4, 11 }, { 4, 10 }, { 4, 9 }, 
														
			{ 5, 9 }, { 6, 9 }, { 7, 9 }, 
											
			{ 7, 8 }, { 7, 7 }, { 7, 6 }, 
			{ 6, 6 }, { 5, 6 }, { 4, 6 }, { 3, 6 }, 
													
			{ 3, 7 }, { 3, 8 }, { 3, 9 }, 
	};

	
	
	private static final Map<Colour, int[][]> SAFE_GRID = new EnumMap<>(
			Colour.class);
	static {
		SAFE_GRID.put(Colour.RED, new int[][] { { 7, 1 }, { 7, 2 }, { 7, 3 },
				{ 7, 4 } });
		SAFE_GRID.put(Colour.GREEN, new int[][] { { 1, 7 }, { 2, 7 }, { 3, 7 },
				{ 4, 7 } });
		SAFE_GRID.put(Colour.YELLOW, new int[][] { { 7, 13 }, { 7, 12 },
				{ 7, 11 }, { 7, 10 } });
		SAFE_GRID.put(Colour.BLUE, new int[][] { { 13, 7 }, { 12, 7 },
				{ 11, 7 }, { 10, 7 } });
	}

	
	private static final Map<Colour, int[][]> HOME_GRID = new EnumMap<>(
			Colour.class);
	static {
		HOME_GRID.put(Colour.RED, new int[][] { { 1, 1 }, { 1, 2 }, { 2, 1 },
				{ 2, 2 } });
		HOME_GRID.put(Colour.GREEN, new int[][] { { 1, 12 }, { 1, 13 },
				{ 2, 12 }, { 2, 13 } });
		HOME_GRID.put(Colour.YELLOW, new int[][] { { 12, 12 }, { 12, 13 },
				{ 13, 12 }, { 13, 13 } });
		HOME_GRID.put(Colour.BLUE, new int[][] { { 12, 1 }, { 12, 2 },
				{ 13, 1 }, { 13, 2 } });
	}

	
	
	
	private Game game;
	private Stage primaryStage;

	
	private GridPane boardGrid;
	private HBox humanHandBox;
	private Label statusLabel;
	private Label currentTurnLabel;
	private Label nextTurnLabel;
	private Label firepitLabel;
	private Label splitLabel;
	private TextField splitField;
	private int splitDistance = 3;
	private Button playButton;
	private Button deselectButton;
	private VBox player1Info, player2Info, player3Info, player4Info;

	
	private Card selectedCard = null;
	private Button selectedCardButton = null;
	private final List<Marble> selectedMarbles = new ArrayList<>();
	private final List<Button> selectedMarbleButtons = new ArrayList<>();

	
	private final Map<Marble, Button> marbleButtonMap = new HashMap<>();
	
	private final StackPane[][] boardCells = new StackPane[BOARD_GRID_SIZE][BOARD_GRID_SIZE];

	
	
	

	
	public void init(Game game, Stage primaryStage) {
		this.game = game;
		this.primaryStage = primaryStage;
		buildGameScene();
		refreshAll();
		startTurnCycle();
	}

	
	
	

	private void buildGameScene() {
		
		boardGrid = new GridPane();
		boardGrid.setHgap(2);
		boardGrid.setVgap(2);
		boardGrid.setPadding(new Insets(4));
		boardGrid.setStyle("-fx-background-color: #2d5a27;");

		for (int r = 0; r < BOARD_GRID_SIZE; r++) {
			boardGrid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
		}
		for (int c = 0; c < BOARD_GRID_SIZE; c++) {
			boardGrid.getColumnConstraints().add(
					new ColumnConstraints(CELL_SIZE));
		}

		for (int r = 0; r < BOARD_GRID_SIZE; r++) {
			for (int c = 0; c < BOARD_GRID_SIZE; c++) {
				StackPane cell = new StackPane();
				cell.setPrefSize(CELL_SIZE, CELL_SIZE);
				cell.setStyle("-fx-background-color: #2d5a27;"); 
																	
				boardCells[r][c] = cell;
				boardGrid.add(cell, c, r);
			}
		}

		
		for (int i = 0; i < TRACK_GRID.length; i++) {
			int r = TRACK_GRID[i][0], c = TRACK_GRID[i][1];
			boardCells[r][c]
					.setStyle("-fx-background-color: #d4b896; -fx-border-color: #8b6914; -fx-border-width: 1;");
		}

		
		for (Colour col : Colour.values()) {
			int[][] safe = SAFE_GRID.get(col);
			if (safe == null)
				continue;
			String fill = colourHex(col);
			for (int[] rc : safe) {
				boardCells[rc[0]][rc[1]]
						.setStyle("-fx-background-color: "
								+ fill
								+ "; -fx-border-color: #333; -fx-border-width: 1; -fx-opacity: 0.7;");
			}
		}

		
		for (Colour col : Colour.values()) {
			int[][] home = HOME_GRID.get(col);
			if (home == null)
				continue;
			String fill = colourHex(col);
			for (int[] rc : home) {
				boardCells[rc[0]][rc[1]]
						.setStyle("-fx-background-color: "
								+ fill
								+ "; -fx-border-color: #666; -fx-border-width: 1; -fx-opacity: 0.5;");
			}
		}

		
		boardCells[7][7].setStyle("-fx-background-color: #8b4513;");
		Label centre = new Label("♠");
		centre.setStyle("-fx-text-fill: gold; -fx-font-size:18;");
		boardCells[7][7].getChildren().add(centre);

		
		VBox infoPanel = new VBox(8);
		infoPanel.setPadding(new Insets(10));
		infoPanel.setPrefWidth(260);
		infoPanel.setStyle("-fx-background-color: #1a3a1a;");

		currentTurnLabel = new Label("Current: —");
		currentTurnLabel
				.setStyle("-fx-text-fill: white; -fx-font-size:14; -fx-font-weight:bold;");

		nextTurnLabel = new Label("Next: —");
		nextTurnLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size:12;");

		firepitLabel = new Label("Fire pit: 0 cards");
		firepitLabel.setStyle("-fx-text-fill: #f90; -fx-font-size:12;");

		statusLabel = new Label("Select a card to play");
		statusLabel.setStyle("-fx-text-fill: #9f9; -fx-font-size:12;");
		statusLabel.setWrapText(true);

		
		player1Info = makePlayerInfoBox();
		player2Info = makePlayerInfoBox();
		player3Info = makePlayerInfoBox();
		player4Info = makePlayerInfoBox();

		Separator sep = new Separator();

		
		
		splitLabel = new Label("Split (Seven): 3");
		splitLabel.setStyle("-fx-text-fill: #ccc; -fx-font-size:11;");
		splitField = new TextField("3");
		splitField.setPrefWidth(36);
		splitField.setStyle("-fx-font-size:11;");
		splitField.setEditable(false);
		Button splitMinus = new Button("-");
		Button splitPlus = new Button("+");
		splitMinus.setStyle("-fx-font-size:11; -fx-padding:2 6;");
		splitPlus.setStyle("-fx-font-size:11; -fx-padding:2 6;");
		splitMinus.setOnAction(e -> adjustSplit(-1));
		splitPlus.setOnAction(e2 -> adjustSplit(+1));
		HBox splitBox = new HBox(4, splitLabel, splitMinus, splitField,
				splitPlus);
		splitBox.setAlignment(Pos.CENTER_LEFT);

		playButton = new Button("▶ Play Turn");
		playButton
				.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size:13; -fx-font-weight:bold;");
		playButton.setPrefWidth(200);
		playButton.setOnAction(e -> onPlayButtonClicked());

		deselectButton = new Button("✕ Deselect All");
		deselectButton
				.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size:11;");
		deselectButton.setPrefWidth(200);
		deselectButton.setOnAction(e -> onDeselectAll());

		infoPanel.getChildren().addAll(new Label("── Players ──") {
			{
				setStyle("-fx-text-fill:#888;");
			}
		}, player1Info, player2Info, player3Info, player4Info, new Separator(),
				currentTurnLabel, nextTurnLabel, firepitLabel, new Separator(),
				statusLabel, sep, splitBox, playButton, deselectButton);

		
		humanHandBox = new HBox(6);
		humanHandBox.setPadding(new Insets(8));
		humanHandBox
				.setStyle("-fx-background-color: #0d2b0d; -fx-border-color: #333; -fx-border-width: 1 0 0 0;");
		humanHandBox.setAlignment(Pos.CENTER_LEFT);

		Label handTitle = new Label("Your cards: ");
		handTitle.setStyle("-fx-text-fill: white; -fx-font-weight:bold;");
		humanHandBox.getChildren().add(handTitle);

		
		BorderPane root = new BorderPane();
		StackPane boardWrapper = new StackPane(boardGrid);
		boardWrapper.setAlignment(Pos.CENTER);

		root.setCenter(boardWrapper);
		root.setRight(infoPanel);
		root.setBottom(humanHandBox);
		root.setStyle("-fx-background-color: #1a3a1a;");

		Scene scene = new Scene(root);

		
		scene.setOnKeyPressed(ev -> {
			switch (ev.getCode()) {
			case F:
				onFieldShortcut();
				break;
			default:
				break;
			}
		});

		primaryStage.setScene(scene);
		primaryStage.setTitle("Jackaroo");
		primaryStage.setWidth(1200);
		primaryStage.setHeight(900);
		primaryStage.setMinWidth(900);
		primaryStage.setMinHeight(700);
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	private VBox makePlayerInfoBox() {
		VBox box = new VBox(2);
		box.setPadding(new Insets(4, 6, 4, 6));
		box.setStyle("-fx-background-color: #2a4a2a; -fx-border-color: #3a5a3a; -fx-border-radius: 4; -fx-background-radius: 4;");
		return box;
	}

	
	
	

	private void startTurnCycle() {
		processTurn();
	}

	
	private void processTurn() {
		refreshAll();

		
		Colour winner = game.checkWin();
		if (winner != null) {
			announceWinner(winner);
			return;
		}

		Player current = currentPlayer();
		boolean isCPU = current.getName().startsWith("CPU");

		if (!game.canPlayTurn()) {
			
			game.endPlayerTurn();
			processTurn();
			return;
		}

		if (isCPU) {
			playButton.setDisable(true);
			deselectButton.setDisable(true);
			statusLabel.setText("CPU " + current.getName() + " is thinking…");

			PauseTransition pause = new PauseTransition(Duration.millis(1200));
			pause.setOnFinished(e -> {
				try {
					current.play();
				} catch (GameException ex) {
					
				}
				game.endPlayerTurn();
				processTurn();
			});
			pause.play();
		} else {
			playButton.setDisable(false);
			deselectButton.setDisable(false);
			statusLabel
					.setText("Your turn! Select a card and marble(s), then click Play.");
		}
	}

	
	
	

	private void onPlayButtonClicked() {
		try {
			game.playPlayerTurn();
			game.endPlayerTurn();
			processTurn();
		} catch (GameException ex) {
			showError(ex.getMessage());
		}
	}

	private void onDeselectAll() {
		game.deselectAll();
		selectedCard = null;
		if (selectedCardButton != null) {
			selectedCardButton.setStyle(cardButtonStyle(false));
			selectedCardButton = null;
		}
		for (Button mb : selectedMarbleButtons) {
			mb.setStyle(mb.getUserData() != null ? (String) mb.getUserData()
					: "");
		}
		selectedMarbles.clear();
		selectedMarbleButtons.clear();
		statusLabel.setText("Deselected. Choose a card.");
		refreshAll();
	}

	private void onCardClicked(Card card, Button button) {
		if (!isHumanTurn())
			return;

		
		if (selectedCardButton != null)
			selectedCardButton.setStyle(cardButtonStyle(false));

		selectedCard = card;
		selectedCardButton = button;
		button.setStyle(cardButtonStyle(true));

		try {
			game.selectCard(card);
		} catch (InvalidCardException ex) {
			showError(ex.getMessage());
			return;
		}

		boolean isSeven = card instanceof Seven;
		splitField.setDisable(!isSeven);
		statusLabel.setText("Selected: " + card.getName() + " — "
				+ card.getDescription());
	}

	private void onMarbleClicked(Marble marble, Button button) {
		if (!isHumanTurn())
			return;

		if (selectedMarbles.contains(marble)) {
			
			selectedMarbles.remove(marble);
			selectedMarbleButtons.remove(button);
			button.setStyle((String) button.getUserData());
			game.deselectAll();
			
			reapplySelections();
			return;
		}

		try {
			game.selectMarble(marble);
			selectedMarbles.add(marble);
			selectedMarbleButtons.add(button);
			button.getUserData(); 
			button.setStyle("-fx-background-color: yellow; -fx-border-color: orange; -fx-border-width:2; -fx-border-radius:3;");
			statusLabel
					.setText(selectedMarbles.size() + " marble(s) selected.");
		} catch (InvalidMarbleException ex) {
			showError(ex.getMessage());
		}
	}

	private void onFieldShortcut() {
		if (!isHumanTurn())
			return;
		
		Player human = currentPlayer();
		for (Card card : human.getHand()) {
			if (card.validateMarbleSize(new ArrayList<>())) {
				try {
					game.selectCard(card);
					selectedCard = card;
					game.playPlayerTurn();
					game.endPlayerTurn();
					processTurn();
					return;
				} catch (GameException ex) {
					showError(ex.getMessage());
					return;
				}
			}
		}
		showError("No card available to field a marble.");
	}

	private void reapplySelections() {
		
		if (selectedCard != null) {
			try {
				game.selectCard(selectedCard);
			} catch (InvalidCardException e) { 
			}
		}
		for (Marble m : selectedMarbles) {
			try {
				game.selectMarble(m);
			} catch (InvalidMarbleException e) { 
			}
		}
	}

	
	
	

	private void refreshAll() {
		refreshBoard();
		refreshHand();
		refreshInfoPanel();
	}

	private void refreshBoard() {
		marbleButtonMap.clear();

		
		for (int r = 0; r < BOARD_GRID_SIZE; r++) {
			for (int c = 0; c < BOARD_GRID_SIZE; c++) {
				StackPane cell = boardCells[r][c];
				cell.getChildren().removeIf(
						n -> n instanceof Button || (n instanceof Circle));
			}
		}

		Board board = game.getBoard();

		
		for (int i = 0; i < board.getTrack().size(); i++) {
			Cell cell = board.getTrack().get(i);
			int r = TRACK_GRID[i][0], c = TRACK_GRID[i][1];
			StackPane sp = boardCells[r][c];

			if (cell.isTrap()) {
				Circle trap = new Circle(4, Color.RED);
				trap.setOpacity(0.6);
				StackPane.setAlignment(trap, Pos.TOP_LEFT);
				sp.getChildren().add(trap);
			}

			if (cell.getMarble() != null) {
				Button mb = createMarbleButton(cell.getMarble(),
						cell.getCellType() == CellType.BASE);
				sp.getChildren().add(mb);
			}
		}

		
		for (SafeZone sz : board.getSafeZones()) {
			int[][] grid = SAFE_GRID.get(sz.getColour());
			if (grid == null)
				continue;
			List<Cell> cells = sz.getCells();
			for (int i = 0; i < cells.size(); i++) {
				if (cells.get(i).getMarble() != null) {
					int r = grid[i][0], c = grid[i][1];
					Button mb = createMarbleButton(cells.get(i).getMarble(),
							false);
					boardCells[r][c].getChildren().add(mb);
				}
			}
		}

		
		for (Player p : game.getPlayers()) {
			int[][] grid = HOME_GRID.get(p.getColour());
			if (grid == null)
				continue;
			List<Marble> homeMarbles = p.getMarbles();
			for (int i = 0; i < homeMarbles.size() && i < grid.length; i++) {
				Marble m = homeMarbles.get(i);
				int r = grid[i][0], c = grid[i][1];
				Button mb = createMarbleButton(m, false);
				boardCells[r][c].getChildren().add(mb);
			}
		}
	}

	private Button createMarbleButton(Marble marble, boolean isBase) {
		Button btn = new Button();
		String baseStyle = "-fx-background-color: "
				+ colourHex(marble.getColour())
				+ "; -fx-border-color: #333; -fx-border-width:1; -fx-border-radius:20;"
				+ "-fx-background-radius:20; -fx-min-width:28; -fx-min-height:28;"
				+ "-fx-max-width:28; -fx-max-height:28;";
		btn.setStyle(baseStyle);
		btn.setUserData(baseStyle); 
									

		Tooltip tip = new Tooltip(marble.getColour().name()
				+ (isBase ? " [BASE]" : ""));
		Tooltip.install(btn, tip);

		btn.setOnAction(e -> onMarbleClicked(marble, btn));

		marbleButtonMap.put(marble, btn);
		return btn;
	}

	private void refreshHand() {
		
		humanHandBox.getChildren().clear();
		Label handTitle = new Label("Your cards: ");
		handTitle.setStyle("-fx-text-fill: white; -fx-font-weight:bold;");
		humanHandBox.getChildren().add(handTitle);

		if (!isHumanTurn()) {
			humanHandBox.getChildren().add(
					styledLabel("(not your turn)", "#888"));
			return;
		}

		Player human = currentPlayer();
		for (Card card : human.getHand()) {
			Button btn = makeCardButton(card);
			humanHandBox.getChildren().add(btn);
		}
	}

	private Button makeCardButton(Card card) {
		String suitSymbol = getSuitSymbol(card);
		String displayText = card.getName() + "\n" + suitSymbol;
		Button btn = new Button(displayText);
		btn.setStyle(cardButtonStyle(false));
		btn.setPrefSize(70, 70);
		btn.setWrapText(true);
		Tooltip tip = new Tooltip(card.getName() + "\n" + card.getDescription());
		Tooltip.install(btn, tip);
		btn.setOnAction(e -> onCardClicked(card, btn));
		return btn;
	}

	private void refreshInfoPanel() {
		List<Player> players = game.getPlayers();
		Player current = currentPlayer();
		int nextIdx = (players.indexOf(current) + 1) % players.size();

		currentTurnLabel.setText("▶ Current: " + current.getName() + " ("
				+ current.getColour() + ")");
		nextTurnLabel.setText("Next: " + players.get(nextIdx).getName() + " ("
				+ players.get(nextIdx).getColour() + ")");
		firepitLabel.setText("Fire pit: " + game.getFirePit().size()
				+ " card(s)");

		
		if (!game.getFirePit().isEmpty()) {
			Card top = game.getFirePit().get(game.getFirePit().size() - 1);
			firepitLabel.setText("Fire pit: " + game.getFirePit().size()
					+ " | Top: " + top.getName());
		}

		VBox[] infoBoxes = { player1Info, player2Info, player3Info, player4Info };
		for (int i = 0; i < players.size() && i < 4; i++) {
			Player p = players.get(i);
			infoBoxes[i].getChildren().clear();
			boolean isActive = p == current;
			String style = isActive ? "-fx-background-color: "
					+ colourHex(p.getColour())
					+ "44; -fx-border-color:"
					+ colourHex(p.getColour())
					+ "; -fx-border-radius:4; -fx-background-radius:4; -fx-border-width:2;"
					: "-fx-background-color: #2a4a2a; -fx-border-color: #3a5a3a; -fx-border-radius: 4; -fx-background-radius: 4;";
			infoBoxes[i].setStyle(style);

			Label name = new Label((isActive ? "▶ " : "  ") + p.getName()
					+ " [" + p.getColour() + "]");
			name.setStyle("-fx-text-fill:" + colourHex(p.getColour())
					+ "; -fx-font-size:12; -fx-font-weight:bold;");

			Label cards = new Label("Cards in hand: " + p.getHand().size());
			cards.setStyle("-fx-text-fill:#ccc; -fx-font-size:11;");

			Label marblesHome = new Label("Marbles at home: "
					+ p.getMarbles().size());
			marblesHome.setStyle("-fx-text-fill:#aaa; -fx-font-size:10;");

			infoBoxes[i].getChildren().addAll(name, cards, marblesHome);
		}
	}

	
	
	

	private void announceWinner(Colour winner) {
		playButton.setDisable(true);
		statusLabel.setText("🏆 " + winner + " WINS!");

		Stage winStage = new Stage();
		winStage.initModality(Modality.APPLICATION_MODAL);
		winStage.setTitle("Game Over!");

		VBox box = new VBox(16);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(30));
		box.setStyle("-fx-background-color: " + colourHex(winner) + "44;");

		Label msg = new Label("🏆 " + winner.name() + " WINS! 🏆");
		msg.setStyle("-fx-text-fill: " + colourHex(winner)
				+ "; -fx-font-size:28; -fx-font-weight:bold;");

		Button close = new Button("Close Game");
		close.setStyle("-fx-background-color: #dc3545; -fx-text-fill:white; -fx-font-size:14;");
		close.setOnAction(e -> Platform.exit());

		box.getChildren().addAll(msg, close);
		Scene s = new Scene(box, 360, 200);
		winStage.setScene(s);
		winStage.show();
	}

	
	
	

	public void showError(String message) {
		Stage err = new Stage();
		err.initModality(Modality.APPLICATION_MODAL);
		err.setTitle("Invalid Action");

		VBox box = new VBox(12);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(20));
		box.setStyle("-fx-background-color: #3a1a1a;");

		Label msg = new Label(message);
		msg.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size:13;");
		msg.setWrapText(true);
		msg.setMaxWidth(340);

		Button ok = new Button("OK");
		ok.setStyle("-fx-background-color: #dc3545; -fx-text-fill:white;");
		ok.setOnAction(e -> err.close());
		
		err.setOnCloseRequest(e -> {
		}); 

		box.getChildren().addAll(msg, ok);
		Scene s = new Scene(box, 380, 150);
		err.setScene(s);
		err.showAndWait(); 
	}

	
	
	

	private Player currentPlayer() {
		return game.getPlayers().stream()
				.filter(p -> p.getColour() == game.getActivePlayerColour())
				.findFirst().orElse(game.getPlayers().get(0));
	}

	private boolean isHumanTurn() {
		return !currentPlayer().getName().startsWith("CPU");
	}

	private String colourHex(Colour c) {
		switch (c) {
		case RED:
			return "#e74c3c";
		case GREEN:
			return "#2ecc71";
		case YELLOW:
			return "#f1c40f";
		case BLUE:
			return "#3498db";
		default:
			return "#ffffff";
		}
	}

	private String cardButtonStyle(boolean selected) {
		if (selected)
			return "-fx-background-color: #ffc107; -fx-text-fill: #000; -fx-font-size:11; -fx-font-weight:bold;"
					+ "-fx-border-color: #ff8c00; -fx-border-width:2; -fx-border-radius:6; -fx-background-radius:6;";
		return "-fx-background-color: #fff8e1; -fx-text-fill: #222; -fx-font-size:11;"
				+ "-fx-border-color: #aaa; -fx-border-width:1; -fx-border-radius:6; -fx-background-radius:6;";
	}

	private String getSuitSymbol(Card card) {
		if (card instanceof Standard) {
			switch (((Standard) card).getSuit()) {
			case HEART:
				return "♥";
			case DIAMOND:
				return "♦";
			case SPADE:
				return "♠";
			case CLUB:
				return "♣";
			}
		}
		return "★"; 
	}

	private Label styledLabel(String text, String colour) {
		Label l = new Label(text);
		l.setStyle("-fx-text-fill:" + colour + ";");
		return l;
	}

	private void adjustSplit(int delta) {
		splitDistance = Math.max(1, Math.min(6, splitDistance + delta));
		splitField.setText(String.valueOf(splitDistance));
		splitLabel.setText("Split (Seven): " + splitDistance);
		try {
			game.editSplitDistance(splitDistance);
		} catch (SplitOutOfRangeException e) { 
		}
	}

}