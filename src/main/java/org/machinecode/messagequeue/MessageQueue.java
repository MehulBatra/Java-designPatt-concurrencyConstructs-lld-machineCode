package org.machinecode.messagequeue;

/*
Role & Responsibility:
This is the primary contract for our messaging system. It declares two essential methods:
produce(String topic, T message): Adds a message to a specified topic.
consume(String topic, String consumerGroup): Retrieves a message for a specific consumer group.

OOP & Design Patterns:
Abstraction: Clients depend on this high-level interface without knowing about the underlying implementation.
Interface Segregation: By defining only the necessary operations, clients are not forced to implement unused methods.

 */
public interface MessageQueue<T> {
    void produce(String topic, T message);

    T consume(String topic, String consumerGroup) throws InterruptedException;
}
