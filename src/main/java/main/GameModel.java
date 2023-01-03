package main;

import main.consumables.Food;
import gui.Controller;
import java.util.List;
import java.util.Random;

public class GameModel {

    private List<Food> availableConsumables;
    private Board board;
    private final int boardSize;
    private Random rand;
    private Boolean gameOver;
    private GameProperties gp = new GameProperties();

    public GameModel(long seed, int boardSize, List<Food> foods) {
        rand = new Random(seed);
        availableConsumables = foods;
        int randX = rand.nextInt(boardSize);
        int randY = rand.nextInt(boardSize);
        board = new Board(new Snake(), boardSize, new Position(randX, randY));
        this.boardSize = boardSize;
    }


    /**
     * main game loop
     */
    public void gameLoop(Direction dir) {
        while (!gameOver) {
            Position pos = getNewRandomPosition();
            gameOver = board.handle(dir, gp, pos);
        }
    }

    /**
     * get new random position on board
     */
    private Position getNewRandomPosition() {
        return new Position(rand.nextInt(boardSize), rand.nextInt(boardSize));
    }

    public List<Position> getSnakePos(){
        return board.getSnakeBody();
    }

    public GameProperties getGp() {
        return gp;
    }
}
