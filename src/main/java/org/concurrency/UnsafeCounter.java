package org.concurrency;

import java.util.Random;

class UnsafeCounter {
    
    static Random random = new Random(System.currentTimeMillis());
    BadCounter badCounter = new BadCounter();

    public void increase (){
        for(int i=0;i<100;i++){
         badCounter.increment();
         UnsafeCounter.sleepThread();
        }
    }

    public void decrease (){
        for(int i=0;i<100;i++){
            badCounter.decrement();
            UnsafeCounter.sleepThread();
        }
    }

    public static void sleepThread(){
        try{
            Thread.sleep(random.nextInt(10));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public  void letsGo() throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                increase();
            }
        });

                Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                decrease();
            }
        });

    thread1.start();

    thread2.start();


    thread1.join();

    thread2.join();

    System.out.println(badCounter.counter);
}


    public static void main(String[] args) throws InterruptedException {

        UnsafeCounter unsafeCounter = new UnsafeCounter();
        unsafeCounter.letsGo();
    }
}



class BadCounter{

    int counter = 0;

    public void increment(){
        counter++;
    }

    public void decrement(){
        counter--;
    }
}


