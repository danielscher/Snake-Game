package main;

import main.consumables.Food;

public class Tile {

    private Position center;
    private Boolean snake = false;
    private Food food = null;
    private Boolean isFoodPresent = false;

    public Tile(Position center){
        this.center = center;
    }

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
        this.isFoodPresent = food != null;
    }

    public Position getCenter() {
        return center;
    }

    public void resetTile(){
        snake = false;
        food = null;
        isFoodPresent = false;
    }
}
