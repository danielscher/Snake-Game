package gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.Direction;
import main.Position;

public class EntityDrawer {


    private Set<Rectangle> snakeBody = new HashSet<>();
    private Set<Circle> currFruits = new HashSet<>();
    private int cellSize;
    private final Color headColor;
    private final Color bodyColor;


    public EntityDrawer(final int cellSize) {
        this.cellSize = cellSize;
        headColor = Color.RED;
        bodyColor = Color.GREEN;
    }

    //TODO: add more initialization properties to constructor.

    public void drawSnakeBody(List<Position> snakePosition, Group root, Direction dir) {

        Set<Rectangle> newSnake = new HashSet<>();

        // clear old body.
        root.getChildren().removeAll(snakeBody);

        //draw new head.
        final int radius = cellSize / 2;
        Position head = snakePosition.get(0);
        assert head != null;
        Rectangle headRect = new Rectangle(head.getX() - radius, head.getY() - radius, cellSize,
                cellSize);
        headRect.setStroke(Color.INDIANRED);
        headRect.setFill(headColor);
        newSnake.add(headRect);

        //draw new body.
        for (int i = 1; i < snakePosition.size(); i++) {
            int xPos = snakePosition.get(i).getX();
            int yPos = snakePosition.get(i).getY();
            Rectangle rect = new Rectangle(xPos - radius, yPos - radius, cellSize, cellSize);
            rect.setFill(bodyColor);
            rect.setStroke(Color.GREENYELLOW);
            newSnake.add(rect);
        }

        // replace current body with new one and add it to root.
        root.getChildren().addAll(newSnake);
        snakeBody = newSnake;
    }

    /**
     * removes consumable object graphics from the Group and adds a new one in the positions
     * indicated by the fruitPosition list
     * @param root is the root node of the UI scene
     * @param fruitPosition positions where the new fruits will be rendered*/
    public void drawConsumbable(List<Position> fruitPosition, Group root) {

        final int radius = cellSize / 2;
        root.getChildren().removeAll(currFruits);
        Set<Circle> newFruits = new HashSet<>();

        for (Position pos : fruitPosition) {
            int xPos = pos.getX();
            int yPos = pos.getY();
            Circle el = new Circle(xPos, yPos, radius);
            el.setFill(Color.MAGENTA);
            el.setStroke(Color.PINK);
            newFruits.add(el);
        }

        root.getChildren().addAll(newFruits);
        currFruits = newFruits;
    }

}
