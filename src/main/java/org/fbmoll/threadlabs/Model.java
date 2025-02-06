package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
    Random random;
    ScheduledExecutorService scheduler;

    public Model(Controller controller) {
        this.controller = controller;
        this.scheduler = Executors.newScheduledThreadPool(10);
    }

    public void play() {
        synchronized (this.lock) {
            if (scheduler.isShutdown() || scheduler.isTerminated()) {
                scheduler = Executors.newScheduledThreadPool(10);
            }

            System.out.println("Starting Consumers...");
            for (Consumer consumer : this.configuration.getConsumers()) {
                consumer.setStatus(Status.DELAYED);
                scheduler.schedule(() -> {
                    consumer.setStatus(Status.RUNNING);
                    consumer.getThread().start();
                    System.out.println("Consumer " + consumer.getId() + " started.");
                }, consumer.getStartDelay(), TimeUnit.MILLISECONDS);
            }

            System.out.println("Starting Producers...");
            for (Producer producer : this.configuration.getProducers()) {
                producer.setStatus(Status.DELAYED);
                scheduler.schedule(() -> {
                    producer.setStatus(Status.RUNNING);
                    producer.getThread().start();
                    System.out.println("Producer " + producer.getId() + " started.");
                }, producer.getStartDelay(), TimeUnit.MILLISECONDS);
            }
        }
    }



    public void stop() throws InterruptedException {
        for (Consumer consumer : this.configuration.getConsumers()) {
            if (consumer.getStatus() == Status.RUNNING) {
                consumer.setStatus(Status.INTERRUPTED);
                consumer.getThread().interrupt();
                consumer.getThread().join();
            }
        }

        for (Producer producer : this.configuration.getProducers()) {
            if (producer.getStatus() == Status.RUNNING) {
                producer.setStatus(Status.INTERRUPTED);
                producer.getThread().interrupt();
                producer.getThread().join();
            }
        }

        scheduler.shutdownNow();
        this.controller.getView().getViewThread().interrupt();
        this.controller.getView().getViewThread().join();
    }
}