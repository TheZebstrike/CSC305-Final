package org.example;

public class Connection {
    private final Node fromNode;
    private final String fromDecoration;
    private final Node toNode;
    private final String toDecoration;

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

    @Override
    public String toString() {
        return String.format(
                "Connection[fromNode=%s,toNode=%s,fromDecoration=%s,toDecoration=%s]",
                fromNode.toString(),
                toNode.toString(),
                fromDecoration,
                toDecoration
        );
    }

    public static Connection fromString(String serialized) {
        String content = serialized.substring(12, serialized.length() - 1);
        String[] parts = content.split(",(?=(fromNode|toNode|fromDecoration|toDecoration)=)");

        Node fromNode = null;
        Node toNode = null;
        String fromDecoration = null;
        String toDecoration = null;

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
                case "fromDecoration":
                    fromDecoration = value;
                    break;
                case "toDecoration":
                    toDecoration = value;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown key: " + key);
            }
        }

        if (fromNode == null || toNode == null || fromDecoration == null || toDecoration == null) {
            throw new IllegalArgumentException("Missing required fields in serialized data.");
        }

        return new Connection(fromNode, fromDecoration, toNode, toDecoration);
    }
}

