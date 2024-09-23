package org.machinecode;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShopMt {

    public static void main(String[] args) throws InterruptedException {

        HashSet<Thread> set = new HashSet<>();
        BarberShop bs = new BarberShop();

        Thread barberThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bs.barbertoCutHair();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        barberThread.start();

        for(int i=0;i<10;i++) {
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        bs.customerWalkin();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            set.add(t);
        }
           for(Thread t : set) {
                    t.start();
           }
        for(Thread t : set) {
            t.join();
        }
        set.clear();
        Thread.sleep(800);

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        bs.customerWalkin();
                    } catch (InterruptedException ie) {

                    }
                }
            });
            set.add(t);
        }
        for (Thread t : set) {
            t.start();
        }

        barberThread.join();
    }
}

class BarberShop {
    final int chairs=3;
    int haircutDone=0;
    int waitingCustomer=0;
    Semaphore waitForCustomerToEnter = new Semaphore(0);
    Semaphore waitForbarbertoGetReady = new Semaphore(0);
    Semaphore waitForbarbertoCutHair = new Semaphore(0);
    Semaphore  waitForCustomerLeaving = new Semaphore(0);
    ReentrantLock lock = new ReentrantLock();



    public void customerWalkin() throws InterruptedException {
        lock.lock();
        if (waitingCustomer == chairs) {
            System.out.println("No more Customers can Enter its full");
            lock.unlock();
            return;
        }
        waitingCustomer++;
        lock.unlock();
        waitForCustomerToEnter.release(); // yeah thread use hoga by barber jo customer ne choda
        waitForbarbertoGetReady.acquire();
        lock.lock();
        waitingCustomer--;
        lock.unlock();
        waitForbarbertoCutHair.acquire();
        waitForCustomerLeaving.release();


    }
    public void barbertoCutHair() throws InterruptedException {
        while(true){
            waitForCustomerToEnter.acquire(); // yeah thread use krega jo customer ne choda
            waitForbarbertoGetReady.release();
            haircutDone++;
            System.out.println("Barber cutting hair..." + haircutDone);
            Thread.sleep(100);
            waitForbarbertoCutHair.release();
            waitForCustomerLeaving.acquire();



    }


    }

}
