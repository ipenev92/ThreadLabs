package org.fbmoll.threadlabs.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.utils.CheckBoxCellEditor;
import org.fbmoll.threadlabs.objects.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationTable extends JTable {
    final Controller controller;
    final JTable table;
    JCheckBox useSynchronizedCheckBox;
    JCheckBox useLimitsCheckBox;

    public ConfigurationTable(Controller controller) {
        this.controller = controller;
        this.table = createConfigurationTable();
    }

    private JTable createConfigurationTable() {
        String[] columnNames = {"Parameter", "Value"};

        useSynchronizedCheckBox = new JCheckBox();
        useSynchronizedCheckBox.setSelected(true);
        useLimitsCheckBox = new JCheckBox();
        useLimitsCheckBox.setSelected(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        JTable tb = getJTable(columnNames);
        for (int i = 0; i < tb.getColumnCount(); i++) {
            tb.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tb.setRowHeight(20);
        tb.getColumnModel().getColumn(1).setCellRenderer(getDefaultTableRenderer());

        return tb;
    }

    private JTable getJTable(String[] columnNames) {
        Object[][] data = {
                {"**ResourceType Settings**", ""},
                {"Resource Types", "1"},
                {"Min Resources", "0"},
                {"Max Resources", "1000"},

                {"**Consumer Settings**", ""},
                {"Consumers", "2"},

                {"**Producer Settings**", ""},
                {"Producers", "15"},

                {"**Delays**", ""},
                {"Min Start Delay (s)", "106"},
                {"Max Start Delay (s)", "365"},
                {"Min Consumer Delay (s)", "1"},
                {"Max Consumer Delay (s)", "15"},
                {"Min Producer Delay (s)", "1"},
                {"Max Producer Delay (s)", "15"},

                {"Utilize Synchronized", useSynchronizedCheckBox},
                {"Utilize Min and Max Resources", useLimitsCheckBox}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column != 1) {
                    return false;
                }
                Object headerValue = getValueAt(row, 0);
                if (headerValue instanceof String && ((String) headerValue).contains("**")) {
                    return false;
                }
                return true;
            }
        };

        JTable tb = new JTable(model) {
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 1) {
                    Object value = getValueAt(row, column);
                    if (value instanceof JCheckBox) {
                        return new CheckBoxCellEditor();
                    } else {
                        return new DefaultCellEditor(new JTextField());
                    }
                }
                return super.getCellEditor(row, column);
            }
        };
        tb.setPreferredScrollableViewportSize(new Dimension(300, 200));
        return tb;
    }

    private DefaultTableCellRenderer getDefaultTableRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                if (value instanceof JCheckBox cb) {
                    cb.setHorizontalAlignment(SwingConstants.CENTER);
                    if (isSelected) {
                        cb.setBackground(table.getSelectionBackground());
                        cb.setForeground(table.getSelectionForeground());
                    } else {
                        cb.setBackground(table.getBackground());
                        cb.setForeground(table.getForeground());
                    }
                    return cb;
                }

                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (comp instanceof JLabel jlabel) {
                    jlabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
                return comp;
            }
        };
    }
}