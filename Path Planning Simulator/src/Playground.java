import java.awt.BasicStroke;
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
        Item item1= new Item(200, 400, false);
//        Item item2= new Item(100, 400, false);
        Item item3= new Item(550, 650, true);
        Item item4= new Item(700, 200, false);
        items.add(item1);
//        items.add(item2);
        items.add(item3);
        items.add(item4);
        tiles.addAll(item1.tiles());
//        tiles.addAll(item2.tiles());
        tiles.addAll(item3.tiles());
        tiles.addAll(item4.tiles());

    }

    public void draw(Graphics2D g) {
        for (Tile tile : tiles) {
            g.setColor(tile.color());
            g.fillRect(tile.x(), tile.y(), R.TILE_LENGTH, R.TILE_LENGTH);
        }

        float thickness= 15;
        BasicStroke newStroke= new BasicStroke(thickness);
        g.setStroke(newStroke);
        g.setColor(R.black);
        g.drawRect(0, 0, R.Frame_Size, R.Frame_Size);
    }
}
