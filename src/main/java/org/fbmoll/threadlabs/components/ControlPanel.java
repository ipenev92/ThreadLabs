package org.fbmoll.threadlabs.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ControlPanel extends JPanel {
    final org.fbmoll.threadlabs.components.Button buttonPlay;
    final org.fbmoll.threadlabs.components.Button buttonStop;

    public ControlPanel() {
        this.buttonPlay = new org.fbmoll.threadlabs.components.Button("Play");
        this.buttonStop = new Button("Stop");

        this.setLayout(new GridBagLayout());
    }
}