package org.concurrency;

public class BadSync2 {

    public static void main(String[] args) {

        Object dummy =  new Object();
        Object lock = new Object();

        synchronized (lock){
            lock.notify();

            // Attempting to call notify() on the object
            // in synchronized block of another object
            dummy.notify();
        }
    }



}
