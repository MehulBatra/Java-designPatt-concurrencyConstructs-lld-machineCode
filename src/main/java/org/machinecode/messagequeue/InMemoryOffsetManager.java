package org.machinecode.messagequeue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryOffsetManager implements OffsetManager {

    private final ConcurrentHashMap<String, AtomicInteger> consumerOffsets = new ConcurrentHashMap<>();


    @Override
    public int getOffset(String consumerGroup) {
        return consumerOffsets.computeIfAbsent(consumerGroup, k -> new AtomicInteger(0)).get();

    }

    @Override
    public void incrementOffset(String consumerGroup) {
        consumerOffsets.putIfAbsent(consumerGroup,new AtomicInteger(0)).incrementAndGet();
    }

    @Override
    public void resetOffset(String consumerGroup) {
        consumerOffsets.putIfAbsent(consumerGroup,new AtomicInteger(0));
    }
}
