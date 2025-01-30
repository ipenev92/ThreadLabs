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
    final ResourceType resourceType;
    final Thread thread;
    int quantityConsumed;

    public Consumer(Model model, String name, ResourceType resourceType) {
        this.model = model;
        this.name = name;
        this.resourceType = resourceType;
        this.thread = new Thread(this);
        this.quantityConsumed = 0;
    }

    private void consume() {
        synchronized (resourceType) {
            while (this.resourceType.getQuantity() == 0) {
                try {
                    System.out.println(name + " is waiting for resources...");
                    resourceType.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            this.resourceType.removeResource();
            this.quantityConsumed++;
            System.out.println(name + " consumed 1 resource. Total consumed: " + this.quantityConsumed);
        }
    }

    @Override
    public void run() {
        this.state = State.RUNNING;

        for (int i = 0; i < 1000; i++) {
            this.consume();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        this.state = State.ENDED;
    }
}