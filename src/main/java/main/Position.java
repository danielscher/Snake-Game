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
            case UP -> yCoord++;
            case DOWN -> yCoord--;
            case LEFT -> xCoord--;
            case RIGHT -> xCoord++;
        }
        ;
    }

    public Position getNextPosition(Direction dir) {

        switch (dir) {
            case UP -> new Position(this.xCoord, this.yCoord++);
            case DOWN -> new Position(this.xCoord, this.yCoord--);
            case LEFT -> new Position(this.xCoord--, this.yCoord);
            case RIGHT -> new Position(this.xCoord++, this.yCoord);
        }
        return this;
    }


}
