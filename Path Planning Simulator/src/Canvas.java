import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Canvas extends JPanel implements ActionListener, MouseListener {
    private static final long serialVersionUID= 1L;
    Bot bot;
    Playground pg;
    JButton addItemButton, addTargetButton, startButton;
    int INIT= 0;
    int RUNNING= 1;
    int state;
    boolean addingTarget, addingItem;

    public Canvas() {
        pg= new Playground();
        bot= new Bot(pg);
        createButtons();
        addingTarget= false;
        addingItem= false;

        addMouseListener(this);
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
            if (e.getSource() == addItemButton) {
                addingItem= true;
            } else if (e.getSource() == addTargetButton) {
                addingTarget= true;
            } else if (e.getSource() == startButton) {
                state= RUNNING;
            }
        }
        repaint();
    }

    public void createButtons() {
        addItemButton= new JButton("Add Item");
        addItemButton.addActionListener(this);
        addTargetButton= new JButton("Add Target");
        addTargetButton.addActionListener(this);
        startButton= new JButton("Start");
        startButton.addActionListener(this);
        this.add(addItemButton);
        this.add(addTargetButton);
        this.add(startButton);
    }

    public boolean start() {
        return state == RUNNING;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX, mouseY;
        mouseX= e.getX();
        mouseY= e.getY();
        System.out.println(mouseX);
        if (addingItem) {
            pg.addItem(mouseX, mouseY);
            addingItem= false;
        } else if (addingTarget) {
            pg.addTarget(mouseX, mouseY);
            addingTarget= false;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

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
