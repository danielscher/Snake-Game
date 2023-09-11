package model;

public class Coords {

    private int xPos;
    private int yPos;

    public Coords(final int xPos, final int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }


    /**sets the current position to the position of the next one in the movement direction*/
    public void moveInDirection(Direction dir, int step) {
        step = 1;
        switch (dir) {
            case UP -> yPos -= step;
            case DOWN -> yPos +=  step;
            case LEFT -> xPos -= step;
            case RIGHT -> xPos += step;
        }
    }

    /**
     * @return a new Position object which represents the next position on the grid in the movement
     * direction
     * @param dir movement direction.
     */
    public Coords getNextCoord(Direction dir) {
        int xPos = this.xPos;
        int yPos = this.yPos;

        switch (dir) {
            case UP -> {
                return new Coords(xPos, yPos-1);
            }
            case DOWN -> {
                return new Coords(xPos, yPos+1);
            }
            case LEFT -> {
                return new Coords(xPos-1, yPos);
            }
            case RIGHT -> {
                return new Coords(xPos+1, yPos);
            }
        }
        return null;
    }

    @Override
    public int hashCode(){
        return xPos*1000+yPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coords coords = (Coords) o;
        return xPos == coords.xPos && yPos == coords.yPos;
    }

    @Override
    public String toString(){
        return "X:" + xPos + " Y:" + yPos ;
    }
}
