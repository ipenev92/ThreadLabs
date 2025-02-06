package org.fbmoll.threadlabs;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
    public CheckBoxRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER); // ✅ Keep checkboxes centered
        setOpaque(true); // ✅ Prevent background blending issues
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Boolean) {
            this.setSelected((Boolean) value);
        }
        return this;
    }
}