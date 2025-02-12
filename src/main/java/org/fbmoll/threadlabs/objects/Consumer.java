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
    final Random random;

    public Consumer(Model model, String id, ResourceType resourceType, int startDelay, int consumeDelay) {
        this.model = model;
        this.id = id;
        this.resourceType = resourceType;
        this.thread = new Thread(this);
        this.quantityConsumed = 0;
        this.startDelay = startDelay;
        this.consumeDelay = consumeDelay;
        this.random = new Random();
    }

    private void consume() {
        while (this.resourceType.getQuantity() == 0) {
            this.status = Status.IDLE;
            this.resourceType.removeResource();
        }
        this.status = Status.RUNNING;
        this.resourceType.removeResource();
        this.quantityConsumed++;
    }

    private void consumeOne() {
        while (this.resourceType.getQuantity() == 0) {
            this.status = Status.IDLE;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.status = Status.RUNNING;
        this.resourceType.removeResource();
        this.quantityConsumed++;
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
                consumeOne();
                try {
                    Thread.sleep(this.consumeDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } else {
             while (this.status == Status.RUNNING) {
                consume();
                try {
                    Thread.sleep(this.consumeDelay);
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