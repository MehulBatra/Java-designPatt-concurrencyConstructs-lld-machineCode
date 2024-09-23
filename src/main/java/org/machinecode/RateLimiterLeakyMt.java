package org.machinecode;

import java.util.HashSet;
import java.util.Set;

public class RateLimiterLeakyMt {
    public static void main(String[] args) throws InterruptedException {
            Set<Thread> allThreads = new HashSet<Thread>();
            TokenBucket tokenBucket = TokenBucketFactory.getInstance(1);


            for (int i = 0; i < 10; i++) {
                Thread thread = new Thread(new Runnable(

                ) {
                    @Override
                    public void run() {
                        try {
                            tokenBucket.getToken();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                thread.setName("Thread_" + (i + 1));
                allThreads.add(thread);
            }

        for (Thread t : allThreads) {
            t.start();
        }

        for (Thread t : allThreads) {
            t.join();
        }
    }
}


// Superclass
abstract class TokenBucket{

    public void getToken() throws InterruptedException {}
}


//The factory class is a design pattern used to create objects. Instead of directly instantiating objects, clients
// use the factory class to get instances.
final class TokenBucketFactory{

    private TokenBucketFactory(){

    }
    //Purpose: It provides a static method makeTokenBucketFilter() to create and initialize instances of
    // MultithreadedTokenBucketFilter.
    public static TokenBucket getInstance(int capacity){
            MultiThreadedTokenBucket mt = new MultiThreadedTokenBucket(capacity);
            mt.instialize();
            return mt;


    }

// subclass
    private static class MultiThreadedTokenBucket extends TokenBucket{
        final int maxTokens;
        int consumableTokens;
        final int refillTime=1000;

        public MultiThreadedTokenBucket(int maxTokens){
            this.maxTokens=maxTokens;

        }

        public void instialize(){
            Thread dt = new Thread(() -> {
                daemonThread();
            });
            dt.setDaemon(true);
            dt.start();
        }

        public void daemonThread(){
            while(true) {
                synchronized (this) {
                    if (consumableTokens < maxTokens) {
                        consumableTokens++;
                    }
                    this.notify();
                    try{
                        Thread.sleep(refillTime);
                    } catch (Exception e) {
                        Thread.currentThread().interrupt(); // Preserve interrupt status
                        break;
                    }

                }
            }
        }

        public void getToken() throws InterruptedException {

            synchronized (this) {
                while(consumableTokens==0){
                    this.wait();
                }
                consumableTokens--;
                System.out.println("Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);

            }
        }
    }
}


