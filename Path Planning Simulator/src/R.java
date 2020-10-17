import java.awt.Color;

public class R {
    // Colors
    public final static Color black= new Color(0, 0, 0);
    public final static Color white= new Color(255, 255, 255);
    public final static Color red= new Color(255, 0, 0);
    public final static Color darkgreen= new Color(150, 200, 75);
    public final static Color green= new Color(178, 255, 102);
    public final static Color blue= new Color(0, 128, 255);
    public final static Color darkblue= new Color(0, 100, 200);
    public final static Color lightyellow= new Color(255, 255, 224);
    public final static Color grey= new Color(125, 125, 125);

    // Sizes in Pixel
    /** Length of a side of the frame */
    public final static int Frame_Size= 1000;

    /** Length of a side of a tile */
    public final static int TILE_LENGTH= 10;

    /** Width and length of the robot */
    public final static int bot_width= 100;
    public final static int bot_length= 120;

    // Sizes in number of Tiles
    /** Length of a side of a square item */
    public final static int SQ_LENGTH= 10;

    // Robot parameters
    /** Change of angle in degrees when spin() is called */
    public final static int spin_angle= 5;

    /** Change of distance (in pixels) when move() is called */
    public final static int step= 5;

    /** The maximum distance (in pixels) that the ultrasonic sensor can scan */
    public final static int US_dis= 330;

    /** The maximum distance (in pixels) that the RFID sensor can scan */
    public final static int RFID_dis= 80;

    public final static int known_area_size= 200;

    public final static int detour_dis= 100;

}
