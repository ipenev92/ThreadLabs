package org.fbmoll.threadlabs.objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceType {
    final String id;
    int quantity;
    final int maxQuantity;
    final int minQuantity;
    int overflow;
    int underflow;

    public ResourceType(String id, int min, int max, boolean useLimits) {
        this.id = id;
        this.quantity = min;
        this.overflow = 0;
        this.underflow = 0;

        if (useLimits) {
            this.minQuantity = min;
            this.maxQuantity = max;
        } else {
            this.minQuantity = Integer.MIN_VALUE;
            this.maxQuantity = Integer.MAX_VALUE;
        }
    }

    public void addResource() {
        if (quantity < maxQuantity) {
            this.quantity++;
        } else {
            overflow++;
        }
    }

    public void removeResource() {
        if (quantity > minQuantity) {
            this.quantity--;
        } else {
            underflow++;
        }
    }
}