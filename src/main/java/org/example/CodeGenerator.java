package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CodeGenerator {
    public Map<String, String> generateCode() {
        Map<String, String> codeMap = new HashMap<>();

        for (Node node : Blackboard.getInstance().getNodes()) {
            String classCode = generateClassCode(node);
            codeMap.put(node.getLabel(), classCode);
        }

        return codeMap;
    }

    private String generateClassCode(Node node) {
        StringBuilder classCode = new StringBuilder();

        classCode.append("public class ").append(node.getLabel());

        //inheritance
        String parentClass = getParentClass(node);
        if (parentClass != null) {
            classCode.append(" extends ").append(parentClass);
        }

        //interfaces
        Set<String> interfaces = getImplementedInterfaces(node);
        if (!interfaces.isEmpty()) {
            classCode.append(" implements ");
            classCode.append(String.join(", ", interfaces));
        }

        classCode.append(" {\n");

        //decorations
        for (String decoration : node.getDecorations()) {
            classCode.append("    // ").append(decoration).append("\n");
        }

        classCode.append("}");
        return classCode.toString();
    }

    private String getParentClass(Node node) {
        for (ClassRelationship relationship : Blackboard.getInstance().getClassRelationships()) {
            if (relationship.getRelationshipType().equals("Inheritance") &&
                    relationship.getFromNode() == node) {
                return relationship.getToNode().getLabel();
            }
        }
        return null;
    }

    private Set<String> getImplementedInterfaces(Node node) {
        Set<String> interfaces = new HashSet<>();
        for (ClassRelationship relationship : Blackboard.getInstance().getClassRelationships()) {
            if (relationship.getRelationshipType().equals("Realization") &&
                    relationship.getFromNode() == node) {
                interfaces.add(relationship.getToNode().getLabel());
            }
        }
        return interfaces;
    }
}
