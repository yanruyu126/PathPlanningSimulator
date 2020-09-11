import java.awt.Color;
import java.awt.Graphics2D;

public class Playground {

    Tile[][] ground;

    public Playground() {
        ground= new Tile[R.PG_WIDTH][R.PG_HEIGHT];
        generateTiles();
        generateRandomObjects();

    }

    public void generateRandomObjects() {
        generateSquare(R.PG_WIDTH / 2, R.PG_HEIGHT / 2, true);
        generateSquare(200, 300, false);
        generateSquare(R.PG_WIDTH / 3 * 2, R.PG_HEIGHT / 6, false);

    }

    public void generateTiles() {
        for (int i= 0; i < R.PG_WIDTH; i++ ) {
            for (int j= 0; j < R.PG_WIDTH; j++ ) {
                ground[i][j]= new Tile(i * R.TILE_WIDTH, j * R.TILE_HEIGHT);
            }
        }
    }

    public void generateSquare(int x, int y, boolean isTarget) {
        Color color= isTarget ? R.red : R.black;
        int leftBound= x;
        int rightBound= Math.min(x + R.SQ_LENGTH, R.PG_WIDTH);
        int lowerBound= y;
        int upperBound= Math.min(y + R.SQ_LENGTH, R.PG_HEIGHT);

        for (int i= leftBound; i < rightBound; i++ ) {
            if (i == leftBound || i == rightBound - 1) {
                for (int j= lowerBound; j < upperBound; j++ ) {
                    fillTile(i, j, isTarget);
                }
            } else {
                fillTile(i, lowerBound, isTarget);
                fillTile(i, upperBound - 1, isTarget);
            }
        }
    }

    private void fillTile(int i, int j, boolean isTarget) {
        Tile myTile= ground[i][j];
        if (isTarget) myTile.setTarget();
        else myTile.setItem();
    }

    public void draw(Graphics2D g) {
        for (int i= 0; i < R.PG_WIDTH; i++ ) {
            for (int j= 0; j < R.PG_WIDTH; j++ ) {
                Tile tile= ground[i][j];
                g.setColor(tile.color());
                g.fillRect(tile.x(), tile.y(), R.TILE_WIDTH, R.TILE_HEIGHT);
                // TODO: draw lines
            }
        }
    }
}
