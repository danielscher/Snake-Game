import consumables.Food;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private Tile[][] grid;

    private Direction currentMovmentDirection;

    private final int gridSize;

    private int score;

    private Snake snake;
    private List<Position> snakeBody = new ArrayList<>();
    private List<Food> currFoods = new ArrayList<Food>();


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
        final Position nextHeadPos = currHeadPos.getNextPosition(dir);
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
    public boolean moveSnake(Direction dir) {

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
        return false;
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

    private void eat(Position pos) {
        getGridTileByPosition(pos).getFood().apply();
    }

    public int getScore() {
        return score;
    }

    private Tile getGridTileByPosition(Position pos) {
        int xPos = pos.getxCoord();
        int yPos = pos.getyCoord();
        return grid[xPos][yPos];
    }
}


