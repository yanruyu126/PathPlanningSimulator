import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/** A class for simulating Minibot with three methods of main categories:
 *
 * 1. Path Planning Algorithms: search for an object with a specific RFID tag. <br>
 * Locomotion, ultrasonic sensor, and RFID sensor methods are called.
 *
 * 2. Robot Methods: simulation of locomotion, ultrasonic sensor, and RFID <br>
 * sensor. See Minibot documentation and codes for more info. <br>
 * .. https://github.com/cornell-cup/MinibotHandoff <br>
 * .. *Check BottomArduinoSPI.ino for locomotion, and TopArduinoSPI.ino <br>
 * .. for sensors; check PiArduino.py to have an overview of the features <br>
 * .. from the top level.
 *
 * 3. Graphic Methods: Draw the robot and update information for the <br>
 * Playground as Minibot moves. */
public class Bot {
    /** Coordinates of the middle-front Minibot */
    int botX, botY;

    /** The angle that Minibot is facing in 0-360 degrees, with the <br>
     * right direction to be 0 degree. */
    int angle;

    /** The environment that Minibot is running in. */
    Playground pg;

    /** A 2D array of 0s and 1s which divides the playground area into
     * [R.known_area_size]*[R.known_area_size] pieces. 1 marks that the <br>
     * area has been scanned by the ultrasonic sensor, while 0 marks that <br>
     * the area has not been scanned yet. */
    int[][] knownArea;

    /** A list of Point obtained by the ultrasonic sensor, indicating <br>
     * the existence of an possible item. */
    ArrayList<Point> knownPoints;

    /** Whether the target item has been found. */
    boolean hasFound;

    /** The constructor initialize an instance of Minibot at the top-left <br>
     * corner of the Playground [pg] which faces the 0 degree angle. */
    public Bot(Playground pg) {
        botX= R.bot_length;
        botY= R.bot_width / 2;
        angle= 0;
        this.pg= pg;
        knownArea= new int[R.known_area_size][R.known_area_size];
        knownPoints= new ArrayList<>();
        hasFound= false;
    }

    // ------------ Path Planning Algorithm ------------- //

    /** The framework of the dynamic path planning algorithm. A cycle <br>
     * for searching is 1. scan the area within 180 degrees in front <br>
     * of the robot. 2. call updatePath(). Once the target item is found, <br>
     * it will stop looping and run backToOrigin(). */
    public void search() {
        while (!hasFound) {
            System.out.println("Ultrasonic Scanning");
            scan(135);
            updatePath();
        }
        delay(500);
        System.out.println("Picking the object and going back!");
        backToOrigin();
    }

    /** One cycle of the dynamic path planning algorithm. It runs <br>
     * according to the current knownPoints and knownArea.
     *
     * This method will end with three possible scenarios:
     *
     * 1. If there is some points in knownPoints (probably obtained by <br>
     * the scan() method run previous to it in search()), then go and <br>
     * check the nearest one with RFID sensor. <br>
     * .. a. If it is the item we want, "pick it up" and go back <br>
     * .. to the starting point (0, 0). <br>
     * .. b. If it is not the item we want, detour from it and move to the next cycle.
     *
     * 2. If there is nothing in knownPoints, drive in a direction that <br>
     * is least known to the Minibot for the distance of ultrasonic <br>
     * sensor's sensing range. (because we know that there will not <br>
     * be obstacles in between) */
    public void updatePath() {
        Point nearestP= findNearest(uncheckedPoints());
        if (nearestP != null) {
            System.out.println("Driving to nearest Point.");
            driveTo(nearestP);
            System.out.println("RFID Scanning");
            hasFound= RFID();
            if (!hasFound) {
                System.out.println("Not found, detouring");
                detourPoint(nearestP);
            } else System.out.println("Found!");
        } else {
            System.out.println("No point, searching");
            turnTo(leastKnownDirection());
            forward(R.US_dis, true);
        }
    }

    /** Dynamic heuristic for the "shortest path" back to the origin point. */
    public void backToOrigin() {
        int disFromOrigin= calculateDistance(0, 0);
        while (disFromOrigin > 50) {
            faceOrigin();
            Point nearestP= scanFront();
            if (nearestP != null) {
                driveTo(nearestP);
                detourBack();
            } else {
                int forwardDis= Math.min(R.US_dis - R.detour_dis, disFromOrigin);
                forward(forwardDis, true);
            }
            disFromOrigin= calculateDistance(0, 0);
        }
    }

    private Point findNearest(ArrayList<Point> data) {
        int dis;
        int minDis= R.Frame_Size;
        Point result= null;
        for (Point p : data) {
            dis= calculateDistance(p.x, p.y);
            if (dis < minDis) {
                minDis= dis;
                result= p;
            }
        }
        return result;
    }

    public void faceOrigin() {
        int ang= angleFromBot(0, 0);
        turnTo(ang);
    }

    public void detourBack() {
        delay(500);
        int ang= angleFromBot(0, 0);
        turnTo((ang + 90) % 360);
        forward(R.detour_dis, true);
    }

    public void detourPoint(Point p) {
        System.out.println("Detour, backing");
        backward(R.detour_dis, true);
        // int ang= angleFromBot(p.x, p.y);
        System.out.println("Detour, turning");
        turnTo(directionAwayFrom(p));
        System.out.println("Detour, forwarding");
        forward(R.bot_length, true);
    }

    private ArrayList<Point> uncheckedPoints() {
        ArrayList<Point> result= new ArrayList<>();
        for (Point p : knownPoints) {
            if (p.isChecked == false) {
                result.add(p);
//                System.out.println("x: " + p.x);
//                System.out.println("y: " + p.y);
//                System.out.println("");
            }
        }
        delay(1000);

        return result;
    }

    public void driveTo(Point p) {
        turnTo(angleFromBot(p.x, p.y));
        forward(calculateDistance(p.x, p.y) - R.RFID_dis / 4, true);
    }

    private int leastKnownDirection() {
        // TODO: To search the most unfamiliar area.

        Point nearestP= findNearest(knownPoints);
        int new_dir;
        if (nearestP != null) {
            new_dir= directionAwayFrom(nearestP);
        } else {
            if (botX < R.Frame_Size / 2) new_dir= 0;
            else if (Math.abs(angle - 90) < 10) new_dir= 180;
            else new_dir= 90;
        }
        return new_dir;
    }

    private int directionAwayFrom(Point p) {
        int np_angle= angleFromBot(p.x, p.y);
        int new_dir= (np_angle + 90) % 360;
        if (new_dir < 315 && new_dir > 135) new_dir= (new_dir + 180) % 360;
        return new_dir;
    }

    /** Prevent the Minibot from running off the edge when the reflectance sensor detects the edge.
     * Will get called when readReflectance() returns true. */
    private void turnBackFromEdge() {
        // TODO: Implement this method with locomotion methods so that the
        // Minibot stays in the area marked by the black tape.
        // Feel free to modify forward() and move() to incorporate this feature.
        System.out.println("Detouring from edge.");
        int next_angle= degreeSubtraction(angle, -90);
        delay(500);
        backward(100, false);
        delay(500);
        turnTo(next_angle);
    }

    // ------------ Robot Methods ------------- //
    public void turnTo(int degree) {
        int diff= degreeSubtraction(degree, angle);
        boolean isLeft= diff < 0;
        degree= (degree + 360) % 360;

//        System.out.println("Turning from: " + angle);
//        System.out.println("Turning to: " + degree);
        while (Math.abs(angle - degree) > 10) {
//            System.out.println("angle: " + angle);
//            System.out.println("degree: " + degree);
            spin(isLeft);
        }

    }

    public void spin(boolean isLeft) {
        double radians= Math.toRadians(angle);
        double botX_center= botX - R.bot_length / 2 * Math.cos(radians);
        double botY_center= botY - R.bot_length / 2 * Math.sin(radians);

        angle= (angle + R.spin_angle * (isLeft ? -1 : 1) + 360) % 360;

        double new_radians= Math.toRadians(angle);
        botX= (int) (botX_center + R.bot_length / 2 * Math.cos(new_radians));
        botY= (int) (botY_center + R.bot_length / 2 * Math.sin(new_radians));
        delay(20);
    }

    public void forward(int distance, boolean edgeDetectionOn) {
        boolean isForward= distance > 0;
        double radians= Math.toRadians(angle);
        int targetX= botX + (int) (Math.cos(radians) * distance);
        int targetY= botY + (int) (Math.sin(radians) * distance);

        while (Math.abs(calculateDistance(targetX, targetY)) > 50) {
//            System.out.println(Math.abs(calculateDistance(targetX, targetY)));
            if (readReflectance() && edgeDetectionOn) {
                turnBackFromEdge();
                break;
            } else {
                move(radians, isForward);
            }
        }
    }

    public void backward(int distance, boolean edgeDetectionOn) {
        forward(-distance, edgeDetectionOn);
    }

    private void move(double radians, boolean isForward) {
        int dir= isForward ? 1 : -1;
        botX+= Math.cos(radians) * R.step * dir;
        botY+= Math.sin(radians) * R.step * dir;
        delay(20);

    }

    public void scan(int range) {
        int end_angle= degreeSubtraction(angle, -range);
        end_angle= (end_angle + 360) % 360;

//        System.out.println("end_angle: " + end_angle);
        while (Math.abs(angle - end_angle) > 10) {
            int new_angle= degreeSubtraction(angle, -R.scan_step);
            turnTo(new_angle);
            scanFront();
        }
        checkNearby();
    }

    private Point scanFront() {
        ArrayList<Tile> tiles= tilesVisible();
        Point myPoint= null;
        for (Tile tile : tiles) {
            int dis= calculateDistance(tile.x(), tile.y());
            int ang= angleFromBot(tile.x(), tile.y());
            int angDiff= Math.abs(degreeSubtraction(ang, angle));

            if (dis < R.US_dis && angDiff < 2) {
                myPoint= new Point(tile.x(), tile.y());
                knownPoints.add(myPoint);
                tile.scan();
                break;
            }
        }
        updateKnownArea();
        delay(50);
        return myPoint;
    }

    public boolean RFID() {
        for (Point p : knownPoints) {
            if (calculateDistance(p.x, p.y) < R.RFID_dis) {
                p.check();
            }
        }
        checkNearby();
        delay(500);
        return check();
    }

    /** Returns true the reflectance sensor is placed over a black type. */
    public boolean readReflectance() {
        return botX < 0 || botX > R.Frame_Size || botY < 0 || botY > R.Frame_Size;
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

        double radians= Math.toRadians(angle);
        int botX_center= (int) (botX - R.bot_length / 2 * Math.cos(radians));
        int botY_center= (int) (botY - R.bot_length / 2 * Math.sin(radians));

        AffineTransform tx= new AffineTransform();
        tx.rotate(Math.toRadians(angle), botX_center, botY_center);

        Rectangle rec= new Rectangle(botX_center - R.bot_length / 2,
            botY_center - R.bot_width / 2, R.bot_length, R.bot_width);
        Shape rotatedRec= tx.createTransformedShape(rec);

        Rectangle head= new Rectangle(botX_center + R.bot_length / 2,
            botY_center - R.bot_width / 2, 10, R.bot_width);
        Shape rotatedHead= tx.createTransformedShape(head);

        g.setColor(R.grey);
        g.fill(rotatedRec);

        g.setColor(R.black);
        g.fill(rotatedHead);

        g.setColor(R.red);
        g.fillOval(botX, botY, 5, 5);
    }

    public void drawPoints(Graphics2D g) {
        for (Point p : knownPoints) {
            Color c= p.isChecked ? R.darkgreen : R.darkblue;
            g.setColor(c);
            g.fillOval(p.x, p.y, 5, 5);
        }
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
        return calculateDistance(x, y, botX, botY);
    }

    public int calculateDistance(double x1, double y1, double x2, double y2) {
        double dis= Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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

    public boolean check() {
        boolean result= false;
        for (Item item : pg.items) {
            int dis= R.RFID_dis + R.SQ_LENGTH * R.TILE_LENGTH + R.bot_length;
            // double disa= calculateDistance(item.x(), item.y());
            // System.out.println("distance: " + disa);

            if (calculateDistance(item.x(), item.y()) < dis) {
                item.check();
                if (item.isTarget()) result= true;
            }
        }
        return result;
    }

    public int degreeSubtraction(int a, int b) {
        int c= a - b;
        if (c >= 0) return c < 180 ? c : c - 360;
        else return c > -180 ? c : c + 360;
    }

    private void updateKnownArea() {
        int range= 2;
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

    private void checkNearby() {
        for (Point p1 : knownPoints) {
            // System.out.println("Running checkNearby " + p1.isChecked);
            if (p1.isChecked) {
                // System.out.println("Checked!!!");
                for (Point p2 : knownPoints) {
                    int dis= calculateDistance(p1.x, p1.y, p2.x, p2.y);
                    if (Math.abs(dis) < R.SQ_LENGTH * R.TILE_LENGTH) {
                        p2.check();

                    }
                }
            }
        }
    }

    private class Point {
        int x;
        int y;
        boolean isChecked;

        Point(int x, int y) {
            this.x= x;
            this.y= y;
            isChecked= false;
        }

        public void check() {
            isChecked= true;
        }
    }

}
