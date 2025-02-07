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
    final Object lock = new Object();
    final boolean useSynchronized;

    public ResourceType(String id, int min, int max, boolean useSynchronized, boolean useLimits) {
        this.id = id;
        this.quantity = min;
        this.overflow = 0;
        this.underflow = 0;
        this.useSynchronized = useSynchronized;

        if (useLimits) {
            this.minQuantity = min;
            this.maxQuantity = max;
        } else {
            this.minQuantity = Integer.MIN_VALUE;
            this.maxQuantity = Integer.MAX_VALUE;
        }
    }
    public void addResource() {
        if (useSynchronized) {
            synchronized (lock) {
                if (quantity < maxQuantity) {
                    this.quantity++;
                    lock.notifyAll();
                } else {
                    overflow++;
                }
            }
        } else {
            if (quantity < maxQuantity) {
                this.quantity++;
            } else {
                overflow++;
            }
        }
    }

    public void removeResource() {
        if (useSynchronized) {
            synchronized (lock) {
                if (quantity > minQuantity) {
                    this.quantity--;
                    lock.notifyAll();
                } else {
                    underflow++;
                }
            }
        } else {
            if (quantity > minQuantity) {
                this.quantity--;
            } else {
                underflow++;
            }
        }
    }
}