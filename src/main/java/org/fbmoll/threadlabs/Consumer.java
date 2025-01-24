package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Consumer implements Runnable {
    final Model model;
    State state;
    final String name;
    final Resources resources;

    public Consumer(Model model, Resources resources, String name) {
        this.model = model;
        this.name = name;
        this.resources = resources;
    }

    private void consume() {
        this.resources.removeResource();
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            this.consume();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.state = State.ENDED;
    }
}