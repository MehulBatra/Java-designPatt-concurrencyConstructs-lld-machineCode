package org.concurrency.multithreadingproblems;

public class ImplementSemaphore {

    public static void main(String[] args) throws InterruptedException {

        final SemaphoreF semaphoreF = new SemaphoreF(1);

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        semaphoreF.acquire();
                        System.out.println(Thread.currentThread().getName() + " acquired");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        semaphoreF.release();
                        System.out.println(Thread.currentThread().getName() + " released");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        thread2.start();
        thread1.start();
        thread2.join();
        thread1.join();
    }
}

class SemaphoreF{
    int capacity;
    int userPermit=0;

    public SemaphoreF(int capacity) {
        this.capacity = capacity;
    }


    public synchronized void acquire() throws InterruptedException {

        while(userPermit==capacity){
            wait();
        }

        userPermit++;
        notify();

    }




    public synchronized void release() throws InterruptedException {

        while(userPermit==0){
            wait();
        }
        userPermit--;
        notify();
    }
}
