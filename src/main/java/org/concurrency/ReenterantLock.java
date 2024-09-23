package org.concurrency;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ReenterantLock {

    private static volatile boolean isSignalled = false;   // A shared flag to check the condition

    public static void example() throws InterruptedException {

        final ReentrantLock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();


        Thread signaller = new Thread(new Runnable() {

            public void run() {
                lock.lock();
                try {
                    isSignalled = true;  // Set the flag to indicate the signal has been sent
                    condition.signal();  // Signal any waiting threads
                    System.out.println("Sent signal");
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread waiter = new Thread(new Runnable() {

            public void run() {
                lock.lock();
                try {
                    while (!isSignalled) { // if we don't use while loop then our waiter thread would always be stuck
                        // in wait condition because signaller thread started and sent signal but we need condition
                        // variable to check that
                        condition.await();  // Wait until the signal is received
                    }
                    System.out.println("Received signal");
                } catch (InterruptedException ie) {
                    // handle interruption
                } finally {
                    lock.unlock();
                }
            }
        });

        // Start the signaller first
        signaller.start();
        signaller.join();  // Ensure the signaller finishes before starting the waiter

        // Now start the waiter after the signaller has finished
        waiter.start();
        waiter.join();  // Wait for the waiter to finish

        System.out.println("Program Exiting.");
    }
}
class Demonstration {

    public static void main(String args[]) throws InterruptedException {
        ReenterantLock.example();
    }
}
