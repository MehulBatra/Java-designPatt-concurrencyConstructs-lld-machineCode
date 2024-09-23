package org.concurrency;

public class BadSync1 {

    public static void main(String[] args) throws InterruptedException {

        Object dummy = new Object();
        // Attempting to call wait() on the object
        // outside of a synchronized block.
        dummy.wait();
    }
}
