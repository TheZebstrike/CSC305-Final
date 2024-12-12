package org.example;

public class ClassRelationship {
    private final Node FROM_NODE;
    private final Node TO_NODE;
    private final String RELATIONSHIP_TYPE;

    public ClassRelationship(Node fromNode, Node toNode, String relationshipType) {
        this.FROM_NODE = fromNode;
        this.TO_NODE = toNode;
        this.RELATIONSHIP_TYPE = relationshipType;
    }

    public Node getFromNode() {
        return FROM_NODE;
    }

    public Node getToNode() {
        return TO_NODE;
    }

    public String getRelationshipType() {
        return RELATIONSHIP_TYPE;
    }

    @Override
    public String toString() {
        return String.format(
                "ClassRelationship[fromNode=%s,toNode=%s,relationshipType=%s]",
                FROM_NODE.toString(),
                TO_NODE.toString(),
                RELATIONSHIP_TYPE
        );
    }

    public static ClassRelationship fromString(String serialized) {
        String content = serialized.substring("ClassRelationship[".length(), serialized.length() - 1);
        String[] parts = content.split(",(?=(fromNode|toNode|relationshipType)=)");
        Node fromNode = null;
        Node toNode = null;
        String relationshipType = null;

        for (String part : parts) {
            String[] keyValue = part.split("=", 2);
            if (keyValue.length < 2) {
                throw new IllegalArgumentException("Invalid key-value pair: " + part);
            }

            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            switch (key) {
                case "fromNode":
                    fromNode = Node.fromString(value);
                    break;
                case "toNode":
                    toNode = Node.fromString(value);
                    break;
                case "relationshipType":
                    relationshipType = value;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown key: " + key);
            }
        }
        if (fromNode == null || toNode == null || relationshipType == null) {
            throw new IllegalArgumentException("Missing required fields in serialized data.");
        }
        return new ClassRelationship(fromNode, toNode, relationshipType);
    }
}
