package gui;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Direction;
import main.GameModel;
import main.Position;
import main.Tile;
import main.consumables.Food;
import main.consumables.Fruit;

public class GUI extends Application {

    Food frt = new Fruit();

    List<Food> foods = List.of(new Food[]{frt});

    GameModel game = new GameModel(2,512,32,foods);

    private Boolean gameOver = false;

    Direction moveDirection = Direction.LEFT;

    private Group root;

    private SimpleStringProperty scoreTxt = new SimpleStringProperty("Score: 0"); {
    };

    // snake drawing classes for creating the snake graphics on board.
    private EntityDrawer entityDrawer = new EntityDrawer(game.getCellSize());

    private Set<Circle> currFruits = new HashSet<>();

    private Set<Rectangle> recs = new HashSet<>();

    private void colorSnakeTiles(Group root){
        int radius = 16;
        Tile[][] grid = game.getBoard().getGrid();
        for (Tile[] row : grid){
            for(Tile col : row){
                if (col.isSnake()){
                    int xPos = col.getCenter().getX();
                    int yPos = col.getCenter().getY();
                    Text txt = new Text(xPos+10,yPos-10,xPos + "," + yPos);
                    Rectangle rect = new Rectangle(xPos-radius,yPos-radius,32,32);
                    rect.setFill(Color.YELLOW);
                    root.getChildren().add(rect);
                    root.getChildren().add(txt);
                }
            }

        }
    }

    private void drawFruit (List<Position> foods){
        int cellSize = game.getCellSize();
        int radius = cellSize/2;

        root.getChildren().removeAll(currFruits);
        currFruits.clear();

        for (Position f : foods){
            int xPos = f.getX();
            int yPos = f.getY();
            Circle el = new Circle(xPos,yPos,radius);
            el.setFill(Color.MAGENTA);
            el.setStroke(Color.PINK);
            root.getChildren().add(el);
            currFruits.add(el);
        }
    }
    private void updateScore(){
        scoreTxt.set("Score:" + game.getScore());
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake Game");
        root = new Group();
        HBox scoreBox = new HBox();
        scoreTxt.set("Score:" + game.getScore());
        Label label = new Label();
        label.textProperty().bind(scoreTxt);
        scoreBox.getChildren().add(label);
        root.getChildren().add(scoreBox);
        //root = new VBox();
        //Canvas canvas = new Canvas(512,512);

        //GraphicsContext gc = canvas.getGraphicsContext2D();
        //root.getChildren().add(canvas);

        /*Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                "scene.fxml")));*/


        EntityDrawer snakeDrawer = new EntityDrawer(game.getCellSize());


        // anonymous animationTimer class.
        new AnimationTimer() {
            long lastTick = 0;
            public void handle(long nanoTimeNow) {

               if (gameOver){
                    //gc.setFill(Color.RED);
                    //gc.fillRect(0,0,512,512);
                    System.out.println("game ended");
                    return;
                }

                /*if (lastTick == 0){
                    lastTick = nanoTimeNow;
                    gameOver = game.makeMove(moveDirection);
                    //drawSnake(gc,game.getSnakePos(),moveDirection);
                    tick();
                    return;
                }*/
                if (nanoTimeNow - lastTick > 1000000000 / game.getGp().getSpeed()){ //each second
                    //root.getChildren().removeAll();
                    lastTick = nanoTimeNow;
                    gameOver = game.makeMove(moveDirection);

                    tick();
                    //colorSnakeTiles(root);
                    //drawSnake(gc,game.getSnakePos(),moveDirection);
                }
            }
        }.start();

        Scene scene = new Scene(root,512,512);
        scene.setFill(Color.BLACK);


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


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * called every in game tick. */
    public void tick(){
        entityDrawer.drawSnakeBody(game.getSnakePos(),root,moveDirection);
        entityDrawer.drawConsumbable(game.getFruits(),root);
        updateScore();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
