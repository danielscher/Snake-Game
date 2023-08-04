package tests;

import main.Board;
import main.Coords;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private final Board board = new Board(320, 32);

    @Test
    void checkOutOfBounds() {
    }

    @Test
    void getNextFreePosition() {
    }

    @Test
    void getGridTileByPosition() {
        assertEquals(board.getGrid()[0][0].getCenter(),board.getGridTileByPosition(new Coords(16,16)).getCenter());
        assertEquals(board.getGrid()[0][9].getCenter(),board.getGridTileByPosition(new Coords(304,16)).getCenter());
        assertEquals(board.getGrid()[9][0].getCenter(),board.getGridTileByPosition(new Coords(16,304)).getCenter());
        assertEquals(board.getGrid()[9][9].getCenter(),board.getGridTileByPosition(new Coords(304,304)).getCenter());
        assertEquals(board.getGrid()[2][2].getCenter(),board.getGridTileByPosition(new Coords(96,96)).getCenter());

    }

    @Test
    void getTileByPos() {
    }

    @Test
    void clearBoard() {
    }
}