import java.awt.Color;

public class Tile {

    private boolean hasTarget, isChecked, isScanned;
    private Color color;
    private int x, y;
    private char side;

    public Tile(int x, int y, char side) {
        isChecked= false;
        isScanned= false;
        hasTarget= false;
        color= R.black;
        this.x= x;
        this.y= y;
        this.side= side;
    }

    public void check() {
        isChecked= true;
        color= R.green;
    }

    public void scan() {
        isScanned= true;
        color= R.blue;
    }

    public void setTarget() {
        hasTarget= true;
        color= R.red;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isScanned() {
        return isScanned;
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

    public char side() {
        return side;
    }

}
