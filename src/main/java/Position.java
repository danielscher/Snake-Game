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

    public void moveInDirection(Direction dir){
        switch (dir) {
                case UP -> yCoord++;
                case DOWN -> yCoord--;
                case LEFT -> xCoord--;
                case RIGHT -> xCoord++;
            };
    }

}
