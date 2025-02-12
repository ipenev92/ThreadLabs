package org.fbmoll.threadlabs.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsumerDTO {
    private final int consumerCount;
    private final int consumerDelayMin;
    private final int consumerDelayMax;
}