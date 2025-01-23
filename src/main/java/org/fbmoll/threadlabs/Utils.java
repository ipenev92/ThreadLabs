package org.fbmoll.threadlabs;

import javax.swing.*;
import java.awt.*;

public class Utils {
    public static void setGBLConstraints(View view, DataPanel dataPanel, Viewer viewer, ControlPanel controlPanel) {
        Utils.setGBLConstraintsDataPanel(view, dataPanel);
        Utils.setGBLConstraintsViewer(view, viewer);
        Utils.setGBLConstraintsPlay(view, controlPanel.getButtonPlay());
        Utils.setGBLConstraintsStop(view, controlPanel.getButtonStop());
        Utils.setGBLConstraintsEmpty(view);
    }

    private static void setGBLConstraintsDataPanel(View view, DataPanel dataPanel) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 1.0;
        view.add(dataPanel, c);
    }

    private static void setGBLConstraintsViewer(View view, Viewer viewer) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.9;
        c.weighty = 1.0;
        view.add(viewer, c);
    }

    private static void setGBLConstraintsPlay(View view, Button button) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        view.add(button, c);
    }

    private static void setGBLConstraintsStop(View view, Button button) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        view.add(button, c);
    }

    private static void setGBLConstraintsEmpty(View view) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.EAST;
        view.add(new JLabel(), c);
    }
}
