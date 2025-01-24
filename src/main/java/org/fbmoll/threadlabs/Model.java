package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Model {
    ConfigurationPropertiesDTO properties;
    Controller controller;
    Consumer consumer;
    Producer producer;
    Thread consumerThread;
    Thread producerThread;
    Resources resources;

    public Model(Controller controller) {
        this.controller = controller;
        this.resources = new Resources(0, 100, 100);
        this.consumer = new Consumer(this, resources, "Consumer");
        this.producer = new Producer(this, resources, "Producer");

        this.properties = new ConfigurationPropertiesDTO(consumer, producer, resources);
    }

    public void play() {
        if (this.consumer.getState() != State.RUNNING && this.producer.getState() != State.RUNNING) {
            this.consumerThread = new Thread(this.consumer);
            this.producerThread = new Thread(this.producer);

            this.consumer.setState(State.RUNNING);
            this.producer.setState(State.RUNNING);

            this.consumerThread.start();
            this.producerThread.start();
        }
    }

    public void stop() {
        if (this.consumer.getState() == State.RUNNING && this.producer.getState() == State.RUNNING) {
            this.consumer.setState(State.STOPPED);
            this.producer.setState(State.STOPPED);

            this.consumerThread.interrupt();
            this.producerThread.interrupt();
        }
    }
}