package org.example;

import java.awt.*;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class Node {
    private final Rectangle bounds;
    private String label;
    private static final int DEFAULT_SIZE = 50;
    private Set<String> decorations = new HashSet<>();

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

        g.setColor(Color.WHITE);
        g.fillRect(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);

        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        g.drawString(label, x + (size - labelWidth) / 2, y + 15);
        int decoY = y + 30;
        g.setFont(new Font("Arial", Font.PLAIN, 10));

        for (String decoration : decorations) {
            String decoText = "<< " + decoration + " >>";
            int decoWidth = fm.stringWidth(decoText);
            g.drawString(decoText, x + (size - decoWidth) / 2, decoY);
            decoY += 15;
        }
    }

    public void addDecoration(String decoration) {
        decorations.add(decoration);
    }

    public Set<String> getDecorations() {
        return decorations;
    }

    public Point getDecorationPosition(String decoration) {
        int x = bounds.x;
        int y = bounds.y;
        int size = bounds.width;

        int decoY = y + 30;
        int decorationHeight = 15;

        for (String deco : decorations) {
            if (deco.equals(decoration)) {
                int decoX = x + size / 2;
                int decoCenterY = decoY - decorationHeight / 2;
                return new Point(decoX, decoCenterY);
            }
            decoY += decorationHeight;
        }
        return null;
    }

    public String getDecorationAtPosition(int x, int y) {
        int nodeX = bounds.x;
        int nodeY = bounds.y;
        int size = bounds.width;

        int decoY = nodeY + 30;
        int decorationHeight = 15;
        int avgCharWidth = 7;

        for (String deco : decorations) {
            String decoText = "<< " + deco + " >>";
            int decoWidth = decoText.length() * avgCharWidth;
            int decoX = nodeX + (size - decoWidth) / 2;
            Rectangle decoBounds = new Rectangle(decoX, decoY - decorationHeight + 5, decoWidth, decorationHeight);
            if (decoBounds.contains(x, y)) {
                return deco;
            }
            decoY += decorationHeight;
        }
        return null;
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

    @Override
    public String toString() {
        StringJoiner decorationJoiner = new StringJoiner(",");
        for (String decoration : decorations) {
            decorationJoiner.add(decoration);
        }

        return String.format(
                "Node[label=%s,x=%d,y=%d,decorations=%s]",
                label,
                bounds.x + bounds.width / 2,
                bounds.y + bounds.height / 2,
                decorationJoiner.toString()
        );
    }

    public static Node fromString(String serialized) {
        String content = serialized.substring(5, serialized.length() - 1);
        String[] parts = content.split(",(?=[a-z]+=)");

        String label = null;
        int x = 0, y = 0;
        Set<String> decorations = new HashSet<>();

        for (String part : parts) {
            String[] keyValue = part.split("=", 2);
            if (keyValue.length < 2) {
                throw new IllegalArgumentException("Invalid key-value pair: " + part);
            }
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            switch (key) {
                case "label":
                    label = value;
                    break;
                case "x":
                    x = Integer.parseInt(value);
                    break;
                case "y":
                    y = Integer.parseInt(value);
                    break;
                case "decorations":
                    if (!value.isEmpty()) {
                        decorations.addAll(Arrays.asList(value.split(",")));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown key: " + key);
            }
        }

        if (label == null) {
            throw new IllegalArgumentException("Missing label in serialized data.");
        }

        Node node = new Node(label, x, y);
        node.decorations.addAll(decorations);
        return node;
    }
}

