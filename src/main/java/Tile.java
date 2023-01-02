import consumables.Food;

public class Tile {

    private Boolean snake;
    private Food food;
    private Boolean isFoodPresent;

    public Boolean isSnake() {
        return snake;
    }

    public void setSnake(Boolean snake) {
        this.snake = snake;
    }

    public Food getFood() {
        return food;
    }

    public Boolean isFoodPresent() {
        return isFoodPresent;
    }

    public void setFood(Food food) {
        this.food = food;
    }

}
