package gui;


import controller.Controller;
import controller.HighScoreDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Direction;
import model.HighScore;
import model.Pixel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GUI extends Application {

    final int cellSize = 32;
    private PausableAnimationTimer timer;
    private final EntityDrawer entityDrawer = new EntityDrawer(cellSize);
    private final Controller controller = new Controller(this, cellSize);
    @FXML
    private final AnchorPane pane = new AnchorPane();
    @FXML
    private final StackPane stackPane;
    @FXML
    private VBox root;
    @FXML
    private Label scoreLabel;
    private Boolean gameOverFlag = false;
    private Boolean newHighScoreFlag = false;
    private Direction direction;
    private static final int TEXT_LIMIT = 5;

    private boolean pauseToggle = false;

    private final SimpleStringProperty scoreTxt = new SimpleStringProperty("Score: 0");

    // snake drawing classes for creating the snake graphics on board.
    private Stage primaryStage;

    public GUI() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Snake Game");

        FXMLLoader loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("snakeGame.fxml")));
        root = loader.load();

        // set label.
        scoreTxt.set("SCORE:  " + 0);
        scoreLabel = new Label();
        scoreLabel.textProperty().bind(scoreTxt);
        scoreLabel.setTextFill(Color.BLUE);
        scoreLabel.setStyle("-fx-padding: 10px;");
        scoreLabel.setFont(new Font(18));

        // set pane.
        pane.setStyle("-fx-background-color: black;");
        pane.setId("pane");
        pane.setPrefSize(512, 512);
        AnchorPane.setTopAnchor(pane, 50.0);

        // Create a Rectangle to act as the border
        Rectangle border = new Rectangle(514, 514);
        border.setStroke(Color.BLUE); // Set the border color
        border.setStrokeWidth(2); // Set the border width
        border.setFill(null); // Set the fill color to transparent
        pane.getChildren().add(border);

        // set root.
        root = new VBox(2);
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(scoreLabel, pane);
        root.setStyle("-fx-background-color: black;");

        // stack pane for pause screen.
        stackPane.getChildren().add(root);

        // set scene.
        Scene scene = new Scene(stackPane);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();

        // add key listener for movement and pausing.
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            switch (key.getCode()) {
                case UP, W -> direction = Direction.UP;
                case DOWN, S -> direction = Direction.DOWN;
                case LEFT, A -> direction = Direction.LEFT;
                case RIGHT, D -> direction = Direction.RIGHT;
                case ESCAPE -> {
                    pauseToggle = !pauseToggle;
                    timer.pause();
                    togglePauseScene();
                }
                default -> {
                }
            }
        });

        timer = new PausableAnimationTimer() {
            @Override
            public void tick(long now) {
                // ignore
                if (timer.isActive) {
                    controller.handleTick(direction, now);
                }

                if (gameOverFlag) {
                    System.out.println("Game Over");
                    timer.pause();
                    switchToGameEndScene();
                    gameOverFlag = false;
                }
            }
        };

        timer.start();
    }

    /**
     * sets flag for game over.
     * */
    public void setGameOverFlag() {
        gameOverFlag = true;
    }

    /**
     * updates the displayed score.
     * */
    public void updateScore(final int score) {
        scoreTxt.set("SCORE: " + score);
    }

    /**
     * draws snake on the given pixels.
     * */
    public void updateSnakeBody(List<Pixel> snakePixelPos) {
        entityDrawer.drawSnakeBody(snakePixelPos, pane);
    }

    /**
     * draws fruits of the given pixels.
     * */
    public void updateFruits(List<Pixel> fruits) {
        entityDrawer.drawConsumbable(fruits, pane);
    }

    /**
     * sets the internal speed of timer of the game.
     * */
    public void setRefreshSpeed(double speed) {
        timer.setSpeed(speed);
    }

    /**
     * sets flag for new high score.
     * */
    public void setNewHighScoreFlag() {
        newHighScoreFlag = true;
    }

    /**
     * Displays game end screen.
     */
    private void switchToGameEndScene() {
        //timer.pause();
        Scene oldScene = primaryStage.getScene();

        // root
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: red");

        // Scene Title.
        Label label = new Label("Game Over");
        label.setFont(new Font(30));

        // if new high score, display message.
        if (newHighScoreFlag) {
            Label newHighScore = new Label("New High Score!");
            Label score = new Label(scoreTxt.getValue());
            newHighScore.setFont(new Font(20));
            score.setFont(new Font(30));
            score.setTextFill(Color.YELLOW);

            // save score button.
            Button submitButton = new Button("Submit");
            TextField name = new TextField();
            AtomicReference<Boolean> canSubmit = new AtomicReference<>(false);
            Label errorLabel = new Label();
            name.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;
            }));
            name .textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[A-Z0-9]*")) {
                    //name.setText(newValue.replaceAll("[^A-Z0-9]", ""));
                    errorLabel.setText("Only letters and numbers allowed");
                    canSubmit.set(false);
                }
                else {
                    errorLabel.setText("");
                    canSubmit.set(true);
                }
            });
            name.lengthProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (name.getText().length() >= TEXT_LIMIT) {

                        // if it's 6th character then just setText to previous
                        // one
                        name.setText(name.getText().substring(0, TEXT_LIMIT));
                    }
                }
            });
            name.setMaxWidth(120);
            name.setAlignment(Pos.CENTER);
            name.setPromptText("Enter Name");
            name.setFocusTraversable(false);
            submitButton.setOnAction(e -> {
                if (!canSubmit.get()) {
                    return;
                }
                String playerName = name.getText();
                HighScore highScore = new HighScore(playerName, controller.getScore());
                controller.saveScore(highScore);

            });
            root.getChildren().addAll(newHighScore, score, name, submitButton,errorLabel);
        }

        // buttons
        Button exit = new Button("EXIT");
        exit.setOnAction(e -> Platform.exit());

        // create Reset button.
        Button reset = new Button("RESET");
        reset.setOnAction(e -> {
            // create new game instance.
            controller.createNewGame(cellSize);
            primaryStage.setScene(oldScene);
            primaryStage.show();
            timer.play();
        });

        // leaderboards button.
        Button leaderboards = new Button("Leaderboards");
        leaderboards.setOnAction(e -> switchToLeaderBoardScreen());

        // add all nodes.
        root.getChildren().addAll(label, exit, reset, leaderboards);

        // set game end scene.
        Scene gameEnd = new Scene(root, 512, 512);
        primaryStage.setScene(gameEnd);
        primaryStage.show();
    }

    /**
     * Displays leader board screen.
     * */
    private void switchToLeaderBoardScreen() {
        //TODO: implement leader board screen.
        Stage newStage = new Stage();

        // Create the content for the new window
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        // Create a scene and set it to the new stage
        Scene newScene = new Scene(root, 300, 200);
        newStage.setScene(newScene);

        // Set the title for the new window
        newStage.setTitle("New Window");

        // Clear Leaderboard button.
        Button clearLeaderboard = new Button("Clear Leaderboard");
        clearLeaderboard.setOnAction(e -> {
            HighScoreDAO.deleteEntries();
            root.getChildren().remove(1);
        });

        // load leaderboard data.
        ListView<HighScore> listView = new ListView<>();
        listView.getItems().addAll(HighScoreDAO.getTopScores());
        root.getChildren().addAll(listView, clearLeaderboard);

        // Show the new window
        newStage.show();
    }

    /**
     * Displays pause screen.
     * */
    private void togglePauseScene() {
        if (pauseToggle) {
            Label label = new Label("Paused");
            label.setFont(new Font(30));
            stackPane.getChildren().add(label);
        } else {
            stackPane.getChildren().remove(1);
        }
    }

}
