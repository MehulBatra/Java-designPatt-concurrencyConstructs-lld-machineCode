package org.machinecode.ratelimiter;

import java.util.HashSet;
import java.util.Set;
/*
Acts as the client code that uses the RateLimiter interface.
It demonstrates DIP by being independent of concrete implementations

What is Thread.currentThread().interrupt()?
This method sets the "interrupted" flag of the current thread to true,
signaling that the thread should stop execution or handle an interruption.

Why is it Needed?
Threads in Java do not stop automatically when interrupted.
The proper way to stop a thread is by checking the interrupted flag and gracefully handling the interruption.
Thread.currentThread().interrupt() helps in propagating the interruption status without swallowing it.
Always call Thread.currentThread().interrupt() after catching InterruptedException
if you want the thread to properly stop. Thread.currentThread().isInterrupted()
 */
public class Main {
  public static void main(String[] args) throws InterruptedException {
      Set<Thread> threads = new HashSet<>();
      RateLimiter rateLimiter = RateLimiterFactory.createRateLimiter("TOKEN",5,1000);

      for(int i=0;i<10;i++){
          Thread thread = new Thread(()->{
              try{
                  rateLimiter.acquireToken();
              }catch (InterruptedException e){
                  Thread.currentThread().interrupt();
                  System.err.println("Thread interrupted: " + Thread.currentThread().getName());

              }
          },"Thread_"+(i+1));
          threads.add(thread);
      }
      threads.forEach(Thread::start);
      for (Thread thread : threads) {
          thread.join();
      }

      rateLimiter.shutdown();

  }
}
