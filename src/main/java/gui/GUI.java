package gui;


import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.Direction;
import main.GameModel;

public class GUI extends Application {

    PausableAnimationTimer timer;

    AnchorPane pane = new AnchorPane();

    GameModel game = new GameModel(2, 512, 32);
    private int counter = 0;

    private Boolean gameOver = false;

    Direction moveDirection = Direction.LEFT;

    private VBox root;

    private SimpleStringProperty scoreTxt = new SimpleStringProperty("Score: 0");


    // snake drawing classes for creating the snake graphics on board.
    private EntityDrawer entityDrawer = new EntityDrawer(game.getCellSize());

    private final Set<Circle> currFruits = new HashSet<>();

    private final Set<Rectangle> recs = new HashSet<>();


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake Game");

        // set label.
        scoreTxt.set("SCORE:  " + game.getScore());
        Label label = new Label();
        label.textProperty().bind(scoreTxt);
        label.setTextFill(Color.BLUE);
        label.setStyle("-fx-padding: 10px;");
        label.setFont(new Font(18));

        // set pane.
        pane.setStyle("-fx-background-color: black;");
        pane.setId("pane");
        pane.setPrefSize(512, 512);
        AnchorPane.setTopAnchor(pane, 50.0);
        root = new VBox(2);
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(label, pane);
        root.setStyle("-fx-background-color: black;");


        // Create a Rectangle to act as the border
        Rectangle border = new Rectangle(514, 514);
        border.setStroke(Color.BLUE); // Set the border color
        border.setStrokeWidth(2); // Set the border width
        border.setFill(null); // Set the fill color to transparent

        pane.getChildren().add(border);
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);

        timer = new PausableAnimationTimer() {
            @Override
            public void draw() {
                entityDrawer.drawSnakeBody(game.getSnakePixelPos(), pane, moveDirection);
                entityDrawer.drawConsumbable(game.getFruits(), pane);
                updateScore();
            }

            @Override
            public void tick(long now) {

                if (game.makeMove(moveDirection)) {
                    System.out.println("game over");
                    pause();
                    switchToEndGameScene(primaryStage);
                }

                if (now > 0 && now % 32 == 0) {
                    if (!game.getEaten()){
                        game.respawnConsumable();
                    }
                    game.resetEaten();
                    counter = 0;
                }
                entityDrawer.drawSnakeBody(game.getSnakePixelPos(), pane, moveDirection);
                entityDrawer.drawConsumbable(game.getFruits(), pane);
                updateScore();
                setSpeed(game.getGp().getSpeed());
            }
        };

        timer.start();


        // anonymous animationTimer class.
//        new AnimationTimer() {
//            long lastTick = 0;
//
//            // nanoTimeNow is timestamp of current frame.
//            public void handle(long nanoTimeNow) {
//
//                if (gameOver) {
//                    //TODO: add game over animation.
//                    System.out.println("game ended");
//                    switchToEndGameScene(primaryStage);
//                    //return;
//                }
//
//                // 1 second = 1e+9 nanoseconds
//                // if time interval is larger than 1 second tick.
//                // game speed = 1 -> 1 frame per second.
//                long timeInterval = nanoTimeNow - lastTick;
//                if (timeInterval > 1000000000 / game.getGp().getSpeed()) { //each second
//                    counter++;
//                    lastTick = nanoTimeNow;
//                    tick(counter);
//                }
//            }
//        }.start();


        //TODO: define new method for key listening and include 'ESC' for pausing.
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.UP || key.getCode() == KeyCode.W) {
                moveDirection = Direction.UP;
            }
            if (key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.S) {
                moveDirection = Direction.DOWN;
            }
            if (key.getCode() == KeyCode.LEFT || key.getCode() == KeyCode.A) {
                moveDirection = Direction.LEFT;
            }
            if (key.getCode() == KeyCode.RIGHT || key.getCode() == KeyCode.D) {
                moveDirection = Direction.RIGHT;
            }
            if (key.getCode() == KeyCode.ESCAPE) {
                timer.pause();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * called every in game tick.
     */
//    public void tick(int time) {
//        gameOver = game.makeMove(moveDirection);
//        if (time > 0 && time % 32 == 0) {
//            if (!game.getEaten()){
//                game.respawnConsumable();
//            }
//            game.resetEaten();
//            counter = 0;
//        }
//        entityDrawer.drawSnakeBody(game.getSnakePixelPos(), pane, moveDirection);
//        entityDrawer.drawConsumbable(game.getFruits(), pane);
//        updateScore();
//    }

    private void switchToEndGameScene(Stage stage) {
        Scene oldScene = stage.getScene();

        //TODO: add score to scene.

        // root
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: red");

        // text
        Label label = new Label("Game Over");
        label.setFont(new Font(30));

        // buttons
        //TODO: add functionality.
        Button exit = new Button("EXIT");
        exit.setOnAction(e -> Platform.exit());

        Button reset = new Button("RESET");
        reset.setOnAction(e -> {
            game = new GameModel(3, 512, 32);
            stage.setScene(oldScene);
            stage.show();
        });

        // add all nodes.
        root.getChildren().addAll(label, exit, reset);

        // set scene.
        Scene gameEnd = new Scene(root, 512, 512);
        stage.setScene(gameEnd);
        stage.show();
        timer.play();
    }

    private void updateScore() {
        scoreTxt.set("SCORE: " + game.getScore());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
