package org.example;

public class Connection {
    private final Node FROM_NODE;
    private final String FROM_DECORATION;
    private final Node TO_NODE;
    private final String TO_DECORATION;

    public Connection(Node fromNode, String fromDecoration, Node toNode, String toDecoration) {
        this.FROM_NODE = fromNode;
        this.FROM_DECORATION = fromDecoration;
        this.TO_NODE = toNode;
        this.TO_DECORATION = toDecoration;
    }

    public Node getFromNode() {
        return FROM_NODE;
    }

    public String getFromDecoration() {
        return FROM_DECORATION;
    }

    public Node getToNode() {
        return TO_NODE;
    }

    public String getToDecoration() {
        return TO_DECORATION;
    }

    @Override
    public String toString() {
        return String.format(
                "Connection[fromNode=%s,toNode=%s,fromDecoration=%s,toDecoration=%s]",
                FROM_NODE.toString(),
                TO_NODE.toString(),
                FROM_DECORATION,
                TO_DECORATION
        );
    }

    public static Connection fromString(String serialized) {
        String content = serialized.substring(11, serialized.length() - 1);
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

