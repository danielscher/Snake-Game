package gui;


import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.Pixel;

import java.util.List;

public class GUI extends Application {

    final int cellSize = 32;

    private PausableAnimationTimer timer;

    private final EntityDrawer entityDrawer = new EntityDrawer(cellSize);
    private final Controller controller = new Controller(this, cellSize);
    private final AnchorPane pane = new AnchorPane();

    private Boolean gameOverFlag = false;

    private KeyEvent keyPressed;

    private final SimpleStringProperty scoreTxt = new SimpleStringProperty("Score: 0");


    // snake drawing classes for creating the snake graphics on board.
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Snake Game");

        // set label.
        scoreTxt.set("SCORE:  " + 0);
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

        // Create a Rectangle to act as the border
        Rectangle border = new Rectangle(514, 514);
        border.setStroke(Color.BLUE); // Set the border color
        border.setStrokeWidth(2); // Set the border width
        border.setFill(null); // Set the fill color to transparent
        pane.getChildren().add(border);

        // set root.
        VBox root = new VBox(2);
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(label, pane);
        root.setStyle("-fx-background-color: black;");

        // set scene.
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);

        // set key listener.
        scene.addEventFilter(KeyEvent.KEY_PRESSED,key -> {keyPressed = key;});

        timer = new PausableAnimationTimer() {
            @Override
            public void tick(long now) {

                controller.handleTick(keyPressed,now);

                if (gameOverFlag) {
                    System.out.println("Game Over");
                    switchToGameEndScene();
                    gameOverFlag = false;
                }
            }
        };

        timer.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setGameOverFlag(){
        gameOverFlag = true;
    }

    public void pauseAnimation() {
        timer.pause();
    }

    public void updateScore(final int score){
        scoreTxt.set("SCORE: " + score);
    }


    public void updateSnakeBody(List<Pixel> snakePixelPos) {
        entityDrawer.drawSnakeBody(snakePixelPos, pane);
    }

    public void updateFruits(List<Pixel> fruits) {
        entityDrawer.drawConsumbable(fruits, pane);
    }

    public void setRefreshSpeed(double speed) {
        timer.setSpeed(speed);
    }

    /**
     * Switches to the game end scene.
     * */
    private void switchToGameEndScene() {
        timer.pause();
        Scene oldScene = primaryStage.getScene();

        // root
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: red");

        // Scene Title.
        Label label = new Label("Game Over");
        label.setFont(new Font(30));

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

        // add all nodes.
        root.getChildren().addAll(label, exit, reset);

        // set game end scene.
        Scene gameEnd = new Scene(root, 512, 512);
        primaryStage.setScene(gameEnd);
        primaryStage.show();
    }
}
