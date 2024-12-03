package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Main extends JFrame {
    private JTextArea codeTextArea;
    private JList<String> classList;
    private DefaultListModel<String> classListModel;
    private Map<String, String> generatedCodeMap;

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

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);

        //left pane
        classListModel = new DefaultListModel<>();
        classList = new JList<>(classListModel);
        JScrollPane classListScrollPane = new JScrollPane(classList);
        splitPane.setLeftComponent(classListScrollPane);

        //right pane
        codeTextArea = new JTextArea();
        codeTextArea.setEditable(false);
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        splitPane.setRightComponent(codeScrollPane);

        tabbedPane.addTab("Code", splitPane);

        classList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedClass = classList.getSelectedValue();
                if (selectedClass != null && generatedCodeMap != null) {
                    String code = generatedCodeMap.get(selectedClass);
                    codeTextArea.setText(code);
                }
            }
        });
        setJMenuBar(createMenuBar(drawAreaListener));
        add(tabbedPane);
    }

    private JMenuBar createMenuBar(DrawAreaListener drawAreaListener) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> {
            //implement New functionality
        });
        fileMenu.add(newItem);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> {
            //implement Open functionality
        });
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> {
            //implement Save functionality
        });
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(e -> {
            //implement Save As functionality
        });
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
        runItem.addActionListener(e -> generateCode());
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
        return menuBar;
    }

    private void generateCode() {
        CodeGenerator codeGenerator = new CodeGenerator();
        generatedCodeMap = codeGenerator.generateCode(); // Now returns a Map

        classListModel.clear();
        for (String className : generatedCodeMap.keySet()) {
            classListModel.addElement(className);
        }
        codeTextArea.setText("");

        if (!classListModel.isEmpty()) {
            classList.setSelectedIndex(0);
        }
    }
}
