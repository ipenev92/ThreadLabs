package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Random;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationDTO {
    final Model model;
    final int resourceTypesCount;
    final int resourcesMin;
    final int resourcesMax;
    final int consumerCount;
    final int producerCount;
    final int startDelayMin;
    final int startDelayMax;
    final int consumerDelayMin;
    final int consumerDelayMax;
    final int producerDelayMin;
    final int producerDelayMax;
    final boolean useSynchronized;
    final boolean useLimits;

    int startDelay = 0;
    int consumerDelay = 0;
    int producerDelay = 0;

    final ArrayList<Consumer> consumers;
    final ArrayList<Producer> producers;
    final ArrayList<ResourceType> resourceTypes;

    final Random random;

    public ConfigurationDTO(Model model, int resourceTypesCount, int resourcesMin, int resourcesMax, int consumerCount,
                            int producerCount, int startDelayMin, int startDelayMax, int consumerDelayMin,
                            int consumerDelayMax, int producerDelayMin, int producerDelayMax, boolean useSynchronized,
                            boolean useLimits) {
        this.model = model;
        this.resourceTypesCount = resourceTypesCount;
        this.resourcesMin = resourcesMin;
        this.resourcesMax = resourcesMax;
        this.consumerCount = consumerCount;
        this.producerCount = producerCount;
        this.startDelayMin = startDelayMin;
        this.startDelayMax = startDelayMax;
        this.consumerDelayMin = consumerDelayMin;
        this.consumerDelayMax = consumerDelayMax;
        this.producerDelayMin = producerDelayMin;
        this.producerDelayMax = producerDelayMax;
        this.useSynchronized = useSynchronized;
        this.useLimits = useLimits;

        this.random = new Random();

        this.consumers = new ArrayList<>();
        this.producers = new ArrayList<>();
        this.resourceTypes = new ArrayList<>();

        this.createResources();
        this.createConsumers();
        this.createProducers();
    }

    public static ConfigurationDTO empty() {
        return new ConfigurationDTO(null, 0,0,0,0,
                0,0,0,0,0,0,
                0, true, true);
    }

    private void createResources() {
        for (int i = 1; i <= this.resourceTypesCount; i++) {
            this.resourceTypes.add(new ResourceType("R" + i, this.resourcesMax, this.resourcesMin,
                    this.useLimits));
        }
    }

    private void createConsumers() {
        for (int i = 1; i <= this.consumerCount; i++) {
            ResourceType resource = this.resourceTypes.get(generateNumber(resourceTypes.size()));
            this.consumers.add(new Consumer(this.model, "C" + i, resource,
                    generateRandom(this.startDelayMin, this.startDelayMax),
                    generateRandom(this.consumerDelayMin, this.consumerDelayMax)));
        }
    }

    private void createProducers() {
        for (int i = 1; i <= this.producerCount; i++) {
            ResourceType resource = this.resourceTypes.get(generateNumber(resourceTypes.size()));
            this.producers.add(new Producer(this.model, "P" + i, resource,
                    generateRandom(this.startDelayMin, this.startDelayMax),
                    generateRandom(this.producerDelayMin, this.producerDelayMax)));
        }
    }

    private int generateNumber(int listSize) {
        return this.random.nextInt(listSize);
    }

    private int generateRandom(int x, int y) {
        return this.random.nextInt(y - x + 1) + x;
    }
}