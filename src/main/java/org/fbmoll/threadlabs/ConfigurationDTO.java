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
    final String[] resources = new String[] {
            "Hamburger", "Pizza", "Pasta", "Sushi", "Tacos"
    };
    String[] names = new String[] {
            "Anna", "Liam", "Emma", "Noah", "Mia", "Eve", "Leo", "Ivy", "Zoe", "Max",
            "Ella", "Lucas", "Sophia", "Mason", "Aria", "Jack", "Luna", "Ethan", "Ava", "James",
            "Elena", "Oliver", "Isla", "Henry", "Chloe", "Oscar", "Grace", "Thea", "Finn", "Ellie",
            "Hugo", "Lily", "Jacob", "Nora", "Caleb", "Ruby", "Leo", "Hazel", "Eli", "Stella",
            "Jude", "Scarlett", "Owen", "Violet", "Ryan", "Layla", "Zane", "Clara", "Reid", "Aurora"
    };
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

    final ArrayList<Consumer> consumers;
    final ArrayList<Producer> producers;
    final ArrayList<ResourceType> resourceTypes;

    public ConfigurationDTO(Model model, int resourceTypesCount, int resourcesMin, int resourcesMax, int consumerCount,
                            int producerCount, int startDelayMin, int startDelayMax, int consumerDelayMin,
                            int consumerDelayMax, int producerDelayMin, int producerDelayMax) {
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
                0);
    }

    private void createResources() {
        for (int i = 0; i < this.resourceTypesCount; i++) {
            String name = this.resources[generateNumber(this.resources.length)];
            this.resourceTypes.add(new ResourceType(name, this.resourcesMin, this.resourcesMax));
        }
    }

    private void createConsumers() {
        for (int i = 0; i < this.consumerCount; i++) {
            int number = generateNumber(this.names.length);
            String name = this.names[number];
            ResourceType resource = this.resourceTypes.get(generateNumber(resourceTypes.size()));
            this.names = removeName(number);
            this.consumers.add(new Consumer(this.model, name, resource));
        }
    }

    private void createProducers() {
        for (int i = 0; i < this.producerCount; i++) {
            int number = generateNumber(this.names.length);
            String name = this.names[number];
            ResourceType resource = this.resourceTypes.get(generateNumber(resourceTypes.size()));
            this.names = removeName(number);
            this.producers.add(new Producer(this.model, name, resource));
        }
    }

    private int generateNumber(int listSize) {
        Random random = new Random();
        return random.nextInt(listSize);
    }

    private String[] removeName(int indexToRemove) {
        if (indexToRemove < 0 || indexToRemove >= this.names.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        String[] result = new String[this.names.length - 1];
        int currentIndex = 0;

        for (int i = 0; i < this.names.length; i++) {
            if (i != indexToRemove) {
                result[currentIndex++] = this.names[i];
            }
        }

        return result;
    }
}