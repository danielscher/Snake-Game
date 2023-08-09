package main;

import java.util.Random;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public Direction getClockwise(){
        return switch (this){
            case UP -> RIGHT;
            case DOWN -> LEFT;
            case LEFT -> UP;
            case RIGHT -> DOWN;
        };
    }

    public Direction getAntiClockwise(){
        return switch (this){
            case UP -> LEFT;
            case DOWN -> RIGHT;
            case LEFT -> DOWN;
            case RIGHT -> UP;
        };
    }

    public static Direction getRandomDirection(Random random){
        return Direction.class.getEnumConstants()[random.nextInt(
                Direction.class.getEnumConstants().length - 1)];
    }

}
