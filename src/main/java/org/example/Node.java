package org.example;

import java.awt.*;
import java.awt.Rectangle;

public class Node {

    private final Rectangle bounds;
    private String label;
    private static final int DEFAULT_SIZE = 50;

    public Node(String label, int x, int y) {
        bounds = new Rectangle(x - DEFAULT_SIZE / 2, y - DEFAULT_SIZE / 2, DEFAULT_SIZE, DEFAULT_SIZE);
        this.label = label;
    }

    public int getX() {
        return bounds.x;
    }

    public int getY() {
        return bounds.y;
    }

    public void draw(Graphics g) {
        int x = bounds.x;
        int y = bounds.y;
        int size = bounds.width;
        g.setColor(Color.BLUE);
        g.fillRect(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString(label, x + 5, y + size / 2);
    }

    public void move(int x, int y) {
        bounds.x = x - bounds.width / 2;
        bounds.y = y - bounds.height / 2;
    }

    public boolean contains(int x, int y) {
        return bounds.contains(x, y);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Point center() {
        return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
    }

    public int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

