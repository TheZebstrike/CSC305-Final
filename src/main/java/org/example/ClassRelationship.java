package org.example;

public class ClassRelationship {
    private Node fromNode;
    private Node toNode;
    private String relationshipType;

    public ClassRelationship(Node fromNode, Node toNode, String relationshipType) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.relationshipType = relationshipType;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public String getRelationshipType() {
        return relationshipType;
    }
}
