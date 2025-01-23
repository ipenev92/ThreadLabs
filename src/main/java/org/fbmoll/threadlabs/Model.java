package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Model {
    Consumer consumer;
    Producer producer;
    Resources resources;

    Thread consumerThread;
    Thread producerThread;

    public Model() {
        resources = new Resources();
        producer = new Producer(this, "Producer");
        consumer = new Consumer(this, "Consumer");
    }

    public void play() {
        if (consumerThread == null && producerThread == null) {
            consumerThread = new Thread(consumer);
            producerThread = new Thread(producer);

            consumerThread.start();
            producerThread.start();

            consumer.setState(State.RUNNING);
            producer.setState(State.RUNNING);
        }
    }

    public void stop() {
        if (producerThread != null && consumerThread != null) {
            producerThread.interrupt();
            consumerThread.interrupt();

            producer.setState(State.STOPPED);
            consumer.setState(State.STOPPED);
        }

        producerThread = null;
        consumerThread = null;
    }
}