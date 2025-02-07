package org.fbmoll.threadlabs.objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.utils.Status;

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
        while (this.resourceType.getQuantity() >= this.resourceType.getMaxQuantity()) {
            this.status = Status.IDLE;
            this.resourceType.addResource();
        }

        this.status = Status.RUNNING;
        this.resourceType.addResource();
        this.quantityProduced++;
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
            this.produce();
            try {
                Thread.sleep(produceDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        this.status = Status.ENDED;
    }
}