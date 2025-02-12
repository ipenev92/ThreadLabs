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
        if (this.useSynchronized) {
            synchronized (this.lock) {
                while (this.quantity == this.maxQuantity) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                this.quantity++;
                this.lock.notifyAll();
            }
        } else {
            if (this.quantity < this.maxQuantity) {
                this.quantity++;
            } else {
                this.overflow++;
            }
        }
    }

    public void removeResource() {
        if (this.useSynchronized) {
            synchronized (this.lock) {
                while (this.quantity == this.minQuantity) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                this.quantity--;
                this.lock.notifyAll();
            }
        } else {
            if (this.quantity > this.minQuantity) {
                this.quantity--;
            } else {
                this.underflow++;
            }
        }
    }
}