package gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.Direction;
import main.Coords;
import main.Pixel;

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

    public void drawSnakeBody(List<Pixel> snakeCoords, AnchorPane pane) {

        Set<Rectangle> newSnake = new HashSet<>();

        // clear old body.
        pane.getChildren().removeAll(snakeBody);

        //draw new head.
        final int radius = cellSize / 2;
        Pixel head = snakeCoords.get(0);
        assert head != null;
        Rectangle headRect = new Rectangle(head.getX() - radius, head.getY() - radius, cellSize,
                cellSize);
        headRect.setStroke(Color.INDIANRED);
        headRect.setFill(headColor);
        newSnake.add(headRect);

        //draw new body.
        for (int i = 1; i < snakeCoords.size(); i++) {
            int xPos = snakeCoords.get(i).getX();
            int yPos = snakeCoords.get(i).getY();
            Rectangle rect = new Rectangle(xPos - radius, yPos - radius, cellSize, cellSize);
            rect.setFill(bodyColor);
            rect.setStroke(Color.GREENYELLOW);
            newSnake.add(rect);
        }

        // replace current body with new one and add it to root.
        pane.getChildren().addAll(newSnake);
        snakeBody = newSnake;
    }

    /**
     * removes consumable object graphics from the Group and adds a new one in the positions
     * indicated by the fruitPosition list
     * @param pane is the root node of the UI scene
     * @param fruitPixelPos positions where the new fruits will be rendered*/
    public void drawConsumbable(List<Pixel> fruitPixelPos, AnchorPane pane) {

        final int radius = cellSize / 2;
        pane.getChildren().removeAll(currFruits);
        Set<Circle> newFruits = new HashSet<>();

        for (Pixel center : fruitPixelPos) {
            int xPos = center.getX();
            int yPos = center.getY();
            Circle el = new Circle(xPos, yPos, radius);
            el.setFill(Color.MAGENTA);
            el.setStroke(Color.PINK);
            newFruits.add(el);
        }

        pane.getChildren().addAll(newFruits);
        currFruits = newFruits;
    }

}
