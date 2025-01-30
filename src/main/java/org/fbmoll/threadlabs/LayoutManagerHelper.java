package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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
        leftC.weightx = 0.3;
        leftC.weighty = 0.8;
        leftPanel.add(new JScrollPane(this.configurationTable), leftC);

        leftC.gridx = 1;
        leftC.weightx = 0.7;
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
        rightC.gridy = 0;
        rightC.weightx = 0.33;
        rightC.weighty = 1.0;
        rightPanel.add(this.resourcesTable, rightC);

        rightC.gridx = 1;
        rightPanel.add(this.producerTable, rightC);

        rightC.gridx = 2;
        rightPanel.add(this.consumerTable, rightC);
    }

    private JTable createConfigurationTable() {
        String[] columnNames = {"Parameter", "Value"};
        Object[][] data = {
                {"**ResourceType Settings**", ""},
                {"Resource Types", "5"},
                {"Min Resources", "0"},
                {"Max Resources", "1000"},

                {"**Consumer Settings**", ""},
                {"Consumers", "5"},

                {"**Producer Settings**", ""},
                {"Producers", "5"},

                {"**Delays**", ""},
                {"Min Start Delay (s)", "2"},
                {"Max Start Delay (s)", "5"},
                {"Min Consumer Delay (s)", "2"},
                {"Max Consumer Delay (s)", "5"},
                {"Min Producer Delay (s)", "2"},
                {"Max Producer Delay (s)", "5"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the second column (Value) editable, but not headers
                return column == 1 && !getValueAt(row, 0).toString().startsWith("**");
            }
        };

        JTable table = createStyledTable(model, "Configuration");

        // Make the header column non-editable and bold
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(100);

        return table;
    }

    private JTable createStatisticsTable() {
        String[] columnNames = {"Statistic", "Value"};
        Object[][] data = {
                {"Total Resources", "0"},
                {"Consumed Resources", "0"},
                {"Produced Resources", "0"}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        return createStyledTable(model, "Statistics");
    }

    private JScrollPane createTable(String title) {
        String[] columnNames = Objects.equals(title, "Resources Data") ? new String[]{"", ""} : new String[]{"", "", ""};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        JTable table = createStyledTable(model, title);

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
            updateConsumer(this.consumerTable, this.configuration.getConsumers());
            updateProducer(this.producerTable, this.configuration.getProducers());
            updateResource(this.resourcesTable, this.configuration.getResourceTypes());
        });
    }

    public void updateConsumer(JScrollPane scrollPane, ArrayList<Consumer> list) {
        JTable table = (JTable) scrollPane.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
        for (Consumer consumer : list) {
            if (consumer.getState() == State.RUNNING) {
                model.addRow(new Object[]{
                        consumer.getName(), consumer.getQuantityConsumed(), consumer.getResourceType().getName()
                });
            }
        }
    }

    public void updateProducer(JScrollPane scrollPane, ArrayList<Producer> list) {
        JTable table = (JTable) scrollPane.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
        for (Producer producer : list) {
            if (producer.getState() == State.RUNNING) {
                model.addRow(new Object[]{
                        producer.getName(), producer.getQuantityProduced(), producer.getResourceType().getName()
                });
            }
        }
    }

    public void updateResource(JScrollPane scrollPane, ArrayList<ResourceType> list) {
        JTable table = (JTable) scrollPane.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
        ArrayList<String> addedResource = new ArrayList<>();
        for (ResourceType resource : list) {
            if (!addedResource.contains(resource.getName())) {
                model.addRow(new Object[]{
                        resource.getName(), resource.getQuantity()
                });
                addedResource.add(resource.getName());
            }
        }
    }
}