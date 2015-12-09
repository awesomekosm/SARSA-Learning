package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Frame {
    Rectangle rec = new Rectangle(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
    int width = rec.width;
    int height = rec.height;
    JFrame f;
    Panel panel;

    public Panel getPanel(){
        return panel;
    }

    public Frame() {
        initializeWindow();
    }

    public void initializeWindow() {
        panel = new Panel();
        f = window();
        f.getContentPane().add(panel.getMainPanel());
    }

    public void resizeRepaint() {
        panel.getMainPanel().removeAll();
        System.gc();
    }

    public void paintButtonLoc(int x, int y, Color color) {
        JButton button[][] = panel.getButtonArray();
        button[x][y].setBackground(color);
        panel.getMainPanel().repaint();
    }

    public JFrame window() {
        JFrame jframe = new JFrame("GridWorld");
        jframe.setSize(new Dimension(width, height));
        jframe.setLocation(0, 0);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return jframe;
    }
}
