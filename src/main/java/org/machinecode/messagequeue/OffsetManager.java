package org.machinecode.messagequeue;

/*
Role & Responsibility:
This interface abstracts the logic for tracking consumer offsets. It defines:
getOffset(String consumerGroup): Retrieves the current offset for a consumer group.
incrementOffset(String consumerGroup): Increments the offset after consuming a message.
resetOffset(String consumerGroup): Resets the offset in a thread-safe way.

OOP & Design Patterns:
Strategy Pattern: By abstracting offset management, different strategies can be implemented and swapped without affecting other parts of the system.
Single Responsibility: Isolates offset tracking from other message queue logic.

 */
public interface OffsetManager {
    int getOffset(String consumerGroup);
    void incrementOffset(String consumerGroup);
    void resetOffset(String consumerGroup);
}
