package org.machinecode;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NonBlockingStackMt {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {


        NonBlockStackOperations<Integer> stack = new NonBlockStackOperations<Integer>();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        int numThread = 2;
        CyclicBarrier barrier = new CyclicBarrier(numThread);
        long start = System.currentTimeMillis();
        Integer testValue = new Integer(51);
        try {
            for (int i = 0; i < numThread; i++) {
            executor.submit(new Runnable() {
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        stack.push(testValue);
                    }

                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException ex) {
                        System.out.println("ignoring exception");
                        //ignore both exceptions
                    }
                    for (int i = 0; i < 10000; i++) {
                        stack.pop();
                    }
                }
            });
        }
    } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
        }

        System.out.println("Number of elements in the stack = " + stack.size());
    }
    }

class StackNode <T>{

    private T data;
    private StackNode<T> next;


    public StackNode(T data) {
        this.data = data;
    }

    public StackNode<T> getNext() {
        return next;
    }

    public void setNext(StackNode<T> stackNode) {
        next = stackNode;
    }

    public T getData() {
        return this.data;
    }
}


class NonBlockStackOperations<T>{

    private AtomicInteger count = new AtomicInteger(0);
    private AtomicReference<StackNode<T>> top = new AtomicReference<>();


    public void push(T data) {
        StackNode<T> oldTop;
        StackNode<T> newTop;
        do {
            oldTop = top.get();
            newTop = new StackNode<>(data);
            newTop.setNext(oldTop);
        } while (!top.compareAndSet(oldTop, newTop));

        count.incrementAndGet();
    }


    public T pop() {
            StackNode<T> oldTop;
            StackNode<T> newTop;
            oldTop = top.get();
            if(oldTop == null) return null;
            newTop = oldTop.getNext();
            while(!top.compareAndSet(oldTop, newTop));
            count.decrementAndGet();
            return oldTop.getData();
    }

    public int size() {
        return count.get();
    }
}
