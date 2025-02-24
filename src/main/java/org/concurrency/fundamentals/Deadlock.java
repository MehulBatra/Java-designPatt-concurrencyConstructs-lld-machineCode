package org.concurrency.fundamentals;

class Deadlock {

    public static void main(String[] args) throws InterruptedException {
        DemoDead demoDead = new DemoDead();
        demoDead.letsgo();

    }


}


class DemoDead{

    int counter = 0;
    private Object lock1 = new Object();
    private Object lock2 = new Object();


    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            for(int i = 0; i < 100; i++) {
                try {
                    incrementCounter();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            for(int i = 0; i < 100; i++) {
                try {
                    decrementCounter();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };


    public void letsgo() throws InterruptedException {
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        Thread.sleep(100);
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("Done : " + counter);
    }

    public void incrementCounter() throws InterruptedException {
        synchronized(lock1) {
            System.out.println("Accquired lock 1");
            Thread.sleep(100);
            synchronized (lock2) {
                System.out.println("Accquired lock 2");
                counter++;
            }
        }
    }

    public void decrementCounter() throws InterruptedException {
        synchronized(lock2) {
            System.out.println("Accquired lock 2");
            Thread.sleep(100);

            synchronized (lock1) {
                System.out.println("Accquired lock 1");
                counter--;
            }
        }
    }


}
