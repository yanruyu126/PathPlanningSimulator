import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Canvas extends JPanel implements ActionListener {
    private static final long serialVersionUID= 1L;
    Bot bot;
    Playground pg;
    JButton addItemButton, addTargetButton, startButton;
    int INIT= 0;
    int RUNNING= 1;
    int state;

    public Canvas() {
        pg= new Playground();
        bot= new Bot(pg);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d= (Graphics2D) g;
        bot.drawKnownArea(g2d);
        pg.draw(g2d);
        bot.drawBot(g2d);
        bot.drawPoints(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == INIT) {

        }
        repaint();
    }

    public void createButtons() {
        addItemButton= new JButton("Add Item");
        addTargetButton= new JButton("Add Target");
        startButton= new JButton("Start");
    }

//    public void mouseClicked(MouseEvent e) {
//        String message =
//        if (e.getButton() == MouseEvent.NOBUTTON) {
//          textArea.setText("No button clicked...");
//        } else if (e.getButton() == MouseEvent.BUTTON1) {
//          textArea.setText("Button 1 clicked...");
//        } else if (e.getButton() == MouseEvent.BUTTON2) {
//          textArea.setText("Button 2 clicked...");
//        } else if (e.getButton() == MouseEvent.BUTTON3) {
//          textArea.setText("Button 3 clicked...");
//        }
//
//        System.out.println("Number of click: " + e.getClickCount());
//        System.out.println("Click position (X, Y):  " + e.getX() + ", " + e.getY());
//      }

}
