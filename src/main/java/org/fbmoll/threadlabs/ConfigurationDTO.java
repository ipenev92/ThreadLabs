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
    final String[] RESOURCES = new String[] {
            "Hamburger", "Pizza", "Pasta", "Sushi", "Tacos"
    };
    String[] NAMES = new String[] {
            "Anna", "Liam", "Emma", "Noah", "Mia", "Eve", "Leo", "Ivy", "Zoe", "Max",
            "Ella", "Lucas", "Sophia", "Mason", "Aria", "Jack", "Luna", "Ethan", "Ava", "James",
            "Elena", "Oliver", "Isla", "Henry", "Chloe", "Oscar", "Grace", "Thea", "Finn", "Ellie",
            "Hugo", "Lily", "Jacob", "Nora", "Caleb", "Ruby", "Leo", "Hazel", "Eli", "Stella",
            "Jude", "Scarlett", "Owen", "Violet", "Ryan", "Layla", "Zane", "Clara", "Reid", "Aurora"
    };
    final Model model;
    final int consumerCount;
    final int producerCount;
    final int resourceTypesCount;
    final int minResourcesCount;
    final int maxResourcesCount;
    final int minCreationDelay;
    final int maxCreationDelay;
    final boolean randomDelay;
    final int creationDelay;

    final ArrayList<Consumer> consumers;
    final ArrayList<Producer> producers;
    final ArrayList<ResourceType> resourceTypes;

    public ConfigurationDTO(Model model, int consumerCount, int producerCount, int resourceTypesCount,
                            int minResourcesCount, int maxResourcesCount, int minCreationDelay, int maxCreationDelay,
                            boolean randomDelay, int creationDelay) {
        this.model = model;
        this.consumerCount = consumerCount;
        this.producerCount = producerCount;
        this.resourceTypesCount = resourceTypesCount;
        this.minResourcesCount = minResourcesCount;
        this.maxResourcesCount = maxResourcesCount;
        this.minCreationDelay = minCreationDelay;
        this.maxCreationDelay = maxCreationDelay;
        this.randomDelay = randomDelay;
        this.creationDelay = creationDelay;

        this.consumers = new ArrayList<>();
        this.producers = new ArrayList<>();
        this.resourceTypes = new ArrayList<>();

        this.createResources();
        this.createConsumers();
        this.createProducers();
    }

    private void createResources() {
        for (int i = 0; i < this.resourceTypesCount; i++) {
            String name = this.RESOURCES[generateNumber(this.RESOURCES.length)];
            this.resourceTypes.add(new ResourceType(name, this.minResourcesCount, this.maxResourcesCount));
        }
    }

    private void createConsumers() {
        for (int i = 0; i < this.consumerCount; i++) {
            int number = generateNumber(this.NAMES.length);
            String name = this.NAMES[number];
            ResourceType resource = this.resourceTypes.get(generateNumber(resourceTypes.size()));
            this.NAMES = removeName(number);
            this.consumers.add(new Consumer(this.model, name, resource));
        }
    }

    private void createProducers() {
        for (int i = 0; i < this.producerCount; i++) {
            int number = generateNumber(this.NAMES.length);
            String name = this.NAMES[number];
            ResourceType resource = this.resourceTypes.get(generateNumber(resourceTypes.size()));
            this.NAMES = removeName(number);
            this.producers.add(new Producer(this.model, name, resource));
        }
    }

    private int generateNumber(int listSize) {
        Random random = new Random();
        return random.nextInt(listSize);
    }

    private String[] removeName(int indexToRemove) {
        if (indexToRemove < 0 || indexToRemove >= this.NAMES.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        String[] result = new String[this.NAMES.length - 1];
        int currentIndex = 0;

        for (int i = 0; i < this.NAMES.length; i++) {
            if (i != indexToRemove) {
                result[currentIndex++] = this.NAMES[i];
            }
        }

        return result;
    }
}