import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Bot {

    int botX;
    int botY;
    int angle;
    Playground pg;
    int[][] knownArea;

    public Bot(Playground pg) {
        botX= R.bot_length / 2;
        botY= R.bot_width / 2;
        angle= 0;
        this.pg= pg;
        knownArea= new int[R.known_area_size][R.known_area_size];
    }

    // ------------ Path Planning Algorithm ------------- //
    public void search() {
        // Pseudo code

//        while (not 90% scanned or didFound) {
//            scan();
//            updatePath();
//        }

    }

    /** Path planning algorithm */
    public void updatePath() {
        // Pseudo code

//        if knowItem {
//            item = getClosestItem();
//            goTo(item);
//            check(item);
//            if (found) grab and go;
//            else driveToBack(item);
//        } else {
//            pick a random direction and go
//        }
    }

    // ------------ Robot Methods ------------- //
    public void turnTo(int degree) {

        while (Math.abs(angle - degree) > 5) {
            spin();
        }

    }

    public void spin() {
        angle= angle + R.spin_angle;
        if (angle > 360) angle-= 360;
        delay(200);

    }

    public void forward(int distance) {
        double radians= Math.toRadians(angle);
        int targetX= botX + (int) (Math.cos(radians) * distance);
        while (Math.abs(botX - targetX) > 10) {
            move(radians);
        }

    }

    public void move(double radians) {
        botX+= Math.cos(radians) * R.step;
        botY+= Math.sin(radians) * R.step;
        delay(20);
    }

    public HashMap<Integer, Integer> scan() {
        HashMap<Integer, Integer> data= new HashMap<>();
        ArrayList<Tile> tiles= tilesVisible();

        for (Tile tile : tiles) {
            int dis= calculateDistance(tile.x(), tile.y());
            int ang= tileAngle(tile);

            if (dis < R.US_dis) {
                try {
                    dis= Math.min(data.get(ang), dis);
                } catch (Exception e) {}
                data.put(ang, dis);
                tile.scan();
                delay(20);
            }
        }
        updateKnownArea();
        return data;
    }

    // ------------ Graphic Methods ------------- //
    public void drawKnownArea(Graphics2D g) {
        g.setColor(R.lightyellow);
        for (int i= 0; i < R.known_area_size; i++ ) {
            for (int j= 0; j < R.known_area_size; j++ ) {
                if (knownArea[i][j] == 1) {
                    int size= R.Frame_Size / R.known_area_size;
                    int x= i * size;
                    int y= j * size;
                    g.fillRect(x, y, size, size);
                }
            }
        }
    }

    public void drawBot(Graphics2D g) {

        AffineTransform tx= new AffineTransform();
        tx.rotate(Math.toRadians(angle), botX, botY);

        Rectangle rec= new Rectangle(botX - R.bot_length / 2, botY - R.bot_width / 2, R.bot_length,
            R.bot_width);
        Shape rotatedRec= tx.createTransformedShape(rec);

        g.setColor(R.grey);
        g.fill(rotatedRec);

        g.setColor(R.red);
        g.fillOval(botX, botY, 3, 3);
    }

    // ------------ Helper Methods ------------- //

    public void delay(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Tile> tilesVisible() {
        ArrayList<Tile> tilesVisible= new ArrayList<>();
        for (Item item : pg.items) {
            tilesVisible.addAll(item.availableTiles(botX, botY));
        }
        return tilesVisible;
    }

    public int calculateDistance(double x, double y) {
        double dis= Math.sqrt(Math.pow(x - botX, 2) + Math.pow(y - botY, 2));
        return (int) dis;
    }

    public int tileAngle(Tile tile) {
        double radians;
        try {
            radians= Math.atan((tile.x() - botX) / (tile.y() - botY));
        } catch (Exception e) {
            radians= 0;
        }
        return (int) Math.toDegrees(radians);
    }

    public void check() {
        for (Item item : pg.items) {
            if (calculateDistance(item.x(), item.y()) < R.RFID_dis) {
                item.check();
            }
        }
    }

    private void updateKnownArea() {
        for (int i= 0; i < R.known_area_size; i++ ) {
            for (int j= 0; j < R.known_area_size; j++ ) {
                int size= R.Frame_Size / R.known_area_size;
                int x= i * size;
                int y= j * size;

                if (calculateDistance(x, y) < R.US_dis) {
                    knownArea[i][j]= 1;
                    System.out.println(x);
                    System.out.println(y);
                    System.out.println("");
                }
            }
        }
    }

}
