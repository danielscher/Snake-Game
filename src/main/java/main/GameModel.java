package main;

import java.util.Deque;
import main.consumables.Food;
import java.util.List;
import java.util.Random;

public class GameModel {

    private final int cellSize;
    private List<Food> availableConsumables;
    private Board board;
    private final int boardSize;
    private Random rand;
    private GameProperties gp = new GameProperties();
    private Direction currentMovmentDirection;

    public GameModel(long seed, int boardSize, int cellSize, List<Food> foods) {
        rand = new Random(seed);
        availableConsumables = foods;
        int randX = rand.nextInt(boardSize/cellSize);
        int randY = rand.nextInt(boardSize/cellSize);
        board = new Board(boardSize, cellSize, new Position(randX, randY));
        this.boardSize = boardSize;
        this.cellSize = cellSize;
    }


    public boolean makeMove(Direction dir) {

        // if trying to move in the opposite direction continue moving the current direction.
        if (dir.getOppesite() == currentMovmentDirection) {
            dir = currentMovmentDirection;
        }

        // game ends if next move is out of bounds or eats self.
        if (board.checkMoveOutOfBounds(dir)) {
            System.out.println("hit wall");
            return true;
        }

        if (board.checkEatsSelf(dir)) {
            System.out.println("hit self");
            return true;
        }

        Position pos = getNewRandomPosition();
        board.updateSnakePos(dir);
        currentMovmentDirection = dir;

        //check and consume food if main.consumables are present on new position
        Tile currHeadTile = board.getGridTileByPosition(board.getHeadPosition());
        if (currHeadTile.isFoodPresent()) {
            board.eat(currHeadTile, gp);
            board.spawnConsumable(pos);
        }

        return false;
    }




    /**
     * get new random position on board
     */
    private Position getNewRandomPosition() {
        return new Position(rand.nextInt(boardSize), rand.nextInt(boardSize));
    }

    public Deque<Position> getSnakePos(){
        return board.getsnakeQueue();
    }

    public GameProperties getGp() {
        return gp;
    }

    public int getCellSize() {
        return cellSize;
    }

    public Board getBoard() {
        return board;
    }
}
