package model;

public class Board {

    /**
     * grid[row][col], row = y, col = x.
     */
    private final Tile[][] grid;
    private final int tileSize;
    private final int tileNum;
    private final int gridSize;


    /**
     * Sets up the board for the game.
     *
     * @param gridSize dimension of the game board in pixels.
     * @param tileSize size of each cell in pixels.
     */
    public Board(final int gridSize, final int tileSize) {
        //TODO: check if initPos < gridSize.

        //calculate number of tiles.
        this.tileSize = tileSize;
        final int tileNum = gridSize / tileSize;
        this.tileNum = tileNum;

        //initialize the grid.
        grid = new Tile[tileNum][tileNum];
        this.gridSize = gridSize;

        //fill grid with tiles.
        for (int row = 0; row < tileNum; row++) {
            for (int col = 0; col < tileNum; col++) {
                // i,j represent the upper left corner of the tile.
                grid[row][col] = new Tile(new Pixel(col * tileSize + (tileSize / 2),
                        row * tileSize + (tileSize / 2)));
            }
        }
    }


    /**
     * check if move results in Game End because of an out-of-bounds move.
     */
    public boolean checkOutOfBounds(Coords startCoord) {
        final int col = startCoord.getX();
        final int row = startCoord.getY();

        // check for board bounds.
        return (col >= tileNum || col < 0 || row >= tileNum || row < 0);
    }

    public Coords getNextFreePosition(Direction dir, Coords start) {
        Coords next = start.getNextCoord(dir);

        for (int i = 0; i < 3; i++) {
            if (!checkOutOfBounds(next)) {
                return next;
            }
            dir = (i % 2 == 0) ? dir.getClockwise() : dir.getAntiClockwise();
            next = start.getNextCoord(dir);
        }

        return null;
    }

    /**
     * calculates tile on grid by PIXEL Position
     *
     * @param pos IS THE PIXEL POSITION.
     */
    public Tile getGridTileByPosition(Coords pos) {
        int col = (pos.getX() - tileSize / 2) / tileSize;
        int row = (pos.getY() - tileSize / 2) / tileSize;
        return grid[row][col];
    }

    /**
     * translates position of a pixel (x,y) to a (row,column) position on the grid useful for
     * checking bounds.
     *
     * @return Position with y = row and x = column
     */
    private Coords translatePixelToCoord(Coords pixelPos) {
        int col = (pixelPos.getX() - tileSize / 2) / tileSize;
        int row = (pixelPos.getY() - tileSize / 2) / tileSize;
        return new Coords(col, row);
    }

    public Tile getTileByCoords(Coords coords) {
        return grid[coords.getY()][coords.getX()];
    }

    public Tile[][] getGrid() {
        return grid;
    }


    public int getTileSize() {
        return tileSize;
    }

    public void clearBoard() {
        for (Tile[] l : grid) {
            for (Tile t : l) {
                t.resetTile();
            }
        }
    }

}


