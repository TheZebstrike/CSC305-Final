package org.example;

import javax.swing.*;
import java.io.*;

public class FileManager {
    private final JFrame frame;
    public FileManager(JFrame f) {
        this.frame = f;
    }
    public void newFile() {
        //clear everything
        Blackboard.getInstance().getNodes().clear();
        Blackboard.getInstance().getClassRelationships().clear();
        Blackboard.getInstance().getConnections().clear();
        Blackboard.getInstance().repaint();
        Blackboard.getInstance().getCodeTextArea().setText("");
        Blackboard.getInstance().getClassListModel().clear();
    }
    public void handleOpenFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Diagram File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("SVG Files", "svg"));

        int userSelection = fileChooser.showOpenDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Blackboard.getInstance().setFileName(selectedFile.getName());
            try {
                String content = readAndParseFile(selectedFile);
                Blackboard.getInstance().setFileContent(content);
                Blackboard.getInstance().repaint();
                Blackboard.getInstance().updateCodeTab();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private String readAndParseFile(File file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        newFile(); //reset everything
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
                if (line.startsWith("Node[") && line.endsWith("]")) {
                    Blackboard.getInstance().getNodes().add(Node.fromString(line));
                }
                if (line.startsWith("ClassRelationship[") && line.endsWith("]")) {
                    Blackboard.getInstance().getClassRelationships().add(ClassRelationship.fromString(line));
                }
                if (line.startsWith("Connection[") && line.endsWith("]")) {
                    Blackboard.getInstance().getConnections().add(Connection.fromString(line));
                }
            }
        }
        return contentBuilder.toString();
    }

    public void handleSaveAs() {
        saveFile();
        String fileContent = Blackboard.getInstance().getFileContent();

        // Use JFileChooser to prompt the user to select a save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Diagram File");
        if (Blackboard.getInstance().getFileName() != null) {
            fileChooser.setSelectedFile(new File(Blackboard.getInstance().getFileName()));
        } else {
            fileChooser.setSelectedFile(new File("diagram.svg"));
        }

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Ensure the file ends with .svg extension
            if (!fileToSave.getName().toLowerCase().endsWith(".svg")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".svg");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(fileContent);
                JOptionPane.showMessageDialog(frame, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void saveFile() {
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
        JOptionPane.showMessageDialog(frame, "File content updated!");
    }
}
