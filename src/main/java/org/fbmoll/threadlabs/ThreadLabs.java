package org.fbmoll.threadlabs;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThreadLabs {
    public static void main(String[] args) {
        new Controller();
    }
}