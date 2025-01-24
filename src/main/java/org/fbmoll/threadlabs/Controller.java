package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Controller {
    final Model model;
    final View view;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);
    }

    public void play() {
        this.model.play();
    }

    public void stop() {
        this.model.stop();
    }
}