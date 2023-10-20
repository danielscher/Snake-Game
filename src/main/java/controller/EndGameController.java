package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.GameModel;
import model.Player;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {

    @FXML
    private VBox gameEndRoot;
    @FXML
    private Label scoreLabel;
    @FXML
    private TextField nameInput;
    @FXML
    private Button submitButton;
    private Integer score;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL stylesheetUrl = getClass().getResource("/endGameScene.css");
        gameEndRoot.getStylesheets().add(stylesheetUrl.toExternalForm());
    }

    public void initData(Boolean newHighScoreFlag, Integer score) {
        this.score = score;
        if (newHighScoreFlag) {
            scoreLabel.setText("New High Score: " + score);
        } else {
            gameEndRoot.getChildren().remove(nameInput);
            gameEndRoot.getChildren().remove(submitButton);
            scoreLabel.setText("Score: " + score);
        }
    }

    @FXML
    public void submitScore() {
        String name = nameInput.getText();
        Player player = new Player(name, score);
        HighScoreDAO.insertScore(player);
        gameEndRoot.getChildren().remove(nameInput);
        gameEndRoot.getChildren().remove(submitButton);
    }

    @FXML
    public void switchToLeaderboard() throws IOException {
        URL url = getClass().getResource("/leaderboard.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        VBox leaderboardRoot = loader.load();
        Stage leaderboardStage = new Stage();
        Scene leaderboardScene = new Scene(leaderboardRoot);
        leaderboardStage.setScene(leaderboardScene);
        leaderboardStage.show();
    }

    @FXML
    public void restartGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/snakeGame.fxml"));
        StackPane newMainRoot = loader.load();
        Scene currentScene = gameEndRoot.getScene();

        // Replace the current scene's root with the new gameplay root
        currentScene.setRoot(newMainRoot);

        // Get the controller for the new gameplay scene and initialize it
        MainController newController = loader.getController();
        newController.initData((Stage) currentScene.getWindow());
    }

    @FXML
    public void exitGame() {
        System.exit(0);
    }
}
