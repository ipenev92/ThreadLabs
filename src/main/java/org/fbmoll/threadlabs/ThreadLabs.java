package org.fbmoll.threadlabs;

public class ThreadLabs {
    public static void main(String[] args) {
        new Controller();

        // thread in view that updates every 1000ms
        // right separated in 3 vertically: un Jtable por cada consumer, producer, product
        // left separated in 4: config table, total stats, play and stop button
        // DTO instanciado en cada lado pasando info a cada cosa
        // while method in increment and decrement; when consumer can't consume, sleep; reverse with producer and max
        // multi recursos, producers y consumers
        // cada producer y consumer tiene un recurso asociado
        // instanciando el producer/consumer, pasamos el recurso

        // num consumers/producers/resources
        // delay random entre x / y para la creacion de cada objeto
        // wait() until it can produce

        // tiempo de consumir y producir
    }
}