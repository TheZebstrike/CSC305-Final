package org.example;

public class ClassRelationship {
    private final Node fromNode;
    private final Node toNode;
    private final String relationshipType;

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

    @Override
    public String toString() {
        return String.format(
                "ClassRelationship[fromNode=%s,toNode=%s,relationshipType=%s]",
                fromNode.toString(),
                toNode.toString(),
                relationshipType
        );
    }

    public static ClassRelationship fromString(String serialized) {
        String content = serialized.substring("ClassRelationship[".length(), serialized.length() - 1);  // Remove "ClassRelationship[" and "]"
        String[] parts = content.split(",(?=(fromNode|toNode|relationshipType)=)"); //split by comma and key names

        Node fromNode = null;
        Node toNode = null;
        String relationshipType = null;

        for (String part : parts) {
            String[] keyValue = part.split("=", 2); // Split content into this format: "key=value"
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
