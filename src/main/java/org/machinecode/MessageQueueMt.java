package org.machinecode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueueMt {

    public static void main(String[] args) throws InterruptedException {

        Queue<Integer> queue = new Queue<>(); // think in OOP prespective

        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                for (int i = 0; i < 10; i++) {
                    queue.producer("topic1", i);
                    System.out.println("Message " + i + " sent");
                    Thread.sleep(100);
                }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try {
                        queue.consumer("topic1", "consumer1");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        thread1.start();
        Thread.sleep(100);
        thread2.start();
        thread1.join();
        thread2.join();
    }

}

class Queue <T> {

    ConcurrentHashMap<String, List<T>> topicQueues = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicInteger>> consumerOffsets = new ConcurrentHashMap<>();
    Object lock = new Object();
//    Condition notEmpty = lock.newCondition();


    public void producer(String topic, T data) {
        synchronized (lock) {
            try { // object level locking
                List<T> queue = topicQueues.computeIfAbsent(topic, k -> new LinkedList<>());
                queue.add(data);
                lock.notifyAll();
//            } finally {
//                lock.unlock();
//            }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public T consumer(String topic, String consumerGroup) throws InterruptedException {
        synchronized (lock) { // lock on the basis of the consumer group not on the entire method 
            try {
                consumerOffsets.computeIfAbsent(topic, k -> new ConcurrentHashMap<>());
                ConcurrentHashMap<String, AtomicInteger> offsets = consumerOffsets.get(topic);

                AtomicInteger currentoffset = offsets.computeIfAbsent(consumerGroup, k-> new AtomicInteger(0));
                while (!topicQueues.containsKey(topic) || currentoffset.get() >= topicQueues.get(topic).size()) {
                    lock.wait();
                }
                List<T> queue = topicQueues.get(topic);
                T message = queue.get(currentoffset.get());
                currentoffset.incrementAndGet();
                System.out.println("Message " + message + " consumed");
                return message;

//        }finally {
//            lock.unlock();
//        }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
