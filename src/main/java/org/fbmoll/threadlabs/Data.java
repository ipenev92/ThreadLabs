package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Data {
    final JTable table;
    final JScrollPane scrollPane;
    final DefaultTableModel model;

    public Data() {
        String[] columnNames = {"Header", "Data"};
        Object[][] tableData = {
                {"col1", "a"},
                {"col2", "b"},
                {"col3", "c"}
        };

        model = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        scrollPane = new JScrollPane(table);
    }
}