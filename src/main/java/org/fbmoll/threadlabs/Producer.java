package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Producer implements Runnable {
    final Model model;
    Status status;
    final String id;
    final ResourceType resourceType;
    final Thread thread;
    int quantityProduced;
    int startDelay;
    int produceDelay;
    int processingTime;

    public Producer(Model model, String id, ResourceType resourceType, int startDelay, int produceDelay) {
        this.id = id;
        this.model = model;
        this.resourceType = resourceType;
        this.thread = new Thread(this);
        this.quantityProduced = 0;
        this.startDelay = startDelay;
        this.produceDelay = produceDelay;
    }

    private void produce() {
        synchronized (resourceType) {
            while (this.resourceType.getQuantity() >= this.resourceType.getMaxQuantity()) {
                try {
                    this.status = Status.IDLE; // 🔄 Set producer to IDLE when waiting
                    System.out.println(id + " is IDLE (waiting to produce). Total produced: " + this.quantityProduced);

                    // ✅ Attempt to trigger overflow
                    this.resourceType.addResource();

                    resourceType.wait(); // WAIT for consumer to consume resources
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // ✅ Always check after being notified
            this.status = Status.RUNNING; // 🔄 Set back to RUNNING
            this.resourceType.addResource();
            this.quantityProduced++;
            System.out.println(id + " PRODUCED 1 resource. Total produced: " + this.quantityProduced);
            resourceType.notifyAll(); // Wake up waiting consumers and producers
        }
    }


    @Override
    public void run() {
        try {
            System.out.println(id + " Producer sleeping for startDelay: " + startDelay);
            Thread.sleep(startDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        this.status = Status.RUNNING;
        System.out.println(id + " Producer STARTED.");

        while (this.status == Status.RUNNING) {
            this.produce();
            try {
                Thread.sleep(produceDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        this.status = Status.ENDED;
        System.out.println(id + " Producer ENDED.");
    }
}