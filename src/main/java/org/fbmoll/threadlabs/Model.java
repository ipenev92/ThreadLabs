package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Model {
    final Object lock = new Object();
    ConfigurationDTO configuration;
    Controller controller;

    public Model(Controller controller) {
        this.controller = controller;
    }

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    public void play() {
        synchronized (this.lock) {
            if (scheduler.isShutdown() || scheduler.isTerminated()) {
                scheduler = Executors.newScheduledThreadPool(10);
            }

            int delay = this.configuration.isRandomDelay() ? this.configuration.getCreationDelay() : getRandomDelay();

            int currentDelay = 0;
            for (Consumer consumer : this.configuration.getConsumers()) {
                int finalDelay = currentDelay;
                consumer.setState(State.DELAYED);

                scheduler.schedule(() -> {
                    consumer.setState(State.RUNNING);
                    consumer.getThread().start();
                }, finalDelay, TimeUnit.MILLISECONDS);

                currentDelay += delay;
            }

            currentDelay = 0;
            for (Producer producer : this.configuration.getProducers()) {
                int finalDelay = currentDelay;
                producer.setState(State.DELAYED);

                scheduler.schedule(() -> {
                    producer.setState(State.RUNNING);
                    producer.getThread().start();
                }, finalDelay, TimeUnit.MILLISECONDS);

                currentDelay += delay;
            }
        }
    }

    public void stop() throws InterruptedException {
        for (Consumer consumer : this.configuration.getConsumers()) {
            if (consumer.getState() == State.RUNNING) {
                consumer.setState(State.INTERRUPTED);
                consumer.getThread().interrupt();
                consumer.getThread().join();
            }
        }

        for (Producer producer : this.configuration.getProducers()) {
            if (producer.getState() == State.RUNNING) {
                producer.setState(State.INTERRUPTED);
                producer.getThread().interrupt();
                producer.getThread().join();
            }
        }

        scheduler.shutdownNow();
        this.controller.getView().getViewThread().interrupt();
        this.controller.getView().getViewThread().join();
    }

    private int getRandomDelay() {
        Random random = new Random();
        int min = this.configuration.getMinCreationDelay();
        int max = this.configuration.getMaxCreationDelay();
        return (random.nextInt((max - min) + 1) + min) * 1000;
    }
}