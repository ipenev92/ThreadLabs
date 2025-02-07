package org.fbmoll.threadlabs.utils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JCheckBox editorComponent;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof JCheckBox) {
            this.editorComponent = (JCheckBox) value;
        } else {
            this.editorComponent = new JCheckBox();
        }
        if (isSelected) {
            this.editorComponent.setBackground(table.getSelectionBackground());
            this.editorComponent.setForeground(table.getSelectionForeground());
        } else {
            this.editorComponent.setBackground(table.getBackground());
            this.editorComponent.setForeground(table.getForeground());
        }
        return this.editorComponent;
    }

    @Override
    public Object getCellEditorValue() {
        return this.editorComponent;
    }
}