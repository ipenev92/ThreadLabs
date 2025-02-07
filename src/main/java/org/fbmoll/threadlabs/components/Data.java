package org.fbmoll.threadlabs.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Data {
    final DefaultTableModel model;
    final JScrollPane scrollPane;
    final JTable table;

    public Data() {
        String[] columnNames = {"Header", "Data"};
        Object[][] tableData = {
                {"col1", "a"},
                {"col2", "b"},
                {"col3", "c"}
        };

        this.model = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.table = new JTable(model);
        this.scrollPane = new JScrollPane(table);
    }
}