package controller;

import dataaccess.HighScoreDAO;
import gui.EntityDrawer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import model.Direction;
import model.GameModel;
import model.Pixel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MainController implements Initializable {

    @FXML
    StackPane mainRoot;

    @FXML
    AnchorPane pane;

    @FXML
    Label scoreLabel;

    private final int SEED = 1;
    private final int CELL_SIZE = 32;
    private final int BOARD_SIZE = 512;


    private GameModel game;
    private final EntityDrawer drawer = new EntityDrawer(CELL_SIZE);
    private long eatenSince = 0;
    private Direction lastInput = Direction.RIGHT;
    private Boolean paused = false;
    private Boolean gameOverFlag = false;
    private Boolean newHighScoreFlag = false;
    private final Timeline timeline = new Timeline();
    private final SimpleStringProperty scoreTxt = new SimpleStringProperty("000");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new GameModel(SEED, BOARD_SIZE, CELL_SIZE);
        scoreLabel.textProperty().bind(scoreTxt);
        Font font = Font.loadFont(getClass()
                .getResourceAsStream("/fonts/RobotoMono-VariableFont_wght.ttf"), 30);
        scoreLabel.setFont(font);
    }

    public void initData(Stage stage) {
        stage.getScene().setOnKeyPressed(this::handleKeyPress);
        updateGui();
        startGame();
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        KeyCode key = event.getCode();
        switch (key) {
            case W, UP -> lastInput = paused? lastInput : Direction.UP;

            case S, DOWN -> lastInput = paused? lastInput : Direction.DOWN;

            case A, LEFT -> lastInput = paused? lastInput : Direction.LEFT;

            case D, RIGHT -> lastInput = paused? lastInput : Direction.RIGHT;

            case ESCAPE -> {
                paused = !paused;
                togglePauseScene();
            }
        }
    }

    private void startGame() {
        AtomicInteger tick = new AtomicInteger();
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(500),
                        event -> {
                            if (!paused && !gameOverFlag) {
                                handleTick(lastInput, tick);
                                tick.getAndIncrement();
                            }
                        }));
        //updateGui();
        timeline.play();
    }

    private void handleTick(Direction dir, AtomicInteger currTick) {

        // make move and check if game is over.
        if (game.makeMove(dir)) {
            gameOverFlag = true;
            if (HighScoreDAO.isInTopTen(game.getScore())) {
                newHighScoreFlag = true;
                timeline.stop();
            }
            try {
                switchToGameEndScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //respawn fruit if not eaten.
        if (currTick.get() - eatenSince > 32) {
            if (!game.getEaten()) {
                game.respawnConsumable();
            }
            game.resetEaten();
            eatenSince = currTick.get();
        }
        // refresh gui.
        updateGui();
    }

    private void switchToGameEndScene() throws IOException {
        URL url = getClass().getResource("/endGame.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        VBox root = loader.load();
        EndGameController controller = loader.getController();
        controller.initData(newHighScoreFlag, game.getScore());
        mainRoot.getChildren().setAll(root);
    }

    private void updateGui() {
        updateScore(game.getScore());
        updateSnakeBody(game.getSnakePixelPos());
        updateFruits(game.getFruits());
        //timer.setSpeed(game.getGp().getSpeed());
    }

    private void updateScore(final int score) {
        String formattedScore = String.format("%03d", score);
        scoreTxt.set(formattedScore);
    }

    private void updateSnakeBody(List<Pixel> snakePixelPos) {
        drawer.drawSnakeBody(snakePixelPos, pane);
    }

    private void updateFruits(List<Pixel> fruits) {
        drawer.drawConsumable(fruits, pane);
    }

    private void togglePauseScene() {
        if (paused) {
            Label label = new Label("Paused");
            label.setFont(new Font(30));
            mainRoot.getChildren().addAll(label);
            timeline.pause();
        } else {
            mainRoot.getChildren().remove(1);
            timeline.play();
        }
    }

}
