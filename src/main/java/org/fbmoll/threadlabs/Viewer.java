package org.fbmoll.threadlabs;

import lombok.Getter;

import java.awt.*;

@Getter
public class Viewer extends Canvas {
    public Viewer() {
        this.setBackground(Color.BLUE);
    }

    public void changeColor(Color color) {
        this.setBackground(color);
        this.repaint();
    }
}