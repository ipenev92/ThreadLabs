package org.fbmoll.threadlabs.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.dto.ConfigurationDTO;
import org.fbmoll.threadlabs.objects.Consumer;
import org.fbmoll.threadlabs.objects.Producer;
import org.fbmoll.threadlabs.utils.Status;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Model {
    ConfigurationDTO configuration;
    Controller controller;
    Random random;
    ScheduledExecutorService scheduler;

    public Model(Controller controller) {
        this.controller = controller;
        this.scheduler = Executors.newScheduledThreadPool(10);
    }

    public void play() {
        if (scheduler.isShutdown() || scheduler.isTerminated()) {
            scheduler = Executors.newScheduledThreadPool(10);
        }

        for (Consumer consumer : this.configuration.getConsumers()) {
            consumer.setStatus(Status.DELAYED);
            scheduler.schedule(() -> {
                consumer.setStatus(Status.RUNNING);
                consumer.getThread().start();
            }, consumer.getStartDelay(), TimeUnit.MILLISECONDS);
        }

        for (Producer producer : this.configuration.getProducers()) {
            producer.setStatus(Status.DELAYED);
            scheduler.schedule(() -> {
                producer.setStatus(Status.RUNNING);
                producer.getThread().start();
            }, producer.getStartDelay(), TimeUnit.MILLISECONDS);
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

        this.controller.getView().getViewThread().interrupt();
    }
}