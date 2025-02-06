package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LayoutManagerHelper {
    final View view;
    final ControlPanel controlPanel;
    ConfigurationDTO configuration;

    JTable configurationTable;
    JTable statisticsTable;
    JScrollPane consumerTable;
    JScrollPane producerTable;
    JScrollPane resourcesTable;

    JCheckBox useSynchronizedCheckBox;
    JCheckBox useLimitsCheckBox;

    public LayoutManagerHelper(View view, ControlPanel controlPanel) {
        this.view = view;
        this.controlPanel = controlPanel;
        this.configuration = ConfigurationDTO.empty();

        this.configurationTable = createConfigurationTable();
        this.statisticsTable = createStatisticsTable();
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
        leftPanel.add(new JScrollPane(this.configurationTable), leftC);

        leftC.gridx = 1;
        leftC.weightx = 0.5;
        leftPanel.add(new JScrollPane(this.statisticsTable), leftC);

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

    private JTable createConfigurationTable() {
        String[] columnNames = {"Parameter", "Value"};

        useSynchronizedCheckBox = new JCheckBox();
        useSynchronizedCheckBox.setSelected(true);

        useLimitsCheckBox = new JCheckBox();
        useLimitsCheckBox.setSelected(true);

        JTable table = getJTable(columnNames);

        DefaultTableCellRenderer checkBoxRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                if ((row == 15 || row == 16) && value instanceof JCheckBox cb) {
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
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };

        table.getColumnModel().getColumn(1).setCellRenderer(checkBoxRenderer);
        table.getColumnModel().getColumn(1).setCellEditor(new CheckBoxCellEditor());

        return table;
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

                {"**Delays**"},
                {"Min Start Delay (s)", "106"},
                {"Max Start Delay (s)", "365"},
                {"Min Consumer Delay (s)", "1"},
                {"Max Consumer Delay (s)", "15"},
                {"Min Producer Delay (s)", "1"},
                {"Max Producer Delay (s)", "15"},

                // Only last two rows have checkboxes (rows 15 and 16)
                {"Utilize Synchronized", useSynchronizedCheckBox},
                {"Utilize Min and Max Resources", useLimitsCheckBox}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow editing only for the checkbox cells (assuming rows 15 and 16)
                return column == 1 && (row == 15 || row == 16);
            }
        };

        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(300, 200));
        return table;
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
        table.setPreferredScrollableViewportSize(new Dimension(300, 200)); // ✅ Keep tables equal width

        return table;
    }

    private void centerTableCells(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
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
            columnNames = new String[] {
                    "Resource ID", "Quantity", "Min Qtty", "Max Qtty", "Consumers", "Producers", "Overflow", "Underflow"
            };
        } else if (Objects.equals(title, "Producer Data")) {
            columnNames = new String[] {
                    "Producer ID", "Res. Bound", "Status", "Produced", "Start Delay", "Produce Delay"
            };
        } else {
            columnNames = new String[] {
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

    public void updateTables() {
        SwingUtilities.invokeLater(() -> {
            updateConsumer();
            updateProducer();
            updateResource();

            updateStatistics();
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

    private void updateStatistics() {
        SwingUtilities.invokeLater(() -> {
            JTable table = statisticsTable;
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            model.setValueAt(configuration.getResourceTypes().stream()
                    .mapToInt(ResourceType::getQuantity).sum(), 0, 1); // Total Resources
            model.setValueAt(configuration.getProducerCount(), 1, 1); // Total Producers
            model.setValueAt(configuration.getConsumerCount(), 2, 1); // Total Consumers
            model.setValueAt(getTotalResources(), 3, 1); // Total Resource Quantity
            model.setValueAt(getActiveThreadsCount(), 4, 1); // Active Threads
            model.setValueAt(getIdleThreadsCount(), 5, 1); // Idle Threads
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

    public void clearTables() {
        System.out.println("Clearing tables...");

        DefaultTableModel consumerModel = (DefaultTableModel) ((JTable) this.consumerTable.getViewport().getView()).getModel();
        consumerModel.setRowCount(0);

        DefaultTableModel producerModel = (DefaultTableModel) ((JTable) this.producerTable.getViewport().getView()).getModel();
        producerModel.setRowCount(0);

        DefaultTableModel resourceModel = (DefaultTableModel) ((JTable) this.resourcesTable.getViewport().getView()).getModel();
        resourceModel.setRowCount(0);

        DefaultTableModel statsModel = (DefaultTableModel) statisticsTable.getModel();
        for (int i = 0; i < statsModel.getRowCount(); i++) {
            statsModel.setValueAt(0, i, 1);
        }

        this.view.repaint();
    }
}