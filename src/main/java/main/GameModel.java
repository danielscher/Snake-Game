package main;

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
    private Direction currentMovementDirection;

    private int score = 0;

    public GameModel(long seed, int boardSize, int cellSize, List<Food> foods) {
        rand = new Random(seed);
        this.boardSize = boardSize;
        this.cellSize = cellSize;
        availableConsumables = foods;
        final Position rndSnakeStart = getNewRandomPosition();
        final Position rndFruitPos = getNewRandomPosition();
        currentMovementDirection = Direction.values()[rand.nextInt(4)];
        board = new Board(boardSize, cellSize, rndSnakeStart,
                rndFruitPos, currentMovementDirection);
    }


    /**
     * moves snake in desired positions while performing checks
     * @return true if the move results in game end.*/
    public boolean makeMove(Direction dir) {

        // ignore if trying to move in opposite direction of curr movement direction.
        if (dir.getOppesite() == currentMovementDirection) {
            dir = currentMovementDirection;
        }

        // return if hit boundaries.
        if (board.checkMoveOutOfBounds(dir)) {
            //System.out.println("hit wall");
            return true;
        }

        // return if hit self.
        if (board.checkEatsSelf(dir)) {
            //System.out.println("hit self");
            return true;
        }

        //TODO: move updateSnakePos to gameModel class

        // move snake in movement direction
        Tile nextHeadTile = getNextTile(dir);
        board.updateSnakePos(dir, nextHeadTile.isFoodPresent());
        currentMovementDirection = dir;

        //check and consume food if main.consumables are present on new position
        Tile currHeadTile = board.getHeadTile();
        if (currHeadTile.isFoodPresent()) {
            eat(currHeadTile, gp, board);
            gp.normalSpeedInc();
        }

        return false;
    }

    /**
     * consumes food present on tile and spawns a new fruit
     * */
    private void eat(Tile tile, GameProperties gp, Board board) {
        Food food = tile.getFood();
        if (food.getType() == FoodType.FRUIT) {
            board.despawnFruit(tile);
            Position pos = getNewRandomPosition();
            board.spawnFruit(pos);
            increaseScore(gp.getScoreMultiplier(), 1);
        } else {
            List<Effect> effects = food.getEffects();
            for (Effect effect : effects) {
                gp.applyEffect(effect);
            }
        }
    }

    private void increaseScore(double multiplier, int amount) {
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

    /**@return current snake position on board as a list of positions */
    public List<Position> getSnakePos() {
        return translateGridToPixel(board.getSnakeQueue().stream().toList());
    }

    /**@return current position of consumables on boards as list of positions*/
    public List<Position> getFruits() {
        return translateGridToPixel(board.getFruits().stream().toList());
    }


    /**
     * @return center pixel position of a grid cell
     * @param cell (col,row) position in the board grid.*/
    private Position translateGridToPixel(Position cell) {
        final int row = cell.getY();
        final int col = cell.getX();
        return board.getGrid()[row][col].getCenter();
    }

    /**
     * @return next closest tile in the given direction*/
    private Tile getNextTile(Direction dir){
        Position nextHeadPos = board.getHeadPosition().getNextPosition(dir);
        final int row = nextHeadPos.getY();
        final int col = nextHeadPos.getX();
        return board.getGrid()[row][col];
    }

    /**@return list of pixel position.
     * @param gridPositions list of grid cell positions (col,row)*/
    public List<Position> translateGridToPixel(List<Position> gridPositions){
        return gridPositions.stream().map(this::translateGridToPixel).collect(Collectors.toList());
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
}
