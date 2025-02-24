package org.machinecode.messagequeue;

import java.util.concurrent.ConcurrentHashMap;

public class StringBasedMessageQueue<T> implements MessageQueue<T> {

    private final ConcurrentHashMap<String, Topic<T>> topics;

    public StringBasedMessageQueue() {
        topics = new ConcurrentHashMap<>();
    }

    private Topic<T> getOrCreateTopic(final String topicName) {
        return topics.computeIfAbsent(topicName, name -> new Topic<>(name));

    }


    @Override
    public void produce(String topic, T message) {
        Topic<T> t = getOrCreateTopic(topic);
        t.addMessage(message);
    }

    @Override
    public T consume(String topic, String consumerGroup) throws InterruptedException {
        Topic<T> t = getOrCreateTopic(topic);
        return t.getMessage(consumerGroup);
    }
}
