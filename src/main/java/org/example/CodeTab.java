package org.example;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class CodeTab implements PropertyChangeListener {
    private final JSplitPane splitPane;
    private final JTextArea codeTextArea;
    private final DefaultListModel<String> classListModel;
    private final JList<String> classList;
    private Map<String, String> generatedCodeMap;


    public CodeTab() {
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);

        classListModel = new DefaultListModel<>();
        classList = new JList<>(classListModel);

        codeTextArea = new JTextArea();
        codeTextArea.setEditable(false);

        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        JScrollPane classListScrollPane = new JScrollPane(classList);

        classList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedClass = classList.getSelectedValue();
                if (selectedClass != null) {
                    Map<String, String> codeMap = generatedCodeMap;
                    codeTextArea.setText(codeMap != null ? codeMap.get(selectedClass) : "");
                }
            }
        });
        splitPane.setLeftComponent(classListScrollPane);
        splitPane.setRightComponent(codeScrollPane);
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public void updateCodeTab() {
        CodeGenerator codeGenerator = new CodeGenerator();
        generatedCodeMap = codeGenerator.generateCode();
        classListModel.clear();

        for (String className : generatedCodeMap.keySet()) {
            classListModel.addElement(className);
        }
        codeTextArea.setText("");

        if (!classListModel.isEmpty()) {
            classList.setSelectedIndex(0);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
