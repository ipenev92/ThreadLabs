package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ControlPanel extends JPanel {
    final Button buttonPlay;
    final Button buttonStop;

    public ControlPanel() {
        buttonPlay = new Button("Play");
        buttonStop = new Button("Stop");

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
    }
}
