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
    Status status;
    final String id;
    final ResourceType resourceType;
    final Thread thread;
    int quantityConsumed;
    int startDelay;
    int consumeDelay;
    int processingTime;

    public Consumer(Model model, String id, ResourceType resourceType, int startDelay, int consumeDelay) {
        this.model = model;
        this.id = id;
        this.resourceType = resourceType;
        this.thread = new Thread(this);
        this.quantityConsumed = 0;
        this.startDelay = startDelay;
        this.consumeDelay = consumeDelay;
    }

    private void consume() {
        synchronized (resourceType) {
            while (this.resourceType.getQuantity() == 0) {
                try {
                    this.status = Status.IDLE; // 🔄 Set consumer to IDLE when waiting
                    System.out.println(id + " is IDLE (waiting for resources). Total consumed: " + this.quantityConsumed);

                    // ✅ Attempt to trigger underflow
                    this.resourceType.removeResource();

                    resourceType.wait(); // WAIT for producer to produce resources
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // ✅ Always check after being notified
            this.status = Status.RUNNING; // 🔄 Set back to RUNNING
            this.resourceType.removeResource();
            this.quantityConsumed++;
            System.out.println(id + " CONSUMED 1 resource. Total consumed: " + this.quantityConsumed);

            resourceType.notifyAll(); // Wake up waiting producers
        }
    }





    @Override
    public void run() {
        try {
            System.out.println(id + " Consumer sleeping for startDelay: " + startDelay);
            Thread.sleep(startDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        this.status = Status.RUNNING;
        System.out.println(id + " Consumer STARTED.");

        while (this.status == Status.RUNNING) {
            this.consume();
            try {
                Thread.sleep(consumeDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        this.status = Status.ENDED;
        System.out.println(id + " Consumer ENDED.");
    }


}