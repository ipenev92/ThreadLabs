package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
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
    final LayoutManagerHelper layoutManagerHelper;
    ConfigurationDTO configuration;
    Thread viewThread;

    public View(Controller controller) {
        this.setSize(1400, 700);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Grid bag layout");
        this.setLayout(new GridBagLayout());

        this.controller = controller;
        this.configurationPanel = new ConfigurationPanel(controller);
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
            Object tewt = this.layoutManagerHelper.getConfigurationTable().getModel().getValueAt(0,1);
            System.out.println(tewt);
            this.controller.play(this.configuration);
            this.layoutManagerHelper.setConfiguration(this.configuration);
            this.layoutManagerHelper.updateTables();
            this.initializeThread();
        } else if (e.getSource() == this.controlPanel.getButtonStop()) {
            this.controller.stop();
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