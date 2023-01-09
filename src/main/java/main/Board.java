package main;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import main.consumables.Effect;
import main.consumables.Food;
import java.util.ArrayDeque;
import java.util.List;
import main.consumables.FoodType;
import main.consumables.Fruit;

public class Board {

    private Tile[][] grid;
    private final int tileSize;
    private final int tileNum;

    private int numCurrentConsumables = 1;
    private Direction currentMovmentDirection;
    private final int gridSize;
    private int score;
    private Snake snake;

    private Deque<Position> snakeQueue = new ArrayDeque<>();

    private Deque<Food> availableFoods = new ArrayDeque<>();

    private Set<Position> fruits = new HashSet<>();


    /**
     * constructs the board for the game.
     */
    public Board(final int gridSize, final int tileSize, final Position snakePos, final Position fruit) {
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
        // set snake
        currentMovmentDirection = Direction.UP;
        this.snake = new Snake();
        Position headPos = translateGridToPixel(snakePos);   //get head position by pixel
        grid[snakePos.getY()][snakePos.getX()].setSnake(true);
        snakeQueue.add(headPos);
        increaseBodySize();
        increaseBodySize();

        // set fruit
        spawnFruit(fruit);
    }


    /**
     * check if move results in Game End because of an out-of-bounds move.
     */
    public boolean checkMoveOutOfBounds(final Direction dir) {
        //get head position on grid by row column.
        final Position currHeadPos = translatePixelToGrid(snakeQueue.getFirst());
        final int row = currHeadPos.getX();
        final int col = currHeadPos.getY();

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
     * checks if snake bites/eats itself on the next move
     */
    public boolean checkEatsSelf(final Direction dir) {
        final Position currHeadPos = snakeQueue.getFirst();
        final Position nextHeadPos = currHeadPos.getNextPosition(dir, tileSize);
        boolean isSelfEat = getGridTileByPosition(nextHeadPos).isSnake();
        return isSelfEat;
    }

    /**
     * moves snake and returns true if game ended
     */
    public boolean handle(Direction dir, GameProperties gp, Position randConsumablePos) {

//        // if trying to move in the opposite direction continue moving the current direction.
//        if (dir.getOppesite() == currentMovmentDirection) {
//            dir = currentMovmentDirection;
//        }

//        // game ends if next move is out of bounds or eats self.
//        if (checkMoveOutOfBounds(dir)) {
//            System.out.println("hit wall");
//            return true;
//        }

//        if (checkEatsSelf(dir)) {
//            System.out.println("hit self");
//            return true;
//        }

        // move snake.
        updateSnakePos(dir);

        //update current movement direction.
        currentMovmentDirection = dir;

        //check and consume food if main.consumables are present on new position
        Tile currHeadTile = getGridTileByPosition(getHeadPosition());
        if (currHeadTile.isFoodPresent()) {
            eat(currHeadTile, gp);
            spawnConsumable(randConsumablePos);
        }

        return false;
    }

    /**
     * spawn consumable on board by poping are pushing to the end the consumable again.
     */
    public void spawnConsumable(Position randConsumablePos) {
        Food food = availableFoods.pop();
        getGridTileByPosition(randConsumablePos).setFood(food);
        availableFoods.addLast(food);
    }

    public void spawnFruit(Position pos){
        Food fruit = new Fruit();
        if (!fruits.contains(pos)){
            fruits.add(pos);
            getGridTileByPosition(pos).setFood(fruit);
        }
    }


    /**
     * moves the snake one tile in the movement direction by removing tail and pushing new head.
     */
    public void updateSnakePos(Direction dir) {

        Position tail = snakeQueue.pollLast();
        assert tail != null;
        getGridTileByPosition(tail).setSnake(false);     // free tile from snake
        Position head = snakeQueue.getFirst();           // get head to compute next snake pos
        Position newHead = new Position(head.getX(), head.getY()); // copy
        newHead.moveInDirection(dir,tileSize);           // new position in movement direction
        snakeQueue.push(newHead);
        getGridTileByPosition(newHead).setSnake(true);
        System.out.println("snake moved to x:" + newHead.getX() + "y:"+ newHead.getY());
    }

    /**
     * consumes food and applies the effect of it.
     */
    public void eat(Tile tile, GameProperties properties) {
        if (tile.getFood().getType() == FoodType.FRUIT) {
            snake.grow();
            increaseBodySize();
            increaseScore(properties);
            return;
        }

        List<Effect> effects = tile.getFood().getEffects();
        for (Effect effect : effects) {
            properties.applyEffect(effect);
        }
        // deletes the food from tile.
        tile.setFood(null);
    }

    public void increaseScore(GameProperties properties) {
        score += properties.getScoreMultiplier() * 1;
    }

    /**
     * increases snakes body by one tile by creating a new position of tail in the opposite
     * direction to the current movement
     */
    public void increaseBodySize() {
        Direction dir = currentMovmentDirection.getOppesite();
        Position newTail = snakeQueue.getLast().getNextPosition(dir, tileSize);
        getGridTileByPosition(newTail).setSnake(true);
        snake.grow();
        snakeQueue.offerLast(newTail);
    }

    public int getScore() {
        return score;
    }

    Position getHeadPosition() {
        return snakeQueue.getFirst();
    }

    public Deque<Position> getsnakeQueue() {
        return snakeQueue;
    }

    /**
     * calculates tile on grid
     */
    Tile getGridTileByPosition(Position pos) {
        int col = (pos.getX() - tileSize / 2) / tileSize;
        int row = (pos.getY() - tileSize / 2) / tileSize;
        return grid[row][col];
    }

    /**
     * translates position of a pixel (x,y) to a (row,column) position on the grid useful for
     * checking bounds.
     */
    private Position translatePixelToGrid(Position pixelPos) {
        int col = (pixelPos.getX() - tileSize / 2) / tileSize;
        int row = (pixelPos.getY() - tileSize / 2) / tileSize;
        return new Position(row, col);
    }

    /**
     * translates pixel position to grid position (row*column)
     */
    private Position translateGridToPixel(Position cell) {
        return grid[cell.getY()][cell.getX()].getCenter();
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public void setSnake(Deque<Position> snake){
        snakeQueue = snake;
    }

    /**checks if food is present in the given position*/
    public boolean checkForFood(Position current){
        return getGridTileByPosition(current).isFoodPresent();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Set<Position> getFruits() {
        return fruits;
    }

    public void despawnFruit(Tile tile) {
        Food food = tile.getFood();
        fruits.remove(food);
    }
}


