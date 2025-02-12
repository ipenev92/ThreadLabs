package org.fbmoll.threadlabs.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.utils.CheckBoxCellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationTable extends JTable {
    final Controller controller;
    final JTable table;
    JCheckBox useSynchronizedCheckBox;
    JCheckBox useLimitsCheckBox;
    JCheckBox useLifecycle;

    public ConfigurationTable(Controller controller) {
        this.controller = controller;
        this.table = createConfigurationTable();
    }

    private JTable createConfigurationTable() {
        String[] columnNames = {"Parameter", "Value"};

        this.useSynchronizedCheckBox = new JCheckBox();
        this.useSynchronizedCheckBox.setSelected(true);
        this.useLimitsCheckBox = new JCheckBox();
        this.useLimitsCheckBox.setSelected(true);
        this.useLifecycle = new JCheckBox();
        this.useLifecycle.setSelected(false);

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
                {"Min Consumer Delay (s)", "1"},
                {"Max Consumer Delay (s)", "15"},

                {"**Producer Settings**", ""},
                {"Producers", "15"},
                {"Min Producer Delay (s)", "1"},
                {"Max Producer Delay (s)", "15"},

                {"**Producer/Consumer Lifecycle**", ""},
                {"Lifecycle Enabled", this.useLifecycle},
                {"Lifecycle Min Count", "10000"},
                {"Lifecycle Max Count", "10000"},

                {"**Extra Configuration**", ""},
                {"Min Start Delay (s)", "106"},
                {"Max Start Delay (s)", "365"},
                {"Synchronize", this.useSynchronizedCheckBox},
                {"Stock Control", this.useLimitsCheckBox}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column != 1) {
                    return false;
                }
                Object headerValue = getValueAt(row, 0);
                return !(headerValue instanceof String) || !((String) headerValue).contains("**");
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

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    if (row == 0 || row == 4 || row == 8 || row == 12 || row == 16) {
                        comp.setBackground(new Color(173, 216, 230)); // light blue
                    } else {
                        comp.setBackground(getBackground());
                    }
                }
                return comp;
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

                // If the value is a String and contains "**", set a light blue background.
                if (value instanceof String && ((String) value).contains("**")) {
                    comp.setBackground(new Color(173, 216, 230)); // light blue
                } else {
                    // Reset the background based on selection state
                    comp.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }

                // Center the text if the component is a JLabel
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
                }

                return comp;
            }
        };
    }
}