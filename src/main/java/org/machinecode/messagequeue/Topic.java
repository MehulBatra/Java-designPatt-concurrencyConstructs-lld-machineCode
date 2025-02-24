package org.machinecode.messagequeue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
Role & Responsibility:
Represents a single topic in the message queue system. It encapsulates:
Message Storage: Maintains a list of messages.
Offset Management: Delegates consumer offset tracking to an instance of OffsetManager (specifically, DefaultOffsetManager).
Concurrency Control: Uses a ReentrantLock to ensure mutual exclusion when adding or retrieving messages and a Condition (notEmpty) to signal waiting consumers.

Key Operations:
addMessage(T message): Locks the topic, adds the message, and signals consumers that a new message is available.
getMessage(String consumerGroup): Waits (using the condition) until there’s a new message for the consumer, retrieves it, and updates the consumer's offset.

OOP & Design Patterns:
Encapsulation: All details related to a topic (message storage, offsets, and synchronization) are contained within this class.
Producer-Consumer Pattern: Implements a classic pattern using locks and conditions to manage concurrent access.

 */
public class Topic<T> {
    private final String name;
    private final List<T> messages;
    private final OffsetManager offsetManager;
    private final ReentrantLock lock;
    private final Condition notEmpty;

    public Topic(String name) {
        this.name = name;
        messages = new LinkedList<>();
        this.offsetManager = new InMemoryOffsetManager();
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
    }

    public void addMessage(T message) {
        lock.lock();
        try{
            messages.add(message);
            notEmpty.signalAll();
        }
        finally{
            lock.unlock();
        }
    }

    public T getMessage(String consumerGroup) throws InterruptedException {
        lock.lock();
        try{
            int offset = offsetManager.getOffset(consumerGroup);
            while(offset >= messages.size()){
                notEmpty.await();
                offset = offsetManager.getOffset(consumerGroup);
            }
            T message = messages.get(offset);
            offsetManager.incrementOffset(consumerGroup);
            return message;
        }
        finally{
            lock.unlock();
        }
    }

}
