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
    final ResourceType resourceType;
    final Thread thread;
    int quantityProduced;

    public Producer(Model model, String name, ResourceType resourceType) {
        this.name = name;
        this.model = model;
        this.resourceType = resourceType;
        this.thread = new Thread(this);
        this.quantityProduced = 0;
    }

    private void produce() {
        synchronized (resourceType) {
            while (this.resourceType.getQuantity() == 1000) {
                try {
                    System.out.println(name + " is waiting for space...");
                    resourceType.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            this.resourceType.addResource();
            this.quantityProduced++;
            System.out.println(name + " produced 1 resource. Total produced: " + this.quantityProduced);
        }
    }

    @Override
    public void run() {
        while (this.state == State.RUNNING) {
            this.produce();
        }

        this.state = State.ENDED;
    }
}