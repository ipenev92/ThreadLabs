package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class View extends JFrame implements ActionListener {
    final ConfigurationPanel configurationPanel;
    final Controller controller;
    final ControlPanel controlPanel;
    final DataPanel dataPanel;
    final JPanel jConfigurationpanelJ;
    final JTable consumerTable;
    final JTable producerTable;
    final JTable resourcesTable;
    final JPanel statisticsPanel;

    public View(Controller controller) {
        this.controller = controller;
        this.configurationPanel = new ConfigurationPanel(controller);
        this.controlPanel = new ControlPanel();
        this.dataPanel = new DataPanel();

        this.jConfigurationpanelJ = createConfigurationPanel();
        this.consumerTable = createPlaceholderTable("Consumer Data");
        this.producerTable = createPlaceholderTable("Producer Data");
        this.resourcesTable = createPlaceholderTable("Resources Data");
        this.statisticsPanel = createPlaceholderPanel("Statistics Panel");

        this.configureLayout();
        this.addButtonListener();

        this.setVisible(true);
    }

    private void configureLayout() {
        this.setSize(1200, 700);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Grid bag layout");
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        // Left-most panel (35% width)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.35;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        this.add(leftPanel, c);

        // Inside left-most panel
        GridBagConstraints leftC = new GridBagConstraints();
        leftC.fill = GridBagConstraints.BOTH;

        // Top-left (ConfigurationPanel)
        leftC.gridx = 0;
        leftC.gridy = 0;
        leftC.weightx = 0.4;
        leftC.weighty = 0.5;
        leftPanel.add(jConfigurationpanelJ, leftC);

        // Top-right (Statistics)
        leftC.gridx = 1;
        leftC.gridy = 0;
        leftC.weightx = 0.6;
        leftC.weighty = 0.5;
        leftPanel.add(statisticsPanel, leftC);

        // Bottom-left (Play Button)
        leftC.gridx = 0;
        leftC.gridy = 1;
        leftC.weightx = 0.5;
        leftC.weighty = 0.2;
        leftPanel.add(controlPanel.getButtonPlay(), leftC);

        // Bottom-right (Stop Button)
        leftC.gridx = 1;
        leftC.gridy = 1;
        leftC.weightx = 0.5;
        leftC.weighty = 0.2;
        leftPanel.add(controlPanel.getButtonStop(), leftC);

        // Consumer table (20% width)
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.20;
        c.weighty = 1.0;
        JScrollPane consumerScrollPane = new JScrollPane(consumerTable);
        this.add(consumerScrollPane, c);

        // Producer table (20 width)
        c.gridx = 2;
        c.weightx = 0.20;
        JScrollPane producerScrollPane = new JScrollPane(producerTable);
        this.add(producerScrollPane, c);

        // Resources table (23% width)
        c.gridx = 3;
        c.weightx = 0.20;
        JScrollPane resourcesScrollPane = new JScrollPane(resourcesTable);
        this.add(resourcesScrollPane, c);
    }

    private void addButtonListener() {
        this.controlPanel.getButtonPlay().addActionListener(this);
        this.controlPanel.getButtonStop().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.controlPanel.getButtonPlay()) {
            this.controller.play();
        } else if (e.getSource() == this.controlPanel.getButtonStop()) {
            this.controller.stop();
        }
    }

    private JTable createPlaceholderTable(String title) {
        String[] columnNames = {"Column 1", "Column 2", "Column 3"};
        Object[][] data = {
                {title + " 1", title + " 2", title + " 3"},
                {"", "", ""},
                {"", "", ""}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        return new JTable(model);
    }

    private JPanel createConfigurationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Configuration"));

        GridBagConstraints configC = new GridBagConstraints();
        configC.insets = new Insets(5, 5, 5, 5);
        configC.fill = GridBagConstraints.HORIZONTAL;

        configC.gridx = 0;
        configC.gridy = 0;
        panel.add(new JLabel("Quantity:"), configC);
        configC.gridx = 1;
        panel.add(new JTextField(10), configC);

        configC.gridx = 0;
        configC.gridy = 1;
        panel.add(new JLabel("Max Quantity:"), configC);
        configC.gridx = 1;
        panel.add(new JTextField(10), configC);

        configC.gridx = 0;
        configC.gridy = 2;
        panel.add(new JLabel("Min Quantity:"), configC);
        configC.gridx = 1;
        panel.add(new JTextField(10), configC);

        configC.gridx = 0;
        configC.gridy = 3;
        panel.add(new JLabel("Consumers:"), configC);
        configC.gridx = 1;
        panel.add(new JTextField(10), configC);

        configC.gridx = 0;
        configC.gridy = 4;
        panel.add(new JLabel("Producers:"), configC);
        configC.gridx = 1;
        panel.add(new JTextField(10), configC);

        return panel;
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(text));
        return panel;
    }
}