package gui;


import java.util.List;
import java.util.Objects;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import main.Direction;
import main.GameModel;
import main.Position;
import main.consumables.Food;
import main.consumables.Fruit;

public class GUI extends Application {

    Food frt = new Fruit();

    List<Food> foods = List.of(new Food[]{frt});

    GameModel game = new GameModel(1,512,foods);
    private Boolean gameOver = false;

    Direction moveDirection = Direction.UP;

    @FXML
    Canvas canvas;

    @FXML
    Shape snakeHead, snakeBody;

    private void drawSnake(GraphicsContext gc, List<Position> snake, Direction dir) {
        //rotate head in direction of movement.
        /*switch (dir) {
            case UP -> snakeHead.setRotate(0);
            case RIGHT -> snakeHead.setRotate(-90);
            case LEFT -> snakeHead.setRotate(90);
            case DOWN -> snakeHead.setRotate(180);
        }*/

        for (int i = 0 ; i < snake.size() - 1; i++){
            int radius = 7;
            int xPos = snake.get(i).getxCoord();
            int yPos = snake.get(i).getyCoord();
            if (i == 0){
                //head
                gc.setFill(Color.RED);
                gc.fillOval(xPos - radius,yPos - radius,radius,radius);
            }
            //body.
            gc.setFill(Color.GREEN);
            gc.fillOval(xPos - radius,yPos - radius,radius,radius);

        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake Game");
        VBox root = new VBox();
        Canvas canvas = new Canvas(512,512);

        /*Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                "scene.fxml")));*/
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);



        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if(key.getCode() == KeyCode.UP || key.getCode() == KeyCode.W){
                moveDirection = Direction.UP;
            }
            if(key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.S){
                moveDirection = Direction.DOWN;
            }
            if(key.getCode() == KeyCode.LEFT || key.getCode() == KeyCode.A){
                moveDirection = Direction.LEFT;
            }
            if(key.getCode() == KeyCode.RIGHT || key.getCode() == KeyCode.D){
                moveDirection = Direction.RIGHT;
            }
        });



        // anonymous animationTimer class.
        new AnimationTimer() {
            long lastTick = 0;
            public void handle(long nanoTimeNow) {

                if (gameOver){
                    gc.setFill(Color.RED);
                    gc.fillRect(0,0,512,512);
                    System.out.println("game ended");
                    return;
                }

                gc.setFill(Color.BLACK);
                gc.fillRect(0,0,512,512);
                //draw snake after move
                drawSnake(gc,game.getSnakePos(),moveDirection);
                if (lastTick == 0){
                    lastTick = nanoTimeNow;
                     gameOver = game.makeMove(moveDirection);
                    return;
                }
                if (nanoTimeNow - lastTick > 1000000000 / game.getGp().getSpeed()){ //each second
                    lastTick = nanoTimeNow;
                    gameOver = game.makeMove(moveDirection);
                }



            }
        }.start();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
