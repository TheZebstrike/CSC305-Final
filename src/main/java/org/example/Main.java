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
        Blackboard.getInstance().addPropertyChangeListener(drawPanel);

        setJMenuBar(createMenuBar());

        add(drawPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        return menuBar;
    }
}
