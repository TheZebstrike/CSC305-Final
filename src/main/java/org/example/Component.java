package org.example;

import java.awt.*;

public abstract class Component {
    protected int x, y;
    protected Color color;

    public Component(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public abstract void draw(Graphics g);

    public abstract String toSvgCode();

    protected String getSvgColor() {
        if (color.equals(Color.RED)) return "red";
        if (color.equals(Color.BLUE)) return "blue";
        if (color.equals(Color.BLACK)) return "black";
        return "black";
    }
}
