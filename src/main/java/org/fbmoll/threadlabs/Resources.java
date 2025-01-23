package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Resources {
    int quantity;
    int maxQuantity;
    int minQuantity;

    public Resources() {
        quantity = 0;
    }

    public void addResource() {
        this.quantity++;
        System.out.println(quantity);
    }

    public void removeResource() {
        this.quantity--;
        System.out.println(quantity);
    }
}
