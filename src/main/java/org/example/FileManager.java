package org.example;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages file operations for the UML editor.
 * Handles saving and loading diagram files, including
 * their connections and relationships.
 *
 * @author Yud Wong, Aidan Stutz
 */

public class FileManager {
    private final JFrame FRAME;

    public FileManager(JFrame f) {
        this.FRAME = f;
    }

    public void newFile() {
        Blackboard.getInstance().getNodes().clear();
        Blackboard.getInstance().getClassRelationships().clear();
        Blackboard.getInstance().getConnections().clear();
        Blackboard.getInstance().repaint();
    }

    public void handleOpenFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Diagram File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("SVG Files", "svg"));
        int userSelection = fileChooser.showOpenDialog(FRAME);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Blackboard.getInstance().setFile(selectedFile);
            try {
                String content = readAndParseFile(selectedFile);
                Blackboard.getInstance().setFileContent(content);
                Blackboard.getInstance().repaint();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(FRAME, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String readAndParseFile(File file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        newFile();
        Map<String, Node> nodeMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
                if (line.startsWith("Node[") && line.endsWith("]")) {
                    Node node = Node.fromString(line);
                    nodeMap.put(node.getLabel(), node);
                    Blackboard.getInstance().add(node);
                }
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ClassRelationship[") && line.endsWith("]")) {
                    ClassRelationship relationship = ClassRelationship.fromString(line);
                    Node fromNode = nodeMap.get(relationship.getFromNode().getLabel());
                    Node toNode = nodeMap.get(relationship.getToNode().getLabel());
                    if (fromNode != null && toNode != null) {
                        ClassRelationship newRelationship = new ClassRelationship(
                                fromNode, toNode, relationship.getRelationshipType());
                        Blackboard.getInstance().addClassRelationship(newRelationship);
                    }
                }
                if (line.startsWith("Connection[") && line.endsWith("]")) {
                    Connection connection = Connection.fromString(line);
                    Node fromNode = nodeMap.get(connection.getFromNode().getLabel());
                    Node toNode = nodeMap.get(connection.getToNode().getLabel());
                    if (fromNode != null && toNode != null) {
                        Connection newConnection = new Connection(
                                fromNode, connection.getFromDecoration(),
                                toNode, connection.getToDecoration());
                        Blackboard.getInstance().addConnection(newConnection);
                    }
                }
            }
        }
        Blackboard.getInstance().repaint();
        return contentBuilder.toString();
    }

    public void handleSave() {
        updateFile();
        File fileToSave = Blackboard.getInstance().getFile();
        if (fileToSave != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(Blackboard.getInstance().getFileContent());
                JOptionPane.showMessageDialog(FRAME, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(FRAME, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            handleSaveAs();
        }
    }

    public void handleSaveAs() {
        updateFile();
        String fileContent = Blackboard.getInstance().getFileContent();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Diagram File");
        if (Blackboard.getInstance().getFile() != null) {
            fileChooser.setSelectedFile(new File(Blackboard.getInstance().getFile().getName()));
        } else {
            fileChooser.setSelectedFile(new File("diagram.svg"));
        }

        int userSelection = fileChooser.showSaveDialog(FRAME);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".svg")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".svg");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(fileContent);
                JOptionPane.showMessageDialog(FRAME, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(FRAME, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateFile() {
        StringBuilder diagramContent = new StringBuilder();
        diagramContent.append("<svg\n");
        diagramContent.append("xmlns=\"http://www.w3.org/2000/svg\"\n");
        diagramContent.append(String.format("width=\"%d\"\n", 800));
        diagramContent.append(String.format("height=\"%d\">\n", 600));
        for (Node node : Blackboard.getInstance().getNodes()) {
            diagramContent.append(node.toString()).append("\n");
        }
        for (ClassRelationship relationship : Blackboard.getInstance().getClassRelationships()) {
            diagramContent.append(relationship.toString()).append("\n");
        }
        for (Connection connection : Blackboard.getInstance().getConnections()) {
            diagramContent.append(connection.toString()).append("\n");
        }
        diagramContent.append("</svg>");
        Blackboard.getInstance().setFileContent(diagramContent.toString());
    }
}
