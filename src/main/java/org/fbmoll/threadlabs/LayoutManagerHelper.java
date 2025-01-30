package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
    ConfigurationDTO configuration;

    final JPanel configurationPanel;
    final JPanel statisticsPanel;
    JScrollPane consumerTable;
    JScrollPane producerTable;
    JScrollPane resourcesTable;

    final JTextField consumerCountField = new JTextField("5", 4);
    final JTextField producerCountField = new JTextField("5", 4);
    final JTextField resourceTypesCountField = new JTextField("5", 4);
    final JTextField minResourcesCountField = new JTextField("0", 4);
    final JTextField maxResourcesCountField = new JTextField("1000", 4);
    final JTextField minCreationDelayField = new JTextField("2", 2);
    final JTextField maxCreationDelayField = new JTextField("5", 2);
    final JCheckBox fixedDelayCheckbox = new JCheckBox("Fixed Delay?");
    final JTextField fixedDelayField = new JTextField("0", 4);

    public LayoutManagerHelper(View view, ControlPanel controlPanel) {
        this.view = view;
        this.controlPanel = controlPanel;
        this.configuration = createEmptyConfiguration();

        this.configurationPanel = this.createConfigurationPanel();
        this.statisticsPanel = this.createStatisticsPanel();
        this.consumerTable = createTable("Consumer Data");
        this.producerTable = createTable("Producer Data");
        this.resourcesTable = createTable("Resources Data");

        this.fixedDelayField.setEnabled(false);
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
        leftPanel.add(this.configurationPanel, leftC);

        leftC.gridx = 1;
        leftC.weightx = 0.7;
        leftPanel.add(this.statisticsPanel, leftC);

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
        JScrollPane resourcesScrollPane = new JScrollPane(this.resourcesTable);
        rightPanel.add(resourcesScrollPane, rightC);

        rightC.gridx = 1;
        JScrollPane producerScrollPane = new JScrollPane(this.producerTable);
        rightPanel.add(producerScrollPane, rightC);

        rightC.gridx = 2;
        JScrollPane consumerScrollPane = new JScrollPane(this.consumerTable);
        rightPanel.add(consumerScrollPane, rightC);
    }

    private JPanel createConfigurationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Configuration",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16),
                Color.BLACK
        ));
        GridBagConstraints configC = new GridBagConstraints();
        configC.insets = new Insets(5, 5, 5, 5);
        configC.fill = GridBagConstraints.HORIZONTAL;

        configC.gridx = 0;
        configC.gridy = 0;
        panel.add(new JLabel("Consumers:"), configC);
        configC.gridx = 1;
        panel.add(consumerCountField, configC);

        configC.gridx = 0;
        configC.gridy = 1;
        panel.add(new JLabel("Producers:"), configC);
        configC.gridx = 1;
        panel.add(producerCountField, configC);

        configC.gridx = 0;
        configC.gridy = 2;
        panel.add(new JLabel("Resource Types:"), configC);
        configC.gridx = 1;
        panel.add(resourceTypesCountField, configC);

        configC.gridx = 0;
        configC.gridy = 3;
        panel.add(new JLabel("Min Resources:"), configC);
        configC.gridx = 1;
        panel.add(minResourcesCountField, configC);

        configC.gridx = 0;
        configC.gridy = 4;
        panel.add(new JLabel("Max Resources:"), configC);
        configC.gridx = 1;
        panel.add(maxResourcesCountField, configC);

        // **Creation Delay (Single Row)**
        configC.gridx = 0;
        configC.gridy = 5;
        panel.add(new JLabel("Creation Delay (s):"), configC);

        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Compact layout
        delayPanel.add(minCreationDelayField);
        delayPanel.add(new JLabel("-"));
        delayPanel.add(maxCreationDelayField);

        configC.gridx = 1;
        panel.add(delayPanel, configC);

        // **Fixed Delay Checkbox (Separate Row)**
        this.fixedDelayCheckbox.setSelected(false);
        this.fixedDelayCheckbox.addActionListener(e -> {
            boolean isFixedDelay = this.fixedDelayCheckbox.isSelected();

            this.fixedDelayField.setEnabled(isFixedDelay);
            this.minCreationDelayField.setEnabled(!isFixedDelay);
            this.maxCreationDelayField.setEnabled(!isFixedDelay);
        });

        // **Fixed Delay Field (Separate Row)**
        configC.gridx = 0;
        configC.gridy = 7;
        configC.gridwidth = 1;
        panel.add(new JLabel("Fixed Delay:"), configC);

        configC.gridx = 0;
        configC.gridy = 6;
        configC.gridwidth = 2; // Span both columns
        panel.add(fixedDelayCheckbox, configC);

        configC.gridx = 1;
        configC.gridy = 7;
        panel.add(this.fixedDelayField, configC);

        return panel;
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Statistics",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16),
                Color.BLACK
        ));
        return panel;
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
                model.addRow(new Object[] {
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
                model.addRow(new Object[] {
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
                model.addRow(new Object[] {
                        resource.getName(), resource.getQuantity()
                });
                addedResource.add(resource.getName());
            }
        }
    }

    private JScrollPane createTable(String title) {
        String[] columnNames = Objects.equals(title, "Resources Data") ? new String[]{"", ""} : new String[]{"", "", ""};
        Object[][] data = {};
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setTableHeader(null);

        table.setPreferredScrollableViewportSize(new Dimension(200, 120));

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(40);
        table.setRowHeight(20);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16),
                Color.BLACK
        ));

        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    private ConfigurationDTO createEmptyConfiguration() {
        return new ConfigurationDTO(this.view.getController().getModel(), 5, 5,
                5, 0, 1000, 2, 5,
                false,0);
    }
}