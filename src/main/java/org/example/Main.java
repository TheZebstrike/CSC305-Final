package org.example;

import javax.swing.*;

/**
 * Main driver class for the UML diagram editor.
 * Sets up the main window, menu bar, and initializes all necessary components.
 * Extends JFrame to provide the main application window.
 *
 * @author Yud Wong, Aidan Stutz
 */

public class Main extends JFrame {
    private final CodeTab codeTab = new CodeTab();

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
        JTabbedPane tabbedPane = new JTabbedPane();
        DrawArea drawPanel = new DrawArea();
        DrawAreaListener drawAreaListener = new DrawAreaListener();
        drawPanel.addMouseListener(drawAreaListener);
        drawPanel.addMouseMotionListener(drawAreaListener);
        drawPanel.setDrawAreaListener(drawAreaListener);
        Blackboard.getInstance().addPropertyChangeListener(drawPanel);
        tabbedPane.addTab("Draw Area", drawPanel);

        tabbedPane.addTab("Code", codeTab.getSplitPane());
        Blackboard.getInstance().addPropertyChangeListener(codeTab);

        setJMenuBar(createMenuBar(drawAreaListener));
        add(tabbedPane);
    }

    private JMenuBar createMenuBar(DrawAreaListener drawAreaListener) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        FileManager fileManager = new FileManager(this);
        menuBar.add(fileMenu);

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> fileManager.newFile());
        fileMenu.add(newItem);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> fileManager.handleOpenFile());
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> fileManager.handleSave());
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(e -> fileManager.handleSaveAs());
        fileMenu.add(saveAsItem);

        JMenu boxConnectorMenu = new JMenu("Box Connector");
        menuBar.add(boxConnectorMenu);

        String[] relationshipTypes = {
                "Inheritance", "Association", "Aggregation",
                "Composition", "Dependency", "Realization"
        };

        JMenu toolsMenu = new JMenu("Tools");
        menuBar.add(toolsMenu);

        JMenuItem runItem = new JMenuItem("Run");
        runItem.addActionListener(e -> codeTab.updateCodeTab());
        toolsMenu.add(runItem);

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

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
    """
                CSC305 Final Project 2024
                Authors: Yud Wong + Aidan Stutz
            """));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }
}
