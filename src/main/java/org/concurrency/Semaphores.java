package org.concurrency;

import java.util.concurrent.Semaphore;

class Semaphores {

     public static void main(String[] args) throws InterruptedException {
         DemoSemaphore.runExample();
     }

}

class DemoSemaphore {



     public static void runExample() throws InterruptedException {

         Semaphore semaphore = new Semaphore(0);
         Thread badThread = new Thread(new Runnable(){

             @Override
             public void run() {
                 while(true) {
                     try {
                         semaphore.acquire();
                     }
                     catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                     finally {
                         System.out.println("Bad thread releasing semahore.");
                         semaphore.release();
                     }
                 }
             }
         });

         badThread.start();
         Thread.sleep(1000);

         Thread goodThread = new Thread(new Runnable(){

             @Override
             public void run() {
                 System.out.println("Good thread patiently waiting to be signalled.");
                 try {
                     semaphore.acquire();
                 } catch (InterruptedException e) {
                     throw new RuntimeException(e);
                 }
             }
         });

         goodThread.start();
         badThread.join();
         goodThread.join();
         System.out.println("Exiting Program");

     }
}

