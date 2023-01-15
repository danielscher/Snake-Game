package main;

import java.util.Deque;
import java.util.Set;
import main.consumables.Effect;
import main.consumables.Food;
import java.util.List;
import java.util.Random;
import main.consumables.FoodType;

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
        this.boardSize = boardSize;
        this.cellSize = cellSize;
        availableConsumables = foods;
        int randX = rand.nextInt(boardSize/cellSize);
        int randY = rand.nextInt(boardSize/cellSize);
        board = new Board(boardSize, cellSize, new Position(randX, randY), getNewRandomPosition());
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
            eat(currHeadTile, gp, board);
            //board.spawnFruit(pos);
        }

        return false;
    }

    private void eat(Tile tile, GameProperties gp, Board board){
        Food food = tile.getFood();

        if (food.getType() == FoodType.FRUIT) {
            board.despawnFruit(tile);
            board.increaseBodySize();
            Position pos = getNewRandomPosition();
            board.spawnFruit(pos);
            increaseScore(gp.getScoreMultiplier(),1);
        }

        else {
            List<Effect> effects = food.getEffects();
            for (Effect effect : effects) {
                gp.applyEffect(effect);
            }

        }

    }

    private boolean checkForFood(Position current){
        return board.getGridTileByPosition(current).isFoodPresent();
    }

    public void increaseScore(double multiplier, int amount){
        int score = board.getScore();
        score += multiplier*amount;
        board.setScore(score);
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
    public Set<Position> getFruits(){
        return board.getFruits();
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
