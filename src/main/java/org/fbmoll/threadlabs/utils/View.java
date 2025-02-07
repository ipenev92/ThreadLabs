package org.fbmoll.threadlabs.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.objects.Controller;
import org.fbmoll.threadlabs.components.ConfigurationTable;
import org.fbmoll.threadlabs.components.ControlPanel;
import org.fbmoll.threadlabs.components.DataPanel;
import org.fbmoll.threadlabs.dto.ConfigurationDTO;
import org.fbmoll.threadlabs.dto.ConsumerDTO;
import org.fbmoll.threadlabs.dto.ProducerDTO;
import org.fbmoll.threadlabs.dto.ResourceTypeDTO;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class View extends JFrame implements ActionListener {
    final ConfigurationTable configurationPanel;
    final Controller controller;
    final ControlPanel controlPanel;
    final DataPanel dataPanel;
    final LayoutManagerHelper layoutManagerHelper;
    ConfigurationDTO configuration;
    Thread viewThread;

    public View(Controller controller) {
        this.setSize(1500, 800);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Thread Labs");
        this.setLayout(new GridBagLayout());

        this.controller = controller;
        this.configurationPanel = new ConfigurationTable(controller);
        this.controlPanel = new ControlPanel();
        this.dataPanel = new DataPanel();
        this.layoutManagerHelper = new LayoutManagerHelper(this, this.controlPanel);
        this.addButtonListener();

        this.setVisible(true);
    }

    private void addButtonListener() {
        this.controlPanel.getButtonPlay().addActionListener(this);
        this.controlPanel.getButtonStop().addActionListener(this);
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.controlPanel.getButtonPlay()) {
            TableModel tableModel = this.layoutManagerHelper.getConfigurationTable().getTable().getModel();

            ConsumerDTO consumer = new ConsumerDTO(
                Integer.parseInt(tableModel.getValueAt(5, 1).toString()),
                Integer.parseInt(tableModel.getValueAt(11, 1).toString()),
                Integer.parseInt(tableModel.getValueAt(12, 1).toString())
            );
            ProducerDTO producer = new ProducerDTO(
                    Integer.parseInt(tableModel.getValueAt(7, 1).toString()),
                    Integer.parseInt(tableModel.getValueAt(13, 1).toString()),
                    Integer.parseInt(tableModel.getValueAt(14, 1).toString())
            );
            ResourceTypeDTO resourceType = new ResourceTypeDTO(
                    Integer.parseInt(tableModel.getValueAt(1, 1).toString()),
                    Integer.parseInt(tableModel.getValueAt(2, 1).toString()),
                    Integer.parseInt(tableModel.getValueAt(3, 1).toString()),
                    Integer.parseInt(tableModel.getValueAt(9, 1).toString()),
                    Integer.parseInt(tableModel.getValueAt(10, 1).toString())
            );
            this.configuration = new ConfigurationDTO(
                    this.controller.getModel(),
                    resourceType, consumer, producer,
                    this.layoutManagerHelper.getConfigurationTable().getUseSynchronizedCheckBox().isSelected(),
                    this.layoutManagerHelper.getConfigurationTable().getUseLimitsCheckBox().isSelected()
            );
            this.controller.play(this.configuration);

            this.layoutManagerHelper.setConfiguration(this.configuration);
            this.layoutManagerHelper.updateTables();
            this.layoutManagerHelper.getStatisticsTable().updateStatistics();
            this.layoutManagerHelper.clearTables();
            this.initializeThread();
        } else if (e.getSource() == this.controlPanel.getButtonStop()) {
            this.controller.stop();
            this.layoutManagerHelper.clearTables();
        }
    }

    private void initializeThread() {
        if (this.viewThread != null && this.viewThread.isAlive()) {
            return;
        }

        this.viewThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SwingUtilities.invokeLater(this.layoutManagerHelper::updateTables);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        this.viewThread.start();
    }
}