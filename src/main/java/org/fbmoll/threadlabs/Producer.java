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

    public Producer(Model model, String name) {
        this.name = name;
        this.model = model;
    }

    private void produce() {
        model.getResources().addResource();
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            produce();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.state = State.ENDED;
    }
}