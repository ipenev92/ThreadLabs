package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Consumer implements Runnable {
    final String name;
    final Model model;
    State state;

    public Consumer(Model model, String name) {
        this.name = name;
        this.model = model;
    }

    private void consume() {
        model.getResources().removeResource();
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            consume();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.state = State.ENDED;
    }
}