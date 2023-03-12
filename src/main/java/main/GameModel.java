package main;

import java.util.Deque;
import java.util.Set;
import java.util.stream.Collectors;
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

    private int score = 0;

    public GameModel(long seed, int boardSize, int cellSize, List<Food> foods) {
        rand = new Random(seed);
        this.boardSize = boardSize;
        this.cellSize = cellSize;
        availableConsumables = foods;
        final Position rndSnakeStart = getNewRandomPosition();
        final Position rndFruitPos = getNewRandomPosition();
        board = new Board(boardSize, cellSize, rndSnakeStart,
                rndFruitPos);
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

        board.updateSnakePos(dir);
        currentMovmentDirection = dir;

        //check and consume food if main.consumables are present on new position
        Tile currHeadTile = board.getHeadTile();
        if (currHeadTile.isFoodPresent()) {
            eat(currHeadTile, gp, board);
            //board.spawnFruit(pos);
        }

        return false;
    }

    private void eat(Tile tile, GameProperties gp, Board board) {
        Food food = tile.getFood();

        if (food.getType() == FoodType.FRUIT) {
            board.despawnFruit(tile);
            board.increaseBodySize();
            Position pos = getNewRandomPosition();
            board.spawnFruit(pos);
            increaseScore(gp.getScoreMultiplier(), 1);
            gp.normalSpeedInc();
        } else {
            List<Effect> effects = food.getEffects();
            for (Effect effect : effects) {
                gp.applyEffect(effect);
            }

        }

    }


    public void increaseScore(double multiplier, int amount) {

//        int score = board.getScore();
//        score += multiplier * amount;
//        board.setScore(score);

        score += multiplier * amount;
    }



    /**
     * get new random position of a cell on grid
     * @return position.getX = col , pos.getY = row
     */
    private Position getNewRandomPosition() {
        final int row = rand.nextInt(boardSize/cellSize);
        final int col = rand.nextInt(boardSize/cellSize);
        return new Position(col, row);
    }

    public List<Position> getSnakePos() {
        return translateGridToPixel(board.getsnakeQueue().stream().toList());
    }

    public List<Position> getFruits() {
        return translateGridToPixel(board.getFruits().stream().toList());
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

    public int getScore() {
        return score;
    }

    private Position translateGridToPixel(Position cell) {
        final int row = cell.getY();
        final int col = cell.getX();
        return board.getGrid()[row][col].getCenter();
    }
    public List<Position> translateGridToPixel(List<Position> gridPositions){
        return gridPositions.stream().map(this::translateGridToPixel).collect(Collectors.toList());
    }
}
