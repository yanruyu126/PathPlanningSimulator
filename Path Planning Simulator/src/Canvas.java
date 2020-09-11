import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class Canvas extends JPanel implements ActionListener {
    Bot bot;
    Playground pg;

    public Canvas() {
        pg= new Playground();
        bot= new Bot(pg);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d= (Graphics2D) g;
        pg.draw(g2d);
        bot.drawBot(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

}
