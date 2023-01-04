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

    public GameModel(long seed, int boardSize, int cellSize, List<Food> foods) {
        rand = new Random(seed);
        availableConsumables = foods;
        int randX = rand.nextInt(boardSize/cellSize);
        int randY = rand.nextInt(boardSize/cellSize);
        board = new Board(boardSize, cellSize, new Position(randX, randY));
        this.boardSize = boardSize;
        this.cellSize = cellSize;
    }


    /**
     * main game loop
     */
    public boolean makeMove(Direction dir) {
        Position pos = getNewRandomPosition();
        return board.handle(dir, gp, pos);
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
