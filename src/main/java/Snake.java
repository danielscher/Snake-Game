import java.util.LinkedList;

public class Snake {

    private int length;

    public Snake() {
        length = 2;
    }

    /**
     * increase the size of the snake by putting a new body part opposite to the movement direction
     */
    public void grow() {
        length++;
    }

    public int getLength() {
        return length;
    }

}
