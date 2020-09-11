import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Bot {

    int botX;
    int botY;
    int angle;
    Playground pg;

    public Bot(Playground pg) {
        botX= 0;
        botY= 0;
        angle= 0;
        this.pg= pg;
    }

    public void drawBot(Graphics2D g) {

        AffineTransform tx= new AffineTransform();
        tx.rotate(Math.toRadians(angle), botX + R.bot_length / 2, botY + R.bot_width / 2);

        Rectangle rec= new Rectangle(botX, botY, R.bot_length, R.bot_width);
        Shape rotatedRec= tx.createTransformedShape(rec);

        g.setColor(R.grey);
        g.fill(rotatedRec);
    }

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

    public void delay(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, Integer> scan() {
        HashMap<Integer, Integer> data= new HashMap<>();
        for (int i= 0; i < R.PG_WIDTH; i++ ) {
            for (int j= 0; j < R.PG_WIDTH; j++ ) {
                Tile tile= pg.ground[i][j];
                int dis= tileDistance(tile);
                int ang= tileAngle(tile);

                if (dis < R.scan_distance) {
                    if (tile.hasItem()) {
                        try {
                            dis= Math.min(data.get(ang), dis);
                        } catch (Exception e) {}
                        data.put(ang, dis);
                        tile.scan();
                        delay(20);
                    }
                }
            }
        }
        return data;
    }

    public int tileDistance(Tile tile) {
        double dis= Math.sqrt(Math.pow(tile.x() - botX, 2) + Math.pow(tile.y() - botY, 2));
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

}
