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
    JButton addItemButton, addTargetButton, startButton, pathButton;
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
            } else if (e.getSource() == pathButton) {
                printPath();
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
        pathButton= new JButton("Create Path");
        pathButton.addActionListener(this);
        this.add(addItemButton);
        this.add(addTargetButton);
        this.add(startButton);
        this.add(pathButton);
    }

    public boolean start() {
        return state == RUNNING;
    }

    public void printPath() {
        // TODO: Merge two projects
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

}
