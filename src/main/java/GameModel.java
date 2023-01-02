import consumables.Food;
import java.util.List;
import java.util.Random;

public class GameModel {

    private List<Food> availableConsumables;
    private Board board;
    private Random rand;

    public GameModel(long seed, Random random, int boardSize, List<Food> foods) {
        availableConsumables = foods;
        int randX = rand.nextInt(boardSize);
        int randY = rand.nextInt(boardSize);
        board = new Board(new Snake(), boardSize, new Position(randX, randY));
    }

    public boolean move(Direction dir) {
        return false;
    }

}
