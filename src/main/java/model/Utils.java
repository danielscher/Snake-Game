package model;

public class Utils {

    public static Coords translatePixelToCoords(Pixel pixel, final int tileSize){
        int col = (pixel.getX() - tileSize / 2) / tileSize;
        int row = (pixel.getY() - tileSize / 2) / tileSize;
        return new Coords(col,row);
    }

    public static Pixel translateCoordToPixel(Coords coords, final int tileSize){
        int xPos = coords.getX() * tileSize + tileSize/2;
        int yPos = coords.getY() * tileSize + tileSize/2;
        return new Pixel(xPos,yPos);
    }
}
