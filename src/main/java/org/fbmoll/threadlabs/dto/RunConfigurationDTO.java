package org.fbmoll.threadlabs.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class RunConfigurationDTO {
    private final int startDelayMin;
    private final int startDelayMax;
    private final int lifeCycleMin;
    private final int lifeCycleMax;
    private final boolean useSynchronized;
    private final boolean useLimits;
    private final boolean useLifeCycle;
}