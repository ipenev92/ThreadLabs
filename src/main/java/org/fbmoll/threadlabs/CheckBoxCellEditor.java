package org.fbmoll.threadlabs;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JCheckBox editorComponent;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // Retrieve the actual JCheckBox from the model
        if (value instanceof JCheckBox) {
            editorComponent = (JCheckBox) value;
        } else {
            editorComponent = new JCheckBox();
            editorComponent.setHorizontalAlignment(SwingConstants.CENTER);
        }
        // You can also set colors based on selection if desired:
        if (isSelected) {
            editorComponent.setBackground(table.getSelectionBackground());
            editorComponent.setForeground(table.getSelectionForeground());
        } else {
            editorComponent.setBackground(table.getBackground());
            editorComponent.setForeground(table.getForeground());
        }
        return editorComponent;
    }

    @Override
    public Object getCellEditorValue() {
        // Return the same JCheckBox instance so the model continues to store a JCheckBox.
        return editorComponent;
    }
}