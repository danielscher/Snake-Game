package main;

public class Tile {

    //center pixel position of tile - used for drawing graphical objects.
    private final Pixel center;
    private Boolean snake = false;
    private int foodCode = -1;


    public Tile(final Pixel center){
        this.center = center;
    }

    public Boolean isSnake() {
        return snake;
    }

    public void setSnake(Boolean snake) {
        this.snake = snake;
    }

    public int getFood() {
        return foodCode;
    }

    public Boolean isFoodPresent() {
        return foodCode!=-1;
    }

    public void setFood(int foodCode) {
        this.foodCode = foodCode;
    }

    public void removeFood(){
        foodCode=-1;
    }

    public Pixel getCenter() {
        return center;
    }

    public void resetTile(){
        snake = false;
        foodCode = -1;
    }

    public boolean isEmpty(){
        return (!isSnake()&&!isFoodPresent());
    }

    @Override
    public String toString() {
        return center + "food: " + foodCode + "snake: " + snake;
    }
}
