package org.fbmoll.threadlabs.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataPanel extends JPanel {
    final Data data;

    public DataPanel() {
        this.data = new Data();
        this.setLayout(new BorderLayout());
        this.add(data.getScrollPane(), BorderLayout.CENTER);
    }
}