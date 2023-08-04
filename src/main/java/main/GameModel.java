package main;

import java.util.*;
import java.util.stream.Collectors;

import static main.Utils.translateCoordToPixel;
import static main.consumables.FoodType.getFoodByCode;

public class GameModel {

    private final int cellSize;
    private final int boardSize;
    private final Board board;
    private final Snake snake;
    private boolean eaten;

    /**
     * used to despawn fruits after some time interval.
     */
    private final LinkedList<Tile> fruitTiles = new LinkedList<>();
    private final HashSet<Pixel> fruitPixelPos = new HashSet<>();
    private final Random rand;
    private final GameProperties gp = new GameProperties();
    private Direction currentMovementDirection;

    private int score = 0;

    public GameModel(long seed, int boardSize, int cellSize) {
        rand = new Random(seed);
        this.boardSize = boardSize;
        this.cellSize = cellSize;
        final Coords rndSnakeStart = getNewRandomCoordPosition();
        //currentMovementDirection = Direction.values()[rand.nextInt(4)];
        currentMovementDirection = Direction.class.getEnumConstants()[Direction.class.getEnumConstants().length - 1];
        board = new Board(boardSize, cellSize);
        snake = new Snake(board, currentMovementDirection, rndSnakeStart, 3);
        spawnConsumable();
    }


    /**
     * moves snake in desired positions while performing checks
     *
     * @return true if the move results in game end.
     */
    public boolean makeMove(Direction dir) {

        // ignore if trying to move in opposite direction of curr movement direction.
        if (dir.getOppesite() == currentMovementDirection) {
            dir = currentMovementDirection;
        }

        //TODO: game not over when right wall hit.
        if (snake.moveSnake(dir)) {
            return true;
        }
        currentMovementDirection = dir;

        //check and consume food if main.consumables are present on new position
        Tile currHeadTile = board.getTileByCoords(snake.getHeadPosition());
        if (currHeadTile.isFoodPresent()) {
            eat(currHeadTile);
            eaten = true;
            gp.increaseSpeed();
        }

        return false;
    }

    /**
     * Consumes food present on tile and spawns a new fruit
     */
    private void eat(Tile tile) {
        int foodCode = tile.getFood();
        getFoodByCode(foodCode).applyEffect();
        despawnConsumable(tile);
        spawnConsumable();
        increaseScore(gp.getScoreMultiplier(), 1);
    }

    private void increaseScore(double multiplier, int amount) {
        score += multiplier * amount;
    }

    /**
     * get new random position of a cell on grid
     *
     * @return position.getX = col , pos.getY = row
     */
    private Coords getNewRandomCoordPosition() {
        //TODO: move to utils.
        final int row = rand.nextInt(boardSize / cellSize);
        final int col = rand.nextInt(boardSize / cellSize);
        return new Coords(col, row);
    }

    /**
     * @return current snake position on board as a list of positions
     */
    public List<Pixel> getSnakePixelPos() {
        return translateGridToPixel(snake.getSnakeCoords().stream().toList());
    }

    /**
     * @return current position of consumables on boards as list of positions
     */
    public List<Pixel> getFruits() {
        return fruitPixelPos.stream().toList();
    }


    /**
     * @param cell (col,row) position in the board grid.
     * @return center pixel position of a grid cell
     */
    private Pixel translateGridToPixel(Coords cell) {
        //TODO: move to utils.
        final int row = cell.getY();
        final int col = cell.getX();
        return board.getGrid()[row][col].getCenter();
    }


    /**
     * @param gridCoords list of grid cell positions (col,row)
     * @return list of pixel position.
     */
    public List<Pixel> translateGridToPixel(List<Coords> gridCoords) {
        return gridCoords.stream().map(c -> translateCoordToPixel(c, cellSize)).collect(Collectors.toList());
    }

    public GameProperties getGp() {
        return gp;
    }

    public int getCellSize() {
        return cellSize;
    }


    public int getScore() {
        return score;
    }

    /**
     * Creates new random consumable on a random tile free tile.
     */
    private void spawnConsumable() {
        Coords fruit = getNewRandomCoordPosition();
        Tile t = board.getTileByCoords(fruit);
        while (!t.isEmpty()) {
            fruit = getNewRandomCoordPosition();
            t = board.getTileByCoords(fruit);
        }
        //TODO: spawn different fruits according to some probability.
        t.setFood(1); // always a fruit
        fruitPixelPos.add(t.getCenter());
        fruitTiles.add(t);
    }

    /**
     * Removes oldest fruit.
     */
    private void despawnConsumable() {
        Tile t = fruitTiles.poll();
        System.out.println(t);
        assert t != null;
        despawnConsumable(t);
    }

    /**
     * Removes consumable at a specific tile.
     */
    private void despawnConsumable(Tile tile) {
        tile.removeFood();
        fruitPixelPos.remove(tile.getCenter());
    }

    /**
     * Removes oldest fruit and spawns a new one on a random position
     */
    public void respawnConsumable() {
        despawnConsumable();
        //eaten = false;
        spawnConsumable();
    }

    public boolean getEaten() {
        return eaten;
    }

    public void resetEaten(){
        eaten = false;
    }

}
