package org.machinecode;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefferedCallbackMt {

    public static void main(String[] args) throws InterruptedException {

        DefferedCall.runService();
    }
}

class DefferedCall{

    PriorityQueue<Callback> q = new PriorityQueue<>(new Comparator<Callback>() {
        public int compare(Callback o1, Callback o2) {
            return (int) (o1.execuateAtTime - o2.execuateAtTime);
        }
    });

    Lock lock = new ReentrantLock();
    Condition defferedCallbackCondition = lock.newCondition();

    public long timeLeft(){

        return q.peek().execuateAtTime - System.currentTimeMillis();
    }

    public void start() throws InterruptedException {

        long timeL = 0;
while(true) {
    lock.lock();
    while (q.size() == 0) {
        defferedCallbackCondition.await();
    }

    while (q.size() != 0) {
        timeL = timeLeft();
        if (timeL <= 0) {
            break;

        }
        defferedCallbackCondition.await(timeL, TimeUnit.MILLISECONDS);

    }

    Callback cb = q.poll();
    System.out.println(
            "Executed at " + System.currentTimeMillis() / 1000 + " required at " + cb.execuateAtTime / 1000
                    + ": message:" + cb.message);


    lock.unlock();


}
    }


    public void  registerCallback(Callback cb){

        lock.lock();
        q.add(cb);
        defferedCallbackCondition.signal();
        lock.unlock();

    }



    public static void runService() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        DefferedCall defferedCall = new DefferedCall();
        Set<Thread> allThreads = new HashSet<>();

        executorService.execute(() -> {
            try {
                defferedCall.start();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        });

        for (int i = 0; i < 10; i++) {
            final int threadNumber = i + 1;
            // Submit tasks to ExecutorService instead of creating a new Thread
            executorService.submit(() -> {
                Callback cb = new Callback(1, "Hello, this is Thread_" + threadNumber);
                defferedCall.registerCallback(cb);
            });
            Thread.sleep(1000); // Simulate delay between task submissions
        }

        // Shut down the ExecutorService gracefully
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }
}




class Callback{

    long execuateAtTime;
    String message;


    public Callback(long execuateAtTime, String message){
        this.execuateAtTime = execuateAtTime;
        this.message = message;

    }


}
