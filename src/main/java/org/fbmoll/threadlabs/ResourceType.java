package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceType {
    final String name;
    int quantity;
    final int maxQuantity;
    final int minQuantity;

    public ResourceType(String name, int max, int min) {
        this.name = name;
        this.maxQuantity = max;
        this.minQuantity = min;
        this.quantity = 0;
    }

    public synchronized void addResource() {
        this.quantity++;
        notifyAll();
    }

    public synchronized void removeResource() {
        this.quantity--;
        notifyAll();
    }
}
