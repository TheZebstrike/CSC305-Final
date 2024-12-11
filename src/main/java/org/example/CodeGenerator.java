package org.example;

import java.util.*;

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
        } else if (node.getDecorations().contains("Observable")
                    || node.getDecorations().contains("Singleton")) {
            classCode.append(" extends PropertyChangeSupport");
        } else if (node.getDecorations().contains("Decoration")) {
            classCode.append(" extends Decorator");
        } else if (node.getDecorations().contains("Decoratable")) {
            classCode.append(" extends Component");
        } else if (node.getDecorations().contains("Factory")) {
            classCode.append(" extends Creator");
        }

        //interfaces
        Set<String> interfaces = getImplementedInterfaces(node);
        if (!interfaces.isEmpty() || node.getDecorations().contains("Observer")
                || node.getDecorations().contains("Product") || node.getDecorations().contains("Strategy")) {
            classCode.append(" implements ");
            List<String> interfacesList = new ArrayList<>();
            if (!interfaces.isEmpty()) {
                classCode.append(String.join(", ", interfaces));
            }
            if (node.getDecorations().contains("Observer")) {
                interfacesList.add("PropertyChangeListener");
            }
            if (node.getDecorations().contains("Product")) {
                interfacesList.add("Product");
            }
            if (node.getDecorations().contains("Strategy")) {
                interfacesList.add("Strategy");
            }
            if (!interfacesList.isEmpty()) {
                classCode.append(String.join(", ", interfacesList));
            }
        }
        classCode.append(" {\n");

        //decorations
        for (String decoration : node.getDecorations()) {
            switch (decoration) {
                case "Observer":
                    classCode.append("  @Override\n");
                    classCode.append("  public void propertyChange(PropertyChangeEvent evt) {\n  }\n");
                    break;
                case "Observable":
                    classCode.append("  public void notifyObservers() {\n");
                    classCode.append("    firePropertyChange(prop name, oldVal, newVal);\n  }\n");
                    break;
                case "Singleton":
                    classCode.append("  private static ").append(node.getLabel()).append(" instance;\n");
                    classCode.append("  private ").append(node.getLabel()).append(" {\n");
                    classCode.append("""
                                        super(new Object());
                                      }
                                    """);
                    classCode.append("  public static ").append(node.getLabel()).append("getInstance() {\n    ");
                    classCode.append("""
                                     if (instance == null) {
                                           instance = new""");
                    classCode.append(" ").append(node.getLabel()).append("();\n");
                            classCode.append("""
                                        return instance;
                                      }
                                    """);
                    break;
                case "Decoration", "Decoratable":
                    classCode.append("  public void operation() {\n  }\n");
                    break;
                case "Chain Member":
                    classCode.append("  public void handle() {\n  }\n");
                    break;
                case "Strategy":
                    classCode.append("  public void algorithm() {\n  }\n");
                    break;
                case "Factory":
                    classCode.append("  public Product factoryMethod() {\n  }\n");
                    break;
                case "Product":
                    break;
                default:
                    System.out.println(decoration + ": shouldn't have this in decorations list");
                    break;
            }
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
