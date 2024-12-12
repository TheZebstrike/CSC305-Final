package org.example;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Represents the code generation tab in the UML editor interface.
 * Provides a split pane view with a list of classes and their generated code.
 * Implements PropertyChangeListener to update when the diagram changes.
 *
 * @author Yud Wong, Aidan Stutz
 */

public class CodeTab implements PropertyChangeListener {
    private final JSplitPane SPLIT_PANE;
    private final JTextArea CODE_TEXT_AREA;
    private final DefaultListModel<String> CLASS_LIST_MODEL;
    private final JList<String> CLASS_LIST;
    private Map<String, String> generatedCodeMap;

    public CodeTab() {
        SPLIT_PANE = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        SPLIT_PANE.setDividerLocation(200);

        CLASS_LIST_MODEL = new DefaultListModel<>();
        CLASS_LIST = new JList<>(CLASS_LIST_MODEL);

        CODE_TEXT_AREA = new JTextArea();
        CODE_TEXT_AREA.setEditable(false);

        JScrollPane codeScrollPane = new JScrollPane(CODE_TEXT_AREA);
        JScrollPane classListScrollPane = new JScrollPane(CLASS_LIST);

        CLASS_LIST.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedClass = CLASS_LIST.getSelectedValue();
                if (selectedClass != null) {
                    Map<String, String> codeMap = generatedCodeMap;
                    CODE_TEXT_AREA.setText(codeMap != null ? codeMap.get(selectedClass) : "");
                }
            }
        });
        SPLIT_PANE.setLeftComponent(classListScrollPane);
        SPLIT_PANE.setRightComponent(codeScrollPane);
    }

    public JSplitPane getSplitPane() {
        return SPLIT_PANE;
    }

    public void updateCodeTab() {
        CodeGenerator codeGenerator = new CodeGenerator();
        generatedCodeMap = codeGenerator.generateCode();
        CLASS_LIST_MODEL.clear();

        for (String className : generatedCodeMap.keySet()) {
            CLASS_LIST_MODEL.addElement(className);
        }
        CODE_TEXT_AREA.setText("");

        if (!CLASS_LIST_MODEL.isEmpty()) {
            CLASS_LIST.setSelectedIndex(0);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
