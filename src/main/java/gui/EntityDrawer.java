package gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Pixel;
import model.Utils;

public class EntityDrawer {


    private List<Circle> snakeBody = new ArrayList<>();
    private Set<Circle> currFruits = new HashSet<>();
    private final int cellSize;
    private final Color headColor;
    private final Color bodyColor;


    public EntityDrawer(final int cellSize) {
        this.cellSize = cellSize;
        headColor = Color.RED;
        bodyColor = Color.GREEN;
    }


    synchronized public void drawSnakeBody(List<Pixel> snakeCoords, AnchorPane pane) {
        List<Circle> newSnake = new ArrayList<>();


        //draw new head.
        final int radius = cellSize / 2;
//        Pixel head = snakeCoords.get(0);
//        assert head != null;
//        Rectangle headRect = new Rectangle(head.getX() - radius, head.getY() - radius, cellSize,
//                cellSize);
//        headRect.setStroke(Color.INDIANRED);
//        headRect.setFill(headColor);
//        newSnake.add(headRect);

        Timeline timeline = new Timeline();

        //draw new body.
        for (int i = 0; i < snakeCoords.size(); i++) {
            final int index = i;
            Pixel segment = snakeCoords.get(i);
            Circle rect = createRectangle(segment);
            newSnake.add(rect);

            if (i >= snakeBody.size()) {continue;}
            // Define a KeyFrame to update the position of this segment over time
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(500),
                    new KeyValue(snakeBody.get(index).centerXProperty(), segment.getX()),
                    new KeyValue(snakeBody.get(index).centerYProperty(), segment.getY())
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        if (snakeBody.isEmpty()) {
            snakeBody = newSnake;
            pane.getChildren().addAll(snakeBody);}

        else if (snakeBody.size() < newSnake.size()) {
            Circle circ = newSnake.get(newSnake.size()-1);
            snakeBody.add(circ);
            pane.getChildren().add(circ);
        }
        timeline.play();
    }

    /**
     * removes consumable object graphics from the Group and adds a new one in the positions
     * indicated by the fruitPosition list
     * @param pane is the root node of the UI scene
     * @param fruitPixelPos positions where the new fruits will be rendered*/
    public void drawConsumable(List<Pixel> fruitPixelPos, AnchorPane pane) {

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

    private Circle createRectangle(Pixel pixel) {
        final int radius = cellSize / 2;
        Circle rect = new Circle(pixel.getX(), pixel.getY(), radius);
        rect.setFill(Color.GREEN);
        rect.setStroke(Color.GREENYELLOW);
        return rect;
    }

}
