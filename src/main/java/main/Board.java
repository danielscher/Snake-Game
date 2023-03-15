package main;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import main.consumables.Food;
import java.util.ArrayDeque;
import main.consumables.Fruit;

public class Board {

    private final Tile[][] grid;
    private final int tileSize;
    private final int tileNum;
    private int numCurrentConsumables = 1;
    private final int gridSize;
    private int score;
    private Snake snake;
    private Deque<Food> availableFoods = new ArrayDeque<>();

    // contains all fruit on board as grid positions (row,col).
    private Set<Position> fruits = new HashSet<>();

    // contains positions of all snake body parts on board as (row,col).
    private Deque<Position> snakeQueue = new ArrayDeque<>();


    /**
     * Sets up the board for the game.
     * @param gridSize dimension of the game board in pixels.
     * @param tileSize size of each cell in pixels.
     * @param snakePos starting grid position of the snake's head (col,row).
     * @param startFruit position of the first fruit on board (col,row)
     * @param startDir starting movement direction at the start of the game.
     */
    public Board(final int gridSize, final int tileSize, final Position snakePos,
            final Position startFruit, Direction startDir) {
        //TODO: check if initPos < gridSize.

        //calculate number of tiles.
        this.tileSize = tileSize;
        final int tileNum = gridSize / tileSize;
        this.tileNum = tileNum;

        //initialize the grid.
        grid = new Tile[tileNum][tileNum];
        this.gridSize = gridSize;

        //fill grid with tiles.
        for (int row = 0; row < tileNum; row++) {
            for (int col = 0; col < tileNum; col++) {
                // i,j represent the upper left corner of the tile.
                grid[row][col] = new Tile(new Position(col * tileSize + (tileSize / 2),
                        row * tileSize + (tileSize / 2)));
            }
        }
        // init snake to length 3.
        this.snake = new Snake();
        grid[snakePos.getY()][snakePos.getX()].setSnake(true);
        snakeQueue.add(snakePos);
        increaseBodySize(startDir);
        increaseBodySize(startDir);

        // set fruit
        spawnFruit(startFruit);
    }


    /**
     * check if move results in Game End because of an out-of-bounds move.
     */
    public boolean checkMoveOutOfBounds(final Direction dir) {
        //get head position on grid by row column.
        final Position currHeadPos = snakeQueue.getFirst();
        final int col = currHeadPos.getX();
        final int row = currHeadPos.getY();

        // check for board bounds.
        switch (dir) {
            case UP -> {
                return (row == 0);
            }
            case DOWN -> {
                return (row == tileNum - 1);
            }
            case LEFT -> {
                return (col == 0);
            }
            case RIGHT -> {
                return (col == tileNum - 1);
            }
        }
        return false;
    }

    /**
     * checks if snake hits/eats itself on the next move
     */
    public boolean checkEatsSelf(final Direction dir) {
        final Position currHeadPos = snakeQueue.getFirst();
        final Position nextHeadPos = currHeadPos.getNextPosition(dir);
        return getTileByPos(nextHeadPos).isSnake();
    }

    /**
     * @param pos is a cell position (col,row)
     */
    public void spawnFruit(Position pos) {
        Food fruit = new Fruit();
        if (!fruits.contains(pos)) {
            fruits.add(pos);
            grid[pos.getY()][pos.getX()].setFood(fruit);
        }
    }


    /**
     * moves the snake one tile in the movement direction by removing tail and pushing new head.
     * @param eat if set to true increases body of snake by 1.
     */
    public void updateSnakePos(Direction dir, Boolean eat) {

        Position tail = snakeQueue.peekLast();
        assert tail != null;

        // delete tail if consumed fruit.
        if (!eat) {
            getTileByPos(tail).setSnake(false);
            snakeQueue.removeLast();
        }

        Position head = snakeQueue.getFirst();
        Position newHead = new Position(head.getX(), head.getY());
        newHead.moveInDirection(dir, tileSize);           // new position in movement direction
        snakeQueue.push(newHead);
        getTileByPos(newHead).setSnake(true);
    }


    /**
     * increases snakes body by one tile by creating a new position of tail in the opposite
     * direction to the current movement
     */
    public void increaseBodySize(Direction dir) {
        dir = dir.getOppesite();
        Position newTail = snakeQueue.getLast().getNextPosition(dir);
        getTileByPos(newTail).setSnake(true);
        snake.grow();
        snakeQueue.offerLast(newTail);
    }

    public int getScore() {
        return score;
    }

    Position getHeadPosition() {
        return snakeQueue.getFirst();
    }

    public Deque<Position> getSnakeQueue() {
        return snakeQueue;
    }


    /**
     * calculates tile on grid by PIXEL Position
     *
     * @param pos IS THE PIXEL POSITION.
     */
    Tile getGridTileByPosition(Position pos) {
        int col = (pos.getX() - tileSize / 2) / tileSize;
        int row = (pos.getY() - tileSize / 2) / tileSize;
        return grid[row][col];
    }

    /**
     * translates position of a pixel (x,y) to a (row,column) position on the grid useful for
     * checking bounds.
     *
     * @return Position with y = row and x = column
     */
    private Position translatePixelToGrid(Position pixelPos) {
        int col = (pixelPos.getX() - tileSize / 2) / tileSize;
        int row = (pixelPos.getY() - tileSize / 2) / tileSize;
        return new Position(col, row);
    }

    /**
     * translates grid cell position (row,col) to pixel position.
     *
     * @param cell corresponds to the y = row, x = column coordinates of the boards grid.
     */
    private Tile getTileByPos(Position cell) {
        final int row = cell.getY();
        final int col = cell.getX();
        return grid[row][col];
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public void setSnake(Deque<Position> snake) {
        snakeQueue = snake;
    }


    public Set<Position> getFruits() {
        return fruits;
    }

    public Tile getHeadTile() {
        return getTileByPos(snakeQueue.getFirst());
    }

    //remove fruit from tile and from positions of curr fruits.
    public void despawnFruit(Tile tile) {
        Position fruitPos = translatePixelToGrid(tile.getCenter());
        fruits.remove(fruitPos);
        tile.setFood(null);
    }
}


