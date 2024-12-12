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
        classCode.append(getGlobalVariablesCode(node));
        classCode.append(getConstructorCode(node));

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
        String methodCode = getMethodCode(node);
        classCode.append(methodCode);
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

    private String getGlobalVariablesCode(Node node) {
        StringBuilder classCode = new StringBuilder();

        if (node.getDecorations().contains("Singleton")) {
            classCode.append("  private static ").append(node.getLabel()).append(" instance;\n");
        }

        for (ClassRelationship relationship : Blackboard.getInstance().getClassRelationships()) {
            if (relationship.getRelationshipType().equals("Composition") &&
                    relationship.getFromNode() == node) {
                classCode.append("  private ").append(relationship.getToNode().getLabel()).append(" compositionVarName;\n");
            }
            if (relationship.getRelationshipType().equals("Aggregation")&&
                    relationship.getFromNode() == node) {
                classCode.append("  private ").append(relationship.getToNode().getLabel()).append(" aggregationVarName;\n");
            }
        }
        return classCode.toString();
    }

    private String getConstructorCode(Node node) {
        StringBuilder classCode = new StringBuilder();
        List<String> paramList = new ArrayList<>();
        List<String> varList = new ArrayList<>();

        String firstLine = "  public " + node.getLabel() + "(";
        if (node.getDecorations().contains("Singleton")) {
            firstLine = "  private " + node.getLabel() + " (";
        }

        for (ClassRelationship relationship : Blackboard.getInstance().getClassRelationships()) {
            if (relationship.getRelationshipType().equals("Composition") &&
                    relationship.getFromNode() == node) {
                String varStr = "    compositionVarName = new" + relationship.getToNode().getLabel() + "(); //composition";
                varList.add(varStr);
            }
            if (relationship.getRelationshipType().equals("Aggregation") &&
                    relationship.getFromNode() == node) {
                String param = relationship.getToNode().getLabel() + " aggrVarName";
                String varStr = "    this.aggregationVarName = aggrVarName; //Aggregation";
                paramList.add(param);
                varList.add(varStr);
            }
        }

        if (!paramList.isEmpty()) {
            firstLine = firstLine.concat(String.join(", ", paramList));
        }
        firstLine = firstLine.concat(") {\n");
        classCode.append(firstLine);
        if (node.getDecorations().contains("Singleton")) {
            classCode.append("    super(new Object());");
        }
        if (!varList.isEmpty()) {
            classCode.append(String.join("\n", varList));
        }
        classCode.append("\n  }\n");
        return classCode.toString();
    }

    private String getMethodCode(Node node) {
        StringBuilder classCode = new StringBuilder();
        for (ClassRelationship relationship : Blackboard.getInstance().getClassRelationships()) {
            if (relationship.getRelationshipType().equals("Association") &&
                    relationship.getFromNode().getLabel().equals(node.getLabel())) {
                classCode.append("  public void method(").append(relationship.getToNode().getLabel()).append(" varName) {\n");
                classCode.append("    ").append(node.getLabel()).append(".method(varName);\n  }\n");
            }
        }
        return classCode.toString();
    }
}
