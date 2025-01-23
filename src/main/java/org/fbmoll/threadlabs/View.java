package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class View extends JFrame implements ActionListener {
    final Controller controller;
    final ControlPanel controlPanel;
    final DataPanel dataPanel;
    final Viewer viewer;

    public View(Controller controller) {
        this.controller = controller;
        this.controlPanel = new ControlPanel();
        this.dataPanel = new DataPanel();
        this.viewer = new Viewer();

        configureLayout();
        addButtonListener();
        Utils.setGBLConstraints(this, dataPanel, viewer, controlPanel);

        this.setVisible(true);
    }

    private void configureLayout() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Grid bag layout");
        this.setLayout(new GridBagLayout());
    }

    private void addButtonListener() {
        controlPanel.getButtonPlay().addActionListener(this);
        controlPanel.getButtonStop().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == controlPanel.getButtonPlay()) {
            controller.play();
        } else if (e.getSource() == controlPanel.getButtonStop()) {
            controller.stop();
        }
    }
}