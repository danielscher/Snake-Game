package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayDeque;
import java.util.Deque;
import main.Board;
import main.Direction;
import main.Position;
import main.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {

    private Board board = new Board(5,1,new Position(5,5));

    private Deque<Position> genSnake2(){
        Deque<Position> snake = new ArrayDeque<>();
        snake.push(new Position(2,3));
        snake.push(new Position(2,2));
        return snake;
    }

    private void clearBoard(){
        for (Tile[] l : board.getGrid()){
            for (Tile t : l){
                t.resetTile();
            }
        }
    }

    private void positionSnake(final int row,final int col, Tile[][] grid){
        grid[row][col].setSnake(true);
    }


    @BeforeEach
    private void resetBoard(){
        clearBoard();
        Deque<Position> snake = genSnake2();
        board.setSnake(snake);
        positionSnake(3,2, board.getGrid());
        positionSnake(2,2, board.getGrid());
    }

    // test if snake moves only one tile at a time.
    //when moving up previous tail tile should indicate no snake is present after move.
    @Test
    public void testSnakeMoves1Tile(){
        board.updateSnakePos(Direction.UP);
        assertTrue(board.getGrid()[1][2].isSnake());
        assertTrue(board.getGrid()[2][2].isSnake());
        assertFalse(board.getGrid()[3][2].isSnake());
    }


}
