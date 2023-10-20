package controller;

import gui.EntityDrawer;
import gui.PausableAnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Direction;
import model.GameModel;
import model.Pixel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    StackPane mainRoot;


    @FXML
    AnchorPane pane;

    @FXML
    Label scoreLabel;

    private final SimpleStringProperty scoreTxt = new SimpleStringProperty("Score: 0");

    private GameModel game;

    private PausableAnimationTimer timer;

    private final EntityDrawer drawer = new EntityDrawer(32);

    private long eatenSince = 0;
    private Direction lastInput = Direction.RIGHT;
    private Boolean paused = false;
    private Boolean gameOverFlag = false;
    private Boolean newHighScoreFlag = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new GameModel(2, 512, 32);
        scoreLabel.textProperty().bind(scoreTxt);
    }

    public void initData(Stage stage) {
        stage.getScene().setOnKeyPressed(this::handleKeyPress);
        startGame();
    }

    @FXML
    void handleKeyPress(KeyEvent event) {
        KeyCode key = event.getCode();
        switch (key) {
            case W, UP -> {lastInput = Direction.UP;}
            case S, DOWN -> {lastInput = Direction.DOWN;}
            case A, LEFT -> {lastInput = Direction.LEFT;}
            case D, RIGHT -> {lastInput = Direction.RIGHT;}
            case ESCAPE -> {
                paused = !paused;
                togglePauseScene();
            }
        }
    }

    public void handleTick(Direction dir, long currTick) {

        // make move and check if game is over.
        if (game.makeMove(dir)) {
            gameOverFlag = true;
            if (HighScoreDAO.isInTopTen(game.getScore())) {
                newHighScoreFlag = true;
            }
        }

        //respawn fruit if not eaten.
        if (currTick - eatenSince > 32) {
            if (!game.getEaten()) {
                game.respawnConsumable();
            }
            game.resetEaten();
            eatenSince = currTick;
        }
        // refresh gui.
        updateGui();
    }

    private void startGame() {
        timer = new PausableAnimationTimer() {
            @Override
            public void tick(long now) {

                if (timer.isActive()) {
                    handleTick(lastInput, now);
                }

                if (gameOverFlag) {
                    System.out.println("Game Over");
                    timer.pause();
                    try {
                        switchToGameEndScene();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    gameOverFlag = false;
                }
            }
        };

        timer.start();
    }

    public void switchToGameEndScene() throws IOException {
        URL url = getClass().getResource("/endGame.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        VBox root = loader.load();
        EndGameController controller = loader.getController();
        controller.initData(newHighScoreFlag, game.getScore());
        mainRoot.getChildren().setAll(root);
    }

    public void updateGui() {
        updateScore(game.getScore());
        updateSnakeBody(game.getSnakePixelPos());
        updateFruits(game.getFruits());
        timer.setSpeed(game.getGp().getSpeed());
    }

    public void updateScore(final int score) {
        scoreTxt.set("SCORE: " + score);
    }

    public void updateSnakeBody(List<Pixel> snakePixelPos) {
        drawer.drawSnakeBody(snakePixelPos, pane);
    }

    public void updateFruits(List<Pixel> fruits) {
        drawer.drawConsumbable(fruits, pane);
    }

    private void togglePauseScene() {
        timer.pause();
        if (paused) {
            Label label = new Label("Paused");
            label.setFont(new Font(30));
            mainRoot.getChildren().add(label);
        } else {
            mainRoot.getChildren().remove(1);
        }
    }

}
