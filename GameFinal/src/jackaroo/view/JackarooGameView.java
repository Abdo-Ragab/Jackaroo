package jackaroo.view;

import engine.Game;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


public class JackarooGameView extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Jackaroo");
        showNameInputScreen();
    }

    

    private void showNameInputScreen() {
        VBox root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a3a1a;");

        Label title = new Label("JACKAROO");
        title.setStyle("-fx-text-fill: #f1c40f; -fx-font-size:36; -fx-font-weight:bold;");

        Label subtitle = new Label("A New Game Spin");
        subtitle.setStyle("-fx-text-fill: #aaa; -fx-font-size:14;");

        Label prompt = new Label("Enter your player name:");
        prompt.setStyle("-fx-text-fill: white; -fx-font-size:14;");

        TextField nameField = new TextField();
        nameField.setMaxWidth(280);
        nameField.setStyle("-fx-font-size:14; -fx-padding:6;");
        nameField.setPromptText("Your name…");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size:12;");

        Button startButton = new Button("Start Game");
        startButton.setStyle(
            "-fx-background-color: #28a745; -fx-text-fill: white; " +
            "-fx-font-size:15; -fx-font-weight:bold; -fx-padding: 8 24; " +
            "-fx-background-radius:6;");
        startButton.setDefaultButton(true);

        startButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("Please enter your name.");
                return;
            }
            startGame(name);
        });

        Label note = new Label("You will play against 3 CPU opponents.");
        note.setStyle("-fx-text-fill: #888; -fx-font-size:11;");

        root.getChildren().addAll(title, subtitle, prompt, nameField, errorLabel, startButton, note);

        Scene scene = new Scene(root, 440, 320, Color.web("#1a3a1a"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    

    private void startGame(String playerName) {
        try {
            Game game = new Game(playerName);
            GameController controller = new GameController();
            controller.init(game, primaryStage);
        } catch (IOException ex) {
            showFatalError("Failed to load cards: " + ex.getMessage());
        }
    }

    private void showFatalError(String message) {
        Stage errStage = new Stage();
        errStage.setTitle("Fatal Error");
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #3a1a1a;");
        Label msg = new Label("Could not start game:\n" + message);
        msg.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size:13;");
        msg.setWrapText(true);
        Button ok = new Button("OK");
        ok.setOnAction(e -> errStage.close());
        box.getChildren().addAll(msg, ok);
        errStage.setScene(new Scene(box, 380, 160));
        errStage.showAndWait();
    }
}