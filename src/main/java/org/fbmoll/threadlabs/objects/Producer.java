package org.fbmoll.threadlabs.objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.components.Model;
import org.fbmoll.threadlabs.utils.Status;

import java.util.Random;

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
    final Random random;

    public Producer(Model model, String id, ResourceType resourceType, int startDelay, int produceDelay) {
        this.id = id;
        this.model = model;
        this.resourceType = resourceType;
        this.thread = new Thread(this);
        this.quantityProduced = 0;
        this.startDelay = startDelay;
        this.produceDelay = produceDelay;
        this.random = new Random();
    }

    // Modified method: added a short delay inside the busy loop to reduce rapid overflow increments.
    private void produce() {
        while (this.resourceType.getQuantity() >= this.resourceType.getMaxQuantity()) {
            this.status = Status.IDLE;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.status = Status.RUNNING;
        this.resourceType.addResource();
        this.quantityProduced++;
    }

    private void produceOne() {
        while (this.resourceType.getQuantity() >= this.resourceType.getMaxQuantity()) {
            this.status = Status.IDLE;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
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

        if (this.model.getConfiguration().getRunConfigurationDTO().isUseLifeCycle()) {
            int count = generateNumber(
                    this.model.getConfiguration().getRunConfigurationDTO().getLifeCycleMin(),
                    this.model.getConfiguration().getRunConfigurationDTO().getLifeCycleMax()
            );
            for (int i = 0; i < count && this.status == Status.RUNNING; i++) {
                produceOne();
                try {
                    Thread.sleep(produceDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } else {
            while (this.status == Status.RUNNING) {
                produce();
                try {
                    Thread.sleep(produceDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        this.status = Status.ENDED;
    }

    private int generateNumber(int x, int y) {
        return this.random.nextInt(y - x + 1) + x;
    }
}
