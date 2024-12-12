package org.example;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;

/**
 * A singleton class that serves as a central data store and event scheduler for the UML diagram editor.
 * Implements the Blackboard architectural pattern to manage nodes, connections, and class relationships.
 * Extends PropertyChangeSupport to provide observer pattern functionality.
 *
 * @author Yud Wong, Aidan Stutz
 */

public class Blackboard extends PropertyChangeSupport {
    private static Blackboard instance;
    private final ArrayList<Node> NODES = new ArrayList<>();
    private final ArrayList<Connection> CONNECTIONS = new ArrayList<>();
    private final ArrayList<ClassRelationship> CLASS_RELATIONSHIPS = new ArrayList<>();
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
        NODES.add(node);
        firePropertyChange("NODES", null, node);
    }

    public ArrayList<Node> getNodes() {
        return NODES;
    }

    public void addConnection(Connection connection) {
        CONNECTIONS.add(connection);
        firePropertyChange("CONNECTIONS", null, connection);
    }

    public ArrayList<Connection> getConnections() {
        return CONNECTIONS;
    }

    public void addClassRelationship(ClassRelationship relationship) {
        CLASS_RELATIONSHIPS.add(relationship);
        firePropertyChange("CLASS_RELATIONSHIPS", null, relationship);
    }

    public ArrayList<ClassRelationship> getClassRelationships() {
        return CLASS_RELATIONSHIPS;
    }

    public int size() {
        return NODES.size();
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
