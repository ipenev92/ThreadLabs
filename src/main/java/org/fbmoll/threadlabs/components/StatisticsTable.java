package org.fbmoll.threadlabs.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.dto.ConfigurationDTO;
import org.fbmoll.threadlabs.objects.Controller;
import org.fbmoll.threadlabs.objects.ResourceType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsTable {
    final Controller controller;
    final ConfigurationDTO configuration;
    final JTable table;

    public StatisticsTable(Controller controller, ConfigurationDTO configuration) {
        this.controller = controller;
        this.configuration = configuration;
        this.table = createStatisticsTable();
    }

    private JTable createStatisticsTable() {
        String[] columnNames = {"Statistic", "Value"};
        Object[][] data = {
                {"Total Resources", "0"},
                {"Total Producers", "0"},
                {"Total Consumers", "0"},
                {"Total Resource Quantity", "0"},
                {"Active Threads", "0"},
                {"Idle Threads", "0"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = createStyledTable(model, "Statistics");
        centerTableCells(table);
        table.setPreferredScrollableViewportSize(new Dimension(300, 200));
        table.setRowHeight(20);
        return table;
    }

    private JTable createStyledTable(DefaultTableModel model, String title) {
        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(250, 150));
        table.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16),
                Color.BLACK
        ));
        return table;
    }

    private void centerTableCells(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void updateStatistics() {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = (DefaultTableModel) this.table.getModel();

            model.setValueAt(configuration.getResourceTypes().stream()
                    .mapToInt(ResourceType::getQuantity).sum(), 0, 1);
            model.setValueAt(configuration.getProducerDTO().producerCount(), 1, 1);
            model.setValueAt(configuration.getConsumerDTO().consumerCount(), 2, 1);
            model.setValueAt(getTotalResources(), 3, 1);
            model.setValueAt(getActiveThreadsCount(), 4, 1);
            model.setValueAt(getIdleThreadsCount(), 5, 1);
        });
    }

    private int getTotalResources() {
        int resources = 0;
        for (ResourceType resource : configuration.getResourceTypes()) {
            resources += resource.getQuantity();
        }

        return resources;
    }

    private int getActiveThreadsCount() {
        return (int) Thread.getAllStackTraces().keySet().stream()
                .filter(Thread::isAlive)
                .count();
    }

    private int getIdleThreadsCount() {
        return (int) Thread.getAllStackTraces().keySet().stream()
                .filter(t -> t.getState() == Thread.State.WAITING)
                .count();
    }
}
