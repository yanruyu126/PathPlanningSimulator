import java.awt.Graphics2D;
import java.util.ArrayList;

public class Playground {

    ArrayList<Tile> tiles;
    ArrayList<Item> items;

    public Playground() {
        tiles= new ArrayList<>();
        items= new ArrayList<>();
        generateItems();

    }

    public void generateItems() {
        Item item1= new Item(500, 100, true);
        Item item2= new Item(100, 300, false);
        Item item3= new Item(400, 600, false);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        tiles.addAll(item1.tiles());
        tiles.addAll(item2.tiles());
        tiles.addAll(item3.tiles());

    }

    public void draw(Graphics2D g) {
        for (Tile tile : tiles) {
            g.setColor(tile.color());
            g.fillRect(tile.x(), tile.y(), R.TILE_LENGTH, R.TILE_LENGTH);
        }
    }
}
