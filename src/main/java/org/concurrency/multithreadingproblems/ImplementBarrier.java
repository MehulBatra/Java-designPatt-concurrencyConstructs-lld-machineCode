package org.concurrency.multithreadingproblems;

public class ImplementBarrier {

    public static void main(String[] args) throws InterruptedException {

        Barrier.runTest();
    }
}

class Barrier {
    int released=0;
    int count=0;
    int totalThreads;

    public Barrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }


    public static void runTest() throws InterruptedException {
        final Barrier barrier = new Barrier(3);

        Thread p1 = new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("Thread 1");
                    barrier.await();
                    System.out.println("Thread 1");
                    barrier.await();
                    System.out.println("Thread 1");
                    barrier.await();
                } catch (InterruptedException ie) {
                }
            }
        });

        Thread p2 = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                    System.out.println("Thread 2");
                    barrier.await();
                    Thread.sleep(500);
                    System.out.println("Thread 2");
                    barrier.await();
                    Thread.sleep(500);
                    System.out.println("Thread 2");
                    barrier.await();
                } catch (InterruptedException ie) {
                }
            }
        });

        Thread p3 = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1500);
                    System.out.println("Thread 3");
                    barrier.await();
                    Thread.sleep(1500);
                    System.out.println("Thread 3");
                    barrier.await();
                    Thread.sleep(1500);
                    System.out.println("Thread 3");
                    barrier.await();
                } catch (InterruptedException ie) {
                }
            }
        });

        p1.start();
        p2.start();
        p3.start();

        p1.join();
        p2.join();
        p3.join();
    }


//    await() Method
//    This method is called by threads to wait at the barrier. It uses synchronization and condition waiting to manage threads at the barrier. Let's examine it line by line:
//
//    public synchronized void await() throws InterruptedException: The method is synchronized to ensure that only one thread can execute it at a time.
//
//            while (count == totalThreads) wait();: If all threads have already reached the barrier (i.e., count is equal to totalThreads), then the thread waits. This handles the case where threads might reach the barrier after it has already been reset. It ensures that no new threads proceed until the previous batch of threads has been completely released.
//
//    count++;: Increment the count of threads that have arrived at the barrier.
//
//if (count == totalThreads): Check if the number of threads that have arrived at the barrier equals the total number of threads.
//
//    notifyAll();: If the count matches totalThreads, it means all threads have arrived, so notify all waiting threads that they can proceed.
//    released = totalThreads;: Set the released counter to the total number of threads, so we know how many threads need to be released before resetting the barrier.
//else { while (count < totalThreads) wait(); }: If not all threads have arrived yet, the current thread waits until the count reaches totalThreads.
//
//            released--;: After being notified and proceeding, decrement the released counter.
//
//            if (released == 0): Check if all threads that were previously at the barrier have been released.
//
//            count = 0;: Reset the count to 0 to allow the next set of threads to use the barrier.
//            notifyAll();: Notify all threads that were waiting in line 14 to proceed, as the barrier is now ready for the next batch of threads.
    public synchronized void await() throws InterruptedException {

        while (count == totalThreads) {
            wait();
        }
        count++;

        if (count==totalThreads) {
        notifyAll();
        released = totalThreads;
        }
        else{
            while(count<totalThreads) {
                wait();
            }
        }
        released--;
        if (released==0) {
            count=0;
            notifyAll();
        }

    }


}
