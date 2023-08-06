package main;

public class Pixel {

    private final int x;
    private final int y;

    public Pixel(final int xPos,final int yPos){
        x = xPos;
        y = yPos;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
