package org.example;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;
    private final ArrayList<Node> nodes = new ArrayList<>();

    private Blackboard() {
        super(new Object());
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public void add(Node node) {
        nodes.add(node);
        firePropertyChange("nodes", null, node);
    }

    public Node get(int index) {
        return nodes.get(index);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public int size() {
        return nodes.size();
    }

    public void repaint() {
        firePropertyChange("repaint", false, true);
    }
}

