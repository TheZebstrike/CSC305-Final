package org.example;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Rectangle;

public class DrawAreaListener implements MouseListener, MouseMotionListener {

    private int offsetX, offsetY;
    private Node selectedNode = null;

    private int nodeSelected(MouseEvent e) {
        for (int i = 0; i < Blackboard.getInstance().size(); i++) {
            if (Blackboard.getInstance().get(i).contains(e.getX(), e.getY())) {
                return i;
            }
        }
        return -1;
    }

    private Node getNodeAtPosition(int x, int y) {
        for (Node node : Blackboard.getInstance().getNodes()) {
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int index = nodeSelected(e);
        if (index == -1) {
            //new square
            String name = "Name" + String.format("%02d", Blackboard.getInstance().size() + 1);
            Node newNode = new Node(name, e.getX(), e.getY());
            Blackboard.getInstance().add(newNode);
            Blackboard.getInstance().repaint();
        } else {
            //edit square name
            Node selectedNode = Blackboard.getInstance().get(index);
            String currentName = selectedNode.getLabel();
            String newName = (String) JOptionPane.showInputDialog(
                    e.getComponent(),
                    "Edit the name of the box:",
                    "Edit Name",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    currentName
            );
            //check for duplicate or empty names
            if (newName != null) {
                newName = newName.trim();
                if (!newName.isEmpty()) {
                    boolean duplicate = false;
                    for (Node node : Blackboard.getInstance().getNodes()) {
                        if (node != selectedNode && node.getLabel().equals(newName)) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (!duplicate) {
                        selectedNode.setLabel(newName);
                        Blackboard.getInstance().repaint();
                    } else {
                        JOptionPane.showMessageDialog(
                                e.getComponent(),
                                "This name already exists. Please choose a different name.",
                                "Duplicate Name",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            e.getComponent(),
                            "Name cannot be empty.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectedNode = getNodeAtPosition(e.getX(), e.getY());
        if (selectedNode != null) {
            offsetX = e.getX() - selectedNode.getX();
            offsetY = e.getY() - selectedNode.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedNode != null) {
            int newX = e.getX() - offsetX;
            int newY = e.getY() - offsetY;

            Rectangle newBounds = new Rectangle(newX, newY, selectedNode.getWidth(), selectedNode.getHeight());

            boolean collision = false;
            for (Node node : Blackboard.getInstance().getNodes()) {
                if (node != selectedNode && node.getBounds().intersects(newBounds)) {
                    collision = true;
                    break;
                }
            }

            if (!collision) {
                selectedNode.move(newX, newY);
                Blackboard.getInstance().repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedNode != null) {
            int gridSize = 50;
            int x = selectedNode.getX();
            int y = selectedNode.getY();
            int snappedX = Math.round(x / (float) gridSize) * gridSize;
            int snappedY = Math.round(y / (float) gridSize) * gridSize;

            Rectangle newBounds = new Rectangle(snappedX, snappedY, selectedNode.getWidth(), selectedNode.getHeight());

            boolean collision = false;
            for (Node node : Blackboard.getInstance().getNodes()) {
                if (node != selectedNode && node.getBounds().intersects(newBounds)) {
                    collision = true;
                    break;
                }
            }

            if (!collision) {
                selectedNode.move(snappedX, snappedY);
            } else {
                JOptionPane.showMessageDialog(
                        e.getComponent(),
                        "Cannot place the box here. It overlaps with another box.",
                        "Collision Detected",
                        JOptionPane.WARNING_MESSAGE
                );
                //revert to previous position or snap to closest open grid?
            }

            Blackboard.getInstance().repaint();
            selectedNode = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}

