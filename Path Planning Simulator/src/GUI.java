import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.Timer;

public class GUI extends JFrame {
    private static final long serialVersionUID= 1L;
    static Canvas canvas;

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(R.Frame_Size, R.Frame_Size);
        setResizable(false);
        setTitle("Test");

        init();
    }

    public void init() {
        setLocationRelativeTo(null);

        setLayout(new GridLayout(1, 1, 0, 0));

        canvas= new Canvas();
        add(canvas);

        setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
        Timer t= new Timer(100, canvas);
        t.start();

        Bot myBot= canvas.bot;

        myBot.forward(400);
        myBot.turnTo(90);
        myBot.forward(600);
        myBot.backToOrigin();

        // myBot.search();

//        myBot.scan(10);
//        myBot.forward(300);
//        myBot.turnTo(60);
//        myBot.forward(200);
//        myBot.scan(145);
//        myBot.turnTo(100);
//        myBot.forward(800);
//        myBot.backToOrigin();
    }
}
