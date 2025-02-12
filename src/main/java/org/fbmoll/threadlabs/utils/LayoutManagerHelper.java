package org.fbmoll.threadlabs.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.components.ConfigurationTable;
import org.fbmoll.threadlabs.components.ControlPanel;
import org.fbmoll.threadlabs.components.StatisticsTable;
import org.fbmoll.threadlabs.components.View;
import org.fbmoll.threadlabs.dto.ConfigurationDTO;
import org.fbmoll.threadlabs.objects.Consumer;
import org.fbmoll.threadlabs.objects.Producer;
import org.fbmoll.threadlabs.objects.ResourceType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LayoutManagerHelper {
    final View view;
    final ControlPanel controlPanel;
    final ConfigurationTable configurationTable;
    final StatisticsTable statisticsTable;
    final JScrollPane consumerTable;
    final JScrollPane producerTable;
    final JScrollPane resourcesTable;
    ConfigurationDTO configuration;

    public LayoutManagerHelper(View view, ControlPanel controlPanel) {
        this.view = view;
        this.controlPanel = controlPanel;
        this.configuration = ConfigurationDTO.empty();

        this.configurationTable = new ConfigurationTable(view.getController());
        this.statisticsTable = new StatisticsTable(view.getController(), this.configuration);
        this.consumerTable = createTable("Consumer Data");
        this.producerTable = createTable("Producer Data");
        this.resourcesTable = createTable("Resources Data");

        this.configureLayout();
    }

    private void configureLayout() {
        GridBagConstraints c = new GridBagConstraints();
        this.view.setLayout(new GridBagLayout());

        JPanel leftPanel = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.4;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        this.view.add(leftPanel, c);

        GridBagConstraints leftC = new GridBagConstraints();
        leftC.fill = GridBagConstraints.BOTH;
        leftC.insets = new Insets(5, 5, 5, 5);

        leftC.gridx = 0;
        leftC.gridy = 0;
        leftC.weightx = 0.5;
        leftC.weighty = 0.8;
        leftPanel.add(new JScrollPane(this.configurationTable.getTable()), leftC);

        leftC.gridx = 1;
        leftC.weightx = 0.5;
        leftPanel.add(new JScrollPane(this.statisticsTable.getTable()), leftC);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        leftC.gridx = 0;
        leftC.gridy = 1;
        leftC.gridwidth = 2;
        leftC.weighty = 0.2;

        GridBagConstraints buttonC = new GridBagConstraints();
        buttonC.fill = GridBagConstraints.BOTH;
        buttonC.insets = new Insets(5, 5, 5, 5);

        buttonC.gridx = 0;
        buttonC.weightx = 0.5;
        buttonPanel.add(this.controlPanel.getButtonPlay(), buttonC);

        buttonC.gridx = 1;
        buttonPanel.add(this.controlPanel.getButtonStop(), buttonC);

        leftPanel.add(buttonPanel, leftC);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        c.gridx = 1;
        c.weightx = 0.6;
        this.view.add(rightPanel, c);

        GridBagConstraints rightC = new GridBagConstraints();
        rightC.fill = GridBagConstraints.BOTH;
        rightC.insets = new Insets(5, 5, 5, 5);
        rightC.gridx = 0;
        rightC.weightx = 1.0;
        rightC.weighty = 0.33;

        rightC.gridy = 0;
        rightPanel.add(this.resourcesTable, rightC);

        rightC.gridy = 1;
        rightPanel.add(this.producerTable, rightC);

        rightC.gridy = 2;
        rightPanel.add(this.consumerTable, rightC);
    }

    public void clearTables() {
        DefaultTableModel consumerModel = (DefaultTableModel) ((JTable) this.consumerTable
                .getViewport().getView()).getModel();
        consumerModel.setRowCount(0);

        DefaultTableModel producerModel = (DefaultTableModel) ((JTable) this.producerTable
                .getViewport().getView()).getModel();
        producerModel.setRowCount(0);

        DefaultTableModel resourceModel = (DefaultTableModel) ((JTable) this.resourcesTable
                .getViewport().getView()).getModel();
        resourceModel.setRowCount(0);

        DefaultTableModel statsModel = (DefaultTableModel) statisticsTable.getTable().getModel();
        for (int i = 0; i < statsModel.getRowCount(); i++) {
            statsModel.setValueAt(0, i, 1);
        }

        this.view.repaint();
        this.view.revalidate();
    }

    private JScrollPane createTable(String title) {
        DefaultTableModel model = getDefaultTableModel(title);
        JTable table = createStyledTable(model, title);
        centerTableCells(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16),
                Color.BLACK
        ));
        return scrollPane;
    }

    private static DefaultTableModel getDefaultTableModel(String title) {
        String[] columnNames;
        if (Objects.equals(title, "Resources Data")) {
            columnNames = new String[]{
                    "Resource ID", "Quantity", "Min Qtty", "Max Qtty", "Consumers", "Producers", "Overflow",
                    "Underflow", "Status"
            };
        } else if (Objects.equals(title, "Producer Data")) {
            columnNames = new String[]{
                    "Producer ID", "Res. Bound", "Status", "Produced", "Start Delay", "Produce Delay"
            };
        } else {
            columnNames = new String[]{
                    "Consumer ID", "Res. Bound", "Status", "Consumed", "Start Delay", "Consume Delay"
            };
        }

        return new DefaultTableModel(null, columnNames);
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

    public void updateTables() {
        SwingUtilities.invokeLater(() -> {
            updateConsumer();
            updateProducer();
            updateResource();

            this.getStatisticsTable().updateStatistics();
        });
    }

    private void updateConsumer() {
        JTable table = (JTable) this.consumerTable.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
        for (Consumer consumer : this.configuration.getConsumers()) {
            if (consumer.getStatus() != Status.DELAYED) {
                model.addRow(new Object[]{
                        consumer.getId(), consumer.getResourceType().getId(), consumer.getStatus(),
                        consumer.getQuantityConsumed(), consumer.getStartDelay(), consumer.getConsumeDelay()
                });
            }
        }
    }

    private void updateProducer() {
        JTable table = (JTable) this.producerTable.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
        for (Producer producer : this.configuration.getProducers()) {
            if (producer.getStatus() != Status.DELAYED) {
                model.addRow(new Object[]{
                        producer.getId(), producer.getResourceType().getId(), producer.getStatus(),
                        producer.getQuantityProduced(), producer.getStartDelay(), producer.getProduceDelay()
                });
            }
        }
    }

    private void updateResource() {
        JTable table = (JTable) this.resourcesTable.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
        ArrayList<String> addedResource = new ArrayList<>();
        for (ResourceType resource : this.configuration.getResourceTypes()) {
            if (!addedResource.contains(resource.getId())) {
                model.addRow(new Object[]{
                        resource.getId(), resource.getQuantity(),
                        this.configuration.getRunConfigurationDTO().isUseLimits() ? resource.getMinQuantity() : "-",
                        this.configuration.getRunConfigurationDTO().isUseLimits() ? resource.getMaxQuantity() : "-",
                        countConsumers(resource), countProducers(resource),
                        resource.getOverflow(), resource.getUnderflow(),
                        (resource.getOverflow() > 0 || resource.getUnderflow() > 0) ? Status.BAD : Status.GOOD
                });
                addedResource.add(resource.getId());
            }
        }
    }

    private int countConsumers(ResourceType resource) {
        int consumers = 0;
        for (Consumer consumer : this.configuration.getConsumers()) {
            if (consumer.getResourceType() == resource) {
                consumers++;
            }
        }

        return consumers;
    }

    private int countProducers(ResourceType resource) {
        int consumers = 0;
        for (Producer producer : this.configuration.getProducers()) {
            if (producer.getResourceType() == resource) {
                consumers++;
            }
        }

        return consumers;
    }
}