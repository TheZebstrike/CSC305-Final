package org.example;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DrawArea extends JPanel implements PropertyChangeListener {

    public DrawArea() {
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //grid lines
        int gridSize = 50;
        g.setColor(new Color(220, 220, 220));
        /*for (int i = 0; i < getWidth(); i += gridSize) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += gridSize) {
            g.drawLine(0, i, getWidth(), i);
        }*/

        for (int i = gridSize / 2; i < getWidth(); i += gridSize) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = gridSize / 2; i < getHeight(); i += gridSize) {
            g.drawLine(0, i, getWidth(), i);
        }

        //nodes
        for (Node node : Blackboard.getInstance().getNodes()) {
            node.draw(g);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }
}

