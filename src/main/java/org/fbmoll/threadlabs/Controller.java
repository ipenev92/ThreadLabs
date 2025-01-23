package org.fbmoll.threadlabs;

public class Controller {
    Model model;

    public Controller() {
        model = new Model();
        new View(this);
    }

    public void play() {
        this.model.play();
    }

    public void stop() {
        this.model.stop();
    }
}