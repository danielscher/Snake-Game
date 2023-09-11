package model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Snake {

    private final Board board;
    private final Deque<Coords> snakeCoords = new ArrayDeque<>();

    /**Create snake of size 3.*/
    public Snake(Board board, Direction direction, Coords head, final int initialSize) {
        this.board = board;
        genSnake(initialSize,head,direction.getOpposite());
    }

    private void genSnake(int size, Coords headCoord , final Direction growthDirection){
        snakeCoords.add(headCoord);
        board.getTileByCoords(headCoord).setSnake(true);
        size--;
        for (;size > 0; size--){
            headCoord = board.getNextFreePosition(growthDirection, headCoord);
            snakeCoords.add(headCoord);
            board.getTileByCoords(headCoord).setSnake(true);
        }
    }

    /**
     * Moves snake's position by pushing new head and deleting tail.
     *
     * @param dir direction of movement.
     * @return true if move results in game ending move.
     */
    public boolean moveSnake(Direction dir) {

        // generate next position of snake.
        Coords head = snakeCoords.getFirst();
        Coords newHead = head.getNextCoord(dir);

        // check for game ending move.
        if (board.checkOutOfBounds(newHead)) {
            return true;
        }

        // don't delete tail if consumed fruit to increase size.
        Coords tail = snakeCoords.peekLast();
        assert tail != null;
        if (!board.getTileByCoords(newHead).isFoodPresent()) {
            board.getTileByCoords(tail).setSnake(false);
            snakeCoords.removeLast();
        }

        // check if snake eats self.
        if (snakeCoords.contains(newHead)) {
            return true;
        }

        // move snake.
        snakeCoords.push(newHead);
        board.getTileByCoords(newHead).setSnake(true);

        return false;
    }

    public void increaseBodySize(Direction dir) {
        dir = dir.getOpposite();
        Coords newTail = snakeCoords.getLast().getNextCoord(dir);
        board.getTileByCoords(newTail).setSnake(true);
        snakeCoords.offerLast(newTail);
    }

    Coords getHeadPosition() {
        return snakeCoords.getFirst();
    }

    public Deque<Coords> getSnakeCoords() {
        return snakeCoords;
    }


    public int getSize(){
        return snakeCoords.size();
    }


}
