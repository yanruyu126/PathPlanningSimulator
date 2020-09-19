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

    public void backToOrigin() {

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

    public HashMap<Integer, Integer> scan(int range) {
        HashMap<Integer, Integer> data= new HashMap<>();
        ArrayList<Tile> tiles= tilesVisible();

        for (Tile tile : tiles) {
            int dis= calculateDistance(tile.x(), tile.y());
            int ang= angleFromBot(tile.x(), tile.y());
            int angDiff= Math.abs(degreeSubtraction(ang, angle));

            if (dis < R.US_dis && angDiff < range) {
                data.put(ang, dis);
                tile.scan();
                delay(20);
            }
        }
        updateKnownArea(range);
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

    public int angleFromBot(double x, double y) {
        double radians;
        try {
            radians= Math.atan((y - botY) / (x - botX));
        } catch (Exception e) {
            radians= Math.PI / 2;
        }

        int degrees= (int) Math.toDegrees(radians); // +-90
        if (y - botY < 0 && x - botX < 0) {
            degrees+= 180;
        } else if (y - botY > 0 && x - botX < 0) {
            degrees-= 180;
        }
        return degrees >= 0 ? degrees : 360 + degrees;
    }

    public void check() {
        for (Item item : pg.items) {
            if (calculateDistance(item.x(), item.y()) < R.RFID_dis) {
                item.check();
            }
        }
    }

    public int degreeSubtraction(int a, int b) {
        int c= a - b;
        if (c >= 0) return c < 180 ? c : c - 360;
        else return c > -180 ? c : c + 360;
    }

    private void updateKnownArea(int range) {
        for (int i= 0; i < R.known_area_size; i++ ) {
            for (int j= 0; j < R.known_area_size; j++ ) {
                int size= R.Frame_Size / R.known_area_size;
                int x= i * size;
                int y= j * size;
                int ang= angleFromBot(x, y);
                int angDiff= Math.abs(degreeSubtraction(ang, angle));

                if (calculateDistance(x, y) < R.US_dis && angDiff < range) {
                    knownArea[i][j]= 1;
                }
            }
        }
    }

}
