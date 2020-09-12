import java.util.ArrayList;

public class Item {
    private double centerX;
    private double centerY;
    private ArrayList<Tile> tiles;
    private boolean isTarget;

    Item(int x, int y, boolean isTarget) {
        centerX= x + R.SQ_LENGTH * R.TILE_LENGTH / 2;
        centerY= y + R.SQ_LENGTH * R.TILE_LENGTH / 2;
        this.isTarget= isTarget;
        tiles= new ArrayList<>();

        generateSquare(x, y);
    }

    private void generateSquare(int x, int y) {
        int tileX, tileY;
        for (int i= 0; i < R.SQ_LENGTH; i++ ) {
            tileX= x + i * R.TILE_LENGTH;
            if (i == 0 || i == R.SQ_LENGTH - 1) {
                for (int j= 0; j < R.SQ_LENGTH; j++ ) {
                    char side= i == 0 ? 'l' : 'r';
                    tileY= y + j * R.TILE_LENGTH;
                    generateTile(tileX, tileY, side);
                }
            } else {
                generateTile(tileX, y, 't');
                tileY= y + (R.SQ_LENGTH - 1) * R.TILE_LENGTH;
                generateTile(tileX, tileY, 'b');
            }
        }
    }

    private void generateTile(int x, int y, char side) {
        Tile myTile= new Tile(x, y, side);
        if (isTarget) myTile.setTarget();
        tiles.add(myTile);
    }

    public ArrayList<Tile> availableTiles(double botX, double botY) {
        ArrayList<Tile> availableTiles= new ArrayList<>();

        char side1= botX < centerX ? 'l' : 'r';
        char side2= botY < centerY ? 't' : 'b';

        for (Tile tile : tiles) {
            if (tile.side() == side1 || tile.side() == side2) {
                availableTiles.add(tile);
            }
        }
        return availableTiles;
    }

    public ArrayList<Tile> tiles() {
        return tiles;
    }
}
