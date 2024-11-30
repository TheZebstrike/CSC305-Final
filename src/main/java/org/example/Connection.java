package org.example;

public class Connection {
    private Node fromNode;
    private String fromDecoration;
    private Node toNode;
    private String toDecoration;

    public Connection(Node fromNode, String fromDecoration, Node toNode, String toDecoration) {
        this.fromNode = fromNode;
        this.fromDecoration = fromDecoration;
        this.toNode = toNode;
        this.toDecoration = toDecoration;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public String getFromDecoration() {
        return fromDecoration;
    }

    public Node getToNode() {
        return toNode;
    }

    public String getToDecoration() {
        return toDecoration;
    }
}

