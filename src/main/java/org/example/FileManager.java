package org.example;

import javax.swing.*;
import java.io.*;

public class FileManager {
    private JFrame frame = null;
    public FileManager(JFrame f) {
        this.frame = f;
    }
    public void handleNewFile() {

    }
    public void handleOpenFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open SVG File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("SVG Files", "svg"));

        int userSelection = fileChooser.showOpenDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String content = readAndParseFile(selectedFile);

                //Blackboard.getInstance().setFileContent(content);
                Blackboard.getInstance().repaint();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private String readAndParseFile(File file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        //Blackboard.getInstance().getShapes().clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
                if (line.startsWith("<circle")) {
                    String[] lineParts = line.split(" ");
                    int radius = Integer.parseInt(lineParts[1].substring(3,5));
                    int cx = Integer.parseInt(lineParts[2].substring(4,lineParts[2].length()-1));
                    int cy = Integer.parseInt(lineParts[3].substring(4,lineParts[3].length()-1));
                    String nextLine = br.readLine();
                }
                if (line.startsWith("<rect")) {
                    String[] lineParts = line.split(" ");
                    int x = Integer.parseInt(lineParts[1].substring(3,lineParts[1].length()-1));
                    int y = Integer.parseInt(lineParts[2].substring(3,lineParts[2].length()-1));
                    int width = Integer.parseInt(lineParts[3].substring(7,lineParts[3].length()-1));
                    String nextLine = br.readLine();
                    String[] nextParts = nextLine.split(" ");
                }
            }
        }
        return contentBuilder.toString();
    }

    private void handleSaveFile() {
        //String svgContent = Blackboard.getInstance().getFileContent();

        // Use JFileChooser to prompt the user to select a save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save SVG File");
        fileChooser.setSelectedFile(new File("drawing.svg"));

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Ensure the file ends with .svg extension
            if (!fileToSave.getName().toLowerCase().endsWith(".svg")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".svg");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                //writer.write(svgContent);
                JOptionPane.showMessageDialog(frame, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showHelpDialog() {
        JOptionPane.showMessageDialog(frame, "Authors: Yud Wong + Aidan Stutz\n" + "You can open or save files when clicking File button\n" +
                        "You can change between circles and squares, blue and red when clicking on shape and color button",
                "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
