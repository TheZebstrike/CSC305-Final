package org.example;

import javax.swing.*;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;

public class Blackboard extends PropertyChangeSupport {
    private static Blackboard instance;
    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Connection> connections = new ArrayList<>();
    private final ArrayList<ClassRelationship> classRelationships = new ArrayList<>();
    private String fileContent;
    private File file = null;

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

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
        firePropertyChange("connections", null, connection);
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void addClassRelationship(ClassRelationship relationship) {
        classRelationships.add(relationship);
        firePropertyChange("classRelationships", null, relationship);
    }

    public ArrayList<ClassRelationship> getClassRelationships() {
        return classRelationships;
    }

    public int size() {
        return nodes.size();
    }

    public void repaint() {
        firePropertyChange("repaint", false, true);
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
