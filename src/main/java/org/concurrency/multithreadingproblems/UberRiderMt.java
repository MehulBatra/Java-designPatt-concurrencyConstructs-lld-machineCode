package org.concurrency.multithreadingproblems;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class UberRiderMt {
    public static void main(String[] args) throws InterruptedException {

        TestSetup.runTest();

    }
}

class UberRider {
    int democrats=0;
    int repulicans=0;

    Semaphore democratsSemaphore = new Semaphore(1);
    Semaphore reuplicansSemaphore = new Semaphore(1);
    ReentrantLock lock = new ReentrantLock();
    CyclicBarrier cyclicBarrier = new CyclicBarrier(4);


    public void seated(){
        System.out.println(Thread.currentThread().getName() + "  seated");
        System.out.flush();
    }

    public void drive(){
        System.out.println("Uber Ride on Its wayyyy... with ride leader " + Thread.currentThread().getName());
        System.out.flush();
    }

    public void seatDemocrats() throws InterruptedException, BrokenBarrierException {
        boolean rideLeader = false;
        lock.lock();
        democrats++;
        if(democrats==4){
            democratsSemaphore.release(3);
            democrats-=4;
            rideLeader = true;
        }
        else if(democrats==2 && repulicans>=2){
            democratsSemaphore.release(1);
            reuplicansSemaphore.release(2);
            rideLeader = true;
            democrats-=2;
            repulicans-=2;

        }else{
            lock.unlock();
            democratsSemaphore.acquire();
        }
        seated();
        cyclicBarrier.await();
        if(rideLeader){
            drive();
            lock.unlock();
        }
    }
    public void seatRepulicans() throws InterruptedException, BrokenBarrierException {
        boolean rideLeader = false;
        lock.lock();
        repulicans++;
        if(repulicans==4){
            reuplicansSemaphore.release(3);
            rideLeader = true;
            repulicans-=4;
        }
        else if(repulicans==2 && democrats>=2){
            reuplicansSemaphore.release(1);
            democratsSemaphore.release(2);
            rideLeader = true;
            democrats-=2;
            repulicans-=2;

        }else{
            lock.unlock();
            reuplicansSemaphore.acquire();
        }
        seated();
        cyclicBarrier.await();
        if(rideLeader){
            drive();
            lock.unlock();
        }
    }


}

class TestSetup{

    public static void runTest() throws InterruptedException {

        final UberRider uberSeatingProblem = new UberRider();
        Set<Thread> allThreads = new HashSet<Thread>();

        for (int i = 0; i < 10; i++) {

            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        uberSeatingProblem.seatDemocrats();
                    } catch (InterruptedException ie) {
                        System.out.println("We have a problem");

                    } catch (BrokenBarrierException bbe) {
                        System.out.println("We have a problem");
                    }

                }
            });
            thread.setName("Democrat_" + (i + 1));
            allThreads.add(thread);

            Thread.sleep(50);
        }

        for (int i = 0; i < 14; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        uberSeatingProblem.seatRepulicans();
                    } catch (InterruptedException ie) {
                        System.out.println("We have a problem");

                    } catch (BrokenBarrierException bbe) {
                        System.out.println("We have a problem");
                    }
                }
            });
            thread.setName("Republican_" + (i + 1));
            allThreads.add(thread);
            Thread.sleep(20);
        }

        for (Thread t : allThreads) {
            t.start();
        }

        for (Thread t : allThreads) {
            t.join();
        }
    }
}
