package org.example;

import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        Main main = new Main();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setTitle("UML Diagram Editor");
        main.setSize(800, 600);
        main.setLocationRelativeTo(null);
        main.setResizable(true);
        main.setVisible(true);
    }

    public Main() {
        DrawArea drawPanel = new DrawArea();
        DrawAreaListener drawAreaListener = new DrawAreaListener();
        drawPanel.addMouseListener(drawAreaListener);
        drawPanel.addMouseMotionListener(drawAreaListener);
        drawPanel.setDrawAreaListener(drawAreaListener);
        Blackboard.getInstance().addPropertyChangeListener(drawPanel);
        setJMenuBar(createMenuBar(drawAreaListener));
        add(drawPanel);
    }

    private JMenuBar createMenuBar(DrawAreaListener drawAreaListener) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenu boxConnectorMenu = new JMenu("Box Connector");
        menuBar.add(boxConnectorMenu);

        String[] relationshipTypes = {
                "Inheritance", "Association", "Aggregation",
                "Composition", "Dependency", "Realization"
        };

        for (String type : relationshipTypes) {
            JMenuItem menuItem = new JMenuItem(type);
            menuItem.addActionListener(e -> {
                drawAreaListener.setSelectedRelationshipType(type);
                JOptionPane.showMessageDialog(this,
                        "Relationship type '" + type + "' selected.\n" +
                                "Now click on the source and target classes to create the relationship.",
                        "Relationship Type Selected", JOptionPane.INFORMATION_MESSAGE);
            });
            boxConnectorMenu.add(menuItem);
        }

        JMenuItem cancelItem = new JMenuItem("Cancel");
        cancelItem.addActionListener(e -> {
            drawAreaListener.resetRelationshipSelection();
            JOptionPane.showMessageDialog(this,
                    "Relationship creation canceled.",
                    "Canceled", JOptionPane.INFORMATION_MESSAGE);
        });
        boxConnectorMenu.add(cancelItem);
        return menuBar;
    }
}
