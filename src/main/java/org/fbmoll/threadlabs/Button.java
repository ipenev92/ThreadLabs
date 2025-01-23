package org.fbmoll.threadlabs;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class Button extends JButton {
    String text;

    public Button(String text) {
        super(text);

        this.text = text;

        this.setText(text);
        this.setFont(new Font("Arial", Font.BOLD, 30));
        this.setBackground(Color.CYAN);
        this.setPreferredSize(new Dimension(150, 150));
    }
}