package org.machinecode.messagequeue;
/*
How They Interlink

Interdependency:

The MessageQueue<T> interface defines the operations, and SimpleMessageQueue<T> provides the concrete behavior by managing Topic<T> instances.
Topic<T> uses the OffsetManager (implemented by DefaultOffsetManager) to handle consumer offsets.
Concurrency: Within Topic<T>, the ReentrantLock and notEmpty Condition ensure that when a producer adds a message, waiting consumers are notified,
and mutual exclusion is maintained during critical operations.

Design Principles in Action:
This separation of concerns, clear delegation, and use of design patterns (Factory, Strategy, and Producer-Consumer)
ensures that each class has a well-defined responsibility, making the system easier to maintain, test, and extend.
 */
public class Main {
    public static void main(final String[] args) throws InterruptedException {
        MessageQueue<String> queue = new StringBasedMessageQueue<>();

        Thread producerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        queue.produce("topic1", String.valueOf(i));
                        System.out.println("Produced " + String.valueOf(i));
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = queue.consume("topic1", "consumer1");
                        System.out.println("Message " + message + " consumed");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        });

        Thread consumerThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = queue.consume("topic1", "consumer2");
                        System.out.println("Message " + message + " consumed by consumer2");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        });

        producerThread.start();
        consumerThread.start();
        consumerThread2.start();
        producerThread.join();
        consumerThread.join();
        consumerThread2.join();

    }
}
