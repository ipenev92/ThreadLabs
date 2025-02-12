package org.fbmoll.threadlabs.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ProducerDTO {
    private final int producerCount;
    private final int producerDelayMin;
    private final int producerDelayMax;
}