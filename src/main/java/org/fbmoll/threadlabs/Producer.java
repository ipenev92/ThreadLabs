package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Producer implements Runnable {
    final String name;
    final Model model;
    State state;
    final Resources resources;

    public Producer(Model model, Resources resources, String name) {
        this.name = name;
        this.model = model;
        this.resources = resources;
    }

    private void produce() {
        this.resources.addResource();
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            this.produce();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.state = State.ENDED;
    }
}