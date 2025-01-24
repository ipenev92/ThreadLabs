package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Resources {
    int quantity;
    final int maxQuantity;
    final int minQuantity;

    public Resources(int max, int min, int quantity) {
        this.maxQuantity = max;
        this.minQuantity = min;
        this.quantity = quantity;
    }

    public void addResource() {
        this.quantity++;
        System.out.println(this.quantity);
    }

    public void removeResource() {
        this.quantity--;
        System.out.println(this.quantity);
    }
}
