package org.fbmoll.threadlabs.objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.utils.Status;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Consumer implements Runnable {
    final Model model;
    final String id;
    final ResourceType resourceType;
    final Thread thread;
    Status status;
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
        if (model.getConfiguration().isUseSynchronized()) {
            synchronized (resourceType) {
                while (this.resourceType.getQuantity() == 0) {
                    try {
                        this.status = Status.IDLE;
                        this.resourceType.removeResource();
                        resourceType.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                this.status = Status.RUNNING;
                this.resourceType.removeResource();
                this.quantityConsumed++;
                resourceType.notifyAll();
            }
        } else {
            while (this.resourceType.getQuantity() == 0) {
                this.status = Status.IDLE;
                this.resourceType.removeResource();
            }

            this.status = Status.RUNNING;
            this.resourceType.removeResource();
            this.quantityConsumed++;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        this.status = Status.RUNNING;

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
    }
}