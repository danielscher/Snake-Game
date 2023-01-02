import java.util.LinkedList;

public class Snake {

    private LinkedList<SnakePart> body;
    private Direction movementDirection;
    private int length;

    public Snake(Position position) {
        length = 1;
        body = new LinkedList<>();
        body.add(new SnakePart(position, true)); //random position
        movementDirection = Direction.UP;
    }

    /**
     * increase the size of the snake by putting a new body part opposite to the movement direction
     */
    public void grow() {
        Position lastPos = body.getLast().getPos();
        Position newPos = new Position(lastPos.getxCoord(), lastPos.getyCoord());
        newPos.moveInDirection(movementDirection.getOppesite());
        SnakePart newTail = new SnakePart(newPos);
        length++;
    }


    /**
     * moves snake in the desired valid direction
     */
    public void move(Direction direction) {

        // if already moving in that direction or the opposite direction do nothing.
        if (direction == movementDirection || direction.getOppesite() == movementDirection) {
            return;
        }

        Position previousPos = body.getFirst().getPos();    //get head position
        body.getFirst().getPos().moveInDirection(direction); // set head to new position

        // iterate over body parts and set them to their predecessors position.
        for (SnakePart part : body) {
            if (!part.isHead()) {
                part.setPos(previousPos);
                previousPos = part.getPos();
            }
        }
    }

    public int getLength() {
        return length;
    }

}
