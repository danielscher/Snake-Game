package main;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction getOppesite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
