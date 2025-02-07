package org.fbmoll.threadlabs.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.dto.ConfigurationDTO;
import org.fbmoll.threadlabs.objects.Consumer;
import org.fbmoll.threadlabs.objects.Producer;
import org.fbmoll.threadlabs.objects.ResourceType;
import org.fbmoll.threadlabs.utils.Status;
import org.fbmoll.threadlabs.utils.View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Viewer extends JPanel {
    final View view;
    final ConfigurationDTO configuration;

    final JScrollPane consumerTable;
    final JScrollPane producerTable;
    final JScrollPane resourcesTable;

    public Viewer(View view, ConfigurationDTO configuration) {
        this.view = view;
        this.configuration = configuration;

        this.consumerTable = createTable("Consumer Data");
        this.producerTable = createTable("Producer Data");
        this.resourcesTable = createTable("Resources Data");
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
                    "Resource ID", "Quantity", "Min Qtty", "Max Qtty", "Consumers", "Producers", "Overflow", "Underflow"
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
                        resource.getId(), resource.getQuantity(), resource.getMinQuantity(),
                        resource.getMaxQuantity(), countConsumers(resource), countProducers(resource),
                        resource.getOverflow(), resource.getUnderflow()
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