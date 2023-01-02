public class SnakePart {

    private Position pos;
    private Boolean head;

    public SnakePart(Position position) {
        this.pos = position;
    }

    public SnakePart(Position position, Boolean head) {
        this.pos = position;
        this.head = true;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public boolean isHead() {
        return head;
    }


}
