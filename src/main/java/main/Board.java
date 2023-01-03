package main;

import java.util.Deque;
import main.consumables.Effect;
import main.consumables.Food;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import main.consumables.FoodType;

public class Board {

    private Tile[][] grid;
    private int numCurrentConsumables;
    private Direction currentMovmentDirection;
    private final int gridSize;
    private int score;
    private Snake snake;
    private List<Position> snakeBody = new ArrayList<>();
    private Deque<Food> availableFoods = new ArrayDeque<>();


    /**
     * constructs the board for the game.
     */
    public Board(final Snake snake, final int gridSize, final Position initialPosition) {
        //initialize the grid.
        grid = new Tile[gridSize][gridSize];
        this.gridSize = gridSize;

        //fill grid with tiles.
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new Tile();
            }
        }

        //set snake head position
        grid[initialPosition.getxCoord()][initialPosition.getyCoord()].setSnake(true);
        snakeBody.add(initialPosition);

        this.snake = snake;
        currentMovmentDirection = Direction.UP;
    }

    /**
     * returns true if snake is located on this position.
     */
    private boolean checkIfSnake(Position pos) {
        return grid[pos.getxCoord()][pos.getyCoord()].isSnake();
    }


    /**
     * check if move results in Game End because of an out-of-bounds move.
     */
    public boolean checkMoveOutOfBounds(final Direction dir) {
        final Position currHeadPos = snakeBody.get(0);
        final int currX = currHeadPos.getxCoord();
        final int currY = currHeadPos.getyCoord();

        // check for board bounds.
        switch (dir) {
            case UP -> {
                return (currY == 0);
            }
            case DOWN -> {
                return (currY == gridSize - 1);
            }
            case LEFT -> {
                return (currX == 0);
            }
            case RIGHT -> {
                return (currX == gridSize - 1);
            }
        }
        return false;
    }

    /**
     * checks if snake bites/eats itself on the next move
     */
    private boolean checkEatsSelf(final Direction dir) {
        final Position currHeadPos = snakeBody.get(0);
        final Position nextHeadPos = currHeadPos.getNextPosition(dir);
        return grid[nextHeadPos.getxCoord()][nextHeadPos.getyCoord()].isSnake();

    }

    /**
     * moves snake and returns true if game ended
     */
    public boolean handle(Direction dir, GameProperties gp, Position randConsumablePos) {

        // if trying to move in the opposite direction continue moving the current direction.
        if (dir.getOppesite() == currentMovmentDirection) {
            dir = currentMovmentDirection;
        }

        // game ends if next move is out of bounds or eats self.
        if (checkMoveOutOfBounds(dir) || checkEatsSelf(dir)) {
            return true;
        }

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

    /**spawn consumable on board by poping are pushing to the end the consumable again.*/
    private void spawnConsumable(Position randConsumablePos) {
        Food food = availableFoods.pop();
        getGridTileByPosition(randConsumablePos).setFood(food);
        availableFoods.addLast(food);
    }


    /**
     * moves the head 1 unit of length in the direction given by setting each successor of the
     * snakes tiles to the predecessor position
     */
    private void updateSnakePos(Direction dir) {
        Position previousPos = snakeBody.get(0);    //get head position
        snakeBody.get(0).moveInDirection(dir); // set head to new position

        for (int i = 1; i < snake.getLength() - 1; i++) {
            Position temp = snakeBody.get(i);
            snakeBody.set(i, previousPos);
            previousPos = temp;
        }
    }

    /**
     * consumes food and applies the effect of it.
     */
    private void eat(Tile tile, GameProperties properties) {
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

    private void increaseScore(GameProperties properties) {
        score += properties.getScoreMultiplier() * 1;
    }

    /**
     * increases snakes body by one tile by creating a new position of tail in the opposite
     * direction to the current movement
     */
    private void increaseBodySize() {
        Direction dir = currentMovmentDirection.getOppesite();
        Position newTail = snakeBody.get(snake.getLength() - 1);
        newTail.getNextPosition(dir);
        snakeBody.add(newTail);
    }

    public int getScore() {
        return score;
    }

    private Position getHeadPosition() {
        return snakeBody.get(0);
    }

    public List<Position> getSnakeBody() {
        return snakeBody;
    }

    private Tile getGridTileByPosition(Position pos) {
        int xPos = pos.getxCoord();
        int yPos = pos.getyCoord();
        return grid[xPos][yPos];
    }
}


