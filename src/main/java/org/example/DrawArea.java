package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DrawArea extends JPanel implements PropertyChangeListener {
    private DrawAreaListener drawAreaListener;

    public DrawArea() {
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //grid lines
        int gridSize = 50;
        g.setColor(new Color(220, 220, 220));
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

        //connections
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK); // Set the color for connections

        for (Connection connection : Blackboard.getInstance().getConnections()) {
            drawCurvedLine(g2, connection);
        }
        for (ClassRelationship relationship : Blackboard.getInstance().getClassRelationships()) {
            drawClassRelationship(g2, relationship);
        }
    }

    private void drawCurvedLine(Graphics2D g2, Connection connection) {
        Node fromNode = connection.getFromNode();
        Node toNode = connection.getToNode();

        Point fromPoint = fromNode.getDecorationPosition(connection.getFromDecoration());
        Point toPoint = toNode.getDecorationPosition(connection.getToDecoration());

        if (fromPoint == null || toPoint == null) {
            //don't draw connections for deleted decorations
            return;
        }

        int ctrlX = (fromPoint.x + toPoint.x) / 2;
        int ctrlY = Math.min(fromPoint.y, toPoint.y) - 50;

        //QuadCurve2D for curved line
        QuadCurve2D q = new QuadCurve2D.Float();
        q.setCurve(fromPoint.x, fromPoint.y, ctrlX, ctrlY, toPoint.x, toPoint.y);
        g2.draw(q);
    }

    private void drawClassRelationship(Graphics2D g2, ClassRelationship relationship) {
        Node fromNode = relationship.getFromNode();
        Node toNode = relationship.getToNode();

        Point fromPoint = fromNode.center();
        Point toPoint = toNode.center();

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        g2.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);

        String relationshipType = relationship.getRelationshipType();

        int midX = (fromPoint.x + toPoint.x) / 2;
        int midY = (fromPoint.y + toPoint.y) / 2;

        int textOffset = 10;

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.BLUE);

        g2.drawString(relationshipType, midX + textOffset, midY - textOffset);
    }

    public void setDrawAreaListener(DrawAreaListener listener) {
        this.drawAreaListener = listener;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }
}

