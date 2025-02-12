package org.fbmoll.threadlabs.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ResourceTypeDTO {
    private final int resourceTypesCount;
    private final int resourcesMin;
    private final int resourcesMax;
}