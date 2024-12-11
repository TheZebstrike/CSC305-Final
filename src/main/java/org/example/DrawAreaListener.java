package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawAreaListener implements MouseListener, MouseMotionListener {

    private int offsetX, offsetY;
    private Node selectedNode = null;
    private Node connectionStartNode = null;
    private String connectionStartDecoration = null;
    private Node relationshipStartNode = null;
    private String selectedRelationshipType = null;

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
        if (SwingUtilities.isRightMouseButton(e)) {
            Node node = getNodeAtPosition(e.getX(), e.getY());
            if (node != null) {
                handlePopup(e, node);
            }
            return;
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            Node node = getNodeAtPosition(e.getX(), e.getY());
            if (node != null) {
                //if a node is clicked on, it can be either to create a relationship, connect a decoration, or change the node's name
                if (selectedRelationshipType != null) {
                    handleRelationshipClick(e, node);
                }
                else {
                    String clickedDecoration = node.getDecorationAtPosition(e.getX(), e.getY());
                    if (clickedDecoration != null) {
                        handleDecorationClick(e, node, clickedDecoration);
                    }
                    else {
                        handleNameChange(e, node);
                    }
                }
            } else {
                //show error if user selected a relationship but didn't click on a node
                if (selectedRelationshipType != null) {
                    JOptionPane.showMessageDialog(e.getComponent(),
                            "Please click on a class (box) to create the relationship.",
                            "No Class Selected", JOptionPane.WARNING_MESSAGE);
                }
                //make a new node
                else {
                    String name = "Name" + String.format("%02d", Blackboard.getInstance().size() + 1);
                    Node newNode = new Node(name, e.getX(), e.getY());
                    Blackboard.getInstance().add(newNode);
                    Blackboard.getInstance().repaint();
                }
            }
        }
    }
    /*if a node is clicked on and if there is a selected relationship:
        if it is the first click, set the start node as the selected node.
        else if you click on the same node as the start node, show error pop up
        if it is a valid second click then create a ClassRelationship between the 2 nodes
        add relationship to singleton and reset relationship + update canvas
     */
    private void handleRelationshipClick(MouseEvent e, Node node) {
        if (relationshipStartNode == null) {
            relationshipStartNode = node;
        } else {
            if (relationshipStartNode == node) {
                JOptionPane.showMessageDialog(e.getComponent(),
                        "Cannot create a relationship with the same class.",
                        "Invalid Relationship", JOptionPane.WARNING_MESSAGE);
            } else {
                ClassRelationship relationship = new ClassRelationship(relationshipStartNode, node, selectedRelationshipType);
                Blackboard.getInstance().addClassRelationship(relationship);
            }
            relationshipStartNode = null;
            selectedRelationshipType = null;
            Blackboard.getInstance().repaint();
        }
    }
    /*
    if a decoration is clicked on:
        if start node isn't set, set clicked node & decoration as the start node & start decoration
        else if clicked node + decor is the same as the start node + decor show error
        if start node is set and is a valid click, make new Connection and add it to the singleton and update canvas
     */
    private void handleDecorationClick(MouseEvent e, Node node, String clickedDecoration) {
        if (connectionStartNode == null) {
            connectionStartNode = node;
            connectionStartDecoration = clickedDecoration;
        } else {
            if (connectionStartNode == node && connectionStartDecoration.equals(clickedDecoration)) {
                JOptionPane.showMessageDialog(e.getComponent(),
                        "Cannot connect a decoration to itself.",
                        "Invalid Connection", JOptionPane.WARNING_MESSAGE);
            } else {
                Connection connection = new Connection(connectionStartNode, connectionStartDecoration, node, clickedDecoration);
                Blackboard.getInstance().addConnection(connection);
            }
            connectionStartNode = null;
            connectionStartDecoration = null;
            Blackboard.getInstance().repaint();
        }
    }
    private void handleNameChange(MouseEvent e, Node node) {
        String currentName = node.getLabel();
        String newName = (String) JOptionPane.showInputDialog(e.getComponent(),
                "Edit the name of the box:",
                "Edit Name", JOptionPane.PLAIN_MESSAGE, null, null, currentName);
        if (newName != null) {
            newName = newName.trim();
            if (!newName.isEmpty()) {
                boolean duplicate = false;
                for (Node otherNode : Blackboard.getInstance().getNodes()) {
                    if (otherNode != node && otherNode.getLabel().equals(newName)) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    node.setLabel(newName);
                    Blackboard.getInstance().repaint();
                } else {
                    JOptionPane.showMessageDialog(e.getComponent(),
                            "This name already exists. Please choose a different name.",
                            "Duplicate Name", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(e.getComponent(),
                        "Name cannot be empty.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void setSelectedRelationshipType(String type) {
        selectedRelationshipType = type;
        relationshipStartNode = null;
    }

    public void resetRelationshipSelection() {
        selectedRelationshipType = null;
        relationshipStartNode = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && e.isPopupTrigger()) {
            Node node = getNodeAtPosition(e.getX(), e.getY());
            if (node != null) {
                handlePopup(e, node);
            }
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            selectedNode = getNodeAtPosition(e.getX(), e.getY());
            if (selectedNode != null) {
                offsetX = e.getX() - selectedNode.getX();
                offsetY = e.getY() - selectedNode.getY();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedNode != null && SwingUtilities.isLeftMouseButton(e)) {
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
        if (SwingUtilities.isRightMouseButton(e) && e.isPopupTrigger()) {
            Node node = getNodeAtPosition(e.getX(), e.getY());
            if (node != null) {
                handlePopup(e, node);
            }
        } else if (SwingUtilities.isLeftMouseButton(e)) {
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
                    JOptionPane.showMessageDialog(e.getComponent(),
                            "Cannot place the box here. It overlaps with another box.",
                            "Collision Detected", JOptionPane.WARNING_MESSAGE);
                    //go to prev pos or snap to closest grid?
                }
                Blackboard.getInstance().repaint();
                selectedNode = null;
            }
        }
    }

    private void handlePopup(MouseEvent e, Node node) {
        JPopupMenu popupMenu = createPopupMenu(node);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private JPopupMenu createPopupMenu(Node node) {
        JPopupMenu popupMenu = new JPopupMenu();

        String[] options = {"Observer", "Observable", "Singleton", "Decoration",
                "Decoratable", "Chain Member", "Strategy", "Factory", "Product"};

        for (String option : options) {
            JMenuItem menuItem = new JMenuItem(option);
            menuItem.addActionListener(e -> {
                node.addDecoration(option);
                Blackboard.getInstance().repaint();
            });
            popupMenu.add(menuItem);
        }
        return popupMenu;
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

