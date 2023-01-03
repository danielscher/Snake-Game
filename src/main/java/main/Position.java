package main;

public class Position {

    private int xCoord;
    private int yCoord;

    public Position(final int xPos, final int yPos) {
        this.xCoord = xPos;
        this.yCoord = yPos;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void moveInDirection(Direction dir) {
        switch (dir) {
            case UP -> yCoord--;
            case DOWN -> yCoord++;
            case LEFT -> xCoord--;
            case RIGHT -> xCoord++;
        }
        ;
    }

    public Position getNextPosition(Direction dir) {
        int xPos = this.xCoord;
        int yPos = this.yCoord;

        switch (dir) {
            case UP -> {
                return new Position(xPos, --yPos);
            }
            case DOWN -> {
                return new Position(xPos, ++yPos);
            }
            case LEFT -> {
                return new Position(--xPos, yPos);
            }
            case RIGHT -> {
                return new Position(++xPos, yPos);
            }
        }
        return this;
    }


}
