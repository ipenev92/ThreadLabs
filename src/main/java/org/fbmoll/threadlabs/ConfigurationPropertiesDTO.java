package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationPropertiesDTO {
    Consumer consumer;
    Producer producer;
    Resources resources;
}