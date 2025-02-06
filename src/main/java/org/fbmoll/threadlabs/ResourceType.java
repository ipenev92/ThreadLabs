package org.fbmoll.threadlabs;

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

    public ResourceType(String id, int max, int min, boolean useLimits) {
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

    public synchronized void addResource() {
        if (quantity < maxQuantity) {
            this.quantity++;
            System.out.println("Resource added: " + id + " - Current quantity: " + this.quantity);
            notifyAll(); // ✅ Notify all waiting consumers and producers
        } else {
            overflow++; // 🔥 Overflow detected
            System.out.println("⚠ OVERFLOW: " + id + " has exceeded max capacity! Overflow count: " + overflow);
        }
    }

    public synchronized void removeResource() {
        if (quantity > minQuantity) {
            this.quantity--;
            System.out.println("Resource consumed: " + id + " - Current quantity: " + this.quantity);
            notifyAll(); // ✅ Notify all waiting producers
        } else {
            underflow++; // 🔥 Underflow detected
            System.out.println("⚠ UNDERFLOW: " + id + " has dropped below minimum capacity! Underflow count: " + underflow);
        }
    }
}