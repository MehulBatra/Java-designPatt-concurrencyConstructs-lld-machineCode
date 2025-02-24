package org.concurrency.fundamentals;

class NonReentrantLock {

    public static void main(String[] args) throws InterruptedException {
        NonReentrantLockDemo nre = new NonReentrantLockDemo();

        nre.lock();
        System.out.println("acquired lock for 1st time");
        nre.lock();
        System.out.println("acquired lock for 2nd time");

    }

}


class NonReentrantLockDemo{

    boolean isLocked = false;


    synchronized public void lock() throws InterruptedException {

        while(isLocked){
            wait();
        }
        isLocked = true;
    }

    synchronized public void unlock() {

        isLocked = false;
        notify();
    }


}
