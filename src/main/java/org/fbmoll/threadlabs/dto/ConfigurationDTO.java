package org.fbmoll.threadlabs.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.objects.Consumer;
import org.fbmoll.threadlabs.objects.Model;
import org.fbmoll.threadlabs.objects.Producer;
import org.fbmoll.threadlabs.objects.ResourceType;

import java.util.ArrayList;
import java.util.Random;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationDTO {
    final Model model;
    final ConsumerDTO consumerDTO;
    final ProducerDTO producerDTO;
    final ResourceTypeDTO resourceTypeDTO;
    final boolean useSynchronized;
    final boolean useLimits;

    final ArrayList<Consumer> consumers;
    final ArrayList<Producer> producers;
    final ArrayList<ResourceType> resourceTypes;
    final Random random;

    public ConfigurationDTO(Model model, ResourceTypeDTO resourceTypeDTO, ConsumerDTO consumerDTO,
                            ProducerDTO producerDTO, boolean useSynchronized, boolean useLimits) {
        this.model = model;
        this.consumerDTO = consumerDTO;
        this.producerDTO = producerDTO;
        this.resourceTypeDTO = resourceTypeDTO;
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
        return new ConfigurationDTO(null,
                new ResourceTypeDTO(0,0,0, 0,0),
                new ConsumerDTO(0,0,0),
                new ProducerDTO(0,0,0), true, true);
    }

    private void createResources() {
        for (int i = 1; i <= this.resourceTypeDTO.resourceTypesCount(); i++) {
            this.resourceTypes.add(new ResourceType("R" + i, this.resourceTypeDTO.resourcesMin(),
                    this.resourceTypeDTO.resourcesMax(), this.useSynchronized, this.useLimits));
        }
    }

    private void createConsumers() {
        for (int i = 1; i <= this.consumerDTO.consumerCount(); i++) {
            ResourceType resource = this.resourceTypes.get(generateNumber(resourceTypes.size()));
            this.consumers.add(new Consumer(this.model, "C" + i, resource,
                    generateNumber(this.resourceTypeDTO.startDelayMin(), this.resourceTypeDTO.startDelayMax()),
                    generateNumber(this.consumerDTO.consumerDelayMin(), this.consumerDTO.consumerDelayMax())));
        }
    }

    private void createProducers() {
        for (int i = 1; i <= this.producerDTO.producerCount(); i++) {
            ResourceType resource = this.resourceTypes.get(generateNumber(resourceTypes.size()));
            this.producers.add(new Producer(this.model, "P" + i, resource,
                    generateNumber(this.resourceTypeDTO.startDelayMin(), this.resourceTypeDTO.startDelayMax()),
                    generateNumber(this.producerDTO.producerDelayMin(), this.producerDTO.producerDelayMax())));
        }
    }

    private int generateNumber(int listSize) {
        return this.random.nextInt(listSize);
    }

    private int generateNumber(int x, int y) {
        return this.random.nextInt(y - x + 1) + x;
    }
}