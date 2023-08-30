package tests;


import main.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {

    private final Board board = new Board(5, 1);
    private Snake snake = new Snake(board, Direction.UP,new Coords(2,2),3);

    @BeforeEach
    public void reset(){
        board.clearBoard();
        snake = new Snake(board,Direction.UP,new Coords(2,2),3);
    }

    @Test
    public void testMoveUp() {
        snake.moveSnake(Direction.UP);
        assertTrue(board.getGrid()[1][2].isSnake());
        assertTrue(board.getGrid()[2][2].isSnake());
        assertTrue(board.getGrid()[3][2].isSnake());
        assertFalse(board.getGrid()[4][2].isSnake(),"Tail position was not deleted.");
    }

    @Test
    public void testMoveLeft() {
        snake.moveSnake(Direction.LEFT);
        assertTrue(board.getGrid()[2][1].isSnake());
        assertTrue(board.getGrid()[2][2].isSnake());
        assertTrue(board.getGrid()[3][2].isSnake());
        assertFalse(board.getGrid()[4][2].isSnake(),"Tail position was not deleted.");
    }

    @Test
    public void testMoveRight() {
        snake.moveSnake(Direction.RIGHT);
        assertTrue(board.getGrid()[2][3].isSnake());
        assertTrue(board.getGrid()[2][2].isSnake());
        assertTrue(board.getGrid()[3][2].isSnake());
        assertFalse(board.getGrid()[4][2].isSnake(),"Tail position was not deleted.");
    }

    @Test
    public void testMoveOppositeDirection() {
        assertTrue(snake.moveSnake(Direction.DOWN),"expected to set flag for game end.");
    }

    @Test
    public void testGrowing(){
        board.getGrid()[1][2].setFood(1);
        snake.moveSnake(Direction.UP);
        assertEquals(4,snake.getSize(),"expected size 4,got"+ snake.getSize()+ ".");
    }

    @Test
    public void testMovingInCycle(){
        board.getGrid()[1][2].setFood(1);
        snake.moveSnake(Direction.UP);
        snake.moveSnake(Direction.RIGHT);
        snake.moveSnake(Direction.DOWN);
        assertFalse(snake.moveSnake(Direction.LEFT));
    }

    @Test
    public void testHitWall(){
        snake.moveSnake(Direction.UP);
        snake.moveSnake(Direction.UP);
        assertTrue(snake.moveSnake(Direction.UP),"expected to set game end flag.");
    }

    @Test
    public void testHitSelf() {
        board.getGrid()[1][2].setFood(1);
        board.getGrid()[0][2].setFood(1);
        snake.moveSnake(Direction.UP);
        snake.moveSnake(Direction.UP);
        snake.moveSnake(Direction.RIGHT);
        snake.moveSnake(Direction.DOWN);
        assertTrue(snake.moveSnake(Direction.LEFT),"expected to eat self.");
    }

    //TODO: add game ending tests.


}
