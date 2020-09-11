import java.awt.Color;

public class Tile {

    private boolean hasItem, hasTarget, isRead, isScanned;
    private Color color;
    private int x, y;

    public Tile(int x, int y) {
        isRead= false;
        isScanned= false;
        hasTarget= false;
        hasItem= false;
        color= R.white;
        this.x= x;
        this.y= y;
    }

    public void read() {
        isRead= true;
        color= R.green;
    }

    public void scan() {
        color= R.lightblue;
        if (hasItem) {
            isScanned= true;
            color= R.blue;
        }
    }

    public void setTarget() {
        hasTarget= true;
        hasItem= true;
        color= R.red;
    }

    public void setItem() {
        hasItem= true;
        color= R.black;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isScanned() {
        return isScanned;
    }

    public boolean hasItem() {
        return hasItem;
    }

    public boolean hasTarget() {
        return hasTarget;
    }

    public Color color() {
        return color;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

}
